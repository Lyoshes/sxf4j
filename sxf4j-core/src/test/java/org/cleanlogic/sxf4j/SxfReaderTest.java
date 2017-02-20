package org.cleanlogic.sxf4j;

import com.vividsolutions.jts.geom.Geometry;
import junit.framework.TestCase;
import junit.textui.TestRunner;
import org.junit.Test;

import java.io.File;
import java.io.IOException;

/**
 * @author Serge Silaev aka iSergio <s.serge.b@gmail.com>
 */
public class SxfReaderTest extends TestCase {
    public static void main(String args[]) {
        TestRunner.run(SxfReaderTest.class);
    }

    @Test(expected = IOException.class)
    public void testSxfReader3() throws IOException {
        File file = new File("src/test/resources/L3710.SXF");
        SXFReader sxfReader = new SXFReader(file);
        sxfReader.close();
        assertEquals(sxfReader.getPassport().getVersion(), SXFPassport.VERSION_3);
    }

    @Test
    public void testSxfReader4() throws IOException {
        File file = new File("src/test/resources/K37007.SXF");
        SXFReader sxfReader = new SXFReader(file);
        sxfReader.close();
        assertEquals(sxfReader.getPassport().getVersion(), SXFPassport.VERSION_4);
    }

    @Test
    public void testSxfReaderUnsupported() {
        File file = new File("src/test/resources/L3710-break.SXF");
        try {
            SXFReader sxfReader = new SXFReader(file);
            sxfReader.close();
        } catch (IOException e) {
            assertNotNull(e);
        }
    }

    @Test
    public void testSxfReaderSheetPolygon() throws IOException {
        File file = new File("src/test/resources/K37007.SXF");
        SXFReader sxfReader = new SXFReader(file);
        Geometry polygon = sxfReader.getPassportXY();
        sxfReader.close();
        assertNotNull(polygon);
    }
}
