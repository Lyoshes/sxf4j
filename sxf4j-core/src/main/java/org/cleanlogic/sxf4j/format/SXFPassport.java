package org.cleanlogic.sxf4j.format;

import com.vividsolutions.jts.geom.*;
import org.cleanlogic.sxf4j.SXF;
import org.cleanlogic.sxf4j.enums.*;
import org.cleanlogic.sxf4j.utils.Utils;

/**
 * @author Serge Silaev aka iSergio <s.serge.b@gmail.com>
 */
public class SXFPassport {
    /**
     * File identifier.
     * @see SXF#FILE_SXF
     * @see SXF#FILE_TXT
     */
    public int identifier;
    /**
     * Passport record length. By this parameter may detect version of SXF(256 = {@link SXF#VERSION_3}; 400 = {@link SXF#VERSION_4})
     * @see SXF#PASSPORT_3_LENGHT
     * @see SXF#PASSPORT_4_LENGHT
     */
    public int length;
    /**
     * Version of SXF file
     * @see SXF#VERSION_3
     * @see SXF#VERSION_4
     */
    public int version;
    /**
     * Check sum for all file
     */
    public int checkSum;
    /**
     * Date of creation dataset sheet
     */
    public String createDate;
    /**
     * Nomenclature of sheet
     */
    public String nomenclature;
    /**
     * Scale of list. Denominator scale.
     */
    public int scale;
    /**
     * Conventional sheet name
     */
    public String name;
    public int conditionFlag;
    /**
     * false - data not match with projection (map in rotation or deformation)
     * true  - data match with projection
     * Only {@link SXF#VERSION_3}
     */
    public boolean dataProjectionFlag;
    /**
     * Auto generate GUID.
     * Only {@link SXF#VERSION_4}
     */
    public boolean autoGUID;
    public int realPlaceFlag;
    public int codeTypeFlag;
    public int generalizationFlag;
    public TextEncoding textEncodingFlag;
    public int coordPrecisionFlag;
    public int orderViewSheetFlag;

    /**
     * Code of classificator
     * Only {@link SXF#VERSION_3}
     */
    public int code;
    /**
     * EPSG code for coordinate system or 0
     */
    public int epsg;

    /**
     * X
     * |
     * |
     * |
     * |
     * +------------Y
     * X south west corner. Meters.
     */
    public double xSouthWest;
    /**
     * Y south west corner. Meters.
     */
    public double ySouthWest;
    /**
     * X north west corner. Meters.
     */
    public double xNorthWest;
    /**
     * Y north west corner. Meters.
     */
    public double yNorthWest;
    /**
     * X north east corner. Meters.
     */
    public double xNorthEast;
    /**
     * Y north east corner. Meters.
     */
    public double yNorthEast;
    /**
     * X south east corner. Meters.
     */
    public double xSouthEast;
    /**
     * Y south east corner. Meters.
     */
    public double ySouthEast;
    /**
     * B south west. Degrees.
     */
    public double bSouthWest;
    /**
     * L south west. Degrees.
     */
    public double lSouthWest;
    /**
     * B north west. Degrees.
     */
    public double bNorthWest;
    /**
     * L north west. Degrees.
     */
    public double lNorthWest;
    /**
     * B north east. Degrees.
     */
    public double bNorthEast;
    /**
     * L north east. Degrees.
     */
    public double lNorthEast;
    /**
     * B south east. Degrees.
     */
    public double bSouthEast;
    /**
     * B south east. Degrees.
     */
    public double lSouthEast;

    public EllipsoidKind ellipsoidKind;
    public HeightSystem heightSystem;
    public MapProjection materialProjection;
    public CoordinateSystem coordinateSystem;
    public Unit planeUnit;
    public Unit heightUnit;
    public FrameKind frameKind;
    public MapType mapType;

    /**
     * Date surveying or map updates.
     * Format: YYYYMMDD
     */
    public String date;
    /**
     * Source material kind.
     */
    public MapInitKind materialKind;
    /**
     * Source material type.
     */
    public MapInitType materialType;
    /**
     * Ident MSK-63.
     * Only {@link SXF#VERSION_4}
     */
    public int msk63Ident;
    /**
     * Limited evidence card frame.
     * Only {@link SXF#VERSION_4}
     */
    public int frameBorder;
    /**
     * Magnetic declination.
     */
    public double magneticAngle;
    /**
     * Mean convergence of meridians.
     */
    public double meridianAngle;
    /**
     * Annual magnetic declination.
     */
    public double yearMagneticAngle;
    /**
     * Date of declination.
     * Format: YYYYMMDD
     */
    public String dateAngle;
    /**
     * Number MSK-63 zone
     * Only {@link SXF#VERSION_4}
     */
    public int msk63Zone;
    /**
     * Vertical interval. Meters.
     */
    public double reliefHeight;
    /**
     * Angle rotation axes for local coordinate systems
     * Only {@link SXF#VERSION_4}
     */
    public double axisAngle;

    public int deviceCapability;

    public int xBorderDeviceSouthWest;
    public int yBorderDeviceSouthWest;
    public int xBorderDeviceNorthWest;
    public int yBorderDeviceNorthWest;
    public int xBorderDeviceNorthEast;
    public int yBorderDeviceNorthEast;
    public int xBorderDeviceSouthEast;
    public int yBorderDeviceSouthEast;

    /**
     * Coordinate system start X coordinate.
     */
    public double dx0 = 0.;
    /**
     * Coordinate system start Y coordinate.
     */
    public double dy0 = 0.;

//    /**
//     * Border offset by X if border of passport not equalent border object int records
//     */
//    public double dxBorderDevice = 0.;
//    /**
//     * Border offset by Y if border of passport not equalent border object int records
//     */
//    public double dyBorderDevice = 0.;

    public int borderExcode;

    public double firstMainParallel;
    public double secondMainParallel;
    public double axisMeridian;
    public double mainPointParallel;
    /**
     * Only {@link SXF#VERSION_4}
     */
    public double poleLatitude;
    /**
     * Only {@link SXF#VERSION_4}
     */
    public double poleLongitude;

    private Geometry _nativeGeometry;
    private Geometry _nativeGeograhy;
    private Geometry _nativeDevice;

    public boolean isFlipCoordinate = false;

    public int getZone() {
        return getZone(this);
    }

    public static int getZone(SXFPassport sxfPassport) {
        double y = Math.max(sxfPassport.yNorthWest, sxfPassport.ySouthWest);
        int zone = (int) (y / 1000000.);
        return zone;
    }

    public Coordinate fromDescret(Coordinate coordinate) {
        return fromDescret(this, coordinate);
    }

    /**
     * Convert (x, y, z) coordinates from descrets (if {@link #realPlaceFlag} != 0 and {@link #dataProjectionFlag} is true) to plane coordinates.
     * @param coordinate source coordinates in descrets.
     * @return converted plane coordinate.
     */
    public static Coordinate fromDescret(SXFPassport sxfPassport, Coordinate coordinate) {
        if (!sxfPassport.dataProjectionFlag) {
            return coordinate;
        }
        if (sxfPassport.realPlaceFlag != 0) {
            return coordinate;
        }

        double x = sxfPassport.xSouthWest + (coordinate.x - sxfPassport.xBorderDeviceSouthWest) / sxfPassport.deviceCapability * sxfPassport.scale + sxfPassport.dx0;
        double y = sxfPassport.ySouthWest + (coordinate.y - sxfPassport.yBorderDeviceSouthWest) / sxfPassport.deviceCapability * sxfPassport.scale + sxfPassport.dy0;

        double z = coordinate.z;
        return new Coordinate(x, y, z);
//        if (!sxfPassport.isFlipCoordinate) {
//            return new Coordinate(x, y, z);
//        } else {
//            return new Coordinate(y, x, z);
//        }
    }

    public Geometry getNativeGeometry() {
        return getNativeGeometry(false);
    }

    public Geometry getNativeGeometry(boolean flipCoordinates) {
        if (_nativeGeometry != null) {
            return _nativeGeometry;
        }
        GeometryFactory geometryFactory = new GeometryFactory(new PrecisionModel(PrecisionModel.FLOATING_SINGLE), Utils.detectSRID(this));

        Coordinate[] coordinates = new Coordinate[5];
        if (!flipCoordinates) {
            coordinates[0] = new Coordinate(xSouthWest, ySouthWest, 0.);
            coordinates[1] = new Coordinate(xNorthWest, yNorthWest, 0.);
            coordinates[2] = new Coordinate(xNorthEast, yNorthEast, 0.);
            coordinates[3] = new Coordinate(xSouthEast, ySouthEast, 0.);
            coordinates[4] = new Coordinate(xSouthWest, ySouthWest, 0.);
        } else {
            coordinates[0] = new Coordinate(ySouthWest, xSouthWest, 0.);
            coordinates[1] = new Coordinate(yNorthWest, xNorthWest, 0.);
            coordinates[2] = new Coordinate(yNorthEast, xNorthEast, 0.);
            coordinates[3] = new Coordinate(ySouthEast, xSouthEast, 0.);
            coordinates[4] = new Coordinate(ySouthWest, xSouthWest, 0.);
        }

        LinearRing linearRing = geometryFactory.createLinearRing(coordinates);
        _nativeGeometry = geometryFactory.createPolygon(linearRing);
        return _nativeGeometry;
    }

    public Geometry getNativeGeography() {
        return getNativeGeography(false);
    }

    public Geometry getNativeGeography(boolean flipCoordinates) {
        if (_nativeGeograhy != null) {
            return _nativeGeograhy;
        }
        GeometryFactory geometryFactory = new GeometryFactory(new PrecisionModel(PrecisionModel.FLOATING_SINGLE));

        Coordinate[] coordinates = new Coordinate[5];
        if (!flipCoordinates) {
            coordinates[0] = new Coordinate(bSouthWest, lSouthWest, 0.);
            coordinates[1] = new Coordinate(bNorthWest, lNorthWest, 0.);
            coordinates[2] = new Coordinate(bNorthEast, lNorthEast, 0.);
            coordinates[3] = new Coordinate(bSouthEast, lSouthEast, 0.);
            coordinates[4] = new Coordinate(bSouthWest, lSouthWest, 0.);
        } else {
            coordinates[0] = new Coordinate(lSouthWest, bSouthWest, 0.);
            coordinates[1] = new Coordinate(lNorthWest, bNorthWest, 0.);
            coordinates[2] = new Coordinate(lNorthEast, bNorthEast, 0.);
            coordinates[3] = new Coordinate(lSouthEast, bSouthEast, 0.);
            coordinates[4] = new Coordinate(lSouthWest, bSouthWest, 0.);
        }

        LinearRing linearRing = geometryFactory.createLinearRing(coordinates);
        _nativeGeograhy = geometryFactory.createPolygon(linearRing);
        return _nativeGeograhy;
    }

    public Geometry getNativeDevice() {
        return getNativeDevice(false);
    }

    public Geometry getNativeDevice(boolean flipCoordinates) {
        if (_nativeDevice != null) {
            return _nativeDevice;
        }
        GeometryFactory geometryFactory = new GeometryFactory(new PrecisionModel(PrecisionModel.FLOATING_SINGLE));

        Coordinate[] coordinates = new Coordinate[5];
        if (!flipCoordinates) {
            coordinates[0] = new Coordinate(xBorderDeviceSouthWest, yBorderDeviceSouthWest, 0.);
            coordinates[1] = new Coordinate(xBorderDeviceNorthWest, yBorderDeviceNorthWest, 0.);
            coordinates[2] = new Coordinate(xBorderDeviceNorthEast, yBorderDeviceNorthEast, 0.);
            coordinates[3] = new Coordinate(xBorderDeviceSouthEast, yBorderDeviceSouthEast, 0.);
            coordinates[4] = new Coordinate(xBorderDeviceSouthWest, yBorderDeviceSouthWest, 0.);
        } else {
            coordinates[0] = new Coordinate(yBorderDeviceSouthWest, xBorderDeviceSouthWest, 0.);
            coordinates[1] = new Coordinate(yBorderDeviceNorthWest, xBorderDeviceNorthWest, 0.);
            coordinates[2] = new Coordinate(yBorderDeviceNorthEast, xBorderDeviceNorthEast, 0.);
            coordinates[3] = new Coordinate(yBorderDeviceSouthEast, xBorderDeviceSouthEast, 0.);
            coordinates[4] = new Coordinate(yBorderDeviceSouthWest, xBorderDeviceSouthWest, 0.);
        }

        LinearRing linearRing = geometryFactory.createLinearRing(coordinates);
        _nativeDevice = geometryFactory.createPolygon(linearRing);
        return _nativeDevice;
    }

    public void print() {
        print(this);
    }

    public static void print(SXFPassport sxfPassport) {
        System.out.printf("Passport Info\n");
        System.out.printf("\tIdentifier:\t\t0x%08x\n", sxfPassport.identifier);
        System.out.printf("\tLength:\t\t\t%d\n",sxfPassport.length);
        System.out.printf("\tVersion:\t\t0x%08x\n", sxfPassport.version);
        System.out.printf("\tCheckSum:\t\t%d\n", sxfPassport.checkSum);
        System.out.printf("\tCreateDate:\t\t%s\n", sxfPassport.createDate);
        System.out.printf("\tNomenclature:\t\t%s\n", sxfPassport.nomenclature);
        System.out.printf("\tScale:\t\t\t%d\n", sxfPassport.scale);
        System.out.printf("\tName:\t\t\t%s\n", sxfPassport.name);
        System.out.printf("\tInfoFlags:\n");
        System.out.printf("\t\tConditionFlag:\t\t%d\n", sxfPassport.conditionFlag);
        if (sxfPassport.version == SXF.VERSION_3) {
            System.out.printf("\t\tProjection flag:\t%b\n", sxfPassport.dataProjectionFlag);
        } else {
            System.out.printf("\t\tAuto GUID:\t\t%b\n", sxfPassport.autoGUID);
        }
        System.out.printf("\t\tRealCoordinatesFlag:\t%d\n", sxfPassport.realPlaceFlag);
        System.out.printf("\t\tCodeTypeFlag:\t\t%d\n", sxfPassport.codeTypeFlag);
        System.out.printf("\t\tGeneralizationFlag:\t%d\n", sxfPassport.generalizationFlag);
        System.out.printf("\t\tTextEncodingFlag:\t%s (%s)\n", sxfPassport.textEncodingFlag, sxfPassport.textEncodingFlag.getName());
        System.out.printf("\t\tCoordPrecisionFlag:\t%d\n", sxfPassport.coordPrecisionFlag);
        System.out.printf("\t\tSpecialSortFlag:\t%d\n", sxfPassport.orderViewSheetFlag);
        if (sxfPassport.version == SXF.VERSION_3) {
            System.out.printf("\tClassificator code:\t%d\n", sxfPassport.code);
        } else {
            if (sxfPassport.epsg == 0) {
                int epsg = Utils.detectSRID(sxfPassport);
                System.out.printf("\tEPSG:\t\t\t%d (Detected from passport constants)\n", epsg);
            } else {
                System.out.printf("\tEPSG:\t\t\t%d\n", sxfPassport.epsg);
            }
        }
        System.out.printf("\tThe geometry coordinates of sheet corners:\n");
        System.out.printf("\t\tX South West:\t%f\n", sxfPassport.xSouthWest);
        System.out.printf("\t\tY South West:\t%f\n", sxfPassport.ySouthWest);
        System.out.printf("\t\tX North West:\t%f\n", sxfPassport.xNorthWest);
        System.out.printf("\t\tY North West:\t%f\n", sxfPassport.yNorthWest);
        System.out.printf("\t\tX North East:\t%f\n", sxfPassport.xNorthEast);
        System.out.printf("\t\tY North East:\t%f\n", sxfPassport.yNorthEast);
        System.out.printf("\t\tX South East:\t%f\n", sxfPassport.xSouthEast);
        System.out.printf("\t\tY South East:\t%f\n", sxfPassport.ySouthEast);
        System.out.printf("\t\tGeometry:\n");
        System.out.printf("\t\t\tWKB: %s\n", SXFRecordMetric.geometryAsWKB(sxfPassport.getNativeGeometry()));
        System.out.printf("\t\t\tWKT: %s\n", SXFRecordMetric.geometryAsWKT(sxfPassport.getNativeGeometry()));
        System.out.printf("\tThe geographic coordinates of sheet corners:\n");
        System.out.printf("\t\tB South West:\t%f\n", sxfPassport.bSouthWest);
        System.out.printf("\t\tL South West:\t%f\n", sxfPassport.lSouthWest);
        System.out.printf("\t\tB North West:\t%f\n", sxfPassport.bNorthWest);
        System.out.printf("\t\tL North West:\t%f\n", sxfPassport.lNorthWest);
        System.out.printf("\t\tB North East:\t%f\n", sxfPassport.bNorthEast);
        System.out.printf("\t\tL North East:\t%f\n", sxfPassport.lNorthEast);
        System.out.printf("\t\tB South East:\t%f\n", sxfPassport.bSouthEast);
        System.out.printf("\t\tL South East:\t%f\n", sxfPassport.lSouthEast);
        System.out.printf("\t\tGeography:\n");
        System.out.printf("\t\t\tWKB: %s\n", SXFRecordMetric.geometryAsWKB(sxfPassport.getNativeGeography()));
        System.out.printf("\t\t\tWKT: %s\n", SXFRecordMetric.geometryAsWKT(sxfPassport.getNativeGeography()));
        System.out.printf("\tThe mathematical basis of the sheet:\n");
        System.out.printf("\t\tEllipsoid kind:\t\t%s (%s)\n", sxfPassport.ellipsoidKind, sxfPassport.ellipsoidKind.getName());
        System.out.printf("\t\tHeight System:\t\t%s (%s)\n", sxfPassport.heightSystem, sxfPassport.heightSystem.getName());
        System.out.printf("\t\tMaterial Projection:\t%s (%s)\n", sxfPassport.materialProjection, sxfPassport.materialProjection.getName());
        System.out.printf("\t\tCoordinate System:\t%s (%s)\n", sxfPassport.coordinateSystem, sxfPassport.coordinateSystem.getName());
        System.out.printf("\t\tPlane Unit:\t\t%s (%s)\n", sxfPassport.planeUnit, sxfPassport.planeUnit.getName());
        System.out.printf("\t\tHeight Unit:\t\t%s (%s)\n", sxfPassport.heightUnit, sxfPassport.heightUnit.getName());
        System.out.printf("\t\tFrame Kind:\t\t%s (%s)\n", sxfPassport.frameKind, sxfPassport.frameKind.getName());
        System.out.printf("\t\tMap Type:\t\t%s (%s)\n", sxfPassport.mapType, sxfPassport.mapType.getName());
        System.out.printf("\tReference data on the source material:\n");
        System.out.printf("\t\tDate:\t\t\t%s\n", sxfPassport.date);
        System.out.printf("\t\tMaterial Init Kind:\t%s (%s)\n", sxfPassport.materialKind, sxfPassport.materialKind.getName());
        System.out.printf("\t\tMaterial Init Type:\t%s (%s)\n", sxfPassport.materialType, sxfPassport.materialType.getName());
        if (sxfPassport.version == SXF.VERSION_4) {
            System.out.printf("\t\tMSK-63 Ident:\t\t%d\n", sxfPassport.msk63Ident);
            System.out.printf("\t\tFrame Border:\t\t%d\n", sxfPassport.frameBorder);
        }
        System.out.printf("\t\tMagnetic Angle:\t\t%f\n", sxfPassport.magneticAngle);
        System.out.printf("\t\tYear Magnetic Angle:\t%f\n", sxfPassport.yearMagneticAngle);
        System.out.printf("\t\tAngle Date:\t\t%s\n", sxfPassport.dateAngle);
        if (sxfPassport.version == SXF.VERSION_4) {
            System.out.printf("\t\tMSK-63 Zone:\t\t%d\n", sxfPassport.msk63Zone);
        }
        System.out.printf("\t\tRelief Height:\t\t%f\n", sxfPassport.reliefHeight);
        if (sxfPassport.version == SXF.VERSION_4) {
            System.out.printf("\tAxis Angle:\t\t%f\n", sxfPassport.axisAngle);
        }
        System.out.printf("\tDevice Capability:\t%d\n", sxfPassport.deviceCapability);
        System.out.printf("\tLocation border on the device:\n");
        System.out.printf("\t\tX Border Device South West:\t%d\n", sxfPassport.xBorderDeviceSouthWest);
        System.out.printf("\t\tY Border Device South West:\t%d\n", sxfPassport.yBorderDeviceSouthWest);
        System.out.printf("\t\tX Border Device North West:\t%d\n", sxfPassport.xBorderDeviceNorthWest);
        System.out.printf("\t\tY Border Device North West:\t%d\n", sxfPassport.yBorderDeviceNorthWest);
        System.out.printf("\t\tX Border Device North East:\t%d\n", sxfPassport.xBorderDeviceNorthEast);
        System.out.printf("\t\tY Border Device North East:\t%d\n", sxfPassport.yBorderDeviceNorthEast);
        System.out.printf("\t\tX Border Device South East:\t%d\n", sxfPassport.xBorderDeviceSouthEast);
        System.out.printf("\t\tY Border Device South East:\t%d\n", sxfPassport.yBorderDeviceSouthEast);
        System.out.printf("\t\tDevice:\n");
        System.out.printf("\t\t\tWKB: %s\n", SXFRecordMetric.geometryAsWKB(sxfPassport.getNativeDevice()));
        System.out.printf("\t\t\tWKT: %s\n", SXFRecordMetric.geometryAsWKT(sxfPassport.getNativeDevice()));
        System.out.printf("\tBorder excode:\t%d\n", sxfPassport.borderExcode);
        System.out.printf("\tSource material projection info:\n");
        System.out.printf("\t\tFirst Main Parallel:\t%f\n", sxfPassport.firstMainParallel);
        System.out.printf("\t\tSecond Main Parallel:\t%f\n", sxfPassport.secondMainParallel);
        System.out.printf("\t\tAxis Meridian:\t\t%f\n", sxfPassport.axisMeridian);
        System.out.printf("\t\tMain Point Parallel:\t%f\n", sxfPassport.mainPointParallel);
        if (sxfPassport.version == SXF.VERSION_4) {
            System.out.printf("\t\tPole Latitude:\t\t%f\n", sxfPassport.poleLatitude);
            System.out.printf("\t\tPole Longitude:\t\t%f\n", sxfPassport.poleLongitude);
        }
        System.out.printf("\tCoordinate system start position:\n");
        System.out.printf("\t\tdx0:\t%f\n", sxfPassport.dx0);
        System.out.printf("\t\tdy0:\t%f\n", sxfPassport.dy0);
    }
}
