package edu.toronto.cs.expr;

public class CILScopeOp extends NamedOp
{
  // scope
  // 0? list (local declarations)
  // 1? list (statements)

  public static final CILScopeOp SCOPE = new CILScopeOp ("scope", -1);

  // -- constructor

  private CILScopeOp (String name, int arity)
  {
    super (name, arity);
  }

  public static Expr getLocalDeclarations (Expr e)
  {
    verify (e);

    if (e.arity () >= 1)
      return e.arg (0);
    
    return null;  
  }

  public static Expr getBody (Expr e)
  {
    verify (e);

    if (e.arity () >= 2)
      return e.arg (1);
    
    return null;  
  }

  private static void verify (Expr e)
  {
    assert e.op () == SCOPE : "Invalid Expr type";
  }
}
