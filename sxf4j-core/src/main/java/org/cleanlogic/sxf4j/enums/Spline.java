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
 * Supported metric spline by SXF format
 * @author Serge Silaev aka iSergio <s.serge.b@gmail.com>
 */
public enum Spline {
    NO_SPLINE,
    SMOOTHING,
    ENVELOPING;

    /**
     * Map contains all enums names. Its will be used in ComboBox's and etc.
     */
    private final static Map<Spline, String> _names = new HashMap<>();
    static {
        _names.put(NO_SPLINE, "Построение сплайна при визуализации не выполняется");
        _names.put(SMOOTHING, "Сглаживающий сплайн (срезание углов)");
        _names.put(ENVELOPING, "Огибающий сплайн (проходит через все точки метрики)");
    }

    /**
     * Converts from integer value into enum.
     * @param value integer value of enum.
     * @return enum by integer.
     */
    public static Spline fromValue(int value) {
        return values()[value];
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
    public static Map<Spline, String> getNames() {
        return _names;
    }

    /**
     * Get name of enum
     * @param spline enum
     * @return name
     */
    public static String getName(Spline spline) {
        return _names.get(spline);
    }
}
