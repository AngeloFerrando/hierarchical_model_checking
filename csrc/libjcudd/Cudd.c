#include "Cudd.h"
#include <stdio.h>

#include "util.h"
#include "cudd.h"
#include "cuddInt.h"

void setMgrPtr (JNIEnv *env, jobject jCudd, jint value);
DdManager* getMgrPtr (JNIEnv *env, jobject jCudd);
DdNode *getBDD (JNIEnv *env, jobject jCudd);

DdNode* bddConvert (DdManager *, DdManager *, DdNode *, int);


JNIEXPORT void JNICALL 
Java_edu_toronto_cs_cudd_Cudd_init (JNIEnv *env, jobject jCudd, jint initVars)
{
  DdManager *manager;
  
  manager = Cudd_Init ((int)initVars, 0, 
		       CUDD_UNIQUE_SLOTS, CUDD_CACHE_SLOTS, 0);
  
  setMgrPtr (env, jCudd, (jint)manager);
  return;
}


JNIEXPORT jint JNICALL 
Java_edu_toronto_cs_cudd_Cudd_cuddBddOnePtr (JNIEnv *env, jobject jCudd)
{
  DdManager *manager;
  DdNode *ddNode;

  manager = getMgrPtr (env, jCudd);
  ddNode = Cudd_ReadOne (manager);

  if (checkResult (env, manager, ddNode) == 0)
    Cudd_Ref (ddNode);
  
  return (jint)ddNode;
}


JNIEXPORT jint JNICALL 
Java_edu_toronto_cs_cudd_Cudd_cuddBddZeroPtr (JNIEnv *env, jobject jCudd)
{
  DdManager *manager;
  DdNode *ddNode;

  manager = getMgrPtr (env, jCudd);
  ddNode = Cudd_ReadLogicZero (manager);

  if (checkResult (env, manager, ddNode) == 0)
    Cudd_Ref (ddNode);

  return (jint)ddNode;  
}

JNIEXPORT jint JNICALL 
Java_edu_toronto_cs_cudd_Cudd_bddVarPtr
(JNIEnv *env, jobject jCudd, jint index)
{
  DdManager *manager;
  DdNode *node;
  
  manager = getMgrPtr (env, jCudd);
  node = Cudd_bddIthVar (manager, index);

  if (checkResult (env, manager, node) == 0)
    Cudd_Ref (node);
  return (jint)node;
}

JNIEXPORT jint JNICALL Java_edu_toronto_cs_cudd_Cudd_bddAndPtr
(JNIEnv *env, jobject jCudd, jint a, jint b)
{
  DdManager *manager;
  DdNode *node;
  
  manager = getMgrPtr (env, jCudd);
  
  node = Cudd_bddAnd (manager, (DdNode*)a, (DdNode*)b);
  if (checkResult (env, manager, node) == 0)
    Cudd_Ref (node);
  return (jint)node;
}

JNIEXPORT jint JNICALL Java_edu_toronto_cs_cudd_Cudd_bddOrPtr
(JNIEnv *env, jobject jCudd, jint a, jint b)
{
  DdManager *manager;
  DdNode *node;
  
  manager = getMgrPtr (env, jCudd);
  
  node = Cudd_bddOr (manager, (DdNode*)a, (DdNode*)b);

  if (checkResult (env, manager, node) == 0)
    Cudd_Ref (node);
  return (jint)node;
}

JNIEXPORT jint JNICALL Java_edu_toronto_cs_cudd_Cudd_bddNotPtr
(JNIEnv *env, jobject jCudd, jint a)
{
  DdManager *manager;
  DdNode *node;
  
  manager = getMgrPtr (env, jCudd);
  
  node = Cudd_Not ((DdNode*)a);
  
  if (checkResult (env, manager, node) == 0)
    Cudd_Ref (node);
  return (jint)node;
}

JNIEXPORT jint JNICALL Java_edu_toronto_cs_cudd_Cudd_bddConstrainPtr
(JNIEnv *env, jobject jCudd, jint a, jint b)
{
  DdManager *manager;
  DdNode *node;
  
  manager = getMgrPtr (env, jCudd);
  
  node = Cudd_bddConstrain (manager, (DdNode*)a, (DdNode*)b);

  if (checkResult (env, manager, node) == 0)
    Cudd_Ref (node);
  return (jint)node;
}


JNIEXPORT jint JNICALL Java_edu_toronto_cs_cudd_Cudd_bddPermutePtr
(JNIEnv *env, jobject jCudd, jint jNode, jintArray map)
{
  DdManager *manager;
  DdNode *node;
  int *permut;
  
  jsize len;
  jint *body;


  len = (*env)->GetArrayLength (env, map);
  body = (*env)->GetIntArrayElements (env, map, 0);
  permut = (int*)body;

  manager = getMgrPtr (env, jCudd);
  node = (DdNode*)jNode;

  
  node = Cudd_bddPermute (manager, node, permut);
  
  (*env)->ReleaseIntArrayElements (env, map, body, JNI_ABORT);
  if (checkResult (env, manager, node) == 0)
    Cudd_Ref (node);
  return (jint)node;
}


JNIEXPORT jint JNICALL Java_edu_toronto_cs_cudd_Cudd_existAbstract 
(JNIEnv *env, jobject jCudd, jint ptr, jint cubePtr)
{
  DdManager * manager = getMgrPtr (env, jCudd);
  DdNode * node = (DdNode *) ptr;
  DdNode * cube = (DdNode *) cubePtr;
  
  node = Cudd_bddExistAbstract (manager, node, cube);
  if (checkResult (env, manager, node) == 0)
    Cudd_Ref (node);
  
  return (jint)node;
}

JNIEXPORT jint JNICALL Java_edu_toronto_cs_cudd_Cudd_forallAbstract 
(JNIEnv *env, jobject jCudd, jint ptr, jint cubePtr)
{
  DdManager * manager = getMgrPtr (env, jCudd);
  DdNode * node = (DdNode *) ptr;
  DdNode * cube = (DdNode *) cubePtr;
  
  node = Cudd_bddUnivAbstract (manager, node, cube);
  if (checkResult (env, manager, node) == 0)
    Cudd_Ref (node);
  
  return (jint)node;
}


JNIEXPORT void JNICALL 
  Java_edu_toronto_cs_cudd_Cudd_ref (JNIEnv *env, jobject jCudd, jint ptr)
{
  DdNode *node;
  
  node = (DdNode*)ptr;
  Cudd_Ref (node);
  return ;
}



JNIEXPORT void JNICALL 
Java_edu_toronto_cs_cudd_Cudd_deref (JNIEnv *env, jobject jCudd, jint ptr)
{
  DdNode *node;

  node = (DdNode*)ptr;  
  Cudd_Deref (node);
  return;
}

JNIEXPORT void JNICALL 
Java_edu_toronto_cs_cudd_Cudd_recursiveDeref 
(JNIEnv *env, jobject jCudd, jint ptr)
{
  DdNode *node;
  DdManager *manager;

  /** XXX See comment in CuddLatAdd */
  if (manager == NULL) return;

  manager = getMgrPtr (env, jCudd);
  node = (DdNode*)ptr;

  Cudd_RecursiveDeref (manager, node);
  return;

}

JNIEXPORT jint JNICALL Java_edu_toronto_cs_cudd_Cudd_refCount
(JNIEnv *env, jobject jCudd, jint ptr)
{
  DdNode * node = (DdNode *)ptr;
  
  DdNode * N = Cudd_Regular (node);
  
  return (jint)N->ref;
}



JNIEXPORT jint JNICALL Java_edu_toronto_cs_cudd_Cudd_checkZeroRef
  (JNIEnv *env, jobject jCudd)
{
  int retval;
  DdManager *manager = getMgrPtr (env, jCudd);
  
  retval = Cudd_CheckZeroRef (manager);
  return (jint)retval;
}

JNIEXPORT void JNICALL Java_edu_toronto_cs_cudd_Cudd_quit
  (JNIEnv *env, jobject jCudd)
{
  DdManager *manager = getMgrPtr (env, jCudd);
  printf ("CUDD: Quiting\n");
  printf ("Quiting manager %i\n", manager);
  Cudd_Quit (manager);
  printf ("Done quiting\n");
}



JNIEXPORT void JNICALL Java_edu_toronto_cs_cudd_Cudd_info 
  (JNIEnv *env, jobject jCudd)
{
  DdManager *manager = getMgrPtr (env, jCudd);
  Cudd_PrintInfo (manager, stdout);
}


JNIEXPORT void JNICALL Java_edu_toronto_cs_cudd_Cudd_reorder
  (JNIEnv *env, jobject jCudd, jint minsize)
{
  DdManager *manager = getMgrPtr (env, jCudd);
  Cudd_ReduceHeap (manager, CUDD_REORDER_SIFT, minsize);
}


JNIEXPORT jint JNICALL Java_edu_toronto_cs_cudd_Cudd_dagSize
  (JNIEnv *env, jobject jCudd, jint ptr)
{
  return (jint)Cudd_DagSize ((DdNode*)ptr);
}


JNIEXPORT jint JNICALL Java_edu_toronto_cs_cudd_Cudd_sharingSize
  (JNIEnv *env, jobject jCudd, jintArray jPtrs)
{
  return (jint)countSharingSize (env, jPtrs);
}


JNIEXPORT jint JNICALL Java_edu_toronto_cs_cudd_Cudd_transferPtr
  (JNIEnv *env, jobject jCudd, jobject dstJCudd, jint ptr, jint factor)
{
  DdManager * srcManager;
  DdManager * dstManager;
  DdNode * node;
  
  srcManager = getMgrPtr (env, jCudd);
  dstManager = getMgrPtr (env, dstJCudd);
  
  node = bddConvert (srcManager, dstManager, (DdNode*)ptr, (int)factor);
  
  if (checkResult (env, dstManager, node) == 0)
    Cudd_Ref (node);
  return (jint)node;
}




void setMgrPtr (JNIEnv *env, jobject jCudd, jint value)
{
  jclass cls;
  jfieldID fieldID;
  
  cls = (*env)->GetObjectClass (env, jCudd);
  fieldID = (*env)->GetFieldID (env, cls, "mgrPtr", "I");
  
  (*env)->SetIntField (env, jCudd, fieldID, value);
  return;
}

DdManager *getMgrPtr (JNIEnv *env, jobject jCudd)
{
  jclass cls;
  jfieldID fieldID;
  
  cls = (*env)->GetObjectClass (env, jCudd);
  fieldID = (*env)->GetFieldID (env, cls, "mgrPtr", "I");
  
  return (DdManager*)(*env)->GetIntField (env, jCudd, fieldID);
}

DdNode *getBDD (JNIEnv *env, jobject jCudd)
{
  jclass cls;
  jfieldID fieldID;
  
  cls = (*env)->GetObjectClass (env, jCudd);
  fieldID = (*env)->GetFieldID (env, cls, "ddNodePtr", "I");
  
  return (DdNode*)(*env)->GetIntField (env, jCudd, fieldID);
}

/***
 *** Check if an error occured and throw an exception 
 *** returns error code on error, 0 otherwise
 ***/
int checkResult (JNIEnv *env, DdManager *manager, DdNode *node)
{
  jthrowable exc;
  jclass cls;

  if (node == NULL)
    {
      
      if (Cudd_ReadErrorCode (manager) == CUDD_MEMORY_OUT)
	{
	  cls = (*env)->FindClass (env, "java/lang/OutOfMemoryError");
	  (*env)->ThrowNew (env, cls, "Out of Memeory (CUDD).");
	}
      else
	{
	  cls = (*env)->FindClass (env, "java/lang/InternalError");
	  (*env)->ThrowNew (env, cls, "Internal Error (CUDD).");
	}
      return 
	Cudd_ReadErrorCode (manager) == 0 ? -1 : Cudd_ReadErrorCode (manager);
    }

  return 0;
}


int countSharingSize (JNIEnv *env, jintArray jPtrs)
{
  int len = (*env)->GetArrayLength (env, jPtrs);
  int *body = (int*)(*env)->GetIntArrayElements (env, jPtrs, 0);

  int sharingSize = Cudd_SharingSize ((DdNode**)body, len);
  (*env)->ReleaseIntArrayElements (env, jPtrs, (jint*)body, JNI_ABORT);
  return sharingSize;
}



/****
 **** Given a DdNode* belonging to a srcManager, converts it into a DdNode
 **** in the dest manager, shifting variables in the process by factor.
 **** thus a variable numbered 'n' in the srcNode will become 'n*factor'
 **** in the dest node
 ****/
DdNode* bddConvert (DdManager *srcManager, DdManager *dstManager, 
		    DdNode *srcNode, int factor)
{
  DdNode * N;
  DdNode * dstN;
  DdNode * dstT;
  DdNode * dstE;
  
  

  N = Cudd_Regular (srcNode);

  /*** Handle the constants */
  if (DD_ONE (srcManager) == N)
    {
      if (Cudd_IsComplement (srcNode))
	return Cudd_Not (DD_ONE (dstManager));
      else
	return DD_ONE (dstManager);
    }
  
    
  dstN = Cudd_bddIthVar (dstManager, N->index * factor);

  dstT = bddConvert (srcManager, dstManager, cuddT (N), factor);
  Cudd_Ref (dstT);

  dstE = bddConvert (srcManager, dstManager, cuddE (N), factor);
  Cudd_Ref (dstE);

  dstN = Cudd_bddIte (dstManager, dstN, dstT, dstE);
  if (Cudd_IsComplement (srcNode))
    dstN = Cudd_Not (dstN);

  Cudd_IterDerefBdd (dstManager, dstT);
  Cudd_IterDerefBdd (dstManager, dstE);

  return dstN;
}

int pickRandomCube (DdManager * manager, DdNode *node, DdNode ** result)
{

  int value;
  /* direction we will follow */
  char dir;
  DdNode * N;
  DdNode * C;

  DdNode * var;

  DdNode * temp;

  DdNode * one;
  DdNode * bzero;
  DdNode * R;

  one = DD_ONE (manager);
  bzero = Cudd_Not (one);

  /** take care of a contant node */
  if (node = one) return 1;
  else if (node == bzero) return 0;

  R = *result;
  

  /** get the node */
  N = Cudd_Regular (node);

  /** pick the direction */
  dir = (char) ((Cudd_Random() & 0x2000) >> 13);

  /** pick a child based on direction */
  C = dir ? cuddT (N) : cuddE (N);

  if (Cudd_IsComplement (node))
    C = Cudd_Not (C);

  value = pickRandomCube (manager, C, result);
  
  /** 
   ** we now have to create a BDD corresponding to this variable 
   ** and add it to the result 
   **/
  
  var = Cudd_bddIthVar (manager, N->index);
  var = dir ? var : Cudd_Not (var);
  
  temp = Cudd_bddAnd (manager, R, var);
  Cudd_Ref (temp);
  Cudd_RecursiveDeref (manager, R);
  R = temp;
  return value;
}
