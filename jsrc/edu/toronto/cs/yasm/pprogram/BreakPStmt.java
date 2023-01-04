package edu.toronto.cs.yasm.pprogram;

import edu.toronto.cs.cparser.block.*;
import edu.toronto.cs.expr.Expr;

public class BreakPStmt extends GotoPStmt
{
  public BreakPStmt (PFunctionDef parent, Block sourceBlock)
  {
    super (parent, sourceBlock);
  }

  public BreakPStmt (PFunctionDef parent, Expr sourceExpr)
  {
    // super (parent, sourceExpr);
  }
}

