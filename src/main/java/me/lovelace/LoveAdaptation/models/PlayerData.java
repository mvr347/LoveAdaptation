package me.lovelace.LoveAdaptation.models;

import org.bukkit.Location;

import java.util.EnumMap;
import java.util.Map;
import java.util.UUID;

public class PlayerData {
    private final UUID uuid;
    private AdaptationType currentAdaptation;
    private final Map<AdaptationType, AdaptationData> adaptationProgress;
    private Location lastLocation;
    private long potionExpiresAt; // timestamp when universal potion expires
    private String activePotionType; // "I" or "II" or null
    private long lastDegradationCheck;
    private long lastConditionCheck;

    public PlayerData(UUID uuid) {
        this.uuid = uuid;
        this.currentAdaptation = AdaptationType.BASE;
        this.adaptationProgress = new EnumMap<>(AdaptationType.class);
        for (AdaptationType type : AdaptationType.values()) {
            if (type != AdaptationType.BASE) {
                adaptationProgress.put(type, new AdaptationData(type));
            }
        }
        this.potionExpiresAt = 0;
        this.activePotionType = null;
        this.lastDegradationCheck = System.currentTimeMillis();
        this.lastConditionCheck = System.currentTimeMillis();
    }

    public UUID getUuid() {
        return uuid;
    }

    public AdaptationType getCurrentAdaptation() {
        return currentAdaptation;
    }

    public void setCurrentAdaptation(AdaptationType currentAdaptation) {
        this.currentAdaptation = currentAdaptation;
    }

    public Map<AdaptationType, AdaptationData> getAdaptationProgressMap() {
        return adaptationProgress;
    }

    public AdaptationData getAdaptationData(AdaptationType type) {
        return adaptationProgress.computeIfAbsent(type, AdaptationData::new);
    }

    public Location getLastLocation() {
        return lastLocation;
    }

    public void setLastLocation(Location lastLocation) {
        this.lastLocation = lastLocation;
    }

    public long getPotionExpiresAt() {
        return potionExpiresAt;
    }

    public void setPotionExpiresAt(long potionExpiresAt) {
        this.potionExpiresAt = potionExpiresAt;
    }

    public String getActivePotionType() {
        return activePotionType;
    }

    public void setActivePotionType(String activePotionType) {
        this.activePotionType = activePotionType;
    }

    public boolean isPotionActive() {
        return System.currentTimeMillis() < potionExpiresAt;
    }

    public long getLastDegradationCheck() {
        return lastDegradationCheck;
    }

    public void setLastDegradationCheck(long lastDegradationCheck) {
        this.lastDegradationCheck = lastDegradationCheck;
    }

    public long getLastConditionCheck() {
        return lastConditionCheck;
    }

    public void setLastConditionCheck(long lastConditionCheck) {
        this.lastConditionCheck = lastConditionCheck;
    }
}
