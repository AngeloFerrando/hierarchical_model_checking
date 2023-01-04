package edu.toronto.cs.yasm.wp;

import edu.toronto.cs.expr.*;
import java.util.List;

public interface WPComputer
{

  /**
   * computes the weakest pre-condition of the expr
   *
   * @param expr an <code>Expr</code> value
   * @return an <code>Expr</code> value
   */
  Expr computeWP (Expr expr);
  
  Expr[] computeWP (Expr[] expr);
  List computeWP (List expr);

}
