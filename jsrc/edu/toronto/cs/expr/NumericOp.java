package edu.toronto.cs.expr;

public class NumericOp extends NamedOp
{
  public static final NumericOp PLUS = new NumericOp ("+", -1);
  public static final NumericOp MINUS = new NumericOp ("-", 2);
  public static final NumericOp UN_MINUS = new NumericOp ("-", 1);
  public static final NumericOp MULT = new NumericOp ("*", 2);
  public static final NumericOp DIV = new NumericOp ("/", 2);
  public static final NumericOp MOD = new NumericOp ("mod", 2);
  
  private NumericOp (String name, int arity)
  {
    super (name, arity);
  }
  
}
