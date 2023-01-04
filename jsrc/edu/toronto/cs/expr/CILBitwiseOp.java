package edu.toronto.cs.expr;

public class CILBitwiseOp extends NamedOp
{
  public static final CILBitwiseOp BAND = new CILBitwiseOp ("&", 2);
  public static final CILBitwiseOp BOR = new CILBitwiseOp ("|", 2);
  public static final CILBitwiseOp BXOR = new CILBitwiseOp ("^", 2);
  public static final CILBitwiseOp LSHIFT = new CILBitwiseOp ("<<", 2);
  public static final CILBitwiseOp RSHIFT = new CILBitwiseOp (">>", 2);

  private CILBitwiseOp (String name, int arity)
  {
    super (name, arity);
  }
}
