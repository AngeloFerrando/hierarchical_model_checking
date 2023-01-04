package edu.toronto.cs.yasm.wp;

import edu.toronto.cs.expr.*;
import java.util.List;
import java.util.ArrayList;
import java.util.Iterator;

public abstract class BaseWPComputer implements WPComputer
{
  public abstract Expr computeWP (Expr expr);

  public Expr[] computeWP (Expr[] expr)
  {
    Expr[] result = new Expr [expr.length];
    for (int i = 0; i < expr.length; i++)
      result [i] = computeWP (expr [i]);
    return result;
  }

  public List computeWP (List expr)
  {
    List result = new ArrayList (expr.size ());
    for (Iterator it = expr.iterator (); it.hasNext ();)
      result.add (computeWP ((Expr) it.next ()));
    return result;
  }

}
