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
import com.vividsolutions.jts.geom.MultiPolygon;
import com.vividsolutions.jts.geom.Polygon;
import junit.framework.TestCase;
import junit.textui.TestRunner;
import org.cleanlogic.sxf4j.enums.Local;
import org.junit.Test;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Random;

/**
 * @author Serge Silaev aka iSergio <s.serge.b@gmail.com>
 */
public class SxfRecordTest extends TestCase {
    public static void main(String args[]) {
        TestRunner.run(SxfRecordTest.class);
    }

    @Test
    public void testReadRecords() throws IOException {
        File file = new File("src/test/resources/K37007.SXF");
        SXFReader sxfReader = new SXFReader(file);
        sxfReader.close();
        assertTrue(sxfReader.getCount() > 0);
    }

    @Test
    public void testReadSheetBorderRecord() throws IOException {
        SXFRecord sxfRecord = null;
        File file = new File("src/test/resources/K37007.SXF");
        SXFReader sxfReader = new SXFReader(file);
        List<SXFRecord> sxfRecords = sxfReader.getRecordByExcode(sxfReader.getPassport().getBorderExcode());
        if (sxfRecords.size() > 0) {
            sxfRecord = sxfRecords.get(0);
        }
        sxfReader.close();
        assertNotNull(sxfRecord);
        assertEquals(sxfReader.getPassport().getBorderExcode(), sxfRecord.getExcode());
    }

    @Test
    public void testReadRandomRecord() throws IOException {
        Random random = new Random();
        File file = new File("src/test/resources/K37007.SXF");
        SXFReader sxfReader = new SXFReader(file);
        int count = sxfReader.getCount();
        for (int i = 0; i < 10; i++) {
            int incode = random.nextInt(count);
            SXFRecord sxfRecord = sxfReader.getRecordByIncode(incode);
            Geometry geometry = sxfRecord.geometry();
            assertNotNull(sxfRecord);
            assertNotNull(geometry);
            if (sxfRecord.isSemanticExists()) {
                assertTrue(sxfRecord.semantics().size() > 0);
            }
            if (sxfRecord.isTextExsits()) {
                assertTrue(sxfRecord.texts().size() > 0);
            }
        }
        sxfReader.close();
    }

    @Test
    public void testReadLineRecord() throws IOException {
        readRecord(1225, 21200000, Local.LINE, 2383, 0);
    }

    @Test
    public void testReadSquareRecord() throws IOException {
        readRecord(435, 31410000, Local.SQUARE, 86, 4);
    }

    @Test
    public void testReadPointRecord() throws IOException {
        readRecord(718, 11200000, Local.POINT, 1, 0);
    }

    @Test
    public void testReadTitleRecord() throws IOException {
        readRecord(648, 91022000, Local.TITLE, 22, 0);
    }

    @Test
    public void testReadVectorRecord() throws IOException {
        readRecord(3261, 44200000, Local.VECTOR, 2, 0);
    }

    @Test
    public void testReadMixedRecord() throws IOException {
        readRecord(3695, 92170000, Local.MIXED, 8, 3);
    }

    private void readRecord(final int number, final int excode, final Local local, final int pointCount, final int subrecordCount) throws IOException {
        File file = new File("src/test/resources/K37007.SXF");
        SXFReader sxfReader = new SXFReader(file);
        SXFRecord sxfRecord = sxfReader.getRecordByNumber(number);
        Geometry geometry = sxfRecord.geometry();

        sxfReader.close();

        String textStr = "";
        for (SXFRecord.Text text : sxfRecord.texts()) {
            textStr += text.getText();
        }
        assertEquals(number, sxfRecord.getNumber());
        assertEquals(excode, sxfRecord.getExcode());
        assertEquals(local, sxfRecord.getLocal());
        assertNotNull(geometry);
        if (local == Local.SQUARE) {
            MultiPolygon multiPolygon = ((MultiPolygon) geometry);
            assertEquals(pointCount, ((Polygon) multiPolygon.getGeometryN(0)).getExteriorRing().getCoordinates().length);
            assertEquals(subrecordCount, ((Polygon) multiPolygon.getGeometryN(0)).getNumInteriorRing());
        } else {
            assertEquals(pointCount, geometry.getCoordinates().length);
        }
        if (local == Local.TITLE) {
            assertEquals("ЧЕРНОЕ МОРЕ", textStr);
        }
        if (sxfRecord.isSemanticExists()) {
            assertEquals(true, sxfRecord.semantics().size() > 0);
        }
    }
}
