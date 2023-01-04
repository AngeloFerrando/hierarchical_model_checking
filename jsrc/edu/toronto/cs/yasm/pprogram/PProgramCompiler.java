package edu.toronto.cs.yasm.pprogram;

import edu.toronto.cs.util.*;
import edu.toronto.cs.algebra.*;
import edu.toronto.cs.modelchecker.*;
import edu.toronto.cs.mvset.*;
import edu.toronto.cs.ctl.*;
import edu.toronto.cs.cfa.*;
import edu.toronto.cs.cfa.CFAMvSetFactory.*;
import edu.toronto.cs.yasm.pprogram.PredicateTable.*;
import edu.toronto.cs.expr.*;

import antlr.*;
import antlr.collections.AST;

import edu.toronto.cs.cparser.*;
import edu.toronto.cs.cparser.block.*;
import edu.toronto.cs.boolpg.abstraction.*;
import edu.toronto.cs.yasm.pprogram.*;
import edu.toronto.cs.yasm.abstractor.*;

import java.io.*;
import java.util.*;

/**
 * PProgramCompiler.java
 *
 *
 * Created: Tue Jun 29 13:41:21 2004
 *
 * @author <a href="mailto:kelvin@epoch.cs">Kelvin Ku</a>
 * @version 1.0
 */
public class PProgramCompiler implements ModelCompiler
{
  CFA cfa;

  // predicate program
  PProgram pProgram;

  // -- algebra we are using
  BelnapAlgebra algebra;
  // -- transition relation we are building
  MvRelation trans;
  // -- our initial state
  MvSet init;

  // -- a way to create CFA based mv-sets
  CFAMvSetFactory cfaMvSetFactory;

  // -- a way to create MDD based mv-sets
  //MDDMvSetFactory mvSetFactory;
  //JCUDDMvSetFactory mvSetFactory;
  JCUDDBelnapMvSetFactory mvSetFactory;
  
  int maxDDVars = 20;


  // -- invariant
  MvSet invar = null;

  // -- usefull constants
  MvSet mvT;
  MvSet mvF;
  MvSet mvD;
  MvSet mvM;

  // -- all pre-state variables cube
  MvSet preVarCube;
  // -- all post-state variables cube
  MvSet postVarCube;

  int[] preToPostMap;
  int[] postToPreMap;

  // -- true if hyper edges are enabled
  boolean useHyperEdges;
  // -- true if initial values of the predicates should be treated as 
  // -- unknown
  boolean unknownInit;

  // -- an mvset for skip
  MvSet skipMvSet = null;

  public PProgramCompiler ()
  {
    this (true, true);
  }


  public PProgramCompiler (boolean _useHyperEdges, 
			   boolean _unknownInit)
  {
    algebra = new BelnapAlgebra ();
    useHyperEdges = _useHyperEdges;
    unknownInit = _unknownInit;
  }

  protected MvSet getInit ()
  {
    return init;
  }
  protected MvRelation getTrans ()
  {
    return trans;
  }


  public void setPProgram (PProgram v)
  {
    pProgram = v;
  }

  public void setCFA( CFA c){
	cfa = c;
  }

  /**
   * Describe <code>setMaxDDVars</code> method here.
   *
   * @param v an <code>int</code> value
   */
  public void setMaxDDVars (int v)
  {
    maxDDVars = v;
  }

  /**
   * Describe <code>seal</code> method here.
   *
   */
  public void seal ()
  {
    try 
    {
      if (mvSetFactory == null)
 	mvSetFactory = (JCUDDBelnapMvSetFactory)
 	  JCUDDBelnapMvSetFactory.newMvSetFactory (algebra, maxDDVars);
//         mvSetFactory = (MDDMvSetFactory)
//           MDDMvSetFactory.newMvSetFactory (algebra, maxDDVars);
//      mvSetFactory = (JCUDDMvSetFactory)
//        JCUDDMvSetFactory.newMvSetFactory (algebra, maxDDVars);

    }
    catch (Exception ex)
    {
      ex.printStackTrace ();
      throw new RuntimeException (ex);
    }
    pProgram.getPredTable ().setMvSetFactory (mvSetFactory);

    mvT = mvSetFactory.top ();
    mvF = mvSetFactory.bot ();
    mvD = mvSetFactory.infoTop ();
    mvM = mvSetFactory.infoBot ();

    PredicateTable pTable = pProgram.getPredTable ();
    preVarCube = mvSetFactory.buildCube (pTable.getVariableIds (0));
    postVarCube = mvSetFactory.buildCube (pTable.getVariableIds (1));
    preToPostMap = pTable.variableMap (0, 1);
    postToPreMap = pTable.variableMap (1, 0);

    //     System.out.println ("current state vars: " + 
    // 			ArrayUtil.toString (pTable.getVariableIds (0)));
    //     System.out.println ("next-state vars: " + 
    // 			ArrayUtil.toString (pTable.getVariableIds (1)));
    //     System.out.println ("preToPost: " + 
    // 			ArrayUtil.toString (preToPostMap));
    //     System.out.println ("postToPre: " + 
    // 			ArrayUtil.toString (postToPreMap));

    //     System.out.println ("variables we have");
    //     for (Iterator it = pTable.getVariables ().iterator (); it.hasNext ();)
    //       {
    // 	System.out.println (it.next ());
    //       }

    skipMvSet = null;
  }

  /**
   * Describe <code>compile</code> method here.
   *
   * @return a <code>XKripkeStructure</code> value
   */
  public XKripkeStructure dammiKripke(){
	
	cfaMvSetFactory = new CFAMvSetFactory (mvSetFactory, cfa);
    	trans = cfaMvSetFactory.getMvRelation ();
	MvSet initStates = mvSetFactory.top ();
    	if (invar != null)
		initStates = initStates.and (invar);
	init = cfaMvSetFactory.embed (0, initStates);
	PredicateTable pTable = pProgram.getPredTable ();

   	return new XKripkeStructure (trans,init,preToPostMap,cfaMvSetFactory.embedMvSet (postVarCube),cfaMvSetFactory.embedMvSet (preVarCube),      null,algebra,pTable.getNumVars (),pTable.getNumVars (),getCtlReWriter (),getStatePresenter ());

  }
  
  public void creaArchi(ArrayList c){
	
	seal ();

	if (pProgram.getInconsistent () != null)
      		invar = exprToMvSet (pProgram.getInconsistent ()).eq (mvT).not ();


	for (Iterator it = pProgram.getStmtList ().iterator (); it.hasNext ();){
      		PStmt pStmt = (PStmt) it.next ();
      		if (pStmt instanceof FunctionCallPrologue){
        		handleFunctionCall (cfa, (FunctionCallPrologue) pStmt);
			//System.out.println("funcallpro: "+pStmt.getLabel());
      		}
      		else if (pStmt instanceof ReturnSelectorPStmt){
        		handleReturnSelector (cfa, (ReturnSelectorPStmt) pStmt);
			//System.out.println("retsel: "+pStmt.getLabel());
      		} 	
      		else if (pStmt instanceof SkipPStmt){
        		handleSkip (cfa, (SkipPStmt) pStmt);
			//System.out.println("skip: "+pStmt.getLabel());
      		}		
      		else if (pStmt instanceof IfPStmt){
        		handleIf (cfa, (IfPStmt) pStmt);
			//System.out.println("if: "+pStmt.getLabel());
      		}
      		else if (pStmt instanceof PrllAsmtPStmt){
        		handleAssign (cfa, (PrllAsmtPStmt) pStmt);
			//System.out.println("prll: "+pStmt.getLabel());
      		}
      		else if (pStmt instanceof GotoPStmt){
        		handleGoto (cfa, (GotoPStmt) pStmt);
			//System.out.println("goto: "+pStmt.getLabel());
      		}
      		else if (pStmt instanceof NDGotoPStmt){
       			handleNDGoto (cfa, (NDGotoPStmt) pStmt);
			//System.out.println("ndgoto: "+pStmt.getLabel());
     		}
      		else
        		throw new RuntimeException ("Unable to handle " + pStmt.getClass ());
    	}

   }



  public XKripkeStructure compile ()
  {
    seal ();
    cfa = buildCFA ();
  
    // XXX DEBUG CODE
    //cfa.dumpNodes ();
    cfa.dumpEdges ();
    // XXX

    cfaMvSetFactory = new CFAMvSetFactory (mvSetFactory, cfa);
    trans = cfaMvSetFactory.getMvRelation ();

    // XXX figure out what init is, for now any variable assignment
    // XXX is deemed possible
    MvSet initStates = mvSetFactory.top ();
    
    // -- if there is an invariant we must intersect init with it
    if (invar != null)
	initStates = initStates.and (invar);

    init = cfaMvSetFactory.embed (0, initStates);
    PredicateTable pTable = pProgram.getPredTable ();

    return new XKripkeStructure (trans,
        init,
        preToPostMap,
        cfaMvSetFactory.embedMvSet (postVarCube),
        cfaMvSetFactory.embedMvSet (preVarCube),
        null,
        algebra,
        pTable.getNumVars (),
        pTable.getNumVars (), 
        getCtlReWriter (),
        getStatePresenter ());
  }

  public CFA getCFA(){
	return cfa;
  }
  
  private CTLNode handleUnknownVariable (String name)
  {

    // -- unknown variable is possibly an algebraic constant
    AlgebraValue value = algebra.getValue (name);
    if (value == algebra.noValue ())
      // -- error handling for unknown variables
      throw new RuntimeException ("Unknown variable: " + name);

    // -- value is valid, so create appropriate CTL for it
    return CTLFactory.createCTLConstantNode (value);
  }


  public CTLReWriter getCtlReWriter ()
  {
    return new CloningRewriter ()
    {
      public Object visitAtomPropNode (CTLAtomPropNode ctl, Object o)
      {
        // -- already done
        if (ctl.getMvSet () != null) return ctl;

        // -- see if we know a predicate with that name
        Predicate pred = 
          pProgram.getPredTable ().getByCtlName (ctl.getName ());

        // -- if we do not, do something bad
        if (pred == null) return handleUnknownVariable (ctl.getName ());


        // -- set mv-set
        ctl.setMvSet (cfaMvSetFactory.embedMvSet (pred.getMvSet ()));


        return ctl;
      }

      public Object visitEqualsNode (CTLEqualsNode ctl, Object o)
      {
        // -- for equals node we first check if its left 
        // -- hand side is a variable name, in which case the 
        // -- right hand side should be a value from an enum type

        if (ctl.getLeft ().getClass () != CTLAtomPropNode.class ||
            ctl.getRight ().getClass () != CTLAtomPropNode.class)
          return super.visitEqualsNode (ctl, o);



        // -- atom = atom   case, assume variable is always on the left
        CTLAtomPropNode left = (CTLAtomPropNode)ctl.getLeft ();
        CTLAtomPropNode right = (CTLAtomPropNode)ctl.getRight ();

        if (!left.getName ().equals ("pc"))
          return super.visitEqualsNode (ctl, o);

        // -- get value from '='
        String value = right.getName ();


        PStmt pstmt = pProgram.getStmt (value);
        if (pstmt == null) 
          throw new RuntimeException ("Unknown statement label: " + value);


        MvSet result = cfaMvSetFactory.embed (pstmt.getId (), 
            mvSetFactory.top ());

        // -- create new CTLNode to represent this equality
        CTLMvSetNode mvSetNode = CTLFactory.createCTLMvSetNode (result);
        // -- set the name of this mv-set node so it will print correctly
        mvSetNode.setName (ctl.toString ());
        return mvSetNode;
      }
    };  
  }  

  // -- returns a state presenter that interpets a value assignment
  // -- with respect to variables in this symbol table
  public StatePresenter getStatePresenter ()
  {
    return new StatePresenter ()
    {
      public CTLNode[] toCTL (AlgebraValue[] state)
      {
        // XXX Don't know how to handle this yet
        throw new UnsupportedOperationException ();
      }
      // XXX Don't know if we ever want this at all!
      public CTLNode[][] toCTL (AlgebraValue[][] states)
      {
        throw new UnsupportedOperationException ();
      }


      public CTLNode[] toCTL (MvSet _cube)
      {
        CFAMvSet cube = (CFAMvSet)_cube;
        int nodeId = firstNonFalse (cube.getCFA ());
        assert nodeId != -1;

        CFA stateCFA = cube.getCFA ();
	MvSet mvSetCube;
	if(stateCFA.getNode (nodeId) instanceof CFA.CFANode) 
        	mvSetCube = ( (CFA.CFANode) stateCFA.getNode (nodeId) ).getState ();
	else
		mvSetCube = ( (CFA.CFABox) stateCFA.getNode (nodeId) ).getState ();
        AlgebraValue[] values = (AlgebraValue[]) 
          mvSetCube.cubeIterator ().next ();

	//System.out.println ("toCTL: " + Arrays.asList (values));

        List result = new LinkedList ();
	if(stateCFA.getNode (nodeId) instanceof CFA.CFANode) 
        	result.add (CTLFactory.createCTLAtomPropNode ("pc").eq(CTLFactory.createCTLAtomPropNode ( ( (CFA.CFANode)stateCFA.getNode (nodeId) ).getStrValue () ) ) );
	else
		result.add (CTLFactory.createCTLAtomPropNode ("pc").eq(CTLFactory.createCTLAtomPropNode ( ( (CFA.CFABox)stateCFA.getNode (nodeId) ).getStrValue () ) ) );

        for (Iterator it = 
            pProgram.getPredTable ().getVariables ().iterator (); 
            it.hasNext ();)
        {
          Predicate var = (Predicate) it.next ();
          if (var.isShadow ()) continue;
          CTLNode ctl = var.toCTL (values);
          if (ctl != null) result.add (ctl);
        }
        return (CTLNode[])result.toArray (new CTLNode [result.size ()]);
      }


	private int firstNonFalse (CFA cfa)
	{
	  MvSet bot = mvSetFactory.bot ();
	  for (int i = 0; i < cfa.nodeSize (); i++)
	    {
		if(cfa.getNode (i) instanceof CFA.CFANode){
	      		CFA.CFANode node = (CFA.CFANode) cfa.getNode (i);
			if (!node.getState ().equals (bot))
				return node.getId ();
		}
		else {
			CFA.CFABox box = (CFA.CFABox) cfa.getNode (i);
	      		if (!box.getState ().equals (bot))
				return box.getId ();
		}
	    }
	  return -1;
	}


    };
  }

  /***********************************************
   ************************************************************
   */

  private CFA buildCFA ()
  {
    // XXX Should compute CFG size here (when function calls are handled)

    if (pProgram.getInconsistent () != null)
      invar = exprToMvSet (pProgram.getInconsistent ()).eq (mvT).not ();

    // -- create the CFA
    CFA cfa = new CFA (pProgram.getStmtList ().size ());

    // -- add nodes and boxes
    for (Iterator it = pProgram.getStmtList ().iterator (); it.hasNext ();){
	String s = ((PStmt) it.next ()).getLabel ();

	if( s.startsWith("Box") )
		cfa.addBox(s,mvSetFactory.top());	
	else
      		cfa.addNode (s, mvSetFactory.top ());
    }

    // -- now we need to add the edges
    for (Iterator it = pProgram.getStmtList ().iterator (); it.hasNext ();)
    {
      PStmt pStmt = (PStmt) it.next ();
      if (pStmt instanceof FunctionCallPrologue){
        handleFunctionCall (cfa, (FunctionCallPrologue) pStmt);
	//System.out.println("funcallpro: "+pStmt.getLabel());
      }
      else if (pStmt instanceof ReturnSelectorPStmt){
        //handleReturnSelector (cfa, (ReturnSelectorPStmt) pStmt);
	cfa.addEdge ("goto", pStmt.getId (), pStmt.getDest ().getId (),new SkipMvRelation (null));
	//System.out.println("retsel: "+pStmt.getLabel());
      } 	
      else if (pStmt instanceof SkipPStmt){
        handleSkip (cfa, (SkipPStmt) pStmt);
	//System.out.println("skip: "+pStmt.getLabel());
      }		
      else if (pStmt instanceof IfPStmt){
        handleIf (cfa, (IfPStmt) pStmt);
	//System.out.println("if: "+pStmt.getLabel());
      }
      else if (pStmt instanceof PrllAsmtPStmt){
        handleAssign (cfa, (PrllAsmtPStmt) pStmt);
	//System.out.println("prll: "+pStmt.getLabel());
      }
      else if (pStmt instanceof GotoPStmt){
        handleGoto (cfa, (GotoPStmt) pStmt);
	//System.out.println("goto: "+pStmt.getLabel());
      }
      else if (pStmt instanceof NDGotoPStmt){
        handleNDGoto (cfa, (NDGotoPStmt) pStmt);
	//System.out.println("ndgoto: "+pStmt.getLabel());
      }
      else
        throw new RuntimeException ("Unable to handle " + pStmt.getClass ());
    }
	System.out.println("NODI E BOX DEL CFA");
	System.out.println(" ");
	ArrayList v = cfa.getNodes();
	for(int i=0;i<v.size();i++){
			if(v.get(i) instanceof CFA.CFANode){
				CFA.CFANode n = (CFA.CFANode) v.get(i);
				String s1 = (String) (n.getStrValue());
				System.out.println("Nodo: "+n.getId()+" Label: "+s1);
				ArrayList fwd_n = (ArrayList) cfa.getFwdEdges(n);
				for(int y=0;y< fwd_n.size();y++){
					CFA.CFAEdge e = (CFA.CFAEdge) fwd_n.get(y);	
					System.out.println("Archi uscenti: "+e.getDestId());
				}
				fwd_n = (ArrayList) cfa.getBwdEdges(n);
				for(int y=0;y< fwd_n.size();y++){
					CFA.CFAEdge e = (CFA.CFAEdge) fwd_n.get(y);	
					System.out.println("Archi entranti: "+e.getSourceId());
				}
				System.out.println(" ");
			}
			else{
				CFA.CFABox b = (CFA.CFABox) v.get(i);
				String s1 = (String) (b.getStrValue());
				System.out.println("Box: "+b.getId()+" Label: "+s1);
				ArrayList fwd_b = (ArrayList) cfa.getFwdEdges(b);
				for(int y=0;y< fwd_b.size();y++){
					CFA.CFAEdge e = (CFA.CFAEdge) fwd_b.get(y);	
					System.out.println("Archi uscenti: "+e.getDestId());
				}
				fwd_b = (ArrayList) cfa.getBwdEdges(b);
				for(int y=0;y< fwd_b.size();y++){
					CFA.CFAEdge e = (CFA.CFAEdge) fwd_b.get(y);	
					System.out.println("Archi entranti: "+e.getSourceId());
				}
				System.out.println(" ");
			}
				
		}
    return cfa;
  }

  private void handleAssign (CFA cfa, PrllAsmtPStmt assignPStmt)
  {
    cfa.addEdge ("assign", assignPStmt.getId (), 
        assignPStmt.getDest ().getId (), 
        relnAssign (assignPStmt));
  }

  private MvRelation relnAssign (PrllAsmtPStmt assign)
  {
    MvSet result = mvT;

    List asmts = assign.getAsmts ();

    for (Iterator it = asmts.iterator (); it.hasNext();)
    {
      AsmtPStmt asmt = (AsmtPStmt) it.next ();
      MvSet var = exprToMvSet (asmt.getVar ());
      MvSet tCond = exprToMvSet (asmt.getTrueCond ());
      MvSet fCond = exprToMvSet (asmt.getFalseCond ());
      result = result.and (doAssign (var, tCond, fCond));
    }

    return toReln (result);
  }

  private void handleIf (CFA cfa, IfPStmt ifStmt)
  {
    assert ifStmt.getCond ().getCond () != null 
      : "if without an abstracted condition! " + ifStmt;

    assert ifStmt.getThenGoto () != null
      : "if without a then goto " + ifStmt;  

    assert ifStmt.getElseGoto () != null
      : "if without a else goto " + ifStmt;  
    
//     System.err.println (">>> handleIf " + ifStmt);  

    MvSet cond = exprToMvSet (ifStmt.getCond ().getCond ());
    
    cfa.addEdge ("if-then", ifStmt.getId (),
		 ifStmt.getThenGoto ().getDest ().getId (), 
		 //new CondMvRelation (invar, cond));
		 doIteBranch (cond));

//         System.out.println ("handle-if: " + ifStmt);
//         ifStmt.printMe ();
//         System.out.println (ifStmt.getElseStmt ());
    cfa.addEdge ("if-else", ifStmt.getId (),
		 ifStmt.getElseGoto ().getDest ().getId (), 
		 //new CondMvRelation (invar, cond.not ()));
		 doIteBranch (cond.not ()));
//     if (useHyperEdges) 
//       cfa.addEdge("if-join", ifStmt.getId (), 
//           ifStmt.getThenGoto ().getDest ().getId (),
//           toReln (doJoinBranch (cond)), 
//           ifStmt.getElseGoto ().getDest ().getId ());
  }


  private void handleGoto (CFA cfa, GotoPStmt gotoStmt)
  {
    cfa.addEdge ("goto", gotoStmt.getId (), gotoStmt.getDest ().getId (),
		 new SkipMvRelation (null));
		 //toReln (doSkip ()));
  }

  private void handleNDGoto (CFA cfa, NDGotoPStmt s)
  {
    for (Iterator it = s.getDests ().iterator (); it.hasNext ();)
      cfa.addEdge ("nd-goto", s.getId (),
                   ((PStmt) it.next ()).getId (), toReln (doSkip ()));
  }

  private void handleInit (CFA cfa, SkipPStmt initSkip)
  {
    cfa.addEdge ("init", initSkip.getId (), initSkip.getDest ().getId (), 
        toReln (doInit ()));
  }

  private void handleSkip (CFA cfa, SkipPStmt skip)
  {

    if (skip.getLabel ().equals ("init"))
      handleInit (cfa, skip);
    else if (skip.getDest () != null)
      // XXX Previously assumed that skip always had a successor
      //cfa.addEdge ("skip", skip.getId (), skip.getDest ().getId (), 
      //toReln (doSkip ()));
      cfa.addEdge ("skip", skip.getId (), skip.getDest ().getId (), 
		   new SkipMvRelation (null));
  }

  private void handleFunctionCall (CFA cfa, FunctionCallPrologue fpro)
  {
    PStmt stmt = fpro.getCall ().getFunctionDef ().getReturnSelector ();

    if (!(stmt instanceof ReturnSelectorPStmt))
      handleAssign (cfa, fpro);
    else
      cfa.addEdge ("function call to " +
          fpro.getCall ().getFunctionDef ().getFunctionName (),
          fpro.getId (),
          fpro.getDest ().getId (),
          doFunctionCall (fpro));
  }

  private MvRelation doFunctionCall (FunctionCallPrologue fpro)
  {
    ReturnSelectorPStmt sel = (ReturnSelectorPStmt)
      fpro.getCall ().getFunctionDef ().getReturnSelector ();

    // System.out.println ("doFunctionCall: " + fpro.getLabel () + " " +
    //                    fpro.getCall ().getCallIndex ());  

    return
      new FunctionCallMvRelation
        (null,
         sel.getReturnIndex ().eq (fpro.getCall ().getCallIndex ()),
         sel.getReturnIndex ().getBitCube (),
         relnAssign (fpro));
  }

  private void handleReturnSelector (CFA cfa, ReturnSelectorPStmt sel)
  {
    int i = 0;
    for (Iterator it = sel.getDests ().iterator (); it.hasNext (); i++)
    {
      PStmt dest = (PStmt) it.next ();
      // System.out.println ("handleReturnSelector: " + dest.getLabel () + " " +
      // i);
      cfa.addEdge ("return to " + dest.getLabel (), sel.getId (),
                   dest.getId (), doReturnSelector (sel, i));
    }
  }

  private MvRelation doReturnSelector (ReturnSelectorPStmt sel, int index)
  {
    return
      new ReturnSelectorMvRelation (null, sel.getReturnIndex ().eq (index),
                                    sel.getReturnIndex ().getBitCube ());
  }

  private MvSet doInit ()
  {
    MvSet init = mvT;

    for (Iterator it = pProgram.getPredTable ().getVariables ().iterator ();
        it.hasNext ();)
    {
      Predicate var = (Predicate) it.next ();
      if (var.isShadow ()) continue;

      if (var instanceof IntVariable) continue;

      if (unknownInit)
	// -- initially all variables are unknown
	init = init.and (doAssign (var.getMvSet (), mvF, mvF));
      else
	// -- initially all variables are set non-deterministically
	init = init.and (doAssign (var.getMvSet (), mvT, mvF).or
			 (doAssign (var.getMvSet (), mvF, mvT)));
    }
    return init;
  }

  private MvRelation doIteBranch (MvSet cond)
  {
    return 
      toReln (currToNext (cond.eq (mvF).not ()).and (cond.and (doSkip ())));
    
  }

  private MvSet doJoinBranch (MvSet cond)
  {
    return currToNext (cond.eq (mvM)).and (doSkip ());
  }

  private MvSet doSkip ()
  {
    if (skipMvSet != null) 
      return skipMvSet;
    
    MvSet skip = mvT;

    for (Iterator it = pProgram.getPredTable ().getVariables ().iterator (); 
        it.hasNext ();)
    {
      Predicate var = (Predicate)it.next ();
      if (var.isShadow ()) continue;

      if (var instanceof IntVariable) continue;

      MvSet curr = var.getMvSet ();
      MvSet next = currToNext (curr);

      next = next.eq (mvT).and (curr).or
        (next.eq (mvF).and (curr.not ())).or 
        (next.eq (mvM).and (mvD));

      skip = skip.and (next);
    }

//      System.out.println (skip);
//      System.exit (1);
    skipMvSet = skip;
    return skipMvSet;
  }

  MvSet doAssign (MvSet var, MvSet tExp, MvSet fExp)
  {
    MvSet nextVar = currToNext (var);

    MvSet leftBranch;

    leftBranch = nextVar.eq (mvT).and 
      (tExp.infoAnd (mvT).infoOr 
       (fExp.infoAnd (mvT).not ()));

    MvSet rightBranch;
    rightBranch = nextVar.eq (mvF).and 
      (fExp.infoAnd (mvT).infoOr
       (tExp.infoAnd (mvT).not ()));

    return leftBranch.or 
      (rightBranch.or 
       (nextVar.eq (mvM).and (mvD)));

  }

  private MvRelation toReln (MvSet reln)
  {
    //skipMvSet = doSkip();
    if( reln.equals (skipMvSet) )
      return new SkipMvRelation (invar);
    else
      return reln.toMvRelation (invar, preVarCube, postVarCube,
				preToPostMap, postToPreMap);
    
    //return reln.toMvRelation (invar, preVarCube, postVarCube,
		//	      preToPostMap, postToPreMap);
  }

  private MvSet currToNext (MvSet v)
  {
    return v.renameArgs (preToPostMap);
  }

  private MvSet exprToMvSet (Expr expr)
  {

    Predicate p = pProgram.getPredTable ().getByName (expr);
    if (p != null) return p.getMvSet ();

    //System.err.println ("LOOKUP2 " + expr + " in " + 
    // pProgram.getPredTable ());

    if (expr.op () == BiLatticeOp.TTOP || expr.op () == BoolOp.TRUE)
      return mvT;
    else if (expr.op () == BiLatticeOp.TBOT || expr.op () == BoolOp.FALSE)
      return mvF;
    else if (expr.op () == BiLatticeOp.IBOT)
      return mvM;
    else if (expr.op () == BiLatticeOp.ITOP)
      return mvD;
    else if (expr.op () == BoolOp.NOT)
      return exprToMvSet (expr.arg (0)).not ();
    else if (expr.op () == BoolOp.AND)
    {
      MvSet result = mvT;
      for (int i = 0; i < expr.arity (); i++)
        result = result.and (exprToMvSet (expr.arg (i)));
      return result;
    }
    else if (expr.op () == BoolOp.OR)
    {
      MvSet result = mvF;
      for (int i = 0; i < expr.arity (); i++)
        result = result.or (exprToMvSet (expr.arg (i)));
      return result;
    }
    else
    {
      pProgram.getPredTable ().dump ();
      System.err.println ("expr is: " + expr);
      System.err.println ("expr's hashcode is: " + expr.hashCode ());
      throw new RuntimeException ("Something is wrong: " + expr);
    }
  }


  /**
   * Test code
   *
   * @param args a <code>String[]</code> value
   */
 /* public static void main (String[] args) throws Exception
  {
    String fileName = args [0];

    PProgram pProgram = PProgram.parse 
      (new NullExprAbstractor (new ExprFactoryImpl ()), fileName);


    PProgramCompiler compiler = new PProgramCompiler ();
    compiler.setPProgram (pProgram);
    compiler.setMaxDDVars (1);
    System.out.println ("Compiling to XKripke structure");
    XKripkeStructure xkripke = compiler.compile ();
    System.out.println ("Done");
  }  */
}
