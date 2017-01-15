package org.cleanlogic.sxf4j.fixes;

import com.vividsolutions.jts.geom.Geometry;
import org.cleanlogic.sxf4j.enums.MapInitKind;
import org.cleanlogic.sxf4j.enums.MapInitType;
import org.cleanlogic.sxf4j.format.SXFPassport;
import org.cleanlogic.sxf4j.format.SXFRecord;
import org.cleanlogic.sxf4j.format.SXFRecordHeader;
import org.cleanlogic.sxf4j.io.SXFReaderOptions;

import java.nio.MappedByteBuffer;

import static org.cleanlogic.sxf4j.enums.MapType.TOPOGRAPHIC;

/**
 * @author Serge Silaev aka iSergio <s.serge.b@gmail.com>
 */
public class SXFPassportFixes {
    public enum FixCoordinates {
        FIX,
        SKIP
    }
    /**
     * Fix map init kind. This fix it if {@link SXFPassport#materialKind} not in enum, set {@link MapInitKind#MAP} by default.
     * @param sxfPassport {@link SXFPassport} to fix.
     */
    public static void FixMapInitKind(SXFPassport sxfPassport) {
        switch (sxfPassport.materialKind) {
            case MAP:
            case PHOTO:
            case IMAGE:
            case GRAM:
            case UNDEFINED:
                break;
            default: sxfPassport.materialKind = MapInitKind.MAP; break;
        }
    }

    /**
     * Fix map init type. This fix if if (@link {@link SXFPassport#materialType} not in enum.
     * @param sxfPassport {@link SXFPassport} to fix.
     */
    public static void FixMapInitType(SXFPassport sxfPassport) {
        switch (sxfPassport.materialKind) {
            case MAP:
                switch (sxfPassport.materialType) {
                    case MAPRUN:
                    case FINAL:
                    case MANUSCRIPT:
                    case UPDATE:
                    case SPECIAL:
                    case CONSTANT:
                    case UNDEFINED:
                        break;
                    default: sxfPassport.materialType = MapInitType.MAPRUN; break;
                }
                break;

            case PHOTO:
            case IMAGE:
            case GRAM:
                switch (sxfPassport.materialType) {
                    case FGM:
                    case FGMMAPRUN:
                    case FGMMANUSCRIPT:
                    case FGMFINAL:
                    case FGMCONSTANT:
                    case FGMSPECIAL:
                    case SPACE:
                    case AERO:
                    case PHOTOGRAM:
                    case UNDEFINED:
                        break;
                    default: sxfPassport.materialType = MapInitType.FGM; break;
                }
                break;
            default: sxfPassport.materialKind = MapInitKind.MAP; break;
        }
    }

    /**
     * Sets offset start coordinate system, if passport border not equals record border (excode = 91000000) or metaobject (number = 0 excode = 0 incode = 1)
     * @param mappedByteBuffer
     * @param sxfPassport
     * @param sxfRecordHeader
     * @param sxfReaderOptions
     */
    public static void FisxBorderRecordOffset(MappedByteBuffer mappedByteBuffer, SXFPassport sxfPassport, SXFRecordHeader sxfRecordHeader, SXFReaderOptions sxfReaderOptions) {
            // Store current reader position
            int position = mappedByteBuffer.position();
            SXFRecord sxfRecord = new SXFRecord(mappedByteBuffer, sxfPassport, sxfReaderOptions);
            sxfRecord.setHeader(sxfRecordHeader);

            Geometry geometry = sxfRecord.getMetric().geometry;
            // If Passport border equals border record dx0 and dy0 = 0
            double dx0 = sxfPassport.xSouthWest - geometry.getCoordinates()[0].x;
            double dy0 = sxfPassport.ySouthWest - geometry.getCoordinates()[0].y;
            sxfPassport.dx0 = dx0;
            sxfPassport.dy0 = dy0;
            // Restore current position
            mappedByteBuffer.position(position);
    }

    /**
     * Fix axis meridian. In some SXF Axis Meridian not set or it wrong. This method fix it.
     * @param sxfPassport {@link SXFPassport} to fix.
     */
    public static void FixSXFPassportAxisMeridian(SXFPassport sxfPassport) {
        if (sxfPassport.mapType == TOPOGRAPHIC) {
            int zone = sxfPassport.getZone();
            double axisMeririan;
            if ((zone <= 0) || (zone > 60)) {
                axisMeririan = 0.;
            } else {
                axisMeririan = (((double)(zone - 1)) * 6. + 3.) * Math.PI / 180.;
            }
            sxfPassport.axisMeridian = axisMeririan * 180. / Math.PI;
        }
    }
//
//    public static void FixSXFPassportBorder(MappedByteBuffer mappedByteBuffer, SXFPassport sxfPassport, SXFRecordHeader sxfRecordHeader, SXFReaderOptions sxfReaderOptions) {
//        int position = mappedByteBuffer.position();
//        SXFRecord sxfRecord = new SXFRecord(mappedByteBuffer, sxfPassport);
//        sxfRecord.setHeader(sxfRecordHeader);
//
//        Geometry geometry = sxfRecord.getMetric().geometry;
//        Envelope envelope = geometry.getEnvelopeInternal();
//        /*
//         X
//         |
//         |
//         |
//         +----------Y
//         */
//        double xSouthWest = envelope.getMinX();
//        double ySouthWest = envelope.getMinY();
//        double xNorthWest = envelope.getMaxX();
//        double yNorthWest = envelope.getMinY();
//        double xNorthEast = envelope.getMaxX();
//        double yNorthEast = envelope.getMaxY();
//        double xSouthEast = envelope.getMinX();
//        double ySouthEast = envelope.getMaxY();
//        System.out.printf("Border envelope: (%f %f), (%f %f), (%f %f), (%f %f)\n", xSouthWest, ySouthWest, xNorthWest, yNorthWest, xNorthEast, yNorthEast, xSouthEast, ySouthEast);
//
//        // Restore buffer position
//        mappedByteBuffer.position(position);
//
//        System.out.printf("Point\tTheory\t\tFact\t\tDelta\n");
//        if (sxfPassport.xSouthWest != xSouthWest) {
//            System.out.printf("x1\t%f\t%f\t%f\n", sxfPassport.xSouthWest, xSouthWest, xSouthWest - sxfPassport.xSouthWest);
//        }
//        if (sxfPassport.ySouthWest != ySouthWest) {
//            System.out.printf("y1\t%f\t%f\t%f\n", sxfPassport.ySouthWest, ySouthWest, ySouthWest - sxfPassport.ySouthWest);
//        }
//
//        if (sxfPassport.xNorthWest != xNorthWest) {
//            System.out.printf("x2\t%f\t%f\t%f\n", sxfPassport.xNorthWest, xNorthWest, xNorthWest - sxfPassport.xNorthWest);
//        }
//        if (sxfPassport.yNorthWest != yNorthWest) {
//            System.out.printf("y2\t%f\t%f\t%f\n", sxfPassport.yNorthWest, yNorthWest, yNorthWest - sxfPassport.yNorthWest);
//        }
//
//        if (sxfPassport.xNorthEast != xNorthEast) {
//            System.out.printf("x3\t%f\t%f\t%f\n", sxfPassport.xNorthEast, xNorthEast, xNorthEast - sxfPassport.xNorthEast);
//        }
//        if (sxfPassport.yNorthEast != yNorthEast) {
//            System.out.printf("y3\t%f\t%f\t%f\n", sxfPassport.yNorthEast, yNorthEast, yNorthEast - sxfPassport.yNorthEast);
//        }
//
//        if (sxfPassport.xSouthEast != xSouthEast) {
//            System.out.printf("x4\t%f\t%f\t%f\n", sxfPassport.xSouthEast, xSouthEast, xSouthEast - sxfPassport.xSouthEast);
//        }
//        if (sxfPassport.ySouthEast != ySouthEast) {
//            System.out.printf("y4\t%f\t%f\t%f\n", sxfPassport.ySouthEast, ySouthEast, ySouthEast - sxfPassport.ySouthEast);
//        }
//
////        // Fix worked on permanent read, because we need store position of reader before run fix and restore after fix
//////        if (sxfPassport.dxBorderDevice == 0 || sxfPassport.dyBorderDevice == 0) {
////            int position = mappedByteBuffer.position();
////            SXFRecord sxfRecord = new SXFRecord(mappedByteBuffer, sxfPassport);
////            sxfRecord.setHeader(sxfRecordHeader);
////
////            Geometry geometry = sxfRecord.getMetric().geometry;
////            Envelope envelope = geometry.getEnvelopeInternal();
////                /*
////                X
////                |
////                |
////                |
////                +----------Y
////                 */
////            double xSouthWest = envelope.getMinX();
////            double ySouthWest = envelope.getMinY();
////            double xNorthWest = envelope.getMaxX();
////            double yNorthWest = envelope.getMinY();
////            double xNorthEast = envelope.getMaxX();
////            double yNorthEast = envelope.getMaxY();
////            double xSouthEast = envelope.getMinX();
////            double ySouthEast = envelope.getMaxY();
////
////            System.out.printf("Border envelope: (%f %f), (%f %f), (%f %f), (%f %f)\n", xSouthWest, ySouthWest, xNorthWest, yNorthWest, xNorthEast, yNorthEast, xSouthEast, ySouthEast);
////
////            // Offset of center coordinate system
////            sxfPassport.dxBorderDevice = sxfPassport.xSouthWest - xSouthWest;
////            sxfPassport.dyBorderDevice = sxfPassport.ySouthWest - ySouthWest;
////            // Restore buffer position
////            mappedByteBuffer.position(position);
////
////            FixSXFPassportBorder(mappedByteBuffer, sxfPassport, sxfRecordHeader, sxfReaderOptions);
////        } else {
////            // Check border corners and border object
////            int position = mappedByteBuffer.position();
////            SXFRecord sxfRecord = new SXFRecord(mappedByteBuffer, sxfPassport);
////            sxfRecord.setHeader(sxfRecordHeader);
////
////            Geometry geometry = sxfRecord.getMetric().geometry;
////            Envelope envelope = geometry.getEnvelopeInternal();
////            /*
////             X
////             |
////             |
////             |
////             +----------Y
////             */
////            double xSouthWest = envelope.getMinX();
////            double ySouthWest = envelope.getMinY();
////            double xNorthWest = envelope.getMaxX();
////            double yNorthWest = envelope.getMinY();
////            double xNorthEast = envelope.getMaxX();
////            double yNorthEast = envelope.getMaxY();
////            double xSouthEast = envelope.getMinX();
////            double ySouthEast = envelope.getMaxY();
////
////            // Restore buffer position
////            mappedByteBuffer.position(position);
////
////            System.out.printf("Point\tTheory\t\tFact\t\tDelta\n");
////            if (sxfPassport.xSouthWest != xSouthWest) {
////                System.out.printf("x1\t%f\t%f\t%f\n", sxfPassport.xSouthWest, xSouthWest, xSouthWest - sxfPassport.xSouthWest);
////            }
////            if (sxfPassport.ySouthWest != ySouthWest) {
////                System.out.printf("y1\t%f\t%f\t%f\n", sxfPassport.ySouthWest, ySouthWest, ySouthWest - sxfPassport.ySouthWest);
////            }
////
////            if (sxfPassport.xNorthWest != xNorthWest) {
////                System.out.printf("x2\t%f\t%f\t%f\n", sxfPassport.xNorthWest, xNorthWest, xNorthWest - sxfPassport.xNorthWest);
////            }
////            if (sxfPassport.yNorthWest != yNorthWest) {
////                System.out.printf("y2\t%f\t%f\t%f\n", sxfPassport.yNorthWest, yNorthWest, yNorthWest - sxfPassport.yNorthWest);
////            }
////
////            if (sxfPassport.xNorthEast != xNorthEast) {
////                System.out.printf("x3\t%f\t%f\t%f\n", sxfPassport.xNorthEast, xNorthEast, xNorthEast - sxfPassport.xNorthEast);
////            }
////            if (sxfPassport.yNorthEast != yNorthEast) {
////                System.out.printf("y3\t%f\t%f\t%f\n", sxfPassport.yNorthEast, yNorthEast, yNorthEast - sxfPassport.yNorthEast);
////            }
////
////            if (sxfPassport.xSouthEast != xSouthEast) {
////                System.out.printf("x4\t%f\t%f\t%f\n", sxfPassport.xSouthEast, xSouthEast, xSouthEast - sxfPassport.xSouthEast);
////            }
////            if (sxfPassport.ySouthEast != ySouthEast) {
////                System.out.printf("y4\t%f\t%f\t%f\n", sxfPassport.ySouthEast, ySouthEast, ySouthEast - sxfPassport.ySouthEast);
////            }
////        }
//    }
}
