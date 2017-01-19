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
