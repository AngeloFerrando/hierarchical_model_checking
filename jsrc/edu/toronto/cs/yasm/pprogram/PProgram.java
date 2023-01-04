package edu.toronto.cs.yasm.pprogram;

import edu.toronto.cs.yasm.abstractor.*;
import edu.toronto.cs.cparser.*;
import edu.toronto.cs.cparser.block.*;
import edu.toronto.cs.boolpg.abstraction.*;
import edu.toronto.cs.expr.*;

import java.util.*;
import java.io.*;

/**
 * PProgram.java
 *
 *
 * Created: Fri Jun 25 14:14:01 2004
 *
 * @author <a href="mailto:kelvin@tallinn.cs">Kelvin Ku</a>
 * @version 1.0
 */
public class PProgram 
{
  // -- (String) function-name -> (PFunctionDef) function-definition
  Map functionDefs;

  // -- (String) variable-name -> (PDecl) variable-declaration
  Map globalDecls;

  // -- Map: (String) label-in-source -> (PStmt) corresponding statement
  Map labelledStatementMap;

  // -- direct map:
  //      (int) generated-label-index -> (PStmt) corresponding statement
  List statementList;

  // -- table of predicates
  PredicateTable pTable;

  public static final String MAIN_FUNCTION_NAME = "main";

  // -- true if declarations (global and local) have been refined; false o.w.
  boolean declsRefined;

  ExprFactory fac;

  // -- the set of inconsistent predicates
  Expr inconsistent;

  // -- the init and end statements
  PStmt programStmts;

  // -- return selector type
  int selectorType;

  // -- return selector types
  public static final int LinSelector = 0;
  public static final int BinSelector = 1;
  public static final int DDSelector = 2;
	
  public static PrintWriter err = new PrintWriter (new OutputStreamWriter (System.err), true);
  String prova;
  public PProgram (ExprFactory _fac, Map _functionDefs, Map _globalDecls)
  {
    new PProgram (_fac, _functionDefs, _globalDecls, DDSelector);
  }

  public PProgram (ExprFactory _fac, Map _functionDefs, Map _globalDecls,
                   int _selectorType)
  {
    fac = _fac;
    functionDefs = _functionDefs;
    globalDecls = _globalDecls;
    selectorType = _selectorType;

    labelledStatementMap = new HashMap ();
    statementList = new LinkedList ();
    pTable = new PredicateTable ();

    inconsistent = null;
    // globalDeclsProcessed = false;
    declsRefined = false;

    // set parent of each PFunctionDefs to be this
    for (Iterator it = functionDefs.values ().iterator (); it.hasNext (); )
    {
      PFunctionDef def = (PFunctionDef) it.next ();
      def.setParent (this);
    }

    // addFunctionReturn ();
	//setta inizio e fine stati init ed end
	//System.out.println("in costruttore 0");
    addProgramInitAndEndFlat ();
	//System.out.println("in costruttore 1");
    // System.err.println ("% labelling %");
    labelAllStmtsFlat ();
    // System.err.println ("% labelling END %");
	//System.out.println("in costruttore 2");
    // System.err.println (">>> labelledStatementMap: " + labelledStatementMap);

    // System.err.println ("% patching CFG %");
    computeCFGForEachFunctionFlat ();
	//System.out.println("in costruttore 3");
    // System.err.println ("% patching CFG END %");
	//print(err);
  }
 
  public PProgram (ExprFactory _fac, Map _functionDefs, Map _globalDecls,
                   int _selectorType, String s)
  {
    fac = _fac;
    functionDefs = _functionDefs;
    globalDecls = _globalDecls;
    selectorType = _selectorType;
    prova = s;	
    labelledStatementMap = new HashMap ();
    statementList = new LinkedList ();
    pTable = new PredicateTable ();

    inconsistent = null;
    // globalDeclsProcessed = false;
    declsRefined = false;

    // set parent of each PFunctionDefs to be this
    for (Iterator it = functionDefs.values ().iterator (); it.hasNext (); )
    {
      PFunctionDef def = (PFunctionDef) it.next ();
      def.setParent (this);
    }

    // addFunctionReturn ();
	//setta inizio e fine stati init ed end
	//System.out.println("in costruttore 0");
    addProgramInitAndEnd ();
//System.out.println("in costruttore 1");
    // System.err.println ("% labelling %");
    labelAllStmts ();
    // System.err.println ("% labelling END %");
//System.out.println("in costruttore 2");
    // System.err.println (">>> labelledStatementMap: " + labelledStatementMap);

    // System.err.println ("% patching CFG %");
    computeCFGForEachFunction ();
//System.out.println("in costruttore 3");
    // System.err.println ("% patching CFG END %");
	//System.out.println(" ");
	//print(err);
  }

  public void addFunctionReturn ()
  {
    for (Iterator it = functionDefs.values ().iterator (); it.hasNext (); )
    {
      PFunctionDef def = (PFunctionDef) it.next ();
      if (!(def.getHead ().getLast () instanceof ReturnPStmt))
      {
        System.err.println (def.getFunctionName () +
            " has no terminating return, inserting one");
        def.getHead ().setTail (new ReturnPStmt (def, (Block) null));
      }
    }
  }

  /** 
   * @deprecated Use getStatementList ()
   */
  public List getStmtList ()
  {
    return statementList;
  }

  public List getStatementList ()
  {
    return statementList;
  }

  /** 
   * @deprecated Use getLabelledStatementsMap ()
   */
  public Map getLabelledStmtsMap ()
  {
    return labelledStatementMap;
  }

  public Map getLabelledStatementMap ()
  {
    return labelledStatementMap;
  }

  /*
     public boolean isGlobalDeclsProcessed ()
     {
     return globalDeclsProcessed;
     }

     public void setGlobalDeclsProcessed (boolean _globalDeclsProcessed)
     {
     globalDeclsProcessed = _globalDeclsProcessed;
     }
   */

  public void setInconsistent (Expr v)
  {
    inconsistent = v;
  }

  public Expr getInconsistent ()
  {
    return inconsistent;
  }

  /** 
   * @deprecated Use getMainFunctionDef ()
   */
  public PStmt getHead ()
  {
    return getMainFunctionDef ().getHead ();
  }

  public Map getFunctionDefs ()
  {
    return functionDefs;
  }

  public Map getGlobalDecls ()
  {
    return globalDecls;
  }

  public void setGlobalDecls (Map _globalDecls)
  {
    globalDecls = _globalDecls;
  }

  public PFunctionDef getMainFunctionDef ()
  {
    return (PFunctionDef) functionDefs.get (MAIN_FUNCTION_NAME);
  }
public PFunctionDef getFunctionDef ()
  {
    return (PFunctionDef) functionDefs.get (prova);
  }

  public void addProgramInitAndEndFlat ()
  {
    SkipPStmt init = new SkipPStmt (getMainFunctionDef (), (Block) null);
    init.setDest (getMainFunctionDef ().getHead ());
    init.setLabel ("init"); // XXX Magic string
    // getMainFunctionDef ().setHead (init);

    // -- add a self-loop at the end
    GotoPStmt g = new GotoPStmt (getMainFunctionDef (), (Block) null);
    g.setDest (g);
    g.setLabel ("END"); // XXX Magic string
    // getMainFunctionDef ().getHead ().setTail (g);

    init.setNext (g);
    programStmts = init;
  }


  public void addProgramInitAndEnd ()
  {
    SkipPStmt init = new SkipPStmt (getFunctionDef (), (Block) null);
    init.setDest (getFunctionDef ().getHead ());
    init.setLabel ("init"); // XXX Magic string
    // getMainFunctionDef ().setHead (init);

    // -- add a self-loop at the end
    GotoPStmt g = new GotoPStmt (getFunctionDef (), (Block) null);
    g.setDest (g);
    g.setLabel ("END"); // XXX Magic string
    // getMainFunctionDef ().getHead ().setTail (g);

    init.setNext (g);
    programStmts = init;
  }

  public void printMainFunction (PrintWriter out)
  {
    PStmt mainFunctionHead = getMainFunctionDef ().getHead ();

    if (mainFunctionHead != null)
      mainFunctionHead.print (out);
  }

  /** 
   * Prints the PProgram to <code>out</code> in a nice format.
   * 
   * @param out - the <code>PrintWriter</code> to print to.
   */
  public void print (PrintWriter out)
  {
    for (Iterator it = statementList.iterator (); it.hasNext ();)
    {
      PStmt s = (PStmt) it.next ();

      out.print (s.getId () + "/" + s.getLabel () + ": ");

      s.printMe (out);

      if (s.getNext () != null)
        out.println ("  next: " + s.getNext ().getId () + "/" +
            s.getNext ().getLabel ());

      if (s.getDest () != null)
        out.println ("  dest: " + s.getDest ().getId () + "/" +
            s.getDest ().getLabel ());

      out.println ();
    }
  }

  public String toString ()
  {
    StringWriter out = new StringWriter ();
    print (new PrintWriter (out, true));
    return out.toString ();
  }

  /** 
   * Returns the PStmt referred to by <code>label</code>
   * 
   * @param label the statement label.
   * @return the PStmt referred to by <code>label</code>; <code>null</code>
   * if the label is unknown.
   */
  public PStmt getStmt (String label)
  {
    PStmt pstmt = (PStmt) labelledStatementMap.get (label);

    if (pstmt != null) return pstmt;

    try
    {
      if (label.startsWith ("l"))
        label = label.substring (1);
      pstmt = (PStmt) statementList.get (Integer.parseInt (label) - 1);
      return pstmt;
    }
    catch (NumberFormatException ex)
    {
      ex.printStackTrace ();
      throw new RuntimeException ("Unknown label");
    }
  }

  public PredicateTable getPredTable ()
  {
    return pTable;
  }

  /** 
   * Calls labelStmts on PProgram statements, then on each function head.
   */
  
   private void labelAllStmtsFlat ()
  {
    labelStmtsFlat (programStmts); // has to be done first so init gets id 0
    for (Iterator it = functionDefs.values ().iterator (); it.hasNext ();)
      labelFunctionStmtsFlat ((PFunctionDef) it.next ());
  }

  private void labelAllStmts ()
  {
    labelStmts (programStmts); // has to be done first so init gets id 0
    for (Iterator it = functionDefs.values ().iterator (); it.hasNext ();)
      labelFunctionStmts ((PFunctionDef) it.next ());
  }
  

  private void labelFunctionStmtsFlat (PFunctionDef f)
  {
	// System.err.println ("labelling " + f.getFunctionName ()); -- add
	// returnSelector into the list of statements -- need to recurse on this to
	// add all of the statements -- and give them an ID

	// XXX Adds return selector to head of f's body
	f.getReturnSelector ();

	// labelStmts (f.getReturnSelector ()); -- then process the rest of
	// function statements
	labelStmtsFlat (f.getHead ()); }
 
 

  private void labelFunctionStmts (PFunctionDef f)
  {
	// System.err.println ("labelling " + f.getFunctionName ()); -- add
	// returnSelector into the list of statements -- need to recurse on this to
	// add all of the statements -- and give them an ID

	// XXX Adds return selector to head of f's body
	f.getReturnSelector ();

	// labelStmts (f.getReturnSelector ()); -- then process the rest of
	// function statements
	labelStmts (f.getHead ()); }

  /** 
   * Assigns a label to every unlabelled statement in the list headed by
   * <CODE>p</CODE>; descends into the children of an IfPStmt. XXX Also adds
   * each statement to statementList in the order that they are labelled ---
   * only the statements in statementList are processed by PProgramCompiler.
   */

  private void labelStmtsFlat (PStmt p)
  {
		  /*
		  if (p != null)	  
		  {
				  System.out.println ("Labelling a " + p.getSourceExpr ());	  
				  if (p.getLabel () != null)
				  {
						  System.out.println ("\tIt came with a label: " + p.getLabel());
				  }
				  else
				  {
						  System.out.println ("\tIt didn't come with a label");
				  }
		  }
		  */

		  if (p instanceof FunctionCallPStmt)
		  {
				  // XXX 2005-06-21: labels of function calls weren't added to
				  // map previously?
				  /*
				  if (p.getLabel () != null)
						  labelledStatementMap.put (p.getLabel (), p);
				  */

				  // adds prologue and epilogue to statementList instead of
				  // FunctionCallPStmt

				  // XXX also sets functionDef field

				  // System.err.println ("PProgram: FunctionCall: " + p.getSourceBlock ());
				  FunctionCallPStmt call = (FunctionCallPStmt) p;
				  //System.err.println ("PProgram: labelling function call: " +
				  //                    call.getFunctionName () + ", " +
				  //                    new Integer (call.getCallIndex ()));
				  PFunctionDef def =
						  (PFunctionDef) functionDefs.get (call.getFunctionName ());

				  PStmt madeCall;  

				  if (def == null)
				  {
						  System.err.println ("PProgram: Missing function def for: " + call.getFunctionName ());
						  madeCall = new SkipPStmt (call.getParent (), call.getSourceExpr ());
						  // XXX probably not the correct way to do this
						  if (p.getLabel () != null)
						  {
								  labelledStatementMap.put (p.getLabel (), madeCall);
								  madeCall.setLabel (p.getLabel ());
						  }
				  }
				  else  
						  madeCall = def.makeFunctionCall (call);

				  call.setFunctionDef (def);
				  call.setLogues (madeCall);
				  // System.err.println ("made call");
				  // madeCall.print ();
				  // madeCall.setLabel (p.getSourceBlock ().getLabel ());
				  if (call.getSourceExpr ().op () == CILLabelledStmtOp.LSTMT)
						  madeCall.setLabel (CILLabelledStmtOp.getLabel (call.getSourceExpr ()));
				  labelStmtsFlat (madeCall);

				  // XXX had to put this here since if next statement is a
				  // FunctionCallPStmt, it will get ignored below
				  labelStmtsFlat (call.getNext ()); // will label successors as usual
				  return;
		  }

		  if (p == null)
				  return;

		  int labelIndex = statementList.size ();  

		  // NB: id is one less than labelIndex below
		  p.setId (labelIndex);

		  statementList.add (p);

		  labelIndex++;

		  /*
			 if (p.getSourceBlock () != null)
			 {  
			 p.setLabel (p.getSourceBlock ().getLabel ());
			 }
		   */

		  if (p.getSourceExpr () != null &&
						  p.getSourceExpr ().op () == CILLabelledStmtOp.LSTMT)
		  {
				  p.setLabel (CILLabelledStmtOp.getLabel (p.getSourceExpr ()));
		  }

		  if (p.getLabel () == null)
				  // statement has no label; generate one and tag it
				  p.setLabel ("l" + labelIndex);
		  else
				  // statement originally had a label, add it to map
				  labelledStatementMap.put (p.getLabel (), p);

		  if (p instanceof IfPStmt)
		  {
				  IfPStmt ip = (IfPStmt) p;
				  labelStmtsFlat (ip.getThenStmt ());
				  labelStmtsFlat (ip.getElseStmt ());
		  }

		  labelStmtsFlat (p.getNext ());
  }
 

  int conta=0;
  private void labelStmts (PStmt p)
  {
		  /*
		  if (p != null)	  
		  {
				  System.out.println ("Labelling a " + p.getSourceExpr ());	  
				  if (p.getLabel () != null)
				  {
						  System.out.println ("\tIt came with a label: " + p.getLabel());
				  }
				  else
				  {
						  System.out.println ("\tIt didn't come with a label");
				  }
		  }
		  */
		  
		  if (p instanceof FunctionCallPStmt)
		  {
				  // XXX 2005-06-21: labels of function calls weren't added to
				  // map previously?
				  /*
				  if (p.getLabel () != null)
						  labelledStatementMap.put (p.getLabel (), p);
				  */

				  // adds prologue and epilogue to statementList instead of
				  // FunctionCallPStmt

				  // XXX also sets functionDef field

				  // System.err.println ("PProgram: FunctionCall: " + p.getSourceBlock ());
				  FunctionCallPStmt call = (FunctionCallPStmt) p;
				  //System.err.println ("PProgram: labelling function call: " +
				  //                    call.getFunctionName () + ", " +
				  //                    new Integer (call.getCallIndex ()));
				  PFunctionDef def =
						  (PFunctionDef) functionDefs.get (call.getFunctionName ());

				  PStmt madeCall;  

				  if (def == null)
				  {
						  System.err.println ("PProgram: Missing function def for: " + call.getFunctionName ());
						  madeCall = new SkipPStmt (call.getParent (), call.getSourceExpr ());
						  // XXX probably not the correct way to do this
						  if (p.getLabel () != null)
						  {		
									System.out.println("qui arrivo dentro if");
								  labelledStatementMap.put (p.getLabel (), madeCall);
								  madeCall.setLabel (p.getLabel());
						  }
						  else{
									//System.out.println("qui arrivo dentro else");
								  madeCall.setLabel ("Box_"+call.getFunctionName()+"_"+conta);
								  conta=conta+1;
						  }
				  }
				  else  
						  
						madeCall = def.makeFunctionCall (call);

				  call.setFunctionDef (def);
				  call.setLogues (madeCall);
				  // System.err.println ("made call");
				  // madeCall.print ();
				  // madeCall.setLabel (p.getSourceBlock ().getLabel ());
				  if (call.getSourceExpr ().op () == CILLabelledStmtOp.LSTMT)			
					madeCall.setLabel (CILLabelledStmtOp.getLabel (call.getSourceExpr ()));
				  labelStmts (madeCall);
					//System.out.println("ecco cosa stampa su madecall: "+madeCall.getLabel() );

				  // XXX had to put this here since if next statement is a
				  // FunctionCallPStmt, it will get ignored below
				  labelStmts (call.getNext ()); // will label successors as usual
					//System.out.println("ecco cosa stampa su call: "+call.getLabel() );
				  return;
		  }

		  if (p == null)
				  return;

		  int labelIndex = statementList.size ();  

		  // NB: id is one less than labelIndex below
		  p.setId (labelIndex);

		  statementList.add (p);

		  labelIndex++;

		  /*
			 if (p.getSourceBlock () != null)
			 {  
			 p.setLabel (p.getSourceBlock ().getLabel ());
			 }
		   */

		  if (p.getSourceExpr () != null &&
						  p.getSourceExpr ().op () == CILLabelledStmtOp.LSTMT)
		  {
				  p.setLabel (CILLabelledStmtOp.getLabel (p.getSourceExpr ()));
		  }

		  if (p.getLabel () == null)
				  // statement has no label; generate one and tag it
				  p.setLabel ("l" + labelIndex);
		  else
				  // statement originally had a label, add it to map
				  labelledStatementMap.put (p.getLabel (), p);

		  if (p instanceof IfPStmt)
		  {
				  IfPStmt ip = (IfPStmt) p;
				  labelStmts (ip.getThenStmt ());
				  labelStmts (ip.getElseStmt ());
		  }

		  labelStmts (p.getNext ());
			
  }

  private void computeCFGForEachFunctionFlat ()
  {
    for (Iterator it = functionDefs.values ().iterator (); it.hasNext ();)
    {
      PFunctionDef def = (PFunctionDef) it.next ();
      // System.err.println (">>> " + def.getFunctionName ());
      computeCFGFlat (def.getHead (), def.getReturnSelector (),
          def.getReturnSelector (), def.getReturnSelector ());
    }
  }



  private void computeCFGForEachFunction ()
  {
    for (Iterator it = functionDefs.values ().iterator (); it.hasNext ();)
    {
      PFunctionDef def = (PFunctionDef) it.next ();
       //System.err.println (">>> " + def.getFunctionName ());
      computeCFG (def.getHead (), def.getReturnSelector (),
          def.getReturnSelector (), def.getReturnSelector ());
    }
  }

  // XXX Obsolete --- use computeCFG (below)
  private void __computeCFG (PStmt p, PStmt ifExitStmt, PStmt whileElseStmt,
      PStmt returnSelector)
  {
    PStmt nextStmt = p.getNext ();

    if (nextStmt == null)
    {
      if (ifExitStmt != null)
      {
        System.err.println (">>> computeCFG: null next, using ifExit: " + p);
        System.err.println (">>> ifExit: " + ifExitStmt);
        p.setNext (ifExitStmt);
      }
      else if (returnSelector != null)
      {
        System.err.println
          (">>> computeCFG: null next, using returnSelector: " + p);
        System.err.println (">>> returnSelector: " + returnSelector);
        p.setNext (returnSelector);
      }
      else
        throw new RuntimeException ("Statement has no next and all patch " +
            "targets are null: " + p);  

      p.setNext (ifExitStmt);
    }



    // XXX if a function call is the first statement of a function body, it
    // does get expanded into its logues but only because it is actually the
    // nextStmt of the function body's entry point (which gets added
    // automatically when a PFunctionDef is created)

    // -- expand function call into logues
    if (nextStmt instanceof FunctionCallPStmt)
    {
      FunctionCallPStmt call = (FunctionCallPStmt) nextStmt;
      p.setNext (call.getLogues ());
      call.getLogues ().setTail (nextStmt.getNext ());
      nextStmt = p.getNext ();
    }

    if (p instanceof IfPStmt)
    {

      IfPStmt ip = (IfPStmt) p;

      // -- if ip.getNext () is a FunctionCallPStmt, the logues are
      // -- patched in above, so we don't do it again here

      // -- if ip.getThenStmt () is a FunctionCallPStmt, the logues are
      // -- patched in here

      if (ip.getThenStmt () instanceof FunctionCallPStmt)
      {
        FunctionCallPStmt call = (FunctionCallPStmt) ip.getThenStmt ();
        call.getLogues ().setTail (ip.getThenStmt ().getNext ());
        ip.setThenStmt (call.getLogues ());
        ip.getThenGoto ().setDest (ip.getThenStmt ());
      }

      if (ip.getElseStmt () instanceof FunctionCallPStmt)
      {
        FunctionCallPStmt call = (FunctionCallPStmt) ip.getElseStmt ();
        call.getLogues ().setTail (ip.getElseStmt ().getNext ());
        ip.setElseStmt (call.getLogues ());
        ip.getElseGoto ().setDest (ip.getElseStmt ());
      }


      if (p instanceof WhilePStmt)
      {
        WhilePStmt wp = (WhilePStmt) ip;

        wp.getElseGoto ().setDest (wp.getNext ());

        /*
        // -- if ep.getThenStmt () is a FunctionCallPStmt, the logues are
        // patched in here
        if (wp.getThenStmt () instanceof FunctionCallPStmt)
        {
        System.err.println (">>> here");
        FunctionCallPStmt call = (FunctionCallPStmt) wp.getThenStmt ();
        call.getLogues ().setTail (wp.getThenStmt ().getNext ());
        wp.setThenStmt (call.getLogues ());
        wp.getThenGoto ().setDest (wp.getThenStmt ());
        }
         */

        computeCFG
          (wp.getThenStmt (), ifExitStmt, wp.getNext (), returnSelector);
      }
      else
      {
        // p instanceof IfPStmt

        if (ip.getThenStmt () != null)
          computeCFG
            (ip.getThenStmt (),
             ip.getNext () == null ? ip.getNext () : returnSelector, 
             whileElseStmt, returnSelector);
        else
          throw new RuntimeException ("If without a body");

        if (ip.getElseStmt () == null)
          ip.getElseGoto ().setDest (ip.getNext ());
        else
        {
          computeCFG
            (ip.getElseStmt (), ip.getNext (), whileElseStmt, returnSelector);
          ip.getExitGoto ().setDest (ip.getNext ());
        }
      }
    }
    else if (p instanceof GotoPStmt)
    {
      if (p instanceof BreakPStmt)
      {
        BreakPStmt bp = (BreakPStmt) p;

        bp.setDest (whileElseStmt);
      }
      else
      {
        // p instanceof GotoPStmt

        GotoPStmt gp = (GotoPStmt) p;

        // The goto dest is already set if it was generated in abstracting a
        // WHILE Block; so we only set it if the GotoPStmt was generated
        // for something else, e.g. an actual GOTO
        if (gp.getDest () == null)
        {
          // System.err.println ("getting statement " + gp.getTargetLabel ());
          // System.err.println ("got ");
          // getStmt (gp.getTargetLabel ()).print ();
          gp.setDest (getStmt (gp.getTargetLabel ()));
        }
      }
    }
    else if (p instanceof PrllAsmtPStmt)
    {
      if (p instanceof ReturnPStmt)
        p.setDest (returnSelector);
    }
    else if (p instanceof SkipPStmt)
    {
      // do nothing
    }
    else if (p instanceof FunctionCallPStmt)
    {
      // XXX Do nothing for now, but this must be removed eventually; we
      // should never handle a FunctionCallPStmt directly
    }
    else
    {
      throw new RuntimeException ("Don't know how to computeCFG for: " +
          p.getClass ());
    }

    if (nextStmt != null)
      computeCFG (nextStmt, ifExitStmt, whileElseStmt, returnSelector);
  }

   private void computeCFGFlat (PStmt p, PStmt ifExitStmt, PStmt whileElseStmt,
      PStmt returnSelector)
  {
    PStmt nextStmt = p.getNext ();

    // XXX if a function call is the first statement of a function body, it
    // does get expanded into its logues but only because it is actually the
    // nextStmt of the function body's entry point (which gets added
    // automatically when a PFunctionDef is created)

    // -- expand function call into logues
    if (nextStmt instanceof FunctionCallPStmt)
    {
      FunctionCallPStmt call = (FunctionCallPStmt) nextStmt;
      p.setNext (call.getLogues ());
      call.getLogues ().setTail (nextStmt.getNext ());
      nextStmt = p.getNext ();
    }

    if (p instanceof IfPStmt)
    {

      IfPStmt ip = (IfPStmt) p;

      // -- if ip.getNext () is a FunctionCallPStmt, the logues are
      // -- patched in above, so we don't do it again here

      // -- if ip.getThenStmt () is a FunctionCallPStmt, the logues are
      // -- patched in here

      if (ip.getThenStmt () instanceof FunctionCallPStmt)
      {
        FunctionCallPStmt call = (FunctionCallPStmt) ip.getThenStmt ();
        call.getLogues ().setTail (ip.getThenStmt ().getNext ());
        ip.setThenStmt (call.getLogues ());
        ip.getThenGoto ().setDest (ip.getThenStmt ());
      }

      if (ip.getElseStmt () instanceof FunctionCallPStmt)
      {
        FunctionCallPStmt call = (FunctionCallPStmt) ip.getElseStmt ();
        call.getLogues ().setTail (ip.getElseStmt ().getNext ());
        ip.setElseStmt (call.getLogues ());
        ip.getElseGoto ().setDest (ip.getElseStmt ());
      }

      if (ip instanceof WhilePStmt)
      {
        WhilePStmt wp = (WhilePStmt) ip;

        // XXX make sure that wp.getNext () == null is handled
        PStmt wpNextDest = wp.getNext ();
        if (wpNextDest == null)
        {
          // -- if this while is the last statement of 
          // -- an if statement, once it is done we exit the 
          // -- if statement as well
          if (ifExitStmt != null) 
            wpNextDest = ifExitStmt;
          // -- we know that 
          // -- whileElseStmt != null -> wp.getNext () != null
          // 	      else if (whileElseStmt != null)
          // 		wpNextDest = whileElseStmt;
          // -- this while is the last statement of a function
          else
            wpNextDest = returnSelector;
        }

        wp.getElseGoto ().setDest (wpNextDest);

        computeCFGFlat (wp.getThenStmt (), 
            ifExitStmt, wp.getElseGoto ().getDest (), 
            returnSelector);
      }
      else // p instanceof IfPStmt
      {
        PStmt ipNextDest = ip.getNext ();

        if (ipNextDest == null)
        {
          if (ifExitStmt != null)
            ipNextDest = ifExitStmt;
          else
            ipNextDest = returnSelector;
        }

        if (ip.getThenStmt () != null)
          computeCFGFlat
            (ip.getThenStmt (), ipNextDest, whileElseStmt, returnSelector);
        else
          throw new RuntimeException ("If without a body");

        if (ip.getElseStmt () == null)
          ip.getElseGoto ().setDest (ipNextDest);
        else
        {
          computeCFGFlat (ip.getElseStmt (), ipNextDest, 
              whileElseStmt, returnSelector);
          ip.getExitGoto ().setDest (ipNextDest);
        }
      }
    }
    else if (p instanceof GotoPStmt)
    {
      if (p instanceof BreakPStmt)
        ((BreakPStmt)p).setDest (whileElseStmt);

      else
      {
        // p instanceof GotoPStmt

        GotoPStmt gp = (GotoPStmt) p;

        // The goto dest is already set if it was generated in abstracting a
        // WHILE Block; so we only set it if the GotoPStmt was generated
        // for something else, e.g. an actual GOTO
        if (gp.getDest () == null)
        {
          /*
          System.err.println (">>> '" + gp.getTargetLabel () + "'");
          System.err.println (">>> " + gp.getSourceExpr ());
          System.err.println (">>> " + gp.getParent ());
          */
          gp.setDest (getStmt (gp.getTargetLabel ()));
        }
      }
    }
    else if (p instanceof ReturnSelectorPStmt)
    {
      for (Iterator it =
           ((ReturnSelectorPStmt) p).getTargetLabels ().iterator ();
           it.hasNext ();)
        ((ReturnSelectorPStmt) p).getDests ().add
          (getStmt ((String) it.next ()));
    }
    else if (p instanceof NDGotoPStmt)
    {
      for (Iterator it = ((NDGotoPStmt) p).getTargetLabels ().iterator ();
           it.hasNext ();)
        ((NDGotoPStmt) p).getDests ().add (getStmt ((String) it.next ()));
    }
    else if (p instanceof PrllAsmtPStmt)
    {
      if (p instanceof ReturnPStmt)
        p.setDest (returnSelector);
    }
    else if (p instanceof SkipPStmt)
    {
      // do nothing
    }
    else if (p instanceof FunctionCallPStmt)
    {
      // XXX Do nothing for now, but this must be removed eventually; we
      // should never handle a FunctionCallPStmt directly
    }
    else
    {
      throw new RuntimeException ("Don't know how to computeCFG for: " +
          p.getClass ());
    }

    if (!(p instanceof IfPStmt) && !(p instanceof NDGotoPStmt) &&
        p.getDest () == null)
    {
      if (ifExitStmt != null)
        p.setDest (ifExitStmt);
      // 	else if (whileElseStmt != null)
      // 	  p.setDest (whileElseStmt);
      else if (returnSelector != null)
        p.setDest (returnSelector);

      assert p.getDest () != null : "Missed a case for destination of " + p;
    }

    if (nextStmt != null)
      computeCFGFlat (nextStmt, ifExitStmt, whileElseStmt, returnSelector);
  }
 


  private void computeCFG (PStmt p, PStmt ifExitStmt, PStmt whileElseStmt,
      PStmt returnSelector)
  {
    PStmt nextStmt = p.getNext ();

    // XXX if a function call is the first statement of a function body, it
    // does get expanded into its logues but only because it is actually the
    // nextStmt of the function body's entry point (which gets added
    // automatically when a PFunctionDef is created)

    // -- expand function call into logues
    if (nextStmt instanceof FunctionCallPStmt)
    {
      FunctionCallPStmt call = (FunctionCallPStmt) nextStmt;
      p.setNext (call.getLogues ());
      call.getLogues ().setTail (nextStmt.getNext ());
      nextStmt = p.getNext ();
	//System.out.println("nextStmt: "+nextStmt.getLabel());
    }

    if (p instanceof IfPStmt)
    {
	//System.out.println("if: "+p.getLabel());
      IfPStmt ip = (IfPStmt) p;

      // -- if ip.getNext () is a FunctionCallPStmt, the logues are
      // -- patched in above, so we don't do it again here

      // -- if ip.getThenStmt () is a FunctionCallPStmt, the logues are
      // -- patched in here

      if (ip.getThenStmt () instanceof FunctionCallPStmt)
      {
        FunctionCallPStmt call = (FunctionCallPStmt) ip.getThenStmt ();
        call.getLogues ().setTail (ip.getThenStmt ().getNext ());
        ip.setThenStmt (call.getLogues ());
        ip.getThenGoto ().setDest (ip.getThenStmt ());
      }

      if (ip.getElseStmt () instanceof FunctionCallPStmt)
      {
        FunctionCallPStmt call = (FunctionCallPStmt) ip.getElseStmt ();
        call.getLogues ().setTail (ip.getElseStmt ().getNext ());
        ip.setElseStmt (call.getLogues ());
        ip.getElseGoto ().setDest (ip.getElseStmt ());
      }

      if (ip instanceof WhilePStmt)
      {
        WhilePStmt wp = (WhilePStmt) ip;

        // XXX make sure that wp.getNext () == null is handled
        PStmt wpNextDest = wp.getNext ();
        if (wpNextDest == null)
        {
          // -- if this while is the last statement of 
          // -- an if statement, once it is done we exit the 
          // -- if statement as well
          if (ifExitStmt != null) 
            wpNextDest = ifExitStmt;
          // -- we know that 
          // -- whileElseStmt != null -> wp.getNext () != null
          // 	      else if (whileElseStmt != null)
          // 		wpNextDest = whileElseStmt;
          // -- this while is the last statement of a function
          else
            wpNextDest = returnSelector;
        }

        wp.getElseGoto ().setDest (wpNextDest);

        computeCFG (wp.getThenStmt (), 
            ifExitStmt, wp.getElseGoto ().getDest (), 
            returnSelector);
      }
      else // p instanceof IfPStmt
      {
        PStmt ipNextDest = ip.getNext ();

        if (ipNextDest == null)
        {
          if (ifExitStmt != null)
            ipNextDest = ifExitStmt;
          else
            ipNextDest = returnSelector;
        }

        if (ip.getThenStmt () != null)
          computeCFG
            (ip.getThenStmt (), ipNextDest, whileElseStmt, returnSelector);
        else
          throw new RuntimeException ("If without a body");

        if (ip.getElseStmt () == null)
          ip.getElseGoto ().setDest (ipNextDest);
        else
        {
          computeCFG (ip.getElseStmt (), ipNextDest, 
              whileElseStmt, returnSelector);
          ip.getExitGoto ().setDest (ipNextDest);
        }
      }
    }
    else if (p instanceof GotoPStmt)
    {
	//System.out.println("goto: "+p.getLabel());
      if (p instanceof BreakPStmt)
        ((BreakPStmt)p).setDest (whileElseStmt);

      else
      {
	//System.out.println("goto entra nell'else: "+p.getLabel());
        // p instanceof GotoPStmt

        GotoPStmt gp = (GotoPStmt) p;

        // The goto dest is already set if it was generated in abstracting a
        // WHILE Block; so we only set it if the GotoPStmt was generated
        // for something else, e.g. an actual GOTO
        if (gp.getDest () == null)
        {
		//System.out.println("goto entra nel successivo if: "+p.getLabel());
          /*
          System.err.println (">>> '" + gp.getTargetLabel () + "'");
          System.err.println (">>> " + gp.getSourceExpr ());
          System.err.println (">>> " + gp.getParent ());
          */
		if( ( p.getLabel() ).equals("main_RETURN_SELECTOR_HEAD") )
          		gp.setDest (getStmt (gp.getTargetLabel ()));
		else
			p.setDest (getStmt ("END"));
		//System.out.println("ecco il target: "+gp.getTargetLabel ());
        }
      }
    }
    else if (p instanceof ReturnSelectorPStmt)
    {
	//System.out.println("returnselector: "+p.getLabel());
      /*for (Iterator it =
           ((ReturnSelectorPStmt) p).getTargetLabels ().iterator ();
           it.hasNext ();)
        ((ReturnSelectorPStmt) p).getDests ().add
          (getStmt ((String) it.next ()));*/
	 p.setDest (getStmt ("END"));
    }
    else if (p instanceof NDGotoPStmt)
    {
	//System.out.println("ndgoto: "+p.getLabel());
      for (Iterator it = ((NDGotoPStmt) p).getTargetLabels ().iterator ();
           it.hasNext ();)
        ((NDGotoPStmt) p).getDests ().add (getStmt ((String) it.next ()));
    }
    else if (p instanceof PrllAsmtPStmt)
    {
	//System.out.println("prll: "+p.getLabel());
      if (p instanceof ReturnPStmt)
        p.setDest (returnSelector);
    }
    else if (p instanceof SkipPStmt)
    {
      // do nothing
	//System.out.println("skip: "+p.getLabel());
    }
    else if (p instanceof FunctionCallPStmt)
    {
      // XXX Do nothing for now, but this must be removed eventually; we
      // should never handle a FunctionCallPStmt directly
	//System.out.println("funcall: "+p.getLabel());
    }
    else
    {
      throw new RuntimeException ("Don't know how to computeCFG for: " +
          p.getClass ());
    }

    if (!(p instanceof IfPStmt) && !(p instanceof NDGotoPStmt) &&
        p.getDest () == null)
    {
	//System.out.println("altri: "+p.getLabel());
      if (ifExitStmt != null)
        p.setDest (ifExitStmt);
      // 	else if (whileElseStmt != null)
      // 	  p.setDest (whileElseStmt);
      else if (returnSelector != null)
        p.setDest (returnSelector);

      assert p.getDest () != null : "Missed a case for destination of " + p;
    }

    if (nextStmt != null)
      computeCFG (nextStmt, ifExitStmt, whileElseStmt, returnSelector);
  }

  public static PProgram __parse (NullAbstractor abstractor, String fileName) 
    throws ParseException, FileNotFoundException
    {
      return __parse (abstractor, new File (fileName));
    }

  public static PProgram __parse (NullAbstractor abstractor, File file) 
    throws ParseException, FileNotFoundException
    {
      return __parse (abstractor, new FileReader (file));
    }

  public static ArrayList parse (NullExprAbstractor abstractor, String fileName) 
    throws ParseException, FileNotFoundException
    {
      return parse (abstractor, new File (fileName));
    }

  public static ArrayList parse (NullExprAbstractor abstractor, File file)
    throws ParseException, FileNotFoundException
    {
      return parse (abstractor, file, DDSelector);
    }

  public static ArrayList parse (NullExprAbstractor abstractor, File file,
                                int selectorType) 
    throws ParseException, FileNotFoundException
    {
      return parse (abstractor, file, selectorType, false);
    }

  public static ArrayList parse (NullExprAbstractor abstractor, File file,
                                int selectorType, boolean stmtBlocking)  
    throws ParseException, FileNotFoundException
    {
      return parse (abstractor, new FileReader (file), selectorType,
                    stmtBlocking);
    }

  public static ArrayList parse (NullExprAbstractor abstractor, Reader in,
                                int selectorType, boolean stmtBlocking)
    throws ParseException
  {
    try
    {
      Expr programTree = CILTransformerUtil.getProgram
        (abstractor.getFactory (), in, stmtBlocking);

		/*
		System.out.println ("Program Expr Tree <<<");
		System.out.println (programTree);
		System.out.println (">>>");
		*/

      ArrayList p = abstractor.doProgramAbstract (programTree, selectorType);
	//System.out.println("arriva qui");
	return p;
    }
    catch (Exception ex)
    {
      throw new ParseException ("Error parsing", ex);
    }
    
  }

  public static PProgram __parse (NullAbstractor abstractor, Reader in)
    throws ParseException
    {
      // -- create a parser
      CILParser cilParser = CILParserUtil.getParser (in);
      // -- create a blocker to break C program into blocks
      CILProgramBlocker cilBlocker = new CILProgramBlocker ();
      cilBlocker.setASTNodeType (Block.class.getName ());

      try 
      {
        // -- parse 
        cilParser.translationUnit ();
      } 
      catch (Exception ex)
      {
        throw new ParseException ("Error parsing", ex);
      }

      Block ast = (Block) cilParser.getAST ();

      try
      {
        // -- run blocker
        cilBlocker.translationUnit (ast);
      }
      catch (Exception ex)
      {
        throw new ParseException ("Error blocking", ex);
      }

      // -- do nullary abstraction
      PProgram pProgram =
        abstractor.doProgramAbstract ((Block) cilBlocker.getAST ());

      return pProgram;
    }

  public static class ParseException extends Exception
  {
    public ParseException (String msg, Exception ex)
    {
      super (msg, ex);
    }
  }

  public ExprFactory getExprFactory ()
  {
    return fac;
  }

  public boolean isDeclsRefined ()
  {
    return declsRefined;
  }

  public void setDeclsRefined (boolean v)
  {
    declsRefined = v;
  }

  public int getSelectorType ()
  {
    return selectorType;
  }

  /** 
   * Class tester method
   * 
   * @param args 
   */
 /* public static void main (String[] args)
  {
    try
    {
      // -- parse input
      PProgram pProgram =
        parse (new NullExprAbstractor (new ExprFactoryImpl ()), args [0]);
    }
    catch (Exception ex)
    {
      System.out.println ("Exception: " + ex);
      ex.printStackTrace ();
    }
  }*/
} 

