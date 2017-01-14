package org.cleanlogic.sxf4j;

import org.cleanlogic.sxf4j.enums.*;
import org.cleanlogic.sxf4j.format.SXFPassport;

/**
 * @author Serge Silaev aka iSergio <s.serge.b@gmail.com>
 */
public class SXF {
    /**
     * Binary file of SXF format
     */
    public static final int FILE_SXF = 0x00465853;
    /**
     * Text file of SXF format
     */
    public static final int FILE_TXT = 0x4658532E;
    /**
     * Data descriptor of SXF format
     */
    public static final int DAT_SXF = 0x00544144;
    public static final int DAT_3_LENGTH = 44;
    public static final int DAT_4_LENGTH = 52;
    /**
     * Record descriptor of SXF format
     */
    public static final int RECORD_SXF = 0x7FFF7FFF;
    /**
     * 3 version redaction of SXF format
     */
    public static final int VERSION_3 = 0x00000300;
    /**
     * Length of SXF Passport for version 3;
     */
    public static final int PASSPORT_3_LENGHT = 256;
    /**
     * 4 version redaction of SXF format
     */
    public static final int VERSION_4 = 0x00040000;
    /**
     * Length of SXF Passport for version 4;
     */
    public static final int PASSPORT_4_LENGHT = 400;

    public static int DetectSRID(SXFPassport sxfPassport) {
        if (sxfPassport.mapType == MapType.LATLONG &&
                sxfPassport.ellipsoidKind == EllipsoidKind.WGS_84 &&
                sxfPassport.coordinateSystem == CoordinateSystem.GEOCOORDINATE &&
                sxfPassport.frameKind == FrameKind.FREE &&
                sxfPassport.planeUnit == Unit.METRE &&
                sxfPassport.materialProjection == MapProjection.LATITUDELONGITUDE) {
            return 4326;
        }
        if (sxfPassport.mapType == MapType.MERCATOR &&
                sxfPassport.ellipsoidKind == EllipsoidKind.WGS_84 &&
                sxfPassport.coordinateSystem == CoordinateSystem.CONDITION &&
                sxfPassport.frameKind == FrameKind.FREE &&
                sxfPassport.planeUnit == Unit.METRE &&
                sxfPassport.materialProjection == MapProjection.WEBMERCATOR) {
            return 3857;
        }
        if (sxfPassport.mapType == MapType.TOPOGRAPHIC &&
                sxfPassport.ellipsoidKind == EllipsoidKind.KRASOVSKY42 &&
                sxfPassport.coordinateSystem == CoordinateSystem.ORTHOGONAL &&
                sxfPassport.frameKind == FrameKind.TRAPEZECURVE &&
                sxfPassport.planeUnit == Unit.METRE &&
                sxfPassport.materialProjection == MapProjection.GAUSSCONFORMAL &&
                sxfPassport.heightSystem == HeightSystem.BALTIC) {
            int zone = sxfPassport.getZone();
            return 28400 + zone;
        }
        return 0;
    }
}
