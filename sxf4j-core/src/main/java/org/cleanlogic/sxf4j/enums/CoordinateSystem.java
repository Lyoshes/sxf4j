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
 * Supported coordinate systems by SXF format
 * @author Serge Silaev aka iSergio <s.serge.b@gmail.com>
 */
public enum CoordinateSystem {
    /**
     * Не установлено
     */
    UNDEFINED(-1),
    /**
     * Система координат 42 года (плоская прямоугольная)
     */
    ORTHOGONAL(1),
    /**
     * Система Универсальной Проекции Меркатора (США - Universal Transverse Mercator)
     */
    UNIVERSALMERCATOR(2),
    /**
     * Национальная прямоугольная сетка Великобритании (National Grid)
     */
    NATIONALGRID(3),
    /**
     * Прямоугольная местная система координат (крупномасштабные планы)
     */
    AREAORTHOGONAL(4),
    /**
     * Система координат 63 года
     */
    SYSTEM_63(5),
    /**
     * Прямоугольная условная для обзорных карт, зависит от типа проекции, значений главных параллелей и осевого меридиана
     */
    CONDITION(6),
    /**
     * Геодезические координаты в соответствии с видом эллипсоида в радианах
     */
    GEOCOORDINATE(7),
    /**
     * Геодезические координаты в соответствии с видом эллипсоида в градусах
     */
    GEOCOORDINATEGRADUS(8),
    /**
     * Система координат 95 года (плоская прямоугольная)
     */
    SYSTEM_95(9),
    /**
     * Система координат ГСК-2011
     */
    GSK(10);

    /**
     * Map contains all enums names. Its will be used in ComboBox's and etc.
     */
    private static final Map<CoordinateSystem, String> _names = new HashMap<>();
    static {
        _names.put(UNDEFINED, "Не установлено");
        _names.put(ORTHOGONAL, "Система координат 42 года (плоская прямоугольная)");
        _names.put(UNIVERSALMERCATOR, "Система Универсальной Проекции Меркатора (США - Universal Transverse Mercator)");
        _names.put(NATIONALGRID, "Национальная прямоугольная сетка Великобритании (National Grid)");
        _names.put(AREAORTHOGONAL, "Прямоугольная местная система координат (крупномасштабные планы)");
        _names.put(SYSTEM_63, "Система координат 63 года");
        _names.put(CONDITION, "Прямоугольная условная для обзорных карт, зависит от типа проекции, значений главных параллелей и осевого меридиана");
        _names.put(GEOCOORDINATE, "Геодезические координаты в соответствии с видом эллипсоида в радианах");
        _names.put(GEOCOORDINATEGRADUS, "Геодезические координаты в соответствии с видом эллипсоида в градусах");
        _names.put(SYSTEM_95, "Система координат 95 года (плоская прямоугольная)");
        _names.put(GSK, "Система координат ГСК-2011");
    }
    /**
     * Map contains integer (value) and enum object.
     */
    private static final Map<Integer, CoordinateSystem> _intToEnumMap = new HashMap<>();
    static {
        for (CoordinateSystem coordinateSystem : values()) {
            _intToEnumMap.put(coordinateSystem.getValue(), coordinateSystem);
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
    CoordinateSystem(int value) {
        _value = value;
    }

    /**
     * Converts from integer value into enum.
     * @param value integer value of enum.
     * @return enum by integer.
     */
    public static CoordinateSystem fromValue(int value) {
        if (value == 0 || value == 255) {
            return UNDEFINED;
        } else if (_intToEnumMap.get(value) == null) {
            return UNDEFINED;
        }
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
    public static Map<CoordinateSystem, String> getNames() {
        return _names;
    }

    /**
     * Get name of enum
     * @param coordinateSystem enum
     * @return name
     */
    public static String getName(CoordinateSystem coordinateSystem) {
        return _names.get(coordinateSystem);
    }
}
