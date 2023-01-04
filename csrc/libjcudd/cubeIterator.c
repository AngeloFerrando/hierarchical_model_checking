#include "CubeIterator.h"
#include <stdio.h>

#include "util.h"
#include "cudd.h"
#include "cuddInt.h"


extern void setMgrPtr (JNIEnv *env, jobject jCudd, jint value);
extern DdManager* getMgrPtr (JNIEnv *env, jobject jCudd);
extern int checkResult (JNIEnv *env, DdManager *manager, DdNode *node);
extern int countSharingSize (JNIEnv *env, jintArray ptrs);

void setGenPtr (JNIEnv *env, jobject jCudd, jint value);
DdGen *getGenPtr (JNIEnv *env, jobject jCudd);
DdNode *getNodePtr (JNIEnv *env, jobject jCudd);

jintArray cubeToJava (JNIEnv *env, int len, int *cube, CUDD_VALUE_TYPE value);

JNIEXPORT jintArray JNICALL 
Java_edu_toronto_cs_cudd_CuddAdd_00024CubeIterator_firstCube 
(JNIEnv *env, jobject jIterator)
{
  DdManager *ddManager;
  
  /* the cube */
  int * cube;
  /* the value */
  CUDD_VALUE_TYPE value;
  /* the iterator */
  DdGen * gen;
  /* node whose cubes we want */
  DdNode * node;
  
  /* extract manager pointer */
  ddManager = getMgrPtr (env, jIterator);
  /* get pointer to the node */
  node = getNodePtr (env, jIterator);

  gen = Cudd_FirstCube (ddManager, node, &cube, &value);
  
  /***
   *** XXX This is extremelly bad but checkResult does not actually care
   *** XXX about the type of its second argument, it should have been 
   *** XXX void*!
   ***/
  if (checkResult (env, ddManager, (DdNode*)gen) == 0)
    {
      /* store gen for future use */
      setGenPtr (env, jIterator, (jint)gen);
      
      /* now convert cube + value into a Java array and return */
      return cubeToJava (env, ddManager->size, cube, value);
    }
  return NULL;
}

JNIEXPORT jintArray JNICALL 
Java_edu_toronto_cs_cudd_CuddAdd_00024CubeIterator_nextCube
(JNIEnv *env, jobject jIterator)
{
  DdManager *ddManager;
  DdGen * gen;
  int * cube;
  CUDD_VALUE_TYPE value;
  
  ddManager = getMgrPtr (env, jIterator);
  gen = getGenPtr (env, jIterator);
  

  if (Cudd_NextCube (gen, &cube, &value) == 0)
    {
      /* no more cubes, return Java null */
      return NULL;
    }
  else
    {
      /* convert this cube into Java array */
      return cubeToJava (env, ddManager->size, cube, value);
    }
}

JNIEXPORT void JNICALL Java_edu_toronto_cs_cudd_CuddAdd_00024CubeIterator_freeGen
(JNIEnv *env, jobject jIterator)
{
  DdGen * gen;
  
  gen = getGenPtr (env, jIterator);
  Cudd_GenFree (gen);
  
}

jintArray cubeToJava (JNIEnv *env, int len, int * cube, CUDD_VALUE_TYPE value)
{
  jintArray jArray;
  int * jBuffer;
  jclass cls;
  
  /* create new Java int array */
  jArray = (*env)->NewIntArray (env, (jsize)(len + 1));
  if (jArray == NULL) 
    {
      cls = (*env)->FindClass (env, "java/lang/OutOfMemoryError");
      (*env)->ThrowNew (env, cls, "Out of Memory  (CubeIterator).");
      return NULL;
    }
  

  /* get its buffer */
  jBuffer = (*env)->GetIntArrayElements (env, jArray, NULL);

  /* copy the cube */
  jBuffer [len] = (jint)value;
  
  {
    int i;
    for (i = 0; i < len; i++)
      jBuffer [i] = (cube [i] == 2) ? (jint)-1 : (jint)cube [i];
  }
  

  (*env)->ReleaseIntArrayElements (env, jArray, jBuffer, 0);
  return jArray;
}




void setGenPtr (JNIEnv *env, jobject jCudd, jint value)
{
  jclass cls;
  jfieldID fieldID;
  
  cls = (*env)->GetObjectClass (env, jCudd);
  fieldID = (*env)->GetFieldID (env, cls, "genPtr", "I");
  
  (*env)->SetIntField (env, jCudd, fieldID, value);
  return;
}

DdGen *getGenPtr (JNIEnv *env, jobject jCudd)
{
  jclass cls;
  jfieldID fieldID;
  
  cls = (*env)->GetObjectClass (env, jCudd);
  fieldID = (*env)->GetFieldID (env, cls, "genPtr", "I");
  
  return (DdGen*)(*env)->GetIntField (env, jCudd, fieldID);
}

DdNode *getNodePtr (JNIEnv *env, jobject jCudd)
{
  jclass cls;
  jfieldID fieldID;
  
  cls = (*env)->GetObjectClass (env, jCudd);
  fieldID = (*env)->GetFieldID (env, cls, "nodePtr", "I");
  
  return (DdNode*)(*env)->GetIntField (env, jCudd, fieldID);
}
