package edu.toronto.cs.boolpg.abstraction;

import edu.toronto.cs.tp.cvcl.*;

import java.util.*;

/**
 * Computes abstraction H (tPredicate, fPredicate) for a statement
 *
 * @author <a href="mailto:maxin@epoch.cs">Xin Ma</a>
 * @version 1.0
 */
public class StmtAbstraction
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
    edu.toronto.cs.yasm.util.Util.substituteVars (e, varMap);


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

    return h;
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
    vc.popto(0);
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
	System.out.println (e);
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
    ValidityChecker vc2 = CVCLUtil.newValidityChecker ();

    ExprManager em2 = vc2.getEM ();

    Expr x = vc.varExpr ("x", vc.realType());
    Expr y = vc.varExpr("y", vc.realType());
    //Expr z = vc.varExpr("z", vc.realType());
    
    List predicates = new ArrayList (2);

    predicates.add (vc.eqExpr (x, y));
    predicates.add (vc.gtExpr (x, vc.ratExpr (0,1)));
    //predicates.add(vc.eqExpr(y, z));
	
    Map varMap = new HashMap();
    Expr one = vc.ratExpr (1,1);
    varMap.put ("x", vc.plusExpr (x, vc.ratExpr (1,1)));
    varMap.put ("y", y);
    //m.put("z", vc.plusExpr(z, one));
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
