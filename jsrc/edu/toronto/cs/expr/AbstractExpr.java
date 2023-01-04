package edu.toronto.cs.expr;

import java.util.List;


public abstract class AbstractExpr implements Expr
{
  public Expr unaryApply (Expr expr)
  {
    return naryApply (new Expr[] {expr});
  }

  public Expr binApply (Expr expr1, Expr expr2)
  {
    return naryApply (new Expr[] {expr1, expr2});
  }
  public Expr naryApply (List expr)
  {
    return naryApply ((Expr[]) expr.toArray (new Expr [expr.size ()]));
  }

  public boolean contains (Expr e)
  {
    if (this == e)
      return true;

    for (int i = 0; i < arity (); i++)  
      if (arg (i) != null && arg (i).contains (e))
        return true;

    return false;  
  }
}
