package org.cleanlogic.sxf4j.enums;

import java.util.HashMap;
import java.util.Map;

/**
 * Supported object locals by SXF format
 * @author Serge Silaev aka iSergio <s.serge.b@gmail.com>
 */
public enum Local {
    /**
     * ЛИНЕЙНЫЕ
     */
    LINE,
    /**
     * ПЛОЩАДНЫЕ
     */
    SQUARE,
    /**
     * ТОЧЕЧНЫЕ
     */
    POINT,
    /**
     * ПОДПИСИ
     */
    TITLE,
    /**
     * ВЕКТОРНЫЕ
     */
    VECTOR,
    /**
     * ШАБЛОНЫ
     */
    MIXED;
    /**
     * Map contains all enums names. Its will be used in ComboBox's and etc.
     */
    private final static Map<Local, String> _names = new HashMap<>();
    static {
        _names.put(LINE, "ЛИНЕЙНЫЕ");
        _names.put(SQUARE, "ПЛОЩАДНЫЕ");
        _names.put(POINT, "ТОЧЕЧНЫЕ");
        _names.put(TITLE, "ПОДПИСИ");
        _names.put(VECTOR, "ВЕКТОРНЫЕ");
        _names.put(MIXED, "ШАБЛОНЫ");
    }

    /**
     * Converts from integer value into enum.
     * @param value integer value of enum.
     * @return enum by integer.
     */
    public static Local fromValue(int value) {
        return values()[value];
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
    public static Map<Local, String> getNames() {
        return _names;
    }

    /**
     * Get name of enum
     * @param local enum
     * @return name
     */
    public static String getName(Local local) {
        return _names.get(local);
    }
}
