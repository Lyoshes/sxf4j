package org.cleanlogic.sxf4j.enums;

import java.util.HashMap;
import java.util.Map;

/**
 * @author Serge Silaev aka iSergio <s.serge.b@gmail.com>
 */
public enum FrameOut {
    NO_FRAME_OUT(0),
    NORTH(8),
    EAST(4),
    SOUTH(2),
    WEST(1);

    private static final Map<FrameOut, String> _names = new HashMap<>();
    static {
        _names.put(NO_FRAME_OUT, "нет выходов нa paмку");
        _names.put(NORTH, "Северная рамка");
        _names.put(EAST, "Восточная рамка");
        _names.put(SOUTH, "Южная рамка");
        _names.put(WEST, "Западная рамка");
    }

    private static final Map<Integer, FrameOut> _intToEnumMap = new HashMap<>();
    static {
        for (FrameOut frameOut : values()) {
            _intToEnumMap.put(frameOut.getValue(), frameOut);
        }
    }

    private final int _value;

    FrameOut(int value) {
        _value = value;
    }

    public static FrameOut fromValue(int value) {
        return _intToEnumMap.get(value);
    }

    public int getValue() {
        return _value;
    }

    public String getName() {
        return _names.get(this);
    }

    public static Map<FrameOut, String> getNames() {
        return _names;
    }

    public static String getName(FrameOut frameOut) {
        return _names.get(frameOut);
    }
}
