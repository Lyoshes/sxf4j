package org.cleanlogic.sxf4j.io;

import org.cleanlogic.sxf4j.format.SXFDescriptor;
import org.cleanlogic.sxf4j.format.SXFPassport;
import org.cleanlogic.sxf4j.format.SXFRecord;
import org.cleanlogic.sxf4j.format.SXFRecordHeader;

import java.nio.MappedByteBuffer;
import java.util.ArrayList;
import java.util.List;

/**
 * @author Serge Silaev aka iSergio <s.serge.b@gmail.com>
 */
class SXFRecordReader {
    private final SXFPassport _sxfPassport;
    private final SXFDescriptor _sxfDescriptor;
    private List<SXFRecord> _sxfRecords = new ArrayList<>();

    SXFRecordReader(SXFPassport sxfPassport, SXFDescriptor sxfDescriptor) {
        _sxfPassport = sxfPassport;
        _sxfDescriptor = sxfDescriptor;
    }

    /**
     * For read records need {@link SXFPassport} and {@link SXFDescriptor} because that method is package-private.
     * @return List of {@link SXFRecord}.
     */
    List<SXFRecord> read() {
        _sxfRecords.clear();

        SXFRecordHeaderReader sxfRecordHeaderReader = new SXFRecordHeaderReader(_sxfPassport, _sxfDescriptor);
        List<SXFRecordHeader> sxfRecordHeaders = sxfRecordHeaderReader.read();

        for (SXFRecordHeader sxfRecordHeader : sxfRecordHeaders) {
            SXFRecord sxfRecord = new SXFRecord(_sxfPassport);
            sxfRecord.setHeader(sxfRecordHeader);
            _sxfRecords.add(sxfRecord);
        }
        return _sxfRecords;
    }
}
