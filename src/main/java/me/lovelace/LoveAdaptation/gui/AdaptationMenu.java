package me.lovelace.LoveAdaptation.gui;

import me.lovelace.LoveAdaptation.LoveAdaptation;
import me.lovelace.LoveAdaptation.config.GuiConfig;
import me.lovelace.LoveAdaptation.config.HeadsConfig;
import me.lovelace.LoveAdaptation.managers.AdaptationManager;
import me.lovelace.LoveAdaptation.managers.PluginManager;
import me.lovelace.LoveAdaptation.models.AdaptationData;
import me.lovelace.LoveAdaptation.models.AdaptationType;
import me.lovelace.LoveAdaptation.models.PlayerData;
import me.lovelace.LoveAdaptation.textures.HeadTextures;
import me.lovelace.LoveAdaptation.utils.Utils;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.SkullMeta;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * Нативное GUI-меню адаптаций (/loveadaptation menu). Раскладка слотов фиксирована в коде,
 * текст и текстуры служебных кнопок настраиваются через gui.yml/heads.yml, текст и текстуры
 * самих адаптаций — через config.yml (единый источник и для чата, и для меню, и для PAPI).
 */
public final class AdaptationMenu {

    public static final int SIZE = 54;
    public static final int PLAYER_HEAD_SLOT = 0;
    public static final int INFO_SLOT = 51;
    public static final int CLOSE_SLOT = 53;

    private static final int[] BORDER_SLOTS = {
        1, 2, 3, 4, 5, 6, 7, 8,
        9, 10, 11, 12, 13, 14, 15, 16, 17,
        18, 26, 27, 35, 36, 44,
        45, 46, 47, 48, 49, 50, 52
    };

    private static final Map<Integer, AdaptationType> ADAPTATION_SLOTS = new LinkedHashMap<>();

    static {
        int slot = 19;
        for (AdaptationType type : AdaptationType.values()) {
            if (type == AdaptationType.BASE) continue;
            ADAPTATION_SLOTS.put(slot++, type);
        }
    }

    private final LoveAdaptation plugin;
    private final GuiConfig guiConfig;

    public AdaptationMenu(LoveAdaptation plugin, GuiConfig guiConfig) {
        this.plugin = plugin;
        this.guiConfig = guiConfig;
    }

    public static AdaptationType adaptationAt(int slot) {
        return ADAPTATION_SLOTS.get(slot);
    }

    public void open(Player player) {
        AdaptationMenuHolder holder = new AdaptationMenuHolder();
        Inventory inventory = Bukkit.createInventory(holder, SIZE,
                Utils.color(guiConfig.getString("menu.title", "&6Адаптации персонажа")));
        holder.setInventory(inventory);

        ItemStack border = new ItemStack(Material.GRAY_STAINED_GLASS_PANE);
        ItemMeta borderMeta = border.getItemMeta();
        if (borderMeta != null) {
            borderMeta.setDisplayName(" ");
            border.setItemMeta(borderMeta);
        }
        for (int slot : BORDER_SLOTS) {
            inventory.setItem(slot, border);
        }

        inventory.setItem(PLAYER_HEAD_SLOT, buildPlayerHead(player));
        for (Map.Entry<Integer, AdaptationType> entry : ADAPTATION_SLOTS.entrySet()) {
            inventory.setItem(entry.getKey(), buildAdaptationItem(player, entry.getValue()));
        }
        inventory.setItem(INFO_SLOT, Utils.createCustomHead(
                HeadsConfig.get("info", HeadTextures.BASE_INFO_FALLBACK),
                guiConfig.getString("items.info.name", "&bИнформация"),
                guiConfig.getStringList("items.info.lore")));
        inventory.setItem(CLOSE_SLOT, Utils.createCustomHead(
                HeadsConfig.get("close", HeadTextures.BASE_CLOSE_FALLBACK),
                guiConfig.getString("items.close.name", "&cЗакрыть"),
                guiConfig.getStringList("items.close.lore")));

        player.openInventory(inventory);
    }

    private ItemStack buildPlayerHead(Player player) {
        AdaptationManager manager = PluginManager.getInstance().getAdaptationManager();
        PlayerData data = manager.getPlayerData(player.getUniqueId());
        AdaptationType current = data != null ? data.getCurrentAdaptation() : AdaptationType.BASE;
        String name = manager.getAdaptationDisplayName(current);
        String color = manager.getAdaptationColor(current);
        AdaptationData currentData = data != null ? data.getAdaptationData(current) : null;
        double percent = currentData != null ? currentData.getProgressPercent() : 0.0;

        List<String> lore = new ArrayList<>();
        for (String line : guiConfig.getStringList("items.player_head.lore")) {
            lore.add(line
                    .replace("%color%", color)
                    .replace("%name%", name)
                    .replace("%progress%", String.format("%.1f%%", percent)));
        }

        ItemStack head = new ItemStack(Material.PLAYER_HEAD);
        SkullMeta meta = (SkullMeta) head.getItemMeta();
        if (meta != null) {
            meta.setOwningPlayer(player);
            meta.setDisplayName(Utils.color("&f" + player.getName()));
            meta.setLore(Utils.color(lore));
            head.setItemMeta(meta);
        }
        return head;
    }

    private ItemStack buildAdaptationItem(Player player, AdaptationType type) {
        AdaptationManager manager = PluginManager.getInstance().getAdaptationManager();
        PlayerData data = manager.getPlayerData(player.getUniqueId());

        String name = manager.getAdaptationDisplayName(type);
        String color = manager.getAdaptationColor(type);
        String description = manager.getAdaptationDescription(type);
        String texture = manager.getHeadTexture(type);

        AdaptationData adaptData = data != null ? data.getAdaptationData(type) : null;
        double percent = adaptData != null ? adaptData.getProgressPercent() : 0.0;
        boolean isCurrent = data != null && data.getCurrentAdaptation() == type;

        String status;
        if (isCurrent) {
            status = percent >= 90.0 ? "&a✓ Активна &7(&e⭐ Мастерство&7)" : "&a✓ Активна";
        } else if (percent >= 90.0) {
            status = "&e⭐ Мастерство";
        } else if (percent > 0) {
            status = "&e⏳ Прокачка";
        } else {
            status = "&7🔐 Не разблокирована";
        }

        List<String> lore = new ArrayList<>();
        lore.add("");
        if (!description.isEmpty()) {
            lore.add("&7" + description);
            lore.add("");
        }
        lore.add("&7Прогресс: &f" + String.format("%.1f%%", percent));
        lore.add("&7Шкала: " + Utils.buildProgressBar(percent, 10));
        lore.add("&7Статус: " + status);

        return Utils.createCustomHead(texture, color + name, lore);
    }
}
