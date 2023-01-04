package edu.toronto.cs.expr;

import java.util.*;

public class CILListOp extends NamedOp
{
  public static final CILListOp LIST = new CILListOp ("list", -1);
  public static final CILListOp SLIST = new CILListOp ("statementList", -1);

  // -- constructors
  private CILListOp (String name, int arity)
  {
    super (name, -1);
  }

  /** 
   * Appends the operands of e2 to those of e1 and returns the resulting
   * expression.
   * 
   * @param e1 the expression to be appended to
   * @param e2 the expression to be appended
   * @return an <code>Expr</code> value 
   */
  public static Expr append (Expr e1, Expr e2)
  {
    /* why not allow this to be used in general
    assert    e1.op () == LIST || e1.op () == CILNullOp.NULL
           && e2.op () == LIST || e2.op () == CILNullOp.NULL;
    */

    if (e1.op () == CILNullOp.NULL)
      return e2;       

    if (e2.op () == CILNullOp.NULL)
      return e1;       

    // (e1.args ()).addAll (e2.args ()); // XXX doesn't work for some reason
                                         //     class casting errors and
                                         //     whatnot

    Expr [] operands = new Expr [e1.arity () + e2.arity ()];

    for (int i = 0; i < e1.arity (); i++)
      operands [i] = e1.arg (i);

    for (int i = 0; i < e2.arity (); i++)
      operands [i + e1.arity ()] = e2.arg (i);

    return e1.getFactory ().op (CILListOp.LIST).naryApply (operands);
  }
}
