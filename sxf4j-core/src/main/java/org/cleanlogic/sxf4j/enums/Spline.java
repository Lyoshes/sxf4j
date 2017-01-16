package org.cleanlogic.sxf4j.enums;

import java.util.HashMap;
import java.util.Map;

/**
 * Supported metric spline by SXF format
 * @author Serge Silaev aka iSergio <s.serge.b@gmail.com>
 */
public enum Spline {
    NO_SPLINE,
    SMOOTHING,
    ENVELOPING;

    /**
     * Map contains all enums names. Its will be used in ComboBox's and etc.
     */
    private final static Map<Spline, String> _names = new HashMap<>();
    static {
        _names.put(NO_SPLINE, "Построение сплайна при визуализации не выполняется");
        _names.put(SMOOTHING, "Сглаживающий сплайн (срезание углов)");
        _names.put(ENVELOPING, "Огибающий сплайн (проходит через все точки метрики)");
    }

    /**
     * Converts from integer value into enum.
     * @param value integer value of enum.
     * @return enum by integer.
     */
    public static Spline fromValue(int value) {
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
    public static Map<Spline, String> getNames() {
        return _names;
    }

    /**
     * Get name of enum
     * @param spline enum
     * @return name
     */
    public static String getName(Spline spline) {
        return _names.get(spline);
    }
}
