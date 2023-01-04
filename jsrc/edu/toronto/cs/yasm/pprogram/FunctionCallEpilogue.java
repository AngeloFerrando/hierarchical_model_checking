package edu.toronto.cs.yasm.pprogram;

import java.io.PrintWriter;

import edu.toronto.cs.cparser.block.Block;

import edu.toronto.cs.expr.*;

import edu.toronto.cs.yasm.wp.*;

public class FunctionCallEpilogue extends PrllAsmtPStmt
{
  // -- call this was generated from
  FunctionCallPStmt call;

  public FunctionCallEpilogue (FunctionCallPStmt _call)
  {
    super (_call.getParent (), (Expr) null);
    call = _call;
  }

  /*
  public void setLabel (String label)
  {
    System.err.println (">>> FunctionCallEpilogue " + label);
    super.setLabel (label);
  }
  */

  public WPComputer getWPComputer ()
  {
    if (wpComputer == null)
    {
      ExprFactory fac = getParent ().getParent ().getExprFactory ();
      Expr lhs = call.getReturnVar ();
      Expr rhs = fac.var (call.getFunctionName () + "::@return_value");
      wpComputer = new ExprWPComputer
        (fac.op (CILAssignOp.ASSIGN).binApply (lhs, rhs));
    }

    return wpComputer;
  }

  public FunctionCallPStmt getCall ()
  {
    return call;
  }

  public int getLineNum ()
  {
    return getCall ().getLineNum ();
  }
  

  public void printMe (PrintWriter out)
  {
    if (getSourceBlock () != null)
      out.println ("epilogue (" + call.getSourceBlock ().getLineNum () + ")");
    else  
      out.println ("epilogue");
  }
}
