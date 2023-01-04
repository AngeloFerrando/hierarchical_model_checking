package edu.toronto.cs.yasm.pgenerator;

import edu.toronto.cs.tp.cvcl.*;
import edu.toronto.cs.expr.CVCLExprConverter;


import java.util.*;
import edu.toronto.cs.ctl.*;
import edu.toronto.cs.ctl.antlr.*;
import edu.toronto.cs.ctl.antlr.CTLNodeParser.*;
import edu.toronto.cs.mvset.*;
import edu.toronto.cs.modelchecker.*;
import edu.toronto.cs.cfa.CFAMvSetFactory.*;
import edu.toronto.cs.boolpg.BoolProgramCompiler;
import edu.toronto.cs.proof2.*;
import edu.toronto.cs.proof2.CTLProver.*;
import edu.toronto.cs.algebra.*;
import edu.toronto.cs.util.*;
import edu.toronto.cs.algebra.*;
import edu.toronto.cs.yasm.util.*;
import edu.toronto.cs.yasm.wp.WPComputer;
import edu.toronto.cs.yasm.pprogram.PrllAsmtPStmt;
import edu.toronto.cs.yasm.pprogram.PProgram;
import edu.toronto.cs.yasm.abstractor.PredicateRefiner;

public class AIntellegence extends BasePredicateGenerator
{
  private List property;

  /**
   * This is the property that we are asking about the whole program
   *
   */
  protected CTLNode prop;
  protected Map predMap;

  public AIntellegence (PProgram p, 
			PrllAsmtPStmt _stmt,
			PredicateRefiner _refiner,
			WPComputer _wp,
			CVectorExpr _pStatePred, 
			CVectorExpr _cStatePred, 
			CTLNode _prop, 
			Map _predMap) 
  {
    super (p, _stmt, _refiner, _wp, _pStatePred, _cStatePred);
    prop = _prop;
    predMap = _predMap;
  }


  public boolean find ()
  {
    newPreds = inducePred (0);
    return !newPreds.isEmpty ();
  }

  // optiions == 0 ---> parentState and ChildState have no difference, 
  // -- except the transitions that are true
  protected List inducePred (int options)
  {
    property = new ArrayList ();
    CTLNode walkingNode = prop;
    findProperty (walkingNode);
    
    List newPredicates = new ArrayList();

    switch(options)
      {
      case 0:
	for (Iterator it = property.iterator(); it.hasNext();)
	  {
	    int i = Util.findIndex ((Expr)it.next (), 
				    Util.fVectorToList (pStatePred));
	    Expr e = pStatePred.get(i);
	    
	    Expr e2 = computeWP (e);
// 	    Expr e2 = Util.preSubstitution(e);
// 	    Util.substituteVars(e2, symbolicMap);
// 	    e2 = vc.importExpr (e2);
	    
	    e = cvclConverter.linearize (e);
	    e2 = cvclConverter.linearize (e2);
	    
	    newPredicates.add (findSatisfiablePred (e, e2));

	  }
	return newPredicates;
      default:
	throw new RuntimeException("AI not that smart yet");
      }
  }
  
  private Expr findSatisfiablePred (Expr before, Expr after)
  {
    Expr e = Util.preSubstitution (before);
    System.out.println ("before" + before);
    System.out.println ("after" + after);
    if (before.arity() == 2)
      {
	e.get (0).assign(vc.simplify (vc.minusExpr (after.get (0), before.get (0))));
      }
    System.out.println ("After simplification");
    System.out.println(e);
    
    vc.popto (0);
    return vc.importExpr(e);
  }
  private void findProperty(CTLNode walkingNode)
  {
    if(walkingNode != null)
      {
	System.out.println(walkingNode);
	System.out.println(walkingNode.getClass());
	if (walkingNode instanceof CTLAtomPropNode)
	  {
	    property.add(predMap.get(walkingNode.toString()));
	  }
	if (! ((walkingNode instanceof CTLMvSetNode) || walkingNode instanceof CTLAtomPropNode))
	  {
	    findProperty(walkingNode.getLeft());
	    findProperty(walkingNode.getRight());
	  }
      }
    else return;
  }
}
