package edu.toronto.cs.yasm.pprogram;

import java.util.*;
import edu.toronto.cs.cparser.block.*;

import edu.toronto.cs.expr.*;
import edu.toronto.cs.yasm.abstractor.*;
import edu.toronto.cs.tp.cvcl.ValidityChecker;
import edu.toronto.cs.tp.cvcl.Type;

/**
 * PFunction.java
 *
 *
 * Created: Fri Jun 25 14:14:01 2004
 *
 * @author <a href="mailto:kelvin@tallinn.cs">Kelvin Ku</a>
 * @version 1.0
 */
public class PFunctionDef
{
  // -- only one of the following source fields should be in use!

  // -- source Block
  Block sourceBlock;

  // -- source Expr
  Expr sourceExpr;

  // -- head of a graph of program statements defining this function
  PStmt head;

  // -- entry point to this function
  // XXX 2004-08-09, return selector happens to be head of function,
  // so we need this
  PStmt entry;

  // -- head of a graph of program statements defining the return selector
  // block of this function
  PStmt returnSelector;

  // -- Map of local declarations, including function parameters:
  //    (String) name -> (PDecl) declaration
  Map localDecls;

  // -- stack variables
  PDecl returnValueDecl;
  PDecl returnIndexDecl;

  // -- PProgram this function belongs to
  PProgram parent;

  // -- true if local decls have been processed (i.e. parsed into CVCL Expr's)
  // boolean declsProcessed;

  public PFunctionDef (PProgram _parent, PStmt _head, Map _localDecls,
                       Block _sourceBlock)
  {
    parent = _parent;
    head = _head;
    localDecls = _localDecls;
    sourceBlock = _sourceBlock;
    // declsProcessed = false;
    returnSelector = null;
    addEntryPoint ();
    returnValueDecl = new PDecl ((Block) null);
    returnIndexDecl = new PDecl ((Block) null);
  }

  public PFunctionDef (PProgram _parent, PStmt _head, Map _localDecls,
                       Expr _sourceExpr)
  {
    parent = _parent;
    head = _head;
    localDecls = _localDecls;
    sourceExpr = _sourceExpr;
    // declsProcessed = false;
    returnSelector = null;
    addEntryPoint ();
    returnValueDecl = new PDecl ((Expr) null);
    returnIndexDecl = new PDecl ((Expr) null);
  }

  public PFunctionDef (Block _sourceBlock)
  {
    this (null, null, null, _sourceBlock);
  }

  public PFunctionDef (Expr _sourceExpr)
  {
    this (null, null, null, _sourceExpr);
  }

  private void addEntryPoint ()
  {
    if (sourceBlock != null)
      entry = new SkipPStmt (this, (Block) null);
    if (sourceExpr != null)  
      entry = new SkipPStmt (this, (Expr) null);
    entry.setLabel (getFunctionName () + "_ENTRY");
    entry.setNext (head);
    head = entry;
  }

  public PStmt getReturnSelector ()
  {
    switch (parent.getSelectorType ())
    {
      case PProgram.LinSelector:
        return getPProgramReturnSelector (PProgram.LinSelector);
      case PProgram.BinSelector:
        return getPProgramReturnSelector (PProgram.BinSelector);
      case PProgram.DDSelector:
        return getDDReturnSelector ();
      default:
        throw new RuntimeException ("Unknown selector type: " +
                                    parent.getSelectorType ());  
    }
  }

  public PStmt getDDReturnSelector ()
  {
    if (returnSelector == null)
    {
      if (getNumCallSites () == 0)
        returnSelector = new GotoPStmt ("END");
      else if (getNumCallSites () == 1)
        returnSelector = new GotoPStmt (callSiteName (0));
      else 
        returnSelector = new ReturnSelectorPStmt (this);

      returnSelector.setLabel (getFunctionName () + "_RETURN_SELECTOR_HEAD");

      returnSelector.setNext (head);
      head = returnSelector;
    }

    // System.out.println ("Return selector: " + returnSelector);    
    
    return returnSelector;
  }

  public PStmt getPProgramReturnSelector (int selectorSubType)
  {
    if (returnSelector != null)
      return returnSelector;

    // if we don't have one yet, make one

    if (getNumCallSites () == 0)
      returnSelector = new GotoPStmt ("END");
    else if (getNumCallSites () == 1)
      returnSelector = new GotoPStmt (callSiteName (0));
    else  
    {
      // XXX 0-based
      switch (selectorSubType)
      {
        case PProgram.LinSelector:
          returnSelector = makeLinSelector (0, getNumCallSites () - 1);
          break;
        case PProgram.BinSelector:  
          returnSelector =
            makeBinSelector (0, getNumCallSites () - 1);
          break;
      }
    }

    returnSelector.setLabel (getFunctionName () + "_RETURN_SELECTOR_HEAD");

    // XXX Adding to head of function body
    returnSelector.setNext (head);
    head = returnSelector;

    return returnSelector;
  }

  public PStmt makeFunctionCall (FunctionCallPStmt call)
  {
    /*
    System.err.println ("Making function call for: " +
                        call.getFunctionName () + ", " +
                        call.getCallIndex ());
    */
    FunctionCallPrologue callAsmt = new FunctionCallPrologue (call);
    callAsmt.setDest (entry);
    callAsmt.setLabel (call.getLabel ());
    // -- not originally labelled, so we can make our own label
    if (call.getLabel () == null)
      callAsmt.setLabel (getFunctionName () + "_CALL_" + call.getCallIndex ());
    FunctionCallEpilogue returnAsmt = new FunctionCallEpilogue (call);
    returnAsmt.setLabel (callSiteName (call.getCallIndex ()));
    callAsmt.setNext (returnAsmt);
    return callAsmt;
  }

  private PStmt makeBinSelector (int left, int right)
  {

    assert left <= right : "Infinite recursion; left = " + left + ", right = "
                           + right;

    // -- base case: single call site
    if (left == right)
    {
      // -- goto call site @ left
      GotoPStmt gp = new GotoPStmt (callSiteName (left));
      gp.setLabel (callSiteName (left) + "_SELECTOR");
      return gp;
    }

    // -- recursive case:  

    int middle = (left + right) / 2; // rounds down 
    ExprFactory fac = parent.getExprFactory ();

    PCond pCond =
      new PCond (fac.op (ComparisonOp.GEQ).binApply (
                  fac.var (getFunctionName () + "::@return_index"),
                  fac.intExpr (middle + 1)),
                 fac.op (BiLatticeOp.IBOT)); // initial approximation

    IfPStmt ifPStmt =
      new IfPStmt (this,        // parent
                   (Expr) null, // sourceExpr
                   pCond,       // condition
                   makeBinSelector (middle + 1, right), // then-body
                   makeBinSelector (left, middle));     // else-body

    ifPStmt.setLabel (callSiteName (middle + 1) + "_SELECTOR_TEST");

    return ifPStmt;
  }

  /** 
   * Makes a return selector for a function, starting from curIndex, and
   * recurses until curIndex = last.
   * 
   * @param curIndex 
   * @param last 
   * @return 
   */
  private PStmt makeLinSelector (int curIndex, int last)
  {
    assert curIndex <= last : "Infinite recursion: curIndex = " + curIndex
                              + ", last = " + last;

    // in either case we have to make a goto
    GotoPStmt gp = new GotoPStmt (callSiteName (curIndex));
    gp.setLabel (callSiteName (curIndex) + "_SELECTOR");

    if (curIndex == last)
      return gp;

    // foo::@return_index == curIndex
    ExprFactory fac = parent.getExprFactory ();
    PCond pCond =
      new PCond (fac.op (ComparisonOp.EQ).binApply (
                   fac.var (getFunctionName () + "::@return_index"),
                   fac.intExpr (curIndex)),
                 fac.op (BiLatticeOp.IBOT));

    IfPStmt ifPStmt = null;
    if (sourceBlock != null)
      ifPStmt = new IfPStmt (this,
          (Block) null,
          pCond,
          gp,
          makeLinSelector (curIndex + 1, last)
          );

    if (sourceExpr != null)
      ifPStmt = new IfPStmt (this,
          (Expr) null,
          pCond,
          gp,
          makeLinSelector (curIndex + 1, last)
          );

    ifPStmt.setLabel (callSiteName (curIndex) + "_SELECTOR_TEST");
    return ifPStmt;
  }

  public String callSiteName (int index)
  {
    return getFunctionName () + "_RETURN_" + index;
  }

  /*
  public boolean isDeclsProcessed ()
  {
    return declsProcessed;
  }

  public void setDeclsProcessed (boolean _declsProcessed)
  {
    declsProcessed = _declsProcessed;
  }
  */

  public void setHead (PStmt v)
  {
    head = v;
  }

  public void addToHead (PStmt v)
  {
    head.setTail (v);
  }

  public PStmt getHead ()
  {
    return head;
  }

  public PStmt getEntryPoint ()
  {
    return head;
  }

  public void setLocalDecls (Map v)
  {
    localDecls = v;
  }

  public Map getLocalDecls ()
  {
    return localDecls;
  }

  public int getNumCallSites ()
  {
    if (sourceBlock != null)
      return sourceBlock.getNumCallSites ();

    if (sourceExpr != null)
      return CILFunctionDefOp.getNumCallSites (sourceExpr);  

    assert false : "Both sourceBlock and sourceExpr are null";
    return -1;  
  }

  public String getFunctionName ()
  {
    if (sourceBlock != null)
      return sourceBlock.getFunctionName ();
    
    if (sourceExpr != null)
      return CILFunctionDefOp.getFunctionName (sourceExpr);  

    assert false : "Both sourceBlock and sourceExpr are null";
    return null;  
  }

  /*
  public Block getFunctionDeclSpecifiers ()
  {
    return sourceBlock.getFunctionReturnType ();
  }
  */

  public Type getReturnType (ValidityChecker vc)
  throws antlr.RecognitionException
  {
    if (sourceBlock != null)
      return DeclsParserUtil.parseBlockTypeSpecifiers
        (sourceBlock.getFunctionReturnType (), vc);

    if (sourceExpr != null)
      return DeclsParserUtil.parseExprTypeSpecifiers
        (CILFunctionDefOp.getReturnType (sourceExpr), vc);

    assert false : "Both sourceBlock and sourceExpr are null";
    return null;
  }

  public PDecl getReturnValueDecl ()
  {
    return returnValueDecl;
  }

  public PDecl getReturnIndexDecl ()
  {
    return returnIndexDecl;
  }

  public PProgram getParent ()
  {
    return parent;
  }

  public void setParent (PProgram v)
  {
    parent = v;
  }

  public void setSourceBlock (Block v)
  {
    sourceBlock = v;
  }

  public Block getSourceBlock ()
  {
    return sourceBlock;
  }

  public Expr getSourceExpr ()
  {
    return sourceExpr;
  }
  
  public int getLineNum ()
  {
    Expr src = getSourceExpr ();
    if (src == null) return -1;
    if (src.arity () != 5)
      {
	System.err.println ("Strange function def without a line number");
	System.err.println ("Srouce expr: " + src);
	return -1;
      }
    
    
    return 
      ((Integer)((JavaObjectOp)src.arg (4).op ()).getObject ()).intValue ();
  }


  public boolean isNonVoid ()
  {
    if (sourceBlock != null)
      return sourceBlock.isNonVoidFunction ();
    
    if (sourceExpr != null)
      return CILFunctionDefOp.isNonVoid (sourceExpr);

    assert false : "Both sourceBlock and sourceExpr are null";
    return false;
  }

  /** 
   * @return List of cs.Expr's, one for each parameter, with corresponding
   * names, in the order they appear in the definition.
   */
  /*
  public List getParameterList ()
  {
    List parameters = new LinkedList ();
    ExprFactory fac = parent.getExprFactory ();

    if (sourceBlock != null)
    {
      for (Block curBlock = sourceBlock.getFunctionParameterList ();
           curBlock != null;
           curBlock = (Block) curBlock.getNextSibling ())
        if (curBlock.getDeclName () != null)
          parameters.add (fac.var (curBlock.getDeclName ()));
      return parameters;  
    }

    if (sourceExpr != null)
    {
      for (Iterator it =
           CILFunctionDefOp.getParameterList (sourceExpr).args ().iterator ();
           it.hasNext ();)
      {
        Expr curDecl = (Expr) it.next ();
        if (CILDeclarationOp.isNamedDeclaration (curDecl))
          parameters.add (fac.var (CILDeclarationOp.getDeclName (curDecl)));
        // XXX unwraps and re-wraps decl name in a varExpr
      }

      return parameters;  
    }

    assert false : "Both sourceBlock and sourceExpr are null";
    return null;
  }
  */

  /*
  public Expr getParameterListExpr ()
  {
    return CILFunctionDefOp.getParameterList (sourceExpr);
  }
  */

  public Expr getFullParametersExpr ()
  {
    ExprFactory fac = getParent ().getExprFactory ();
    List params = new LinkedList ();
    params.add (fac.var (getFunctionName () + "::@return_index"));
    if (CILFunctionDefOp.hasParameters (sourceExpr))
      params.addAll (CILFunctionDefOp.getParameterNames (sourceExpr).args ());
    return fac.op (CILListOp.LIST).naryApply (params);
  }

  public String toString ()
  {
    return getFunctionName ();
  }

  public void print ()
  {
    if (head != null)
      head.print ();
  }

  public ExprFactory getExprFactory ()
  {
    return parent.getExprFactory ();
  }

} 
