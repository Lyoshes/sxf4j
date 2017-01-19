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
 * Object frame out metrics.
 * @author Serge Silaev aka iSergio <s.serge.b@gmail.com>
 */
public enum FrameOut {
    NO_FRAME_OUT(0),
    NORTH(8),
    EAST(4),
    SOUTH(2),
    WEST(1);
    /**
     * Map contains all enums names. Its will be used in ComboBox's and etc.
     */
    private static final Map<FrameOut, String> _names = new HashMap<>();
    static {
        _names.put(NO_FRAME_OUT, "Нет выходов нa paмку");
        _names.put(NORTH, "Северная рамка");
        _names.put(EAST, "Восточная рамка");
        _names.put(SOUTH, "Южная рамка");
        _names.put(WEST, "Западная рамка");
    }
    /**
     * Map contains integer (value) and enum object.
     */
    private static final Map<Integer, FrameOut> _intToEnumMap = new HashMap<>();
    static {
        for (FrameOut frameOut : values()) {
            _intToEnumMap.put(frameOut.getValue(), frameOut);
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
    FrameOut(int value) {
        _value = value;
    }

    /**
     * Converts from integer value into enum.
     * @param value integer value of enum.
     * @return enum by integer.
     */
    public static FrameOut fromValue(int value) {
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
    public static Map<FrameOut, String> getNames() {
        return _names;
    }

    /**
     * Get name of enum
     * @param frameOut enum
     * @return name
     */
    public static String getName(FrameOut frameOut) {
        return _names.get(frameOut);
    }
}
