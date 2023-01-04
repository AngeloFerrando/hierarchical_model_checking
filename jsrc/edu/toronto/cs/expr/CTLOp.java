package edu.toronto.cs.expr;

public class CTLOp extends NamedOp
{
  // -- next step
  public static CTLOp EX = new CTLOp ("EX", 1);
  public static CTLOp AX = new CTLOp ("AX", 1);

  // -- future
  public static CTLOp EF = new CTLOp ("EF", 1);
  public static CTLOp AF = new CTLOp ("AF", 1);

  // -- globally
  public static CTLOp EG = new CTLOp ("EG", 1);
  public static CTLOp AG = new CTLOp ("AG", 1);

  // -- strong until
  public static CTLOp EU = new CTLOp ("EU", 2);
  public static CTLOp AU = new CTLOp ("AU", 2);

  // -- bounded EU and AU, the first argument is the bound
  public static CTLOp bEU = new CTLOp ("bEU", 3);
  public static CTLOp bAU = new CTLOp ("bAU", 3);

  // -- release
  public static CTLOp ER = new CTLOp ("ER", 2);
  public static CTLOp AR = new CTLOp ("AR", 2);  
  
  public CTLOp (String name, int arity)
  {
    super (name, arity);
  }
  
}
