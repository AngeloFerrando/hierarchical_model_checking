package edu.toronto.cs.yasm.pprogram;

import edu.toronto.cs.mvset.*;


public class SkipMvRelation implements MvRelation
{
  MvSet invariant;
  
  public SkipMvRelation (MvSet _invariant)
  {
    invariant = _invariant;
  }

  public MvSet fwdImage (MvSet v)
  {
    return v;
  }
  public MvSet dualBwdImage (MvSet v)
  {
    return bwdImage(v.not ()).not ();
  }

  public MvSet bwdImage (MvSet v)
  {
    return v;
  }
  public MvSet toMvSet ()
  {
    throw new RuntimeException ("not implemented");
  }
  
  
}
