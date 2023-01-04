#include "CuddLatAdd.h"
#include <stdio.h>

#include "util.h"
#include "cudd.h"
#include "cuddInt.h"

typedef struct lattice lattice;

#define LATTICE(M) ((lattice*)(M)->data) 
#define DD_BOT(M)  LATTICE(M)->bot
#define DD_TOP(M) LATTICE(M)->top
#define MEET(M,X,Y) LATTICE(M)->meet[(int)X][(int)Y]
#define JOIN(M,X,Y) LATTICE(M)->join[(int)X][(int)Y]
#define NEG(M,X) LATTICE(M)->neg[(int)X]

#define SET_ENV(M,E) LATTICE((M))->env=(E)
#define TABLE_LOOKUP_MODE 1
#define CALLBACK_MODE 2

struct lattice
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


  /*** granted that we can use a union here, we avoid doing it for no good
   *** reason. The mode indicates the mode of operation, either through
   *** a table lookup or callbacks into java code 
   ***/
  int mode;

  /*** table lookups **/
  CUDD_VALUE_TYPE** meet;
  CUDD_VALUE_TYPE** join;
  CUDD_VALUE_TYPE* neg;
  int size;  /** the size of each array */

  /** calbacks **/
  JNIEnv *env;
  jobject jlat;
  jmethodID meetID;
  jmethodID joinID;
  jmethodID negID;
  jmethodID eqID;
  jmethodID leqID;
  jmethodID geqID;
  
};

extern void setMgrPtr (JNIEnv *env, jobject jCudd, jint value);
extern DdManager* getMgrPtr (JNIEnv *env, jobject jCudd);
extern int checkResult (JNIEnv *env, DdManager *manager, DdNode *node);
extern int countSharingSize (JNIEnv *env, jintArray ptrs);

DdNode *Lat_addNeg (DdManager * dd, DdNode * f);
DdNode *Lat_addMeet(DdManager * dd, DdNode ** f, DdNode ** g);
DdNode *Lat_addJoin(DdManager * dd, DdNode ** f, DdNode ** g);
DdNode *Lat_addAbove(DdManager * dd, DdNode ** f, DdNode ** g);
DdNode *Lat_addBelow(DdManager * dd, DdNode ** f, DdNode ** g);
DdNode *Lat_addEqual(DdManager * dd, DdNode ** f, DdNode ** g);

DdNode *Lat_addNegCallback (DdManager * dd, DdNode * f);
DdNode *Lat_addMeetCallback (DdManager * dd, DdNode ** f, DdNode ** g);
DdNode *Lat_addJoinCallback (DdManager * dd, DdNode ** f, DdNode ** g);
DdNode *Lat_addAboveCallback (DdManager * dd, DdNode ** f, DdNode ** g);
DdNode *Lat_addBelowCallback (DdManager * dd, DdNode ** f, DdNode ** g);
DdNode *Lat_addEqualCallback (DdManager * dd, DdNode ** f, DdNode ** g);


int * getIntArray (JNIEnv *env, jintArray jarray);
int **getIntIntArray (JNIEnv *env, jobjectArray jarray);
lattice *buildLattice (JNIEnv *env, DdManager *manager, 
		       int top, int bot, 
		       jobjectArray meet, jobjectArray join, 
		       jintArray neg);
void destroyLattice (lattice *lattice);

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

JNIEXPORT void JNICALL Java_edu_toronto_cs_cudd_CuddLatAdd_init__Ledu_toronto_cs_algebra_Algebra_2III
(JNIEnv *env, jobject jCudd, jobject jlat, jint initVars, jint top, jint bot)
{
  DdManager *manager;
  lattice *lattice;
  jclass clazz;
  
  manager = Cudd_Init ((int)initVars, 0, 
		       CUDD_UNIQUE_SLOTS, CUDD_CACHE_SLOTS, 0);
  
  lattice = (struct lattice *) malloc (sizeof (struct lattice));
  lattice->mode = CALLBACK_MODE;
  
  lattice->top = Cudd_addConst (manager, (CUDD_VALUE_TYPE)top);
  Cudd_Ref (lattice->top);
  lattice->bot = Cudd_addConst (manager, (CUDD_VALUE_TYPE)bot);
  Cudd_Ref (lattice->bot);


  /*** create a global reference since we are going to keep this 
   *** object during our lifetime
   ***/
  lattice->jlat = (*env)->NewGlobalRef (env, jlat);
  clazz = (*env)->GetObjectClass (env, lattice->jlat);
  
  /*** get method ids **/
  lattice->meetID = (*env)->GetMethodID (env, clazz, "times", "(II)I");
  lattice->joinID = (*env)->GetMethodID (env, clazz, "plus", "(II)I");
  lattice->negID = (*env)->GetMethodID (env, clazz, "neg", "(I)I");
  /*  lattice->eqID = (*env)->GetMethodID (env, clazz, "eq", "(II)Z");
  lattice->leqID = (*env)->GetMethodID (env, clazz, "leq", "(II)Z");
  lattice->geqID = (*env)->GetMethodID (env, clazz, "geq", "(II)Z");
  */
  /*** set the function pointers ***/
  lattice->meetfn = Lat_addMeetCallback;
  lattice->joinfn = Lat_addJoinCallback;
  lattice->negfn = Lat_addNegCallback;
  lattice->eqfn = Lat_addEqualCallback;
  lattice->leqfn = Lat_addBelowCallback;
  lattice->geqfn = Lat_addAboveCallback;


  manager->data = lattice;
  Cudd_RecursiveDeref (manager, manager->one);
  manager->one = lattice->top;
  Cudd_RecursiveDeref (manager, manager->zero);
  manager->zero = lattice->bot;
  Cudd_SetBackground (manager, lattice->bot);
  setMgrPtr (env, jCudd, (jint)manager);
}


JNIEXPORT void JNICALL Java_edu_toronto_cs_cudd_CuddLatAdd_init__III_3_3I_3_3I_3I
  (JNIEnv *env, jobject jCudd, jint initVars,
   jint top, jint bot, 
   jobjectArray meet, jobjectArray join, jintArray neg)
{
  DdManager *manager;
  lattice *lattice;
  DdNode * var;
  
  manager = Cudd_Init ((int)initVars, 0, 
		       CUDD_UNIQUE_SLOTS, CUDD_CACHE_SLOTS, 0);


  lattice = buildLattice (env, manager, top, bot, meet, join, neg);
  lattice->mode = TABLE_LOOKUP_MODE;

  /*** set the function pointers ***/
  lattice->meetfn = Lat_addMeet;
  lattice->joinfn = Lat_addJoin;
  lattice->negfn = Lat_addNeg;
  lattice->eqfn = Lat_addEqual;
  lattice->leqfn = Lat_addBelow;
  lattice->geqfn = Lat_addAbove;


  manager->data = lattice;


  Cudd_RecursiveDeref (manager, manager->one);
  manager->one = lattice->top;
  Cudd_RecursiveDeref (manager, manager->zero);
  manager->zero = lattice->bot;
/*    cuddV(manager->one) = top; */
/*    cuddV(manager->zero) = bot; */

/*    lattice->top = manager->one; */
/*    lattice->bot = manager->zero; */
  
  Cudd_SetBackground (manager, lattice->bot);

  setMgrPtr (env, jCudd, (jint)manager);
  return;
}



JNIEXPORT jint JNICALL 
Java_edu_toronto_cs_cudd_CuddLatAdd_cuddAddOnePtr (JNIEnv *env, jobject jCudd)
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
Java_edu_toronto_cs_cudd_CuddLatAdd_cuddAddZeroPtr (JNIEnv *env, 
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
Java_edu_toronto_cs_cudd_CuddLatAdd_addVarPtr
(JNIEnv *env, jobject jCudd, jint index)
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
  Java_edu_toronto_cs_cudd_CuddLatAdd_addConstant
  (JNIEnv *env, jobject jCudd, jint value)
{
  DdManager *manager;
  DdNode *node;
  
  manager = getMgrPtr (env, jCudd);
  node = Cudd_addConst (manager, (CUDD_VALUE_TYPE)value);
  if (checkResult (env, manager, node) == 0)
    Cudd_Ref (node);
  return (jint)node;
}



JNIEXPORT jint JNICALL Java_edu_toronto_cs_cudd_CuddLatAdd_addAndPtr
(JNIEnv *env, jobject jCudd, jint a, jint b)
{
  DdManager *manager;
  DdNode *node;
  
  manager = getMgrPtr (env, jCudd);
  SET_ENV(manager,env);
  
  node = Cudd_addApply (manager, LATTICE(manager)->meetfn, 
			(DdNode*)a, (DdNode*)b);
  if (checkResult (env, manager, node) == 0)
    Cudd_Ref (node);
  return (jint)node;
}

JNIEXPORT jint JNICALL Java_edu_toronto_cs_cudd_CuddLatAdd_addOrPtr
(JNIEnv *env, jobject jCudd, jint a, jint b)
{
  DdManager *manager;
  DdNode *node;
  
  manager = getMgrPtr (env, jCudd);
  SET_ENV(manager,env);
  node = Cudd_addApply (manager, LATTICE(manager)->joinfn, 
			(DdNode*)a, (DdNode*)b);

  if (checkResult (env, manager, node) == 0)
    Cudd_Ref (node);
  return (jint)node;
}

JNIEXPORT jint JNICALL Java_edu_toronto_cs_cudd_CuddLatAdd_addNotPtr
(JNIEnv *env, jobject jCudd, jint a)
{
  DdManager *manager;
  DdNode *node;
  
  manager = getMgrPtr (env, jCudd);
  SET_ENV(manager,env);
  node = Cudd_addMonadicApply (manager, LATTICE(manager)->negfn, (DdNode*)a);
  
  
  if (checkResult (env, manager, node) == 0)
    Cudd_Ref (node);
  return (jint)node;
}

JNIEXPORT jint JNICALL Java_edu_toronto_cs_cudd_CuddLatAdd_addGeqPtr
  (JNIEnv *env, jobject jCudd, jint a, jint b)
{
  DdManager *manager;
  DdNode *node;
  
  manager = getMgrPtr (env, jCudd);
  SET_ENV(manager,env);
  
  node = Cudd_addApply (manager, LATTICE(manager)->geqfn, 
			(DdNode*)a, (DdNode*)b);

  if (checkResult (env, manager, node) == 0)
    Cudd_Ref (node);
  return (jint)node;
}

JNIEXPORT jint JNICALL Java_edu_toronto_cs_cudd_CuddLatAdd_addLeqPtr
  (JNIEnv *env, jobject jCudd, jint a, jint b)
{
  DdManager *manager;
  DdNode *node;
  
  manager = getMgrPtr (env, jCudd);
  SET_ENV(manager,env);
  
  node = Cudd_addApply (manager, LATTICE(manager)->leqfn,
			(DdNode*)a, (DdNode*)b);

  if (checkResult (env, manager, node) == 0)
    Cudd_Ref (node);
  return (jint)node;
}

JNIEXPORT jint JNICALL Java_edu_toronto_cs_cudd_CuddLatAdd_addEqPtr
  (JNIEnv *env, jobject jCudd, jint a, jint b)
{
  DdManager *manager;
  DdNode *node;
  
  manager = getMgrPtr (env, jCudd);
  SET_ENV(manager,env);
  
  node = Cudd_addApply (manager, LATTICE(manager)->eqfn, 
			(DdNode*)a, (DdNode*)b);

  if (checkResult (env, manager, node) == 0)
    Cudd_Ref (node);
  return (jint)node;
}


JNIEXPORT jint JNICALL Java_edu_toronto_cs_cudd_CuddLatAdd_addConstrainPtr
(JNIEnv *env, jobject jCudd, jint a, jint b)
{
  DdManager *manager;
  DdNode *node;
  
  manager = getMgrPtr (env, jCudd);
    
  node = Cudd_addConstrain (manager, (DdNode*)a, (DdNode*)b);

  if (checkResult (env, manager, node) == 0)
    Cudd_Ref (node);
  return (jint)node;
}


JNIEXPORT jint JNICALL Java_edu_toronto_cs_cudd_CuddLatAdd_addPermutePtr
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
  
  node = Cudd_addPermute (manager, node, permut);

  (*env)->ReleaseIntArrayElements (env, map, body, 0);
  if (checkResult (env, manager, node) == 0)
    Cudd_Ref (node);
  return (jint)node;
}

JNIEXPORT jint JNICALL 
  Java_edu_toronto_cs_cudd_CuddLatAdd_addGetValue
  (JNIEnv *env, jobject jCudd, jint ptr)
{
  DdNode *node;
  DdManager *manager;
  
  manager = getMgrPtr (env, jCudd);
  node = (DdNode*)ptr;
  
  return cuddV (node);
}


JNIEXPORT jint JNICALL Java_edu_toronto_cs_cudd_CuddLatAdd_existAbstract
  (JNIEnv *env, jobject jCudd, jint ptr, jint cubePtr)
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

JNIEXPORT jint JNICALL Java_edu_toronto_cs_cudd_CuddLatAdd_forallAbstract
  (JNIEnv *env, jobject jCudd, jint ptr, jint cubePtr)
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
  Java_edu_toronto_cs_cudd_CuddLatAdd_ref (JNIEnv *env, jobject jCudd, 
					   jint ptr)
{
  DdNode *node;
  
  node = (DdNode*)ptr;
  Cudd_Ref (node);
  return ;
}



JNIEXPORT void JNICALL 
Java_edu_toronto_cs_cudd_CuddLatAdd_deref (JNIEnv *env, jobject jCudd, 
					   jint ptr)
{
  DdNode *node;

  node = (DdNode*)ptr;  
  Cudd_Deref (node);
  return;
}

JNIEXPORT void JNICALL 
Java_edu_toronto_cs_cudd_CuddLatAdd_recursiveDeref 
(JNIEnv *env, jobject jCudd, jint ptr)
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

JNIEXPORT jint JNICALL Java_edu_toronto_cs_cudd_CuddLatAdd_checkZeroRef
  (JNIEnv *env, jobject jCudd)
{
  int retval;
  DdManager *manager = getMgrPtr (env, jCudd);
  
  retval = Cudd_CheckZeroRef (manager);
  return (jint)retval;
}

JNIEXPORT void JNICALL Java_edu_toronto_cs_cudd_CuddLatAdd_quit
  (JNIEnv *env, jobject jCudd)
{
  DdManager *manager = getMgrPtr (env, jCudd);
  
  if (manager->data != NULL)
    {
      /** destroy the lattice struct based on the mode of operation **/
      if (LATTICE(manager)->mode == TABLE_LOOKUP_MODE)
	/** Free lattice */
	destroyLattice (LATTICE(manager));
      else
	(*env)->DeleteGlobalRef (env, LATTICE(manager)->jlat);

      free (manager->data);
      manager->data = NULL;
    }
  
  
  Cudd_Quit (manager);
}



JNIEXPORT void JNICALL Java_edu_toronto_cs_cudd_CuddLatAdd_info 
  (JNIEnv *env, jobject jCudd)
{
  DdManager *manager = getMgrPtr (env, jCudd);
  Cudd_PrintInfo (manager, stdout);
}

JNIEXPORT void JNICALL Java_edu_toronto_cs_cudd_CuddLatAdd_printMintermPtr
  (JNIEnv *env, jobject jCudd, jint ptr)
{
  DdManager *manager = getMgrPtr (env, jCudd);
  Cudd_PrintMinterm (manager, (DdNode*)ptr);
}

JNIEXPORT void JNICALL Java_edu_toronto_cs_cudd_CuddLatAdd_reorder
  (JNIEnv *env, jobject jCudd, jint minsize)
{
  DdManager *manager = getMgrPtr (env, jCudd);
  Cudd_ReduceHeap (manager, CUDD_REORDER_SIFT, minsize);
}


JNIEXPORT jint JNICALL Java_edu_toronto_cs_cudd_CuddLatAdd_dagSize
  (JNIEnv *env, jobject jCudd, jint ptr)
{
  return (jint)Cudd_DagSize ((DdNode*)ptr);
}


JNIEXPORT jint JNICALL Java_edu_toronto_cs_cudd_CuddLatAdd_sharingSize
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
  
  env = LATTICE(dd)->env;

  F = *f; G = *g;
  if (F == DD_TOP (dd) || G == DD_TOP (dd)) return (DD_TOP(dd));
  if (F == DD_BOT(dd)) return(G);
  if (G == DD_BOT(dd)) return(F);
  if (cuddIsConstant(F) && cuddIsConstant(G)) {
    value = 
      (int)(*env)->CallIntMethod (env, LATTICE(dd)->jlat, LATTICE(dd)->joinID,
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




/** performs a join */
DdNode *Lat_addJoin(DdManager * dd, DdNode ** f, DdNode ** g)
{
  DdNode *res;
  DdNode *F, *G;
  CUDD_VALUE_TYPE value;

  F = *f; G = *g;
  if (F == DD_TOP (dd) || G == DD_TOP (dd)) return (DD_TOP(dd));
  if (F == DD_BOT(dd)) return(G);
  if (G == DD_BOT(dd)) return(F);
  if (cuddIsConstant(F) && cuddIsConstant(G)) {
    value = JOIN(dd,cuddV(F),cuddV(G));
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
  JNIEnv *env = LATTICE(dd)->env;
  
  F = *f; G = *g;
  if (F == DD_BOT(dd) || G == DD_BOT(dd)) return(DD_BOT(dd));
  if (F == DD_TOP(dd)) return(G);
  if (G == DD_TOP(dd)) return(F);
  if (cuddIsConstant(F) && cuddIsConstant(G)) {
    value = (int)(*env)->CallIntMethod (env, LATTICE(dd)->jlat, 
				     LATTICE(dd)->meetID, 
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


DdNode *Lat_addMeet(DdManager * dd, DdNode ** f, DdNode ** g)
{
  DdNode *res;
  DdNode *F, *G;
  CUDD_VALUE_TYPE value;

  F = *f; G = *g;
  if (F == DD_BOT(dd) || G == DD_BOT(dd)) return(DD_BOT(dd));
  if (F == DD_TOP(dd)) return(G);
  if (G == DD_TOP(dd)) return(F);
  if (cuddIsConstant(F) && cuddIsConstant(G)) {
    value = MEET(dd,cuddV(F),cuddV(G));
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
  JNIEnv *env = LATTICE(dd)->env;
  if (cuddIsConstant(f)) {
    DdNode * res;
    CUDD_VALUE_TYPE value;
    
    value = (CUDD_VALUE_TYPE)(*env)->CallIntMethod (env, LATTICE(dd)->jlat, 
						 LATTICE(dd)->negID,
						 (jint)cuddV(f));    
    res = cuddUniqueConst(dd,value);
    return(res);
  }
  return(NULL);

}

DdNode *Lat_addNeg(DdManager * dd, DdNode * f)
{
  if (cuddIsConstant(f)) {
    CUDD_VALUE_TYPE value = NEG(dd,cuddV(f));
    DdNode *res = cuddUniqueConst(dd,value);
    return(res);
  }
  return(NULL);
} 

DdNode *Lat_addAboveCallback(DdManager * dd, DdNode ** f, DdNode ** g)
{
  DdNode *res;
  DdNode *F, *G;
  CUDD_VALUE_TYPE value;
  JNIEnv *env = LATTICE(dd)->env;

  jboolean result;
  

  F = *f; G = *g;
  if (cuddIsConstant(F) && cuddIsConstant(G)) 
    {
      result = (*env)->CallBooleanMethod (env, LATTICE(dd)->jlat, 
				       LATTICE(dd)->geqID,
				       (jint)cuddV(F), (jint)cuddV(G));
      if (result)
	return DD_TOP(dd);
      else
	return DD_BOT(dd);
    }
  return(NULL);
}



DdNode *Lat_addAbove(DdManager * dd, DdNode ** f, DdNode ** g)
{
  DdNode *res;
  DdNode *F, *G;
  CUDD_VALUE_TYPE value;
  
  F = *f; G = *g;
  if (cuddIsConstant(F) && cuddIsConstant(G)) 
    {
      if (JOIN(dd, cuddV(F),cuddV(G)) == cuddV(F))
	return DD_TOP(dd);
      else
	return DD_BOT(dd);
    }
  return(NULL);
}


DdNode *Lat_addBelowCallback(DdManager * dd, DdNode ** f, DdNode ** g)
{
  DdNode *res;
  DdNode *F, *G;
  CUDD_VALUE_TYPE value;
  JNIEnv *env = LATTICE(dd)->env;

  jboolean result;
  

  F = *f; G = *g;
  if (cuddIsConstant(F) && cuddIsConstant(G)) 
    {
      result = (*env)->CallBooleanMethod (env, LATTICE(dd)->jlat, 
				       LATTICE(dd)->leqID,
				       (jint)cuddV(F), (jint)cuddV(G));
      if (result)
	return DD_TOP(dd);
      else
	return DD_BOT(dd);
    }
  return(NULL);
}

		     
DdNode *Lat_addBelow(DdManager * dd, DdNode ** f, DdNode ** g)
{
  DdNode *res;
  DdNode *F, *G;
  CUDD_VALUE_TYPE value;
  
  F = *f; G = *g;
  if (cuddIsConstant(F) && cuddIsConstant(G)) 
    {
      if (MEET(dd,cuddV(F),cuddV(G)) == cuddV(F))
	return DD_TOP(dd);
      else
	return DD_BOT(dd);
    }
  return(NULL);
}


DdNode *Lat_addEqualCallback (DdManager * dd, DdNode **f, DdNode ** g)
{
  // -- equality can be done directly without callbacks
  return Lat_addEqual (dd, f, g);
}

DdNode *Lat_addEqual(DdManager * dd, DdNode ** f, DdNode ** g)
{
  DdNode *res;
  DdNode *F, *G;
  CUDD_VALUE_TYPE value;

  F = *f; G = *g;
  if (cuddIsConstant(F) && cuddIsConstant(G)) 
    {
      if (cuddV(F) == cuddV(G))
	return DD_TOP(dd);
      else
	return DD_BOT(dd);
    }
  return(NULL);
}



int * getIntArray (JNIEnv *env, jintArray jarray);

int **getIntIntArray (JNIEnv *env, jobjectArray jarray)
{
  int i;
  int **result;
  /*** get the size of the array */
  jsize size = (*env)->GetArrayLength (env, jarray);

  result = (int**)malloc(size * sizeof(int*));
  
  for (i = 0; i < size; i++)
    result [i] = 
      getIntArray (env, 
		   (jintArray)(*env)->GetObjectArrayElement (env, jarray, i));
  return result;
}

int * getIntArray (JNIEnv *env, jintArray jarray)
{
  int i;
  int size = (*env)->GetArrayLength (env, jarray);
  
  jint *data = (*env)->GetIntArrayElements (env, jarray, (jboolean*)NULL);
  int *result = (int*)malloc (size * sizeof (int));
  
  memcpy (result, data, size*sizeof(int));
  (*env)->ReleaseIntArrayElements (env, jarray, data, JNI_ABORT);
  return result;
}

lattice *buildLattice (JNIEnv *env, DdManager *manager, 
		       int top, int bot, 
		       jobjectArray meet, jobjectArray join, 
		       jintArray neg)

{
  lattice *lattice;

  lattice = (struct lattice *) malloc (sizeof(struct lattice));
  
  lattice->meet = getIntIntArray (env, meet);
  lattice->join = getIntIntArray (env, join);
  lattice->neg = getIntArray (env, neg);
  
  lattice->top = Cudd_addConst (manager, (CUDD_VALUE_TYPE)top);
  Cudd_Ref (lattice->top);

  lattice->bot = Cudd_addConst (manager, (CUDD_VALUE_TYPE)bot);
  Cudd_Ref (lattice->bot);
  
  return lattice;
}

void destroyLattice (lattice* lattice)
{
  int i;

  /*** XXX: Don't even try to destroy an empty lattice */
  if (lattice == NULL) return;
  
  free (lattice->neg);
  lattice->neg = NULL;
  for (i = 0; i < lattice->size; i++)
    {
      free (lattice->meet [i]);
      free (lattice->join [i]);
    }
  lattice->meet = NULL;
  lattice->join = NULL;
  lattice->top = NULL;
  lattice->bot = NULL;
  return;
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
/*     cuddRef(res1); */
    /* Use the "internal" procedure to be alerted in case of
    ** dynamic reordering. If dynamic reordering occurs, we
    ** have to abort the entire abstraction.
    */
/*     res = cuddAddApplyRecur(manager, Lat_addJoin, res1, res1); */
/*     if (res == NULL) { */
/*       Cudd_RecursiveDeref(manager,res1); */
/*       return(NULL); */
/*     } */
/*     cuddRef(res); */
/*     Cudd_RecursiveDeref(manager,res1); */
/*     cuddDeref(res); */
/*     return(res); */
/*     cuddDeref (res1); */
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
      res = cuddAddApplyRecur(manager, LATTICE(manager)->joinfn, res1, res2);
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
/*     cuddRef(res1); */
    /* Use the "internal" procedure to be alerted in case of
    ** dynamic reordering. If dynamic reordering occurs, we
    ** have to abort the entire abstraction.
    */
/*     res = cuddAddApplyRecur(manager, Lat_addMeet, res1, res1); */
/*     if (res == NULL) { */
/*       Cudd_RecursiveDeref(manager,res1); */
/*       return(NULL); */
/*     } */
/*     cuddRef(res); */
/*     Cudd_RecursiveDeref(manager,res1); */
/*     cuddDeref(res); */
/*     return(res); */
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
      res = cuddAddApplyRecur(manager, LATTICE(manager)->meetfn, res1, res2);
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

JNIEXPORT jint JNICALL Java_edu_toronto_cs_cudd_CuddLatAdd_dumpDaVinciPtr
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


