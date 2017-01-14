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

    private final static Map<Local, String> _names = new HashMap<>();
    static {
        _names.put(LINE, "ЛИНЕЙНЫЕ");
        _names.put(SQUARE, "ПЛОЩАДНЫЕ");
        _names.put(POINT, "ТОЧЕЧНЫЕ");
        _names.put(TITLE, "ПОДПИСИ");
        _names.put(VECTOR, "ВЕКТОРНЫЕ");
        _names.put(MIXED, "ШАБЛОНЫ");
    }

    public static Local fromValue(int value) {
        return values()[value];
    }

    public String getName() {
        return _names.get(this);
    }

    public static Map<Local, String> getNames() {
        return _names;
    }

    public static String getName(Local local) {
        return _names.get(local);
    }
}
