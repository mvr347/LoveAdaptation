package me.lovelace.LoveAdaptation.listeners;

import me.lovelace.LoveAdaptation.managers.PluginManager;
import me.lovelace.LoveAdaptation.models.AdaptationData;
import me.lovelace.LoveAdaptation.models.AdaptationType;
import me.lovelace.LoveAdaptation.models.PlayerData;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Monster;
import org.bukkit.entity.Player;
import org.bukkit.entity.Projectile;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.FoodLevelChangeEvent;
import org.bukkit.event.player.PlayerItemConsumeEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

public class PlayerListener implements Listener {

    @EventHandler(priority = EventPriority.MONITOR)
    public void onPlayerJoin(PlayerJoinEvent event) {
        PluginManager.getInstance().getAdaptationManager().loadPlayer(event.getPlayer());
    }

    @EventHandler(priority = EventPriority.MONITOR)
    public void onPlayerQuit(PlayerQuitEvent event) {
        PluginManager.getInstance().getAdaptationManager().unloadPlayer(event.getPlayer());
    }

    private final java.util.Map<java.util.UUID, Double> travelAccumulator = new java.util.concurrent.ConcurrentHashMap<>();

    @EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
    public void onPlayerMove(PlayerMoveEvent event) {
        Player player = event.getPlayer();
        PlayerData data = PluginManager.getInstance().getAdaptationManager().getPlayerData(player.getUniqueId());
        if (data == null)
            return;

        Location from = event.getFrom();
        Location to = event.getTo();
        if (to == null)
            return;

        // Calculate travel distance
        if (from.getWorld() != null && from.getWorld().equals(to.getWorld())) {
            double distance = from.distance(to);
            if (distance > 0.05 && distance < 10.0) { // Exclude teleports
                double accumulated = travelAccumulator.getOrDefault(player.getUniqueId(), 0.0) + distance;
                if (accumulated >= 1.0) {
                    long blocks = (long) accumulated;
                    PluginManager.getInstance().getAdaptationManager().addProgress(player, AdaptationType.TRAVEL, blocks);
                    travelAccumulator.put(player.getUniqueId(), accumulated - blocks);
                } else {
                    travelAccumulator.put(player.getUniqueId(), accumulated);
                }
            }
        }

        // Water tracking
        if (player.isInWater() || player.isSwimming()) {
            PluginManager.getInstance().getAdaptationManager().addProgress(player, AdaptationType.WATER, 1);
        }

        // Nether tracking
        if (player.getWorld().getEnvironment() == World.Environment.NETHER) {
            PluginManager.getInstance().getAdaptationManager().addProgress(player, AdaptationType.NETHER, 1);
        }

        // Cave tracking (Y < 32)
        int caveY = PluginManager.getInstance().getPlugin().getConfig().getInt("adaptations.cave.y_threshold", 32);
        if (to.getBlockY() < caveY) {
            PluginManager.getInstance().getAdaptationManager().addProgress(player, AdaptationType.CAVE, 1);
        }

        // End tracking
        if (player.getWorld().getEnvironment() == World.Environment.THE_END) {
            PluginManager.getInstance().getAdaptationManager().addProgress(player, AdaptationType.END, 1);
        }
    }

    @EventHandler(priority = EventPriority.HIGH, ignoreCancelled = true)
    public void onEntityDamage(EntityDamageEvent event) {
        if (!(event.getEntity() instanceof Player))
            return;
        Player player = (Player) event.getEntity();
        PlayerData data = PluginManager.getInstance().getAdaptationManager().getPlayerData(player.getUniqueId());
        if (data == null)
            return;

        AdaptationType active = data.getCurrentAdaptation();
        boolean potionActive = data.isPotionActive();

        // Height adaptation fall damage reduction
        if (event.getCause() == EntityDamageEvent.DamageCause.FALL) {
            PluginManager.getInstance().getAdaptationManager().addProgress(player, AdaptationType.HEIGHT, 1);

            if (potionActive || active == AdaptationType.HEIGHT) {
                AdaptationData heightData = data.getAdaptationData(AdaptationType.HEIGHT);
                boolean isMastery = potionActive || (heightData != null && heightData.getProgressPercent() >= 90.0);
                double reduction = isMastery
                        ? PluginManager.getInstance().getPlugin().getConfig()
                                .getDouble("adaptations.height.fall_damage_reduction_bonus", 0.7)
                        : PluginManager.getInstance().getPlugin().getConfig()
                                .getDouble("adaptations.height.fall_damage_reduction_base", 0.5);

                event.setDamage(event.getDamage() * (1.0 - reduction));

                if (isMastery) {
                    player.addPotionEffect(new PotionEffect(PotionEffectType.RESISTANCE, 40, 0, false, false));
                }
            }
        }

        // Nether adaptation fire/lava damage reduction
        if (event.getCause() == EntityDamageEvent.DamageCause.FIRE ||
                event.getCause() == EntityDamageEvent.DamageCause.FIRE_TICK ||
                event.getCause() == EntityDamageEvent.DamageCause.LAVA) {

            if (potionActive || active == AdaptationType.NETHER) {
                AdaptationData netherData = data.getAdaptationData(AdaptationType.NETHER);
                boolean isMastery = potionActive || (netherData != null && netherData.getProgressPercent() >= 90.0);
                double reduction = isMastery
                        ? PluginManager.getInstance().getPlugin().getConfig()
                                .getDouble("adaptations.nether.fire_damage_reduction_bonus", 0.5)
                        : PluginManager.getInstance().getPlugin().getConfig()
                                .getDouble("adaptations.nether.fire_damage_reduction_base", 0.4);

                event.setDamage(event.getDamage() * (1.0 - reduction));
            }
        }
    }

    @EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
    public void onEntityDamageByEntity(EntityDamageByEntityEvent event) {
        if (!(event.getEntity() instanceof Player))
            return;
        Player player = (Player) event.getEntity();
        PlayerData data = PluginManager.getInstance().getAdaptationManager().getPlayerData(player.getUniqueId());
        if (data == null)
            return;

        Entity damager = event.getDamager();
        if (damager instanceof org.bukkit.entity.Projectile) {
            org.bukkit.projectiles.ProjectileSource shooter = ((org.bukkit.entity.Projectile) damager).getShooter();
            if (shooter instanceof Entity) {
                damager = (Entity) shooter;
            }
        }

        if (damager instanceof Monster || damager instanceof Player) {
            long damageTaken = (long) event.getFinalDamage();
            PluginManager.getInstance().getAdaptationManager().addProgress(player, AdaptationType.COMBAT, damageTaken);

            boolean potionActive = data.isPotionActive();
            if (potionActive || data.getCurrentAdaptation() == AdaptationType.COMBAT) {
                AdaptationData combatData = data.getAdaptationData(AdaptationType.COMBAT);
                boolean isMastery = potionActive || (combatData != null && combatData.getProgressPercent() >= 90.0);

                int regenLevel = isMastery ? 2 : 1; // 0-indexed: level 3 vs level 2
                int duration = isMastery ? 120 : 100;
                player.addPotionEffect(
                        new PotionEffect(PotionEffectType.REGENERATION, duration, regenLevel, false, false));

                if (isMastery) {
                    player.addPotionEffect(new PotionEffect(PotionEffectType.RESISTANCE, 60, 0, false, false));
                }
            }
        }
    }

    @EventHandler(priority = EventPriority.HIGH, ignoreCancelled = true)
    public void onFoodLevelChange(FoodLevelChangeEvent event) {
        if (!(event.getEntity() instanceof Player))
            return;
        Player player = (Player) event.getEntity();
        PlayerData data = PluginManager.getInstance().getAdaptationManager().getPlayerData(player.getUniqueId());
        if (data == null)
            return;

        // Apply hunger reduction for TRAVEL or WATER adaptation
        boolean potionActive = data.isPotionActive();
        AdaptationType active = data.getCurrentAdaptation();

        if (event.getFoodLevel() < player.getFoodLevel()) { // Hunger decreasing
            double modifier = 1.0;

            if (potionActive || active == AdaptationType.TRAVEL) {
                AdaptationData travelData = data.getAdaptationData(AdaptationType.TRAVEL);
                boolean isMastery = potionActive || (travelData != null && travelData.getProgressPercent() >= 90.0);
                modifier = isMastery
                        ? PluginManager.getInstance().getPlugin().getConfig()
                                .getDouble("adaptations.travel.hunger_modifier_bonus", 0.5)
                        : PluginManager.getInstance().getPlugin().getConfig()
                                .getDouble("adaptations.travel.hunger_modifier_base", 0.7);
            } else if (active == AdaptationType.WATER) {
                AdaptationData waterData = data.getAdaptationData(AdaptationType.WATER);
                boolean isMastery = waterData != null && waterData.getProgressPercent() >= 90.0;
                if (isMastery) {
                    modifier = PluginManager.getInstance().getPlugin().getConfig()
                            .getDouble("adaptations.water.hunger_modifier_bonus", 0.9);
                }
            }

            if (modifier < 1.0) {
                int loss = player.getFoodLevel() - event.getFoodLevel();
                int reducedLoss = (int) Math.round(loss * modifier);
                event.setFoodLevel(player.getFoodLevel() - reducedLoss);
            }
        }
    }

    @EventHandler
    public void onItemConsume(PlayerItemConsumeEvent event) {
        if (PluginManager.getInstance().getPotionManager().isAdaptationPotion(event.getItem())) {
            int level = PluginManager.getInstance().getPotionManager().getPotionLevel(event.getItem());
            PluginManager.getInstance().getPotionManager().applyAdaptationPotion(event.getPlayer(), level);
        }
    }
}