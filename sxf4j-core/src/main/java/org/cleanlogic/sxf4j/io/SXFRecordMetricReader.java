package org.cleanlogic.sxf4j.io;

import com.vividsolutions.jts.geom.*;
import org.cleanlogic.sxf4j.SXF;
import org.cleanlogic.sxf4j.enums.MetricElementSize;
import org.cleanlogic.sxf4j.enums.TextEncoding;
import org.cleanlogic.sxf4j.enums.TextMetricAlign;
import org.cleanlogic.sxf4j.format.SXFPassport;
import org.cleanlogic.sxf4j.format.SXFRecordHeader;
import org.cleanlogic.sxf4j.format.SXFRecordMetric;
import org.cleanlogic.sxf4j.format.SXFRecordMetricText;
import org.cleanlogic.sxf4j.utils.Utils;

import java.io.UnsupportedEncodingException;
import java.nio.MappedByteBuffer;
import java.util.ArrayList;
import java.util.List;

/**
 * @author Serge Silaev aka iSergio <s.serge.b@gmail.com>
 */
public class SXFRecordMetricReader {
    private final MappedByteBuffer _mappedByteBuffer;
    private final SXFPassport _sxfPassport;
    private final SXFReaderOptions _sxfReaderOptions;
    private final GeometryFactory _geometryFactory;

    public SXFRecordMetricReader(MappedByteBuffer mappedByteBuffer, SXFPassport sxfPassport) {
        _mappedByteBuffer = mappedByteBuffer;
        _sxfPassport = sxfPassport;
        _sxfReaderOptions = new SXFReaderOptions();
        _geometryFactory = new GeometryFactory(new PrecisionModel(PrecisionModel.FLOATING_SINGLE), Utils.detectSRID(sxfPassport));
    }

    public SXFRecordMetricReader(MappedByteBuffer mappedByteBuffer, SXFPassport sxfPassport, SXFReaderOptions sxfReaderOptions) {
        _mappedByteBuffer = mappedByteBuffer;
        _sxfPassport = sxfPassport;
        _sxfReaderOptions = sxfReaderOptions;
        _geometryFactory = new GeometryFactory(new PrecisionModel(PrecisionModel.FLOATING_SINGLE), Utils.detectSRID(sxfPassport));
    }

    public SXFRecordMetric read(SXFRecordHeader sxfRecordHeader) {
        _mappedByteBuffer.position((int) sxfRecordHeader.metricOffset);
        List<Coordinate> recordCoordinates = new ArrayList<>();

        for (int i = 0; i < sxfRecordHeader.pointCount; i++) {
            Coordinate coordinate = readCoordinate(sxfRecordHeader, _sxfReaderOptions.flipCoordinates);
            coordinate = _sxfPassport.fromDescret(coordinate);
            if (_sxfReaderOptions.flipCoordinates) {
                coordinate = new Coordinate(coordinate.y, coordinate.x, coordinate.z);
            }
            recordCoordinates.add(coordinate);
        }
        // After main metric may be text metric
        List<SXFRecordMetricText> sxfRecordMetricTexts = new ArrayList<>();
        if (sxfRecordHeader.isText) {
            SXFRecordMetricText sxfRecordMetricText = readText();
            sxfRecordMetricTexts.add(sxfRecordMetricText);
        }

        List<List<Coordinate>> subrecordsCoordinates = new ArrayList<>();
        // Process subject
        for (int i = 0; i < sxfRecordHeader.subjectCount; i++) {
            // First two bytes is reserver, skip them
            _mappedByteBuffer.position(_mappedByteBuffer.position() + 2);
            int pointCount = _mappedByteBuffer.getShort();
            List<Coordinate> subrecordCoordinates =  new ArrayList<>();
            for (int k = 0; k < pointCount; k++) {
                Coordinate coordinate = readCoordinate(sxfRecordHeader, _sxfReaderOptions.flipCoordinates);
                coordinate = _sxfPassport.fromDescret(coordinate);
                if (_sxfReaderOptions.flipCoordinates) {
                    coordinate = new Coordinate(coordinate.y, coordinate.x, coordinate.z);
                }
                subrecordCoordinates.add(coordinate);
            }
            subrecordsCoordinates.add(subrecordCoordinates);
            // Text of subjects
            if (sxfRecordHeader.isText) {
                SXFRecordMetricText sxfRecordMetricText = readText();
                sxfRecordMetricTexts.add(sxfRecordMetricText);
            }
        }

        SXFRecordMetric sxfRecordMetric = new SXFRecordMetric();
        if (sxfRecordHeader.isText) {
            sxfRecordMetric.metricTexts = sxfRecordMetricTexts;
        }
        switch (sxfRecordHeader.local) {
            case LINE:
            case TITLE:
            case VECTOR:
            case MIXED:
                if (recordCoordinates.size() == 1) {
                    recordCoordinates.add(recordCoordinates.get(0));
                }
                LineString[] lineStrings = new LineString[1 + subrecordsCoordinates.size()];
                lineStrings[0] = _geometryFactory.createLineString(recordCoordinates.toArray(new Coordinate[recordCoordinates.size()]));
                for (int i = 0; i < subrecordsCoordinates.size(); i++) {
                    List<Coordinate> coordinates = subrecordsCoordinates.get(i);
                    if (coordinates.size() == 1) {
                        coordinates.add(coordinates.get(0));
                    }
                    lineStrings[i + 1] = _geometryFactory.createLineString(coordinates.toArray(new Coordinate[coordinates.size()]));
                }
                sxfRecordMetric.geometry = _geometryFactory.createMultiLineString(lineStrings);
                break;
            case POINT:
                Point[] points = new Point[1 + subrecordsCoordinates.size()];
                points[0] = _geometryFactory.createPoint(recordCoordinates.get(0));
                for (int i = 0; i < subrecordsCoordinates.size(); i++) {
                    List<Coordinate> coordinates = subrecordsCoordinates.get(i);
                    points[i + 1] = _geometryFactory.createPoint(coordinates.get(0));
                }
                sxfRecordMetric.geometry = _geometryFactory.createMultiPoint(points);
                break;
            case SQUARE: {
                LinearRing shell = _geometryFactory.createLinearRing(recordCoordinates.toArray(new Coordinate[recordCoordinates.size()]));
                LinearRing[] holes = new LinearRing[subrecordsCoordinates.size()];
                for (int i = 0; i < subrecordsCoordinates.size(); i++) {
                    List<Coordinate> coordinates = subrecordsCoordinates.get(i);
                    holes[i] = _geometryFactory.createLinearRing(coordinates.toArray(new Coordinate[coordinates.size()]));
                }
                Polygon polygon = _geometryFactory.createPolygon(shell, holes);
                sxfRecordMetric.geometry = _geometryFactory.createMultiPolygon(new Polygon[]{polygon});
            } break;
            default: break;
        }

        return sxfRecordMetric;
    }

    /**
     * Read coordinate by MappedByteBuffer. Function not will be used directly!
     * @param sxfRecordHeader record header.
     * @return {@link Coordinate} in descrets.
     */
    private Coordinate readCoordinate(SXFRecordHeader sxfRecordHeader, boolean flipCoordinate) {
        double x = 0.;
        double y = 0.;
        double z = 0.;

        if (!sxfRecordHeader.isFloat) {
            if (sxfRecordHeader.metricElementSize == MetricElementSize.SHORT) {
                x = _mappedByteBuffer.getShort();
                y = _mappedByteBuffer.getShort();
            } else if (sxfRecordHeader.metricElementSize == MetricElementSize.INT) {
                x = _mappedByteBuffer.getInt();
                y = _mappedByteBuffer.getInt();
            }

            if (sxfRecordHeader.is3D) {
                z = _mappedByteBuffer.getFloat();
            }
        } else {
            if (sxfRecordHeader.metricElementSize == MetricElementSize.FLOAT) {
                x = _mappedByteBuffer.getFloat();
                y = _mappedByteBuffer.getFloat();

                if (sxfRecordHeader.is3D) {
                    z = _mappedByteBuffer.getFloat();
                }
            } else if (sxfRecordHeader.metricElementSize == MetricElementSize.DOUBLE) {
                x = _mappedByteBuffer.getDouble();
                y = _mappedByteBuffer.getDouble();

                if (sxfRecordHeader.is3D) {
                    z = _mappedByteBuffer.getDouble();
                }
            }
        }

        return new Coordinate(x, y, z);
    }

    /**
     * Read text of metric and they align. Function not will be used directly!
     * @return {@link SXFRecordMetricText}
     */
    private SXFRecordMetricText readText() {
        int length = (int) _mappedByteBuffer.get();
        byte[] string = new byte[length];
        _mappedByteBuffer.get(string);
        String text = null;
        try {
            text = new String(string, TextEncoding.CP1251.getName());
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        // Close binary \0
        _mappedByteBuffer.get();
        // Calculate align of text
        TextMetricAlign align = TextMetricAlign.MIDDLE_LEFT;
        if (text != null) {
            if ((length - text.trim().length()) >= 3) {
                for (int i = 0; i < text.length(); i++) {
                    char c = text.charAt(i);
                    if (c == '\0') {
                        c = text.charAt(i + 1);
                        align = TextMetricAlign.fromValue((int) c);
                        break;
                    }
                }
            }
        }
        return new SXFRecordMetricText(text, align);
    }
}
