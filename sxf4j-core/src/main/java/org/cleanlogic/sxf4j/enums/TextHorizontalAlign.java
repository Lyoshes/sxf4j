package org.cleanlogic.sxf4j.enums;

import java.util.HashMap;
import java.util.Map;

/**
 * Enum of horizontal align in windows constants
 * @author Serge Silaev aka iSergio <s.serge.b@gmail.com>
 */
public enum TextHorizontalAlign {
    /**
     * Слева
     */
    LEFT(0),
    /**
     * Справа
     */
    RIGHT(2),
    /**
     * По центру
     */
    CENTER(6);

    private static final Map<TextHorizontalAlign, String> _names = new HashMap<>();
    static {
        _names.put(LEFT, String.valueOf(LEFT));
        _names.put(RIGHT, String.valueOf(RIGHT));
        _names.put(CENTER, String.valueOf(CENTER));
    }

    private static final Map<Integer, TextHorizontalAlign> _intToEnumMap = new HashMap<>();
    static {
        for (TextHorizontalAlign textHorizontalAlign : values()) {
            _intToEnumMap.put(textHorizontalAlign.getValue(), textHorizontalAlign);
        }
    }

    private final int _value;

    TextHorizontalAlign(int value) {
        _value = value;
    }

    public static TextHorizontalAlign fromValue(int value) {
        return _intToEnumMap.get(value);
    }

    public int getValue() {
        return _value;
    }

    public String getName() {
        return _names.get(this);
    }

    public static Map<TextHorizontalAlign, String> getNames() {
        return _names;
    }

    public static String getName(TextHorizontalAlign textHorizontalAlign) {
        return _names.get(textHorizontalAlign);
    }
}
