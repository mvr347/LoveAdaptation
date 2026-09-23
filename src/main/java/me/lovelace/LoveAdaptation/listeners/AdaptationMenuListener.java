package me.lovelace.LoveAdaptation.listeners;

import me.lovelace.LoveAdaptation.LoveAdaptation;
import me.lovelace.LoveAdaptation.gui.AdaptationMenu;
import me.lovelace.LoveAdaptation.gui.AdaptationMenuHolder;
import me.lovelace.LoveAdaptation.managers.AdaptationManager;
import me.lovelace.LoveAdaptation.managers.PluginManager;
import me.lovelace.LoveAdaptation.models.AdaptationData;
import me.lovelace.LoveAdaptation.models.AdaptationType;
import me.lovelace.LoveAdaptation.models.PlayerData;
import me.lovelace.LoveAdaptation.utils.Utils;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryDragEvent;
import org.bukkit.inventory.InventoryHolder;

public final class AdaptationMenuListener implements Listener {

    // Больше не хранит plugin - раньше нужен был только для Bukkit.getScheduler().runTask() при
    // диспатче команды "Назад" напрямую отсюда; с 2026-09-23 диспатч команд (Back и
    // items.custom_buttons) выполняется в AdaptationMenu#dispatchConfiguredCommand при рендере
    // (см. AdaptationMenu#renderBackButton/#renderCustomButtons), листенер только запускает уже
    // зарегистрированное действие.
    public AdaptationMenuListener(LoveAdaptation plugin) {
        // plugin оставлен параметром конструктора ради обратной совместимости вызова в
        // LoveAdaptation#onEnable (new AdaptationMenuListener(this)) - самому листенеру он не нужен.
    }

    @EventHandler
    public void onDrag(InventoryDragEvent event) {
        // Without this, a player holding an item on their cursor could drag it across the
        // menu's slots and have it land on top of the decorative glass/heads, since cancelling
        // InventoryClickEvent does not also cover drag actions.
        InventoryHolder holder = event.getInventory().getHolder();
        if (holder instanceof AdaptationMenuHolder) {
            event.setCancelled(true);
        }
    }

    @EventHandler
    public void onClick(InventoryClickEvent event) {
        InventoryHolder holder = event.getInventory().getHolder();
        if (!(holder instanceof AdaptationMenuHolder)) {
            return;
        }
        event.setCancelled(true);

        if (!(event.getWhoClicked() instanceof Player)) {
            return;
        }
        Player player = (Player) event.getWhoClicked();
        int slot = event.getRawSlot();
        if (slot < 0) {
            return;
        }

        // Since 2026-09-23 every clickable slot (Info/Back/Close, or a configured
        // items.custom_buttons entry - see gui.yml) is registered as an action on the holder at
        // render time in AdaptationMenu#open - buttons can be repositioned/disabled in config, so
        // slot numbers are no longer compile-time constants the listener can switch on directly.
        AdaptationMenuHolder menuHolder = (AdaptationMenuHolder) holder;
        Runnable action = menuHolder.getAction(slot);
        if (action != null) {
            action.run();
            return;
        }

        AdaptationType type = AdaptationMenu.adaptationAt(slot);
        if (type != null) {
            sendAdaptationDetails(player, type);
        }
    }

    private void sendAdaptationDetails(Player player, AdaptationType type) {
        AdaptationManager manager = PluginManager.getInstance().getAdaptationManager();
        PlayerData data = manager.getPlayerData(player.getUniqueId());
        if (data == null) {
            return;
        }

        AdaptationData adaptData = data.getAdaptationData(type);
        double percent = adaptData != null ? adaptData.getProgressPercent() : 0.0;
        String name = manager.getAdaptationDisplayName(type);
        String color = manager.getAdaptationColor(type);
        String description = manager.getAdaptationDescription(type);

        player.sendMessage(Utils.color("&8=== " + color + name + " &8==="));
        if (!description.isEmpty()) {
            player.sendMessage(Utils.color("&7" + description));
        }
        player.sendMessage(Utils.color("&7Прогресс: &f" + String.format("%.1f%%", percent)
                + " " + Utils.buildProgressBar(percent, 20)));
    }
}
