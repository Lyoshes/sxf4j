#!/bin/sh
SXF4J=/opt/sxf4j/sxf4j-utils/target/sxf4j-utils-0.0.1.jar

java -cp $SXF4J org.cleanlogic.sxf4j.utils.SxfInfo "$@"
