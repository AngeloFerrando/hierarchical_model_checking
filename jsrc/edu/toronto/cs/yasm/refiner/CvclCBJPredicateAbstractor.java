package edu.toronto.cs.yasm.refiner;

import edu.toronto.cs.tp.cvcl.*;

import java.util.HashMap;
import java.util.Map;
import java.util.List;
import java.util.LinkedList;
import java.util.BitSet;


public class CvclCBJPredicateAbstractor extends CBJPredicateAbstractor
{
  ValidityChecker vc;

  Map exprToVarMap;

  Expr tExpr;
  Expr fExpr;

  public CvclCBJPredicateAbstractor (List _srcPredicates, 
				     Expr _targetPredicate, 
				     ValidityChecker _vc)
  {
    super (_srcPredicates, _targetPredicate);
    vc = _vc;

    for (int i = 0; i < variables.length; i++)
      System.err.println ("Variable: " + i + " is " + variables [i]);
    
    System.err.println ("target: " + target);
    tExpr = vc.falseExpr ();
    fExpr = vc.falseExpr ();

    exprToVarMap = new HashMap ();
    for (int i = 0; i < variables.length; i++)
      exprToVarMap.put (variables [i].getExpr (), variables [i]);
    
  }

  
  public BitSet checkConsistency (CSPVariable curVar)
  {
    
    System.err.println ("in check consistency of " + curVar);
    BitSet set = new BitSet ();
    set.set (0);

    //if (curVar != variables [variables.length - 1]) return set;
    //if (assignedVariables != variables.length - 1) return set;
    if (!(oneVariables.isEmpty () && twoVariables.isEmpty ()))
      return set;

    int currentScope = vc.scopeLevel ();

    vc.push ();

    // -- assert current assignment
//     for (int i = 1; i <= assignedVariables; i++)
//       {
// 	// -- get an exression for variable i and negate it
// 	// -- if necessary
// 	Expr assertion = currentAssignment [i].getExpr ();
// 	if (currentAssignment [i].getCurVal () == 0)
// 	  assertion = assertion.notExpr ();

// 	System.err.println ("\tasserting: " + assertion);
// 	vc.assertFormula (assertion);
//       }

    // -- query 
    System.err.println ("\tquery: " + target);
    Boolean result = CVCLUtil.checkTruth (vc, target, vc.notExpr (target));
//     Boolean result = 
//       checkTruth (vc.orExpr (target, tExpr), 
// 		  vc.orExpr (vc.notExpr (target), fExpr));

    Expr solution = vc.trueExpr ();
    
    if (result != null)
      {
	CVectorExpr assumptions = new CVectorExpr (0);
	vc.getAssumptions (assumptions);
	for (int i = 0; i < assumptions.size (); i++)
	  {
	    
	    System.err.println ("\t" + i + " " + assumptions.get (i));
	    
	    // -- remove negation if it is present
	    Expr expr = assumptions.get (i);

	    // ---
	    solution = vc.andExpr (solution, expr);
	    if (expr.isNot ())
	      expr = expr.get (0);
	    
	    CSPVariable var = (CSPVariable) exprToVarMap.get (expr);

	    assert var != null : 
	      "Could not find a variable for " + expr;
	    
	    set.set (var.getCurLevel ());

	  }

	if (result == Boolean.TRUE)
	  tExpr = vc.orExpr (tExpr, solution);
	else 
	  fExpr = vc.orExpr (fExpr, solution);
      }

    

    vc.popto (currentScope);
    System.err.println ("Done with checkConsistency of " + curVar);
    return set;
  }


  void assign (CSPVariable var, int val, int level)
  {
    super.assign (var, val, level);

    Expr assertion = var.getExpr ();
    if (var.getCurVal () == 0)
      assertion = assertion.notExpr ();
    
    
    var.setCurScope (vc.scopeLevel ());
    vc.push ();
    vc.assertFormula (assertion);

    
  }
  void undo (CSPVariable var)
  {	    
    vc.popto (var.getCurScope ());
    super.undo (var);
  }
  


  public Expr getTExpr ()
  {
    return tExpr;
  }
  public Expr getFExpr ()
  {
    return fExpr;
  }

  public static void main (String[] args)
  {
    ValidityChecker vc = CVCLUtil.newValidityChecker ();
    
    List preds = new LinkedList ();

    Expr x = vc.varExpr ("x", vc.intType ());
    Expr y = vc.varExpr ("y", vc.intType ());

    preds.add (vc.eqExpr (x, vc.ratExpr (3, 1)));
    preds.add (vc.gtExpr (x, vc.ratExpr (0, 1)));
    preds.add (vc.gtExpr (y, vc.ratExpr (0, 1)));
    
    Expr x_plus_y = vc.plusExpr (x, y);
    preds.add (vc.gtExpr (x_plus_y, vc.ratExpr (0, 1)));


    
    
    Expr target = vc.gtExpr 
      (vc.plusExpr (x_plus_y, 
		    vc.plusExpr (x_plus_y, y)), 
       vc.ratExpr (0, 1));
    
    CvclCBJPredicateAbstractor cbj = 
      new CvclCBJPredicateAbstractor (preds, target, vc);

    edu.toronto.cs.util.StopWatch sw = new edu.toronto.cs.util.StopWatch ();
    cbj.recSearch (1);
    System.err.println ("Done in: " + sw);
    System.err.println ("QUERY: " + vc.sw);

    System.err.println ("True set is: " + cbj.tExpr);
    System.err.println ("False set is: " + cbj.fExpr);
  }
}
