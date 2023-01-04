package edu.toronto.cs.boolpg.abstraction;

import edu.toronto.cs.tp.cvcl.*;
import edu.toronto.cs.yasm.util.Util;

import java.util.*;


public class SmartCompute
{
  private static Expr workingPred;
  
  /**
   * for a new target predicate, all the conditions
   * of the old predicates have to be changed.  This
   * method call sets the current (old) predicate that
   * the target predicate is being added to.
   *
   * @param e an <code>Expr</code> value
   */
  public static void setWorkingPred (Expr e)
  {
    workingPred = e;
    System.err.print ("\t working predicate: ");
    System.err.println (e);
  }
  
  
  /**
   * Just a helper's method of findeing whether
   * Expr1 implies Expr2
   *
   * @param e an <code>Expr</code> value
   * @param e2 an <code>Expr</code> value
   * @param vc a <code>ValidityChecker</code> value
   * @return a <code>boolean</code> value
   */
  public static boolean implies(Expr e, Expr e2, ValidityChecker vc)
  {
    boolean result;
    int level = vc.scopeLevel();
    result = vc.query (vc.impliesExpr (e, e2), false);
    vc.popto(level);;
    return result;
  }
  
  
  /**
   * This is the main method to call to compute the truePred
   * falsePred, nTruePred, nFalsePred for the working predicate
   * given target predicate
   *
   * @param vc a <code>ValidityChecker</code> value
   * @param predicate an <code>Expr</code> value
   * @param l a <code>Ladder</code> value
   * @param m a <code>Map</code> value
   * @param p an <code>int</code> value
   */
  public static void smartCompute (ValidityChecker vc, Expr predicate, Ladder l, 
				   Map m, int p)
  {
    List predicates = new ArrayList();
    List nPredicates = new ArrayList();
   
    //substitution
    Expr tpredicate = Util.preSubstitution (predicate);
    Util.substituteVars (tpredicate, m);
    tpredicate = vc.importExpr(tpredicate);
    System.err.print("\t working target predicate: ");
    System.err.println(tpredicate);
    
    //if the there is nothing in the truePred List yet
    //then it's the frist time that the condition of the
    //working predicate (in this case target predicate as well)
    //is been calculated.
   
    if (predicate.equal(workingPred, predicate))
      {
	if (implies (predicate, tpredicate, vc))
	    predicates.add (predicate);
	else
	    nPredicates.add(predicate);
	if (implies (predicate.notExpr (), tpredicate, vc))
	    predicates.add (predicate.notExpr ());
	else
	    nPredicates.add (predicate.notExpr ());
	if (predicates.size()==0)
	  predicates.add (vc.falseExpr());
	l.truePred.add(predicates);
	l.nFalsePred.add(nPredicates);

	

	predicates = new ArrayList();
	nPredicates = new ArrayList();

	if (implies (predicate, tpredicate.notExpr(), vc))
	    predicates.add (predicate);
	else
	    nPredicates.add(predicate);
	if (implies (predicate.notExpr (), tpredicate.notExpr(), vc))
	    predicates.add (predicate.notExpr ());
	else
	    nPredicates.add (predicate.notExpr ());
	if (predicates.size() == 0)
	  predicates.add (vc.falseExpr());

	l.falsePred.add(predicates);
	l.nTruePred.add(nPredicates);

	printBP (l, p);
      }
    else
      {
	// nFalsePred are the possible Predicates makes the working
	// predicates true
	int size = ((List)l.nFalsePred.get(p)).size();
	for (int i = 0; i < size; i++)
	  {  
	    Expr e = new Expr();
	    check ((Expr)((List)l.nFalsePred.get (p)).get (0),
		   tpredicate, (List)l.truePred.get (p), 
		   (List)l.nFalsePred.get (p), vc);
	  }
	// nTruePred are all the possible Predicates that make
	// the working predicates false
	size = ((List)l.nTruePred.get(p)).size();
	for (int i = 0; i < size; i++)
	  {  
	    Expr e = new Expr();
	    check ((Expr)((List)l.nTruePred.get (p)).get (0),
		   tpredicate.notExpr(), (List)l.falsePred.get (p), 
		   (List)l.nTruePred.get (p), vc);
	  }
	printBP(l, p);
      }
  }
  
  /**
   * Helper function
   *
   * @param existingPred an <code>Expr</code> value
   * @param tpredicate an <code>Expr</code> value
   * @param l a <code>List</code> value
   * @param nl a <code>List</code> value
   * @param vc a <code>ValidityChecker</code> value
   */
  private static void check (Expr existingPred, Expr tpredicate,
			    List l, List nl, ValidityChecker vc)
  {
    Expr e;
    
    e = vc.andExpr (existingPred, workingPred);
    if (implies(e, tpredicate, vc))
      {
	l.add(e);
      }
    else nl.add(e);
    
    e = vc.andExpr (existingPred, workingPred.notExpr());
    if (implies(e, tpredicate, vc))
      {
	l.add(e);
      }
    else nl.add(e);
    
    nl.remove(existingPred);
  }
  
  
  /**
   * Helper function prints all the information given
   * the ladder and the position in the ladder (the working target)
   *
   * @param l a <code>Ladder</code> value
   * @param p an <code>int</code> value
   */
  private static void printBP (Ladder l, int p)
  {
    System.err.println("Position: #" + p);
    System.err.println("Making true:");
    Util.printList((List)l.truePred.get(p));
    System.err.println("Possibly Making false: ");
    Util.printList((List)l.nTruePred.get(p));
    System.err.println("Making false: ");
    Util.printList((List)l.falsePred.get(p));
    System.err.println("Possibly Making true: ");
    Util.printList((List)l.nFalsePred.get(p));
  }
}
