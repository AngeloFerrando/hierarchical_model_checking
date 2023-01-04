%module JMVLCudd
%include exception.i
%include carrays.i
%array_class(int, CIntArray);
%include cpointer.i
%pointer_class(int,CIntPtr);
%include typemaps.i

%{
#include "cudd.h"
#include "jmvlcudd.h"
#include "jmvlcuddInt.h"

/* some garbage to get rid of NuSMV dependecies */
int one_number = 1;
int zero_number = 0;

int print_node (FILE* fp, node_ptr v)
{
  fprintf (fp, "%d", v);
  return 0;
}
void start_parsing_err ()
{
}

void finish_parsing_err ()
{
}

#define ddSize(X) (X)->size

%}

%inline %{
  typedef struct 
  {
    int * cube;
    int value;
  } CuddCube;  
%}

%typemap(in) (int** CUBE, int *VALUE)
{
  jclass clazz;
  jfieldID fid;
  jlong cPtr;
  
  CuddCube *pCuddCube = NULL;

  clazz = (*jenv)->GetObjectClass (jenv, $input);
  fid = (*jenv)->GetFieldID (jenv, clazz, "swigCPtr", "J");
  cPtr = (*jenv)->GetLongField (jenv, $input, fid);

  pCuddCube = *(CuddCube **) &cPtr;
  $1 = &pCuddCube->cube;
  $2 = &pCuddCube->value;
  /* fprintf (stderr, "cube record %x, %x, %x\n", pCuddCube, $1, $2);*/
}
%typemap(jtype) (int** CUBE, int *VALUE) "CuddCube";
%typemap(jstype) (int** CUBE, int *VALUE) "CuddCube";
%typemap(jni) (int** CUBE, int *VALUE) "jobject";
%typemap(javain) (int** CUBE, int *VALUE) "$javainput";


// -- set environment field to be used by callbacks
%typemap(in) mvlDdManager * jmanager
{
  $1 = (mvlDdManager*)$input;
  SET_ENV($1,jenv);
}

// -- extract DdManager from mvlDdManager
%typemap(in) DdManager * 
{
  $1 = CUDD((mvlDdManager*)$input);
}

%typemap(jni) algebra * jalgebra "jobject";
%typemap(jtype) algebra * jalgebra "Object";
%typemap(jstype) algebra * jalgebra "Object";
%typemap(javain) algebra * jalgebra "$javainput";


%typemap(in) algebra * jalgebra
{
  jclass clazz;
  $1 = (struct algebra *) malloc (sizeof (struct algebra));
  $1->jalgebra = (*jenv)->NewGlobalRef (jenv, $input);

  clazz = (*jenv)->GetObjectClass (jenv, $input);
  
  $1->meetID = (*jenv)->GetMethodID (jenv, clazz, "meet", "(II)I");
  $1->joinID = (*jenv)->GetMethodID (jenv, clazz, "join", "(II)I");
  $1->negID = (*jenv)->GetMethodID (jenv, clazz, "neg", "(I)I");

  $1->infoMeetID = (*jenv)->GetMethodID (jenv, clazz, "infoMeet", "(II)I");
  $1->infoJoinID = (*jenv)->GetMethodID (jenv, clazz, "infoJoin", "(II)I");
  $1->infoNegID = (*jenv)->GetMethodID (jenv, clazz, "infoNeg", "(I)I");

  
}

%typemap(in) mvlDdManager * quit
{
  $1 = (mvlDdManager*) $input;
  (*jenv)->DeleteGlobalRef (jenv, ALGEBRA($1)->jalgebra);
}


%typemap(jni) DdNode * "jint";
%typemap(jtype) DdNode * "int";
%typemap(jstype) DdNode * "int";
%typemap(javaout) DdNode * "{return $jnicall;}"
%typemap(javain) DdNode * "$javainput"

%typemap(jni) DdManager * "jint";
%typemap(jtype) DdManager * "int";
%typemap(jstype) DdManager * "int";
%typemap(javaout) DdManager * "{return $jnicall;}"
%typemap(javain) DdManager * "$javainput"

%typemap(jni) mvlDdManager * "jint";
%typemap(jtype) mvlDdManager * "int";
%typemap(jstype) mvlDdManager * "int";
%typemap(javaout) mvlDdManager * "{return $jnicall;}"
%typemap(javain) mvlDdManager * "$javainput"

%typemap(jni) DdGen * "jint";
%typemap(jtype) DdGen * "int";
%typemap(jstype) DdGen * "int";
%typemap(javaout) DdGen * "{return $jnicall;}"
%typemap(javain) DdGen * "$javainput"

%typemap(jni)  CUDD_VALUE_TYPE "jint";
%typemap(jtype) CUDD_VALUE_TYPE "int";
%typemap(jstype)  CUDD_VALUE_TYPE "int";
%typemap(javaout) CUDD_VALUE_TYPE "{return $jnicall;}"
%typemap(javain)  CUDD_VALUE_TYPE "$javainput"

/*%javamethodmodifiers "public synchronized"*/




typedef enum {
    CUDD_REORDER_SAME,
    CUDD_REORDER_NONE,
    CUDD_REORDER_RANDOM,
    CUDD_REORDER_RANDOM_PIVOT,
    CUDD_REORDER_SIFT,
    CUDD_REORDER_SIFT_CONVERGE,
    CUDD_REORDER_SYMM_SIFT,
    CUDD_REORDER_SYMM_SIFT_CONV,
    CUDD_REORDER_WINDOW2,
    CUDD_REORDER_WINDOW3,
    CUDD_REORDER_WINDOW4,
    CUDD_REORDER_WINDOW2_CONV,
    CUDD_REORDER_WINDOW3_CONV,
    CUDD_REORDER_WINDOW4_CONV,
    CUDD_REORDER_GROUP_SIFT,
    CUDD_REORDER_GROUP_SIFT_CONV,
    CUDD_REORDER_ANNEALING,
    CUDD_REORDER_GENETIC,
    CUDD_REORDER_LINEAR,
    CUDD_REORDER_LINEAR_CONVERGE,
    CUDD_REORDER_EXACT
} Cudd_ReorderingType;


%inline %{
  int isCube (DdManager * dd, DdNode * node)
    {
      DdNode * zero, *one;
      DdNode * N;
      DdNode * n0, * n1;
      
      one = DD_ONE(dd);
      zero = Cudd_Not (one);
      
      if (node == zero) return 0;
      else if (node == one) return 1;
      
      N = Cudd_Regular (node);
      n1 = cuddT (N);
      n0 = cuddE (N);
      if (N != node)
	{
	  n1 = Cudd_Not (n1);
	  n0 = Cudd_Not (n0);
	}
      

      if (n1 != zero && n0 == zero)
	return isCube (dd, n1);
      else if (n1 == zero && n0 != zero)
	return isCube (dd, n0);
      return 0;

    }

%}


void Cudd_AutodynEnable (DdManager * dd, Cudd_ReorderingType method);
void Cudd_AutodynDisable (DdManager * dd);

mvlDdManager * jmvlCudd_Init (int nvars, algebra * jalgebra, 
			      int top, int bot, 
			      int infoTop, int infoBot);
void jmvlCudd_Quit (mvlDdManager * quit);
void Cudd_RecursiveDeref (DdManager * dd, DdNode * f);

int Cudd_PrintMinterm(DdManager *dd, DdNode *node);

int Cudd_CheckZeroRef (DdManager *dd);

int cuddV (DdNode *node);
int Cudd_IsConstant (DdNode *node);
int cuddGarbageCollect (DdManager *dd, int clearCache);
void Cudd_Ref (DdNode *node);
int Cudd_DagSize (DdNode *node);

DdGen * Cudd_FirstCube (DdManager *dd, DdNode *node, int **CUBE, int *VALUE);
int Cudd_NextCube (DdGen * gen, int **CUBE, int *VALUE);
int Cudd_GenFree (DdGen * gen);
int ddSize (DdManager *dd);



%exception 
  { 
	$action
	if (result == NULL)
         { SWIG_exception (SWIG_RuntimeError, "CUDD Error"); }
        else
           Cudd_Ref (result);

  }

/***
 *** CUDD bdd operations
 ***/
DdNode * Cudd_ReadOne (DdManager *dd);
DdNode * Cudd_ReadLogicZero (DdManager *dd);

DdNode * Cudd_Not (DdNode * node);
DdNode * Cudd_bddAnd (DdManager *dd, DdNode *f, DdNode *g);
DdNode * Cudd_bddOr (DdManager *dd, DdNode *f, DdNode *g);
DdNode * Cudd_bddIte (DdManager *dd, DdNode *f, DdNode *g, DdNode *h);
DdNode * Cudd_bddUnivAbstract (DdManager *manager, DdNode *f, DdNode *cube);
DdNode * Cudd_bddExistAbstract (DdManager *manager, DdNode *f, DdNode *cube);
DdNode * Cudd_bddNewVar (DdManager *dd);
DdNode * Cudd_bddIthVar (DdManager *dd, int i);
DdNode * Cudd_bddXor (DdManager *dd, DdNode *f, DdNode *g);
DdNode * Cudd_bddPermute (DdManager *manager, DdNode *node, int *permut);
DdNode * Cudd_bddAndAbstract (DdManager *manager, DdNode *f, DdNode *g, DdNode *cube);
/***
 *** CUDD add operations
 ***/
DdNode * Cudd_addNewVar (DdManager *dd);
DdNode * Cudd_addIthVar (DdManager *dd, int i);
DdNode * Cudd_addConst (DdManager *dd, int c);
DdNode * Cudd_addIte (DdManager *dd, DdNode *f, DdNode *g, DdNode *h);
DdNode * Cudd_addPermute (DdManager *dd, DdNode *node, int permut[]);
DdNode * Cudd_Cofactor (DdManager *dd, DdNode *f, DdNode *g);


DdNode * jmvlCudd_Not (mvlDdManager * jmanager, DdNode * f);
DdNode * jmvlCudd_And (mvlDdManager * jmanager, DdNode * f, DdNode * g);
DdNode * jmvlCudd_Or (mvlDdManager * jmanager, DdNode * f, DdNode * g);
DdNode * jmvlCudd_Eq (mvlDdManager * jmanager, DdNode * f, DdNode * g);
DdNode * jmvlCudd_Above (mvlDdManager * jmanager, DdNode * f, DdNode * g);
DdNode * jmvlCudd_Below (mvlDdManager * jmanager, DdNode * f, DdNode * g);

DdNode * jmvlCudd_infoNot (mvlDdManager * jmanager, DdNode * f);
DdNode * jmvlCudd_infoMeet (mvlDdManager * jmanager, DdNode * f, DdNode * g);
DdNode * jmvlCudd_infoJoin (mvlDdManager * jmanager, DdNode * f, DdNode * g);
DdNode * jmvlCudd_infoAbove (mvlDdManager * jmanager, DdNode * f, DdNode * g);
DdNode * jmvlCudd_infoBelow (mvlDdManager * jmanager, DdNode * f, DdNode * g);


DdNode * mvlCudd_ExistAbstract (mvlDdManager * jmanager, DdNode *f, DdNode *cube);
DdNode * mvlCudd_UnivAbstract (mvlDdManager * jmanager, DdNode *f, DdNode *cube);
DdNode * mvlCudd_ExistMeetAbstract (mvlDdManager * jmanager, DdNode *f, DdNode *g, DdNode *cube);
DdNode * mvlCudd_Support (mvlDdManager * jmanager, DdNode *f);
DdNode * mvlCudd_CubeDiff (mvlDdManager * jmanager, DdNode * a, DdNode * b);

DdNode * mvlCudd_NextMinterm (DdGen * gen, int termValue, DdNode * vars, DdNode * top, DdNode * bot);
DdNode * mvlCudd_bddNextMinterm (DdGen * gen, int termValue, DdNode * vars, DdNode * top, DdNode * bot);
%exception;

DdGen * mvlCudd_MintermIterator (mvlDdManager * mvlDd, DdNode * f);
int mvlCudd_HasNextMinterm (DdGen * gen, int termValue);


/*
%typemap(jni) DdNode * "jlong";
%typemap(jtype) DdNode * "long";
%typemap(jstype) DdNode * "long";

%typemap(javaout) DdNode * "return $jnicall;"

%typemap(javain) DdNode * "$javainput"

%typemap(jni) DdManager * jmanager "jlong";
%typemap(jtype) DdManager * jmanager "long";
%typemap(jstype) DdManager * jmanager "long";
%typemap(javain) DdManager * jmanager "$javainput";


%exception JCudd_addNeg 
{
  $action
    if (!result)
      return $null;
  
}


	 
DdNode* JCudd_addNeg (DdManager * jmanager, DdNode * node);

*/
