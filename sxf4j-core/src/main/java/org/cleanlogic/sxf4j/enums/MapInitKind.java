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

    private static final Map<MapInitKind, String> _names = new HashMap<>();
    static {
        _names.put(UNDEFINED, "Не установлено");
        _names.put(MAP, "Картографический");
        _names.put(PHOTO, "Фотоплан");
        _names.put(IMAGE, "Фотоснимок");
        _names.put(GRAM, "Фотограмметрический");
    }
    private static final Map<Integer, MapInitKind> _intToEnumMap = new HashMap<>();
    static {
        for (MapInitKind mapInitKind : values()) {
            _intToEnumMap.put(mapInitKind.getValue(), mapInitKind);
        }
    }
    private final int _value;
    MapInitKind(int value) {
        _value = value;
    }

    public static MapInitKind fromValue(int value) {
        if (!_intToEnumMap.containsKey(value)) {
            return MAP;
        }
        return _intToEnumMap.get(value);
    }

    public int getValue() {
        return _value;
    }

    public String getName() {
        return _names.get(this);
    }

    public static Map<MapInitKind, String> getNames() {
        return _names;
    }

    public static String getName(MapInitKind mapInitKind) {
        return _names.get(mapInitKind);
    }
}
