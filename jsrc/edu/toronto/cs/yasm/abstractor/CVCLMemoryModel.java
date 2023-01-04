package edu.toronto.cs.yasm.abstractor;

import edu.toronto.cs.util.*;
import edu.toronto.cs.expr.*;

import java.util.HashMap;

/**
 * CVCLMemoryModel.java
 *
 *
 * Created: Mon Jul  5 13:00:17 2004
 *
 * @author <a href="mailto:kelvin@tallinn.cs">Kelvin Ku</a>
 * @version 1.0
 */
public class CVCLMemoryModel extends HashMap
{
  MemoryModel mm;
  ExprFactory fac;
  CVCLExprConverter cvcl;
  
  public CVCLMemoryModel (MemoryModel _mm, ExprFactory _fac, 
			  CVCLExprConverter _cvcl) 
  {
    super ();
    mm = _mm;
    fac = _fac;
    cvcl = _cvcl;
  } 

  public Object get (Object o)
  {
    return cvcl.toCVCL ((Expr) mm.get (fac.var ((String) o)));
  }
  
  public String toString ()
  {
    return mm.toString ();
  }
  
  // CVCLMemoryModel constructor
  
} // CVCLMemoryModel
