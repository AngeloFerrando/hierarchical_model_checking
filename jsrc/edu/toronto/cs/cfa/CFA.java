package edu.toronto.cs.cfa;

import edu.toronto.cs.mvset.*;
import edu.toronto.cs.grappa.*;
import edu.toronto.cs.davinci.*;

import java.util.*;

/**
 * CFA.java
 *
 *
 * Created: Sun May 30 00:13:44 2004
 *
 * @author <a href="mailto:arie@localhost.localdomain">Arie Gurfinkel</a>
 * @version
 */

public class CFA 
{
  ArrayList nb;
  int used = 0;
  List fwdEdges;
  List bwdEdges;
  //CFANode[] nodes;	


  public CFA (int nodesSize)
  {
    this (nodesSize, true);
  }
  
  public CFA (int nodesSize, boolean edges)
  {
    nb = new ArrayList(nodesSize);
    fwdEdges = null;
    bwdEdges = null;
    if (edges)
      {
	fwdEdges = new ArrayList (nodesSize);
	bwdEdges = new ArrayList (nodesSize);
	for (int i = 0; i < nodesSize; i++)
	  {
	    fwdEdges.add (new ArrayList ());
	    bwdEdges.add (new ArrayList ());
	  }
      }
  }
  
//   public CFA() 
//   {
//     nodes = new ArrayList ();
//     fwdEdges = new ArrayList ();
//     bwdEdges = new ArrayList ();
//   }
  
  public CFANode addNode (String strValue, MvSet state)
  {
    assert state != null : "a mess";

    // -- not thread safe! 
    // -- since 'used' can be changed by another thread    
    CFANode n =  new CFANode (used, strValue, state);
    nb.add(n);
    used++;

    if (fwdEdges != null && used > fwdEdges.size ())
      {
	fwdEdges.add (new ArrayList ());
	bwdEdges.add (new ArrayList ());
      }
    
    return n;
  }
  
  public CFABox addBox (String strValue, MvSet state)
  {
    assert state != null : "a mess";

    // -- not thread safe! 
    // -- since 'used' can be changed by another thread    
    CFABox b =  new CFABox (used, strValue, state);
    nb.add(b);
    used++;	

    if (fwdEdges != null && used > fwdEdges.size ())
      {
	fwdEdges.add (new ArrayList ());
	bwdEdges.add (new ArrayList ());
      }
    
    return b;
  } 

  public CFAEdge addEdge (String strValue, int src, int dst, MvRelation data)
  {
    assert data != null;
    
    CFAEdge edg = new CFAEdge (strValue, src, dst, data);
    ((List)fwdEdges.get (src)).add (edg);
    ((List)bwdEdges.get (dst)).add (edg);
    return edg;    
  }
  
  public CFAEdge addEdge (String strValue, int src, int dst, 
			  MvRelation data, int dst2)
  {
    assert data != null;
    
    CFAEdge edg = new CFAEdge (strValue, src, dst, data, dst2);
    ((List)fwdEdges.get (src)).add (edg);
    //((List)bwdEdges.get (dst)).add (edg);
    //((List)bwdEdges.get (dst2)).add (edg);
    return edg;
  }
  
  public CFAEdge addEdge (String strValue, CFANode src, CFANode dst, 
			  MvRelation data)
  {
    return addEdge (strValue, src.getId (), dst.getId (), data);
  }
  

  public ArrayList getNodes ()
  {
    return nb;
  }
  public int nodeSize ()
  {
    return used;
  }
  /*public CFANode[] getNod ()
  {
    return nodes;
  }*/

  public void dumpNodes ()
  {
     System.err.println ("CFA has: " + used + " nodes");
     int count = 0;
     for (int i = 0; i < used; i++)
       {
	 CFANode node = (CFANode) nb.get(i);
	 System.err.println ("Node  " + count + ": " + node);
	 dumpMvSet (node.getState ());
       }
  }
  
  public void dumpEdges ()
  {
//     int count = 0;
//     for (Iterator it = fwdEdges.iterator (); it.hasNext (); count++)
//       {
// 	System.err.println ("Edges from: " + count);
// 	dumpEdgeList ((List)it.next ());
//       }
    
  }
  public void dumpEdgeList (List edgeList)
  {
    if (edgeList.isEmpty ()) System.err.println ("empty");
    for (Iterator it = edgeList.iterator (); it.hasNext ();)
      {
	CFAEdge edge = (CFAEdge) it.next ();
	if (edge.isHyperEdge ())
          System.err.println ("edge " + edge.getStrValue () + 
	  		      " (" + edge.getSourceId () + ", " + 
			      edge.getDestId () + ", " + edge.getDest2Id () + ")");
        else
          System.err.println ("edge " + edge.getStrValue () + 
	  		      " (" + edge.getSourceId () + ", " + 
			      edge.getDestId () + ")");
        
	dumpMvSet (edge.getData ().toMvSet ());
      }
    
  }
  
  
  private void dumpMvSet (MvSet mvSet)
  {
    for (Iterator it = mvSet.cubeIterator (); it.hasNext ();)
      System.err.println (Arrays.asList ((Object[])it.next ()));
  }
  
  public List getBwdEdges (CFANode node)
  {
    return getBwdEdges (node.getId ());
  }
  public List getBwdEdges (CFABox box)
  {
    return getBwdEdges (box.getId ());
  }
  public List getBwdEdges (int nodeId)
  {
    return (List) bwdEdges.get (nodeId);
  }
  
  public List getFwdEdges (CFANode node)
  {
    return getFwdEdges (node.getId ());
  }
  public List getFwdEdges (CFABox box)
  {
    return getFwdEdges (box.getId ());
  }
  public List getFwdEdges (int nodeId)
  {
    return (List) fwdEdges.get (nodeId);
  }
  
  
  
  public Object getNode (int id)
  {
    return nb.get(id);
  }
  public List getNodeFwdEdges (int id)
  {
    return (List) fwdEdges.get (id);
  }
  public List getNodeBwdEdges (int id)
  {
    return (List) bwdEdges.get (id);
  }
  
  public List getNodeFwdEdges (CFANode n)
  {
    return getNodeFwdEdges (n.getId ());
  }
  public List getNodeBwdEdges (CFANode n)
  {
    return getNodeBwdEdges (n.getId ());
  }

  
  

  public boolean equals (Object o)
  {
    if (this == o) return true;
    
    if (o != null && o.getClass () == CFA.class)
      return equals ((CFA)o);
    return false;
  }
  public boolean equals (CFA cfa)
  {
    // XXX this is node equality only, edges are ignored

    if (used != cfa.nodeSize ()) return false;

    ArrayList cfaNodes = cfa.getNodes ();

    for (int i = 0; i < used; i++)
      if (! cfaNodes.get(i).equals (nb.get(i))) return false;
    return true;
  }  
  
  public DaVinciGraph toDaVinci ()
  {
    DaVinciGraph graph = new DaVinciGraph ();
    List graphNodes = new ArrayList ();
    
    for (int i = 0; i < used; i++)
      {
	CFANode node = (CFANode) nb.get(i) ;
	graphNodes.add (graph.node ().label (node.toString ()));
      }    

    for (Iterator it = fwdEdges.iterator (); it.hasNext ();)
      {
	List edges = (List) it.next ();

	for (Iterator jt = edges.iterator (); jt.hasNext ();)
	  {
	    CFAEdge edge = (CFAEdge) jt.next ();
	    DaVinciGraph.FullNode src = 
	      (DaVinciGraph.FullNode) graphNodes.get (edge.getSourceId ());
	    DaVinciGraph.FullNode dst = 
	      (DaVinciGraph.FullNode) graphNodes.get (edge.getDestId ());
            src.labeledEdge (dst, edge.toString ());
            if (edge.isHyperEdge ()) {
              DaVinciGraph.FullNode dst2 = 
	       (DaVinciGraph.FullNode) graphNodes.get (edge.getDest2Id ());     
	      src.labeledEdge (dst2, edge.toString ());
            }
	  }
      }
    return graph;
  }

  public GrappaGraph toDot ()
  {
    GrappaGraph graph = new GrappaGraph ();
    List graphNodes = new ArrayList ();
    
    for (int i = 0; i < used; i++)
      {
	CFANode node = (CFANode) nb.get(i);
	graphNodes.add (graph.node ().label (node.toString ()));
      }
    for (Iterator it = fwdEdges.iterator (); it.hasNext ();)
      {
	List edges = (List) it.next ();
	
	for (Iterator jt = edges.iterator (); jt.hasNext ();)
	  {
	    CFAEdge edge = (CFAEdge) jt.next ();
	    
	    GrappaGraph.GrappaNode src = 
	      (GrappaGraph.GrappaNode) graphNodes.get (edge.getSourceId ());
	    GrappaGraph.GrappaNode dst = 
	      (GrappaGraph.GrappaNode) graphNodes.get (edge.getDestId ());
            src.edge (dst).label (edge.toString ());
            if (edge.isHyperEdge ()) {
              GrappaGraph.GrappaNode dst2 = 
	       (GrappaGraph.GrappaNode) graphNodes.get (edge.getDest2Id ());
	      src.edge (dst2).label (edge.toString ());
            }
	  }
      }
    return graph;    
  }
  
  
  /**
   * <code>CFANode</code> a node of a CFA 
   * a CFA node contains
   * -- a unique identity
   * -- a string representation
   * -- an mvset for representing node data
   * -- a set of edges pointing to other CFANodes
   *
   * @author <a href="mailto:arie@localhost.localdomain">Arie Gurfinkel</a>
   * @version 1.0
   */
  public class CFANode
  {
    int id;
    String strValue;
    MvSet state;

  
    public CFANode (int _id, String _strValue, MvSet _state)
    {
      id = _id;
      strValue = _strValue;
      state = _state;
    }
  

    /**
     * Get the State value.
     * @return the State value.
     */
    public MvSet getState() {
      return state;
    }

    /**
     * Set the State value.
     * @param newState The new State value.
     */
    public void setState(MvSet newState) {
      this.state = newState;
    }

  

    /**
     * Get the Id value.
     * @return the Id value.
     */
    public int getId() {
      return id;
    }

    /**
     * Set the Id value.
     * @param newId The new Id value.
     */
    public void setId(int newId) {
      this.id = newId;
    }


    /**
     * Get the StrValue value.
     * @return the StrValue value.
     */
    public String getStrValue() {
      return strValue;
    }

    /**
     * Set the StrValue value.
     * @param newStrValue The new StrValue value.
     */
    public void setStrValue(String newStrValue) {
      this.strValue = newStrValue;
    }

  
  
    public String toString ()
    {
      return strValue;
    }

    public boolean equals (Object o)
    {
      if (this == o) return true;
      if (o != null && o.getClass () == CFANode.class)
	return equals ((CFANode)o);
      return false;
    }
  
    public boolean equals (CFANode n)
    {
      return (getId () == n.getId () && getState ().equals (n.getState ()));
    }
 
  
    // -- need a clone function 
    // -- need mv-set interface functions probably
  
  }

   public class CFABox
  {
    int id;
    String strValue;
    MvSet state;

  
    public CFABox (int _id, String _strValue, MvSet _state)
    {
      id = _id;
      strValue = _strValue;
      state = _state;
    }
  

    /**
     * Get the State value.
     * @return the State value.
     */
    public MvSet getState() {
      return state;
    }

    /**
     * Set the State value.
     * @param newState The new State value.
     */
    public void setState(MvSet newState) {
      this.state = newState;
    }

  

    /**
     * Get the Id value.
     * @return the Id value.
     */
    public int getId() {
      return id;
    }

    /**
     * Set the Id value.
     * @param newId The new Id value.
     */
    public void setId(int newId) {
      this.id = newId;
    }


    /**
     * Get the StrValue value.
     * @return the StrValue value.
     */
    public String getStrValue() {
      return strValue;
    }

    /**
     * Set the StrValue value.
     * @param newStrValue The new StrValue value.
     */
    public void setStrValue(String newStrValue) {
      this.strValue = newStrValue;
    }

  
  
    public String toString ()
    {
      return strValue;
    }

    public boolean equals (Object o)
    {
      if (this == o) return true;
      if (o != null && o.getClass () == CFABox.class)
	return equals ((CFABox)o);
      return false;
    }
  
    public boolean equals (CFABox n)
    {
      return (getId () == n.getId () && getState ().equals (n.getState ()));
    }
 
  
    // -- need a clone function 
    // -- need mv-set interface functions probably
  
  }

  /**
   * CFAEdge.java
   *
   * Contains
   *  -- string representation
   *  -- source identity
   *  -- destination identity
   *  -- mvset data
   *
   * Created: Sun May 30 00:10:26 2004
   *
   * @author <a href="mailto:arie@localhost.localdomain">Arie Gurfinkel</a>
   * @version
   */

  public class CFAEdge 
  {
    String strValue;

    int sourceId;
    int destId;
    int destId2;
    MvRelation data;
   
    public CFAEdge (String _strValue, int sId, int dId, MvRelation _data, int dId2)
    {
      strValue = _strValue;
      sourceId = sId;
      destId = dId;
      destId2 = dId2;
      data = _data;
    }

    public CFAEdge (String _strValue, int sId, int dId, MvRelation _data)
    {
      strValue = _strValue;
      sourceId = sId;
      destId = dId;
      destId2 = -1;
      data = _data;
    }
    
    public boolean isHyperEdge ()
    {
      return (destId2 != -1);
    }
    
    /**
     * Get the Data value.
     * @return the Data value.
     */
    public MvRelation getData () 
    {
      return data;
    }

    /**
     * Set the Data value.
     * @param newData The new Data value.
     */
    public void setData (MvRelation newData) 
    {
      this.data = newData;
    }

  
    /**
     * Get the DestId value.
     * @return the DestId value.
     */
    public int getDestId () 
    {
      return destId;
    }
    
    public int getDest2Id ()
    {
      return destId2;
    }

    /**
     * Set the DestId value.
     * @param newDestId The new DestId value.
     */
    public void setDestId(int newDestId) {
      this.destId = newDestId;
    }

  
    /**
     * Get the SourceId value.
     * @return the SourceId value.
     */
    public int getSourceId() {
      return sourceId;
    }
    


    /**
     * Set the SourceId value.
     * @param newSourceId The new SourceId value.
     */
    public void setSourceId(int newSourceId) {
      this.sourceId = newSourceId;
    }

  
  
    /**
     * Get the StrValue value.
     * @return the StrValue value.
     */
    public String getStrValue() {
      return strValue;
    }

    /**
     * Set the StrValue value.
     * @param newStrValue The new StrValue value.
     */
    public void setStrValue(String newStrValue) {
      this.strValue = newStrValue;
    }
  
    public String toString ()
    {
      return strValue;
    }
  
  
  }// CFAEdge

  
}// CFA
