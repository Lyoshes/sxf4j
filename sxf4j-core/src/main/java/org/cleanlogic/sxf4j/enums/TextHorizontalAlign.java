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

    /**
     * Map contains all enums names. Its will be used in ComboBox's and etc.
     */
    private static final Map<TextHorizontalAlign, String> _names = new HashMap<>();
    static {
        _names.put(LEFT, String.valueOf(LEFT));
        _names.put(RIGHT, String.valueOf(RIGHT));
        _names.put(CENTER, String.valueOf(CENTER));
    }
    /**
     * Map contains integer (value) and enum object.
     */
    private static final Map<Integer, TextHorizontalAlign> _intToEnumMap = new HashMap<>();
    static {
        for (TextHorizontalAlign textHorizontalAlign : values()) {
            _intToEnumMap.put(textHorizontalAlign.getValue(), textHorizontalAlign);
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
    TextHorizontalAlign(int value) {
        _value = value;
    }

    /**
     * Converts from integer value into enum.
     * @param value integer value of enum.
     * @return enum by integer.
     */
    public static TextHorizontalAlign fromValue(int value) {
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
    public static Map<TextHorizontalAlign, String> getNames() {
        return _names;
    }

    /**
     * Get name of enum
     * @param textHorizontalAlign enum
     * @return name
     */
    public static String getName(TextHorizontalAlign textHorizontalAlign) {
        return _names.get(textHorizontalAlign);
    }
}
