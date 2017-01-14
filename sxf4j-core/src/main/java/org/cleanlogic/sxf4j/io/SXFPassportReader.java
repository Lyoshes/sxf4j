package org.cleanlogic.sxf4j.io;

import org.cleanlogic.sxf4j.SXF;
import org.cleanlogic.sxf4j.enums.*;
import org.cleanlogic.sxf4j.exceptions.SXFNotSupportedVersion;
import org.cleanlogic.sxf4j.exceptions.SXFPassportReadError;
import org.cleanlogic.sxf4j.exceptions.SXFWrongFormatException;
import org.cleanlogic.sxf4j.fixes.SXFPassportFixes;
import org.cleanlogic.sxf4j.format.SXFPassport;

import java.io.File;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.ByteOrder;
import java.nio.MappedByteBuffer;
import java.nio.channels.FileChannel;

/**
 * @author Serge Silaev aka iSergio <s.serge.b@gmail.com>
 */
public class SXFPassportReader {
    private RandomAccessFile _randomAccessFile;
    private MappedByteBuffer _mappedByteBuffer;
    private SXFPassport _sxfPassport;

    public SXFPassportReader() {}

    public SXFPassportReader(String filePath) throws IOException {
        _randomAccessFile = new RandomAccessFile(filePath, "r");

        _mappedByteBuffer = _randomAccessFile.getChannel().map(FileChannel.MapMode.READ_ONLY, 0, _randomAccessFile.length());
        _mappedByteBuffer.order(ByteOrder.LITTLE_ENDIAN);
    }

    public SXFPassportReader(File file) throws IOException {
        _randomAccessFile = new RandomAccessFile(file, "r");

        _mappedByteBuffer = _randomAccessFile.getChannel().map(FileChannel.MapMode.READ_ONLY, 0, _randomAccessFile.length());
        _mappedByteBuffer.order(ByteOrder.LITTLE_ENDIAN);
    }

    public SXFPassportReader(MappedByteBuffer mappedByteBuffer) {
        _mappedByteBuffer = mappedByteBuffer;
        _mappedByteBuffer.order(ByteOrder.LITTLE_ENDIAN);
    }


    public MappedByteBuffer getMappedByteBuffer() {
        return _mappedByteBuffer;
    }

    /**
     * Process read SXF passport
     * @return SXFPassport which will be read
     */
    public SXFPassport read() throws SXFWrongFormatException {
        if (_mappedByteBuffer.position() != 0) {
            _mappedByteBuffer.position(0);
        }

        if (_sxfPassport != null) {
            _sxfPassport = null;
        }

        _sxfPassport = new SXFPassport();
        _sxfPassport.identifier = _mappedByteBuffer.getInt();
        if (_sxfPassport.identifier != SXF.FILE_SXF) {
            throw new SXFWrongFormatException("Identifier " + _sxfPassport.identifier + " Is not SXF Format");
        }

        // Read SXF passport length
        _sxfPassport.length = _mappedByteBuffer.getInt();

        // Try read version of SXF format
        if (_sxfPassport.length == SXF.PASSPORT_3_LENGHT) {
            _sxfPassport.version = _mappedByteBuffer.getShort();
        } else if (_sxfPassport.length == SXF.PASSPORT_4_LENGHT) {
            _sxfPassport.version = _mappedByteBuffer.getInt();
        } else {
            throw new SXFPassportReadError("Passport length " + _sxfPassport.length + " is wrong");
        }

        if (_sxfPassport.version != SXF.VERSION_3 && _sxfPassport.version != SXF.VERSION_4) {
            throw new SXFNotSupportedVersion(String.format("Version 0x%8x not supported", _sxfPassport.version));
        }

        if (_sxfPassport.version == SXF.VERSION_3 && _sxfPassport.length != SXF.PASSPORT_3_LENGHT) {
            throw new SXFPassportReadError("Version of SXF file is 3 but passport length is not 256");
        } else if (_sxfPassport.version == SXF.VERSION_4 && _sxfPassport.length != SXF.PASSPORT_4_LENGHT) {
            throw new SXFPassportReadError("Version of SXF file is 4 but passport length is not 400");
        }

        if (_sxfPassport.version == SXF.VERSION_3) {
            readVersion3();
        } else {
            readVersion4();
        }

        SXFPassportFixes.FixMapInitKind(_sxfPassport);
        SXFPassportFixes.FixMapInitType(_sxfPassport);
        SXFPassportFixes.FixSXFPassportAxisMeridian(_sxfPassport);

        return _sxfPassport;
    }

    /**
     * Reader for {@link SXF#VERSION_3} SXF format
     */
    private void readVersion3() {
        _sxfPassport.checkSum = _mappedByteBuffer.getInt();

        byte[] createDate = new byte[10];
        _mappedByteBuffer.get(createDate);
        _sxfPassport.createDate = new String(createDate);

        byte[] nomenclature = new byte[24];
        _mappedByteBuffer.get(nomenclature);
        _sxfPassport.nomenclature = new String(nomenclature);
        _sxfPassport.scale = _mappedByteBuffer.getInt();

        byte[] name = new byte[26];
        _mappedByteBuffer.get(name);
        _sxfPassport.name = new String(name);

        byte[] infoFlags = new byte[4];
        _mappedByteBuffer.get(infoFlags);
        _sxfPassport.conditionFlag = infoFlags[0] & 0x3;
        _sxfPassport.dataProjectionFlag = (((infoFlags[0] >> 2) & 0x1) == 1);
        _sxfPassport.realPlaceFlag = (infoFlags[0] >> 3) & 0x3;
        _sxfPassport.codeTypeFlag = (infoFlags[0] >> 5) & 0x3;
        _sxfPassport.generalizationFlag = (infoFlags[0] >> 7) & 0x1;

        _sxfPassport.textEncodingFlag = TextEncoding.ASCIIZ;

        _sxfPassport.code = _mappedByteBuffer.getInt();

        // Reserve = 8
        _mappedByteBuffer.position(_mappedByteBuffer.position() + 8);

        _sxfPassport.xSouthWest = _mappedByteBuffer.getInt() / 10.;
        _sxfPassport.ySouthWest = _mappedByteBuffer.getInt() / 10.;
        _sxfPassport.xNorthWest = _mappedByteBuffer.getInt() / 10.;
        _sxfPassport.yNorthWest = _mappedByteBuffer.getInt() / 10.;
        _sxfPassport.xNorthEast = _mappedByteBuffer.getInt() / 10.;
        _sxfPassport.yNorthEast = _mappedByteBuffer.getInt() / 10.;
        _sxfPassport.xSouthEast = _mappedByteBuffer.getInt() / 10.;
        _sxfPassport.ySouthEast = _mappedByteBuffer.getInt() / 10.;

        _sxfPassport.bSouthWest = _mappedByteBuffer.getInt() / Math.pow(10., 8.) * 180.0 / Math.PI;
        _sxfPassport.lSouthWest = _mappedByteBuffer.getInt() / Math.pow(10., 8.) * 180.0 / Math.PI;
        _sxfPassport.bNorthWest = _mappedByteBuffer.getInt() / Math.pow(10., 8.) * 180.0 / Math.PI;
        _sxfPassport.lNorthWest = _mappedByteBuffer.getInt() / Math.pow(10., 8.) * 180.0 / Math.PI;
        _sxfPassport.bNorthEast = _mappedByteBuffer.getInt() / Math.pow(10., 8.) * 180.0 / Math.PI;
        _sxfPassport.lNorthEast = _mappedByteBuffer.getInt() / Math.pow(10., 8.) * 180.0 / Math.PI;
        _sxfPassport.bSouthEast = _mappedByteBuffer.getInt() / Math.pow(10., 8.) * 180.0 / Math.PI;
        _sxfPassport.lSouthEast = _mappedByteBuffer.getInt() / Math.pow(10., 8.) * 180.0 / Math.PI;

        byte[] mathematicalBasis = new byte[8];
        _mappedByteBuffer.get(mathematicalBasis);
        _sxfPassport.ellipsoidKind = EllipsoidKind.fromValue(mathematicalBasis[0]);
        _sxfPassport.heightSystem = HeightSystem.fromValue(mathematicalBasis[1]);
        _sxfPassport.materialProjection = MapProjection.fromValue(mathematicalBasis[2]);
        _sxfPassport.coordinateSystem = CoordinateSystem.fromValue(mathematicalBasis[3]);
        _sxfPassport.planeUnit = Unit.fromValue(mathematicalBasis[4]);
        _sxfPassport.heightUnit = Unit.fromValue(mathematicalBasis[5]);
        _sxfPassport.frameKind = FrameKind.fromValue(mathematicalBasis[6]);
        _sxfPassport.mapType = MapType.fromValue(mathematicalBasis[7]);

        byte[] date = new byte[10];
        _mappedByteBuffer.get(date);
        byte[] sourceInfo = new byte[2];
        _mappedByteBuffer.get(sourceInfo);
        _sxfPassport.date = new String(date);
        _sxfPassport.materialKind = MapInitKind.fromValue(sourceInfo[0]);
        _sxfPassport.materialType = MapInitType.fromValue(sourceInfo[1]);
        _sxfPassport.magneticAngle = _mappedByteBuffer.getInt() / Math.pow(10., 8.) * 180.0 / Math.PI;
        _sxfPassport.meridianAngle = _mappedByteBuffer.getInt() / Math.pow(10., 8.) * 180.0 / Math.PI;
        _sxfPassport.reliefHeight = _mappedByteBuffer.getShort();// / 10.; // In metres. Bug in Documentation ?
        _sxfPassport.yearMagneticAngle = _mappedByteBuffer.getInt() / Math.pow(10., 8.) * 180.0 / Math.PI;
        byte[] dateAngle = new byte[10];
        _mappedByteBuffer.get(dateAngle);
        _sxfPassport.dateAngle = new String(dateAngle);
        // Reserve = 10
        _mappedByteBuffer.position(_mappedByteBuffer.position() + 10);

        _sxfPassport.deviceCapability = _mappedByteBuffer.getInt();

        _sxfPassport.xBorderDeviceSouthWest = _mappedByteBuffer.getShort();
        _sxfPassport.yBorderDeviceSouthWest = _mappedByteBuffer.getShort();
        _sxfPassport.xBorderDeviceNorthWest = _mappedByteBuffer.getShort();
        _sxfPassport.yBorderDeviceNorthWest = _mappedByteBuffer.getShort();
        _sxfPassport.xBorderDeviceNorthEast = _mappedByteBuffer.getShort();
        _sxfPassport.yBorderDeviceNorthEast = _mappedByteBuffer.getShort();
        _sxfPassport.xBorderDeviceSouthEast = _mappedByteBuffer.getShort();
        _sxfPassport.yBorderDeviceSouthEast = _mappedByteBuffer.getShort();

        _sxfPassport.borderExcode = _mappedByteBuffer.getInt();

        _sxfPassport.firstMainParallel = _mappedByteBuffer.getInt() / Math.pow(10., 8.) * 180.0 / Math.PI;
        _sxfPassport.secondMainParallel = _mappedByteBuffer.getInt() / Math.pow(10., 8.) * 180.0 / Math.PI;
        _sxfPassport.axisMeridian  = _mappedByteBuffer.getInt() / Math.pow(10., 8.) * 180.0 / Math.PI;
        _sxfPassport.mainPointParallel = _mappedByteBuffer.getInt() / Math.pow(10., 8.) * 180.0 / Math.PI;
        // Reserve = 4
        _mappedByteBuffer.position(_mappedByteBuffer.position() + 4);

        if (_mappedByteBuffer.position() != _sxfPassport.length) {
            throw new SXFPassportReadError("Final position of reader is " + _mappedByteBuffer.position() + " but length of passport is " + _sxfPassport.length);
        }
    }

    /**
     * Reader for {@link SXF#VERSION_4} SXF format
     */
    private void readVersion4() {
        _sxfPassport.checkSum = _mappedByteBuffer.getInt();

        byte[] createDate = new byte[12];
        _mappedByteBuffer.get(createDate);
        _sxfPassport.createDate = new String(createDate);

        byte[] nomenclature = new byte[32];
        _mappedByteBuffer.get(nomenclature);
        _sxfPassport.nomenclature = new String(nomenclature);
        _sxfPassport.scale = _mappedByteBuffer.getInt();

        byte[] name = new byte[32];
        _mappedByteBuffer.get(name);
        _sxfPassport.name = new String(name);

        byte[] infoFlags = new byte[4];
        _mappedByteBuffer.get(infoFlags);
        _sxfPassport.conditionFlag = infoFlags[0] & 0x3;
        _sxfPassport.autoGUID = (((infoFlags[0] >> 2) & 0x1) == 1);
        _sxfPassport.realPlaceFlag = (infoFlags[0] >> 3) & 0x3;
        _sxfPassport.codeTypeFlag = (infoFlags[0] >> 5) & 0x3;
        _sxfPassport.generalizationFlag = (infoFlags[0] >> 7) & 0x1;

        _sxfPassport.textEncodingFlag = TextEncoding.fromValue(infoFlags[1]);
        _sxfPassport.coordPrecisionFlag = infoFlags[2];
        _sxfPassport.orderViewSheetFlag = infoFlags[3] & 0x1;

        _sxfPassport.epsg = _mappedByteBuffer.getInt();

        _sxfPassport.xSouthWest = _mappedByteBuffer.getDouble();
        _sxfPassport.ySouthWest = _mappedByteBuffer.getDouble();
        _sxfPassport.xNorthWest = _mappedByteBuffer.getDouble();
        _sxfPassport.yNorthWest = _mappedByteBuffer.getDouble();
        _sxfPassport.xNorthEast = _mappedByteBuffer.getDouble();
        _sxfPassport.yNorthEast = _mappedByteBuffer.getDouble();
        _sxfPassport.xSouthEast = _mappedByteBuffer.getDouble();
        _sxfPassport.ySouthEast = _mappedByteBuffer.getDouble();

        _sxfPassport.bSouthWest = _mappedByteBuffer.getDouble() * 180.0 / Math.PI;
        _sxfPassport.lSouthWest = _mappedByteBuffer.getDouble() * 180.0 / Math.PI;
        _sxfPassport.bNorthWest = _mappedByteBuffer.getDouble() * 180.0 / Math.PI;
        _sxfPassport.lNorthWest = _mappedByteBuffer.getDouble() * 180.0 / Math.PI;
        _sxfPassport.bNorthEast = _mappedByteBuffer.getDouble() * 180.0 / Math.PI;
        _sxfPassport.lNorthEast = _mappedByteBuffer.getDouble() * 180.0 / Math.PI;
        _sxfPassport.bSouthEast = _mappedByteBuffer.getDouble() * 180.0 / Math.PI;
        _sxfPassport.lSouthEast = _mappedByteBuffer.getDouble() * 180.0 / Math.PI;

        byte[] mathematicalBasis = new byte[8];
        _mappedByteBuffer.get(mathematicalBasis);
        _sxfPassport.ellipsoidKind = EllipsoidKind.fromValue(mathematicalBasis[0]);
        _sxfPassport.heightSystem = HeightSystem.fromValue(mathematicalBasis[1]);
        _sxfPassport.materialProjection = MapProjection.fromValue(mathematicalBasis[2]);
        _sxfPassport.coordinateSystem = CoordinateSystem.fromValue(mathematicalBasis[3]);
        _sxfPassport.planeUnit = Unit.fromValue(mathematicalBasis[4]);
        _sxfPassport.heightUnit = Unit.fromValue(mathematicalBasis[5]);
        _sxfPassport.frameKind = FrameKind.fromValue(mathematicalBasis[6]);
        _sxfPassport.mapType = MapType.fromValue(mathematicalBasis[7]);

        byte[] date = new byte[12];
        _mappedByteBuffer.get(date);
        byte[] sourceInfo = new byte[4];
        _mappedByteBuffer.get(sourceInfo);
        _sxfPassport.date = new String(date);
        _sxfPassport.materialKind = MapInitKind.fromValue(sourceInfo[0]);
        _sxfPassport.materialType = MapInitType.fromValue(sourceInfo[1]);
        _sxfPassport.msk63Ident = sourceInfo[2];
        _sxfPassport.frameBorder = sourceInfo[3];
        _sxfPassport.magneticAngle = _mappedByteBuffer.getDouble() * 180.0 / Math.PI;
        _sxfPassport.meridianAngle = _mappedByteBuffer.getDouble() * 180.0 / Math.PI;
        _sxfPassport.yearMagneticAngle = _mappedByteBuffer.getDouble() * 180.0 / Math.PI;
        byte[] dateAngle = new byte[12];
        _mappedByteBuffer.get(dateAngle);
        _sxfPassport.dateAngle = new String(dateAngle);
        _sxfPassport.msk63Zone = _mappedByteBuffer.getInt();
        _sxfPassport.reliefHeight = _mappedByteBuffer.getDouble();

        _sxfPassport.axisAngle = _mappedByteBuffer.getDouble() * 180.0 / Math.PI;

        _sxfPassport.deviceCapability = _mappedByteBuffer.getInt();

        _sxfPassport.xBorderDeviceSouthWest = _mappedByteBuffer.getInt();
        _sxfPassport.yBorderDeviceSouthWest = _mappedByteBuffer.getInt();
        _sxfPassport.xBorderDeviceNorthWest = _mappedByteBuffer.getInt();
        _sxfPassport.yBorderDeviceNorthWest = _mappedByteBuffer.getInt();
        _sxfPassport.xBorderDeviceNorthEast = _mappedByteBuffer.getInt();
        _sxfPassport.yBorderDeviceNorthEast = _mappedByteBuffer.getInt();
        _sxfPassport.xBorderDeviceSouthEast = _mappedByteBuffer.getInt();
        _sxfPassport.yBorderDeviceSouthEast = _mappedByteBuffer.getInt();

        _sxfPassport.borderExcode = _mappedByteBuffer.getInt();

        _sxfPassport.firstMainParallel = _mappedByteBuffer.getDouble() * 180.0 / Math.PI;
        _sxfPassport.secondMainParallel = _mappedByteBuffer.getDouble() * 180.0 / Math.PI;
        _sxfPassport.axisMeridian  = _mappedByteBuffer.getDouble() * 180.0 / Math.PI;
        _sxfPassport.mainPointParallel = _mappedByteBuffer.getDouble() * 180.0 / Math.PI;
        _sxfPassport.poleLatitude = _mappedByteBuffer.getDouble() * 180.0 / Math.PI;
        _sxfPassport.poleLongitude = _mappedByteBuffer.getDouble() * 180.0 / Math.PI;

        if (_mappedByteBuffer.position() != _sxfPassport.length) {
            throw new SXFPassportReadError("Final position of reader is " + _mappedByteBuffer.position() + " but length of passport is " + _sxfPassport.length);
        }
    }

    /**
     * Function returns SXFPassport which will be reader
     * @return SXFPassport which will be reader
     */
    public SXFPassport getPassport() {
        return _sxfPassport;
    }

    public void close() throws IOException {
        if (_randomAccessFile != null) {
            _randomAccessFile.close();
        }
    }
}
