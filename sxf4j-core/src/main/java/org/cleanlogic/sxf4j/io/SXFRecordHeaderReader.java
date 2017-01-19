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

import org.cleanlogic.sxf4j.SXF;
import org.cleanlogic.sxf4j.enums.FrameOut;
import org.cleanlogic.sxf4j.enums.Local;
import org.cleanlogic.sxf4j.enums.MetricElementSize;
import org.cleanlogic.sxf4j.enums.Spline;
import org.cleanlogic.sxf4j.fixes.SXFPassportFixes;
import org.cleanlogic.sxf4j.format.SXFDescriptor;
import org.cleanlogic.sxf4j.format.SXFPassport;
import org.cleanlogic.sxf4j.format.SXFRecordHeader;

import java.nio.BufferUnderflowException;
import java.nio.MappedByteBuffer;
import java.util.ArrayList;
import java.util.List;

/**
 * @author Serge Silaev aka iSergio <s.serge.b@gmail.com>
 */
class SXFRecordHeaderReader {
    private final MappedByteBuffer _mappedByteBuffer;
    private final SXFPassport _sxfPassport;
    private final SXFDescriptor _sxfDescriptor;

    private List<SXFRecordHeader> _sxfRecordHeaders = new ArrayList<>();

    private SXFRecordHeader _borderRecordHeader;

    SXFRecordHeaderReader(SXFPassport sxfPassport, SXFDescriptor sxfDescriptor) {
        _mappedByteBuffer = sxfPassport.getMappedByteBuffer();
        _sxfPassport = sxfPassport;
        _sxfDescriptor = sxfDescriptor;
    }

    List<SXFRecordHeader> read() {
        // Set position of first record header.
        if (_sxfPassport.version == SXF.VERSION_3) {
            readVersion3();
        } else if (_sxfPassport.version == SXF.VERSION_4) {
            readVersion4();
        }

        return _sxfRecordHeaders;
    }

    private void readVersion3() {
        _mappedByteBuffer.position(SXF.PASSPORT_3_LENGHT + SXF.DAT_3_LENGTH);
        while (_mappedByteBuffer.position() < _mappedByteBuffer.limit()) {
            try {
                int identifier = _mappedByteBuffer.getInt();
                if (_mappedByteBuffer.limit() - _mappedByteBuffer.position() <= 0) {
                    break;
                }
                if (identifier != SXF.RECORD_SXF) {
                    if (!_sxfPassport.getReaderOptions().quite) {
                        System.out.printf("Warning!!! Wrong identifier of record. Try fix it and find next identifier. position: %d from %d\n", _mappedByteBuffer.position(), _mappedByteBuffer.limit());
                    }
                    while (identifier != SXF.RECORD_SXF && (_mappedByteBuffer.limit() < _mappedByteBuffer.position())) {
                        identifier = _mappedByteBuffer.getInt();
                    }
                    if (identifier != SXF.RECORD_SXF) {
                        continue;
                    }
                }
                SXFRecordHeader sxfRecordHeader = new SXFRecordHeader(_sxfPassport.version);
                sxfRecordHeader.identifier = identifier;
                sxfRecordHeader.length = _mappedByteBuffer.getInt();
                sxfRecordHeader.metricLength = _mappedByteBuffer.getInt();
                sxfRecordHeader.excode = _mappedByteBuffer.getInt();
                sxfRecordHeader.number = _mappedByteBuffer.getInt();

                byte[] infoFlags = new byte[3];
                _mappedByteBuffer.get(infoFlags);
                sxfRecordHeader.local = Local.fromValue(infoFlags[0] & 0xF);
                sxfRecordHeader.frameOut = FrameOut.fromValue((infoFlags[0] >> 4) & 0xF);

                sxfRecordHeader.isClose = (infoFlags[1] & 0x1) == 1;
                sxfRecordHeader.isSemantic = ((infoFlags[1] >> 1) & 0x1) == 1;
                int metricElementSize = ((infoFlags[1] >> 2) & 0x1);
                sxfRecordHeader.isGroup = ((infoFlags[1] >> 3) & 0x1) == 1;

                sxfRecordHeader.metricFormat = (infoFlags[2] & 0x1);
                sxfRecordHeader.is3D = ((infoFlags[2] >> 1) & 0x1) == 1;
                sxfRecordHeader.isFloat = ((infoFlags[2] >> 2) & 0x1) == 1;
                sxfRecordHeader.isText = ((infoFlags[2] >> 3) & 0x1) == 1;
                sxfRecordHeader.metricElementSize = getMetricElementSizeEnum(metricElementSize, sxfRecordHeader.isFloat);

                byte generalization = _mappedByteBuffer.get();
                sxfRecordHeader.bottom = generalization & 0xF;
                sxfRecordHeader.upper = (generalization >> 4) & 0xF;

                sxfRecordHeader.groupNumber = _mappedByteBuffer.getInt();

                sxfRecordHeader.metricDescription = _mappedByteBuffer.getInt();
                sxfRecordHeader.subjectCount = sxfRecordHeader.metricDescription & 0xFFFF;//_mappedByteBuffer.getShort();
                sxfRecordHeader.pointCount = (sxfRecordHeader.metricDescription >> 16) & 0xFFFF;//_mappedByteBuffer.getShort();

                sxfRecordHeader.metricOffset = _mappedByteBuffer.position();
                sxfRecordHeader.semanticOffset = sxfRecordHeader.metricOffset + sxfRecordHeader.metricLength;

                _mappedByteBuffer.position(_mappedByteBuffer.position() + sxfRecordHeader.length - 32);

                // Set coordinate offset by border record
                if (sxfRecordHeader.excode == _sxfPassport.borderExcode) {
                    SXFPassportFixes.FisxBorderRecordOffset(_sxfPassport, sxfRecordHeader);
                }

                _sxfRecordHeaders.add(sxfRecordHeader);
            } catch (BufferUnderflowException ex) {
                break;
            }
        }

        if (_sxfDescriptor.recordCount != _sxfRecordHeaders.size()) {
            if (!_sxfPassport.getReaderOptions().quite) {
                System.out.printf("Warning!!! Descriptor record count is %d, but readed records is %d\n", _sxfDescriptor.recordCount, _sxfRecordHeaders.size());
            }
        }
    }

    private void readVersion4() {
        _mappedByteBuffer.position(SXF.PASSPORT_4_LENGHT + SXF.DAT_4_LENGTH);
        while (_mappedByteBuffer.position() < _mappedByteBuffer.limit()) {
            try {
                int identifier = _mappedByteBuffer.getInt();
                if (_mappedByteBuffer.limit() - _mappedByteBuffer.position() <= 0) {
                    break;
                }
                if (identifier != SXF.RECORD_SXF) {
                    if (!_sxfPassport.getReaderOptions().quite) {
                        System.out.printf("Warning!!! Wrong identifier of record. Try fix it and find next identifier. position: %d from %d\n", _mappedByteBuffer.position(), _mappedByteBuffer.limit());
                    }
                    while (identifier != SXF.RECORD_SXF && (_mappedByteBuffer.limit() < _mappedByteBuffer.position())) {
                        identifier = _mappedByteBuffer.getInt();
                    }
                    if (identifier != SXF.RECORD_SXF) {
                        continue;
                    }
                }

                SXFRecordHeader sxfRecordHeader = new SXFRecordHeader(_sxfPassport.version);
                sxfRecordHeader.identifier = identifier;
                sxfRecordHeader.length = _mappedByteBuffer.getInt();
                sxfRecordHeader.metricLength = _mappedByteBuffer.getInt();
                sxfRecordHeader.excode = _mappedByteBuffer.getInt();
                sxfRecordHeader.number = _mappedByteBuffer.getInt();

                byte[] infoFlags = new byte[3];
                _mappedByteBuffer.get(infoFlags);

                sxfRecordHeader.local = Local.fromValue(infoFlags[0] & 0xF);
                sxfRecordHeader.isMultiPolygon = ((infoFlags[0] >> 4) & 0x1) == 1;

                sxfRecordHeader.isCompressible = (infoFlags[1] & 0x1) == 1;
                sxfRecordHeader.isSemantic = ((infoFlags[1] >> 1) & 0x1) == 1;
                int metricElementSize = ((infoFlags[1] >> 2) & 0x1);
                sxfRecordHeader.isVector = ((infoFlags[1] >> 3) & 0x1) == 1;
                sxfRecordHeader.isUnicode = ((infoFlags[1] >> 4) & 0x1) == 1;
                sxfRecordHeader.isAbove = ((infoFlags[1] >> 5) & 0x1) == 1;
                sxfRecordHeader.isBelow = ((infoFlags[1] >> 6) & 0x1) == 1;
                sxfRecordHeader.isVAlign = ((infoFlags[1] >> 7) & 0x1) == 1;

                sxfRecordHeader.metricFormat = (infoFlags[2] & 0x1);
                sxfRecordHeader.is3D = ((infoFlags[2] >> 1) & 0x1) == 1;
                sxfRecordHeader.isFloat = ((infoFlags[2] >> 2) & 0x1) == 1;
                sxfRecordHeader.isText = ((infoFlags[2] >> 3) & 0x1) == 1;
                sxfRecordHeader.isGraphic = ((infoFlags[2] >> 4) & 0x1) == 1;
                sxfRecordHeader.isGraphicScale = ((infoFlags[2] >> 5) & 0x1) == 1;
                sxfRecordHeader.spline = Spline.fromValue((infoFlags[2] >> 6) & 0x3);
                sxfRecordHeader.metricElementSize = getMetricElementSizeEnum(metricElementSize, sxfRecordHeader.isFloat);

                byte generalization = _mappedByteBuffer.get();
                sxfRecordHeader.bottom = generalization & 0xF;
                sxfRecordHeader.upper = (generalization >> 4) & 0xF;

                sxfRecordHeader.bigRecordPointCount = _mappedByteBuffer.getInt();

                sxfRecordHeader.metricDescription = _mappedByteBuffer.getInt();
                sxfRecordHeader.subjectCount = sxfRecordHeader.metricDescription & 0xFFFF;//_mappedByteBuffer.getShort();
                sxfRecordHeader.pointCount = (sxfRecordHeader.metricDescription >> 16) & 0xFFFF;//_mappedByteBuffer.getShort();

                sxfRecordHeader.metricOffset = _mappedByteBuffer.position();
                sxfRecordHeader.semanticOffset = sxfRecordHeader.metricOffset + sxfRecordHeader.metricLength;

                _mappedByteBuffer.position(_mappedByteBuffer.position() + sxfRecordHeader.length - 32);

                // Set coordinate offset by border record
                if (sxfRecordHeader.excode == _sxfPassport.borderExcode) {
                    SXFPassportFixes.FisxBorderRecordOffset(_sxfPassport, sxfRecordHeader);
                }

                _sxfRecordHeaders.add(sxfRecordHeader);
            } catch (BufferUnderflowException ex) {
                break;
            }
        }

        if (_sxfDescriptor.recordCount != _sxfRecordHeaders.size()) {
            if (!_sxfPassport.getReaderOptions().quite) {
                System.out.printf("Warning!!! Descriptor record count is %d, but readed records is %d\n", _sxfDescriptor.recordCount, _sxfRecordHeaders.size());
            }
        }
    }

    private MetricElementSize getMetricElementSizeEnum(int metricElementSize, boolean isFloat) {
        MetricElementSize metricElementSizeEnum = null;
        switch (metricElementSize) {
            case 0:
                if (!isFloat) {
                    metricElementSizeEnum = MetricElementSize.SHORT;
                } else {
                    metricElementSizeEnum = MetricElementSize.FLOAT;
                }
                break;
            case 1:
                if (!isFloat) {
                    metricElementSizeEnum = MetricElementSize.INT;
                } else {
                    metricElementSizeEnum = MetricElementSize.DOUBLE;
                }
                break;
            default: break;
        }
        return metricElementSizeEnum;
    }
}
