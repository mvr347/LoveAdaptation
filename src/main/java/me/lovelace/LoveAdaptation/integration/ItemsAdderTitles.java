package me.lovelace.LoveAdaptation.integration;

import java.util.function.Function;
import java.util.regex.Pattern;
import me.clip.placeholderapi.PlaceholderAPI;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.minimessage.MiniMessage;
import net.kyori.adventure.text.minimessage.tag.resolver.Placeholder;
import net.kyori.adventure.text.minimessage.tag.resolver.TagResolver;
import net.kyori.adventure.text.serializer.legacy.LegacyComponentSerializer;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

/**
 * Inventory artwork as ItemsAdder font glyphs; no resource-pack files or compile-time IA
 * dependency.
 */
public final class ItemsAdderTitles {
    private static final Pattern IMAGE = Pattern.compile("%(?:img|ia)_([a-zA-Z0-9_:.-]+)%");
    private static final MiniMessage MINI = MiniMessage.miniMessage();

    private ItemsAdderTitles() {}

    public static Component render(Player viewer, String title, String fallback) {
        return render(title, fallback, token -> resolve(viewer, token));
    }

    /** External legacy-formatted glyphs enter as components, never as MiniMessage source. */
    static Component render(String title, String fallback, Function<String, String> resolver) {
        title = miniSource(title);
        fallback = miniSource(fallback);
        var matcher = IMAGE.matcher(title);
        var template = new StringBuilder();
        var tags = TagResolver.builder();
        int end = 0, index = 0;
        while (matcher.find()) {
            String value = resolver.apply(matcher.group());
            if (value == null || value.equals(matcher.group()) || IMAGE.matcher(value).find())
                return MINI.deserialize(fallback);
            String key = "ia_image_" + index++;
            // Section codes emitted by ItemsAdder/PAPI must not reach the MiniMessage parser.
            Component image = white(LegacyComponentSerializer.legacySection().deserialize(value));
            tags.resolver(Placeholder.component(key, image));
            template.append(title, end, matcher.start()).append('<').append(key).append('>');
            end = matcher.end();
        }
        template.append(title, end, title.length());
        return MINI.deserialize(template.toString(), tags.build());
    }

    /** Normalize only administrator-authored legacy text, before inserting external glyph components. */
    private static String miniSource(String text) {
        var hex = java.util.regex.Pattern.compile("(?i)&#([0-9a-f]{6})|§x((?:§[0-9a-f]){6})").matcher(text);
        text = hex.replaceAll(match -> "<#" + (match.group(1) != null ? match.group(1) : match.group(2).replace("§", "")) + ">");
        String colored = org.bukkit.ChatColor.translateAlternateColorCodes('&', text);
        return colored.indexOf('§') < 0 ? colored : legacySource(colored);
    }

    private static String legacySource(String text) {
        // Serialize legacy runs, keeping the surrounding MiniMessage tags authored in gui.yml.
        var output = new StringBuilder();
        var codes = java.util.regex.Pattern.compile("§([0-9a-fk-or])", java.util.regex.Pattern.CASE_INSENSITIVE).matcher(text);
        int end = 0;
        String[] colors = {"black", "dark_blue", "dark_green", "dark_aqua", "dark_red", "dark_purple",
                "gold", "gray", "dark_gray", "blue", "green", "aqua", "red", "light_purple", "yellow", "white"};
        while (codes.find()) {
            output.append(text, end, codes.start());
            char code = Character.toLowerCase(codes.group(1).charAt(0));
            int color = Character.digit(code, 16);
            String tag = color >= 0 ? colors[color] : switch (code) {
                case 'k' -> "obfuscated";
                case 'l' -> "bold";
                case 'm' -> "strikethrough";
                case 'n' -> "underlined";
                case 'o' -> "italic";
                default -> "reset";
            };
            output.append('<').append(tag).append('>');
            end = codes.end();
        }
        return output.append(text, end, text.length()).toString();
    }

    private static Component white(Component image) {
        return image.color(NamedTextColor.WHITE)
                .children(image.children().stream().map(ItemsAdderTitles::white).toList());
    }

    private static String resolve(Player viewer, String token) {
        var itemsAdder = Bukkit.getPluginManager().getPlugin("ItemsAdder");
        if (itemsAdder == null || !itemsAdder.isEnabled()) return null;
        if (Bukkit.getPluginManager().isPluginEnabled("PlaceholderAPI")) {
            try {
                String value = PlaceholderAPI.setPlaceholders(viewer, token);
                if (value != null && !value.equals(token)) return value;
            } catch (RuntimeException | LinkageError ignored) {
                // Fall back to the optional ItemsAdder API without replacing unrelated text.
            }
        }
        var matcher = IMAGE.matcher(token);
        if (!matcher.matches()) return null;
        String marker = ":" + matcher.group(1) + ":";
        try {
            var wrapper =
                    Class.forName(
                            "dev.lone.itemsadder.api.FontImages.FontImageWrapper",
                            true,
                            itemsAdder.getClass().getClassLoader());
            Object result =
                    wrapper.getMethod("replaceFontImages", String.class).invoke(null, marker);
            return result instanceof String value && !value.contains(marker) ? value : null;
        } catch (ReflectiveOperationException | LinkageError ignored) {
            return null;
        }
    }
}
