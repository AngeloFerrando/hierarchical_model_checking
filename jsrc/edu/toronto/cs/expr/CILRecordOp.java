package edu.toronto.cs.expr;

public class CILRecordOp extends NamedOp
{
  public static final CILRecordOp STRUCT = new CILRecordOp ("struct", -1);
  public static final CILRecordOp UNION  = new CILRecordOp ("union" , -1);

  // -- constructor

  private CILRecordOp (String name, int arity)
  {
    super (name, arity);
  }
}
