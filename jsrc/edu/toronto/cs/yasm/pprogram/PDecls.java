package edu.toronto.cs.yasm.pprogram;

import edu.toronto.cs.cparser.block.*;

public class PDecls
{
  Block sourceDecls;

  public PDecls (Block _sourceDecls)
  {
    sourceDecls = _sourceDecls;
  }

  public Block getSourceDecls ()
  {
    return sourceDecls;
  }
}
