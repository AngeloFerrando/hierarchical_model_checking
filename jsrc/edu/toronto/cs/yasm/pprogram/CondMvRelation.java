package edu.toronto.cs.yasm.pprogram;

import edu.toronto.cs.mvset.*;


/**
 * MvRelation corresponding to a conditon
 *
 * @author <a href="mailto:arie@cs.toronto.edu">Arie Gurfinkel</a>
 * @version 1.0
 */
public class CondMvRelation implements MvRelation
{
  MvSet invariant;
  MvSet cond;
  MvSet notCond;

  public CondMvRelation (MvSet _invariant, MvSet _cond)
  {
    invariant = _invariant;
    cond = _cond;
    notCond = cond.not ();
  }

  public MvSet fwdImage (MvSet v)
  {
    return cond.and (v);
  }
  public MvSet bwdImage (MvSet v)
  {
    /** 
     ** not sure if this is completelly correct. 
     ** in particular, the corner cases where v is true under 
     ** 'cond = true', and maybe under 'cond = maybe'
     **/
    return cond.and (v);
  }
  public MvSet dualBwdImage (MvSet v)
  {
    return notCond.or (v);    
  }
  
  public MvSet toMvSet ()
  {
    throw new RuntimeException ("not implemented");
  }
  
  
}
