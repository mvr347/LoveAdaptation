package me.lovelace.LoveAdaptation.bestiary;

/**
 * Семейства и классы монстров в Полевом Бестиарии.
 * Монстры одного семейства разделяют общий прогресс исследований и боевые баффы.
 */
public enum MobFamily {
    ZOMBIES("Семейство Немертвых (Зомби)", "Немертвые", "zombie"),
    SKELETONS("Семейство Костяных (Скелеты)", "Костяные", "skeleton"),
    ARTHROPODS("Семейство Членистоногих", "Членистоногие", "arthropod"),
    CREEPERS("Семейство Криперов", "Криперы", "creeper"),
    SLIMES("Семейство Слизеподобных", "Слизеподобные", "slime"),
    ENDERS("Семейство Обитателей Края", "Обитатели Края", "ender"),
    ILLAGERS("Семейство Злыдней (Илладжеры)", "Илладжеры", "illager"),
    PIGLINS("Семейство Пиглинов", "Пиглины", "piglin"),
    HOGLINS("Семейство Вепрей (Хоглины)", "Вепри", "hoglin"),
    ELEMENTALS("Семейство Стихий и Духов", "Стихии и Духи", "elemental"),
    GUARDIANS("Семейство Морских Стражей", "Морские Стражи", "guardian");

    private final String displayName;
    private final String shortName;
    private final String key;

    MobFamily(String displayName, String shortName, String key) {
        this.displayName = displayName;
        this.shortName = shortName;
        this.key = key;
    }

    public String getDisplayName() {
        return displayName;
    }

    public String getShortName() {
        return shortName;
    }

    public String getKey() {
        return key;
    }
}
