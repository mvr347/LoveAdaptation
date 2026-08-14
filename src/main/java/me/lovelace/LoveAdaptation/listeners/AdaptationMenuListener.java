package me.lovelace.LoveAdaptation.listeners;

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

        if (slot == AdaptationMenu.CLOSE_SLOT) {
            player.closeInventory();
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
