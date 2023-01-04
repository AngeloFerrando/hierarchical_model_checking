package edu.toronto.cs.yasm.pgenerator;

import java.util.*;

import edu.toronto.cs.tp.cvcl.*;
import edu.toronto.cs.expr.CVCLExprConverter;
import edu.toronto.cs.expr.ExprFactory;
import edu.toronto.cs.expr.BoolOp;
import edu.toronto.cs.yasm.pprogram.PProgram;
import edu.toronto.cs.yasm.pprogram.PrllAsmtPStmt;
import edu.toronto.cs.yasm.pprogram.AsmtPStmt;
import edu.toronto.cs.yasm.wp.*;
import edu.toronto.cs.yasm.abstractor.PredicateRefiner;


public class InconsistentDestinationGenerator extends BasePredicateGenerator
{
  
  public InconsistentDestinationGenerator (PProgram _pProgram,
					   PredicateRefiner _refiner,
					   CVectorExpr _cStatePred)
    
  {
    super (_pProgram, null,_refiner, null, null, _cStatePred);    
  }



  public boolean find ()
  {
    int scope = vc.scopeLevel ();
    try
      {
	// -- push current scope and assume constraints of 
	// -- the parent state
	vc.push ();
	CVCLUtil.assertFormula (vc, cStatePred);
	// -- make sure we are going to a consistent state
	return isInconsistent ();
      }
    finally
      {
	vc.popto (scope);
      }
  }

  boolean isInconsistent ()
  {
    int scope = vc.scopeLevel ();
    try
      {
	vc.push ();
	// -- if everything is consistent nothing to do
	if (!vc.query (vc.falseExpr (), false)) 
	  return false;

	Expr newInconsistent = vc.andExpr (CVCLUtil.getAssumptions (vc));
	System.err.println (this.getClass () + 
			    ": New inconsistent constraint: " + 
			    newInconsistent);
	
	// -- 
	PProgram prog = getPProgram ();
	ExprFactory fac = prog.getInconsistent ().getFactory ();
	prog.
	  setInconsistent (fac.op (BoolOp.OR).
			  binApply (prog.getInconsistent (),
				 cvclConverter.fromCVCL (newInconsistent)));
	return true;
      }
    finally
      {
	vc.popto (scope);
      }
  }
}
