package org.cleanlogic.sxf4j.io;

import org.cleanlogic.sxf4j.fixes.SXFPassportFixes;

/**
 * @author Serge Silaev aka iSergio <s.serge.b@gmail.com>
 */
public class SXFReaderOptions {
    public boolean quite = false;
    public int srcSRID = 0;
    public int dstSRID = 0;
    public boolean flipCoordinates = false;
//    public SXFPassportFixes.FixCoordinates fixCoordinates = SXFPassportFixes.FixCoordinates.SKIP;
}
