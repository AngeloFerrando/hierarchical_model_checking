package edu.toronto.cs.boolpg.abstraction;

import edu.toronto.cs.tp.cvcl.*;
import edu.toronto.cs.yasm.util.Util;

import java.util.*;

public class Ladder
{
  public List pred;
  public List truePred;
  public List falsePred;
  public List nTruePred;
  public List nFalsePred;

  public Ladder () 
  {
    pred = new ArrayList ();
    truePred = new ArrayList ();
    falsePred = new ArrayList ();
    nTruePred = new ArrayList ();
    nFalsePred = new ArrayList ();
  }

  public static void sCompute(ValidityChecker vc, List predicates, 
			      Map m, Ladder l)
  {
    for (int i = 0; i < predicates.size (); i++)
      {
	System.err.println("NEW**************************************");
	System.err.println(predicates.get(i));
	l.preSmartCompute (vc, (Expr)predicates.get(i), m, predicates, l, i);
      }
  }
  public Expr[] getAbstraction(int i, ValidityChecker vc)
  {

    Expr c = (Expr)((List)truePred.get(i)).get(0);
    for (int j = 1; j < ((List)truePred.get(i)).size(); j++)
      
	c = vc.orExpr(c, (Expr)((List)truePred.get(i)).get(j));
      
    Expr d = (Expr)((List)falsePred.get(i)).get(0);
    for (int j = 1; j < ((List)falsePred.get(i)).size(); j++)
	d = vc.orExpr(d, (Expr)((List)falsePred.get(i)).get(j));
    return new Expr[]{c, d};
  }

  public static void preSmartCompute (ValidityChecker vc, Expr predicate, 
				       Map m, List predicates, Ladder l, 
				       int p)
  {

    System.err.println (">>> preSmartCompute map: " + m);
    
    Expr tpredicate = Util.preSubstitution (predicate);
    Util.substituteVars (tpredicate, m);
    tpredicate = vc.importExpr(tpredicate);

    System.err.println (">>> preSmartCompute tpredicate: " + tpredicate);
    
    vc.popto(0);
    if (p >= l.pred.size())
      {
	System.err.println("It's the new pred that we are working on");
	if (vc.query(tpredicate, false))
	  {
	    System.err.print("\t working target predicate is always true: ");
	    System.err.println(tpredicate);
	    List temp = new ArrayList();
	    temp.add(vc.trueExpr());
	    l.truePred.add(temp);
	    vc.popto(0);
	    temp = new ArrayList();
	    temp.add(vc.falseExpr());
	    l.falsePred.add(temp);


	    temp = new ArrayList();
	    l.nTruePred.add(temp);
	    l.nFalsePred.add(temp);
	  }
	else
	  {
	    vc.popto(0);
	    if (vc.query(tpredicate.notExpr(), false))
	      {
		vc.popto(0);
		System.err.print("\t working target predicate is always false: "
				 );
		System.err.println(tpredicate);
		List temp = new ArrayList();
		temp.add(vc.trueExpr());
		l.falsePred.add(temp);

		temp = new ArrayList();
		temp.add(vc.falseExpr());
		l.truePred.add(temp);
		
		temp = new ArrayList();
		l.nTruePred.add(temp);
		l.nFalsePred.add(temp);
	      }
	    else
	      {
		vc.popto(0);
		for (int i = 0; i < predicates.size(); i++)
		  {
		    if (i == 0)
		      SmartCompute.setWorkingPred(predicate);
		    else if ( i == p)
		      SmartCompute.setWorkingPred((Expr)predicates.get(0));
		    else
		      SmartCompute.setWorkingPred((Expr)predicates.get(i));
		    SmartCompute.smartCompute(vc, predicate, l, m, p);
		  }
	      }
	  }
	l.pred.add(predicates.get(p));
      }
    else
      {
	System.err.println("It's the old pred that we are working on");

	Expr temp = new Expr();
	if (temp.equal(((Expr)((List)l.truePred.get(p)).get(0)), vc.trueExpr()))
	  {
	    System.err.print("\t working target predicate is always true");
	    System.err.println(tpredicate);
	  }
	else if (temp.equal(((Expr)((List)l.falsePred.get(p)).get(0)),vc.trueExpr()))
	  {
	    System.err.print("\t working target predicate is always false");
	    System.err.println(tpredicate);
	  }
	    else
	      {
		for (int i = l.pred.size(); i < predicates.size(); i++)
		  {
		    SmartCompute.setWorkingPred((Expr)predicates.get(i));
		    SmartCompute.smartCompute(vc, predicate, l, m, p);
		  }
	      }
      }	
  }
}
