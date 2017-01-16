package org.cleanlogic.sxf4j.utils;

import com.sun.javafx.binding.StringFormatter;
import org.apache.commons.cli.*;
import org.cleanlogic.sxf4j.SXF;
import org.cleanlogic.sxf4j.enums.Local;
import org.cleanlogic.sxf4j.format.SXFPassport;
import org.cleanlogic.sxf4j.format.SXFRecord;
import org.cleanlogic.sxf4j.format.SXFRecordHeader;
import org.cleanlogic.sxf4j.io.SXFReader;
import org.cleanlogic.sxf4j.io.SXFReaderOptions;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * @author Serge Silaev aka iSergio <s.serge.b@gmail.com>
 */
public class SXF2Pgsql {
    static class Sxf2PgsqlOptions {
        String schemaName = "public";
        String tableName = "";

        int srcSRID = 0;
        int dstSRID = 0;
        String geocolumnName = "geom";
        boolean transaction = true;
        boolean spatialIndex = false;
        String geometryFormat = "WKB";
        String encoding = "UTF-8";
        boolean createTable = true;
        boolean dropTable = false;
    }
    static Sxf2PgsqlOptions sxf2PgsqlOptions = new Sxf2PgsqlOptions();

    public static void main(String... args) {
        Options options = new Options();

        Option sridOption = new Option("s", true, "Set the SRID field. Defaults to detect from passport or 0. Optionally reprojects from given SRID");
        sridOption.setArgName("[<from>:]<srid>");
        options.addOption(sridOption);

        Option geometryColumnOption = new Option("g", true, "Specify the name of the geometry/geography column");
        geometryColumnOption.setArgName("geocolumn");
        options.addOption(geometryColumnOption);

        Option transactionOption = new Option("e", false, "Execute each statement individually, do not use a transaction. Not compatible with -D.");
        options.addOption(transactionOption);

        Option spatialIndexOption = new Option("I", false, "Create a spatial index on the geocolumn.");
        options.addOption(spatialIndexOption);

        Option geometryTypeOption = new Option("w", false, "Output WKT instead of WKB.  Note that this can result in coordinate drift.");
        options.addOption(geometryTypeOption);

        Option encodingOption = new Option("W", true, "Specify the character encoding of SXF attribute column. (default: \"UTF-8\")");
        encodingOption.setArgName("encoding");
        options.addOption(encodingOption);

        Option tablespaceTableOption = new Option("T", true, "Specify the tablespace for the new table. Note that indexes will still use the default tablespace unless the -X flag is also used.");
        tablespaceTableOption.setArgName("tablespace");
        options.addOption(tablespaceTableOption);

        Option tablespaceIndexOption = new Option("X", true, "Specify the tablespace for the table's indexes. This applies to the primary key, and the spatial index if the -I flag is used.");
        tablespaceIndexOption.setArgName("tablespace");
        options.addOption(tablespaceIndexOption);

        Option helpOption = new Option("h", "help", false, "Display this help screen.");
        options.addOption(helpOption);

        OptionGroup optionGroup = new OptionGroup();

        Option dropTableOption = new Option("d", false, "Drops the table, then recreates it and populates it with current shape file data.");
        optionGroup.addOption(dropTableOption);

        Option createTableOption = new Option("c", false, "Creates a new table and populates it, this is the default if you do not specify any options.");
        optionGroup.addOption(createTableOption);

        options.addOptionGroup(optionGroup);

        CommandLineParser commandLineParser = new DefaultParser();
        HelpFormatter helpFormatter = new HelpFormatter();
        CommandLine commandLine;

        try {
            commandLine = commandLineParser.parse(options, args);

            if (commandLine.hasOption('s')) {
                String srid = commandLine.getOptionValue('s');
                String[] sridPair = srid.split(":");
                if (sridPair.length == 2) {
                    sxf2PgsqlOptions.srcSRID = Integer.parseInt(sridPair[0]);
                    sxf2PgsqlOptions.dstSRID = Integer.parseInt(sridPair[1]);
                } else if (sridPair.length == 1) {
                    sxf2PgsqlOptions.dstSRID = Integer.parseInt(srid);
                }
            }
            sxf2PgsqlOptions.dropTable = commandLine.hasOption('d');
            if (commandLine.hasOption('g')) {
                sxf2PgsqlOptions.geocolumnName = commandLine.getOptionValue('g');
            }
            sxf2PgsqlOptions.transaction = !commandLine.hasOption('e');
            sxf2PgsqlOptions.spatialIndex = commandLine.hasOption('I');
            if (commandLine.hasOption('w')) {
                sxf2PgsqlOptions.geometryFormat = "WKT";
            }
            if (commandLine.hasOption('W')) {
                sxf2PgsqlOptions.encoding = commandLine.getOptionValue('W');
            }

            List<File> files = new ArrayList<>();
            if (commandLine.getArgList().size() > 0) {
                File file = new File(commandLine.getArgList().get(0));

                if (file.isFile()) {
                    files.add(file);
                } else if (file.isDirectory()) {
                    Utils.search(file, files, ".sxf");
                }
            }
            if (commandLine.getArgList().size() >= 2) {
                String[] schemaTablePair = commandLine.getArgList().get(1).split(".");
                if (schemaTablePair.length == 2) {
                    sxf2PgsqlOptions.schemaName = schemaTablePair[0];
                    sxf2PgsqlOptions.tableName = schemaTablePair[1];
                } else if (schemaTablePair.length == 1) {
                    sxf2PgsqlOptions.schemaName = schemaTablePair[0];
                }
            }

            SXFReaderOptions sxfReaderOptions = new SXFReaderOptions();
            sxfReaderOptions.flipCoordinates = true;
            SXFReader sxfReader = new SXFReader(sxfReaderOptions);
            for (File file : files) {
                try {
                    sxfReader.read(file);
                    SXFPassport sxfPassport = sxfReader.getPassport();
                    sxf2PgsqlOptions.srcSRID = Utils.detectSRID(sxfPassport);
                    if (sxf2PgsqlOptions.tableName.isEmpty()) {
                        sxf2PgsqlOptions.tableName = sxfPassport.nomenclature;
                    }
                    StringBuilder stringBuilder = new StringBuilder();
                    stringBuilder.append("SET CLIENT_ENCODING TO UTF8;\n");
                    stringBuilder.append("SET STANDARD_CONFORMING_STRINGS TO ON;\n");
                    if (sxf2PgsqlOptions.dropTable) {
                        stringBuilder.append(dropTables());
                    }
                    if (sxf2PgsqlOptions.transaction) {
                        stringBuilder.append("BEGIN;\n");
                    }
                    stringBuilder.append(createTables());
                    stringBuilder.append(createInserts(sxfReader.getRecords()));
                    if (sxf2PgsqlOptions.transaction) {
                        stringBuilder.append("END;\n");
                    }
                    System.out.printf(stringBuilder.toString());
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

            if (commandLine.hasOption("help") || commandLine.getArgList().size() == 0) {
                helpFormatter.printHelp("sxf2pgsql [<options>] <sxfile|dir> [[<schema>.]<table>]", options);
                return;
            }
        } catch (ParseException e) {
            System.out.println(e.getMessage());
            helpFormatter.printHelp("sxf2pgsql [<options>] <sxfile|dir> [[<schema>.]<table>]", options);

            System.exit(1);
        }
    }

    public static String dropTables() {
        StringBuilder stringBuilder = new StringBuilder();
        for (Local local : Local.values()) {
            String schemaName = sxf2PgsqlOptions.schemaName;
            String tableName = String.format("%s_%s", sxf2PgsqlOptions.tableName, local);

            stringBuilder.append(String.format("SELECT DropGeometryColumn('%s', '%s', '%s');\n",
                    schemaName,
                    tableName,
                    sxf2PgsqlOptions.geocolumnName));

            stringBuilder.append(String.format("DROP TABLE IF EXISTS \"%s\".\"%s\";\n", schemaName, tableName));
        }
        return stringBuilder.toString();
    }

    public static String createTables() {
        StringBuilder stringBuilder = new StringBuilder();
        for (Local local : Local.values()) {
            String schemaName = sxf2PgsqlOptions.schemaName;
            String tableName = String.format("%s_%s", sxf2PgsqlOptions.tableName, local);
            stringBuilder.append(String.format("CREATE TABLE \"%s\".\"%s\" (gid SERIAL PRIMARY KEY,\n", schemaName, tableName));
            stringBuilder.append("\"excode\" integer,\n");
            stringBuilder.append("\"number\" integer,\n");
            stringBuilder.append("\"text\" text,\n");
            stringBuilder.append("\"semantics\" varchar[]);\n");

            String geometryType = getGeometryType(local);
            stringBuilder.append(String.format("SELECT AddGeometryColumn('%s', '%s', '%s', '%s', '%s', %d);\n",
                    schemaName,
                    tableName,
                    sxf2PgsqlOptions.geocolumnName,
                    sxf2PgsqlOptions.srcSRID,
                    geometryType,
                    3));
        }
        return stringBuilder.toString();
    }

    public static String createInserts(List<SXFRecord> sxfRecords) {
        StringBuilder stringBuilder = new StringBuilder();

        String schemaName = sxf2PgsqlOptions.schemaName;
        for (SXFRecord sxfRecord : sxfRecords) {
            SXFRecordHeader sxfRecordHeader = sxfRecord.getHeader();
            String tableName = String.format("%s_%s", sxf2PgsqlOptions.tableName, sxfRecordHeader.local);
            stringBuilder.append(String.format("INSERT INTO \"%s\".\"%s\" ", schemaName, tableName));
            stringBuilder.append(String.format("(\"excode\", \"number\", \"text\", \"semantics\", %s) ", sxf2PgsqlOptions.geocolumnName));
            stringBuilder.append("VALUES ");
            stringBuilder.append("(");
            stringBuilder.append(String.format("'%d',", sxfRecordHeader.excode));
            stringBuilder.append(String.format("'%d',", sxfRecordHeader.number));
            stringBuilder.append(String.format("'%s',", "text"));
            stringBuilder.append(String.format("'{%s}'::varchar[],", "semantics"));
            stringBuilder.append(String.format("'%s'", sxfRecord.getMetric().geometryAsWKB()));
            stringBuilder.append(");\n");
        }

        return stringBuilder.toString();
    }

    private static String getGeometryType(Local local) {
        String geometryType = "";
        switch (local) {
            case LINE:
            case MIXED:
            case TITLE:
            case VECTOR: geometryType = "MULTILINESTRING"; break;
            case SQUARE: geometryType = "MULTIPOLYGON"; break;
            case POINT: geometryType = "MULTIPOINT"; break;
            default: break;
        }
        return geometryType;
    }
}
