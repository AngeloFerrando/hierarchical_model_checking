package edu.toronto.cs.expr;

import java.util.*;

public class CILFunctionDefOp extends NamedOp
{
  // functionDef
  // 0   list (return type specifiers)
  // 1   list (signature)
  // 1 0 list (pointer group)
  // 1 1 var  (function name)
  // 1 2 list (parameter declarations)
  // 2   scope
  // 3   intExpr (number of call sites)

  public static final CILFunctionDefOp FUNCTION_DEF =
    new CILFunctionDefOp ("functionDef", -1);

  // -- constructor

  private CILFunctionDefOp (String name, int arity)
  {
    super (name, arity);
  }

  // -- accessor methods

  public static Expr getReturnType (Expr e)
  {
    verify (e);

    return e.arg (0);
  }

  public static String getFunctionName (Expr e)
  {
    verify (e);

    return getFunctionNameFromSig (e.arg (1));
  }

  public static String getFunctionNameFromSig (Expr e)
  {
    return ((VariableOp) e.arg (1).op ()).name ();
  }

  public static Expr getParameterDecls (Expr e)
  {
    verify (e);

    return e.arg (1).arg (2);
  }

  /** 
   * @param e a FUNCTION_DEF Expr
   * @return true if the function has parameters (other than void)
   */
  public static boolean hasParameters (Expr e)
  {
    verify (e);

    // System.err.println ("hasParameters: " + e);

    return
      // declaration list is not null AND
      // list has at least one decl AND
      // first decl has a declarator (void declaration does not)
      (e.arg (1).arg (2) != null) &&
      (e.arg (1).arg (2).arg (0) != null) &&  
      (e.arg (1).arg (2).arg (0).arg (1) != null);
  }

  public static Expr getParameterNames (Expr e)
  {
    verify (e);

    List paramNames = new LinkedList ();

    for (Iterator it = getParameterDecls (e).args ().iterator ();
         it.hasNext ();)
      paramNames.add (CILDeclarationOp.getDeclNameExpr ((Expr) it.next ()));
    
    // System.err.println (">>> paramNames: " + paramNames);

    return e.getFactory ().op (CILListOp.LIST).naryApply (paramNames);
  }

  public static Expr getLocalDecls (Expr e)
  {
    verify (e);

    return e.arg (2).arg (0);
  }

  public static Expr getBody (Expr e)
  {
    verify (e);

    return e.arg (2);
  }

  public static int getNumCallSites (Expr e)
  {
    verify (e);

    return ((Integer) ((JavaObjectOp)
              e.arg (3).op ()).getObject ()).intValue ();
  }

  public static boolean isNonVoid (Expr e)
  {
    verify (e);

    // unless we find a non-void type specifier below, the function is
    // deemed void
    boolean isNonVoid = false;

    // -- search through type specifiers/qualifiers for a non-void
    // type specifier
    for (Iterator it = e.arg (0).args ().iterator (); it.hasNext ();)
    {
      Expr curExpr = (Expr) it.next ();

      if (curExpr.op () instanceof CILTypeSpecifierOp)
        if (curExpr.op () != CILTypeSpecifierOp.VOID)
          isNonVoid = true;
    }

    return isNonVoid;
  }

  private static void verify (Expr e)
  {
    assert e.op () == FUNCTION_DEF : "Invalid Expr type";
  }
}
