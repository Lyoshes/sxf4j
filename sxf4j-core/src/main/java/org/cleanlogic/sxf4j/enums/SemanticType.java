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
 * Supported semantic type by SXF format
 * @author Serge Silaev aka iSergio <s.serge.b@gmail.com>
 */
public enum SemanticType {
    /**
     * CТРОКА СИМВОЛОВ (ASCIIZ),ОГРАНИЧЕННАЯ НУЛЕМ
     * ASCIIZ string with \0 in end
     */
    STRDOS(0),
    /**
     * CТРОКА СИМВОЛОВ (ANSI),ОГРАНИЧЕННАЯ НУЛЕМ
     * ANSI string with \0 in end
     */
    STRING(126),
    /**
     * CТРОКА СИМВОЛОВ (UNICODE),ОГРАНИЧЕННАЯ НУЛЕМ
     * Unicode string with \0 in end
     */
    STRUNI(127),
    /**
     * Строка переменной длинны в UTF-16
     */
    STRUTF16(128),
    /**
     * 1 БАЙТ SIGNED CHAR
     */
    CHAR (1),
    /**
     * 2 БАЙТA SIGNED SHORT
     */
    SHORT(2),
    /**
     * 4 БАЙТA SIGNED INT
     */
    LONG(4),
    /**
     * 8 БАЙТ DOUBLE
     */
    DOUBLE(8);

    /**
     * Map contains all enums names. Its will be used in ComboBox's and etc.
     */
    private static final Map<SemanticType, String> _names = new HashMap<>();
    static {
        _names.put(STRDOS, "CТРОКА СИМВОЛОВ (ASCIIZ),ОГРАНИЧЕННАЯ НУЛЕМ");
        _names.put(STRING, "CТРОКА СИМВОЛОВ (ANSI),ОГРАНИЧЕННАЯ НУЛЕМ");
        _names.put(STRUNI, "CТРОКА СИМВОЛОВ (UNICODE),ОГРАНИЧЕННАЯ НУЛЕМ");
        _names.put(STRUTF16, "СТРОКА ПЕРЕМЕННОЙ ДЛИННЫ В UTF-16");
        _names.put(CHAR, "2 БАЙТA SIGNED SHORT");
        _names.put(SHORT, "2 БАЙТA SIGNED SHORT");
        _names.put(LONG, "4 БАЙТA SIGNED INT");
        _names.put(DOUBLE, "8 БАЙТ DOUBLE");
    }
    /**
     * Map contains integer (value) and enum object.
     */
    private static final Map<Integer, SemanticType> _intToEnumMap = new HashMap<>();
    static {
        for (SemanticType semanticType : values()) {
            _intToEnumMap.put(semanticType.getValue(), semanticType);
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
    SemanticType(int value) {
        _value = value;
    }

    /**
     * Converts from integer value into enum.
     * @param value integer value of enum.
     * @return enum by integer.
     */
    public static SemanticType fromValue(int value) {
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
    public static Map<SemanticType, String> getNames() {
        return _names;
    }

    /**
     * Get name of enum
     * @param semanticType enum
     * @return name
     */
    public static String getName(SemanticType semanticType) {
        return _names.get(semanticType);
    }
}
