package edu.toronto.cs.yasm;

import java.util.*;
import java.io.*;

import java.util.prefs.*;

import edu.toronto.cs.tp.cvcl.CVectorExpr;
import edu.toronto.cs.util.*;
import edu.toronto.cs.util.Clapi.*;

import edu.toronto.cs.yasm.pgenerator.*;
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
import edu.toronto.cs.yasm.pprogram.*;
import edu.toronto.cs.yasm.abstractor.*;
import edu.toronto.cs.expr.*;
import edu.toronto.cs.yasm.util.*;
import edu.toronto.cs.yasm.wp.WPComputer;
import edu.toronto.cs.yasm.trace.*;
import edu.toronto.cs.cfa.*;

/**
 * YasmApp.java
 *
 * Yet Another Software Model Checker
 *
 * Created: Tue Jun 22 13:38:03 2004
 *
 * @author <a href="mailto:maxin@epoch.cs">Xin Ma</a>
 * @version 1.0
 */
public class YasmApp 
{

  /**
   * Standard Output
   *
   */
  public static PrintWriter out = 
    new PrintWriter (new OutputStreamWriter (System.out), true);

  /**
   * Standad Error
   *
   */
  public static PrintWriter err = 
    new PrintWriter (new OutputStreamWriter (System.err), true);

  /**
   * Preferences of the application
   *
   */
  public static final Preferences PREFS 
    = Preferences.userRoot ().node ("edu/toronto/cs/yasm/YasmApp");
  NullExprAbstractor abstractor;

  int controllo = 0;

  int length = 0;	

  ArrayList cfa = new ArrayList();

  CFA flatCFA = null;
  
  ArrayList pProgram = new ArrayList();
  
  ModelCompiler compiler = null;

  ArrayList compilers = new ArrayList();

  ArrayList refiners = new ArrayList();

  PredicateRefiner refiner = null;

  ArrayList stringhe = new ArrayList();

  ArrayList st_ret = new ArrayList();

  Map mappa = new HashMap();

  JCUDDBelnapMvSetFactory mvSetFactory = null;

  PProgram prog = null; 	
	
  PProgramCompiler pCompilerFlat;

  ModelCompiler compilerFlat;  	

  int contatore = 1;

  /**
   * A file containting the boolean program
   *
   */
  File bpFile;

  File initPredFile;
  /**
   * CTL Property being checked
   *
   */
  CTLNode prop;

  /**
   * The XKripke structure for model-checking
   *
   */
  ArrayList kripke = new ArrayList(); 	
  XKripkeStructure xkripke;

  /**
   * Our model-checker
   *
   */
  MvSetModelChecker xchek;

  /**
   * If true then a proof is produced,  false by default
   *
   */
  boolean doProof = true;

  
  /**
   * our algebra
   *
   */
  BelnapAlgebra algebra;

  boolean cFile = true;

  String refinerType = "new"; // <-- override this
  String pGeneratorType = "bend:wpd:wps";
  int selectorType = -1;
  boolean stmtBlocking = false;

  //Boolean found = Boolean.TRUE;
  boolean found = false;
  int newPredFound = 0;
  static int MAX_PREDICATES_PER_REFINEMENT = 1;

  ExprFactory fac;
  
  boolean useHyperEdges;
  boolean unknownInit;


  /**
   * true if previous results of the computation should be reused
   *
   */
  boolean reuse = false;

  YasmStatistics stats = new YasmStatistics ();
  
  StopWatch overall = new StopWatch ();

  /**
   * port used by counterexample trace generator
   *
   */
  int tracePort = 6700;
  
  /**
   * set to true to enable cex trace generation
   *
   */
  boolean doTrace = false;
  
  
  
  
  public YasmApp () 
  {
    fac = new ExprFactoryImpl ();
    // setting up some defaults
    // redundant, but safe
    doProof = true;
    cFile = true;
    useHyperEdges = true;
    unknownInit = true;
    pGeneratorType = "bend:wpd:wps";
    refinerType = "cbj";
    
    
  }

  public void setInitPredFile (File f)
  {
    initPredFile = f;
  }

  public void setBpFile (File f)
  {
    bpFile = f;
  }
  public void setProp (CTLNode v)
  {
    prop = v;
  }

  public void setCFile (boolean v)
  {
    cFile = v;
  }
  public boolean getCFile ()
  {
    return cFile;
  }

  public void setRefinerType (String v)
  {
    refinerType = v;
  }

  public void setSelectorType (String s)
  {
    if (s.equals ("dd"))
      selectorType = PProgram.DDSelector;
    else if (s.equals ("bin"))
      selectorType = PProgram.BinSelector;
    else if (s.equals ("lin"))
      selectorType = PProgram.LinSelector;
    else
      throw new RuntimeException ("Unknown selector type: " + s);
  }

  public void setStmtBlocking (boolean v)
  {
    stmtBlocking = v;
  }

  public void setMaxPredsPerRefinement (int i)
  {
    MAX_PREDICATES_PER_REFINEMENT = i;
  }

  public void setTracePort (int v)
  {
    tracePort = v;
  }
  public void setDoTrace (boolean v)
  {
    doTrace = v;
  }
  public boolean isDoTrace ()
  {
    return doTrace;
  }
  

  public void setPGeneratorType (String v)
  {
    pGeneratorType = v;
  }
  
  public void setHyper (boolean v)
  {
    useHyperEdges = v;
  }
  public void setUnknownInit (boolean v)
  {
    unknownInit = v;
  }

  public boolean isReuse ()
  {
    return reuse;
  }
  public void setReuse (boolean v)
  {
    reuse = v;
  }
  
  


  /**
   * doProof setter
   *
   * @param v a <code>boolean</code> value
   */
  public void setDoProof (boolean v)
  {
    doProof = v;
  }

  public void setXKripkeStructure (XKripkeStructure v)
  {
    //xkripke = v;
    //algebra = (BelnapAlgebra) xkripke.getAlgebra ();
    kripke.add(v);
  }

  public void setXChek (MvSetModelChecker v)
  {
    xchek = v;
  }


  /**
   * Compile boolean program into model-checker representation
   *
   */
  XKripkeStructure compileBooleanProgram ()
  {
    BoolProgramCompiler bCompiler = new BoolProgramCompiler ();
    bCompiler.setInputFile (bpFile);
    setXKripkeStructure (bCompiler.compile ());    
    return xkripke;
  }

  static PredicateRefiner getRefiner (String name, ExprFactory fac)
  {
    if (name.equals ("new"))
      return new LadderPredicateRefiner (fac);
    else if (name.equals ("csp1")) 
      return new BranchPredicateRefiner (fac);
    else if (name.equals ("cbj"))
      return new CBJPredicateRefiner (fac);
    else if (name.equals ("cbj-i"))
      return new CBJPredicateRefiner (fac, false, true);
    else if (name.equals ("cbj-s"))
      return new CBJPredicateRefiner (fac, true, false);
    else if (name.equals ("cbj-s-i") || 
	     name.equals ("cbj-i-s"))
      return new CBJPredicateRefiner (fac, false, false);
    else if (name.equals ("cffc"))
      return new CFFCPredicateRefiner (fac);
    else if (name.equals ("cffc-i"))
      return new CFFCPredicateRefiner (fac, false, true);
    else if (name.equals ("cffc-s"))
      return new CFFCPredicateRefiner (fac, true, false);
    else if (name.equals ("cffc-s-i") || 
	     name.equals ("cffc-i-s"))
      return new CFFCPredicateRefiner (fac, false, false);
    else if (name.equals ("cffc-s-i-d"))
      return new CFFCPredicateRefiner (fac, false, false, false);
    else if (name.equals ("cffc-d"))
      return new CFFCPredicateRefiner (fac, true, true, false);
    
    throw new RuntimeException ("Unknown refiner: " + name);
  }

  XKripkeStructure compileCProgram ()
    throws PProgram.ParseException, FileNotFoundException
    {

      abstractor = new NullExprAbstractor (fac);
      if (pProgram.size() == 0)
	{
	  stats.startParse ();
	  pProgram = PProgram.parse (abstractor, bpFile,selectorType, stmtBlocking);
	  stats.stopParse ();
	  prog = abstractor.getPProgram();
	}
      PredicateRefiner ref = null;
      if(controllo != -1){	
      	for(int i=0;i < pProgram.size();i++){
		PProgram program = (PProgram) pProgram.get(i);
          	compiler = new PProgramCompiler (useHyperEdges,unknownInit);      

      		if (refiners.size() < i+1)
      		{
        		ref = getRefiner (refinerType, fac);

        		ref.stats = stats;

        	if (initPredFile != null)
          		try
          		{
            		ref.readPredicates (new FileReader (initPredFile));
          		}
        		catch (Exception ex)
        		{
          		YasmApp.err.println ("Could not read initial predicates");
          		throw new RuntimeException (ex);
        		}

        	ref.doDeclsRefine (program);
      		}

      		stats.startRefine ();
      		program = ref.doProgramRefine (program);
      		stats.stopRefine ();
      		refiners.add(ref);	

      		System.err.println ();
      		System.err.println ("Refined Boolean Program");
     		System.err.println ();
      		program.print (YasmApp.err);
      		System.err.println ();
      		System.err.println ("Refined Boolean Program END");
      		System.err.println ();

      		PProgramCompiler pCompiler = (PProgramCompiler)compiler;

      		pCompiler.setPProgram (program);
      		// XXX just for now
      		pCompiler.setMaxDDVars (100);
      		stats.startCompile ();
      		setXKripkeStructure (pCompiler.compile ());
      		stats.stopCompile ();
      		compilers.add(pCompiler);
      	}//fine for
      	setListCFA();
      }//fine if
      controllo = -1;		
      setKripkeFlat();
      return xkripke;
    }
    
   public void setListCFA(){
	for(int i=0;i<compilers.size();i++){
		PProgramCompiler p = (PProgramCompiler) compilers.get(i);
		cfa.add( p.getCFA() );
	}
   } 
 
   public void setKripkeFlat()throws PProgram.ParseException, FileNotFoundException{


	if(mvSetFactory == null)
		mvSetFactory =  (JCUDDBelnapMvSetFactory) JCUDDBelnapMvSetFactory.newMvSetFactory (new BelnapAlgebra(), 20);

	
	if (compilerFlat == null)
		compilerFlat = new PProgramCompiler (useHyperEdges,unknownInit);

	if (refiner == null){
		refiner = getRefiner (refinerType, fac);
		refiner.stats = stats;

        	if (initPredFile != null){
          		try
          		{
            		refiner.readPredicates (new FileReader (initPredFile));
          		}
          		catch (Exception ex)
          		{
          		YasmApp.err.println ("Could not read initial predicates");
          		throw new RuntimeException (ex);
          		}
		}        
		refiner.doDeclsRefine (prog);
	}
      
	stats.startRefine ();
      	prog = refiner.doProgramRefine (prog);
      	stats.stopRefine ();

	System.err.println ();
      	System.err.println ("Refined Boolean Program Flat");
     	System.err.println ();
      	prog.print (YasmApp.err);
      	System.err.println ();
      	System.err.println ("Refined Boolean Program Flat END");
      	System.err.println ();

	pCompilerFlat = (PProgramCompiler)compilerFlat;
	pCompilerFlat.setPProgram (prog);
      	pCompilerFlat.setMaxDDVars (100); 

	stats.startCompile ();

	if(length == 0 )
		length = contaNodi();
	flatCFA = new CFA(length);
	setCFA( (CFA) cfa.get(0) , 0 );
	rimuovi(st_ret);
	rimuovi(stringhe);
	contatore = 1;
	pCompilerFlat.setCFA(flatCFA);
        
	pCompilerFlat.creaArchi(cfa);

	StampaCFAFlat();
	
	xkripke = pCompilerFlat.dammiKripke();

	stats.stopCompile ();
	algebra = (BelnapAlgebra) xkripke.getAlgebra ();

   }

   public int contaNodi(){
	int length = 0;
	for(int i=0;i<cfa.size();i++){
		CFA c = (CFA) cfa.get(i);
		ArrayList l = c.getNodes();
		length = length + (l.size()-2);
		//System.out.println("size cfa: "+c.nodeSize());
		for(int j=0;j<l.size();j++){
			if( l.get(j) instanceof CFA.CFANode ){
				CFA.CFANode n = (CFA.CFANode) l.get(j);
				//System.out.println("NODO: "+n.getStrValue());
			} 
			else{
				CFA.CFABox b = (CFA.CFABox) l.get(j);
				//System.out.println("Box: "+b.getStrValue());
				length = length+1;
			}
		}
	}
 	return length+2;
   }
   public void setCFA(CFA c , int index ){
	boolean entry = false;
	if(c.nodeSize() > index ){
		if( c.getNode(index) instanceof CFA.CFANode ){
			CFA.CFANode n = (CFA.CFANode) c.getNode(index);
			if( n.getStrValue().equals("main_RETURN_SELECTOR_HEAD") ){
				if(cfa.size() > 1){
					CFA cc = (CFA) cfa.get(1);	
					CFA.CFANode nn = (CFA.CFANode) cc.getNode(2);
					String ss = nn.getStrValue();
					String[] sst = ss.split("_");
					ss = sst[0];
					stringhe.add(ss); 
					setCFA( cc , 2 );
				}
			}
			if( n.getStrValue().startsWith("l") ){
				flatCFA.addNode( "l"+contatore, mvSetFactory.top() );
				contatore++;
				
			}
			else{
				flatCFA.addNode( n.getStrValue(), mvSetFactory.top() );
				contatore++;
				
			}
			setCFA( c , index+1 );
		}			 
		else{
			CFA.CFABox b = (CFA.CFABox) c.getNode(index) ;
			String s = b.getStrValue();
			mappa.put( s, (ArrayList) c.getFwdEdges(b) );
			String st;
			String [] str;
			str = s.split("_");
			s = str[1];
			if( !trova(s) ){
				st = s+"_CALL_"+str[2];
				flatCFA.addNode(s+"_CALL_"+str[2], mvSetFactory.top() );
				contatore++;
				st = s+"_RETURN_"+str[2];
				flatCFA.addNode(s+"_RETURN_"+str[2], mvSetFactory.top() );
				contatore++;	
				
				entry = true;
			}
			else{
				
				st = s+"_CALL_"+str[2];
				flatCFA.addNode(s+"_CALL_"+str[2], mvSetFactory.top() );
				contatore++;
				st = s+"_RETURN_"+str[2];
				flatCFA.addNode(s+"_RETURN_"+str[2], mvSetFactory.top() );
				contatore++;
						
			}
			stringhe.add(s);
			st_ret.add(st);
			setCFA( c , index+1 );
			if( entry )
				setCFA( cercaCFA(s) , 2 );
			
		}
   	}
   }

   public boolean trova(String s){
	int i=0;
	boolean trovato = false; 	
	while ( !trovato && i<stringhe.size() ){
		String s1 = (String) stringhe.get(i);
		if( s.equals(s1) )
			trovato = true;
	
		i++;
	}
	return trovato;
   }

   public CFA cercaCFA( String s ){
	CFA c = null;
	String [] str;
	//System.out.println("string s: "+s);
	boolean trovato = false;
	int i=0;
	while( !trovato ){
		c = (CFA) cfa.get(i);
		String st = ( (CFA.CFANode) c.getNode(2) ).getStrValue();
		str = st.split("_");
		st = str[0];
		//System.out.println("string st: "+st);
		if( s.equals(st) ){
			trovato = true;
			//System.out.println("stringa accettata: "+st);
		}
		else
			i++;
	}
	return c;
   }
   
   public void rimuovi(ArrayList s){
	int l = s.size();
	while(l>0){
		s.remove(0);
		l--;
	}
   }

   
   public void StampaCFAFlat() {
	ArrayList ls = flatCFA.getNodes();
	System.out.println("NODI DEL CFA-FLAT");
	System.out.println(" ");
	for(int j=0;j<ls.size();j++){
		CFA.CFANode n = (CFA.CFANode) ls.get(j);
		String s = n.getStrValue();
		System.out.println("ID:"+n.getId()+" NODO: "+s);
		//System.out.println("intero stampato: "+d); 
		ArrayList fwd_n = (ArrayList) flatCFA.getFwdEdges(n);
		for(int y=0;y< fwd_n.size();y++){
			CFA.CFAEdge e = (CFA.CFAEdge) fwd_n.get(y);	
			System.out.println("Archi uscenti: "+e.getDestId());
		}
		fwd_n = (ArrayList) flatCFA.getBwdEdges(n);
		for(int y=0;y< fwd_n.size();y++){
			CFA.CFAEdge e = (CFA.CFAEdge) fwd_n.get(y);	
			System.out.println("Archi entranti: "+e.getSourceId());
		}
		System.out.println(" "); 
	}
   }	 
  /**
   * Prepare our property to be checked
   *
   */
  CTLNode prepareProperty ()
  {
    CTLNode result = prop;
    
    // -- resolve all variables
    result = xkripke.rewrite (result);
    
    // -- rewrite the formula using only existential temporal operators
    result = new ExistentialRewriter ().rewrite (result);

    // -- get rid of EF and AF, note that we need to know what 
    // -- 'true' or 'top' is before we can use that
    result = new CTLUntilExpander (xkripke.getMvSetFactory ().top ()).
      rewrite (result);

    // -- check the syntax of the end result in case one of our
    // -- rewriters has a bug
    new SyntaxChecker ().rewrite (result);

    // -- set the property again, and we are done
    setProp (result);
    
    return result;
  }


  MvSet doFwdImage ()
  {
    MvSet prev;
    MvSet cur = xkripke.getInit ();
    
    MvRelation trans = xkripke.getTrans ();
    
    int i = 0;
    do {
      System.out.println ("Fwd Image iteration: " + i);
      i++;
      prev = cur;
      cur = prev.or (trans.fwdImage (cur));
    } while (!prev.equals (cur));
    return prev;
  }
  
  void createXChek ()
  {
    setXChek (new MvSetModelChecker (xkripke));
  }


  void showTrace (MvSet result, AlgebraValue value)
  {
    YasmApp.err.println ("Showing trace for: " + value);
    TraceGenerator trace = new TraceGenerator ((PProgram) pProgram.get(0),
					       xkripke, 
					       xchek,
					       prop,
					       result, 
					       value);
    try 
      {
	trace.startOnPort (tracePort);
      }
    catch (Exception ex)
      {
	ex.printStackTrace ();
      }
          
  }  

  /**
   * Shows a proof on the screen
   *
   */
  void showProof (MvSet result, AlgebraValue value)
  {

    // -- to initialize the proover we need to create one
    // -- goal we are trying to prove. Then (BAD) add the proof rules
    // -- we want to use, and we are done.
	System.out.println("qui entro");
    // -- create ||prop||(init) = value
    MvSet initState = (MvSet)
      xkripke.getInit ().and (result).
      mintermIterator (xkripke.getUnPrimeCube (), value).next ();
    
    Formula formula = new EqualFormula (prop, value, initState);
    TreeProofStep rootStep = new TreeProofStep (formula, null);
    CTLProver prover = new CTLProver (xchek, rootStep);
    
    // -- initialize the proover with the rules we need
    initProver (prover);

    found = false;
    newPredFound = 0;
	System.out.println("qui entro");
    preRefine (prover, rootStep);
	System.out.println("qui entro");
    //    DynamicProofDisplay.showProof (prover, rootStep);
  }

  /**
   * initializes the proover 
   *
   * @return a <code>CTLProover</code> value
   */
  CTLProver initProver (CTLProver prover)
  {
    // -- initialize the prover with the rules!
    prover.addProofRule (new EqualsProofRule ());
    prover.addProofRule (new CheckingTopBottom (xchek));
    prover.addProofRule (new EqNegationProofRule (xchek));
    prover.addProofRule (new AtomicProofRule (xchek));
    // new!
    prover.addProofRule (new DepthProofRule(xchek));

    prover.addProofRule (new AndOrProofRule (xchek));
    prover.addProofRule (new EXAboveMProofRule (xchek));
    //prover.addProofRule (new EXProofRule (xchek,false));
    //prover.addProofRule (new EXCexProofRule (xchek));
    prover.addProofRule (new EUProofRule (xchek));
    prover.addProofRule (new EUiProofRule (xchek, false));
    prover.addProofRule (new AXProofRule (xchek));
    prover.addProofRule (new EGProofRule (xchek));
    prover.addProofRule (new AUProofRule (xchek));
    prover.addProofRule (new AUiProofRule (xchek));
    return prover;
  }


  /**
   * Tries to refine a proof
   *
   * @param prover a <code>CTLProver</code> value
   * @param proofStep a <code>ProofStep</code> value
   */
  void preRefine (CTLProver prover, ProofStep proofStep)
  {
	
    // -- if the value of this formula is not "maybe" (or bottom of
    // -- information ordering, we don't need to do anything
    if (!proofStep.getFormula ().getValue ().equals (algebra.infoBot ()))
      return ;
    else if (found) return;
	

    CTLNode[] parentState = null;
    CTLNode[] childState = null;

    StopWatch proverSW = new StopWatch ();
    YasmApp.err.println ("proof step started");
    prover.expand (proofStep);
    YasmApp.err.println ("proof step done in: " + proverSW);
    if (proofStep.getChildLength () == 0)
    {
      YasmApp.err.println ();
      YasmApp.err.println ();
      YasmApp.err.println ("\t*********");
      YasmApp.err.println ("\tPotential place for refining");
      YasmApp.err.println ("\tType: " + proofStep.getClass ());
      YasmApp.err.println ("\t" + proofStep.getFormula ());
      YasmApp.err.println ("\tFormulaType: " + 
          proofStep.getFormula ().getClass ());
      ProofStep parent = proofStep.getParent ();
      while (parent != null)
      {
        if (!parent.getFormula ().getState ().equals 
            (proofStep.getFormula ().getState ()))
        {
          parentState = xkripke.getStatePresenter ().
            toCTL (parent.getFormula ().getState ());
          YasmApp.err.println 
            ("\tPrev state is: " + 
             ArrayUtil.toString 
             (parentState));
          break;
        }
        parent = parent.getParent ();
      }
      childState = xkripke.getStatePresenter ().
        toCTL (proofStep.getFormula ().getState ());
      YasmApp.err.println 
        ("\tThe state is: " + 
         ArrayUtil.toString 
         (childState));


      String parentLabel = null;
      if (parentState != null && parentState.length >= 1)
        parentLabel = parentState [0].getRight ().toString ();

      String childLabel = childState [0].getRight ().toString ();
      YasmApp.err.println ("parent label: " + parentLabel);
      YasmApp.err.println ("childLabel: " + childLabel);

      PStmt parentStmt = null;
      if (parentLabel != null)
        parentStmt = prog.getStmt (parentLabel);
      PStmt childStmt = prog.getStmt (childLabel);

      if (parentStmt == null) return ;

      if (parentStmt instanceof PrllAsmtPStmt)
      {
        PredicateGenerator pg = 
	  createPredicateGenerator (parentStmt, parentState, childState);
	
        List newPred = null;
	if (pg.find ())
	  {
	    newPred = pg.getNewPreds ();
	    newPredFound += newPred.size () + 1;
	    found = newPredFound >= MAX_PREDICATES_PER_REFINEMENT;
	  }

   	if (newPred != null && !newPred.isEmpty ())
	  {
	    for (Iterator it = newPred.iterator (); it.hasNext ();)
	      refiner.addPredicate 
		(refiner.getCVCLConverter ().fromCVCL 
		 ((edu.toronto.cs.tp.cvcl.Expr) it.next()));
   	  }
      }
      else if (parentStmt instanceof IfPStmt)
      {
        Expr realCond = (Expr) 
	  ((IfPStmt)parentStmt).getCond ().getOrigCond ();


        // -- set the if-condition abstraction to the original condition
        ((IfPStmt) parentStmt).getCond ().setCond (realCond);
	System.out.println("qui Arrivo");
	while (realCond.op () == BoolOp.NOT)
	  realCond = realCond.arg (0);

        YasmApp.out.println ("**************** " + 
			     "Adding predicate due to unknown If condition" + 
			     "****************");
        YasmApp.out.println (refiner.getCVCLConverter().toCVCL(realCond));
	newPredFound++;
	found = newPredFound >= MAX_PREDICATES_PER_REFINEMENT;
	refiner.addPredicate (realCond);
//         refiner.addPredicate (refiner.getCVCLConverter ().fromCVCL 
// 			      (refiner.getVC ().simplify 
// 			       (refiner.getCVCLConverter().
// 				toCVCL(realCond))));
      }
      else if (parentStmt instanceof SkipPStmt && parentLabel != null 
	       && parentLabel.equals ("init"))
	{
	  
	  // 	  System.out.println ("Got parent statement of type: " + 
	  // 			      parentStmt.getClass ());
	  
	  found = isStateConsistent (childState);	  
	}
      


      //found = PreRefiner.addPred (parentState, childState);
      YasmApp.out.println ();
      YasmApp.out.println ();
      // -- done
      return ;
    }

    // -- by this time we know we have children
    TreeProofStep treeStep = (TreeProofStep) proofStep;
    for (int i = 0; i < treeStep.getChildLength (); i++)
      preRefine (prover, treeStep.getChild (i));

  }

  // XXXX Garbage things

  PredicateGenerator createPredicateGenerator (Object stmt, 
					       CTLNode[] parentState, 
					       CTLNode[] childState)
  {
    PredicateTable pTable = prog.getPredTable ();
    CVCLExprConverter cvcl = refiner.getCVCLConverter ();

    Map predMap = new HashMap ();
    for (Iterator it = pTable.getVariables ().iterator (); it.hasNext ();)
      {
	PredicateTable.Predicate pred = (PredicateTable.Predicate) it.next ();
	predMap.put (pred.getCtlName (), cvcl.toCVCL (pred.getName ()));
      }
    // Map symbolicMap = refiner.getMemoryModel ((PrllAsmtPStmt) stmt);    
    // WPComputer wp = refiner.getWPComputer ((PrllAsmtPStmt) stmt);
    WPComputer wp = ((PrllAsmtPStmt) stmt).getWPComputer ();
    
    List cvclPreds = cvcl.toCVCL (refiner.getPredicates ());
    
    CVectorExpr pState = getStatePred (predMap, parentState);
    CVectorExpr cState = getStatePred (predMap, childState);
 
    
    return findPredicateGenerator (pGeneratorType,  prog, 
				   (PrllAsmtPStmt) stmt,
				   refiner, wp, pState, cState);
    
//       add (new AIntellegence (refiner.getCVCLConverter(),
// 			      refiner.getVC (),
// 			      cvclPreds,
// 			      predMap, wp, 
// 			      parentState, childState, prop));
  }


  static PredicateGenerator findPredicateGenerator (String name,
						    PProgram pProgram,
						    PrllAsmtPStmt stmt,
						    PredicateRefiner refiner,
						    WPComputer wp,
						    CVectorExpr pState,
						    CVectorExpr cState)
  {
    ChainPredicateGenerator pg = new ChainPredicateGenerator ();
    
    String[] result = name.split (":");
    for (int i = 0; i < result.length; i++ )
      {
	String generatorName = result[i];
	if (generatorName.equals ("wpd"))
	  {
	    pg.add (new WPPredicateGenerator.WPDiff 
		    (pProgram,
		     (PrllAsmtPStmt)stmt,
		     refiner, wp, pState, cState));
	  }
	else if (generatorName.equals ("wps"))
	  {
	    pg.add (new WPPredicateGenerator.WPSame 
		    (pProgram, 
		     (PrllAsmtPStmt)stmt,
		     refiner, wp, pState, cState));
	  }
	else if (generatorName.equals ("bend"))
	  {
	    pg.add (new BackEndGenerator 
		    (pProgram, 
		     (PrllAsmtPStmt) stmt,
		     refiner, wp, pState, cState));
	  }
	else 
	  throw new NoSuchElementException ("Unknow Predicate Generator in "
					    + name + " of " + generatorName);
      }
    return pg;
  }

  private CVectorExpr getStatePred (Map predMap, CTLNode[] state)
  {
    CVectorExpr vExpr = new CVectorExpr (0);
    for (int i = 1; i < state.length; i++)
      {
	edu.toronto.cs.tp.cvcl.Expr leftChild = (edu.toronto.cs.tp.cvcl.Expr)
	  predMap.get (state [i].getLeft ().toString ());

	if (state [i].getRight ().toString ().equals ("false"))
	  vExpr.add (((edu.toronto.cs.tp.cvcl.Expr)leftChild).notExpr ());
	else
	  vExpr.add ((edu.toronto.cs.tp.cvcl.Expr)leftChild);
      }
    return vExpr;
  }


  boolean isStateConsistent (CTLNode[] state)
  {
    PredicateTable pTable = prog.getPredTable ();
    CVCLExprConverter cvcl = refiner.getCVCLConverter ();
	  
    // XXX big ugly hack for now
    Map predMap = new HashMap ();
    for (Iterator it = pTable.getVariables ().iterator (); 
	 it.hasNext ();)
      {
	PredicateTable.Predicate pred = 
	  (PredicateTable.Predicate) it.next ();
	predMap.put (pred.getCtlName (), cvcl.toCVCL (pred.getName ()));
      }

    // -- here we need pProgram, refiner, and CVectorExpr of childState
    PredicateGenerator pg = 
      new InconsistentDestinationGenerator (prog,
					    refiner,
					    getStatePred (predMap, 
							  state));
    return pg.find ();
  }
  
  public AlgebraValue run () throws CTLNodeParserException, 
				    PProgram.ParseException, 
				    FileNotFoundException
  {
  
    stats.start ();
  
    AlgebraValue value = null;
    MvSet result = null;
  
    do
      {
	if (getCFile ())
	  {
	    YasmApp.out.println ("Compiling C program: " + bpFile);
	    if (refiner != null)
	      {
		YasmApp.out.println ("Old predicates: " + 
				     refiner.getPredicates ());
		YasmApp.out.println ("New predicates: " + 
				     refiner.getNewPredicates ());
	      }
	    
	    compileCProgram ();
	  }
	else
	  {
	    YasmApp.out.println ("Compiling boolean program: " + bpFile);
	    compileBooleanProgram ();
	  }

	// -- force garbage collection 
	System.gc ();

	// -- prepare property for checking
	// -- change this condition to 'result != null' 
	// -- to reuse previous results of model-checking
	if (isReuse () && result != null)
	  {
	    // -- let result be the set of states in which the property
	    // -- has already been proven to be definitelly true.
	    // -- Comment this line to start from the previous result, 
	    // -- this is much more efficient, however, it does not produce
	    // -- proofs necessary to continue with the refinement steps
	    result = result.geq (result.getFactory ().top ());
	    // -- call previous result set 'BAD' and form a new reachability
	    // -- property using it
	    CTLMvSetNode resultCTL = CTLFactory.createCTLMvSetNode (result);
	    resultCTL.setName ("BAD");
	    setProp (resultCTL.ef ());
	  }

	// -- convert the property to checkable form
	prepareProperty ();
	
	// -- construct the model-checker
	createXChek ();

// 	StopWatch fwdSw = new StopWatch ();
// 	doFwdImage ();
// 	System.out.println ("FWD in: " + fwdSw);

	// -- do the actual model-checking
	YasmApp.out.println ();
	YasmApp.out.println ();
	YasmApp.out.println ("***** Model-Checking ***** ");
	StopWatch sw = new StopWatch ();
	stats.startMC ();
	result = xchek.checkCTL (prop);
	stats.stopMC ();
	YasmApp.out.println ("Done in: " + sw);


	//	if (result instanceof CFAMvSet)
	//	  {
	//	    YasmApp.out.println ();
	//	    YasmApp.out.println ("Result mvSet");
	//	    YasmApp.out.flush ();
	    
	//	    ((CFAMvSet)result).getCFA ().dumpNodes ();
	//	  }
	

	// -- finally, print what we think is the result in the 
	// -- initial state
	
	// -- Print  " \forall {s \in States} INIT(s) -> result(s) "
	// --
	YasmApp.out.println ();
	YasmApp.out.println ("******* SEE RESULT BELOW ***********");
	value = xkripke.getInit ().not ().or (result).
	  forallAbstract (xkripke.getUnPrimeCube ()).getValue ();
	YasmApp.out.println 
	  ("The property is: " + value);
	YasmApp.out.println ("********** LOOK UP  ^ **********");

	YasmApp.out.println ("Predicates used: ");
	YasmApp.out.println (refiner.getPredicates ());


	if (!value.toString ().equals ("maybe"))
	  {
	    System.out.println ("we are done");
	    if (isDoTrace ())
	      showTrace (result, value);
	    break;
	  }
	
	if (doProof)
	  {
	    stats.startNewPred ();
	    showProof (result, value);
	    stats.stopNewPred ();
	  }

	YasmApp.out.println ("Stats so far");
	YasmApp.out.println (stats);

	
	CTLFactory.renew ();

	found |= newPredFound > 0;
	if (!found)
	  {
	    YasmApp.out.println ("FAILED TO FIND A SOLUTION");
// 	    YasmApp.err.println ("Final solution was");
// 	    ((CFAMvSet)result).getCFA ().dumpNodes ();
	  }
      } while (found);

    return value;
  
  } // end of run


  public void parseCmdLine (String[] args)
  throws CTLNodeParserException
{
  OptParser optParser = new OptParser ().startClass (YasmApp.class);

  BooleanOpt runOnceOpt = optParser.opt ().longName ("once").
    description ("does not go into the refinement loop").argumentArity (0).
    asBoolean ().defaultValue (false);
  StringOpt ctlPropOpt = 
    optParser.opt ().longName ("property").shortName ('p').
    description ("property to be checked").required ().
    asString ();

//   BooleanOpt cFile = optParser.opt ().longName ("pfile").
//     shortName ('c').
//     description ("input file is assumed to be a predicate program").
//     asBoolean ().defaultValue (true);

  StringOpt refinerOpt = optParser.opt ().longName ("refiner").
    description ("Refiner to be used. Choices are:\n" + 
		 //"\told -- complete slow search\n" + 
		 "new -- incremental full search\n" + 
		 "csp1 -- first try at CSP based search\n" + 
		 "cbj -- conflict-driven back jumping with prunning\n" + 
		 "cbj-i -- as cbj but don't check inconsistencies\n" + 
		 "cbj-s -- as cbj but don't abstract statements\n" + 
		 "cbj-s-i -- as cbj but only use cheap heuristics\n" + 
		 "cffc -- forward check with back jumping with prunning\n"+ 
		 "cffc-i -- as cffc but don't check inconsistencies\n" + 
		 "cffc-s -- as cffc but don't abstract statements\n" + 
		 "cffc-s-i -- as cffc but only use cheap heuristics\n" + 
		 "cffc-d -- as cffc but no use all predicates\n" +
		 "cffc-s-i-d -- take a guess\n" +
		   "\tDEFAULT IS cbj\n")
    .asString ().defaultValue ("cbj");

  StringOpt pGeneratorOpt = optParser.opt ().longName ("pg").
    description ("The order in which predicate generators are used\n " + 
		   "  separated by colon (':'). The choices are:\n" + 
		   "wpd -- weakest precondition based on difference\n" + 
		   "wps -- weakest precondition based on similarities\n" + 
		   "bend -- backend generator that solves\n" + 
		   "\tincompleteness of the refiner\n" + 
		   "\tDEFAULT IS bend:wpd:wps\n").asString ().
    defaultValue ("bend:wpd:wps");

  BooleanOpt hyperOpt = optParser.opt ().longName ("nohyper").
    description ("disables hyper edges").argumentArity (0).
    asBoolean ().defaultValue (false);

  BooleanOpt reuseOpt = optParser.opt ().longName ("reuse").
    description ("enables reusing previous results during EF computation").
    argumentArity (0).asBoolean ().defaultValue (false);
						  

  BooleanOpt nonDetInitOpt = optParser.opt ().longName ("ndinit").
    description ("if present initial values of predicates are " + 
		 "assumed to be non-deterministic").
    argumentArity (0).asBoolean ().defaultValue (false);
    
  FileNameOpt initPredOpt = optParser.opt ().longName ("init-pred").
    description ("A file containing intial predicates").asFileName ();
  FileNameOpt inputFileOpt = 
    optParser.opt ().nameless ().required ().
    description ("boolean program").asFileName ();

  StringOpt selectorOpt = optParser.opt ().longName ("sel").
    description ("Return selector type. The choices are:\n" +
      "dd -- Decision diagram selector\n" +
      "bin -- Binary search PProgram selector\n" +
      "lin -- Linear search PProgram selector\n" +
      "\tDEFAULT IS dd\n").
    asString ().defaultValue ("dd");  

  BooleanOpt stmtBlockingOpt = optParser.opt ().longName ("blocking").
    description ("if present, consecutive simple assignments are merged " +
                 "into a basic block\n" +
                 "\tDEFAULT IS false\n").
    argumentArity (0).asBoolean ().defaultValue (false);

  IntOpt maxPredsPerRefinementOpt = optParser.opt ().longName ("maxpreds").
    description ("Max. number of predicates to discover per " +
                 "predicate-generation pass\n" +
                 "\tDEFAULT is 1\n").
    asInt ().defaultValue (1);             

  BooleanOpt doTraceOpt = optParser.opt ().longName ("do-trace").
    description ("Starts a trace server to feed eclipse plugin").
    argumentArity (0).asBoolean ().defaultValue (false);
  
  IntOpt tracePortOpt = optParser.opt ().longName ("trace-port").
    description ("Port for tracer to listen to.\n\tDEFAULT is 6700").
    asInt ().defaultValue (6700);

  

  // -- actually parse the command line
  OptResult result = optParser.run (args);

  // -- initialize our class

  setBpFile (new File (inputFileOpt.getString (result)));
  setProp (CTLNodeParser.parse (ctlPropOpt.getString (result)));
  setDoProof (!runOnceOpt.getBoolean (result));
  setHyper (!hyperOpt.getBoolean (result));
  setReuse (reuseOpt.getBoolean (result));
  setUnknownInit (!nonDetInitOpt.getBoolean (result));
  //setCFile (cFile.getBoolean (result));
  setCFile (true);

  setRefinerType (refinerOpt.getString (result));
  setPGeneratorType (pGeneratorOpt.getString (result));

  setSelectorType (selectorOpt.getString (result));
  setStmtBlocking (stmtBlockingOpt.getBoolean (result));

  setMaxPredsPerRefinement (maxPredsPerRefinementOpt.getInt (result));
  setTracePort (tracePortOpt.getInt (result));
  setDoTrace (doTraceOpt.getBoolean (result));
  
  String initPredFileName = initPredOpt.getString (result);
  if (initPredFileName != null)
    setInitPredFile (new File (initPredOpt.getString (result)));
  else
    setInitPredFile (null);
} // end of parseCmdLine

 
  public static void main (String[] args)
{
  try
  {

    // -- create our application
    YasmApp yasmApp = new YasmApp ();

    // -- parse command line option
    yasmApp.parseCmdLine (args);


    System.err.println ("max preds: " + MAX_PREDICATES_PER_REFINEMENT);

    AlgebraValue value = yasmApp.run ();

    YasmApp.out.println ("Done in " + yasmApp.overall);

    YasmApp.out.println (yasmApp.stats);
    YasmApp.out.println ("QUERY: " + yasmApp.refiner.getVC ().sw);

    YasmApp.out.println ("Predicates used: ");
    YasmApp.out.println (yasmApp.refiner.getPredicates ());


    // -- show the result
    //printResult ();

    // -- flush all our streams
  }
  catch (CTLNodeParserException ex)
  {
    YasmApp.out.println ("Could not parse CTL property: " + ex);
    ex.printStackTrace (YasmApp.err);
  }
  catch (PProgram.ParseException ex)
  {
    YasmApp.out.println ("Could not parse C program: " + ex);
    ex.printStackTrace (YasmApp.err);
  }
  catch (FileNotFoundException ex)
  {
    YasmApp.out.println ("Could not find C file: " + ex);
    ex.printStackTrace (YasmApp.err);
  }
  catch (Throwable ex)
  {
    YasmApp.out.println ("Unhandled Exception: " + ex);
    ex.printStackTrace ();
  }
  YasmApp.out.flush ();
  YasmApp.err.flush ();
} // end of main

  
  
} // YasmApp
