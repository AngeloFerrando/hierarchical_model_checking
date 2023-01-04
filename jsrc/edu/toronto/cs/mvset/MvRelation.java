package edu.toronto.cs.mvset;

/**
 * MvRelation.java
 *
 *
 * Created: Thu Jun 10 22:56:31 2004
 *
 * @author <a href="mailto:arie@cs.toronto.edu">Arie Gurfinkel</a>
 * @version
 */

public interface MvRelation 
{

  /**
   * Describe <code>fwdImage</code> method here.
   *
   * computes forward image of v
   * @param v a <code>MvSet</code> value
   * @return a <code>MvSet</code> value
   */
  MvSet fwdImage (MvSet v);
 
  /**
   * Describe <code>bwdImage</code> method here.
   *
   * computes backward image of v
   * @param v a <code>MvSet</code> value
   * @return a <code>MvSet</code> value
   */
  MvSet bwdImage (MvSet v);


  /**
   * Describe <code>toMvSet</code> method here.
   *
   * @return a <code>MvSet</code> value
   */
  MvSet toMvSet ();

  /**
   * Dual of bwdImage
   * dualBwdImage (v) == ! bwdImage (! v)
   *
   * @param v a <code>MvSet</code> value
   * @return a <code>MvSet</code> value
   */
  MvSet dualBwdImage (MvSet v);

}
