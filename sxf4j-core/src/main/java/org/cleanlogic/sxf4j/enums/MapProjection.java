package org.cleanlogic.sxf4j.enums;

import java.util.HashMap;
import java.util.Map;

/**
 * Supported map projections by SXF format
 * @author Serge Silaev aka iSergio s.serge.b@gmail.com>
 */
public enum MapProjection {
    /**
     * Не установлено
     */
    UNDEFINED(-1),
    /**
     * Равноугольная Гаусса-Крюгера
     */
    GAUSSCONFORMAL(1),
    /**
     * Коническая равноугольная
     */
    CONICALORTHOMORPHIC(2),
    /**
     * Цилиндрическая специальная
     */
    CYLINDRICALSPECIAL(3),
    /**
     * Азимутальная поперечная Ламберта
     */
    LAMBERT(4),
    /**
     * Стереографическая
     */
    STEREOGRAPHIC(5),
    /**
     * (Нормальная) равнопромежуточная азимутальная проекция Постеля
     */
    POSTEL(6),
    /**
     * Азимутальная равнопромежуточная косая
     */
    AZIMUTHALOBLIQUE(7),
    /**
     * Цилиндрическая прямая равноугольная Меркатора
     */
    MERCATORMAP(8),
    /**
     * Цилиндрическая произвольная(проф.Урмаева)
     */
    URMAEV(9),
    /**
     * Поликоническая проекция ЦНИИГАиК
     */
    POLYCONICAL(10),
    /**
     * Простая видоизмененная поликоническая
     */
    SIMPLEPOLYCONICAL(11),
    /**
     * Псевдоконическая произвольная
     */
    PSEUDOCONICAL(12),
    /**
     * Стереографическая полярная
     */
    STEREOGRAPHICPOLAR(13),
    /**
     * Равноугольная Чебышева
     */
    CHEBISHEV(14),
    /**
     * Гномоническая
     */
    GNOMONIC(15),
    /**
     * Цилиндрическая специальная для бланковой карты
     */
    CYLINDRICALSPECIALBLANK(16),
    /**
     * UTM
     */
    UTM(17),
    /**
     * Псевдоцилиндрическая равновеликая синусоидальная проекция Каврайского
     */
    KAVRAJSKY(18),
    /**
     * Псевдоцилиндрическая равновеликая эллиптическая проекция Мольвейде
     */
    MOLLWEIDE(19),
    /**
     * (Прямая) равнопромежуточная коническая проекция
     */
    CONICALEQUIDISTANT(20),
    /**
     * (Прямая) равновеликая коническая проекция
     */
    CONICALEQUALAREA(21),
    /**
     * (Прямая) равноугольная коническая проекция
     */
    CONICALDIRECTORTHOMORPHIC(22),
    /**
     * Полярная равноугольная азимутальная (стереографическая) проекция
     */
    AZIMUTHALORTHOMORPHICPOLAR(23),
    /**
     * (Нормальная) равновеликая азимутальная проекция Ламберта
     */
    LAMBERTAZIMUTHALEQUALAREA(24),
    /**
     * Псевдоцилиндрическая синусоидальная проекция Урмаева для карт океанов(Тихого и Индийского)
     */
    URMAEVSINUSOIDAL(25),
    /**
     * Производная равновеликая проекция Аитова-Гамера
     */
    AITOFF(26),
    /**
     * Равнопромежуточная цилиндрическая проекция
     */
    CYLINDRICALEQUALSPACED(27),
    /**
     * Равновеликая цилиндрическая проекция Ламберта
     */
    LAMBERTCYLINDRICALEQUALAREA(28),
    /**
     * Видоизмененная простая поликоническая проекция (международная)
     */
    MODIFIEDPOLYCONICAL(29),
    /**
     * Косая равновеликая азимутальная проекция Ламберта
     */
    LAMBERTOBLIQUEAZIMUTHAL(30),
    /**
     * Равноугольная поперечно-цилиндрическая проекция
     */
    TRANSVERSECYLINDRICAL(31),
    /**
     * Система координат 63 года
     */
    GAUSSCONFORMAL_SYSTEM_63(32),
    /**
     * Широта/Долгота Цилиндрическая на шаре (Popular Visualisation Pseudo Mercator EPSG:3857)
     */
    LATITUDELONGITUDE(33),
    /**
     * Цилиндрическая Миллера на шаре ESRI:54003
     */
    MILLERCYLINDRICAL(34),
    /**
     * (Google) Popular Visualisation Pseudo Mercator EPSG:3857 (900913)
     */
    WEBMERCATOR(35),
    /**
     * Цилиндрическая прямая равноугольная Меркатора
     */
    MERCATOR_2SP(36),
    /**
     * Крайний номер проекции
     */
    LASTPROJECTIONNUMBER(36);

    /**
     * Map contains all enums names. Its will be used in ComboBox's and etc.
     */
    private static final Map<MapProjection, String> _names = new HashMap<>();
    static {
        _names.put(UNDEFINED, "Не установлено");
        _names.put(GAUSSCONFORMAL, "Равноугольная Гаусса-Крюгера");
        _names.put(CONICALORTHOMORPHIC, "Коническая равноугольная");
        _names.put(CYLINDRICALSPECIAL, "Цилиндрическая специальная");
        _names.put(LAMBERT, "Азимутальная поперечная Ламберта");
        _names.put(STEREOGRAPHIC, "Стереографическая");
        _names.put(POSTEL, "(Нормальная) равнопромежуточная азимутальная проекция Постеля");
        _names.put(AZIMUTHALOBLIQUE, "Азимутальная равнопромежуточная косая");
        _names.put(MERCATORMAP, "Цилиндрическая прямая равноугольная Меркатора");
        _names.put(URMAEV, "Цилиндрическая произвольная(проф.Урмаева)");
        _names.put(POLYCONICAL, "Поликоническая проекция ЦНИИГАиК");
        _names.put(SIMPLEPOLYCONICAL, "Простая видоизмененная поликоническая");
        _names.put(PSEUDOCONICAL, "Псевдоконическая произвольная");
        _names.put(STEREOGRAPHICPOLAR, "Стереографическая полярная");
        _names.put(CHEBISHEV, "Равноугольная Чебышева");
        _names.put(GNOMONIC, "Гномоническая");
        _names.put(CYLINDRICALSPECIALBLANK, "Цилиндрическая специальная для бланковой карты");
        _names.put(UTM, "UTM");
        _names.put(KAVRAJSKY, "Псевдоцилиндрическая равновеликая синусоидальная проекция Каврайского");
        _names.put(MOLLWEIDE, "Псевдоцилиндрическая равновеликая эллиптическая проекция Мольвейде");
        _names.put(CONICALEQUIDISTANT, "(Прямая) равнопромежуточная коническая проекция");
        _names.put(CONICALEQUALAREA, "(Прямая) равновеликая коническая проекция");
        _names.put(CONICALDIRECTORTHOMORPHIC, "(Прямая) равноугольная коническая проекция");
        _names.put(AZIMUTHALORTHOMORPHICPOLAR, "Полярная равноугольная азимутальная (стереографическая) проекция");
        _names.put(LAMBERTAZIMUTHALEQUALAREA, "(Нормальная) равновеликая азимутальная проекция Ламберта");
        _names.put(URMAEVSINUSOIDAL, "Псевдоцилиндрическая синусоидальная проекция Урмаева для карт океанов(Тихого и Индийского)");
        _names.put(AITOFF, "Производная равновеликая проекция Аитова-Гамера");
        _names.put(CYLINDRICALEQUALSPACED, "Равнопромежуточная цилиндрическая проекция");
        _names.put(LAMBERTCYLINDRICALEQUALAREA, "Равновеликая цилиндрическая проекция Ламберта");
        _names.put(MODIFIEDPOLYCONICAL, "Видоизмененная простая поликоническая проекция (международная)");
        _names.put(LAMBERTOBLIQUEAZIMUTHAL, "Косая равновеликая азимутальная проекция Ламберта");
        _names.put(TRANSVERSECYLINDRICAL, "Равноугольная поперечно-цилиндрическая проекция");
        _names.put(GAUSSCONFORMAL_SYSTEM_63, "Система координат 63 года");
        _names.put(LATITUDELONGITUDE, "Широта/Долгота Цилиндрическая на шаре (Popular Visualisation Pseudo Mercator EPSG:3857)");
        _names.put(MILLERCYLINDRICAL, "Цилиндрическая Миллера на шаре ESRI:54003");
        _names.put(WEBMERCATOR, "(Google) Popular Visualisation Pseudo Mercator EPSG:3857 (900913)");
        _names.put(MERCATOR_2SP, "Цилиндрическая прямая равноугольная Меркатора");
        _names.put(LASTPROJECTIONNUMBER, "Крайний номер проекции");
    }
    /**
     * Map contains integer (value) and enum object.
     */
    private static final Map<Integer, MapProjection> _intToEnumMap = new HashMap<>();
    static {
        for (MapProjection mapProjection : values()) {
            _intToEnumMap.put(mapProjection.getValue(), mapProjection);
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
    MapProjection(int value) {
        _value = value;
    }

    /**
     * Converts from integer value into enum.
     * @param value integer value of enum.
     * @return enum by integer.
     */
    public static MapProjection fromValue(int value) {
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
    public static Map<MapProjection, String> getNames() {
        return _names;
    }

    /**
     * Get name of enum
     * @param mapProjection enum
     * @return name
     */
    public static String getName(MapProjection mapProjection) {
        return _names.get(mapProjection);
    }
}
