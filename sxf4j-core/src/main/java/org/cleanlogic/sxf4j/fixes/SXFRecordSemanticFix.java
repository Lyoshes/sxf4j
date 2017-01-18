package org.cleanlogic.sxf4j.fixes;

import org.cleanlogic.sxf4j.format.SXFRecordSemantic;

/**
 * @author Serge Silaev aka iSergio <s.serge.b@gmail.com>
 */
public class SXFRecordSemanticFix {
    public static SXFRecordSemantic FixScaleValue(SXFRecordSemantic sxfRecordSemantic) {
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
}
