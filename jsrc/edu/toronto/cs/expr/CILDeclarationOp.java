package edu.toronto.cs.expr;

public class CILDeclarationOp extends NamedOp
{
  // variable/record declaration
  // 0 list (type specifiers and qualifiers, can be an entire struct tree)
  // 1 0 list (pointer group)
  // 1 1 var (identifier)
  // 1 2 list (parameter list of function pointer declaration)

  public static final CILDeclarationOp DECL =
    new CILDeclarationOp ("declaration", -1);

  // -- constructor

  private CILDeclarationOp (String name, int arity)
  {
    super (name, arity);
  }

  public static boolean isNamedDeclaration (Expr e)
  {
    verify (e);

    return e.arity () > 1 && e.arg (1) != null;
  }

  public static String getDeclName (Expr e)
  {
    verify (e);

    if (isNamedDeclaration (e))
      return e.arg (1).arg (1).op ().name ();
    
    return "";
  }

  public static Expr getDeclNameExpr (Expr e)
  {
    verify (e);

    if (isNamedDeclaration (e))
      return e.arg (1).arg (1);

    return e.getFactory ().var ("");  
  }

  private static void verify (Expr e)
  {
    assert e.op () == DECL : "Invalid Expr type " + e;
  }
}
