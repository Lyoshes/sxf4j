package org.cleanlogic.sxf4j.enums;

import java.util.HashMap;
import java.util.Map;

/**
 * Enum of vertical align in windows constants
 * @author Serge Silaev aka iSergio <s.serge.b@gmail.com>
 */
public enum TextVerticalAlign {
    /**
     * Сверху
     */
    TOP(0),
    /**
     * Снизу
     */
    BOTTOM(8),
    /**
     * По базовой линии
     */
    BASELINE(24),
    /**
     * По средней линии
     */
    MIDDLE(4120);

    private static final Map<TextVerticalAlign, String> _names = new HashMap<>();
    static {
        _names.put(TOP, String.valueOf(TOP));
        _names.put(BOTTOM, String.valueOf(BOTTOM));
        _names.put(BASELINE, String.valueOf(BASELINE));
        _names.put(MIDDLE, String.valueOf(MIDDLE));
    }

    private static final Map<Integer, TextVerticalAlign> _intToEnumMap = new HashMap<>();
    static {
        for (TextVerticalAlign textVerticalAlign : values()) {
            _intToEnumMap.put(textVerticalAlign.getValue(), textVerticalAlign);
        }
    }

    private final int _value;

    TextVerticalAlign(int value) {
        _value = value;
    }

    public static TextVerticalAlign fromValue(int value) {
        return _intToEnumMap.get(value);
    }

    public int getValue() {
        return _value;
    }

    public String getName() {
        return _names.get(this);
    }

    public static Map<TextVerticalAlign, String> getNames() {
        return _names;
    }

    public static String getName(TextVerticalAlign textVerticalAlign) {
        return _names.get(textVerticalAlign);
    }
}
