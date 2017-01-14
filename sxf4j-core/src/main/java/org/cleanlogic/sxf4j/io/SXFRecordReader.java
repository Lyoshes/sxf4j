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
    private MappedByteBuffer _mappedByteBuffer;
    private final SXFReaderOptions _sxfReaderOptions;
    private List<SXFRecord> _sxfRecords = new ArrayList<>();

    /**
     * Default consructor for read records with default {@link SXFReaderOptions}.
     * @param mappedByteBuffer opened buffered access to SXF file.
     */
    SXFRecordReader(MappedByteBuffer mappedByteBuffer) {
        _mappedByteBuffer = mappedByteBuffer;
        _sxfReaderOptions = new SXFReaderOptions();
    }

    /**
     * Default consructor for read records.
     * @param mappedByteBuffer opened buffered access to SXF file.
     * @param sxfReaderOptions {@link SXFReaderOptions}.
     */
    SXFRecordReader(MappedByteBuffer mappedByteBuffer, SXFReaderOptions sxfReaderOptions) {
        _mappedByteBuffer = mappedByteBuffer;
        _sxfReaderOptions = sxfReaderOptions;
    }

    /**
     * For read records need {@link SXFPassport} and {@link SXFDescriptor} because that method is package-private.
     * @param sxfPassport Correct {@link SXFPassport}.
     * @param sxfDescriptor Correct {@link SXFDescriptor}.
     * @return List of {@link SXFRecord}.
     */
    List<SXFRecord> read(SXFPassport sxfPassport, SXFDescriptor sxfDescriptor) {
        _sxfRecords.clear();

        SXFRecordHeaderReader sxfRecordHeaderReader = new SXFRecordHeaderReader(_mappedByteBuffer, _sxfReaderOptions);
        List<SXFRecordHeader> sxfRecordHeaders = sxfRecordHeaderReader.read(sxfPassport, sxfDescriptor);

        for (SXFRecordHeader sxfRecordHeader : sxfRecordHeaders) {
            SXFRecord sxfRecord = new SXFRecord(_mappedByteBuffer, sxfPassport, _sxfReaderOptions);
            sxfRecord.setHeader(sxfRecordHeader);
            _sxfRecords.add(sxfRecord);
        }
        return _sxfRecords;
    }
}
