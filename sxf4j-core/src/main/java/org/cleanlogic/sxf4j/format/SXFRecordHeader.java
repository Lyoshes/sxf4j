package org.cleanlogic.sxf4j.format;

import org.cleanlogic.sxf4j.SXF;
import org.cleanlogic.sxf4j.enums.FrameOut;
import org.cleanlogic.sxf4j.enums.Local;
import org.cleanlogic.sxf4j.enums.MetricElementSize;
import org.cleanlogic.sxf4j.enums.Spline;

/**
 * @author Serge Silaev aka iSergio <s.serge.b@gmail.com>
 */
public class SXFRecordHeader {
    public final int _version;
    /**
     * Identifier of begin data
     * @see org.cleanlogic.sxf4j.SXF#RECORD_SXF
     */
    public int identifier;
    /**
     * Total length with header
     */
    public int length;
    /**
     * Metric only length
     */
    public int metricLength;
    /**
     * RSC excode
     */
    public int excode;
    /**
     * Object number
     */
    public int number;
    /**
     * Localization of object {@link Local}
     */
    public Local local;
    /**
     * Only {@link org.cleanlogic.sxf4j.SXF#VERSION_3}
     */
    public FrameOut frameOut;
    /**
     * Only {@link org.cleanlogic.sxf4j.SXF#VERSION_3}
     */
    public boolean isClose;
    /**
     * Flag if polygon is Multi
     * Only {@link org.cleanlogic.sxf4j.SXF#VERSION_4}
     */
    public boolean isMultiPolygon;
    /**
     * Flag if polygon is Multi
     * Only {@link org.cleanlogic.sxf4j.SXF#VERSION_4}
     */
    public boolean isCompressible;
    /**
     * Exists of semantic
     */
    public boolean isSemantic;
    /**
     * Metric element size (Float, Double, Short, Int)
     */
    public MetricElementSize metricElementSize;
    /**
     * Only {@link org.cleanlogic.sxf4j.SXF#VERSION_3}
     */
    public boolean isGroup;
    /**
     * Only {@link org.cleanlogic.sxf4j.SXF#VERSION_4}
     */
    public boolean isVector;
    /**
     * Only {@link org.cleanlogic.sxf4j.SXF#VERSION_4}
     */
    public boolean isUnicode;
    /**
     * Object to upper from other
     * Only {@link org.cleanlogic.sxf4j.SXF#VERSION_4}
     */
    public boolean isAbove;
    /**
     * Object to lower from other
     * Only {@link org.cleanlogic.sxf4j.SXF#VERSION_4}
     */
    public boolean isBelow;
    /**
     * Vertical align for record subjects
     * Only {@link org.cleanlogic.sxf4j.SXF#VERSION_4}
     */
    public boolean isVAlign;
    /**
     * Metric format:
     * 0 - linear format
     * 1 - vector format
     */
    public int metricFormat;

    /**
     * False - 2D, True - 3D
     */
    public boolean is3D;
    /**
     * If false - metric provided as integers. If true - metric as Floating-point. Height - is always Floating-point
     */
    public boolean isFloat;
    /**
     * Metric contains text.
     */
    public boolean isText;
    /**
     * Graphic available
     */
    public boolean isGraphic;
    /**
     * Available graphic is scale
     */
    public boolean isGraphicScale;
    /**
     * Build spline by metric
     */
    public Spline spline;
    /**
     * Bottom level of generalization(object visible/invisible) table
     */
    public int bottom;
    /**
     * Upper level of generalization(object visible/invisible) table
     */
    public int upper;
    /**
     * Only {@link org.cleanlogic.sxf4j.SXF#VERSION_3}
     */
    public int groupNumber;
    /**
     * Metric point count for big objects if subjectCount + pointCount == 65535 (0xFFFF)
     */
    public int bigRecordPointCount;
    /**
     * Total subject count
     */
    public int subjectCount;
    /**
     * Total point count in metric
     */
    public int pointCount;
    /**
     * Byte offset for begin Metric Object
     */
    public long metricOffset;
    public long semanticOffset;

    public SXFRecordHeader(int version) {
        _version = version;
    }

    public int getVersion() {
        return _version;
    }

    public void print() {
        print(this);
    }

    public static void print(SXFRecordHeader sxfRecordHeader) {
        System.out.printf("RecordHeader Info (%d)\n", sxfRecordHeader.number);
        System.out.printf("\tIdentifier:\t\t0x%08x\n", sxfRecordHeader.identifier);
        System.out.printf("\tTotal length:\t\t%d\n", sxfRecordHeader.length);
        System.out.printf("\tMetric only length:\t%d\n", sxfRecordHeader.metricLength);
        System.out.printf("\tExcode:\t\t\t%d\n", sxfRecordHeader.excode);
        System.out.printf("\tNumber:\t\t\t%d\n", sxfRecordHeader.number);
        System.out.printf("\tLocal:\t\t\t%s (%s)\n", sxfRecordHeader.local, sxfRecordHeader.local.getName());
        if (sxfRecordHeader.getVersion() == SXF.VERSION_3) {
            System.out.printf("\tFrame out:\t\t%b\n", sxfRecordHeader.frameOut, sxfRecordHeader.frameOut.getName());
            System.out.printf("\tClosed:\t\t\t%b\n", sxfRecordHeader.isClose);
        }
        System.out.printf("\tMultiPolygon:\t\t%b\n", sxfRecordHeader.isMultiPolygon);
        System.out.printf("\tCompressible:\t\t%b\n", sxfRecordHeader.isCompressible);
        System.out.printf("\tSemantic:\t\t%b\n", sxfRecordHeader.isSemantic);
        System.out.printf("\tMetric element size:\t%s (%s)\n", sxfRecordHeader.metricElementSize, sxfRecordHeader.metricElementSize.getName());
        if (sxfRecordHeader.getVersion() == SXF.VERSION_3) {
            System.out.printf("\tGroup:\t\t\t%b\n", sxfRecordHeader.isGroup);
        } else if (sxfRecordHeader.getVersion() == SXF.VERSION_4) {
            System.out.printf("\tVector:\t\t\t%b\n", sxfRecordHeader.isVector);
            System.out.printf("\tUnicode:\t\t%b\n", sxfRecordHeader.isUnicode);
            System.out.printf("\tAbove:\t\t\t%b\n", sxfRecordHeader.isAbove);
            System.out.printf("\tBelow:\t\t\t%b\n", sxfRecordHeader.isBelow);
            System.out.printf("\tRecord subject VAlign:\t%b\n", sxfRecordHeader.isVAlign);
        }
        System.out.printf("\tMetric format:\t\t%d\n", sxfRecordHeader.metricFormat);
        System.out.printf("\tis3D:\t\t\t%b\n", sxfRecordHeader.is3D);
        System.out.printf("\tisFloat:\t\t%b\n", sxfRecordHeader.isFloat);
        System.out.printf("\tisText:\t\t\t%b\n", sxfRecordHeader.isText);
        if (sxfRecordHeader.getVersion() == SXF.VERSION_4) {
            System.out.printf("\tisGraphic:\t\t%b\n", sxfRecordHeader.isGraphic);
            System.out.printf("\tisGraphicScale:\t\t%b\n", sxfRecordHeader.isGraphicScale);
            System.out.printf("\tSpline:\t\t\t%s (%s)\n", sxfRecordHeader.spline, sxfRecordHeader.spline.getName());
        }
        System.out.printf("\tBottom:\t\t\t%d\n", sxfRecordHeader.bottom);
        System.out.printf("\tUpper:\t\t\t%d\n", sxfRecordHeader.upper);
        if (sxfRecordHeader.getVersion() == SXF.VERSION_3) {
            System.out.printf("\tGroup number:\t\t%d\n", sxfRecordHeader.groupNumber);
        }
        System.out.printf("\tBig record point count:\t%d\n", sxfRecordHeader.bigRecordPointCount);
        System.out.printf("\tSubject count:\t\t%d\n", sxfRecordHeader.subjectCount);
        System.out.printf("\tPoint count:\t\t%d\t\t\n", sxfRecordHeader.pointCount);
        System.out.printf("\tMetric offset:\t\t%d\n", sxfRecordHeader.metricOffset);
    }
}
