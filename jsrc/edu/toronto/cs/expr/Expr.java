package edu.toronto.cs.expr;

import java.util.List;
import java.util.Map;

public interface Expr
{

  public static final Expr[] EMPTY_EXPR_ARRAY = new Expr [0];

  /**
   * Operator of this expression
   *
   * @return an <code>Operator</code> value
   */
  Operator op ();

  /**
   * Unary application
   *
   * @param expr an <code>Expr</code> value
   * @return an <code>Expr</code> value
   */
  Expr unaryApply (Expr expr);

  /**
   * Binary application
   *
   * @param expr an <code>Expr</code> value
   * @param expr an <code>Expr</code> value
   * @return an <code>Expr</code> value
   */
  Expr binApply (Expr expr1, Expr expr2);
  

  /**
   * n-ary application
   *
   * @param expr an <code>Expr[]</code> value
   * @return an <code>Expr</code> value
   */
  Expr naryApply (Expr[] expr);
  
  Expr naryApply (List expr);
  
  /**
   * True arity of this expression, i.e. number of top-level sub-expressions
   * it has
   *
   * @return an <code>int</code> value
   */
  int arity ();
  
  /**
   * returns i'th argument
   * constraint: 0 <= i < arity ()
   *
   * @param i an <code>int</code> value
   * @return an <code>Expr</code> value
   */
  Expr arg (int i);
  
  /**
   * The list of arguments
   *
   * @return a <code>Collection</code> value
   */
  List args ();

  /**
   * Checks if this expression is well formed
   *
   * @return a <code>boolean</code> value
   */
  boolean isWellFormed ();


  /**
   * Substitution of expressions by expressions
   *
   * @param subMap a <code>Map</code> of type Expr -> Expr
   * @return an <code>Expr</code> value
   */
  Expr subst (Map subMap);
  
  /**
   * Substitutes operators in the expression
   *
   * @param subMap a map of type Operator -> Expr
   * mapping operators to be replaced by an expression
   * @return an <code>Expr</code> value
   */
  Expr substOp (Map subMap);

  /**
   * Returns a factory for this expression
   *
   * @return an <code>ExprFactory</code> value
   */
  ExprFactory getFactory ();

  /** 
   * Returns true if this contains e, false otherwise.
   * 
   * @param e 
   * @return 
   */
  boolean contains (Expr e);

/////// common infix operators
//   Expr and (Expr expr);
//   Expr or (Expr expr);
//   Expr not ();
//   Expr impl (Expr expr);
//   Expr iff (Expr expr);
  
//   Expr plus (Expr expr);
//   Expr minus (Expr expr);
//   Expr div (Expr expr);
//   Expr mult (Expr expr);
  
//   Expr bitAnd (Expr expr);
//   Expr bitOr (Expr expr);
//   Expr bitNot ();
  
//   Expr leq (Expr expr);
//   Expr geq (Expr expr);
//   Expr eq (Expr expr);
}
