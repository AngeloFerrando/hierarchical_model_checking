package edu.toronto.cs.expr;

public class CILEnumOp extends NamedOp
{

  public static final CILEnumOp ENUM = new CILEnumOp ("enum", -1);

  // -- constructor
  private CILEnumOp (String name, int arity)
  {
    super (name, arity);
  }
}
