#include "CuddAdd.h"
#include <stdio.h>

#include "util.h"
#include "cudd.h"
#include "cuddInt.h"

typedef struct algebra algebra;

#define ALGEBRA(M) ((algebra*)(M)->data) 
#define DD_BOT(M)  ALGEBRA(M)->bot
#define DD_TOP(M) ALGEBRA(M)->top

#define SET_ENV(M,E) ALGEBRA((M))->env=(E)

struct algebra
{
  DdNode* bot;
  DdNode* top;


  /*** functions to perform meet, join, neg, and other operations */
  DdNode * (*meetfn)(DdManager *, DdNode **, DdNode **);
  DdNode * (*joinfn)(DdManager *, DdNode **, DdNode **);
  DdNode * (*negfn)(DdManager *, DdNode *);
  DdNode * (*eqfn)(DdManager *, DdNode **, DdNode **);
  DdNode * (*leqfn)(DdManager *, DdNode **, DdNode **);
  DdNode * (*geqfn)(DdManager *, DdNode **, DdNode **);
  DdNode * (*implfn)(DdManager *, DdNode **, DdNode **);
  DdNode * (*plusfn)(DdManager *, DdNode **, DdNode **);


  /** calbacks **/

  // -- JNI environment for call backs
  JNIEnv *env;
  // -- Java based algebra implementation
  jobject jalgebra;

  // -- ID's of all of the functions we need
  jmethodID meetID;
  jmethodID joinID;
  jmethodID negID;
  jmethodID eqID;
  jmethodID leqID;
  jmethodID geqID;
  jmethodID implID;
  
};

extern void setMgrPtr (JNIEnv *env, jobject jCudd, jint value);
extern DdManager* getMgrPtr (JNIEnv *env, jobject jCudd);
extern int checkResult (JNIEnv *env, DdManager *manager, DdNode *node);
extern int countSharingSize (JNIEnv *env, jintArray ptrs);

DdNode *Lat_addNegCallback (DdManager * dd, DdNode * f);
DdNode *Lat_addMeetCallback (DdManager * dd, DdNode ** f, DdNode ** g);
DdNode *Lat_addJoinCallback (DdManager * dd, DdNode ** f, DdNode ** g);
DdNode *Lat_addAboveCallback (DdManager * dd, DdNode ** f, DdNode ** g);
DdNode *Lat_addBelowCallback (DdManager * dd, DdNode ** f, DdNode ** g);
DdNode *Lat_addImplCallback (DdManager * dd, DdNode ** f, DdNode ** g);
DdNode *Lat_addEqual(DdManager * dd, DdNode ** f, DdNode ** g);



/*** abstraction */

static int addCheckPositiveCube ARGS((DdManager *manager, DdNode *cube));
DdNode * cuddLatAddUnivAbstractRecur (DdManager * manager, DdNode * f,
				      DdNode * cube);
DdNode * cuddLatAddExistAbstractRecur (DdManager * manager, DdNode * f,
				       DdNode * cube);
DdNode * CuddLat_addUnivAbstract (DdManager * manager, 
				  DdNode * f, DdNode * cube);
DdNode * CuddLat_addExistAbstract (DdManager * manager, 
				   DdNode * f, DdNode * cube);


/*** end abstraction */
JNIEXPORT void JNICALL 
Java_edu_toronto_cs_cudd_CuddAdd_init (JNIEnv *env, jobject jCudd, 
				       jobject jAlgebra, 
				       jint initVars, jint top, jint bot)
{
  DdManager *manager;
  algebra *algebra;
  jclass clazz;
  
  manager = Cudd_Init ((int)initVars, 0, 
		       CUDD_UNIQUE_SLOTS, CUDD_CACHE_SLOTS, 0);
  
  algebra = (struct algebra *) malloc (sizeof (struct algebra));
  
  algebra->top = Cudd_addConst (manager, (CUDD_VALUE_TYPE)top);
  Cudd_Ref (algebra->top);
  algebra->bot = Cudd_addConst (manager, (CUDD_VALUE_TYPE)bot);
  Cudd_Ref (algebra->bot);


  /*** create a global reference since we are going to keep this 
   *** object during our lifetime
   ***/
  algebra->jalgebra = (*env)->NewGlobalRef (env, jAlgebra);
  clazz = (*env)->GetObjectClass (env, algebra->jalgebra);
  
  /*** get method ids **/
  algebra->meetID = (*env)->GetMethodID (env, clazz, "meet", "(II)I");
  algebra->joinID = (*env)->GetMethodID (env, clazz, "join", "(II)I");
  algebra->negID = (*env)->GetMethodID (env, clazz, "neg", "(I)I");
  algebra->eqID = (*env)->GetMethodID (env, clazz, "eq", "(II)I");
  algebra->leqID = (*env)->GetMethodID (env, clazz, "leq", "(II)I");
  algebra->geqID = (*env)->GetMethodID (env, clazz, "geq", "(II)I");
  algebra->implID = (*env)->GetMethodID (env, clazz, "impl", "(II)I");
  
  /*** set the function pointers ***/
  algebra->meetfn = Lat_addMeetCallback;
  algebra->joinfn = Lat_addJoinCallback;
  algebra->negfn = Lat_addNegCallback;
  algebra->eqfn = Lat_addEqual;
  algebra->leqfn = Lat_addBelowCallback;
  algebra->geqfn = Lat_addAboveCallback;
  algebra->implfn = Lat_addImplCallback;
  algebra->plusfn = Cudd_addPlus;

  /* store pointer to the algebra object in DdManager */
  manager->data = algebra;
  
  /* we have a new idea of true, or 1 in CUDD speak */
  Cudd_RecursiveDeref (manager, manager->one);
  manager->one = algebra->top;
  /* we have a new idea of false, or 0 in CUDD speak */
  Cudd_RecursiveDeref (manager, manager->zero);
  manager->zero = algebra->bot;
  /* we have to set our own background since we changed the notion
     of true and false */
  Cudd_SetBackground (manager, algebra->bot);

  /* finally set the pointer to the new manager in the Java land */
  setMgrPtr (env, jCudd, (jint)manager);
}




JNIEXPORT jint JNICALL 
Java_edu_toronto_cs_cudd_CuddAdd_topPtr (JNIEnv *env, jobject jCudd)
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
Java_edu_toronto_cs_cudd_CuddAdd_botPtr (JNIEnv *env, 
						    jobject jCudd)
{
  DdManager *manager;
  DdNode *ddNode;

  manager = getMgrPtr (env, jCudd);
  ddNode = Cudd_ReadZero (manager);

  if (checkResult (env, manager, ddNode) == 0)
    Cudd_Ref (ddNode);

  return (jint)ddNode;  
}

JNIEXPORT jint JNICALL 
Java_edu_toronto_cs_cudd_CuddAdd_addVarPtr (JNIEnv *env, jobject jCudd, 
					       jint index)
{
  DdManager *manager;
  DdNode *node;
  
  manager = getMgrPtr (env, jCudd);
  node = Cudd_addIthVar (manager, index);

  if (checkResult (env, manager, node) == 0)
    Cudd_Ref (node);
  
  return (jint)node;
}

JNIEXPORT jint JNICALL 
Java_edu_toronto_cs_cudd_CuddAdd_addConstantPtr (JNIEnv *env, jobject jCudd, 
						 jint value)
{
  DdManager *manager;
  DdNode *node;
  
  manager = getMgrPtr (env, jCudd);
  node = Cudd_addConst (manager, (CUDD_VALUE_TYPE)value);
  if (checkResult (env, manager, node) == 0)
    Cudd_Ref (node);
  return (jint)node;
}



JNIEXPORT jint JNICALL Java_edu_toronto_cs_cudd_CuddAdd_addAndPtr
(JNIEnv *env, jobject jCudd, jint a, jint b)
{
  DdManager *manager;
  DdNode *node;
  
  manager = getMgrPtr (env, jCudd);
  SET_ENV(manager,env);
  
  node = Cudd_addApply (manager, ALGEBRA(manager)->meetfn, 
			(DdNode*)a, (DdNode*)b);
  if (checkResult (env, manager, node) == 0)
    Cudd_Ref (node);
  return (jint)node;
}

JNIEXPORT jint JNICALL Java_edu_toronto_cs_cudd_CuddAdd_addOrPtr
(JNIEnv *env, jobject jCudd, jint a, jint b)
{
  DdManager *manager;
  DdNode *node;
  
  manager = getMgrPtr (env, jCudd);
  SET_ENV(manager,env);
  node = Cudd_addApply (manager, ALGEBRA(manager)->joinfn, 
			(DdNode*)a, (DdNode*)b);

  if (checkResult (env, manager, node) == 0)
    Cudd_Ref (node);
  return (jint)node;
}

JNIEXPORT jint JNICALL Java_edu_toronto_cs_cudd_CuddAdd_addNotPtr
(JNIEnv *env, jobject jCudd, jint a)
{
  DdManager *manager;
  DdNode *node;
  
  manager = getMgrPtr (env, jCudd);
  SET_ENV(manager,env);
  node = Cudd_addMonadicApply (manager, ALGEBRA(manager)->negfn, (DdNode*)a);
  
  
  if (checkResult (env, manager, node) == 0)
    Cudd_Ref (node);
  return (jint)node;
}

JNIEXPORT jint JNICALL Java_edu_toronto_cs_cudd_CuddAdd_addGeqPtr
  (JNIEnv *env, jobject jCudd, jint a, jint b)
{
  DdManager *manager;
  DdNode *node;
  
  manager = getMgrPtr (env, jCudd);
  SET_ENV(manager,env);
  
  node = Cudd_addApply (manager, ALGEBRA(manager)->geqfn, 
			(DdNode*)a, (DdNode*)b);

  if (checkResult (env, manager, node) == 0)
    Cudd_Ref (node);
  return (jint)node;
}

JNIEXPORT jint JNICALL Java_edu_toronto_cs_cudd_CuddAdd_addLeqPtr
  (JNIEnv *env, jobject jCudd, jint a, jint b)
{
  DdManager *manager;
  DdNode *node;
  
  manager = getMgrPtr (env, jCudd);
  SET_ENV(manager,env);
  
  node = Cudd_addApply (manager, ALGEBRA(manager)->leqfn,
			(DdNode*)a, (DdNode*)b);

  if (checkResult (env, manager, node) == 0)
    Cudd_Ref (node);
  return (jint)node;
}

JNIEXPORT jint JNICALL Java_edu_toronto_cs_cudd_CuddAdd_addEqPtr
  (JNIEnv *env, jobject jCudd, jint a, jint b)
{
  DdManager *manager;
  DdNode *node;
  
  manager = getMgrPtr (env, jCudd);
  SET_ENV(manager,env);
  
  node = Cudd_addApply (manager, ALGEBRA(manager)->eqfn, 
			(DdNode*)a, (DdNode*)b);

  if (checkResult (env, manager, node) == 0)
    Cudd_Ref (node);
  return (jint)node;
}

JNIEXPORT jint JNICALL Java_edu_toronto_cs_cudd_CuddAdd_addImplPtr
  (JNIEnv *env, jobject jCudd, jint a, jint b)
{
  DdManager *manager;
  DdNode *node;
  
  manager = getMgrPtr (env, jCudd);
  SET_ENV(manager,env);
  
  node = Cudd_addApply (manager, ALGEBRA(manager)->implfn, 
			(DdNode*)a, (DdNode*)b);

  if (checkResult (env, manager, node) == 0)
    Cudd_Ref (node);
  return (jint)node;
}

JNIEXPORT jint JNICALL Java_edu_toronto_cs_cudd_CuddAdd_addPlusPtr
  (JNIEnv *env, jobject jCudd, jint a, jint b)
{
  DdManager *manager;
  DdNode *node;
  
  manager = getMgrPtr (env, jCudd);
  SET_ENV(manager,env);
  
  node = Cudd_addApply (manager, ALGEBRA(manager)->plusfn, 
			(DdNode*)a, (DdNode*)b);

  if (checkResult (env, manager, node) == 0)
    Cudd_Ref (node);
  return (jint)node;
}


JNIEXPORT jint JNICALL Java_edu_toronto_cs_cudd_CuddAdd_addCofactorPtr
(JNIEnv *env, jobject jCudd, jint a, jint b)
{
  DdManager *manager;
  DdNode *node;
  
  manager = getMgrPtr (env, jCudd);
    
  node = Cudd_Cofactor (manager, (DdNode*)a, (DdNode*)b);

  if (checkResult (env, manager, node) == 0)
    Cudd_Ref (node);
  return (jint)node;
}

JNIEXPORT jint JNICALL Java_edu_toronto_cs_cudd_CuddAdd_addItePtr
(JNIEnv *env, jobject jCudd, jint var, jint jLeftNode, jint jRightNode)
{
  DdManager *manager;
  DdNode *node;
  DdNode *varNode;
  
  manager = getMgrPtr (env, jCudd);
    
  varNode = Cudd_addIthVar (manager, (int)var);
  Cudd_Ref (varNode);

  node = Cudd_addIte (manager, 
		      varNode, (DdNode*)jLeftNode, (DdNode*)jRightNode);
  if (checkResult (env, manager, node) == 0)
    Cudd_Ref (node);

  Cudd_RecursiveDeref (manager, varNode);

  return (jint)node;
}


JNIEXPORT jboolean JNICALL Java_edu_toronto_cs_cudd_CuddAdd_isConstantPtr
(JNIEnv *env, jobject jCudd, jint jNode)
{
  DdManager *manger;

  manger = getMgrPtr (env, jCudd);
  return (jboolean)cuddIsConstant ((DdNode*)jNode);
}



JNIEXPORT jint JNICALL 
Java_edu_toronto_cs_cudd_CuddAdd_addPermutePtr (JNIEnv *env, jobject jCudd,
						   jint jNode, jintArray map)
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
  
  node = Cudd_addPermute (manager, node, permut);

  (*env)->ReleaseIntArrayElements (env, map, body, 0);
  if (checkResult (env, manager, node) == 0)
    Cudd_Ref (node);
  return (jint)node;
}

JNIEXPORT jint JNICALL 
Java_edu_toronto_cs_cudd_CuddAdd_addGetValue (JNIEnv *env, jobject jCudd, 
						 jint ptr)
{
  DdNode *node;
  DdManager *manager;
  
  manager = getMgrPtr (env, jCudd);
  node = (DdNode*)ptr;
  
  return cuddV (node);
}


JNIEXPORT jint JNICALL 
Java_edu_toronto_cs_cudd_CuddAdd_existAbstract (JNIEnv *env, jobject jCudd,
						   jint ptr, jint cubePtr)
{
  DdManager * manager = getMgrPtr (env, jCudd);
  DdNode * node = (DdNode *) ptr;
  DdNode * cube = (DdNode *) cubePtr;
  
  SET_ENV(manager,env);
  node = CuddLat_addExistAbstract (manager, node, cube);
  if (checkResult (env, manager, node) == 0)
    Cudd_Ref (node);
  
  return (jint)node;
}

JNIEXPORT jint JNICALL 
Java_edu_toronto_cs_cudd_CuddAdd_forallAbstract (JNIEnv *env, 
						    jobject jCudd, jint ptr, 
						    jint cubePtr)
{
  DdManager * manager = getMgrPtr (env, jCudd);
  DdNode * node = (DdNode *) ptr;
  DdNode * cube = (DdNode *) cubePtr;

  SET_ENV(manager,env);
  node = CuddLat_addUnivAbstract (manager, node, cube);
  if (checkResult (env, manager, node) == 0)
    Cudd_Ref (node);
  
  return (jint)node;
}



JNIEXPORT void JNICALL 
Java_edu_toronto_cs_cudd_CuddAdd_ref (JNIEnv *env, jobject jCudd, 
					 jint ptr)
{
  DdNode *node;
  
  node = (DdNode*)ptr;
  Cudd_Ref (node);
  return ;
}



JNIEXPORT void JNICALL 
Java_edu_toronto_cs_cudd_CuddAdd_deref (JNIEnv *env, jobject jCudd, 
					   jint ptr)
{
  DdNode *node;

  node = (DdNode*)ptr;  
  Cudd_Deref (node);
  return;
}

JNIEXPORT void JNICALL 
Java_edu_toronto_cs_cudd_CuddAdd_recursiveDeref (JNIEnv *env, 
						    jobject jCudd, jint ptr)
{
  DdNode *node;
  DdManager *manager;
  
  manager = getMgrPtr (env, jCudd);

  /*** If java gc decides to kill the manager before killing all of the 
   *** decision diagrams, we may get into this type of a situation, 
   *** in which case recursiveDeref is not possible ***/
  /*** XXX This needs some more thought **/
  if (manager == NULL) return;

  node = (DdNode*)ptr;

  Cudd_RecursiveDeref (manager, node);
  return;

}

JNIEXPORT jint JNICALL 
Java_edu_toronto_cs_cudd_CuddAdd_checkZeroRef (JNIEnv *env, jobject jCudd)
{
  int retval;
  DdManager *manager = getMgrPtr (env, jCudd);
  
  retval = Cudd_CheckZeroRef (manager);
  return (jint)retval;
}

JNIEXPORT void JNICALL 
Java_edu_toronto_cs_cudd_CuddAdd_quit (JNIEnv *env, jobject jCudd)
{
  DdManager *manager = getMgrPtr (env, jCudd);
  
  if (manager->data != NULL)
    {
      /** destroy the lattice struct based on the mode of operation **/
      (*env)->DeleteGlobalRef (env, ALGEBRA(manager)->jalgebra);

      free (manager->data);
      manager->data = NULL;
    }
  
  
  Cudd_Quit (manager);
}


JNIEXPORT jint JNICALL Java_edu_toronto_cs_cudd_CuddAdd_gc
(JNIEnv *env, jobject jCudd, jboolean jClearCache)
{
  DdManager *manager = getMgrPtr (env, jCudd);
  
  return (jint)cuddGarbageCollect (manager, (int)jClearCache);
}




JNIEXPORT void JNICALL Java_edu_toronto_cs_cudd_CuddAdd_info 
  (JNIEnv *env, jobject jCudd)
{
  DdManager *manager = getMgrPtr (env, jCudd);
  Cudd_PrintInfo (manager, stdout);
}

JNIEXPORT void JNICALL Java_edu_toronto_cs_cudd_CuddAdd_printMintermPtr
  (JNIEnv *env, jobject jCudd, jint ptr)
{
  DdManager *manager = getMgrPtr (env, jCudd);
  Cudd_PrintMinterm (manager, (DdNode*)ptr);
}

JNIEXPORT void JNICALL Java_edu_toronto_cs_cudd_CuddAdd_reorder
  (JNIEnv *env, jobject jCudd, jint minsize)
{
  DdManager *manager = getMgrPtr (env, jCudd);
  Cudd_ReduceHeap (manager, CUDD_REORDER_SIFT, minsize);
}


JNIEXPORT jint JNICALL Java_edu_toronto_cs_cudd_CuddAdd_dagSize
  (JNIEnv *env, jobject jCudd, jint ptr)
{
  return (jint)Cudd_DagSize ((DdNode*)ptr);
}


JNIEXPORT jint JNICALL Java_edu_toronto_cs_cudd_CuddAdd_sharingSize
  (JNIEnv *env, jobject jCudd, jintArray jPtrs)
{
  return (jint)countSharingSize (env, jPtrs);
}





DdNode *Lat_addJoinCallback(DdManager * dd, DdNode ** f, DdNode ** g)
{
  DdNode *res;
  DdNode *F, *G;
  CUDD_VALUE_TYPE value;
  JNIEnv *env;
  
  /* get JNI environment so that we can call Java methods */
  env = ALGEBRA(dd)->env;

  F = *f; G = *g;
  if (F == DD_TOP (dd) || G == DD_TOP (dd)) return (DD_TOP(dd));
  if (F == DD_BOT(dd)) return(G);
  if (G == DD_BOT(dd)) return(F);
  if (cuddIsConstant(F) && cuddIsConstant(G)) {
    value = 
      (int)(*env)->CallIntMethod (env, ALGEBRA(dd)->jalgebra, 
				  ALGEBRA(dd)->joinID,
				  (jint)cuddV(F), (jint)cuddV(G));
    res = cuddUniqueConst(dd,value);
    return(res);
  }
  if (F > G) { /* swap f and g */
    *f = G;
    *g = F;
  }
  return(NULL);
}




DdNode *Lat_addMeetCallback(DdManager * dd, DdNode ** f, DdNode ** g)
{
  DdNode *res;
  DdNode *F, *G;
  CUDD_VALUE_TYPE value;
  JNIEnv *env = ALGEBRA(dd)->env;
  
  F = *f; G = *g;
  if (F == DD_BOT(dd) || G == DD_BOT(dd)) return(DD_BOT(dd));
  if (F == DD_TOP(dd)) return(G);
  if (G == DD_TOP(dd)) return(F);
  if (cuddIsConstant(F) && cuddIsConstant(G)) {
    value = (int)(*env)->CallIntMethod (env, ALGEBRA(dd)->jalgebra, 
					ALGEBRA(dd)->meetID, 
					(jint)cuddV(F), (jint)cuddV(G));
    res = cuddUniqueConst(dd,value);
    return(res);
  }
  if (F > G) { /* swap f and g */
    *f = G;
    *g = F;
  }
  return(NULL);

} 

DdNode *Lat_addImplCallback(DdManager * dd, DdNode ** f, DdNode ** g)
{
  DdNode *res;
  DdNode *F, *G;
  CUDD_VALUE_TYPE value;
  JNIEnv *env = ALGEBRA(dd)->env;
  
  F = *f; G = *g;
  if (cuddIsConstant(F) && cuddIsConstant(G)) {
    value = (int)(*env)->CallIntMethod (env, ALGEBRA(dd)->jalgebra, 
					ALGEBRA(dd)->implID, 
					(jint)cuddV(F), (jint)cuddV(G));
    res = cuddUniqueConst(dd,value);
    return(res);
  }
  if (F > G) { /* swap f and g */
    *f = G;
    *g = F;
  }
  return(NULL);

} 



DdNode *Lat_addNegCallback (DdManager * dd, DdNode * f)
{
  JNIEnv *env = ALGEBRA(dd)->env;
  if (cuddIsConstant(f)) {
    DdNode * res;
    CUDD_VALUE_TYPE value;
    
    value = (CUDD_VALUE_TYPE)(*env)->CallIntMethod (env, 
						    ALGEBRA(dd)->jalgebra, 
						    ALGEBRA(dd)->negID,
						    (jint)cuddV(f));    
    res = cuddUniqueConst(dd,value);
    return(res);
  }
  return(NULL);

}

DdNode *Lat_addAboveCallback(DdManager * dd, DdNode ** f, DdNode ** g)
{
  DdNode *res;
  DdNode *F, *G;
  CUDD_VALUE_TYPE value;
  JNIEnv *env = ALGEBRA(dd)->env;


  F = *f; G = *g;
  if (cuddIsConstant(F) && cuddIsConstant(G)) 
    {
      value = (CUDD_VALUE_TYPE)(*env)->CallIntMethod (env, 
						      ALGEBRA(dd)->jalgebra, 
						      ALGEBRA(dd)->geqID,
						      (jint)cuddV(F), 
						      (jint)cuddV(G));
      res = cuddUniqueConst(dd, value);
      return res;
    }
  return(NULL);
}

DdNode *Lat_addBelowCallback(DdManager * dd, DdNode ** f, DdNode ** g)
{
  DdNode *res;
  DdNode *F, *G;
  CUDD_VALUE_TYPE value;
  JNIEnv *env = ALGEBRA(dd)->env;

  F = *f; G = *g;
  if (cuddIsConstant(F) && cuddIsConstant(G)) 
    {
      value = 
	(CUDD_VALUE_TYPE)(*env)->CallBooleanMethod (env, 
						    ALGEBRA(dd)->jalgebra, 
						    ALGEBRA(dd)->leqID,
						    (jint)cuddV(F), 
						    (jint)cuddV(G));
      res = cuddUniqueConst(dd, value);
      return res;
    }
  return(NULL);
}

DdNode *Lat_addEqual(DdManager * dd, DdNode ** f, DdNode ** g)
{
  DdNode *res;
  DdNode *F, *G;
  CUDD_VALUE_TYPE value;

  F = *f; G = *g;
  if (F == G) return DD_TOP(dd);
  
  if (cuddIsConstant(F) && cuddIsConstant(G)) 
    return cuddV(F) == cuddV(G) ? DD_TOP(dd) : DD_BOT(dd);

  return(NULL);
}




/*******************************************************************/
/******** univ and exist abstract for lattice based adds **********/
DdNode * CuddLat_addExistAbstract (DdManager * manager, 
				   DdNode * f, DdNode * cube)
{
  DdNode *res;

  if (addCheckPositiveCube(manager, cube) == 0) 
    {
      (void) fprintf(manager->err,"Error: Can only abstract cubes");
      return(NULL);
    }

  do 
    {
      manager->reordered = 0;
      res = cuddLatAddExistAbstractRecur(manager, f, cube);
    } 
  while (manager->reordered == 1);

  if (res == NULL) return(NULL);
  
  cuddRef(res);
  cuddDeref(res);
  
  return(res);
} 

DdNode * CuddLat_addUnivAbstract (DdManager * manager, 
				  DdNode * f, DdNode * cube)
{
  DdNode *res;

  if (addCheckPositiveCube(manager, cube) == 0) 
    {
      (void) fprintf(manager->err,"Error: Can only abstract cubes");
      return(NULL);
    }

  do 
    {
      manager->reordered = 0;
      res = cuddLatAddUnivAbstractRecur(manager, f, cube);
    } 
  while (manager->reordered == 1);

  if (res == NULL) return(NULL);
  
  cuddRef(res);
  cuddDeref(res);
  
  return(res);
} 


DdNode * cuddLatAddExistAbstractRecur (DdManager * manager, DdNode * f,
				       DdNode * cube)
{
  DdNode	*T, *E, *res, *res1, *res2, *one;

  statLine(manager);
  one = DD_ONE(manager);

  /* Cube is guaranteed to be a cube at this point. */
  if (cuddIsConstant(f) || cube == one) {  
    return(f);
  }

  
  /* Abstract a variable that does not appear in f. */
  if (cuddI(manager,f->index) > cuddI(manager,cube->index)) {
    res1 = cuddLatAddExistAbstractRecur(manager, f, cuddT(cube));
    if (res1 == NULL) return(NULL);
    return res1;
  }
  

  if ((res = cuddCacheLookup2(manager, CuddLat_addExistAbstract, f, cube)) 
      != NULL) 
    {
      return(res);
    }

  T = cuddT(f);
  E = cuddE(f);



  /* If the two indices are the same, so are their levels. */
  if (f->index == cube->index) {
    res1 = cuddLatAddExistAbstractRecur(manager, T, cuddT(cube));
    if (res1 == NULL) return(NULL);
    cuddRef(res1);
    if (res1 != one) {
      res2 = cuddLatAddExistAbstractRecur(manager, E, cuddT(cube));
      if (res2 == NULL) {
	Cudd_RecursiveDeref(manager,res1);
	return(NULL);
      }
      cuddRef(res2);
      res = cuddAddApplyRecur(manager, ALGEBRA(manager)->joinfn, res1, res2);
      if (res == NULL) {
	Cudd_RecursiveDeref(manager,res1);
	Cudd_RecursiveDeref(manager,res2);
	return(NULL);
      }
      cuddRef(res);
      Cudd_RecursiveDeref(manager,res1);
      Cudd_RecursiveDeref(manager,res2);
    } else {
      res = res1;
    }
    cuddCacheInsert2(manager, CuddLat_addExistAbstract, f, cube, res);
    cuddDeref(res);
    return(res);
  } else { /* if (cuddI(manager,f->index) < cuddI(manager,cube->index)) */
    res1 = cuddLatAddExistAbstractRecur(manager, T, cube);
    if (res1 == NULL) return(NULL);
    cuddRef(res1);
    res2 = cuddLatAddExistAbstractRecur(manager, E, cube);
    if (res2 == NULL) {
      Cudd_RecursiveDeref(manager,res1);
      return(NULL);
    }
    cuddRef(res2);
    res = (res1 == res2) ? res1 :
      cuddUniqueInter(manager, (int) f->index, res1, res2);
    if (res == NULL) {
      Cudd_RecursiveDeref(manager,res1);
      Cudd_RecursiveDeref(manager,res2);
      return(NULL);
    }
    cuddDeref(res1);
    cuddDeref(res2);
    cuddCacheInsert2(manager, CuddLat_addExistAbstract, f, cube, res);
    return(res);
  }

} 

DdNode * cuddLatAddUnivAbstractRecur (DdManager * manager, DdNode * f,
				       DdNode * cube)
{
  DdNode	*T, *E, *res, *res1, *res2, *one;
  DdNode *zero;

  statLine(manager);
  one = DD_ONE(manager);
  zero = DD_ZERO(manager);
  
  /* Cube is guaranteed to be a cube at this point. */
  if (cuddIsConstant(f) || cube == one) {  
    return(f);
  }

  /* Abstract a variable that does not appear in f. */
  if (cuddI(manager,f->index) > cuddI(manager,cube->index)) {
    res1 = cuddLatAddUnivAbstractRecur(manager, f, cuddT(cube));
    if (res1 == NULL) return(NULL);
    return res1;
  }

  if ((res = cuddCacheLookup2(manager, CuddLat_addUnivAbstract, f, cube)) 
      != NULL) 
    {
      return(res);
    }

  T = cuddT(f);
  E = cuddE(f);

  /* If the two indices are the same, so are their levels. */
  if (f->index == cube->index) {
    res1 = cuddLatAddUnivAbstractRecur(manager, T, cuddT(cube));
    if (res1 == NULL) return(NULL);
    cuddRef(res1);
    if (res1 != zero) {
      res2 = cuddLatAddUnivAbstractRecur(manager, E, cuddT(cube));
      if (res2 == NULL) {
	Cudd_RecursiveDeref(manager,res1);
	return(NULL);
      }
      cuddRef(res2);
      res = cuddAddApplyRecur(manager, ALGEBRA(manager)->meetfn, res1, res2);
      if (res == NULL) {
	Cudd_RecursiveDeref(manager,res1);
	Cudd_RecursiveDeref(manager,res2);
	return(NULL);
      }
      cuddRef(res);
      Cudd_RecursiveDeref(manager,res1);
      Cudd_RecursiveDeref(manager,res2);
    } else {
      res = res1;
    }
    cuddCacheInsert2(manager, CuddLat_addUnivAbstract, f, cube, res);
    cuddDeref(res);
    return(res);
  } else { /* if (cuddI(manager,f->index) < cuddI(manager,cube->index)) */
    res1 = cuddLatAddUnivAbstractRecur(manager, T, cube);
    if (res1 == NULL) return(NULL);
    cuddRef(res1);
    res2 = cuddLatAddUnivAbstractRecur(manager, E, cube);
    if (res2 == NULL) {
      Cudd_RecursiveDeref(manager,res1);
      return(NULL);
    }
    cuddRef(res2);
    res = (res1 == res2) ? res1 :
      cuddUniqueInter(manager, (int) f->index, res1, res2);
    if (res == NULL) {
      Cudd_RecursiveDeref(manager,res1);
      Cudd_RecursiveDeref(manager,res2);
      return(NULL);
    }
    cuddDeref(res1);
    cuddDeref(res2);
    cuddCacheInsert2(manager, CuddLat_addUnivAbstract, f, cube, res);
    return(res);
  }

} 

static int addCheckPositiveCube(DdManager * manager, DdNode * cube)
{
  if (Cudd_IsComplement(cube)) return(0);
  if (cube == DD_ONE(manager)) return(1);
  if (cuddIsConstant(cube)) return(0);
  if (cuddE(cube) == DD_ZERO(manager)) {
    return(addCheckPositiveCube(manager, cuddT(cube)));
  }
  return(0);
}

JNIEXPORT jint JNICALL Java_edu_toronto_cs_cudd_CuddAdd_dumpDaVinciPtr
(JNIEnv *env, jobject jCudd, jstring jFileName, jint addPtr)
{
  DdManager * manager;
  DdNode * node;
  const char * fileName;
  FILE * file;
  int result;
  
  
  manager = getMgrPtr (env, jCudd);
  node = (DdNode*) addPtr;
  
  /* get string chars */
  fileName = (*env)->GetStringUTFChars (env, jFileName, NULL);

  /* open a file and call Cudd to dump the graph */
  file = fopen (fileName, "w");
  
  /* release the string since we don't need it any more */
  (*env)->ReleaseStringUTFChars (env, jFileName, fileName);

  result = Cudd_DumpDaVinci (manager, 1, &node, NULL, NULL, file);
  /* XXX Should check result and through an exception */
  fclose (file);
  return result;
  
}


