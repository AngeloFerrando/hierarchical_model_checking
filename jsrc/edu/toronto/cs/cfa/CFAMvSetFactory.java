package edu.toronto.cs.cfa;

import edu.toronto.cs.mvset.*;
import edu.toronto.cs.algebra.*;
import edu.toronto.cs.util.*;
import edu.toronto.cs.cfa.CFA.*;

import java.util.*;

/**
 * CFAMvSetFactory.java
 *
 *
 * Created: Sun May 30 18:55:34 2004
 *
 * @author <a href="mailto:arie@localhost.localdomain">Arie Gurfinkel</a>
 * @version
 */

public class CFAMvSetFactory extends AbstractMvSetFactory
{
  // -- base CFA that has the right string representations
  // -- and tells us how many nodes we have
  CFA cfa;
  ArrayList cfaNodes;
  int usedNodes;
  
  // -- the underlying mvset-factory we are using
  MvSetFactory factory;

  CFAMvSet top;
  CFAMvSet bot;
  CFAMvSet infoTop;
  CFAMvSet infoBot;
  
  MvSet topMvSet;
  MvSet botMvSet;
  MvSet infoTopMvSet;
  MvSet infoBotMvSet;
  

  BelnapAlgebra balgebra;

  public CFAMvSetFactory (MvSetFactory _factory, CFA _cfa)
  {
    super (_factory.getAlgebra ());
    factory = _factory;
    cfa = _cfa;
    usedNodes = cfa.nodeSize ();
    cfaNodes = cfa.getNodes ();
    
    balgebra = (BelnapAlgebra) getAlgebra ();
    assert cfa.nodeSize () > 0 : "Empty CFA";


    
    topMvSet = factory.createConstant (balgebra.top ());
    botMvSet = factory.createConstant (balgebra.bot ());
    infoTopMvSet = factory.createConstant (balgebra.infoTop ());
    infoBotMvSet = factory.createConstant (balgebra.infoBot ());
    
    top = (CFAMvSet) embedMvSet (topMvSet);
    bot = (CFAMvSet) embedMvSet (botMvSet);
    infoTop = (CFAMvSet) embedMvSet (infoTopMvSet);
    infoBot = (CFAMvSet) embedMvSet (infoBotMvSet);
  }

  public MvRelation getMvRelation ()
  {
    return new CFAMvRelation (cfa);
  }
  
  // -- returns the factory we were initialized with
  public MvSetFactory getMvSetFactory ()
  {
    return factory;
  }

  public CFA getCFA ()
  {
    return cfa;
  }

  public MvSet createConstant (AlgebraValue value)
  {
    if (value == balgebra.top ()) return top;
    if (value == balgebra.bot ()) return bot;
    if (value == balgebra.infoTop ()) return infoTop;
    if (value == balgebra.infoBot ()) return infoBot;
    
    return embedMvSet (factory.createConstant (value));
  }
  
  public MvSet createProjection (int argIdx)
  {
    return embedMvSet (factory.createProjection (argIdx));
  }

  // -- embeds an mv-set into a cfa
  public MvSet embed (int nodeId, MvSet mvSet)
  {
    CFA result = new CFA (usedNodes, false);
    MvSet resultMvSet;
    
    int count = 0;
    for (int i = 0; i < usedNodes; i++)
      {
	if(cfaNodes.get(i) instanceof CFA.CFANode){
		CFA.CFANode node = (CFA.CFANode) cfaNodes.get(i);
		resultMvSet = (i == nodeId) ? mvSet : factory.bot ();
		result.addNode (node.getStrValue (), resultMvSet);
	}
	else{
		CFA.CFABox box = (CFA.CFABox) cfaNodes.get(i);
		resultMvSet = (i == nodeId) ? mvSet : factory.bot ();
		result.addBox (box.getStrValue (), mvSet);	
	}
      }
    return makeMvSet (result);
  }

  public MvSet embedMvSet (MvSet mvSet)
  { 
    CFA result = new CFA (usedNodes, false);
    MvSet resultMvSet;
    
    for (int i = 0; i < usedNodes; i++)
      {
	if(cfaNodes.get(i) instanceof CFA.CFANode){
		CFA.CFANode node = (CFA.CFANode) cfaNodes.get(i);
		result.addNode (node.getStrValue (), mvSet);
	}
	else{
		CFA.CFABox box = (CFA.CFABox) cfaNodes.get(i);
		result.addBox (box.getStrValue (), mvSet);
	}
      }
    return makeMvSet (result, mvSet.toString ());
  }
  
  
  
  public MvSet top ()
  {
    return top;
  }
  
  public MvSet bot ()
  {
    return bot;
  }

  public MvSet infoTop ()
  {
    return infoTop;
  }
  public MvSet infoBot ()
  {
    return infoBot;
  }
  
  
  public MvSet createCase (int argIdx, MvSet[] children)
  {
    return embedMvSet (factory.createCase (argIdx, children));
  }
  
  public MvSet createPoint (AlgebraValue[] args, AlgebraValue value)
  {
    return embedMvSet (factory.createPoint (args, value));
  }
  public MvSet buildCube (int[] varIndex)
  {
    return embedMvSet (factory.buildCube (varIndex));
  }

  public MvSet var (int arg, AlgebraValue argVal, AlgebraValue value)
  {
    return embedMvSet (factory.var (arg, argVal, value));
  }
  
  // XXX use with care
  public CFAMvSet makeMvSet (CFA v, String name)
  {
    return new CFAMvSet (v, name);
  }
  CFAMvSet makeMvSet (CFA v)
  {
    return makeMvSet (v, null);
  }
  

  

  // XXX use with care
//   public CFAEdgedMvSet makeEdgedMvSet (CFA v)
//   {
//     // -- an mv-set where edges are important
//     return new CFAEdgedMvSet (v);
//   }
  
  
  public class CFAMvSet extends AbstractMvSet
  {
    CFA cfa;
    ArrayList cfaNodes;
    int usedNodes;
    String name;
    
    protected CFAMvSet (CFA _cfa)
    {
      this (_cfa, null);
    }
    
    protected CFAMvSet (CFA _cfa, String _name)
    {
      cfa = _cfa;
      name = _name;
      cfaNodes = cfa.getNodes ();
      usedNodes = cfa.nodeSize ();
	//System.out.println("nodi "+usedNodes);
    }

    public String toString ()
    {
      //cfa.dumpNodes ();
      if (name == null) return super.toString ();
      return name;
    }
    
    
    public CFA getCFA ()
    {
      return cfa;
    }
    
    public MvSet ptwiseCompose (int op, MvSet g)
    {
      assert g.getClass () == CFAMvSet.class;
      
      CFA gCFA = ((CFAMvSet)g).getCFA ();

      assert usedNodes == gCFA.nodeSize () 
	: "Different number of nodes: " + usedNodes + 
	" versus " + gCFA.nodeSize ();

      switch (op)
	{
	case MEET:
	  if (this == g) return this;
	  if (this == top) return g;
	  if (g == top) return this;
	  if (g == bot || this == bot) return bot;
	  break;
	case JOIN:
	  if (this == g) return this;
	  if (this == bot) return g;
	  if (g == bot) return this;
	  if (g == top || this == top) return top;
	  break;
	default:
	  break;
	}

      CFA result = new CFA (usedNodes, false);
      
      //for (int i = 0; i < cfa.getNodes ().size (); i++)
      ArrayList gNodes = gCFA.getNodes ();

      boolean sameAsG = true;
      boolean sameAsSelf = true;
      
      for (int i = 0; i < usedNodes; i++)
	{
	  if(cfaNodes.get(i) instanceof CFA.CFANode){
		MvSet nodeValue;
		if(gNodes.get(i) instanceof CFA.CFANode){
	  		nodeValue = ((CFA.CFANode)cfaNodes.get(i)).getState ().ptwiseCompose (op, ((CFA.CFANode) gNodes.get(i)).getState ());
			if (sameAsG && !nodeValue.equals (( (CFA.CFANode) gNodes.get(i)).getState ()))
	    			sameAsG = false;
	  		if (sameAsSelf && !nodeValue.equals (( (CFA.CFANode) cfaNodes.get(i)).getState ()))
	    			sameAsSelf = false;
	  		result.addNode (((CFA.CFANode) cfaNodes.get(i)).getStrValue (), nodeValue);
		}
		else{
			nodeValue = ((CFA.CFANode)cfaNodes.get(i)).getState ().ptwiseCompose (op, ((CFA.CFABox) gNodes.get(i)).getState ());
			if (sameAsG && !nodeValue.equals (( (CFA.CFABox) gNodes.get(i)).getState ()))
	    			sameAsG = false;
	  		if (sameAsSelf && !nodeValue.equals (( (CFA.CFANode) cfaNodes.get(i)).getState ()))
	    			sameAsSelf = false;
	  		result.addNode (((CFA.CFANode) cfaNodes.get(i)).getStrValue (), nodeValue);
		}
	  }
	  else{
		MvSet boxValue;
		if(gNodes.get(i) instanceof CFA.CFANode){
			boxValue = ((CFA.CFABox)cfaNodes.get(i)).getState ().ptwiseCompose (op, ((CFA.CFANode) gNodes.get(i)).getState ());
			if (sameAsG && !boxValue.equals (( (CFA.CFANode) gNodes.get(i)).getState ()))
	    			sameAsG = false;
	  		if (sameAsSelf && !boxValue.equals (( (CFA.CFABox) cfaNodes.get(i)).getState ()))
	    			sameAsSelf = false;
	  		result.addBox (((CFA.CFABox) cfaNodes.get(i)).getStrValue (), boxValue);
	  	}
		else{
			boxValue = ((CFA.CFABox)cfaNodes.get(i)).getState ().ptwiseCompose (op, ((CFA.CFABox) gNodes.get(i)).getState ());
			if (sameAsG && !boxValue.equals (( (CFA.CFABox) gNodes.get(i)).getState ()))
	    			sameAsG = false;
	  		if (sameAsSelf && !boxValue.equals (( (CFA.CFABox) cfaNodes.get(i)).getState ()))
	    			sameAsSelf = false;
	  		result.addBox (((CFA.CFABox) cfaNodes.get(i)).getStrValue (), boxValue);
		}
	 }
 	}
      
      if (sameAsSelf) return this;
      if (sameAsG) return g;
      
      return makeMvSet (result);
    }

    public MvSet ptwiseNeg ()
    {

      if (this == top) return bot;
      if (this == bot) return top;
      if (this == infoTop || this == infoBot) return this;
      
      CFA result = new CFA (usedNodes, false);
      
      //for (Iterator it = cfa.getNodes ().iterator (); it.hasNext ();)
      for (int i = 0; i < usedNodes; i++)
	{
	  if(cfaNodes.get(i) instanceof CFA.CFANode){
	  	CFA.CFANode node = (CFA.CFANode) cfaNodes.get(i);
	  	result.addNode (node.getStrValue (), node.getState ().ptwiseNeg ());
	  }
	  else{
		CFA.CFABox box = (CFA.CFABox) cfaNodes.get(i);
	  	result.addBox (box.getStrValue (), box.getState ().ptwiseNeg ());
	  }
	}
      return makeMvSet (result);
    }

    public MvSet ptwiseCompare (int op, MvSet g)
    {
      assert g.getClass () == CFAMvSet.class;

      CFA gCFA = ((CFAMvSet)g).getCFA ();
      
      CFA result = new CFA (usedNodes, false);
      ArrayList gNodes = gCFA.getNodes ();
      
      for (int i = 0; i < usedNodes; i++)
	{
	  if(cfaNodes.get(i) instanceof CFA.CFANode){
		CFA.CFANode selfNode = (CFA.CFANode)  cfaNodes.get(i) ;
		if(gNodes.get(i) instanceof CFA.CFANode){
	  		CFA.CFANode gNode = (CFA.CFANode) gNodes.get(i);
	  		result.addNode (selfNode.getStrValue (), selfNode.getState ().ptwiseCompare (op, gNode.getState ()));
		}
		else{
	  		CFA.CFABox gBox = (CFA.CFABox) gNodes.get(i);
	  		result.addNode (selfNode.getStrValue (), selfNode.getState ().ptwiseCompare (op, gBox.getState ()));
		}
	  }
	  else{
		CFA.CFABox selfBox = (CFA.CFABox)  cfaNodes.get(i) ;
		if(gNodes.get(i) instanceof CFA.CFANode){
			CFA.CFANode gNode = (CFA.CFANode) gNodes.get(i);
	  		result.addBox (selfBox.getStrValue (), selfBox.getState ().ptwiseCompare (op, gNode.getState ()));
		}
		else{
			CFA.CFABox gBox = (CFA.CFABox) gNodes.get(i);
	  		result.addBox (selfBox.getStrValue (), selfBox.getState ().ptwiseCompare (op, gBox.getState ()));
	  	}
	  }
      }
      return makeMvSet (result);
    }
   
    public MvSet cofactor (int argIdx, AlgebraValue value)
    {

      CFA result = new CFA (usedNodes, false);
      
      for (int i = 0; i < usedNodes; i++)
	{
	  if(cfaNodes.get(i) instanceof CFA.CFANode){
	  	CFA.CFANode node = (CFA.CFANode) cfaNodes.get(i);
	  	result.addNode (node.getStrValue (), node.getState ().cofactor (argIdx, value));
	  }
	  else{
		CFA.CFABox box = (CFA.CFABox) cfaNodes.get(i);
	  	result.addBox (box.getStrValue (), box.getState ().cofactor (argIdx, value));
	  }
	}
      
      return makeMvSet (result);
    }

    public MvSet cofactor (AlgebraValue[] r)
    {

      CFA result = new CFA (usedNodes, false);
      
      for (int i = 0; i < usedNodes; i++)
	{
	  if(cfaNodes.get(i) instanceof CFA.CFANode){
	  	CFA.CFANode node = (CFA.CFANode) cfaNodes.get(i);
	  	result.addNode (node.getStrValue (), node.getState ().cofactor (r));
	  }
	  else{
		CFA.CFABox box = (CFA.CFABox) cfaNodes.get(i);
	  	result.addBox (box.getStrValue (), box.getState ().cofactor (r));
          }
	}
      
      return makeMvSet (result);

    }
    

    public MvSet cofactor (MvSet v)
    {
      CFA vCFA = ((CFAMvSet)v).getCFA ();
      
      int id = firstNonFalse (vCFA.getNodes ());
      
      //assert id >= 0 && id < usedNodes;
      if (id < 0 || id >= usedNodes) return this;
      if(cfaNodes.get(id) instanceof CFA.CFANode){
	if( ( vCFA.getNodes().get (id) ) instanceof CFA.CFANode)
      		return embedMvSet (((CFA.CFANode)cfaNodes.get(id)).getState ().cofactor (((CFA.CFANode)vCFA.getNodes().get (id)).getState ()));
	else
		return embedMvSet (((CFA.CFANode)cfaNodes.get(id)).getState ().cofactor (((CFA.CFABox)vCFA.getNodes().get (id)).getState ()));
      }
      else{
	 if( ( vCFA.getNodes().get (id) ) instanceof CFA.CFANode)
	 	return embedMvSet (((CFA.CFABox)cfaNodes.get(id)).getState ().cofactor (((CFA.CFANode)vCFA.getNodes().get (id)).getState ()));
	 else
		return embedMvSet (((CFA.CFABox)cfaNodes.get(id)).getState ().cofactor (((CFA.CFABox)vCFA.getNodes().get (id)).getState ()));
      }
	
    }
    private final int firstNonFalse (ArrayList nodes)
    {
      for (int i = 0; i < usedNodes; i++){
	if(cfaNodes.get(i) instanceof CFA.CFANode){
		if (!( ( (CFA.CFANode)nodes.get(i)).getState ().equals (factory.bot ())) )
	  		return i;
        }
	else {
		if (!( ( (CFA.CFABox)nodes.get(i)).getState ().equals (factory.bot ())) )
	  		return i;
        }
      }
      return -1;
    }
  
    
    public MvSet existAbstract (MvSet _cube)
    {
      // XXX we must abstract all nodes as though they were part
      // XXX of the cube
      // XXX this is ugly 
      
     MvSet cube;
     if( ( ( (CFAMvSet)_cube).getCFA ().getNode (0) ) instanceof CFA.CFANode)
      		cube = ((CFA.CFANode)((CFAMvSet)_cube).getCFA ().getNode (0)).getState ();
     else
		cube = ((CFA.CFABox)((CFAMvSet)_cube).getCFA ().getNode (0)).getState ();
     MvSet result = getMvSetFactory ().bot ();
      
      
      
      //for (Iterator it = cfa.getNodes ().iterator (); it.hasNext ();)
      for (int i = 0; i < usedNodes; i++)
	{
	  if(cfaNodes.get(i) instanceof CFA.CFANode){
	  	CFA.CFANode node = (CFA.CFANode) cfaNodes.get(i);
	  	result = result.or (node.getState ().existAbstract (cube));
	  }
	  else{
		CFA.CFABox box = (CFA.CFABox) cfaNodes.get(i);
	  	result = result.or (box.getState ().existAbstract (cube));
	  }
	  
// 	  result.addNode (node.getStrValue (), 
// 			  node.getState ().existAbstract (cube));
	}
      
      //       return makeMvSet (result);
      return embedMvSet (result);
    }
    
    public MvSet forallAbstract (MvSet _cube)
    {

      // XXX see comments for existAbstract
      // XXX this is ugly 
      MvSet cube;
      if( (((CFAMvSet)_cube).getCFA ().getNode (0)) instanceof CFA.CFANode)
      		cube = ((CFA.CFANode)((CFAMvSet)_cube).getCFA ().getNode (0)).getState ();
      else
		cube = ((CFA.CFABox)((CFAMvSet)_cube).getCFA ().getNode (0)).getState ();

      MvSet result = getMvSetFactory ().top ();

      
      for (int i = 0; i < usedNodes; i++)
	{
 	  if(cfaNodes.get(i) instanceof CFA.CFANode){	
	  	CFA.CFANode node = (CFA.CFANode) cfaNodes.get(i);
	  	result = result.and (node.getState ().forallAbstract (cube));
          }
	  else{
		CFA.CFABox box = (CFA.CFABox) cfaNodes.get(i);
	  	result = result.and (box.getState ().forallAbstract (cube));
	  }
	}      
      return embedMvSet (result);
    }
    
    public MvSet renameArgs (int[] newArgs)
    {
      CFA result = new CFA (usedNodes, false);
      
      
      for (int i = 0; i < usedNodes; i++)
	{
	  if(cfaNodes.get(i) instanceof CFA.CFANode){
	  	CFA.CFANode node = (CFA.CFANode) cfaNodes.get(i);
	  	result.addNode (node.getStrValue (), node.getState ().renameArgs(newArgs));
	  }
	  else{
		CFA.CFABox box = (CFA.CFABox) cfaNodes.get(i);
	  	result.addBox (box.getStrValue (), box.getState ().renameArgs(newArgs));
	  }
	}
      
      return makeMvSet (result);
    }
    
    public AlgebraValue evaluate (AlgebraValue[] values)
    {
      throw new UnsupportedOperationException ("evaluate not supported");
    }
    
    public MvSetFactory getFactory ()
    {
      return CFAMvSetFactory.this;
    }
    
    public IAlgebra getAlgebra ()
    {
      return getFactory ().getAlgebra ();
    }
   
    public boolean isConstant ()
    {
      if (this == top || this == bot || this == infoTop || this == infoBot)
	return true;
      
      MvSet value = null;
      for (int i = 0; i < usedNodes; i++)
	{
          if(cfaNodes.get(i) instanceof CFA.CFANode){
	  	CFA.CFANode node = (CFA.CFANode) cfaNodes.get(i);
	  	if (value == null) value = node.getState ();
	  	else if (value != node.getState ()) return false;
	  }
 	  else{
		CFA.CFABox box = (CFA.CFABox) cfaNodes.get(i);
	  	if (value == null) value = box.getState ();
	  	else if (value != box.getState ()) return false;
	  }
	}
      return value.isConstant ();
    }
    public AlgebraValue getValue ()
    {
      // System.out.println ("******* getValue of ********");
      // cfa.dumpNodes ();
      // System.out.println ("****************************");
      MvSet value = null;
      // XXX big hack for now
      //assert cfa.getNodes () != null && cfa.getNodes ().size () > 0;
      if( cfaNodes.get(0) instanceof CFA.CFANode)
      	value = ((CFA.CFANode)cfaNodes.get(0)).getState ();
      else
	value = ((CFA.CFABox)cfaNodes.get(0)).getState ();
      return value.getValue ();
    }

    public boolean equals (Object o)
    {
      if (o == this) return true;
      if (o == null || o.getClass () != CFAMvSet.class) return false;
      return equals ((CFAMvSet)o);
    }
    public boolean equals (CFAMvSet v)
    {
      return getCFA ().equals (v.getCFA ());
    }
    
    // -- we need this to hash on MvSets sometimes
    public int hashCode ()
    {
      int stop = 4;
      
      stop = stop >= usedNodes ? usedNodes : stop;
      long hashCode = 0;
      for (int i = 0; i < stop; i++){
	if( cfaNodes.get(i) instanceof CFA.CFANode)
		hashCode +=  ( (CFA.CFANode) cfaNodes.get(i) ).getState ().hashCode ();
	else
		hashCode +=  ( (CFA.CFABox) cfaNodes.get(i) ).getState ().hashCode ();
      }
      return (int) hashCode;
    }
    

    public Iterator mintermIterator (MvSet _vars, AlgebraValue val)
    {
      CFAMvSet vars = (CFAMvSet)_vars;
      // XXX extract the cube 
      MvSet varCube;
      if( vars.getCFA ().getNodes ().get(0) instanceof CFA.CFANode)
		varCube= ( (CFA.CFANode) vars.getCFA ().getNodes ().get(0) ).getState ();
      else
		varCube= ( (CFA.CFABox) vars.getCFA ().getNodes () .get(0) ).getState ();
      MvSet mvSetBot = getMvSetFactory ().bot ();

      for (int i = 0; i < usedNodes; i++)
	{
	  // -- find first cfa node that is not consistently false
	  // -- one of such things must exist
	  if(cfaNodes.get(i) instanceof CFA.CFANode){
	  	CFA.CFANode node = (CFA.CFANode) cfaNodes.get(i);
	  	if (node.getState ().equals (mvSetBot)) continue;
	  	return new MintermIterator (node.getId (), node.getState (),varCube, val);
	  }
	  else{
		CFA.CFABox box = (CFA.CFABox) cfaNodes.get(i);
	  	if (box.getState ().equals (mvSetBot)) continue;
	  	return new MintermIterator (box.getId (), box.getState (),varCube, val);
	  }
	  
      }
      return new Iterator ()
	{
	  public void remove ()
	  {
	    throw new UnsupportedOperationException ();
	  }
	  public boolean hasNext ()
	  {
	    return false;
	  }
	  public Object next ()
	  {
	    throw 
	      new NoSuchElementException ("No elements in empty iterator");
	  }
	};
    }
    
    public BitSet getImage ()
    {
      // -- return an image of the first non-false node
      BitSet image = new BitSet ();
      MvSet mvSetBot = getMvSetFactory ().bot ();
      for (int i = 0; i < usedNodes; i++)
	{
	  if( cfaNodes.get(i) instanceof CFA.CFANode){
	  	CFA.CFANode node = (CFA.CFANode) cfaNodes.get(i);
	  	if (node.getState ().equals (mvSetBot)) continue;
	  	return node.getState ().getImage ();
	  }
	  else{
		CFA.CFABox box = (CFA.CFABox) cfaNodes.get(i);
	  	if (box.getState ().equals (mvSetBot)) continue;
	  	return box.getState ().getImage ();	
	  }	
	}
      return image;
    }
    
    class MintermIterator implements Iterator 
    {
      Iterator mintermIterator;
      int nodeId;
      
      public MintermIterator (int _nodeId, MvSet _mvSet, MvSet _varCube, 
			      AlgebraValue _val)
      {
	nodeId = _nodeId;
	mintermIterator = _mvSet.mintermIterator (_varCube, _val);
      }
      public void remove ()
      {
	throw new UnsupportedOperationException ();
      }
      public boolean hasNext ()
      {
	return mintermIterator.hasNext ();
      }
      public Object next ()
      {
	return embed (nodeId, (MvSet)mintermIterator.next ());
      }
    }
    
    
  }

  

  public class CFAMvRelation implements MvRelation
  {
    CFA cfa;
    ArrayList cfaNodes;
    int usedNodes;
 
    
    public CFAMvRelation (CFA _cfa)
    {
      cfa = _cfa;
      cfaNodes = cfa.getNodes ();
      usedNodes = cfa.nodeSize ();
	//System.out.println("size "+usedNodes);
  
    }
    
    
    public MvSet fwdImage (MvSet v)
    {
      CFA vCFA = ((CFAMvSet)v).getCFA ();
      
      CFA result = new CFA (usedNodes, false);
      
      for (int i = 0; i < vCFA.nodeSize (); i++)
	{
	  if( vCFA.getNode (i) instanceof CFA.CFANode){
	  	CFA.CFANode node = (CFA.CFANode) vCFA.getNode (i);
	  	MvSet nodeValue = getMvSetFactory ().bot ();
	  
	  	for (Iterator jt = cfa.getBwdEdges (node.getId ()).iterator (); jt.hasNext ();)
	    	{
	      		CFA.CFAEdge edge = (CFA.CFAEdge) jt.next ();
			if( vCFA.getNode (edge.getDestId ()) instanceof CFA.CFANode)
   				nodeValue = nodeValue.or (edge.getData ().fwdImage ( ( (CFA.CFANode)vCFA.getNode (edge.getSourceId ()) ).getState ()));
			else
				nodeValue = nodeValue.or (edge.getData ().fwdImage ( ( (CFA.CFABox)vCFA.getNode (edge.getSourceId ()) ).getState ()));
	    	}
	  	result.addNode (node.getStrValue (), nodeValue);
	  }
	  else{
		CFA.CFABox box = (CFA.CFABox) vCFA.getNode (i);
	  	MvSet boxValue = getMvSetFactory ().bot ();
	  
	  	for (Iterator jt = cfa.getBwdEdges (box.getId ()).iterator (); jt.hasNext ();)
	    	{
	      		CFA.CFAEdge edge = (CFA.CFAEdge) jt.next ();
			if( vCFA.getNode (edge.getDestId ()) instanceof CFA.CFANode)
   				boxValue = boxValue.or (edge.getData ().fwdImage ( ( (CFA.CFANode)vCFA.getNode (edge.getSourceId ()) ).getState ()));
			else
				boxValue = boxValue.or (edge.getData ().fwdImage ( ( (CFA.CFABox)vCFA.getNode (edge.getSourceId ()) ).getState ()));
	    	}
	  	result.addBox (box.getStrValue (), boxValue);
	  }
		
	}
      return makeMvSet (result);
    }

    public MvSet dualBwdImage (MvSet v)
    {
      return bwdImage (v.not ()).not ();
    }

    
    public MvSet bwdImage (MvSet v)
    {

      if (v == bot) return bot;
      
      CFA vCFA = ((CFAMvSet)v).getCFA ();
      
      CFA result = new CFA (usedNodes, false);
      
      ArrayList l = vCFA.getNodes();
      for (int i = 0; i < vCFA.nodeSize (); i++)
      {
	if( l.get(i) instanceof CFA.CFANode){
        	CFA.CFANode node = (CFA.CFANode) l.get (i);

        	MvSet nodeValue = getMvSetFactory ().bot ();
       		MvSet axValue = getMvSetFactory ().top ();
        	boolean computeAX = false;
       		for (Iterator jt = cfa.getFwdEdges (node.getId ()).iterator (); jt.hasNext ();)
        	{
          		CFA.CFAEdge edge = (CFA.CFAEdge) jt.next ();
          		if (edge.isHyperEdge()) continue;
			if( vCFA.getNode (edge.getDestId ()) instanceof CFA.CFANode)
          			nodeValue = nodeValue.or (edge.getData ().bwdImage ( ( (CFA.CFANode)vCFA.getNode (edge.getDestId ()) ).getState ()));
			else
				nodeValue = nodeValue.or (edge.getData ().bwdImage ( ( (CFA.CFABox)vCFA.getNode (edge.getDestId ()) ).getState ()));
          		if(edge.getStrValue ().startsWith ("if")) 
	    		{
	      			computeAX = true;
	      			//             axValue = axValue.and 
	      			//               (edge.getData().
	      			//                bwdImage((vCFA.getNode (edge.getDestId ()).getState ()).
	      			//                  not ()).not ());
				if( vCFA.getNode (edge.getDestId ()) instanceof CFA.CFANode)
	      				axValue = axValue.and (edge.getData().dualBwdImage( ( (CFA.CFANode)vCFA.getNode (edge.getDestId ()) ).getState ()));
				else
					axValue = axValue.and (edge.getData().dualBwdImage( ( (CFA.CFABox)vCFA.getNode (edge.getDestId ()) ).getState ()));
	    		}   
        	}
          	if (computeAX) 
            		nodeValue = nodeValue.or (axValue.geq (getMvSetFactory ().infoTop ()));

        	result.addNode (node.getStrValue (), nodeValue);
      	}
	else{
		CFA.CFABox box = (CFA.CFABox) l.get (i);

        	MvSet boxValue = getMvSetFactory ().bot ();
       		MvSet aValue = getMvSetFactory ().top ();
        	boolean computeA = false;
       		for (Iterator jt = cfa.getFwdEdges (box.getId ()).iterator (); jt.hasNext ();)
        	{
          		CFA.CFAEdge edge = (CFA.CFAEdge) jt.next ();
          		if (edge.isHyperEdge()) continue;
			if( vCFA.getNode (edge.getDestId ()) instanceof CFA.CFANode)
          			boxValue = boxValue.or (edge.getData ().bwdImage ( ( (CFA.CFANode)vCFA.getNode (edge.getDestId ()) ).getState ()));
			else
				boxValue = boxValue.or (edge.getData ().bwdImage ( ( (CFA.CFABox)vCFA.getNode (edge.getDestId ()) ).getState ()));

          		if(edge.getStrValue ().startsWith ("if")) 
	    		{
	      			computeA = true;
	      			//             axValue = axValue.and 
	      			//               (edge.getData().
	      			//                bwdImage((vCFA.getNode (edge.getDestId ()).getState ()).
	      			//                  not ()).not ());
				if( vCFA.getNode (edge.getDestId ()) instanceof CFA.CFANode)
	      				aValue = aValue.and (edge.getData().dualBwdImage( ( (CFA.CFANode)vCFA.getNode (edge.getDestId ()) ).getState ()));
				else
					aValue = aValue.and (edge.getData().dualBwdImage( ( (CFA.CFABox)vCFA.getNode (edge.getDestId ()) ).getState ()));
	    		}   
        	}
          	if (computeA) 
            		boxValue = boxValue.or (aValue.geq (getMvSetFactory ().infoTop ()));

        	result.addBox (box.getStrValue (), boxValue);
	}
      }
      return makeMvSet (result);
    }
    
    public MvSet toMvSet ()
    {
      //return makeEdgedMvSet (cfa);
      throw new UnsupportedOperationException 
	("Cannot convert " + CFAMvRelation.class + " to an mvset");
      
    }
   
    
  }
  
  public static void dumpMvSet (MvSet mvSet)
  {
    for (Iterator it = mvSet.cubeIterator (); it.hasNext ();)
      System.out.println (Arrays.asList ((Object[])it.next ()));
  }
  
}
