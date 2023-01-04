package edu.toronto.cs.yasm.pprogram;

import edu.toronto.cs.mvset.*;


public class ReturnSelectorMvRelation implements MvRelation
{
  MvSet invariant;
  MvSet selector;
  MvSet selectorCube;
  
  public ReturnSelectorMvRelation (MvSet _invariant, MvSet _selector,
                                   MvSet _selectorCube)
  {
    invariant = _invariant;
    selector = _selector;
    selectorCube = _selectorCube;
  }

  public MvSet fwdImage (MvSet v)
  {
    //return selector.and (v).existAbstract (selectorCube);
    return v.cofactor (selector);
  }

  public MvSet dualBwdImage (MvSet v)
  {
    return bwdImage(v.not ()).not ();
  }

  public MvSet bwdImage (MvSet v)
  {
    return selector.and (v);
  }
  public MvSet toMvSet ()
  {
    throw new RuntimeException ("not implemented");
  }
  
  
}
