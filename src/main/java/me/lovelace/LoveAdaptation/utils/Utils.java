package me.lovelace.LoveAdaptation.utils;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.SkullMeta;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.profile.PlayerProfile;
import org.bukkit.profile.PlayerTextures;

import net.kyori.adventure.text.minimessage.MiniMessage;
import net.kyori.adventure.text.serializer.legacy.LegacyComponentSerializer;

import java.net.MalformedURLException;
import java.net.URI;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.logging.Logger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Utils {

    private static final Pattern HEX_PATTERN = Pattern.compile("&#([A-Fa-f0-9]{6})");
    private static final MiniMessage MINI_MESSAGE = MiniMessage.miniMessage();
    private static final LegacyComponentSerializer LEGACY_SECTION = LegacyComponentSerializer.legacySection();

    private static Logger logger() {
        try {
            return JavaPlugin.getProvidingPlugin(Utils.class).getLogger();
        } catch (IllegalArgumentException | IllegalStateException e) {
            return Logger.getLogger(Utils.class.getName());
        }
    }

    public static String color(String text) {
        if (text == null) return "";
        if (text.contains("<") && text.contains(">")) {
            try {
                text = LEGACY_SECTION.serialize(MINI_MESSAGE.deserialize(text));
            } catch (Throwable ignored) {
                // fallback to raw text if invalid tag
            }
        }
        Matcher matcher = HEX_PATTERN.matcher(text);
        StringBuffer buffer = new StringBuffer();
        while (matcher.find()) {
            String hex = matcher.group(1);
            StringBuilder replacement = new StringBuilder("§x");
            for (char c : hex.toCharArray()) {
                replacement.append("§").append(c);
            }
            matcher.appendReplacement(buffer, replacement.toString());
        }
        matcher.appendTail(buffer);
        return ChatColor.translateAlternateColorCodes('&', buffer.toString());
    }

    public static List<String> color(List<String> list) {
        if (list == null) return new ArrayList<>();
        List<String> colored = new ArrayList<>();
        for (String s : list) {
            colored.add(color(s));
        }
        return colored;
    }

    public static void sendMessage(Player player, String message) {
        if (player != null && message != null && !message.isEmpty()) {
            player.sendMessage(color(message));
        }
    }

    /** Legacy/MiniMessage string to Adventure component, same colour rules as {@link #color}. */
    public static net.kyori.adventure.text.Component component(String message) {
        return LEGACY_SECTION.deserialize(color(message));
    }

    /** Event-style action bar notice, routed through LoveNotify (see {@link Notify}). */
    public static void sendActionBar(Player player, String message) {
        if (player != null && message != null && !message.isEmpty()) {
            Notify.actionBar(player, component(message));
        }
    }

    /** Event-style critical notice (always mirrored to chat by LoveCore). */
    public static void sendImportantActionBar(Player player, String message) {
        if (player != null && message != null && !message.isEmpty()) {
            Notify.important(player, component(message));
        }
    }

    /** Event-style title + subtitle, routed through LoveNotify. */
    public static void sendTitle(Player player, String title, String subtitle) {
        if (player != null) {
            Notify.title(player, component(title), component(subtitle));
        }
    }

    /**
     * Combat HUD read-out: fires on every hit, so it deliberately bypasses LoveNotify
     * (a chat fallback per hit would flood the chat for players with the action bar off).
     */
    public static void sendHudActionBar(Player player, String message) {
        if (player != null && message != null && !message.isEmpty()) {
            player.sendActionBar(component(message));
        }
    }

    @SuppressWarnings("deprecation")
    public static void playSound(Player player, String soundName, float volume, float pitch) {
        if (player == null || soundName == null || soundName.isEmpty()) return;
        try {
            Sound sound = null;
            try {
                org.bukkit.NamespacedKey key = org.bukkit.NamespacedKey.minecraft(soundName.toLowerCase(java.util.Locale.ROOT));
                sound = org.bukkit.Registry.SOUNDS.get(key);
            } catch (Throwable ignored) {}
            if (sound == null) {
                sound = Sound.valueOf(soundName.toUpperCase(java.util.Locale.ROOT));
            }
            player.playSound(player.getLocation(), sound, volume, pitch);
        } catch (IllegalArgumentException e) {
            logger().warning("Unknown sound name in config: '" + soundName + "' (" + e.getMessage() + ")");
        }
    }

    public static ItemStack createCustomHead(String base64Texture, String displayName, List<String> lore) {
        ItemStack item = new ItemStack(Material.PLAYER_HEAD);
        SkullMeta meta = (SkullMeta) item.getItemMeta();
        if (meta != null) {
            if (displayName != null) {
                meta.setDisplayName(color(displayName));
            }
            if (lore != null) {
                meta.setLore(color(lore));
            }
            if (base64Texture != null && !base64Texture.isEmpty()) {
                applyTexture(meta, base64Texture);
            }
            item.setItemMeta(meta);
        }
        return item;
    }

    private static void applyTexture(SkullMeta meta, String texture) {
        texture = texture.trim();
        try {
            PlayerProfile profile = Bukkit.createPlayerProfile(UUID.randomUUID());
            PlayerTextures textures = profile.getTextures();
            
            String textureUrl;
            if (texture.startsWith("http://") || texture.startsWith("https://")) {
                textureUrl = texture;
            } else if (texture.startsWith("basehead-")) {
                String rawBase64 = texture.substring("basehead-".length());
                textureUrl = getUrlFromBase64(rawBase64);
            } else {
                textureUrl = getUrlFromBase64(texture);
            }
            
            if (textureUrl != null) {
                textures.setSkin(new URI(textureUrl).toURL());
                profile.setTextures(textures);
                meta.setOwnerProfile(profile);
            }
        } catch (Exception e) {
            logger().warning("Failed to apply head texture '" + texture + "': " + e.getMessage());
        }
    }

    private static String getUrlFromBase64(String base64) {
        try {
            byte[] decoded = java.util.Base64.getDecoder().decode(base64);
            String json = new String(decoded, java.nio.charset.StandardCharsets.UTF_8);
            int urlIndex = json.indexOf("\"url\":\"");
            if (urlIndex != -1) {
                int start = urlIndex + 7;
                int end = json.indexOf("\"", start);
                if (end != -1) {
                    return json.substring(start, end);
                }
            }
        } catch (Exception e) {
            logger().warning("Failed to decode base64 head texture: " + e.getMessage());
        }
        return null;
    }

    public static String buildProgressBar(double percent, int length) {
        int filled = (int) Math.round((percent / 100.0) * length);
        filled = Math.max(0, Math.min(length, filled));
        String fillColor = percent >= 100.0 ? "&a" : "&6";
        StringBuilder sb = new StringBuilder("&8[");
        sb.append(fillColor);
        for (int i = 0; i < filled; i++) {
            sb.append("█");
        }
        sb.append("&8");
        for (int i = filled; i < length; i++) {
            sb.append("█");
        }
        sb.append("&8] &8(").append(fillColor).append((int) Math.round(percent)).append("%&8)");
        return color(sb.toString());
    }
}