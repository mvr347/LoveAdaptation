package me.lovelace.LoveAdaptation.bestiary;

import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class BookFontTest {

    @Test
    void everyWrappedLineFitsTheWidth() {
        String text = "Зомби медленно бредёт по ночным равнинам и сгорает на солнце, если не найдёт тени. "
                + "Щёлкающие, шипящие и ЖУЖЖАЩИЕ твари особенно опасны в пещерах.";
        for (int px : new int[]{80, 108}) {
            List<String> lines = BookFont.wrap(text, px);
            assertFalse(lines.isEmpty());
            for (String l : lines) assertTrue(BookFont.width(l) <= px, l + " = " + BookFont.width(l));
        }
    }

    @Test
    void wrappingKeepsAllWordsInOrder() {
        String text = "один два три четыре пять шесть семь восемь девять десять";
        assertEquals(text, String.join(" ", BookFont.wrap(text, 70)));
    }

    @Test
    void overlongWordIsSplitNotLost() {
        String word = "Длинношеепятнистоголовый";
        List<String> lines = BookFont.wrap(word, 60);
        assertTrue(lines.size() > 1);
        assertEquals(word, String.join("", lines));
        lines.forEach(l -> assertTrue(BookFont.width(l) <= 60));
    }

    @Test
    void paragraphsAndBlankLinesSurvive() {
        assertEquals(List.of("а", "", "б"), BookFont.wrap("а\n\nб", 100));
    }

    @Test
    void separatorIsTrimmedToThePage() {
        String sep = "───────────────";
        String fitted = BookFont.fit(sep, 108);
        assertTrue(BookFont.width(fitted) <= 108);
        assertFalse(fitted.isEmpty());
        assertEquals("abc", BookFont.fit("abc", 100));
    }

    @Test
    void cyrillicIsWiderThanTheOldCharacterBudgetAssumed() {
        // 19 wide Cyrillic letters (the old per-line budget) do not fit a 114 px line.
        assertTrue(BookFont.width("Ж".repeat(19)) > 114);
    }
}
