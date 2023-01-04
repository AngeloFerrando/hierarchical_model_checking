package edu.toronto.cs.yasm.pgenerator;

import edu.toronto.cs.tp.cvcl.*;
import edu.toronto.cs.expr.CVCLExprConverter;
import edu.toronto.cs.yasm.pprogram.PrllAsmtPStmt;
import edu.toronto.cs.yasm.pprogram.PProgram;
import edu.toronto.cs.yasm.wp.WPComputer;
import edu.toronto.cs.yasm.abstractor.PredicateRefiner;

import java.util.*;

/**
 * Generates predicates based on the weakest pre-condition of 
 * existing predicates
 *
 * This class solely handles the part that generates new predicate(s) 
 * Call the constructor Function to initiate.  
 * Call the function newPred() to return a list of Predicates
 *
 * @author <a href="mailto:maxin@epoch.cs">Xin Ma</a>
 * @version 1.0
 */
public abstract class WPPredicateGenerator extends BasePredicateGenerator
{

  
  public WPPredicateGenerator (PProgram p, 
			       PrllAsmtPStmt _stmt,
			       PredicateRefiner _refiner,
			       WPComputer _wp,
			       CVectorExpr _pStatePred, 
			       CVectorExpr _cStatePred)
  {
    super (p, _stmt,_refiner, _wp,_pStatePred, _cStatePred);
  }
  
  
  public boolean find ()
  {
    newPreds = newPred ();
    return !newPreds.isEmpty ();
  }
  
  
  /**
   * This is the main function to call after construct and object
   * to obtain one or more new Predicates in the form of a list
   *
   * @return a <code>List</code> value
   */
  public List newPred()
  { 
    // -- if child state has no predicates, nothing to do
    // XXX as far as I can see, this should not happen
    if (cStatePred.size () == 0) return null;
    
    // -- find variables that we want to use to construct new
    // -- predicates
    List l = findSuspects (pStatePred, cStatePred);
    if (l.size() != 0)
      return updatePred (l);
    else
      return l;

    // if the previous method fails, one of the reason is that 
    // none of the predicates change value in this maybe transition
    // Indicating the reason for maybe transition does not come
    // from the sofisfication of the current predicates (which means
    // substitution to find the new predicates can't help), but from
    // insufficient predicates due to prove or disprove logically the
    // maybe transition.

    // In this case, we need to do something more sofisticated.
//     AIntellegence Ai = 
//       new AIntellegence (cvclConverter, vc, existingPred, predMap, 
// 			 wp, pStatePred, cStatePred, prop);
//     return Ai.inducePred (0);
  }
  


  /**
   * Given parent state and child state return the list
   * of predicates that should be used for refining
   *
   * @param pStatePred a <code>CVectorExpr</code> value
   * @param cStatePred a <code>CVectorExpr</code> value
   * @return a <code>List</code> value
   */
  protected abstract List findSuspects (CVectorExpr pStatePred, 
					CVectorExpr cStatePred);

  

  
  /**
   * This method checks whether the change of value
   * of the same predicate before and after the maybe
   * transition is due to a change of
   * value (in a true transition), or it's during a 
   * maybe transition
   *
   * @param from an <code>Expr</code> value
   * @param to an <code>Expr</code> value
   * @return a <code>boolean</code> value
   */
  protected boolean maybeTransition (Expr from, Expr to)
  {
    // XXX variable 'from' is not used !

    // -- compute weakest pre-condition of 'to' with respect to 
    // -- current statement
    //     to = edu.toronto.cs.yasm.util.Util.preSubstitution (to);
    //     edu.toronto.cs.yasm.util.Util.substituteVars (to, symbolicMap);
    //     to = vc.importExpr (to);

    to = computeWP (to);
    int scope = vc.scopeLevel ();
    assert scope == 1 : "Something is wrong with vc";
    vc.push ();
    // -- assert  the constraint corresponding to the parent state
    CVCLUtil.assertFormula (vc, pStatePred);
    

    // -- check if parent state satisfies the pre-condition
    boolean result = vc.query (to, false);

    
    //vc.popto (0);
    vc.popto (scope);
    return !result;
  }
  
  
  /**
   * Check if all the the predicates do not repeat the predicate
   * to be added
   *
   * @param l a <code>List</code> value
   * @return a <code>List</code> value
   */
  private List updatePred (List l)
  {
    List temp = new ArrayList();

    
    for (Iterator it = l.iterator(); it.hasNext();)
      {
	// -- compute wp of every predicate we found
	Expr e = computeWP ((Expr) it.next ());

	System.err.println ("AAA Found a predicate: " + e);
	System.err.println ("AAA comparing to: " + existingPred);
	e = hasPredicate (e);
	if (e == null)
	  System.err.println ("AAA But think I have it already");
	
	if (e != null)
	  {
	    System.out.println ("\t**********ATTENTION**************");
	    System.out.println ("\tNew Predicates is:");
	    System.out.println ("\t"+e);
	    temp.add(e);
	    //temp.add(vc.simplify (e));
	  }
      }
    return temp;
  }


  public static class WPDiff extends WPPredicateGenerator
  {
    
    
    public WPDiff (PProgram p, 
		   PrllAsmtPStmt _stmt,
		   PredicateRefiner _refiner,
		   WPComputer _wp,
		   CVectorExpr _pStatePred, 
		   CVectorExpr _cStatePred)
    {
      super (p, _stmt,_refiner, _wp,_pStatePred, _cStatePred);
    }
    
    /**
     * This returns a list of predicates that are different
     * before and after the maybe transition
     *
     * @param a a <code>CVectorExpr</code> value
     * @param b a <code>CVectorExpr</code> value
     * @return a <code>List</code> value
     */
    protected List findSuspects (CVectorExpr a, CVectorExpr b)
    {
      List l = new ArrayList ();
      for (int i = 0; i < a.size (); i++)
	if (!a.get (i).equals (b.get (i)) && 
	    maybeTransition (a.get (i), b.get (i)))
	  l.add (a.get (i));
      return l;
    }

  }
  

  public static class WPSame extends WPPredicateGenerator
  {


    public WPSame  (PProgram p, 
		    PrllAsmtPStmt _stmt,
		    PredicateRefiner _refiner,
		    WPComputer _wp,
		    CVectorExpr _pStatePred, 
		    CVectorExpr _cStatePred)
    {
      super (p, _stmt,_refiner, _wp,_pStatePred, _cStatePred);
    }
    
    /**
     * This returns a list of predicates that are different
     * before and after the maybe transition
     *
     * @param a a <code>CVectorExpr</code> value
     * @param b a <code>CVectorExpr</code> value
     * @return a <code>List</code> value
     */
    protected List findSuspects (CVectorExpr a, CVectorExpr b)
    {
      List l = new ArrayList ();
      for (int i = 0; i < a.size (); i++)
	if (a.get (i).equals (b.get (i)) && 
	    maybeTransition (a.get (i), b.get (i)))
	  l.add (a.get (i));
      return l;
    }

  }


}
