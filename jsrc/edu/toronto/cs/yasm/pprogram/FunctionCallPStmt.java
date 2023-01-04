package edu.toronto.cs.yasm.pprogram;

import edu.toronto.cs.cparser.block.Block;
import java.io.*;
import edu.toronto.cs.expr.*;
import edu.toronto.cs.yasm.abstractor.*;
import antlr.RecognitionException;
import java.util.*;

public class FunctionCallPStmt extends PStmt
{
  // -- identifies this call statement
  int callIndex;

  // -- prologue and epilogue (linked by next)
  PStmt logues;

  // -- definition of function this statement is calling
  PFunctionDef functionDef;

  // -- variable where return value might be stored
  Expr returnVarExpr;

  public FunctionCallPStmt (PFunctionDef parent, Block sourceBlock)
  {
    super (parent, sourceBlock);
    callIndex = sourceBlock.getCallIndex ();
  }

  public FunctionCallPStmt (PFunctionDef parent, Expr sourceExpr)
  {
    this (parent, sourceExpr, null);
  }

  public FunctionCallPStmt (PFunctionDef parent, Expr callExpr,
                            Expr _returnVarExpr)
  {
    super (parent, callExpr);
    callIndex = CILFunctionCallOp.getCallIndex (callExpr);
    returnVarExpr = _returnVarExpr;
  }

  public String getFunctionName ()
  {
    if (sourceBlock != null)
      return sourceBlock.getFunctionCallName ();

    if (sourceExpr != null)
      return CILFunctionCallOp.getName (sourceExpr);

    return null;
  }

  public String getResultVarName ()
  {
    if (sourceBlock != null)
      return sourceBlock.getFunctionCallReturnVarName ();
    
    return null;
  }

  public Expr getReturnVar ()
  {
    if (sourceExpr != null)
      return returnVarExpr;

    return null;  
  }

  public Block getArgumentsBlock ()
  {
    return sourceBlock.getFunctionCallArgs ();
  }

  public Expr getArgumentsExpr ()
  {
    return CILFunctionCallOp.getArguments (sourceExpr);  
  }

  /** 
   * Returns a CILListOp.LIST Expr beginning with the return index value,
   * followed by the argument values.
   *
   * @return 
   */
  public Expr getFullArgumentsExpr ()
  {
    List args = new LinkedList ();
    args.add (CILFunctionCallOp.getCallIndexExpr (sourceExpr));
    if (CILFunctionCallOp.hasArguments (sourceExpr))
      args.addAll (CILFunctionCallOp.getArguments (sourceExpr).args ());
    //System.err.println (getFunctionName () + " call has #args: " + args.size
    //());  
    return getParent ().getParent ().getExprFactory ().
           op (CILListOp.LIST).naryApply (args);
  }

  public int getCallIndex ()
  {
    return callIndex;
  }

  public void setLogues (PStmt _logues)
  {
    logues = _logues;
  }

  public PStmt getLogues ()
  {
    return logues;
  }

  public void setFunctionDef (PFunctionDef v)
  {
    functionDef = v;
  }

  public PFunctionDef getFunctionDef ()
  {
    return functionDef;
  }

  public void printMe (PrintWriter out)
  {
    out.println ("function-call " + sourceExpr);
  }

  /*
  public MemoryModel symExec (ExprFactory fac)
  {
    MemoryModel mm = new MemoryModel ();

    // -- argument assignment
    if (sourceBlock != null)
      try
      {
        mm = SymbolicExecutorUtil.newSymbolicExecutor (fac).functionCallArgs
          (getArgumentsBlock (),
           getFunctionDef ().getParameterList (),
           mm);
      }
      catch (RecognitionException ex)
      {
        return null;
      }

    if (sourceExpr != null)
      try
      {
        mm = SymbolicExecutorUtil.newExprExecutor (fac).functionCallArgs
          (getArgumentsExpr (),
           getFunctionDef ().getParameterList (),
           mm);
      }
      catch (RecognitionException ex)
      {
        return null;
      }

      // -- call site index assignment
      mm.assign
        (fac.var (getFunctionName () + "::@return_index"),
         fac.intExpr (getCallIndex ()));
           
      return mm;     
  }
  */

  public int getLineNum ()
  {
    Expr src = getSourceExpr ();
    if (src == null) return super.getLineNum ();
    if (src.arity () != 5)
      {
	System.err.println ("Strange function call without a line number");
	System.err.println ("Srouce expr: " + src);
	return super.getLineNum ();
      }
    
    
    return 
      ((Integer)((JavaObjectOp)src.arg (4).op ()).getObject ()).intValue ();
  }

}

