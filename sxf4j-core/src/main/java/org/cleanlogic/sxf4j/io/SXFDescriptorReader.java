package org.cleanlogic.sxf4j.io;

import org.cleanlogic.sxf4j.SXF;
import org.cleanlogic.sxf4j.enums.Secrecy;
import org.cleanlogic.sxf4j.enums.TextEncoding;
import org.cleanlogic.sxf4j.exceptions.SXFDescriptorReadError;
import org.cleanlogic.sxf4j.format.SXFDescriptor;
import org.cleanlogic.sxf4j.format.SXFPassport;

import java.nio.MappedByteBuffer;

/**
 * @author Serge Silaev aka iSergio <s.serge.b@gmail.com>
 */
class SXFDescriptorReader {
    private MappedByteBuffer _mappedByteBuffer;
    private  SXFDescriptor _sxfDescriptor;

    SXFDescriptorReader(MappedByteBuffer mappedByteBuffer) {
        _mappedByteBuffer = mappedByteBuffer;
    }

    SXFDescriptor read(SXFPassport sxfPassport) {
        // Set to position of descriptor data
        if (_mappedByteBuffer.position() != sxfPassport.length) {
            _mappedByteBuffer.position(sxfPassport.length);
        }
        int identifier = _mappedByteBuffer.getInt();
        if (identifier != SXF.DAT_SXF) {
            throw new SXFDescriptorReadError(String.format("Identifier by offset %d not is 0x%08x", SXF.DAT_SXF));
        }

        _sxfDescriptor = new SXFDescriptor();
        _sxfDescriptor.identifier = identifier;
        if (sxfPassport.version == SXF.VERSION_3) {
            readVersion3();
        } else if (sxfPassport.version == SXF.VERSION_4) {
            readVersion4();
            // Set data projection flag in passport
            sxfPassport.dataProjectionFlag = _sxfDescriptor.dataProjectionFlag;
        }

        int total = sxfPassport.length + _sxfDescriptor.length;

        if (total != _mappedByteBuffer.position()) {
            throw new SXFDescriptorReadError(String.format("Error length of readed data. Readed: %d. Must be: %d", _mappedByteBuffer.position(), total));
        }

        return _sxfDescriptor;
    }

    private void readVersion3() {
        _sxfDescriptor.length = _mappedByteBuffer.getInt();
        if (_sxfDescriptor.length != SXF.DAT_3_LENGTH) {
            throw new SXFDescriptorReadError(String.format("SXF descriptor of version 0x%08x not equals %d", SXF.VERSION_3, SXF.DAT_3_LENGTH));
        }
        byte[] nomenclature = new byte[24];
        _mappedByteBuffer.get(nomenclature);
        _sxfDescriptor.nomenclature = new String(nomenclature);
        _sxfDescriptor.recordCount = _mappedByteBuffer.getInt();

        byte[] infoFlags = new byte[4];
        _mappedByteBuffer.get(infoFlags);
        _sxfDescriptor.conditionFlag = infoFlags[0] & 0x3;
        _sxfDescriptor.dataProjectionFlag = ((infoFlags[0] >> 2) & 0x1) == 1;
        _sxfDescriptor.realPlaceFlag = (infoFlags[0] >> 3) & 0x3;
        _sxfDescriptor.codeTypeFlag = (infoFlags[0] >> 5) & 0x3;
        _sxfDescriptor.generalizationFlag = (infoFlags[0] >> 7) & 0x1;

        _sxfDescriptor.code = _mappedByteBuffer.getInt();
    }

    private void readVersion4() {
        _sxfDescriptor.length = _mappedByteBuffer.getInt();
        if (_sxfDescriptor.length != SXF.DAT_4_LENGTH) {
            throw new SXFDescriptorReadError(String.format("SXF descriptor of version 0x%08x not equals %d", SXF.VERSION_4, SXF.DAT_4_LENGTH));
        }
        byte[] nomenclature = new byte[32];
        _mappedByteBuffer.get(nomenclature);
        _sxfDescriptor.nomenclature = new String(nomenclature);
        _sxfDescriptor.recordCount = _mappedByteBuffer.getInt();

        byte[] infoFlags = new byte[4];
        _mappedByteBuffer.get(infoFlags);
        _sxfDescriptor.conditionFlag = infoFlags[0] & 0x3;
        _sxfDescriptor.dataProjectionFlag = ((infoFlags[0] >> 2) & 0x1) == 1;
        _sxfDescriptor.realPlaceFlag = (infoFlags[0] >> 3) & 0x3;
        _sxfDescriptor.codeTypeFlag = (infoFlags[0] >> 5) & 0x3;
        _sxfDescriptor.generalizationFlag = (infoFlags[0] >> 7) & 0x1;
        _sxfDescriptor.textEncodingFlag = TextEncoding.fromValue(infoFlags[1]);
        _sxfDescriptor.secrecy = Secrecy.fromValue(infoFlags[2]);

        // Reserve
        _mappedByteBuffer.getInt();
    }
}
