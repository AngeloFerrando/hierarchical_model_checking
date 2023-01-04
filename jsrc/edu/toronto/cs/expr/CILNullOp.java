package edu.toronto.cs.expr;

public class CILNullOp extends NullaryOperator
{
  public static final CILNullOp NULL = new CILNullOp ();

  private CILNullOp ()
  {
  }

  public String name ()
  {
    return "CIL_NULL";
  }
}
