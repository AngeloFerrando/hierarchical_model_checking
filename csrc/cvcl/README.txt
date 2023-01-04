1.  Download the latest SWIG  (simplified wrapper and interface generator) and CVCLite, install them on your computer

2.  USE linus system! Will NOT run in Solaris Machines

3.  Make sure that you have the latest Java Comiler.  (The version I used was j2sdk1.4.2_04) Otherwise, you could get a segmentation fault

4.  Make sure that you have the latest version of G++ compiler

5.  In the make file, 
       - change the HOME to the directory of your home
       - change the JAVA_LIB to the directory of your java compiler
       - change the CVCL_DIR to the directory of CVCLite
       - change the SWIG_DIR to the directory of SWIG

6. Due to an internal error of SWIG, you need to add two default constructors to two classes in the CVCLite library
       - go to (home directory of CVCLite)/src/include/
       - open file expr_op.h
       - find the class definition of Op (class Op)
       - find the default destructor ~Op(), and add a default construtor just before the destructor as follows
		       Op(){} //added
		       ~Op() //original
       - open file debug.h and repeat the same procedure for class DebugFlag
       - open file expr_manager.h and repeat the same precedure for class ExprManager

7. Run make

8. To run the examples, you also need to complile exampleMethods.java

       
    
