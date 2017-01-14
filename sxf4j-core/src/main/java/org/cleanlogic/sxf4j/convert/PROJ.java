package org.cleanlogic.sxf4j.convert;

import org.osgeo.proj4j.*;

import java.util.HashMap;
import java.util.Map;

/**
 * Class for convert SXF Records coordinate through PROJ4J
 * @author Serge Silaev aka iSergio <s.serge.b@gmail.com>
 */
public class PROJ {
    /**
     * Main CoordinateTransform
     */
    private final CoordinateTransform _coordinateTransform;
    /**
     * Extended map by SRID's Gauss-Krugger
     */
    private static final Map<Integer, String> _proj = new HashMap<>();
    static {
        _proj.put(28401, "+proj=tmerc +lat_0=0 +lon_0=3 +k=1 +x_0=1500000 +y_0=0 +ellps=krass +towgs84=23.92,-141.27,-80.9,-0,0.35,0.82,-0.12 +units=m +no_defs");

        _proj.put(28433, "+proj=tmerc +lat_0=0 +lon_0=-165 +k=1 +x_0=33500000 +y_0=0 +ellps=krass +towgs84=23.92,-141.27,-80.9,-0,0.35,0.82,-0.12 +units=m +no_defs");
        _proj.put(28434, "+proj=tmerc +lat_0=0 +lon_0=-159 +k=1 +x_0=34500000 +y_0=0 +ellps=krass +towgs84=23.92,-141.27,-80.9,-0,0.35,0.82,-0.12 +units=m +no_defs");
        _proj.put(28435, "+proj=tmerc +lat_0=0 +lon_0=-153 +k=1 +x_0=35500000 +y_0=0 +ellps=krass +towgs84=23.92,-141.27,-80.9,-0,0.35,0.82,-0.12 +units=m +no_defs");
        _proj.put(28436, "+proj=tmerc +lat_0=0 +lon_0=-147 +k=1 +x_0=36500000 +y_0=0 +ellps=krass +towgs84=23.92,-141.27,-80.9,-0,0.35,0.82,-0.12 +units=m +no_defs");
        _proj.put(28437, "+proj=tmerc +lat_0=0 +lon_0=-141 +k=1 +x_0=37500000 +y_0=0 +ellps=krass +towgs84=23.92,-141.27,-80.9,-0,0.35,0.82,-0.12 +units=m +no_defs");
        _proj.put(28438, "+proj=tmerc +lat_0=0 +lon_0=-135 +k=1 +x_0=38500000 +y_0=0 +ellps=krass +towgs84=23.92,-141.27,-80.9,-0,0.35,0.82,-0.12 +units=m +no_defs");
        _proj.put(28439, "+proj=tmerc +lat_0=0 +lon_0=-129 +k=1 +x_0=39500000 +y_0=0 +ellps=krass +towgs84=23.92,-141.27,-80.9,-0,0.35,0.82,-0.12 +units=m +no_defs");
        _proj.put(28440, "+proj=tmerc +lat_0=0 +lon_0=-123 +k=1 +x_0=40500000 +y_0=0 +ellps=krass +towgs84=23.92,-141.27,-80.9,-0,0.35,0.82,-0.12 +units=m +no_defs");
        _proj.put(28441, "+proj=tmerc +lat_0=0 +lon_0=-117 +k=1 +x_0=41500000 +y_0=0 +ellps=krass +towgs84=23.92,-141.27,-80.9,-0,0.35,0.82,-0.12 +units=m +no_defs");
        _proj.put(28442, "+proj=tmerc +lat_0=0 +lon_0=-111 +k=1 +x_0=42500000 +y_0=0 +ellps=krass +towgs84=23.92,-141.27,-80.9,-0,0.35,0.82,-0.12 +units=m +no_defs");
        _proj.put(28443, "+proj=tmerc +lat_0=0 +lon_0=-105 +k=1 +x_0=43500000 +y_0=0 +ellps=krass +towgs84=23.92,-141.27,-80.9,-0,0.35,0.82,-0.12 +units=m +no_defs");
        _proj.put(28444, "+proj=tmerc +lat_0=0 +lon_0=-99 +k=1 +x_0=44500000 +y_0=0 +ellps=krass +towgs84=23.92,-141.27,-80.9,-0,0.35,0.82,-0.12 +units=m +no_defs");
        _proj.put(28445, "+proj=tmerc +lat_0=0 +lon_0=-93 +k=1 +x_0=45500000 +y_0=0 +ellps=krass +towgs84=23.92,-141.27,-80.9,-0,0.35,0.82,-0.12 +units=m +no_defs");
        _proj.put(28446, "+proj=tmerc +lat_0=0 +lon_0=-87 +k=1 +x_0=46500000 +y_0=0 +ellps=krass +towgs84=23.92,-141.27,-80.9,-0,0.35,0.82,-0.12 +units=m +no_defs");
        _proj.put(28447, "+proj=tmerc +lat_0=0 +lon_0=-81 +k=1 +x_0=47500000 +y_0=0 +ellps=krass +towgs84=23.92,-141.27,-80.9,-0,0.35,0.82,-0.12 +units=m +no_defs");
        _proj.put(28448, "+proj=tmerc +lat_0=0 +lon_0=-75 +k=1 +x_0=48500000 +y_0=0 +ellps=krass +towgs84=23.92,-141.27,-80.9,-0,0.35,0.82,-0.12 +units=m +no_defs");
        _proj.put(28449, "+proj=tmerc +lat_0=0 +lon_0=-69 +k=1 +x_0=49500000 +y_0=0 +ellps=krass +towgs84=23.92,-141.27,-80.9,-0,0.35,0.82,-0.12 +units=m +no_defs");
        _proj.put(28450, "+proj=tmerc +lat_0=0 +lon_0=-63 +k=1 +x_0=50500000 +y_0=0 +ellps=krass +towgs84=23.92,-141.27,-80.9,-0,0.35,0.82,-0.12 +units=m +no_defs");
        _proj.put(28451, "+proj=tmerc +lat_0=0 +lon_0=-57 +k=1 +x_0=51500000 +y_0=0 +ellps=krass +towgs84=23.92,-141.27,-80.9,-0,0.35,0.82,-0.12 +units=m +no_defs");
        _proj.put(28452, "+proj=tmerc +lat_0=0 +lon_0=-51 +k=1 +x_0=52500000 +y_0=0 +ellps=krass +towgs84=23.92,-141.27,-80.9,-0,0.35,0.82,-0.12 +units=m +no_defs");
        _proj.put(28453, "+proj=tmerc +lat_0=0 +lon_0=-45 +k=1 +x_0=53500000 +y_0=0 +ellps=krass +towgs84=23.92,-141.27,-80.9,-0,0.35,0.82,-0.12 +units=m +no_defs");
        _proj.put(28454, "+proj=tmerc +lat_0=0 +lon_0=-39 +k=1 +x_0=54500000 +y_0=0 +ellps=krass +towgs84=23.92,-141.27,-80.9,-0,0.35,0.82,-0.12 +units=m +no_defs");
        _proj.put(28455, "+proj=tmerc +lat_0=0 +lon_0=-33 +k=1 +x_0=55500000 +y_0=0 +ellps=krass +towgs84=23.92,-141.27,-80.9,-0,0.35,0.82,-0.12 +units=m +no_defs");
        _proj.put(28456, "+proj=tmerc +lat_0=0 +lon_0=-27 +k=1 +x_0=56500000 +y_0=0 +ellps=krass +towgs84=23.92,-141.27,-80.9,-0,0.35,0.82,-0.12 +units=m +no_defs");
        _proj.put(28457, "+proj=tmerc +lat_0=0 +lon_0=-21 +k=1 +x_0=57500000 +y_0=0 +ellps=krass +towgs84=23.92,-141.27,-80.9,-0,0.35,0.82,-0.12 +units=m +no_defs");
        _proj.put(28458, "+proj=tmerc +lat_0=0 +lon_0=-15 +k=1 +x_0=58500000 +y_0=0 +ellps=krass +towgs84=23.92,-141.27,-80.9,-0,0.35,0.82,-0.12 +units=m +no_defs");
        _proj.put(28459, "+proj=tmerc +lat_0=0 +lon_0=-9 +k=1 +x_0=59500000 +y_0=0 +ellps=krass +towgs84=23.92,-141.27,-80.9,-0,0.35,0.82,-0.12 +units=m +no_defs");
        _proj.put(28460, "+proj=tmerc +lat_0=0 +lon_0=-3 +k=1 +x_0=60500000 +y_0=0 +ellps=krass +towgs84=23.92,-141.27,-80.9,-0,0.35,0.82,-0.12 +units=m +no_defs");
    }

    /**
     * Constructor of PROJ convert
     * @param srcCode EPSG code from convert
     * @param dstCode EPSG code to convert
     */
    public PROJ(int srcCode, int dstCode) {
        CoordinateTransformFactory coordinateTransformFactory = new CoordinateTransformFactory();
        CRSFactory crsFactory = new CRSFactory();

        CoordinateReferenceSystem srcCRS;
        CoordinateReferenceSystem dstCRS;

        if (_proj.containsKey(srcCode)) {
            srcCRS = crsFactory.createFromParameters("EPSG:" + srcCode, _proj.get(srcCode));
        } else {
            srcCRS = crsFactory.createFromName("EPSG:" + srcCode);
        }
        if (_proj.containsKey(dstCode)) {
            dstCRS = crsFactory.createFromParameters("EPSG:" + dstCode, _proj.get(dstCode));
        } else {
            dstCRS = crsFactory.createFromName("EPSG:" + dstCode);
        }

        _coordinateTransform = coordinateTransformFactory.createTransform(srcCRS, dstCRS);
    }

    /**
     * Converts coodrinates from source SRID to destination SRID
     * @param srcProjCoordinate {@link ProjCoordinate} coordinate in source SRID
     * @return reprojected coordinate in destination SRID
     */
    public ProjCoordinate doConvert(ProjCoordinate srcProjCoordinate) {
        ProjCoordinate dstProjCoordinate = new ProjCoordinate();

        _coordinateTransform.transform(srcProjCoordinate, dstProjCoordinate);
        return dstProjCoordinate;
    }

    /**
     * Converts coodrinates from source SRID to destination SRID
     * @param x X coordinate for reproject
     * @param y Y coordinate for reproject
     * @param h Z coordinate for reproject
     * @return reprojected coordinate in destination SRID
     */
    public ProjCoordinate doConvert(double x, double y, double h) {
        ProjCoordinate srcProjCoordinate = new ProjCoordinate(y, x, h);
        ProjCoordinate dstProjCoordinate = new ProjCoordinate();

        _coordinateTransform.transform(srcProjCoordinate, dstProjCoordinate);
        return dstProjCoordinate;
    }
}
