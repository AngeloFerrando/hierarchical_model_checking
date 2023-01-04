package edu.toronto.cs.expr;

public interface Operator
{
  /**
   * Arrity of this operator
   *
   * @return an <code>int</code> value
   */
  int arity ();
  
  /**
   * true if this operator is well-formed with this arguments
   * provided that the arguments are well formed themselves
   *
   * @param args an <code>Expr[]</code> value
   * @return a <code>boolean</code> value
   */
  boolean isWellFormed (Expr[] args);
  
  /**
   * Name of this operator
   *
   * @return a <code>String</code> value
   */
  String name ();
  
  
}
