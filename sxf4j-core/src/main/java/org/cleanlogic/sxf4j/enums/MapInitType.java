package org.cleanlogic.sxf4j.enums;

import java.util.HashMap;
import java.util.Map;

/**
 * Supported map init types by SXF format
 * @author Serge Silaev aka iSergio <s.serge.b@gmail.com>
 */
public enum MapInitType {
    /**
     * Не установлено
     */
    UNDEFINED(-1),
    /**
     * Тиражный оттиск
     */
    MAPRUN(1),
    /**
     * Издательский оригинал
     */
    FINAL(2),
    /**
     * Составительский оригинал
     */
    MANUSCRIPT(3),
    /**
     * Оригинал изменений
     */
    UPDATE(4),
    /**
     * Специальный оригинал
     */
    SPECIAL(5),
    /**
     * Диапозитив постоянного хранения
     */
    CONSTANT(6),
    // Вид исходного фотоматериала
    /**
     * Фотограмметрический материал
     */
    FGM(7),
    /**
     * ФГМ и тиражный оттиск
     */
    FGMMAPRUN(8),
    /**
     * ФГМ и составительский оригинал
     */
    FGMMANUSCRIPT(9),
    /**
     * ФГМ и издательский оригинал
     */
    FGMFINAL(10),
    /**
     * ФГМ и диапозитив постоянного хранения
     */
    FGMCONSTANT(11),
    /**
     * ФГМ и специальный оригинал
     */
    FGMSPECIAL(12),
    /**
     * Космические
     */
    SPACE(64),
    /**
     * Аэроснимки
     */
    AERO(65),
    /**
     * Фототеодолитные снимки
     */
    PHOTOGRAM(66);

    private static final Map<MapInitType, String> _names = new HashMap<>();
    static {
        _names.put(UNDEFINED, "Не установлено");
        _names.put(MAPRUN, "Тиражный оттиск");
        _names.put(FINAL, "Издательский оригинал");
        _names.put(MANUSCRIPT, "Составительский оригинал");
        _names.put(UPDATE, "Оригинал изменений");
        _names.put(SPECIAL, "Специальный оригинал");
        _names.put(CONSTANT, "Диапозитив постоянного хранения");
        // Вид исходного фотоматериала
        _names.put(FGM, "Фотограмметрический материал");
        _names.put(FGMMAPRUN, "ФГМ и тиражный оттиск");
        _names.put(FGMMANUSCRIPT, "ФГМ и составительский оригинал");
        _names.put(FGMFINAL, "ФГМ и издательский оригинал");
        _names.put(FGMCONSTANT, "ФГМ и диапозитив постоянного хранения");
        _names.put(FGMSPECIAL, "ФГМ и специальный оригинал");
        _names.put(SPACE, "Космические");
        _names.put(AERO, "Аэроснимки");
        _names.put(PHOTOGRAM, "Фототеодолитные снимки");
    }
    private static final Map<Integer, MapInitType> _intToEnumMap = new HashMap<>();
    static {
        for (MapInitType mapInitType : values()) {
            _intToEnumMap.put(mapInitType.getValue(), mapInitType);
        }
    }

    private final int _value;

    MapInitType(int value) {
        _value = value;
    }

    public static MapInitType fromValue(int value) {
        if (!_intToEnumMap.containsKey(value)) {
            return MAPRUN;
        }
        return _intToEnumMap.get(value);
    }

    public int getValue() {
        return _value;
    }

    public String getName() {
        return _names.get(this);
    }

    public static Map<MapInitType, String> getNames() {
        return _names;
    }

    public static String getName(MapInitType mapInitType) {
        return _names.get(mapInitType);
    }
}
