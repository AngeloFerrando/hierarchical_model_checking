/* ----------------------------------------------------------------------------
 * This file was automatically generated by SWIG (http://www.swig.org).
 * Version: 1.3.21
 *
 * Do not make changes to this file unless you know what you are doing--modify
 * the SWIG interface file instead.
 * ----------------------------------------------------------------------------- */

package edu.toronto.cs.tp.cvcl;


public class CLFlags {
  private long swigCPtr;
  protected boolean swigCMemOwn;

  protected CLFlags(long cPtr, boolean cMemoryOwn) {
    swigCMemOwn = cMemoryOwn;
    swigCPtr = cPtr;
  }

  protected CLFlags() {
    this(0, false);
  }

  protected void finalize() {
    delete();
  }

  public void delete() {
    if(swigCPtr != 0 && swigCMemOwn) {
      swigCMemOwn = false;
      JavaCVCJNI.delete_CLFlags(swigCPtr);
    }
    swigCPtr = 0;
  }

  protected static long getCPtr(CLFlags obj) {
    return (obj == null) ? 0 : obj.swigCPtr;
  }

  public int countFlags(String name) {
    return JavaCVCJNI.CLFlags_countFlags__SWIG_0(swigCPtr, name);
  }

  public int countFlags(String name, SWIGTYPE_p_std__vectorTstd__string_t names) {
    return JavaCVCJNI.CLFlags_countFlags__SWIG_1(swigCPtr, name, SWIGTYPE_p_std__vectorTstd__string_t.getCPtr(names));
  }

  public void setFlag(String name, boolean b) {
    JavaCVCJNI.CLFlags_setFlag__SWIG_0(swigCPtr, name, b);
  }

  public void setFlag(String name, int i) {
    JavaCVCJNI.CLFlags_setFlag__SWIG_1(swigCPtr, name, i);
  }

  public void setFlag(String name, String s) {
    JavaCVCJNI.CLFlags_setFlag__SWIG_2(swigCPtr, name, s);
  }

  public void setFlag(String name, SWIGTYPE_p_std__pairTstd__string_bool_t p) {
    JavaCVCJNI.CLFlags_setFlag__SWIG_3(swigCPtr, name, SWIGTYPE_p_std__pairTstd__string_bool_t.getCPtr(p));
  }

  public void setFlag(String name, SWIGTYPE_p_std__vectorTstd__pairTstd__string_bool_t_t sv) {
    JavaCVCJNI.CLFlags_setFlag__SWIG_4(swigCPtr, name, SWIGTYPE_p_std__vectorTstd__pairTstd__string_bool_t_t.getCPtr(sv));
  }

}
