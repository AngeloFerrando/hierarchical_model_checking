package edu.toronto.cs.yasm.pprogram;

import edu.toronto.cs.cparser.block.*;
import edu.toronto.cs.expr.*;

import java.util.*;
import java.io.PrintWriter;


public class NDGotoPStmt extends PStmt
{
  List dests;
  List targetLabels;

  public NDGotoPStmt (PFunctionDef parent, Block sourceBlock) 
  {
    super (parent, sourceBlock);
    dests = new LinkedList ();
    targetLabels = new LinkedList ();
    if (sourceBlock != null)
      for (Block b = (Block) sourceBlock.getFirstChild (); b != null;
           b = (Block) b.getNextSibling ())
      {
        String label = b.getText ();
        targetLabels.add (label.substring (1, label.length () - 1));
      }
    
    /*
    for (Iterator it = targetLabels.iterator (); it.hasNext ();)
      System.err.println (">>> " + (String) it.next ());  
    */
  }

  public NDGotoPStmt (PFunctionDef parent, Expr sourceExpr)
  {
    super (parent, sourceExpr);
    dests = new LinkedList ();
    targetLabels = new LinkedList ();
    for (Iterator it = CILNDGotoOp.getLabels (sourceExpr).iterator ();
         it.hasNext ();)
    {
      String label = ((VariableOp) ((Expr) it.next ()).op ()).name ();
      targetLabels.add (label.substring (1, label.length () - 1));
        // strips leading and trailing quotation marks
    }
  }

  public List getDests ()
  {
    return dests;
  }

  public List getTargetLabels ()
  {
    return targetLabels;
  }

  public PStmt getDest ()
  {
    /*
    assert false :
      "NDGotoPStmt may not have a single CFG successor; use getDests instead";
    */
    
    return null;  
  }

  public void setDest (PStmt v)
  {
    assert false :
      "NDGotoPStmt may not have a single CFG successor; modify the List" +
      "returned by getDests instead";
  }

  public void printMe (PrintWriter out)
  {
    out.print ("nd-goto ");
    for (Iterator it = dests.iterator (); it.hasNext (); )
    {
      PStmt dest = (PStmt) it.next ();
      out.print (dest.getLabel () + "/" + dest.getId () + ", ");
    }

    if (sourceBlock != null)
      out.println (" (" + sourceBlock.getLineNum () + ")");
    else
      out.println (" (no source line number)");
  }
}
