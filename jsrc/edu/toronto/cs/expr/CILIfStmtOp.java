package edu.toronto.cs.expr;

public class CILIfStmtOp extends NamedOp
{
  // if-then-else
  // 0 condition
  // 1 then
  // 2? else

  public static final CILIfStmtOp IF_THEN_ELSE =
    new CILIfStmtOp ("ifThenElse", -1);

  private CILIfStmtOp (String name, int arity)
  {
    super (name, arity);
  }

  public static Expr getCond (Expr e)
  {
    verify (e);

    return e.arg (0);
  }

  public static Expr getThen (Expr e)
  {
    verify (e);

    return e.arg (1);
  }

  public static Expr getElse (Expr e)
  {
    verify (e);

    if (e.arity () >= 3)
      return e.arg (2);
    
    return null;  
  }

  private static void verify (Expr e)
  {
    assert e.op () == IF_THEN_ELSE : "Invalid Expr type";
  }
}
