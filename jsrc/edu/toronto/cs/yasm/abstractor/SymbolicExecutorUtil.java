package edu.toronto.cs.yasm.abstractor;

import edu.toronto.cs.cparser.block.*;

import edu.toronto.cs.expr.*;

import java.io.*;

import antlr.*;

public class SymbolicExecutorUtil
{
  static SymbolicExecutor cachedSymbolicExecutor;
  static ExprFactory cachedFac;

  public static SymbolicExecutor newSymbolicExecutor (ExprFactory fac)
  {
    if (fac == cachedFac && cachedSymbolicExecutor != null)
      return cachedSymbolicExecutor;

    SymbolicExecutor symbolicExecutor = new SymbolicExecutor ();
    symbolicExecutor.setASTNodeClass (Block.class.getName ());
    Block.setTokenVocabulary
      (edu.toronto.cs.cparser.CILTokenTypes.class.getName ());
    symbolicExecutor.setExprFactory (fac);
    cachedSymbolicExecutor = symbolicExecutor;
    return symbolicExecutor;  
  }

  public static ExprExecutor newExprExecutor (ExprFactory fac)
  {
    return null;
  }

  public static Expr parseBlockExpr (Block b, ExprFactory fac)
  {
    try
    {
      return newSymbolicExecutor (fac).expr (b, new MemoryModel ());
    }
    catch (RecognitionException ex)
    {
      return null;
    }
  }
}

