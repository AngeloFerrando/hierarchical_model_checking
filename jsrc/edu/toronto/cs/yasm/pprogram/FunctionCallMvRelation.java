package edu.toronto.cs.yasm.pprogram;

import edu.toronto.cs.mvset.*;

import java.util.Arrays;


public class FunctionCallMvRelation implements MvRelation
{
  MvSet invariant;
  MvSet selector;
  MvSet selectorCube;
  MvRelation args;
  
  public FunctionCallMvRelation (MvSet _invariant, MvSet _selector,
                                 MvSet _selectorCube, MvRelation _args)
  {
    invariant = _invariant;
    selector = _selector;
    selectorCube = _selectorCube;
    args = _args;
  }

  public MvSet fwdImage (MvSet v)
  {
    if (v == v.getFactory ().bot ())
      return v;
    
    return selector.and (args.fwdImage (v));
  }
  public MvSet dualBwdImage (MvSet v)
  {
    return bwdImage(v.not ()).not ();
  }

  public MvSet bwdImage (MvSet v)
  {
    if (v.equals (v.getFactory ().bot ()))
      return v;

    //return args.bwdImage (selector.and (v).existAbstract (selectorCube));
    return args.bwdImage (v.cofactor (selector));
    
  }
  public MvSet toMvSet ()
  {
    throw new RuntimeException ("not implemented");
  }
  
  
}
