package me.lovelace.LoveAdaptation.managers;

import java.util.HashMap;
import java.util.Map;

/**
 * Pure diff between the attribute bonuses other plugins want ({@code desired}) and the modifiers
 * LoveAdaptation currently has on the player ({@code present}); both keyed by modifier key.
 */
public record BuffPlan(Map<String, Double> toAdd, java.util.Set<String> toRemove) {

    public static BuffPlan diff(Map<String, Double> desired, Map<String, Double> present) {
        Map<String, Double> add = new HashMap<>();
        java.util.Set<String> remove = new java.util.HashSet<>();
        for (var wanted : desired.entrySet()) {
            Double have = present.get(wanted.getKey());
            if (have == null) add.put(wanted.getKey(), wanted.getValue());
            else if (Math.abs(have - wanted.getValue()) > 1e-9) {
                remove.add(wanted.getKey());
                add.put(wanted.getKey(), wanted.getValue());
            }
        }
        for (String key : present.keySet()) if (!desired.containsKey(key)) remove.add(key);
        return new BuffPlan(add, remove);
    }

    public boolean isEmpty() {
        return toAdd.isEmpty() && toRemove.isEmpty();
    }
}
