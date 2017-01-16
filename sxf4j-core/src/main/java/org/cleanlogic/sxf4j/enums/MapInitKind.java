package org.cleanlogic.sxf4j.enums;

import java.util.HashMap;
import java.util.Map;

/**
 * Supported map init kinds by SXF format
 * @author Serge Silaev aka iSergio <s.serge.b@gmail.com>
 */
public enum MapInitKind {
    /**
     * Не установлено
     */
    UNDEFINED(-1),
    /**
     * Картографический
     */
    MAP(1),
    /**
     * Фотоплан
     */
    PHOTO(2),
    /**
     * Фотоснимок
     */
    IMAGE(3),
    /**
     * Фотограмметрический
     */
    GRAM(4);

    /**
     * Map contains all enums names. Its will be used in ComboBox's and etc.
     */
    private static final Map<MapInitKind, String> _names = new HashMap<>();
    static {
        _names.put(UNDEFINED, "Не установлено");
        _names.put(MAP, "Картографический");
        _names.put(PHOTO, "Фотоплан");
        _names.put(IMAGE, "Фотоснимок");
        _names.put(GRAM, "Фотограмметрический");
    }
    /**
     * Map contains integer (value) and enum object.
     */
    private static final Map<Integer, MapInitKind> _intToEnumMap = new HashMap<>();
    static {
        for (MapInitKind mapInitKind : values()) {
            _intToEnumMap.put(mapInitKind.getValue(), mapInitKind);
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
    MapInitKind(int value) {
        _value = value;
    }

    /**
     * Converts from integer value into enum.
     * @param value integer value of enum.
     * @return enum by integer.
     */
    public static MapInitKind fromValue(int value) {
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
    public static Map<MapInitKind, String> getNames() {
        return _names;
    }

    /**
     * Get name of enum
     * @param mapInitKind enum
     * @return name
     */
    public static String getName(MapInitKind mapInitKind) {
        return _names.get(mapInitKind);
    }
}
