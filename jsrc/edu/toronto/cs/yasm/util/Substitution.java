package edu.toronto.cs.yasm.util;

import edu.toronto.cs.expr.*;
import edu.toronto.cs.yasm.abstractor.MemoryModel;

import java.util.*;

public class Substitution
{
  CVCLExprConverter converter;
  ExprFactory fac;
  Map m;

  public Substitution (CVCLExprConverter _converter, ExprFactory _fac, Map _m)
  {
    converter = _converter;
    fac = _fac;
    m = _m;
  } 
  public List convert (List l)
  {
    List newList = new ArrayList ();
    for (Iterator it = l.iterator (); it.hasNext ();)
	newList.add (converter.toCVCL(recConvert ((Expr)it.next ())));
    return newList;
  }

  public Expr recConvert (Expr expr)
  {
    return expr.subst (m);
  }
  
  public Expr __recConvert (Expr e)
  {
    int ar = e.arity ();
    if(ar == 0){
      if(e.op () instanceof VariableOp)
	{
	  System.err.println (((MemoryModel)m).lookup (e));
	  Expr temp = (Expr) ((MemoryModel)m).lookup (e);
	  if(temp==null)
	    throw new RuntimeException ("VarMap incomplete!");
	  e = temp;
	}
      return e;
    }
    else
      {
	Expr[] expr = new Expr [ar];
	for(int k = 0; k<ar; k++)
	  {
	    System.err.println ("before " + e.arg (k));
	    expr[k] = __recConvert (e.arg (k));
	    System.err.println ("after " + expr [k]);
	  }
	return fac.op (e.op ()).naryApply (expr);
    }
  }
}
