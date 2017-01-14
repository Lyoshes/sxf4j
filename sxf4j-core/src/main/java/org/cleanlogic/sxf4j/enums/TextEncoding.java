package org.cleanlogic.sxf4j.enums;

import java.util.HashMap;
import java.util.Map;

/**
 * Supported text encodings by SXF format
 * @author Serge Silaev aka iSergio <s.serge.b@gmail.com>
 */
public enum TextEncoding {
    /**
     * В кодировке ASCIIZ(DOS)
     */
    ASCIIZ,
    /**
     * В кодировке ANSI
     */
    ANSI,
    /**
     * В кодировке KOI8-R(Unix)
     */
    KOI8R;

    private static final Map<TextEncoding, String> _names = new HashMap<>();
    static {
        _names.put(ASCIIZ, "В кодировке ASCIIZ(DOS)");
        _names.put(ANSI, "В кодировке ANSI");
        _names.put(KOI8R, "В кодировке KOI8-R(Unix)");
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
