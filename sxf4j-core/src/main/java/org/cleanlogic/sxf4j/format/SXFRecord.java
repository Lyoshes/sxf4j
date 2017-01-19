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

import org.cleanlogic.sxf4j.io.SXFRecordMetricReader;
import org.cleanlogic.sxf4j.io.SXFRecordSemanticReader;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Serge Silaev aka iSergio <s.serge.b@gmail.com>
 */
public class SXFRecord {
    private final SXFPassport _sxfPassport;
    private SXFRecordHeader _sxfRecordHeader;
    private SXFRecordMetric _sxfRecordMetric;
    private List<SXFRecordSemantic> _sxfRecordSemantics;

    public SXFRecord(SXFPassport sxfPassport) {
        _sxfPassport = sxfPassport;
    }

    public void setHeader(SXFRecordHeader sxfRecordHeader) {
        _sxfRecordHeader = sxfRecordHeader;
    }

    public SXFRecordHeader getHeader() {
        return _sxfRecordHeader;
    }

    public SXFRecordMetric getMetric() {
        if (_sxfRecordMetric != null) {
            return _sxfRecordMetric;
        }

        SXFRecordMetricReader sxfRecordMetricReader = new SXFRecordMetricReader(_sxfPassport);
        _sxfRecordMetric = sxfRecordMetricReader.read(_sxfRecordHeader);

        return _sxfRecordMetric;
    }

    public List<SXFRecordSemantic> getSemantics() {
        if (_sxfRecordSemantics != null) {
            return _sxfRecordSemantics;
        }

        if (!_sxfRecordHeader.isSemantic) {
            _sxfRecordSemantics = new ArrayList<>();
        } else {
            SXFRecordSemanticReader sxfRecordSemanticReader = new SXFRecordSemanticReader(_sxfPassport.getMappedByteBuffer());
            _sxfRecordSemantics = sxfRecordSemanticReader.read(_sxfRecordHeader);
        }

        return _sxfRecordSemantics;
    }

    public void destroy() {
        _sxfRecordHeader = null;
        _sxfRecordMetric = null;
        if (_sxfRecordSemantics != null) {
            for (SXFRecordSemantic sxfRecordSemantic : _sxfRecordSemantics) {
                sxfRecordSemantic = null;
            }
        }
    }
}
