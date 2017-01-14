package org.cleanlogic.sxf4j.io;

import com.vividsolutions.jts.geom.*;
import com.vividsolutions.jts.geom.impl.CoordinateArraySequence;
import org.cleanlogic.sxf4j.SXF;
import org.cleanlogic.sxf4j.enums.MetricElementSize;
import org.cleanlogic.sxf4j.format.SXFPassport;
import org.cleanlogic.sxf4j.format.SXFRecordHeader;
import org.cleanlogic.sxf4j.format.SXFRecordMetric;

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
        _geometryFactory = new GeometryFactory(new PrecisionModel(PrecisionModel.FLOATING_SINGLE), SXF.DetectSRID(sxfPassport));
    }

    public SXFRecordMetricReader(MappedByteBuffer mappedByteBuffer, SXFPassport sxfPassport, SXFReaderOptions sxfReaderOptions) {
        _mappedByteBuffer = mappedByteBuffer;
        _sxfPassport = sxfPassport;
        _sxfReaderOptions = sxfReaderOptions;
        _geometryFactory = new GeometryFactory(new PrecisionModel(PrecisionModel.FLOATING_SINGLE), SXF.DetectSRID(sxfPassport));
    }

    public SXFRecordMetric read(SXFRecordHeader sxfRecordHeader) {
        _mappedByteBuffer.position((int) sxfRecordHeader.metricOffset);
        List<Coordinate> recordCoordinates = new ArrayList<>();

        for (int i = 0; i < sxfRecordHeader.pointCount; i++) {
            Coordinate coordinate = readCoordinate(sxfRecordHeader);
            coordinate = _sxfPassport.fromDescret(coordinate);
            recordCoordinates.add(coordinate);
        }
        // After main metric may be text metric
        if (sxfRecordHeader.isText) {
            //TODO: Later
        }

        List<List<Coordinate>> subrecordsCoordinates = new ArrayList<>();
        // Process subject
        for (int i = 0; i < sxfRecordHeader.subjectCount; i++) {
            // First two bytes is reserver, skip them
            _mappedByteBuffer.position(_mappedByteBuffer.position() + 2);
            int pointCount = _mappedByteBuffer.getShort();
            List<Coordinate> subrecordCoordinates =  new ArrayList<>();
            for (int k = 0; k < pointCount; k++) {
                Coordinate coordinate = readCoordinate(sxfRecordHeader);
                coordinate = _sxfPassport.fromDescret(coordinate);
                subrecordCoordinates.add(coordinate);
            }
            subrecordsCoordinates.add(subrecordCoordinates);
        }
        // After main metric may be text metric
        if (sxfRecordHeader.isText) {
            //TODO: Later
        }

        SXFRecordMetric sxfRecordMetric = new SXFRecordMetric();
        switch (sxfRecordHeader.local) {
            case LINE:
            case TITLE:
            case VECTOR:
            case MIXED:
                sxfRecordMetric.geometry = _geometryFactory.createLineString(recordCoordinates.toArray(new Coordinate[recordCoordinates.size()]));
                break;
            case POINT:
                sxfRecordMetric.geometry = _geometryFactory.createPoint(recordCoordinates.get(0));
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
    private Coordinate readCoordinate(SXFRecordHeader sxfRecordHeader) {
        double x = 0.;
        double y = 0.;
        double h = 0.;

        if (!sxfRecordHeader.isFloat) {
            if (sxfRecordHeader.metricElementSize == MetricElementSize.SHORT) {
                x = _mappedByteBuffer.getShort();
                y = _mappedByteBuffer.getShort();
            } else if (sxfRecordHeader.metricElementSize == MetricElementSize.INT) {
                x = _mappedByteBuffer.getInt();
                y = _mappedByteBuffer.getInt();
            }

            if (sxfRecordHeader.is3D) {
                h = _mappedByteBuffer.getFloat();
            }
        } else {
            if (sxfRecordHeader.metricElementSize == MetricElementSize.FLOAT) {
                x = _mappedByteBuffer.getFloat();
                y = _mappedByteBuffer.getFloat();

                if (sxfRecordHeader.is3D) {
                    h = _mappedByteBuffer.getFloat();
                }
            } else if (sxfRecordHeader.metricElementSize == MetricElementSize.DOUBLE) {
                x = _mappedByteBuffer.getDouble();
                y = _mappedByteBuffer.getDouble();

                if (sxfRecordHeader.is3D) {
                    h = _mappedByteBuffer.getDouble();
                }
            }
        }

        return new Coordinate(x, y, h);
    }
}
