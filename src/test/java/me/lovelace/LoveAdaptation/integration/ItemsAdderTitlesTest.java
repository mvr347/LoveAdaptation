package me.lovelace.LoveAdaptation.integration;

import static org.junit.jupiter.api.Assertions.*;
import java.util.ArrayList;
import java.util.List;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.TextComponent;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.format.TextColor;
import net.kyori.adventure.text.serializer.plain.PlainTextComponentSerializer;
import org.junit.jupiter.api.Test;

class ItemsAdderTitlesTest {
    static final String FALLBACK = "<gold>Адаптации персонажа</gold>";

    static String plain(Component text) {
        return PlainTextComponentSerializer.plainText().serialize(text);
    }

    record Part(String text, TextColor color) {}

    static void collect(Component c, TextColor inherited, List<Part> parts) {
        TextColor color = c.color() == null ? inherited : c.color();
        if (c instanceof TextComponent t && !t.content().isEmpty())
            parts.add(new Part(t.content(), color));
        c.children().forEach(child -> collect(child, color, parts));
    }

    @Test
    void oldLegacyTitlesRemainReadableWithTheirColors() {
        var title = ItemsAdderTitles.render("&6Свой заголовок", FALLBACK, token -> null);
        assertEquals("Свой заголовок", plain(title));
        assertEquals(NamedTextColor.GOLD, title.color());
        title = ItemsAdderTitles.render("&#123456Текст", FALLBACK, token -> null);
        assertEquals("Текст", plain(title));
        assertEquals(TextColor.fromHexString("#123456"), title.color());
    }

    @Test
    void ordinaryTitlesKeepTheirFormattingAndNeverCallAnIntegration() {
        var title =
                ItemsAdderTitles.render(
                        FALLBACK,
                        FALLBACK,
                        token -> {
                            fail("No image requested");
                            return null;
                        });
        assertEquals("Адаптации персонажа", plain(title));
        assertEquals(NamedTextColor.GOLD, title.color());
    }

    @Test
    void imageAndNegativeOffsetWithLegacySectionCodesDoNotReachMiniMessage() {
        var title =
                ItemsAdderTitles.render(
                        "<white>%img_offset_-8%%img_adaptation_menu%",
                        FALLBACK,
                        token -> token.equals("%img_offset_-8%") ? "\uF808" : "§f\uE020§r");
        assertEquals("\uF808\uE020", plain(title));
        assertFalse(plain(title).contains("§"));
    }

    @Test
    void imageIsWhiteWithoutRecoloringTextAroundIt() {
        var title =
                ItemsAdderTitles.render(
                        "<red>До %img_adaptation_menu% после</red>",
                        FALLBACK, token -> "§c\uE020§r");
        var parts = new ArrayList<Part>();
        collect(title, null, parts);
        assertTrue(
                parts.stream()
                        .anyMatch(
                                p ->
                                        p.text().contains("\uE020")
                                                && NamedTextColor.WHITE.equals(p.color())));
        assertTrue(
                parts.stream()
                        .anyMatch(
                                p ->
                                        p.text().contains("после")
                                                && NamedTextColor.RED.equals(p.color())));
    }

    @Test
    void missingImagesFallBackToAReadableTitleInsteadOfRawPlaceholders() {
        assertEquals(
                "Адаптации персонажа",
                plain(ItemsAdderTitles.render("%img_adaptation_menu%", FALLBACK, token -> null)));
        assertEquals(
                "Адаптации персонажа",
                plain(ItemsAdderTitles.render("%img_adaptation_menu%", FALLBACK, token -> token)));
    }

    @Test
    void replacementTextIsLiteralAndCannotInjectMiniMessageTags() {
        assertEquals(
                "<red>буквально",
                plain(
                        ItemsAdderTitles.render(
                                "%ia_adaptation_menu%", FALLBACK, token -> "<red>буквально")));
    }

    @Test
    void aZeroWidthOffsetMayResolveToAnEmptyString() {
        assertEquals(
                "\uE020",
                plain(
                        ItemsAdderTitles.render(
                                "%img_offset_0%%img_adaptation_menu%",
                                FALLBACK,
                                token -> token.equals("%img_offset_0%") ? "" : "\uE020")));
    }
}
