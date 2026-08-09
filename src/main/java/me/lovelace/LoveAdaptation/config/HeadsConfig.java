package me.lovelace.LoveAdaptation.config;

import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;

/**
 * Читает base64-текстуры служебных иконок GUI (кнопки "Информация"/"Закрыть") из heads.yml,
 * чтобы их можно было заменить без пересборки плагина.
 */
public final class HeadsConfig {
    private static volatile YamlConfiguration config;

    private HeadsConfig() {
    }

    public static String get(String key, String fallback) {
        YamlConfiguration yaml = config();
        if (yaml == null) {
            return fallback;
        }
        return yaml.getString(key, fallback);
    }

    private static YamlConfiguration config() {
        YamlConfiguration loaded = config;
        if (loaded == null) {
            synchronized (HeadsConfig.class) {
                loaded = config;
                if (loaded == null) {
                    loaded = load();
                    config = loaded;
                }
            }
        }
        return loaded;
    }

    private static YamlConfiguration load() {
        try {
            JavaPlugin plugin = JavaPlugin.getProvidingPlugin(HeadsConfig.class);
            File file = new File(plugin.getDataFolder(), "heads.yml");
            if (!file.exists()) {
                plugin.saveResource("heads.yml", false);
            }
            return YamlConfiguration.loadConfiguration(file);
        } catch (IllegalArgumentException | IllegalStateException exception) {
            return null;
        }
    }
}
