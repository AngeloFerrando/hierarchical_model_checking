
### Makefile for libjcudd

CC 	= gcc
CUDDLIB	= $(ROOT)/tools/cudd

INCLUDE = -I $(JAVA_HOME)/include -I $(JAVA_HOME)/include/linux -I $(CUDDLIB)/include 
OPTS	= -shared $(INCLUDE) $(XCFLAGS)

LIBS	= $(CUDDLIB)/lib/libcudd.a  $(CUDDLIB)/lib/libdddmp.a  $(CUDDLIB)/lib/libepd.a  $(CUDDLIB)/lib/libmtr.a    $(CUDDLIB)/lib/libst.a  $(CUDDLIB)/lib/libutil.a
#$(CUDDLIB)/lib/libobj.a

XCFLAGS = -mcpu=pentiumpro -malign-double -DHAVE_IEEE_754 -DBSD 

libjcudd.so: Cudd.c CuddAdd.h CuddAdd.c CubeIterator.h cubeIterator.c
	$(CC) $(OPTS) -o libjcudd.so Cudd.c CuddAdd.c cubeIterator.c $(LIBS)
#Cudd.h: $(ROOT)/edu/toronto/cs/cudd/Cudd.java
#	javah -jni -o Cudd.h edu.toronto.cs.cudd.Cudd
CuddAdd.h: $(ROOT)/edu/toronto/cs/cudd/CuddAdd.java
	javah -jni -o CuddAdd.h edu.toronto.cs.cudd.CuddAdd
CubeIterator.h: $(ROOT)/edu/toronto/cs/cudd/CuddAdd.java
	javah -jni -o CubeIterator.h 'edu.toronto.cs.cudd.CuddAdd$$CubeIterator'
cubeIterator.o: cubeIterator.c
	$(CC) $(INCLUDE) -c -o cubeIterator.o cubeIterator.c
CuddAdd.o: CuddAdd.c
	$(CC) $(INCLUDE) -c -o CuddAdd.o CuddAdd.c
install: libjcudd.so
	cp libjcudd.so $(ROOT)/lib
test:	cuddTest.c
	$(CC) $(XCFLAGS) $(INCLUDE) -g  -o test cuddTest.c $(LIBS)
