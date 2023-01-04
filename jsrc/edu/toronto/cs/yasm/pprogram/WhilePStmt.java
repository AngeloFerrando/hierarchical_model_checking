package edu.toronto.cs.yasm.pprogram;

import edu.toronto.cs.expr.*;

import edu.toronto.cs.cparser.block.*;

import java.io.PrintWriter;


public class WhilePStmt extends IfPStmt
{
    GotoPStmt headGoto;

    public WhilePStmt (PFunctionDef def, Block sourceBlock, PCond cond,
                       PStmt whileBody)
    {
        super (def, sourceBlock, cond, whileBody, null);
        headGoto = new GotoPStmt (this);
        whileBody.setTail (headGoto); 
    }

    public WhilePStmt (PFunctionDef def, Expr sourceExpr, PCond cond,
                       PStmt whileBody)
    {
        super (def, sourceExpr, cond, whileBody, null);
        headGoto = new GotoPStmt (this);
        whileBody.setTail (headGoto); 
    }


    public GotoPStmt getHeadGoto ()
    {
      return headGoto;
    }

    public void printMe (PrintWriter out)
    {
      if (sourceBlock != null)
        out.println ("while (" + cond + ") " + thenGoto + " else " + elseGoto +
                     " (" + sourceBlock.getLineNum () + ")");
      /*
      out.println
        ("while " + cond + " (" + sourceBlock.getLineNum () + ")");
      thenGoto.print (out);  
      out.println ("else");
      elseGoto.print (out);
      thenStmt.print (out);
      headGoto.print (out);
      */

      // XXX do sourceExpr case
    }

  public int getLineNum ()
  {
    Expr src = getSourceExpr ();
    if (src == null) return super.getLineNum ();
    
    return 
      ((Integer)((JavaObjectOp)src.arg (2).op ()).getObject ()).intValue ();
  }

}
