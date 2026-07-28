package me.lovelace.LoveAdaptation.models;

public class AdaptationData {
    private final AdaptationType type;
    private double progressPercent; // 0.0 to 100.0
    private long progressValue; // ticks, events, distance in blocks, damage
    private boolean unlocked; // Ever reached 90%
    private boolean active;
    private long unlockedAt;
    private long activatedAt;
    private long deactivatedAt;
    private long lastConditionFulfilledTime;

    public AdaptationData(AdaptationType type) {
        this.type = type;
        this.progressPercent = 0.0;
        this.progressValue = 0;
        this.unlocked = false;
        this.active = false;
        this.lastConditionFulfilledTime = System.currentTimeMillis();
    }

    public AdaptationType getType() {
        return type;
    }

    public double getProgressPercent() {
        return progressPercent;
    }

    public void setProgressPercent(double progressPercent) {
        this.progressPercent = Math.max(0.0, Math.min(100.0, progressPercent));
        if (this.progressPercent >= 90.0) {
            if (!this.unlocked) {
                this.unlocked = true;
                this.unlockedAt = System.currentTimeMillis();
            }
        }
    }

    public long getProgressValue() {
        return progressValue;
    }

    public void setProgressValue(long progressValue) {
        this.progressValue = Math.max(0, progressValue);
    }

    public void addProgressValue(long amount) {
        this.progressValue += amount;
    }

    public boolean isUnlocked() {
        return unlocked;
    }

    public void setUnlocked(boolean unlocked) {
        this.unlocked = unlocked;
    }

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        if (this.active != active) {
            this.active = active;
            long now = System.currentTimeMillis();
            if (active) {
                this.activatedAt = now;
            } else {
                this.deactivatedAt = now;
            }
        }
    }

    public long getUnlockedAt() {
        return unlockedAt;
    }

    public void setUnlockedAt(long unlockedAt) {
        this.unlockedAt = unlockedAt;
    }

    public long getActivatedAt() {
        return activatedAt;
    }

    public void setActivatedAt(long activatedAt) {
        this.activatedAt = activatedAt;
    }

    public long getDeactivatedAt() {
        return deactivatedAt;
    }

    public void setDeactivatedAt(long deactivatedAt) {
        this.deactivatedAt = deactivatedAt;
    }

    public long getLastConditionFulfilledTime() {
        return lastConditionFulfilledTime;
    }

    public void setLastConditionFulfilledTime(long lastConditionFulfilledTime) {
        this.lastConditionFulfilledTime = lastConditionFulfilledTime;
    }
}
