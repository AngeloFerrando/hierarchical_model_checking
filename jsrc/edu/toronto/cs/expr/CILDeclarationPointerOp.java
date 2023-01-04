package edu.toronto.cs.expr;

public class CILDeclarationPointerOp extends NullaryOperator
{
  int numPointers = 0;

  public CILDeclarationPointerOp (int _numPointers)
  {
    numPointers = _numPointers;
  }

  public String name ()
  {
    return numPointers == 1 ? "pointer" : "pointers (" + numPointers + ")";
  }
}
