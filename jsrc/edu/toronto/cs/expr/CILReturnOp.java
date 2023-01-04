package edu.toronto.cs.expr;

public class CILReturnOp extends NamedOp
{
  // return
  // 0? expr (return value)

  public static final CILReturnOp RETURN = new CILReturnOp ("return", 1);

  private CILReturnOp (String name, int arity)
  {
    super (name, arity);
  }

  public static boolean hasReturnValue (Expr e)
  {
    return e.arity () >= 1;
  }

  public static Expr getReturnValue (Expr e)
  {
    return e.arg (0);
  }
}

