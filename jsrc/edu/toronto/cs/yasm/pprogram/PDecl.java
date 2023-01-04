package edu.toronto.cs.yasm.pprogram;

import edu.toronto.cs.cparser.block.*;
import edu.toronto.cs.expr.Expr;
import edu.toronto.cs.tp.cvcl.ValidityChecker;
import edu.toronto.cs.yasm.abstractor.DeclsParserUtil;

public class PDecl
{
  Block sourceBlock;
  Expr sourceExpr;

  // -- reference to CVCL representation; required to prevent CVCL
  // from garbage collecting the varExpr corresponding to this declaration
  // -- use full class name for clarity
  edu.toronto.cs.tp.cvcl.Expr cvclExpr;

  public PDecl (Block _sourceBlock)
  {
    sourceBlock = _sourceBlock;
  }

  public PDecl (Expr _sourceExpr)
  {
    sourceExpr = _sourceExpr;
  }

  public Block getSourceBlock ()
  {
    return sourceBlock;
  }

  public Expr getSourceExpr ()
  {
    return sourceExpr;
  }

  /** 
   * Computes and stores the CVCL representation of this declaration.
   */
  public void computeCVCL (ValidityChecker vc)
  {
    assert (sourceBlock != null || sourceExpr != null)
      : "Both sourceBlock and sourceExpr are null";

    if (sourceBlock != null)
      cvclExpr = DeclsParserUtil.parseBlockDecl (sourceBlock, vc);

    if (sourceExpr != null)
      cvclExpr = DeclsParserUtil.parseExprDecl (sourceExpr, vc);

    if (cvclExpr == null)
      System.err.println ("computeCVCL: got a null CVCL Expr");  
  }

  public void setCVCLExpr (edu.toronto.cs.tp.cvcl.Expr _cvclExpr)
  {
    cvclExpr = _cvclExpr;
  }
}
