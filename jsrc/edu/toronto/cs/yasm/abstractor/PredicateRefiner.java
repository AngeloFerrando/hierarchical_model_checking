package edu.toronto.cs.yasm.abstractor;

import edu.toronto.cs.cparser.block.*;
import edu.toronto.cs.cparser.*;
import edu.toronto.cs.expr.*;
import edu.toronto.cs.yasm.pprogram.*;

import edu.toronto.cs.tp.cvcl.CVCLUtil;
import edu.toronto.cs.tp.cvcl.ValidityChecker;

import edu.toronto.cs.boolpg.abstraction.StmtAbstraction;

import edu.toronto.cs.yasm.wp.*;

import antlr.*;

import java.util.*;
import java.io.*;

public class PredicateRefiner implements Refiner
{
  public edu.toronto.cs.yasm.YasmStatistics stats;

  List predicates; // e.t.c.expr.Expr's
  List newPredicates; // e.t.c.expr.Expr's

  ExprFactory fac;
  CVCLExprConverter cvcl;
  ValidityChecker vc;

  // DeclsParser declsParser;
  // SymbolicExecutor symbolicExecutor;

  // -- marks whether a PProgram component has been refined already
  Map isRefined;

  Expr unknownCond;
  Object cvclUnknownCond;

  public PredicateRefiner (ExprFactory _fac)
  {
    this (_fac, new ArrayList ());
  }

  public PredicateRefiner (ExprFactory _fac, List _newPredicates)
  {
    fac = _fac;

    predicates = new ArrayList ();
    newPredicates = new ArrayList ();
    addToNewPredicates (_newPredicates);

    vc = CVCLUtil.newValidityChecker ();

    // XXX will have to modify CVCLExprConverter to use vc only (no map)
    cvcl = new CVCLExprConverter (fac, vc, null);

    isRefined = new IdentityHashMap ();
  }

  public List getPredicates ()
  {
    return predicates;
  }

  public List getNewPredicates ()
  {
    return newPredicates;
  }


  public ValidityChecker getVC ()
  {
    return vc;
  }

  public CVCLExprConverter getCVCLConverter ()
  {
    return cvcl;
  }

  public void addToNewPredicates (List l)
  {
    isRefined = new IdentityHashMap (); // all nodes to be refined again

    for (Iterator it = l.iterator (); it.hasNext ();)
      addToNewPredicates ((Expr) it.next ());
  }

  public void addToNewPredicates (Expr p)
  {
    isRefined = new IdentityHashMap (); // all nodes to be refined again

    // -- only add predicates which are not already in newPredicates
    if (!newPredicates.contains (p))
      newPredicates.add (p);
    else
      System.err.println ("Refiner: addToNewPredicates: Not adding " +
                          "existing predicate: " + p);
  }

  /** 
   * @deprecated Use addToNewPredicates
   */
  public void addPredicates (List l)
  {
    addToNewPredicates (l);
  }

  /** 
   * @deprecated Use addToNewPredicates
   */
  public void addPredicate (Expr p)
  {
    addToNewPredicates (p);
  }

  public PProgram doProgramRefine (PProgram p)
  {
    assert p.isDeclsRefined () == true
      : "Decls not refined; cannot refine PProgram";

    // -- refine each function
    for (Iterator it = p.getFunctionDefs ().values ().iterator ();
        it.hasNext ();)
      doFunctionRefine ((PFunctionDef) it.next ());

    // -- add new predicates to PProgram
    for (Iterator it = newPredicates.iterator (); it.hasNext(); )
      p.getPredTable ().newPredicate ((Expr) it.next ());

    // -- add new predicates to Refiner (this)
    predicates.addAll (newPredicates);

    // -- reset new predicates
    newPredicates = new ArrayList ();

    return p;
  }

  public void doDeclsRefine (PProgram p)
  {
    // -- process global declarations
    for (Iterator it = p.getGlobalDecls ().values ().iterator ();
        it.hasNext ();)
      doDeclRefine ((PDecl) it.next ());

    // -- process local declarations
    for (Iterator it = p.getFunctionDefs ().values ().iterator ();
        it.hasNext ();)
      doFunctionDeclsRefine ((PFunctionDef) it.next ());

    // -- make unknown if-condition
    unknownCond = fac.op (ComparisonOp.EQ).binApply
      (fac.var ("__UNKNOWN__"), fac.intExpr (0));
    cvclUnknownCond = vc.varExpr ("__UNKNOWN__", vc.intType ());  

    p.setDeclsRefined (true);  
  }

  public void doFunctionDeclsRefine (PFunctionDef f)
  {
    // -- process local declarations
    for (Iterator it = f.getLocalDecls ().values ().iterator ();
        it.hasNext ();)
      doDeclRefine ((PDecl) it.next ());

    // -- create return index CVCL Expr
    f.getReturnIndexDecl ().setCVCLExpr
      (vc.varExpr
       (f.getFunctionName () + "::@return_index", vc.intType ()));

    edu.toronto.cs.tp.cvcl.Type returnType;  

    // -- create return value CVCL Expr
    try
    {
      /*
      returnType =
        declsParser.declSpecifiers (f.getFunctionDeclSpecifiers (), vc);
      */
      returnType = f.getReturnType (vc);
    }
    catch (RecognitionException ex)
    {
      ex.printStackTrace ();
      throw new RuntimeException
        ("Error parsing return type for function " + f.getFunctionName ());
    }

    f.getReturnValueDecl ().setCVCLExpr
      (vc.varExpr
       (f.getFunctionName () + "::@return_value", returnType));  
  }

  public boolean hasNewPredicates ()
  {
    return !newPredicates.isEmpty ();
  }

  /** 
   * Registers a variable v corresponding to <code>pDecl</code> with the vc
   * and also stores a reference to v in pDecl (to prevent CVCL from garbage
   * collecting v).
   * 
   * @param pDecl the declaration to process.
   */
  public void doDeclRefine (PDecl pDecl)
  {
    pDecl.computeCVCL (vc);
  }

  public PFunctionDef doFunctionRefine (PFunctionDef f)
  {
    // -- refine the body
    f.setHead (doInsideFunctionRefine (f.getHead ()));

    return f;
  }

  public PStmt doInsideFunctionRefine (PStmt s)
  {
    if (s == null)
      return null;

    PStmt refinedStmt;    

    if (!isRefined.containsKey (s))
    {
      // have not refined this node on this refinement pass; remember that  
      // we've visited this node
      isRefined.put (s, s);


      if (s instanceof IfPStmt)
        refinedStmt = doIfRefine ((IfPStmt) s);
      else if (s instanceof PrllAsmtPStmt)
        refinedStmt = doAsmtRefine ((PrllAsmtPStmt) s);
      else if (s instanceof GotoPStmt)
        refinedStmt = doGotoRefine ((GotoPStmt) s);
      else if (s instanceof ReturnSelectorPStmt)
        refinedStmt = doReturnSelectorRefine ((ReturnSelectorPStmt) s);
      else if (s instanceof NDGotoPStmt)
        refinedStmt = doNDGotoRefine ((NDGotoPStmt) s);
      else if (s instanceof SkipPStmt)
        refinedStmt = doSkipRefine ((SkipPStmt) s);
      else if (s instanceof FunctionCallPStmt)
        refinedStmt = doFunctionCallRefine ((FunctionCallPStmt) s);
      else
        throw new RuntimeException ("Can't refine: " + s.getClass ());   

      refinedStmt.setNext (doInsideFunctionRefine (s.getNext ()));

      return refinedStmt;
    }
    else
    {
      System.out.println ("Saw " + s + " twice");
      // node was refined on this pass already
      s.setNext (doInsideFunctionRefine (s.getNext ()));
      return s;
    }

  }

  public IfPStmt doIfRefine (IfPStmt s)
  {
    
    assert vc.scopeLevel () == 1 : "Scope level is: " + vc.scopeLevel ();
    // System.err.println (">>> doIfRefine " + s.getLabel ());
    s.setCond (doCondRefine (s.getCond ()));
    assert vc.scopeLevel () == 1 : "Scope level is: " + vc.scopeLevel ();
    s.setThenStmt (doInsideFunctionRefine (s.getThenStmt ()));
    s.setElseStmt (doInsideFunctionRefine (s.getElseStmt ()));
    return s;
  }

  // -- returns true if every operator/operand in expr is known
  public boolean knownCond (Expr expr)
  {
    if (expr == null)
      return false;

    if (expr.op () instanceof VariableOp)
      return true;

    if (expr.op () instanceof JavaObjectOp)
      return true;

    if (expr.op () instanceof BoolOp ||  
        (expr.op () instanceof NumericOp &&
        expr.op () != NumericOp.MOD) ||
        expr.op () instanceof ComparisonOp ||
	expr.op () instanceof CILIndirectionOp)
    {
      for (int i = 0; i < expr.arity (); i++)
        if (!knownCond (expr.arg (i)))
          return false;
      return true;
    }
    
    return false;  
  }

  public PCond doCondRefine (PCond e)
  {
    // System.err.println ("doCondRefine: " + e);

    // -- check that condition is as refined as can be already
    // -- XXX currently, each condition can be refined at most once
    // -- XXX at which point it's value changed from IBOT to something 
    // -- XXX else. 
    if (e.getCond ().op () != BiLatticeOp.IBOT)
      return e;

    // -- NOTE: YasmApp.preRefine now sets cond's abstracted condition to the
    // if-condition itself

    try
    {
      Expr sourceExpr = (Expr) e.getOrigCond ();

      // -- unable to convert to cvcl, e.g. contains an unknown operator
      if (!knownCond (e.getOrigCond ()))
	e.setOrigCond (unknownCond);
      

      sourceExpr = e.getOrigCond ();  

      // -- check if the condition is a constant
      Boolean result = 
        CVCLUtil.quickCheckTruth (vc, cvcl.toCVCL (sourceExpr),
            cvcl.toCVCL (sourceExpr).notExpr ());

      // -- result != null iff original condition is a constant
      if (result != null)
      {
        e.setCond 
          (result == Boolean.TRUE ? fac.trueExpr () : fac.falseExpr ());
        return e;
      }

      // -- check if condition is negated and strip the negation
      boolean negated = sourceExpr.op () == BoolOp.NOT;
      Expr condExpr = negated ? sourceExpr.arg (0) : sourceExpr;

      // -- see if the condition is equivalent to one of our 
      // -- new predicates
      for (Iterator it = newPredicates.iterator (); it.hasNext ();)
      {
        Expr pred = (Expr) it.next ();
        if (condExpr == pred /* || 
            CVCLUtil.quickQuery (vc, 
              vc.iffExpr (cvcl.toCVCL (condExpr),
                cvcl.toCVCL (pred))) */)
        {
          // -- got a new predicate equivalent to the condition
          e.setCond (negated ? 
              fac.op (BoolOp.NOT).unaryApply (pred) : pred);
          return e;
        }

      }
    }
    catch (Exception ex)
    {
      ex.printStackTrace ();
      throw new RuntimeException (ex);
    }
    return e;

  }

  public CVCLMemoryModel getMemoryModel (PrllAsmtPStmt asmt)
  {
    return (CVCLMemoryModel) asmt.getRefinerInfo ();
  }

  public WPComputer getWPComputer (PrllAsmtPStmt asmt)
  {
    return getPredRefinerInfo (asmt).wp;
  }

  private PredicateRefinerInfo getPredRefinerInfo (PrllAsmtPStmt asmt)
  {
    PredicateRefinerInfo info = (PredicateRefinerInfo) asmt.getRefinerInfo ();
    if (info == null)
    {
      info = new PredicateRefinerInfo ();
      asmt.setRefinerInfo (info);
    }
    return info;
  }

  public MemoryModel computeRegularMemoryModel (PrllAsmtPStmt s)
  {
    assert false : "Use getWPComputer!";

    return null;

    /*
    MemoryModel mem;

    if (s instanceof ReturnPStmt)
    {
      ReturnPStmt ret = (ReturnPStmt) s;

      mem = ret.symExec (fac);
    }
    else if (s instanceof FunctionCallPrologue)
    {
      FunctionCallPrologue prologue = (FunctionCallPrologue) s;
      FunctionCallPStmt call = prologue.getCall ();

      mem = call.symExec (fac);

    }
    else if (s instanceof FunctionCallEpilogue)
    {
      FunctionCallEpilogue epilogue = (FunctionCallEpilogue) s;
      FunctionCallPStmt call = epilogue.getCall ();

      String resultVarName = call.getResultVarName ();

      mem = new MemoryModel ();

      // return value assignment
      if (resultVarName != null)
        mem.assign (fac.var (resultVarName),
            fac.var (
              call.getFunctionName () + "::@return_value"));

    }
    else // regular PrllAsmtPStmt
    {
      mem = s.symExec (fac);
    }

    return mem;
    */
  }

  public CVCLMemoryModel computeMemoryModel (PrllAsmtPStmt s)
  {
    assert false : "Use getWPComputer!";
    return null;
  }

  public PrllAsmtPStmt doAsmtRefine (PrllAsmtPStmt s)
  {
    assert false : "Shouldn't be using base refiner!";

    return null;
    /*
    CVCLMemoryModel cvclMM = getMemoryModel (s);
    if (cvclMM == null)
    {
      cvclMM = computeMemoryModel (s);
      s.setRefinerInfo (cvclMM);
    }

    List allPreds = new ArrayList ();
    allPreds.addAll (predicates);
    allPreds.addAll (newPredicates);

    //System.out.println ("allPreds: " + allPreds);

    List cvclPred = new ArrayList ();
    for (Iterator it = allPreds.iterator (); it.hasNext ();)
      cvclPred.add (cvcl.toCVCL ((Expr) it.next ()));

    //System.out.println ("cvclPred: " + cvclPred);

    List asmts = new ArrayList ();

    //System.out.println ("Abstracting stmt labeled: " + s.getLabel ());

    // do actual work
    for (Iterator it = allPreds.iterator (); it.hasNext ();)
    {
      Expr var = (Expr)it.next ();
      edu.toronto.cs.tp.cvcl.Expr[] hFunc = 
        StmtAbstraction.computeH (vc, cvclPred, cvcl.toCVCL (var),
            cvclMM);
      System.err.println ("\t" + var + " is ");
      System.err.println ("\ttrue part: " + hFunc [0]);
      System.err.println ("\tfalse part: " + hFunc [1]);

      asmts.add (new AsmtPStmt (var, cvcl.fromCVCL (hFunc [0]),
            cvcl.fromCVCL (hFunc [1])));
    }
    s.setAsmts (asmts);
    return s;
    */
  }

  public GotoPStmt doGotoRefine (GotoPStmt s)
  {
    return s;
  }

  public ReturnSelectorPStmt doReturnSelectorRefine (ReturnSelectorPStmt s)
  {
    return s;
  }

  public NDGotoPStmt doNDGotoRefine (NDGotoPStmt s)
  {
    return s;
  }

  public SkipPStmt doSkipRefine (SkipPStmt skip)
  {
    return skip;
  }

  public FunctionCallPStmt doFunctionCallRefine (FunctionCallPStmt fcall)
  {
    FunctionCallPrologue prologue = (FunctionCallPrologue) fcall.getLogues ();
    doAsmtRefine (prologue);

    FunctionCallEpilogue epilogue =
      (FunctionCallEpilogue) fcall.getLogues ().getNext ();
    doAsmtRefine (epilogue);  

    return fcall;
  }

  /** 
   * Parses stream read from Reader; each predicate parsed is added to
   * the list of predicates.
   * 
   * @param in Source of predicates.
   */
  public void readPredicates (Reader in) 
    throws RecognitionException, TokenStreamException 
    {
      CILLexer lexer = new CILLexer (in);
      lexer.setTokenObjectClass (CToken.class.getName ());

      PredicateParser parser = new PredicateParser (lexer);
      parser.setASTNodeClass (Block.class.getName ());
      Block.setTokenVocabulary (CILTokenTypes.class.getName ());

      parser.parse();

      Block cPred = (Block) parser.getAST ();
      while (cPred != null)
      {
        /*
           System.err.println ("attempting to parse predicate:");
           TNode.setTokenVocabulary (CILTokenTypes.class.getName ());
           TNode.printTree (cPred);
         */

        Expr pred = SymbolicExecutorUtil.newSymbolicExecutor (fac).pureExpr
                    (cPred, new MemoryModel ());
        System.err.println ("Adding predicate: " + pred);
        addToNewPredicates (pred);

        cPred = (Block) cPred.getNextSibling ();
      }
    }

  protected List computeDependency (List l, Expr e, Map m)
  {
    List temp = new ArrayList ();

    for (Iterator it = l.iterator (); it.hasNext ();)
    {
      Expr expr = (Expr) it.next ();
      List dependingVars = (List)m.get (expr);

      assert dependingVars!=null;

      if (contains (e, dependingVars))
        temp.add (expr);
    }
    return temp;
  }

  protected boolean contains (Expr e, List l)
  {
    if (e.arity () == 0)
    {
      for (Iterator it = l.iterator (); it.hasNext ();)
        if (e.equals ((Expr)it.next ()))
          return true;
    }
    else 
      for (int i = 0; i < e.arity (); i++)
        return contains (e.arg (i), l);
    return false;
  }

  protected List findVars (Expr e)
  {
    List l = new LinkedList ();
    if (e.arity () == 0)
    {
      if (e.op () instanceof VariableOp)
        l.add (e);
    }
    else
      for (int i = 0; i < e.arity (); i++)
      {
        l.addAll (findVars (e.arg (i)));
      }
    return l;
  }

  class PredicateRefinerInfo 
  {
    CVCLMemoryModel mmModel;
    WPComputer wp;
  }

}

