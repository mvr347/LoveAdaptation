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
import java.util.Map;

/**
 * Доступ к gui.yml — тексту и раскладке служебных кнопок нативного меню адаптаций.
 *
 * <p>С 2026-09-23 (по запросу владельца — довести GUI до той же степени
 * настраиваемости, что и у LoveBehaivor: снимать/добавлять базовые кнопки,
 * двигать их и добавлять полностью свои со своими текстурами и командами)
 * базовые кнопки (info/back) дополнительно поддерживают {@code enabled}, а
 * {@code items.custom_buttons} — список полностью новых кнопок. Для этого
 * нужны {@link #getInt(String, int)}, {@link #getBoolean(String, boolean)} и
 * {@link #getMapList(String)} в дополнение к уже существовавшим
 * {@link #getString(String, String)}/{@link #getStringList(String)} — тот же
 * набор методов, что и у {@code GuiConfigManager} в LoveBehaivor.</p>
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
        } catch (IOException e) {
            // используем то, что уже загружено
            plugin.getLogger().warning("Failed to load default gui.yml from plugin jar: " + e.getMessage());
        }
    }

    public String getString(String path, String def) {
        return gui.getString(path, def);
    }

    public List<String> getStringList(String path) {
        List<String> list = gui.getStringList(path);
        return list != null ? list : Collections.emptyList();
    }

    public int getInt(String path, int def) {
        return gui.getInt(path, def);
    }

    public boolean getBoolean(String path, boolean def) {
        return gui.getBoolean(path, def);
    }

    /**
     * Список произвольных записей (карта ключ-значение на элемент) — например
     * {@code items.custom_buttons}. Тот же формат, что YamlConfiguration использует
     * для списков карт в других Love*-плагинах (ср. {@code GuiConfigManager#getMapList}
     * у LoveBehaivor).
     */
    public List<Map<?, ?>> getMapList(String path) {
        return gui.getMapList(path);
    }

    public boolean contains(String path) {
        return gui != null && gui.contains(path);
    }

    public boolean isSet(String path) {
        return gui != null && gui.isSet(path);
    }

    public Object get(String path) {
        return gui != null ? gui.get(path) : null;
    }
}
