package edu.toronto.cs.expr;

import edu.toronto.cs.util.Primes;

public class RationalOp extends NullaryOperator
{
  int n;
  int d;

  public RationalOp (int _n, int _d)
  {
    n = _n;
    d = _d;
  }
  
  public String name ()
  {
    return n + "/" + d;
  }
  
  public boolean equals (Object v)
  {
    if (this == v) return true;
   
    return 
      v != null && 
      v instanceof RationalOp && 
      ((RationalOp)v).getN () == this.n &&
      ((RationalOp)v).getD () == this.d;
  }

  public int hashCode ()
  {
    long hashCode = Primes.getPrime (0) * n + d;
    return (int) (hashCode >>> 32);
  }

  public int getN ()
  {
    return n;
  }
  public int getD ()
  {
    return d;
  }
  
}
