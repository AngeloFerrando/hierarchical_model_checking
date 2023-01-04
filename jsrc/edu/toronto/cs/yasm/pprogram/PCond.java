package edu.toronto.cs.yasm.pprogram;

import edu.toronto.cs.cparser.block.*;
import edu.toronto.cs.expr.*;
import edu.toronto.cs.yasm.abstractor.SymbolicExecutorUtil;

/**
 * PCond.java
 *
 *
 * Created: Mon Jul  5 12:26:11 2004
 *
 * @author <a href="mailto:kelvin@tallinn.cs">Kelvin Ku</a>
 * @version 1.0
 */
public class PCond 
{
  Block sourceBlock;

  // -- original unabstracted condition
  Expr origCond;

  // -- current abstraction of the condition
  Expr cond;

  // -- refiner information
  Object info;

  public PCond (Block _sourceBlock, Expr _cond) 
  {
    sourceBlock = _sourceBlock;
    cond = _cond;
  }

  public PCond (Expr _origCond, Expr _cond)
  {
    origCond = fromCBool (_origCond);
    // System.err.println ("PCond: origCond: " + origCond);
    cond = _cond;
  }

  public Expr fromCBool (Expr cExpr)
  {
    if (cExpr == null)
      return null;

    ExprFactory fac = cExpr.getFactory ();

    if (cExpr.op () instanceof BoolOp)
    {
      // -- eliminate double negation
      if (cExpr.op () == BoolOp.NOT)
      {
        Expr childCBool = fromCBool (cExpr.arg (0));
	
	if (childCBool == null) return null;

        if (childCBool.op () == BoolOp.NOT)
          return childCBool.arg (0);
        
        return fac.op (BoolOp.NOT).unaryApply (childCBool);  
      }

      Expr [] kids = new Expr [cExpr.arity ()];
      for (int i = 0; i < cExpr.arity (); i++)
	{
	  kids [i] = fromCBool (cExpr.arg (i));
	  if (kids [i] == null) return null;
	}
      
      return fac.op (cExpr.op ()).naryApply (kids);
    }

    if (cExpr.op () instanceof ComparisonOp)
      // -- do nothing
      return cExpr;

    if (cExpr.op () instanceof JavaObjectOp)
    {
      JavaObjectOp jOp = (JavaObjectOp) cExpr.op ();
      if (jOp.getObject () instanceof Integer)
        return ((Integer) jOp.getObject ()).intValue () == 0 ?
               fac.falseExpr () : fac.trueExpr ();
    }

    return fac.op (BoolOp.NOT).
        unaryApply
          (fac.op (ComparisonOp.EQ).
            binApply (cExpr, fac.intExpr (0)));
  }

  public Expr getCond ()
  {
    return cond;
  }

  public Block getSourceCond ()
  {
    return sourceBlock;
  }

  public void setCond (Expr e)
  {
    cond = e;
  }

  public Expr getOrigCond ()
  {
    return origCond;
  }

  public void setOrigCond (Expr v)
  {
    origCond = v;
  }

  public void setRefinerInfo (Object o)
  {
    assert false : "Should not be called";
    info = o;
  }

  public Object getRefinerInfo ()
  {
    assert false : "Should not be called";
    return info;
  }

  public void computeExpr (ExprFactory fac)
  {
    if (sourceBlock == null)
      return;

    assert sourceBlock != null || origCond != null
      : "Both sourceBlock and (Expr) origCond are null";

    // only have to do this if this came from a Block

    if (sourceBlock != null)
      setOrigCond (SymbolicExecutorUtil.parseBlockExpr (sourceBlock, fac));

    if (getOrigCond () == null)
      System.err.println ("Failed parsing if-condition");  
  }

  public String toString ()
  {
    return
      "cond: " + (cond == null ? "null" : cond.toString ()) +
	  ", origCond: " + (origCond == null ? "null" : origCond.toString ());
  }
  
}

