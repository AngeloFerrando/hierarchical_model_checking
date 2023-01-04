package edu.toronto.cs.yasm.pprogram;

import edu.toronto.cs.cparser.*;
import edu.toronto.cs.cparser.block.*;

import edu.toronto.cs.expr.Expr;

import java.io.PrintWriter;



/**
 * SkipPStmt.java
 *
 *
 * Created: Fri Jun 25 14:21:32 2004
 *
 * @author <a href="mailto:kelvin@tallinn.cs">Kelvin Ku</a>
 * @version 1.0
 */
public class SkipPStmt extends PStmt
{
  public SkipPStmt (PFunctionDef parent, Block sourceBlock) 
  {
    super (parent, sourceBlock);
  }

  public SkipPStmt (PFunctionDef parent, Expr sourceExpr)
  {
    super (parent, sourceExpr);
  }

  public void printMe (PrintWriter out)
  {
    if (sourceBlock != null)
      out.println ("skip" + " (" + sourceBlock.getLineNum () + ")");
    else
      out.println ("skip (no-line-number)");

    // XXX add sourceExpr case  
  }
  
}
