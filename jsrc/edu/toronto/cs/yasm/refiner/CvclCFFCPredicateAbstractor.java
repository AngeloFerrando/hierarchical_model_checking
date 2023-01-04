package edu.toronto.cs.yasm.refiner;

import edu.toronto.cs.tp.cvcl.*;

import java.util.HashMap;
import java.util.Map;
import java.util.List;
import java.util.LinkedList;
import java.util.BitSet;


public class CvclCFFCPredicateAbstractor extends CFFCPredicateAbstractor
{
  ValidityChecker vc;

  Map exprToVarMap;

  Expr tExpr;
  Expr fExpr;

  public CvclCFFCPredicateAbstractor (List _srcPredicates, 
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

  public void forwardCheckCF (CSPVariable currentVar, int level)
  {
    if (variables.length == 1)
      {
	int scope = vc.scopeLevel ();
	vc.push ();
	Expr assertion = currentAssignment [1].getExpr ();
	if (currentAssignment [1].getCurVal () == 0)
	  assertion = assertion.notExpr ();
	vc.assertFormula (assertion);
	vc.push ();
	if (CVCLUtil.quickQuery (vc, target))
	  tExpr = vc.orExpr (tExpr, assertion);
	else if (CVCLUtil.quickQuery (vc,target.notExpr ()))
	  fExpr = vc.orExpr (fExpr, assertion);
	vc.popto (scope);
	return;
      }

    if (assignedVariables != variables.length - 1) return;

    System.err.println ("in forward checking of " + currentVar);
    BitSet noGood = new BitSet ();
    

    int scopeLevel = vc.scopeLevel ();
    vc.push ();
    for (int i = 1; i <= assignedVariables; i++)
      {
	Expr assertion = currentAssignment [i].getExpr ();
	if (currentAssignment [i].getCurVal () == 0)
	  assertion = assertion.notExpr ();
	System.err.println ("\tasserting: " + assertion);
	vc.assertFormula (assertion);
      }
    
    int scopeLevel2 = vc.scopeLevel ();
    for (int j = 0; j < variables.length; j++)
      {	
	if (variables [j].getCurVal () == -1)
	  {
	    CSPVariable curVar = variables [j];
	    for (int i = 1; i >= 0; i --)
	      {	      
		assert vc.scopeLevel () == scopeLevel2 : "Scope is wrong: " 
		  + vc.scopeLevel ();
		vc.push ();
		if (curVar.hasCurrentValue (i))
		  {
		    Expr e;
		    if (i == 1)
		      e = curVar.getExpr ();
		    else 
		      e = curVar.getExpr ().notExpr ();
		    
		    vc.assertFormula (e);
		    Boolean result = CVCLUtil.checkTruth (vc, target,
							  target.notExpr ());
		    
		    if (result == Boolean.TRUE)
		      {
			System.err.println ("prune1:" + e);
			//System.err.println (i);
			noGood = settingValue (e, 1);
			
			noGoodSet [curVar.getId ()][i] = noGood;
			prune (curVar, level, i);
		      }
		    else if (result == Boolean.FALSE)
		      {
			System.err.println ("prune2: " + e);
			
			noGood = settingValue (e, 0);
			
			noGoodSet [curVar.getId ()][i] = noGood;
			prune (curVar, level, i);
		      }
		    
		    // 		    if (CVCLUtil.implies (vc, e, target))
		    // 		      {
		    // 			//System.err.println ("prune1:" + e);
		    // 			//System.err.println (i);
		    // 			noGood = settingValue (e, 1);
		    // 			noGoodSet [curVar.getId ()][i] = noGood;
		    // 			prune (curVar, level, i);
		    // 		      }
		    // 		    else if (CVCLUtil.implies (vc, e, target.notExpr ()))
		    // 		      {
		    // 			//xSystem.err.println ("prune2: " + e);
		    // 			noGood = settingValue (e, 0);
		    // 			noGoodSet [curVar.getId ()][i] = noGood;
		    // 			prune (curVar, level, i);
		    // 		      }
		  }
		vc.popto (scopeLevel2);
	      }
	  }
	
      }
    
    vc.popto (scopeLevel);
    return;
  }

  private BitSet settingValue (Expr e, int result)
  {
    if (e.isNot ())
      e = e.get (0);
    BitSet set = new BitSet ();
    set.set (0);
    Expr solution = vc.trueExpr ();
    CVectorExpr assumptions = new CVectorExpr (0);
    vc.getAssumptions (assumptions);
    for (int i = 0; i < assumptions.size (); i++)
      {
	Expr expr = assumptions.get (i);
	solution = vc.andExpr (solution, expr);
	if (expr.isNot ())
	  expr = expr.get (0);
	
	CSPVariable var = (CSPVariable) exprToVarMap.get (expr);
	
	assert var!= null : 
	  "Could not find a variable for " + expr;
	if (!var.getExpr ().equals (e))
	  set.set (var.getCurLevel ());
      }
    
    if (result == 1)
      {
	System.err.println ("Adding " + solution + "to tExpr");
	tExpr = vc.orExpr (tExpr, solution);
      }
    else
      {
	System.err.println ("Adding " + solution + "to fExpr");
	fExpr = vc.orExpr (fExpr, solution);
      }
    return set;
  }
//   public BitSet checkConsistency (CSPVariable curVar)
//   {
    
//     System.err.println ("in check consistency of " + curVar);
//     BitSet set = new BitSet ();
//     set.set (0);

//     //if (curVar != variables [variables.length - 1]) return set;
//     //if (assignedVariables != variables.length - 1) return set;
//     if (!(oneVariables.isEmpty () && twoVariables.isEmpty ()))
//       return set;

//     int currentScope = vc.scopeLevel ();

//     vc.push ();
    
//     // -- assert current assignment
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

//     // -- query 
//     System.err.println ("\tquery: " + target);
//     Boolean result = CVCLUtil.checkTruth (vc, target, vc.notExpr (target));
// //     Boolean result = 
// //       checkTruth (vc.orExpr (target, tExpr), 
// // 		  vc.orExpr (vc.notExpr (target), fExpr));

//     Expr solution = vc.trueExpr ();
    
//     if (result != null)
//       {
// 	CVectorExpr assumptions = new CVectorExpr (0);
// 	vc.getAssumptions (assumptions);
// 	for (int i = 0; i < assumptions.size (); i++)
// 	  {
	    
// 	    System.err.println ("\t" + i + " " + assumptions.get (i));
	    
// 	    // -- remove negation if it is present
// 	    Expr expr = assumptions.get (i);

// 	    // ---
// 	    solution = vc.andExpr (solution, expr);
// 	    if (expr.isNot ())
// 	      expr = expr.get (0);
	    
// 	    CSPVariable var = (CSPVariable) exprToVarMap.get (expr);

// 	    assert var != null : 
// 	      "Could not find a variable for " + expr;
	    
// 	    set.set (var.getCurLevel ());

// 	  }

// 	if (result == Boolean.TRUE)
// 	  tExpr = vc.orExpr (tExpr, solution);
// 	else 
// 	  fExpr = vc.orExpr (fExpr, solution);
//       }

    

//     vc.popto (currentScope);
//     System.err.println ("Done with checkConsistency of " + curVar);
//     return set;
//   }



//   Boolean checkTruth (Expr target)
//   {
//     return checkTruth (target, vc.notExpr (target));
//   }
  /**
   * returns TRUE if assumptions => target,
   * returns FALSE if assumpitions => !target
   * returns null otherwise
   *
   * @param target an <code>Expr</code> value
   * @return a <code>Boolean</code> value
   */
//   Boolean checkTruth (Expr tTarget, Expr fTarget)
//   {
//     int scopeLevel = vc.scopeLevel ();

//     vc.push ();
//     boolean result = vc.query (tTarget, false);
//     if (result) return Boolean.TRUE;	
//     vc.popto (scopeLevel);
    
//     vc.push ();
//     result = vc.query (fTarget, false);
//     if (result) return Boolean.FALSE;

//     return null;
//   }

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
    //preds.add (vc.gtExpr (x, vc.ratExpr (2, 1)));
    preds.add (vc.gtExpr (y, vc.ratExpr (0, 1)));
    
    Expr x_plus_y = vc.plusExpr (x, y);
    preds.add (vc.gtExpr (x_plus_y, vc.ratExpr (0, 1)));


    
    
    Expr target = vc.gtExpr 
      (vc.plusExpr (x_plus_y, 
   		    vc.plusExpr (x_plus_y, y)), 
       vc.ratExpr (0, 1));
       //Expr target = vc.eqExpr (x, vc.ratExpr (2, 1));

    CvclCFFCPredicateAbstractor cbj = 
      new CvclCFFCPredicateAbstractor (preds, target, vc);

    edu.toronto.cs.util.StopWatch sw = new edu.toronto.cs.util.StopWatch ();
    cbj.recSearch (1);
    System.err.println ("Done in: " + sw);
    System.err.println ("QUERY: " + vc.sw);

    System.err.println ("True set is: " + cbj.tExpr);
    System.err.println ("False set is: " + cbj.fExpr);
  }
}
