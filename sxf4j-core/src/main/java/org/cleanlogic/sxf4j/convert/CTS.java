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
import org.cts.CRSFactory;
import org.cts.IllegalCoordinateException;
import org.cts.crs.CRSException;
import org.cts.crs.CoordinateReferenceSystem;
import org.cts.crs.GeodeticCRS;
import org.cts.op.CoordinateOperation;
import org.cts.op.CoordinateOperationFactory;
import org.cts.registry.*;

import java.util.List;

/**
 * Class for convert SXF Records coordinates based on <a href="https://github.com/orbisgis/cts">CTS</a> library.
 * In this moment twice slower then PROJ4J.
 * @author Serge Silaev aka iSergio <s.serge.b@gmail.com>
 */
public class CTS {
    private final CoordinateOperation _coordinateOperation;
    public CTS(int srcCode, int dstCode) throws CRSException {
        CRSFactory crsFactory = new CRSFactory();
        RegistryManager registryManager = crsFactory.getRegistryManager();
        registryManager.addRegistry(new IGNFRegistry());
        registryManager.addRegistry(new EPSGRegistry());
        registryManager.addRegistry(new ESRIRegistry());
        registryManager.addRegistry(new Nad27Registry());
        registryManager.addRegistry(new Nad83Registry());
        registryManager.addRegistry(new WorldRegistry());

        CoordinateReferenceSystem srcCRS;
        if (Utils.getProjParams().containsKey(srcCode)) {
            srcCRS = crsFactory.createFromPrj(Utils.getProjParams().get(srcCode));
        } else {
            srcCRS = crsFactory.getCRS("EPSG:" + srcCode);
        }

        CoordinateReferenceSystem dstCRS;
        if (Utils.getProjParams().containsKey(dstCode)) {
            dstCRS = crsFactory.createFromPrj(Utils.getProjParams().get(dstCode));
        } else {
            dstCRS = crsFactory.getCRS("EPSG:" + dstCode);
        }

        List<CoordinateOperation> coordinateOperations = CoordinateOperationFactory.createCoordinateOperations((GeodeticCRS) srcCRS, (GeodeticCRS) dstCRS);
        if (coordinateOperations.size() > 0) {
            _coordinateOperation = coordinateOperations.get(0);
        } else {
            _coordinateOperation = null;
        }
    }

    public Coordinate doConvert(Coordinate srcCoordinate) throws IllegalCoordinateException {
        return doConvert(srcCoordinate.x, srcCoordinate.y, srcCoordinate.z);
    }

    public Coordinate doConvert(double x, double y, double z) throws IllegalCoordinateException {
        double[] input = new double[] {x, y, z};
        double[] output = _coordinateOperation.transform(input);
        if (output.length == 2) {
            return new Coordinate(output[0], output[1], 0.);
        }
        return new Coordinate(output[0], output[1], output[2]);
    }
}
