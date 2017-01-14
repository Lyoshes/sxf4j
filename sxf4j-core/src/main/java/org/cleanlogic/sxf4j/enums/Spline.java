package org.cleanlogic.sxf4j.enums;

import java.util.HashMap;
import java.util.Map;

/**
 * @author Serge Silaev aka iSergio <s.serge.b@gmail.com>
 */
public enum Spline {
    NO_SPLINE,
    SMOOTHING,
    ENVELOPING;

    private final static Map<Spline, String> _names = new HashMap<>();
    static {
        _names.put(NO_SPLINE, "Построение сплайна при визуализации не выполняется");
        _names.put(SMOOTHING, "Сглаживающий сплайн (срезание углов)");
        _names.put(ENVELOPING, "Огибающий сплайн (проходит через все точки метрики)");
    }

    public static Spline fromValue(int value) {
        return values()[value];
    }

    public String getName() {
        return _names.get(this);
    }

    public static Map<Spline, String> getNames() {
        return _names;
    }

    public static String getName(Spline spline) {
        return _names.get(spline);
    }
}
