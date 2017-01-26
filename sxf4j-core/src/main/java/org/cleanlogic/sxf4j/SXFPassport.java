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

import org.cleanlogic.sxf4j.enums.*;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;

/**
 * @author Serge Silaev aka iSergio <s.serge.b@gmail.com>
 */
public class SXFPassport {
    /**
     * Magic number of SXF format
     */
    public static final int IDENTIFIER = 0x00465853;
    /**
     * SXF 3 version
     */
    public static final int VERSION_3 = 0x00000300;
    /**
     * SXF 4 version
     */
    public static final int VERSION_4 = 0x00040000;
    /**
     * Length of SXF passport of version 3 format.
     */
    public static final int LENGTH_3 = 256;
    /**
     * Length of SXF passport of version 3 format.
     */
    public static final int LENGTH_4 = 400;

    /**
     * File identifier.
     */
    private int identifier;
    /**
     * Length of SXF passport. Depends on version.
     */
    private int length;
    /**
     * Version of SXF file format.
     * {@see #VERSION_3}
     * {@see #VERSION_4}
     */
    private int version;
    /**
     * Checksum for all file without this field.
     */
    private int checkSum;
    /**
     * Date of create SXF
     */
    private String createDate;
    /**
     * Nomenclature of sheet
     */
    private String nomenclature;
    /**
     * Scale of sheet. Denominator scale.
     */
    private int scale;
    /**
     * Conventional sheet name
     */
    private String name;
    /**
     * Condition data flag. Must be = 3!
     */
    private int conditionFlag;
    /**
     * false - data not match with projection (map in rotation or deformation).
     * true  - data match with projection.
     * Only {@link #VERSION_3} can fill from {@link SXFDescriptor} in {@link #VERSION_4}.
     */
    private Projection projectionFlag;
    /**
     * Auto generate GUID.
     * Only {@link #VERSION_4}
     */
    private boolean autoGUID;
    private int realPlaceFlag;
    private int codeTypeFlag;
    private int generalizationFlag;
    private TextEncoding textEncodingFlag;
    private CoordinatePrecision coordinatePrecisionFlag;
    private int orderViewSheetFlag;

    /**
     * Code of classificator
     * Only {@link #VERSION_3}
     */
    private int code;
    /**
     * EPSG code for coordinate system or 0
     */
    private int srid;
    /**
     * X
     * |
     * |
     * |
     * |
     * +------------Y
     * X south west corner. Meters.
     */
    private double xSouthWest;
    /**
     * Y south west corner. Meters.
     */
    private double ySouthWest;
    /**
     * X north west corner. Meters.
     */
    private double xNorthWest;
    /**
     * Y north west corner. Meters.
     */
    private double yNorthWest;
    /**
     * X north east corner. Meters.
     */
    private double xNorthEast;
    /**
     * Y north east corner. Meters.
     */
    private double yNorthEast;
    /**
     * X south east corner. Meters.
     */
    private double xSouthEast;
    /**
     * Y south east corner. Meters.
     */
    private double ySouthEast;
    /**
     * B south west. Degrees.
     */
    private double bSouthWest;
    /**
     * L south west. Degrees.
     */
    private double lSouthWest;
    /**
     * B north west. Degrees.
     */
    private double bNorthWest;
    /**
     * L north west. Degrees.
     */
    private double lNorthWest;
    /**
     * B north east. Degrees.
     */
    private double bNorthEast;
    /**
     * L north east. Degrees.
     */
    private double lNorthEast;
    /**
     * B south east. Degrees.
     */
    private double bSouthEast;
    /**
     * B south east. Degrees.
     */
    private double lSouthEast;

    private EllipsoidKind ellipsoidKind;
    private HeightSystem heightSystem;
    private MapProjection mapProjection;
    private CoordinateSystem coordinateSystem;
    private Unit planeUnit;
    private Unit heightUnit;
    private FrameKind frameKind;
    private MapType mapType;

    /**
     * Date surveying or map updates.
     * Format: YYYYMMDD
     */
    private String date;
    /**
     * Source material kind.
     */
    private MapInitKind materialKind;
    /**
     * Source material type.
     */
    private MapInitType materialType;
    /**
     * Ident MSK-63.
     * Only {@link #VERSION_4}
     */
    private int msk63Ident;
    /**
     * Limited evidence card frame.
     * Only {@link #VERSION_4}
     */
    private int frameBorder;
    /**
     * Magnetic declination.
     */
    private double magneticAngle;
    /**
     * Mean convergence of meridians.
     */
    private double meridianAngle;
    /**
     * Annual magnetic declination.
     */
    private double yearMagneticAngle;
    /**
     * Date of declination.
     * Format: YYYYMMDD
     */
    private String dateAngle;
    /**
     * Number MSK-63 zone
     * Only {@link #VERSION_4}
     */
    private int msk63Zone;
    /**
     * Vertical interval. Meters.
     */
    private double reliefHeight;
    /**
     * Angle rotation axes for local coordinate systems
     * Only {@link #VERSION_4}
     */
    private double axisAngle;

    private int deviceCapability;

    private int xBorderDeviceSouthWest;
    private int yBorderDeviceSouthWest;
    private int xBorderDeviceNorthWest;
    private int yBorderDeviceNorthWest;
    private int xBorderDeviceNorthEast;
    private int yBorderDeviceNorthEast;
    private int xBorderDeviceSouthEast;
    private int yBorderDeviceSouthEast;

    private int borderExcode;

    private double firstMainParallel;
    private double secondMainParallel;
    private double axisMeridian;
    private double mainPointParallel;
    /**
     * Only {@link #VERSION_4}
     */
    private double poleLatitude;
    /**
     * Only {@link #VERSION_4}
     */
    private double poleLongitude;

    /**
     * Coordinate system start X coordinate.
     */
    private double dx0 = 0.;
    /**
     * Coordinate system start Y coordinate.
     */
    private double dy0 = 0.;

    private boolean isReadOnly;

    /**
     * Function check SXF magic number.
     * @param strict Show message through println or IOException.
     * @throws IOException exception if wrong.
     */
    private void checkIdentifier(boolean strict) throws IOException {
        if (identifier != IDENTIFIER) {
            String message = "Wrong identifier(magic number) of SXF Passport, expected " + IDENTIFIER + ", got " + identifier;
            if (!strict) {
                System.err.println(message);
            } else {
                throw new IOException(message);
            }
        }
    }

    /**
     * Function check SXF version .
     * @param strict Show message through println or IOException.
     * @throws IOException exception if wrong.
     */
    private void checkVersion(boolean strict) throws IOException {
        if (version != VERSION_3 && version != VERSION_4) {
            String message = "Not supported version - " + version + ". Supported " + VERSION_3 + " and " + VERSION_4;
            if (!strict) {
                System.err.println(message);
            } else {
                throw new IOException(message);
            }
        }
    }

    /**
     * Fill {@link SXFPassport} fields.
     * @param buffer opened ByteBuffer of file.
     * @param strict Messages format on Exceptions.
     * @throws IOException If problems arise.
     */
    public void read(ByteBuffer buffer, boolean strict) throws IOException {
        isReadOnly = buffer.isReadOnly();

        buffer.order(ByteOrder.LITTLE_ENDIAN);

        identifier = buffer.getInt();
        checkIdentifier(strict);

        length = buffer.getInt();

        if (length == LENGTH_3) {
            version = buffer.getShort();
        } else if (length == LENGTH_4) {
            version = buffer.getInt();
        }
        checkVersion(strict);

        checkSum = buffer.getInt();

        if (version == VERSION_3) {
            read3(buffer, strict);
        } else if (version == VERSION_4) {
            read4(buffer, strict);
        }

        // Set or correct SRID code
        srid = srid();
    }

    /**
     * Current version of opened SXF file.
     * @return current version.
     */
    public int getVersion() {
        return version;
    }

    /**
     * Get length of SXFPassport.
     * @return length of passport (must be 256 or 400).
     */
    public int getLength() {
        return length;
    }

    public int getBorderExcode() {
        return borderExcode;
    }

    public boolean isDescrets() {
        return ((projectionFlag == Projection.ADEQUACY) && realPlaceFlag == 0);
    }

    public double[][] getXY() {
        return new double[][] {
                new double[] {xSouthWest, ySouthWest},
                new double[] {xNorthWest, yNorthWest},
                new double[] {xNorthEast, yNorthEast},
                new double[] {xSouthEast, ySouthEast}};
    }

    public double[][] getBL() {
        return new double[][] {
                new double[] {bSouthWest, lSouthWest},
                new double[] {bNorthWest, lNorthWest},
                new double[] {bNorthEast, lNorthEast},
                new double[] {bSouthEast, lSouthEast}};
    }

    public double[][] getDeviceXY() {
        return new double[][] {
                new double[] {xBorderDeviceSouthWest, yBorderDeviceSouthWest},
                new double[] {xBorderDeviceNorthWest, yBorderDeviceNorthWest},
                new double[] {xBorderDeviceNorthEast, yBorderDeviceNorthEast},
                new double[] {xBorderDeviceSouthEast, yBorderDeviceSouthEast}};
    }

    public int getDeviceCapability() {
        return deviceCapability;
    }

    public int getScale() {
        return scale;
    }

    public double[] getDXY0() {
        return new double[] {dx0, dy0};
    }

    public void setDXY0(double[] dxy0) {
        dx0 = dxy0[0];
        dy0 = dxy0[1];
    }
    public void setProjectionFlag(Projection projectionFlag) {
        if (version == VERSION_4) {
            this.projectionFlag = projectionFlag;
        }
    }

    public int srid() {
        // In some files srid sets to 65537
        if (srid == 65537) {
            srid = 0;
        }

        if (srid == 0) {
            if (mapType == MapType.LATLONG && ellipsoidKind == EllipsoidKind.WGS_84 &&
                    coordinateSystem == CoordinateSystem.GEOCOORDINATE &&  planeUnit == Unit.METRE &&
                    mapProjection == MapProjection.LATITUDELONGITUDE) {
                srid = 4326;
            }
            if (mapType == MapType.MERCATOR &&
                    (ellipsoidKind == EllipsoidKind.WGS_84 || ellipsoidKind == EllipsoidKind.SPHERE_WGS_84) &&
                    coordinateSystem == CoordinateSystem.CONDITION && planeUnit == Unit.METRE &&
                    mapProjection == MapProjection.WEBMERCATOR) {
                srid = 3857;
            }
            if (mapType == MapType.TOPOGRAPHIC && ellipsoidKind == EllipsoidKind.KRASOVSKY42 &&
                    coordinateSystem == CoordinateSystem.ORTHOGONAL && planeUnit == Unit.METRE &&
                    mapProjection == MapProjection.GAUSSCONFORMAL) {
                int zone = getZone();
                srid =  28400 + zone;
            }
        }
        return srid;
    }

    public int getZone() {
        double y = Math.max(yNorthWest, ySouthWest);
        int zone = (int) (y / 1000000.);
        return zone;
    }

    public String getNomenclature() {
        return nomenclature;
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
        if (sxfPassport.version == VERSION_3) {
            System.out.printf("\t\tProjection flag:\t%s (%s)\n", sxfPassport.projectionFlag, sxfPassport.projectionFlag.getName());
        } else {
            System.out.printf("\t\tAuto GUID:\t\t%b\n", sxfPassport.autoGUID);
        }
        System.out.printf("\t\tRealCoordinatesFlag:\t%d\n", sxfPassport.realPlaceFlag);
        System.out.printf("\t\tCodeTypeFlag:\t\t%d\n", sxfPassport.codeTypeFlag);
        System.out.printf("\t\tGeneralizationFlag:\t%d\n", sxfPassport.generalizationFlag);
        System.out.printf("\t\tTextEncodingFlag:\t%s (%s)\n", sxfPassport.textEncodingFlag, sxfPassport.textEncodingFlag.getName());
        if (sxfPassport.version == VERSION_4) {
            System.out.printf("\t\tCoordPrecisionFlag:\t%s (%s)\n", sxfPassport.coordinatePrecisionFlag, sxfPassport.coordinatePrecisionFlag.getName());
        }
        System.out.printf("\t\tSpecialSortFlag:\t%d\n", sxfPassport.orderViewSheetFlag);
        if (sxfPassport.version == VERSION_3) {
            System.out.printf("\tClassificator code:\t%d\n", sxfPassport.code);
        } else {
            System.out.printf("\tEPSG:\t\t\t%d\n", sxfPassport.srid);
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
//        System.out.printf("\t\t\tWKB: %s\n", Utils.geometryAsWKB(sxfPassport.getNativeGeometry()));
//        System.out.printf("\t\t\tWKT: %s\n", Utils.geometryAsWKT(sxfPassport.getNativeGeometry()));
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
//        System.out.printf("\t\t\tWKB: %s\n", Utils.geometryAsWKB(sxfPassport.getNativeGeography()));
//        System.out.printf("\t\t\tWKT: %s\n", Utils.geometryAsWKT(sxfPassport.getNativeGeography()));
        System.out.printf("\tThe mathematical basis of the sheet:\n");
        System.out.printf("\t\tEllipsoid kind:\t\t%s (%s)\n", sxfPassport.ellipsoidKind, sxfPassport.ellipsoidKind.getName());
        System.out.printf("\t\tHeight System:\t\t%s (%s)\n", sxfPassport.heightSystem, sxfPassport.heightSystem.getName());
        System.out.printf("\t\tMaterial Projection:\t%s (%s)\n", sxfPassport.mapProjection, sxfPassport.mapProjection.getName());
        System.out.printf("\t\tCoordinate System:\t%s (%s)\n", sxfPassport.coordinateSystem, sxfPassport.coordinateSystem.getName());
        System.out.printf("\t\tPlane Unit:\t\t%s (%s)\n", sxfPassport.planeUnit, sxfPassport.planeUnit.getName());
        System.out.printf("\t\tHeight Unit:\t\t%s (%s)\n", sxfPassport.heightUnit, sxfPassport.heightUnit.getName());
        System.out.printf("\t\tFrame Kind:\t\t%s (%s)\n", sxfPassport.frameKind, sxfPassport.frameKind.getName());
        System.out.printf("\t\tMap Type:\t\t%s (%s)\n", sxfPassport.mapType, sxfPassport.mapType.getName());
        System.out.printf("\tReference data on the source material:\n");
        System.out.printf("\t\tDate:\t\t\t%s\n", sxfPassport.date);
        System.out.printf("\t\tMaterial Init Kind:\t%s (%s)\n", sxfPassport.materialKind, sxfPassport.materialKind.getName());
        System.out.printf("\t\tMaterial Init Type:\t%s (%s)\n", sxfPassport.materialType, sxfPassport.materialType.getName());
        if (sxfPassport.version == VERSION_4) {
            System.out.printf("\t\tMSK-63 Ident:\t\t%d\n", sxfPassport.msk63Ident);
            System.out.printf("\t\tFrame Border:\t\t%d\n", sxfPassport.frameBorder);
        }
        System.out.printf("\t\tMagnetic Angle:\t\t%f\n", sxfPassport.magneticAngle);
        System.out.printf("\t\tYear Magnetic Angle:\t%f\n", sxfPassport.yearMagneticAngle);
        System.out.printf("\t\tAngle Date:\t\t%s\n", sxfPassport.dateAngle);
        if (sxfPassport.version == VERSION_4) {
            System.out.printf("\t\tMSK-63 Zone:\t\t%d\n", sxfPassport.msk63Zone);
        }
        System.out.printf("\t\tRelief Height:\t\t%f\n", sxfPassport.reliefHeight);
        if (sxfPassport.version == VERSION_4) {
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
//        System.out.printf("\t\t\tWKB: %s\n", Utils.geometryAsWKB(sxfPassport.getNativeDevice()));
//        System.out.printf("\t\t\tWKT: %s\n", Utils.geometryAsWKT(sxfPassport.getNativeDevice()));
        System.out.printf("\tBorder excode:\t%d\n", sxfPassport.borderExcode);
        System.out.printf("\tSource material projection info:\n");
        System.out.printf("\t\tFirst Main Parallel:\t%f\n", sxfPassport.firstMainParallel);
        System.out.printf("\t\tSecond Main Parallel:\t%f\n", sxfPassport.secondMainParallel);
        System.out.printf("\t\tAxis Meridian:\t\t%f\n", sxfPassport.axisMeridian);
        System.out.printf("\t\tMain Point Parallel:\t%f\n", sxfPassport.mainPointParallel);
        if (sxfPassport.version == VERSION_4) {
            System.out.printf("\t\tPole Latitude:\t\t%f\n", sxfPassport.poleLatitude);
            System.out.printf("\t\tPole Longitude:\t\t%f\n", sxfPassport.poleLongitude);
        }
        System.out.printf("\tCoordinate system start position:\n");
        System.out.printf("\t\tdx0:\t%f\n", sxfPassport.dx0);
        System.out.printf("\t\tdy0:\t%f\n", sxfPassport.dy0);
    }

    /**
     * Fill {@link SXFPassport} of 3 version fields.
     * @param buffer opened ByteBuffer of file.
     * @param strict Messages format on Exceptions.
     * @throws IOException If problems arise.
     */
    private void read3(ByteBuffer buffer, boolean strict) throws IOException {
        String textEncoding = TextEncoding.IBM866.getName();

        byte[] createDate = new byte[10];
        buffer.get(createDate);
        this.createDate = new String(createDate).trim().intern();

        byte[] nomenclature = new byte[24];
        buffer.get(nomenclature);
        this.nomenclature = new String(nomenclature, textEncoding).trim().intern();

        scale = buffer.getInt();

        byte[] name = new byte[26];
        buffer.get(name);
        this.name = new String(name, textEncoding).trim().intern();

        byte[] infoFlags = new byte[4];
        buffer.get(infoFlags);
        conditionFlag = infoFlags[0] & 0x3;
        projectionFlag = Projection.fromValue((infoFlags[0] >> 2) & 0x1);
        realPlaceFlag = (infoFlags[0] >> 3) & 0x3;
        codeTypeFlag = (infoFlags[0] >> 5) & 0x3;
        generalizationFlag = (infoFlags[0] >> 7) & 0x1;

        textEncodingFlag = TextEncoding.IBM866;

        code = buffer.getInt();

        // Reserve = 8
        buffer.position(buffer.position() + 8);

        xSouthWest = buffer.getInt() / 10.;
        ySouthWest = buffer.getInt() / 10.;
        xNorthWest = buffer.getInt() / 10.;
        yNorthWest = buffer.getInt() / 10.;
        xNorthEast = buffer.getInt() / 10.;
        yNorthEast = buffer.getInt() / 10.;
        xSouthEast = buffer.getInt() / 10.;
        ySouthEast = buffer.getInt() / 10.;

        bSouthWest = buffer.getInt() / Math.pow(10., 8.) * 180.0 / Math.PI;
        lSouthWest = buffer.getInt() / Math.pow(10., 8.) * 180.0 / Math.PI;
        bNorthWest = buffer.getInt() / Math.pow(10., 8.) * 180.0 / Math.PI;
        lNorthWest = buffer.getInt() / Math.pow(10., 8.) * 180.0 / Math.PI;
        bNorthEast = buffer.getInt() / Math.pow(10., 8.) * 180.0 / Math.PI;
        lNorthEast = buffer.getInt() / Math.pow(10., 8.) * 180.0 / Math.PI;
        bSouthEast = buffer.getInt() / Math.pow(10., 8.) * 180.0 / Math.PI;
        lSouthEast = buffer.getInt() / Math.pow(10., 8.) * 180.0 / Math.PI;

        byte[] mathematicalBasis = new byte[8];
        buffer.get(mathematicalBasis);
        ellipsoidKind = EllipsoidKind.fromValue(mathematicalBasis[0]);
        heightSystem = HeightSystem.fromValue(mathematicalBasis[1]);
        mapProjection = MapProjection.fromValue(mathematicalBasis[2]);
        coordinateSystem = CoordinateSystem.fromValue(mathematicalBasis[3]);
        planeUnit = Unit.fromValue(mathematicalBasis[4]);
        heightUnit = Unit.fromValue(mathematicalBasis[5]);
        frameKind = FrameKind.fromValue(mathematicalBasis[6]);
        mapType = MapType.fromValue(mathematicalBasis[7]);

        byte[] date = new byte[10];
        buffer.get(date);
        byte[] sourceInfo = new byte[2];
        buffer.get(sourceInfo);
        this.date = new String(date).trim();
        materialKind = MapInitKind.fromValue(sourceInfo[0]);
        materialType = MapInitType.fromValue(sourceInfo[1]);
        magneticAngle = buffer.getInt() / Math.pow(10., 8.) * 180.0 / Math.PI;
        meridianAngle = buffer.getInt() / Math.pow(10., 8.) * 180.0 / Math.PI;
        reliefHeight = buffer.getShort();// / 10.; // In metres. Bug in Documentation ?
        yearMagneticAngle = buffer.getInt() / Math.pow(10., 8.) * 180.0 / Math.PI;
        byte[] dateAngle = new byte[10];
        buffer.get(dateAngle);
        this.dateAngle = new String(dateAngle).trim();
        // Reserve = 10
        buffer.position(buffer.position() + 10);

        deviceCapability = buffer.getInt();

        xBorderDeviceSouthWest = buffer.getShort();
        yBorderDeviceSouthWest = buffer.getShort();
        xBorderDeviceNorthWest = buffer.getShort();
        yBorderDeviceNorthWest = buffer.getShort();
        xBorderDeviceNorthEast = buffer.getShort();
        yBorderDeviceNorthEast = buffer.getShort();
        xBorderDeviceSouthEast = buffer.getShort();
        yBorderDeviceSouthEast = buffer.getShort();

        borderExcode = buffer.getInt();

        firstMainParallel = buffer.getInt() / Math.pow(10., 8.) * 180.0 / Math.PI;
        secondMainParallel = buffer.getInt() / Math.pow(10., 8.) * 180.0 / Math.PI;
        axisMeridian  = buffer.getInt() / Math.pow(10., 8.) * 180.0 / Math.PI;
        mainPointParallel = buffer.getInt() / Math.pow(10., 8.) * 180.0 / Math.PI;
        // Reserve = 4
        buffer.position(buffer.position() + 4);
    }

    /**
     * Fill {@link SXFPassport} of 4 version fields.
     * @param buffer opened ByteBuffer of file.
     * @param strict Messages format on Exceptions.
     * @throws IOException If problems arise.
     */
    private void read4(ByteBuffer buffer, boolean strict) throws IOException {
        String textEncoding = TextEncoding.CP1251.getName();

        byte[] createDate = new byte[12];
        buffer.get(createDate);
        this.createDate = new String(createDate).trim().intern();

        byte[] nomenclature = new byte[32];
        buffer.get(nomenclature);
        this.nomenclature = new String(nomenclature, textEncoding).trim().intern();

        scale = buffer.getInt();

        byte[] name = new byte[32];
        buffer.get(name);
        this.name = new String(name, textEncoding).trim().intern();
        byte[] infoFlags = new byte[4];
        buffer.get(infoFlags);
        conditionFlag = infoFlags[0] & 0x3;
        autoGUID = (((infoFlags[0] >> 2) & 0x1) == 1);
        realPlaceFlag = (infoFlags[0] >> 3) & 0x3;
        codeTypeFlag = (infoFlags[0] >> 5) & 0x3;
        generalizationFlag = (infoFlags[0] >> 7) & 0x1;

        textEncodingFlag = TextEncoding.fromValue(infoFlags[1]);
        coordinatePrecisionFlag = CoordinatePrecision.fromValue(infoFlags[2]);
        orderViewSheetFlag = infoFlags[3] & 0x1;

        srid = buffer.getInt();

        xSouthWest = buffer.getDouble();
        ySouthWest = buffer.getDouble();
        xNorthWest = buffer.getDouble();
        yNorthWest = buffer.getDouble();
        xNorthEast = buffer.getDouble();
        yNorthEast = buffer.getDouble();
        xSouthEast = buffer.getDouble();
        ySouthEast = buffer.getDouble();

        bSouthWest = buffer.getDouble() * 180.0 / Math.PI;
        lSouthWest = buffer.getDouble() * 180.0 / Math.PI;
        bNorthWest = buffer.getDouble() * 180.0 / Math.PI;
        lNorthWest = buffer.getDouble() * 180.0 / Math.PI;
        bNorthEast = buffer.getDouble() * 180.0 / Math.PI;
        lNorthEast = buffer.getDouble() * 180.0 / Math.PI;
        bSouthEast = buffer.getDouble() * 180.0 / Math.PI;
        lSouthEast = buffer.getDouble() * 180.0 / Math.PI;

        byte[] mathematicalBasis = new byte[8];
        buffer.get(mathematicalBasis);
        ellipsoidKind = EllipsoidKind.fromValue(mathematicalBasis[0]);
        heightSystem = HeightSystem.fromValue(mathematicalBasis[1]);
        mapProjection = MapProjection.fromValue(mathematicalBasis[2]);
        coordinateSystem = CoordinateSystem.fromValue(mathematicalBasis[3]);
        planeUnit = Unit.fromValue(mathematicalBasis[4]);
        heightUnit = Unit.fromValue(mathematicalBasis[5]);
        frameKind = FrameKind.fromValue(mathematicalBasis[6]);
        mapType = MapType.fromValue(mathematicalBasis[7]);

        byte[] date = new byte[12];
        buffer.get(date);
        byte[] sourceInfo = new byte[4];
        buffer.get(sourceInfo);
        this.date = new String(date).trim();
        materialKind = MapInitKind.fromValue(sourceInfo[0]);
        materialType = MapInitType.fromValue(sourceInfo[1]);
        msk63Ident = sourceInfo[2];
        frameBorder = sourceInfo[3];
        magneticAngle = buffer.getDouble() * 180.0 / Math.PI;
        meridianAngle = buffer.getDouble() * 180.0 / Math.PI;
        yearMagneticAngle = buffer.getDouble() * 180.0 / Math.PI;
        byte[] dateAngle = new byte[12];
        buffer.get(dateAngle);
        this.dateAngle = new String(dateAngle).trim().intern();
        msk63Zone = buffer.getInt();
        reliefHeight = buffer.getDouble();

        axisAngle = buffer.getDouble() * 180.0 / Math.PI;

        deviceCapability = buffer.getInt();

        xBorderDeviceSouthWest = buffer.getInt();
        yBorderDeviceSouthWest = buffer.getInt();
        xBorderDeviceNorthWest = buffer.getInt();
        yBorderDeviceNorthWest = buffer.getInt();
        xBorderDeviceNorthEast = buffer.getInt();
        yBorderDeviceNorthEast = buffer.getInt();
        xBorderDeviceSouthEast = buffer.getInt();
        yBorderDeviceSouthEast = buffer.getInt();

        borderExcode = buffer.getInt();

        firstMainParallel = buffer.getDouble() * 180.0 / Math.PI;
        secondMainParallel = buffer.getDouble() * 180.0 / Math.PI;
        axisMeridian  = buffer.getDouble() * 180.0 / Math.PI;
        mainPointParallel = buffer.getDouble() * 180.0 / Math.PI;
        poleLatitude = buffer.getDouble() * 180.0 / Math.PI;
        poleLongitude = buffer.getDouble() * 180.0 / Math.PI;
    }
}
