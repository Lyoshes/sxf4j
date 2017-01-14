package org.cleanlogic.sxf4j;

import junit.framework.TestCase;
import junit.textui.TestRunner;

import org.cleanlogic.sxf4j.exceptions.SXFWrongFormatException;
import org.cleanlogic.sxf4j.io.SXFReader;

import java.io.IOException;

import static org.cleanlogic.sxf4j.SXF.FILE_SXF;

/**
 * @author Serge Silaev aka iSergio <s.serge.b@gmail.com>
 */
public class SXFReaderTest extends TestCase {
    SXFReader sxfReader = new SXFReader();

    public static void main(String args[]) {
        TestRunner.run(SXFReaderTest.class);
    }

    public SXFReaderTest(String name) throws IOException {
        super(name);
    }

    public void testSXFReaderVersion4() throws IOException, SXFWrongFormatException {
        sxfReader.read("src/test/resources/K37007.SXF");
        assertNotNull(sxfReader.getPassport());
        assertTrue(sxfReader.getPassport().identifier == FILE_SXF);
    }

//    public void testSXFReaderVersion3() throws IOException {
//        sxfReader.read("src/test/resources/L3710.SXF");
//        assertNotNull(sxfReader.getPassport());
//        assertTrue(sxfReader.getPassport().identifier == FILE_SXF);
//    }

//    public void testSXFPassportReaderVersion3() throws IOException {
//        SXFPassportReader sxfPassportReader = new SXFPassportReader("src/test/resources/L3710.SXF");
//        SXFPassport sxfPassport = sxfPassportReader.read();
//        assertNotNull(sxfPassport);
//        assertTrue(sxfPassport.identifier == FILE_SXF);
//    }
//
//    public void testSXFPassportReaderVersion4() throws IOException {
//        SXFPassportReader sxfPassportReader = new SXFPassportReader("src/test/resources/K37007.SXF");
//        SXFPassport sxfPassport = sxfPassportReader.read();
//        assertNotNull(sxfPassport);
//        assertTrue(sxfPassport.identifier == FILE_SXF);
//    }
}
