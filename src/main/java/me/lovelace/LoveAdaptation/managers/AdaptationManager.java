package me.lovelace.LoveAdaptation.managers;

import me.lovelace.LoveAdaptation.LoveAdaptation;
import me.lovelace.LoveAdaptation.models.AdaptationData;
import me.lovelace.LoveAdaptation.models.AdaptationType;
import me.lovelace.LoveAdaptation.models.PlayerData;
import me.lovelace.LoveAdaptation.textures.HeadTextures;
import me.lovelace.LoveAdaptation.utils.Utils;
import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Monster;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

public class AdaptationManager {

    private final LoveAdaptation plugin;
    private final Map<UUID, PlayerData> playerDataMap = new ConcurrentHashMap<>();
    // Момент последнего полученного урона (любого) - нужен "боевому закалу", чтобы включать
    // пассивную регенерацию только пока игрок какое-то время НЕ дерётся, а не постоянно.
    private final Map<UUID, Long> lastDamageTime = new ConcurrentHashMap<>();

    public AdaptationManager(LoveAdaptation plugin) {
        this.plugin = plugin;
    }

    public void recordDamageTaken(UUID uuid) {
        lastDamageTime.put(uuid, System.currentTimeMillis());
    }

    public PlayerData getPlayerData(UUID uuid) {
        return playerDataMap.get(uuid);
    }

    /** Flips the per-player on/off switch for adaptation notifications and returns the new state. */
    public boolean toggleNotifications(Player player) {
        PlayerData data = getPlayerData(player.getUniqueId());
        if (data == null) return true;
        boolean newState = !data.isNotificationsEnabled();
        data.setNotificationsEnabled(newState);
        plugin.getDatabaseManager().savePlayerData(data);
        return newState;
    }

    public void loadPlayer(Player player) {
        UUID uuid = player.getUniqueId();
        plugin.getDatabaseManager().loadPlayerData(uuid).thenAccept(data -> {
            // loadPlayerData() completes on an async DB thread (CompletableFuture.supplyAsync),
            // so thenAccept runs there too. Hop back to the main thread before touching any
            // Bukkit API (player.getLocation(), sendTitle/sound calls inside
            // evaluateActiveAdaptation, etc.) - calling those off-thread is unsafe.
            Bukkit.getScheduler().runTask(plugin, () -> {
                Player online = Bukkit.getPlayer(uuid);
                if (online == null || !online.isOnline()) {
                    // Player disconnected while their data was still loading; discard instead
                    // of publishing a playerDataMap entry that would never be saved or removed.
                    return;
                }
                data.setLastLocation(online.getLocation().clone());
                playerDataMap.put(uuid, data);
                evaluateActiveAdaptation(online, data);
            });
        });
    }

    public void unloadPlayer(Player player) {
        lastDamageTime.remove(player.getUniqueId());
        PlayerData data = playerDataMap.remove(player.getUniqueId());
        if (data != null) {
            if (plugin.isEnabled()) {
                plugin.getDatabaseManager().savePlayerData(data);
            } else {
                plugin.getDatabaseManager().savePlayerDataSync(data);
            }
        }
    }

    public void saveAllPlayers() {
        for (PlayerData data : playerDataMap.values()) {
            plugin.getDatabaseManager().savePlayerData(data);
        }
    }

    public void saveAllPlayersSync() {
        for (PlayerData data : playerDataMap.values()) {
            plugin.getDatabaseManager().savePlayerDataSync(data);
        }
    }

    public void addProgress(Player player, AdaptationType type, long amount) {
        PlayerData data = getPlayerData(player.getUniqueId());
        if (data == null) return;

        // Blocking check: If an active adaptation exists (>90%) and it is NOT this adaptation, block progression
        boolean blockingEnabled = plugin.getConfig().getBoolean("progression_blocking.enabled", true);
        if (blockingEnabled && data.getCurrentAdaptation() != AdaptationType.BASE && data.getCurrentAdaptation() != type) {
            AdaptationData activeAdaptData = data.getAdaptationData(data.getCurrentAdaptation());
            if (activeAdaptData != null && activeAdaptData.getProgressPercent() >= 90.0) {
                // Progression blocked for other adaptations
                return;
            }
        }

        AdaptationData adaptData = data.getAdaptationData(type);
        if (adaptData == null) return;

        long requiredValue = getRequiredValue(type);
        if (requiredValue <= 0) return;

        adaptData.addProgressValue(amount);
        double maxProgressPercent = plugin.getConfig().getDouble("adaptations." + type.getConfigKey() + ".max_progress_percent", 90.0);
        double newPercent = Math.min(maxProgressPercent, ((double) adaptData.getProgressValue() / requiredValue) * maxProgressPercent);

        double oldPercent = adaptData.getProgressPercent();
        adaptData.setProgressPercent(newPercent);
        adaptData.setLastConditionFulfilledTime(System.currentTimeMillis());

        // Check if just reached 90% mastery
        if (oldPercent < 90.0 && newPercent >= 90.0) {
            handleMasteryUnlock(player, data, adaptData);
        }

        // Auto activate if no active adaptation or higher priority
        evaluateActiveAdaptation(player, data);
    }

    private long getRequiredValue(AdaptationType type) {
        String key = type.getConfigKey();
        ConfigurationSection sec = plugin.getConfig().getConfigurationSection("adaptations." + key);
        if (sec == null) return 100;

        if (sec.contains("required_ticks")) return sec.getLong("required_ticks");
        if (sec.contains("required_events")) return sec.getLong("required_events");
        if (sec.contains("required_damage")) return (long) sec.getDouble("required_damage");
        if (sec.contains("required_distance_blocks")) return (long) sec.getDouble("required_distance_blocks");

        return 100;
    }

    private void handleMasteryUnlock(Player player, PlayerData playerData, AdaptationData adaptData) {
        // addProgress() calls this every time progressPercent recrosses 90% from below - which,
        // once already unlocked, happens on essentially every subsequent qualifying event: each
        // periodic degradation tick (AdaptationTask, every degradation_system.check_interval_seconds)
        // nudges progressPercent a fraction below the cap without touching the underlying
        // progressValue counter, so the very next qualifying event (e.g. one more fall for HEIGHT)
        // recomputes percent straight back to the cap and would re-trigger the full celebration
        // (title+sound+history log) again - a permanent flap between "Мастерство разблокировано"
        // and "деградирует" instead of a one-time achievement. Cooldown-gate the celebratory
        // effects; the mechanical state below (current adaptation, active flag) still updates
        // every time regardless, so nothing about the actual gameplay bonus is delayed.
        long cooldownMs = plugin.getConfig().getLong("notifications.mastery_unlock.cooldown_seconds", 300) * 1000L;
        long now = System.currentTimeMillis();
        boolean onCooldown = (now - adaptData.getLastMasteryNotifyAt()) < cooldownMs;

        adaptData.setUnlocked(true);
        adaptData.setUnlockedAt(now);

        if (!onCooldown) {
            adaptData.setLastMasteryNotifyAt(now);
            plugin.getDatabaseManager().logHistory(player.getUniqueId(), adaptData.getType().name(), "MASTERY_UNLOCK", adaptData.getProgressPercent(), "Mastery 90% unlocked");

            if (playerData.isNotificationsEnabled() && plugin.getConfig().getBoolean("notifications.mastery_unlock.enabled", true)) {
                String name = getAdaptationDisplayName(adaptData.getType());
                String title = plugin.getConfig().getString("notifications.mastery_unlock.title", "&6✨ Мастерство разблокировано");
                String subtitle = plugin.getConfig().getString("notifications.mastery_unlock.subtitle", "&f%adaptation_name%").replace("%adaptation_name%", name);
                String sound = plugin.getConfig().getString("notifications.mastery_unlock.sound", "ENTITY_PLAYER_LEVELUP");
                float vol = (float) plugin.getConfig().getDouble("notifications.mastery_unlock.sound_volume", 1.0);
                float pitch = (float) plugin.getConfig().getDouble("notifications.mastery_unlock.sound_pitch", 1.2);

                Utils.sendTitle(player, title, subtitle);
                Utils.playSound(player, sound, vol, pitch);
            }
        }

        // Set as current active adaptation if higher or available
        playerData.setCurrentAdaptation(adaptData.getType());
        adaptData.setActive(true);
    }

    public void evaluateActiveAdaptation(Player player, PlayerData data) {
        if (player == null || data == null) return;

        // Check condition fulfillment for current adaptation
        AdaptationType current = data.getCurrentAdaptation();
        boolean currentValid = isConditionFulfilled(player, current);

        if (currentValid && current != AdaptationType.BASE) {
            data.getAdaptationData(current).setLastConditionFulfilledTime(System.currentTimeMillis());
            return;
        }

        // If current is not valid or degraded below 60%, check priority order for reactivation
        List<String> priorityList = plugin.getConfig().getStringList("reactivation_priority.order");
        if (priorityList.isEmpty()) {
            priorityList = Arrays.asList("NETHER", "ABYSS", "CAVE", "WATER", "HEIGHT", "COMBAT", "TRAVEL");
        }

        AdaptationType bestMatch = AdaptationType.BASE;
        double bestProgress = -1.0;

        for (String typeStr : priorityList) {
            AdaptationType type = AdaptationType.fromString(typeStr);
            if (type == AdaptationType.BASE) continue;

            AdaptationData adaptData = data.getAdaptationData(type);
            if (adaptData != null && adaptData.getProgressPercent() >= 60.0 && isConditionFulfilled(player, type)) {
                if (adaptData.getProgressPercent() > bestProgress) {
                    bestProgress = adaptData.getProgressPercent();
                    bestMatch = type;
                }
            }
        }

        if (bestMatch != current) {
            switchAdaptation(player, data, bestMatch);
        }
    }

    public void switchAdaptation(Player player, PlayerData data, AdaptationType newType) {
        AdaptationType oldType = data.getCurrentAdaptation();
        if (oldType == newType) return;

        if (oldType != AdaptationType.BASE) {
            AdaptationData oldData = data.getAdaptationData(oldType);
            if (oldData != null) oldData.setActive(false);
        }

        data.setCurrentAdaptation(newType);
        if (newType != AdaptationType.BASE) {
            AdaptationData newData = data.getAdaptationData(newType);
            if (newData != null) newData.setActive(true);

            if (data.isNotificationsEnabled() && plugin.getConfig().getBoolean("notifications.adaptation_activated.enabled", true)) {
                String name = getAdaptationDisplayName(newType);
                String msg = plugin.getConfig().getString("notifications.adaptation_activated.actionbar", "&aАдаптация активна: &f%adaptation_name%").replace("%adaptation_name%", name);
                String sound = plugin.getConfig().getString("notifications.adaptation_activated.sound", "BLOCK_RESPAWN_ANCHOR_CHARGE");
                Utils.sendActionBar(player, msg);
                Utils.playSound(player, sound, 0.8f, 1.0f);
            }
        }

        plugin.getDatabaseManager().logHistory(player.getUniqueId(), newType.name(), "ACTIVATED", newType != AdaptationType.BASE ? data.getAdaptationData(newType).getProgressPercent() : 0.0, "Switched adaptation from " + oldType.name());
    }

    public boolean isConditionFulfilled(Player player, AdaptationType type) {
        if (player == null || type == AdaptationType.BASE) return false;
        switch (type) {
            case WATER:
                return player.isInWater() || player.isSwimming();
            case NETHER:
                return player.getWorld().getEnvironment() == World.Environment.NETHER;
            case CAVE:
                int caveY = plugin.getConfig().getInt("adaptations.cave.y_threshold", 32);
                return player.getLocation().getBlockY() < caveY;
            case ABYSS:
                int abyssY = plugin.getConfig().getInt("adaptations.abyss.y_threshold", -40);
                return player.getLocation().getBlockY() < abyssY;
            case HEIGHT:
                return player.getFallDistance() > 3.0;
            case COMBAT:
                Entity damageCause = player.getLastDamageCause() != null ? player.getLastDamageCause().getEntity() : null;
                return (player.getNoDamageTicks() > 0 || (damageCause instanceof Monster));
            case TRAVEL:
                return player.isSprinting() || player.getVelocity().lengthSquared() > 0.001;
            default:
                return false;
        }
    }

    public void processDegradation(Player player, PlayerData data) {
        if (!plugin.getConfig().getBoolean("degradation_system.enabled", true)) return;

        AdaptationType current = data.getCurrentAdaptation();
        if (current == AdaptationType.BASE) return;

        AdaptationData adaptData = data.getAdaptationData(current);
        if (adaptData == null) return;

        boolean conditionMet = isConditionFulfilled(player, current);
        if (!conditionMet) {
            double ratePerHour = plugin.getConfig().getDouble("adaptations." + current.getConfigKey() + ".degradation_rate_percent_per_hour", 5.0);
            double checkIntervalSec = plugin.getConfig().getDouble("degradation_system.check_interval_seconds", 60.0);
            double loss = (ratePerHour / 3600.0) * checkIntervalSec;

            double oldPercent = adaptData.getProgressPercent();
            double newPercent = Math.max(0.0, oldPercent - loss);
            adaptData.setProgressPercent(newPercent);

            if (oldPercent >= 90.0 && newPercent < 90.0) {
                // Lost mastery. Cooldown-gated for the same reason as handleMasteryUnlock's
                // celebration: once percent sits right at the cap, degradation crosses this same
                // 90% line on effectively every check tick until a fresh fall pushes it back up,
                // so without a cooldown this actionbar would fire once a minute forever.
                long degradingCooldownMs = plugin.getConfig().getLong("notifications.adaptation_degrading.cooldown_seconds", 300) * 1000L;
                long now = System.currentTimeMillis();
                if (now - adaptData.getLastDegradingNotifyAt() >= degradingCooldownMs) {
                    adaptData.setLastDegradingNotifyAt(now);
                    if (data.isNotificationsEnabled() && plugin.getConfig().getBoolean("notifications.adaptation_degrading.enabled", true)) {
                        String name = getAdaptationDisplayName(current);
                        String msg = plugin.getConfig().getString("notifications.adaptation_degrading.actionbar", "&c⚠ Адаптация деградирует: &f%adaptation_name% (%progress%)")
                                .replace("%adaptation_name%", name)
                                .replace("%progress%", String.format("%.1f%%", newPercent));
                        Utils.sendActionBar(player, msg);
                        Utils.playSound(player, "ENTITY_GENERIC_HURT", 0.5f, 0.8f);
                    }
                }
            }

            double deactivationThreshold = plugin.getConfig().getDouble("degradation_system.deactivation_threshold_percent", 60.0);
            if (newPercent < deactivationThreshold) {
                // Lost active status
                adaptData.setActive(false);
                switchAdaptation(player, data, AdaptationType.BASE);
                if (data.isNotificationsEnabled() && plugin.getConfig().getBoolean("notifications.adaptation_deactivated.enabled", true)) {
                    String name = getAdaptationDisplayName(current);
                    String msg = plugin.getConfig().getString("notifications.adaptation_deactivated.actionbar", "&cАдаптация потеряна: &f%adaptation_name%").replace("%adaptation_name%", name);
                    // Losing an adaptation is a loss of progress: always mirrored to chat
                    Utils.sendImportantActionBar(player, msg);
                    Utils.playSound(player, "ENTITY_GENERIC_DEATH", 0.5f, 0.5f);
                }
            }
        }
    }

    private final Random passiveRegenRandom = new Random();

    /**
     * "Боевой закал" больше не выдаёт регенерацию за каждый полученный удар (это делало её
     * фактически постоянной и слишком сильной в затяжном бою) - вместо этого, пока адаптация
     * активна, она изредка подлечивает игрока, только если он какое-то время не дрался
     * (не получал урона) и не голоден. Вызывается из {@link me.lovelace.LoveAdaptation.tasks.AdaptationTask}
     * с интервалом {@code adaptations.combat.passive_regen.check_interval_seconds}.
     */
    public void processCombatPassiveRegen(Player player, PlayerData data) {
        if (!plugin.getConfig().getBoolean("adaptations.combat.passive_regen.enabled", true)) return;

        boolean potionActive = data.isPotionActive();
        if (!potionActive && data.getCurrentAdaptation() != AdaptationType.COMBAT) return;

        Long lastDamage = lastDamageTime.get(player.getUniqueId());
        long minSecondsSinceDamage = plugin.getConfig().getLong("adaptations.combat.passive_regen.min_seconds_since_damage", 60);
        if (lastDamage != null && System.currentTimeMillis() - lastDamage < minSecondsSinceDamage * 1000L) {
            return;
        }

        int minFoodLevel = plugin.getConfig().getInt("adaptations.combat.passive_regen.min_food_level", 18);
        if (player.getFoodLevel() < minFoodLevel) return;

        AdaptationData combatData = data.getAdaptationData(AdaptationType.COMBAT);
        boolean isMastery = potionActive || (combatData != null && combatData.getProgressPercent() >= 90.0);

        double chance = isMastery
                ? plugin.getConfig().getDouble("adaptations.combat.passive_regen.chance_bonus", 0.4)
                : plugin.getConfig().getDouble("adaptations.combat.passive_regen.chance_base", 0.25);
        if (passiveRegenRandom.nextDouble() >= chance) return;

        int level = isMastery
                ? plugin.getConfig().getInt("adaptations.combat.passive_regen.level_bonus", 1)
                : plugin.getConfig().getInt("adaptations.combat.passive_regen.level_base", 0);
        int duration = isMastery
                ? plugin.getConfig().getInt("adaptations.combat.passive_regen.duration_ticks_bonus", 100)
                : plugin.getConfig().getInt("adaptations.combat.passive_regen.duration_ticks_base", 60);

        player.addPotionEffect(new PotionEffect(PotionEffectType.REGENERATION, duration, level, false, false));
    }

    /** Parsed once per config load instead of getMapList() per player per tick. */
    private record EffectSpec(PotionEffectType type, int level, int durationTicks, String applyIn, Integer belowY) {}

    private final Map<String, List<EffectSpec>> effectCache = new ConcurrentHashMap<>();

    /** Drop parsed effect lists; call after plugin.reloadConfig(). */
    public void reloadEffectCache() {
        effectCache.clear();
    }

    private List<EffectSpec> getEffectSpecs(String secPath) {
        return effectCache.computeIfAbsent(secPath, path -> {
            List<EffectSpec> out = new ArrayList<>();
            for (Map<?, ?> effectMap : plugin.getConfig().getMapList(path)) {
                Object typeObj = effectMap.get("type");
                if (!(typeObj instanceof String typeStr)) continue;
                // Entries with "trigger" are granted at the event in PlayerListener, not by the periodic loop.
                if (effectMap.containsKey("trigger")) continue;
                PotionEffectType effectType = PotionEffectType.getByName(typeStr.toUpperCase());
                if (effectType == null) continue;
                int level = effectMap.get("level") instanceof Number n ? n.intValue() : 1;
                int duration = effectMap.get("duration_ticks") instanceof Number n ? n.intValue() : 40;
                String applyIn = null;
                if (effectMap.get("apply_in") instanceof List<?> inList && !inList.isEmpty()) {
                    applyIn = inList.get(0).toString();
                }
                Integer belowY = effectMap.get("apply_below_y") instanceof Number n ? n.intValue() : null;
                out.add(new EffectSpec(effectType, level, duration, applyIn, belowY));
            }
            return Collections.unmodifiableList(out);
        });
    }

    public void applyEffects(Player player, PlayerData data) {
        applyEffects(player, data, true);
    }

    public void applyEffects(Player player, PlayerData data, boolean scanNearbyForGlow) {
        if (player == null || data == null || !player.isOnline()) return;

        boolean potionActive = data.isPotionActive();
        AdaptationType activeType = data.getCurrentAdaptation();

        for (AdaptationType type : AdaptationType.values()) {
            if (type == AdaptationType.BASE) continue;
            boolean applyThis = potionActive || (activeType == type);
            if (!applyThis) continue;

            AdaptationData adaptData = data.getAdaptationData(type);
            if (adaptData == null) continue;

            boolean isMastery = potionActive || adaptData.getProgressPercent() >= 90.0;
            String secPath = "adaptations." + type.getConfigKey() + (isMastery ? ".effects_bonus" : ".effects_base");

            for (EffectSpec spec : getEffectSpecs(secPath)) {
                if (!potionActive) {
                    if (spec.applyIn() != null) {
                        if ("WATER".equalsIgnoreCase(spec.applyIn()) && !player.isInWater()) continue;
                        if ("NETHER".equalsIgnoreCase(spec.applyIn()) && player.getWorld().getEnvironment() != World.Environment.NETHER) continue;
                    }
                    if (spec.belowY() != null && player.getLocation().getBlockY() >= spec.belowY()) continue;
                }

                if (spec.type() == PotionEffectType.GLOWING && type == AdaptationType.CAVE) {
                    if (!scanNearbyForGlow) continue;
                    // Glow outlives the (rarer) scan interval so monsters do not flicker.
                    for (Entity nearby : player.getNearbyEntities(16, 16, 16)) {
                        if (nearby instanceof Monster) {
                            ((Monster) nearby).addPotionEffect(new PotionEffect(PotionEffectType.GLOWING, 100, 0, false, false));
                        }
                    }
                } else {
                    int amplifier = spec.level() - 1;
                    PotionEffect current = player.getPotionEffect(spec.type());
                    // Threshold must exceed the task interval, otherwise the effect would lapse between runs.
                    int threshold = (int) me.lovelace.LoveAdaptation.tasks.AdaptationTask.INTERVAL_TICKS + 10;
                    if (AdaptationRules.needsEffectRefresh(current != null,
                            current != null ? current.getDuration() : 0,
                            current != null ? current.getAmplifier() : 0,
                            amplifier, threshold)) {
                        // Duration is never shorter than the threshold window so refresh gaps cannot appear.
                        int duration = Math.max(spec.durationTicks(), threshold + 20);
                        player.addPotionEffect(new PotionEffect(spec.type(), duration, amplifier, false, false, true));
                    }
                }
            }
        }
    }

    public String getAdaptationDisplayName(AdaptationType type) {
        if (type == AdaptationType.BASE) {
            return plugin.getConfig().getString("base_adaptation.name", "Базовая форма");
        }
        return plugin.getConfig().getString("adaptations." + type.getConfigKey() + ".name", type.name());
    }

    public String getAdaptationColor(AdaptationType type) {
        if (type == AdaptationType.BASE) {
            return plugin.getConfig().getString("base_adaptation.color", "&7");
        }
        return plugin.getConfig().getString("adaptations." + type.getConfigKey() + ".color", "&f");
    }

    public String getAdaptationDescription(AdaptationType type) {
        if (type == AdaptationType.BASE) {
            return "";
        }
        return plugin.getConfig().getString("adaptations." + type.getConfigKey() + ".description", "");
    }

    public String getHeadTexture(AdaptationType type) {
        if (type == AdaptationType.BASE) {
            return plugin.getConfig().getString("base_adaptation.head_texture", HeadTextures.BASE);
        }
        return plugin.getConfig().getString("adaptations." + type.getConfigKey() + ".head_texture", HeadTextures.forType(type));
    }
}
