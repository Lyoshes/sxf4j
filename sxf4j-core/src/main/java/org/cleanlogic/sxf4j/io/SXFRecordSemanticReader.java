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

import org.cleanlogic.sxf4j.enums.SemanticType;
import org.cleanlogic.sxf4j.enums.TextEncoding;
import org.cleanlogic.sxf4j.fixes.SXFRecordSemanticFix;
import org.cleanlogic.sxf4j.format.SXFRecordHeader;
import org.cleanlogic.sxf4j.format.SXFRecordSemantic;

import java.nio.MappedByteBuffer;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;

/**
 * @author Serge Silaev aka iSergio <s.serge.b@gmail.com>
 */
public class SXFRecordSemanticReader {
    private final MappedByteBuffer _mappedByteBuffer;

    public SXFRecordSemanticReader(MappedByteBuffer mappedByteBuffer) {
        _mappedByteBuffer = mappedByteBuffer;
    }

    public List<SXFRecordSemantic> read(SXFRecordHeader sxfRecordHeader) {
        List<SXFRecordSemantic> sxfRecordSemantics = new ArrayList<>();

        int totalBytes = sxfRecordHeader.length - sxfRecordHeader.metricLength - 32;

        _mappedByteBuffer.position((int) sxfRecordHeader.semanticOffset);

        while (totalBytes > 0) {
            SXFRecordSemantic sxfRecordSemantic = new SXFRecordSemantic();

            sxfRecordSemantic.code = _mappedByteBuffer.getShort();
            byte[] lengthCode = new byte[2];
            _mappedByteBuffer.get(lengthCode);
            int type = lengthCode[0];
            int scale = lengthCode[1];
            // Bytes left to read
            totalBytes -= 4;

            if (type == 128 && scale == 255) {
                // This is dynamic length string
                int length = _mappedByteBuffer.getInt();
                // Bytes left to read
                totalBytes -= 4;
                byte[] string = new byte[length];
                _mappedByteBuffer.get(string);
                // Bytes left to read
                totalBytes -= length;
                sxfRecordSemantic.type = SemanticType.fromValue(type);
                sxfRecordSemantic.scale = scale;
                sxfRecordSemantic.value = new String(string).trim().intern();
            } else {
                sxfRecordSemantic.type = SemanticType.fromValue(type);
                sxfRecordSemantic.scale = scale;
                if (sxfRecordSemantic.type == null) {
                    break;
                }

                if (sxfRecordSemantic.type == SemanticType.CHAR || sxfRecordSemantic.type == SemanticType.SHORT ||
                        sxfRecordSemantic.type == SemanticType.DOUBLE || sxfRecordSemantic.type == SemanticType.LONG) {
                    if (sxfRecordSemantic.scale < -127 || sxfRecordSemantic.scale > 127) {
                        break;
                    }
                } else {
                    if (sxfRecordSemantic.scale > 255) {
                        break;
                    } else if (sxfRecordSemantic.scale + 1 < 0) {
                        break;
                    }
                }

                switch (sxfRecordSemantic.type) {
                    case STRDOS:
                    case STRING:
                    case STRUNI: {
                        Charset charset;
                        switch (sxfRecordSemantic.type) {
                            case STRDOS:
                                charset = Charset.forName(TextEncoding.IBM866.getName());
                                break;
                            case STRING:
                                charset = Charset.forName(TextEncoding.CP1251.getName());
                                break;
                            case STRUNI:
                                charset = Charset.forName(TextEncoding.KOI8R.getName());
                                break;
                            default:
                                charset = Charset.forName("UTF-8");
                        }
                        byte[] string = new byte[sxfRecordSemantic.scale + 1];
                        _mappedByteBuffer.get(string);
                        String value = new String(string, charset).trim().intern();
                        if (sxfRecordSemantic.type == SemanticType.STRUNI) {
                            value = SXFRecordSemanticFix.RemoveBinaryZero(value.getBytes(charset));
                            value = value.intern();
                        }
                        sxfRecordSemantic.value = value;
                        totalBytes -= (sxfRecordSemantic.scale + 1);
                    }
                    break;
                    case CHAR: {
                        byte value = _mappedByteBuffer.get();
                        sxfRecordSemantic.value = String.valueOf((int) value * Math.pow(10., sxfRecordSemantic.scale)).intern();
                        totalBytes -= 1;
                    }
                    break;
                    case SHORT: {
                        short value = _mappedByteBuffer.getShort();
                        sxfRecordSemantic.value = String.valueOf(value * Math.pow(10., sxfRecordSemantic.scale)).intern();
                        totalBytes -= 2;
                    }
                    break;
                    case LONG: {
                        int value = _mappedByteBuffer.getInt();
                        sxfRecordSemantic.value = String.valueOf(value * Math.pow(10., sxfRecordSemantic.scale)).intern();
                        totalBytes -= 4;
                    }
                    break;
                    case DOUBLE: {
                        double value = _mappedByteBuffer.getDouble();
                        sxfRecordSemantic.value = String.valueOf(value * Math.pow(10., sxfRecordSemantic.scale)).intern();
                        totalBytes -= 8;
                    }
                    break;
                    default:
                        break;
                }
            }
            sxfRecordSemantics.add(sxfRecordSemantic);
        }

        return sxfRecordSemantics;
    }
}
