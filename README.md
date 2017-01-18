# SXF (Storage and eXchange Format) open format digital map information for Java
Development for use SXF russian vector maps in java with support PostGIS and etc.

Library was designed according with documentation on [SXF format of version 4](http://gistoolkit.ru/download/doc/sxf4bin.pdf) 
and [SXF format of version 3](http://public.gisinfo.ru/Forum/SXF3-30.TXT). All documentation on Russian language.

Library based on Read/Write.
On this moment supports:
- [x] Read SXF
- [ ] Write SXF

Dependes:
 - Java Topology Suite (JTS) - for create geometry and provide them out in WKT, WKB, EWKT formats.
 - PROJ4J - for client side reproject coordinates.
 
 ## Getting Started
 ```
 mvn -U clean install
 ```
 Using sxf4j as library:
 ```
 SXFReaderOptions sxfReaderOptions = new SXFReaderOptions();
 sxfReaderOptions.flipCoordinates = true;
 SXFReader sxfReader = new SXFReader(sxfReaderOptions);
 SXFRecord borderRecord = sxfReader.getRecordByExcode(_sxfPassport.borderExcode).get(0);
 borderRecord.getHeader().print();
 System.out.println(SXFRecordMetric.geometryAsWKT(borderRecord.getMetric().geometry, true));
```
 
 ## Command line tools
 ### sxfinfo [\<options\>] \<sxfile|dir\>
 This tool like as shp2pgsql with same flags and output formats.
 
 Example usage:
 
 `./bin/sxfino -p FILE.SXF`
 
 `./bin/sxfino -p -d FILE.SXF`
 
 `./bin/sxfino -r number:3702 FILE.SXF`
 
 `./bin/sxfino -rg number:3702 FILE.SXF`
 
 ### Command line flags:
 |Flag|Description|Required|
 |----|-----------|--------|
 |`-c`|Print record count|No|
 |`-d`|Print descriptor of SXF|NO|
 |`-f`|Flip coordinates
 |`-gt`|All geometry print type (WKT, EWKT, WKB). Default: WKT.|No|
 |`-h`|Print usage|No|
 |`-p`|Print passport of SXF|No|
 |`-q`|Not print warning messages|No|
 |`-r`|Print record header, text (if exists), semantics (if exists) without geometry (incode:\<i\> - by incode, excode:\<i\> -by excode, number:\<i\> - by number|No|
 |`-rg`|Print record geometry only (incode:\<i\> - by incode, excode:\<i\> - by excode, number:\<i\> - by number|No|
 |`-s`|Set the SRID field. Defaults to 0.|No|
 
 ### sxf2pgsql [\<options\>] <sxfile|dir> [[\<schema\>.]\<table\>]
 `./bin/sxf2pgsql.sh -d -s 4326 /Users/iserge/Develop/Map/500\ 000 | psql -U postgres sxf`
 
 ![separated](https://github.com/iSergio/sxf4j/images/sxf2pgsql.png)
 
 `./bin/sxf2pgsql.sh -s 4326 /Users/iserge/Develop/Map/1\ 000\ 000 1kk | psql -U postgres sxf -q`
 
 ![many2one](https://github.com/iSergio/sxf4j/images/sxf2pgsql-many2one.png)
 
 ### Command line flags:
 |Flag|Description|Required|
 |----|-----------|--------|
 |`-c`|Creates a new table and populates it, this is the default if you do not specify any options.|No|
 |`-d`|Drops the table, then recreates it and populates it with current shape file data.|No|
 |`-e`|Execute each statement individually, do not use a transaction. Not compatible with -D.|No|
 |`-g`|Specify the name of the geometry/geography column.|No, default `geog`|
 |`-h`|Display this help screen.|No|
 |`-I`|Create a spatial index on the geocolumn.|No|
 |`-s`|Set the SRID field. Defaults to detect from passport or 0. Optionally reprojects from given SRID|NO|
 |`-t`|Use only PostGIS coordinates transform (ST_Transform), Use with -s option. Not worked with -D. Default: client side convert (Slow)|
 |`-T`|Specify the tablespace for the new table. Note that indexes will still use the default tablespace unless the -X flag is also used.|No|
 |`-w`|Output WKT instead of WKB.  Note that this can result in coordinate drift.|No|
 |`-W`|Specify the character encoding of SXF attribute column.|No, default `UTF8`|
 |`-X`|Specify the tablespace for the table's indexes. This applies to the primary key, index if the -I flag is used.|No|