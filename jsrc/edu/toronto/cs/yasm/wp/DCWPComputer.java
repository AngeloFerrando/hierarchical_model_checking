package edu.toronto.cs.yasm.wp;

import edu.toronto.cs.expr.*;
import java.util.*;

/** 
 * Dependent (sequential) composition WP Computer.
 * 
 * @author Kelvin Ku (kelvin@cs.toronto.edu)
 * @version 
 */
public class DCWPComputer
  extends BaseWPComputer
  implements WPComputer
{
  // -- one WPComputer per assignment, in reverse order
  LinkedList wpComputers;

  public DCWPComputer (Expr stmtList)
  {
    assert (stmtList.op () == CILListOp.SLIST) :
      "Invalid Expr: " + stmtList;

    wpComputers = new LinkedList ();

    // -- create WP computers list in reverse order
    for (Iterator it = stmtList.args ().iterator (); it.hasNext ();)
      wpComputers.addFirst (WPComputerFactory.wp ((Expr) it.next ()));
  }

  public Expr computeWP (Expr e)
  {
    Expr post = e;

    for (Iterator it = wpComputers.iterator (); it.hasNext ();)
      post = ((WPComputer) it.next ()).computeWP (post);

    return post;
  }

}

