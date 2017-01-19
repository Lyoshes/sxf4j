#!/bin/sh
SXF4J=/opt/sxf4j/sxf4j-utils/target/sxf4j-utils-0.0.1.jar

STARTTIME=$(date +%s)
java -Xmx2048m -cp $SXF4J org.cleanlogic.sxf4j.utils.Sxf2Pgsql "$@"
ENDTIME=$(date +%s)
(>&2 echo "Time elapsed: $(($ENDTIME - $STARTTIME))")
