package edu.toronto.cs.yasm.pprogram;

import java.io.PrintWriter;

import edu.toronto.cs.cparser.block.Block;

import edu.toronto.cs.expr.*;

import edu.toronto.cs.yasm.wp.*;

public class FunctionCallPrologue extends PrllAsmtPStmt
{
  // -- call this was generated from
  FunctionCallPStmt call;

  public FunctionCallPrologue (FunctionCallPStmt _call)
  {
    super (_call.getParent (), (Expr) null);
    call = _call;
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
      out.println ("prologue (" + call.getSourceBlock ().getLineNum () + ")");
    else  
      out.println ("prologue");
  }

  public WPComputer getWPComputer ()
  {
    if (wpComputer == null)
    {
      // -- includes return index value
      Expr argList = call.getFullArgumentsExpr ();
      // -- includes return index name
      Expr paramList = call.getFunctionDef ().getFullParametersExpr ();

      /*
      System.err.println ("getWPComputer for function call: " +
                          call.getFunctionName ());
      System.err.println ("argList:\n" + argList + "\n");
      System.err.println ("#argList:\n" + argList.arity () + "\n");
      System.err.println ("paramList:\n" + paramList + "\n");
      System.err.println ("#paramList:\n" + paramList.arity () + "\n");
      */

      wpComputer = new ExprWPComputer (paramList, argList);
    }

    return wpComputer;
  }
}
