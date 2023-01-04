package edu.toronto.cs.yasm.abstractor;

import edu.toronto.cs.expr.*;
import edu.toronto.cs.yasm.pprogram.*;
import edu.toronto.cs.yasm.YasmApp;

import java.util.*;

public class NullExprAbstractor
{

  ExprFactory exprFactory;
  PProgram pProgramFlat;

  public NullExprAbstractor (ExprFactory _exprFactory)
  {
    exprFactory = _exprFactory;
  }

  public ExprFactory getFactory ()
  {
    return exprFactory;
  }
  public PProgram getPProgram(){
	return pProgramFlat;
  }


  public ArrayList doProgramAbstract (Expr program, int selectorType)
  {

    	assert program.op () == CILProgramOp.PROGRAM : "Invalid Expr type";
	ArrayList temp = new ArrayList();
	for (int conta=0;conta < program.arity();conta++)
     		temp.add( new PProgram (exprFactory,doShallowFunctionDefAbstract (program,conta),doShallowDeclAbstract (program),selectorType ,doShallowFunctionDef (program,conta)));
	//System.out.println("arriva qui");
	pProgramFlat = new PProgram (exprFactory,doShallowFunctionDefAbstract(program),doShallowDeclAbstract(program),selectorType);
	return temp;
  }

  public PProgram doProgramAbstract (Expr program)
  {
    assert program.op () == CILProgramOp.PROGRAM : "Invalid Expr type";

    return new PProgram (exprFactory,
                         doShallowFunctionDefAbstract (program),
                         doShallowDeclAbstract (program));
  }

  public Map doShallowFunctionDefAbstract (Expr program,int conta)
  {
    assert program.op () == CILProgramOp.PROGRAM : "Invalid Expr type";

    Map functionDefs = new HashMap ();  
	
      Expr e = (Expr) program.arg(conta) ;
      if (e.op () == CILFunctionDefOp.FUNCTION_DEF)
      {
        System.err.println(" ");
        System.err.println ("Abstracting function: " +CILFunctionDefOp.getFunctionName (e));
        
        functionDefs.put (CILFunctionDefOp.getFunctionName (e),
                          doFunctionAbstract (e));
      }

    return functionDefs;
  }
  public String doShallowFunctionDef (Expr program,int conta)
  {
    assert program.op () == CILProgramOp.PROGRAM : "Invalid Expr type";
	
      Expr e = (Expr) program.arg(conta) ;
      if (e.op () == CILFunctionDefOp.FUNCTION_DEF)
        //System.err.println ("Abstracting function: " + CILFunctionDefOp.getFunctionName (e));
	return CILFunctionDefOp.getFunctionName (e);
      else
		return "nonloso";
}

  public Map doShallowFunctionDefAbstract (Expr program)
  {
    assert program.op () == CILProgramOp.PROGRAM : "Invalid Expr type";

    Map functionDefs = new HashMap ();  
    for (Iterator it = program.args ().iterator (); it.hasNext ();)	{
      Expr e = (Expr) it.next() ;
      if (e.op () == CILFunctionDefOp.FUNCTION_DEF)
      {
        System.err.println(" ");
        System.err.println ("Abstracting function: " +
                             CILFunctionDefOp.getFunctionName (e));
        
        functionDefs.put (CILFunctionDefOp.getFunctionName (e),
                          doFunctionAbstract (e));
      }
    }
    return functionDefs;
  }
  /** 
   * Returns a Map of the top-level declarations in a given PROGRAM or
   * SCOPE Block (i.e. does not descend into nested scopes; this is sufficient
   * for CIL programs since all nested declarations are moved to top-level).
   * The Map is: (String) declaration-name -> (PDecl) DECLARATION Block.
   */
  public Map doShallowDeclAbstract (Expr e)
  {
    assert e.op () == CILProgramOp.PROGRAM ||
           e.op () == CILFunctionDefOp.FUNCTION_DEF
      : "Invalid Expr type";

    Map retMap = new HashMap ();

    if (e.op () == CILProgramOp.PROGRAM)
      mapDeclList (retMap, e);

    if (e.op () == CILFunctionDefOp.FUNCTION_DEF)
    {
      mapDeclList (retMap, CILFunctionDefOp.getParameterDecls (e));
      mapDeclList (retMap, CILFunctionDefOp.getLocalDecls (e));
    }

    return retMap;
  }

  /** 
   * Maps identifiers to declarations.
   * 
   * @param declMap a Map
   * @param e an Expr which should be a LIST or a PROGRAM; result
   * unpredictable otherwise
   */
  private void mapDeclList (Map declMap, Expr e)
  {
    if (e == null)
      return;

    assert e.op () == CILListOp.LIST ||
           e.op () == CILProgramOp.PROGRAM
      : "Invalid Expr type";

    for (Iterator it = e.args ().iterator (); it.hasNext ();)
    {
      Expr curExpr = (Expr) it.next ();
      if (curExpr.op () == CILDeclarationOp.DECL)
        if (CILDeclarationOp.isNamedDeclaration (curExpr))
        {
          System.err.println ("Adding decl: " + CILDeclarationOp.getDeclName (curExpr));
          declMap.put (CILDeclarationOp.getDeclName (curExpr),
                       new PDecl (curExpr));
        }
    }
  }

  // Initial abstraction
  public PFunctionDef doFunctionAbstract (Expr e)
  {
    assert e.op () == CILFunctionDefOp.FUNCTION_DEF : "Invalid Expr type";

    PFunctionDef def = new PFunctionDef (e);
    def.addToHead
      (doInsideFunctionAbstract (def, CILFunctionDefOp.getBody (e)));
    def.setLocalDecls (doShallowDeclAbstract (e));  

    return def;
  }

  // Dispatches block to appropriate abstractor method; no nested
  // function definitions, so we don't need to cover FUNCTION_DEF
  public PStmt doInsideFunctionAbstract (PFunctionDef def, Expr e)
  {
    if (e == null)
      return new SkipPStmt (def, e);
    
    Operator op = e.op ();   

    if (op == CILScopeOp.SCOPE)
      return doScopeAbstract (def, e);
    else if (op == CILIfStmtOp.IF_THEN_ELSE)
      return doIfAbstract (def, e);
    else if (op == CILWhileOp.WHILE)
      return doWhileAbstract (def, e);
    else if (op == CILAssignOp.ASSIGN)
      return doAssignAbstract (def, e);
    else if (op == CILListOp.SLIST)
      return doStmtListAbstract (def, e);
    else if (op == CILLabelledStmtOp.LSTMT)
      return doLabelledStmtAbstract (def, e);
    else if (op == CILFunctionCallOp.FCALL)
      return doFunctionCallAbstract (def, e);  
    else if (op == CILGotoOp.GOTO)
      return doGotoAbstract (def, e);  
    /*
    else if (op == BlockType.NDGOTO)
      return doNDGotoAbstract (def, e);
    */
    else if (op == CILBreakOp.BREAK)
      return doBreakAbstract (def, e);  
    else if (op == CILReturnOp.RETURN)
      return doReturnAbstract (def, e);  
    else if (op == CILEmptyStmtOp.EMPTY)
      return new SkipPStmt (def, e);
    else if (op == CILNDGotoOp.NDGOTO)
      return doNDGotoAbstract (def, e);
    else
    {
      System.err.println ("Abstractor: Can't handle Expr tree: ");
      System.err.println (e);
      throw new RuntimeException ("Can't handle op type " + op);
    }
  }

  // PRE: def != null, e != null, e.op () == CILListOp.LIST
  // Abstracts each e's children in order; the results are linked and the head
  // is returned
  private PStmt doListAbstract (PFunctionDef def, Expr e)
  {
    assert (def != null) : "def is null";
    assert (e != null) : "e is null";
    assert (e.op () == CILListOp.LIST) : "e is not a LIST";

    PStmt stmt = null;

    for (int i = 0; i < e.arity (); i++)
    {
      PStmt temp = doInsideFunctionAbstract (def, e.arg (i));

      if (temp == null)
        continue;

      stmt = concatStmts (stmt, temp);  
    }

    return stmt;
  }

  private PStmt concatStmts (PStmt s1, PStmt s2)
  {
    if (s1 == null)
      return s2;
    
    s1.setTail (s2);

    return s1;
  }

  // Abstracts each statement in the body of the scope
  private PStmt doScopeAbstract (PFunctionDef def, Expr e)
  {
    Expr body = CILScopeOp.getBody (e);

    if (body == null)
      return new SkipPStmt (def, e);

    return doListAbstract (def, body);  
  }

  // Abstracts the condition and the then and else parts
  private PStmt doIfAbstract (PFunctionDef def, Expr e)
  {
    // System.err.println ("Abstractor: doIfAbstract: " + e);
    return new IfPStmt (
        def,
        e,
        doCondAbstract (CILIfStmtOp.getCond (e)),
        doInsideFunctionAbstract (def, CILIfStmtOp.getThen (e)),
        doInsideFunctionAbstract (def, CILIfStmtOp.getElse (e)));
    // exitGoto to be patched
  }

  // Abstracts the condition and the loop body
  private PStmt doWhileAbstract (PFunctionDef def, Expr e)
  {
    WhilePStmt wp =
      new WhilePStmt (def,
                      e,
                      doCondAbstract (CILWhileOp.getCond (e)),
                      doInsideFunctionAbstract (def, CILWhileOp.getBody (e)));
    // elseGoto to be patched                                 
    return wp;
  }

  // Abstracts an if/while condition as "unknown".
  private PCond doCondAbstract (Expr e)
  {
    // System.err.println ("Abstractor: doCondAbstract: " + e);
    return new PCond (e, exprFactory.op (BiLatticeOp.IBOT));
  }

  // Abstracts a list of statements as an empty parallel assignment.
  private PStmt doStmtListAbstract (PFunctionDef def, Expr e)
  {
    return new PrllAsmtPStmt (def, e);
  }

  private PStmt doLabelledStmtAbstract (PFunctionDef def, Expr e)
  {
    // System.err.println ("Abstractor: labelled statement: " + e);
    PStmt pStmt = doInsideFunctionAbstract (def, CILLabelledStmtOp.getStmt (e));
    pStmt.setLabel (CILLabelledStmtOp.getLabel (e));
    return pStmt;
  }

  private PStmt doFunctionCallAbstract (PFunctionDef def, Expr e)
  {
    return new FunctionCallPStmt (def, e);
  }

  private PStmt doGotoAbstract (PFunctionDef def, Expr e)
  {
    return new GotoPStmt (def, e);
  }

  /*
  private PStmt doNDGotoAbstract (PFunctionDef def, Block block)
  {
    return new NDGotoPStmt (def, block);
  }
  */

  private PStmt doNDGotoAbstract (PFunctionDef def, Expr e)
  {
    return new NDGotoPStmt (def, e);
  }

  private PStmt doBreakAbstract (PFunctionDef def, Expr e)
  {
    return new BreakPStmt (def, e);
  }

  private PStmt doReturnAbstract (PFunctionDef def, Expr e)
  {
    return new ReturnPStmt (def, e);
  }

  private PStmt doAssignAbstract (PFunctionDef def, Expr e)
  {
    if (e.arg (1) == null)
      return new PrllAsmtPStmt (def, e);
    // -- if op is simple (i.e. not a function call), then create a
    // parallel assignment, otherwise, handle specifically
    else if (e.arg (1).op () != CILFunctionCallOp.FCALL)
      return new PrllAsmtPStmt (def, e);
    else
      return new FunctionCallPStmt (def, e.arg (1), e.arg (0));  
  }
}

