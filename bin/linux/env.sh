#### source it somewhere to get going
####

#### depends on ROOT pointing to the project directory



#### JDK working environment
export JAVA_HOME=/usr/lib/jvm/java-17-openjdk-i386
export JAVAC=$JAVA_HOME/bin/javac
export JAVA=$JAVA_HOME/bin/java
export ANT_HOME=/w/10/share/tools/apache-ant
#export JAVAC=$ROOT/bin/linux/jikes
#export JAVACOPT='+E +D -g'

#### put java runtime, project dir, and buildLib.jar into the classpath
### build the classpath
### include Java Runtime Libraries and our build directory
MYCLASSPATH=$JAVA_HOME/jre/lib/rt.jar:$ROOT/build
### add libraries from buildLib
for i in $ROOT/lib/buildLib/*.jar
do
  MYCLASSPATH=$MYCLASSPATH:$i
done
#export CLASSPATH=$JAVA_HOME/jre/lib/rt.jar:$ROOT:$ROOT/lib/buildLib.jar
export CLASSPATH=$MYCLASSPATH

### XXX Bad but simple
#export DAVINCIHOME=/w/10/share/tools/daVinci
export DAVINCIHOME=${DAVINCIHOME:-"/w/10/share/tools/daVinci-3.0.4"}
export CILHOME=${CILHOME:-"/w/10/share/tools/cil/bin"}
#### put the right things into path
export PATH=$ROOT/bin:$ROOT/bin/linux:$JAVA_HOME/bin:$PATH:$DAVINCIHOME:$DAVINCIHOME/bin:$CILHOME:$ANT_HOME/bin

#### LD_LIBRARY_PATH -- source of shared libraries
export LD_LIBRARY_PATH=$ROOT/lib:$LD_LIBRARY_PATH
