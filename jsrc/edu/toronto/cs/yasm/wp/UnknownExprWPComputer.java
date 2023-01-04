package edu.toronto.cs.yasm.wp;

import edu.toronto.cs.expr.*;
import java.util.*;

public class UnknownExprWPComputer 
  extends BaseWPComputer 
  implements WPComputer
{

  Expr var;

  public UnknownExprWPComputer (Expr _var)
  {
    var = _var;
  }

  public Expr computeWP (Expr expr)
  {
    if (expr.contains (var))
      return expr.getFactory ().falseExpr ();
    
    return expr;  
  }
}

