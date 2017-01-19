package org.cleanlogic.sxf4j.format;

import com.vividsolutions.jts.geom.Geometry;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Serge Silaev aka iSergio <s.serge.b@gmail.com>
 */
public class SXFRecordMetric {
    private final Geometry _srcGeometry;
    private final Geometry _dstGeometry;

    public List<SXFRecordMetricText> metricTexts = new ArrayList<>();

    public SXFRecordMetric(Geometry srcGeometry, Geometry dstGeometry) {
        _srcGeometry = srcGeometry;
        _dstGeometry = dstGeometry;
    }

    public Geometry getSrcGeometry() {
        return _srcGeometry;
    }

    public Geometry getDstGeometry() {
        return _dstGeometry;
    }

    /**
     * Function return optimal geometry. If source geometry reprojected - returns it, else returns source geometry
     * @return
     */
    public Geometry getGeometry() {
        if (_dstGeometry != null) {
            return _dstGeometry;
        }
        return _srcGeometry;
    }
}
