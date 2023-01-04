package edu.toronto.cs.expr;

import java.util.List;

public class CILNDGotoOp extends NamedOp
{
  // ndgoto
  // 0 list (goto labels)

  public static final CILNDGotoOp NDGOTO =
    new CILNDGotoOp ("NDGoto", -1);

  private CILNDGotoOp (String name, int arity)
  {
    super (name, arity);
  }

  public static List getLabels (Expr e)
  {
    return e.arg (0).args ();
  }
}
