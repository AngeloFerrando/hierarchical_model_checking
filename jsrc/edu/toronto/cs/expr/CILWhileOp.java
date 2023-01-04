package edu.toronto.cs.expr;

public class CILWhileOp extends NamedOp
{
  public static final CILWhileOp WHILE = new CILWhileOp ("while", -1);

  private CILWhileOp (String name, int arity)
  {
    super (name, arity);
  }

  public static Expr getCond (Expr e)
  {
    verify (e);

    return e.arg (0);
  }

  public static Expr getBody (Expr e)
  {
    verify (e);

    return e.arg (1);
  }

  private static void verify (Expr e)
  {
    assert e.op () == WHILE : "Invalid Expr type (" + e.op () + ")";
  }
}
