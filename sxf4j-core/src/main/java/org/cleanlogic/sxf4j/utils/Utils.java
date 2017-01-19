/*
 * Copyright 2017 iserge.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.cleanlogic.sxf4j.utils;

import com.vividsolutions.jts.geom.Geometry;
import com.vividsolutions.jts.io.WKBWriter;
import com.vividsolutions.jts.io.WKTWriter;
import org.cleanlogic.sxf4j.enums.*;
import org.cleanlogic.sxf4j.format.SXFPassport;

import java.io.File;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author Serge Silaev aka iSergio <s.serge.b@gmail.com>
 */
public class Utils {
    /**
     * Extended map by SRID's Gauss-Krugger
     */
    private static final Map<Integer, String> _projParams = new HashMap<>();
    static {
        _projParams.put(28401, "+proj=tmerc +lat_0=0 +lon_0=3 +k=1 +x_0=1500000 +y_0=0 +ellps=krass +towgs84=23.92,-141.27,-80.9,-0,0.35,0.82,-0.12 +units=m +no_defs");

        _projParams.put(28433, "+proj=tmerc +lat_0=0 +lon_0=-165 +k=1 +x_0=33500000 +y_0=0 +ellps=krass +towgs84=23.92,-141.27,-80.9,-0,0.35,0.82,-0.12 +units=m +no_defs");
        _projParams.put(28434, "+proj=tmerc +lat_0=0 +lon_0=-159 +k=1 +x_0=34500000 +y_0=0 +ellps=krass +towgs84=23.92,-141.27,-80.9,-0,0.35,0.82,-0.12 +units=m +no_defs");
        _projParams.put(28435, "+proj=tmerc +lat_0=0 +lon_0=-153 +k=1 +x_0=35500000 +y_0=0 +ellps=krass +towgs84=23.92,-141.27,-80.9,-0,0.35,0.82,-0.12 +units=m +no_defs");
        _projParams.put(28436, "+proj=tmerc +lat_0=0 +lon_0=-147 +k=1 +x_0=36500000 +y_0=0 +ellps=krass +towgs84=23.92,-141.27,-80.9,-0,0.35,0.82,-0.12 +units=m +no_defs");
        _projParams.put(28437, "+proj=tmerc +lat_0=0 +lon_0=-141 +k=1 +x_0=37500000 +y_0=0 +ellps=krass +towgs84=23.92,-141.27,-80.9,-0,0.35,0.82,-0.12 +units=m +no_defs");
        _projParams.put(28438, "+proj=tmerc +lat_0=0 +lon_0=-135 +k=1 +x_0=38500000 +y_0=0 +ellps=krass +towgs84=23.92,-141.27,-80.9,-0,0.35,0.82,-0.12 +units=m +no_defs");
        _projParams.put(28439, "+proj=tmerc +lat_0=0 +lon_0=-129 +k=1 +x_0=39500000 +y_0=0 +ellps=krass +towgs84=23.92,-141.27,-80.9,-0,0.35,0.82,-0.12 +units=m +no_defs");
        _projParams.put(28440, "+proj=tmerc +lat_0=0 +lon_0=-123 +k=1 +x_0=40500000 +y_0=0 +ellps=krass +towgs84=23.92,-141.27,-80.9,-0,0.35,0.82,-0.12 +units=m +no_defs");
        _projParams.put(28441, "+proj=tmerc +lat_0=0 +lon_0=-117 +k=1 +x_0=41500000 +y_0=0 +ellps=krass +towgs84=23.92,-141.27,-80.9,-0,0.35,0.82,-0.12 +units=m +no_defs");
        _projParams.put(28442, "+proj=tmerc +lat_0=0 +lon_0=-111 +k=1 +x_0=42500000 +y_0=0 +ellps=krass +towgs84=23.92,-141.27,-80.9,-0,0.35,0.82,-0.12 +units=m +no_defs");
        _projParams.put(28443, "+proj=tmerc +lat_0=0 +lon_0=-105 +k=1 +x_0=43500000 +y_0=0 +ellps=krass +towgs84=23.92,-141.27,-80.9,-0,0.35,0.82,-0.12 +units=m +no_defs");
        _projParams.put(28444, "+proj=tmerc +lat_0=0 +lon_0=-99 +k=1 +x_0=44500000 +y_0=0 +ellps=krass +towgs84=23.92,-141.27,-80.9,-0,0.35,0.82,-0.12 +units=m +no_defs");
        _projParams.put(28445, "+proj=tmerc +lat_0=0 +lon_0=-93 +k=1 +x_0=45500000 +y_0=0 +ellps=krass +towgs84=23.92,-141.27,-80.9,-0,0.35,0.82,-0.12 +units=m +no_defs");
        _projParams.put(28446, "+proj=tmerc +lat_0=0 +lon_0=-87 +k=1 +x_0=46500000 +y_0=0 +ellps=krass +towgs84=23.92,-141.27,-80.9,-0,0.35,0.82,-0.12 +units=m +no_defs");
        _projParams.put(28447, "+proj=tmerc +lat_0=0 +lon_0=-81 +k=1 +x_0=47500000 +y_0=0 +ellps=krass +towgs84=23.92,-141.27,-80.9,-0,0.35,0.82,-0.12 +units=m +no_defs");
        _projParams.put(28448, "+proj=tmerc +lat_0=0 +lon_0=-75 +k=1 +x_0=48500000 +y_0=0 +ellps=krass +towgs84=23.92,-141.27,-80.9,-0,0.35,0.82,-0.12 +units=m +no_defs");
        _projParams.put(28449, "+proj=tmerc +lat_0=0 +lon_0=-69 +k=1 +x_0=49500000 +y_0=0 +ellps=krass +towgs84=23.92,-141.27,-80.9,-0,0.35,0.82,-0.12 +units=m +no_defs");
        _projParams.put(28450, "+proj=tmerc +lat_0=0 +lon_0=-63 +k=1 +x_0=50500000 +y_0=0 +ellps=krass +towgs84=23.92,-141.27,-80.9,-0,0.35,0.82,-0.12 +units=m +no_defs");
        _projParams.put(28451, "+proj=tmerc +lat_0=0 +lon_0=-57 +k=1 +x_0=51500000 +y_0=0 +ellps=krass +towgs84=23.92,-141.27,-80.9,-0,0.35,0.82,-0.12 +units=m +no_defs");
        _projParams.put(28452, "+proj=tmerc +lat_0=0 +lon_0=-51 +k=1 +x_0=52500000 +y_0=0 +ellps=krass +towgs84=23.92,-141.27,-80.9,-0,0.35,0.82,-0.12 +units=m +no_defs");
        _projParams.put(28453, "+proj=tmerc +lat_0=0 +lon_0=-45 +k=1 +x_0=53500000 +y_0=0 +ellps=krass +towgs84=23.92,-141.27,-80.9,-0,0.35,0.82,-0.12 +units=m +no_defs");
        _projParams.put(28454, "+proj=tmerc +lat_0=0 +lon_0=-39 +k=1 +x_0=54500000 +y_0=0 +ellps=krass +towgs84=23.92,-141.27,-80.9,-0,0.35,0.82,-0.12 +units=m +no_defs");
        _projParams.put(28455, "+proj=tmerc +lat_0=0 +lon_0=-33 +k=1 +x_0=55500000 +y_0=0 +ellps=krass +towgs84=23.92,-141.27,-80.9,-0,0.35,0.82,-0.12 +units=m +no_defs");
        _projParams.put(28456, "+proj=tmerc +lat_0=0 +lon_0=-27 +k=1 +x_0=56500000 +y_0=0 +ellps=krass +towgs84=23.92,-141.27,-80.9,-0,0.35,0.82,-0.12 +units=m +no_defs");
        _projParams.put(28457, "+proj=tmerc +lat_0=0 +lon_0=-21 +k=1 +x_0=57500000 +y_0=0 +ellps=krass +towgs84=23.92,-141.27,-80.9,-0,0.35,0.82,-0.12 +units=m +no_defs");
        _projParams.put(28458, "+proj=tmerc +lat_0=0 +lon_0=-15 +k=1 +x_0=58500000 +y_0=0 +ellps=krass +towgs84=23.92,-141.27,-80.9,-0,0.35,0.82,-0.12 +units=m +no_defs");
        _projParams.put(28459, "+proj=tmerc +lat_0=0 +lon_0=-9 +k=1 +x_0=59500000 +y_0=0 +ellps=krass +towgs84=23.92,-141.27,-80.9,-0,0.35,0.82,-0.12 +units=m +no_defs");
        _projParams.put(28460, "+proj=tmerc +lat_0=0 +lon_0=-3 +k=1 +x_0=60500000 +y_0=0 +ellps=krass +towgs84=23.92,-141.27,-80.9,-0,0.35,0.82,-0.12 +units=m +no_defs");
    }
    public static Map<Integer, String> getProjParams() {
        return _projParams;
    }

    /**
     * Try detect SRID of SXF by they passport by constants
     * @param sxfPassport passport of SXF
     * @return detected SRID or 0 if not detected
     */
    public static int detectSRID(SXFPassport sxfPassport) {
        if (sxfPassport.mapType == MapType.LATLONG &&
                sxfPassport.ellipsoidKind == EllipsoidKind.WGS_84 &&
                sxfPassport.coordinateSystem == CoordinateSystem.GEOCOORDINATE &&
//                sxfPassport.frameKind == FrameKind.FREE &&
                sxfPassport.planeUnit == Unit.METRE &&
                sxfPassport.materialProjection == MapProjection.LATITUDELONGITUDE) {
            return 4326;
        }
        if (sxfPassport.mapType == MapType.MERCATOR &&
                (sxfPassport.ellipsoidKind == EllipsoidKind.WGS_84 || sxfPassport.ellipsoidKind == EllipsoidKind.SPHERE_WGS_84) &&
                sxfPassport.coordinateSystem == CoordinateSystem.CONDITION &&
//                sxfPassport.frameKind == FrameKind.FREE &&
                sxfPassport.planeUnit == Unit.METRE &&
                sxfPassport.materialProjection == MapProjection.WEBMERCATOR) {
            return 3857;
        }
        if (sxfPassport.mapType == MapType.TOPOGRAPHIC &&
                sxfPassport.ellipsoidKind == EllipsoidKind.KRASOVSKY42 &&
                sxfPassport.coordinateSystem == CoordinateSystem.ORTHOGONAL &&
//                sxfPassport.frameKind == FrameKind.TRAPEZECURVE &&
                sxfPassport.planeUnit == Unit.METRE &&
                sxfPassport.materialProjection == MapProjection.GAUSSCONFORMAL) {
            // sxfPassport.heightSystem == HeightSystem.BALTIC // Try skip height system
            int zone = sxfPassport.getZone();
            return 28400 + zone;
        }
        return 0;
    }

    public static String geometryAsWKT(Geometry geometry) {
        return geometryAsWKT(geometry, false);
    }

    public static String geometryAsWKT(Geometry geometry, boolean isFormatted) {
        WKTWriter wktWriter = new WKTWriter(3);
        wktWriter.setFormatted(isFormatted);
        return wktWriter.write(geometry);
    }

    public static String geometryAsEWKT(Geometry geometry) {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("SRID=");
        stringBuilder.append(geometry.getSRID());
        stringBuilder.append(";");
        stringBuilder.append(geometryAsWKT(geometry));
        return stringBuilder.toString();
    }

    public static String geometryAsWKB(Geometry geometry) {
        WKBWriter wkbWriter = new WKBWriter(3, true);
        byte[] hex = wkbWriter.write(geometry);
        String result = WKBWriter.toHex(hex);
        if (geometry.isEmpty()) {
            if (result.charAt(8) != 'A') {
                StringBuilder sb = new StringBuilder(result);
                sb.setCharAt(8, 'A');
                result = sb.toString();
            }
        }
        return result;
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
