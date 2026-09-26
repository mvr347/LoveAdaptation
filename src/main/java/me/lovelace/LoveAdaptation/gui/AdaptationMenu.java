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

/**
 * Нативное GUI-меню адаптаций (/loveadaptation menu). Раскладка — стандарт gui_gen v2.1
 * (54 слота): слот 0 — голова игрока, footer (45-53) — Info (позиция "Д", слот 51, опциональная
 * кнопка), Back (позиция 7, слот 52, опциональная) и Close (позиция 8, слот 53, всегда). Рабочая
 * зона 19-25 (ряд 1) занята типами адаптаций (контент, не настраивается по позиции — их набор и
 * порядок берутся из {@link AdaptationType}); ряды 28-34 и 37-43 рабочей зоны свободны для
 * {@code items.custom_buttons}.
 *
 * <p>С 2026-09-23 (owner request: довести это меню до той же степени настраиваемости, что и у
 * LoveBehaivor — "снимать/добавлять базовые кнопки, двигать их, добавлять свои со своими
 * текстурами и командами") Info и Back дополнительно поддерживают {@code enabled} в gui.yml, а
 * {@code items.custom_buttons} — список полностью новых кнопок с произвольным material/texture/
 * name/lore/command, размещаемых в любом легальном по gui_gen v2.1 слоте (зона управления 2-7,
 * незанятая ячейка рабочей зоны кроме боковых стенок, footer-позиции 6/7). Close остаётся всегда
 * на слоте 53 и не конфигурируется — gui_gen v2.1 требует его присутствия без исключений.</p>
 */
public final class AdaptationMenu {

    public static final int SIZE = 54;
    public static final int PLAYER_HEAD_SLOT = 0;
    public static final int INFO_SLOT = 51;
    public static final int BACK_SLOT = 52;
    public static final int CLOSE_SLOT = 53;
    public static final int NOTIFICATIONS_SLOT = 4;

    private static final String EMPTY_NAME = " ";
    private static final String FILLER_MATERIAL_DEFAULT = "GRAY_STAINED_GLASS_PANE";

    // Рамка Header/Footer по стандарту gui_gen v2.1: верхний ряд 1-8, Row1 9-17
    // и нижний футер 45-52 заполняются стеклом по умолчанию. Кнопки (уведомления,
    // инфо, назад, кастомные) заменяют стекло на своих слотах при рендере.
    // Рабочая зона 18-44 (в т.ч. стенки) стеклом не заливается.
    private static final int[] FRAME_GLASS_SLOTS = {
        1, 2, 3, 4, 5, 6, 7, 8,
        9, 10, 11, 12, 13, 14, 15, 16, 17,
        45, 46, 47, 48, 49, 50, 51, 52
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
        open(player, null);
    }

    /**
     * Открывает то же самое меню (/adaptation mainmenu), но с активной кнопкой "Назад" в
     * BACK_SLOT, если режим mainmenu включён и итоговая команда возврата не пустая
     * (см. {@link #resolveBackCommand()}) - иначе на месте кнопки остаётся стекло.
     */
    public void openMainMenu(Player player) {
        if (!isMainMenuEnabled()) {
            open(player);
            return;
        }
        String backCommand = resolveBackCommand();
        open(player, (backCommand == null || backCommand.isBlank()) ? null : backCommand);
    }

    /**
     * items.back.command (новый ключ, часть конфигурируемых кнопок) переопределяет
     * gui.mainmenu_back_command (старый ключ в config.yml), если задан. Старый ключ продолжает
     * работать как есть - уже развёрнутые конфиги не ломаются, никаких действий от владельца
     * сервера не требуется.
     */
    private String resolveBackCommand() {
        String configured = guiConfig.getString("items.back.command", "");
        if (configured != null && !configured.isBlank()) {
            return configured;
        }
        return plugin.getConfig().getString("gui.mainmenu_back_command", "");
    }

    private void open(Player player, String backCommand) {
        AdaptationMenuHolder holder = new AdaptationMenuHolder();
        Inventory inventory = Bukkit.createInventory(holder, SIZE,
                Utils.color(guiConfig.getString("menu.title", "&6Адаптации персонажа")));
        holder.setInventory(inventory);

        String fillerMaterialName = guiConfig.getString("menu.filler_material", FILLER_MATERIAL_DEFAULT);
        ItemStack border = fillerItem(fillerMaterialName);
        for (int slot : FRAME_GLASS_SLOTS) {
            inventory.setItem(slot, border);
        }

        inventory.setItem(PLAYER_HEAD_SLOT, buildPlayerHead(player));
        for (Map.Entry<Integer, AdaptationType> entry : ADAPTATION_SLOTS.entrySet()) {
            inventory.setItem(entry.getKey(), buildAdaptationItem(player, entry.getValue()));
        }

        // Слоты, уже занятые фиксированной раскладкой независимо от конфигурируемых кнопок ниже -
        // голова, все слоты адаптаций и Close.
        Set<Integer> occupied = new HashSet<>();
        occupied.add(PLAYER_HEAD_SLOT);
        int configuredCloseSlot = guiConfig.getInt("items.close.slot", CLOSE_SLOT);
        occupied.add(configuredCloseSlot);
        occupied.addAll(ADAPTATION_SLOTS.keySet());

        renderNotificationsButton(inventory, holder, occupied, backCommand, player);
        renderInfoButton(inventory, holder, occupied, player);
        renderBackButton(inventory, holder, occupied, backCommand, player);
        renderCloseButton(inventory, holder, configuredCloseSlot, player);
        renderCustomButtons(inventory, holder, occupied, player);

        player.openInventory(inventory);
    }

    /**
     * Notifications - кнопка управления меню (Вк, gui_gen v2.1 RULE 4) этого меню.
     * По умолчанию слот 4. Переключает per-player уведомления адаптаций.
     */
    private void renderNotificationsButton(Inventory inventory, AdaptationMenuHolder holder, Set<Integer> occupied,
                                            String backCommand, Player viewer) {
        boolean enabledInConfig = guiConfig.getBoolean("items.notifications.enabled", true);
        if (!enabledInConfig) {
            return; // Слот остаётся стеклом
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

        String texture = guiConfig.getString("items.notifications.texture_base64", "");
        String material = guiConfig.getString("items.notifications.material", notificationsOn ? "BELL" : "GRAY_DYE");
        String name = guiConfig.getString("items.notifications.name",
                notificationsOn ? "&aУведомления: &fВКЛ" : "&cУведомления: &fВЫКЛ");
        List<String> lore = guiConfig.getStringList("items.notifications.lore");
        if (lore.isEmpty()) {
            lore = List.of("", "&7Титры/сообщения/звуки", "&7о прокачке и деградации адаптаций.", "", "&aЛКМ &7- переключить");
        }

        ItemStack item = GuiItemBuilder.resolveIcon(texture, material, null, name, lore);
        inventory.setItem(slot, item);
        holder.registerAction(slot, () -> {
            PluginManager.getInstance().getAdaptationManager().toggleNotifications(viewer);
            Bukkit.getScheduler().runTask(plugin, () -> {
                if (viewer.isOnline()) {
                    open(viewer, backCommand);
                }
            });
        });
    }

    /**
     * Info - кнопка информации. По умолчанию слот 51 (footer-позиция "Д").
     * Поддерживает настраиваемый slot и опциональную команду command при клике.
     */
    private void renderInfoButton(Inventory inventory, AdaptationMenuHolder holder, Set<Integer> occupied, Player viewer) {
        boolean enabled = guiConfig.getBoolean("items.info.enabled", true);
        if (!enabled) {
            return; // слот остаётся стеклом
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
     * Back - кнопка Назад (по умолчанию слот 52). Активна только если mainmenu включён,
     * команда возврата не пустая и items.back.enabled != false.
     */
    private void renderBackButton(Inventory inventory, AdaptationMenuHolder holder, Set<Integer> occupied,
                                   String backCommand, Player viewer) {
        if (!isMainMenuEnabled() || backCommand == null || backCommand.isBlank()) {
            return;
        }
        boolean enabled = guiConfig.getBoolean("items.back.enabled", true);
        if (!enabled) {
            return;
        }
        int slot = guiConfig.getInt("items.back.slot", BACK_SLOT);
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
        holder.registerAction(slot, () -> dispatchConfiguredCommand(viewer, backCommand));
    }

    /** Close - кнопка Закрыть (по умолчанию слот 53). Всегда присутствует. */
    private void renderCloseButton(Inventory inventory, AdaptationMenuHolder holder, int slot, Player viewer) {
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
     * items.custom_buttons - произвольные дополнительные кнопки (owner request 2026-09-23: та же
     * степень настраиваемости, что и behavior_menu.custom_buttons у LoveBehaivor). Каждая запись
     * валидируется независимо: слот обязан попасть в зону, которую gui_gen v2.1 разрешает для
     * кнопок в этой раскладке (см. {@link #isValidCustomButtonSlot}), не должен быть уже занят,
     * и должна быть непустая команда. Некорректная запись логируется и пропускается - не ломает
     * рендер остального меню и не падает.
     */
    private void renderCustomButtons(Inventory inventory, AdaptationMenuHolder holder, Set<Integer> occupied, Player viewer) {
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
