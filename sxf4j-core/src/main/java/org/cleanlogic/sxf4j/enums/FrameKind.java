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
    /**
     * Map contains all enums names. Its will be used in ComboBox's and etc.
     */
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
    /**
     * Map contains integer (value) and enum object.
     */
    private static final Map<Integer, FrameKind> _intToEnumMap = new HashMap<>();
    static {
        for (FrameKind frameKind : values()) {
            _intToEnumMap.put(frameKind.getValue(), frameKind);
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
    FrameKind(int value) {
        _value = value;
    }

    /**
     * Converts from integer value into enum.
     * @param value integer value of enum.
     * @return enum by integer.
     */
    public static FrameKind fromValue(int value) {
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
    public static Map<FrameKind, String> getNames() {
        return _names;
    }

    /**
     * Get name of enum
     * @param frameKind enum
     * @return name
     */
    public static String getName(FrameKind frameKind) {
        return _names.get(frameKind);
    }
}
