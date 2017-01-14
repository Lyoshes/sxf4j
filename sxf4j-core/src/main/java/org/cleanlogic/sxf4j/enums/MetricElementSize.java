package org.cleanlogic.sxf4j.enums;

import java.util.HashMap;
import java.util.Map;

/**
 * @author Serge Silaev aka iSergio <s.serge.b@gmail.com>
 */
public enum MetricElementSize {
    /**
     * 2 байта(для целочисленного значения)
     */
    SHORT,
    /**
     * 4 байта(для плавающей точки)
     */
    FLOAT,
    /**
     * 4 байта(для целочисленного значения)
     */
    INT,
    /**
     * 8 байта(для плавающей точки)
     */
    DOUBLE;

    private static final Map<MetricElementSize, String> _names = new HashMap<>();
    static {
        _names.put(SHORT, "2 байта(для целочисленного значения)");
        _names.put(FLOAT, "4 байта(для плавающей точки)");
        _names.put(INT, "4 байта(для целочисленного значения)");
        _names.put(DOUBLE, "8 байта(для плавающей точки)");
    }

    public static MetricElementSize fromValue(int value) {
        return values()[value];
    }

    public String getName() {
        return _names.get(this);
    }

    public static Map<MetricElementSize, String> getNames() {
        return _names;
    }

    public static String getName(MetricElementSize metricElementSize) {
        return _names.get(metricElementSize);
    }
}
