package edu.toronto.cs.mdd;

import java.util.*;

/*** Given an MDDNode returns an array 
 *** representing the same function
 ***/
public class MDDValueCollector
{
  // -- total number of variables
  int totalVars;
  
  
  private MDDValueCollector (int _totalVars)
  {
    totalVars = _totalVars;
  }

  public static int[][] collectValues (MDDNode node, int totalVars)
  {
    return new MDDValueCollector (totalVars).collectValues (node);
  }
  
  int[][] collectValues (MDDNode node)
  {
    List l = collectValuesRecur (node);
    return (int[][]) l.toArray (new int[l.size ()][]);
  }
  

  // -- recursively collects values
  List collectValuesRecur (MDDNode node)
  {
    List result = new ArrayList ();

    if (node.isConstant ())
      {
	// -- for a constant node there is just one assigment array
	int[] values = new int [totalVars + 1];
	Arrays.fill (values, MDDManager.NO_VALUE);
	values [totalVars] = node.getValue ();
	result.add (values);
	return result;
      }

    // -- recursive call
    MDDNode[] kids = node.getChildren ();
    int var = node.getVarIndex ();
    
    for (int i = 0; i < kids.length; i++)
      {
	List kidResults = collectValuesRecur (kids [i]);
	for (Iterator it = kidResults.iterator (); it.hasNext (); )
	  {
	    int[] kidResult = (int[])it.next ();
	    int[] myResult = (int[])kidResult.clone ();
	    myResult [var] = i;
	    result.add (myResult);
	  }
      }
    return result;
  }
  
}
