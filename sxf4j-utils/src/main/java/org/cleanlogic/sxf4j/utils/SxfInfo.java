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

package org.cleanlogic.sxf4j.utils;

import com.vividsolutions.jts.geom.Coordinate;
import com.vividsolutions.jts.geom.Geometry;
import org.apache.commons.cli.*;
import org.cleanlogic.sxf4j.SXFPassport;
import org.cleanlogic.sxf4j.SXFReader;
import org.cleanlogic.sxf4j.SXFRecord;
import org.osgeo.proj4j.*;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * @author Serge Silaev aka iSergio <s.serge.b@gmail.com>
 */
public class SxfInfo {
    private static int srcSRID;
    private static int dstSRID;
    private static CoordinateTransform coordinateTransform;

    public static void main(String... args) throws IOException {
        Options options = new Options();

        Option quietOption = new Option("q", "quiet", false, "Not print warning messages");
        options.addOption(quietOption);

        Option passportOption = new Option("p", "passport", false, "Print passport of SXF");
        options.addOption(passportOption);

        Option descriptorOption = new Option("d", "descriptor", false, "Print descriptor of SXF");
        options.addOption(descriptorOption);

        Option recordCountOption = new Option("c", "count", false, "Print record count");
        options.addOption(recordCountOption);

        Option fileOption = new Option("f", "flipCoordinates", false, "Flip coordinates");
        options.addOption(fileOption);

        Option sridOption = new Option("s", "srid", true, "Set the SRID field. Defaults to 0.");
        sridOption.setArgName("[<from>:]<srid>");
        options.addOption(sridOption);

        Option recordOption = new Option("r", "record", true, "Print record header, text (if exists), semantics (if exists) without geometry (incode:<i> - by incode, excode:<i> - by excode, number:<i> - by number");
        recordOption.setArgName("type:<i>");
        options.addOption(recordOption);

        Option recordGeometryOption = new Option("rg", "recordGeometry", true, "Print record geometry only (incode:<i> - by incode, excode:<i> - by excode, number:<i> - by number");
        recordGeometryOption.setArgName("type:<i>");
        options.addOption(recordGeometryOption);

        Option recordGeomertyType = new Option("gt", "geometryType", true, "All geometry print type (WKT, EWKT, WKB). Default: WKT.");
        recordGeomertyType.setArgName("type");
        options.addOption(recordGeomertyType);

        Option helpOption = new Option("h", "help", false, "Print usage");
        options.addOption(helpOption);

        CommandLineParser commandLineParser = new DefaultParser();
        HelpFormatter helpFormatter = new HelpFormatter();
        CommandLine commandLine;

        try {
            commandLine = commandLineParser.parse(options, args);

            if (commandLine.hasOption("help") || commandLine.getArgList().size() != 1) {
                helpFormatter.printHelp("sxfinfo [<options>] <sxfile|dir>", options);
                return;
            }

            File file = new File(commandLine.getArgList().get(0));

            List<File> files = new ArrayList<>();

            if (file.isFile()) {
                files.add(file);
            } else if (file.isDirectory()) {
                if (commandLine.hasOption("record")) {
                    System.out.println("Record info print not supported on directory mode");
                    return;
                }
                Utils.search(file, files, ".sxf");
            }

//            SXFReaderOptions sxfReaderOptions = new SXFReaderOptions();
//            sxfReaderOptions.quite = commandLine.hasOption("quiet");
//            sxfReaderOptions.flipCoordinates = commandLine.hasOption('f');
            if (commandLine.hasOption('s')) {
                String srid = commandLine.getOptionValue('s');
                String[] sridPair = srid.split(":");
                if (sridPair.length == 2) {
                    srcSRID = Integer.parseInt(sridPair[0]);
                    dstSRID = Integer.parseInt(sridPair[1]);
                } else if (sridPair.length == 1) {
                    dstSRID = Integer.parseInt(srid);
                }
            }

            StringList geometryTypes = new StringList();
            geometryTypes.add("WKT");
            geometryTypes.add("WKB");
            geometryTypes.add("EWKT");

            String geometryType = "WKT";
            if (commandLine.hasOption("geometryType")) {
                geometryType = commandLine.getOptionValue("geometryType");
                if (!geometryTypes.contains(geometryType)) {
                    System.err.printf("GeometryType - %s not supported.\n", geometryType);
                    return;
                }
            }

            for (File _file : files) {
//                if (!sxfReaderOptions.quite) {
//                    System.out.printf("Process file %s\n", _file.toString());
//                }
                try {
                    SXFReader sxfReader = new SXFReader(_file);
                    SXFPassport sxfPassport = sxfReader.getPassport();
                    int srid = sxfPassport.srid();
                    if (srid != 0) {
                        srcSRID = srid;
                    }
                    if (!commandLine.hasOption("t")) {
                        if (srcSRID != dstSRID && dstSRID != 0) {
                            coordinateTransform = createCoordinateTransform();
                        }
                    }
                    if (commandLine.hasOption("passport")) {
                        sxfReader.getPassport().print();
                    }
                    if (commandLine.hasOption("descriptor")) {
                        sxfReader.getDescriptor().print();
                    }
                    if (commandLine.hasOption("count")) {
                        System.out.printf("Total records: %d\n", sxfReader.getCount());
                    }
                    String[] recordPair = null;
                    if (commandLine.hasOption("record")) {
                        recordPair = commandLine.getOptionValue("record").split(":");
                    } else if (commandLine.hasOption("recordGeometry")) {
                        recordPair = commandLine.getOptionValue("recordGeometry").split(":");
                    }
                    if (recordPair != null) {
                        if (recordPair.length != 2) {
                            System.err.println("Record search format must be - <type:i>");
                            return;
                        }
                        String type = recordPair[0];
                        int value = Integer.parseInt(recordPair[1]);
                        if (type.equalsIgnoreCase("incode")) {
                            SXFRecord sxfRecord = sxfReader.getRecordByIncode(value);
                            if (sxfRecord == null) {
                                return;
                            }
                            if (commandLine.hasOption("record")) {
                                System.out.println(sxfRecord.toString());
                                if (sxfRecord.isTextExsits()) {
                                    printText(sxfRecord);
                                }
                                if (sxfRecord.isSemanticExists()) {
                                    printSemantics(sxfRecord);
                                }
                            }
                            if (commandLine.hasOption("recordGeometry")) {
                                printGeometry(sxfRecord, geometryType);
                            }
                        } else if (type.equalsIgnoreCase("excode")) {
                            List<SXFRecord> sxfRecords = sxfReader.getRecordByExcode(value);
                            for (SXFRecord sxfRecord : sxfRecords) {
                                if (commandLine.hasOption("record")) {
                                    System.out.println(sxfRecord.toString());
                                    if (sxfRecord.isTextExsits()) {
                                        printText(sxfRecord);
                                    }
                                    if (sxfRecord.isSemanticExists()) {
                                        printSemantics(sxfRecord);
                                    }
                                }
                                if (commandLine.hasOption("recordGeometry")) {
                                    if (commandLine.hasOption("recordGeometry")) {
                                        printGeometry(sxfRecord, geometryType);
                                    }
                                }
                            }
                        } else if (type.equalsIgnoreCase("number")) {
                            SXFRecord sxfRecord = sxfReader.getRecordByNumber(value);
                            if (sxfRecord == null) {
                                return;
                            }
                            if (commandLine.hasOption("record")) {
                                System.out.println(sxfRecord.toString());
                                if (sxfRecord.isTextExsits()) {
                                    printText(sxfRecord);
                                }
                                if (sxfRecord.isSemanticExists()) {
                                    printSemantics(sxfRecord);
                                }
                            }
                            if (commandLine.hasOption("recordGeometry")) {
                                printGeometry(sxfRecord, geometryType);
                            }
                        } else {
                            System.err.printf("Record search type - %s - not supported.\n", type);
                            return;
                        }
                    }
                } catch (IOException ex) {
                    ex.printStackTrace();
                    continue;
                }
            }
        } catch (ParseException e) {
            System.out.println(e.getMessage());
            helpFormatter.printHelp("sxfinfo [<options>] <sxfile|dir>", options);

            System.exit(1);
        }
    }

    private static void printGeometry(SXFRecord sxfRecord, String geometryType) throws IOException {
        Geometry geometry = sxfRecord.geometry();
        if (coordinateTransform != null) {
            geometry.normalize();
            geometry = geometryTransform(geometry.norm());
        }
        if (geometryType.equalsIgnoreCase("WKT")) {
            System.out.printf("Geometry: %s\n", Utils.geometryAsWKT(geometry));
        } else if (geometryType.equalsIgnoreCase("EWKT")) {
            System.out.printf("Geometry: %s\n", Utils.geometryAsEWKT(geometry));
        } else if (geometryType.equalsIgnoreCase("WKB")) {
            System.out.printf("Geometry: %s\n", Utils.geometryAsWKB(geometry));
        }
    }

    private static void printSemantics(SXFRecord sxfRecord) {
        System.out.printf("RecordSemantics info:\n");
        for (SXFRecord.Semantic semantic : sxfRecord.semantics()) {
            System.out.printf("%s\n", semantic.toString());
        }
    }

    private static void printText(SXFRecord sxfRecord) throws IOException {
        System.out.printf("Text:\t{");
        for (SXFRecord.Text text : sxfRecord.texts()) {
            System.out.printf("%s", text.toString());
        }
        System.out.println("}");
    }

    private static CoordinateTransform createCoordinateTransform() {
        CoordinateTransformFactory coordinateTransformFactory = new CoordinateTransformFactory();
        CRSFactory crsFactory = new CRSFactory();

        CoordinateReferenceSystem srcCRS;
        CoordinateReferenceSystem dstCRS;

        if (Utils.SRID_EX.containsKey(srcSRID)) {
            srcCRS = crsFactory.createFromParameters("EPSG:" + srcSRID, Utils.SRID_EX.get(srcSRID));
        } else {
            srcCRS = crsFactory.createFromName("EPSG:" + srcSRID);
        }

        if (Utils.SRID_EX.containsKey(dstSRID)) {
            dstCRS = crsFactory.createFromParameters("EPSG:" + dstSRID, Utils.SRID_EX.get(dstSRID));
        } else {
            dstCRS = crsFactory.createFromName("EPSG:" + dstSRID);
        }

        return coordinateTransformFactory.createTransform(srcCRS, dstCRS);
    }

    private static Geometry geometryTransform(Geometry srcGeometry) {
        Geometry geometry = (Geometry) srcGeometry.clone();
        ProjCoordinate srcCoordinate = new ProjCoordinate();
        ProjCoordinate dstCoordinate = new ProjCoordinate();
        for (Coordinate coordinate : geometry.getCoordinates()) {
            srcCoordinate.setValue(coordinate.x, coordinate.y, coordinate.z);
            try {
                coordinateTransform.transform(srcCoordinate, dstCoordinate);
            } catch (java.lang.IllegalStateException ex) {
                //
            } finally {
                coordinate.setOrdinate(0, dstCoordinate.x);
                coordinate.setOrdinate(1, dstCoordinate.y);
                coordinate.setOrdinate(2, dstCoordinate.z);
            }
        }
        geometry.setSRID(dstSRID);
        return geometry;
    }
}
