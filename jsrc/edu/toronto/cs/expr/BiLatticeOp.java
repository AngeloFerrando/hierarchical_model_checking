package edu.toronto.cs.expr;

public class BiLatticeOp extends NamedOp
{
  public static BiLatticeOp TTOP = new BiLatticeOp ("true", 0);
  public static BiLatticeOp TBOT = new BiLatticeOp ("false", 0);
  public static BiLatticeOp ITOP = new BiLatticeOp ("d", 0);
  public static BiLatticeOp IBOT = new BiLatticeOp ("maybe", 0);
  
  public static BiLatticeOp TAND = new BiLatticeOp ("&", -1);
  public static BiLatticeOp TOR = new BiLatticeOp ("|", -1);
  public static BiLatticeOp TNOT = new BiLatticeOp ("!", 1);
  
  public static BiLatticeOp IAND = new BiLatticeOp ("/\\", -1);
  public static BiLatticeOp IOR = new BiLatticeOp ("\\/", -1);
  public static BiLatticeOp INOT = new BiLatticeOp ("-", 1);
  
  public static BiLatticeOp TLEQ = new BiLatticeOp ("<=", 2);
  public static BiLatticeOp TGEQ = new BiLatticeOp (">=", 2);
  
  public static BiLatticeOp ILEQ = new BiLatticeOp ("i<=", 2);
  public static BiLatticeOp IGEQ = new BiLatticeOp ("i>=", 2);

  private BiLatticeOp (String name, int arity)
  {
    super (name, arity);
  }
  
}
