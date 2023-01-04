package edu.toronto.cs.mvset;

/**
 * MvSetMvRelation.java
 *
 *
 * Created: Thu Jun 10 22:57:42 2004
 *
 * @author <a href="mailto:arie@cs.toronto.edu">Arie Gurfinkel</a>
 * @version
 */

public class MvSetMvRelation implements MvRelation
{


  /**
   * MvSet representation of the relation
   *
   */
  MvSet reln;

  /**
   * cube of pre-state variables
   *
   */
  MvSet preVariablesCube;

  /**
   * cube of post-state variables
   *
   */
  MvSet postVariablesCube;


  /**
   * map from pre- to post-state variables
   *
   */
  int[] preToPostMap;

  /**
   * map from post- to pre-state variables
   *
   */
  int[] postToPreMap;
	

  /**
   * an invariant of the relation. The actual relation is 
   * invariant /\ reln /\ currToNext (invariant)
   *
   */
  MvSet invariant;
  

  public MvSetMvRelation (MvSet _reln, 
			  MvSet _preVariablesCube,
			  MvSet _postVariablesCube, 
			  int[] _preToPostMap,
			  int[] _postToPreMap)
  {
    this (_reln, null, _preVariablesCube, 
	  _postVariablesCube, _preToPostMap, _postToPreMap);
  }
  

  
  /**
   * Creates a new <code>MvSetMvRelation</code> instance.
   *
   * @param _reln a <code>MvSet</code> value
   * @param _invariant a <code>MvSet</code> value
   * @param _preVariablesCube a <code>MvSet</code> value
   * @param _postVariablesCube a <code>MvSet</code> value
   * @param _preToPostMap an <code>int[]</code> value
   * @param _postToPreMap an <code>int[]</code> value
   */
  public MvSetMvRelation (MvSet _reln, 
			  MvSet _invariant,
			  MvSet _preVariablesCube,
			  MvSet _postVariablesCube, 
			  int[] _preToPostMap,
			  int[] _postToPreMap)
  {
    reln = _reln;
    invariant = _invariant;
    preVariablesCube = _preVariablesCube;
    postVariablesCube = _postVariablesCube;
    preToPostMap = _preToPostMap;
    postToPreMap = _postToPreMap;
  }
  
  
  public MvSet bwdImage (MvSet v)
  {
    if (invariant != null)
      v = v.and (invariant);
    MvSet result = reln.and (v.renameArgs (preToPostMap)).
      existAbstract (postVariablesCube);
    if (invariant != null)
      result = result.and (invariant);
    return result;
  }

  public MvSet dualBwdImage (MvSet v)
  {
    return bwdImage (v.not ()).not ();
  }


  public MvSet fwdImage (MvSet v)
  {
    if (invariant != null)
      v = v.and (invariant);
    MvSet result = reln.and (v).existAbstract (preVariablesCube).
      renameArgs (postToPreMap);
    if (invariant != null)
      result = result.and (invariant);

    return result;
  }

  public MvSet toMvSet ()
  {
    return reln;
  }  
}// MvSetMvRelation
