#!/bin/bash

TESTS="*.cil.c"
LOG="LOG"

printSeparator ()
{
  echo \
  "----------------------------------------------------------------------" \
  >> $LOG
}

rm -f $LOG

for f in $TESTS
do
  printSeparator
  echo "$f BEGIN" >> $LOG
  printSeparator
  echo -n "$f ... "
  yasm -p 'EF (pc = END)' $f >> $LOG 2>/dev/null
  echo "done."
  printSeparator
  echo "$f END" >> $LOG
  printSeparator
done  


