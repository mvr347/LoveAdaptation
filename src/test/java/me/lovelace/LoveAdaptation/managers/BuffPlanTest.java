package me.lovelace.LoveAdaptation.managers;

import org.junit.jupiter.api.Test;

import java.util.Map;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

class BuffPlanTest {

    @Test
    void addsMissingModifier() {
        var plan = BuffPlan.diff(Map.of("buff_hunter_max_health", 2.0), Map.of());
        assertEquals(Map.of("buff_hunter_max_health", 2.0), plan.toAdd());
        assertTrue(plan.toRemove().isEmpty());
    }

    @Test
    void leavesUnchangedModifierAlone() {
        var plan = BuffPlan.diff(Map.of("a", 2.0), Map.of("a", 2.0));
        assertTrue(plan.isEmpty());
    }

    @Test
    void replacesModifierWhoseAmountChanged() {
        var plan = BuffPlan.diff(Map.of("a", 4.0), Map.of("a", 2.0));
        assertEquals(Map.of("a", 4.0), plan.toAdd());
        assertEquals(Set.of("a"), plan.toRemove());
    }

    @Test
    void removesModifierWhenSourceStopsGrantingIt() {
        var plan = BuffPlan.diff(Map.of(), Map.of("a", 2.0));
        assertTrue(plan.toAdd().isEmpty());
        assertEquals(Set.of("a"), plan.toRemove());
    }
}
