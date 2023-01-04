package edu.toronto.cs.expr;

public class CILBreakOp extends NamedOp
{
  public static final CILBreakOp BREAK = new CILBreakOp ("break", 0);

  private CILBreakOp (String name, int arity)
  {
    super (name, arity);
  }
}
