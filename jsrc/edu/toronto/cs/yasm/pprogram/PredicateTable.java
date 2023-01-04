package edu.toronto.cs.yasm.pprogram;

/**
 * PredicateTable.java
 *
 *
 * Created: Tue Jun 29 12:49:55 2004
 *
 * @author <a href="mailto:kelvin@epoch.cs">Kelvin Ku</a>
 * @version 1.0
 */

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
import edu.toronto.cs.expr.*;


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
public class PredicateTable 
{


  // -- how many variables we have already seen
  int idCounter;

  // -- a map to lookup predicates by their name 
  // -- the name of a predicate is an Expr
  Map predNames;

  // XXX just for now
  // -- maps CTL names to predicates, eventually we should use
  // -- pred names and teach CTL parser about Expr
  Map ctlNames;

  // -- an mv-set factory so that we can create variables as well 
  // -- as keep track of them
  MvSetFactory factory;
  // -- algebra used by the factory
  BelnapAlgebra algebra;

  int shadows;

  public PredicateTable ()
  {
    this (1);
  }
  /**
   * Creates a new <code>PredicateTable</code> instance.
   *
   * @param _shadows an <code>int</code> number of shadow variables 
   */
  private PredicateTable (int _shadows)
  {
    shadows = _shadows;
    idCounter = 1;
    predNames = new HashMap ();
    ctlNames = new HashMap ();
  }

  public String toString ()
  {
    StringBuffer sb = new StringBuffer ();

    for (Iterator it = predNames.values ().iterator (); it.hasNext ();)
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

    // -- once an mvSetFactory is set we should forget previously 
    // -- computed results
    for (Iterator it = getVariables ().iterator (); it.hasNext ();)
      ((Predicate)it.next ()).resetMvSet ();
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
   * Adds a new predicate with a name 'name'. Use this with MDDs such
   * as MDDMvSetFactory
   *
   * @param name an <code>Expr</code> value
   * @return a <code>Predicate</code> value
   */
  public Predicate __newPredicate (Expr name)
  {
    // -- create an array to hold the predicate and its shadows
    List vars = new ArrayList (shadows + 1);

    // -- allocate predicates
    for (int i = 0; i < shadows + 1; i++)
      vars.add (new Predicate (name, i, getIdBlock (1)));

    // -- the first predicate is the actual predicated added
    // -- so we need to set what it shadows are
    Predicate var = (Predicate) vars.get (0);
    var.setShadows ((Predicate[]) 
        vars.toArray (new Predicate [vars.size ()]));
    // XXX even bigger hack
    //var.setCtlName (newCtlName ());
    var.setCtlName ("b" + var.getId ());

    // -- finally add the predicates to our map
    predNames.put (var.getName (), var);
    // XXX add something to ctlNames
    ctlNames.put (var.getCtlName (), var);
    return var;
  }

  /**
   * <code>newPredicate</code> creates a new predicate corresponding
   * to name. Use this with ADD based MvSets such as JCUDDBelnapMvSet
   * and JCUDDMvSet
   *
   * @param name an <code>Expr</code> value
   * @return a <code>Predicate</code> value
   */
  public Predicate newPredicate (Expr name)
  {
    // -- create an array to hold the predicate and its shadows
    List vars = new ArrayList (shadows + 1);

    // -- allocate predicates
    for (int i = 0; i < shadows + 1; i++)
      vars.add (new AddPredicate (name, i, getIdBlock (2)));

    // -- the first predicate is the actual predicated added
    // -- so we need to set what it shadows are
    Predicate var = (Predicate) vars.get (0);
    var.setShadows ((Predicate[]) 
        vars.toArray (new Predicate [vars.size ()]));
    // XXX even bigger hack
    var.setCtlName (newCtlName ());

    // -- finally add the predicates to our map
    predNames.put (var.getName (), var);
    // XXX add something to ctlNames
    ctlNames.put (var.getCtlName (), var);
    return var;
  }

  public IntVariable newIntVar (Expr name, int range)
  {
    IntVariable var = new IntVariable (name, range);

    // -- allocate predicates
    var.setId (getIdBlock (var.bitWidth ()));

    // XXX even bigger hack
    var.setCtlName (name.toString ());

    // -- finally add the predicates to our map
    predNames.put (var.getName (), var);
    // XXX add something to ctlNames
    ctlNames.put (var.getCtlName (), var);
    return var;
  }


  // XXX big hack for now
  private String newCtlName ()
  {
    return "b" + ctlNames.size ();
  }

  /* this should return number of bit variables we have */
  public int getNumVars ()
  {
    return idCounter;
  }

  public Predicate getByName (Expr name)
  {
    return (Predicate)predNames.get (name);
  }

  public Predicate getByCtlName (String name)
  {
    return (Predicate)ctlNames.get (name);
  }

  public Collection getVariables ()
  {
    return predNames.values ();
  }


  // -- a helper debug method, dumps this symbol table
  public void dump () 
  {
    System.out.println("Dumping: " + predNames.keySet().size ());

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

    // -- initialize map to identity
    for (int i = 0; i < map.length; i++)
      map [i] = i;

    for (Iterator it = getVariables ().iterator (); it.hasNext ();)
    {
      Predicate var = (Predicate) it.next ();
      // -- skip shadow variables, maybe we should not even 
      // -- keep them in our varNames?
      if (var.isShadow ()) continue;

      if (var instanceof IntVariable) continue;

      if (var instanceof Predicate)
      {
        Predicate[] shadows = (Predicate[])var.getShadows ();
        for (int i = 0; i < shadows.length; i++)
        {
          int k = (i == fromSet ? toSet : i);
          updateMap (map, shadows [i], shadows [k]);
          //map [shadows [i].getId ()] = shadows [k].getId ();
        }

      }
      else
        // XXX Should never happen
        throw 
          new RuntimeException ("Unknown variable " + var + " of class " +
              (var == null ? "null" : 
               var.getClass ().toString ()));
    }
    return map;
  }



  private void updateMap (int[] map, Predicate p, Predicate pShadow)
  {
    for (int i = 0; i < p.size (); i++)
      map [p.getId () + i] = pShadow.getId () + i;
  }

  public int primaryBitSize ()
  {
    int bits = 0;

    for (Iterator it = getVariables ().iterator (); it.hasNext ();)
    {
      Predicate var = (Predicate) it.next ();

      if (var.isShadow ()) continue;

      if (var instanceof IntVariable) continue;

      if (var instanceof Predicate)
      {
        bits += var.size ();
      }
    }

    return bits;
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
    // int[] vars = new int [(getNumVars () - 1) / (shadows + 1)];
    int[] vars = new int [primaryBitSize ()];

    int count = 0;
    for (Iterator it = getVariables ().iterator (); it.hasNext ();)
    {
      Predicate var = (Predicate)it.next ();
      // -- skip shadow variables
      if (var.isShadow ()) continue;

      if (var instanceof IntVariable) continue;

      if (var instanceof Predicate)
      {
        Predicate pred = ((Predicate)var.getShadow (set));
        for (int i = 0; i < pred.size (); i++)
          vars [count++] = pred.getId () + i;

        //vars [count++] = ((Predicate)var.getShadow (set)).getId ();
      }

    }
    return vars;
  }




  public class Predicate
  {
    // -- the name of the predicate as an expression
    Expr name;
    // -- ctl name of the predicate
    String ctlName;

    // -- the list of shadow predicates
    Predicate[] shadows;

    // -- the shadowId of the predicate
    int shadowIdx;

    // -- decision diagram id 
    int id;

    // -- mvSet representation of this predicate
    MvSet mvSet;

    public Predicate (Expr _name, int _shadowIdx, int _id)
    {
      name = _name;
      shadows = null;
      mvSet = null;
      shadowIdx = _shadowIdx;
      id = _id;
      ctlName = "";
    }

    public Expr getName ()
    {
      return name;
    }
    public String getCtlName ()
    {
      return ctlName;
    }
    public void setCtlName (String v)
    {
      ctlName = v;
    }

    public Predicate getShadow (int i)
    {
      assert !isShadow () : " no shadow of a shadow";

      return shadows [i];
    }

    public void setShadows (Predicate[] v)
    {
      for (int i = 0; i < v.length; i++)
        assert v [i] != null : "shadow " + i + " is null";

      shadows = v;
    }

    public Predicate[] getShadows ()
    {
      return shadows;
    }

    public boolean isShadow ()
    {
      return shadowIdx != 0;
    }    

    public String toString ()
    {
      return getCtlName () + ": " + getName () + ": " + getId ();
    }

    // -- returns CTL representation of this variable 
    // -- with respect to the state
    //public abstract CTLNode toCTL (AlgebraValue[] state);

    public int getId ()
    { 
      return id;
    }
    public int size ()
    {
      return 1;
    }

    public MvSet getMvSet ()
    {
      if (mvSet == null)
      {
        mvSet = 
          factory.var (id, algebra.top (), algebra.top ()).or 
          (factory.var (id, algebra.bot (), algebra.bot ())).or
          (factory.var (id, algebra.infoBot (), algebra.infoBot ()));
      }
      return mvSet;
    }

    public void resetMvSet ()
    {
      mvSet = null;
    }


    public CTLNode toCTL (AlgebraValue[] state)
    {
      // -- check that id of this variable is valid w.r.t. the state
      if (getId () < 0 || getId () >= state.length) return null;

      // -- if state does not assign a value to this variable
      // -- it is ignored
      if (state [getId ()] == algebra.noValue ()) return null;

      // -- otherwise we get 'varname = algebra_value'
      CTLNode ctl = CTLFactory.createCTLAtomPropNode (getCtlName ());
      return ctl.eq (CTLFactory.createCTLConstantNode (state [getId ()]));
    } 

    public Expr toExpr (AlgebraValue[] state)
    {
      // -- check that id of this variable is valid w.r.t. the state
      if (getId () < 0 || getId () >= state.length) return null;

      // -- if state does not assign a value to this variable
      // -- it is ignored
      if (state [getId ()] == algebra.noValue ()) return null;

      ExprFactory exprFactory = getName ().getFactory ();
      return exprFactory.op (ComparisonOp.EQ).
        binApply (getName (), 
            exprFactory.op (new JavaObjectOp (state [getId ()])));
    }
  }

  public class AddPredicate extends Predicate
  {

    // -- mvSet representation of this predicate
    MvSet mvSet;
    int firstBit;

    public AddPredicate (Expr _name, int _shadowIdx, int _id)
    {
      super (_name, _shadowIdx, _id);
      firstBit = _id;
    }

    public int size ()
    {
      return 2;
    }

    public MvSet __getMvSet ()
    {
      if (mvSet == null)
      {
        // -- encode a variable with value {true, maybe, false}
        // -- using 2 bits and the encoding
        // -- 00 - false
        // -- 10 - true
        // -- -1 - maybe

        MvSet topMv = factory.createConstant (algebra.top ());
        MvSet botMv = factory.createConstant (algebra.bot ());
        MvSet infoBotMv = factory.createConstant (algebra.infoBot ());


        MvSet lhs = factory.createCase (firstBit + 1, 
            new MvSet[] {botMv, infoBotMv});
        MvSet rhs = factory.createCase (firstBit + 1, 
            new MvSet[] {topMv, infoBotMv });

        mvSet = factory.createCase (firstBit, new MvSet[] {lhs, rhs});


      }
      return mvSet;
    }

    public MvSet getMvSet ()
    {
      if (mvSet == null)
      {
        MvSet bit1;

        bit1 = factory.createProjection (firstBit + 1);
        mvSet = 
          factory.createCase 
          (firstBit, 
           new MvSet[] {factory.createConstant (algebra.infoBot ()), 
           bit1 });

      }
      return mvSet;
    }


    public CTLNode __toCTL (AlgebraValue[] state)
    {
      // -- check that id of this variable is valid w.r.t. the state
      if (getId () < 0 || getId () >= state.length + 1) return null;

      AlgebraValue val0 = state [firstBit];
      AlgebraValue val1 = state [firstBit + 1];
      if (val0 == algebra.noValue () ||
          val1 == algebra.noValue ())
        return null;

      AlgebraValue val = algebra.infoBot ();
      if (val1 == algebra.top ())
        val = algebra.infoBot ();
      else if (val0 == algebra.top ())
        val = algebra.top ();
      else if (val0 == algebra.bot ())
        val = algebra.bot ();


      // -- otherwise we get 'varname = algebra_value'
      CTLNode ctl = CTLFactory.createCTLAtomPropNode (getCtlName ());
      return ctl.eq (CTLFactory.createCTLConstantNode (val));
    } 


    public CTLNode toCTL (AlgebraValue[] state)
    {
      // -- check that id of this variable is valid w.r.t. the state
      if (getId () < 0 || getId () >= state.length + 1) return null;

      AlgebraValue val0 = state [firstBit];
      AlgebraValue val1 = state [firstBit + 1];
      if (val0 == algebra.noValue () ||
          val1 == algebra.noValue ())
        return null;

      AlgebraValue val = (val0 == algebra.top ()) ? algebra.infoBot () : val1;


      // -- otherwise we get 'varname = algebra_value'
      CTLNode ctl = CTLFactory.createCTLAtomPropNode (getCtlName ());
      return ctl.eq (CTLFactory.createCTLConstantNode (val));
    } 

    public Expr toExpr (AlgebraValue[] state)
    {
      // -- check that id of this variable is valid w.r.t. the state
      if (getId () < 0 || getId () >= state.length + 1) return null;

      AlgebraValue val0 = state [firstBit];
      AlgebraValue val1 = state [firstBit + 1];
      if (val0 == algebra.noValue () ||
          val1 == algebra.noValue ())
        return null;

      AlgebraValue val = (val0 == algebra.top ()) ? algebra.infoBot () : val1;



      ExprFactory exprFactory = getName ().getFactory ();
      return exprFactory.op (ComparisonOp.EQ).
        binApply (getName (), 
            exprFactory.op (new JavaObjectOp (val)));
    }
  }

  public class IntVariable extends Predicate
  {
    // -- mvSet representation of this predicate
    MvSet mvSet;
    int range;

    EnumType enumType;

    int bitWidth;

    public IntVariable (Expr _name, int _range)
    {
      super (_name, 0, -1);
      range = _range;

      // -- create array of Integers to initialize EnumType
      Integer[] intArray = new Integer [range];
      for (int i = 0; i < range; i++)
      {
        intArray [i] = new Integer (i);
      }

      enumType = new EnumType (intArray);

      bitWidth = enumType.bitSize ();
    }

    public EnumType getEnumType ()
    {
      return enumType;
    }

    public int bitWidth ()
    {
      return bitWidth;
    }

    public void setId (int _id)
    {
      id = _id;
    }

    public int size ()
    {
      return bitWidth ();
    }

    public String toString ()
    {
      return getCtlName () + ": " + name + " [0," + range + "): " + getId ();
    }

    public MvSet eq (int i)
    {
      return eq (new Integer (i));
    }

    public MvSet getBitCube ()
    {

      int[] vars = new int [bitWidth];
      for (int i = 0; i < bitWidth; i++)
	vars [i] = id + i;
      
      return factory.buildCube (vars);
    }

    public MvSet eq (Integer i)
    {
      MvSet result = factory.top ();

      AlgebraValue top = factory.getAlgebra ().top ();
      AlgebraValue bot = factory.getAlgebra ().bot ();

      int[] en = enumType.bitValue (i);

      for (int j = 0; j < bitWidth; j++)
      {
        // -- skip unset bits
        if (en [j] == -1) continue;

        result = factory.var (id + j, // want to use getBitId here, ideally
            en [j] == 1 ? top : bot,
            factory.getAlgebra ().top ()).and (result);
      }

      return result;
    }

    // -- XXX there is no getMvSet for this, is there?
    public MvSet getMvSet ()
    {
      if (mvSet == null)
      {
        mvSet = null; /* fill this in */
      }
      return mvSet;
    }

    public CTLNode toCTL (AlgebraValue[] state)
    {
      // -- start with all bits being unset (don't care)
      /*
      int[] en = new int [bitWidth];
      Arrays.fill (en, -1);

      for (int i = 0; i < bitWidth; i++)
      {
        if (state [id + i] == algebra.top ())
          en [i] = 1;
        else if (state [id + i] == algebra.bot ())
          en [i] = 0;  
      }

      Object[] enumValues = enumType.enumValues (en);

      for (int i = 0; i < enumValues.length; i++)
        System.out.println ("enumValues [" + i + "]: " + enumValues [i]);  
      */

      // -- assume we're not going to use the result
      return null;
    } 

    public Expr toExpr (AlgebraValue[] state)
    {
      // -- assume we're not going to use the result
      return null;
    }
  }

  public static void main (String[] args)
  {
    int shadows = 1;
    int maxDDVars = 10;
    int range = 7;

    PredicateTable pTable = new PredicateTable (shadows);
    BelnapAlgebra algebra = new BelnapAlgebra ();
    ExprFactory fac = new ExprFactoryImpl ();

    MDDMvSetFactory mvSetFactory =
      (MDDMvSetFactory) MDDMvSetFactory.newMvSetFactory (algebra, maxDDVars);

    pTable.setMvSetFactory (mvSetFactory);
    pTable.setAlgebra (algebra);

    // x == 1
    Expr e1 = fac.op (ComparisonOp.EQ).binApply
                (fac.var ("x"), fac.intExpr (1));
    Predicate p1 = pTable.__newPredicate (e1);

    // myInt:[0,range)
    IntVariable var = pTable.newIntVar (fac.var ("myInt"), range);

    for (int i = 0; i < range; i++)
    {
      System.out.print (var.getName () + " == " + i + " EnumType encoding: ");
      System.out.println (ArrayUtil.toString
                          (var.getEnumType ().bitValue (new Integer (i))));
    }

    for (int i = 0; i < range; i++)
    {
      for (Iterator it = var.eq (new Integer (i)).cubeIterator ();
           it.hasNext ();)
      {
        System.out.print (var.getName () + " == " + i + " MvSet encoding: ");
        System.out.println (ArrayUtil.toString ((Object []) it.next ()));
      }
    }

    /*
    AlgebraValue top = algebra.top ();
    AlgebraValue bot = algebra.bot ();
    AlgebraValue infoBot = algebra.infoBot ();

    AlgebraValue[] st1 = new AlgebraValue[]
      {infoBot, infoBot, infoBot, infoBot, infoBot, top, top, top};
    AlgebraValue[] st2 = new AlgebraValue[]
      {infoBot, infoBot, infoBot, infoBot, infoBot, top, infoBot, top};

    System.out.println ("State: " + Arrays.asList (st1));
    var.toCTL (st1);

    System.out.println ("State: " + Arrays.asList (st2));
    var.toCTL (st2);
    */

    // x == y
    Expr e2 = fac.op (ComparisonOp.EQ).binApply
                (fac.var ("x"), fac.var ("y"));
    Predicate p2 = pTable.__newPredicate (e2);

    // z < 5
    Expr e3 = fac.op (ComparisonOp.LEQ).binApply
                (fac.var ("z"), fac.intExpr (5));
    Predicate p3 = pTable.__newPredicate (e3);


    System.out.println (pTable);

    System.out.println ("Primary bit size: " + pTable.primaryBitSize ());

    System.out.println ("0 to 1 Map: ");
    System.out.println (ArrayUtil.toString (pTable.variableMap (0, 1)));
    System.out.println ("1 to 0 Map: ");
    System.out.println (ArrayUtil.toString (pTable.variableMap (1, 0)));
    System.out.println ("Unprimed Variable IDs: ");
    System.out.println (ArrayUtil.toString (pTable.getVariableIds (0)));
    System.out.println ("Primed Variable IDs: ");
    System.out.println (ArrayUtil.toString (pTable.getVariableIds (1)));

  }

}
