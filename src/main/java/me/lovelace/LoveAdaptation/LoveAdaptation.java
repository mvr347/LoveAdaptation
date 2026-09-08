package me.lovelace.LoveAdaptation;

import me.lovelace.LoveAdaptation.commands.LoveAdaptationAdminCommand;
import me.lovelace.LoveAdaptation.commands.LoveAdaptationCommand;
import me.lovelace.LoveAdaptation.config.GuiConfig;
import me.lovelace.LoveAdaptation.database.DatabaseManager;
import me.lovelace.LoveAdaptation.gui.AdaptationMenu;
import me.lovelace.LoveAdaptation.listeners.AdaptationMenuListener;
import me.lovelace.LoveAdaptation.listeners.PlayerListener;
import me.lovelace.LoveAdaptation.managers.PluginManager;
import me.lovelace.LoveAdaptation.placeholders.LoveAdaptationExpansion;
import me.lovelace.LoveAdaptation.tasks.AdaptationTask;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.HandlerList;
import org.bukkit.plugin.java.JavaPlugin;

public class LoveAdaptation extends JavaPlugin {

    private DatabaseManager databaseManager;
    private AdaptationTask adaptationTask;
    private AdaptationMenu adaptationMenu;

    @Override
    public void onEnable() {
        saveDefaultConfig();

        GuiConfig guiConfig = new GuiConfig(this);
        guiConfig.load();
        adaptationMenu = new AdaptationMenu(this, guiConfig);

        // Initialize database manager
        databaseManager = new DatabaseManager(this);
        databaseManager.initialize();

        // Initialize managers
        PluginManager.getInstance().initialize(this);

        // Register listeners
        getServer().getPluginManager().registerEvents(new PlayerListener(), this);
        getServer().getPluginManager().registerEvents(new AdaptationMenuListener(), this);
        getServer().getPluginManager().registerEvents(new me.lovelace.LoveAdaptation.bestiary.BestiaryListener(this, PluginManager.getInstance().getBestiaryManager()), this);

        // Register commands
        LoveAdaptationCommand command = new LoveAdaptationCommand(this);
        if (getCommand("loveadaptation") != null) {
            getCommand("loveadaptation").setExecutor(command);
            getCommand("loveadaptation").setTabCompleter(command);
        }

        LoveAdaptationAdminCommand adminCommand = new LoveAdaptationAdminCommand(this);
        if (getCommand("loveadaptationadmin") != null) {
            getCommand("loveadaptationadmin").setExecutor(adminCommand);
            getCommand("loveadaptationadmin").setTabCompleter(adminCommand);
        }

        me.lovelace.LoveAdaptation.commands.BestiaryCommand bestiaryCommand = new me.lovelace.LoveAdaptation.commands.BestiaryCommand(this);
        if (getCommand("bestiary") != null) {
            getCommand("bestiary").setExecutor(bestiaryCommand);
            getCommand("bestiary").setTabCompleter(bestiaryCommand);
        }

        // Register PlaceholderAPI expansion if available
        if (Bukkit.getPluginManager().getPlugin("PlaceholderAPI") != null) {
            new LoveAdaptationExpansion(this).register();
            getLogger().info("Registered PlaceholderAPI expansion for LoveAdaptation");
        }

        // Load currently online players (for reload support)
        for (Player player : Bukkit.getOnlinePlayers()) {
            PluginManager.getInstance().getAdaptationManager().loadPlayer(player);
        }

        // Start periodic background tasks
        adaptationTask = new AdaptationTask(this);
        adaptationTask.runTaskTimer(this, 1L, 1L);

        getLogger().info("LoveAdaptation v" + getDescription().getVersion() + " has been successfully enabled!");
    }

    @Override
    public void onDisable() {
        HandlerList.unregisterAll(this);
        if (adaptationTask != null) {
            adaptationTask.cancel();
        }

        // Save all player data synchronously before shutdown
        if (PluginManager.getInstance().getAdaptationManager() != null) {
            PluginManager.getInstance().getAdaptationManager().saveAllPlayersSync();
        }

        getLogger().info("LoveAdaptation has been disabled!");
    }

    public DatabaseManager getDatabaseManager() {
        return databaseManager;
    }

    public AdaptationMenu getAdaptationMenu() {
        return adaptationMenu;
    }
}