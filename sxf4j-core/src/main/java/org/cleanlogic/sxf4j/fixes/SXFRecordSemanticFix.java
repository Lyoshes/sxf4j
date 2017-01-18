package org.cleanlogic.sxf4j.fixes;

import org.cleanlogic.sxf4j.format.SXFRecordSemantic;

/**
 * @author Serge Silaev aka iSergio <s.serge.b@gmail.com>
 */
public class SXFRecordSemanticFix {
    public static SXFRecordSemantic ScaleValue(SXFRecordSemantic sxfRecordSemantic) {
        switch (sxfRecordSemantic.type) {
            case STRDOS:
            case STRING:
            case STRUNI:
            case STRUTF16:
                if (sxfRecordSemantic.scale < 0) {
                    sxfRecordSemantic.scale *= -1;
                }
                break;
            default: break;
        }
        return sxfRecordSemantic;
    }

    public static String RemoveBinaryZero(byte[] string) {
        String result = "";
        for (byte b : string) {
            if (b != 0x00) {
                result += (char) b;
            }
        }
        return result;
    }
}
