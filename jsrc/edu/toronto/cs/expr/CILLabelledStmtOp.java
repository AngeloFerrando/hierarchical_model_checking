package edu.toronto.cs.expr;

public class CILLabelledStmtOp extends NamedOp
{
  // labelled statement
  // 0  var  (label)
  // 1? expr (statement)

  public static final CILLabelledStmtOp LSTMT =
    new CILLabelledStmtOp ("labelledStmt", -1);
  
  private CILLabelledStmtOp (String name, int arity)
  {
    super (name, arity);
  }

  public static String getLabel (Expr e)
  {
    verify (e);

    return e.arg (0).op ().name ();
  }

  public static Expr getStmt (Expr e)
  {
    verify (e);

    if (e.arity () >= 2)
      return e.arg (1);
    
    return null;  
  }

  private static void verify (Expr e)
  {
    assert e.op () == LSTMT : "Invalid Expr type";

    assert e.arg (0).op () instanceof VariableOp :
      "First child is not a VariableOp Expr";
  }

}
