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

import com.vividsolutions.jts.geom.*;
import org.cleanlogic.sxf4j.enums.*;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;

/**
 * @author Serge Silaev aka iSergio <s.serge.b@gmail.com>
 */
public class SXFRecord {
    public static final int IDENTIFIER = 0x7FFF7FFF;

    /**
     * Record offset in global ByteBuffer position (from begin).
     */
    private int offset;
    /**
     * Identifier of begin data {@link #IDENTIFIER}.
     */
    private int identifier;
    /**
     * Total length with header
     */
    private int length;
    /**
     * Metric only length
     */
    private int metricLength;
    /**
     * RSC excode
     */
    private int excode;
    /**
     * Object number
     */
    private int number;
    /**
     * Localization of object {@link Local}
     */
    private Local local;
    /**
     * Only {@link SXFPassport#VERSION_3}
     */
    private FrameOut frameOut;
    /**
     * Only {@link SXFPassport#VERSION_3}
     */
    private boolean isClose;
    /**
     * Flag if polygon is Multi
     * Only {@link SXFPassport#VERSION_4}
     */
    private boolean isMultiPolygon;
    /**
     * Flag if polygon is Multi
     * Only {@link SXFPassport#VERSION_4}
     */
    private boolean isCompressible;
    /**
     * Exists of semantic
     */
    private boolean isSemantic;
    /**
     * Metric element size (Float, Double, Short, Int)
     */
    private MetricElementSize metricElementSize;
    /**
     * Only {@link SXFPassport#VERSION_3}
     */
    private boolean isGroup;
    /**
     * Only {@link SXFPassport#VERSION_4}
     */
    private boolean isVector;
    /**
     * Only {@link SXFPassport#VERSION_4}
     */
    private boolean isUnicode;
    /**
     * Object to upper from other
     * Only {@link SXFPassport#VERSION_4}
     */
    private boolean isAbove;
    /**
     * Object to lower from other
     * Only {@link SXFPassport#VERSION_4}
     */
    private boolean isBelow;
    /**
     * Vertical align for record subjects
     * Only {@link SXFPassport#VERSION_4}
     */
    private boolean isVAlign;
    /**
     * Metric format:
     * 0 - linear format
     * 1 - vector format
     */
    private int metricFormat;
    /**
     * False - 2D, True - 3D
     */
    private boolean is3D;
    /**
     * If false - metric provided as integers. If true - metric as Floating-point. Height - is always Floating-point
     */
    private boolean isFloat;
    /**
     * Metric contains text.
     */
    private boolean isText;
    /**
     * Graphic available
     */
    private boolean isGraphic;
    /**
     * Available graphic is scale
     */
    private boolean isGraphicScale;
    /**
     * Build spline by metric
     */
    private Spline spline;
    /**
     * Bottom level of generalization(object visible/invisible) table
     */
    private int bottom;
    /**
     * Upper level of generalization(object visible/invisible) table
     */
    private int upper;
    /**
     * Only {@link SXFPassport#VERSION_3}
     */
    private int groupNumber;
    /**
     * Metric point count for big objects if metricDescription == 65535 (0xFFFF)
     */
    private int bigRecordPointCount;
    /**
     * Metric description. subjectCount and pointCount from this.
     */
    private int metricDescription;
    /**
     * Total subject count
     */
    private int subrecordCount;
    /**
     * Total point count in metric
     */
    private int pointCount;
    /**
     * Byte offset for begin metric block
     */
    private int metricOffset;
    /**
     * Byte offset for begin seamntics block
     */
    private int semanticOffset;

    private ByteBuffer buffer;

    private Geometry geometry;
    private List<Text> texts = new ArrayList<>();
    private List<Semantic> semantics = new ArrayList<>();

    private final SXFPassport sxfPassport;
    private final GeometryFactory geometryFactory;

    public final class Text {
        private String text;
        private TextMetricAlign align;

        public Text(String text, TextMetricAlign align) {
            this.text = text;
            this.align = align;
        }

        public String getText() {
            return text;
        }

        public TextMetricAlign getAlign() {
            return align;
        }

        @Override
        public String toString() {
            return String.format("{\"%s\", \"%d\"}", text, align.getValue());
        }
    }

    public final class Semantic {
        public short code;
        public SemanticType type;
        public int scale;
        public String value;

        @Override
        public String toString() {
            return "Semantic:\n" +
                    String.format("\t\tCode:\t%d\n", code) +
                    String.format("\t\tType:\t%s (%s)\n", type, type.getName()) +
                    String.format("\t\tScale:\t%d\n", scale) +
                    String.format("\t\tValue:\t%s\n", value);
        }
    }

    public SXFRecord(SXFPassport sxfPassport, GeometryFactory geometryFactory) {
        this.sxfPassport = sxfPassport;
        this.geometryFactory = geometryFactory;
    }

    public int getExcode() {
        return excode;
    }

    public int getNumber() {
        return number;
    }

    public Local getLocal() {
        return local;
    }

    public int getSubrecordCount() {
        return subrecordCount;
    }

    public boolean isSemanticExists() {
        return isSemantic;
    }

    public boolean isTextExsits() {
        return isText;
    }

    public boolean isMultiPolygon() {
        return isMultiPolygon;
    }

    /**
     * Function check SXFDescriptor magic number.
     * @param strict Show message through println or IOException.
     * @throws IOException exception if wrong.
     */
    private void checkIdentifier(boolean strict) throws IOException {
        if (identifier != IDENTIFIER) {
            String message = "Wrong identifier(magic number) of SXF Record, expected " + IDENTIFIER + ", got " + identifier;
            if (!strict) {
                System.err.println(message);
            } else {
                throw new IOException(message);
            }
        }
    }

    /**
     * Function read only record header information.
     * @param buffer Opened buffer SXF file
     * @param strict Show message through println or IOException.
     * @throws IOException exception if wrong.
     */
    public void read(ByteBuffer buffer, boolean strict) throws IOException {
        this.offset = buffer.position();
        this.buffer = buffer;

        identifier = buffer.getInt();
        checkIdentifier(strict);

        if (sxfPassport.getVersion() == SXFPassport.VERSION_3) {
            read3(buffer, strict);
        } else if (sxfPassport.getVersion() == SXFPassport.VERSION_4) {
            read4(buffer, strict);
        }
        // End of record, skip metric,text,semantics and etc.
        buffer.position(buffer.position() + length - 32);
    }

    /**
     * Function read only record header information.
     * @param buffer Opened buffer SXF file
     * @param strict Show message through println or IOException.
     * @throws IOException exception if wrong.
     */
    private void read3(ByteBuffer buffer, boolean strict) throws IOException {
        length = buffer.getInt();
        metricLength = buffer.getInt();
        excode = buffer.getInt();
        number = buffer.getInt();

        byte[] infoFlags = new byte[3];
        buffer.get(infoFlags);
        local = Local.fromValue(infoFlags[0] & 0xF);
        frameOut = FrameOut.fromValue((infoFlags[0] >> 4) & 0xF);

        isClose = (infoFlags[1] & 0x1) == 1;
        isSemantic = ((infoFlags[1] >> 1) & 0x1) == 1;
        int metricElementSize = ((infoFlags[1] >> 2) & 0x1);
        isGroup = ((infoFlags[1] >> 3) & 0x1) == 1;

        metricFormat = (infoFlags[2] & 0x1);
        is3D = ((infoFlags[2] >> 1) & 0x1) == 1;
        isFloat = ((infoFlags[2] >> 2) & 0x1) == 1;
        isText = ((infoFlags[2] >> 3) & 0x1) == 1;
        this.metricElementSize = getMetricElementSizeEnum(metricElementSize, isFloat);

        byte generalization = buffer.get();
        bottom = generalization & 0xF;
        upper = (generalization >> 4) & 0xF;

        groupNumber = buffer.getInt();

        metricDescription = buffer.getInt();
        subrecordCount = metricDescription & 0xFFFF;
        pointCount = (metricDescription >> 16) & 0xFFFF;

        metricOffset = buffer.position();
        semanticOffset = metricOffset + metricLength;
    }

    /**
     * Function read only record header information.
     * @param buffer Opened buffer SXF file
     * @param strict Show message through println or IOException.
     * @throws IOException exception if wrong.
     */
    private void read4(ByteBuffer buffer, boolean strict) throws IOException {
        length = buffer.getInt();
        metricLength = buffer.getInt();
        excode = buffer.getInt();
        number = buffer.getInt();

        byte[] infoFlags = new byte[3];
        buffer.get(infoFlags);

        local = Local.fromValue(infoFlags[0] & 0xF);
        isMultiPolygon = ((infoFlags[0] >> 4) & 0x1) == 1;

        isCompressible = (infoFlags[1] & 0x1) == 1;
        isSemantic = ((infoFlags[1] >> 1) & 0x1) == 1;
        int metricElementSize = ((infoFlags[1] >> 2) & 0x1);
        isVector = ((infoFlags[1] >> 3) & 0x1) == 1;
        isUnicode = ((infoFlags[1] >> 4) & 0x1) == 1;
        isAbove = ((infoFlags[1] >> 5) & 0x1) == 1;
        isBelow = ((infoFlags[1] >> 6) & 0x1) == 1;
        isVAlign = ((infoFlags[1] >> 7) & 0x1) == 1;

        metricFormat = (infoFlags[2] & 0x1);
        is3D = ((infoFlags[2] >> 1) & 0x1) == 1;
        isFloat = ((infoFlags[2] >> 2) & 0x1) == 1;
        isText = ((infoFlags[2] >> 3) & 0x1) == 1;
        isGraphic = ((infoFlags[2] >> 4) & 0x1) == 1;
        isGraphicScale = ((infoFlags[2] >> 5) & 0x1) == 1;
        spline = Spline.fromValue((infoFlags[2] >> 6) & 0x3);
        this.metricElementSize = getMetricElementSizeEnum(metricElementSize, isFloat);

        byte generalization = buffer.get();
        bottom = generalization & 0xF;
        upper = (generalization >> 4) & 0xF;

        bigRecordPointCount = buffer.getInt();

        metricDescription = buffer.getInt();
        subrecordCount = metricDescription & 0xFFFF;
        pointCount = (metricDescription >> 16) & 0xFFFF;

        metricOffset = buffer.position();
        semanticOffset = metricOffset + metricLength;
    }

    private MetricElementSize getMetricElementSizeEnum(int metricElementSize, boolean isFloat) {
        MetricElementSize metricElementSizeEnum = null;
        switch (metricElementSize) {
            case 0:
                if (!isFloat) {
                    metricElementSizeEnum = MetricElementSize.SHORT;
                } else {
                    metricElementSizeEnum = MetricElementSize.FLOAT;
                }
                break;
            case 1:
                if (!isFloat) {
                    metricElementSizeEnum = MetricElementSize.INT;
                } else {
                    metricElementSizeEnum = MetricElementSize.DOUBLE;
                }
                break;
            default: break;
        }
        return metricElementSizeEnum;
    }

    public Geometry geometry() throws IOException {
        if (geometry != null) {
            return geometry;
        }
        // Set offset to metric
        buffer.position(metricOffset);

        // Read main record metric
        int pointCount = (this.pointCount == 65537 ? bigRecordPointCount : this.pointCount);
        double[][] srcRecordCoordinates = new double[pointCount][];
        for (int i = 0; i < pointCount; i++) {
            srcRecordCoordinates[i] = readCoordinate();
        }

        // After main metric may be text metric
        if (isText) {
            texts.add(readText());
        }

        // Process subrecords
        double[][][] srcSubrecordsCoordinates = new double[subrecordCount][][];
        for (int i = 0; i < subrecordCount; i++) {
            // First two bytes is reserver, skip them
            buffer.position(buffer.position() + 2);
            pointCount = buffer.getShort();
            double[][] srcSubrecordCoordinates = new double[pointCount][];
            for (int k = 0; k < pointCount; k++) {
                srcSubrecordCoordinates[k] = readCoordinate();
            }
            srcSubrecordsCoordinates[i] = srcSubrecordCoordinates;
            if (isText) {
                texts.add(readText());
            }
        }

        switch (local) {
            case MIXED:
            case TITLE:
            case VECTOR:
            case LINE:
                geometry = createMultiLineString(srcRecordCoordinates, srcSubrecordsCoordinates);
                break;
            case SQUARE:
                geometry = createMultiPolygon(srcRecordCoordinates, srcSubrecordsCoordinates);
                break;
            case POINT:
                geometry = createMultiPoint(srcRecordCoordinates, srcSubrecordsCoordinates);
                break;
            default:
                break;
        }

        return geometry;
    }

    public List<Text> texts() throws IOException {
        if (isText && geometry == null) {
            geometry();
        }
        return texts;
    }

    private double[] readCoordinate() {
        double x = 0.;
        double y = 0.;
        double z = 0.;

        if (!isFloat) {
            if (metricElementSize == MetricElementSize.SHORT) {
                x = buffer.getShort();
                y = buffer.getShort();
            } else if (metricElementSize == MetricElementSize.INT) {
                x = buffer.getInt();
                y = buffer.getInt();
            }

            if (is3D) {
                z = buffer.getFloat();
            }
        } else {
            if (metricElementSize == MetricElementSize.FLOAT) {
                x = buffer.getFloat();
                y = buffer.getFloat();

                if (is3D) {
                    z = buffer.getFloat();
                }
            } else if (metricElementSize == MetricElementSize.DOUBLE) {
                x = buffer.getDouble();
                y = buffer.getDouble();

                if (is3D) {
                    z = buffer.getDouble();
                }
            }
        }

        if (sxfPassport.isDescrets()) {
            double[][] xy = sxfPassport.getXY();
            double[][] deviceXY = sxfPassport.getDeviceXY();
            x = xy[0][0] + (x - deviceXY[0][0]) / sxfPassport.getDeviceCapability() * sxfPassport.getScale() + sxfPassport.getDXY0()[0];
            y = xy[0][1] + (y - deviceXY[0][1]) / sxfPassport.getDeviceCapability() * sxfPassport.getScale() + sxfPassport.getDXY0()[1];
        }
        return new double[] {y, x, z};
    }

    private Text readText() throws IOException {
        int length = buffer.get() & 0xFF;
        byte[] string = new byte[length];
        buffer.get(string);
        // Final 0x00 by documentation
        byte zero   = buffer.get();
        int strlen = 0;
        for (byte b : string) {
            if (b == 0x00) {
                break;
            }
            strlen++;
        }
        String textEncoding = "UTF16";
        if (!isUnicode) {
            textEncoding = TextEncoding.CP1251.getName();
        }
        String text = new String(string, textEncoding).substring(0, strlen).intern();
        TextMetricAlign align = TextMetricAlign.BASELINE_LEFT;
        if (strlen + 1 < length) {
            byte c = string[strlen + 1];
            align = TextMetricAlign.fromValue(c);
        } else {
            // If final zero != 0x00, use them
            if (zero != 0x00) {
                align = TextMetricAlign.fromValue(zero);
            }
        }

        return new Text(text, align);
    }

    public List<Semantic> semantics() {
        if (isSemantic && semantics.size() > 0) {
            return semantics;
        }
        // Read semantics
        int totalBytes = length - metricLength - 32;

        buffer.position(semanticOffset);

        while (totalBytes > 0) {
            Semantic semantic = new Semantic();

            short code = buffer.getShort();
            byte[] lengthCode = new byte[2];
            buffer.get(lengthCode);
            int type = lengthCode[0] & 0xFF;
            int scale = lengthCode[1];
            // Bytes left to read
            totalBytes -= 4;

            if (type == 128 && scale == 255) {
                // This is dynamic length string
                int length = buffer.getInt();
                // Bytes left to read
                totalBytes -= 4;
                byte[] string = new byte[length];
                buffer.get(string);
                // Bytes left to read
                totalBytes -= length;
                semantic.type = SemanticType.fromValue(type);
                semantic.scale = scale;
                semantic.value = new String(string).trim().intern();
            } else {
                semantic.type = SemanticType.fromValue(type);
                semantic.scale = scale;
                if (semantic.type == null) {
                    break;
                }

                if (semantic.type == SemanticType.CHAR || semantic.type == SemanticType.SHORT ||
                        semantic.type == SemanticType.DOUBLE || semantic.type == SemanticType.LONG) {
                    if (semantic.scale < -127 || semantic.scale > 127) {
                        break;
                    }
                } else {
                    // Texts can't have negative length!
                    semantic.scale = semantic.scale & 0xFF;
                }

                switch (semantic.type) {
                    case STRDOS:
                    case STRING:
                    case STRUNI: {
                        Charset charset;
                        switch (semantic.type) {
                            case STRDOS:
                                charset = Charset.forName(TextEncoding.IBM866.getName());
                                break;
                            case STRING:
                                charset = Charset.forName(TextEncoding.CP1251.getName());
                                break;
                            case STRUNI:
                                charset = Charset.forName(TextEncoding.KOI8R.getName());
                                break;
                            default:
                                charset = Charset.forName("UTF-8");
                        }
                        byte[] string = new byte[semantic.scale + 1];
                        buffer.get(string);
                        String value = new String(string, charset).trim().intern();
                        if (semantic.type == SemanticType.STRUNI) {
                            value = value.replaceAll("\\p{Cc}", "");
                        }
                        semantic.value = value;
                        totalBytes -= (semantic.scale + 1);
                    }
                    break;
                    case CHAR: {
                        byte value = buffer.get();
                        semantic.value = String.valueOf((int) value * Math.pow(10., semantic.scale)).intern();
                        totalBytes -= 1;
                    }
                    break;
                    case SHORT: {
                        short value = buffer.getShort();
                        semantic.value = String.valueOf(value * Math.pow(10., semantic.scale)).intern();
                        totalBytes -= 2;
                    }
                    break;
                    case LONG: {
                        int value = buffer.getInt();
                        semantic.value = String.valueOf(value * Math.pow(10., semantic.scale)).intern();
                        totalBytes -= 4;
                    }
                    break;
                    case DOUBLE: {
                        double value = buffer.getDouble();
                        semantic.value = String.valueOf(value * Math.pow(10., semantic.scale)).intern();
                        totalBytes -= 8;
                    }
                    break;
                    default:
                        break;
                }
            }
            semantics.add(semantic);
        }

        return semantics;
    }

    private Geometry createMultiLineString(double[][] coordinates, double[][][] subCoordinates) {
        boolean clonePoint;
        int length = coordinates.length;
        if (length == 1) {
            length = 2;
            clonePoint = true;
        } else {
            clonePoint = false;
        }
        CoordinateSequence coordinateSequence = geometryFactory.getCoordinateSequenceFactory().create(length, 3);
        for (int i = 0; i < coordinates.length; i++) {
            for (int n = 0; n < 3; n++) {
                coordinateSequence.setOrdinate(i, n, coordinates[i][n]);
            }
        }
        if (clonePoint) {
            for (int n = 0; n < 3; n++) {
                coordinateSequence.setOrdinate(1, n, coordinates[0][n]);
            }
        }
        LineString[] lines = new LineString[1 + subCoordinates.length];
        lines[0] = geometryFactory.createLineString(coordinateSequence);

        for (int i = 0; i < subCoordinates.length; i++) {
            double[][] subCoordinate = subCoordinates[i];
            length = subCoordinate.length;
            if (length == 1) {
                length = 2;
                clonePoint = true;
            } else {
                clonePoint = false;
            }
            coordinateSequence = geometryFactory.getCoordinateSequenceFactory().create(length, 3);
            for (int k = 0; k < subCoordinate.length; k++) {
                for (int n = 0; n < 3; n++) {
                    coordinateSequence.setOrdinate(k, n, subCoordinate[k][n]);
                }
            }
            if (clonePoint) {
                for (int n = 0; n < 3; n++) {
                    coordinateSequence.setOrdinate(1, n, subCoordinate[0][n]);
                }
            }
            lines[i + 1] = geometryFactory.createLineString(coordinateSequence);
        }

        return geometryFactory.createMultiLineString(lines);
    }

    private Geometry createMultiPolygon(double[][] coordinates, double[][][] subCoordinates) {
        CoordinateSequence coordinateSequence = geometryFactory.getCoordinateSequenceFactory().create(coordinates.length, 3);
        for (int i = 0; i < coordinates.length; i++) {
            for (int n = 0; n < 3; n++) {
                coordinateSequence.setOrdinate(i, n, coordinates[i][n]);
            }
        }
        LinearRing shell = geometryFactory.createLinearRing(coordinateSequence);

        LinearRing[] holes = new LinearRing[subCoordinates.length];
        for (int i = 0; i < subCoordinates.length; i++) {
            coordinateSequence = geometryFactory.getCoordinateSequenceFactory().create(subCoordinates[i].length, 3);
            for (int k = 0; k < subCoordinates[i].length; k++) {
                for (int n = 0; n < 3; n++) {
                    coordinateSequence.setOrdinate(k, n, subCoordinates[i][k][n]);
                }
            }
            holes[i] = geometryFactory.createLinearRing(coordinateSequence);
        }

        Polygon polygon = geometryFactory.createPolygon(shell, holes);

        return geometryFactory.createMultiPolygon(new Polygon[] {polygon});
    }

    private Geometry createMultiPoint(double[][] coordinates, double[][][] subCoordinates) {
        Point[] points = new Point[1 + subCoordinates.length];
        CoordinateSequence coordinateSequence = geometryFactory.getCoordinateSequenceFactory().create(1, 3);
        for (int n = 0; n < 3; n++) {
            coordinateSequence.setOrdinate(0, n, coordinates[0][n]);
        }
        points[0] = geometryFactory.createPoint(coordinateSequence);

        for (int i = 0; i < subCoordinates.length; i++) {
            coordinateSequence = geometryFactory.getCoordinateSequenceFactory().create(1, 3);
            for (int n = 0; n < 3; n++) {
                coordinateSequence.setOrdinate(0, n, subCoordinates[i][0][n]);
            }
            points[i + 1] = geometryFactory.createPoint(coordinateSequence);
        }

        return geometryFactory.createMultiPoint(points);
    }

    @Override
    public String toString() {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append(String.format("RecordHeader Info (%d)\n", number));
        stringBuilder.append(String.format("\tIdentifier:\t\t0x%08x\n", identifier));
        stringBuilder.append(String.format("\tTotal length:\t\t%d\n", length));
        stringBuilder.append(String.format("\tMetric only length:\t%d\n", metricLength));
        stringBuilder.append(String.format("\tExcode:\t\t\t%d\n", excode));
        stringBuilder.append(String.format("\tNumber:\t\t\t%d\n", number));
        stringBuilder.append(String.format("\tLocal:\t\t\t%s (%s)\n", local, local.getName()));
        if (sxfPassport.getVersion() == SXFPassport.VERSION_3) {
            stringBuilder.append(String.format("\tFrame out:\t\t%s (%s)\n", frameOut, frameOut.getName()));
            stringBuilder.append(String.format("\tClosed:\t\t\t%b\n", isClose));
        }
        stringBuilder.append(String.format("\tMultiPolygon:\t\t%b\n", isMultiPolygon));
        stringBuilder.append(String.format("\tCompressible:\t\t%b\n", isCompressible));
        stringBuilder.append(String.format("\tSemantic:\t\t%b\n", isSemantic));
        stringBuilder.append(String.format("\tMetric element size:\t%s (%s)\n", metricElementSize, metricElementSize.getName()));
        if (sxfPassport.getVersion() == SXFPassport.VERSION_3) {
            stringBuilder.append(String.format("\tGroup:\t\t\t%b\n", isGroup));
        } else if (sxfPassport.getVersion() == SXFPassport.VERSION_4) {
            stringBuilder.append(String.format("\tVector:\t\t\t%b\n", isVector));
            stringBuilder.append(String.format("\tUnicode:\t\t%b\n", isUnicode));
            stringBuilder.append(String.format("\tAbove:\t\t\t%b\n", isAbove));
            stringBuilder.append(String.format("\tBelow:\t\t\t%b\n", isBelow));
            stringBuilder.append(String.format("\tRecord subject VAlign:\t%b\n", isVAlign));
        }
        stringBuilder.append(String.format("\tMetric format:\t\t%d\n", metricFormat));
        stringBuilder.append(String.format("\tis3D:\t\t\t%b\n", is3D));
        stringBuilder.append(String.format("\tisFloat:\t\t%b\n", isFloat));
        stringBuilder.append(String.format("\tisText:\t\t\t%b\n", isText));
        if (sxfPassport.getVersion() == SXFPassport.VERSION_4) {
            stringBuilder.append(String.format("\tisGraphic:\t\t%b\n", isGraphic));
            stringBuilder.append(String.format("\tisGraphicScale:\t\t%b\n", isGraphicScale));
            stringBuilder.append(String.format("\tSpline:\t\t\t%s (%s)\n", spline, spline.getName()));
        }
        stringBuilder.append(String.format("\tBottom:\t\t\t%d\n", bottom));
        stringBuilder.append(String.format("\tUpper:\t\t\t%d\n", upper));
        if (sxfPassport.getVersion() == SXFPassport.VERSION_3) {
            stringBuilder.append(String.format("\tGroup number:\t\t%d\n", groupNumber));
        }
        stringBuilder.append(String.format("\tBig record point count:\t%d\n", bigRecordPointCount));
        stringBuilder.append(String.format("\tSubject count:\t\t%d\n", subrecordCount));
        stringBuilder.append(String.format("\tPoint count:\t\t%d\t\t\n", pointCount));
        stringBuilder.append(String.format("\tMetric offset:\t\t%d\n", metricOffset));
        stringBuilder.append(String.format("\tSemantic offset:\t%d\n", semanticOffset));

        return stringBuilder.toString();
    }

    public void destroy() {
        geometry = null;
        texts.clear();
        semantics.clear();
    }
}