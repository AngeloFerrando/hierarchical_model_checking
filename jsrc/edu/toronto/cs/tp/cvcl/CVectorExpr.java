/* ----------------------------------------------------------------------------
 * This file was automatically generated by SWIG (http://www.swig.org).
 * Version: 1.3.21
 *
 * Do not make changes to this file unless you know what you are doing--modify
 * the SWIG interface file instead.
 * ----------------------------------------------------------------------------- */

package edu.toronto.cs.tp.cvcl;


public class CVectorExpr {
  private long swigCPtr;
  protected boolean swigCMemOwn;

  protected CVectorExpr(long cPtr, boolean cMemoryOwn) {
    swigCMemOwn = cMemoryOwn;
    swigCPtr = cPtr;
  }

  protected CVectorExpr() {
    this(0, false);
  }

  protected void finalize() {
    delete();
  }

  public void delete() {
    if(swigCPtr != 0 && swigCMemOwn) {
      swigCMemOwn = false;
      JavaCVCJNI.delete_CVectorExpr(swigCPtr);
    }
    swigCPtr = 0;
  }

  protected static long getCPtr(CVectorExpr obj) {
    return (obj == null) ? 0 : obj.swigCPtr;
  }

  public CVectorExpr(long size) {
    this(JavaCVCJNI.new_CVectorExpr(size), true);
  }

  public long size() {
    return JavaCVCJNI.CVectorExpr_size(swigCPtr);
  }

  public boolean isEmpty() {
    return JavaCVCJNI.CVectorExpr_isEmpty(swigCPtr);
  }

  public void clear() {
    JavaCVCJNI.CVectorExpr_clear(swigCPtr);
  }

  public void add(Expr x) {
    JavaCVCJNI.CVectorExpr_add(swigCPtr, Expr.getCPtr(x));
  }

  public Expr get(int i) {
    return new Expr(JavaCVCJNI.CVectorExpr_get(swigCPtr, i), false);
  }

  public void set(int i, Expr x) {
    JavaCVCJNI.CVectorExpr_set(swigCPtr, i, Expr.getCPtr(x));
  }

}
