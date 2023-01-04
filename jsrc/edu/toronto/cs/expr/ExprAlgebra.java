package edu.toronto.cs.expr;


import edu.toronto.cs.tp.cvcl.*;
import java.util.*;



/**
 * Describe class <code>ExprAlgebra</code> here.
 *
 * @author <a href="mailto:maxin@epoch.cs">Xin Ma</a>
 * @version 1.0
 */
public class ExprAlgebra implements JavaCVCConstants
{
  ExprFactory fac;
  public ExprAlgebra (ExprFactory _fac)
  {
    fac = _fac;
  }
  public edu.toronto.cs.expr.Expr linearize (edu.toronto.cs.expr.Expr e, int sign)
  {
    System.out.println (e);
    System.out.println (e.arity());
    switch (e.arity())
      {
      case 0:
	break;
      case 1:
	if (sign == 0)
	  return e;
	break;
      case 2:
	System.out.println (e.op ().name ());
	if (e.arg (1).op () instanceof JavaObjectOp &&
	    ((JavaObjectOp)e.arg(1).op ()).getObject () instanceof Integer && 
	    ((Integer)((JavaObjectOp)e.arg(1).op ()).getObject ()).intValue() == 0 )
	  return fac.op (e.op ()).binApply (linearize (e.arg (0), sign), e.arg (1));
	else if (e.op () instanceof ComparisonOp)
	  return linearize (fac.op(e.op ()).binApply (fac.op (NumericOp.MINUS).binApply (e.arg(0), e.arg(1)), fac.intExpr(0)), sign);
	else if (e.op () instanceof NumericOp)
	  {
	    if (e.op () == NumericOp.MINUS)
	      return fac.op (e.op ()).binApply (linearize (e.arg (0), 1), linearize (e.arg (1), 1));
	    if (e.op () == NumericOp.PLUS)
	      {
	      }
	  }
	else break;
      default:
      }
    throw new UnsupportedOperationException ("does not work yet: " + e);
  }
  public edu.toronto.cs.expr.Expr moveToLeft (edu.toronto.cs.expr.Expr e)
  {
    switch (e.arity())
      {
      case 2:
	if (e.op () instanceof ComparisonOp)
	  {
	    return fac.op(e.op ()).binApply (fac.op (NumericOp.MINUS).binApply (e.arg(0), e.arg(1)), fac.intExpr(0));
	  }
	break;
      default:
	throw new UnsupportedOperationException ("does not work yet: " + e);
      }
    return null;
  }
}
