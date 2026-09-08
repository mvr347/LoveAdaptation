package me.lovelace.LoveAdaptation.bestiary;

/**
 * Скрытые уровни исследования существ в Бестиарии Охотника.
 */
public enum ResearchTier {
    NONE(0, 0.0, 0.0, "Запись 0 (Без баффов)", "Запись 0", "0"),
    TIER_1(50, 0.03, 0.0, "⭐ Запись 1", "Запись 1", "1"),
    TIER_2(200, 0.07, 0.05, "⭐⭐ Запись 2", "Запись 2", "2"),
    TIER_3(500, 0.12, 0.10, "👑 Запись 3", "Запись 3", "3");

    private final int requiredKills;
    private final double bonusDamage;
    private final double resistance;
    private final String displayName;
    private final String shortBadge;
    private final String roman;

    ResearchTier(int kills, double bonusDamage, double resistance, String displayName, String shortBadge, String roman) {
        this.requiredKills = kills;
        this.bonusDamage = bonusDamage;
        this.resistance = resistance;
        this.displayName = displayName;
        this.shortBadge = shortBadge;
        this.roman = roman;
    }

    public int getRequiredKills() {
        return requiredKills;
    }

    public double getBonusDamage() {
        return bonusDamage;
    }

    public double getResistance() {
        return resistance;
    }

    public String getDisplayName() {
        return displayName;
    }

    public String getShortBadge() {
        return shortBadge;
    }

    public String getRoman() {
        return roman;
    }

    public ResearchTier getNextTier() {
        return switch (this) {
            case NONE -> TIER_1;
            case TIER_1 -> TIER_2;
            case TIER_2 -> TIER_3;
            case TIER_3 -> null;
        };
    }

    public static ResearchTier getTier(int kills) {
        ResearchTier current = NONE;
        for (ResearchTier t : values()) {
            if (kills >= t.requiredKills) {
                current = t;
            }
        }
        return current;
    }
}

