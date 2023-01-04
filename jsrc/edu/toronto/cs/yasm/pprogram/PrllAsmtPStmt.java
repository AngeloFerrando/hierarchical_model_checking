package edu.toronto.cs.yasm.pprogram;

import edu.toronto.cs.cparser.block.*;
import java.util.*;
import edu.toronto.cs.expr.*;
import edu.toronto.cs.yasm.abstractor.*;
import antlr.RecognitionException;

import edu.toronto.cs.yasm.wp.*;

import java.io.PrintWriter;

/**
 * ParallelAsgnPStmt.java
 *
 *
 * Created: Fri Jun 25 14:27:37 2004
 *
 * @author <a href="mailto:kelvin@tallinn.cs">Kelvin Ku</a>
 * @version 1.0
 */
public class PrllAsmtPStmt extends PStmt 
{
  List asmts;

  WPComputer wpComputer;

  public PrllAsmtPStmt (PFunctionDef parent, Block sourceBlock) 
  {
    super (parent, sourceBlock);
    assert false : "Blocks no longer supported!";
    init ();
  }

  public PrllAsmtPStmt (PFunctionDef parent, Expr sourceExpr)
  {
    super (parent, sourceExpr);
    init ();
  }

  private void init ()
  {
    asmts = new ArrayList ();
    wpComputer = null;
  }

  public void setAsmts (List v)
  {
    asmts = v;
  }

  public void addAsmt (AsmtPStmt asmt)
  {
    asmts.add (asmt);
  }

  public List getAsmts ()
  {
    return asmts;
  }

  public MemoryModel symExec (ExprFactory fac)
  {
    if (sourceBlock != null)
    try
    {
      return SymbolicExecutorUtil.newSymbolicExecutor (fac).block
        (sourceBlock, new MemoryModel ());
    }
    catch (RecognitionException ex)
    {
      return null;
    }
    
    if (sourceExpr != null)
      return SymbolicExecutorUtil.newExprExecutor (fac).stmtList
        (sourceExpr, new MemoryModel ());    

    return null;
  }

  // -- returns true if all children are non-null, false otherwise
  // -- replace with known (Expr e) when null ops are eliminated
  /*
  private boolean recNonNull (Expr e)
  {
    if (e == null) return false;

    for (Iterator it = e.args ().iterator (); it.hasNext ();)
    {
      if (!recNonNull ((Expr) it.next ())) return false;
    }

    return true;
  }
  */

  public WPComputer getWPComputer ()
  {
    // System.err.println (sourceExpr);
    if (wpComputer == null)
      if (sourceExpr != null)
        // -- this op should only show up if the RHS is a function call, since
        // any other assign is an SLIST
        // wpComputer = new DCWPComputer (sourceExpr);
        wpComputer = WPComputerFactory.wp (sourceExpr);
        /*
        if (sourceExpr.op () == CILAssignOp.ASSIGN)
        {
          // -- an unknown RHS of an assignment may get parsed as null
          if (sourceExpr.arg (1) != null)
          // if (recNonNull (sourceExpr.arg (1)))
          {
            wpComputer = new ExprWPComputer (sourceExpr);
          }
          else
            return new UnknownExprWPComputer (sourceExpr.arg (0));
        }
        else if (sourceExpr.op () == CILListOp.SLIST)
        {
          wpComputer = new ExprWPComputer (sourceExpr);
        }
        else
        {
          throw new RuntimeException ("Can't get WPComputer for " +
                                      sourceExpr);
        }
        */
      else
        return ExprWPComputer.ID;

    assert sourceBlock == null : "sourceBlock is not null " + sourceBlock;

    assert wpComputer != null : "wpComputer is null";

    return wpComputer;
  }

  public int getLineNum ()
  {
    Expr src = getSourceExpr ();
    if (src == null) return super.getLineNum ();
    if (src.arity () != 3)
      {
	System.err.println ("****Strange assignment without a line number");
	System.err.println ("****Srouce expr: " + src);
	System.err.println ("WOWOWOWOWO");
	System.err.println ("WOWOWOWOWO");
	System.err.println ("WOWOWOWOWO");
	System.err.println ("WOWOWOWOWO");
	System.err.println ("WOWOWOWOWO");
	System.err.println ("WOWOWOWOWO");
	System.err.println ("WOWOWOWOWO");
	System.err.println ("WOWOWOWOWO");

	return super.getLineNum ();
      }
    
    
    return 
      ((Integer)((JavaObjectOp)src.arg (2).op ()).getObject ()).intValue ();
  }


  public void printMe (PrintWriter out)
  {
    out.print ("assignment");
    for (Iterator it = asmts.iterator (); it.hasNext ();)
    {
      ((AsmtPStmt)it.next ()).printMe (out);
      out.println (",");
      out.print ("\t");
    }
    if (sourceBlock != null)
      out.println (" (" + sourceBlock.getLineNum () + ")");
    else
      out.println ();  
  }

} 
