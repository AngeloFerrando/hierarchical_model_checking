package edu.toronto.cs.expr;


public class VariableOp extends NullaryOperator
{
  String name;
  
  public VariableOp (String _name)
  {
    name = _name;
  }
  
  public String name ()
  {
    return name;
  }
  
  public int hashCode ()
  {
    return name.hashCode ();
  }
  
  public boolean equals (Object v)
  {
    if (this == v) return true;
    if (v != null && v instanceof VariableOp) 
      return ((VariableOp)v).name ().equals (name ());
    return false;
  }

  public static final String varName (Expr expr)
  {
    return ((VariableOp)expr.op ()).name ();
  }
  
}
