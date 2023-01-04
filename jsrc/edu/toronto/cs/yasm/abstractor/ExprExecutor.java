package edu.toronto.cs.yasm.abstractor;

import edu.toronto.cs.expr.*;

public class ExprExecutor
{
  ExprFactory fac;

  public ExprExecutor (ExprFactory _fac)
  {
    fac = _fac;
  }

  public MemoryModel stmtList (Expr e, MemoryModel mm)
  {
    return mm;
  }

  public MemoryModel returnStmt (Expr e, String s, MemoryModel mm)
  {
    return mm;
  }

  public MemoryModel functionCallArgs (Expr args, Expr params, MemoryModel mm)
  {
    return mm;
  }
}

