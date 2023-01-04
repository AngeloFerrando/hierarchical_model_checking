package edu.toronto.cs.yasm.pprogram;

import edu.toronto.cs.expr.*;

import java.util.*;
import java.io.PrintWriter;

public class ReturnSelectorPStmt extends PStmt
{
  List dests;
  PredicateTable.IntVariable returnIndex;

  public ReturnSelectorPStmt (PFunctionDef parent)
  {
    super (parent, (Expr) null);
    dests = new ArrayList (parent.getNumCallSites ());
    returnIndex = getPProgram ().getPredTable ().newIntVar
      (exprFac ().var (parent.getFunctionName () + "::@return_index"),
       parent.getNumCallSites ());
  }

  public PredicateTable.IntVariable getReturnIndex ()
  {
    return returnIndex;
  }

  public List getTargetLabels ()
  {
    List labels = new ArrayList (parent.getNumCallSites ());

    for (int i = 0; i < parent.getNumCallSites (); i++)
      labels.add (parent.callSiteName (i));

    return labels;  
  }

  public List getDests ()
  {
    return dests;
  }

  public void printMe (PrintWriter out)
  {
    out.println ("Return " + getTargetLabels ());
  }
}
