package edu.toronto.cs.yasm.pprogram;

import edu.toronto.cs.cparser.block.*;
import edu.toronto.cs.expr.*;
import java.io.PrintWriter;

/**
 * GotoPStmt.java
 *
 *
 * Created: Fri Jun 25 14:22:38 2004
 *
 * @author <a href="mailto:kelvin@tallinn.cs">Kelvin Ku</a>
 * @version 1.0
 */
public class GotoPStmt extends PStmt
{
  String targetLabel = null;

  public GotoPStmt ()
  {
  }

  public GotoPStmt (PFunctionDef parent, Block sourceBlock) 
  {
    super (parent, sourceBlock);
    if (sourceBlock != null)
      targetLabel = sourceBlock.getGotoTargetLabel ();
  } 

  public GotoPStmt (PFunctionDef parent, Expr sourceExpr) 
  {
    super (parent, sourceExpr);
    if (sourceExpr != null)
    {
      setTargetLabel(CILGotoOp.getTargetLabel (sourceExpr));
      assert targetLabel != null && !targetLabel.equals("");
    }
  } 

  public GotoPStmt (PStmt _dest)
  {
    setDest (_dest);
  }

  public GotoPStmt (String _targetLabel)
  {
    setTargetLabel (_targetLabel);
  }

  public int getLineNum ()
  {
    Expr src = getSourceExpr ();
    if (src == null) return super.getLineNum ();
    
    return 
      ((Integer)((JavaObjectOp)src.arg (1).op ()).getObject ()).intValue ();
  }
  
  public String getTargetLabel ()
  {
    if (targetLabel == null)
      if (dest != null)
        return dest.getLabel ();

    return targetLabel;
  }

  public void setTargetLabel (String _targetLabel)
  {
    targetLabel = _targetLabel;
    // System.err.println
    //  ("Making GotoPStmt with targetLabel '" + targetLabel + "'");
  }

  public PStmt getDest ()
  {
    return dest;
  }

  public void setDest (PStmt v)
  {
    if (v != null)
      targetLabel = v.getLabel ();

    /* DOESN'T WORK --- logues not available when this is called
       if (v instanceof FunctionCallPStmt)
       {
       FunctionCallPStmt call = (FunctionCallPStmt) v;
       dest = call.getLogues ();
       }
       else
     */
    if (!(v instanceof FunctionCallPStmt))
      super.setDest (v);
  }

  public void printMe (PrintWriter out)
  {
    /*
       if (dest == null)
       out.println ("goto <unknown>");
       else
       out.println ("goto " + dest.getLabel ());
     */
    if (dest != null)
    {
      out.print ("goto ");

      if (dest.getLabel () != null)
        out.print (dest.getLabel ());
      else if (dest.getId () != -1)
        out.print ("l" + dest.getId ());
      else
        out.print ("<" + dest.getClass () + ">");  
    }
    else
      out.print ("goto <null>");

    if (sourceBlock != null)
      out.println (" (" + sourceBlock.getLineNum () + ")");
    else
      out.println (" (no source line number)");

    // XXX do sourceExpr case  
  }
}

