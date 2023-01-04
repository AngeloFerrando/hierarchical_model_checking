package edu.toronto.cs.yasm.wp;

import edu.toronto.cs.yasm.abstractor.MemoryModel;
import edu.toronto.cs.expr.*;

public class MemoryModelWPComputer 
  extends BaseWPComputer 
  implements WPComputer
{

  MemoryModel mmModel;

  public MemoryModelWPComputer (MemoryModel _mmModel)
  {
    mmModel = _mmModel;
  }

  public Expr computeWP (Expr expr)
  {
    return expr.subst (mmModel);
  }
}
