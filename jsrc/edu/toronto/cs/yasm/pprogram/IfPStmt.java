package edu.toronto.cs.yasm.pprogram;

import edu.toronto.cs.expr.*;

import edu.toronto.cs.cparser.block.*;

import java.io.PrintWriter;


/**
 * IfPStmt.java
 *
 *
 * Created: Fri Jun 25 14:25:25 2004
 *
 * @author <a href="mailto:kelvin@tallinn.cs">Kelvin Ku</a>
 * @version 1.0
 */
public class IfPStmt extends PStmt
{
  GotoPStmt thenGoto;
  GotoPStmt exitGoto;
  GotoPStmt elseGoto;

  PStmt thenStmt;
  PStmt elseStmt;

  PCond cond;

  public IfPStmt (PFunctionDef parent, Block sourceBlock, PCond _cond,
                  PStmt _thenStmt, PStmt _elseStmt)
  {
    super (parent, sourceBlock);

    cond = _cond;
    thenStmt = _thenStmt;

    thenGoto = new GotoPStmt (thenStmt);
    elseStmt = _elseStmt;
    elseGoto = new GotoPStmt (elseStmt);
    exitGoto = new GotoPStmt ();
  }

  public IfPStmt (PFunctionDef parent, Expr sourceExpr, PCond _cond,
                  PStmt _thenStmt, PStmt _elseStmt)
  {
    super (parent, sourceExpr);

    cond = _cond;
    thenStmt = _thenStmt;

    thenGoto = new GotoPStmt (thenStmt);
    elseStmt = _elseStmt;
    elseGoto = new GotoPStmt (elseStmt);
    exitGoto = new GotoPStmt ();
  }

  public PStmt getThenStmt ()
  {
    return thenStmt;
  }

  public void setThenStmt (PStmt _thenStmt)
  {
    thenStmt = _thenStmt;
  }

  public PStmt getElseStmt ()
  {
    return elseStmt;
  }

  public void setElseStmt (PStmt _elseStmt)
  {
    elseStmt = _elseStmt;
  }

  public PCond getCond ()
  {
    return cond;
  }

  public void setCond (PCond _cond)
  {
    cond = _cond;
  }

  public GotoPStmt getThenGoto ()
  {
    return thenGoto;
  }

  public GotoPStmt getElseGoto ()
  {
    return elseGoto;
  }

  public GotoPStmt getExitGoto ()
  {
    return exitGoto;
  }

  // Assumes branch targets have been patched and labelled
  public void printMe (PrintWriter out)
  {
    out.print ("if (" + cond + ") goto " + thenGoto.getTargetLabel () +
      " else goto " + elseGoto.getTargetLabel ());

    if (sourceBlock != null)
      out.println (" (" + sourceBlock.getLineNum () + ")");
    else
      out.println ();

    /*
    if (sourceBlock != null)
      out.println ("if " + cond + " (" + sourceBlock.getLineNum () + ")");
    else
      out.println ("if " + cond + " (?)");  
    thenGoto.print (out);
    out.println ("else");
    elseGoto.print (out);
    thenStmt.print (out);

    if (elseStmt != null)
      {
	exitGoto.print (out);
	elseStmt.print (out);
      }
    */

    // XXX do sourceExpr case
  }

  public int getLineNum ()
  {
    Expr src = getSourceExpr ();
    if (src == null) return super.getLineNum ();
    
    return 
      ((Integer)((JavaObjectOp)src.arg (3).op ()).getObject ()).intValue ();
  }
  
}
