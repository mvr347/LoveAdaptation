package me.lovelace.LoveAdaptation.models;

public enum AdaptationType {
    WATER("water", "WATER", "ticks_in_water"),
    NETHER("nether", "NETHER", "ticks_in_nether"),
    CAVE("cave", "CAVE", "ticks_in_cave"),
    HEIGHT("height", "HEIGHT", "fall_damage_events"),
    END("end", "END", "ticks_in_end"),
    COMBAT("combat", "COMBAT", "damage_taken_from_hostiles"),
    TRAVEL("travel", "TRAVEL", "distance_walked"),
    BASE("base", "BASE", "none");

    private final String configKey;
    private final String dbName;
    private final String progressMetric;

    AdaptationType(String configKey, String dbName, String progressMetric) {
        this.configKey = configKey;
        this.dbName = dbName;
        this.progressMetric = progressMetric;
    }

    public String getConfigKey() {
        return configKey;
    }

    public String getDbName() {
        return dbName;
    }

    public String getProgressMetric() {
        return progressMetric;
    }

    public static AdaptationType fromString(String name) {
        if (name == null) return BASE;
        for (AdaptationType type : values()) {
            if (type.name().equalsIgnoreCase(name) || 
                type.configKey.equalsIgnoreCase(name) || 
                type.dbName.equalsIgnoreCase(name)) {
                return type;
            }
        }
        return BASE;
    }
}
