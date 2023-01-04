package edu.toronto.cs.tp.cvcl;

import java.util.List;
import java.util.Iterator;

/**
 * Utility class to use CVCLite
 *
 * @author <a href="mailto:maxin@epoch.cs">Xin Ma</a>
 * @version 1.0
 */
public class CVCLUtil
{
  static
  {
    try
      {
	System.loadLibrary ("JavaCVC");
      }
    catch (UnsatisfiedLinkError ex)
      {
	System.err.println ("Cannot load JavaCVC library");
        ex.printStackTrace ();
	System.exit (1);
      }
  }

  /**
   * Creates default version of ValidtyChecker
   *
   * @return a <code>ValidityChecker</code> value
   */
  public static ValidityChecker newValidityChecker ()
  {
    CLFlags flags = ValidityChecker.createFlags ();
    flags.setFlag ("dagify-exprs", false);
    // flags.setFlag ("proofs", true);
    flags.setFlag ("indent", true);
    return newValidityChecker (flags);
  }

  public static ValidityChecker newValidityChecker (CLFlags flags)
  {
    return new ValidityChecker (flags);
  }


  /**
   * returns true if expr is valid. Leaves the ValidityChecker 
   * in the same state as it gets it
   *
   * @param vc a <code>ValidityChecker</code> value
   * @param expr an <code>Expr</code> value
   * @return a <code>boolean</code> value
   */
  public static boolean quickQuery (ValidityChecker vc, Expr expr)
  {
    int scope = vc.scopeLevel ();
    try 
      {
	vc.push ();
	return vc.query (expr, false);
      }
    finally 
      {
	vc.popto (scope);
      }
  }

  /**
   * Returns Boolean.TRUE if tExpr is valid,
   *         Boolean.FALSE if fExpr is valid and tExpr is not valid
   * side effects: ValidityChecker is left as-is after the last query
   * which makes it possible to extract the set of assumptions
   *
   * @param vc a <code>ValidityChecker</code> value
   * @param tExpr an <code>Expr</code> value
   * @param fExpr an <code>Expr</code> value
   * @return a <code>Boolean</code> value
   */
  public static Boolean checkTruth (ValidityChecker vc, Expr tExpr, 
				    Expr fExpr)
  {
    int scope = vc.scopeLevel ();
    vc.push ();
    boolean result = vc.query (tExpr, false);
    if (result) return Boolean.TRUE;
    
    vc.popto (scope);
    vc.push ();
    result = vc.query (fExpr, false);
    if (result) return Boolean.FALSE;
    return null;
  }
  

  /**
   * Same as checkTruth (ValidityChecker, Expr, Expr)
   * but checks if expr or expr.notExpr are valid
   *
   * @param vc a <code>ValidityChecker</code> value
   * @param expr an <code>Expr</code> value
   * @return a <code>Boolean</code> value
   */
  public static Boolean checkTruth (ValidityChecker vc, Expr expr)
  {
    return checkTruth (vc, expr, expr.notExpr ());
  }


  /**
   * A side effect free version of checkTruth
   *
   * @param vc a <code>ValidityChecker</code> value
   * @param tExpr an <code>Expr</code> value
   * @param fExpr an <code>Expr</code> value
   * @return a <code>Boolean</code> value
   */
  public static Boolean quickCheckTruth (ValidityChecker vc, 
					 Expr tExpr, 
					 Expr fExpr)
  {
    int scope = vc.scopeLevel ();
    try 
      {
	vc.push ();
	return checkTruth (vc, tExpr, fExpr);
      }
    finally
      {
	vc.popto (scope);
      }
  }
  
  /**
   * A side effect free version of checkTruth
   *
   * @param vc a <code>ValidityChecker</code> value
   * @param expr an <code>Expr</code> value
   * @return a <code>Boolean</code> value
   */
  public static Boolean quickCheckTruth (ValidityChecker vc, Expr expr)
  {
    return quickCheckTruth (vc, expr, expr.notExpr ());
  }
  
  /**
   * Assert all formulas in vector v
   *
   * @param vc a <code>ValidityChecker</code> value
   * @param v a <code>CVectorExpr</code> value
   */
  public static void assertFormula (ValidityChecker vc, CVectorExpr v)
  {
    for (int i = 0; i < v.size (); i++)
      vc.assertFormula (v.get(i));
  }

  /**
   * Assert all formulas in a List v
   *
   * @param vc a <code>ValidityChecker</code> value
   * @param v a <code>List</code> value
   */
  public static void assertFormula (ValidityChecker vc, List v)
  {
    for (Iterator it = v.iterator (); it.hasNext ();)
      vc.assertFormula ((Expr) it.next ());
  }
  
  public static boolean implies (ValidityChecker vc, Expr e1, Expr e2)
  {
    return quickQuery (vc, vc.impliesExpr (e1, e2));
  }

  public static CVectorExpr getAssumptions (ValidityChecker vc)
  {
    CVectorExpr vector = new CVectorExpr (0);
    vc.getAssumptions (vector);
    return vector;
  }
  
}
