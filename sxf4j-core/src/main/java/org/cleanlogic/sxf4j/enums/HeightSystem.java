package org.cleanlogic.sxf4j.enums;

import java.util.HashMap;
import java.util.Map;

/**
 * Supported height systems by SXF format
 * @author Serge Silaev aka iSergio <s.serge.b@gmail.com>
 */
public enum HeightSystem {
    /**
     * Не установлено
     */
    UNDEFINED(-1),
    /**
     * Балтийская система высот
     */
    BALTIC(1),
    /**
     * Австралийская система высот 1971г.
     */
    AUSTRALIAN(2),
    /**
     * Средний уровень Адриатического моря в Триесте (Австрия, Югославия)
     */
    ADRIATIC(3),
    /**
     * Средний уровень Северного моря в Остенде "Зеро-Нормаль"
     */
    OSTAND_ZERO_NORMAL(4),
    /**
     * Средний уровень низких вод Северного моря в Остенде - "Нуль понт дешоссе" (Бельгия)
     */
    OSTEND_NULL_PONT_DEHOSSE(5),
    /**
     * Средний уровень моря в проливе Ламанш
     */
    LAMANSH(6),
    /**
     * Средний уровень Ирландского моря в Белфасте (Северная Ирландия)
     */
    BELFEST(7),
    /**
     * Средний уровень Атлантического океана в Малик-Xед (Ирландия)
     */
    MALIC_XED(8),
    /**
     * Уровень низкой воды в Дублинском заливе (Ирландия)
     */
    DUBLIN(9),
    /**
     * Средний уровень Эгейского моря в порту Пирей (Греция)
     */
    AEGEAN(10),
    /**
     * Средний уровень моря у датского побережья (Дания)
     */
    DENMARK(11),
    /**
     * Средний уровень залива Фахсафлоуи у Рейкьявика (Исландия)
     */
    REYKJAVIK(12),
    /**
     * Средний уровень Средиземного моря в Аликанте (Испания)
     */
    ALIKANT(13),
    /**
     * Средний уровень Атлантического океана (для Канарских островов)
     */
    CANARIAS(14),
    /**
     * Средний уровень Лигурийского моря в Генуе (Италия)
     */
    GENUE(15),
    /**
     * Средний уровень Cеверного мор
     */
    NORMALNULL(16),
    /**
     * Средний уровень моря в Осло - "Норвежский нормальный нуль" (Южная Норвегия)
     */
    OSLO(17),
    /**
     * Средний уровень моря в бухте Нарвик (Северная Норвегия)
     */
    NARVIC(18),
    /**
     * Средний уровень Атлантического океана в Кашкаиш (Португалия)
     */
    CASCAIS(19),
    /**
     * Средний уровень Балтийского моря в Xельсинки (Финляндия)
     */
    HELSINKI(20),
    /**
     * Средний уровень воды у шведских берегов (Швеция)
     */
    SHWEDEN(21),
    /**
     * Средний уровень Средиземного моря в Марселе
     */
    MARSEL(22),
    /**
     * Средний уровень морей, омывающих Турцию (Турция)
     */
    TURKEY_NORMAL(23),
    /**
     * Средний уровень морей и океанов,омывающих США и Канаду
     */
    USAKANADA(24),
    /**
     * Балтийская система 1977 г.
     */
    BALTIC77(25),
    /**
     * Средний уровень Охотского моря и Тихого океана
     */
    OKHOTSK(26),
    /**
     * Средний уровень мирового океана
     */
    PEACEOCEAN(27);

    private static final Map<HeightSystem, String> _names = new HashMap<>();
    static {
        _names.put(UNDEFINED, "Не установлено");
        _names.put(BALTIC, "Балтийская система высот");
        _names.put(AUSTRALIAN, "Австралийская система высот 1971г.");
        _names.put(ADRIATIC, "Средний уровень Адриатического моря в Триесте (Австрия, Югославия)");
        _names.put(OSTAND_ZERO_NORMAL, "Средний уровень Северного моря в Остенде \"Зеро-Нормаль\"");
        _names.put(OSTEND_NULL_PONT_DEHOSSE, "Средний уровень низких вод Северного моря в Остенде - \"Нуль понт дешоссе\" (Бельгия)");
        _names.put(LAMANSH, "Средний уровень моря в проливе Ламанш");
        _names.put(BELFEST, "Средний уровень Ирландского моря в Белфасте (Северная Ирландия)");
        _names.put(MALIC_XED, "Средний уровень Атлантического океана в Малик-Xед (Ирландия)");
        _names.put(DUBLIN, "Уровень низкой воды в Дублинском заливе (Ирландия)");
        _names.put(AEGEAN, "Средний уровень Эгейского моря в порту Пирей (Греция)");
        _names.put(DENMARK, "Средний уровень моря у датского побережья (Дания)");
        _names.put(REYKJAVIK, "Средний уровень залива Фахсафлоуи у Рейкьявика (Исландия)");
        _names.put(ALIKANT, "Средний уровень Средиземного моря в Аликанте (Испания)");
        _names.put(CANARIAS, "Средний уровень Атлантического океана (для Канарских островов)");
        _names.put(GENUE, "Средний уровень Лигурийского моря в Генуе (Италия)");
        _names.put(NORMALNULL, "Средний уровень Cеверного мор");
        _names.put(OSLO, "Средний уровень моря в Осло - \"Норвежский нормальный нуль\" (Южная Норвегия)");
        _names.put(NARVIC, "Средний уровень моря в бухте Нарвик (Северная Норвегия)");
        _names.put(CASCAIS, "Средний уровень Атлантического океана в Кашкаиш (Португалия)");
        _names.put(HELSINKI, "Средний уровень Балтийского моря в Xельсинки (Финляндия)");
        _names.put(SHWEDEN, "Средний уровень воды у шведских берегов (Швеция)");
        _names.put(MARSEL, "Средний уровень Средиземного моря в Марселе");
        _names.put(TURKEY_NORMAL, "Средний уровень морей, омывающих Турцию (Турция)");
        _names.put(USAKANADA, "Средний уровень морей и океанов,омывающих США и Канаду");
        _names.put(BALTIC77, "Балтийская система 1977 г.");
        _names.put(OKHOTSK, "Средний уровень Охотского моря и Тихого океана");
        _names.put(PEACEOCEAN, "Средний уровень мирового океана");
    }

    private static final Map<Integer, HeightSystem> _intToEnumMap = new HashMap<>();
    static {
        for (HeightSystem heightSystem : values()) {
            _intToEnumMap.put(heightSystem.getValue(), heightSystem);
        }
    }

    private final int _value;

    HeightSystem(int value) {
        _value = value;
    }

    public static HeightSystem fromValue(int value) {
        return _intToEnumMap.get(value);
    }

    public int getValue() {
        return _value;
    }

    public String getName() {
        return _names.get(this);
    }

    public static Map<HeightSystem, String> getNames() {
        return _names;
    }

    public static String getName(HeightSystem heightSystem) {
        return _names.get(heightSystem);
    }
}
