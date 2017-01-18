#!/bin/sh
SXF4J=target/sxf4j-utils-0.0.1.jar
java -cp $SXF4J org.cleanlogic.sxf4j.utils.SxfInfo "$@"
