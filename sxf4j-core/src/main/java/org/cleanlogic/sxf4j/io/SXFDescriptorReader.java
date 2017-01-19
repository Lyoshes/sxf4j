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
import org.cleanlogic.sxf4j.enums.Projection;
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
    private final SXFPassport _sxfPassport;
    private final MappedByteBuffer _mappedByteBuffer;
    private SXFDescriptor _sxfDescriptor;

    SXFDescriptorReader(SXFPassport sxfPassport) {
        _sxfPassport = sxfPassport;
        _mappedByteBuffer = sxfPassport.getMappedByteBuffer();
    }

    SXFDescriptor read() {
        // Set to position of descriptor data
        if (_mappedByteBuffer.position() != _sxfPassport.length) {
            _mappedByteBuffer.position(_sxfPassport.length);
        }
        int identifier = _mappedByteBuffer.getInt();
        if (identifier != SXF.DAT_SXF) {
            throw new SXFDescriptorReadError(String.format("Identifier by offset %d not is 0x%08x", SXF.DAT_SXF));
        }

        _sxfDescriptor = new SXFDescriptor();
        _sxfDescriptor.identifier = identifier;
        if (_sxfPassport.version == SXF.VERSION_3) {
            readVersion3();
        } else if (_sxfPassport.version == SXF.VERSION_4) {
            readVersion4();
            // Set data projection flag in passport
            _sxfPassport.projectionFlag = _sxfDescriptor.projectionFlag;
        }

        int total = _sxfPassport.length + _sxfDescriptor.length;

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
        _sxfDescriptor.projectionFlag = Projection.fromValue((infoFlags[0] >> 2) & 0x1);
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
        _sxfDescriptor.projectionFlag = Projection.fromValue((infoFlags[0] >> 2) & 0x1);
        _sxfDescriptor.realPlaceFlag = (infoFlags[0] >> 3) & 0x3;
        _sxfDescriptor.codeTypeFlag = (infoFlags[0] >> 5) & 0x3;
        _sxfDescriptor.generalizationFlag = (infoFlags[0] >> 7) & 0x1;
        _sxfDescriptor.textEncodingFlag = TextEncoding.fromValue(infoFlags[1]);
        _sxfDescriptor.secrecy = Secrecy.fromValue(infoFlags[2]);

        // Reserve
        _mappedByteBuffer.getInt();
    }
}
