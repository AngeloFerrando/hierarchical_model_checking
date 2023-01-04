package edu.toronto.cs.boolpg.abstraction;

import edu.toronto.cs.tp.cvcl.*;

import java.util.*;

/**
 * Computes abstraction H (tPredicate, fPredicate) for a statement
 *
 * @author <a href="mailto:maxin@epoch.cs">Xin Ma</a>
 * @version 1.0
 */
public class StmtAbstraction2
{
  
      
  /**
   * Computes H (tPredicate, fPredicate) and returns 
   * an Expr array
   *
   * @param vc a <code>ValidityChecker</code> value
   * @param predicates a <code>List</code> value
   * @param tPredicate an <code>Expr</code> value
   * @param varMap a <code>Map</code> value
   * @return a <code>CVectorExpr</code> value
   */
  public static Expr[] computeH (ValidityChecker vc, 
				 List predicates, 
				 Expr tPredicate, 
				 Map varMap)
  {
    print (predicates, varMap);
    ValidityChecker vc2 = CVCLUtil.newValidityChecker ();
    ExprManager em2 = vc2.getEM();
    ExprManager em = vc.getEM();
    Expr e = em2.rebuildExpr (tPredicate);
    substituteVars (e, varMap);


    Expr[] h = preComputeH (vc, vc2, predicates, e);
    
    for (int i = 0; i < 2; i++)
      {
	if(h [i].isNull ())
	  h [i] = vc.falseExpr ();
	//else if(vc.query(h [i], false))
	//  h [i] = vc.trueExpr ();

	// -- clean theorem proover state
	vc.popto (0);
      }
    for (int i = 0; i < 2; i++)
      System.out.println(h[i]);
    return h;
  }


  /**
   * Describe <code>substituteVars</code> method here.
   *
   * @param e an <code>Expr</code> value
   * @param varMap a <code>Map</code> value
   */
  private static void substituteVars (Expr e, Map varMap)
  {
    int ar = e.arity ();
    
    if(ar == 0){
      if(e.isVar()){
	Expr temp = (Expr) varMap.get(e.toString());
	if(temp==null)
	  return;
	e.assign(temp);
      }
      return;
    }
    if(ar>0){
      for(int k = 0; k<ar; k++){
	substituteVars(e.get(k), varMap);
      }
    }
  }


  /**
   * Describe <code>preComputeH</code> method here.
   *
   * @param vc a <code>ValidityChecker</code> value
   * @param vc2 a <code>ValidityChecker</code> value
   * @param predicates a <code>List</code> value
   * @param e2 an <code>Expr</code> value
   * @return an <code>Expr[]</code> value
   */
  private static Expr[] preComputeH (ValidityChecker vc,
				     ValidityChecker vc2, 
				     List predicates, 
				     Expr e2)
  {
    
    boolean[] a = new boolean [predicates.size()];
    int i = 0;
    
    Expr p = new Expr();
    Expr e = new Expr();
    Expr test = new Expr();
    Expr c = new Expr();
    Expr d = new Expr();

    boolean cExpr = true;
    boolean dExpr = true;
    
    e = vc.importExpr(e2);
    if (vc.query(e, false))
      {
	cExpr = false;
	c = vc.trueExpr();
      }
    else
      {
	vc.popto(0);
	if (vc.query(e.notExpr(), false))
	  {
	    dExpr = false;
	    d = vc.trueExpr();
	  }
      }
    vc.popto(0);
    
    while( i < predicates.size())
      {
	i = 0;
	if(a [0])
	  p.assign((Expr) predicates.get (0));
	else
	  p.assign (((Expr)predicates.get (0)).notExpr());
	
	for(int j = 1; j < predicates.size(); j++)
	  {
	    if(a[j])
	      p = vc.andExpr(p, ((Expr)predicates.get(j)));
	    else
	      p = vc.andExpr(p, ((Expr)predicates.get(j)).notExpr());
	  }
	boolean result;
	
	if (cExpr)
	  {
	    test = vc.impliesExpr(p, e);
	 
	    result = vc.query(test,false);
	    vc.popto(0);
	    
	    if(result == true)
	      {
		if(c.isNull())
		  c.assign(p);
		else
		  c = vc.orExpr(c, p);
	      }
	  }
	if (dExpr)
	  {
	    test = vc.impliesExpr(p, e.notExpr());
	    result=vc.query(test,false);
	    if(result)
	      {
		if(d.isNull())
		  d.assign(p);
		else
		  d = vc.orExpr(d, p);
	      }
	  } 
	vc.popto(0);
	while(i < predicates.size() && a [i])
	  {
	    a[i] = false;
	    i++;
	  }
	
	if(i != predicates.size())
	  a[i] = true;
      }
    
    return new Expr[] {c, d};
  }

  /**
   * Testing only
   *
   * @param argv[] a <code>String</code> value
   * @exception Exception if an error occurs
   */
  public static void main (String argv[]) throws Exception
  {
    test ();
  }

  /**
   * Tests this class
   *
   */
  private static void test ()
  {
    ValidityChecker vc = CVCLUtil.newValidityChecker ();
    List predicates = new ArrayList();
    Expr x = vc.varExpr ("x", vc.realType());
    Expr y = vc.varExpr ("y", vc.realType());
    Expr zero = vc.ratExpr (0,1);
    Expr one = vc.ratExpr (1,1);
    Expr two = vc.ratExpr (2,1);
    Expr three = vc.ratExpr (3, 1);
    Expr four = vc.ratExpr (4, 1);
    Expr five = vc.ratExpr (5, 1);
    Expr six = vc.ratExpr (6, 1);
    Expr seven = vc.ratExpr (7, 1);
    Expr eight = vc.ratExpr (8, 1);
    Expr nine = vc.ratExpr (9, 1);
    Expr ten = vc.ratExpr (10, 1);
    //test.c
    Expr xeqy = vc.eqExpr(x, y);
    
    //**************************
    //test1.c
    Expr xgtzero = vc.gtExpr (x, zero);
    Expr xleone = vc.leExpr (x, one);
    Expr xletwo = vc.leExpr (x, two);
    Expr xlethree = vc.leExpr(x, three);
    //predicates.add (xgtzero);
    //predicates.add (vc.gtExpr(vc.minusExpr(x, one), zero));
    //predicates.add (vc.gtExpr(vc.minusExpr(vc.minusExpr(x, one), one), zero));
    //predicates.add (xlethree);
    //predicates.add(vc.eqExpr(x, y));
    predicates.add(vc.eqExpr(y, nine));
    predicates.add(vc.gtExpr(y, eight));
    
    //predicates.add(vc.gtExpr(y, four));
    //predicates.add(vc.gtExpr(y, five));
    //predicates.add(vc.eqExpr(x, vc.plusExpr(y, one)));
    //predicates.add(xgtzero);
    //---********************************

    Map varMap = new HashMap();
    
    computeH (vc, predicates, (Expr) predicates.get (0), varMap);
    computeH (vc, predicates, (Expr) predicates.get (1), varMap);
    
    
    //for(int i = 0; i<predicates.size(); i++){
    //  ExprManager em = vc.getEM();
    //  Expr e = em2.rebuildExpr(predicates.get(i));
    //  replace(e, m);
    //  testPredicate(vc,vc2, predicates,e);
    //}

  }

  /**
   * used for testing
   *
   * @param predicates a <code>CVectorExpr</code> value
   * @param m a <code>Map</code> value
   */
  private static void print (List predicates, Map m)
  {
    for(int i = 0; i < predicates.size (); i++)
      System.out.println ("predicate " + i + " " + predicates.get (i));
    
    String key;
    for (Iterator it = m.keySet ().iterator (); it.hasNext ();)
      {
	key =(String) it.next ();
	System.out.println (key + " " + m.get (key));
      }
  }


}
