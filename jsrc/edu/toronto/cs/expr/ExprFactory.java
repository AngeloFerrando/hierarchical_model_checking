package edu.toronto.cs.expr;


public interface ExprFactory
{

  /**
   * returns an integer expression
   *
   * @param n an <code>int</code> value
   * @return an <code>Expr</code> value
   */
  Expr intExpr (int n);
  /**
   * expression with a nullary opertor
   *
   * @param name a <code>String</code> value
   * @return an <code>Expr</code> value
   */
  Expr var (String name);
  
  /**
   * Expression with an arbitrary operator
   *
   * @param op an <code>Opeartor</code> value
   * @return an <code>Expr</code> value
   */
  Expr op (Operator op);

  /**
   * Returns the 'true' constant
   *
   * @return an <code>Expr</code> value
   */
  Expr trueExpr ();

  /**
   * Returns the 'false' constant
   *
   * @return an <code>Expr</code> value
   */
  Expr falseExpr ();

  /**
   * Returns a cannonical version of an expression
   *
   * @param expr an <code>Expr</code> value
   * @return an <code>Expr</code> value
   */
  Expr cannonize (Expr expr);
  

}
