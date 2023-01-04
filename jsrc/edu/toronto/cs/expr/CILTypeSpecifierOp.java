package edu.toronto.cs.expr;

public class CILTypeSpecifierOp extends NamedOp
{

  // qualifiers are included here too (e.g. const)

  public static final CILTypeSpecifierOp
    VOID     = new CILTypeSpecifierOp ("void"),
    CHAR     = new CILTypeSpecifierOp ("char"),
    SHORT    = new CILTypeSpecifierOp ("short"),
    INT      = new CILTypeSpecifierOp ("int"),
    LONG     = new CILTypeSpecifierOp ("long"),
    FLOAT    = new CILTypeSpecifierOp ("float"),
    DOUBLE   = new CILTypeSpecifierOp ("double"),
    SIGNED   = new CILTypeSpecifierOp ("signed"),
    UNSIGNED = new CILTypeSpecifierOp ("unsigned"),
    CONST    = new CILTypeSpecifierOp ("const");

  // -- constructor

  private CILTypeSpecifierOp (String name)
  {
    super (name, 0);
  }

  private CILTypeSpecifierOp (String name, int arity)
  {
    super (name, arity);
  }
}
