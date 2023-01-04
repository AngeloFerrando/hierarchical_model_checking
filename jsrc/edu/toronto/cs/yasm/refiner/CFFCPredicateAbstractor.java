package edu.toronto.cs.yasm.refiner;

import java.util.BitSet;
import java.util.Iterator;
import java.util.List;
import java.util.LinkedList;

import edu.toronto.cs.tp.cvcl.Expr;

public class CFFCPredicateAbstractor
{

  // -- number of variables currently assigned
  int assignedVariables;
  
  boolean[] unassigned;

  CSPVariable[] currentAssignment;
  BitSet[][] noGoodSet;
  List[] prunedVals;

  Expr target;

  CSPVariable[] variables;

  // -- variables that have two elements in their domain
  BitSet twoVariables;
  // -- variables that have one element in their domain
  BitSet oneVariables;
  // -- variables that have zero elements in their domain
  BitSet zeroVariables;

  public CFFCPredicateAbstractor (List _srcPredicates, 
				 Expr _target)
  {
    target = _target;
    variables = new CSPVariable [_srcPredicates.size ()];

    twoVariables = new BitSet ();
    oneVariables = new BitSet ();
    zeroVariables = new BitSet ();

    int count = 0;
    for (Iterator it = _srcPredicates.iterator (); it.hasNext (); count++)
      variables [count] = new CSPVariable (count, (Expr) it.next ());

    currentAssignment = new CSPVariable [variables.length + 1];
    assignedVariables = 0;

    noGoodSet = new BitSet [variables.length] [2];

    prunedVals = new List [variables.length + 1];
    for (int i = 0; i < prunedVals.length; i++)
      prunedVals [i] = new LinkedList ();
    
    unassigned = new boolean[variables.length];
    for ( int i = 0; i < unassigned.length; i++)
      unassigned [i] = true;
  }

  /**
   * Describe <code>recSearch</code> method here.
   *
   * @param level an <code>int</code> value
   * @return an <code>int</code> value
   */
  public int recSearch (int level)
  {
    
    if (assignedVariables == variables.length)
      {
	System.err.println ("FOUND: " + currentAssignmentToString ());
	BitSet noGood = new BitSet ();
	noGood.set (0, level - 1);
// 	System.err.println ("Setting noGood for " + 
// 			    currentAssignment [level - 1] + 
// 			    " to " + noGood);
	noGoodSet 
	  [currentAssignment [level - 1].getId ()]
	  [currentAssignment [level - 1].getCurVal ()] = noGood;

	prune (currentAssignment [level - 1], level - 2);

	return level - 1;
      }
    CSPVariable curVar = pickNextVariable ();
    // -- backtrack level
    int btLevel;

    BitSet noGood = null;

    for (int i = 0; i < curVar.domainSize (); i++)
      {
	// System.err.println ("falsePrunedLevel" + curVar.falsePrunedLevel);
// 	System.err.println ("truePrunedLevel" + curVar.truePrunnedLevel);
// 	System.err.println ("hasTrue" + curVar.hasTrue);
// 	System.err.println ("hasFalse" + curVar.hasFalse);
	//System.err.println (curVar);
	if (!curVar.hasCurrentValue (i)) continue;
	//System.err.println ("2" + curVar);
	int val = i;
	assign (curVar, val, level);
	
	forwardCheckCF (curVar, level);

	btLevel = recSearch (level + 1);

	//System.err.println ("btLevel " + btLevel);

	undo (curVar);
	if (btLevel < level)
	  return btLevel;
      }
    

    btLevel = curVar.maxPrunnedLevel ();
    if (btLevel == 0)
      return btLevel;

    //     System.err.println ("Set btLevel to: " + btLevel);
//     System.err.println ("Current variable is: " + curVar);
    
    // -- reusing our noGood variable
    noGood = new BitSet ();
    for (int i = 0; i < curVar.domainSize (); i++)
      {
// 	System.err.println ("Adding: " + noGoodSet [curVar.getId ()] [i]);
	noGood.or (noGoodSet [curVar.getId ()] [i]);
// 	System.err.println ("got: " + noGood);
      }

    //System.err.println ("unioned no-good is: " + noGood);
    noGood.clear (btLevel);

    noGoodSet 
      [currentAssignment [btLevel].getId ()] 
      [currentAssignment [btLevel].getCurVal ()] = noGood;
			
    
    prune (currentAssignment [btLevel], noGood.length () - 1);
    
    return btLevel;
  }

  public void forwardCheckCF (CSPVariable curVar, int level)
  {
  }

  void assign (CSPVariable var, int val, int level)
  {
    System.err.println ("Trying an assignment " + var + " = " + val + 
			 " at level " + level);
    var.assign (val, level);
    currentAssignment [++assignedVariables] = var;
    unassigned [var.getId ()] = false;
  }
  void undo (CSPVariable var)
  {

    //System.err.println ("undo level: " + var.getCurLevel ());
    // -- restore pruned values
    for (Iterator it = prunedVals [var.getCurLevel ()].iterator (); 
	 it.hasNext ();)
      {
	CSPVariable prunedVar = (CSPVariable) it.next ();
	int prunedVal = prunedVar.unPruneLevel (var.getCurLevel ());
	//System.err.println ("pruned Val: " + prunedVal);
	//System.err.println ("undo var: " + prunedVar.getExpr ());
	noGoodSet [prunedVar.getId ()] [prunedVal] = null;
	it.remove ();
      }
    //prunedVals [var.getCurLevel ()] = new LinkedList ();
    

    // -- undo the assignment
    var.undoAssign ();

    assignedVariables--;
  }
  public void prune (CSPVariable curVar, int pruneLevel)
  {
    // -- remove current assignment from current domain
    curVar.prune (pruneLevel);
    System.err.println ("adding pruned var: " + curVar.getExpr () 
			+ "at level " + pruneLevel);
    prunedVals [pruneLevel].add (curVar);
  }
  public void prune (CSPVariable curVar, int pruneLevel, int i)
  {
    if (curVar.hasCurrentValue(i))
      {
	curVar.prune (pruneLevel, i);
	System.err.println ("adding pruned var2: " + curVar.getExpr ()
			    + "at level" + pruneLevel);
	prunedVals [pruneLevel].add (curVar);
      }
  }

  private String currentAssignmentToString ()
  {
    StringBuffer sb = new StringBuffer ();
    for (int i = 1; i <= assignedVariables; i++)
      {
	if (currentAssignment [i].getCurVal () == 0)
	  sb.append ("!");
	sb.append (currentAssignment [i].toString ());
	sb.append (" ");
      }
    return sb.toString ();
  }

  public BitSet checkConsistency (CSPVariable var)
  {
    BitSet set = new BitSet ();
    set.set (0);
    return set;
  }
    
  public CSPVariable pickNextVariable ()
  {
    //return variables [assignedVariables];
    BitSet set = zeroVariables;
    
    if (set.isEmpty ())
      {
	set = oneVariables;
	//System.err.println ("picking variables because only one value");
      }
    if (set.isEmpty ())
      {
	set = twoVariables;
	//System.err.println ("picking variables because only two value");
      }
    //System.err.println ("picking varables because only no value");

    assert !set.isEmpty () : "Somehow got to an empty set";
    return variables [set.nextSetBit (0)];
  }

  /**
   *  a boolean CSPVariable
   *
   */
  public class CSPVariable
  {
    Expr expr;
    int id;

    int curVal;
    int curLevel;

    // -- true if 1 is in the current domain
    boolean hasTrue;
    // -- true if 0 is in the current domain
    boolean hasFalse;

    int truePrunnedLevel;
    int falsePrunedLevel;


    
    public CSPVariable (int _id, Expr _expr)
    {
      curVal = -1;
      curLevel = -1;

      id = _id;
      expr = _expr;
      hasTrue = true;
      hasFalse = true;

      twoVariables.set (id);
    }


    
    public int getId ()
    {
      return id;
    }

    public Expr getExpr ()
    {
      return expr;
    }

    public String toString ()
    {
      return expr.toString ();
    }
    /**
     * returns current value of this variable
     *
     * @return an <code>int</code> value
     */
    public int getCurVal ()
    {
      return curVal;
    }

    /**
     * returns current level at which this variable is assigned
     *
     * @return an <code>int</code> value
     */
    public int getCurLevel ()
    {
      return curLevel;
    }
    

    public void assign (int value, int level)
    {
      curVal = value;
      curLevel = level;

      twoVariables.clear (id);
      oneVariables.clear (id);
    }

    public void undoAssign ()
    {
      curVal = -1;
      curLevel = -1;

      unassigned [id] = true;

      if (hasFalse && hasTrue)
	twoVariables.set (id);
      else if (hasFalse || hasTrue)
	oneVariables.set (id);
      else
	zeroVariables.set (id);
    }    

    public void prune (int pruneLevel, int i)
    {
      if (hasTrue && hasFalse)
	{
	  twoVariables.clear (id);
	  oneVariables.set (id);
	}
      else if ((hasTrue && i==1)|(hasFalse && i ==0))
	{
	  oneVariables.clear (id);
	  zeroVariables.set (id);
	}
      
      if (i == 0 && hasFalse) 
	{
	  hasFalse = false;
	  falsePrunedLevel = pruneLevel;
	}
      else if (i == 1 && hasTrue)
	{
	  hasTrue = false;
	  truePrunnedLevel = pruneLevel;
	}
    }

    public void prune (int pruneLevel)
    {
      if (hasTrue && hasFalse)
	{
	  twoVariables.clear (id);
	  oneVariables.set (id);
	}
      else
	{
	  oneVariables.clear (id);
	  zeroVariables.set (id);
	}
      
      if (curVal == 0) 
	{
	  hasFalse = false;
	  falsePrunedLevel = pruneLevel;
	}
      else if (curVal == 1)
	{
	  hasTrue = false;
	  truePrunnedLevel = pruneLevel;
	}
    }

    public int unPruneLevel (int pruneLevel)
    {
      if (hasTrue || hasFalse)
	{
	  oneVariables.clear (id);
	  twoVariables.set (id);
	}
      else
	{
	  zeroVariables.clear (id);
	  oneVariables.set (id);
	}
      
      if (pruneLevel == falsePrunedLevel) 
	{
	  hasFalse = true;
	  falsePrunedLevel = 0;
	  return 0;
	}
      else if (pruneLevel == truePrunnedLevel)
	{
	  hasTrue = true;
	  truePrunnedLevel = 0;
	  return 1;
	}
      assert false;
      return -1;
    }
    
    public int maxPrunnedLevel ()
    {
      return truePrunnedLevel > falsePrunedLevel ? 
	truePrunnedLevel : falsePrunedLevel;
    }

    public int domainSize ()
    {
      return 2;
    }

    /**
     * return true if 'i' is in the current domain
     *
     * @param i an <code>int</code> value
     * @return a <code>boolean</code> value
     */
    public boolean hasCurrentValue (int i)
    {
      return (i == 0 && hasFalse) || (i == 1 && hasTrue);
    }


  }


//   public static void main (String[] args)
//   {
//     List predicates = new LinkedList ();
//     ExprFactory fac = new ExprFactoryImpl ();
//     predicates.add (fac.var ("a"));
//     predicates.add (fac.var ("b"));
//     predicates.add (fac.var ("c"));

//     CBJPredicateAbstractor cbj = new CBJPredicateAbstractor (predicates, 
// 							     fac.var ("d"));
    
//     cbj.recSearch (1);
//   }
}
