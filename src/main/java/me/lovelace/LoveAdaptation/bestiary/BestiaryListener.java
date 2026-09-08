package me.lovelace.LoveAdaptation.bestiary;

import me.lovelace.LoveAdaptation.LoveAdaptation;
import me.lovelace.LoveAdaptation.utils.Utils;
import org.bukkit.attribute.Attribute;
import org.bukkit.attribute.AttributeInstance;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.entity.Projectile;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.entity.CreatureSpawnEvent;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.ItemStack;
import org.bukkit.metadata.FixedMetadataValue;
import org.bukkit.persistence.PersistentDataType;
import org.bukkit.projectiles.ProjectileSource;

import java.util.Locale;

/**
 * Слушатель событий для Полевого Бестиария Охотника.
 * Обеспечивает надежный учет каждого убийства, боевые баффы и открытие интерфейса книги.
 */
public class BestiaryListener implements Listener {

    private final LoveAdaptation plugin;
    private final BestiaryManager bestiaryManager;

    public BestiaryListener(LoveAdaptation plugin, BestiaryManager bestiaryManager) {
        this.plugin = plugin;
        this.bestiaryManager = bestiaryManager;
    }

    // =========================================================================
    // 1. МЕТКА МОБОВ ИЗ СПАВНЕРОВ
    // =========================================================================
    @EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
    public void onCreatureSpawn(CreatureSpawnEvent event) {
        if (event.getSpawnReason() == CreatureSpawnEvent.SpawnReason.SPAWNER) {
            LivingEntity entity = event.getEntity();
            entity.setMetadata("is_farm_mob", new FixedMetadataValue(plugin, true));
            entity.getPersistentDataContainer().set(bestiaryManager.getFarmMobKey(), PersistentDataType.BYTE, (byte) 1);
        }
    }

    private boolean isFarmMob(LivingEntity entity) {
        if (entity == null) return false;
        if (entity.hasMetadata("is_farm_mob")) return true;
        return entity.getPersistentDataContainer().has(bestiaryManager.getFarmMobKey(), PersistentDataType.BYTE);
    }

    // =========================================================================
    // 2. НАДЕЖНЫЙ УЧЕТ УБИЙСТВ В БЕСТИАРИЙ
    // =========================================================================
    @EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
    public void onEntityDeath(EntityDeathEvent event) {
        LivingEntity victim = event.getEntity();
        Player killer = victim.getKiller();
        if (killer == null) return;

        bestiaryManager.addKill(killer, victim.getType());
    }

    // =========================================================================
    // 3. БОЕВЫЕ БАФФЫ И ОТОБРАЖЕНИЕ ЗДОРОВЬЯ (ПО СЕМЕЙСТВАМ)
    // =========================================================================
    @EventHandler(priority = EventPriority.HIGH, ignoreCancelled = true)
    public void onEntityDamageByEntity(EntityDamageByEntityEvent event) {
        // --- 3.1. Игрок бьет моба ---
        Player playerAttacker = getPlayerAttacker(event.getDamager());
        if (playerAttacker != null && event.getEntity() instanceof LivingEntity target && !(target instanceof Player)) {
            ItemStack book = bestiaryManager.getBestiaryFromInventory(playerAttacker);
            if (book != null) {
                StudiedMob studiedMob = StudiedMob.fromEntityType(target.getType());
                int kills = (studiedMob != null)
                        ? bestiaryManager.getFamilyKills(book, studiedMob.getFamily())
                        : bestiaryManager.getKills(book, target.getType());

                ResearchTier tier = bestiaryManager.getTier(kills);

                if (tier != ResearchTier.NONE) {
                    String mobName = (studiedMob != null) ? bestiaryManager.getMobDisplayName(studiedMob) : target.getName();

                    double healthRemaining = Math.max(0.0, target.getHealth() - event.getFinalDamage());
                    AttributeInstance maxHealthAttr = target.getAttribute(Attribute.MAX_HEALTH);
                    double maxHealth = (maxHealthAttr != null) ? maxHealthAttr.getValue() : 20.0;

                    String actionbarPattern = plugin.getConfig().getString("bestiary.messages.actionbar_hp", "&c[%mob_name%] &fHP: &e%current_hp%&7/&c%max_hp%");
                    String actionbarMsg = actionbarPattern.replace("%mob_name%", mobName)
                            .replace("%current_hp%", String.format(Locale.US, "%.1f", healthRemaining))
                            .replace("%max_hp%", String.format(Locale.US, "%.1f", maxHealth));
                    Utils.sendActionBar(playerAttacker, actionbarMsg);

                    double bonus = bestiaryManager.getTierBonusDamage(tier);
                    if (isFarmMob(target)) {
                        bonus = plugin.getConfig().getDouble("bestiary.anti_abuse.farm_mob_damage_bonus", 0.01);
                    }
                    event.setDamage(event.getDamage() * (1.0 + bonus));
                }
            }
        }

        // --- 3.2. Моб бьет игрока (Сопротивление урону) ---
        if (event.getEntity() instanceof Player playerVictim) {
            LivingEntity mobAttacker = getLivingAttacker(event.getDamager());
            if (mobAttacker != null && !(mobAttacker instanceof Player)) {
                ItemStack book = bestiaryManager.getBestiaryFromInventory(playerVictim);
                if (book != null) {
                    StudiedMob studiedMob = StudiedMob.fromEntityType(mobAttacker.getType());
                    int kills = (studiedMob != null)
                            ? bestiaryManager.getFamilyKills(book, studiedMob.getFamily())
                            : bestiaryManager.getKills(book, mobAttacker.getType());

                    ResearchTier tier = bestiaryManager.getTier(kills);
                    double resistance = bestiaryManager.getTierResistance(tier);

                    if (resistance > 0.0) {
                        event.setDamage(event.getDamage() * (1.0 - resistance));
                    }
                }
            }
        }
    }

    private Player getPlayerAttacker(Entity damager) {
        if (damager instanceof Player p) {
            return p;
        }
        if (damager instanceof Projectile projectile) {
            ProjectileSource shooter = projectile.getShooter();
            if (shooter instanceof Player p) {
                return p;
            }
        }
        return null;
    }

    private LivingEntity getLivingAttacker(Entity damager) {
        if (damager instanceof LivingEntity l) {
            return l;
        }
        if (damager instanceof Projectile projectile) {
            ProjectileSource shooter = projectile.getShooter();
            if (shooter instanceof LivingEntity l) {
                return l;
            }
        }
        return null;
    }

    // =========================================================================
    // 4. ИНТЕРФЕЙС ЧТЕНИЯ КНИГИ (ПКМ)
    // =========================================================================
    @EventHandler
    public void onPlayerInteract(PlayerInteractEvent event) {
        if (event.getHand() != EquipmentSlot.HAND) return;

        Action action = event.getAction();
        if (action != Action.RIGHT_CLICK_AIR && action != Action.RIGHT_CLICK_BLOCK) return;

        ItemStack item = event.getItem();
        if (bestiaryManager.isBestiaryBook(item)) {
            event.setCancelled(true);
            bestiaryManager.openBestiary(event.getPlayer(), item);
        }
    }
}
