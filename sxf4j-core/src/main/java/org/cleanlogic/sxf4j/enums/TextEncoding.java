package org.cleanlogic.sxf4j.enums;

import java.util.HashMap;
import java.util.Map;

/**
 * Supported text encodings by SXF format
 * ASCIIZ - IBM866
 * ANSI - CP1251
 * KOI8-r - KOI8R
 * @author Serge Silaev aka iSergio <s.serge.b@gmail.com>
 */
public enum TextEncoding {
    /**
     * В кодировке ASCIIZ(DOS)
     */
    IBM866,
    /**
     * В кодировке ANSI
     */
    CP1251,
    /**
     * В кодировке KOI8-R(Unix)
     */
    KOI8R;

    private static final Map<TextEncoding, String> _names = new HashMap<>();
    static {
        _names.put(IBM866, String.valueOf(IBM866));
        _names.put(CP1251, String.valueOf(CP1251));
        _names.put(KOI8R, "KOI8-R");
    }

    public static TextEncoding fromValue(int value) {
        return values()[value];
    }

    public String getName() {
        return _names.get(this);
    }

    public static Map<TextEncoding, String> getNames() {
        return _names;
    }

    public static String getName(TextEncoding textEncoding) {
        return _names.get(textEncoding);
    }
}
