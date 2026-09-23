package me.lovelace.LoveAdaptation.gui;

import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;
import org.jetbrains.annotations.NotNull;

import java.util.HashMap;
import java.util.Map;

/**
 * Маркер инвентаря меню адаптаций — используется в листенере, чтобы отличать
 * клики в этом меню от кликов в любом другом инвентаре игрока.
 *
 * <p>С 2026-09-23 все кнопки этого меню (Info/Back и {@code items.custom_buttons})
 * можно включать/выключать и (для custom_buttons) свободно размещать в gui.yml —
 * поэтому слот, где живёт конкретная кнопка, больше не константа времени
 * компиляции. Вместо сравнения кликнутого слота с {@code AdaptationMenu.INFO_SLOT}
 * и т.п. листенер просто ищет действие, зарегистрированное для этого слота при
 * последнем рендере — тот же паттерн, что {@code BehaviorGuiHolder} у LoveBehaivor.
 * Close по-прежнему рендерится на фиксированном слоте (gui_gen v2.1: позиция 8/Close
 * не конфигурируется), но регистрируется так же единообразно.</p>
 */
public final class AdaptationMenuHolder implements InventoryHolder {

    private Inventory inventory;
    private final Map<Integer, Runnable> actions = new HashMap<>();

    void setInventory(Inventory inventory) {
        this.inventory = inventory;
    }

    public void registerAction(int slot, Runnable action) {
        actions.put(slot, action);
    }

    /** Действие, зарегистрированное для этого слота в текущем рендере, или null, если слот не кликабелен. */
    public Runnable getAction(int slot) {
        return actions.get(slot);
    }

    @Override
    public @NotNull Inventory getInventory() {
        return inventory;
    }
}
