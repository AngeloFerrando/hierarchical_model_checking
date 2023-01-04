package edu.toronto.cs.expr;

import java.util.*;

public class CILProgramOp extends NamedOp
{
  public static final CILProgramOp PROGRAM = new CILProgramOp ("program", -1);

  // -- constructor

  private CILProgramOp (String name, int arity)
  {
    super (name, arity);
  }
}
