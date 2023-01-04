####
#### This sets up work environment for a linux workstation
#### source it somewhere to get going
####

#### depends on ROOT pointing to the project directory

#### JDK working environment
export JAVA_HOME=/pkgs/jdk1.3.0/sol8
#export JAVAC=$JAVA_HOME/bin/javac
export JAVA=$JAVA_HOME/bin/java
export JAVAC=$ROOT/bin/$SYSTEM_DIR/jikes
export JAVACOPT='+E +D -g'
#### put java runtime, project dir, and buildLib.jar into the classpath
export CLASSPATH=$JAVA_HOME/jre/lib/rt.jar:$ROOT:$ROOT/lib/buildLib.jar

#### put the right things into path
export OLD_PATH=$PATH
export PATH=$ROOT/bin:$ROOT/bin/$SYSTEM_DIR:$JAVA_HOME/bin:$PATH
