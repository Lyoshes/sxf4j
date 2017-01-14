package org.cleanlogic.sxf4j.enums;

import java.util.HashMap;
import java.util.Map;

/**
 * Supported units by SXF format
 * @author Serge Silaev aka iSergio <s.serge.b@gmail.com>
 */
public enum Unit {
    /**
     * Не установлено
     */
    UNDEFINED(-1),
    /**
     * Километры
     */
    KILOMETRE(5),
    /**
     * 0.5 м
     */
    METRE05(4),
    /**
     * Метры
     */
    METRE(0),
    /**
     * Дециметры
     */
    DECIMETRE(1),
    /**
     * Сантиметры
     */
    CENTIMETRE(2),
    /**
     * Миллиметры
     */
    MILLIMETRE(3),
    /**
     * 0.1 сек
     */
    SECOND01(66),
    /**
     * Радианы
     */
    RADIAN(64),
    /**
     * 10e-8 рад
     */
    RADIAN8(67),
    /**
     * Градусы
     */
    DEGREE(65),
    /**
     * Футы
     */
    FOOT(16);

    private static final Map<Unit, String> _names = new HashMap<>();
    static {
        _names.put(UNDEFINED, "Не установлено");
        _names.put(KILOMETRE, "Километры");
        _names.put(METRE05, "0.5 м");
        _names.put(METRE, "Метры");
        _names.put(DECIMETRE, "Дециметры");
        _names.put(CENTIMETRE, "Сантиметры");
        _names.put(MILLIMETRE, "Миллиметры");
        _names.put(SECOND01, "0.1 сек");
        _names.put(RADIAN, "Радианы");
        _names.put(RADIAN8, "10e-8 рад");
        _names.put(DEGREE, "Градусы");
        _names.put(FOOT, "Футы");
    }

    private static final Map<Integer, Unit> _intToEnumMap = new HashMap<>();
    static {
        for (Unit unit : values()) {
            _intToEnumMap.put(unit.getValue(), unit);
        }
    }

    private final int _value;

    Unit(int value) {
        _value = value;
    }

    public static Unit fromValue(int value) {
        return _intToEnumMap.get(value);
    }

    public int getValue() {
        return _value;
    }

    public String getName() {
        return _names.get(this);
    }

    public static Map<Unit, String> getNames() {
        return _names;
    }

    public static String getName(Unit unit) {
        return _names.get(unit);
    }
}
