package edu.toronto.cs.mdd;

import edu.toronto.cs.davinci.*;
import edu.toronto.cs.davinci.DaVinciGraph.*;
import java.util.*;


public class MDDToDaVinci
{
  DaVinciGraph graph;

  Map seen;
  

  private MDDToDaVinci ()
  {
    graph = new DaVinciGraph ();
    seen = new HashMap ();
  }
  
  public static DaVinciGraph toDavinci (MDDNode node)
  {
    MDDToDaVinci a = new MDDToDaVinci ();
    a.toDavinciRecur (node);
    return a.graph;
  }
  

  public FullNode toDavinciRecur (MDDNode node)
  {
    FullNode self;

    self = (FullNode)seen.get (node);
    if (self != null) return self;

    if (node.isConstant ())
      {
	self = graph.node ().
	  label (String.valueOf (node.getValue ())).
	  box ("circle");
	seen.put (node, self);
	return self;
      }
    

    MDDNode[] kids = node.getChildren ();
    
    self = graph.node ().
      label (String.valueOf (node.getVarIndex ())).
      border ("double");
    
    

    for (int i = 0; i < kids.length; i++)
      self.labeledEdge (toDavinciRecur (kids [i]), String.valueOf (i));
    
    seen.put (node, self);
    return self;
  }
  

  public static void main (String[] args)
  {
    Map seen = new HashMap ();
    MDDManager manager = new MDDManager (10, 2);

    MDDNode constant = manager.getLeafNode (0);
    System.out.println ("Putting in a map: " + constant);
    seen.put (constant, constant);
    seen.put (manager.getLeafNode (1), manager.getLeafNode (1));
    System.out.println ("Getting from a map");
    System.out.println (seen.get (manager.getLeafNode (0)));
  }
  
}
