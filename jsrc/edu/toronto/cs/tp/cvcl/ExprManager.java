/* ----------------------------------------------------------------------------
 * This file was automatically generated by SWIG (http://www.swig.org).
 * Version: 1.3.21
 *
 * Do not make changes to this file unless you know what you are doing--modify
 * the SWIG interface file instead.
 * ----------------------------------------------------------------------------- */

package edu.toronto.cs.tp.cvcl;


public class ExprManager {
  private long swigCPtr;
  protected boolean swigCMemOwn;

  protected ExprManager(long cPtr, boolean cMemoryOwn) {
    swigCMemOwn = cMemoryOwn;
    swigCPtr = cPtr;
  }

  protected ExprManager() {
    this(0, false);
  }

  public void delete() {
    if(swigCPtr != 0 && swigCMemOwn) {
      swigCMemOwn = false;
      throw new UnsupportedOperationException("C++ destructor does not have public access");
    }
    swigCPtr = 0;
  }

  protected static long getCPtr(ExprManager obj) {
    return (obj == null) ? 0 : obj.swigCPtr;
  }

  public Expr rebuildExpr(Expr e) {
    return new Expr(JavaCVCJNI.ExprManager_rebuildExpr(swigCPtr, Expr.getCPtr(e)), true);
  }

}
