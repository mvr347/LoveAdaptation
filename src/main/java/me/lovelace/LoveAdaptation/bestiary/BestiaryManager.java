package me.lovelace.LoveAdaptation.bestiary;

import me.lovelace.LoveAdaptation.LoveAdaptation;
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
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.BookMeta;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;

import java.util.ArrayList;
import java.util.List;

/**
 * Менеджер Полевого Бестиария Охотника.
 * Управляет физическим артефактом, динамическим заполнением страниц по мере охоты,
 * раскрытием подвидов и родства семейств, расчетом бонусов и интерфейсом книги.
 */
public class BestiaryManager {

    private final LoveAdaptation plugin;
    private final NamespacedKey isBestiaryKey;
    private final NamespacedKey farmMobKey;

    public BestiaryManager(LoveAdaptation plugin) {
        this.plugin = plugin;
        this.isBestiaryKey = new NamespacedKey(plugin, "is_bestiary_book");
        this.farmMobKey = new NamespacedKey(plugin, "is_farm_mob");
    }

    public NamespacedKey getIsBestiaryKey() {
        return isBestiaryKey;
    }

    public NamespacedKey getFarmMobKey() {
        return farmMobKey;
    }

    // =========================================================================
    // КОНФИГУРАЦИОННЫЕ ХЕЛПЕРЫ (РАЗХАРДКОЖЕННЫЕ ТЕКСТЫ И БАФФЫ)
    // =========================================================================

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

    /**
     * Создает новый предмет «Полевой Бестиарий».
     */
    public ItemStack createBestiaryBook() {
        return createBestiaryBook((Player) null);
    }

    /**
     * Создает новый предмет «Полевой Бестиарий» для конкретного игрока.
     */
    public ItemStack createBestiaryBook(Player player) {
        ItemStack book = new ItemStack(Material.WRITTEN_BOOK);
        BookMeta meta = (BookMeta) book.getItemMeta();
        if (meta != null) {
            String title = plugin.getConfig().getString("bestiary.book.title", "Полевой Бестиарий");
            String authorDefault = plugin.getConfig().getString("bestiary.book.author_default", "Исследователь");
            String displayName = plugin.getConfig().getString("bestiary.book.display_name", "&6Полевой Бестиарий");

            meta.setTitle(title);
            meta.setAuthor(player != null ? player.getName() : authorDefault);
            meta.setGeneration(BookMeta.Generation.ORIGINAL);
            meta.displayName(Component.text(Utils.color(displayName)));
            meta.getPersistentDataContainer().set(isBestiaryKey, PersistentDataType.BYTE, (byte) 1);

            List<String> rawLore = plugin.getConfig().getStringList("bestiary.book.lore");
            if (rawLore.isEmpty()) {
                rawLore = List.of(
                        "&7Физический журнал исследований.",
                        "&7Заполняется во время живой охоты.",
                        " ",
                        "&e[ПКМ] &7- Открыть страницы бестиария.",
                        "&8Бонусы активны, пока книга в инвентаре."
                );
            }
            List<Component> lore = new ArrayList<>();
            for (String l : rawLore) {
                lore.add(Component.text(Utils.color(l)));
            }
            meta.lore(lore);

            meta.addPages(Component.text("§8[Пустой бестиарий]"));
            book.setItemMeta(meta);
        }
        return book;
    }

    /**
     * Проверяет, является ли предмет Полевым Бестиарием.
     */
    public boolean isBestiaryBook(ItemStack item) {
        if (item == null || item.getType() != Material.WRITTEN_BOOK) return false;
        ItemMeta meta = item.getItemMeta();
        if (meta == null) return false;
        return meta.getPersistentDataContainer().has(isBestiaryKey, PersistentDataType.BYTE);
    }

    /**
     * Находит слот Бестиария в инвентаре игрока.
     * Возвращает 0..35 для основного инвентаря, 40 для второй руки, либо -1 если книги нет.
     */
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

    /**
     * Находит Бестиарий в основном инвентаре или второй руке игрока.
     */
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

    /**
     * Получает количество убийств конкретного вида моба.
     */
    public int getKills(ItemStack book, EntityType type) {
        if (book == null || type == null) return 0;
        ItemMeta meta = book.getItemMeta();
        if (meta == null) return 0;
        NamespacedKey key = new NamespacedKey(plugin, "kills_" + type.name().toLowerCase());
        return meta.getPersistentDataContainer().getOrDefault(key, PersistentDataType.INTEGER, 0);
    }

    /**
     * Получает суммарное количество убийств для всего семейства монстров.
     */
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

    /**
     * Считает общее количество исследованных (убитых хотя бы раз) видов в бестиарии.
     */
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

    /**
     * Считает количество исследованных видов в конкретном семействе.
     */
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

    /**
     * Определяет, открыто ли семейство монстров охотником (только при убийстве другого вида из того же семейства).
     */
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

    /**
     * Добавляет убийство моба в Бестиарий игрока, обновляет данные в слоте инвентаря и отправляет уведомления.
     */
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

            // 1. Первое убийство нового вида
            if (currentKills == 0 && player.isOnline()) {
                String msg = plugin.getConfig().getString(
                        "bestiary.messages.new_mob_discovered",
                        "&6[Бестиарий] &aВ журнал добавлена новая запись: &f«&e%mob_name%&f»&7!"
                ).replace("%mob_name%", mobName);
                player.sendMessage(Utils.color(msg));
                player.playSound(player.getLocation(), Sound.ITEM_BOOK_PAGE_TURN, 1.0f, 1.2f);
                player.playSound(player.getLocation(), Sound.ENTITY_EXPERIENCE_ORB_PICKUP, 0.8f, 1.5f);

                // Если открыт второй вид семейства — обнаружено родство!
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

            // Обновление Lore книги
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

        // Синхронизируем слот инвентаря
        if (slot == 40) {
            player.getInventory().setItemInOffHand(book);
        } else {
            player.getInventory().setItem(slot, book);
        }
    }

    public void addKill(Player player, ItemStack book, EntityType type) {
        addKill(player, type);
    }

    /**
     * Создает интерактивный компонент подсказки (HoverEvent) со всеми бонусами и прогрессом моба/семейства.
     */
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

    // Vanilla written books wrap plain paragraph text at roughly 19 characters per line and
    // show about 14 lines per page at the default font. Bold text (titles, stat badges,
    // footer bonuses below) renders with noticeably wider glyphs in Minecraft's default font,
    // so the same character budget wraps a line earlier in the real client than it does here -
    // use a tighter budget for anything built with TextDecoration.BOLD so our own line count
    // matches what the player actually sees instead of under-counting it.
    private static final int WRAP_WIDTH = 19;
    private static final int BOLD_WRAP_WIDTH = 15;
    // One line of safety margin below the vanilla ~14-line page: wrapText() is only an
    // approximation of the client's real pixel-width wrapping, so the very last line of
    // visible space is never bet on that approximation being exact.
    private static final int MAX_LINES_PER_PAGE = 13;

    /**
     * Разбивает длинный текст на строки с учетом максимальной ширины строки в символах.
     */
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

    /**
     * Builds the physical line(s) for a page's mob title: name, family (once discovered) and,
     * on multi-page entries, the "[page/total]" marker. Long name+family combinations (e.g.
     * "Зомби-пиглин (Немертвые)") exceed a bold line's pixel width even though they fit under
     * wrapText()'s plain-text budget, so this falls back to a second line rather than silently
     * overflowing the page - see {@link #titleLineCount} for the matching budgeting counterpart.
     */
    private List<Component> buildTitleLines(String mobName, String familyShort, boolean familyDiscovered,
                                             int pageNum, int totalMobPages) {
        String familyToken = familyDiscovered ? "(" + familyShort + ")" : null;
        String markerToken = totalMobPages > 1 ? "[" + pageNum + "/" + totalMobPages + "]" : null;
        int oneLineLen = mobName.length()
                + (familyToken != null ? 1 + familyToken.length() : 0)
                + (markerToken != null ? 1 + markerToken.length() : 0);

        List<Component> lines = new ArrayList<>();
        TextComponent.Builder first = Component.text()
                .append(Component.text(mobName, NamedTextColor.DARK_BLUE, TextDecoration.BOLD));
        if (oneLineLen <= BOLD_WRAP_WIDTH) {
            if (familyToken != null) first.append(Component.text(" " + familyToken, NamedTextColor.DARK_GRAY));
            if (markerToken != null) first.append(Component.text(" " + markerToken, NamedTextColor.DARK_GRAY));
            lines.add(first.build());
        } else {
            lines.add(first.build());
            TextComponent.Builder second = Component.text();
            if (familyToken != null) second.append(Component.text(familyToken, NamedTextColor.DARK_GRAY));
            if (markerToken != null) {
                second.append(Component.text((familyToken != null ? " " : "") + markerToken, NamedTextColor.DARK_GRAY));
            }
            lines.add(second.build());
        }
        return lines;
    }

    /**
     * Line count {@link #buildTitleLines} would produce, without building any Components -
     * used to size the per-page content budget before we know exact page numbers. The exact
     * digits of the page marker never change whether it needs a second line in practice
     * (Bestiary entries never run long enough to need double-digit page counts), so a
     * representative "[1/2]" marker is used to decide the line count.
     */
    private int titleLineCount(String mobName, String familyShort, boolean familyDiscovered, boolean withMarker) {
        String familyToken = familyDiscovered ? "(" + familyShort + ")" : null;
        String markerToken = withMarker ? "[1/2]" : null;
        int oneLineLen = mobName.length()
                + (familyToken != null ? 1 + familyToken.length() : 0)
                + (markerToken != null ? 1 + markerToken.length() : 0);
        return oneLineLen <= BOLD_WRAP_WIDTH ? 1 : 2;
    }

    /**
     * Kill count has no upper bound, so "Убито: N | badge [?]" on one bold line eventually
     * overflows for any long-term hunter (3+ digit kill counts) even though the badge text
     * itself is short. Always split onto two fixed lines instead of gambling on it fitting.
     */
    private List<Component> buildStatsLines(int speciesKills, String recordBadge) {
        List<Component> lines = new ArrayList<>();
        lines.add(Component.text()
                .append(Component.text("Убито: ", NamedTextColor.BLACK))
                .append(Component.text(String.valueOf(speciesKills), NamedTextColor.DARK_GREEN, TextDecoration.BOLD))
                .build());
        lines.add(Component.text()
                .append(Component.text(recordBadge, NamedTextColor.GOLD, TextDecoration.BOLD))
                .append(Component.text(" [?]", NamedTextColor.DARK_AQUA, TextDecoration.BOLD))
                .build());
        return lines;
    }

    /**
     * Builds the buffs footer as a list of physical lines (1-3 depending on tier/resistance)
     * instead of a single Component with embedded "\n" - the old single-line "Бонус: +X% ур.
     * +Y% защ." combo silently exceeded a bold line's width at tier 2/3, and pagination needs
     * to know the real line count to size the content budget correctly.
     */
    private List<Component> buildFooterLines(ResearchTier tier, int speciesKills) {
        List<Component> lines = new ArrayList<>();
        if (tier == ResearchTier.NONE) {
            if (speciesKills >= 50) {
                lines.add(Component.text("✔ Анатомия изучена", NamedTextColor.DARK_GREEN, TextDecoration.BOLD));
            }
            lines.add(Component.text("[⭐ Наведите для бонусов]", NamedTextColor.DARK_BLUE, TextDecoration.UNDERLINED));
        } else {
            int dmgPct = (int) Math.round(getTierBonusDamage(tier) * 100);
            lines.add(Component.text()
                    .append(Component.text("⭐ Бонус: ", NamedTextColor.DARK_GREEN, TextDecoration.BOLD))
                    .append(Component.text("+" + dmgPct + "% ур.", NamedTextColor.DARK_GREEN, TextDecoration.BOLD))
                    .build());
            double res = getTierResistance(tier);
            if (res > 0.0) {
                int resPct = (int) Math.round(res * 100);
                lines.add(Component.text("+" + resPct + "% защ.", NamedTextColor.DARK_GREEN));
            }
            lines.add(Component.text("[Наведите для деталей]", NamedTextColor.DARK_GRAY, TextDecoration.ITALIC));
        }
        return lines;
    }

    private void appendLinesWithHover(TextComponent.Builder pageBuilder, List<Component> lines, Component hover) {
        for (Component line : lines) {
            pageBuilder.append(line.hoverEvent(HoverEvent.showText(hover))).append(Component.text("\n"));
        }
    }

    /**
     * Открывает виртуальный интерфейс чтения Бестиария для игрока.
     * Реализует многостраничный вывод для длинных заметок мобов с обязательным заголовком
     * моба на каждой странице, предотвращая появление пустых страниц.
     */
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
        // Bold text wraps earlier than plain text (see WRAP_WIDTH/BOLD_WRAP_WIDTH notes near
        // wrapText below): the default header "ПОЛЕВОЙ БЕСТИАРИЙ" (17 chars) and "Автор: " plus
        // a long player name (Minecraft allows up to 16 characters) both silently wrapped mid
        // word before this, so both are wrapped explicitly here instead.
        List<String> titleHeaderLines = wrapText(titleHeader, BOLD_WRAP_WIDTH);
        TextComponent.Builder titlePageBuilder = Component.text();
        for (String l : titleHeaderLines) {
            titlePageBuilder.append(Component.text(l + "\n", NamedTextColor.DARK_RED, TextDecoration.BOLD));
        }
        if (authorLabel.length() + player.getName().length() <= BOLD_WRAP_WIDTH) {
            titlePageBuilder.append(Component.text(authorLabel, NamedTextColor.DARK_RED, TextDecoration.BOLD))
                    .append(Component.text(player.getName() + "\n", NamedTextColor.DARK_BLUE, TextDecoration.BOLD));
        } else {
            titlePageBuilder.append(Component.text(authorLabel + "\n", NamedTextColor.DARK_RED, TextDecoration.BOLD))
                    .append(Component.text(player.getName() + "\n", NamedTextColor.DARK_BLUE, TextDecoration.BOLD));
        }
        Component titlePage = titlePageBuilder
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

                List<String> contentLines = wrapText(stageText, WRAP_WIDTH);
                List<String> subtitleLines = (familyDiscovered && subTitle != null && !subTitle.isEmpty())
                        ? wrapText(subTitle, WRAP_WIDTH) : List.of();
                List<String> stageTitleLines = wrapText(stageTitle, BOLD_WRAP_WIDTH);
                List<Component> statsLines = buildStatsLines(speciesKills, recordBadge);
                List<Component> footerLines = buildFooterLines(tier, speciesKills);

                // Dynamic pagination: measure every element's REAL wrapped line count (title,
                // subtitle, stats, stage header, footer) instead of assuming each is exactly
                // one line, then fit as much body content as actually remains on each page.
                // The old fixed "5 body lines on page 1 / 6 on continuations" budget silently
                // overflowed the moment the stage header wrapped to 2 lines (true for every
                // stage past the first) or the footer needed a resistance line (tier 2/3) -
                // that's what made long entries get visually cut off near the bottom.
                int singlePageHeader = titleLineCount(mobName, familyShort, familyDiscovered, false)
                        + subtitleLines.size() + statsLines.size() + 1 /* separator */ + stageTitleLines.size();
                int singlePageFooter = 1 /* separator */ + footerLines.size();
                int singlePageBudget = Math.max(1, MAX_LINES_PER_PAGE - singlePageHeader - singlePageFooter);

                List<List<String>> pagesContent = new ArrayList<>();
                if (contentLines.size() <= singlePageBudget) {
                    pagesContent.add(new ArrayList<>(contentLines));
                } else {
                    int firstHeader = titleLineCount(mobName, familyShort, familyDiscovered, true)
                            + subtitleLines.size() + statsLines.size() + 1 /* separator */ + stageTitleLines.size();
                    int firstFooter = 1 /* separator */ + 1 /* continuation notice */;
                    int firstBudget = Math.max(1, MAX_LINES_PER_PAGE - firstHeader - firstFooter);

                    int contHeader = titleLineCount(mobName, familyShort, familyDiscovered, true) + 1 /* separator */;
                    int contFooter = 1 /* separator */ + footerLines.size();
                    int contBudget = Math.max(1, MAX_LINES_PER_PAGE - contHeader - contFooter);

                    int index = 0;
                    List<String> firstPage = new ArrayList<>();
                    for (int i = 0; i < firstBudget && index < contentLines.size(); i++, index++) {
                        firstPage.add(contentLines.get(index));
                    }
                    pagesContent.add(firstPage);

                    while (index < contentLines.size()) {
                        List<String> continuation = new ArrayList<>();
                        for (int i = 0; i < contBudget && index < contentLines.size(); i++, index++) {
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
                    List<Component> titleLines = buildTitleLines(mobName, familyShort, familyDiscovered, pageNum, totalMobPages);

                    if (pageNum == 1) {
                        // Страница 1: Шапка с названием, семейством, подзаголовком, убийствами
                        appendLinesWithHover(pageBuilder, titleLines, hoverTooltip);

                        if (!subtitleLines.isEmpty()) {
                            for (String sl : subtitleLines) {
                                pageBuilder.append(Component.text(sl + "\n", NamedTextColor.DARK_GRAY, TextDecoration.ITALIC));
                            }
                        }

                        appendLinesWithHover(pageBuilder, statsLines, hoverTooltip);

                        pageBuilder.append(Component.text(separator + "\n", NamedTextColor.DARK_GRAY));
                        for (String stl : stageTitleLines) {
                            pageBuilder.append(Component.text(stl + "\n", NamedTextColor.DARK_RED, TextDecoration.BOLD));
                        }

                        for (String l : lines) {
                            pageBuilder.append(Component.text(l + "\n", NamedTextColor.BLACK));
                        }

                        pageBuilder.append(Component.text(separator + "\n", NamedTextColor.DARK_GRAY));

                        if (totalMobPages > 1) {
                            Component continuationNotice = Component.text("[➡ Стр. 1/" + totalMobPages + " • Листайте]", NamedTextColor.DARK_BLUE, TextDecoration.ITALIC)
                                    .hoverEvent(HoverEvent.showText(hoverTooltip));
                            pageBuilder.append(continuationNotice);
                        } else {
                            appendLinesWithHover(pageBuilder, footerLines, hoverTooltip);
                        }
                    } else {
                        // Страница продолжения: ОБЯЗАТЕЛЬНАЯ ВЕРХНЯЯ СТРОКА С ИМЕНЕМ МОБА
                        appendLinesWithHover(pageBuilder, titleLines, hoverTooltip);

                        pageBuilder.append(Component.text(separator + "\n", NamedTextColor.DARK_GRAY));

                        for (String l : lines) {
                            pageBuilder.append(Component.text(l + "\n", NamedTextColor.BLACK));
                        }

                        pageBuilder.append(Component.text(separator + "\n", NamedTextColor.DARK_GRAY));

                        appendLinesWithHover(pageBuilder, footerLines, hoverTooltip);
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
}

