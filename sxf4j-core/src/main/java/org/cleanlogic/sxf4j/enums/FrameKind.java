package org.cleanlogic.sxf4j.enums;

import java.util.HashMap;
import java.util.Map;

/**
 * Supported frames by SXF format
 * @author Serge Silaev aka iSergio <s.serge.b@gmail.com>
 */
public enum FrameKind {
    /**
     * Не установлено
     */
    UNDEFINED(-1),
    /**
     * Карта не ограничена рамкой
     */
    NOFRAME(0),
    /**
     * Трапециевидная без точек излома
     */
    TRAPEZE(1),
    /**
     * Трапециевидная с точками излома
     */
    TRAPEZECURVE(2),
    /**
     * Прямоугольная
     */
    RECTANGULAR(3),
    /**
     * Круговая
     */
    CIRCLE(4),
    /**
     * Произвольная
     */
    FREE(5);

    private static final Map<FrameKind, String> _names = new HashMap<>();
    static {
        _names.put(UNDEFINED, "Не установлено");
        _names.put(NOFRAME, "Карта не ограничена рамкой");
        _names.put(TRAPEZE, "Трапециевидная без точек излома");
        _names.put(TRAPEZECURVE, "Трапециевидная с точками излома");
        _names.put(RECTANGULAR, "Прямоугольная");
        _names.put(CIRCLE, "Круговая");
        _names.put(FREE, "Произвольная");
    }
    private static final Map<Integer, FrameKind> _intToEnumMap = new HashMap<>();
    static {
        for (FrameKind frameKind : values()) {
            _intToEnumMap.put(frameKind.getValue(), frameKind);
        }
    }

    private final int _value;
    FrameKind(int value) {
        _value = value;
    }

    public static FrameKind fromValue(int value) {
        return _intToEnumMap.get(value);
    }

    public int getValue() {
        return _value;
    }

    public String getName() {
        return _names.get(this);
    }

    public static Map<FrameKind, String> getNames() {
        return _names;
    }

    public static String getName(FrameKind frameKind) {
        return _names.get(frameKind);
    }
}
