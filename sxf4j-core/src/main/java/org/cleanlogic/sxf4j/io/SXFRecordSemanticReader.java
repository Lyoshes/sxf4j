package org.cleanlogic.sxf4j.io;

import org.cleanlogic.sxf4j.enums.SemanticType;
import org.cleanlogic.sxf4j.enums.TextEncoding;
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
            sxfRecordSemantic.type = SemanticType.fromValue(lengthCode[0]);
            sxfRecordSemantic.scale = (int) lengthCode[1];
            totalBytes -= 4;

            switch (sxfRecordSemantic.type) {
                case STRDOS:
                case STRING:
                case STRUNI: {
                    Charset charset;
                    switch (sxfRecordSemantic.type) {
                        case STRDOS: charset = Charset.forName(TextEncoding.IBM866.getName()); break;
                        case STRING: charset = Charset.forName(TextEncoding.CP1251.getName()); break;
                        case STRUNI: charset = Charset.forName(TextEncoding.KOI8R.getName()); break;
                        default: charset = Charset.forName("UTF-8");
                    }

                    byte[] string = new byte[sxfRecordSemantic.scale + 1];
                    _mappedByteBuffer.get(string);
                    sxfRecordSemantic.value = new String(string, charset);
                    totalBytes -= (sxfRecordSemantic.scale + 1);
                } break;
                case CHAR: {
                    char value = _mappedByteBuffer.getChar();
                    sxfRecordSemantic.value = String.valueOf((int) value * Math.pow(10., sxfRecordSemantic.scale));
                    totalBytes -= 1;
                } break;
                case SHORT: {
                    short value = _mappedByteBuffer.getShort();
                    sxfRecordSemantic.value = String.valueOf(value * Math.pow(10., sxfRecordSemantic.scale));
                    totalBytes -= 2;
                } break;
                case LONG: {
                    int value = _mappedByteBuffer.getInt();
                    sxfRecordSemantic.value = String.valueOf(value * Math.pow(10., sxfRecordSemantic.scale));
                    totalBytes -= 4;
                } break;
                case DOUBLE: {
                    double value = _mappedByteBuffer.getDouble();
                    sxfRecordSemantic.value = String.valueOf(value * Math.pow(10., sxfRecordSemantic.scale));
                    totalBytes -= 8;
                } break;
                default: break;
            }
            sxfRecordSemantics.add(sxfRecordSemantic);
        }

        return sxfRecordSemantics;
    }
}
