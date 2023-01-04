package edu.toronto.cs.expr;


import edu.toronto.cs.tp.cvcl.*;
import java.util.*;

/**
 * CVCLExprConverter.java
 *
 *
 * Created: Thu Jul  1 11:54:27 2004
 *
 * @author <a href="mailto:maxin@epoch.cs">Xin Ma</a>
 * @version 1.0
 */
public class CVCLExprConverter implements JavaCVCConstants
{
  ExprFactory fac;
  ValidityChecker vc;
  Map cvclForwardMap;
  Map cvclBackwardMap;

  public CVCLExprConverter (ExprFactory _fac, ValidityChecker _vc, Map _map)
  {
    vc = _vc;
    fac = _fac;
    CVCLConstantMap m = new CVCLConstantMap ();
    cvclForwardMap = m.getForwardMap ();
    cvclBackwardMap = m.getBackwardMap ();
  }

  public edu.toronto.cs.tp.cvcl.Expr linearize (edu.toronto.cs.tp.cvcl.Expr e)
  {
    ExprAlgebra eA = new ExprAlgebra (fac);
    return toCVCL (eA.moveToLeft (fromCVCL (e)));
  }

  public List toCVCL (List l)
  {
    List temp = new ArrayList();
    for (Iterator it = l.iterator(); it.hasNext();)
      {
	temp.add(toCVCL((edu.toronto.cs.expr.Expr)it.next()));
      }
    return temp;
  }

  private edu.toronto.cs.tp.cvcl.Expr 
    addrOfToCVCL (edu.toronto.cs.expr.Expr expr)
  {
    if (! (expr.arg (0).op () instanceof VariableOp))
      throw new RuntimeException 
	("Only one level of address of is supported: " + expr);

    return vc.varExpr ("__addr_of__" + 
		       VariableOp.varName (expr.arg (0)), 
		       vc.intType ());
    
    
  }

  private edu.toronto.cs.expr.Expr addrOfFromCVCL (String addrOfName)
  {
    assert addrOfName.startsWith ("__addr_of__") 
      : "Not an addrOf expr: " + addrOfName;

    return fac.op (CILIndirectionOp.ADDR_OF).unaryApply 
      (fac.var (addrOfName.substring ("__addr_of__".length ())));
  }
  
  private edu.toronto.cs.tp.cvcl.Expr ptrToCVCL (Expr expr, String prefix)
  {
    if (expr.op () == CILIndirectionOp.DEREF)
      return ptrToCVCL (expr.arg (0), "__ptr__");


    return vc.varExpr (prefix + VariableOp.varName (expr), 
		       vc.intType ());
  }

  private edu.toronto.cs.expr.Expr ptrFromCVCL (String ptrName)
  {
    if (ptrName.startsWith ("__ptr__"))
      return 
	fac.op (CILIndirectionOp.DEREF).
	unaryApply (ptrFromCVCL (ptrName.substring ("__ptr__".length ())));
    return fac.var (ptrName);
  }
  


  private String indirectionOpToString (Expr expr)
  {
    if (expr.op () == CILIndirectionOp.DEREF)
      return "__ptr__" + indirectionOpToString (expr.arg (0));
    if (expr.op () == CILIndirectionOp.ADDR_OF)
      return "__addr_of__" + indirectionOpToString (expr.arg (0));
    if (expr.op () == CILIndirectionOp.ARROW)
      return indirectionOpToString (expr.arg (0)) + 
	"->" + indirectionOpToString (expr.arg (1));
    if (expr.op () == CILIndirectionOp.DOT)
      return indirectionOpToString (expr.arg (0)) + 
	"." + indirectionOpToString (expr.arg (1));
    if (expr.op () instanceof VariableOp)
      return VariableOp.varName (expr);
    throw new RuntimeException ("Not an indirection op: " + expr);
  }

  private edu.toronto.cs.expr.Expr indirectionOpFromString (String name)
  {
    if (name.startsWith ("__ptr__"))
      return 
	fac.op (CILIndirectionOp.DEREF).unaryApply
	(indirectionOpFromString (name.substring ("__ptr__".length ())));
    if (name.startsWith ("__addr_of__"))
      return 
	fac.op (CILIndirectionOp.ADDR_OF).unaryApply 
	(indirectionOpFromString (name.substring ("__addr_of__".length ())));
    
    int dotIdx = name.indexOf (".");
    int arrowIdx = name.indexOf ("->");
    
    if (dotIdx == -1 && arrowIdx == -1)
      return fac.var (name);
    
    Operator op;
    edu.toronto.cs.expr.Expr lhs;
    edu.toronto.cs.expr.Expr rhs;
    
    if (dotIdx != -1 && (arrowIdx == -1 || dotIdx < arrowIdx))
      {
	op = CILIndirectionOp.DOT;
	lhs = indirectionOpFromString (name.substring (0, dotIdx));
	rhs = indirectionOpFromString (name.substring (dotIdx + 1));
      }
    else 
      {
	op = CILIndirectionOp.ARROW;
	lhs = indirectionOpFromString (name.substring (0, arrowIdx));
	rhs = indirectionOpFromString (name.substring (arrowIdx + 2));
      }
    
    
    return fac.op (op).binApply (lhs, rhs);
  }
  
  
  
  public edu.toronto.cs.tp.cvcl.Expr toCVCL (edu.toronto.cs.expr.Expr expr)
  {
    // System.err.println ("toCVCL: " + expr);

    assert expr != null : "null expr";
    assert expr.op () != null : "null expr op: " + expr;

    if (expr.op () instanceof CILIndirectionOp)
      return vc.varExpr (indirectionOpToString (expr), 
          vc.intType ());

    //assert vc.scopeLevel () == 1;
    if (expr.op () instanceof VariableOp)
      return vc.lookupVar (((VariableOp) expr.op ()).name (), new Type ());
    //assert vc.scopeLevel () == 1;
    if (expr.arity () == 0)
    {
      if (expr.op () == BoolOp.TRUE)
        return vc.trueExpr ();
      else if (expr.op () == BoolOp.FALSE)
        return vc.falseExpr ();
      else if (expr.op () instanceof JavaObjectOp &&
          ((JavaObjectOp)expr.op ()).getObject () instanceof Integer)
        return vc.ratExpr 
          (((Integer)((JavaObjectOp)expr.op ()).getObject ()).intValue (), 1);
      else if (expr.op () instanceof RationalOp)
        return vc.ratExpr (((RationalOp)expr.op ()).getN(), 
            ((RationalOp)expr.op ()).getD());
    }
    else if (expr.arity() == 1)
    {
      //         if (expr.op () == CILIndirectionOp.ADDR_OF)
      //           return addrOfToCVCL (expr);
      // 	if (expr.op () == CILIndirectionOp.DEREF)
      // 	  {
      // 	    return ptrToCVCL (expr, "");
      // 	  }


      Integer kind = (Integer)cvclBackwardMap.get(expr.op ());
      assert kind != null : "Cannot convert: " + expr;
      return new edu.toronto.cs.tp.cvcl.Expr 
        (vc.getEM (), kind.intValue (), toCVCL(expr.arg(0)));
    }
    else if (expr.arity () == 2)
    {
      //         if (expr.op () == CILIndirectionOp.ARROW ||
      //             expr.op () == CILIndirectionOp.DOT)
      //         {
      //           return toCVCL (expr.arg (0));
      //         } 

      assert expr.arg (0) != null : "null lhs: " + expr;
      assert expr.arg (1) != null : "null rhs: " + expr;

      edu.toronto.cs.tp.cvcl.Expr lhs = toCVCL (expr.arg (0));
      edu.toronto.cs.tp.cvcl.Expr rhs = toCVCL (expr.arg (1));




      Integer kind = (Integer)cvclBackwardMap.get(expr.op ());
      assert kind != null : "Op unknown to CVCL: " + expr;
      // System.err.println ("building Expr: kind: " + kind + ", lhs: " + lhs + ", rhs: " + rhs);
      return new edu.toronto.cs.tp.cvcl.Expr 
        (vc.getEM (), kind.intValue (), lhs, rhs);

    }
    else if (expr.arity () > 2)
    {
      CVectorExpr kids = new CVectorExpr (0);
      for (int i = 0; i < expr.arity (); i++)
        kids.add (toCVCL (expr.arg (i)));

      Integer kind = (Integer) cvclBackwardMap.get(expr.op ());
      assert kind != null : "Op unknown to CVCL: " + expr;
      return new edu.toronto.cs.tp.cvcl.Expr 
        (vc.getEM (), kind.intValue (), kids);
    }

    throw new UnsupportedOperationException ("does not work yet: " + expr);
  }


  public edu.toronto.cs.expr.Expr fromCVCL (edu.toronto.cs.tp.cvcl.Expr expr) 
  {
    if (expr.arity() ==0)
      {
	if (expr.isFalse())
	  {
	    return fac.op (BoolOp.FALSE);
	  }
	else if (expr.isTrue())
	  {
	    return fac.op (BoolOp.TRUE);
	  }
	if (expr.isRational())
	  {
	    if (expr.getRational().isInteger())
	      return fac.op (new JavaObjectOp 
			     (new Integer (expr.toString ())));
	    else
	      return fac.op 
		(new RationalOp 
		 (expr.getRational ().getNumerator ().getInt (),
		  expr.getRational ().getDenominator ().getInt ()));
	  }
	else if (expr.isVar ())
	  {
	    String varName = expr.toString ();
	    return indirectionOpFromString (varName);
	  }
	
      }
    else if (expr.arity () == 1)
      {
	if (expr.getKind () == AND)
	  return fromCVCL (expr.get (0));
	else if (expr.getKind () == OR)
	  return fromCVCL (expr.get (0));
	// if (expr.isNot())
	// return fac.op (BoolOp.NOT).unaryApply(fromCVCL(expr.get(0)));
	Operator op = (Operator)(cvclForwardMap.get 
				 (new Integer (expr.getKind ())));
	assert op != null;
	return fac.op (op).unaryApply (fromCVCL (expr.get (0)));
      }
    else if (expr.arity () == 2)
      {
	edu.toronto.cs.expr.Expr lhs = fromCVCL (expr.get (0));
	edu.toronto.cs.expr.Expr rhs = fromCVCL (expr.get (1));

	
	
	Operator op = (Operator)(cvclForwardMap.get 
				 (new Integer (expr.getKind ())));
	assert op != null;
	return fac.op (op).binApply(lhs, rhs);
      }
    else if (expr.arity () > 2)
      {
	Expr[] gExpr = new Expr[expr.arity ()];
	for (int i = 0; i < expr.arity (); i++)
	    gExpr[i] = fromCVCL (expr.get (i));
	Operator op = (Operator)(cvclForwardMap.get 
				 (new Integer (expr.getKind ())));
	assert op != null;
	return fac.op (op).naryApply (gExpr);
	
      }
    throw new UnsupportedOperationException ("Not here yet: "  + expr);
    
  }

  public static void main (String[] args)
  {
    ValidityChecker vc = CVCLUtil.newValidityChecker ();
    ExprFactory fac = new ExprFactoryImpl ();


    edu.toronto.cs.expr.Expr x = fac.var ("x");
    edu.toronto.cs.expr.Expr y = fac.var ("y");
    edu.toronto.cs.expr.Expr xGeqy = 
      fac.op (ComparisonOp.GEQ).binApply (x, y);

    System.out.println (xGeqy);

    Map map = new HashMap ();
    map.put (x, vc.varExpr (x.op ().name (), vc.intType ()));
    map.put (y, vc.varExpr (y.op ().name (), vc.intType ()));
    
    CVCLExprConverter converter = new CVCLExprConverter (fac, vc, map);
   
    System.out.println ("arity of x is " + converter.toCVCL (x).arity ()); 
    System.out.println (vc.query (converter.toCVCL (xGeqy), false));

    System.out.println (converter.fromCVCL (converter.toCVCL (xGeqy)));

    edu.toronto.cs.expr.Expr ptrX = 
      fac.op (CILIndirectionOp.DEREF).unaryApply (x);

    edu.toronto.cs.expr.Expr addrX = 
      fac.op (CILIndirectionOp.ADDR_OF).unaryApply (x);
    

    edu.toronto.cs.expr.Expr xArrowY = 
      fac.op (CILIndirectionOp.ARROW).binApply (x, y);
    edu.toronto.cs.expr.Expr xDotY = 
      fac.op (CILIndirectionOp.DOT).binApply (x, y);
    
    System.out.println (ptrX + " is " + converter.toCVCL (ptrX));
    System.out.println (converter.fromCVCL (converter.toCVCL (ptrX)));

    System.out.println (addrX + " is " + converter.toCVCL (addrX));
    System.out.println (converter.fromCVCL (converter.toCVCL (addrX)));

    System.out.println (xArrowY + " is " + converter.toCVCL (xArrowY));
    System.out.println (converter.fromCVCL (converter.toCVCL (xArrowY)));

    System.out.println (xDotY + " is " + converter.toCVCL (xDotY));
    System.out.println (converter.fromCVCL (converter.toCVCL (xDotY)));
    
    
  }
  
} 
