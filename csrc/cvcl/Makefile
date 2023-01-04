CC = g++
#JAVA_LIB=/w/10/share/tools/j2sdk1.4.2_04
JAVA_LIB=/w/10/share/tools/jdk1.5.0
#CVCL_DIR=/w/10/share/tools/cvcl-1.1.0
CVCL_DIR=/h/43/arie/cvcl/cvcl-20040808
SWIG_DIR=/w/10/share/tools/SWIG
INCLUDE=-I $(CVCL_DIR)/src/include -I $(JAVA_LIB)/include \
	-I $(JAVA_LIB)/include/linux
#SPECIAL_OPTS=-DDEBUG -D__DEPRECATED -DHAVE_STD__HASH_MAP -DHAVE_STD__HASH_SET
SPECIAL_OPTS=-D__DEPRECATED -DHAVE_STD__HASH_MAP -DHAVE_STD__HASH_SET

all: libJavaCVC.so #example

swigInterface_wrap.cxx: swigInterface.i
	$(SWIG_DIR)/bin/swig -java -c++ -package edu.toronto.cs.tp.cvcl -outdir ../../edu/toronto/cs/tp/cvcl swigInterface.i

swigInterface_wrap.o: swigInterface_wrap.cxx 
	g++ -c -g -O6 -Wall $(INCLUDE) $(SPECIAL_OPTS) swigInterface_wrap.cxx

libJavaCVC.so: swigInterface_wrap.o
	g++ -g -shared swigInterface_wrap.o $(CVCL_DIR)/lib/linux-i686/libcvclite.a -o libJavaCVC.so

#example: example1.java example2.java example3.java example4.java example5.java example6.java exampleMethods.java
#	$(JAVA_LIB)/bin/javac example1.java
#	$(JAVA_LIB)/bin/javac example2.java
#	$(JAVA_LIB)/bin/javac example3.java
#	$(JAVA_LIB)/bin/javac example4.java
#	$(JAVA_LIB)/bin/javac example5.java
#	$(JAVA_LIB)/bin/javac example6.java
#	$(JAVA_LIB)/bin/javac exampleMethods.java

clean:
	rm -rf libJavaCVC.so swigInterface_wrap.o swigInterface_wrap.cxx exampleMethods.class example1.class example2.class \
	example3.class example4.class example5.class example6.class


###
### g++  -I ../src/include -I /w/10/share/tools/j2sdk1.4.2_04/include -I/w/10/share/tools/j2sdk1.4.2_04/include/linux -DDEBUG -D__DEPRECATED -DHAVE_STD__HASH_MAP -DHAVE_STD__HASH_SET -shared -o libJavaCVC.so swigInterface_wrap.cxx libcvclite.a
###
