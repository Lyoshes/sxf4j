package org.cleanlogic.sxf4j.format;

import org.cleanlogic.sxf4j.enums.SemanticType;

/**
 * @author Serge Silaev aka iSergio <s.serge.b@gmail.com>
 */
public class SXFRecordSemantic {
    /**
     * Semantic code
     */
    public int code;
    /**
     * Semantic type
     */
    public SemanticType type;
    /**
     * Semantic scale
     */
    public int scale;
    /**
     * Semantic value
     */
    public String value;

    public void print() {
        print(this);
    }

    public static void print(SXFRecordSemantic sxfRecordSemantic) {
        System.out.printf("Semantic:\n");
        System.out.printf("\t\tCode:\t%d\n", sxfRecordSemantic.code);
        System.out.printf("\t\tType:\t%s (%s)\n", sxfRecordSemantic.type, sxfRecordSemantic.type.getName());
        System.out.printf("\t\tScale:\t%d\n", sxfRecordSemantic.scale);
        System.out.printf("\t\tValue:\t%s\n", sxfRecordSemantic.value);
    }
}
