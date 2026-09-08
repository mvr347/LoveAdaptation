package me.lovelace.LoveAdaptation.bestiary;

import me.lovelace.LoveAdaptation.utils.Utils;
import net.kyori.adventure.inventory.Book;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.TextComponent;
import net.kyori.adventure.text.event.HoverEvent;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.format.TextDecoration;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.Sound;
import org.bukkit.attribute.Attribute;
import org.bukkit.attribute.AttributeInstance;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
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
import org.bukkit.inventory.meta.BookMeta;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.metadata.FixedMetadataValue;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.projectiles.ProjectileSource;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public final class BestiaryModule implements Listener, CommandExecutor {

    private final JavaPlugin plugin;
    private final NamespacedKey isBestiaryKey;
    private final NamespacedKey farmMobKey;

    public BestiaryModule(JavaPlugin plugin) {
        this.plugin = plugin;
        this.isBestiaryKey = new NamespacedKey(plugin, "is_bestiary_book");
        this.farmMobKey = new NamespacedKey(plugin, "is_farm_mob");
    }

    public ItemStack createBestiaryBook() {
        return createBestiaryBook((Player) null);
    }

    public ItemStack createBestiaryBook(Player player) {
        ItemStack book = new ItemStack(Material.WRITTEN_BOOK);
        BookMeta meta = (BookMeta) book.getItemMeta();
        if (meta != null) {
            meta.setTitle("Полевой Бестиарий");
            meta.setAuthor(player != null ? player.getName() : "Исследователь");
            meta.setGeneration(BookMeta.Generation.ORIGINAL);
            meta.displayName(Component.text("§6Полевой Бестиарий"));
            meta.getPersistentDataContainer().set(isBestiaryKey, PersistentDataType.BYTE, (byte) 1);

            List<Component> lore = new ArrayList<>();
            lore.add(Component.text("§7Физический журнал исследований."));
            lore.add(Component.text("§7Заполняется во время живой охоты."));
            lore.add(Component.text(" "));
            lore.add(Component.text("§e[ПКМ] §7- Открыть страницы бестиария."));
            lore.add(Component.text("§8Бонусы активны, пока книга в инвентаре."));
            meta.lore(lore);

            meta.addPages(Component.text("§8[Пустой бестиарий]"));
            book.setItemMeta(meta);
        }
        return book;
    }

    public boolean isBestiaryBook(ItemStack item) {
        if (item == null || item.getType() != Material.WRITTEN_BOOK) return false;
        ItemMeta meta = item.getItemMeta();
        if (meta == null) return false;
        return meta.getPersistentDataContainer().has(isBestiaryKey, PersistentDataType.BYTE);
    }

    public int getBestiarySlot(Player player) {
        if (player == null) return -1;
        for (int i = 0; i < player.getInventory().getSize(); i++) {
            ItemStack item = player.getInventory().getItem(i);
            if (isBestiaryBook(item)) {
                return i;
            }
        }
        ItemStack offHand = player.getInventory().getItemInOffHand();
        if (isBestiaryBook(offHand)) {
            return 40;
        }
        return -1;
    }

    public ItemStack getBestiaryFromInventory(Player player) {
        if (player == null) return null;
        int slot = getBestiarySlot(player);
        if (slot == 40) {
            return player.getInventory().getItemInOffHand();
        } else if (slot >= 0) {
            return player.getInventory().getItem(slot);
        }
        return null;
    }

    public int getKills(ItemStack book, EntityType type) {
        if (book == null || type == null) return 0;
        ItemMeta meta = book.getItemMeta();
        if (meta == null) return 0;
        NamespacedKey key = new NamespacedKey(plugin, "kills_" + type.name().toLowerCase());
        return meta.getPersistentDataContainer().getOrDefault(key, PersistentDataType.INTEGER, 0);
    }

    public int getFamilyKills(ItemStack book, MobFamily family) {
        if (book == null || family == null) return 0;
        int total = 0;
        for (StudiedMob mob : StudiedMob.values()) {
            if (mob.getFamily() == family) {
                total += getKills(book, mob.getType());
            }
        }
        return total;
    }

    public int countDiscoveredSpecies(ItemStack book) {
        if (book == null) return 0;
        int count = 0;
        for (StudiedMob mob : StudiedMob.values()) {
            if (getKills(book, mob.getType()) > 0) {
                count++;
            }
        }
        return count;
    }

    public int countDiscoveredSpeciesInFamily(ItemStack book, MobFamily family) {
        if (book == null || family == null) return 0;
        int count = 0;
        for (StudiedMob mob : StudiedMob.values()) {
            if (mob.getFamily() == family && getKills(book, mob.getType()) > 0) {
                count++;
            }
        }
        return count;
    }

    public boolean isFamilyDiscovered(ItemStack book, MobFamily family) {
        if (book == null || family == null) return false;
        int totalInFamily = 0;
        for (StudiedMob mob : StudiedMob.values()) {
            if (mob.getFamily() == family) {
                totalInFamily++;
            }
        }

        if (totalInFamily <= 1) {
            return getFamilyKills(book, family) >= 20;
        }

        return countDiscoveredSpeciesInFamily(book, family) >= 2;
    }

    public void addKill(Player player, EntityType type) {
        if (player == null || type == null) return;

        int slot = getBestiarySlot(player);
        if (slot == -1) return;

        ItemStack book = (slot == 40) ? player.getInventory().getItemInOffHand() : player.getInventory().getItem(slot);
        if (book == null) return;

        ItemMeta meta = book.getItemMeta();
        if (meta == null) return;

        PersistentDataContainer pdc = meta.getPersistentDataContainer();
        NamespacedKey key = new NamespacedKey(plugin, "kills_" + type.name().toLowerCase());
        int currentKills = pdc.getOrDefault(key, PersistentDataType.INTEGER, 0);
        int newKills = currentKills + 1;

        StudiedMob studiedMob = StudiedMob.fromEntityType(type);
        int discoveredInFamilyBefore = (studiedMob != null)
                ? countDiscoveredSpeciesInFamily(book, studiedMob.getFamily())
                : 0;

        pdc.set(key, PersistentDataType.INTEGER, newKills);

        if (studiedMob != null) {
            boolean familyDiscovered = isFamilyDiscovered(book, studiedMob.getFamily());
            String mobName = getMobDisplayName(studiedMob);
            String familyName = getFamilyDisplayName(studiedMob.getFamily());
            String displaySub = familyDiscovered ? getMobSubtitle(studiedMob) : null;

            if (currentKills == 0 && player.isOnline()) {
                String msg = plugin.getConfig().getString(
                        "bestiary.messages.new_mob_discovered",
                        "&6[Бестиарий] &aВ журнал добавлена новая запись: &f«&e%mob_name%&f»&7!"
                ).replace("%mob_name%", mobName);
                player.sendMessage(Utils.color(msg));
                player.playSound(player.getLocation(), Sound.ITEM_BOOK_PAGE_TURN, 1.0f, 1.2f);
                player.playSound(player.getLocation(), Sound.ENTITY_EXPERIENCE_ORB_PICKUP, 0.8f, 1.5f);

                if (discoveredInFamilyBefore == 1) {
                    String kinshipMsg = plugin.getConfig().getString(
                            "bestiary.messages.family_discovered",
                            "&6[Бестиарий] &eНовое родство! &7Анатомия &f«&e%mob_name%&f» &7указывает на связь с &6%family_name%&7!"
                    ).replace("%mob_name%", mobName).replace("%family_name%", familyName);
                    player.sendMessage(Utils.color(kinshipMsg));
                    player.playSound(player.getLocation(), Sound.ENTITY_PLAYER_LEVELUP, 0.8f, 1.8f);
                }
            } else if (newKills == 5 && player.isOnline()) {
                String msg = plugin.getConfig().getString(
                        "bestiary.messages.stage_2_unlocked",
                        "&6[Бестиарий] &7Собраны полевые наброски о &f«&e%mob_name%&f» &a(5 убийств)&7!"
                ).replace("%mob_name%", mobName);
                player.sendMessage(Utils.color(msg));
                player.playSound(player.getLocation(), Sound.ITEM_BOOK_PAGE_TURN, 1.0f, 1.0f);
            } else if (newKills == 20 && player.isOnline()) {
                String msg = plugin.getConfig().getString(
                        "bestiary.messages.stage_3_unlocked",
                        "&6[Бестиарий] &7Изучены повадки и адаптация &f«&e%mob_name%&f» &a(20 убийств)&7!"
                ).replace("%mob_name%", mobName);
                player.sendMessage(Utils.color(msg));
                player.playSound(player.getLocation(), Sound.ITEM_BOOK_PAGE_TURN, 1.0f, 1.0f);
            } else if (newKills == 50 && player.isOnline()) {
                String msg = plugin.getConfig().getString(
                        "bestiary.messages.stage_4_unlocked",
                        "&6[Бестиарий] &7Анатомический анализ &f«&e%mob_name%&f» &aзавершен (50 убийств, шкала HP)&7!"
                ).replace("%mob_name%", mobName);
                player.sendMessage(Utils.color(msg));
                player.playSound(player.getLocation(), Sound.ITEM_BOOK_PAGE_TURN, 1.0f, 1.0f);
                player.playSound(player.getLocation(), Sound.ENTITY_PLAYER_LEVELUP, 0.8f, 1.4f);
            }

            List<Component> lore = new ArrayList<>();
            lore.add(Component.text("§7Физический журнал исследований."));
            lore.add(Component.text("§7Заполняется во время живой охоты."));
            lore.add(Component.text(" "));
            lore.add(Component.text("§6Последнее наблюдение:"));
            if (displaySub != null && !displaySub.isEmpty()) {
                lore.add(Component.text(" §f- §e" + mobName + "§7: §a" + newKills + " §7особей (" + displaySub + ")"));
            } else {
                lore.add(Component.text(" §f- §e" + mobName + "§7: §a" + newKills + " §7особей"));
            }
            lore.add(Component.text(" "));
            lore.add(Component.text("§e[ПКМ] §7- Открыть страницы бестиария."));
            lore.add(Component.text("§8Бонусы активны, пока книга в инвентаре."));
            meta.lore(lore);
        }

        if (meta instanceof BookMeta bookMeta) {
            bookMeta.setAuthor(player.getName());
        }

        book.setItemMeta(meta);

        if (slot == 40) {
            player.getInventory().setItemInOffHand(book);
        } else {
            player.getInventory().setItem(slot, book);
        }
    }

    public void addKill(Player player, ItemStack book, EntityType type) {
        addKill(player, type);
    }

    @EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
    public void onCreatureSpawn(CreatureSpawnEvent event) {
        if (event.getSpawnReason() == CreatureSpawnEvent.SpawnReason.SPAWNER) {
            LivingEntity entity = event.getEntity();
            entity.setMetadata("is_farm_mob", new FixedMetadataValue(plugin, true));
            entity.getPersistentDataContainer().set(farmMobKey, PersistentDataType.BYTE, (byte) 1);
        }
    }

    private boolean isFarmMob(LivingEntity entity) {
        if (entity == null) return false;
        if (entity.hasMetadata("is_farm_mob")) return true;
        return entity.getPersistentDataContainer().has(farmMobKey, PersistentDataType.BYTE);
    }

    @EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
    public void onEntityDeath(EntityDeathEvent event) {
        LivingEntity victim = event.getEntity();
        Player killer = victim.getKiller();
        if (killer == null) return;

        addKill(killer, victim.getType());
    }

    @EventHandler(priority = EventPriority.HIGH, ignoreCancelled = true)
    public void onEntityDamageByEntity(EntityDamageByEntityEvent event) {
        Player playerAttacker = getPlayerAttacker(event.getDamager());
        if (playerAttacker != null && event.getEntity() instanceof LivingEntity target && !(target instanceof Player)) {
            ItemStack book = getBestiaryFromInventory(playerAttacker);
            if (book != null) {
                StudiedMob studiedMob = StudiedMob.fromEntityType(target.getType());
                int kills = (studiedMob != null)
                        ? getFamilyKills(book, studiedMob.getFamily())
                        : getKills(book, target.getType());

                ResearchTier tier = getTier(kills);

                if (tier != ResearchTier.NONE) {
                    String mobName = studiedMob != null ? getMobDisplayName(studiedMob) : target.getName();

                    double healthRemaining = Math.max(0.0, target.getHealth() - event.getFinalDamage());
                    AttributeInstance maxHealthAttr = target.getAttribute(Attribute.MAX_HEALTH);
                    double maxHealth = (maxHealthAttr != null) ? maxHealthAttr.getValue() : 20.0;

                    String actionbarPattern = plugin.getConfig().getString("bestiary.messages.actionbar_hp", "&c[%mob_name%] &fHP: &e%current_hp%&7/&c%max_hp%");
                    String actionbarMsg = actionbarPattern.replace("%mob_name%", mobName)
                            .replace("%current_hp%", String.format(Locale.US, "%.1f", healthRemaining))
                            .replace("%max_hp%", String.format(Locale.US, "%.1f", maxHealth));
                    Utils.sendActionBar(playerAttacker, actionbarMsg);

                    double bonus = getTierBonusDamage(tier);
                    if (isFarmMob(target)) {
                        bonus = plugin.getConfig().getDouble("bestiary.anti_abuse.farm_mob_damage_bonus", 0.01);
                    }
                    event.setDamage(event.getDamage() * (1.0 + bonus));
                }
            }
        }

        if (event.getEntity() instanceof Player playerVictim) {
            LivingEntity mobAttacker = getLivingAttacker(event.getDamager());
            if (mobAttacker != null && !(mobAttacker instanceof Player)) {
                ItemStack book = getBestiaryFromInventory(playerVictim);
                if (book != null) {
                    StudiedMob studiedMob = StudiedMob.fromEntityType(mobAttacker.getType());
                    int kills = (studiedMob != null)
                            ? getFamilyKills(book, studiedMob.getFamily())
                            : getKills(book, mobAttacker.getType());

                    ResearchTier tier = getTier(kills);
                    double resistance = getTierResistance(tier);

                    if (resistance > 0.0) {
                        event.setDamage(event.getDamage() * (1.0 - resistance));
                    }
                }
            }
        }
    }

    private Player getPlayerAttacker(Entity damager) {
        if (damager instanceof Player p) return p;
        if (damager instanceof Projectile projectile) {
            ProjectileSource shooter = projectile.getShooter();
            if (shooter instanceof Player p) return p;
        }
        return null;
    }

    private LivingEntity getLivingAttacker(Entity damager) {
        if (damager instanceof LivingEntity l) return l;
        if (damager instanceof Projectile projectile) {
            ProjectileSource shooter = projectile.getShooter();
            if (shooter instanceof LivingEntity l) return l;
        }
        return null;
    }

    @EventHandler
    public void onPlayerInteract(PlayerInteractEvent event) {
        if (event.getHand() != EquipmentSlot.HAND) return;

        Action action = event.getAction();
        if (action != Action.RIGHT_CLICK_AIR && action != Action.RIGHT_CLICK_BLOCK) return;

        ItemStack item = event.getItem();
        if (isBestiaryBook(item)) {
            event.setCancelled(true);
            openBestiary(event.getPlayer(), item);
        }
    }

    public String getMobDisplayName(StudiedMob mob) {
        if (mob == null) return "Монстр";
        return plugin.getConfig().getString("bestiary.mobs." + mob.name() + ".name", mob.getName());
    }

    public String getMobSubtitle(StudiedMob mob) {
        if (mob == null) return "";
        return plugin.getConfig().getString("bestiary.mobs." + mob.name() + ".subtitle", mob.getSubTitle());
    }

    public String getFamilyDisplayName(MobFamily family) {
        if (family == null) return "Семейство";
        return plugin.getConfig().getString("bestiary.families." + family.name() + ".display_name", family.getDisplayName());
    }

    public String getFamilyShortName(MobFamily family) {
        if (family == null) return "Семейство";
        return plugin.getConfig().getString("bestiary.families." + family.name() + ".short_name", family.getShortName());
    }

    public String getMobStageHeader(int kills) {
        String stageKey = (kills < 5) ? "stage_1" : (kills < 20) ? "stage_2" : (kills < 50) ? "stage_3" : "stage_4";
        String fallback = (kills < 5) ? "Полевая запись (I):" : (kills < 20) ? "Полевые наброски (II):" : (kills < 50) ? "Повадки и среда (III):" : "Анатомический анализ (IV):";
        return plugin.getConfig().getString("bestiary.ui.stage_headers." + stageKey, fallback);
    }

    public String getMobStageContent(StudiedMob mob, int kills) {
        if (mob == null) return "Информация отсутствует.";
        String stageKey = (kills < 5) ? "stage_1" : (kills < 20) ? "stage_2" : (kills < 50) ? "stage_3" : "stage_4";
        return plugin.getConfig().getString("bestiary.mobs." + mob.name() + "." + stageKey, mob.getStageContent(kills));
    }

    public ResearchTier getTier(int kills) {
        int t3 = plugin.getConfig().getInt("bestiary.tiers.tier_3.required_kills", 500);
        int t2 = plugin.getConfig().getInt("bestiary.tiers.tier_2.required_kills", 200);
        int t1 = plugin.getConfig().getInt("bestiary.tiers.tier_1.required_kills", 50);

        if (kills >= t3) return ResearchTier.TIER_3;
        if (kills >= t2) return ResearchTier.TIER_2;
        if (kills >= t1) return ResearchTier.TIER_1;
        return ResearchTier.NONE;
    }

    public int getTierRequiredKills(ResearchTier tier) {
        if (tier == null || tier == ResearchTier.NONE) return 0;
        return plugin.getConfig().getInt("bestiary.tiers." + tier.name().toLowerCase() + ".required_kills", tier.getRequiredKills());
    }

    public double getTierBonusDamage(ResearchTier tier) {
        if (tier == null || tier == ResearchTier.NONE) return 0.0;
        return plugin.getConfig().getDouble("bestiary.tiers." + tier.name().toLowerCase() + ".bonus_damage", tier.getBonusDamage());
    }

    public double getTierResistance(ResearchTier tier) {
        if (tier == null || tier == ResearchTier.NONE) return 0.0;
        return plugin.getConfig().getDouble("bestiary.tiers." + tier.name().toLowerCase() + ".resistance", tier.getResistance());
    }

    public String getTierDisplayName(ResearchTier tier) {
        if (tier == null) return "Ранг 0";
        return plugin.getConfig().getString("bestiary.tiers." + tier.name().toLowerCase() + ".display_name", tier.getDisplayName());
    }

    public String getTierShortBadge(ResearchTier tier) {
        if (tier == null) return "Ранг 0";
        return plugin.getConfig().getString("bestiary.tiers." + tier.name().toLowerCase() + ".short_badge", tier.getShortBadge());
    }

    public static List<String> wrapText(String text, int maxCharsPerLine) {
        List<String> lines = new ArrayList<>();
        if (text == null || text.trim().isEmpty()) return lines;

        String[] paragraphs = text.split("\n");
        for (String paragraph : paragraphs) {
            if (paragraph.isEmpty()) {
                lines.add("");
                continue;
            }
            String[] words = paragraph.split(" ");
            StringBuilder currentLine = new StringBuilder();
            for (String word : words) {
                if (currentLine.length() + word.length() + (currentLine.length() > 0 ? 1 : 0) <= maxCharsPerLine) {
                    if (currentLine.length() > 0) currentLine.append(" ");
                    currentLine.append(word);
                } else {
                    if (currentLine.length() > 0) {
                        lines.add(currentLine.toString());
                        currentLine = new StringBuilder();
                    }
                    while (word.length() > maxCharsPerLine) {
                        lines.add(word.substring(0, maxCharsPerLine));
                        word = word.substring(maxCharsPerLine);
                    }
                    currentLine.append(word);
                }
            }
            if (currentLine.length() > 0) {
                lines.add(currentLine.toString());
            }
        }
        return lines;
    }

    private Component createFooterLine(ResearchTier tier, int speciesKills, Component hoverTooltip) {
        TextComponent.Builder footerLine = Component.text();
        if (tier == ResearchTier.NONE) {
            if (speciesKills >= 50) {
                footerLine.append(Component.text("✔ Анатомия изучена\n", NamedTextColor.DARK_GREEN, TextDecoration.BOLD));
                footerLine.append(Component.text("[⭐ Наведите для баффов]", NamedTextColor.DARK_BLUE, TextDecoration.UNDERLINED));
            } else {
                footerLine.append(Component.text("[⭐ Наведите для бонусов]", NamedTextColor.DARK_BLUE, TextDecoration.UNDERLINED));
            }
        } else {
            int dmgPct = (int) Math.round(getTierBonusDamage(tier) * 100);
            footerLine.append(Component.text("⭐ Бонус: ", NamedTextColor.DARK_GREEN, TextDecoration.BOLD))
                    .append(Component.text("+" + dmgPct + "% ур. ", NamedTextColor.DARK_GREEN, TextDecoration.BOLD));
            double res = getTierResistance(tier);
            if (res > 0.0) {
                int resPct = (int) Math.round(res * 100);
                footerLine.append(Component.text("+" + resPct + "% защ.", NamedTextColor.DARK_GREEN));
            }
            footerLine.append(Component.text("\n[Наведите для деталей]", NamedTextColor.DARK_GRAY, TextDecoration.ITALIC));
        }
        footerLine.hoverEvent(HoverEvent.showText(hoverTooltip));
        return footerLine.build();
    }

    public Component createBuffsHoverComponent(StudiedMob mob, int speciesKills, int familyKills) {
        MobFamily family = mob.getFamily();
        ResearchTier tier = getTier(familyKills);
        ResearchTier nextTier = tier.getNextTier();
        String familyName = getFamilyDisplayName(family);

        int currentStage = (speciesKills < 5) ? 1 : (speciesKills < 20) ? 2 : (speciesKills < 50) ? 3 : 4;

        TextComponent.Builder hover = Component.text();
        hover.append(Component.text("⭐ ", NamedTextColor.GOLD, TextDecoration.BOLD))
                .append(Component.text("БОЕВЫЕ БАФФЫ БЕСТИАРИЯ\n", NamedTextColor.YELLOW, TextDecoration.BOLD));
        hover.append(Component.text("Семейство: ", NamedTextColor.GRAY))
                .append(Component.text(familyName + "\n", NamedTextColor.GOLD, TextDecoration.BOLD));
        hover.append(Component.text("────────────────────────\n", NamedTextColor.DARK_GRAY));
        hover.append(Component.text("• Убито этого вида: ", NamedTextColor.GRAY))
                .append(Component.text(speciesKills + " особей\n", NamedTextColor.WHITE, TextDecoration.BOLD));
        hover.append(Component.text("• Прогресс семейства: ", NamedTextColor.GRAY))
                .append(Component.text(familyKills + " особей\n", NamedTextColor.AQUA, TextDecoration.BOLD));
        hover.append(Component.text("• Текущая запись: ", NamedTextColor.GRAY))
                .append(Component.text("Запись " + currentStage + "\n\n", NamedTextColor.GOLD, TextDecoration.BOLD));

        hover.append(Component.text("АКТИВНЫЕ БОНУСЫ:\n", NamedTextColor.GREEN, TextDecoration.BOLD));
        if (tier == ResearchTier.NONE) {
            int reqT1 = getTierRequiredKills(ResearchTier.TIER_1);
            int reqT2 = getTierRequiredKills(ResearchTier.TIER_2);
            hover.append(Component.text(" ✖ Бонус к урону: ", NamedTextColor.DARK_GRAY))
                    .append(Component.text("не активен (нужно " + reqT1 + ")\n", NamedTextColor.RED));
            hover.append(Component.text(" ✖ Защита от атак: ", NamedTextColor.DARK_GRAY))
                    .append(Component.text("не активна (нужно " + reqT2 + ")\n", NamedTextColor.RED));
            hover.append(Component.text(" ✖ Шкала HP: ", NamedTextColor.DARK_GRAY))
                    .append(Component.text("скрыта (нужно 50)\n", NamedTextColor.RED));
        } else {
            int dmgPct = (int) Math.round(getTierBonusDamage(tier) * 100);
            double res = getTierResistance(tier);
            hover.append(Component.text(" ✔ Бонус к урону: ", NamedTextColor.DARK_GREEN))
                    .append(Component.text("+" + dmgPct + "% по семейству\n", NamedTextColor.GREEN, TextDecoration.BOLD));
            if (res > 0.0) {
                int resPct = (int) Math.round(res * 100);
                hover.append(Component.text(" ✔ Защита от атак: ", NamedTextColor.DARK_GREEN))
                        .append(Component.text("+" + resPct + "% от атак\n", NamedTextColor.GREEN, TextDecoration.BOLD));
            } else {
                hover.append(Component.text(" ✖ Защита от атак: ", NamedTextColor.DARK_GRAY))
                        .append(Component.text("нет (доступна со 2-й записи)\n", NamedTextColor.GRAY));
            }
            hover.append(Component.text(" ✔ Шкала здоровья (HP): ", NamedTextColor.DARK_GREEN))
                    .append(Component.text("Отображается в бою\n", NamedTextColor.AQUA));
        }

        hover.append(Component.text("────────────────────────\n", NamedTextColor.DARK_GRAY));
        if (nextTier != null) {
            int nextReq = getTierRequiredKills(nextTier);
            int left = Math.max(0, nextReq - familyKills);
            hover.append(Component.text("До след. записи (", NamedTextColor.GRAY))
                    .append(Component.text(getTierShortBadge(nextTier), NamedTextColor.YELLOW))
                    .append(Component.text("): ", NamedTextColor.GRAY))
                    .append(Component.text(left + " убийств\n", NamedTextColor.GOLD, TextDecoration.BOLD));
            hover.append(Component.text(" Следующие бонусы: ", NamedTextColor.GRAY))
                    .append(Component.text("+" + (int) Math.round(getTierBonusDamage(nextTier) * 100) + "% ур.", NamedTextColor.WHITE));
            if (getTierResistance(nextTier) > 0.0) {
                hover.append(Component.text(", +" + (int) Math.round(getTierResistance(nextTier) * 100) + "% защ.", NamedTextColor.WHITE));
            }
            hover.append(Component.text("\n", NamedTextColor.GRAY));
        } else {
            hover.append(Component.text("👑 Максимальный уровень изучения!\n", NamedTextColor.GOLD, TextDecoration.BOLD));
        }
        hover.append(Component.text("────────────────────────\n", NamedTextColor.DARK_GRAY));
        hover.append(Component.text("Бонусы активны, пока книга в инвентаре", NamedTextColor.DARK_AQUA, TextDecoration.ITALIC));

        return hover.build();
    }

    public void openBestiary(Player player, ItemStack realBook) {
        if (player == null || realBook == null) return;

        ItemMeta rawMeta = realBook.getItemMeta();
        if (rawMeta instanceof BookMeta bookMeta) {
            if (bookMeta.getAuthor() == null || !bookMeta.getAuthor().equals(player.getName())) {
                bookMeta.setAuthor(player.getName());
                realBook.setItemMeta(bookMeta);
            }
        }

        List<Component> pages = new ArrayList<>();
        int discoveredSpeciesCount = countDiscoveredSpecies(realBook);
        int totalKills = 0;

        for (StudiedMob mob : StudiedMob.values()) {
            totalKills += getKills(realBook, mob.getType());
        }

        String titleHeader = plugin.getConfig().getString("bestiary.ui.title_page_header", "ПОЛЕВОЙ БЕСТИАРИЙ");
        String authorLabel = plugin.getConfig().getString("bestiary.ui.title_page_author_label", "Автор: ");
        String titleSubtitle = plugin.getConfig().getString("bestiary.ui.title_page_subtitle", "Журнал полевых заметок");
        String separator = plugin.getConfig().getString("bestiary.ui.separator", "───────────────");

        // 1. Титульный лист
        Component titlePage = Component.text()
                .append(Component.text(titleHeader + "\n", NamedTextColor.DARK_RED, TextDecoration.BOLD))
                .append(Component.text(authorLabel, NamedTextColor.DARK_RED, TextDecoration.BOLD))
                .append(Component.text(player.getName() + "\n", NamedTextColor.DARK_BLUE, TextDecoration.BOLD))
                .append(Component.text(titleSubtitle + "\n", NamedTextColor.DARK_GRAY, TextDecoration.ITALIC))
                .append(Component.text(separator + "\n", NamedTextColor.DARK_GRAY))
                .append(Component.text("Открыто видов: ", NamedTextColor.BLACK))
                .append(Component.text(discoveredSpeciesCount + "/" + StudiedMob.values().length + "\n", NamedTextColor.DARK_GREEN, TextDecoration.BOLD))
                .append(Component.text("Всего убито: ", NamedTextColor.BLACK))
                .append(Component.text(totalKills + " особей\n", NamedTextColor.GOLD, TextDecoration.BOLD))
                .append(Component.text(separator + "\n", NamedTextColor.DARK_GRAY))
                .append(Component.text("• Бонусы на семейство\n", NamedTextColor.DARK_BLUE))
                .append(Component.text("• Держите книгу при себе\n", NamedTextColor.DARK_BLUE))
                .append(Component.text("• Наведите на текст для бонусов\n", NamedTextColor.DARK_GREEN, TextDecoration.ITALIC)
                        .hoverEvent(HoverEvent.showText(Component.text("⭐ Наводите на статусы мобов для просмотра подробных боевых баффов!", NamedTextColor.GOLD))))
                .build();
        pages.add(titlePage);

        // 2. Если пока не убито ни одного моба — страница-подсказка
        if (discoveredSpeciesCount == 0) {
            String emptyTitle = plugin.getConfig().getString("bestiary.ui.empty_page_title", "СТРАНИЦЫ ПУСТЫ");
            List<String> emptyLines = plugin.getConfig().getStringList("bestiary.ui.empty_page_lines");
            if (emptyLines.isEmpty()) {
                emptyLines = List.of(
                        "Вы пока не исследовали ни одного монстра.",
                        "",
                        "Держите этот журнал при себе во время охоты.",
                        "",
                        "Каждый поверженный вид откроет новую страницу с повадками и бонусами урона."
                );
            }

            TextComponent.Builder emptyBuilder = Component.text()
                    .append(Component.text(emptyTitle + "\n", NamedTextColor.DARK_RED, TextDecoration.BOLD))
                    .append(Component.text(separator + "\n", NamedTextColor.DARK_GRAY));

            for (String line : emptyLines) {
                if (line.isEmpty()) {
                    emptyBuilder.append(Component.text("\n"));
                } else {
                    emptyBuilder.append(Component.text(line + "\n", NamedTextColor.BLACK));
                }
            }
            pages.add(emptyBuilder.build());
        } else {
            // 3. Страницы только исследованных мобов
            for (StudiedMob mob : StudiedMob.values()) {
                int speciesKills = getKills(realBook, mob.getType());
                if (speciesKills <= 0) {
                    continue;
                }

                int familyKills = getFamilyKills(realBook, mob.getFamily());
                boolean familyDiscovered = isFamilyDiscovered(realBook, mob.getFamily());

                String mobName = getMobDisplayName(mob);
                String familyShort = getFamilyShortName(mob.getFamily());
                String subTitle = familyDiscovered ? getMobSubtitle(mob) : null;
                ResearchTier tier = getTier(familyKills);

                int stageNumber = (speciesKills < 5) ? 1 : (speciesKills < 20) ? 2 : (speciesKills < 50) ? 3 : 4;
                String recordBadge = plugin.getConfig().getString("bestiary.ui.record_badge", "Запись %number%").replace("%number%", String.valueOf(stageNumber));

                Component hoverTooltip = createBuffsHoverComponent(mob, speciesKills, familyKills);

                String stageTitle = getMobStageHeader(speciesKills);
                String stageText = getMobStageContent(mob, speciesKills);

                List<String> contentLines = wrapText(stageText, 19);

                List<List<String>> pagesContent = new ArrayList<>();
                if (contentLines.size() <= 5) {
                    pagesContent.add(new ArrayList<>(contentLines));
                } else {
                    int index = 0;
                    List<String> firstPage = new ArrayList<>();
                    for (int i = 0; i < 5 && index < contentLines.size(); i++, index++) {
                        firstPage.add(contentLines.get(index));
                    }
                    pagesContent.add(firstPage);

                    while (index < contentLines.size()) {
                        List<String> continuation = new ArrayList<>();
                        for (int i = 0; i < 6 && index < contentLines.size(); i++, index++) {
                            continuation.add(contentLines.get(index));
                        }
                        if (!continuation.isEmpty()) {
                            pagesContent.add(continuation);
                        }
                    }
                }

                int totalMobPages = pagesContent.size();
                for (int p = 0; p < totalMobPages; p++) {
                    int pageNum = p + 1;
                    List<String> lines = pagesContent.get(p);
                    TextComponent.Builder pageBuilder = Component.text();

                    if (pageNum == 1) {
                        TextComponent.Builder titleLine = Component.text()
                                .append(Component.text(mobName, NamedTextColor.DARK_BLUE, TextDecoration.BOLD));
                        if (familyDiscovered) {
                            titleLine.append(Component.text(" (" + familyShort + ")", NamedTextColor.DARK_GRAY));
                        }
                        if (totalMobPages > 1) {
                            titleLine.append(Component.text(" [" + pageNum + "/" + totalMobPages + "]", NamedTextColor.DARK_GRAY));
                        }
                        titleLine.append(Component.text("\n"));
                        titleLine.hoverEvent(HoverEvent.showText(hoverTooltip));
                        pageBuilder.append(titleLine);

                        if (familyDiscovered && subTitle != null && !subTitle.isEmpty()) {
                            pageBuilder.append(Component.text(subTitle + "\n", NamedTextColor.DARK_GRAY, TextDecoration.ITALIC));
                        }

                        TextComponent.Builder statsLine = Component.text()
                                .append(Component.text("Убито: ", NamedTextColor.BLACK))
                                .append(Component.text(speciesKills + "", NamedTextColor.DARK_GREEN, TextDecoration.BOLD))
                                .append(Component.text(" | ", NamedTextColor.DARK_GRAY))
                                .append(Component.text(recordBadge, NamedTextColor.GOLD, TextDecoration.BOLD))
                                .append(Component.text(" [?]\n", NamedTextColor.DARK_AQUA, TextDecoration.BOLD));
                        statsLine.hoverEvent(HoverEvent.showText(hoverTooltip));
                        pageBuilder.append(statsLine);

                        pageBuilder.append(Component.text(separator + "\n", NamedTextColor.DARK_GRAY));
                        pageBuilder.append(Component.text(stageTitle + "\n", NamedTextColor.DARK_RED, TextDecoration.BOLD));

                        for (String l : lines) {
                            pageBuilder.append(Component.text(l + "\n", NamedTextColor.BLACK));
                        }

                        pageBuilder.append(Component.text(separator + "\n", NamedTextColor.DARK_GRAY));

                        if (totalMobPages > 1) {
                            Component continuationNotice = Component.text("[➡ Стр. 1/" + totalMobPages + " • Листайте]", NamedTextColor.DARK_BLUE, TextDecoration.ITALIC)
                                    .hoverEvent(HoverEvent.showText(hoverTooltip));
                            pageBuilder.append(continuationNotice);
                        } else {
                            Component footerLine = createFooterLine(tier, speciesKills, hoverTooltip);
                            pageBuilder.append(footerLine);
                        }
                    } else {
                        TextComponent.Builder continuationHeader = Component.text()
                                .append(Component.text(mobName, NamedTextColor.DARK_BLUE, TextDecoration.BOLD));
                        if (familyDiscovered) {
                            continuationHeader.append(Component.text(" (" + familyShort + ")", NamedTextColor.DARK_GRAY));
                        }
                        continuationHeader.append(Component.text(" [" + pageNum + "/" + totalMobPages + "]\n", NamedTextColor.DARK_GRAY));
                        continuationHeader.hoverEvent(HoverEvent.showText(hoverTooltip));
                        pageBuilder.append(continuationHeader);

                        pageBuilder.append(Component.text(separator + "\n", NamedTextColor.DARK_GRAY));

                        for (String l : lines) {
                            pageBuilder.append(Component.text(l + "\n", NamedTextColor.BLACK));
                        }

                        pageBuilder.append(Component.text(separator + "\n", NamedTextColor.DARK_GRAY));

                        Component footerLine = createFooterLine(tier, speciesKills, hoverTooltip);
                        pageBuilder.append(footerLine);
                    }

                    pages.add(pageBuilder.build());
                }
            }
        }

        Book virtualBook = Book.book(
                Component.text(plugin.getConfig().getString("bestiary.book.title", "Полевой Бестиарий")),
                Component.text(player.getName()),
                pages
        );

        player.openBook(virtualBook);
        player.playSound(player.getLocation(), Sound.ITEM_BOOK_PAGE_TURN, 1.0f, 1.0f);
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!sender.hasPermission("loveadaptation.admin")) {
            sender.sendMessage(Utils.color("&cУ вас нет прав."));
            return true;
        }

        if (args.length >= 1 && args[0].equalsIgnoreCase("give")) {
            Player target = null;
            if (args.length >= 2) {
                target = plugin.getServer().getPlayer(args[1]);
            } else if (sender instanceof Player p) {
                target = p;
            }

            if (target == null) {
                sender.sendMessage(Utils.color("&cИгрок не найден."));
                return true;
            }

            target.getInventory().addItem(createBestiaryBook(target));
            target.sendMessage(Utils.color("&aВам выдан Полевой Бестиарий!"));
            target.playSound(target.getLocation(), Sound.ENTITY_ITEM_PICKUP, 1.0f, 1.0f);

            if (!target.equals(sender)) {
                sender.sendMessage(Utils.color("&aВы выдали Полевой Бестиарий игроку &f" + target.getName()));
            }
            return true;
        }

        sender.sendMessage(Utils.color("&6Использование: /" + label + " give [игрок]"));
        return true;
    }
}
