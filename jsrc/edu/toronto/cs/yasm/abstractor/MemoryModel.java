package edu.toronto.cs.yasm.abstractor;

import edu.toronto.cs.expr.*;
import java.util.*;

public class MemoryModel extends HashMap
{

  public MemoryModel ()
  {
      super ();
  }

  public Expr lookup (Expr e)
  {
      return (Expr) get (e);
  }

  public Object get (Object e)
  {
    Expr result = (Expr) super.get (e);

    if (result == null)
      return e;

    return result;
  }

  public void assign (Expr e, Expr v)
  {
    // System.err.println ("assigning " + e + " -> " + v);
    this.put (e, v);
  }

}
