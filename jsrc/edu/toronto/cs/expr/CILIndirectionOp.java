package edu.toronto.cs.expr;

public class CILIndirectionOp extends NamedOp
{
  public static final CILIndirectionOp
    DEREF   = new CILIndirectionOp ("*", 1),
    DOT     = new CILIndirectionOp (".", 2),
    ARROW   = new CILIndirectionOp ("->", 2),
    ADDR_OF = new CILIndirectionOp ("&", 1);

  private CILIndirectionOp (String name, int arity)
  {
    super (name, arity);
  }
}
