CC = g++
JAVA_LIB=/w/10/share/tools/j2sdk1.4.2_04
CUDD_DIR=/h/43/arie/NuSMV-vac/cudd-2.3.0.1
MVLCUDD_DIR=/h/43/arie/NuSMV-vac/mvdd
SWIG_DIR=/w/10/share/tools/SWIG
INCLUDE=-I $(CUDD_DIR)/src/include -I $(JAVA_LIB)/include \
	-I $(JAVA_LIB)/include/linux -I $(CUDD_DIR)/include -I $(MVLCUDD_DIR)/include
CUDD_LIBS=$(CUDD_DIR)/lib/libcudd.a $(CUDD_DIR)/lib/libcudd_util.a $(CUDD_DIR)/lib/libmtr.a $(CUDD_DIR)/lib/libst.a 
MVLCUDD_LIBS=$(MVLCUDD_DIR)/lib/libjmvlcudd.a $(MVLCUDD_DIR)/lib/libmvlcudd.a 
#DDDEBUG = -DDD_DEBUG -DDD_VERBOSE -DDD_STATS -DDD_CACHE_PROFILE -DDD_UNIQUE_PROFILE -DDD_COUNT
DDDEBUG = 
all: libCudd.so

cudd_wrap.c: cudd.i
	$(SWIG_DIR)/bin/swig -java -package edu.toronto.cs.jcudd -outdir ../../edu/toronto/cs/jcudd cudd.i

#cudd_wrap.o: cudd_wrap.c 
#	gcc -c -g -O0 -Wall $(INCLUDE) $(SPECIAL_OPTS) cudd_wrap.c

libCudd.so: cudd_wrap.c
	gcc -g -O6 $(DDDEBUG) -shared $(INCLUDE) -o libCudd.so cudd_wrap.c $(MVLCUDD_LIBS)  $(CUDD_LIBS)

#example: example1.java example2.java example3.java example4.java example5.java example6.java exampleMethods.java
#	$(JAVA_LIB)/bin/javac example1.java
#	$(JAVA_LIB)/bin/javac example2.java
#	$(JAVA_LIB)/bin/javac example3.java
#	$(JAVA_LIB)/bin/javac example4.java
#	$(JAVA_LIB)/bin/javac example5.java
#	$(JAVA_LIB)/bin/javac example6.java
#	$(JAVA_LIB)/bin/javac exampleMethods.java

clean:
	rm -rf *.class JMVLCudd.java JMVLCuddJNI.java cudd_wrap.c cudd_wrap.o libCudd.so


###
### g++  -I ../src/include -I /w/10/share/tools/j2sdk1.4.2_04/include -I/w/10/share/tools/j2sdk1.4.2_04/include/linux -DDEBUG -D__DEPRECATED -DHAVE_STD__HASH_MAP -DHAVE_STD__HASH_SET -shared -o libJavaCVC.so swigInterface_wrap.cxx libcvclite.a
###
