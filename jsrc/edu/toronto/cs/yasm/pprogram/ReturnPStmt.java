package edu.toronto.cs.yasm.pprogram;

import edu.toronto.cs.cparser.block.Block;
import java.io.*;
import edu.toronto.cs.yasm.abstractor.*;
import edu.toronto.cs.expr.*;
import antlr.RecognitionException;

import edu.toronto.cs.yasm.wp.*;

public class ReturnPStmt extends PrllAsmtPStmt
{
  public ReturnPStmt (PFunctionDef parent, Block sourceBlock)
  {
    super (parent, sourceBlock);
  }

  public ReturnPStmt (PFunctionDef parent, Expr sourceExpr)
  {
    super (parent, sourceExpr);
  }

  public void printMe (PrintWriter out)
  {
    /*
    if (sourceBlock != null)
      out.println ("return (" + sourceBlock.getLineNum () + ")");
    else
      out.println ("return (no-line-number)");
    */

    /* do sourceExpr case */  
    out.println ("(" + parent.getFunctionName () + ") " + sourceExpr);
  }

  public MemoryModel symExec (ExprFactory fac)
  {
    if (sourceBlock != null)
      try
      {
      return SymbolicExecutorUtil.newSymbolicExecutor (fac).returnStmt
        (sourceBlock,
         getParent ().getFunctionName () + "::@return_value",
         new MemoryModel ());
      }
      catch (RecognitionException ex)
      {
        return null;
      }
        
   if (sourceExpr != null)
       return SymbolicExecutorUtil.newExprExecutor (fac).returnStmt
        (sourceExpr,
         getParent ().getFunctionName () + "::@return_value",
         new MemoryModel ());

   assert false : "Both sourceBlock and sourceExpr are null!";  

   return null;
  }

  public WPComputer getWPComputer ()
  {
    if (sourceExpr != null && wpComputer == null)
    {
      if (CILReturnOp.hasReturnValue (sourceExpr))
      {
        Expr returnExpr;
        ExprFactory fac = getParent ().getParent ().getExprFactory ();
        returnExpr = fac.op (CILAssignOp.ASSIGN).binApply (
            fac.var (getParent ().getFunctionName () + "::@return_value"),
            CILReturnOp.getReturnValue (sourceExpr));
        wpComputer = new ExprWPComputer (returnExpr);
      }
      else
      {
        wpComputer = ExprWPComputer.ID;
      }
    }
    return wpComputer;
  }
  public int getLineNum ()
  {
    Expr src = getSourceExpr ();
    if (src == null) return super.getLineNum ();
    if (src.arity () != 2)
      {
	System.err.println ("Strange assignment without a line number");
	System.err.println ("Srouce expr: " + src);
	return super.getLineNum ();
      }
    
    
    return 
      ((Integer)((JavaObjectOp)src.arg (1).op ()).getObject ()).intValue ();
  }

}

