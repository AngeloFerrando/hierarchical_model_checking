package edu.toronto.cs.yasm.pprogram;

import java.io.PrintWriter;
import java.io.OutputStreamWriter;
import java.io.StringWriter;

import edu.toronto.cs.expr.Expr;
import edu.toronto.cs.cparser.block.*;

import edu.toronto.cs.expr.*;

/**
 * PStmt.java
 *
 * A statement of a PProgram
 *
 * Created: Fri Jun 25 14:17:56 2004
 *
 * @author <a href="mailto:kelvin@tallinn.cs">Kelvin Ku</a>
 * @version 1.0
 */

public class PStmt 
{
  int id = -1;
  String label = null;

  PStmt next;
  PStmt dest; // CFG dest

  Block sourceBlock;
  Expr sourceExpr;

  Object info;

  // -- PFunctionDef this statement belongs to
  PFunctionDef parent;

  public PStmt () 
  {

  }

  public PStmt (Block _sourceBlock)
  {
    sourceBlock = _sourceBlock;
  }

  public PStmt (Expr _sourceExpr)
  {
    sourceExpr = _sourceExpr;
  }

  public PStmt (PFunctionDef _parent, Block _sourceBlock)
  {
    parent = _parent;
    sourceBlock = _sourceBlock;
  }

  public PStmt (PFunctionDef _parent, Expr _sourceExpr)
  {
    parent = _parent;
    sourceExpr = _sourceExpr;
  }

  public int getId ()
  {
    return id;
  }

  public void setId (int v)
  {
    id = v;
  }

  public PStmt getNext ()
  {
    return next;
  }

  public void setNext (PStmt _next)
  {
    assert _next != this : "Creating a LOOP in the AST";
    next = _next;
  }

  public void setDest (PStmt v)
  {
    assert !(v instanceof FunctionCallPStmt)
      : "trying to setDest to FunctionCallPStmt " + v;

    dest = v;
  }

  public PStmt getDest ()
  {
    // XXX Hack so that PProgramCompiler can always look at dest
    return dest != null ? dest : next;
  }

  public void setTail (PStmt tail)
  {
    if (next == null)
      setNext (tail);
    else
      getNext ().setTail (tail);
  }

  public PStmt getLast ()
  {
    if (getNext () == null)
      return this;
      
    return getNext ().getLast ();  
  }

  public void setLabel (String _label)
  {
    label = _label;
  }

  public String getLabel ()
  {
    return label;
  }

  public Block getSourceBlock ()
  {
    return sourceBlock;
  }

  public Expr getSourceExpr ()
  {
    return sourceExpr;
  }

  public void setSourceBlock (Block _sourceBlock)
  {
    sourceBlock = _sourceBlock;
  }

  public void print (PrintWriter out)
  {
    if (label != null)
      out.print (label + ": ");

    printMe (out);

    if (next != null)
      next.print (out);
  }

  public void print ()
  {
    print (new PrintWriter (new OutputStreamWriter (System.err), true));
  }
  public void printMe ()
  {
    printMe (new PrintWriter (new OutputStreamWriter (System.err), true));
  }

  public void printMe (PrintWriter out)
  {
    if (sourceBlock == null)
      out.println ("<unknown> (no-line-number)");
    else  
      out.println ("<unknown> (" + sourceBlock.getLineNum () + ")");

    // XXX do sourceExpr  
  }

  public String toString ()
  {
    StringWriter out = new StringWriter ();
    printMe (new PrintWriter (out, true));
    return out.toString ();
  }

  public Object getRefinerInfo ()
  {
    return info;
  }

  public void setRefinerInfo (Object v)
  {
    info = v;
  }

  public PFunctionDef getParent ()
  {
    return parent;
  }

  public PProgram getPProgram ()
  {
    return parent.getParent ();
  }

  public ExprFactory exprFac ()
  {
    return parent.getExprFactory ();
  }
 
  public void setParent (PFunctionDef v)
  {
    parent = v;
  }

  /**
   * Returns line number of this statement, or -1 if the line number is 
   * unknown.
   *
   * @return an <code>int</code> value
   */
  public int getLineNum ()
  {
    return -1;
  }
  
}
