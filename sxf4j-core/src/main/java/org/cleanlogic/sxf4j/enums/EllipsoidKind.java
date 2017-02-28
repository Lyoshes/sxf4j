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
 * Supported ellipsoids by SXF format
 * @author Serge Silaev aka iSergio <s.serge.b@gmail.com>
 */
public enum EllipsoidKind {
    /**
     * Не установлено
     */
    UNDEFINED(-1),
    /**
     * Красовского 1942 г. (большая полуось - 6378245 м, сжатие - 1: 298.3)
     */
    KRASOVSKY42(1),
    /**
     * Международный 1972 г. (WGS-72) (6378135 м, 1: 298.26)
     */
    WGS_76(2),
    /**
     * Хейфорда 1909 г. (6378388 м, 1: 297.0)
     */
    HEFORD(3),
    /**
     * Кларка 1880 г. (6378249 м, 1: 293.5)
     */
    CLARKE_80(4),
    /**
     * Кларка 1866 г. (6378206 м, 1: 295.0)
     */
    CLARKE_66(5),
    /**
     * Эвереста 1857 г. (6377276 м, 1: 300.0)
     */
    EVEREST_57(6),
    /**
     * Бесселя 1841г. (6377397 м, 1: 299.2)
     */
    BESSEL(7),
    /**
     * Эри 1830 г. (6377491 м, 1: 299.3)
     */
    AIRY(8),
    /**
     * Международный 1984 г. (WGS-84) (6378137 м, 1: 298.257)
     */
    WGS_84(9),
    /**
     * Параметры Земли 90.02 (SGS-85) (6378136 м, 1: 298.257839)
     */
    SGS_85(10),
    /**
     * GRS-80 (6378137 м, 1: 298.257222101)
     */
    GRS_80(11),
    /**
     * IERS 1996г. (6378136.49 298.25645)
     */
    IERS_96(12),
    /**
     * Международный 1924г. (6378388.0 297.00)
     */
    WGS_24(13),
    /**
     * Южно-Американский 1969г. (6378155.0 298.3)
     */
    SOUTHAMERICAN(14),
    /**
     * Индонезийский 1974г. (6378160.0 298.25)
     */
    INDONESIAN(15),
    /**
     * Гельмерта 1906г. (6378160.0 298.247)
     */
    GELMERT(16),
    /**
     * Фишера 1960г. Модифицированный (6378200.0 298.3)
     */
    FISHER_60(17),
    /**
     * Фишера 1968г. (6378150.0 298.3)
     */
    FISHER_68(18),
    /**
     * Хафа 1960г. (6378270.0 297.0)
     */
    HAFA(19),
    /**
     * Эвереста 1830г. (6377276.345 300.8017)
     */
    EVEREST_30(20),
    /**
     * Австралийский национальный 1000 – Произвольный эллипсоид (6378160.0 298.25)
     */
    NATIONAL_AUSTRALIAN(21),
    /**
     * CGCS2000
     */
    CGCS2000(22),
    /**
     * Airy Modified
     */
    AIRYM(23),
    /**
     * Bessel Modified
     */
    BESSELM(24),
    /**
     * Bessel Namibia
     */
    BESSELNAMIBIA(25),
    /**
     * Bessel Namibia (GLM)
     */
    BESSELGLM(26),
    /**
     * Clarke 1880 (Arc)
     */
    CLARKE1880ARC(27),
    /**
     * Clarke 1880 (SGA 1922)
     */
    CLARKE1880SGA22(28),
    /**
     * Everest 1830 (1967 Definition)
     */
    EVEREST1830D67(29),
    /**
     * Everest 1830 Modified
     */
    EVEREST1830M(30),
    /**
     * Everest 1830 (RSO 1969)
     */
    EVEREST1830RSO69(31),
    /**
     * Everest 1830 (1975 Definition)
     */
    EVEREST1830D75(32),
    /**
     * NWL 9D
     */
    NWL9D(33),
    /**
     * Plessis 1817
     */
    PLESSIS1817(34),
    /**
     * Struve 1860
     */
    STRUVE1860(35),
    /**
     * War Office
     */
    WAROFFICE(36),
    /**
     * GEM 10C
     */
    GEM10C(37),
    /**
     * OSU86F
     */
    OSU86F(38),
    /**
     * OSU91A
     */
    OSU91A(39),
    /**
     * GRS 1967
     */
    GRS_67(40),
    /**
     * Average Terrestrial System 1977
     */
    ATS_77(41),
    /**
     * IAG 1975
     */
    IAG_75(42),
    /**
     * GRS 1967 Modified
     */
    GRS_67M(43),
    /**
     * Danish 1876
     */
    DANISH1876(44),
    /**
     * Шар с радиусом, равным большой полуоси WGS-84
     */
    SPHERE_WGS_84(45),
    /**
     * ГСК-2011
     */
    ELLIPSOID_GSK(46),
    /**
     * ПЗ 90.11
     */
    ELLIPSOID_EC9011(47),
//    /**
//     * Количество эллипсойдов в структуре
//     */
//    ELLIPSOIDCOUNT(47),
    /**
     * Произвольный (пользовательский) эллипсоид
     */
    USERELLIPSOID(1000);

    //KRASOVSKY42_95 = 1001,  // Красовского 1940г. СК-95
    //EC90TEMP       = 1002  // ПЗ-90

    /**
     * Map contains all enums names. Its will be used in ComboBox's and etc.
     */
    private static final Map<EllipsoidKind, String> _names = new HashMap<>();
    static {
        _names.put(UNDEFINED, "Не установлено");
        _names.put(KRASOVSKY42, "Красовского 1942 г. (большая полуось - 6378245 м, сжатие - 1: 298.3)");
        _names.put(WGS_76, "Международный 1972 г. (WGS-72) (6378135 м, 1: 298.26)");
        _names.put(HEFORD, "Хейфорда 1909 г. (6378388 м, 1: 297.0)");
        _names.put(CLARKE_80, "Кларка 1880 г. (6378249 м, 1: 293.5)");
        _names.put(CLARKE_66, "Кларка 1866 г. (6378206 м, 1: 295.0)");
        _names.put(EVEREST_57, "Эвереста 1857 г. (6377276 м, 1: 300.0)");
        _names.put(BESSEL, "Бесселя 1841г. (6377397 м, 1: 299.2)");
        _names.put(AIRY, "Эри 1830 г. (6377491 м, 1: 299.3)");
        _names.put(WGS_84, "Международный 1984 г. (WGS-84) (6378137 м, 1: 298.257)");
        _names.put(SGS_85, "Параметры Земли 90.02 (SGS-85) (6378136 м, 1: 298.257839)");
        _names.put(GRS_80, "GRS-80 (6378137 м, 1: 298.257222101)");
        _names.put(IERS_96, "IERS 1996г. (6378136.49 298.25645)");
        _names.put(WGS_24, "Международный 1924г. (6378388.0 297.00)");
        _names.put(SOUTHAMERICAN, "Южно-Американский 1969г. (6378155.0 298.3)");
        _names.put(INDONESIAN, "Индонезийский 1974г. (6378160.0 298.25)");
        _names.put(GELMERT, "Гельмерта 1906г. (6378160.0 298.247)");
        _names.put(FISHER_60, "Фишера 1960г. Модифицированный (6378200.0 298.3)");
        _names.put(FISHER_68, "Фишера 1968г. (6378150.0 298.3)");
        _names.put(HAFA, "Хафа 1960г. (6378270.0 297.0)");
        _names.put(EVEREST_30, "Эвереста 1830г. (6377276.345 300.8017)");
        _names.put(NATIONAL_AUSTRALIAN, "Австралийский национальный 1000 – Произвольный эллипсоид (6378160.0 298.25)");
        _names.put(CGCS2000, "CGCS2000");
        _names.put(AIRYM, "Airy Modified");
        _names.put(BESSELM, "Bessel Modified");
        _names.put(BESSELNAMIBIA, "Bessel Namibia");
        _names.put(BESSELGLM, "Bessel Namibia (GLM)");
        _names.put(CLARKE1880ARC, "Clarke 1880 (Arc)");
        _names.put(CLARKE1880SGA22, "Clarke 1880 (SGA 1922)");
        _names.put(EVEREST1830D67, "Everest 1830 (1967 Definition)");
        _names.put(EVEREST1830M, "Everest 1830 Modified");
        _names.put(EVEREST1830RSO69, "Everest 1830 (RSO 1969)");
        _names.put(EVEREST1830D75, "Everest 1830 (1975 Definition)");
        _names.put(NWL9D, "NWL 9D");
        _names.put(PLESSIS1817, "Plessis 1817");
        _names.put(STRUVE1860, "Struve 1860");
        _names.put(WAROFFICE, "War Office");
        _names.put(GEM10C, "GEM 10C");
        _names.put(OSU86F, "OSU86F");
        _names.put(OSU91A, "OSU91A");
        _names.put(GRS_67, "GRS 1967");
        _names.put(ATS_77, "Average Terrestrial System 1977");
        _names.put(IAG_75, "IAG 1975");
        _names.put(GRS_67M, "GRS 1967 Modified");
        _names.put(DANISH1876, "Danish 1876");
        _names.put(SPHERE_WGS_84, "Шар с радиусом, равным большой полуоси WGS-84");
        _names.put(ELLIPSOID_GSK, "ГСК-2011");
        _names.put(ELLIPSOID_EC9011, "ПЗ 90.11");
//        _names.put(ELLIPSOIDCOUNT, "Количество эллипсойдов в структуре");
        _names.put(USERELLIPSOID, "Произвольный (пользовательский) эллипсоид");
    }

    /**
     * Map contains integer (value) and enum object.
     */
    private static final Map<Integer, EllipsoidKind> _intToEnumMap = new HashMap<>();
    static {
        for (EllipsoidKind ellipsoidKind : values()) {
            _intToEnumMap.put(ellipsoidKind.getValue(), ellipsoidKind);
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
    EllipsoidKind(int value) {
        _value = value;
    }

    /**
     * Converts from integer value into enum.
     * @param value integer value of enum.
     * @return enum by integer.
     */
    public static EllipsoidKind fromValue(int value) {
        if (_intToEnumMap.get(value) == null) {
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
    public static Map<EllipsoidKind, String> getNames() {
        return _names;
    }

    /**
     * Get name of enum
     * @param ellipsoidKind enum
     * @return name
     */
    public static String getName(EllipsoidKind ellipsoidKind) {
        return _names.get(ellipsoidKind);
    }
}
