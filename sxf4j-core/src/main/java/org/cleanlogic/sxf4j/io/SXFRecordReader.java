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

package org.cleanlogic.sxf4j.io;

import org.cleanlogic.sxf4j.format.SXFDescriptor;
import org.cleanlogic.sxf4j.format.SXFPassport;
import org.cleanlogic.sxf4j.format.SXFRecord;
import org.cleanlogic.sxf4j.format.SXFRecordHeader;

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
