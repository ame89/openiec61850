#!/bin/bash

DIR_NAME=`dirname $0`

CLASSPATH=""

for l in `find $DIR_NAME/../../../build/libsdeps/ -name "*jar"`; do CLASSPATH=${CLASSPATH}:$l ; done

java -Dlogback.configurationFile=logback.xml -cp $CLASSPATH org.openmuc.openiec61850.sample.SampleClient $@
