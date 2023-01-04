package edu.toronto.cs.expr;

public class CILGotoOp extends NamedOp
{
  // goto
  // 0 var (target label)

  public static final CILGotoOp GOTO = new CILGotoOp ("goto", 1);
 
  private CILGotoOp (String name, int arity)
  {
    super (name, arity);
  }

  public static String getTargetLabel (Expr e)
  {
    assert e.op () == GOTO : "Invalid Expr type (" + e.op () + ")";

    return ((VariableOp) e.arg (0).op ()).name ();
  }
}

