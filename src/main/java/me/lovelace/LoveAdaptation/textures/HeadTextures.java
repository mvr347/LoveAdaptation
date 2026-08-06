package me.lovelace.LoveAdaptation.textures;

import me.lovelace.LoveAdaptation.models.AdaptationType;

/**
 * Централизованное хранилище base64 текстур голов (skull textures) плагина LoveAdaptation.
 * <p>
 * Каждая текстура здесь совпадает со значением {@code head_texture} по умолчанию в
 * {@code config.yml} и используется как резервный вариант, если админ удалит или очистит
 * соответствующий ключ конфигурации — {@link me.lovelace.LoveAdaptation.managers.AdaptationManager#getHeadTexture}
 * подставляет эти константы вместо пустой строки, чтобы голова адаптации никогда не осталась
 * без текстуры. Сам config.yml остаётся источником истины (админ может переопределить текстуру
 * для каждой адаптации) — это не хардкод вместо конфигурации, а единая точка правды для
 * дефолтных значений, вместо того чтобы дублировать одни и те же base64-строки по коду.
 * <p>
 * Меню {@code gui/adaptation_menu.yml} сюда не перенесено: это файл DeluxeMenus — стороннего
 * плагина, который читает обычный YAML и не может ссылаться на константы Java-класса, поэтому
 * его литералы неизбежно остаются инлайн в самом YAML.
 */
public final class HeadTextures {

    private HeadTextures() {
        // Утилитарный класс-константа, инстанцирование не предполагается
    }

    /**
     * Текстура головы базовой формы (адаптация ещё не выбрана).
     */
    public static final String BASE =
            "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvNTc5YzU1MWQyOGQzY2JjYjUzOTJkMTYyZTI5ZjJjODUzYzJkMTQzNzI3YzE3OWJlODZkNjlmNmY5YTEwNjUifX19";

    /**
     * Текстура головы адаптации «Дельфин» (вода).
     */
    public static final String WATER =
            "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvYjA3ZGJhMjZmMjliOGEzYjcxYzg1ODhjNzZkNDVmMDdkNmU3ZjQ4YjdiODA4YjUxZTJjNTk0OThhYjljMTQ0ZDYifX19";

    /**
     * Текстура головы адаптации «Адепт огня» (Ад).
     */
    public static final String NETHER =
            "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvMjEzYWZiNWJkM2NmNDNlZGRjNjQ1Y2MyNmNhMDNhM2E4MTgwZmJjNGExNGE2MTRlYmFkY2Q1NzFjYjliIn19fQ==";

    /**
     * Текстура головы адаптации «Зрение во мраке» (пещеры).
     */
    public static final String CAVE =
            "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvZDEyNjQ5NDk5YjkzNzU3YTA1YmExODVkMmZjMDAxMzAwYzJiNWI2MjVhMDcyNTkxYzI0YzhlYjQ0ZmY2ODkifX19";

    /**
     * Текстура головы адаптации «Падальщик» (падения).
     */
    public static final String HEIGHT =
            "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvNDg5ZTRkOTY2N2RkOGU5ZTY0MjYxN2RhMGFjMWJkZGY1YTk3ZDE3NzIyYzBhMzk4MGMxMjhiNDVjMTkwNzAifX19";

    /**
     * Текстура головы адаптации «Скиталец Эндера» (Энд).
     */
    public static final String END =
            "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvM2I5ODNlY2VhNTJlM2Y3ODNlNWYxY2Y2YWVkNWI0Yjc2NWVkZjhiNzc0ZDNlNTExM2EzNTAzYWIxYjY5YmEyIn19fQ==";

    /**
     * Текстура головы адаптации «Боевой закал» (бой).
     */
    public static final String COMBAT =
            "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvMmE2ZDM2NjBiN2QxYmMzYTgyM2E4MTJhNjYwZDRmZGMzY2M5NzkxOTNjNDNlNmI5YWIyY2I3YzgxYjdmNzEifX19";

    /**
     * Текстура головы адаптации «Странник» (путешествия).
     */
    public static final String TRAVEL =
            "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvMmU2YTJmNjMxZTUwZTg1MzAwZTFjMjA3YTBjNjJiZDUxYjc5Nzc4OGQwMDkzMTIxY2VmNmQ1ZWNlYTI5ZTNkIn19fQ==";

    /**
     * Возвращает резервную текстуру головы для указанного типа адаптации.
     */
    public static String forType(AdaptationType type) {
        return switch (type) {
            case WATER -> WATER;
            case NETHER -> NETHER;
            case CAVE -> CAVE;
            case HEIGHT -> HEIGHT;
            case END -> END;
            case COMBAT -> COMBAT;
            case TRAVEL -> TRAVEL;
            case BASE -> BASE;
        };
    }
}
