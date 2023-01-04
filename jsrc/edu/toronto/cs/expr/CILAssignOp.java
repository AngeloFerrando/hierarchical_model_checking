package edu.toronto.cs.expr;

public class CILAssignOp extends NamedOp
{
  // assign
  // 0 expr (lhs)
  // 1 expr (rhs)

  public static final CILAssignOp ASSIGN = new CILAssignOp (":=", 2);

  private CILAssignOp (String name, int arity)
  {
    super (name, arity);
  }
}
