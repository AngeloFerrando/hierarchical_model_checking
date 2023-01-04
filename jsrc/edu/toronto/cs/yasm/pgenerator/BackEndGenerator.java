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


public class BackEndGenerator extends BasePredicateGenerator
{

  boolean worked = false;
  public BackEndGenerator (PProgram _pProgram,
      PrllAsmtPStmt _stmt,
      PredicateRefiner _refiner,
      WPComputer _wp,
      CVectorExpr _pStatePred, 
      CVectorExpr _cStatePred)

  {
    super (_pProgram, _stmt,_refiner, _wp,_pStatePred, _cStatePred);    
  }



  public boolean find ()
  {
    int scope = vc.scopeLevel ();
    try
    {
      // -- push current scope and assume constraints of 
      // -- the parent state
      vc.push ();
      CVCLUtil.assertFormula (vc, pStatePred);
      // -- make sure we are in a consistent state
      if (isInconsistent ())
        return true;

      // -- refine the statement, as a side-effect worked
      // -- is set to true if the refinement process worked
      refineStatement ();
      return worked;
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
      // 	System.err.println ("Checking inconsistency of: " + 
      // 			    vc.andExpr (pStatePred));
      vc.push ();
      // -- if everything is consistent nothing to do
      if (!vc.query (vc.falseExpr (), false)) 
        return false;

      Expr newInconsistent = vc.andExpr (CVCLUtil.getAssumptions (vc));
      System.err.println ("New inconsistent constraint: " + 
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

  void refineStatement ()
  {
    for (Iterator it = stmt.getAsmts ().iterator (); it.hasNext ();)
    {
      AsmtPStmt asmt = (AsmtPStmt) it.next ();
      // -- get the value of current variable in the child state
      boolean childVal = 
        getChildVal (cvclConverter.toCVCL (asmt.getVar ()));


      // -- skip variables for which the next-state change is definite
      if (!childVal &&
          CVCLUtil.quickQuery (vc, 
            cvclConverter.toCVCL (asmt.getFalseCond ())))
        continue;
      else if (childVal &&
          CVCLUtil.quickQuery 
          (vc, cvclConverter.toCVCL (asmt.getTrueCond ())))
        continue;

      // -- if we got here then we can refine current assignment
      refineAsmt (asmt);
    }
  }


  boolean getChildVal (Expr expr)
  {
    for (int i = 0; i < cStatePred.size (); i++)
    {
      Expr child = cStatePred.get (i);
      if (child.isNot ()) child = child.get (0);

      if (expr.equals (child))
        return !cStatePred.get (i).isNot ();
    }

    assert false : "Was not able to find child expression for " + expr;
    throw new RuntimeException ("Should never be here");
  }
  void refineAsmt (AsmtPStmt asmt)
  {
    int scope = vc.scopeLevel ();
    try
    {
      vc.push ();

      Boolean result = 
        CVCLUtil. checkTruth (vc, 
            cvclConverter.toCVCL 
            (wp.computeWP (asmt.getVar ())));

      if (result != null)
      {
        worked = true;
        Expr assumptions = vc.andExpr (CVCLUtil.getAssumptions (vc));
        System.err.println ("Got new assumptions: " + assumptions + 
            " for " + 
            (result == Boolean.TRUE ? "" : "!") + 
            asmt.getVar ());

        ExprFactory fac = asmt.getTrueCond ().getFactory ();

        if (result == Boolean.TRUE)
          asmt.setTrueCond 
            (fac.op (BoolOp.OR).
             binApply (asmt.getTrueCond (),
               cvclConverter.fromCVCL (assumptions)));
        else
          asmt.setFalseCond 
            (fac.op (BoolOp.OR).
             binApply (asmt.getFalseCond (),
               cvclConverter.fromCVCL (assumptions)));
      }
    }
    finally
    {
      vc.popto (scope);
    }
  }

  public boolean __find ()
  {
    //return false;
    backEndRefine ();
    // XXX this is wrong, only return true if found something to fix
    return worked;
  }

  public List getNewPreds ()
  {
    return java.util.Collections.EMPTY_LIST;
  }

  public void backEndRefine ()
  {
    int scope = vc.scopeLevel ();

    assert scope == 1 : "Strange scope level: " + scope;

    System.err.println ("ASSERTING: " + vc.andExpr (pStatePred));

    vc.push ();
    CVCLUtil.assertFormula (vc, pStatePred);

    int scope2 = vc.scopeLevel ();

    for (int i = 0; i < cStatePred.size (); i++)
    {
      Expr e = cStatePred.get (i);
      for (int j = 0; j < 2; j++)
      {
        if (j == 1) e = flipExpr (e);
        assert vc.scopeLevel () == scope2 : 
          "Strange scope level: " + vc.scopeLevel ();
        vc.push ();
        if (!vc.query (computeWP (e), false))
        {
          vc.popto (scope2);
          continue;
        }
        CVectorExpr assumptions = new CVectorExpr (0);
        vc.getAssumptions (assumptions);
        Expr assumption = vc.andExpr (assumptions);
        improveAbstraction (assumption, e);
        vc.popto (scope2);
        break;
      }
    }

    vc.popto (scope);
  } 

  private Expr flipExpr (Expr expr)
  {
    return expr.isNot () ? expr.get (0) : expr.notExpr ();
  }

  private void improveAbstraction (Expr assumption, Expr e)
  {

    boolean found = false;
    boolean truePred;
    if (e.isNot ())
    {
      truePred = false;
      e = e.get (0);
    }
    else
      truePred = true;

    List asmts = stmt.getAsmts ();
    System.out.println ("Adding " + assumption + " to " + 
        (truePred ? "" : "!") + e);

    for (int i = 0; i < asmts.size (); i++)
    {
      if (e.equals 
          (cvclConverter.toCVCL (((AsmtPStmt) asmts.get (i)).getVar ())))

      {
        if (truePred && !CVCLUtil.implies 
            (vc, assumption,
             (cvclConverter.toCVCL
              (((AsmtPStmt)asmts.get (i)).getTrueCond ()))))
        {
          ((AsmtPStmt)asmts.get (i)).setTrueCond
            (cvclConverter.fromCVCL 
             (vc.orExpr 
              (cvclConverter.toCVCL
               (((AsmtPStmt)asmts.get (i)).getTrueCond ()), 
               assumption)));

          worked = true;
        }
        else if (!truePred && !CVCLUtil.implies 
            (vc, assumption,
             (cvclConverter.toCVCL
              (((AsmtPStmt)asmts.get (i)).getFalseCond ()))))
        {
          ((AsmtPStmt)asmts.get (i)).setFalseCond
            (cvclConverter.fromCVCL 
             (vc.orExpr 
              (cvclConverter.toCVCL
               (((AsmtPStmt)asmts.get (i)).getFalseCond ()), 
               assumption)));
          worked = true;
        }
        found = true;
        break;
      }
    }

    System.err.println ("Updated parallel assignment to");
    stmt.print (new java.io.PrintWriter (System.err, true));

    if (!found)
      throw new RuntimeException ("BackEndGenerator.java Error 2");
  }

}
