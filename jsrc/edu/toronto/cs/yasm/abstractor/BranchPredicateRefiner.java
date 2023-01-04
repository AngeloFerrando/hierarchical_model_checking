package edu.toronto.cs.yasm.abstractor;

import edu.toronto.cs.boolpg.abstraction.*;
import edu.toronto.cs.expr.*;
import edu.toronto.cs.yasm.pprogram.*;
import edu.toronto.cs.yasm.wp.*;

import java.util.*;

/**
 * LadderPredicateRefiner.java
 *
 *
 * Created: Wed Jul  7 11:27:19 2004
 *
 * @author <a href="mailto:maxin@epoch.cs">Xin Ma</a>
 * @version 1.0
 */
public class BranchPredicateRefiner extends PredicateRefiner 
{
  public BranchPredicateRefiner (ExprFactory _fac)
  {
    super (_fac);
  }
  public BranchPredicateRefiner (ExprFactory _fac, 
				 List _newPredicates)
  {
    super (_fac, _newPredicates);
  }
  public MemoryModel getRegularMemoryModel (PrllAsmtPStmt asmt)
  {
    return getRefinerInfo (asmt).memoryModel;
  }
  private StatementAbstraction getStatementAbstraction (PrllAsmtPStmt asmt)
  {
    return getRefinerInfo (asmt).sA;
  }
  
  public CVCLMemoryModel getMemoryModel (PrllAsmtPStmt asmt)
  {
    return getRefinerInfo (asmt).mModel;
  }
  
  public WPComputer getWPComputer (PrllAsmtPStmt asmt)
  {
    return getRefinerInfo (asmt).wp;
  }
  
  private  BranchRefinerInfo getRefinerInfo (PrllAsmtPStmt asmt)
  {
    BranchRefinerInfo info = (BranchRefinerInfo) asmt.getRefinerInfo ();
    if (info == null)
      {
	info = new BranchRefinerInfo();
	asmt.setRefinerInfo (info);
      }
    return info;
  }
  public PrllAsmtPStmt doAsmtRefine (PrllAsmtPStmt asmt)
  {
    System.err.println("******************************************************************************************");
    System.err.println("BLOCK REFINE STARTTTTTTTTTTTTTTTTTTTTTTTTTTTTTTTTTTTTTTTTTTTTTTTTTTTTTTTTTTTING");
    System.err.println ("Doing: " + asmt.getSourceBlock ());

    StatementAbstraction sA = getStatementAbstraction (asmt);
    if (sA == null)
      {
	sA = new StatementAbstraction (vc);
	getRefinerInfo (asmt).sA = sA;
      }

    // -- if this is true we already been here, so bail out quickly
    if (sA.getTruePreds ().size () >= predicates.size () + newPredicates.size ()) return asmt;

    int size = predicates.size ();
    List allPreds = new ArrayList ();
    allPreds.addAll (predicates);
    allPreds.addAll (newPredicates);

    MemoryModel mem = getRegularMemoryModel (asmt);
    if (mem == null)
      {
	mem = computeRegularMemoryModel (asmt);
	getRefinerInfo (asmt).memoryModel = mem;
	getRefinerInfo (asmt).mModel = computeMemoryModel (asmt);
	getRefinerInfo (asmt).wp = new MemoryModelWPComputer (mem);
      }

    

    edu.toronto.cs.yasm.util.Substitution sub = new edu.toronto.cs.yasm.util.Substitution (cvcl, fac, mem);
    
    List tPredicate = new ArrayList ();
    tPredicate.addAll (allPreds);
    
    tPredicate = sub.convert (tPredicate);
    if (tPredicate.size ()!= 0)
      System.err.println (tPredicate.get (0));
    
    stats.startPredicateAbstraction ();
    sA.branchRefinement (cvcl.toCVCL (allPreds), size, tPredicate);
    stats.stopPredicateAbstraction ();

    List asmts = new ArrayList ();

    for (int i = 0; i < allPreds.size (); i++)
      {
	asmts.add (new AsmtPStmt ((Expr)allPreds.get (i), 
				  cvcl.fromCVCL ((edu.toronto.cs.tp.cvcl.Expr)sA.getTruePreds().get (i)),
				  cvcl.fromCVCL ((edu.toronto.cs.tp.cvcl.Expr)sA.getFalsePreds().get (i))));
      }
    
    asmt.setAsmts (asmts);
    return asmt;
  }

  public class BranchRefinerInfo 
  {
    StatementAbstraction sA;
    MemoryModel memoryModel;
    CVCLMemoryModel mModel;
    WPComputer wp;
  }

}
