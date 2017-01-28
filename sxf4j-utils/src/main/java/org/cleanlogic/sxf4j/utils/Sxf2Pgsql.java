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
import org.cleanlogic.sxf4j.enums.Local;
import org.osgeo.proj4j.*;
import org.osgeo.proj4j.io.Proj4FileReader;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author Serge Silaev aka iSergio <s.serge.b@gmail.com>
 */
public class Sxf2Pgsql {
    private static class Sxf2PgsqlOptions {
        String schemaName = "public";
        String tableName = "";

        int srcSRID = 0;
        int dstSRID = 0;
        boolean stTransform = false;

        String geocolumnName = "geom";
        boolean transaction = true;
        boolean spatialIndex = false;
        String geometryFormat = "WKB";
        String encoding = "UTF-8";

        boolean createTable = true;
        boolean dropTable = false;

        boolean pgdumpFormat = false;
    }
    private static Sxf2PgsqlOptions sxf2PgsqlOptions = new Sxf2PgsqlOptions();
    private static CoordinateTransform coordinateTransform;

    public static void main(String... args) {
        Options options = new Options();

        Option sridOption = new Option("s", true, "Set the SRID field. Defaults to detect from passport or 0. Optionally reprojects from given SRID");
        sridOption.setArgName("[<from>:]<srid>");
        options.addOption(sridOption);

        Option stTransformOption = new Option("t", false, "Use only PostGIS coordinates transform (ST_Transform), Use with -s option. Not worked with -D. Default: client side convert.");
        options.addOption(stTransformOption);

        Option geometryColumnOption = new Option("g", true, "Specify the name of the geometry/geography column");
        geometryColumnOption.setArgName("geocolumn");
        options.addOption(geometryColumnOption);

        Option pgdumpFormatOption = new Option("D", false, "Use postgresql dump format (COPY from stdin) (defaults to SQL insert statements).");
        options.addOption(pgdumpFormatOption);

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

            if (commandLine.hasOption("help") || commandLine.getArgList().size() == 0) {
                helpFormatter.printHelp("sxf2pgsql [<options>] <sxfile|dir> [[<schema>.]<table>]", options);
                return;
            }

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
            sxf2PgsqlOptions.stTransform = commandLine.hasOption('t');
            sxf2PgsqlOptions.dropTable = commandLine.hasOption('d');
            if (commandLine.hasOption('g')) {
                sxf2PgsqlOptions.geocolumnName = commandLine.getOptionValue('g');
            }
            sxf2PgsqlOptions.pgdumpFormat = commandLine.hasOption('D');
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
            boolean useNomenclature = true;
            if (commandLine.getArgList().size() == 2) {
                String[] schemaTablePair = commandLine.getArgList().get(1).split(".");
                if (schemaTablePair.length == 2) {
                    sxf2PgsqlOptions.schemaName = schemaTablePair[0];
                    sxf2PgsqlOptions.tableName = schemaTablePair[1];
                } else if (schemaTablePair.length == 0) {
                    sxf2PgsqlOptions.tableName = commandLine.getArgList().get(1);
                }
                useNomenclature = false;
            }

//            SXFReaderOptions sxfReaderOptions = new SXFReaderOptions();
//            sxfReaderOptions.flipCoordinates = true;
//            if (!commandLine.hasOption("t")) {
//                sxfReaderOptions.srcSRID = sxf2PgsqlOptions.srcSRID;
//                sxfReaderOptions.dstSRID = sxf2PgsqlOptions.dstSRID;
//            }

            // Begin document
            System.out.println("SET CLIENT_ENCODING TO UTF8;");
            System.out.println("SET STANDARD_CONFORMING_STRINGS TO ON;");
            System.out.println("SET STATEMENT_TIMEOUT TO 0;");
            System.out.println("SET CLIENT_MIN_MESSAGES TO WARNING;");

            System.out.println("CREATE EXTENSION IF NOT EXISTS plpgsql WITH SCHEMA pg_catalog;");

            // Create schema.table at once (use from command line params)
            if (!useNomenclature && sxf2PgsqlOptions.dropTable) {
                System.out.print(dropTables());
            }
            // Single table mode
            if (!useNomenclature) {
//                if (sxf2PgsqlOptions.transaction) {
//                    System.out.println("BEGIN;");
//                }
                System.out.print(createTables());
            }

            for (File file : files) {
                try {
                    SXFReader sxfReader = new SXFReader(file, true, true);
                    SXFPassport sxfPassport = sxfReader.getPassport();
                    int srid = sxfPassport.srid();
                    //
                    if (!Utils.SRID_EX.containsKey(srid)) {
                        Proj4FileReader proj4FileReader = new Proj4FileReader();
                        String params[] = proj4FileReader.readParametersFromFile("EPSG", String.valueOf(srid));
                        if (params == null || params.length == 0) {
                            // Wrong srid. Force from passport.
                            srid = sxfPassport.srid(true);
                        }
                    }
                    //
                    if (srid != 0) {
                        sxf2PgsqlOptions.srcSRID = srid;
                    }
                    if (!commandLine.hasOption("t")) {
                        if (sxf2PgsqlOptions.srcSRID != sxf2PgsqlOptions.dstSRID) {
                            coordinateTransform = createCoordinateTransform();
                        }
                    }
                    // Each file in separate transaction
                    if (useNomenclature) {
                        sxf2PgsqlOptions.tableName = sxfPassport.getNomenclature();
                        if (sxf2PgsqlOptions.dropTable) {
                            System.out.print(dropTables());
                        }
                        if (sxf2PgsqlOptions.transaction) {
                            System.out.println("BEGIN;");
                        }
                        System.out.print(createTables());
                    }
                    if (!sxf2PgsqlOptions.pgdumpFormat) {
                        for (int i = 0; i < sxfReader.getCount(); i++) {
                            System.out.print(createInsert(sxfReader.getRecordByIncode(i)));
                        }
                    } else {
                        createCopy(sxfReader);
                    }
                    if (useNomenclature) {
                        if (sxf2PgsqlOptions.spatialIndex) {
                            for (Local local : Local.values()) {
                                System.out.print(createIndex(local));
                            }
                        }
                        System.out.println("END;");
                    }
                    coordinateTransform = null;
                } catch (IOException e) {
                    e.printStackTrace();
                } finally {
                    coordinateTransform = null;
                }
            }
            if (!useNomenclature) {
                if (sxf2PgsqlOptions.spatialIndex) {
                    for (Local local : Local.values()) {
                        System.out.print(createIndex(local));
                    }
                }
//                if (sxf2PgsqlOptions.transaction) {
//                    System.out.println("END;");
//                }
            }
        } catch (ParseException e) {
            System.out.println(e.getMessage());
            helpFormatter.printHelp("sxf2pgsql [<options>] <sxfile|dir> [[<schema>.]<table>]", options);

            System.exit(1);
        }
    }

    private static String dropTables() {
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

    private static String createTables() {
        StringBuilder stringBuilder = new StringBuilder();
        for (Local local : Local.values()) {
            String schemaName = sxf2PgsqlOptions.schemaName;
            String tableName = String.format("%s_%s", sxf2PgsqlOptions.tableName, local);
            stringBuilder.append(String.format("CREATE TABLE \"%s\".\"%s\" (\n", schemaName, tableName));
            stringBuilder.append("\t\"gid\" SERIAL PRIMARY KEY,\n");
            stringBuilder.append("\t\"excode\" integer,\n");
            stringBuilder.append("\t\"number\" integer,\n");
            stringBuilder.append("\t\"text\" text,\n");
            stringBuilder.append("\t\"semantics\" varchar[]);\n");

            String geometryType = getGeometryType(local);
            stringBuilder.append(String.format("SELECT AddGeometryColumn('%s', '%s', '%s', '%s', '%s', %d);\n",
                    schemaName,
                    tableName,
                    sxf2PgsqlOptions.geocolumnName,
                    sxf2PgsqlOptions.dstSRID,
                    geometryType,
                    3));
        }
        return stringBuilder.toString();
    }

    private static void createCopy(SXFReader sxfReader) throws IOException {
        // Prepare copy by locals
        Map<Local, List<SXFRecord>> preparedSXFRecords = new HashMap<>();
        for (int i = 0; i < sxfReader.getCount(); i++) {
            SXFRecord sxfRecord = sxfReader.getRecordByIncode(i);
            Local local = sxfRecord.getLocal();
            if (!preparedSXFRecords.containsKey(local)) {
                preparedSXFRecords.put(local, new ArrayList<SXFRecord>());
            }
            preparedSXFRecords.get(local).add(sxfRecord);
        }
        // Done prepare copy

        String schemaName = sxf2PgsqlOptions.schemaName;
        for (Local local : Local.values()) {
            if (preparedSXFRecords.get(local) == null) {
                continue;
            }
            String tableName = String.format("%s_%s", sxf2PgsqlOptions.tableName, local);
            System.out.printf("COPY \"%s\".\"%s\" (\"excode\", \"number\", \"text\", \"semantics\", %s) FROM stdin;\n",
                    schemaName,
                    tableName,
                    sxf2PgsqlOptions.geocolumnName);
            for (SXFRecord sxfRecord : preparedSXFRecords.get(local)) {
                System.out.printf("%s", createCopy(sxfRecord));
            }
            System.out.printf("\\.\n");
        }
    }

    private static String createCopy(SXFRecord sxfRecord) throws IOException {
        Geometry geometry = sxfRecord.geometry();
        if (coordinateTransform != null) {
            geometry = geometryTransform(geometry);
        }
        return String.format("%d\t%d\t%s\t%s\t%s\n",
                sxfRecord.getExcode(),
                sxfRecord.getNumber(),
                textsToString(sxfRecord.texts(), true),
                semanticsToPgArray(sxfRecord.semantics(), true),
                Utils.geometryAsWKB(geometry));
    }

    private static String createInsert(SXFRecord sxfRecord) throws IOException {
        Geometry geometry = sxfRecord.geometry();
        if (coordinateTransform != null) {
            geometry = geometryTransform(geometry);
        }

        StringBuilder stringBuilder = new StringBuilder();

        String schemaName = sxf2PgsqlOptions.schemaName;
        String tableName = String.format("%s_%s", sxf2PgsqlOptions.tableName, sxfRecord.getLocal());
        stringBuilder.append(String.format("INSERT INTO \"%s\".\"%s\" ", schemaName, tableName));
        stringBuilder.append(String.format("(\"excode\", \"number\", \"text\", \"semantics\", %s) ", sxf2PgsqlOptions.geocolumnName));
        stringBuilder.append("VALUES ");
        stringBuilder.append("(");
        stringBuilder.append(String.format("'%d',", sxfRecord.getExcode()));
        stringBuilder.append(String.format("'%d',", sxfRecord.getNumber()));
        stringBuilder.append(String.format("'%s',", textsToString(sxfRecord.texts())));
        stringBuilder.append(String.format("'%s'::varchar[],", semanticsToPgArray(sxfRecord.semantics())));
        if (sxf2PgsqlOptions.stTransform) {
            stringBuilder.append(String.format("ST_Transform('%s'::geometry, %d)", Utils.geometryAsWKB(geometry), sxf2PgsqlOptions.dstSRID));
        } else {
            stringBuilder.append(String.format("'%s'", Utils.geometryAsWKB(geometry)));
        }
        stringBuilder.append(");\n");
        sxfRecord.destroy();

        return stringBuilder.toString();
    }

    private static String createIndex(Local local) {
        String indexStr = String.format("CREATE INDEX \"%s_%s_%s_gist\" ON \"%s\".\"%s_%s\" USING GIST (\"%s\");\n",
                                        sxf2PgsqlOptions.tableName.toLowerCase(),
                                        local.toString().toLowerCase(),
                                        sxf2PgsqlOptions.geocolumnName.toLowerCase(),
                                        sxf2PgsqlOptions.schemaName,
                                        sxf2PgsqlOptions.tableName,
                                        local,
                                        sxf2PgsqlOptions.geocolumnName);
        indexStr += String.format("CREATE INDEX \"%s_%s_excode_btree\" ON \"%s\".\"%s_%s\" USING BTREE (\"excode\");\n",
                                    sxf2PgsqlOptions.tableName.toLowerCase(),
                                    local.toString().toLowerCase(),
                                    sxf2PgsqlOptions.schemaName,
                                    sxf2PgsqlOptions.tableName,
                                    local);
        indexStr += String.format("CREATE INDEX \"%s_%s_number_btree\" ON \"%s\".\"%s_%s\" USING BTREE (\"number\");\n",
                                    sxf2PgsqlOptions.tableName.toLowerCase(),
                                    local.toString().toLowerCase(),
                                    sxf2PgsqlOptions.schemaName,
                                    sxf2PgsqlOptions.tableName,
                                    local);
        return indexStr;
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
        geometry.setSRID(sxf2PgsqlOptions.dstSRID);
        return geometry;
    }

    private static String textsToString(List<SXFRecord.Text> texts) {
        return textsToString(texts, false);
    }

    private static String textsToString(List<SXFRecord.Text> texts, boolean copy) {
        StringBuilder stringBuilder = new StringBuilder();
        for (SXFRecord.Text text : texts) {
            String value = text.getText();
            if (!copy) {
                value = value.replace("\\", "\\\\\\");
                value = value.replace("\n", "\\\\n");
                value = value.replace("\r", "\\\\r");
                value = value.replace("\t", "\\\\t");
                value = value.replace("\"", "\\\"");
                value = value.replace("'", "''");
            } else {
                value = value.replace("\\", "\\\\\\\\");
                value = value.replace("\n", "\\\\\\n");
                value = value.replace("\r", "\\\\\\r");
                value = value.replace("\t", "\\\\\\t");
                value = value.replace("\"", "\\\\\"");
            }
            stringBuilder.append(value);
        }
        return stringBuilder.toString();
    }

    private static String semanticsToPgArray(List<SXFRecord.Semantic> semantics) {
        return semanticsToPgArray(semantics, false);
    }

    private static String semanticsToPgArray(List<SXFRecord.Semantic> semantics, boolean copy) {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("{");
        for (int i = 0; i < semantics.size(); i++) {
            SXFRecord.Semantic semantic = semantics.get(i);
            if (i != 0) {
                stringBuilder.append(",");
            }

            if (!copy) {
                semantic.value = semantic.value.replace("\\", "\\\\\\");
                semantic.value = semantic.value.replace("\n", "\\\\n");
                semantic.value = semantic.value.replace("\r", "\\\\r");
                semantic.value = semantic.value.replace("\t", "\\\\t");
                semantic.value = semantic.value.replace("\"", "\\\"");
                semantic.value = semantic.value.replace("'", "''");
            } else {
                semantic.value = semantic.value.replace("\\", "\\\\\\\\");
                semantic.value = semantic.value.replace("\n", "\\\\\\n");
                semantic.value = semantic.value.replace("\r", "\\\\\\r");
                semantic.value = semantic.value.replace("\t", "\\\\\\t");
                semantic.value = semantic.value.replace("\"", "\\\\\"");
            }
            stringBuilder.append(String.format("{\"%s\",\"%s\"}", semantic.code, semantic.value));
        }
        stringBuilder.append("}");
        return stringBuilder.toString();
    }

    private static CoordinateTransform createCoordinateTransform() {
        CoordinateTransformFactory coordinateTransformFactory = new CoordinateTransformFactory();
        CRSFactory crsFactory = new CRSFactory();

        CoordinateReferenceSystem srcCRS;
        CoordinateReferenceSystem dstCRS;

        if (Utils.SRID_EX.containsKey(sxf2PgsqlOptions.srcSRID)) {
            srcCRS = crsFactory.createFromParameters("EPSG:" + sxf2PgsqlOptions.srcSRID, Utils.SRID_EX.get(sxf2PgsqlOptions.srcSRID));
        } else {
            srcCRS = crsFactory.createFromName("EPSG:" + sxf2PgsqlOptions.srcSRID);
        }

        if (Utils.SRID_EX.containsKey(sxf2PgsqlOptions.dstSRID)) {
            dstCRS = crsFactory.createFromParameters("EPSG:" + sxf2PgsqlOptions.dstSRID, Utils.SRID_EX.get(sxf2PgsqlOptions.dstSRID));
        } else {
            dstCRS = crsFactory.createFromName("EPSG:" + sxf2PgsqlOptions.dstSRID);
        }

        return coordinateTransformFactory.createTransform(srcCRS, dstCRS);
    }
}
