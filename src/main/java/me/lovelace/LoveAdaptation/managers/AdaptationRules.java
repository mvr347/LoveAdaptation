package me.lovelace.LoveAdaptation.managers;

/** Pure decision helpers (no Bukkit types) so they can be unit-tested. */
public final class AdaptationRules {
    private AdaptationRules() {}

    /**
     * Whether a potion effect must be (re)applied. Re-applying every run resets particles/ticks and
     * costs a packet per player, so only do it when the effect is missing, differs, or is about to expire.
     *
     * @param hasEffect         player currently has this effect type
     * @param remainingTicks    remaining duration of the current effect (ignored if !hasEffect)
     * @param currentAmplifier  amplifier of the current effect (ignored if !hasEffect)
     * @param wantedAmplifier   amplifier we want
     * @param thresholdTicks    refresh when remaining is at or below this (must exceed the task interval)
     */
    public static boolean needsEffectRefresh(boolean hasEffect, int remainingTicks, int currentAmplifier,
                                             int wantedAmplifier, int thresholdTicks) {
        if (!hasEffect) return true;
        if (currentAmplifier != wantedAmplifier) return true;
        return remainingTicks <= thresholdTicks;
    }

    /** Remaining adaptation types blocked from progress while a mastered one is active. */
    public static int blockedCount(int typeCountIncludingBase, boolean blockingEnabled, boolean activeMastered) {
        if (!blockingEnabled || !activeMastered) return 0;
        return Math.max(0, typeCountIncludingBase - 2); // all real types minus the active one
    }

    /** Total real adaptation types (BASE excluded). */
    public static int totalTypes(int typeCountIncludingBase) {
        return Math.max(0, typeCountIncludingBase - 1);
    }
}
