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

    void setInventory(Inventory inventory) {
        this.inventory = inventory;
    }

    @Override
    public @NotNull Inventory getInventory() {
        return inventory;
    }
}
