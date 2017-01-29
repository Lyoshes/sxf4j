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

import com.vividsolutions.jts.geom.Geometry;
import com.vividsolutions.jts.geom.GeometryFactory;
import com.vividsolutions.jts.geom.PrecisionModel;

import java.io.File;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;
import java.nio.channels.ReadableByteChannel;
import java.util.ArrayList;
import java.util.List;

/**
 * Main class of access to SXF file format.
 * @author Serge Silaev aka iSergio <s.serge.b@gmail.com>
 */
public class SXFReader {
    private ReadableByteChannel readableByteChannel;
    private ByteBuffer buffer;

    private SXFPassport sxfPassport;
    private SXFDescriptor sxfDescriptor;

    private List<SXFRecord> sxfRecords = new ArrayList<>();

    /**
     * Factory for create geometries
     */
    private GeometryFactory geometryFactory;

    public SXFReader(File file) throws IOException {
        this(file, false, false);
    }

    public SXFReader(File file, boolean strict) throws IOException {
        this(file, strict, false);
    }

    /**
     * Constructor of SXF file format reader.
     * @param file file of SXF format for read
     * @param strict exceptions output.
     * @param findNext
     * @throws IOException
     */
    public SXFReader(File file, boolean strict, boolean findNext) throws IOException {
        RandomAccessFile raf = new RandomAccessFile(file, "r");
        readableByteChannel = raf.getChannel();
        buffer = ((FileChannel)readableByteChannel).map(FileChannel.MapMode.READ_ONLY, 0, ((FileChannel) readableByteChannel).size());

        sxfPassport = new SXFPassport();
        sxfPassport.read(buffer, strict);

        // Set srid for factory
        geometryFactory = new GeometryFactory(new PrecisionModel(PrecisionModel.FLOATING_SINGLE), sxfPassport.srid());

        sxfDescriptor = new SXFDescriptor(sxfPassport);
        sxfDescriptor.read(buffer, strict);
        // Now we can read records.
        while (buffer.remaining() >= 32) {
            SXFRecord sxfRecord = new SXFRecord(sxfPassport, geometryFactory);
            sxfRecord.read(buffer, strict, findNext);
            sxfRecords.add(sxfRecord);
        }
        // Need find border record and set dx0 and dy0 for passport
        List<SXFRecord> borderRecords = getRecordByExcode(sxfPassport.getBorderExcode());
        if (borderRecords.size() > 0) {
            SXFRecord borderRecord = borderRecords.get(0);
            Geometry geometry = borderRecord.geometry();
            double dx0 = sxfPassport.getXY()[0][0] - geometry.getCoordinates()[0].y;
            double dy0 = sxfPassport.getXY()[0][1] - geometry.getCoordinates()[0].x;
            sxfPassport.setDXY0(new double[] {dx0, dy0});
            borderRecord.destroy();
        }
    }

    public SXFPassport getPassport() {
        return sxfPassport;
    }

    public SXFDescriptor getDescriptor() {
        return sxfDescriptor;
    }

    public int getCount() {
        return sxfRecords.size();
    }

    public List<SXFRecord> getRecordByExcode(int excode) {
        List<SXFRecord> result = new ArrayList<>();
        for (SXFRecord sxfRecord : sxfRecords) {
            if (sxfRecord.getExcode() == excode) {
                result.add(sxfRecord);
            }
        }
        return result;
    }

    public SXFRecord getRecordByIncode(int incode) {
        return sxfRecords.get(incode);
    }

    public SXFRecord getRecordByNumber(int number) {
        for (SXFRecord sxfRecord : sxfRecords) {
            if (sxfRecord.getNumber() == number) {
                return sxfRecord;
            }
        }
        return null;
    }

    public void close() throws IOException {
        if (readableByteChannel.isOpen()) {
            readableByteChannel.close();
        }
        buffer.clear();
        buffer = null;
        readableByteChannel = null;
    }
}
