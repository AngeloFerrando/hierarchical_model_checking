package edu.toronto.cs.tp.cvcl.test;

import edu.toronto.cs.tp.cvcl.*;


/**
 * ExceptionTest.java
 *
 *
 * Created: Thu Jul  1 10:53:53 2004
 *
 * @author <a href="mailto:arie@cs.toronto.edu">Arie Gurfinkel</a>
 * @version 1.0
 */
public class ExceptionTest 
{
  public static void main (String[] args)
  {
    ValidityChecker vc = CVCLUtil.newValidityChecker ();
    
    Expr one = vc.ratExpr (1, 0);
    Expr two = vc.ratExpr (2, 1);
    System.out.println (vc.andExpr (one, two));
    
    System.out.println ("Done");
    
  }
  
  
} // ExceptionTest
