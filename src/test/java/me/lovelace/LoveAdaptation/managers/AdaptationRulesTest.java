package me.lovelace.LoveAdaptation.managers;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class AdaptationRulesTest {
    @Test
    void missingEffectIsApplied() {
        assertTrue(AdaptationRules.needsEffectRefresh(false, 0, 0, 1, 30));
    }

    @Test
    void healthyEffectIsLeftAlone() {
        assertFalse(AdaptationRules.needsEffectRefresh(true, 200, 1, 1, 30));
    }

    @Test
    void expiringEffectIsRefreshedAtThreshold() {
        assertTrue(AdaptationRules.needsEffectRefresh(true, 30, 1, 1, 30));
        assertFalse(AdaptationRules.needsEffectRefresh(true, 31, 1, 1, 30));
    }

    @Test
    void differentAmplifierIsRefreshed() {
        assertTrue(AdaptationRules.needsEffectRefresh(true, 500, 0, 2, 30));
    }

    @Test
    void blockedCountHonoursConfigAndEnumSize() {
        int values = 8; // 7 real types + BASE
        assertEquals(7, AdaptationRules.totalTypes(values));
        assertEquals(6, AdaptationRules.blockedCount(values, true, true));
        assertEquals(0, AdaptationRules.blockedCount(values, false, true));
        assertEquals(0, AdaptationRules.blockedCount(values, true, false));
    }
}
