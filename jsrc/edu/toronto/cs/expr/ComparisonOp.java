package edu.toronto.cs.expr;


public class ComparisonOp extends NamedOp
{
  public static ComparisonOp EQ = new ComparisonOp ("=", 2);
  public static ComparisonOp NEQ = new ComparisonOp ("!=", 2);
  public static ComparisonOp LEQ = new ComparisonOp ("<=", 2);
  public static ComparisonOp GEQ = new ComparisonOp (">=", 2);
  public static ComparisonOp GT = new ComparisonOp(">", 2);
  public static ComparisonOp LT = new ComparisonOp("<", 2);
 
  private ComparisonOp (String name, int arity)
  {
    super (name, arity);
  }
  
}
