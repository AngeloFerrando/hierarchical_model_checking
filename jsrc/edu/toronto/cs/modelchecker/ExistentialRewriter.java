package edu.toronto.cs.modelchecker;

import edu.toronto.cs.ctl.*;
import edu.toronto.cs.util.*;

public class ExistentialRewriter extends CloningRewriter
{
  public Object visitAFNode (CTLAFNode node, Object o)
  {
    // --  AF p == ~EG ~p
    if (node.getFairness ().length > 0)
      return node;
    return rewrite (node.getRight ()).neg ().eg ().neg ();
  }
  
  public Object visitAGNode (CTLAGNode node, Object o)
  {
    if (node.getFairness ().length > 0)
      return node;
    // -- AG p == ~EF ~p
    return rewrite (node.getRight ()).neg ().ef ().neg ();
  }
  
  public Object visitAUNode (CTLAUNode node, Object o)
  {
    // -- A[p U q] = ~ (EG ~q \/ E[~q /\ p U ~p /\ ~q])
    CTLNode p = rewrite (node.getLeft ());
    CTLNode q = rewrite (node.getRight ());
    
    return 
      q.neg ().eg ().
      or 
      (p.and (q.neg ()).eu (p.neg ().and (q.neg ()))).
      neg ();
  }

  public Object visitAUiNode (CTLAUiNode node, Object o)
  {
    assert false : "Don't know how to rewrite AUi node";
    return null;
  }
  
  public Object visitAXNode (CTLAXNode node, Object o)
  {
    // -- AX p == ~EX~p
    return rewrite (node.getRight ()).neg ().ex ().neg ();
  }
  
}

