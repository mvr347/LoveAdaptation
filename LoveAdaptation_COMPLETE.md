# 📚 LoveAdaptation — ПОЛНАЯ ДОКУМЕНТАЦИЯ

---

## 📖 ВСЕ ДОКУМЕНТЫ

### 1️⃣ **LoveAdaptation_PROMPT.md** (59 KB) ⭐ ОСНОВНОЙ

**Полная техническая спецификация плагина для разработки.**

Содержит:
- ✅ 7 типов адаптаций (WATER, NETHER, CAVE, HEIGHT, END, COMBAT, TRAVELER)
- ✅ Механика прогресса (0-90% базовые эффекты, 90-100% бонусы)
- ✅ Система блокировки (1 активна, остальные 🔒)
- ✅ Деградация и реактивация адаптаций
- ✅ Database schema (SQLite)
- ✅ Event listeners (PLAYER_MOVE, ENTITY_DAMAGE, и т.д.)
- ✅ GUI конфиг (54-слотовое по gui_gen стандарту)
- ✅ Плейсхолдеры PlaceholderAPI
- ✅ Конфиг config.yml (все параметры)
- ✅ Команды (/loveadaptation menu, info, reset, и т.д.)
- ✅ Интеграции (LoveClans, LoveBehavior)

**Использование:** Копируй весь текст в Claude Code / JetBrains AI Assistant для реализации

---

### 2️⃣ **LoveAdaptation_MECHANICS_VISUAL.md** (22 KB) 📊 ВИЗУАЛЬНОЕ ОБЪЯСНЕНИЕ

**Диаграммы и примеры всех механик с ASCII-артом.**

Содержит:
- 📊 Диаграмма прогресса адаптации (0-90-100%)
- 🔒 Диаграмма блокировки (как работает 🔒 ЗАБЛОКИРОВАНА)
- 📉 Диаграмма деградации (временная шкала падения)
- 🔄 Полный цикл переключения адаптаций
- 📋 Примеры плейсхолдеров с результатами
- 🎯 Практические сценарии (для новичков, в пещерах, PvP)
- 📱 Пример GUI меню в реальном виде
- 🔔 Notifications (звуки + сообщения)
- ✅ Контрольный список для разработчика
- 🚨 Common mistakes (частые ошибки)

**Использование:** Показывай это визуальное объяснение если нужно понять механики

---

### 3️⃣ **LoveAdaptation_FUTURE_IDEAS.md** (25 KB) 🚀 ИДЕИ РАСШИРЕНИЯ

**20+ идей для будущих версий плагина.**

Содержит:
- ✅ Идея 1: ADAPTATION PRESTIGE (сброс с бонусом, Lvl 1-5)
- ✅ Идея 2: ADAPTATION TEACHING (обучение соседей +50%)
- ✅ Идея 3: ADAPTATION AFFINITY (совместимость адаптаций)
- ✅ Идея 4: SEASONAL ADAPTATIONS (сезонные: зима, весна, лето, осень)
- ✅ Идея 5: ADAPTATION COOLDOWN (30 сек между переключениями)
- ✅ Идея 6: STREAKS & ACHIEVEMENTS (1+ час в адаптации = бонус)
- ✅ Идея 7: ADAPTATION MEMORY (история и статистика)
- ✅ Идея 8: ADAPTATION DECAY (потеря через 7 дней неиспользования)
- ✅ Идея 9: TALENT TREE (специализация при 90%)
- ✅ Идея 10: DUAL ADAPTATION MODE (синергия двух адаптаций)
- ✅ Идея 11: MUTATION SYSTEM (гибридные адаптации)
- ✅ Идея 12: COMBO CHAIN (активация 3+ подряд = бонус)
- ✅ Идея 13: SOULBIND (связь с душой, не теряется при смерти)
- ✅ Идея 14: PVP BALANCE (отдельная система для PvP)
- ✅ Идея 15: SACRIFICE (пожертвовать адаптацию = кристалл)
- ✅ Идея 16: LEGEND TIER (5 престиж = легенда)
- ✅ Идея 17: LEVELING (уровни адаптации 1-5)
- ✅ Идея 18: LEADERBOARDS (кто первый 90%, лучшие streaks)
- ✅ Идея 19: STATS (подробная статистика)
- ✅ Идея 20: GUILDS (гильдии по типам адаптаций)

Plus: Implementation roadmap (v1.0 → v3.0)

**Использование:** Выбирай идеи для следующих версий

---

### 4️⃣ **LoveAdaptation_QUICK_START.md** (16 KB) ⚡ БЫСТРЫЙ СТАРТ

**Краткое руководство для разработчика перед реализацией.**

Содержит:
- 🎯 3 основные механики v1.0 (кратко)
- 📋 Полный checklist (backend, GUI, commands, config, testing)
- 🗺️ Структура проекта (где какие файлы)
- 🔌 Зависимости (pom.xml)
- 💾 Database queries (готовые SQL)
- 🎨 GUI structure (точные слоты)
- 💾 Startup sequence (код для onEnable)
- 🧪 Тестирование вручную (команды)
- 🚨 Common mistakes (частые ошибки)
- 📞 Интеграции (LoveCore, PlaceholderAPI)
- ✨ Финальный checklist перед деплоем
- 📚 Документация для игроков (wiki)

**Использование:** Читай перед началом разработки!

---

## 🎯 РЕКОМЕНДУЕМЫЙ ПОРЯДОК

### Для **разработчика** (вы):

1. ⭐ Прочитать **LoveAdaptation_QUICK_START.md** (15 минут)
   → Быстрое понимание + checklist

2. 📖 Прочитать **LoveAdaptation_PROMPT.md** полностью (60 минут)
   → Детали реализации

3. 📊 Смотреть **LoveAdaptation_MECHANICS_VISUAL.md** по мере разработки
   → Визуальные диаграммы и проверка механик

4. 🚀 После v1.0 → планировать идеи из **LoveAdaptation_FUTURE_IDEAS.md**

### Для **тестирования** (QA):

1. 📖 **LoveAdaptation_QUICK_START.md** (checklist testing)
2. 📊 **LoveAdaptation_MECHANICS_VISUAL.md** (как должно работать)
3. 🧪 Использовать commands из QUICK_START для тестирования

### Для **игроков** (документация):

1. 📊 **LoveAdaptation_MECHANICS_VISUAL.md** (практические примеры)
2. 🚀 **LoveAdaptation_FUTURE_IDEAS.md** (чего ожидать в будущем)

---

## 📊 СТАТИСТИКА ДОКУМЕНТОВ

| Файл | Размер | Назначение |
|------|--------|-----------|
| **PROMPT.md** | 59 KB | Полная спецификация (для разработки) |
| **MECHANICS_VISUAL.md** | 22 KB | Диаграммы и примеры |
| **FUTURE_IDEAS.md** | 25 KB | 20+ идей расширения |
| **QUICK_START.md** | 16 KB | Быстрый старт + checklist |
| **TOTAL** | **122 KB** | Полная документация |

---

## 🎨 СИСТЕМА АДАПТАЦИЙ v1.0 (КРАТКОЕ РЕЗЮМЕ)

```
7 АДАПТАЦИЙ:
  🌊 WATER      (15 часов в воде)        → SPEED 3 + DOLPHINS_GRACE
  🔥 NETHER     (12 часов в Нижнем мире) → -50% урома + RESISTANCE
  👁 CAVE       (10 часов ниже Y:32)     → ночное видение + враги светятся
  ⬆️ HEIGHT     (10 падений)              → -70% урома от падения
  🎆 END        (8 часов в Энде)         → SLOW_FALLING 2 + JUMP_BOOST
  ⚔️ COMBAT     (100 урома от мобов)     → REGEN 3 + RESISTANCE
  🚶 TRAVELER   (20 км пешком)           → -50% голода + SPEED

ПРОГРЕСС: 0% → 90% (базовые) → 100% (бонусы)

БЛОКИРОВКА: 1 активна ✓, остальные 🔒

ДЕГРАДАЦИЯ: -5% в час (COMBAT: -2%), при <60% теряется

ПЛЕЙСХОЛДЕРЫ: %loveadaptation_*
```

---

## 🔗 КАК ИСПОЛЬЗОВАТЬ ВСЮ ДОКУМЕНТАЦИЮ

### Шаг 1: ПОНИМАНИЕ
```
👁️ Читай QUICK_START (15 мин)
👁️ Смотри MECHANICS_VISUAL диаграммы
👁️ Поймешь основы
```

### Шаг 2: ДЕТАЛИ
```
📖 Читай PROMPT полностью (всё есть там)
📖 Скопируй код в свой проект
📖 Следуй архитектуре
```

### Шаг 3: ТЕСТИРОВАНИЕ
```
🧪 Используй QUICK_START checklist
🧪 Запусти команды на test server
🧪 Проверь все механики
```

### Шаг 4: РАСШИРЕНИЕ (ПОЗЖЕ)
```
🚀 Смотри FUTURE_IDEAS для v1.1+
🚀 Выбирай лучшие идеи
🚀 Реализуй по одной
```

---

## ✨ КЛЮЧЕВЫЕ ОСОБЕННОСТИ v1.0

✅ **7 адаптаций** — вода, огонь, пещеры, высота, эндер, боевая, путешествие  
✅ **Блокировка** — только 1 адаптация активна одновременно  
✅ **Деградация** — упал <90% → теряет прогресс  
✅ **Реактивация** — автоматическое переключение по приоритету  
✅ **Бонусы от 90%** — улучшенные эффекты после разблокировки мастерства  
✅ **GUI по gui_gen** — точный стандарт (54 слота, все слоты либо контент, либо glass)  
✅ **Плейсхолдеры** — интеграция с PlaceholderAPI  
✅ **Зелья адаптации** — поп все эффекты на время  
✅ **Полная конфигурируемость** — zero hardcoding  
✅ **AsyncScheduler** — оптимизированное отслеживание событий  

---

## 📞 ПОДДЕРЖКА ИНТЕГРАЦИЙ

| Плагин | Уровень | Статус |
|--------|---------|--------|
| **LoveCore** | ⭐⭐⭐⭐⭐ | ОБЯЗАТЕЛЕН (AsyncScheduler, ServicesManager) |
| **PlaceholderAPI** | ⭐⭐⭐⭐⭐ | ОБЯЗАТЕЛЕН (плейсхолдеры) |
| **DeluxeMenus** | ⭐⭐⭐⭐ | РЕКОМЕНДУЕТСЯ (GUI) |
| **LoveClans** | ⭐⭐⭐ | ОПЦИОНАЛЬНО (+10% бонус) |
| **LoveBehavior** | ⭐⭐⭐ | ОПЦИОНАЛЬНО (репутация) |
| **LoveShops** | ⭐⭐ | ПЛАНИРУЕТСЯ (торговля зельями) |

---

## 🚀 READY TO GO!

**Вся документация готова к использованию. Начинай разработку!**

1. Открой **LoveAdaptation_QUICK_START.md** ← Начни отсюда
2. Затем весь **LoveAdaptation_PROMPT.md** ← Вся спецификация
3. Используй **LoveAdaptation_MECHANICS_VISUAL.md** ← По мере разработки
4. Планируй идеи из **LoveAdaptation_FUTURE_IDEAS.md** ← После v1.0

---

**Версия документации:** 1.0  
**Дата:** 2026-07-26  
**Статус:** ✅ COMPLETE & READY  
**Размер:** 122 KB полной документации

---

### 💬 КРАТКИЕ НАПОМИНАНИЯ

❓ **Как устроена блокировка?**  
→ Смотри MECHANICS_VISUAL (диаграмма блокировки)

❓ **Как реализовать деградацию?**  
→ Смотри PROMPT (раздел degradation_system в config.yml)

❓ **Какие плейсхолдеры нужны?**  
→ Смотри PROMPT (раздел PLACEHOLDERS)

❓ **Как настроить GUI?**  
→ Смотри PROMPT (раздел 6. MENU ARCHITECTURE) + QUICK_START (GUI STRUCTURE)

❓ **Какие идеи расширения есть?**  
→ Смотри FUTURE_IDEAS (20 идей от простых к сложным)

---

**Успешной разработки! 🚀**
# LoveAdaptation Plugin — Comprehensive Specification & Implementation Prompt

## 🎯 PROJECT OVERVIEW

**Plugin Name:** LoveAdaptation  
**Version:** 1.0.0  
**Target:** Purpur 1.21+  
**Dependencies:** LoveCore (ServicesManager, AsyncScheduler, LangManager), PlaceholderAPI, MiniMessage  
**Architecture:** Paper AsyncScheduler for I/O, SQLite per-plugin database, Bukkit ServicesManager for cross-plugin communication, full zero-hardcoding configurability  

---

## 📋 CORE MECHANICS

### 1. ADAPTATION SYSTEM (адаптации)

Персонаж развивается естественно в зависимости от активности в определённых условиях. Адаптации:
- **Несовместимы** (одна активна за раз)
- **Автоматически переключаются** при смене окружения
- При переходе из адаптации X в адаптацию Y все остальные теряются
- При отсутствии активной адаптации применяется **Base Adaptation** (голова из конфига)

#### 1.1 ADAPTATION TYPES WITH PROGRESSION & DEGRADATION

```yaml
# ===== ОБЩИЕ ПРАВИЛА ПРОГРЕССИРОВАНИЯ =====
# 1. Адаптация прокачивается ДО 90% (максимум)
# 2. От 90% до 100%: БОНУСЫ НАЧИНАЮТ РАБОТАТЬ
#    - SPEED level 2 → SPEED level 3
#    - FIRE_RESISTANCE 40% → 50%
#    - fall_damage_reduction 0.5 → 0.3 (больше снижение)
#    - hunger_modifier 0.7 → 0.5
# 3. АКТИВНАЯ адаптация (>90%) блокирует прокачку других
# 4. Если активная адаптация ПАДАЕТ ниже 90%:
#    - другие адаптации начинают прокачиваться снова
#    - деградация активной адаптации: -5% за каждый час отсутствия условий

WATER_ADAPTATION:
  name: "Дельфин"
  description: "Ускорение в воде и улучшенное дыхание"
  color: "&9"
  unlock_hours: 15
  progress_metric: "ticks_in_water"
  max_progress: 90  # НОВОЕ: максимум 90%, дальше бонусы
  bonus_threshold: 90
  
  # БАЗОВЫЕ ЭФФЕКТЫ (0-89%)
  effects_base:
    - type: SPEED
      level: 2
      apply_only_in: "WATER"
    - type: CONDUIT_POWER
      level: 1
      apply_only_in: "WATER"
  
  # БОНУС-ЭФФЕКТЫ (90-100%)
  effects_bonus:
    - type: SPEED
      level: 3  # Улучшение
      apply_only_in: "WATER"
    - type: DOLPHINS_GRACE
      level: 1  # Новый эффект
      apply_only_in: "WATER"
  
  damage_modifiers_base: {}
  damage_modifiers_bonus: {}
  hunger_modifier_base: 1.0
  hunger_modifier_bonus: 0.9  # 10% меньше голода
  
  # Деградация: -5% прогресса в час, если не в воде
  degradation_rate_percent_per_hour: 5
  degradation_check_interval: 3600  # каждый час
  
  ticks_required_for_100: 54000  # 15 часов на 90%, дальше бонусы

NETHER_ADAPTATION:
  name: "Адепт огня"
  description: "Снижение урона от огня и лавы"
  color: "&c"
  unlock_hours: 12
  progress_metric: "ticks_in_nether"
  max_progress: 90
  bonus_threshold: 90
  
  effects_base:
    - type: FIRE_RESISTANCE
      level: 1
      apply_only_in: "NETHER"
  
  effects_bonus:
    - type: FIRE_RESISTANCE
      level: 2  # Усиление
      apply_only_in: "NETHER"
    - type: RESISTANCE
      level: 1  # Доп защита
      apply_only_in: "NETHER"
  
  fire_damage_reduction_base: 0.4  # 40% reduction
  fire_damage_reduction_bonus: 0.5  # 50% reduction (бонус)
  
  damage_modifiers_base:
    FIRE: 0.6
    LAVA: 0.7
  damage_modifiers_bonus:
    FIRE: 0.5  # Улучшено
    LAVA: 0.5  # Улучшено
  
  degradation_rate_percent_per_hour: 5
  degradation_check_interval: 3600
  
  ticks_required_for_100: 43200  # 12 часов

CAVE_ADAPTATION:
  name: "Зрение во мраке"
  description: "Ночное видение в пещерах и подземелье"
  color: "&8"
  unlock_hours: 10
  progress_metric: "ticks_in_cave"
  max_progress: 90
  bonus_threshold: 90
  
  effects_base:
    - type: NIGHT_VISION
      level: 1
      apply_only_below_y: 32
  
  effects_bonus:
    - type: NIGHT_VISION
      level: 1
      apply_only_below_y: 32
    - type: GLOWING
      level: 1  # Враги светятся при >90%
      apply_only_below_y: 32
  
  degradation_rate_percent_per_hour: 5
  degradation_check_interval: 3600
  
  ticks_required_for_100: 36000  # 10 часов

HEIGHT_ADAPTATION:
  name: "Падальщик"
  description: "Снижение урода от падения"
  color: "&e"
  unlock_falls: 10
  progress_metric: "fall_damage_events"
  max_progress: 90
  bonus_threshold: 90
  
  effects_base: []
  effects_bonus:
    - type: RESISTANCE
      level: 1
      duration_ticks: 20
      trigger: "after_fall_damage"
  
  fall_damage_reduction_base: 0.5  # 50% reduction
  fall_damage_reduction_bonus: 0.3  # 70% reduction (больше бонус)
  
  degradation_rate_percent_per_hour: 5  # -5% если не падаешь
  degradation_check_interval: 3600
  
  ticks_required_for_100: -1  # Event-based (не время)

END_ADAPTATION:
  name: "Скиталец Эндера"
  description: "Плавное падение в Энде"
  color: "&5"
  unlock_hours: 8
  progress_metric: "ticks_in_end"
  max_progress: 90
  bonus_threshold: 90
  
  effects_base:
    - type: SLOW_FALLING
      level: 1
      apply_only_in: "THE_END"
  
  effects_bonus:
    - type: SLOW_FALLING
      level: 2  # Ещё медленнее падение
      apply_only_in: "THE_END"
    - type: JUMP_BOOST
      level: 1  # Выше прыгаешь
      apply_only_in: "THE_END"
  
  degradation_rate_percent_per_hour: 5
  degradation_check_interval: 3600
  
  ticks_required_for_100: 28800  # 8 часов

COMBAT_ADAPTATION:
  name: "Боевой закал"
  description: "Ускоренная регенерация после урона"
  color: "&4"
  unlock_damage: 100
  progress_metric: "damage_taken_from_hostiles"
  max_progress: 90
  bonus_threshold: 90
  
  effects_base:
    - type: REGENERATION
      level: 2
      duration_ticks: 100
      trigger: "after_combat_damage"
  
  effects_bonus:
    - type: REGENERATION
      level: 3  # Быстрее хиление
      duration_ticks: 120
      trigger: "after_combat_damage"
    - type: RESISTANCE
      level: 1
      duration_ticks: 60
      trigger: "after_combat_damage"
  
  healing_boost_base: 1.5  # 150% регенерации
  healing_boost_bonus: 2.0  # 200% регенерации (бонус)
  
  degradation_rate_percent_per_hour: 3  # Медленнее падает (боевая адаптация "запоминается" дольше)
  degradation_check_interval: 3600  # При отсутствии боя
  
  ticks_required_for_100: -1  # Event-based

TRAVELER_ADAPTATION:
  name: "Странник"
  description: "Снижение траты голода при ходьбе"
  color: "&6"
  unlock_km: 20
  progress_metric: "distance_walked"
  max_progress: 90
  bonus_threshold: 90
  
  effects_base: []
  effects_bonus:
    - type: SPEED
      level: 1  # Чуть быстрее ходить
      apply_when_walking: true
  
  hunger_modifier_base: 0.7  # 30% меньше голода
  hunger_modifier_bonus: 0.5  # 50% меньше голода (бонус)
  
  degradation_rate_percent_per_hour: 5  # -5% если не ходить далеко
  degradation_check_interval: 3600
  
  distance_required_for_100: 20000  # 20 км в блоках
```

---

### 1.2 PROGRESSION & BONUS MECHANICS (НОВОЕ)

```yaml
PROGRESSION_SYSTEM:
  # На протяжении адаптации:
  # 0-89%: базовые эффекты работают
  # 90-100%: БОНУСЫ АКТИВИРУЮТСЯ
  
  progression_stages:
    - stage: "early"
      min_percent: 0
      max_percent: 30
      bonus_multiplier: 1.0  # Нет бонусов
      color: "&8"
      indicator: "⬜"
    
    - stage: "mid"
      min_percent: 30
      max_percent: 60
      bonus_multiplier: 1.0
      color: "&7"
      indicator: "⬜"
    
    - stage: "advanced"
      min_percent: 60
      max_percent: 89
      bonus_multiplier: 1.0
      color: "&e"
      indicator: "⬜"
    
    - stage: "mastery"
      min_percent: 90
      max_percent: 100
      bonus_multiplier: 1.3  # 30% усиление эффектов
      color: "&6"
      indicator: "⭐"  # Звёздочка при достижении 90%
  
  # НОВОЕ: Когда адаптация достигает 90%, срабатывает эффект
  mastery_unlock_effect:
    - type: "TITLE"
      message: "&6✨ Мастерство: &f%adaptation_name%"
      subtitle: "&7Бонусы активированы!"
      duration_ticks: 60
    - type: "SOUND"
      sound: "ENTITY_PLAYER_LEVELUP"
      volume: 1.0
      pitch: 1.2

BLOCKING_MECHANICS:
  # АКТИВНАЯ адаптация (>90%) полностью блокирует прокачку других
  active_adaptation_blocks_others: true
  active_threshold: 90
  
  # Когда адаптация активирована:
  # - progress других = 0 (не накапливается)
  # - только активная считает события
  # - плейсхолдер показывает 🔒 для заблокированных
  
  blocking_indicator: "🔒"
  active_indicator: "✓"
  progressing_indicator: "⏳"

DEGRADATION_SYSTEM:
  # Если активная адаптация ПАДАЕТ ниже 90% (условия перестали выполняться):
  # 1. Другие адаптации разблокируются и начинают прокачиваться
  # 2. Активная адаптация деградирует со скоростью degradation_rate_percent_per_hour
  # 3. Если деградирует до <60%: становится неактивной
  
  enabled: true
  check_interval_seconds: 60  # Проверка каждую минуту
  
  # Деградация происходит только если УСЛОВИЯ НЕ ВЫПОЛНЯЮТСЯ
  # Условие: вода → если не в воде, деградирует
  # Условие: Нижний мир → если не в NETHER, деградирует
  
  examples:
    - adaptation: "WATER"
      condition: "player_in_water == false"
      degradation_rate: 5  # -5% в час
      
    - adaptation: "NETHER"
      condition: "player_in_nether == false"
      degradation_rate: 5
      
    - adaptation: "COMBAT"
      condition: "time_since_combat > 3600"  # Если 1 час без боя
      degradation_rate: 3  # Медленнее падает

REACTIVATION_LOGIC:
  # Когда активная адаптация падает ниже 90%:
  # 1. Срабатывает REACTIVATION CHECK
  # 2. Смотрим какие условия сейчас выполняются
  # 3. Активируем адаптацию с МАКСИМАЛЬНЫМ прогрессом (не <90%)
  # 4. Остальные адаптации разблокируются для прокачки
  
  enabled: true
  check_interval_seconds: 30
  
  priority_order:
    - NETHER  # Самый высокий приоритет
    - THE_END
    - CAVE
    - WATER
    - HEIGHT
    - COMBAT
    - TRAVELER  # Самый низкий приоритет (всегда можно прокачивать)
```

---

### 1.3 DATABASE SCHEMA (ОБНОВЛЕННАЯ)

```sql
CREATE TABLE IF NOT EXISTS player_adaptations (
    uuid TEXT PRIMARY KEY,
    current_adaptation VARCHAR(50),
    current_progress_percent FLOAT DEFAULT 0,
    is_mastery_unlocked BOOLEAN DEFAULT 0,  -- достиг ли 90%
    mastery_unlock_time BIGINT,  -- когда разблокирован мастерству
    last_condition_check BIGINT,  -- последняя проверка условий для адаптации
    last_degradation_check BIGINT,  -- последняя проверка деградации
    created_at BIGINT,
    updated_at BIGINT
);

CREATE TABLE IF NOT EXISTS adaptation_progress (
    id INTEGER PRIMARY KEY AUTOINCREMENT,
    uuid TEXT NOT NULL,
    adaptation_name VARCHAR(50),
    progress_percent FLOAT DEFAULT 0,  -- 0-100
    progress_type VARCHAR(50),  -- 'ticks', 'events', 'distance'
    progress_value LONG DEFAULT 0,  -- фактическое значение (тики, события, блоки)
    is_unlocked BOOLEAN DEFAULT 0,  -- когда-либо достигал 90%?
    is_active BOOLEAN DEFAULT 0,  -- сейчас активна?
    unlocked_at BIGINT,  -- когда достиг 90%
    activated_at BIGINT,  -- когда активировалась
    deactivated_at BIGINT,  -- когда деактивировалась
    mastery_benefits_applied BOOLEAN DEFAULT 0,  -- применены ли бонусы?
    created_at BIGINT,
    updated_at BIGINT,
    UNIQUE(uuid, adaptation_name),
    FOREIGN KEY(uuid) REFERENCES player_adaptations(uuid)
);

CREATE TABLE IF NOT EXISTS adaptation_history (
    id INTEGER PRIMARY KEY AUTOINCREMENT,
    uuid TEXT NOT NULL,
    adaptation_name VARCHAR(50),
    event_type VARCHAR(50),  -- 'PROGRESS', 'MASTERY_UNLOCK', 'ACTIVATED', 'DEACTIVATED', 'DEGRADED', 'BLOCKED'
    event_percent FLOAT,  -- прогресс при событии
    event_timestamp BIGINT,
    notes TEXT,  -- доп информация (например, почему деградировало)
    FOREIGN KEY(uuid) REFERENCES player_adaptations(uuid)
);

CREATE TABLE IF NOT EXISTS potion_effects (
    uuid TEXT NOT NULL,
    potion_type VARCHAR(20),  -- 'UNIVERSAL_POTION_I', 'UNIVERSAL_POTION_II'
    applied_at BIGINT,
    expires_at BIGINT,
    FOREIGN KEY(uuid) REFERENCES player_adaptations(uuid)
);
```


---

### 2. PROGRESS TRACKING & ACCUMULATION

#### 2.1 Database Schema (SQLite)

```sql
CREATE TABLE IF NOT EXISTS player_adaptations (
    uuid TEXT PRIMARY KEY,
    current_adaptation VARCHAR(50),
    last_biome_check BIGINT,
    last_location_x DOUBLE,
    last_location_y DOUBLE,
    last_location_z DOUBLE,
    created_at BIGINT,
    updated_at BIGINT
);

CREATE TABLE IF NOT EXISTS adaptation_progress (
    id INTEGER PRIMARY KEY AUTOINCREMENT,
    uuid TEXT NOT NULL,
    adaptation_name VARCHAR(50),
    progress LONG DEFAULT 0,
    progress_type VARCHAR(50),  -- 'ticks', 'events', 'distance'
    progress_percentage FLOAT,
    is_unlocked BOOLEAN DEFAULT 0,
    unlocked_at BIGINT,
    UNIQUE(uuid, adaptation_name),
    FOREIGN KEY(uuid) REFERENCES player_adaptations(uuid)
);

CREATE TABLE IF NOT EXISTS adaptation_effects (
    uuid TEXT NOT NULL,
    current_adaptation VARCHAR(50),
    effect_name VARCHAR(50),
    effect_level INT,
    applied_at BIGINT,
    FOREIGN KEY(uuid) REFERENCES player_adaptations(uuid)
);

CREATE TABLE IF NOT EXISTS potion_effects (
    uuid TEXT NOT NULL,
    potion_type VARCHAR(20),  -- UNIVERSAL_POTION_I, UNIVERSAL_POTION_II
    applied_at BIGINT,
    expires_at BIGINT,
    FOREIGN KEY(uuid) REFERENCES player_adaptations(uuid)
);
```

#### 2.2 Event Listeners (AsyncScheduler)

**Water Tracking:**
- PLAYER_MOVE: Проверка если игрок в воде → increment `ticks_in_water`
- PLAYER_LEAVE_WATER: Сохранить прогресс в БД
- Frequency: каждый tick (асинхронно)

**Nether Tracking:**
- PLAYER_CHANGED_WORLD: Проверка если в NETHER
- PLAYER_MOVE: increment `ticks_in_nether`
- Frequency: каждый tick

**Cave Tracking (Y < 32):**
- PLAYER_MOVE: Проверка высоту, if Y < 32 → increment `ticks_in_cave`
- Frequency: каждый tick (но проверка 1x в 5 секунд для оптимизации)

**Height Tracking:**
- ENTITY_DAMAGE (cause=FALL): Capture event, increment `fall_damage_events`, check if >= 10 → unlock

**End Tracking:**
- PLAYER_CHANGED_WORLD: Проверка если в THE_END
- PLAYER_MOVE: increment `ticks_in_end`

**Combat Tracking:**
- ENTITY_DAMAGE (from mobs/players): Summate урона в `damage_taken_from_hostiles`
- Apply REGENERATION effect если адаптация активна

**Distance Tracking:**
- PLAYER_MOVE: Вычислить дельту (new_loc - last_loc), add to distance
- Сохранять каждые 100м

---

### 3. ADAPTATION SWITCHING LOGIC

```java
// Pseudocode для определения текущей адаптации
public void checkAndSwitchAdaptation(Player player) {
    String currentAdaptation = getPlayerCurrentAdaptation(player);
    String requiredAdaptation = determineRequiredAdaptation(player);
    
    if (!requiredAdaptation.equals(currentAdaptation)) {
        // Несовместимость: remove all progress except new one
        removeAllAdaptationProgressExcept(player, requiredAdaptation);
        setPlayerAdaptation(player, requiredAdaptation);
        
        // Apply effects асинхронно
        applyAdaptationEffects(player, requiredAdaptation);
    }
    
    // Apply passive effects (hunger, damage reduction)
    applyAdaptationPassives(player, requiredAdaptation);
}

public String determineRequiredAdaptation(Player player) {
    // Priority (в порядке проверки):
    // 1. NETHER (highest priority — if in NETHER → NETHER)
    // 2. THE_END (if in THE_END → END)
    // 3. CAVE (if Y < 32 → CAVE)
    // 4. WATER (if in water → WATER)
    // 5. TRAVEL (if moved > threshold → TRAVEL)
    // 6. COMBAT (if in recent combat → COMBAT)
    // 7. BASE_ADAPTATION (fallback)
}
```

---

### 4. EFFECTS & PASSIVES

#### 4.1 Active Effects (из конфига)
- Применяются через `player.addPotionEffect(PotionEffect)`
- Duration: permanent, но переприменяются каждый tick
- Условие: `apply_only_in` (WATER, NETHER, THE_END, Y < 32)

#### 4.2 Passive Modifiers
- **Hunger Loss:** При выборе TRAVELER → hunger_modifier = 0.7
  - On PLAYER_MOVE: `hungerlevel -= 1 * (1 - modifier)`
  
- **Damage Reduction:** NETHER (40%), HEIGHT (50%)
  - On ENTITY_DAMAGE: `event.setDamage(event.getDamage() * reduction_factor)`

- **Healing Boost:** COMBAT (150%)
  - On REGENERATION: `health += (regen_level / 5) * 1.5`

---

## 🧪 POTION SYSTEM

### 5. POTION OF ADAPTATION

**Craftable via:**
- Дефолт рецепт (за конфигом) или NPC торговец (если интегрируется с LoveShops)

**Types:**
```yaml
ADAPTATION_POTION_I:
  name: "Зелье адаптации"
  description: "1 минута всех адаптаций одновременно"
  duration_ticks: 1200  # 60 сек
  apply_all_adaptations: true
  
ADAPTATION_POTION_II:
  name: "Усиленное зелье адаптации"
  description: "2 минуты всех адаптаций одновременно"
  duration_ticks: 2400  # 120 сек
  apply_all_adaptations: true
```

**Mechanics:**
- Drink: все эффекты из всех адаптаций применяются одновременно (SPEED от WATER + FIRE_RESISTANCE от NETHER + NIGHT_VISION от CAVE и т.д.)
- Duration: по конфигу (1 мин для уровня I, 2 мин для II)
- После истечения: вернуться к текущей активной адаптации

---

## 🎨 GUI MENU (54-слотовое, DeluxeMenus YAML)

### 6. MENU ARCHITECTURE

#### 6.1 Main Menu (54 слота)

**Слот 0:** Базовая голова адаптации (системная голова в Base64, зависит от текущей адаптации)
- При WATER: голова воды
- При NETHER: голова огня
- При CAVE: голова глаза
- При BASE: конфиг-заданная голова

**Слоты 2-7:** Вкладки
- Слот 2: "Адаптации" (текущая)
- Слот 3: "Мутации" (заглушка "🔒 Скоро...")
- Слоты 4-7: резерв или доп информация

**Рабочая зона (18-44):** 21 слот для адаптаций
- **Строка 1 (19-25, 7 слотов):** Активные адаптации (3-4 штуки с max прогрессом)
- **Строка 2 (28-34, 7 слотов):** В процессе (3-4 адаптации с прогрессом)
- **Строка 3 (37-43, 7 слотов):** Заблокированные (серо)

**Footer (45-53):**
- 45-50: Стекло
- 51: Доп кнопка (например, "Инфо") - optional
- 52: Back или Стекло
- 53: Close (обязательно)

#### 6.2 YAML Configuration (DeluxeMenus)

```yaml
menu_title: '&6Адаптации персонажа'
size: 54

## Параметры обновления
refresh_interval: 20  # обновление каждый tick (1 сек)

items:
  # ===== СЛОТ 0: Голова адаптации (системная) =====
  'adaptation_head':
    material: 'basehead-eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvZWMyZjc3ZjY1ZDQ1ZDUwNGQ0OWIyNjc5ZjcwMjMwNDExMjQ3YjMyNGU2NzMwYjI5MzJlNjA4YTY4MDhkMWYzIn19fQ=='
    slot: 0
    display_name: '&6Система адаптаций'
    lore:
      - ''
      - '&7Текущая: %loveadaptation_current_name%'
      - '&7Прогресс: %loveadaptation_progress_percent%'
      - ''
      - '&7Адаптируйся естественно'
      - '&7К окружению вокруг тебя'

  # ===== СЛОТЫ 1 & 8: GLASS BARRIER =====
  'glass_left':
    material: GRAY_STAINED_GLASS_PANE
    display_name: ' '
    slot: 1

  'glass_right':
    material: GRAY_STAINED_GLASS_PANE
    display_name: ' '
    slot: 8

  # ===== ВКЛАДКИ (Слоты 2-7) =====
  'tab_adaptations':
    material: 'basehead-eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvMmU2YTJmNjMxZTUwZTg1MzAwZTFjMjA3YTBjNjJiZDUxYjc5Nzc4OGQwMDkzMTIxY2VmNmQ1ZWNlYTI5ZTNkIn19fQ=='
    slot: 2
    display_name: '&aАдаптации &7(вкл)'
    lore:
      - ''
      - '&7ЛКМ - список адаптаций'

  'tab_mutations':
    material: 'basehead-eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvZDEyNjQ5NDk5YjkzNzU3YTA1YmExODVkMmZjMDAxMzAwYzJiNWI2MjVhMDcyNTkxYzI0YzhlYjQ0ZmY2ODkifX19'
    slot: 3
    display_name: '&8Мутации &7(вкл)'
    lore:
      - ''
      - '&c🔒 Скоро...'
      - '&7Система мутаций в разработке'

  # ===== СТЕКЛО: Слоты 9-17 =====
  'glass_top_row':
    material: GRAY_STAINED_GLASS_PANE
    display_name: ' '
    slots: [9, 10, 11, 12, 13, 14, 15, 16, 17]

  # ===== РАБОЧАЯ ЗОНА (18-44): СТЕНКИ =====
  'glass_wall_left':
    material: GRAY_STAINED_GLASS_PANE
    display_name: ' '
    slots: [18, 27, 36]

  'glass_wall_right':
    material: GRAY_STAINED_GLASS_PANE
    display_name: ' '
    slots: [26, 35, 44]

  # ===== КОНТЕНТ: Адаптации (PLACEHOLDER для динамических) =====
  # Используем dynamic item pattern, но предоставляем примеры
  
  'adaptation_water':
    material: 'basehead-eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvYjA3ZGJhMjZmMjliOGEzYjcxYzg1ODhjNzZkNDVmMDdkNmU3ZjQ4YjdiODA4YjUxZTJjNTk0OThhYjljMTQ0ZDYifX19'
    slot: 19
    display_name: '&9Дельфин'
    lore:
      - ''
      - '&7Ускорение в воде'
      - '&7Улучшенное дыхание'
      - ''
      - '&7Прогресс: &f%loveadaptation_water_progress%'
      - '&7Статус: &f%loveadaptation_water_status%'
      - ''
      - '&eЛКМ - подробнее'

  # Дублировать для каждой адаптации (NETHER, CAVE, HEIGHT, END, COMBAT, TRAVEL)
  # Плейсхолдеры: %loveadaptation_{name}_progress%, %loveadaptation_{name}_status%

  # ===== FOOTER: Стекло & Кнопки =====
  'glass_footer':
    material: GRAY_STAINED_GLASS_PANE
    display_name: ' '
    slots: [45, 46, 47, 48, 49, 50]

  'info_button':
    material: 'basehead-eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvNDMyMmE1NzY1NDA2YWU0YTg0YzFlMGZiYjkxODMzN2JmMzU0ZWM3ZWJkODllNDE3MzI1MzljZjhkZTQ1OTA5ZiJ9fX0='
    slot: 51
    display_name: '&bИнформация'
    lore:
      - ''
      - '&7ЛКМ - справка по адаптациям'

  'close_button':
    material: 'basehead-eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvM2VkMWFiYTczZjYzOWY0YmM0MmJkNDgxOTZjNzE1MTk3YmUyNzEyYzNiOTYyYzk3ZWJmOWU5ZWQ4ZWZhMDI1In19fQ=='
    slot: 53
    display_name: '&cЗакрыть'
    left_click_commands:
      - '[close]'
```

#### 6.3 Mutation Menu (Tab — заглушка)

```yaml
menu_title: '&6Мутации персонажа'
size: 54

items:
  'mutation_head':
    material: 'basehead-...'  # Голова мутации (другая)
    slot: 0
    display_name: '&6Система мутаций'
    lore:
      - ''
      - '&8🔒 ЗАКРЫТО'
      - ''
      - '&7Мутации — продвинутая эволюция'
      - '&7Персонажа через стечение адаптаций'
      - ''
      - '&7Добавлено: &fскоро'

  'coming_soon':
    material: 'basehead-...'
    slot: 22
    display_name: '&c🔒 Это в разработке'
    lore:
      - ''
      - '&7Система мутаций появится'
      - '&7В будущих обновлениях'
      - ''
      - '&aПосле: стабилизация адаптаций'

  # Остальное: glass + close
```

---

## 🔖 PLACEHOLDERS (PlaceholderAPI)

```
%loveadaptation_current_name%
  → Возвращает: "Дельфин", "Адепт огня", "Странник", и т.д.
  → Fallback: "Адаптация не активна" / "Базовая форма"

%loveadaptation_current_color%
  → Возвращает: &9, &c, &6, и т.д. (color code)

%loveadaptation_current_progress%
  → Возвращает: "45%", "92% ⭐" (звёздочка при 90%+)

%loveadaptation_progress_percent%
  → DEPRECATED: используй %loveadaptation_current_progress%
  → Возвращает: "45%", "100%", и т.д.

%loveadaptation_progress_next_adaptation%
  → Возвращает: "Странник (67%)" — ближайшая адаптация к разблокировке (не заблокированная)

%loveadaptation_{adaptation_name}_progress%
  → Примеры: 
  → %loveadaptation_water_progress% → "45%"
  → %loveadaptation_water_progress% → "92% ⭐" (при достижении 90%+)
  → %loveadaptation_nether_progress% → "100% ✓" (полная активная)

%loveadaptation_{adaptation_name}_status%
  → Возвращает одно из:
  → "✓ Активна" — адаптация сейчас активна и >90%
  → "⏳ Прокачка (45%)" — адаптация прокачивается
  → "⭐ Мастерство (95%)" — достигнута 90%, применяются бонусы
  → "🔒 Заблокирована" — другая адаптация активна, эта заблокирована
  → "📉 Деградирует (75%)" — активная адаптация падает ниже 90%
  → "🔐 Не разблокирована" — ещё не достигал 90%

%loveadaptation_{adaptation_name}_indicator%
  → Возвращает символ:
  → "✓" если активна
  → "⭐" если 90%+
  → "⏳" если прокачивается
  → "🔒" если заблокирована
  → "📉" если деградирует
  → "⬜" если <90%

%loveadaptation_{adaptation_name}_next_bonus%
  → Возвращает описание следующего бонуса:
  → "Достигни 90% для SPEED уровня 3"
  → "Достигни 90% для -50% урона от огня"

%loveadaptation_blocked_count%
  → Возвращает: количество заблокированных адаптаций ("3/7 заблокировано")

%loveadaptation_potion_cooldown%
  → Возвращает: оставшееся время зелья адаптации или "Доступно"
  → Пример: "⏱️ 1:23 (зелье действует)" или "✓ Доступно"

%loveadaptation_base_head_uuid%
  → Возвращает: UUID базовой головы (для конфига)

%loveadaptation_stages%
  → Возвращает прогресс по стадиям:
  → "⬜⬜⭐⭐⭐⭐⭐" (ранняя, средняя, продвинутая, мастерство...)

%loveadaptation_degradation_risk%
  → Возвращает риск деградации текущей адаптации:
  → "❌ Риск деградации: вышел из воды 5 минут назад"
  → "✅ Безопасно: ещё 55 минут без условий"
```

---

## ⚙️ CONFIGURATION (config.yml)

```yaml
# LoveAdaptation v1.0.0 Configuration

plugin:
  name: "LoveAdaptation"
  version: "1.0.0"
  author: "Love* Ecosystem"

# ===== DATABASE =====
database:
  type: "sqlite"
  file: "plugins/LoveAdaptation/data.db"
  auto_backup: true
  backup_interval_hours: 6

# ===== BASE ADAPTATION =====
base_adaptation:
  # Если игрок не имеет активной адаптации
  enabled: true
  head_texture: "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvNTc5YzU1MWQyOGQzY2JjYjUzOTJkMTYyZTI5ZjJjODUzYzJkMTQzNzI3YzE3OWJlODZkNjlmNmY5YTEwNjUifX19"
  name: "Базовая форма"
  color: "&7"

# ===== ADAPTATIONS CONFIG (С БОНУСАМИ И ДЕГРАДАЦИЕЙ) =====
adaptations:
  water:
    enabled: true
    unlock_hours: 15
    max_progress_percent: 90  # НОВОЕ: максимум 90%, дальше бонусы
    bonus_threshold_percent: 90  # От этого процента включаются бонусы
    
    # Базовые эффекты (0-89%)
    effects_base:
      - type: "SPEED"
        level: 2
        apply_in: ["WATER"]
      - type: "CONDUIT_POWER"
        level: 1
        apply_in: ["WATER"]
    
    # Бонус-эффекты (90-100%)
    effects_bonus:
      - type: "SPEED"
        level: 3  # Улучшение с 2 на 3
        apply_in: ["WATER"]
      - type: "DOLPHINS_GRACE"
        level: 1  # Новый эффект!
        apply_in: ["WATER"]
    
    hunger_modifier_base: 1.0
    hunger_modifier_bonus: 0.9  # Чуть меньше голода при мастерстве
    
    # Деградация: если не в воде, теряет -5% в час
    degradation_enabled: true
    degradation_rate_percent_per_hour: 5
    degradation_check_interval_seconds: 60
    
    head_texture: "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvYjA3ZGJhMjZmMjliOGEzYjcxYzg1ODhjNzZkNDVmMDdkNmU3ZjQ4YjdiODA4YjUxZTJjNTk0OThhYjljMTQ0ZDYifX19"

  nether:
    enabled: true
    unlock_hours: 12
    max_progress_percent: 90
    bonus_threshold_percent: 90
    
    effects_base:
      - type: "FIRE_RESISTANCE"
        level: 1
        apply_in: ["NETHER"]
    
    effects_bonus:
      - type: "FIRE_RESISTANCE"
        level: 2  # Усиление
        apply_in: ["NETHER"]
      - type: "RESISTANCE"
        level: 1  # Доп защита
        apply_in: ["NETHER"]
    
    fire_damage_reduction_base: 0.4  # 40% снижение базово
    fire_damage_reduction_bonus: 0.5  # 50% при мастерстве
    
    degradation_enabled: true
    degradation_rate_percent_per_hour: 5
    degradation_check_interval_seconds: 60
    
    head_texture: "..."

  cave:
    enabled: true
    unlock_hours: 10
    unlock_at_y: 32
    max_progress_percent: 90
    bonus_threshold_percent: 90
    
    effects_base:
      - type: "NIGHT_VISION"
        level: 1
        apply_at_y_below: 32
    
    effects_bonus:
      - type: "NIGHT_VISION"
        level: 1
        apply_at_y_below: 32
      - type: "GLOWING"
        level: 1  # Враги светятся!
        apply_at_y_below: 32
    
    degradation_enabled: true
    degradation_rate_percent_per_hour: 5
    degradation_check_interval_seconds: 60
    
    head_texture: "..."

  height:
    enabled: true
    unlock_falls: 10
    max_progress_percent: 90
    bonus_threshold_percent: 90
    
    effects_base: []
    
    effects_bonus:
      - type: "RESISTANCE"
        level: 1
        duration_ticks: 20
        trigger: "after_fall_damage"
    
    fall_damage_reduction_base: 0.5  # 50% базово
    fall_damage_reduction_bonus: 0.3  # 70% при мастерстве (больше снижение)
    
    degradation_enabled: true
    degradation_rate_percent_per_hour: 5  # -5% если не падаешь 1 час
    degradation_check_interval_seconds: 60
    
    head_texture: "..."

  end:
    enabled: true
    unlock_hours: 8
    max_progress_percent: 90
    bonus_threshold_percent: 90
    
    effects_base:
      - type: "SLOW_FALLING"
        level: 1
        apply_in: ["THE_END"]
    
    effects_bonus:
      - type: "SLOW_FALLING"
        level: 2  # Ещё медленнее
        apply_in: ["THE_END"]
      - type: "JUMP_BOOST"
        level: 1  # Выше прыгаешь
        apply_in: ["THE_END"]
    
    degradation_enabled: true
    degradation_rate_percent_per_hour: 5
    degradation_check_interval_seconds: 60
    
    head_texture: "..."

  combat:
    enabled: true
    unlock_damage: 100
    max_progress_percent: 90
    bonus_threshold_percent: 90
    
    effects_base:
      - type: "REGENERATION"
        level: 2
        duration_ticks: 100
        trigger: "after_combat_damage"
    
    effects_bonus:
      - type: "REGENERATION"
        level: 3  # Быстрее хилится
        duration_ticks: 120
        trigger: "after_combat_damage"
      - type: "RESISTANCE"
        level: 1
        duration_ticks: 60
        trigger: "after_combat_damage"
    
    healing_boost_base: 1.5  # 150% регенерации базово
    healing_boost_bonus: 2.0  # 200% при мастерстве
    
    # Боевая адаптация деградирует медленнее (запоминается дольше)
    degradation_enabled: true
    degradation_rate_percent_per_hour: 2  # Только -2% в час без боя (медленнее!)
    degradation_check_interval_seconds: 60
    
    head_texture: "..."

  travel:
    enabled: true
    unlock_km: 20
    max_progress_percent: 90
    bonus_threshold_percent: 90
    
    effects_base: []
    
    effects_bonus:
      - type: "SPEED"
        level: 1  # Чуть быстрее ходить
        apply_when_walking: true
    
    hunger_modifier_base: 0.7  # 30% меньше голода базово
    hunger_modifier_bonus: 0.5  # 50% меньше при мастерстве
    
    degradation_enabled: true
    degradation_rate_percent_per_hour: 5  # -5% если долго не ходить
    degradation_check_interval_seconds: 60
    
    head_texture: "..."

# ===== POTIONS =====
potions:
  adaptation_potion_i:
    enabled: true
    duration_seconds: 60
    name: "&eЗелье адаптации I"
    apply_all_adaptations: true
    # Рецепт за DeluxeMenus или Bukkit (если реализуется)

  adaptation_potion_ii:
    enabled: true
    duration_seconds: 120
    name: "&eУсиленное зелье адаптации II"
    apply_all_adaptations: true

# ===== BLOCKING & DEGRADATION SYSTEM =====
progression_blocking:
  enabled: true
  active_adaptation_blocks_others: true  # Активная адаптация блокирует прокачку других
  active_threshold_percent: 90  # От этого % считается активной
  
  # Когда адаптация активна (>90%):
  # - progress других адаптаций = 0 (не накапливается)
  # - плейсхолдер показывает 🔒 для заблокированных
  # - в GUI видно "Заблокирована" для не-активных
  
  blocking_indicator: "🔒"
  active_indicator: "✓"
  progressing_indicator: "⏳"
  mastery_indicator: "⭐"
  degrading_indicator: "📉"

degradation_system:
  enabled: true
  check_interval_seconds: 60  # Проверка деградации каждую минуту
  
  # Деградация если активная адаптация падает <90% (условия не выполняются)
  # Пример: если WATER адаптация активна на 95%, но игрок вышел из воды:
  # - через 1 час: падает до 90%
  # - через 2 часа: падает до 85%
  # - через 3 часа: падает до 80% и деактивируется
  
  reactivation_check_interval_seconds: 30  # Проверять каждые 30 сек можно ли активировать другую
  
  # Когда адаптация переходит в режим деградации (падает <90%):
  # - Другие адаптации разблокируются и начинают прокачиваться снова
  # - Деградирующая адаптация теряет прогресс по установленному rate
  # - Если упала ниже 60%: полностью деактивируется и становится доступной для прокачки
  
  deactivation_threshold_percent: 60  # Ниже 60% = полностью неактивна

reactivation_priority:
  # Когда активная адаптация падает <90%, выбираем следующую адаптацию по приоритету
  # Приоритет: где условия выполняются И прогресс максимален
  order:
    - NETHER  # Высокий приоритет
    - THE_END
    - CAVE
    - WATER
    - HEIGHT
    - COMBAT
    - TRAVELER  # Низкий приоритет (всегда можно прокачивать)

# ===== TRACKING & PERFORMANCE =====
tracking:
  check_interval_ticks: 1  # каждый tick (базовое отслеживание)
  cave_check_interval_ticks: 100  # каждые 5 сек (оптимизация для Y-check)
  distance_tracking_threshold_blocks: 100  # сохранять БД каждые 100м
  enable_debug_logging: false

# ===== NOTIFICATIONS =====
notifications:
  mastery_unlock:  # Когда адаптация достигает 90%
    enabled: true
    title: "&6✨ Мастерство разблокировано"
    subtitle: "&f%adaptation_name%"
    actionbar: "&7Бонусы активированы: &f%bonus_description%"
    sound: "ENTITY_PLAYER_LEVELUP"
    sound_volume: 1.0
    sound_pitch: 1.2
  
  adaptation_activated:  # Когда адаптация становится активной
    enabled: true
    actionbar: "&aАдаптация активна: &f%adaptation_name%"
    sound: "BLOCK_RESPAWN_ANCHOR_CHARGE"
    sound_volume: 0.8
    sound_pitch: 1.0
  
  adaptation_degrading:  # Когда адаптация начинает падать
    enabled: true
    actionbar: "&c⚠ Адаптация деградирует: &f%adaptation_name% (%progress%)"
    sound: "ENTITY_GENERIC_HURT"
    sound_volume: 0.5
    sound_pitch: 0.8
  
  adaptation_deactivated:  # Когда адаптация полностью теряется
    enabled: true
    actionbar: "&cАдаптация потеряна: &f%adaptation_name%"
    sound: "ENTITY_GENERIC_DEATH"
    sound_volume: 0.5
    sound_pitch: 0.5

# ===== LANG (MiniMessage) =====
lang:
  locale: "ru_RU"
  # Все строки хранятся в отдельном lang файле
  # Примеры:
  # adaptation_unlocked: "&aТы разблокировал адаптацию: &f%adaptation_name%"
  # adaptation_switched: "&7Адаптация изменена на &f%adaptation_name%"
  # potion_used: "&eТы использовал зелье адаптации на &f%duration% сек"
```

---

## 🎯 COMMAND STRUCTURE

```
/loveadaptation [subcommand]

/loveadaptation menu
  → Открыть GUI меню адаптаций

/loveadaptation info [player_name]
  → Показать инфо о текущей адаптации игрока (ActionBar + Chat)

/loveadaptation reset <player_name>  [perm: loveadaptation.admin]
  → Сбросить все адаптации игрока

/loveadaptation check <adaptation_name>  [perm: loveadaptation.admin]
  → Отладка: проверить текущий прогресс адаптации

/loveadaptation reload  [perm: loveadaptation.admin]
  → Перезагрузить конфиг + БД
```

---

## 🔌 INTEGRATION POINTS (ServicesManager)

### Integration with LoveClans (optional)
- Если игрок состоит в клане: бонус +10% к скорости разблокировки адаптаций
- Используется LoveClans API через ServicesManager

### Integration with LoveBehavior (optional)
- Политичность (Politeness): игроки с низкой вежливостью медленнее разблокируют COMBAT адаптацию
- Используется LoveBehavior API через ServicesManager

### Integration with LoveShops (future)
- Зелья адаптации продаются у NPC (Auctioneer или Buyer)

---

## 🎮 GAME MECHANICS CLARIFICATION (ТЕКУЩАЯ СИСТЕМА v1.0)

### PROGRESSION STAGES

```
0%        → 90%: БАЗОВЫЕ ЭФФЕКТЫ
┣━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━┫

90%       → 100%: БОНУСЫ ВКЛЮЧЕНЫ
┣━━━━━━━━━━━━━━━━━━━━━━━┫

100%      → MAX: "МАСТЕРСТВО ПОЛНОЕ"
```

**Примеры бонусов (от 90%):**
- WATER: SPEED 2→3, добавляется DOLPHINS_GRACE
- NETHER: FIRE_RESISTANCE 40%→50%, добавляется RESISTANCE
- CAVE: добавляется GLOWING (враги светятся)
- HEIGHT: падение на 50%→70% меньше урона
- END: SLOW_FALLING 1→2, добавляется JUMP_BOOST
- COMBAT: REGENERATION 2→3, healing_boost 150%→200%
- TRAVELER: hunger_modifier 0.7→0.5 (-50% голода)

### BLOCKING MECHANICS

```
АКТИВНАЯ АДАПТАЦИЯ (>90%):
┌─────────────────────────────────┐
│ WATER (95%) ✓ АКТИВНА           │
├─────────────────────────────────┤
│ NETHER (70%) 🔒 ЗАБЛОКИРОВАНА   │ ← Не прокачивается!
│ CAVE (65%)   🔒 ЗАБЛОКИРОВАНА   │ ← Не прокачивается!
│ HEIGHT (80%) 🔒 ЗАБЛОКИРОВАНА   │ ← Не прокачивается!
│ END (55%)    🔒 ЗАБЛОКИРОВАНА   │ ← Не прокачивается!
│ COMBAT (75%) 🔒 ЗАБЛОКИРОВАНА   │ ← Не прокачивается!
│ TRAVEL (45%) 🔒 ЗАБЛОКИРОВАНА   │ ← Не прокачивается!
└─────────────────────────────────┘

Только WATER накапливает прогресс, остальные замерзли!
```

**Что происходит:**
1. Игрок достигает 90% WATER (мастерство разблокировано)
2. WATER становится АКТИВНОЙ (блокирует все остальные)
3. Плейсхолдеры для других показывают: "🔒 Заблокирована"
4. GUI отмечает их как заблокированные
5. События (swim, move, fall, damage) не учитываются для них

### DEGRADATION & REACTIVATION

```
СЦЕНАРИЙ 1: Активная адаптация падает <90%

ШАГ 1: Игрок вышел из воды (WATER активна на 95%)
        ↓
ШАГ 2: Начинается деградация (-5% в час, проверка каждую минуту)
        WATER: 95% → 94% → 93% → 92% → 91% → 90%
        (прошло ~1 час без воды)
        ↓
ШАГ 3: WATER упала ниже 90% → теряет статус АКТИВНОЙ
        ↓
ШАГ 4: РАЗБЛОКИРОВКА ДРУГИХ АДАПТАЦИЙ
        Теперь начинают прокачиваться:
        - NETHER (если в Нижнем мире)
        - CAVE (если ниже Y:32)
        - HEIGHT (если падает)
        - COMBAT (если получает урон)
        - TRAVEL (если ходит)
        ↓
ШАГ 5: WATER продолжает деградировать
        WATER: 90% → 85% → 80% → 75% → ... (если не вернётся в воду)

ЕСЛИ ДО 60%: WATER полностью потеряна и разблокирована для прокачки с 0%
```

```
СЦЕНАРИЙ 2: Активная адаптация упала, но игрок вернулся в условия

БЫЛО: WATER активна (95%), упала до 90%, начала деградировать
      ↓
ИГРОК ВЕРНУЛСЯ В ВОДУ (через 10 минут после выхода)
      ↓
WATER прерывает деградацию и начинает накапливать прогресс снова
      WATER: 90% → 91% → 92% → ... (растёт)
      
Другие адаптации блокируются снова!
```

```
СЦЕНАРИЙ 3: Две адаптации претендуют на активность

БЫЛО: WATER активна (85%), деградирует
      Игрок входит в NETHER (NETHER у него 70% неправда от блокировки?)
      
      ❌ НЕПРАВДА! Пока WATER активна, даже если уходит, NETHER не может
         активироваться, пока WATER полностью не упадёт ниже 60%
         
РЕАКТИВАЦИЯ ПО ПРИОРИТЕТУ:
- WATER упала ниже 60% → полностью неактивна
- Система проверяет: какие условия выполняются СЕЙЧАС?
- Игрок в NETHER? Есть NETHER (70%)? ДА → NETHER активируется!
- Система выбирает по ПРИОРИТЕТУ:
  1. NETHER (высокий приоритет)
  2. THE_END
  3. CAVE
  4. WATER
  5. HEIGHT
  6. COMBAT
  7. TRAVELER (низкий приоритет)
  
RESULT: NETHER активна (70%), WATER начинает прокачиваться заново с 0%
```

### SUMMARY TABLE

| Статус | Прогресс | Эффекты | Блокирует | Дальше |
|--------|----------|---------|-----------|--------|
| 0-89% | Растёт | Базовые | ❌ Нет | Может быть заблокирована |
| 90-100% | Макс + Бонусы | Базовые + Бонус | ✅ ДА | Может деградировать |
| Деградирует | Падает | Все ещё работают | ❌ Нет (отпущены) | Переход к другой |
| <60% | — | ❌ Нет | ❌ Нет | Перезагрузка с 0% |

---

## 🎨 IDEAS FOR FUTURE EXPANSION

### Идея 1: MUTATION SYSTEM
Если игрок достиг 3+ адаптаций одновременно (через зелье или специальное событие), может разблокировать мутацию:
- **Гибридные мутации** (комбо адаптаций):
  - WATER + CAVE → "Морской разведчик" (ночное видение + скорость в воде)
  - NETHER + COMBAT → "Геройский дух" ( 50% меньше урона + 2x регенерация)
  - END + HEIGHT → "Парящий странник" (плавное падение + прыжок выше)

### Идея 2: ADAPTATION MASTERY
Когда адаптация разблокирована, игрок может потратить "очки мастерства" (зарабатываются медленно) для улучшения эффектов:
- WATER: Level 1 (базовая SPEED 2) → Level 2 (SPEED 3 + AQUA_AFFINITY)
- COMBAT: Level 1 (базовая REGEN 2) → Level 2 (REGEN 3 + RESISTANCE 1)

### Идея 3: ADAPTATION COOLDOWN
После переключения адаптации, игрок не может снова переключиться 30 секунд (чтобы не было спама).

### Идея 4: ADAPTATION STREAKS & ACHIEVEMENTS
- Если игрок активен в одной адаптации 1+ час подряд → unlock бонусный эффект
- Разблокировать все 6 адаптаций → "Master of Adaptation" титул + бонус

### Идея 5: BIOME-SPECIFIC BONUSES
- В Джунглях: +20% к разблокировке TRAVEL адаптации
- В Снежных биомах: новая адаптация "Ледяной странник" (FROST_WALKER + SLOW_FALLING)
- В Пустыне: адаптация "Сын пустыни" (меньше голода + SPEED в песке)

### Идея 6: PvP ADAPTATION BALANCE
- В PvP зонах (если есть система регионов): урон от игроков считается отдельно
- COMBAT адаптация разблокируется быстрее в PvP, но медленнее от мобов

### Идея 7: ADAPTATION DECAY
Если игрок не пользуется адаптацией 7 дней → прогресс снижается на 20% (мотивирует разнообразие)

### Идея 8: SEASONAL ADAPTATIONS
- Каждый месяц: новая временная адаптация (например, "Зимний закал" в декабре)
- Разблокируется быстро, но исчезает в конце месяца (на БД сохраняются прогресс)

### Идея 9: ADAPTATION PERKS (как в LoveClans)
На каждую адаптацию уровня 5+ можно выбрать 1 доп перк:
- WATER: "Дыхание дельфина" (можно дышать под водой бесконечно)
- COMBAT: "Боевой инстинкт" (срабатывает парри 1x в 10 сек)
- TRAVEL: "Беспримерный путник" (телепортация на 50м в любом направлении 1x в минуту)

### Идея 10: ADAPTATION SHOWCASE (в профилях)
- Интеграция с LoveProfile (если есть): показывать активные адаптации под аватаром
- Плейсхолдер: %loveadaptation_showcase% → "[🌊 Дельфин ⭐] [🔥 Адепт огня]"

### Идея 11: DUAL ADAPTATION MODE (Супер-редкий режим)
После достижения 90% в двух адаптациях одновременно через зелье:
- Перейти в режим **"Синергия"** на 10 минут (1 раз в час)
- Активные обе адаптации со всеми бонусами
- Получить эффект GLOWING для других игроков (светишься как осадок)
- Бонусный урон/защита в эту синергию

Пример: WATER (90%) + NETHER (90%) = "Паровой воин" (огненная вода, SPEED + FIRE_RESISTANCE на макс)

### Идея 12: ADAPTATION COMBO CHAIN
Если последовательно активировать 3+ адаптации подряд:
- 1 адаптация активна 5 минут
- 2 адаптация активна 5 минут
- 3 адаптация активна 5 минут
- Комбо сработало! → бонус +20% ко всем эффектам на 30 минут

Отслеживание: комбо цепи в БД (какие адаптации в какой последовательности активировались)

### Идея 13: ADAPTATION AFFINITY (Усиление через совместимость)
Некоторые адаптации работают лучше вместе (не конфликтуют):
- WATER + TRAVELER → "Морской странник" (+25% SPEED в воде + -25% голода)
- NETHER + COMBAT → "Геройский дух" (50% больше регенерации в Нижнем мире)
- CAVE + COMBAT → "Подземный истребитель" (ночное видение + 2x регенерация в пещерах)

Система "скрытых комбо" — игрок находит их натурально, тратя время в условиях

### Идея 14: ADAPTATION MEMORY (История адаптаций)
В БД сохранять историю:
- Когда адаптация была впервые разблокирована
- Как часто была активна
- Общее время в статусе 90%+
- Достижение: "Верный друг воды" (WATER была активна 50+ часов)

В профиле показывать ленту событий: "🌊 Дельфин разблокирован 2 дня назад"

### Идея 15: ADAPTATION SACRIFICE (Жертва для мощного бонуса)
Специальная механика: если игрок сознательно деградирует адаптацию на 50% (сбросит до 0%):
- Получит 1 "адаптационный кристалл" (редкий дроп-подобный предмет)
- Его можно скомбинировать с зельем для создания "Супер-зелья адаптации" (все эффекты на 5 минут на макс)
- Или использовать для разблокировки "Мутаций"

### Идея 16: SEASONAL ADAPTATION ROTATION
Каждый месяц (или сезон) основной мир получает "сезонную адаптацию":
- **Зима (декабрь-февраль):** "Ледяной странник" (FROST_WALKER + -50% урома от льда)
- **Весна (март-май):** "Зелёный чародей" (растения растут быстрее, SPEED в тропическом лесу)
- **Лето (июнь-август):** "Солнечный странник" (ускорение днём, -50% урона от жара)
- **Осень (сент-ноябрь):** "Сборщик урожая" (LUCK эффект для дропов, специально для ферм)

Сезонные адаптации обновляются автоматически, всем игрокам синхронизируется

### Идея 17: ADAPTATION PRESTIGE (Перезагрузка с бонусом)
После достижения 100% во всех адаптациях:
- Предложить "PRESTIGE" режим
- Сбросить все адаптации на 0%
- Получить видимый титул "Адаптированный" [Lvl 1]
- Следующий прогресс идёт на 20% быстрее
- Максимум 5 уровней престижа (Lvl 5 = 100% быстрее)

Пример: Максим, адаптированный [Lvl 3] — его система адаптаций работает на 60% быстрее

### Идея 18: ADAPTATION TEACHING (Делиться опытом)
Если игрок (Мастер адаптации, 90%+) находится рядом с другим игроком (Ученик, <90%):
- Ученик получает +50% бонус к скорости прокачки адаптации
- Но ТОЛЬКО ТУ адаптацию, которую имеет Мастер
- "Ты учишься у [player_name] с 95% адаптации к воде"

Кооперативная механика для серверов с гильдиями

### Идея 19: ADAPTATION TALENT TREE (Не совместимо с Мутациями)
Вместо простой прокачки, предложить "дерево умений" для каждой адаптации:
- WATER 90%+: выбрать один путь развития
  - A) "Охотник на кальмаров" — лучше дропятся чернила
  - B) "Ловец жемчуга" — лучше находятся жемчуга
  - C) "Ускоритель" — SPEED 4 вместо 3
  
Позже можно переделать дерево, потратив редкий ресурс

### Идея 20: ADAPTATION SOULBIND (Личное развитие)
Адаптация не теряется при смерти, но "повреждается":
- После смерти: прогресс адаптации -10%
- Но эффекты остаются работать на 1 минуту
- Затем восстанавливается медленно (по 1% в минуту)

Система "душевной связи" с адаптацией — она "запомнила" тебя

---

## 📚 IMPLEMENTATION CHECKLIST

- [ ] Создать database schema + миграции
- [ ] Реализовать AdaptationManager с методами: unlock, switch, apply, remove
- [ ] Реализовать listeners для всех event типов
- [ ] Реализовать AsyncScheduler для tick-based tracking
- [ ] Создать PlaceholderAPI hooks
- [ ] Создать GUI (DeluxeMenus YAML)
- [ ] Реализовать команды (/loveadaptation)
- [ ] Зелья адаптации (через конфиг или custom items)
- [ ] Тестирование всех адаптаций на test server
- [ ] Документация для админа (в wiki)

---

## 🚀 DEPLOYMENT NOTES

1. **Зависимости:** LoveCore v1.5+, PlaceholderAPI
2. **Performance:** Ожидаемое использование CPU <1%, RAM ~5-10 МБ (1000 игроков)
3. **Database:** SQLite автоматически создается, резервные копии каждые 6 часов
4. **GUI:** Обновляется каждый tick (для плейсхолдеров), можно снизить до 5 тиков если лаги

---

**Версия:** 1.0.0  
**Дата:** 2026-07-26  
**Автор:** Максим (Love* Ecosystem)  
**Статус:** READY FOR IMPLEMENTATION
# LoveAdaptation — Визуальное объяснение МЕХАНИК v1.0

---

## 📊 ДИАГРАММА ПРОГРЕССА АДАПТАЦИИ

```
АДАПТАЦИЯ: WATER (Дельфин)

ШКАЛА ПРОГРЕССА:
0%                    90%                    100%
│                      │                      │
├──────────────────────┼──────────────────────┤
│   БАЗОВЫЕ ЭФФЕКТЫ    │  БОНУСЫ ВКЛЮЧЕНЫ     │
│                      │                      │
│ SPEED 2              │ SPEED 3 ⭐           │
│ CONDUIT_POWER 1      │ DOLPHINS_GRACE ⭐    │
│ Hunger: 1.0x         │ Hunger: 0.9x ⭐      │
└──────────────────────┴──────────────────────┘
         ФАЗА 1: НАРАСТАНИЕ          ФАЗА 2: МАСТЕРСТВО


ЭТАПЫ РАЗВИТИЯ:
0-30%      ⬜ Рано (еще 6 часов?)
30-60%     ⬜ Середина (еще 3 часа?)
60-89%     ⬜ Продвинуто (еще час?)
90-100%    ⭐ МАСТЕРСТВО (звёздочка!)
```

---

## 🔒 ДИАГРАММА БЛОКИРОВКИ

### Сценарий: WATER активна (95%)

```
┌──────────────────────────────────────────────────────────┐
│                   АДАПТАЦИИ ПЕРСОНАЖА                    │
├──────────────────────────────────────────────────────────┤
│                                                          │
│  🌊 WATER           95%  ✓ АКТИВНА (блокирует остальные)│
│     └─ SPEED 3, DOLPHINS_GRACE, -10% голода             │
│                                                          │
│  🔥 NETHER          70%  🔒 ЗАБЛОКИРОВАНА              │
│     └─ прогресс НЕ идёт (тики не считаются)           │
│                                                          │
│  👁 CAVE            65%  🔒 ЗАБЛОКИРОВАНА              │
│     └─ прогресс НЕ идёт (тики не считаются)           │
│                                                          │
│  ⬆️ HEIGHT          80%  🔒 ЗАБЛОКИРОВАНА              │
│     └─ прогресс НЕ идёт (падения не считаются)        │
│                                                          │
│  🎆 END             55%  🔒 ЗАБЛОКИРОВАНА              │
│     └─ прогресс НЕ идёт (тики не считаются)           │
│                                                          │
│  ⚔️ COMBAT          75%  🔒 ЗАБЛОКИРОВАНА              │
│     └─ прогресс НЕ идёт (урон не считается)           │
│                                                          │
│  🚶 TRAVELER        45%  🔒 ЗАБЛОКИРОВАНА              │
│     └─ прогресс НЕ идёт (расстояние не считается)     │
│                                                          │
└──────────────────────────────────────────────────────────┘

ЧТО ОЗНАЧАЕТ 🔒 ЗАБЛОКИРОВАНА?
- Событие происходит, но прогресс не добавляется (0%)
- В GUI показывается статус "🔒 Заблокирована"
- Плейсхолдер %loveadaptation_water_progress% вернёт "🔒"
- Нельзя вручную повысить прогресс командой
- Останется на текущем проценте, пока WATER активна
```

### Сценарий: Все адаптации свободны (нет активной)

```
┌──────────────────────────────────────────────────────────┐
│         АДАПТАЦИИ ПЕРСОНАЖА (ВСЕ ДОСТУПНЫ)              │
├──────────────────────────────────────────────────────────┤
│                                                          │
│  🌊 WATER           45%  ⏳ Прокачка (в воде?)         │
│  🔥 NETHER          60%  ⏳ Прокачка (в Нижнем мире?)  │
│  👁 CAVE            65%  ⏳ Прокачка (ниже Y:32?)      │
│  ⬆️ HEIGHT          80%  ⏳ Прокачка (падает?)         │
│  🎆 END             30%  ⏳ Прокачка (в Энде?)         │
│  ⚔️ COMBAT          75%  ⏳ Прокачка (в бою?)          │
│  🚶 TRAVELER        50%  ⏳ Прокачка (ходит?)          │
│                                                          │
└──────────────────────────────────────────────────────────┘

ВСЕ накапливают прогресс одновременно (если условия выполняются)!
```

---

## 📉 ДИАГРАММА ДЕГРАДАЦИИ

### Временная шкала: WATER падает с 95%

```
ВРЕМЯ (часы)    WATER (%)   СТАТУС                    ЧТО ПРОИСХОДИТ
─────────────────────────────────────────────────────────────────────
T=0            95%        ✓ АКТИВНА                Игрок вышел из воды
               
T=1h           90%        ✓ АКТИВНА (граница)      -5% за час (начало деградации)
               
T=2h           85%        📉 ДЕГРАДИРУЕТ            Упала <90% → потеряла активность
                                                    ДРУГИЕ РАЗБЛОКИРОВАНЫ!
               
T=3h           80%        📉 ДЕГРАДИРУЕТ            Продолжает падать
               
T=4h           75%        📉 ДЕГРАДИРУЕТ            -5% в час = линейное падение
               
T=5h           70%        📉 ДЕГРАДИРУЕТ            Пока не вернётся в воду
               
T=6h           65%        📉 ДЕГРАДИРУЕТ            ...
               
T=7h           60%        ❌ ПОЛНОСТЬЮ ПОТЕРЯНА     <60% → полностью неактивна
                                                    Разблокирована с 0% для прокачки!
               
───────────────────────────────────────────────────────────────────
АЛЬТЕРНАТИВА: Игрок вернулся в воду (например, на T=3h на 75%)
───────────────────────────────────────────────────────────────────

T=3.5h         75% → 76%  ✓ ВЕРНУЛАСЬ               Деградация СТОПИРОВАНА
               
T=4h           77%        ✓ АКТИВНА                 Начинает расти снова!
               
T=5h           78%        ✓ АКТИВНА                 Прогресс восстанавливается
```

---

## 🔄 ПОЛНЫЙ ЦИКЛ: ПЕРЕКЛЮЧЕНИЕ АДАПТАЦИЙ

### Сценарий: Игрок меняет окружение

```
ЭТАП 1: WATER активна
┌────────────────────────────────────────┐
│ WATER 95% ✓           (в озере)       │
│ NETHER 70% 🔒 блокирована             │
│ CAVE 65% 🔒 блокирована               │
│ HEIGHT 80% 🔒 блокирована             │
│ ...                                    │
└────────────────────────────────────────┘
      ↓ Игрок ВЫШЕЛ из воды и вошёл в NETHER


ЭТАП 2: WATER начинает деградировать, NETHER готовится
┌────────────────────────────────────────┐
│ WATER 94% 📉 (деградирует -5%/ч)       │
│ NETHER 70% 🔒 всё ещё блокирована    │
│ CAVE 65% 🔒 всё ещё блокирована      │
│ HEIGHT 80% 🔒 всё ещё блокирована    │
│ ...                                    │
└────────────────────────────────────────┘
      ↓ Прошла 1 час (нет воды), WATER упала <90%


ЭТАП 3: WATER теряет активность, NETHER активируется
┌────────────────────────────────────────┐
│ WATER 89% (макс без активности)       │
│ NETHER 70% ✓ АКТИВНА (в Нижнем мире) │ ← АВТОМАТИЧЕСКИ АКТИВИРОВАНА!
│ CAVE 65% 🔒 блокирована               │
│ HEIGHT 80% 🔒 блокирована             │
│ ...                                    │
└────────────────────────────────────────┘
      ↓ NETHER теперь активна и блокирует остальные


ЭТАП 4: Если игрок вернётся в NETHER на 90%
┌────────────────────────────────────────┐
│ WATER 89% 🔒 блокирована              │
│ NETHER 90% ✓ АКТИВНА (мастерство!)   │ ← ЗВЁЗДОЧКА! Бонусы работают!
│ CAVE 65% 🔒 блокирована               │
│ HEIGHT 80% 🔒 блокирована             │
│ ...                                    │
└────────────────────────────────────────┘
```

---

## 📋 ПРИМЕРЫ ПЛЕЙСХОЛДЕРОВ

### Сценарий: WATER активна (95%), остальные заблокированы

```
Плейсхолдер: %loveadaptation_water_status%
Результат:   "✓ Активна"

Плейсхолдер: %loveadaptation_nether_status%
Результат:   "🔒 Заблокирована"

Плейсхолдер: %loveadaptation_water_progress%
Результат:   "95% ⭐"

Плейсхолдер: %loveadaptation_nether_progress%
Результат:   "70% 🔒"

Плейсхолдер: %loveadaptation_current_name%
Результат:   "Дельфин"

Плейсхолдер: %loveadaptation_blocked_count%
Результат:   "6/7 заблокировано"

Плейсхолдер: %loveadaptation_progress_next_adaptation%
Результат:   "NETHER (70% - доступна когда WATER упадёт)"
```

---

## 🎯 ПРАКТИЧЕСКИЕ СЦЕНАРИИ

### Сценарий 1: Новый игрок начинает

```
ДЕНЬ 1
- Прогулка в мире → TRAVELER +0.5% (за прогулку)
- Попадание в лаву → NETHER +0.1% (не в Нижнем мире, но урон)
- Падение → HEIGHT +10% (событие падения)
- Прогресс: TRAVELER 0.5%, NETHER 0.1%, HEIGHT 10%, остальные 0%

ДЕНЬ 2 (Игрок чит-ит в воду)
- 2 часа в воде → WATER +13% (2 часа из 15 = ~13%)
- TRAVELER, NETHER, HEIGHT не накапливают (но прогресс НЕ теряется!)
- Прогресс: WATER 13%, TRAVELER 0.5%, NETHER 0.1%, HEIGHT 10%

ДЕНЬ 7 (Игрок достиг 90% WATER)
- 15 часов в воде (всего) → WATER 90% → МАСТЕРСТВО РАЗБЛОКИРОВАНО! ⭐
- Бонусы активированы: SPEED 3, DOLPHINS_GRACE
- Все остальные заблокированы (🔒)

ДЕНЬ 8 (Игрок вышел из воды на целый день)
- WATER деградирует на 24 часа / 5% в час = 120% (но max 90%, поэтому упала до 0%)
- Если упала ниже 90% → теряет активность → другие разблокируются
- Если 12 часов без воды → WATER 90% - 60% = 30% осталось
- Если 24 часа без воды → WATER 0% (полностью потеряна)
- Прогресс начинают накапливать: NETHER, CAVE, HEIGHT, COMBAT, TRAVELER (но только если условия!)
```

### Сценарий 2: Опытный игрок играет в пещерах

```
СИТУАЦИЯ:
- CAVE адаптация: 85% (прокачивалась долго, но не до 90%)
- HEIGHT адаптация: 75%
- Остальные: <50%

В ПЕЩЕРЕ (Y < 32):
- CAVE начинает расти (тики добавляются)
- Другие НЕ растут (условия не выполняются)
- Если CAVE достигнет 90% → станет активной, остальные заблокируются

ЕСЛИ CAVE 90%+ АКТИВНА:
- Все получают NIGHT_VISION (базовый эффект)
- При 90%+ также получают GLOWING (враги светятся) - БОНУС
- Если упадёт ниже 90% (вышел из пещеры):
  - Потеря активности
  - HEIGHT разблокируется и начнёт расти (если падает)
  - CAVE будет деградировать на -5% в час
```

### Сценарий 3: PvP боец с COMBAT адаптацией

```
COMBAT АДАПТАЦИЯ: особенная (не время-основана)
- Разблокируется после 100 урона от мобов/игроков (eventos, не время)
- Деградирует МЕДЛЕННЕЕ: -2% в час (не -5%)
- Это означает: боевой опыт "запоминается" дольше

БОЕЦ В БОЯХ:
1. Получил 100 урона → COMBAT 90% (мастерство разблокировано!)
2. Бонусы: REGENERATION 3 + RESISTANCE 1 (вместо базовой REGENERATION 2)
3. Благодаря -2%/ч деградации:
   - Даже если 5 часов не в бою → COMBAT 90% → COMBAT 80%
   - Опыт боя долго "помнится" персонажем
   - Новичок же теряет опыт на -5%/ч

ЕСЛИ НЕ В БОЮ ДОЛГО:
- COMBAT: 90% → 88% → 86% → 84% → ... (медленная потеря)
- Другие адаптации разблокируются и начинают расти
```

---

## 📱 EXAMPLE: GUI MENU STATUS

### Типичный вид GUI при WATER 95% активной

```
                    🌊 Адаптации персонажа

┌────────────────────────────────────────────────────────┐
│                   🐬 Дельфин                           │
│  Система адаптаций | Дельфин (95% ⭐)                 │
│                   Прогресс: 95%                        │
│                                                        │
│  ⏳ Адаптации (вкл)      🔒 Мутации (закрыто)         │
│                                                        │
├────────────────────────────────────────────────────────┤
│                                                        │
│  🌊 Дельфин              ✓ 95% ⭐                     │
│     Ускорение в воде     Статус: ✓ Активна            │
│     Улучшенное дыхание   Бонусы: SPEED 3, DOLPHINS    │
│                                                        │
│  🔥 Адепт огня           70% 🔒                       │
│     Снижение урома огня  Статус: 🔒 Заблокирована    │
│     40% защиты           (будет разблокирована)      │
│                                                        │
│  👁 Зрение во мраке      65% 🔒                       │
│     Ночное видение       Статус: 🔒 Заблокирована    │
│                                                        │
│  ⬆️ Падальщик             80% 🔒                       │
│     Снижение урома       Статус: 🔒 Заблокирована    │
│     от падения                                        │
│                                                        │
│  🎆 Скиталец Эндера      55% 🔒                       │
│     Плавное падение      Статус: 🔒 Заблокирована    │
│                                                        │
│  ⚔️ Боевой закал         75% 🔒                       │
│     Регенерация          Статус: 🔒 Заблокирована    │
│                                                        │
│  🚶 Странник             45% 🔒                       │
│     -30% голода          Статус: 🔒 Заблокирована    │
│                                                        │
├────────────────────────────────────────────────────────┤
│            [ℹ️ Информация]     [❌ Закрыть]           │
└────────────────────────────────────────────────────────┘
```

---

## 🔔 NOTIFICATIONS (звуки + сообщения)

### Когда адаптация достигает 90%

```
[TITLE]
    &6✨ Мастерство разблокировано
    &f Дельфин

[SUBTITLE]
    &7Бонусы активированы!

[ACTIONBAR]
    &7Бонусы: &f SPEED 3, DOLPHINS_GRACE

[SOUND]
    ENTITY_PLAYER_LEVELUP (volume=1.0, pitch=1.2)
```

### Когда адаптация становится активной

```
[ACTIONBAR]
    &a Адаптация активна: &f Дельфин

[SOUND]
    BLOCK_RESPAWN_ANCHOR_CHARGE (volume=0.8, pitch=1.0)
```

### Когда адаптация начинает деградировать

```
[ACTIONBAR]
    &c⚠ Адаптация деградирует: &f Дельфин (89%)

[SOUND]
    ENTITY_GENERIC_HURT (volume=0.5, pitch=0.8)
```

### Когда адаптация полностью потеряна

```
[ACTIONBAR]
    &c Адаптация потеряна: &f Дельфин

[SOUND]
    ENTITY_GENERIC_DEATH (volume=0.5, pitch=0.5)
```

---

## ✅ КОНТРОЛЬНЫЙ СПИСОК (для разработчика)

### Database должна отслеживать:

- [ ] Текущий прогресс каждой адаптации (0-100%)
- [ ] Какая адаптация активна в данный момент
- [ ] Когда была разблокирована 90% (для истории)
- [ ] Время последней проверки условий (для деградации)
- [ ] История событий (PROGRESS, UNLOCK, ACTIVATE, DEGRADE, DEACTIVATE)

### Логика должна проверять:

- [ ] Каждый tick: есть ли условие для активной адаптации?
- [ ] Каждую минуту: нужна ли деградация? (если условия не выполняются)
- [ ] Каждые 30 сек: может ли активироваться другая адаптация? (по приоритету)
- [ ] При событии: считать ли в "активную" или в "блокированные"?
- [ ] При достижении 90%: применить ли бонусы?

### GUI должна показывать:

- [ ] Текущую активную адаптацию с прогрессом %
- [ ] Заблокированные адаптации с иконкой 🔒
- [ ] Прокачиваемые адаптации с иконкой ⏳
- [ ] Достигшие 90% с иконкой ⭐
- [ ] Деградирующие с иконкой 📉
- [ ] Рядом статус в текстовом виде

---

**Версия:** 1.0  
**Дата:** 2026-07-26  
**Статус:** Visual Guide Complete
# LoveAdaptation — QUICK START GUIDE

---

## 🎯 ЧТО ИМЕННО РЕАЛИЗОВАТЬ?

### **3 ОСНОВНЫХ МЕХАНИКИ v1.0**

#### 1️⃣ PROGRESSION TO 90% + BONUSES
```
0% ─── Базовые эффекты ──→ 90% ─── БОНУСЫ! ──→ 100%

WATER: SPEED 2 → SPEED 3 + DOLPHINS_GRACE
NETHER: -40% урома → -50% урома + RESISTANCE
CAVE: NIGHT_VISION → NIGHT_VISION + враги светятся
HEIGHT: -50% падения → -70% падения
END: SLOW_FALLING → SLOW_FALLING 2 + JUMP_BOOST
COMBAT: REGEN 2 → REGEN 3 + RESISTANCE
TRAVEL: -30% голода → -50% голода
```

#### 2️⃣ BLOCKING (только 1 активна)
```
ACTIVE (>90%): ✓ накапливает прогресс
OTHERS (все):  🔒 ЗАБЛОКИРОВАНЫ (0% прогресса)
```

#### 3️⃣ DEGRADATION + REACTIVATION
```
ACTIVE адаптация упала <90%:
  ↓ -5% в час (COMBAT: -2% в час)
  ↓ Если <60% → полностью потеряна
  ↓ ДРУГИЕ разблокируются и начинают расти
```

---

## 📋 QUICK CHECKLIST

### Backend (Java)

- [ ] Database schema (SQLite)
  - [ ] `player_adaptations` (UUID, current, progress%, is_active)
  - [ ] `adaptation_progress` (UUID, adaptation_name, progress_value, progress_percent)
  - [ ] `adaptation_history` (логирование событий)

- [ ] AdaptationManager класс
  ```java
  public class AdaptationManager {
      public void addProgress(Player p, String adaptation, double amount);
      public void activateAdaptation(Player p, String adaptation);
      public void deactivateAdaptation(Player p, String adaptation);
      public void checkAndSwitchAdaptation(Player p);
      public void applyDegradation(Player p);
      public void applyEffects(Player p, String adaptation);
  }
  ```

- [ ] Event Listeners
  - [ ] PLAYER_MOVE: вода, Y-check, расстояние
  - [ ] ENTITY_DAMAGE: урам, падение
  - [ ] PLAYER_CHANGED_WORLD: Нижний мир, Эндер
  - [ ] PLAYER_LEAVE_WATER: сохранить прогресс

- [ ] AsyncScheduler
  - [ ] Каждый tick: отслеживание событий (но не DB!)
  - [ ] Каждую минуту: проверка деградации
  - [ ] Каждые 30 сек: проверка реактивации

- [ ] PlaceholderAPI hooks
  - [ ] %loveadaptation_current_name%
  - [ ] %loveadaptation_{name}_progress%
  - [ ] %loveadaptation_{name}_status%
  - [ ] %loveadaptation_progress_next_adaptation%

### GUI (DeluxeMenus YAML)

- [ ] 54-слотовое меню (gui_gen стандарт!)
  - [ ] Слот 0: системная голова адаптации
  - [ ] Слоты 2-7: вкладки (Адаптации + Мутации заглушка)
  - [ ] Рабочая зона 18-44: динамический контент с плейсхолдерами
  - [ ] Footer: glass + close + optional info button
  - [ ] Слоты 9-17: glass (54-слотовое правило)

- [ ] Визуальные индикаторы в GUI
  - [ ] ✓ Активна
  - [ ] ⭐ Мастерство (90%+)
  - [ ] ⏳ Прокачка
  - [ ] 🔒 Заблокирована
  - [ ] 📉 Деградирует
  - [ ] ⬜ Не разблокирована

### Commands

- [ ] `/loveadaptation menu` — открыть GUI
- [ ] `/loveadaptation info [player]` — инфо
- [ ] `/loveadaptation reset <player>` — admin сброс
- [ ] `/loveadaptation reload` — перезагрузка

### Configuration (config.yml)

- [ ] Base adaptation (если нет активной)
- [ ] 7 адаптаций (7 блоков в конфиге)
  - [ ] unlock_hours / unlock_km / unlock_damage / unlock_falls
  - [ ] max_progress_percent: 90
  - [ ] bonus_threshold_percent: 90
  - [ ] effects_base / effects_bonus
  - [ ] degradation_rate_percent_per_hour
  - [ ] head_texture (Base64)
- [ ] Потионы (I и II уровень)
- [ ] Tracking intervals
- [ ] Notifications (sounds + messages)
- [ ] Blocking & degradation параметры

### Testing на Server Тестовый

- [ ] WATER: 15 часов в воде → 90% → проверить бонусы
- [ ] NETHER: 12 часов в Нижнем мире
- [ ] CAVE: 10 часов ниже Y:32
- [ ] HEIGHT: 10 падений
- [ ] END: 8 часов в Энде
- [ ] COMBAT: 100 урома от мобов
- [ ] TRAVEL: 20 км пешком
- [ ] Блокировка: одна активна → остальные заблокированы
- [ ] Деградация: при выходе из условий → -5% в час
- [ ] Реактивация: при возврате в условия → активируется по приоритету
- [ ] Плейсхолдеры: все работают правильно
- [ ] GUI: слоты по стандарту, плейсхолдеры обновляются
- [ ] Зелья: пить и получать все эффекты на время

---

## 🗺️ ФАЙЛЫ В ПРОЕКТЕ

```
LoveAdaptation/
├── src/main/java/me/lovelace/loveadaptation/
│   ├── LoveAdaptation.java (main plugin class)
│   ├── config/
│   │   └── AdaptationConfig.java
│   ├── manager/
│   │   ├── AdaptationManager.java
│   │   ├── ProgressManager.java
│   │   ├── DegradationManager.java
│   │   └── ReactivationManager.java
│   ├── listener/
│   │   ├── PlayerMoveListener.java (вода, пещеры, расстояние)
│   │   ├── PlayerDamageListener.java (урам, падения, боевая)
│   │   ├── WorldChangeListener.java (мир)
│   │   └── PotionListener.java (зелья)
│   ├── command/
│   │   └── AdaptationCommand.java
│   ├── placeholder/
│   │   └── AdaptationPlaceholder.java
│   ├── data/
│   │   ├── database/
│   │   │   ├── Database.java
│   │   │   └── DatabaseMigration.java
│   │   └── model/
│   │       ├── PlayerAdaptationData.java
│   │       └── AdaptationProgress.java
│   └── util/
│       ├── ConfigUtil.java
│       ├── EffectUtil.java
│       └── MessageUtil.java
│
├── src/main/resources/
│   ├── plugin.yml
│   ├── config.yml
│   ├── lang_ru_RU.yml
│   └── gui/
│       └── adaptation_menu.yml (DeluxeMenus)
│
└── pom.xml
```

---

## 🔌 ЗАВИСИМОСТИ (pom.xml)

```xml
<!-- LoveCore (для AsyncScheduler, ServicesManager) -->
<dependency>
    <groupId>me.lovelace</groupId>
    <artifactId>lovecore</artifactId>
    <version>1.5</version>
    <scope>provided</scope>
</dependency>

<!-- PlaceholderAPI -->
<dependency>
    <groupId>me.clip</groupId>
    <artifactId>placeholderapi</artifactId>
    <version>2.11.3</version>
    <scope>provided</scope>
</dependency>

<!-- DeluxeMenus (если нужны GUI integration) -->
<!-- Adventure API (для MiniMessage) -->
<dependency>
    <groupId>net.kyori</groupId>
    <artifactId>adventure-api</artifactId>
    <version>4.14.0</version>
    <scope>provided</scope>
</dependency>
```

---

## 📊 DATABASE QUERIES (готовые)

```sql
-- Получить текущий статус игрока
SELECT * FROM player_adaptations WHERE uuid = ?;

-- Получить прогресс адаптации
SELECT * FROM adaptation_progress WHERE uuid = ? AND adaptation_name = ?;

-- Обновить прогресс
UPDATE adaptation_progress 
SET progress_value = progress_value + ?, 
    progress_percent = ?,
    updated_at = ?
WHERE uuid = ? AND adaptation_name = ?;

-- Активировать адаптацию
UPDATE player_adaptations 
SET current_adaptation = ?, updated_at = ?
WHERE uuid = ?;

-- Блокировать остальные (зануления)
UPDATE adaptation_progress 
SET progress_percent = 0
WHERE uuid = ? AND adaptation_name != ?;

-- История
INSERT INTO adaptation_history (uuid, adaptation_name, event_type, event_percent, event_timestamp, notes)
VALUES (?, ?, ?, ?, ?, ?);
```

---

## 🎨 GUI STRUCTURE (точные слоты)

```
      0    1    2    3    4    5    6    7    8
0  [HEAD] [G] [TAB] [TAB] [   ] [   ] [   ] [   ] [G]
   
9-17   [全 GLASS 全 GLASS 全]  (9 слотов стекла)

18   [G]
19-25   [WATER] [NETHER] [CAVE] [HEIGHT] [END] [COMBAT] [TRAVEL]
26   [G]

27   [G]
28-34   [...]  (ещё контент если нужен)
35   [G]

36   [G или ← PAGINATION]
37-43   [...]  (ещё контент)
44   [G или → PAGINATION]

45-50  [全 GLASS 全]
51   [Optional button или glass]
52   [BACK если chained, иначе glass]
53   [CLOSE]
```

---

## 💾 STARTUP SEQUENCE

```java
@Override
public void onEnable() {
    // 1. Загрузить конфиг
    loadConfig();
    
    // 2. Инициализировать БД
    database.initialize();
    database.createTables();
    
    // 3. Создать менеджеры
    adaptationManager = new AdaptationManager(this);
    progressManager = new ProgressManager(this);
    degradationManager = new DegradationManager(this);
    reactivationManager = new ReactivationManager(this);
    
    // 4. Зарегистрировать listeners
    getServer().getPluginManager().registerEvents(new PlayerMoveListener(), this);
    getServer().getPluginManager().registerEvents(new PlayerDamageListener(), this);
    // ... остальные
    
    // 5. Зарегистрировать команды
    getCommand("loveadaptation").setExecutor(new AdaptationCommand(this));
    
    // 6. Зарегистрировать плейсхолдеры
    new AdaptationPlaceholder(this).register();
    
    // 7. Запустить AsyncScheduler
    new BukkitRunnable() {
        @Override
        public void run() {
            // Каждый tick: проверка условий
            for (Player player : Bukkit.getOnlinePlayers()) {
                adaptationManager.checkAndSwitchAdaptation(player);
            }
        }
    }.runTaskTimer(this, 0, 1);
    
    new BukkitRunnable() {
        @Override
        public void run() {
            // Каждую минуту: деградация
            for (Player player : Bukkit.getOnlinePlayers()) {
                degradationManager.checkAndApplyDegradation(player);
            }
        }
    }.runTaskTimer(this, 60 * 20, 60 * 20);
    
    getLogger().info("LoveAdaptation v1.0 enabled!");
}
```

---

## 🧪 ТЕСТИРОВАНИЕ ВРУЧНУЮ

```bash
# На Server Тестовый:

# Окрыть GUI
/loveadaptation menu

# Просмотреть инфо
/loveadaptation info Максим

# Сбросить (admin)
/loveadaptation reset Максим

# Проверить логи
logs/latest.log | grep LoveAdaptation

# Проверить БД
sqlite3 plugins/LoveAdaptation/data.db
  SELECT * FROM player_adaptations;
  SELECT * FROM adaptation_progress;
  SELECT * FROM adaptation_history;

# Использовать плейсхолдеры в чате
say %loveadaptation_current_name%
say %loveadaptation_water_progress%
say %loveadaptation_blocked_count%
```

---

## 🚨 COMMON MISTAKES (в разработке)

❌ **Неправильно:** Сохранять в БД каждый tick (ЛАГИ!)
✅ **Правильно:** Батчить updates и сохранять раз в минуту

❌ **Неправильно:** Блокировка на UI (главный тред)
✅ **Правильно:** Все I/O в AsyncScheduler

❌ **Неправильно:** Считать прогресс для всех адаптаций одновременно
✅ **Правильно:** Только активная + остальные если разблокированы

❌ **Неправильно:** Применять эффекты каждый tick
✅ **Правильно:** Применять один раз при активации, удалять при деактивации

❌ **Неправильно:** GUI слоты не по gui_gen стандарту
✅ **Правильно:** Ровно 54 слота, точные позиции, GRAY_STAINED_GLASS везде

---

## 📞 ИНТЕГРАЦИИ

### LoveCore
```java
// Использовать AsyncScheduler
LoveCore.getInstance().getAsyncScheduler().run(() -> {
    database.updatePlayer(player.getUniqueId(), data);
});

// Использовать ServicesManager (для LoveClans, LoveBehavior)
ServiceManager services = LoveCore.getInstance().getServiceManager();
LoveClanService clanService = services.load(LoveClanService.class);
if (clanService != null) {
    // Игрок в клане? +10% к скорости разблокировки
    Clan clan = clanService.getPlayerClan(player.getUniqueId());
    if (clan != null) speedMultiplier = 1.1;
}
```

### PlaceholderAPI
```java
public class AdaptationPlaceholder extends PlaceholderExpansion {
    @Override
    public String onPlaceholderRequest(Player player, String identifier) {
        if (identifier.equals("current_name")) {
            return adaptationManager.getCurrentAdaptation(player).getName();
        }
        if (identifier.startsWith("water_progress")) {
            return progressManager.getProgress(player, "WATER") + "%";
        }
        // ... остальное
        return null;
    }
}
```

---

## ✨ ФИНАЛЬНЫЙ ЧЕККЛИСТ ДО ДЕПЛОЯ

- [ ] Все 7 адаптаций работают
- [ ] Блокировка работает (1 активна, остальные 🔒)
- [ ] Деградация работает (-5% в час)
- [ ] Реактивация работает (переключение по приоритету)
- [ ] Плейсхолдеры работают все
- [ ] GUI соответствует gui_gen (54 слота, точные позиции)
- [ ] Все слоты либо контент, либо glass, либо пусто (никогда воздух!)
- [ ] Потионы работают (I и II уровень)
- [ ] Нотификации работают (звуки, actionbar, title)
- [ ] команды работают
- [ ] БД мигрирует корректно
- [ ] Performance < 1% CPU на 1000 игроков
- [ ] Логи чистые (никакх ошибок)
- [ ] Тестовый сервер стабилен

---

## 📚 ДОКУМЕНТАЦИЯ ДЛЯ ИГРОКОВ

Создать wiki страницу:
```markdown
# Адаптации персонажа (LoveAdaptation)

## Что это?
Система, где персонаж развивается под окружение.

## 7 адаптаций
1. 🌊 WATER (15 часов в воде)
2. 🔥 NETHER (12 часов в Нижнем мире)
3. 👁 CAVE (10 часов ниже Y:32)
4. ⬆️ HEIGHT (10 падений)
5. 🎆 END (8 часов в Энде)
6. ⚔️ COMBAT (100 урама)
7. 🚶 TRAVEL (20 км)

## Прогресс
- 0-89%: базовые эффекты
- 90-100%: **БОНУСЫ!** ⭐

## Блокировка
Только 1 адаптация может быть активной одновременно.
Остальные блокируются 🔒

## Деградация
Если активная адаптация упала <90%:
- Теряет -5% в час (если условия не выполняются)
- Другие разблокируются

## Команды
/loveadaptation menu  — GUI меню
/loveadaptation info  — инфо о прогрессе
```

---

**Ready to implement!** 🚀

Скопируй весь контент промпта в Claude Code / JetBrains AI Assistant и начинай разработку.

**Версия:** Quick Start 1.0
**Дата:** 2026-07-26
# LoveAdaptation — 20+ ИДЕЙ для будущих версий

---

## 🚀 ТОП ИДЕИ (easiest to implement)

### ✅ Идея 1: ADAPTATION PRESTIGE (сброс с бонусом)

После достижения 100% во ВСЕХ адаптациях:
- Предложить игроку "PRESTIGE" режим
- Сбросить все адаптации на 0%
- Получить видимый титул: **"Адаптированный [Lvl 1]"**
- Следующий прогресс идёт на **20% быстрее** (время сокращается)
- Максимум 5 уровней престижа (Lvl 5 = 100% быстрее)

**Пример:**
```
[Максим, адаптированный Lvl 3]

WATER обычно: 15 часов
WATER с Lvl 3: 15 * (1 - 0.6) = 6 часов

Плейсхолдер: %loveadaptation_prestige_level%
Вывод: "3" или "Не активирован"
```

**Implementation:** +1 счётчик в БД, множитель при расчёте

---

### ✅ Идея 2: ADAPTATION TEACHING (обучение соседям)

Если игрок (Мастер адаптации, >90%) находится рядом с другим игроком (<90% той же адаптации):
- Ученик получает **+50% бонус** к скорости прокачки **только той адаптации**
- Статус в actionbar: `"👨‍🏫 Ты учишься у [player_name] с 95% адаптации к воде"`
- Мастер не получает штрафа

**Радиус:** 16 блоков (1 чанк)

**Implementation:** 
- Listener PLAYER_MOVE проверяет соседних игроков
- Если нашли Мастера → modifier *= 1.5

**Плейсхолдеры:**
```
%loveadaptation_mentor_name%     → "Максим"
%loveadaptation_mentor_adaptation%  → "Дельфин"
%loveadaptation_learning%         → "true" или "false"
```

---

### ✅ Идея 3: ADAPTATION AFFINITY (совместимость)

Некоторые адаптации работают лучше вместе (не конфликтуют):

```yaml
affinities:
  - name: "Морской странник"
    adaptations: ["WATER", "TRAVELER"]
    bonus_effects:
      - type: "SPEED"
        level: 1  # +1 уровень
      - type: "SATURATION"
        level: 1
    description: "WATER + TRAVELER → +25% скорость в воде, -25% голода"
  
  - name: "Геройский дух"
    adaptations: ["NETHER", "COMBAT"]
    bonus_effects:
      - type: "REGENERATION"
        level: 1  # +1 уровень
      - type: "STRENGTH"
        level: 1
    description: "NETHER + COMBAT → 200% регенерация в Нижнем мире"
  
  - name: "Подземный истребитель"
    adaptations: ["CAVE", "COMBAT"]
    bonus_effects:
      - type: "REGENERATION"
        level: 2  # +2 уровня
    description: "CAVE + COMBAT → 2x регенерация в пещерах"
```

**Как работает:**
- Система проверяет: АКТИВНА ли адаптация + УСЛОВИЯ выполнены ли?
- Если да → применяет AFFINITY-бонусы
- Плейсхолдер: `%loveadaptation_affinity_active%` → "Морской странник"

---

### ✅ Идея 4: SEASONAL ADAPTATIONS (сезонные)

Каждый месяц (или сезон) основной мир получает "сезонную адаптацию":

```yaml
seasonal_adaptations:
  december:
    name: "Ледяной странник"
    icon: "❄️"
    effects:
      - type: "FROST_WALKER"
        level: 1
      - type: "SPEED"
        level: 1
        apply_when: "walking_on_ice"
    damage_modifiers:
      FREEZE: 0.5  # -50% от ледяного урона
    unlock_hours: 8  # Быстрее разблокируется
    description: "Специальная адаптация на декабрь-февраль"

  march:
    name: "Зелёный чародей"
    icon: "🌿"
    effects:
      - type: "SPEED"
        level: 1
        apply_when: "in_jungle_or_forest"
    bonuses:
      crop_growth_speed: 1.5  # Растения растут на 50% быстрее
      leaf_drop_chance: 0.3  # 30% шанс дропа саженца
    description: "Весенняя адаптация (март-май)"

  june:
    name: "Солнечный странник"
    icon: "☀️"
    effects:
      - type: "SPEED"
        level: 1
        apply_only_when: "is_day"
      - type: "REGENERATION"
        level: 1
        apply_only_when: "is_day"
    description: "Летняя адаптация (июнь-август)"

  september:
    name: "Сборщик урожая"
    icon: "🌾"
    effects:
      - type: "LUCK"
        level: 2
        apply_when: "on_farm"
    bonuses:
      crop_drops_multiplier: 1.3  # +30% дропа урожая
    description: "Осенняя адаптация (сент-ноябрь)"
```

**Implementation:**
- Проверяем месяц при запуске плагина
- Заменяем одну "сезонную слот" адаптацию
- Уведомляем игроков: "Сезонная адаптация изменилась!"
- Плейсхолдер: `%loveadaptation_seasonal_name%` → "Ледяной странник"

---

### ✅ Идея 5: ADAPTATION COOLDOWN (перезарядка)

После переключения адаптации игрок не может снова переключиться N секунд:

```yaml
switching_cooldown:
  enabled: true
  cooldown_seconds: 30  # 30 сек между переключениями
  
  # При попытке переключиться:
  actionbar: "&c⏱ Дождись 15 сек перед переключением адаптации"
```

**Зачем:** 
- Предотвращает спам переключением
- Мотивирует игрока остаться в одной адаптации дольше
- Честивает способ получить все бонусы

**Плейсхолдеры:**
```
%loveadaptation_switch_cooldown_remaining%  → "15" (в секундах)
%loveadaptation_can_switch%                  → "true" / "false"
```

---

## 🔥 СРЕДНИЕ ИДЕИ (moderate complexity)

### ✅ Идея 6: ADAPTATION STREAKS & ACHIEVEMENTS

Если игрок активен в одной адаптации 1+ час подряд → unlock бонусный эффект:

```yaml
streaks:
  one_hour:
    name: "Новичок адаптации"
    requirement: "1 час активности"
    reward_effect:
      type: "SATURATION"
      level: 1
      duration: 300  # 5 минут

  six_hours:
    name: "Опытный адепт"
    requirement: "6 часов активности"
    reward_effect:
      type: "REGENERATION"
      level: 1
      duration: 600  # 10 минут
    reward_xp: 100

  twenty_four_hours:
    name: "Мастер адаптации"
    requirement: "24 часа активности"
    reward_effect:
      type: "RESISTANCE"
      level: 1
      duration: 1200  # 20 минут
    reward_xp: 500
    unlock_achievement: "true"
```

**Плейсхолдеры:**
```
%loveadaptation_current_streak_time%   → "2h 34m" (сколько в текущей адаптации)
%loveadaptation_longest_streak_time%   → "24h 15m" (рекорд)
%loveadaptation_streak_level%          → "Master"
```

---

### ✅ Идея 7: ADAPTATION MEMORY (история)

В БД сохранять полную историю адаптаций:

```sql
CREATE TABLE adaptation_memory (
    uuid TEXT,
    adaptation_name VARCHAR(50),
    first_unlocked_date BIGINT,
    total_active_time LONG,  -- сколько всего времени была активна
    times_activated INT,  -- сколько раз переключалась
    peak_progress FLOAT,  -- максимальный % когда-либо
    current_streak_time LONG,  -- текущий streak
    longest_streak_time LONG,  -- рекордный streak
    last_degraded_date BIGINT,
    achievement_unlocked BOOLEAN
);
```

**Плейсхолдеры в профиле:**
```
%loveadaptation_water_first_unlock%    → "2026-07-15"
%loveadaptation_water_total_active_time% → "48h 23m"
%loveadaptation_water_times_activated% → "12" раз
%loveadaptation_water_achievement%     → "Master of Water" или "None"
```

---

### ✅ Идея 8: ADAPTATION DECAY OVER TIME (снижение)

Если игрок не пользуется адаптацией 7+ дней → прогресс снижается:

```yaml
decay_system:
  enabled: true
  decay_after_days: 7
  decay_rate: 0.05  # -5% в день, если не используется
  min_progress: 10  # Не может упасть ниже 10%
  notification_days: [1, 3, 7]  # Уведомлять перед decay
  
  # На день 7:
  actionbar: "&c⚠ ВНИМАНИЕ: адаптация %adaptation_name% теряет прогресс! 
              Используй её, чтобы спасти!"
```

**Зачем:** Мотивирует разнообразие, не фокусироваться на одной адаптации вечно

---

### ✅ Идея 9: ADAPTATION TALENT TREE (выбор развития)

Когда адаптация достигает 90%, предложить игроку выбрать "специализацию":

```yaml
water_talents:
  option_a:
    name: "Охотник на кальмаров"
    description: "Чернила падают чаще (+50%)"
    bonus: 
      squid_ink_drop_rate: 1.5
  
  option_b:
    name: "Ловец жемчуга"
    description: "Жемчуга находятся лучше (+50%)"
    bonus:
      pearl_drop_rate: 1.5
  
  option_c:
    name: "Ускоритель"
    description: "SPEED 4 вместо 3"
    bonus:
      speed_level: 4

nether_talents:
  option_a:
    name: "Рубиновый охотник"
    description: "Древний мусор падает 2x чаще"
    bonus:
      ancient_debris_chance: 2.0
  
  option_b:
    name: "Лавовый странник"
    description: "-30% урома от лавы (дополнительно)"
    bonus:
      lava_damage_reduction: 0.3
  
  option_c:
    name: "Гнезданец"
    description: "Гнёзда гхастов видны светятся (как враги)"
    bonus:
      ghast_nest_visibility: "glowing"
```

**Механика:**
- При достижении 90% → menu появляется в GUI
- Выбор применяется и сохраняется в БД
- Можно переделать за редкий ресурс или "точку переspec'а"

---

## 💎 СЛОЖНЫЕ ИДЕИ (hard to implement)

### ✅ Идея 10: DUAL ADAPTATION MODE (синергия)

После достижения 90% в двух адаптациях одновременно (через зелье или другой способ):

```yaml
dual_modes:
  - name: "Паровой воин"
    primary: "WATER"
    secondary: "NETHER"
    unlocks_after: "both 90%"
    duration_ticks: 12000  # 10 минут
    cooldown_ticks: 36000  # 1 час перезарядки
    effects:
      - type: "SPEED"
        level: 4
      - type: "STRENGTH"
        level: 1
      - type: "FIRE_RESISTANCE"
        level: 2
    bonus_damage:
      against_water_mobs: 1.5
      against_nether_mobs: 1.5
    player_effect: "GLOWING"  # Светишься красным гулем
    description: "Огненная вода, боец хаоса"

  - name: "Тень пещер"
    primary: "CAVE"
    secondary: "COMBAT"
    effects:
      - type: "NIGHT_VISION"
        level: 2
      - type: "REGENERATION"
        level: 4
      - type: "SPEED"
        level: 2

  - name: "Парящий странник"
    primary: "END"
    secondary: "HEIGHT"
    effects:
      - type: "SLOW_FALLING"
        level: 2
      - type: "JUMP_BOOST"
        level: 3
```

**Активация:** Зелье адаптации II уровня (2 минуты) или специальный крафт/команда

---

### ✅ Идея 11: ADAPTATION MUTATION SYSTEM (мутация)

После достижения 90% в трёх адаптациях, разблокируется "слот мутации":

```yaml
mutations:
  - name: "Гибридный исследователь"
    required_adaptations: ["WATER", "CAVE", "TRAVEL"]  # Все 3 на 90%+
    effects:
      - type: "SPEED"
        level: 3
      - type: "NIGHT_VISION"
        level: 1
      - type: "WATER_BREATHING"
        level: 1
    passive_bonuses:
      hunger_modifier: 0.6  # -40% голода
      water_speed_boost: 1.5
    description: "Вода + Пещеры + Путешествие = Экспериментатор пещер"

  - name: "Боевой аристократ"
    required_adaptations: ["COMBAT", "NETHER", "HEIGHT"]
    effects:
      - type: "STRENGTH"
        level: 2
      - type: "REGENERATION"
        level: 3
      - type: "FIRE_RESISTANCE"
        level: 1
    passive_bonuses:
      damage_reduction: 0.2  # 20% меньше урома всегда
      healing_multiplier: 1.5
```

**Как использовать:**
- Достичь 90% в трёх адаптациях
- Открыть вкладку "Мутации" в GUI
- Выбрать мутацию → активируется поверх одной из адаптаций
- Мутация занимает место одной адаптации (например, WATER становится "Гибридный исследователь")

---

### ✅ Идея 12: ADAPTATION COMBO CHAIN (комбо цепь)

Если последовательно активировать 3+ адаптации подряд без перерыва:

```yaml
combo_chains:
  three_chain:
    length: 3
    time_window: 300  # 5 минут на переключение
    reward:
      multiplier: 1.2  # +20% ко всем эффектам
      duration: 1800  # 30 минут
      bonus_effect:
        type: "LUCK"
        level: 1

  five_chain:
    length: 5
    time_window: 600  # 10 минут на переключение
    reward:
      multiplier: 1.5  # +50% ко всем эффектам
      duration: 3600  # 1 час
      bonus_effect:
        type: "LUCK"
        level: 2

  seven_chain:
    length: 7
    time_window: 900  # 15 минут
    reward:
      multiplier: 2.0  # ДВОЙНОЙ урон/защита!
      duration: 7200  # 2 часа
      bonus_effect:
        type: "LUCK"
        level: 3
      unlock_achievement: true
```

**Отслеживание в БД:**
```sql
CREATE TABLE combo_chain_history (
    uuid TEXT,
    adaptations_sequence TEXT,  -- "WATER,NETHER,CAVE"
    started_at BIGINT,
    completed_at BIGINT,
    multiplier FLOAT,
    chain_length INT
);
```

**Плейсхолдеры:**
```
%loveadaptation_combo_length%    → "3" (текущая цепь)
%loveadaptation_combo_multiplier% → "1.2" 
%loveadaptation_combo_time_left%  → "2m 34s" (до истечения)
```

---

### ✅ Идея 13: ADAPTATION SOULBIND (душевная связь)

Адаптация связана с душой персонажа и не теряется при смерти, но "повреждается":

```yaml
soulbind_system:
  enabled: true
  on_death:
    damage_percent: 10  # -10% прогресса при смерти
    effect_duration: 60  # Эффекты работают ещё 1 минуту после смерти
    notification: "&c⚠ Твоя адаптация повреждена на смерти!"
  
  recovery:
    recovery_rate: 1  # +1% в минуту
    full_recovery_time: 600  # 10 минут на восстановление
    notification_at_percents: [50, 75, 100]
    
    actionbar_recovering: "&7🔧 Восстановление адаптации: %progress%"
    actionbar_recovered: "&a✓ Адаптация полностью восстановлена!"
```

**Что даёт:**
- Адаптация не теряется при смерти (не как другие эффекты)
- Но срабатывает "волшебное восстановление" (анимация + звук)
- Мотивирует быть осторожнее

---

### ✅ Идея 14: PVP ADAPTATION BALANCE

В PvP зонах адаптации считаются по-другому:

```yaml
pvp_balance:
  enabled: true
  pvp_zone_identifier: "LuckPerms" # или WorldGuard regions
  
  # Урон от игроков считается отдельно
  combat_damage_multiplier: 2.0  # Урам от игроков = 2x от мобов
  
  # Адаптации разблокируются быстрее в PvP
  pvp_unlock_speed: 1.5  # +50% скорости разблокировки
  
  special_effects:
    # В PvP режиме дополнительные бонусы за боевую адаптацию
    combat_adaptation_bonus_in_pvp:
      healing_multiplier: 3.0  # 3x регенерация в PvP
      resistance_boost: 2.0  # 2x сопротивление урому
```

---

## 🌟 УЛЬТИМАТИВНЫЕ ИДЕИ (game-changing)

### ✅ Идея 15: ADAPTATION SACRIFICE (жертва)

Игрок может сознательно деградировать адаптацию для получения редкого ресурса:

```yaml
sacrifice_system:
  enabled: true
  sacrifice_threshold: 50  # Нужно деградировать до 50%
  
  rewards_by_adaptation:
    - adaptation: "WATER"
      item: "ADAPTATION_CRYSTAL_AQUATIC"
      quantity: 1
      
    - adaptation: "COMBAT"
      item: "ADAPTATION_CRYSTAL_WAR"
      quantity: 1
```

**Использование кристаллов:**
- Скомбинировать с зельем адаптации → "Супер-зелье" (все эффекты на макс 5 минут)
- Или использовать для разблокировки "Мутаций"
- Или продать NPС за дорого

---

### ✅ Идея 16: ADAPTATION LEGEND TIER

После 5 уровней Prestige разблокируется "LEGEND TIER":

```yaml
legend_tier:
  unlock_requirement: "5 prestige levels"
  effects:
    - passive_multiplier: 2.0  # 2x все эффекты всегда
    - immunity_to_degradation: true  # Адаптации не деградируют
    - simultaneous_adaptations: 2  # Одновременно 2 адаптации активны!
    - auto_reactivation: true  # Автоматически переключается на 90%+ адаптацию
  
  title: "&6&lЛегенда адаптаций"
  
  unique_particle_effect: "FLAME"  # Окружена магией
```

**Это ендгейм контент** — даёт огромное преимущество

---

### ✅ Идея 17: ADAPTATION LEVELING (уровни)

После 90% адаптация может качаться дальше до "Уровня N":

```yaml
adaptation_levels:
  level_1:
    progress_percent: 0-90
    effects: "БАЗОВЫЕ"
  
  level_2:
    progress_percent: 91-100
    effects: "БАЗОВЫЕ + БОНУСЫ"
  
  level_3:  # НОВОЕ: сверх 100%
    progress_percent: 101-150
    effects: "БАЗОВЫЕ + БОНУСЫ + УРОВЕНЬ 3"
    unlock_cost: "50 Adaptation Crystals"
    special_effect:
      type: "HASTE"
      level: 1
  
  level_4:
    progress_percent: 151-200
    unlock_cost: "100 Adaptation Crystals"
    special_effect:
      type: "HASTE"
      level: 2

  level_5:
    progress_percent: 201-250
    unlock_cost: "150 Adaptation Crystals"
    effects_max: true
    special_effect:
      type: "CONDUIT_POWER"
      level: 2
```

**Плейсхолдеры:**
```
%loveadaptation_water_level%        → "3"
%loveadaptation_water_level_name%   → "Продвинуто"
%loveadaptation_water_next_level_cost%  → "50 кристаллов"
```

---

## 📈 СТАТИСТИКА & ЛИДЕРБОРДЫ

### ✅ Идея 18: ADAPTATION LEADERBOARD

Глобальный лидерборд по адаптациям:

```yaml
leaderboards:
  global_mastery:
    # Кто достиг 90% первым?
    display_top: 10
    reset: "never"
    
  monthly_activations:
    # Кто активирует адаптацию чаще всех в месяц?
    display_top: 5
    reset: "monthly"
    
  longest_streak:
    # Кто дольше всего использует одну адаптацию?
    display_top: 5
    reset: "never"
    
  combo_chain_record:
    # Кто собрал самую длинную комбо?
    display_top: 5
    reset: "never"
```

**Команда:** `/loveadaptation leaderboard [type]`

---

### ✅ Идея 19: ADAPTATION STATS

Детальная статистика адаптации в профиле:

```
/loveadaptation stats

═══════════════════════════════════════
    📊 СТАТИСТИКА АДАПТАЦИЙ
═══════════════════════════════════════

🌊 WATER
  Прогресс: 95% ⭐
  Первое разблокирование: 2026-07-15
  Общее активное время: 48h 23m
  Раз переключалась: 12
  Рекордный streak: 24h 15m
  Статус: ✓ Активна сейчас
  
🔥 NETHER
  Прогресс: 70%
  Общее активное время: 18h 30m
  Раз переключалась: 5
  Статус: 🔒 Заблокирована

[...остальные...]

═══════════════════════════════════════
Рекорд общего времени: 120h (TRAVELER)
Текущий streak: 2h 34m (WATER)
```

---

## 🎭 СОЦИАЛЬНЫЕ МЕХАНИКИ

### ✅ Идея 20: ADAPTATION GUILDS (гильдии адаптаций)

Игроки могут объединяться в гильдии по типу адаптации:

```yaml
adaptation_guilds:
  water_guild:
    name: "Общество дельфинов"
    members_only_effect: "+10% скорость в воде для членов"
    chest: "общий гильдийский сундук"
    rank_system: "Member, Elder, Guild Master"
  
  combat_guild:
    name: "Круг воинов"
    members_only_effect: "+5% регенерация в боях"
    tournament: "ежемесячный турнир PvP"
```

**Функционал:**
- Кооперативная тренировка (идея #2 + гильдия)
- Гильдийские квесты ("разблокируйте 50% адаптации вместе")
- Гильдийский уровень (все члены в одном уровне)

---

## 🔧 АДМИНИСТРАТОРСКИЕ КОМАНДЫ

```bash
/loveadaptation admin reset-all                    # Сбросить ВСЕ адаптации всем
/loveadaptation admin set-progress <player> <adapt> <percent>
/loveadaptation admin give-crystal <player> <type> <qty>
/loveadaptation admin enable-mutation <player> <mutation_name>
/loveadaptation admin leaderboard-reset <type>
/loveadaptation admin prestige <player> [level]   # Дать престиж
/loveadaptation admin force-adaptation <player> <adaptation>
```

---

## 📋 IMPLEMENTATION ROADMAP

```
v1.0 - БАЗОВАЯ СИСТЕМА (текущая)
├─ 7 адаптаций
├─ Блокировка, деградация, реактивация
├─ GUI меню (54 слота)
├─ Плейсхолдеры
└─ Зелья адаптации I-II

v1.1 - СОЦИАЛЬНЫЕ МЕХАНИКИ
├─ Идея 1: PRESTIGE
├─ Идея 2: TEACHING
├─ Идея 3: AFFINITY
└─ Идея 5: COOLDOWN

v1.2 - СЕЗОННОСТЬ
├─ Идея 4: SEASONAL
├─ Идея 6: STREAKS & ACHIEVEMENTS
├─ Идея 7: MEMORY
└─ Идея 8: DECAY

v1.3 - СПЕЦИАЛИЗАЦИЯ
├─ Идея 9: TALENT TREE
├─ Идея 15: SACRIFICE
└─ Идея 17: LEVELING

v2.0 - УЛЬТРА
├─ Идея 10: DUAL MODE
├─ Идея 11: MUTATIONS
├─ Идея 12: COMBO CHAIN
├─ Идея 13: SOULBIND
└─ Идея 14: PVP BALANCE

v2.1 - СОЦИАЛ
├─ Идея 18: LEADERBOARDS
├─ Идея 19: STATS
└─ Идея 20: GUILDS

v3.0 - ЕНДГЕЙМ
├─ Идея 16: LEGEND TIER
└─ Остальные комбинации
```

---

**Версия:** Ideas Collection 1.0  
**Дата:** 2026-07-26  
**Статус:** Ready for brainstorming
