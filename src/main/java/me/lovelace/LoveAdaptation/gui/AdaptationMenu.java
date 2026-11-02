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
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

/** Меню адаптаций: 45 слотов, фон ItemsAdder, адаптации 19–25, навигация 43/44. */
public final class AdaptationMenu {

    public static final int SIZE = 45;
    public static final int PLAYER_HEAD_SLOT = 0;
    public static final int INFO_SLOT = 42;
    public static final int BACK_SLOT = 43;
    public static final int CLOSE_SLOT = 44;
    public static final int NOTIFICATIONS_SLOT = 4;

    private static final String EMPTY_NAME = " ";
    private static final String FILLER_MATERIAL_DEFAULT = "GRAY_STAINED_GLASS_PANE";

    // Рамка Header/Footer по стандарту gui_gen v2.1: верхний ряд 1-8, Row1 9-17
    // и нижний футер 36-44 заполняются стеклом по умолчанию. Кнопки (уведомления,
    // инфо, назад, кастомные) заменяют стекло на своих слотах при рендере.
    // Рабочая зона 18-35 (в т.ч. стенки) стеклом не заливается.
    private static final int[] FRAME_GLASS_SLOTS = {
        0, 1, 2, 3, 4, 5, 6, 7, 8,
        9, 10, 11, 12, 13, 14, 15, 16, 17,
        36, 37, 38, 39, 40, 41, 42, 43, 44
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

    public boolean isMainMenuEnabled() {
        return guiConfig.getBoolean("menu.mainmenu", guiConfig.getBoolean("mainmenu", true));
    }

    public void open(Player player) {
        // A fresh normal menu forgets the Back command of an earlier mainmenu opening.
        returnCommands.remove(player.getUniqueId());
        open(player, false);
    }

    /**
     * Открывает меню со скрытым аргументом mainmenu (из хаб-меню сервера).
     * Каждая кнопка проверяет свой параметр mainmenu (true - только в mainmenu,
     * false - только в обычном режиме, не задано - в обоих).
     */
    public void openMainMenu(Player player) {
        openMainMenu(player, "");
    }

    /** Command each player's Back button runs, taken from the opening command; empty means the configured one. */
    private final java.util.Map<java.util.UUID, String> returnCommands = new java.util.concurrent.ConcurrentHashMap<>();

    /** {@code returnCommand} (for example "profile") overrides the configured Back command for this player. */
    public void openMainMenu(Player player, String returnCommand) {
        if (!isMainMenuEnabled() && (returnCommand == null || returnCommand.isBlank())) {
            open(player, false);
            return;
        }
        if (returnCommand == null || returnCommand.isBlank()) returnCommands.remove(player.getUniqueId());
        else returnCommands.put(player.getUniqueId(), returnCommand);
        open(player, true);
    }

    /**
     * items.back.command (новый ключ, часть конфигурируемых кнопок) переопределяет
     * gui.mainmenu_back_command (старый ключ в config.yml), если задан. Старый ключ продолжает
     * работать как есть - уже развёрнутые конфиги не ломаются, никаких действий от владельца
     * сервера не требуется.
     */
    private String resolveBackCommand(Player player) {
        String fromOpener = returnCommands.get(player.getUniqueId());
        if (fromOpener != null && !fromOpener.isBlank()) {
            return fromOpener;
        }
        String configured = guiConfig.getString("items.back.command", "");
        if (configured != null && !configured.isBlank()) {
            return configured;
        }
        return plugin.getConfig().getString("gui.mainmenu_back_command", "");
    }

    private void open(Player player, boolean isMainMenu) {
        AdaptationMenuHolder holder = new AdaptationMenuHolder();
        Inventory inventory = Bukkit.createInventory(holder, SIZE,
                me.lovelace.LoveAdaptation.integration.ItemsAdderTitles.render(
                        player,
                        guiConfig.getString("menu.title", "<white>%img_offset_-8%%img_adaptation_menu%"),
                        guiConfig.getString("menu.fallback_title", "<gold>Адаптации персонажа</gold>")));
        holder.setInventory(inventory);

        if (guiConfig.getBoolean("menu.frame", false)) {
            String material = guiConfig.getString("menu.filler_material", FILLER_MATERIAL_DEFAULT);
            ItemStack border = fillerItem(material);
            for (int slot : FRAME_GLASS_SLOTS) inventory.setItem(slot, border.clone());
        }

        Set<Integer> occupied = new HashSet<>();

        // Слот 0: голова игрока
        boolean playerHeadEnabled = guiConfig.getBoolean("items.player_head.enabled", true);
        Object playerHeadMainMenu = guiConfig.get("items.player_head.mainmenu");
        int playerHeadSlot = guiConfig.getInt("items.player_head.slot", PLAYER_HEAD_SLOT);
        if (playerHeadEnabled && shouldShowInMode(playerHeadMainMenu, null, isMainMenu) && isValidButtonSlot(playerHeadSlot)) {
            inventory.setItem(playerHeadSlot, buildPlayerHead(player));
            occupied.add(playerHeadSlot);
        }

        for (Map.Entry<Integer, AdaptationType> entry : ADAPTATION_SLOTS.entrySet()) {
            inventory.setItem(entry.getKey(), buildAdaptationItem(player, entry.getValue()));
        }
        occupied.addAll(ADAPTATION_SLOTS.keySet());

        int configuredCloseSlot = CLOSE_SLOT;
        occupied.add(configuredCloseSlot);

        String backCommand = resolveBackCommand(player);

        renderBackButton(inventory, holder, occupied, backCommand, player, isMainMenu);
        renderCloseButton(inventory, holder, configuredCloseSlot, player, isMainMenu);
        renderNotificationsButton(inventory, holder, occupied, player, isMainMenu);
        renderInfoButton(inventory, holder, occupied, player, isMainMenu);
        renderCustomButtons(inventory, holder, occupied, player, isMainMenu);

        player.openInventory(inventory);
    }

    /**
     * Notifications - кнопка управления меню (Вк, gui_gen v2.1 RULE 4) этого меню.
     * По умолчанию слот 4. Переключает per-player уведомления адаптаций.
     * Поддерживает mainmenu: true/false.
     */
    private void renderNotificationsButton(Inventory inventory, AdaptationMenuHolder holder, Set<Integer> occupied,
                                            Player viewer, boolean isMainMenu) {
        boolean enabledInConfig = guiConfig.getBoolean("items.notifications.enabled", true);
        if (!enabledInConfig) {
            return; // Слот остаётся стеклом
        }
        Object mainmenu = guiConfig.get("items.notifications.mainmenu");
        if (!shouldShowInMode(mainmenu, null, isMainMenu)) {
            return;
        }
        int slot = guiConfig.getInt("items.notifications.slot", NOTIFICATIONS_SLOT);
        if (!isValidButtonSlot(slot)) {
            plugin.getLogger().warning("gui.yml: items.notifications.slot=" + slot
                    + " вне диапазона инвентаря (0-" + (SIZE - 1) + ") - кнопка пропущена.");
            return;
        }
        if (!occupied.add(slot)) {
            plugin.getLogger().warning("gui.yml: items.notifications.slot=" + slot
                    + " уже занят другим элементом меню - кнопка уведомлений пропущена.");
            return;
        }

        PlayerData data = PluginManager.getInstance().getAdaptationManager().getPlayerData(viewer.getUniqueId());
        boolean notificationsOn = data == null || data.isNotificationsEnabled();
        String stateStr = notificationsOn ? "&aВКЛ" : "&cВЫКЛ";

        String defaultTex = HeadsConfig.get("notifications", HeadTextures.BASE_NOTIFICATIONS_FALLBACK);
        String texture = notificationsOn
                ? guiConfig.getString("items.notifications.texture_base64_on", guiConfig.getString("items.notifications.texture_base64", defaultTex))
                : guiConfig.getString("items.notifications.texture_base64_off", guiConfig.getString("items.notifications.texture_base64", defaultTex));

        String material = guiConfig.getString("items.notifications.material", "");
        String rawName = guiConfig.getString("items.notifications.name", "&eУведомления: %state%");
        String name = rawName.replace("%state%", stateStr);
        List<String> rawLore = guiConfig.getStringList("items.notifications.lore");
        if (rawLore.isEmpty()) {
            rawLore = List.of("", "&7Статус: %state%", "", "&7Титры, сообщения и звуки", "&7о прокачке и деградации адаптаций.", "", "&eЛКМ &7— переключить");
        }
        List<String> lore = new ArrayList<>();
        for (String line : rawLore) {
            lore.add(line.replace("%state%", stateStr));
        }

        ItemStack item = GuiItemBuilder.resolveIcon(texture, material, defaultTex, name, lore);
        inventory.setItem(slot, item);
        holder.registerAction(slot, () -> {
            PluginManager.getInstance().getAdaptationManager().toggleNotifications(viewer);
            Bukkit.getScheduler().runTask(plugin, () -> {
                if (viewer.isOnline()) {
                    open(viewer, isMainMenu);
                }
            });
        });
    }

    /**
     * Info - кнопка информации. По умолчанию слот 42 (footer-позиция "Д").
     * Поддерживает настраиваемый slot, mainmenu и опциональную команду command при клике.
     */
    private void renderInfoButton(Inventory inventory, AdaptationMenuHolder holder, Set<Integer> occupied,
                                   Player viewer, boolean isMainMenu) {
        boolean enabled = guiConfig.getBoolean("items.info.enabled", true);
        if (!enabled) {
            return; // слот остаётся стеклом
        }
        Object mainmenu = guiConfig.get("items.info.mainmenu");
        if (!shouldShowInMode(mainmenu, null, isMainMenu)) {
            return;
        }
        int slot = guiConfig.getInt("items.info.slot", INFO_SLOT);
        if (!isValidButtonSlot(slot)) {
            plugin.getLogger().warning("gui.yml: items.info.slot=" + slot
                    + " вне диапазона инвентаря (0-" + (SIZE - 1) + ") - кнопка Информация пропущена.");
            return;
        }
        if (!occupied.add(slot)) {
            if (guiConfig.contains("items.info.slot")) {
                plugin.getLogger().warning("gui.yml: items.info.slot=" + slot
                    + " уже занят другим элементом меню - кнопка Информация пропущена.");
            }
            return;
        }

        String texture = guiConfig.getString("items.info.texture_base64", "");
        String material = guiConfig.getString("items.info.material", "");
        String name = guiConfig.getString("items.info.name", "&bИнформация");
        List<String> lore = guiConfig.getStringList("items.info.lore");

        ItemStack item = GuiItemBuilder.resolveIcon(texture, material,
                HeadsConfig.get("info", HeadTextures.BASE_INFO_FALLBACK), name, lore);
        inventory.setItem(slot, item);

        String command = guiConfig.getString("items.info.command", "");
        if (command != null && !command.isBlank()) {
            holder.registerAction(slot, () -> dispatchConfiguredCommand(viewer, command));
        }
    }

    /**
     * Back - кнопка Назад (по умолчанию слот 43). По умолчанию mainmenu: true.
     */
    private void renderBackButton(Inventory inventory, AdaptationMenuHolder holder, Set<Integer> occupied,
                                   String backCommand, Player viewer, boolean isMainMenu) {
        if (backCommand == null || backCommand.isBlank()) return;
        boolean explicitParent = isMainMenu && returnCommands.containsKey(viewer.getUniqueId());
        boolean enabled = guiConfig.getBoolean("items.back.enabled", true);
        if (!enabled && !explicitParent) {
            return;
        }
        Object mainmenu = guiConfig.get("items.back.mainmenu");
        if (!explicitParent && !shouldShowInMode(mainmenu, Boolean.TRUE, isMainMenu)) {
            return;
        }
        int slot = BACK_SLOT;
        if (!isValidButtonSlot(slot)) {
            plugin.getLogger().warning("gui.yml: items.back.slot=" + slot
                    + " вне диапазона инвентаря (0-" + (SIZE - 1) + ") - кнопка Назад пропущена.");
            return;
        }
        if (!occupied.add(slot)) {
            plugin.getLogger().warning("gui.yml: items.back.slot=" + slot
                    + " уже занят другим элементом меню - кнопка Назад пропущена.");
            return;
        }

        String texture = guiConfig.getString("items.back.texture_base64", "");
        String material = guiConfig.getString("items.back.material", "");
        String name = guiConfig.getString("items.back.name", "&7Назад");
        List<String> lore = guiConfig.getStringList("items.back.lore");

        ItemStack item = GuiItemBuilder.resolveIcon(texture, material,
                HeadsConfig.get("back", HeadTextures.BASE_BACK_FALLBACK), name, lore);
        inventory.setItem(slot, item);

        String opener = returnCommands.get(viewer.getUniqueId());
        String command = opener != null && !opener.isBlank() ? opener : guiConfig.getString("items.back.command", "");
        if (command == null || command.isBlank()) {
            command = backCommand;
        }
        final String effectiveCommand = command;
        if (effectiveCommand != null && !effectiveCommand.isBlank()) {
            holder.registerAction(slot, () -> dispatchConfiguredCommand(viewer, effectiveCommand));
        } else {
            holder.registerAction(slot, viewer::closeInventory);
        }
    }

    /** Close - кнопка Закрыть (по умолчанию слот 44). Поддерживает mainmenu: true/false. */
    private void renderCloseButton(Inventory inventory, AdaptationMenuHolder holder, int slot,
                                    Player viewer, boolean isMainMenu) {
        String texture = guiConfig.getString("items.close.texture_base64", "");
        String material = guiConfig.getString("items.close.material", "");
        String name = guiConfig.getString("items.close.name", "&cЗакрыть");
        List<String> lore = guiConfig.getStringList("items.close.lore");

        ItemStack item = GuiItemBuilder.resolveIcon(texture, material,
                HeadsConfig.get("close", HeadTextures.BASE_CLOSE_FALLBACK), name, lore);
        inventory.setItem(slot, item);

        String command = guiConfig.getString("items.close.command", "");
        if (command != null && !command.isBlank()) {
            holder.registerAction(slot, () -> {
                dispatchConfiguredCommand(viewer, command);
            });
        } else {
            holder.registerAction(slot, viewer::closeInventory);
        }
    }

    /**
     * items.custom_buttons - произвольные дополнительные кнопки со своим слотом,
     * командой и параметром mainmenu (true - только в mainmenu, false - только в обычном, не задано - в обоих).
     */
    private void renderCustomButtons(Inventory inventory, AdaptationMenuHolder holder, Set<Integer> occupied,
                                      Player viewer, boolean isMainMenu) {
        List<Map<?, ?>> entries = guiConfig.getMapList("items.custom_buttons");
        for (Map<?, ?> entry : entries) {
            Object rawSlot = entry.get("slot");
            if (!(rawSlot instanceof Number slotNumber)) {
                plugin.getLogger().warning("gui.yml: items.custom_buttons запись без числового 'slot' "
                        + "(" + entry + ") - пропущена.");
                continue;
            }
            int slot = slotNumber.intValue();

            boolean enabled = !(entry.get("enabled") instanceof Boolean b) || b;
            if (!enabled) {
                continue; // выключена явно - не резервируем слот, не логируем (это не ошибка)
            }
            Object mainmenu = entry.get("mainmenu");
            if (!shouldShowInMode(mainmenu, null, isMainMenu)) {
                continue;
            }

            if (!isValidCustomButtonSlot(slot)) {
                plugin.getLogger().warning("gui.yml: items.custom_buttons слот " + slot
                        + " вне диапазона инвентаря (0-" + (SIZE - 1) + ") - кнопка пропущена.");
                continue;
            }
            if (!occupied.add(slot)) {
                plugin.getLogger().warning("gui.yml: items.custom_buttons слот " + slot
                        + " уже занят другой кнопкой этого меню - кнопка пропущена.");
                continue;
            }

            Object rawCommand = entry.get("command");
            String command = rawCommand != null ? String.valueOf(rawCommand) : "";
            if (command.isBlank()) {
                plugin.getLogger().warning("gui.yml: items.custom_buttons запись в слоте " + slot
                        + " без 'command' - кнопка пропущена.");
                occupied.remove(slot);
                continue;
            }

            String material = entry.get("material") != null ? String.valueOf(entry.get("material")) : "";
            String texture = entry.get("texture_base64") != null ? String.valueOf(entry.get("texture_base64")) : "";
            String name = entry.get("name") != null ? String.valueOf(entry.get("name")) : EMPTY_NAME;
            List<String> lore = toStringList(entry.get("lore"));

            ItemStack item = GuiItemBuilder.resolveIcon(texture, material, null, name, lore);
            inventory.setItem(slot, item);
            holder.registerAction(slot, () -> dispatchConfiguredCommand(viewer, command));
        }
    }

    /**
     * Проверяет, должна ли кнопка отображаться в текущем режиме открытия (обычный vs mainmenu).
     *
     * @param configuredMode Значение параметра mainmenu из конфига (Boolean, String "true"/"false"/"both", либо null)
     * @param defaultMode Режим по умолчанию для этой кнопки (true для Back, null/both для обычных кнопок)
     * @param isMainMenu true если меню открыто со скрытым аргументом mainmenu/главменю
     * @return true если кнопка должна быть отображена
     */
    private static boolean shouldShowInMode(Object configuredMode, Boolean defaultMode, boolean isMainMenu) {
        Boolean mode = defaultMode;
        if (configuredMode instanceof Boolean b) {
            mode = b;
        } else if (configuredMode instanceof String s) {
            if (s.equalsIgnoreCase("true") || s.equalsIgnoreCase("only") || s.equalsIgnoreCase("mainmenu")) {
                mode = Boolean.TRUE;
            } else if (s.equalsIgnoreCase("false") || s.equalsIgnoreCase("normal")) {
                mode = Boolean.FALSE;
            } else if (s.equalsIgnoreCase("both") || s.equalsIgnoreCase("all")) {
                mode = null;
            }
        }

        if (mode == null) {
            return true; // both modes
        }
        return mode ? isMainMenu : !isMainMenu;
    }

    /** Выполняет command как сам игрок (тот же паттерн, что и gui.mainmenu_back_command раньше) -
     * ведущий "/" отрезается, закрытие инвентаря и сам диспатч разнесены на следующий тик, чтобы не мешать
     * обработке текущего InventoryClickEvent. */
    private void dispatchConfiguredCommand(Player viewer, String rawCommand) {
        String substituted = rawCommand.replace("%player%", viewer.getName()).replace("{player}", viewer.getName());
        String command = substituted.startsWith("/") ? substituted.substring(1) : substituted;
        final String finalCommand = command;
        viewer.closeInventory();
        Bukkit.getScheduler().runTask(plugin, () -> {
            if (!viewer.isOnline()) {
                return;
            }
            boolean recognized = viewer.performCommand(finalCommand);
            if (!recognized) {
                plugin.getLogger().warning("AdaptationMenu: команда \"" + finalCommand + "\", настроенная для кнопки "
                        + "GUI, не распознана сервером для игрока " + viewer.getName() + " - проверьте gui.yml/"
                        + "config.yml на опечатку или отсутствующий плагин.");
            }
        });
    }

    private static boolean isValidControlSlot(int slot) {
        return slot >= 0 && slot < SIZE;
    }

    private static boolean isValidButtonSlot(int slot) {
        return slot >= 0 && slot < SIZE;
    }

    private static boolean isValidCustomButtonSlot(int slot) {
        return slot >= 0 && slot < SIZE;
    }

    private static List<String> toStringList(Object raw) {
        if (!(raw instanceof List<?> list)) {
            return List.of();
        }
        List<String> result = new ArrayList<>(list.size());
        for (Object line : list) {
            result.add(String.valueOf(line));
        }
        return result;
    }

    private ItemStack fillerItem(String materialName) {
        Material material = Material.matchMaterial(materialName);
        ItemStack item = new ItemStack(material != null ? material : Material.GRAY_STAINED_GLASS_PANE);
        ItemMeta meta = item.getItemMeta();
        if (meta != null) {
            meta.setDisplayName(EMPTY_NAME);
            item.setItemMeta(meta);
        }
        return item;
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
            String configuredName = guiConfig.getString("items.player_head.name", "&f%player%");
            meta.setDisplayName(Utils.color(configuredName.replace("%player%", player.getName())));
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
            status = percent >= 90.0 ? "&a✓ Активна &8(&eМастерство&8)" : "&a✓ Активна";
        } else if (percent >= 90.0) {
            status = "&eМастерство";
        } else if (percent > 0) {
            status = "&eВ процессе";
        } else {
            status = "&7Не разблокирована";
        }

        List<String> lore = new ArrayList<>();
        lore.add("");
        if (!description.isEmpty()) {
            lore.add("&7" + description);
            lore.add("");
        }
        lore.add("&8▪ &7Прогресс адаптации:");
        lore.add("  &7Текущий уровень: &f" + String.format("%.1f%%", percent));
        lore.add("  " + Utils.buildProgressBar(percent, 16));
        lore.add("  &7Статус: " + status);

        return Utils.createCustomHead(texture, color + name, lore);
    }
}
