package edu.toronto.cs.yasm.abstractor;

import edu.toronto.cs.cparser.block.*;
import edu.toronto.cs.yasm.pprogram.*;

public interface Abstractor
{
  public PProgram doProgramAbstract (Block block);
}
