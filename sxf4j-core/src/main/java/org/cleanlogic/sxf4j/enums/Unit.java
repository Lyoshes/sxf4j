/*
 * Copyright 2017 iserge.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

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

    /**
     * Map contains all enums names. Its will be used in ComboBox's and etc.
     */
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
    /**
     * Map contains integer (value) and enum object.
     */
    private static final Map<Integer, Unit> _intToEnumMap = new HashMap<>();
    static {
        for (Unit unit : values()) {
            _intToEnumMap.put(unit.getValue(), unit);
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
    Unit(int value) {
        _value = value;
    }

    /**
     * Converts from integer value into enum.
     * @param value integer value of enum.
     * @return enum by integer.
     */
    public static Unit fromValue(int value) {
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
    public static Map<Unit, String> getNames() {
        return _names;
    }

    /**
     * Get name of enum
     * @param unit enum
     * @return name
     */
    public static String getName(Unit unit) {
        return _names.get(unit);
    }
}
