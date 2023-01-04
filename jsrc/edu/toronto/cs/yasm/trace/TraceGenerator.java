package edu.toronto.cs.yasm.trace;

import java.util.*;
import java.net.*;
import java.io.*;


import edu.toronto.cs.ctl.*;
import edu.toronto.cs.mvset.*;
import edu.toronto.cs.modelchecker.*;
import edu.toronto.cs.proof2.*;
import edu.toronto.cs.proof2.CTLProver.*;
import edu.toronto.cs.algebra.*;
import edu.toronto.cs.yasm.pprogram.*;


/**
 * Describe class TraceGenerator here.
 *
 *
 * Created: Mon May 16 14:46:38 2005
 *
 * @author <a href="mailto:arie@eon.cs">Arie Gurfinkel</a>
 * @version 1.0
 */
public class TraceGenerator 
{
  XKripkeStructure xkripke;
  MvSetModelChecker xchek;
  MvSet result;
  AlgebraValue value;

  // -- current proof step
  ProofStep pfStep;
  // -- ctl prover
  CTLProver prover;

  boolean done;
  
  PProgram pProgram;
  

  /**
   * Creates a new <code>TraceGenerator</code> instance.
   *
   */
  public TraceGenerator (PProgram _pProgram,
			 XKripkeStructure _xkripke, 
			 MvSetModelChecker _xchek,
			 CTLNode prop,
			 MvSet _result, AlgebraValue _value) 
  {
    xkripke = _xkripke;
    xchek = _xchek;
    result = _result;
    value = _value;
    pProgram = _pProgram;

    done = false;
    
    prover = null;
    
    MvSet initState = (MvSet)
      xkripke.getInit ().and (result).
      mintermIterator (xkripke.getUnPrimeCube (), value).next ();
    
    Formula formula = new AboveFormula (prop, value, initState);
    pfStep = new TreeProofStep (formula, null);

    
  }

  public Map step ()
  {
    if (prover == null)
	initProver ();
    else
      doNextStep (pfStep);
    
    return currentState ();
  }

  public Map currentState ()
  {
    Map stateView = new HashMap ();
    // -- get current state as CTL
    CTLNode[] ctlState = xkripke.getStatePresenter ().
      toCTL (pfStep.getFormula ().getState ());
    String stateLabel = ctlState [0].getRight ().toString ();
    stateView.put ("@pc", stateLabel);
    
    // -- get a statement corresponding to this program point
    // -- we need this to extract the line number
    PStmt stmt = pProgram.getStmt (stateLabel);
    stateView.put ("@lineNumber", new Integer (stmt.getLineNum ()));
    
    PredicateTable pTable = pProgram.getPredTable ();
    
    for (int i = 0; i < ctlState.length; i++)
      {
	System.err.println ("looking up: " + ctlState [i].toString ());
	PredicateTable.Predicate pred = 
	  pTable.getByCtlName (ctlState [i].getLeft ().toString ());
	System.err.println ("Got: " + pred);

	if (pred != null)
	  stateView.put (pred.getName ().toString (), 
			 ctlState[i].getRight ().toString ());
      }
    
    return stateView;
    
  }
  
  public boolean hasNext ()
  {
    return !done;
  }
  

  private void initProver ()
  {
    prover = new CTLProver (xchek, pfStep);
    // -- initialize the prover with the rules!
    prover.addProofRule (new EqualsProofRule ());
    prover.addProofRule (new CheckingTopBottom (xchek));
    prover.addProofRule (new EqNegationProofRule (xchek));
    prover.addProofRule (new AtomicProofRule (xchek));
    // new!
    prover.addProofRule (new DepthProofRule(xchek));

    prover.addProofRule (new AndOrProofRule (xchek));
    //prover.addProofRule (new EXAboveMProofRule (xchek));
    prover.addProofRule (new EXProofRule (xchek,false));
    //prover.addProofRule (new EXCexProofRule (xchek));
    prover.addProofRule (new EUProofRule (xchek));
    prover.addProofRule (new EUiProofRule (xchek, false));
    prover.addProofRule (new AXProofRule (xchek));
    prover.addProofRule (new EGProofRule (xchek));
    prover.addProofRule (new AUProofRule (xchek));
    prover.addProofRule (new AUiProofRule (xchek));
    
  }
  
  private void doNextStep (ProofStep rootStep)
  {
    System.err.println ("next step of: " + 
			rootStep.getFormula ().getConsequent ());
    
    prover.expand (rootStep);
    if (rootStep.getChildLength () == 0)
      {
	done = true;
	return;
      }
    
    // -- there are children so we are at a TreeProofStep
    TreeProofStep treeStep = (TreeProofStep) rootStep;
    
    // -- pick the best child to follow, we only get 
    // -- one chance here. The idea is to put everything into 
    // -- a sorted set under our special order, and then extract
    // -- the first element
    SortedSet choiceSet = new TreeSet (new Comparator ()
      {
	public int compare (Object _a, Object _b)
	{
	  // -- deal with equality first
	  if (_a.equals (_b)) return 0;

	  ProofStep a = (ProofStep) _a;
	  ProofStep b = (ProofStep) _b;

	  Formula aF = a.getFormula ();
	  Formula bF = b.getFormula ();

	  // -- first choice is EX
	  if (aF.getConsequent () instanceof CTLEXNode)
	    return -1;
	  else if (bF.getConsequent () instanceof CTLEXNode)
	    return 1;
	  
	  // -- second choice is EU
	  if (aF.getConsequent () instanceof CTLEUNode)
	    return -1;
	  else if (bF.getConsequent () instanceof CTLEUNode)
	    return 1;

	  // -- neither is EX, pick based on value
	  if (aF.getValue ().geq (bF.getValue ()).isTop ())
	    return -1;

	  // -- this ensures we have some reasonable ordering
	  // -- in all other cases
	  return a.hashCode () - b.hashCode ();
	  
	}
	
	public boolean equals (Object o)
	{
	  return o == this;
	}
      });
    
    for (int i = 0; i < treeStep.getChildLength (); i++)
      choiceSet.add (treeStep.getChild (i));

    ProofStep nextStep = (ProofStep)choiceSet.iterator ().next ();
    
    if (!nextStep.getFormula ().getState ().
	equals (pfStep.getFormula ().getState ()))
      {
	System.err.println ("Trace generation: found a new state");
	// -- get some presentable form of a state
	CTLNode[] childState = xkripke.getStatePresenter ().
	  toCTL (nextStep.getFormula ().getState ());
	String childLabel = childState [0].getRight ().toString ();
	System.err.println ("nextState: " + childLabel);
	pfStep = nextStep;
	return; 
	
      }
	
    doNextStep (nextStep);
  }
  

  public void startOnPort (int port) throws Exception
  {
    ServerSocket srvr = new ServerSocket (port);
    //srvr.bind (null);
    Socket clntSocket = srvr.accept ();

    InputStream in = clntSocket.getInputStream ();
    OutputStream out = clntSocket.getOutputStream ();
    
    ObjectInputStream oin = new ObjectInputStream (in);
    ObjectOutputStream oout = new ObjectOutputStream (out);
    
    System.out.println ("Waiting for commands");
    
    try {
      while (true)
	{
	  String cmd = (String) oin.readObject ();
	  if (cmd.equals ("quit")) return;
	  
	  Map state;
	  if (hasNext ())
	    {
	      state = step ();
	    }
	  else
	    {
	      state = new HashMap ();
	      state.put ("@done", "@done");
	    }
	  oout.writeObject (state);
	  oout.flush ();
	}
      

    }
    catch (IOException ex) {}
    
    
    
  }
  

}
