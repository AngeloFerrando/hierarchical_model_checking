package edu.toronto.cs.expr;


public abstract class NullaryOperator implements Operator 
{
  public int arity () 
  {
    return 0;
  }

  public boolean isWellFormed (Expr[] args)
  {
    return args == null || args.length == 0;
  }  
}
