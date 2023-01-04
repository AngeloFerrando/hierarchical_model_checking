package edu.toronto.cs.expr;

public class CILUnknownOp extends NamedOp
{
  public static final CILUnknownOp UNKNOWN = new CILUnknownOp ("unknown", -1);

  private CILUnknownOp (String name, int arity)
  {
    super (name, arity);
  }
}
