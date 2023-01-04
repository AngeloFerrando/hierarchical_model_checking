package edu.toronto.cs.expr;

import edu.toronto.cs.cparser.*;

import java.io.*;

public class JavaObjectOp extends NullaryOperator
{
  Object object;
  
  public JavaObjectOp (Object o)
  {
    object = o;
  }

  public String name ()
   {
     if (object instanceof Integer)
       return ((Integer)object).toString ();
     else if (object instanceof TNode)
     {
       StringWriter out = new StringWriter ();
       TNode.printTree ((TNode) object, new PrintWriter (out, true));
       return out.toString ();
     }
     else
       return "JAVA_OBJECT (" + object + ")";
   }

  
  public int hashCode ()
  {
    return object.hashCode ();
  }
  
  public Object getObject ()
  {
    return object;
  }
  
  public boolean equals (Object o)
  {
    if (super.equals (o)) return true;
    if (o != null && o instanceof JavaObjectOp)
      return object.equals (((JavaObjectOp)o).getObject ());
    return false;
  }
  
  
}
