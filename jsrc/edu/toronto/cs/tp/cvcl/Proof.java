/* ----------------------------------------------------------------------------
 * This file was automatically generated by SWIG (http://www.swig.org).
 * Version: 1.3.21
 *
 * Do not make changes to this file unless you know what you are doing--modify
 * the SWIG interface file instead.
 * ----------------------------------------------------------------------------- */

package edu.toronto.cs.tp.cvcl;


public class Proof {
  private long swigCPtr;
  protected boolean swigCMemOwn;

  protected Proof(long cPtr, boolean cMemoryOwn) {
    swigCMemOwn = cMemoryOwn;
    swigCPtr = cPtr;
  }

  protected Proof() {
    this(0, false);
  }

  public void delete() {
    if(swigCPtr != 0 && swigCMemOwn) {
      swigCMemOwn = false;
      throw new UnsupportedOperationException("C++ destructor does not have public access");
    }
    swigCPtr = 0;
  }

  protected static long getCPtr(Proof obj) {
    return (obj == null) ? 0 : obj.swigCPtr;
  }

  public String toString() {
    return JavaCVCJNI.Proof_toString(swigCPtr);
  }

}
