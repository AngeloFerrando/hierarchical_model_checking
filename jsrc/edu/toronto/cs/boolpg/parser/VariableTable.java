package edu.toronto.cs.boolpg.parser;

// XXX Yet another copy of the same!
// XXX New version of VariableTable from edu.toronto.cs.smv
// XXX Should be merged with the above once we are done
import java.util.*;

import edu.toronto.cs.mvset.*;
import edu.toronto.cs.ctl.*;
import edu.toronto.cs.modelchecker.*;
import edu.toronto.cs.algebra.*;
import edu.toronto.cs.util.*;
import edu.toronto.cs.cfa.*;



/**
 * Describe class <code>VariableTable</code> here.
 * Symbol table that keeps track of variables. 
 * Since we often need several copies of the same variable, for example, 
 * current and next state, or even next next state, we support arbitrary
 * number of shadow variables.
 *
 * @author <a href="mailto:arie@cs.toronto.edu">Arie Gurfinkel</a>
 * @version 1.0
 */
public class VariableTable 
{

  
  // -- how many variables we have already seen
  int idCounter = 0;

  // -- symbol table to lookup variables by their name
  Map varNames;


  // -- an mv-set factory so that we can create variables as well 
  // -- as keep track of them
  MvSetFactory factory;
  // -- algebra used by the factory
  BelnapAlgebra algebra;
  
  int shadows;

  /**
   * Creates a new <code>VariableTable</code> instance.
   *
   * @param _shadows an <code>int</code> number of shadow variables 
   */
  public VariableTable (int _shadows)
  {
    shadows = _shadows;
    idCounter = 0;
    varNames = new HashMap ();

    // -- there is always a single process counter
    ProcessCounter pc = new ProcessCounter ();
    varNames.put (pc.getName (), pc);
  }
  
  public VariableTable ()
  {
    this (2);
  }
  

  public String toString ()
  {
    StringBuffer sb = new StringBuffer ();
    
    for (Iterator it = varNames.values ().iterator (); it.hasNext ();)
      {
	sb.append (it.next ().toString ());
	sb.append ('\n');
      }
    return sb.toString ();
  }
  

  // -- mv-set factory setter and getter
  public void setMvSetFactory (MvSetFactory _factory)
  {
    factory = _factory;
    setAlgebra ((BelnapAlgebra)factory.getAlgebra ());
  }

  public MvSetFactory getMvSetFactory()
  {
    return factory;
  }
  

  void setAlgebra (BelnapAlgebra v)
  {
    algebra = v;
  }

  
  /**
   * <code>getIdBlock</code> allocates <code>size</code> number of bits
   *
   * @param size an <code>int</code> value
   * @return an <code>int</code> value
   */
  int getIdBlock (int size)
  {
    int result = idCounter;
    idCounter += size;
    return result;
  }
  
  
  
  /**
   * Describe <code>declarePropositional</code> method here.
   *
   * @param name a <code>String</code> value
   * @return a <code>StateVariable</code> value
   */
  public StateVariable declarePropositional (String name)
  {
    
    List vars = new ArrayList (shadows + 1);
    
    for (int i = 0; i < shadows + 1; i++)
      vars.add (new StateVariable (name, i, getIdBlock (1)));
    
    StateVariable var = (StateVariable) vars.get (0);
    
    var.setShadows ((StateVariable[]) 
		    vars.toArray (new StateVariable [vars.size ()]));
    

    for (Iterator it = vars.iterator (); it.hasNext ();)
      {
	StateVariable v = (StateVariable) it.next ();
	varNames.put (v.getName (), v);
      }
    return var;
  }

  /* this should return number of bit variables we have */
  public int getNumVars ()
  {
    return idCounter;
  }

  public Variable getByName (String name)
  {
    return (Variable)varNames.get (name);
  }
  
  public Collection getVariables ()
  {
    return varNames.values ();
  }
  
  /**
   * Describe <code>getVarNames</code> method here.
   *
   * currently called to construct a KripkeStructure but not used there.
   * @return a <code>String[]</code> value
   */
  public String[] getVarNames ()
  {
    return null;
  }
  
  
  // -- a helper debug method, dumps this symbol table
  public void dump () 
  {
     System.out.println("Dumping: " + varNames.keySet().size ());

     for (Iterator it = getVariables ().iterator (); it.hasNext ();)
       System.out.println (it.next ());
  }
  


  /**
   * Describe <code>variableMap</code> method here.  returns an
   * integer array that maps variables in the 'fromSet' to variables
   * in the 'toSet'. For example, variableMap (0, 1) returns a map
   * that maps 0 variables to 1 variables. The intention is that 0
   * variables are current variables, and 1 variables are next state
   * variables, in which case we get a map from current to next state
   *
   * @param fromSet an <code>int</code> value
   * @param toSet an <code>int</code> value
   * @return an <code>int[]</code> value
   */
  public int[] variableMap (int fromSet, int toSet)
  {
    int[] map = new int [getNumVars ()];
    
    for (Iterator it = getVariables ().iterator (); it.hasNext ();)
      {
	Variable var = (Variable) it.next ();
	// -- skip shadow variables, maybe we should not even 
	// -- keep them in our varNames?
	if (var.isShadow ()) continue;
	
	if (var instanceof StateVariable)
	  {
	    StateVariable[] shadows = (StateVariable[])var.getShadows ();
	    for (int i = 0; i < shadows.length; i++)
	      {
		int k = (i == fromSet ? toSet : i);
		map [shadows [i].getId ()] = shadows [k].getId ();
	      }
	    
	  }
	else if (var instanceof ProcessCounter)
	  {
	    // -- do nothing at the moment
	  }
	else
	  throw 
	    new RuntimeException ("Unknown variable " + var + " of class " +
				  (var == null ? "null" : 
				   var.getClass ().toString ()));
      }
    return map;
  }
  




  /**
   * Describe <code>getVariableIds</code> method here.
   *
   * returns ids of all of the variables in set <code>set</code>
   * @param set an <code>int</code> value
   * @return an <code>int[]</code> value
   */
  public int[] getVariableIds (int set)
  {
    int[] vars = new int [getNumVars () / (shadows + 1)];

    int count = 0;
    for (Iterator it = getVariables ().iterator (); it.hasNext ();)
      {
	Variable var = (Variable)it.next ();
	// -- skip shadow variables
	if (var.isShadow ()) continue;
	
	if (var instanceof StateVariable)
	  vars [count++] = ((StateVariable)var.getShadow (set)).getId ();
      }
    return vars;
  }
  

  

  public abstract class Variable 
  {
    // -- the name
    String name;
    Variable[] shadows;
    int shadowIdx;

    String computedName = null;
      
    public static final String NAME_SUFFIX = "'";

    public Variable (String _name, int _idx)
    {
      name = _name;
      shadows = null;
      shadowIdx = _idx;
    }
    
    public String getName ()
    {
      if (computedName == null)
	{
	  StringBuffer sb = new StringBuffer ();
	  sb.append (name);
	  for (int i = 0; i < shadowIdx; i++)
	    sb.append (NAME_SUFFIX);
	  computedName = sb.toString ();
	}
      
      return computedName;
    }

    public Variable getShadow (int i)
    {
      assert !isShadow () : " no shadow of a shadow";
      
      return shadows [i];
    }

    public void setShadows (Variable[] v)
    {
      for (int i = 0; i < v.length; i++)
	assert v [i] != null : "shadow " + i + " is null";
      
      shadows = v;
    }
    
    public Variable[] getShadows ()
    {
      return shadows;
    }
    
    public boolean isShadow ()
    {
      return shadowIdx != 0;
    }    

    public String toString ()
    {
      return getName ();
    }

    public abstract MvSet eq (Variable v);
    
    
    // -- returns an mv-set corresponding to var = next (var) in SMV
    public abstract MvSet eqShadow (int i);
    
    public abstract MvSet eq (MvSet v);
    public abstract MvSet eq (String v);

    // -- returns CTL representation of this variable 
    // -- with respect to the state
    public abstract CTLNode toCTL (AlgebraValue[] state);
    public int size ()
    {
      return 0;
    }
    
  }  

  public class ProcessCounter extends Variable
  {
    public ProcessCounter ()
    {
      super ("pc", -1);
    }
    
    public CTLNode toCTL (AlgebraValue[] state)
    {
      return null;
    }
    
    public MvSet eq (Variable v)
    {
      throw new UnsupportedOperationException ();
    }
    
    public MvSet eq (MvSet v)
    {
      throw new UnsupportedOperationException ();
    }
    public MvSet eq (String v)
    {
      throw new UnsupportedOperationException ();
      
    }


    public MvSet eqShadow (int i)
    {
      throw new UnsupportedOperationException ();
    }
    
    
    
  }
  

  // -- symbol table entry for a state variable
  public class StateVariable  extends Variable
  {
    // -- mv-set representation of the variable
    MvSet mvSet;

    //  Type vtype;
    // -- an ID of the variable
    int id;
  
  
    
    public StateVariable (String name, int _shadowIdx, int _id)
    {
      super (name, _shadowIdx);
      id = _id;
    }
    
    public int size ()
    {
	return 1;
    }
    
    public String toString ()
    {
      return "bool " + getName ();
    }
    
  
    public int getId ()
    {
      return id;
    }
    
    public MvSet getMvSet ()
    {
      if (mvSet == null)
	//mvSet = factory.createProjection (id);
	{
	  mvSet = 
	    factory.var (id, algebra.top (), algebra.top ()).or 
	    (factory.var (id, algebra.bot (), algebra.bot ())).or
	   (factory.var (id, algebra.infoBot (), algebra.infoBot ()));
	}
      


      return mvSet;
    }

    protected void setMvSet (MvSet v)
    {
      mvSet = v;
    }
    

    public MvSet eqShadow (int idx)
    {
      assert !isShadow () : "Cannot apply eqShadow on shadow variables";
      
      return getMvSet ().eq (((StateVariable)getShadow (idx)).getMvSet ());
    }
    

    public MvSet eq (Variable v)
    {
      assert v instanceof StateVariable;
      
      if (v instanceof StateVariable)
	return eq ((StateVariable)v);
      throw new UnsupportedOperationException ();
    }
    

    public MvSet eq (MvSet v)
    {
      return getMvSet ().eq (v);
    }
    
    public MvSet eq (StateVariable v)
    {
      return getMvSet ().eq (v.getMvSet ());
    }

    public MvSet eq (String v) 
    { 
      // -- we can handle this if String is an algebra value
      AlgebraValue value = algebra.getValue (v);
      assert value != null : "Comparing " + getName () + " with " + v;
      
      return getMvSet ().eq (factory.createConstant (value));
    }

  
    public CTLNode toCTL (AlgebraValue[] state)
    {
      // -- check that id of this variable is valid w.r.t. the state
      if (getId () < 0 || getId () >= state.length) return null;

      // -- if state does not assign a value to this variable
      // -- it is ignored
      if (state [getId ()] == algebra.noValue ()) return null;
      
      // -- otherwise we get 'varname = algebra_value'
       CTLNode ctl = CTLFactory.createCTLAtomPropNode (getName ());
       return ctl.eq (CTLFactory.createCTLConstantNode (state [getId ()]));
    }
    
  }
  

}
