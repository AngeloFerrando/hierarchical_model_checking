package edu.toronto.cs.expr;

public class CILFunctionCallOp extends NamedOp
{
  // function call
  // 0 var (function identifier)
  // 1 list (argument list)
  // 2 var (return var name)
  // 3 int (call index)

  public static final CILFunctionCallOp FCALL =
    new CILFunctionCallOp ("functionCall", -1);
  
  private CILFunctionCallOp (String name, int arity)
  {
    super (name, arity);
  }

  public static String getName (Expr e)
  {
    verify (e);

    return ((VariableOp) e.arg (0).op ()).name ();
  }

  public static boolean hasArguments (Expr e)
  {
    verify (e);

    return e.arg (1) != null;
  }

  public static Expr getArguments (Expr e)
  {
    verify (e);

    return e.arg (1);
  }

  public static String getReturnVarName (Expr e)
  {
    verify (e);

    return ((VariableOp) e.arg (2).op ()).name ();
  }

  public static int getCallIndex (Expr e)
  {
    verify (e);

    return ((Integer) ((JavaObjectOp)
              e.arg (3).op ()).getObject ()).intValue ();
  }

  public static Expr getCallIndexExpr (Expr e)
  {
    verify (e);

    return e.arg (3);
  }

  private static void verify (Expr e)
  {
    assert e.op () == FCALL;
  }
}

