package org.cleanlogic.sxf4j.format;

import org.cleanlogic.sxf4j.SXF;
import org.cleanlogic.sxf4j.enums.Projection;
import org.cleanlogic.sxf4j.enums.Secrecy;
import org.cleanlogic.sxf4j.enums.TextEncoding;

/**
 * The descriptor contains information that is used to monitor and restore the structural integrity of the format.
 * @author Serge Silaev aka iSergio <s.serge.b@gmail.com>
 */
public class SXFDescriptor {
    /**
     * Identifier of Description data. Must be {@link SXF#DAT_SXF}
     */
    public int identifier;
    /**
     * Length of Description
     * @see SXF#DAT_4_LENGTH
     * @see SXF#DAT_3_LENGTH
     */
    public int length;
    /**
     * Sheet nomenclature
     */
    public String nomenclature;
    /**
     * Record count
     */
    public int recordCount;
    /**
     * Flag of condition SXF. Must be 3
     */
    public int conditionFlag;
    /**
     * Projection of condition coordinates data.
     */
    public Projection projectionFlag;
    /**
     * Real coordinates.
     */
    public int realPlaceFlag;
    public int codeTypeFlag;
    /**
     * Table of generalization
     */
    public int generalizationFlag;
    /**
     * Only {@link SXF#VERSION_4}
     */
    public TextEncoding textEncodingFlag;
    /**
     * Only {@link SXF#VERSION_4}
     */
    public Secrecy secrecy;
    /**
     * Code of classificator
     * Only {@link SXF#VERSION_3}
     */
    public int code;

    public void print() {
        print(this);
    }

    public static void print(SXFDescriptor sxfDescriptor) {
        System.out.printf("Descriptor Info\n");
        System.out.printf("\tIdentifier:\t0x%08x\n", sxfDescriptor.identifier);
        System.out.printf("\tLength:\t\t%d\n", sxfDescriptor.length);
        System.out.printf("\tNomenclature:\t%s\n", sxfDescriptor.nomenclature);
        System.out.printf("\tRecord count:\t%d\n", sxfDescriptor.recordCount);
        System.out.printf("\tInfoFlags:\n");
        System.out.printf("\t\tConditionFlag:\t\t%d\n", sxfDescriptor.conditionFlag);
        System.out.printf("\t\tProjection flag:\t%s (%s)\n", sxfDescriptor.projectionFlag, sxfDescriptor.projectionFlag.getName());
        System.out.printf("\t\tRealCoordinatesFlag:\t%d\n", sxfDescriptor.realPlaceFlag);
        System.out.printf("\t\tCodeTypeFlag:\t\t%d\n", sxfDescriptor.codeTypeFlag);
        System.out.printf("\t\tGeneralizationFlag:\t%d\n", sxfDescriptor.generalizationFlag);
        if (sxfDescriptor.length == SXF.DAT_4_LENGTH) {
            System.out.printf("\t\tTextEncodingFlag:\t%s (%s)\n", sxfDescriptor.textEncodingFlag, sxfDescriptor.textEncodingFlag.getName());
            System.out.printf("\tSecrecy:\t%s (%s)\n", sxfDescriptor.secrecy, sxfDescriptor.secrecy.getName());
        }
    }
}
