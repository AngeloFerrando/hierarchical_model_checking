package edu.toronto.cs.cparser;

import java.io.*;

import antlr.*;

import edu.toronto.cs.expr.*;

import java.util.Map;

public class CILTransformerUtil
{

  /**
   * Parses an input stream into an expression tree
   *
   * same as <code>getProgram (fac, file, false)</code>
   */
  public static Expr getProgram (ExprFactory fac, Reader file)
    throws RecognitionException, TokenStreamException
  {
    return getProgram (fac, file, false);
  }

  public static Expr getProgram (ExprFactory fac, Reader file, int traceFrom,
                                 int traceTo)
    throws RecognitionException, TokenStreamException
  {
    return getProgram (fac, file, false, traceFrom, traceTo);
  }

  /**
   * Describe <code>getProgram</code> method here.
   *
   * Parses an input stream into an expression tree
   *
   * @param fac an <code>ExprFactory</code> used to build expressions
   * @param file a <code>Reader</code> over the input stream
   * @param stmtBlocking indicates whether to block the program
   * @return a parsed program as an <code>Expr</code> 
   * @exception RecognitionException if input is not a CIL program
   * @exception TokenStreamException if there is an error in the input
   * stream
   */
  public static Expr getProgram (ExprFactory fac, Reader file,
                                 boolean stmtBlocking)
    throws RecognitionException, TokenStreamException
  {
		  return getProgram (fac, file, stmtBlocking, 0, 0);
  }

  public static Expr getProgram (ExprFactory fac, Reader file,
                                 boolean stmtBlocking, int traceFrom,
								 int traceTo)
    throws RecognitionException, TokenStreamException
  {
    // -- create a GnuC parser and run it over the input stream
    // -- the result is an AST or exception if unparsable
    GnuCParser gnuCParser = GnuCParserUtil.getParser (file);
	gnuCParser.setTraceFrom (traceFrom);
	gnuCParser.setTraceTo (traceTo);

    gnuCParser.translationUnit ();

    // -- construct a CILTransformer to convert the AST to Expr tree
    CILTransformer cilTransformer = new CILTransformer ();
    cilTransformer.setASTNodeClass (TNode.class.getName ());

    // -- parse the AST tree, getting Expr tree as a side-effect
    Expr pTree = cilTransformer.translationUnit (gnuCParser.getAST (), fac,
                                                 stmtBlocking);
    // -- fix number of call sites for functions
    return resolveFunctionCalls (fac, pTree, cilTransformer.getCallNumMap ());
  }

  /**
   * For every function call adds number of call sites
   *
   * PRECONDITION: <code>pTree</code> is a
   * <code>CILProgramOp.PROGRAM</code> expression
   *
   * @param fac an <code>ExprFactory</code> value
   * @param pTree a program
   * @param callNumMap a mapping functionName -> num of call sites
   * @return a new program
   */
  private static Expr resolveFunctionCalls (ExprFactory fac, Expr pTree,
					    Map callNumMap)
  {

    Expr[] kids = new Expr[pTree.arity ()];

    for (int i = 0; i < pTree.arity (); i++)
      kids [i] = pTree.arg (i).op () == CILFunctionDefOp.FUNCTION_DEF ?
                 fixedFunctionDef (fac, pTree.arg (i), callNumMap) :
                 pTree.arg (i);

    return fac.op (CILProgramOp.PROGRAM).naryApply (kids);
  }



  /**
   * Looks up the number of calls to <code>functionDef</code> in
   * <code>callNumMap</code> and returns a corresponding
   * <code>FUNCTION_DEF</code> with the (number of call sites) child
   * filled in 
   *
   * PRECONDITION: <code>functionDef</code> has an entry in callNumMap
   *
   * @param fac an <code>ExprFactory</code> 
   * @param functionDef a function definition to be fixed
   * @param callNumMap a callNumMap from
   * <code>CILTransformer.getCallNumMap ()</code>
   * @return a fixed function definition as an  <code>Expr</code>
   */
  private static Expr fixedFunctionDef (ExprFactory fac, Expr functionDef,
                                 Map callNumMap)
  {
    // System.err.println ("fixedFD: " + functionDef);
    if (callNumMap.containsKey 
	(CILFunctionDefOp.getFunctionName (functionDef)))
      return fac.op (CILFunctionDefOp.FUNCTION_DEF).naryApply
        (new Expr[] {functionDef.arg (0),
                     functionDef.arg (1),
                     functionDef.arg (2),
                     fac.intExpr (((Integer) callNumMap.get
                      (CILFunctionDefOp.getFunctionName (functionDef))).
                        intValue ())});
    // -- otherwise there are no calls to this function anyway
    return functionDef;    
  }
}
