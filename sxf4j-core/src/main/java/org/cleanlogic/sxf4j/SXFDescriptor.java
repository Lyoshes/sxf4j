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

package org.cleanlogic.sxf4j;

import org.cleanlogic.sxf4j.enums.Projection;
import org.cleanlogic.sxf4j.enums.Secrecy;
import org.cleanlogic.sxf4j.enums.TextEncoding;

import java.io.IOException;
import java.nio.ByteBuffer;

/**
 * @author Serge Silaev aka iSergio <s.serge.b@gmail.com>
 */
public class SXFDescriptor {
    public static final int IDENTIFIER = 0x00544144;
    public static final int LENGTH_3 = 44;
    public static final int LENGTH_4 = 52;
    /**
     * Identifier of Description data.
     */
    private int identifier;
    /**
     * Length of Description
     */
    private int length;
    /**
     * Sheet nomenclature
     */
    private String nomenclature;
    /**
     * Record count
     */
    private int recordCount;
    /**
     * Flag of condition SXF. Must be 3
     */
    private int conditionFlag;
    /**
     * Projection of condition coordinates data.
     */
    private Projection projectionFlag;
    /**
     * Real coordinates.
     */
    private int realPlaceFlag;
    private int codeTypeFlag;
    /**
     * Table of generalization
     */
    private int generalizationFlag;
    /**
     * Only {@link SXFPassport#VERSION_4}
     */
    private TextEncoding textEncodingFlag;
    /**
     * Only {@link SXFPassport#VERSION_4}
     */
    private Secrecy secrecy;
    /**
     * Code of classificator
     * Only {@link SXFPassport#VERSION_3}
     */
    private int code;

    private final SXFPassport sxfPassport;

    public SXFDescriptor(SXFPassport sxfPassport) {
        this.sxfPassport = sxfPassport;
    }

    public int getRecordCount() {
        return recordCount;
    }
    /**
     * Function check SXFDescriptor magic number.
     * @param strict Show message through println or IOException.
     * @throws IOException exception if wrong.
     */
    private void checkIdentifier(boolean strict) throws IOException {
        if (identifier != IDENTIFIER) {
            String message = "Wrong identifier(magic number) of SXF Descriptor, expected " + IDENTIFIER + ", got " + identifier;
            if (!strict) {
                System.err.println(message);
            } else {
                throw new IOException(message);
            }
        }
    }

    private void checkLength(boolean strict) throws IOException {
        String message = "";
        if (sxfPassport.getVersion() == SXFPassport.VERSION_3 && length != LENGTH_3) {
            message = "Descriptor length for SXF passport version " + sxfPassport.getVersion() + " must be " + LENGTH_3 + ", got " + length;
        } else if (sxfPassport.getVersion() == SXFPassport.VERSION_4 && length != LENGTH_4) {
            message = "Descriptor length for SXF passport version " + sxfPassport.getVersion() + " must be " + LENGTH_4 + ", got " + length;
        }
        if (!message.isEmpty()) {
            if (!strict) {
                System.err.println(message);
            } else {
                throw new IOException(message);
            }
        }
    }

    public void print() {
        print(this);
    }

    public static void print(SXFDescriptor sxfDescriptor) {
        System.out.printf("Descriptor Info\n");
        System.out.printf("\tIdentifier:\t0x%08x\n", sxfDescriptor.identifier);
        System.out.printf("\tLength:\t\t%d\n", sxfDescriptor.length);
        System.out.printf("\tNomenclature:\t%s\n", sxfDescriptor.nomenclature);
        System.out.printf("\tRecord count:\t%d\n", sxfDescriptor.recordCount);
        System.out.printf("\tInfoFlags:\n");
        System.out.printf("\t\tConditionFlag:\t\t%d\n", sxfDescriptor.conditionFlag);
        System.out.printf("\t\tProjection flag:\t%s (%s)\n", sxfDescriptor.projectionFlag, sxfDescriptor.projectionFlag.getName());
        System.out.printf("\t\tRealCoordinatesFlag:\t%d\n", sxfDescriptor.realPlaceFlag);
        System.out.printf("\t\tCodeTypeFlag:\t\t%d\n", sxfDescriptor.codeTypeFlag);
        System.out.printf("\t\tGeneralizationFlag:\t%d\n", sxfDescriptor.generalizationFlag);
        if (sxfDescriptor.sxfPassport.getVersion() == SXFPassport.VERSION_4) {
            System.out.printf("\t\tTextEncodingFlag:\t%s (%s)\n", sxfDescriptor.textEncodingFlag, sxfDescriptor.textEncodingFlag.getName());
            System.out.printf("\tSecrecy:\t%s (%s)\n", sxfDescriptor.secrecy, sxfDescriptor.secrecy.getName());
        }
    }

    public int getLength() {
        return length;
    }

    public void read(ByteBuffer byteBuffer, boolean strict) throws IOException {
        byteBuffer.position(sxfPassport.getLength());

        identifier = byteBuffer.getInt();
        checkIdentifier(strict);

        length = byteBuffer.getInt();
        checkLength(strict);

        if (sxfPassport.getVersion() == SXFPassport.VERSION_3) {
            read3(byteBuffer, strict);
        } else if (sxfPassport.getVersion() == SXFPassport.VERSION_4) {
            read4(byteBuffer, strict);
        }

        sxfPassport.setProjectionFlag(projectionFlag);
    }

    private void read3(ByteBuffer byteBuffer, boolean strict) throws IOException {
        String textEncoding = TextEncoding.IBM866.getName();

        byte[] nomenclature = new byte[24];
        byteBuffer.get(nomenclature);
        this.nomenclature = new String(nomenclature, textEncoding).trim().intern();
        recordCount = byteBuffer.getInt();

        byte[] infoFlags = new byte[4];
        byteBuffer.get(infoFlags);
        conditionFlag = infoFlags[0] & 0x3;
        projectionFlag = Projection.fromValue((infoFlags[0] >> 2) & 0x1);
        realPlaceFlag = (infoFlags[0] >> 3) & 0x3;
        codeTypeFlag = (infoFlags[0] >> 5) & 0x3;
        generalizationFlag = (infoFlags[0] >> 7) & 0x1;

        code = byteBuffer.getInt();
    }

    private void read4(ByteBuffer byteBuffer, boolean strict) throws IOException {
        String textEncoding = TextEncoding.CP1251.getName();

        byte[] nomenclature = new byte[32];
        byteBuffer.get(nomenclature);
        this.nomenclature = new String(nomenclature, textEncoding).trim().intern();
        recordCount = byteBuffer.getInt();

        byte[] infoFlags = new byte[4];
        byteBuffer.get(infoFlags);
        conditionFlag = infoFlags[0] & 0x3;
        projectionFlag = Projection.fromValue((infoFlags[0] >> 2) & 0x1);
        realPlaceFlag = (infoFlags[0] >> 3) & 0x3;
        codeTypeFlag = (infoFlags[0] >> 5) & 0x3;
        generalizationFlag = (infoFlags[0] >> 7) & 0x1;
        textEncodingFlag = TextEncoding.fromValue(infoFlags[1]);
        secrecy = Secrecy.fromValue(infoFlags[2]);

        // Reserve
        byteBuffer.getInt();
    }
}
