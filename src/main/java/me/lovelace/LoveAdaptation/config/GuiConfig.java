package me.lovelace.LoveAdaptation.config;

import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.Collections;
import java.util.List;

/**
 * Доступ к gui.yml — тексту служебных кнопок нативного меню адаптаций.
 */
public final class GuiConfig {

    private final JavaPlugin plugin;
    private FileConfiguration gui;

    public GuiConfig(JavaPlugin plugin) {
        this.plugin = plugin;
    }

    public void load() {
        File file = new File(plugin.getDataFolder(), "gui.yml");
        if (!file.exists()) {
            plugin.saveResource("gui.yml", false);
        }
        gui = YamlConfiguration.loadConfiguration(file);

        try (InputStream defStream = plugin.getResource("gui.yml")) {
            if (defStream != null) {
                YamlConfiguration defConfig = YamlConfiguration.loadConfiguration(
                        new InputStreamReader(defStream, StandardCharsets.UTF_8));
                gui.setDefaults(defConfig);
            }
        } catch (IOException ignored) {
            // используем то, что уже загружено
        }
    }

    public String getString(String path, String def) {
        return gui.getString(path, def);
    }

    public List<String> getStringList(String path) {
        List<String> list = gui.getStringList(path);
        return list != null ? list : Collections.emptyList();
    }
}
