/* ----------------------------------------------------------------------------
 * This file was automatically generated by SWIG (http://www.swig.org).
 * Version: 1.3.21
 *
 * Do not make changes to this file unless you know what you are doing--modify
 * the SWIG interface file instead.
 * ----------------------------------------------------------------------------- */

package edu.toronto.cs.tp.cvcl;


public class Type {
  private long swigCPtr;
  protected boolean swigCMemOwn;

  protected Type(long cPtr, boolean cMemoryOwn) {
    swigCMemOwn = cMemoryOwn;
    swigCPtr = cPtr;
  }

  public void delete() {
    if(swigCPtr != 0 && swigCMemOwn) {
      swigCMemOwn = false;
      throw new UnsupportedOperationException("C++ destructor does not have public access");
    }
    swigCPtr = 0;
  }

  protected static long getCPtr(Type obj) {
    return (obj == null) ? 0 : obj.swigCPtr;
  }

  public Type() {
    this(JavaCVCJNI.new_Type(), true);
  }

  public String toString() {
    return JavaCVCJNI.Type_toString(swigCPtr);
  }

  public Expr getExpr() {
    return new Expr(JavaCVCJNI.Type_getExpr(swigCPtr), false);
  }

  public int arity() {
    return JavaCVCJNI.Type_arity(swigCPtr);
  }

}
