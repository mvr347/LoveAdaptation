package me.lovelace.LoveAdaptation.gui;

import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;
import org.jetbrains.annotations.NotNull;

/**
 * Маркер инвентаря меню адаптаций — используется в листенере, чтобы отличать
 * клики в этом меню от кликов в любом другом инвентаре игрока.
 */
public final class AdaptationMenuHolder implements InventoryHolder {

    private Inventory inventory;
    private String backCommand;

    void setInventory(Inventory inventory) {
        this.inventory = inventory;
    }

    /** Command (without the leading /) the Back button runs, or null if it's inactive (glass). */
    void setBackCommand(String backCommand) {
        this.backCommand = backCommand;
    }

    public String getBackCommand() {
        return backCommand;
    }

    @Override
    public @NotNull Inventory getInventory() {
        return inventory;
    }
}
