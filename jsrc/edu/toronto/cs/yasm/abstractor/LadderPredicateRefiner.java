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
public class LadderPredicateRefiner extends PredicateRefiner 
{
  public LadderPredicateRefiner (ExprFactory _fac)
  {
    super (_fac);
  }
  public LadderPredicateRefiner (ExprFactory _fac, 
				 List _newPredicates)
  {
    super (_fac, _newPredicates);
  }


  public CVCLMemoryModel getMemoryModel (PrllAsmtPStmt asmt)
  {
    return getRefinerInfo (asmt).memoryModel;
  }

  public WPComputer getWPComputer (PrllAsmtPStmt asmt)
  {
    return getRefinerInfo (asmt).wp;
  }

  private Ladder getLadder (PrllAsmtPStmt asmt)
  {
    return getRefinerInfo (asmt).ladder;
  }
  private LadderRefinerInfo getRefinerInfo (PrllAsmtPStmt asmt)
  {
    LadderRefinerInfo info = (LadderRefinerInfo) asmt.getRefinerInfo ();
    if (info == null)
      {
	info = new LadderRefinerInfo ();
	asmt.setRefinerInfo (info);
      }
    return info;
  }

  public PrllAsmtPStmt doAsmtRefine (PrllAsmtPStmt asmt)
  {
    
    CVCLMemoryModel memoryModel = getMemoryModel (asmt);
    if (memoryModel == null)
      {
	memoryModel = computeMemoryModel (asmt);
	getRefinerInfo (asmt).memoryModel = memoryModel;
	getRefinerInfo (asmt).wp = 
	  new MemoryModelWPComputer (computeRegularMemoryModel (asmt));
      }


    
    // -- create a new ladder
     Ladder ladder = getLadder (asmt);
     if (ladder == null)
       {
 	ladder = new Ladder ();
 	getRefinerInfo (asmt).ladder = ladder;
       }

    // ****************************************************************************************************************
     List oldCVCLPred = getRefinerInfo (asmt).oldCVCLPred;
     if (oldCVCLPred.size () == getPredicates ().size ())
       for (Iterator it = newPredicates.iterator (); it.hasNext ();)
 	{
 	  edu.toronto.cs.tp.cvcl.Expr newCVCL = cvcl.toCVCL((Expr)it.next());
 	  oldCVCLPred.add(newCVCL);
 	}

     System.err.println("******************************************************************************************");
     System.err.println("BLOCK REFINE STARTTTTTTTTTTTTTTTTTTTTTTTTTTTTTTTTTTTTTTTTTTTTTTTTTTTTTTTTTTTING");
     Ladder.sCompute(vc, oldCVCLPred, memoryModel, ladder);
    
    
     List asmts = new ArrayList ();

     List allPred = new ArrayList ();
     allPred.addAll (predicates);
     allPred.addAll (newPredicates);

     for (int i = 0; i < allPred.size (); i++)
       {
 	edu.toronto.cs.tp.cvcl.Expr[] hFunc =
 	  ladder.getAbstraction (i, getVC ());
 	asmts.add (new AsmtPStmt ((Expr)allPred.get (i), 
 				   cvcl.fromCVCL (hFunc [0]),
 				   cvcl.fromCVCL (hFunc [1])));
     }
     //******************************************************************************************************************

   //  int size = predicates.size ();
//     List allPreds = new ArrayList ();
//     allPreds.addAll (predicates);
//     allPreds.addAll (newPredicates);

//     MemoryModel mem = 
//       computeRegularMemoryModel (asmt);

    
//     edu.toronto.cs.yasm.util.Substitution sub = new edu.toronto.cs.yasm.util.Substitution (cvcl, fac, mem);
    
//     List tPredicate = new ArrayList ();
//     tPredicate.addAll (allPreds);

//     tPredicate = sub.convert (tPredicate);
//     if (tPredicate.size ()!= 0)
//       System.out.println (tPredicate.get (0));
    
//     if (sA == null)
//       {
// 	sA = new StatementAbstraction (vc);
//       }
//     sA.branchRefinement (cvcl.toCVCL (allPreds), size, tPredicate);

//     List asmts = new ArrayList ();

//     for (int i = 0; i < allPreds.size (); i++)
//       {
// 	asmts.add (new AsmtPStmt ((Expr)allPreds.get (i), 
// 				 cvcl.fromCVCL ((edu.toronto.cs.tp.cvcl.Expr)sA.getTruePreds().get (i)),
// 				 cvcl.fromCVCL ((edu.toronto.cs.tp.cvcl.Expr)sA.getFalsePreds().get (i))));
//       }
    
    asmt.setAsmts (asmts);
    return asmt;
  }

//   public StatementAbstraction sA;

  public class LadderRefinerInfo 
  {
    Ladder ladder;
    List oldCVCLPred = new ArrayList ();
    CVCLMemoryModel memoryModel;
    WPComputer wp;
  }
  
} // LadderPredicateRefiner
