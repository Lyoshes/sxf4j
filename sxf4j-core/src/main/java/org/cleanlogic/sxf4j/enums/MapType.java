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
 * Supported map types by SXF format
 * @author Serge Silaev aka iSergio <s.serge.b@gmail.com>
 */
public enum MapType {
    /**
     * Не установлено
     */
    UNDEFINED(-1),
    /**
     * Топографическая
     */
    TOPOGRAPHIC(1),
    /**
     * Обзорно-географическая
     */
    GEOGRAPHIC(2),
    /**
     * Космонавигационная (ГЛОБУС)
     */
    GLOBE(3),
    /**
     * Топографический план города
     */
    CITYPLAN(4),
    /**
     * Крупномасштабный план местности
     */
    LARGESCALE(5),
    /**
     * Аэронавигационная
     */
    AERONAUTIC(6),
    /**
     * Морская навигационная
     */
    SEANAUTICOLD(7),
    /**
     * Авиационная
     */
    AVIATION(8),
    /**
     * Бланковка
     */
    BLANK(9),
    /**
     * Универсальная топографическая Меркатора North American Datum 1927
     */
    UTMNAD27(10),
    /**
     * Универсальная топографическая Меркатора на WGS 84 (UTM)
     */
    UTMWGS84(11),
    /**
     * Универсальная топографическая Меркатора на своем эллипсоиде
     */
    UTMTYPE(12),
    /**
     * Топографическая система координат 63 года (СК 63)
     */
    CK_63(13),
    /**
     * Топографическая система координат 95 года (СК 95)
     */
    CK_95(14),
    /**
     * Топографическая местная (с произвольной главной точкой)
     */
    FLOAT_POINT(15),
    /**
     * Обзорно-географическая Широта/Долгота
     */
    LATLONG(16),
    /**
     * Карта Мира (Цилиндрическая Миллера), можно выбрать эллипсоид
     */
    WORLDMAP(17),
    /**
     * Местная система координат на базе СК-63
     */
    MCK_CK63(18),
    /**
     * Цилиндрическая Меркатора (EPSG:3395, EPSG:3857)
     */
    MERCATOR(19),
    /**
     * Морская навигационная (Mercator_2SP), можно выбрать эллипсоид (Цилиндрическая равноугольная Меркатора на эллипсоиде WGS84)
     */
    SEANAUTIC(20),
    /**
     * Карта в системе координат ГСК-2011
     */
    GSKMAP(21),
    /**
     * Крайнее значение типа карты
     */
    MAPTYPELIMIT(21);

    /**
     * Map contains all enums names. Its will be used in ComboBox's and etc.
     */
    private static final Map<MapType, String> _names = new HashMap<>();
    static {
        _names.put(UNDEFINED, "Не установлено");
        _names.put(TOPOGRAPHIC, "Топографическая");
        _names.put(GEOGRAPHIC, "Обзорно-географическая");
        _names.put(GLOBE, "Космонавигационная (ГЛОБУС)");
        _names.put(CITYPLAN, "Топографический план города");
        _names.put(LARGESCALE, "Крупномасштабный план местности");
        _names.put(AERONAUTIC, "Аэронавигационная");
        _names.put(SEANAUTICOLD, "Морская навигационная");
        _names.put(AVIATION, "Авиационная");
        _names.put(BLANK, "Бланковка");
        _names.put(UTMNAD27, "Универсальная топографическая Меркатора North American Datum 1927");
        _names.put(UTMWGS84, "Универсальная топографическая Меркатора на WGS 84 (UTM)");
        _names.put(UTMTYPE, "Универсальная топографическая Меркатора на своем эллипсоиде");
        _names.put(CK_63, "Топографическая система координат 63 года (СК 63)");
        _names.put(CK_95, "Топографическая система координат 95 года (СК 95)");
        _names.put(FLOAT_POINT, "Топографическая местная (с произвольной главной точкой)");
        _names.put(LATLONG, "Обзорно-географическая Широта/Долгота");
        _names.put(WORLDMAP, "Карта Мира (Цилиндрическая Миллера), можно выбрать эллипсоид");
        _names.put(MCK_CK63, "Местная система координат на базе СК-63");
        _names.put(MERCATOR, "Цилиндрическая Меркатора (EPSG:3395, EPSG:3857)");
        _names.put(SEANAUTIC, "Морская навигационная (Mercator_2SP), можно выбрать эллипсоид (Цилиндрическая равноугольная Меркатора на эллипсоиде WGS84)");
        _names.put(GSKMAP, "Карта в системе координат ГСК-2011");
        _names.put(MAPTYPELIMIT, "Крайнее значение типа карты");
    }
    /**
     * Map contains integer (value) and enum object.
     */
    private static final Map<Integer, MapType> _intToEnumMap = new HashMap<>();
    static {
        for (MapType mapType : values()) {
            _intToEnumMap.put(mapType.getValue(), mapType);
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
    MapType(int value) {
        _value = value;
    }

    /**
     * Converts from integer value into enum.
     * @param value integer value of enum.
     * @return enum by integer.
     */
    public static MapType fromValue(int value) {
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
    public static Map<MapType, String> getNames() {
        return _names;
    }

    /**
     * Get name of enum
     * @param mapType enum
     * @return name
     */
    public static String getName(MapType mapType) {
        return _names.get(mapType);
    }
}
