package me.lovelace.LoveAdaptation.tasks;

import me.lovelace.LoveAdaptation.LoveAdaptation;
import me.lovelace.LoveAdaptation.managers.PluginManager;
import me.lovelace.LoveAdaptation.models.PlayerData;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

public class AdaptationTask extends BukkitRunnable {

    private final LoveAdaptation plugin;
    private long tickCounter = 0;

    public AdaptationTask(LoveAdaptation plugin) {
        this.plugin = plugin;
    }

    @Override
    public void run() {
        tickCounter++;

        long checkSeconds = plugin.getConfig().getLong("degradation_system.check_interval_seconds", 60);
        long degradationCheckIntervalTicks = Math.max(20L, checkSeconds * 20L);
        long saveIntervalTicks = 300 * 20L; // 5 minutes

        long passiveRegenCheckSeconds = plugin.getConfig().getLong("adaptations.combat.passive_regen.check_interval_seconds", 20);
        long passiveRegenCheckIntervalTicks = Math.max(20L, passiveRegenCheckSeconds * 20L);

        for (Player player : Bukkit.getOnlinePlayers()) {
            PlayerData data = PluginManager.getInstance().getAdaptationManager().getPlayerData(player.getUniqueId());
            if (data == null) continue;

            // Apply active adaptation effects every tick
            PluginManager.getInstance().getAdaptationManager().applyEffects(player, data);

            // Periodic degradation & reactivation check
            if (tickCounter % degradationCheckIntervalTicks == 0) {
                PluginManager.getInstance().getAdaptationManager().processDegradation(player, data);
                PluginManager.getInstance().getAdaptationManager().evaluateActiveAdaptation(player, data);
            }

            // Periodic passive-regen roll for "Боевой закал" (see processCombatPassiveRegen)
            if (tickCounter % passiveRegenCheckIntervalTicks == 0) {
                PluginManager.getInstance().getAdaptationManager().processCombatPassiveRegen(player, data);
            }
        }

        // Periodic database auto-save
        if (tickCounter % saveIntervalTicks == 0) {
            PluginManager.getInstance().getAdaptationManager().saveAllPlayers();
        }
    }
}
