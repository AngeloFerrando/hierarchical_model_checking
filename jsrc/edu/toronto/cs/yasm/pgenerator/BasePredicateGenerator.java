package edu.toronto.cs.yasm.pgenerator;

import edu.toronto.cs.tp.cvcl.*;
import edu.toronto.cs.yasm.wp.WPComputer;
import edu.toronto.cs.expr.CVCLExprConverter;
import edu.toronto.cs.yasm.pprogram.PrllAsmtPStmt;
import edu.toronto.cs.yasm.pprogram.PProgram;
import edu.toronto.cs.yasm.abstractor.PredicateRefiner;

import java.util.*;


public abstract class BasePredicateGenerator implements PredicateGenerator
{
  protected PrllAsmtPStmt stmt;
  protected ValidityChecker vc;
  protected List existingPred;

 
  
  // -- symbolic execution that we want to get rid of
  //protected Map symbolicMap;
  protected WPComputer wp;
  
  // -- parent state
  protected CVectorExpr pStatePred;
  // -- child state
  protected CVectorExpr cStatePred;

  protected CVCLExprConverter cvclConverter;

  protected List newPreds;

  
  protected PProgram pProgram;

  
  /**
   * This constructor of PredicateGenerator is almost the same 
   * as the one above, the only difference is the last
   * two premeters.  This Constructor uses CVectorExpr to 
   * represent the parent/child state of the maybe transition.
   *
   * @param _pProgram a <code>PProgram</code> value
   * @param _stmt a <code>PrllAsmtPStmt</code> value
   * @param _refiner a <code>PredicateRefiner</code> value
   * @param _wp a <code>WPComputer</code> value
   * @param _pStatePred a <code>CVectorExpr</code> value
   * @param _cStatePred a <code>CVectorExpr</code> value
   */
  public BasePredicateGenerator (PProgram _pProgram, 
				 PrllAsmtPStmt _stmt,
				 PredicateRefiner _refiner,
				 WPComputer _wp,
				 CVectorExpr _pStatePred, 
				 CVectorExpr _cStatePred)
				 
  {
    pProgram = _pProgram;
    stmt = _stmt;
    cvclConverter = _refiner.getCVCLConverter ();
    vc = _refiner.getVC ();
    existingPred = _refiner.getCVCLConverter ().toCVCL (_refiner.
							getPredicates ());
    wp = _wp;
    pStatePred = _pStatePred;
    cStatePred = _cStatePred;

    newPreds = new ArrayList ();

    // Class invariants:

    // assert wp != null : "WP is null";
  }

  /**
   * Describe <code>getPProgram</code> method here.
   *
   * @return a <code>PProgram</code> value
   */
  public PProgram getPProgram ()
  {
    return pProgram;
  }
  
  /**
   * returns null if expr is in existingPred, and a cannonical version
   * of expr otherwise
   *
   * @param expr an <code>Expr</code> value
   * @return an <code>Expr</code> value
   */
  public Expr hasPredicate (Expr expr)
  {
    // -- get rid of negation if there is one
    if (expr.isNot ())
      expr = expr.get (0);

    return existingPred.contains (expr) ? null : expr;
  }
  
  
  protected Expr computeWP (Expr expr)
  {
    return cvclConverter.toCVCL 
      (wp.computeWP (cvclConverter.fromCVCL (expr)));
  }

  public List getNewPreds ()
  {
    return newPreds;
  }
 
}
