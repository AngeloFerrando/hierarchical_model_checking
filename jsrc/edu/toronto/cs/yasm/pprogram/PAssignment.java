package edu.toronto.cs.yasm.pprogram;

import edu.toronto.cs.expr.*;

/**
 * PAssignment.java
 *
 *
 * Created: Fri Jun 25 14:29:05 2004
 *
 * @author <a href="mailto:kelvin@tallinn.cs">Kelvin Ku</a>
 * @version 1.0
 */
public class PAssignment 
{
  
  /**
   * Variable being assigned
   *
   */
  Expr var;

  /**
   * A condition under which the variable as assigned 'true'
   *
   */
  Expr tCond;
  
  /**
   * A condition under which the variable is assigned 'false'
   *
   */
  Expr fCond;
  
  public PAssignment() 
  {
    
  } 
  
} 
