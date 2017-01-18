package org.cleanlogic.sxf4j.enums;

import java.util.HashMap;
import java.util.Map;

/**
 * @author Serge Silaev aka iSergio <s.serge.b@gmail.com>
 */
public enum Projection {
    /**
     * Данные не соответствуют проекции
     */
    NOADEQUACY,
    /**
     * Данные соответствуют проекции
     */
    ADEQUACY;

    /**
     * Map contains all enums names. Its will be used in ComboBox's and etc.
     */
    private static final Map<Projection, String> _names = new HashMap<>();
    static {
        _names.put(NOADEQUACY, "Данные не соответствуют проекции");
        _names.put(ADEQUACY, "Данные соответствуют проекции");
    }

    /**
     * Converts from integer value into enum.
     * @param value integer value of enum.
     * @return enum by integer.
     */
    public static Projection fromValue(int value) {
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
    public static Map<Projection, String> getNames() {
        return _names;
    }

    /**
     * Get name of enum
     * @param projection enum
     * @return name
     */
    public static String getName(Projection projection) {
        return _names.get(projection);
    }
}
