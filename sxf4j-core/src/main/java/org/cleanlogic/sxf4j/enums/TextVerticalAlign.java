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

    /**
     * Map contains all enums names. Its will be used in ComboBox's and etc.
     */
    private static final Map<TextVerticalAlign, String> _names = new HashMap<>();
    static {
        _names.put(TOP, String.valueOf(TOP));
        _names.put(BOTTOM, String.valueOf(BOTTOM));
        _names.put(BASELINE, String.valueOf(BASELINE));
        _names.put(MIDDLE, String.valueOf(MIDDLE));
    }
    /**
     * Map contains integer (value) and enum object.
     */
    private static final Map<Integer, TextVerticalAlign> _intToEnumMap = new HashMap<>();
    static {
        for (TextVerticalAlign textVerticalAlign : values()) {
            _intToEnumMap.put(textVerticalAlign.getValue(), textVerticalAlign);
        }
    }

    static final TextVerticalAlign[] TableTextVerticalAlign = new TextVerticalAlign[] {
            BASELINE, BASELINE, BASELINE,
            MIDDLE,   MIDDLE,   MIDDLE,
            TOP,      TOP,      TOP,
            BOTTOM,   BOTTOM,   BOTTOM
    };

    /**
     * Current value of enum.
     */
    private final int _value;

    /**
     * Default constructor.
     * @param value value of enum.
     */
    TextVerticalAlign(int value) {
        _value = value;
    }

    /**
     * Converts from integer value into enum.
     * @param value integer value of enum.
     * @return enum by integer.
     */
    public static TextVerticalAlign fromValue(int value) {
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
    public static Map<TextVerticalAlign, String> getNames() {
        return _names;
    }

    /**
     * Get name of enum
     * @param textVerticalAlign enum
     * @return name
     */
    public static String getName(TextVerticalAlign textVerticalAlign) {
        return _names.get(textVerticalAlign);
    }
}
