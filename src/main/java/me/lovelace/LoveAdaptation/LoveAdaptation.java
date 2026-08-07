package me.lovelace.LoveAdaptation;

import me.lovelace.LoveAdaptation.commands.LoveAdaptationAdminCommand;
import me.lovelace.LoveAdaptation.commands.LoveAdaptationCommand;
import me.lovelace.LoveAdaptation.database.DatabaseManager;
import me.lovelace.LoveAdaptation.listeners.PlayerListener;
import me.lovelace.LoveAdaptation.managers.PluginManager;
import me.lovelace.LoveAdaptation.placeholders.LoveAdaptationExpansion;
import me.lovelace.LoveAdaptation.tasks.AdaptationTask;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;
import org.bukkit.event.HandlerList;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.util.logging.Level;

public class LoveAdaptation extends JavaPlugin {

    private DatabaseManager databaseManager;
    private AdaptationTask adaptationTask;

    @Override
    public void onEnable() {
        saveDefaultConfig();
        deployDeluxeMenu();

        // Initialize database manager
        databaseManager = new DatabaseManager(this);
        databaseManager.initialize();

        // Initialize managers
        PluginManager.getInstance().initialize(this);

        // Register listeners
        getServer().getPluginManager().registerEvents(new PlayerListener(), this);

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

    /**
     * Меню адаптаций лежит в jar-е, а открывается через {@code dm open adaptation_menu}.
     * DeluxeMenus читает меню только из своей папки gui_menus, поэтому файл нужно туда
     * положить — иначе команда отвечает "Could not find menu: adaptation_menu".
     * Существующий файл не трогаем: админ мог его отредактировать под себя.
     */
    private void deployDeluxeMenu() {
        Plugin deluxeMenus = Bukkit.getPluginManager().getPlugin("DeluxeMenus");
        if (deluxeMenus == null) {
            getLogger().warning("DeluxeMenus не найден — меню адаптаций (/adaptations) работать не будет.");
            return;
        }

        File menuFile = new File(deluxeMenus.getDataFolder(), "gui_menus/adaptation_menu.yml");
        if (menuFile.exists()) {
            return;
        }

        File parent = menuFile.getParentFile();
        if (!parent.isDirectory() && !parent.mkdirs()) {
            getLogger().warning("Не удалось создать папку " + parent.getPath() + " — меню адаптаций не установлено.");
            return;
        }

        try (InputStream in = getResource("gui/adaptation_menu.yml")) {
            if (in == null) {
                getLogger().warning("Ресурс gui/adaptation_menu.yml отсутствует в jar — меню адаптаций не установлено.");
                return;
            }
            Files.copy(in, menuFile.toPath());
        } catch (IOException e) {
            getLogger().log(Level.WARNING, "Не удалось скопировать меню адаптаций в DeluxeMenus", e);
            return;
        }

        // Без перезагрузки DeluxeMenus подхватит меню только со следующего запуска сервера.
        Bukkit.dispatchCommand(Bukkit.getConsoleSender(), "dm reload");
        getLogger().info("Меню адаптаций установлено в DeluxeMenus (gui_menus/adaptation_menu.yml).");
    }

    public DatabaseManager getDatabaseManager() {
        return databaseManager;
    }
}