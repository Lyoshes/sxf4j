package org.cleanlogic.sxf4j.format;

import com.vividsolutions.jts.geom.Geometry;
import com.vividsolutions.jts.io.WKBWriter;
import com.vividsolutions.jts.io.WKTWriter;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Serge Silaev aka iSergio <s.serge.b@gmail.com>
 */
public class SXFRecordMetric {
    public Geometry geometry;
    public List<SXFRecordMetricText> metricTexts = new ArrayList<>();

    public SXFRecordMetric() {}

    public String geometryAsWKB() {
        return geometryAsWKB(geometry);
    }

    public String geometryAsWKT() {
        return geometryAsWKT(geometry);
    }

    public String geometryAsWKT(boolean isFormatted) {
        return geometryAsWKT(geometry, isFormatted);
    }

    public String geometryAsEWKT() {
        return geometryAsEWKT(geometry);
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
}
