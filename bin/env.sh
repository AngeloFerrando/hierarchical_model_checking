#!/bin/sh

###
### Sets up a working environment
###

###
### usage env ROOT_DIR
### 

ROOT=$1

echo "Using $1 as ROOT"
export ROOT

SYSTEM=`uname`

### Figure out system directory
case $SYSTEM in
  Linux) 
    SYSTEM_DIR=linux ;;
  SunOS)
    SYSTEM_DIR=sparc ;;
  *)
    echo "I don't know your system: $SYSTEM, sorry" 
    exit 1 ;;
esac

###
### Debug
###

#echo $SYSTEM
#echo $SYSTEM_DIR
#echo $ROOT

### Now we source the environment settings
echo "Setting up environment variables for you"
source $ROOT/bin/$SYSTEM_DIR/env.sh

echo "done"