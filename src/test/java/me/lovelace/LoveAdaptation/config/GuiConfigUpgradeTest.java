package me.lovelace.LoveAdaptation.config;

import static org.junit.jupiter.api.Assertions.*;

import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.Map;
import org.bukkit.configuration.file.YamlConfiguration;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

class GuiConfigUpgradeTest {
    @TempDir Path directory;

    YamlConfiguration defaults() throws Exception {
        var result = new YamlConfiguration();
        try (var in = getClass().getResourceAsStream("/gui.yml")) {
            result.load(new InputStreamReader(in, StandardCharsets.UTF_8));
        }
        return result;
    }

    @Test
    void defaultLayoutMigratesOnceAndKeepsAnExactBackup() throws Exception {
        var file = directory.resolve("gui.yml");
        String original = "menu:\n  title: '&6Адаптации персонажа'\nitems:\n  back:\n    slot: 52\n    command: custom:home\n  close:\n    slot: 53\n";
        Files.writeString(file, original);
        var loaded = GuiConfig.loadAndUpgrade(file, defaults());
        assertEquals(defaults().getString("menu.title"), loaded.getString("menu.title"));
        assertFalse(loaded.getBoolean("menu.frame"));
        assertEquals(43, loaded.getInt("items.back.slot"));
        assertEquals(44, loaded.getInt("items.close.slot"));
        assertEquals("custom:home", loaded.getString("items.back.command"));
        var backup = directory.resolve("gui.yml.pre-itemsadder-45.bak");
        assertEquals(original, Files.readString(backup));
        String migrated = Files.readString(file);
        GuiConfig.loadAndUpgrade(file, defaults());
        assertEquals(migrated, Files.readString(file));
        assertEquals(original, Files.readString(backup));
    }

    @Test
    void artworkAndCustomControlsArePreservedWhileFooterControlsMoveUpOneRow() throws Exception {
        var installed = new YamlConfiguration();
        installed.set("menu.title", "<white>%img_my_art%");
        installed.set("menu.frame", true);
        installed.set("items.player_head.name", "<gold>Мой профиль");
        installed.set("items.custom_buttons", List.of(
                Map.of("slot", 50, "command", "custom:menu", "name", "Моя кнопка"),
                Map.of("slot", 28, "command", "custom:other")));
        assertTrue(GuiConfig.upgrade(installed, defaults()));
        assertEquals("<white>%img_my_art%", installed.getString("menu.title"));
        assertTrue(installed.getBoolean("menu.frame"));
        assertEquals("<gold>Мой профиль", installed.getString("items.player_head.name"));
        var controls = installed.getMapList("items.custom_buttons");
        assertEquals(41, controls.get(0).get("slot"));
        assertEquals("custom:menu", controls.get(0).get("command"));
        assertEquals("Моя кнопка", controls.get(0).get("name"));
        assertEquals(28, controls.get(1).get("slot"));
        assertFalse(GuiConfig.upgrade(installed, defaults()));
    }

    @Test
    void malformedFilesAreNeverReplacedOrBackedUpAsValidLayouts() throws Exception {
        var file = directory.resolve("gui.yml");
        String broken = "menu: [\n";
        Files.writeString(file, broken);
        assertThrows(Exception.class, () -> GuiConfig.loadAndUpgrade(file, defaults()));
        assertEquals(broken, Files.readString(file));
        assertFalse(Files.exists(directory.resolve("gui.yml.pre-itemsadder-45.bak")));
    }
}
