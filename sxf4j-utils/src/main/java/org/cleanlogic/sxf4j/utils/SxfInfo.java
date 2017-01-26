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

import com.vividsolutions.jts.geom.Geometry;
import org.apache.commons.cli.*;
import org.cleanlogic.sxf4j.SXFReader;
import org.cleanlogic.sxf4j.SXFRecord;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * @author Serge Silaev aka iSergio <s.serge.b@gmail.com>
 */
public class SxfInfo {
    public static void main(String... args) {
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
//            if (commandLine.hasOption('s')) {
//                String srid = commandLine.getOptionValue('s');
//                String[] sridPair = srid.split(":");
//                if (sridPair.length == 2) {
//                    sxfReaderOptions.srcSRID = Integer.parseInt(sridPair[0]);
//                    sxfReaderOptions.dstSRID = Integer.parseInt(sridPair[1]);
//                } else if (sridPair.length == 1) {
//                    sxfReaderOptions.dstSRID = Integer.parseInt(srid);
//                }
//            }

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
                SXFReader sxfReader;
                try {
                    sxfReader = new SXFReader(_file);
                } catch (IOException ex) {
                    ex.printStackTrace();
                    continue;
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
                            if (sxfRecord.isSemanticExists()) {
                                printText(sxfRecord);
                            }
                            printSemantics(sxfRecord);
                        }
                        if (commandLine.hasOption("recordGeometry")) {
                            printGeometry(sxfRecord, geometryType);
                        }
                    } else if (type.equalsIgnoreCase("excode")) {
                        List<SXFRecord> sxfRecords = sxfReader.getRecordByExcode(value);
                        for (SXFRecord sxfRecord : sxfRecords) {
                            if (commandLine.hasOption("record")) {
                                System.out.println(sxfRecord.toString());
                                if (sxfRecord.isSemanticExists()) {
                                    printText(sxfRecord);
                                }
                                printSemantics(sxfRecord);
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
                            if (sxfRecord.isSemanticExists()) {
                                printText(sxfRecord);
                            }
                            printSemantics(sxfRecord);
                        }
                        if (commandLine.hasOption("recordGeometry")) {
                            printGeometry(sxfRecord, geometryType);
                        }
                    } else {
                        System.err.printf("Record search type - %s - not supported.\n", type);
                        return;
                    }
                }
            }
        } catch (ParseException e) {
            System.out.println(e.getMessage());
            helpFormatter.printHelp("sxfinfo [<options>] <sxfile|dir>", options);

            System.exit(1);
        }
    }

    private static void printGeometry(SXFRecord sxfRecord, String geometryType) {
//        Geometry srcGeometry = sxfRecord.getMetric().getSrcGeometry();
//        Geometry dstGeometry = sxfRecord.getMetric().getDstGeometry();
//
//        if (geometryType.equalsIgnoreCase("WKT")) {
//            System.out.printf("Source: %s\n", Utils.geometryAsWKT(srcGeometry));
//            if (dstGeometry != null) {
//                System.out.printf("Target: %s\n", Utils.geometryAsWKT(dstGeometry));
//            }
//        } else if (geometryType.equalsIgnoreCase("EWKT")) {
//            System.out.printf("Source: %s\n", Utils.geometryAsEWKT(srcGeometry));
//            if (dstGeometry != null) {
//                System.out.printf("Target: %s\n", Utils.geometryAsEWKT(dstGeometry));
//            }
//        } else if (geometryType.equalsIgnoreCase("WKB")) {
//            System.out.printf("Source: %s\n", Utils.geometryAsWKB(srcGeometry));
//            if (dstGeometry != null) {
//                System.out.printf("Target: %s\n", Utils.geometryAsWKB(dstGeometry));
//            }
//        }
    }

    private static void printSemantics(SXFRecord sxfRecord) {
        System.out.printf("RecordSemantics info:\n");
        for (SXFRecord.Semantic semantic : sxfRecord.semantics()) {

        }
//        List<SXFRecordSemantic> sxfRecordSemantics = sxfRecord.getSemantics();
//        for (SXFRecordSemantic sxfRecordSemantic : sxfRecordSemantics) {
//            System.out.printf("\t");
//            sxfRecordSemantic.print();
//        }
    }

    private static void printText(SXFRecord sxfRecord) {
//®
    }
}
