package edu.toronto.cs.expr;

public abstract class NamedOp implements Operator
{
  int arity;
  String name;
  
  public NamedOp (String _name, int _arity)
  {
    name = _name;
    arity = _arity;
  }
  
  public int arity ()
  {
    return arity;
  }
  
  public String name ()
  {
    return name;
  }
  
  public boolean isWellFormed (Expr[] args)
  {
    return arity == -1 || (args != null && args.length == arity);
  }

  public boolean equals (Object o)
  {
    return this == o;
  }  
}
