#!/bin/sh
CLASSPATH=../tools/lcdgen
LCDGEN="java -cp $CLASSPATH Main"

set -x
FILES=`pwd`/*.txt

for FIN in $FILES
do
    FOUT="${FIN%.txt}"
    for SIZE in "small" "medium" "large"
    do
	$LCDGEN $SIZE "$FIN" "$FOUT-$SIZE.gif"
	convert "$FOUT-$SIZE.gif" "$FOUT-$SIZE.eps"
	convert "$FOUT-$SIZE.gif" "$FOUT-$SIZE.pdf"
    done
done