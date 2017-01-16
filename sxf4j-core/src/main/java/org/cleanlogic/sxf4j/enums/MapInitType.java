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

    /**
     * Map contains all enums names. Its will be used in ComboBox's and etc.
     */
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
    /**
     * Map contains integer (value) and enum object.
     */
    private static final Map<Integer, MapInitType> _intToEnumMap = new HashMap<>();
    static {
        for (MapInitType mapInitType : values()) {
            _intToEnumMap.put(mapInitType.getValue(), mapInitType);
        }
    }

    /**
     * Current value of enum.
     */
    private final int _value;

    /**
     * Default constructor.
     * @param value value of enum.
     */
    MapInitType(int value) {
        _value = value;
    }

    /**
     * Converts from integer value into enum.
     * @param value integer value of enum.
     * @return enum by integer.
     */
    public static MapInitType fromValue(int value) {
        return _intToEnumMap.get(value);
    }

    /**
     * Get integer value of enum.
     * @return integer value.
     */
    public int getValue() {
        return _value;
    }

    /**
     * Get name of enum.
     * @return name of enum.
     */
    public String getName() {
        return _names.get(this);
    }

    /**
     * Get list of name of all enums.
     * @return name list of all enums.
     */
    public static Map<MapInitType, String> getNames() {
        return _names;
    }

    /**
     * Get name of enum
     * @param mapInitType enum
     * @return name
     */
    public static String getName(MapInitType mapInitType) {
        return _names.get(mapInitType);
    }
}
