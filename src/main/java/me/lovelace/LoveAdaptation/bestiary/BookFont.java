package me.lovelace.LoveAdaptation.bestiary;

import java.util.ArrayList;
import java.util.List;

/**
 * Pixel widths of the default Minecraft font as a written book shows it. A page line is about 114 px
 * wide; counting characters (the old approach) under-counts wide Cyrillic letters and box-drawing
 * separators, so text wrapped later than the client would wrap it and ran off the page.
 *
 * <p>The table is deliberately conservative: where the real advance is unknown the wider value is used,
 * so a line is never longer than estimated. Italic text has the same advances as plain text.
 */
public final class BookFont {

    /** Advance of a glyph the table does not know (unifont fallback: symbols, box drawing, emoji). */
    private static final int UNKNOWN = 9;

    private BookFont() {}

    public static int charWidth(char c) {
        if (c == ' ') return 4;
        if ("i!.,:;|".indexOf(c) >= 0) return 2;
        if ("l'`".indexOf(c) >= 0) return 3;
        if ("It[]".indexOf(c) >= 0) return 4;
        if ("fk(){}*<>\"".indexOf(c) >= 0) return 5;
        if (c == '@' || c == '~') return 7;
        if (c < 128) return c >= 32 ? 6 : 0; // other printable ASCII and digits
        if ((c >= 'А' && c <= 'я') || c == 'Ё' || c == 'ё') {
            return "ЖШЩЮЫЦжшщюыц".indexOf(c) >= 0 ? 7 : 6;
        }
        return UNKNOWN;
    }

    public static int width(String s) {
        if (s == null) return 0;
        int w = 0;
        for (int i = 0; i < s.length(); i++) w += charWidth(s.charAt(i));
        return w;
    }

    /** Longest prefix of {@code s} that fits {@code maxPx}. */
    public static String fit(String s, int maxPx) {
        if (s == null) return "";
        int w = 0;
        for (int i = 0; i < s.length(); i++) {
            w += charWidth(s.charAt(i));
            if (w > maxPx) return s.substring(0, i);
        }
        return s;
    }

    /** Word wrap by pixel width; {@code \n} starts a new paragraph, over-long words are split by characters. */
    public static List<String> wrap(String text, int maxPx) {
        List<String> lines = new ArrayList<>();
        if (text == null || text.trim().isEmpty()) return lines;
        for (String paragraph : text.split("\n", -1)) {
            if (paragraph.isEmpty()) {
                lines.add("");
                continue;
            }
            StringBuilder line = new StringBuilder();
            int lineW = 0;
            for (String word : paragraph.split(" ")) {
                int ww = width(word);
                int need = line.length() == 0 ? ww : lineW + charWidth(' ') + ww;
                if (need <= maxPx) {
                    if (line.length() > 0) {
                        line.append(' ');
                        lineW += charWidth(' ');
                    }
                    line.append(word);
                    lineW += ww;
                    continue;
                }
                if (line.length() > 0) {
                    lines.add(line.toString());
                    line.setLength(0);
                    lineW = 0;
                }
                while (width(word) > maxPx) {
                    String head = fit(word, maxPx);
                    if (head.isEmpty()) break; // a single glyph wider than the line: cannot happen with maxPx >= 9
                    lines.add(head);
                    word = word.substring(head.length());
                }
                line.append(word);
                lineW = width(word);
            }
            if (line.length() > 0) lines.add(line.toString());
        }
        return lines;
    }
}
