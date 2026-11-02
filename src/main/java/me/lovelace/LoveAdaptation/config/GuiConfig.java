package me.lovelace.LoveAdaptation.config;

import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
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
        try (InputStream defStream = plugin.getResource("gui.yml")) {
            if (defStream == null) throw new IOException("В JAR отсутствует gui.yml");
            YamlConfiguration defaults = new YamlConfiguration();
            defaults.load(new InputStreamReader(defStream, StandardCharsets.UTF_8));
            gui = loadAndUpgrade(file.toPath(), defaults);
        } catch (Exception e) {
            throw new IllegalStateException("Не удалось загрузить gui.yml; исходный файл сохранён", e);
        }
    }

    static YamlConfiguration loadAndUpgrade(Path file, YamlConfiguration defaults) throws Exception {
        var current = new YamlConfiguration();
        current.load(file.toFile()); // malformed YAML must never be replaced with an empty document
        if (upgrade(current, defaults)) {
            Path backup = file.resolveSibling(file.getFileName() + ".pre-itemsadder-45.bak");
            if (!Files.exists(backup)) Files.copy(file, backup);
            Path temporary = Files.createTempFile(file.getParent(), ".gui-", ".tmp");
            try {
                Files.writeString(temporary, current.saveToString(), StandardCharsets.UTF_8);
                Files.move(temporary, file, StandardCopyOption.ATOMIC_MOVE, StandardCopyOption.REPLACE_EXISTING);
            } finally {
                Files.deleteIfExists(temporary);
            }
        }
        current.setDefaults(defaults);
        return current;
    }

    static boolean upgrade(YamlConfiguration current, YamlConfiguration defaults) {
        if (current.getInt("menu.layout-version", 0) >= 2) return false;
        if ("&6Адаптации персонажа".equals(current.getString("menu.title")))
            current.set("menu.title", defaults.getString("menu.title"));
        for (String key : List.of("title", "fallback_title", "frame"))
            if (!current.contains("menu." + key)) current.set("menu." + key, defaults.get("menu." + key));
        for (String button : List.of("info", "back", "close")) {
            String path = "items." + button + ".slot";
            int old = switch (button) { case "info" -> 51; case "back" -> 52; default -> 53; };
            if (current.getInt(path, old) == old) current.set(path, old - 9);
        }
        // Preserve footer-relative positions of custom controls that used the old sixth row.
        if (current.isList("items.custom_buttons")) {
            var buttons = new java.util.ArrayList<Map<String, Object>>();
            for (var entry : current.getMapList("items.custom_buttons")) {
                var copy = new java.util.LinkedHashMap<String, Object>();
                entry.forEach((key, value) -> copy.put(key.toString(), value));
                if (copy.get("slot") instanceof Number n && n.intValue() >= 45 && n.intValue() < 54)
                    copy.put("slot", n.intValue() - 9);
                buttons.add(copy);
            }
            current.set("items.custom_buttons", buttons);
        }
        current.set("menu.layout-version", 2);
        return true;
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
