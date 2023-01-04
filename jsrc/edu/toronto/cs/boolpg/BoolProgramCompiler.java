package edu.toronto.cs.boolpg;

import edu.toronto.cs.util.*;
import edu.toronto.cs.algebra.*;
import edu.toronto.cs.modelchecker.*;
import edu.toronto.cs.mvset.*;
import edu.toronto.cs.ctl.*;
import edu.toronto.cs.cfa.*;
import edu.toronto.cs.cfa.CFAMvSetFactory.*;
import edu.toronto.cs.boolpg.parser.*;
import edu.toronto.cs.boolpg.parser.VariableTable.*;


import java.io.*;
import java.util.*;
import antlr.*;


public class BoolProgramCompiler implements ModelCompiler
{
  BelnapAlgebra algebra;
  VariableTable symbolTable;
  Map labelMap;
  MvRelation trans;
  MvSet init;
  boolean useHyperEdges;

  int lines;
  CFAMvSetFactory cfaMvSetFactory;
  MDDMvSetFactory mvSetFactory;
  
  File inputFile;
  
  public BoolProgramCompiler ()
  {
    algebra = new BelnapAlgebra ();
    inputFile = null;
    useHyperEdges = false;
  }
  
  public void setUseHyperEdges (boolean v)
  {
    useHyperEdges = v;
  }
  
  public boolean getUseHyperEdges ()
  {
    return useHyperEdges; 
  }

  public File getInputFile ()
  {
    return inputFile;
  }
  public void setInputFile (File v)
  {
    inputFile = v;
  }
  
  public MvSet getInit ()
  {
    return init;
  }
  public MvRelation getTrans ()
  {
    return trans;
  }
  

  private void seal ()
  {
    try 
      {
	mvSetFactory = (MDDMvSetFactory)
	  MDDMvSetFactory.newMvSetFactory (algebra, 0);
	//symbolTable.getNumVars ());
	
      }
    catch (Exception ex)
      {
	ex.printStackTrace ();
	throw new RuntimeException (ex);
      }
    symbolTable.setMvSetFactory (mvSetFactory);

    for (Iterator it = symbolTable.getVariables ().iterator (); 
	 it.hasNext ();)
      {
	Variable v = (Variable) it.next ();
	
	if (v instanceof StateVariable)
	  ((StateVariable)v).getMvSet ();
      }
    
    
  }
  
  public XKripkeStructure compile ()
  {
    CommonAST parseTree = parseFile ();
    
    BoolProgramBuilder builder =
      new BoolProgramBuilder (symbolTable, labelMap, lines, useHyperEdges);

    try
      {
	builder.start (parseTree);
      }
    catch (RecognitionException ex) 
      {
	ex.printStackTrace ();
	throw new RuntimeException (ex);
      }
    
    CFA cfa = builder.getCFA ();
    cfa.dumpNodes ();
    cfa.dumpEdges ();
    
    cfaMvSetFactory = new CFAMvSetFactory (mvSetFactory, cfa);
//     trans = new MvSetMvRelation (cfaMvSetFactory.makeEdgedMvSet (cfa),
// 				 cfaMvSetFactory.buildCube
// 				 (symbolTable.getVariableIds (0)),
// 				 cfaMvSetFactory.buildCube
// 				 (symbolTable.getVariableIds (1)),
// 				 symbolTable.variableMap (0, 1),
// 				 symbolTable.variableMap (1, 0));
    trans = cfaMvSetFactory.getMvRelation ();
    // XXX figure out what init is
    init = cfaMvSetFactory.embed (0, mvSetFactory.top ());

    return new XKripkeStructure (trans,
				 init,
				 symbolTable.variableMap (0, 1),
				 cfaMvSetFactory.buildCube 
				 (symbolTable.getVariableIds (1)),
				 cfaMvSetFactory.buildCube 
				 (symbolTable.getVariableIds (0)),
				 symbolTable.getVarNames (),
				 algebra,
				 symbolTable.getNumVars (),
				 symbolTable.getNumVars (), 
				 getCtlReWriter (),
				 getStatePresenter (), 
                                 inputFile.getAbsolutePath ());
    
  }
  

  CommonAST parseFile ()
  {
    try
      {
	BoolProgramLexer lexer = 
	  new BoolProgramLexer (new FileReader (inputFile));
	BoolProgramParser parser =
	  new BoolProgramParser (lexer);
	parser.start ();
	symbolTable = parser.getSymbolTable ();
	labelMap = parser.getLabelMap ();
	lines = parser.size ();
	seal ();
	return (CommonAST) parser.getAST ();
      }
    catch (Exception ex)
      {
	// XXX Very bad
	ex.printStackTrace ();
	throw new RuntimeException (ex);
      }
    
  }
  
  
  public CTLNode handleUnknownVariable (String name)
  {

    // -- unknown variable is possibly an algebraic constant
    AlgebraValue value = algebra.getValue (name);
    if (value == algebra.noValue ())
      // -- error handling for unknown variables
      throw new RuntimeException ("Unknown variable: " + name);

    // -- value is valid, so create appropriate CTL for it
    return CTLFactory.createCTLConstantNode (value);
  }
  
  // -- returns a ctl rewriter that resolves variable names in CTL
  public CTLReWriter getCtlReWriter ()
  {
    return new CloningRewriter ()
      {
	public Object visitAtomPropNode (CTLAtomPropNode ctl, Object o)
	{
	  // -- already done
	  if (ctl.getMvSet () == null)
	    {
	      // -- if we seen an atomPropNode it must by a state variable
	      // -- since enumerated variables are handled at '=' level
	      StateVariable var = (StateVariable)symbolTable.
		getByName (ctl.getName ());
	      if (var == null) return handleUnknownVariable (ctl.getName ());
	      
	      
	      // -- set mv-set
	      ctl.setMvSet (cfaMvSetFactory.embedMvSet (var.getMvSet ()));
	    }
	  
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

	  if (!(symbolTable.
		getByName (left.getName ()) instanceof ProcessCounter))
	    return super.visitEqualsNode (ctl, o);

	  // -- get variable from symbol table
	  ProcessCounter var = 
	    (ProcessCounter)symbolTable.getByName (left.getName ());
	  // -- get value from '='
	  String value = right.getName ();

	  int count = -1;

	  // -- first see if we have value in labelMap
	  if (labelMap.containsKey (value))
	    count = ((Integer)labelMap.get (value)).intValue ();
	  else
	    {
	      // -- maybe it is a number of a line
	      try {
		count = Integer.parseInt (value);
	      } catch (NumberFormatException ex) {
		System.out.println ("Unknown pc value" + value);
		ex.printStackTrace ();
		throw new RuntimeException (ex);
	      }
	    }
	  
	  // -- build an mv-set
	  MvSet result = cfaMvSetFactory.embed (count, mvSetFactory.top ());
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
	public CTLNode[] toCTL (MvSet _cube)
	{
	  CFAMvSet cube = (CFAMvSet)_cube;
	  int nodeId = firstNonFalse (cube.getCFA ());
	  assert nodeId != -1;
	  
	  CFA stateCFA = cube.getCFA ();
	  MvSet mvSetCube = stateCFA.getNode (nodeId).getState ();
	  AlgebraValue[] values = (AlgebraValue[]) 
	    mvSetCube.cubeIterator ().next ();
	  
	  List result = new LinkedList ();
	  result.add (CTLFactory.createCTLAtomPropNode ("pc").eq
		      (CTLFactory.createCTLAtomPropNode 
		       (stateCFA.getNode (nodeId).getStrValue ())));
	  for (Iterator it = symbolTable.getVariables ().iterator (); 
	       it.hasNext ();)
	    {
	      Variable var = (Variable) it.next ();
	      if (var.isShadow ()) continue;
	      CTLNode ctl = var.toCTL (values);
	      if (ctl != null) result.add (ctl);
	    }
	  return (CTLNode[])result.toArray (new CTLNode [result.size ()]);
	}
	
	// XXX Don't know if we ever want this at all!
	public CTLNode[][] toCTL (AlgebraValue[][] states)
	{
	  throw new UnsupportedOperationException ();
	}

	private int firstNonFalse (CFA cfa)
	{
	  MvSet bot = mvSetFactory.bot ();
	  //for (Iterator it = cfa.getNodes ().iterator (); it.hasNext ();)
	  for (int i = 0; i < cfa.nodeSize (); i++)
	    {
	      CFA.CFANode node = cfa.getNode (i);
	      if (!node.getState ().equals (bot))
		return node.getId ();
	    }
	  return -1;
	}
	
      };
  }
  
  public static void main (String[] args) throws Exception
  {
    BoolProgramCompiler compiler = new BoolProgramCompiler ();
    compiler.setInputFile (new File (args [0]));
    compiler.compile ();

    CFAMvSetFactory factory = compiler.cfaMvSetFactory;
    
    MvSet bot = factory.createConstant (compiler.algebra.infoTop ());
    System.out.println ("Is constant a constant: " + bot.isConstant ());
    System.out.println ("The value is: " + bot.getValue ());

    compiler.symbolTable.dump ();
    System.out.println ("prime var map: " + 
			ArrayUtil.toString 
			(compiler.symbolTable.variableMap (0,1)));
    System.out.println ("primed variables: " + 
			ArrayUtil.toString 
			(compiler.symbolTable.getVariableIds (1)));

    System.out.println ("unprimed variables: " + 
			ArrayUtil.toString 
			(compiler.symbolTable.getVariableIds (0)));

    MDDMvSetFactory mvSetFactory = compiler.mvSetFactory;
    MvSet a = 
      ((StateVariable)compiler.symbolTable.getByName ("a")).getMvSet ();
    System.out.println ("a is...");
    dumpMvSet (a);
    MvSet mvT = mvSetFactory.top ();
    MvSet mvF = mvSetFactory.bot ();
    System.out.println ("Left branch is ");
     dumpMvSet (a.eq (mvT).and 
 	       (mvT.infoAnd (mvT).infoOr
 		(mvF.infoAnd (mvT).not ()))
 	       );
    
  }

  static void dumpMvSet (MvSet v)
  {
    for (Iterator it = v.cubeIterator (); it.hasNext ();)
      System.out.println (Arrays.asList ((Object[])it.next ()));
  }
  
  
}
