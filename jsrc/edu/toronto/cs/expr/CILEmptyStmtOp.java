package edu.toronto.cs.expr;

public class CILEmptyStmtOp extends NullaryOperator
{
  public static CILEmptyStmtOp EMPTY = new CILEmptyStmtOp ();

  private CILEmptyStmtOp ()
  {
  }

  public String name ()
  {
    return "emptyStmt";
  }
}
