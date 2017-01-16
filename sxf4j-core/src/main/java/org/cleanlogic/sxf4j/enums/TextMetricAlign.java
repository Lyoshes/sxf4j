package org.cleanlogic.sxf4j.enums;

import java.util.HashMap;
import java.util.Map;

/**
 * Enum of text align in SXF format constants
 * @author Serge Silaev aka iSergio <s.serge.b@gmail.com>
 */
public enum TextMetricAlign {
    BASELINE_LEFT(20),
    BASELINE_RIGHT(21),
    BASELINE_CENTER(22),

    MIDDLE_LEFT(23),
    MIDDLE_RIGHT(24),
    MIDDLE_CENTER(25),

    TOP_LEFT(26),
    TOP_RIGHT(27),
    TOP_CENTER(28),

    BOTTOM_LEFT(29),
    BOTTOM_RIGHT(30),
    BOTTOM_CENTER(31);

    /**
     * Map contains all enums names. Its will be used in ComboBox's and etc.
     */
    private static final Map<TextMetricAlign, String> _names = new HashMap<>();
    static {
        _names.put(BASELINE_LEFT, String.valueOf(BASELINE_LEFT));
        _names.put(BASELINE_RIGHT, String.valueOf(BASELINE_RIGHT));
        _names.put(BASELINE_CENTER, String.valueOf(BASELINE_CENTER));

        _names.put(MIDDLE_LEFT, String.valueOf(MIDDLE_LEFT));
        _names.put(MIDDLE_RIGHT, String.valueOf(MIDDLE_RIGHT));
        _names.put(MIDDLE_CENTER, String.valueOf(MIDDLE_CENTER));

        _names.put(TOP_LEFT, String.valueOf(TOP_LEFT));
        _names.put(TOP_RIGHT, String.valueOf(TOP_RIGHT));
        _names.put(TOP_CENTER, String.valueOf(TOP_CENTER));

        _names.put(BOTTOM_LEFT, String.valueOf(BOTTOM_LEFT));
        _names.put(BOTTOM_RIGHT, String.valueOf(BOTTOM_RIGHT));
        _names.put(BOTTOM_CENTER, String.valueOf(BOTTOM_CENTER));
    }
    /**
     * Map contains integer (value) and enum object.
     */
    private static final Map<Integer, TextMetricAlign> _intToEnumMap = new HashMap<>();
    static {
        for (TextMetricAlign textMetricAlign : values()) {
            _intToEnumMap.put(textMetricAlign.getValue(), textMetricAlign);
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
    TextMetricAlign(int value) {
        _value = value;
    }

    /**
     * Converts from integer value into enum.
     * @param value integer value of enum.
     * @return enum by integer.
     */
    public static TextMetricAlign fromValue(int value) {
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
    public static Map<TextMetricAlign, String> getNames() {
        return _names;
    }

    /**
     * Get name of enum
     * @param textMetricAlign enum
     * @return name
     */
    public static String getName(TextMetricAlign textMetricAlign) {
        return _names.get(textMetricAlign);
    }
}