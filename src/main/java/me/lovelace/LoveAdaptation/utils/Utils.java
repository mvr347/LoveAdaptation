package me.lovelace.LoveAdaptation.utils;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.SkullMeta;
import org.bukkit.profile.PlayerProfile;
import org.bukkit.profile.PlayerTextures;

import java.net.MalformedURLException;
import java.net.URI;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Utils {

    private static final Pattern HEX_PATTERN = Pattern.compile("&#([A-Fa-f0-9]{6})");

    public static String color(String text) {
        if (text == null) return "";
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

    public static void sendActionBar(Player player, String message) {
        if (player != null && message != null && !message.isEmpty()) {
            // Не кэшируем Optional<LoveNotify> — сосед может зарегистрировать реализацию
            // позже, см. LoveCore.service(...) javadoc в LoveCore.
            boolean allowed = dev.lovelace.lovecore.api.LoveCore.service(dev.lovelace.lovecore.api.notify.LoveNotify.class)
                    .map(n -> n.isChannelEnabled(player.getUniqueId(), dev.lovelace.lovecore.api.notify.LoveNotify.Channel.ACTION_BAR))
                    .orElse(true);
            if (!allowed) {
                return;
            }
            try {
                player.spigot().sendMessage(net.md_5.bungee.api.ChatMessageType.ACTION_BAR, net.md_5.bungee.api.chat.TextComponent.fromLegacyText(color(message)));
            } catch (Throwable t) {
                player.sendMessage(color(message));
            }
        }
    }

    public static void playSound(Player player, String soundName, float volume, float pitch) {
        if (player == null || soundName == null || soundName.isEmpty()) return;
        try {
            Sound sound = Sound.valueOf(soundName.toUpperCase());
            player.playSound(player.getLocation(), sound, volume, pitch);
        } catch (IllegalArgumentException e) {
            // Sound not found, fallback silently
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
            // Texture parsing fallback
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
        } catch (Exception ignored) {
        }
        return null;
    }

    public static String buildProgressBar(double percent, int length) {
        int filled = (int) Math.round((percent / 100.0) * length);
        filled = Math.max(0, Math.min(length, filled));
        StringBuilder sb = new StringBuilder("&a");
        for (int i = 0; i < filled; i++) {
            sb.append("■");
        }
        sb.append("&7");
        for (int i = filled; i < length; i++) {
            sb.append("■");
        }
        return color(sb.toString());
    }
}