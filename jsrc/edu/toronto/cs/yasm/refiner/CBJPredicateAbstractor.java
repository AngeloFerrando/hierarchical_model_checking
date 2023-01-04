package edu.toronto.cs.yasm.refiner;

import java.util.BitSet;
import java.util.Iterator;
import java.util.List;
import java.util.LinkedList;

import edu.toronto.cs.tp.cvcl.Expr;

public class CBJPredicateAbstractor
{

  // -- number of variables currently assigned
  int assignedVariables;


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

  public CBJPredicateAbstractor (List _srcPredicates, 
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
  }

  /**
   * Describe <code>recSearch</code> method here.
   *
   * @param level an <code>int</code> value
   * @return an <code>int</code> value
   */
  public int recSearch (int level)
  {
    //    System.err.println ("recSerach at level: " + level);
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

    btLevel = level;

    BitSet noGood = null;
    boolean skipConsistencyCheck = false;
    for (int i = 0; i < curVar.domainSize (); i++)
      {
	if (!curVar.hasCurrentValue (i)) continue;
	
	int val = i;
	assign (curVar, val, level);
	
	if (!skipConsistencyCheck)
	  noGood = checkConsistency (curVar);
	if (noGood.length () <= 1)
	  btLevel = recSearch (level + 1);
	else
	  {
	    if (noGood.get (level))
	      noGood.clear (level);
	    else
	      skipConsistencyCheck = true;
	    noGoodSet [curVar.getId ()][curVar.getCurVal ()] = noGood;
	    prune (curVar, noGood.length () - 1);
	  }
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

//     System.err.println ("unioned no-good is: " + noGood);

    noGood.clear (btLevel);

    noGoodSet 
      [currentAssignment [btLevel].getId ()] 
      [currentAssignment [btLevel].getCurVal ()] = noGood;
			
    
    prune (currentAssignment [btLevel], noGood.length () - 1);
    
    return btLevel;
  }


  void assign (CSPVariable var, int val, int level)
  {
    System.err.println ("Trying an assignment " + var + " = " + val + 
			 " at level " + level);
    var.assign (val, level);
    currentAssignment [++assignedVariables] = var;
  }
  void undo (CSPVariable var)
  {

    System.err.println ("Undoing an assignment " + var + 
			" = " + var.getCurVal () + 
			" at level " + var.getCurLevel ());
    
    // -- restore pruned values
    for (Iterator it = prunedVals [var.getCurLevel ()].iterator (); 
	 it.hasNext ();)
      {
	CSPVariable prunedVar = (CSPVariable) it.next ();
	int prunedVal = prunedVar.unPruneLevel (var.getCurLevel ());
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
    prunedVals [pruneLevel].add (curVar);
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
      set = oneVariables;
    if (set.isEmpty ())
      set = twoVariables;

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


    int curScope;
    
    
    public CSPVariable (int _id, Expr _expr)
    {
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

      if (hasFalse && hasTrue)
	twoVariables.set (id);
      else if (hasFalse || hasTrue)
	oneVariables.set (id);
      else
	zeroVariables.set (id);
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

    public int getCurScope ()
    {
      return curScope;
    }
    public void setCurScope (int v)
    {
      curScope = v;
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
