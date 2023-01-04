package edu.toronto.cs.yasm.pprogram;

import edu.toronto.cs.expr.*;
import java.io.PrintWriter;

/**
 * PAssignment.java
 *
 *
 * Created: Fri Jun 25 14:29:05 2004
 *
 * @author <a href="mailto:kelvin@tallinn.cs">Kelvin Ku</a>
 * @version 1.0
 */
public class AsmtPStmt extends PStmt
{
  
  /**
   * Variable being assigned
   *
   */
  Expr var;

  /**
   * A condition under which the variable is assigned 'true'
   *
   */
  Expr tCond;
  
  /**
   * A condition under which the variable is assigned 'false'
   *
   */
  Expr fCond;
  
  public AsmtPStmt (Expr _var, Expr _tCond, Expr _fCond) 
  {
    var = _var;
    tCond = _tCond;
    fCond = _fCond;
  } 

  public Expr getVar ()
  {
    return var;
  }

  public Expr getTrueCond ()
  {
    return tCond;
  }

  public Expr getFalseCond ()
  {
    return fCond;
  }

  public void setTrueCond (Expr v)
  {
    tCond = v;
  }
  public void setFalseCond (Expr v)
  {
    fCond = v;
  }

  public void printMe (PrintWriter out)
  {
    out.print (var + " := H (" + tCond + ", " + fCond + ")");
  }
  
} 
