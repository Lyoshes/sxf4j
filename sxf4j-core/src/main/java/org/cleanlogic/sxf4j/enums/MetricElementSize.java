package org.cleanlogic.sxf4j.enums;

import java.util.HashMap;
import java.util.Map;

/**
 * Supported metric elements size by SXF format.
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

    /**
     * Map contains all enums names. Its will be used in ComboBox's and etc.
     */
    private static final Map<MetricElementSize, String> _names = new HashMap<>();
    static {
        _names.put(SHORT, "2 байта(для целочисленного значения)");
        _names.put(FLOAT, "4 байта(для плавающей точки)");
        _names.put(INT, "4 байта(для целочисленного значения)");
        _names.put(DOUBLE, "8 байта(для плавающей точки)");
    }

    /**
     * Converts from integer value into enum.
     * @param value integer value of enum.
     * @return enum by integer.
     */
    public static MetricElementSize fromValue(int value) {
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
    public static Map<MetricElementSize, String> getNames() {
        return _names;
    }

    /**
     * Get name of enum
     * @param metricElementSize enum
     * @return name
     */
    public static String getName(MetricElementSize metricElementSize) {
        return _names.get(metricElementSize);
    }
}
