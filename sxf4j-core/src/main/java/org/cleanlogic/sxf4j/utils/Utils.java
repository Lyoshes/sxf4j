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

import java.io.File;
import java.util.List;

/**
 * @author Serge Silaev aka iSergio <s.serge.b@gmail.com>
 */
public class Utils {
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
