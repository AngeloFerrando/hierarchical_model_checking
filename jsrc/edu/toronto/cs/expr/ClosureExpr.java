package edu.toronto.cs.expr;

import java.util.List;
import java.util.Map;
import java.util.HashMap;
import java.util.Collections;


public class ClosureExpr extends AbstractExpr
{
  // -- parameteres
  Expr[] params;
  // -- the body
  Expr body;
  
  public static Operator CLOSURE = new NamedOp ("CLOSURE", 0) {};
  
  public ClosureExpr (Expr _body, Expr[] _params)
  {
    body = _body;
    params = _params;
  }
  
  public Expr getBody ()
  {
    return body;
  }
  public Expr[] getParams ()
  {
    return params;
  }
  
  
  public Operator op ()
  {
    return CLOSURE;
  }

  public Expr naryApply (Expr[] args)
  {
    if (params.length != args.length)
      throw 
	new IllegalArgumentException ("Applying closure with " + 
				       params.length + " parameters " + 
				       " to " + args.length + " arguments");
    
    Map subMap = new HashMap ();
    for (int i = 0; i < params.length; i++)
      subMap.put (params [i], args [i]);

    return body.subst (subMap);
  }
  
  public int arity ()
  {
    return 0;
  }

  public Expr arg (int i)
  {
    throw new IllegalArgumentException ("Closure has no children");
  }
  
  public List args ()
  {
    return Collections.EMPTY_LIST;
  }
  public boolean isWellFormed ()
  {
    if (!body.isWellFormed ()) return false;
    
    for (int i = 0; i < params.length; i++)
      if (!params [i].isWellFormed ())
	return false;

    return true;
  }
  
  public Expr subst (Map subMap)
  {
    Expr[] newParams = new Expr [params.length];
    for (int i = 0; i < newParams.length; i++)
      newParams [i] = params [i].subst (subMap);
    
    return new ClosureExpr (body.subst (subMap), newParams);
  }
  
  public Expr substOp (Map subMap)
  {
    Expr[] newParams = new Expr [params.length];
    for (int i = 0; i < newParams.length; i++)
      newParams [i] = params [i].substOp (subMap);
    
    return new ClosureExpr (body.substOp (subMap), newParams);
  }

  public ExprFactory getFactory ()
  {
    return body.getFactory ();
  }
  
}
