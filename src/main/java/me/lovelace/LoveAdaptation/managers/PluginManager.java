package me.lovelace.LoveAdaptation.managers;

import me.lovelace.LoveAdaptation.LoveAdaptation;

public class PluginManager {

    private static PluginManager instance;
    private LoveAdaptation plugin;
    private AdaptationManager adaptationManager;
    private PotionManager potionManager;

    private PluginManager() {
    }

    public static PluginManager getInstance() {
        if (instance == null) {
            instance = new PluginManager();
        }
        return instance;
    }

    public void initialize(LoveAdaptation plugin) {
        this.plugin = plugin;
        this.adaptationManager = new AdaptationManager(plugin);
        this.potionManager = new PotionManager(plugin);
    }

    public LoveAdaptation getPlugin() {
        return plugin;
    }

    public AdaptationManager getAdaptationManager() {
        return adaptationManager;
    }

    public PotionManager getPotionManager() {
        return potionManager;
    }
}