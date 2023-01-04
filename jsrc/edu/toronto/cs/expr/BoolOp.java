package edu.toronto.cs.expr;

public class BoolOp extends NamedOp 
{
  public static BoolOp TRUE = new BoolOp ("true", 0);
  public static BoolOp FALSE = new BoolOp ("false", 0);
  
  public static BoolOp AND = new BoolOp ("&", -1);
  public static BoolOp OR = new BoolOp ("|", -1);
  public static BoolOp NOT = new BoolOp ("!", 1);
  public static BoolOp IMPL = new BoolOp ("->", 2);
  public static BoolOp IFF = new BoolOp ("<->", 2);
  public static BoolOp LPMI = new BoolOp ("<-", 2);
  
  
  private BoolOp (String _name, int _arity)
  {
    super (_name, _arity);
  }
}
