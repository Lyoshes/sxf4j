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

package org.cleanlogic.sxf4j.convert;

import com.vividsolutions.jts.geom.Coordinate;
import org.cleanlogic.sxf4j.utils.Utils;
import org.osgeo.proj4j.*;

import java.util.ArrayList;
import java.util.List;

/**
 * Class for convert SXF Records coordinates based on PROJ4J library
 * @author Serge Silaev aka iSergio <s.serge.b@gmail.com>
 */
public class PROJ {
    /**
     * Main CoordinateTransform
     */
    private final CoordinateTransform _coordinateTransform;
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

        if (Utils.getProjParams().containsKey(srcCode)) {
            srcCRS = crsFactory.createFromParameters("EPSG:" + srcCode, Utils.getProjParams().get(srcCode));
        } else {
            srcCRS = crsFactory.createFromName("EPSG:" + srcCode);
        }
        if (Utils.getProjParams().containsKey(dstCode)) {
            dstCRS = crsFactory.createFromParameters("EPSG:" + dstCode, Utils.getProjParams().get(dstCode));
        } else {
            dstCRS = crsFactory.createFromName("EPSG:" + dstCode);
        }

        _coordinateTransform = coordinateTransformFactory.createTransform(srcCRS, dstCRS);
    }

    public int getSrcSRID() {
        return _coordinateTransform.getSourceCRS().getProjection().getEPSGCode();
    }

    public int getDstSRID() {
        return _coordinateTransform.getTargetCRS().getProjection().getEPSGCode();
    }

    /**
     * Converts coodrinates from source SRID to destination SRID
     * @param srcCoordinate {@link Coordinate} coordinate in source SRID
     * @return reprojected coordinate in destination SRID
     */
    public Coordinate doConvert(Coordinate srcCoordinate) {
        ProjCoordinate srcProjCoordinate = new ProjCoordinate(srcCoordinate.x, srcCoordinate.y, srcCoordinate.z);
        ProjCoordinate dstProjCoordinate = new ProjCoordinate();

        _coordinateTransform.transform(srcProjCoordinate, dstProjCoordinate);
        return new Coordinate(dstProjCoordinate.x, dstProjCoordinate.y, dstProjCoordinate.z);
    }

    /**
     * Converts coodrinates from source SRID to destination SRID
     * @param x X coordinate for reproject
     * @param y Y coordinate for reproject
     * @param z Z coordinate for reproject
     * @return reprojected coordinate in destination SRID
     */
    public Coordinate doConvert(double x, double y, double z) {
        ProjCoordinate srcProjCoordinate = new ProjCoordinate(y, x, z);
        ProjCoordinate dstProjCoordinate = new ProjCoordinate();

        _coordinateTransform.transform(srcProjCoordinate, dstProjCoordinate);
        return new Coordinate(dstProjCoordinate.x, dstProjCoordinate.y, dstProjCoordinate.z);
    }

    public List<Coordinate> doConvert(List<Coordinate> srcCoordinates) {
        List<Coordinate> dstCoordinates = new ArrayList<>();
        ProjCoordinate srcProjCoordinate = new ProjCoordinate();
        ProjCoordinate dstProjCoordinate = new ProjCoordinate();
        Coordinate dstCoordinate = new Coordinate();
        for (Coordinate srcCoordinate : srcCoordinates) {
            srcProjCoordinate.setValue(srcCoordinate.x, srcCoordinate.y, srcCoordinate.z);
            _coordinateTransform.transform(srcProjCoordinate, dstProjCoordinate);
            dstCoordinate.x = dstProjCoordinate.x;
            dstCoordinate.y = dstProjCoordinate.y;
            dstCoordinate.z = dstProjCoordinate.z;
            dstCoordinates.add(dstCoordinate);
        }
        return dstCoordinates;
    }

//    public void test() {
////        double[] coord = new double[2];
////        coord[0] = 54.321;
////        coord[1] = 9.876;
//
//        CRSFactory crsFactory = new CRSFactory();
//        CoordinateReferenceSystem srcCRS =
////        CoordinateReferenceSystem crs1 = crsFactory.getCRS("EPSG:4326");
////        CoordinateReferenceSystem crs2 = crsFactory.getCRS("EPSG:25832");
////        GeodeticCRS sourceGCRS = (GeodeticCRS) sourceCRS;
////        GeodeticCRS targetGCRS = (GeodeticCRS) targetCRS;
//        List<CoordinateOperation> coordOps = CoordinateOperationFactory.createCoordinateOperations(sourceGCRS, targetGCRS);
//
////        if (coordOps.size() != 0) {
////            for (CoordinateOperation op : coordOps) {
////                double[] dd  = op.transform(coord);
////                for (int i = 0; i < dd.length; i++) {
////                    System.out.println(dd[i]); // for debugging
////                }
////            }
////        }
////        CoordinateOperationFactory dasd;
//    }
}
