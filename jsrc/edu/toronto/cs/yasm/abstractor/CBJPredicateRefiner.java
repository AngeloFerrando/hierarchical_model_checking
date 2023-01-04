package edu.toronto.cs.yasm.abstractor;


import edu.toronto.cs.yasm.refiner.*;
import edu.toronto.cs.yasm.pprogram.*;
import edu.toronto.cs.expr.*;
import edu.toronto.cs.yasm.YasmStatistics;
import edu.toronto.cs.yasm.wp.*;

import edu.toronto.cs.tp.cvcl.CVCLUtil;


import java.util.*;

public class CBJPredicateRefiner extends PredicateRefiner
{

  Map cache;

  List allPreds;
  List allCvclPreds;

  boolean doInconsitent;
  boolean doSearch;

  public CBJPredicateRefiner (ExprFactory _fac)
  {
    super (_fac);
    doInconsitent = true;
    doSearch = true;
  }

  /**
   * doInconsitent -- if true compute inconsistent cubes
   * doSearch -- if true do abstraction search
   *
   * @param _fac an <code>ExprFactory</code> value
   * @param _doInconsitent a <code>boolean</code> value
   * @param _doSearch a <code>boolean</code> value
   */
  public CBJPredicateRefiner (ExprFactory _fac, boolean _doInconsitent,
      boolean _doSearch)
  {
    super (_fac);

    doInconsitent = _doInconsitent;
    doSearch = _doSearch;
  }
  //   public CBJPredicateRefiner (ExprFactory _fac, List _newPredicates)
  //   {
  //     super (_fac, _newPredicates);
  //   }


  public PProgram doProgramRefine (PProgram p)
  {
    if (newPredicates.isEmpty () && !predicates.isEmpty ()) return p;

    // -- predicates is empty when this is the first run of this refiner
    if (predicates.isEmpty ())
      p.setInconsistent (fac.falseExpr ());

    cache = new HashMap ();

    allPreds = new ArrayList ();
    allPreds.addAll (predicates);
    allPreds.addAll (newPredicates);

    allCvclPreds = cvcl.toCVCL (allPreds);

    if (!allPreds.isEmpty () && doInconsitent)
    {

      CvclCBJPredicateAbstractor cbj = 
        new CvclCBJPredicateAbstractor (allCvclPreds,
            vc.falseExpr (),
            vc);
      cbj.recSearch (1);
      Expr inconsistent = cvcl.fromCVCL (cbj.getTExpr ());
      System.err.println ("INCONSISTENT: " + inconsistent);
      p.setInconsistent (inconsistent);
    }

    return super.doProgramRefine (p);
  }

  private CBJRefinerInfo getRefinerInfo (PrllAsmtPStmt asmt)
  {
    CBJRefinerInfo info = (CBJRefinerInfo) asmt.getRefinerInfo ();
    if (info == null)
    {
      info = new CBJRefinerInfo ();
      asmt.setRefinerInfo (info);
    }
    return info;
  }

  public MemoryModel getRegularMemoryModel (PrllAsmtPStmt asmt)
  {
    return getRefinerInfo (asmt).memoryModel;
  }

  public CVCLMemoryModel getMemoryModel (PrllAsmtPStmt asmt)
  {
    return getRefinerInfo (asmt).cvclMemoryModel;
  }

  public WPComputer getWPComputer (PrllAsmtPStmt astmt)
  {
    return getRefinerInfo (astmt).wp;
  }

  public PrllAsmtPStmt doAsmtRefine (PrllAsmtPStmt asmt)
  {
    /*
       if (asmt.getSourceBlock () == null)
       {
       System.out.println ("Got a statement with no source block");
       return asmt;
       }
     */

    // System.err.println (">>> (" + asmt.getParent ().getFunctionName () + ") " + asmt.getSourceExpr ());

    /*
       MemoryModel mmModel = getRegularMemoryModel (asmt);
       if (mmModel == null)
       {
       mmModel = computeRegularMemoryModel (asmt);
    // System.err.println ("Memory model for " + asmt.getSourceBlock ());
    // System.err.println (mmModel);

    getRefinerInfo (asmt).memoryModel = mmModel;
    getRefinerInfo (asmt).cvclMemoryModel = computeMemoryModel (asmt);
    getRefinerInfo (asmt).wp = new MemoryModelWPComputer (mmModel);
    }

    WPComputer wp = getRefinerInfo (asmt).wp;
     */

    // System.err.println ("CBJRefiner: doAsmtRefine: " + asmt.getSourceExpr ()); 

    WPComputer wp = asmt.getWPComputer ();

    //List asmts = new ArrayList ();
    List asmts;
    int start;


    /**
     ** This is a bit confusing, but this is how it works:
     ** if doSearch is true, we go over all new and old predicates
     ** and compute the new abstraction of each statement. Therefore, 
     ** we start at the first predicate and an empty list

     ** If we do not do search, then we only need to compute new information
     ** for new predicates we have. Thus we start with previous computation
     ** which is stored in 'astm.getAsmts ()', and only process new predicates
     ** that start at the index 'predicates.size ()'
     **/
    if (doSearch)
    {
      start = 0;
      asmts = new ArrayList ();
    }
    else
    {
      asmts = asmt.getAsmts ();
      start = predicates.size ();
    }

    for (int i = start; i < allPreds.size (); i++)
    {
      //Expr target = converted.get (i);

      Expr wpExpr = wp.computeWP ((Expr) allPreds.get (i));

      assert wpExpr != null : "null wp for: " + asmt;

      edu.toronto.cs.tp.cvcl.Expr target = cvcl.toCVCL (wpExpr);

      Expr[] result = null;
      if (target.equals (allCvclPreds.get (i)))
      {
        //System.err.println ("QUICKTEST1");
        result = 
          new Expr[] {fac.op (BoolOp.NOT).
            unaryApply ((Expr) allPreds.get (i)),
            (Expr) allPreds.get (i)};
      }
      else if (CVCLUtil.quickQuery (vc, target))
      {
        //System.err.println ("QUICKTEST2");
        result = new Expr[] 
        { fac.falseExpr (), fac.trueExpr () };
      }
      else if (CVCLUtil.quickQuery (vc, target.notExpr ()))
      {
        //System.err.println ("QUICKTEST3");
        result = new Expr[] 
        { fac.trueExpr (), fac.falseExpr () };
      }
      else
        result = (Expr[]) cache.get (target);

      if (result == null && doSearch)
      { 
        CvclCBJPredicateAbstractor cbj
          = new CvclCBJPredicateAbstractor (allCvclPreds, 
              target,
              vc);

        stats.startPredicateAbstraction ();
        cbj.recSearch (1);
        stats.stopPredicateAbstraction ();
        result = new Expr[] 
        {cvcl.fromCVCL ((edu.toronto.cs.tp.cvcl.Expr) cbj.getFExpr ()),
          cvcl.fromCVCL ((edu.toronto.cs.tp.cvcl.Expr) cbj.getTExpr ())};
          cache.put (target, result);
      }
      else if (result == null && !doSearch)
        result = new Expr[] { fac.falseExpr (), fac.falseExpr () };
      else
        ; //System.err.println ("CACHE HIT");

      asmts.add 
        (new AsmtPStmt 
         ((Expr) allPreds.get (i), result [1], result [0]));
    }

    asmt.setAsmts (asmts);
    return asmt;
  }


  //   private boolean quickQuery (edu.toronto.cs.tp.cvcl.Expr expr)
  //   {

  //     int scope = vc.scopeLevel ();
  //     try 
  //       {
  // 	return vc.query (expr, false);
  //       }
  //     finally 
  //       {
  // 	vc.popto (scope);
  //       }

  //   }


  public class CBJRefinerInfo
  {
    MemoryModel memoryModel;
    CVCLMemoryModel cvclMemoryModel;
    WPComputer wp;

    // -- more stuff
  }


}
