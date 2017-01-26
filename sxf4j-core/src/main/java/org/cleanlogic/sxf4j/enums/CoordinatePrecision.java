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
 * @author Serge Silaev aka iSergio <s.serge.b@gmail.com>
 */
public enum CoordinatePrecision {
    UNDEFINED(0),
    MAXIMUM(1),
    CENTIMETRE(2),
    MILLIMETRE(3);

    /**
     * Map contains all enums names. Its will be used in ComboBox's and etc.
     */
    private static final Map<CoordinatePrecision, String> _names = new HashMap<>();
    static {
        _names.put(UNDEFINED, "Не установлено");
        _names.put(MAXIMUM, "Повышенная точность хранения координат (метры, радианы или градусы)");
        _names.put(CENTIMETRE, "Координаты записаны с точностью до сантиметра (метры, 2 знака после запятой)");
        _names.put(MILLIMETRE, "Координаты записаны с точностью до миллиметра (метры, 3 знака после запятой)");
    }
    /**
     * Map contains integer (value) and enum object.
     */
    private static final Map<Integer, CoordinatePrecision> _intToEnumMap = new HashMap<>();
    static {
        for (CoordinatePrecision coordinatePrecision : values()) {
            _intToEnumMap.put(coordinatePrecision.getValue(), coordinatePrecision);
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
    CoordinatePrecision(int value) {
        _value = value;
    }

    /**
     * Converts from integer value into enum.
     * @param value integer value of enum.
     * @return enum by integer.
     */
    public static CoordinatePrecision fromValue(int value) {
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
    public static Map<CoordinatePrecision, String> getNames() {
        return _names;
    }

    /**
     * Get name of enum
     * @param coordinatePrecision enum
     * @return name
     */
    public static String getName(CoordinatePrecision coordinatePrecision) {
        return _names.get(coordinatePrecision);
    }
}
