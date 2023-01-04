package edu.toronto.cs.expr;

import java.util.*;

import edu.toronto.cs.util.SoftSoftHashMap;
import edu.toronto.cs.util.Primes;


public class ExprFactoryImpl implements ExprFactory
{

  Map unique;

  public ExprFactoryImpl ()
  {
    unique = new SoftSoftHashMap ();

  }

  public Expr intExpr (int num)
  {
    return op (new JavaObjectOp (new Integer (num)));
  }
  public Expr ratExpr (int n, int d)
  {
    // -- alternative, if creator method is defined in RationalOp:
    // return RationalOp.ratExpr (this, n, d);
    return op (new RationalOp (n, d));
  }

  public Expr trueExpr ()
  {
    return op (BoolOp.TRUE);
  }
  public Expr falseExpr ()
  {
    return op (BoolOp.FALSE);
  }
  public Expr var (String name)
  {
    return op (new VariableOp (name));
  }


  public Expr op (Operator op)
  {
    Expr expr = new ExprImpl (op);
    // -- Nullary expressions are cannonized
    if (op.arity () == 0) 
      expr = cannonize (expr);
    return expr;
  }


  public Expr cannonize (Expr expr)
  {
    Expr u = (Expr)unique.get (expr);
    if (u != null) return u;

    unique.put (expr, expr);
    return expr;
  }



  class ExprImpl extends AbstractExpr
  {
    Operator op;
    Expr[] args;


    public ExprImpl (Operator _op)
    {
      op = _op;
      args = EMPTY_EXPR_ARRAY;
    }


    public Operator op ()
    {
      return op;
    }

    public Expr nullaryApply ()
    {
      return naryApply (new Expr [0]);
    }

    public Expr unaryApply (Expr expr)
    {
      return naryApply (new Expr[] {expr});
    }

    public Expr binApply (Expr expr1, Expr expr2)
    {
      return naryApply (new Expr[] {expr1, expr2});
    }
    public Expr naryApply (Expr[] expr)
    {
      args = expr;
      //return this;
      // -- applying an operator cannonizes it
      return cannonize (this);
    }
    public Expr naryApply (List expr)
    {
      return naryApply ((Expr[]) expr.toArray (new Expr [expr.size ()]));
    }


    public int arity ()
    {
      if (args == null) return 0;
      return args.length;
    }

    public Expr arg (int i)
    {
      return args [i];
    }
    public List args ()
    {
      return Arrays.asList (args);
    }

    public boolean isWellFormed ()
    {

      if (op.isWellFormed (args))
      {
        for (int i = 0; i < arity (); i++)
          if (!arg (i).isWellFormed ()) return false;
        return true;
      }
      return false;
    }


    public Expr subst (Map subMap)
    {
      // -- if current expression is in the substitution map, 
      // -- return the map entry
      if (subMap.containsKey (this))
        return (Expr) subMap.get (this);

      if (arity () == 0) return this;

      // -- otherwise apply substitution to children


      Expr[] kids = new Expr [args.length];
      for (int i = 0; i < kids.length; i++)
        kids [i] = args [i].subst (subMap);

      return ExprFactoryImpl.this.op (op ()).naryApply (kids);
    }

    public Expr substOp (Map subMap)
    {

      // -- get the operator from the map, can be null if 
      // -- this operator should not be replaced
      Expr newOp = (Expr) subMap.get (op ());

      // -- bail out quickly on nullary expressions
      if (arity () == 0)
        return newOp == null ? this : newOp;

      // -- if newOp is null, duplicate the current operator
      if (newOp == null)
        newOp = ExprFactoryImpl.this.op (op ());      

      // -- recursively apply substOp to arguments
      Expr[] kids = new Expr [args.length];
      for (int i = 0; i < kids.length; i++)
        kids [i] = args [i].substOp (subMap);

      // -- apply the new operator to the arguments and return
      return newOp.naryApply (kids);
    }



    public boolean equals (Object v)
    {
      if (this == v) return true;
      if (v == null) return false;
      if (v instanceof ExprImpl)
        return equals ((ExprImpl) v, true);

      return false;
    }


    public boolean equals (ExprImpl expr, boolean identity)
    {
      if (op.equals (expr.op ()) && arity () == expr.arity ())
      {
        for (int i = 0; i < arity (); i++)
        {
          if (!identity && !args [i].equals (expr.arg (i)))
            return false;
          else if (identity && args [i] != expr.arg (i))
            return false;
        }
        return true;
      }
      return false;
    }


    public ExprFactory getFactory ()
    {
      return ExprFactoryImpl.this;
    }


    private int hashBound = 3;
    public int hashCode ()
    {
      // -- Compute the hash code for this expression. 
      // -- this assumes that expressions are immutable so hashCode of this
      // -- expression depeneds on its local information and identity hashcode
      // -- of its children
      long hashCode = op.hashCode () * Primes.primes [0];

      for (int i = 0; i < arity () && i < hashBound; i++)
        hashCode += 
          System.identityHashCode (args [i]) * Primes.getPrime (i + 1);

      return (int) (hashCode >>> 32);
    }


    public String toString ()
    {
      /*
      if (arity () == 2)
        return "(" + arg (0).toString() + op.name () + arg (1).toString () + ")";
      if (arity () == 1)
        return op.name () + args ();
      if (arity () == 0)
        return op.name ();
      if (arity () > 0)
        return "(" + op.name () + " " + args () + ")";
      return op.name ();
      */

      return toIndentedString (0);
    }

    private String toIndentedString (int depth)
    {
      String s = "";

      // -- indentation
      for (int i = 0; i < depth; i++)
        s += "  ";

      if (op.arity () == 2 ||
          (op == BoolOp.AND && arity () == 2) ||
          (op == BoolOp.OR && arity () == 2) ||
          (op == NumericOp.PLUS && arity () == 2))
      {
        /*
        assert (arg (0) != null);
        assert (arg (0).toString () != null);
        assert (arg (1) != null);
        assert (arg (1).toString () != null);
        assert (op != null);
        assert (op.name () != null);
        */
        return s + "(" +
               (arg (0) == null ? "null" : arg (0).toString ()) +
               " " + op.name () + " " +
               (arg (1) == null ? "null" : arg (1).toString ()) +
               ")";
      }
      
      if (op.arity () == 1)
        return s + op.name () + " " +
               (arg (0) == null ? "null" : arg (0).toString ());

      if (op.arity () == 0)
        return s + op.name ();

      // -- root label
      s += op.name () + "\n";

      // -- add children with additional indentation
      for (Iterator it = args ().iterator (); it.hasNext ();)
      {
        ExprImpl ei = (ExprImpl) it.next ();
        if (ei != null)
          s += ei.toIndentedString (depth + 1) + "\n";
        else // ei == null
        {
          for (int i = 0; i < depth + 1; i++)
            s += "  ";
          s += "null\n";
        }
      }

      return s;  
    }
   
  }

  /**
   * test code
   *
   * @param args a <code>String[]</code> value
   */
  public static void main (String[] args)
  {
    ExprFactory fac = new ExprFactoryImpl ();

    Expr x = fac.var ("x");
    Expr y = fac.var ("y");
    Expr z = fac.var ("z");

    System.out.println ("x is: " + x);
    System.out.println ("x is x: " + (x == fac.var ("x")));

    Expr exp = fac.op (ComparisonOp.EQ).binApply (x, y);
    //exp = fac.op (ComparisonOp.EQ).binApply (z, exp);

    System.out.println ("exp contains itself " + exp.contains (exp));
    System.out.println ("exp contains x " + exp.contains (x));
    System.out.println ("exp contains z " + exp.contains (z));

    System.out.println ("x = y: " + exp);

    Map subMap = new java.util.HashMap ();
    subMap.put (x, z);
    System.out.println ("z = y: " + exp.subst (subMap));
  }
}

