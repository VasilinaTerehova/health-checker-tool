#!/bin/bash

BASEDIR=$( cd "$( dirname "$0" )" && pwd )

if [ -z "$TEMP" ]; then
  echo "No temp dir, will use $BASEDIR/upload"
  OPT="-Djava.io.tmpdir=$BASEDIR/upload"
else
  echo "Temp dir - $TEMP"
fi

java -jar $OPT health-checker-tool-controller-0.0.1-SNAPSHOT.jar