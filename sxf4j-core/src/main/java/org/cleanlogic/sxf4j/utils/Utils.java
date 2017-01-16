package org.cleanlogic.sxf4j.utils;

import org.cleanlogic.sxf4j.enums.*;
import org.cleanlogic.sxf4j.format.SXFPassport;

import java.io.File;
import java.util.List;

/**
 * @author Serge Silaev aka iSergio <s.serge.b@gmail.com>
 */
public class Utils {
    /**
     * Try detect SRID of SXF by they passport by constants
     * @param sxfPassport passport of SXF
     * @return detected SRID or 0 if not detected
     */
    public static int detectSRID(SXFPassport sxfPassport) {
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

    /**
     * Recursive search files by extension filter
     * @param file where will be search processed
     * @param files list of finded files
     * @param filter file extension filter (.sxf)
     */
    public static void search(File file, List<File> files, String filter) {
        File[] listFiles = file.listFiles();
        if (listFiles == null) {
            return;
        }
        for (File _file : listFiles) {
            if (_file.isDirectory()) {
                search(_file, files, filter);
            } else if (_file.isFile()) {
                if (_file.getName().toLowerCase().endsWith(filter.toLowerCase())) {
                    files.add(_file);
                }
            }
        }
    }
}
