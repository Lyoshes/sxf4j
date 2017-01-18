package org.cleanlogic.sxf4j.utils;

import org.apache.commons.cli.*;
import org.cleanlogic.sxf4j.exceptions.SXFWrongFormatException;
import org.cleanlogic.sxf4j.fixes.SXFPassportFixes;
import org.cleanlogic.sxf4j.format.SXFRecord;
import org.cleanlogic.sxf4j.format.SXFRecordMetricText;
import org.cleanlogic.sxf4j.format.SXFRecordSemantic;
import org.cleanlogic.sxf4j.io.SXFReaderOptions;
import org.cleanlogic.sxf4j.io.SXFReader;

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

            SXFReaderOptions sxfReaderOptions = new SXFReaderOptions();
            sxfReaderOptions.quite = commandLine.hasOption("quiet");
            sxfReaderOptions.flipCoordinates = commandLine.hasOption('f');
            if (commandLine.hasOption("srid")) {
                String srid = commandLine.getOptionValue("srid");
                if (srid.split(":").length == 2) {
                    sxfReaderOptions.srcSRID = Integer.parseInt(srid.split(":")[0]);
                    sxfReaderOptions.dstSRID = Integer.parseInt(srid.split(":")[1]);
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

            SXFReader sxfReader = new SXFReader(sxfReaderOptions);

            for (File _file : files) {
                if (!sxfReaderOptions.quite) {
                    System.out.printf("Process file %s\n", _file.toString());
                }
                try {
                    sxfReader.read(_file);
                } catch (IOException ex) {
                    ex.printStackTrace();
                    continue;
                } catch (SXFWrongFormatException e) {
                    e.printStackTrace();
                    continue;
                }
                if (commandLine.hasOption("passport")) {
                    sxfReader.getPassport().print();
                }
                if (commandLine.hasOption("descriptor")) {
                    sxfReader.getDescriptor().print();
                }
                if (commandLine.hasOption("count")) {
                    System.out.printf("Total records: %d\n", sxfReader.getRecords().size());
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
                        if (commandLine.hasOption("record")) {
                            sxfRecord.getHeader().print();
                            if (sxfRecord.getHeader().isText) {
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
                                sxfRecord.getHeader().print();
                                if (sxfRecord.getHeader().isText) {
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
                        if (commandLine.hasOption("record")) {
                            sxfRecord.getHeader().print();
                            if (sxfRecord.getHeader().isText) {
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
        if (geometryType.equalsIgnoreCase("WKT")) {
            System.out.println(Utils.geometryAsWKT(sxfRecord.getMetric().getGeometry()));
        } else if (geometryType.equalsIgnoreCase("EWKT")) {
            System.out.println(Utils.geometryAsEWKT(sxfRecord.getMetric().getGeometry()));
        } else if (geometryType.equalsIgnoreCase("WKB")) {
            System.out.println(Utils.geometryAsWKB(sxfRecord.getMetric().getGeometry()));
        }
    }

    private static void printSemantics(SXFRecord sxfRecord) {
        System.out.printf("RecordSemantics info:\n");
        List<SXFRecordSemantic> sxfRecordSemantics = sxfRecord.getSemantics();
        for (SXFRecordSemantic sxfRecordSemantic : sxfRecordSemantics) {
            System.out.printf("\t");
            sxfRecordSemantic.print();
        }
    }

    private static void printText(SXFRecord sxfRecord) {
        System.out.printf("Text:\t{");
        for (int i = 0; i < sxfRecord.getMetric().metricTexts.size(); i++) {
            SXFRecordMetricText sxfRecordMetricText = sxfRecord.getMetric().metricTexts.get(i);
            if (i != 0) {
                System.out.printf(",");
            }
            System.out.printf("%s", sxfRecordMetricText.toString());
        }
        System.out.println("}");
    }
}
