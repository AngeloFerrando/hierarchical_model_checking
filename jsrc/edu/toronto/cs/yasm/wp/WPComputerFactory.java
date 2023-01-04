package edu.toronto.cs.yasm.wp;

import edu.toronto.cs.expr.*;

import java.util.Iterator;

public class WPComputerFactory
{
  public static WPComputer wp (Expr e)
  {
    if (e.op () == CILAssignOp.ASSIGN)
    {
      if (recNonNull (e))
        return new ExprWPComputer (e);
      
      return unknown (e);  
    }
    
    if (e.op () == CILListOp.SLIST)
      return new DCWPComputer (e);  
    
    throw new
      RuntimeException ("Don't know how to make WP computer for: " + e);  
  }

  public static WPComputer prllAsmt (Expr lhs, Expr rhs)
  {
    return new ExprWPComputer (lhs, rhs);
  }

  public static WPComputer unknown (Expr e)
  {
    return new UnknownExprWPComputer (e);
  }

  public static WPComputer id ()
  {
    return ExprWPComputer.ID;
  }

  // -- returns true if all children are non-null, false otherwise
  // -- XXX replace with recKnownOp (Expr e) when null ops are eliminated
  private static boolean recNonNull (Expr e)
  {
    if (e == null) return false;

    for (Iterator it = e.args ().iterator (); it.hasNext ();)
    {
      if (!recNonNull ((Expr) it.next ())) return false;
    }

    return true;
  }

}
