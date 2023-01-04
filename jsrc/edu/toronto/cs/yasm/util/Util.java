package edu.toronto.cs.yasm.util;

import edu.toronto.cs.tp.cvcl.*;
import java.util.*;
import edu.toronto.cs.yasm.abstractor.PredicateRefiner;
public class Util
{
  public static ValidityChecker vc2 = CVCLUtil.newValidityChecker();

  public static CVectorExpr cleanVector (CVectorExpr v)
  {
    CVectorExpr temp = new CVectorExpr (0);
    for (int i = 1; i < v.size (); i++)
      {
	temp.add (v.get (i));
      }
    return temp;
	   
  }
  public static void printVector (CVectorExpr e)
  {
    for (int i = 0; i < e.size (); i++)
      System.err.println (e.get (i));
  }
  public static void printList (List l)
  {
    for(Iterator it = l.iterator(); it.hasNext();)
      System.err.println ((Expr)it.next());
  }
  private static void substituteVars (Expr e, Map varMap, Map m)
  {
    if ((Boolean)m.get(e.toString())==Boolean.TRUE)
	return;
    int ar = e.arity ();
    
    if(ar == 0){
      if(e.isVar()){
	Expr temp = (Expr) varMap.get(e.toString());
	if(temp==null)
	  return;
	e.assign(temp);
	m.put(e.toString(), Boolean.TRUE);
      }
      return;
    }
    if(ar>0){
      for(int k = 0; k<ar; k++){
	substituteVars(e.get(k), varMap, m);
      }
    }
    
  }
  public static void substituteVars (Expr e, Map varMap)
  {
    Map m = new HashMap();
    for (Iterator it = varMap.keySet().iterator(); it.hasNext();)
      {
	m.put(it.next(), Boolean.FALSE);
      }
    substituteVars (e, varMap, m);
  }
  public static Expr preSubstitution(Expr e)
  {
    ExprManager em2 = vc2.getEM();
    return em2.rebuildExpr(e);
  }

  public static boolean belong (Expr e, List l)
  {
    for (Iterator it = l.iterator(); it.hasNext();)
      {
	if (e.equal(e, (Expr)it.next()))
	  return true;
      }
    return false;
  }

  public static CVectorExpr fListToVector (List l)
  {
    CVectorExpr v = new CVectorExpr (0);
    for (int i = 0; i < l.size (); i++)
	v.add ((Expr)l.get (i));
    return v;
  }
  public static List fVectorToList (CVectorExpr v)
  {
    List l = new ArrayList();
    for (int i = 0; i < v.size (); i++)
	l.add (v.get (i));
    return l;
  }
  public static int findIndex (Expr e, List l)
  {
    for (int i = 0; i < l.size(); i++)
      {
	if (((Expr)l.get (i)).isNot ())
	  {
	    if (e.equal(e, ((Expr)l.get(i)).get(0)))
	      return i;
	  }
	else
	  if (e.equal(e, (Expr)l.get(i)))
	    return i;
      } 
    return -1;
  }
}
