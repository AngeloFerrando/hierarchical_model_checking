package edu.toronto.cs.boolpg.abstraction;

import edu.toronto.cs.tp.cvcl.*;
import edu.toronto.cs.yasm.util.Util;

import java.util.*;

public class StatementAbstraction
{
  List truePreds = new ArrayList ();
  List falsePreds = new ArrayList ();
  
  List allPreds;
  
  
  /**
   * The index of which new predicates begin
   *
   */
  int targetIndex;

  
  /**
   * Instead of using symbolic excution and mapping
   * to find out the predicates to be queried, this 
   * List is a list of predicates after substitution
   * substitution is done using Yasm Expr
   *
   */
  List tPredicate;

  ValidityChecker vc;

  
  /**
   * For each new predicate, all the old/new predicates have 
   * to be updated.  tExpr is the target predicate we are
   * currently working on
   *
   */
  Expr tExpr;

  /**
   * The index of tExpr
   *
   */
  int tExprIndex;

  
  /**
   * When a query is true, not all the assertions are used
   * only the useful are found
   *
   */
  Expr usefulExpr;
  public StatementAbstraction (ValidityChecker _vc)
  {
    vc = _vc;
  }

  
  public List getTruePreds ()
  {
    return truePreds;
  }
  public List getFalsePreds ()
  {
    return falsePreds;
  }
  
  public void branchRefinement (List _allPreds, int _targetIndex, List _tPredicate)
  {
    vc.popto (0);
    allPreds = _allPreds;
    targetIndex = _targetIndex;
    tPredicate = _tPredicate;
    
    while (targetIndex < allPreds.size ())
      {
	System.err.println ("NEW PRED********************************************************************");
	System.err.println (allPreds.get (targetIndex));
	for (int i = 0; i <= targetIndex; i ++ )
	  {
	    System.err.println ("working: " + tPredicate.get (i));
	    //**************************************************
//  	    if (check ((Expr)allPreds.get (i), (Expr)tPredicate.get (i)) && 
//  		check (((Expr)allPreds.get (i)).notExpr (), ((Expr)tPredicate.get (i)).notExpr ()))
//  	      {
//  		//assert !oldTargetExpr (i) || (!truePreds.isEmpty () && !falsePreds.isEmpty ());
//   		if (!oldTargetExpr (i))
//   		  {
//  		    truePreds.add ((Expr)allPreds.get (i));
//  		    falsePreds.add (((Expr)allPreds.get (i)).notExpr ());
//   		  }
//  	      }
 	    //**************************************************
		
	    //	    	     else 
	      if (check ((Expr)tPredicate.get (i)))
	      {
		System.err.println ("always true: " + i);
		if (!oldTargetExpr(i) && truePreds.size () == targetIndex)
		  {
		    truePreds.add (vc.trueExpr ());
		    falsePreds.add (vc.falseExpr ());
		  }
	      }
	    else if (check (((Expr)tPredicate.get (i)).notExpr ()))
	      {
		System.err.println ("always false: " + i);
		if (!oldTargetExpr (i) && truePreds.size () == targetIndex)
		  {
		    falsePreds.add (vc.trueExpr ());
		    truePreds.add (vc.falseExpr ());
		  }
	      }
	    else
	      {
		System.err.println ("normal: " + i);

		tExpr = (Expr)tPredicate.get (i);
		tExprIndex = i;
		findAssignment (targetIndex, new ArrayList ());

		if (truePreds.size () == targetIndex  && !oldTargetExpr (i))
		  truePreds.add (vc.falseExpr ());
		if (falsePreds.size () == targetIndex && !oldTargetExpr (i))
		  falsePreds.add (vc.falseExpr ());
	      }
	  }
	targetIndex ++;
      }
    printMe ();
    assert vc.scopeLevel () == 1;
    //vc.popto (0);
  }
  private boolean oldTargetExpr (int i)
  {
    if (i < targetIndex)
      return true;
    else return false;
  }
  
  private void findAssignment (int i, List l)
  {
    if (i < 0)
      return;
    
    Expr current;
    current = (Expr)allPreds.get(i);
    l.add (current);

    System.err.println ("current level " + i + "  +");
    Util.printList (l);
    
    boolean found = false;
    
    if (truePreds.size () > tExprIndex)
      if (check (l, (Expr)truePreds.get (tExprIndex)))
	found = true;
    if (falsePreds.size () > tExprIndex & !found)
      if (check (l, (Expr)falsePreds.get (tExprIndex)))
	found = true;
    
    if (!found)
      {
	if (query (l, tExpr))
	  {
	    if (!oldTargetExpr (tExprIndex)  && targetIndex == truePreds.size ())
	      truePreds.add (usefulExpr);
	    else
	      truePreds.set (tExprIndex, vc.orExpr ((Expr)truePreds.get (tExprIndex), vc.andExpr (Util.fListToVector (l))));
	  }
	else if (query (l, tExpr.notExpr()))
	  {
	    if (!oldTargetExpr(tExprIndex) && targetIndex == falsePreds.size ())
	      falsePreds.add (usefulExpr);
	    else
	      falsePreds.set (tExprIndex, vc.orExpr ((Expr)falsePreds.get (tExprIndex), vc.andExpr (Util.fListToVector (l))));
	  }
	else
	  findAssignment (i - 1, l);
      }
    l.remove (current);

    current = ((Expr)allPreds.get(i)).notExpr();
    l.add (current);

    found = false;
    System.err.println ("current level " + i + "  -");
    Util.printList (l);

    if (truePreds.size () > tExprIndex)
      if (check (l, (Expr)truePreds.get (tExprIndex)))
	found = true;
    if (falsePreds.size () > tExprIndex)
      if (check (l, (Expr)falsePreds.get (tExprIndex)))
	found = true;
    
    if (!found)
      {
	if (query (l, tExpr))
	  {
	    if (!oldTargetExpr (tExprIndex) && targetIndex == truePreds.size ())
	      truePreds.add (usefulExpr);
	    else
	      truePreds.set (tExprIndex, vc.orExpr ((Expr)truePreds.get (tExprIndex), vc.andExpr (Util.fListToVector (l))));
	  }
	else if (query (l, tExpr.notExpr()))
	  {
	    if (!oldTargetExpr(tExprIndex) && targetIndex == falsePreds.size ())
	      falsePreds.add (usefulExpr);
	    else
	      falsePreds.set (tExprIndex, vc.orExpr ((Expr)falsePreds.get (tExprIndex), vc.andExpr (Util.fListToVector (l))));
	  }
	else
	  findAssignment (i - 1, l);
      }
    
    l.remove (current);

  }
  
  private boolean query (List assertions, Expr question)
  {
    CVectorExpr assumptions = new CVectorExpr (0);
    System.err.println (vc.impliesExpr (vc.andExpr (Util.fListToVector (assertions)), question));
    int scope = vc.scopeLevel ();
    System.err.println ("Scope in query is: " + vc.scopeLevel ());
    vc.push ();
    for (int i = 0; i < assertions.size (); i++)
      {
	System.err.println ("Asserting " + i + ": " + assertions.get (i));
	vc.assertFormula ((Expr)assertions.get (i));
      }

    boolean result = vc.query (question, false);
    if (result == true)
      {
	System.err.println ("true: " + vc.impliesExpr (vc.andExpr (Util.fListToVector (assertions)), question));
	vc.getAssumptions (assumptions);
	System.err.println ("Assumptions: ");
	    
	    
	if ( assumptions.size () > 1 && 
	     vc.trueExpr ().equal (assumptions.get (0), vc.trueExpr ()))
	  assumptions = Util.cleanVector (assumptions);
	
	Util.printVector (assumptions);
	System.err.println ("End of Assumptions");
	usefulExpr = vc.andExpr (assumptions);
      }
    vc.popto (scope);
    return result;
  }
  
  private boolean check (Expr e)
  {
    int scope = vc.scopeLevel ();
    boolean result = vc.query (e, false);
    vc.popto (scope);
    return result;
  }
  private boolean check (Expr a, Expr b)
  {
    int scope = vc.scopeLevel ();
    boolean result = vc.query (vc.impliesExpr (a, b), false);
    vc.popto (scope);
    return result;
  }
  private boolean check (List l, Expr e)
  {
    int scope = vc.scopeLevel ();
    boolean result = vc.query (vc.impliesExpr (vc.andExpr (Util.fListToVector(l)), e), false);
    if (result)
      System.err.println (vc.impliesExpr(vc.andExpr(Util.fListToVector(l)), e));
    vc.popto (scope);
    return result;
  }
  
  public void printMe()
  {  
    System.err.println ("\t\t All predicates:");
    Util.printList (allPreds);
    System.err.println ("\t\t True Predicates");
    Util.printList (truePreds);
    System.err.println ("\t\t False Predicates");
    Util.printList (falsePreds);
      
      
    System.err.println ("\t\t targetIndex:   " + targetIndex);
    System.err.println ("\t\t all newPredicates:");
    Util.printList (tPredicate);
      
    System.err.println ("\t\t working on predicate:   " + tExpr);
    System.err.println ("\t\t its index is:    " + tExprIndex);
  }
}
