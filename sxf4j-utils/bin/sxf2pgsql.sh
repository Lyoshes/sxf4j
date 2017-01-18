#!/bin/sh
SXF4J=target/sxf4j-utils-0.0.1.jar

#STARTTIME=$(date +%s)
java -Xmx1024m -cp $SXF4J org.cleanlogic.sxf4j.utils.Sxf2Pgsql "$@"
#ENDTIME=$(date +%s)
#(>&2 echo "Time elapsed: $(($ENDTIME - $STARTTIME))")
