package org.cleanlogic.sxf4j.enums;

import java.util.HashMap;
import java.util.Map;

/**
 * @author Serge Silaev aka iSergio <s.serge.b@gmail.com>
 */
public enum Secrecy {
    /**
     * Не установлено
     */
    UNKNOWN,
    /**
     * Открытая информация
     */
    OPEN,
    /**
     * Информация с ограниченным доступом
     */
    LIMITED,
    /**
     * Информация для служебного пользования
     */
    OFFICIAL,
    /**
     * Секретная информация
     */
    SECRET,
    /**
     * Совершенно секретная информация
     */
    TOP;

    private static final Map<Secrecy, String> _names = new HashMap<>();
    static {
        _names.put(UNKNOWN, "Не установлено");
        _names.put(OPEN, "Открытая информация");
        _names.put(LIMITED, "Информация с ограниченным доступом");
        _names.put(OFFICIAL, "Информация для служебного пользования");
        _names.put(SECRET, "Секретная информация");
        _names.put(TOP, "Совершенно секретная информация");
    }

    public static Secrecy fromValue(int value) {
        return values()[value];
    }

    public String getName() {
        return _names.get(this);
    }

    public static Map<Secrecy, String> getNames() {
        return _names;
    }

    public static String getName(Secrecy secrecy) {
        return _names.get(secrecy);
    }
}
