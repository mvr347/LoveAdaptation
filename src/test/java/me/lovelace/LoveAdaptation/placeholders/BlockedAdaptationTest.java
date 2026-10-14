package me.lovelace.LoveAdaptation.placeholders;

import me.lovelace.LoveAdaptation.models.AdaptationType;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

class BlockedAdaptationTest {

    private static AdaptationType firstNonBase() {
        for (AdaptationType t : AdaptationType.values()) if (t != AdaptationType.BASE) return t;
        throw new IllegalStateException();
    }

    private static AdaptationType secondNonBase() {
        AdaptationType first = firstNonBase();
        for (AdaptationType t : AdaptationType.values()) if (t != AdaptationType.BASE && t != first) return t;
        throw new IllegalStateException();
    }

    @Test
    void missingDataOfCurrentAdaptationDoesNotThrowAndBlocksNothing() {
        assertFalse(LoveAdaptationExpansion.isBlocked(firstNonBase(), secondNonBase(), null));
    }

    @Test
    void masteredCurrentBlocksOthersButNotItself() {
        AdaptationType a = firstNonBase(), b = secondNonBase();
        assertTrue(LoveAdaptationExpansion.isBlocked(a, b, 90.0));
        assertFalse(LoveAdaptationExpansion.isBlocked(a, a, 95.0));
        assertFalse(LoveAdaptationExpansion.isBlocked(a, b, 89.9));
    }

    @Test
    void baseNeverBlocks() {
        assertFalse(LoveAdaptationExpansion.isBlocked(AdaptationType.BASE, firstNonBase(), 100.0));
    }
}
