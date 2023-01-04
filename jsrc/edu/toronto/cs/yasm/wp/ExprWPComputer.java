package edu.toronto.cs.yasm.wp;

import edu.toronto.cs.expr.*;
import java.util.*;

public class ExprWPComputer 
  extends BaseWPComputer 
  implements WPComputer
{
  Map varMap;

  // Map unknownVarMap;
  // -- vars that get assigned an expression with an unknown operator are put
  // here (mapped to themselves)

  // -- identity WP computer (maps Expr e to e)
  public static final WPComputer ID =
    new
    BaseWPComputer ()
    {
      public Expr computeWP (Expr expr)
      {
        return expr;
      }
    };

  // -- returns true if all children are non-null, false otherwise
  // -- XXX replace with recKnownOp (Expr e) when null ops are eliminated
  private boolean recNonNull (Expr e)
  {
    if (e == null) return false;

    for (Iterator it = e.args ().iterator (); it.hasNext ();)
    {
      if (!recNonNull ((Expr) it.next ())) return false;
    }

    return true;
  }

  public ExprWPComputer (Expr stmt)
  {
    varMap = new HashMap ();
    // unknownVarMap = new HashMap ();

    if (stmt.op () == CILAssignOp.ASSIGN)
    {
      // System.out.println ("Mapping " + stmt.arg (0) + " to " + stmt.arg (1));
      varMap.put (stmt.arg (0), stmt.arg (1));
    }
    /*
    else if (stmt.op () == CILListOp.SLIST)
    {
      // System.out.println ("{");
      for (Iterator it = stmt.args ().iterator (); it.hasNext ();)
      {
        Expr asmt = (Expr) it.next ();
        // System.out.println ("asmt: " + asmt);
        if (recNonNull (asmt.arg (1)))
        {
          varMap.put (asmt.arg (0), asmt.arg (1).subst (varMap));
          // System.out.println ("varMap: " + asmt.arg (0) + " -> " +
          //                    varMap.get (asmt.arg (0)));
        }
        else // RHS contains some unknown/null operator
        {
          unknownVarMap.put (asmt.arg (0), asmt.arg (0));
        }
      }
      // System.out.println ("}");
    }
    */
    else
    {
      throw new RuntimeException ("WP of " + stmt + " not supported");
    }
  }

  // -- multiple parallel assignments; PRE: lhs.arity () == rhs.arity ()
  public ExprWPComputer (Expr lhs, Expr rhs)
  {
	/* XXX suppress this for now; VARARGS workaround	  
    assert (lhs.op ().arity () == rhs.op ().arity ()) :
      lhs.op ().arity () + "!=" + rhs.op ().arity ();
    assert (lhs.arity () == rhs.arity ()) :
      "lhs " + lhs + "\nrhs " + rhs + "\narity mismatch: " + lhs.arity () + "!=" + rhs.arity ();
	*/  

    varMap = new HashMap ();

    for (int i = 0; i < lhs.arity (); i++)
    {
      // System.err.println ("Mapping " + lhs.arg (i) + " to " + rhs.arg (i) +
      // "\n");
      varMap.put (lhs.arg (i), rhs.arg (i));
    }
  }

  public Expr computeWP (Expr expr)
  {
    // -- return false as the precondition if expr contains any of the unknown
    // vars
    /*
    if (unknownVarMap != null)
    {
      for (Iterator it = unknownVarMap.keySet ().iterator (); it.hasNext ();)
      {
        if (expr.contains ((Expr) it.next ()))
        {
          return expr.getFactory ().falseExpr ();
        }
      }
    }
    */

    return expr.subst (varMap);
  }
}
