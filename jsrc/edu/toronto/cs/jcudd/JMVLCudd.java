package edu.toronto.cs.jcudd;

/* ----------------------------------------------------------------------------
 * This file was automatically generated by SWIG (http://www.swig.org).
 * Version: 1.3.21
 *
 * Do not make changes to this file unless you know what you are doing--modify
 * the SWIG interface file instead.
 * ----------------------------------------------------------------------------- */


public class JMVLCudd implements JMVLCuddConstants {
  public static int isCube(int dd, int node) {
    return JMVLCuddJNI.isCube(dd, node);
  }

  public static void Cudd_AutodynEnable(int dd, int method) {
    JMVLCuddJNI.Cudd_AutodynEnable(dd, method);
  }

  public static void Cudd_AutodynDisable(int dd) {
    JMVLCuddJNI.Cudd_AutodynDisable(dd);
  }

  public static int jmvlCudd_Init(int nvars, Object jalgebra, int top, int bot, int infoTop, int infoBot) {return JMVLCuddJNI.jmvlCudd_Init(nvars, jalgebra, top, bot, infoTop, infoBot);}

  public static void jmvlCudd_Quit(int quit) {
    JMVLCuddJNI.jmvlCudd_Quit(quit);
  }

  public static void Cudd_RecursiveDeref(int dd, int f) {
    JMVLCuddJNI.Cudd_RecursiveDeref(dd, f);
  }

  public static int Cudd_PrintMinterm(int dd, int node) {
    return JMVLCuddJNI.Cudd_PrintMinterm(dd, node);
  }

  public static int Cudd_CheckZeroRef(int dd) {
    return JMVLCuddJNI.Cudd_CheckZeroRef(dd);
  }

  public static int cuddV(int node) {
    return JMVLCuddJNI.cuddV(node);
  }

  public static int Cudd_IsConstant(int node) {
    return JMVLCuddJNI.Cudd_IsConstant(node);
  }

  public static int cuddGarbageCollect(int dd, int clearCache) {
    return JMVLCuddJNI.cuddGarbageCollect(dd, clearCache);
  }

  public static void Cudd_Ref(int node) {
    JMVLCuddJNI.Cudd_Ref(node);
  }

  public static int Cudd_DagSize(int node) {
    return JMVLCuddJNI.Cudd_DagSize(node);
  }

  public static int Cudd_FirstCube(int dd, int node, CuddCube CUBE) {return JMVLCuddJNI.Cudd_FirstCube(dd, node, CUBE);}

  public static int Cudd_NextCube(int gen, CuddCube CUBE) {
    return JMVLCuddJNI.Cudd_NextCube(gen, CUBE);
  }

  public static int Cudd_GenFree(int gen) {
    return JMVLCuddJNI.Cudd_GenFree(gen);
  }

  public static int ddSize(int dd) {
    return JMVLCuddJNI.ddSize(dd);
  }

  public static int Cudd_ReadOne(int dd) {return JMVLCuddJNI.Cudd_ReadOne(dd);}

  public static int Cudd_ReadLogicZero(int dd) {return JMVLCuddJNI.Cudd_ReadLogicZero(dd);}

  public static int Cudd_Not(int node) {return JMVLCuddJNI.Cudd_Not(node);}

  public static int Cudd_bddAnd(int dd, int f, int g) {return JMVLCuddJNI.Cudd_bddAnd(dd, f, g);}

  public static int Cudd_bddOr(int dd, int f, int g) {return JMVLCuddJNI.Cudd_bddOr(dd, f, g);}

  public static int Cudd_bddIte(int dd, int f, int g, int h) {return JMVLCuddJNI.Cudd_bddIte(dd, f, g, h);}

  public static int Cudd_bddUnivAbstract(int manager, int f, int cube) {return JMVLCuddJNI.Cudd_bddUnivAbstract(manager, f, cube);}

  public static int Cudd_bddExistAbstract(int manager, int f, int cube) {return JMVLCuddJNI.Cudd_bddExistAbstract(manager, f, cube);}

  public static int Cudd_bddNewVar(int dd) {return JMVLCuddJNI.Cudd_bddNewVar(dd);}

  public static int Cudd_bddIthVar(int dd, int i) {return JMVLCuddJNI.Cudd_bddIthVar(dd, i);}

  public static int Cudd_bddXor(int dd, int f, int g) {return JMVLCuddJNI.Cudd_bddXor(dd, f, g);}

  public static int Cudd_bddPermute(int manager, int node, SWIGTYPE_p_int permut) {return JMVLCuddJNI.Cudd_bddPermute(manager, node, SWIGTYPE_p_int.getCPtr(permut));}

  public static int Cudd_bddAndAbstract(int manager, int f, int g, int cube) {return JMVLCuddJNI.Cudd_bddAndAbstract(manager, f, g, cube);}

  public static int Cudd_addNewVar(int dd) {return JMVLCuddJNI.Cudd_addNewVar(dd);}

  public static int Cudd_addIthVar(int dd, int i) {return JMVLCuddJNI.Cudd_addIthVar(dd, i);}

  public static int Cudd_addConst(int dd, int c) {return JMVLCuddJNI.Cudd_addConst(dd, c);}

  public static int Cudd_addIte(int dd, int f, int g, int h) {return JMVLCuddJNI.Cudd_addIte(dd, f, g, h);}

  public static int Cudd_addPermute(int dd, int node, SWIGTYPE_p_int permut) {return JMVLCuddJNI.Cudd_addPermute(dd, node, SWIGTYPE_p_int.getCPtr(permut));}

  public static int Cudd_Cofactor(int dd, int f, int g) {return JMVLCuddJNI.Cudd_Cofactor(dd, f, g);}

  public static int jmvlCudd_Not(int jmanager, int f) {return JMVLCuddJNI.jmvlCudd_Not(jmanager, f);}

  public static int jmvlCudd_And(int jmanager, int f, int g) {return JMVLCuddJNI.jmvlCudd_And(jmanager, f, g);}

  public static int jmvlCudd_Or(int jmanager, int f, int g) {return JMVLCuddJNI.jmvlCudd_Or(jmanager, f, g);}

  public static int jmvlCudd_Eq(int jmanager, int f, int g) {return JMVLCuddJNI.jmvlCudd_Eq(jmanager, f, g);}

  public static int jmvlCudd_Above(int jmanager, int f, int g) {return JMVLCuddJNI.jmvlCudd_Above(jmanager, f, g);}

  public static int jmvlCudd_Below(int jmanager, int f, int g) {return JMVLCuddJNI.jmvlCudd_Below(jmanager, f, g);}

  public static int jmvlCudd_infoNot(int jmanager, int f) {return JMVLCuddJNI.jmvlCudd_infoNot(jmanager, f);}

  public static int jmvlCudd_infoMeet(int jmanager, int f, int g) {return JMVLCuddJNI.jmvlCudd_infoMeet(jmanager, f, g);}

  public static int jmvlCudd_infoJoin(int jmanager, int f, int g) {return JMVLCuddJNI.jmvlCudd_infoJoin(jmanager, f, g);}

  public static int jmvlCudd_infoAbove(int jmanager, int f, int g) {return JMVLCuddJNI.jmvlCudd_infoAbove(jmanager, f, g);}

  public static int jmvlCudd_infoBelow(int jmanager, int f, int g) {return JMVLCuddJNI.jmvlCudd_infoBelow(jmanager, f, g);}

  public static int mvlCudd_ExistAbstract(int jmanager, int f, int cube) {return JMVLCuddJNI.mvlCudd_ExistAbstract(jmanager, f, cube);}

  public static int mvlCudd_UnivAbstract(int jmanager, int f, int cube) {return JMVLCuddJNI.mvlCudd_UnivAbstract(jmanager, f, cube);}

  public static int mvlCudd_ExistMeetAbstract(int jmanager, int f, int g, int cube) {return JMVLCuddJNI.mvlCudd_ExistMeetAbstract(jmanager, f, g, cube);}

  public static int mvlCudd_Support(int jmanager, int f) {return JMVLCuddJNI.mvlCudd_Support(jmanager, f);}

  public static int mvlCudd_CubeDiff(int jmanager, int a, int b) {return JMVLCuddJNI.mvlCudd_CubeDiff(jmanager, a, b);}

  public static int mvlCudd_NextMinterm(int gen, int termValue, int vars, int top, int bot) {return JMVLCuddJNI.mvlCudd_NextMinterm(gen, termValue, vars, top, bot);}

  public static int mvlCudd_bddNextMinterm(int gen, int termValue, int vars, int top, int bot) {return JMVLCuddJNI.mvlCudd_bddNextMinterm(gen, termValue, vars, top, bot);}

  public static int mvlCudd_MintermIterator(int mvlDd, int f) {return JMVLCuddJNI.mvlCudd_MintermIterator(mvlDd, f);}

  public static int mvlCudd_HasNextMinterm(int gen, int termValue) {
    return JMVLCuddJNI.mvlCudd_HasNextMinterm(gen, termValue);
  }

}