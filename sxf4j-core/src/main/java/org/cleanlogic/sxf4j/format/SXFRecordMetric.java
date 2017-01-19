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
