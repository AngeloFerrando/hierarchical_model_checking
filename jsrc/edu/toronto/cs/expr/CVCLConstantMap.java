package edu.toronto.cs.expr;

import java.util.*;

import edu.toronto.cs.tp.cvcl.JavaCVCConstants;

public class CVCLConstantMap implements JavaCVCConstants
{
  
  Map cvclForwardMap;
  Map cvclBackwardMap;
  
  CVCLConstantMap () 
  {
    cvclForwardMap = new HashMap ();     
    cvclBackwardMap = new HashMap ();

    cvclForwardMap.put (new Integer (EQ), ComparisonOp.EQ);
    cvclBackwardMap.put (ComparisonOp.EQ, new Integer (EQ));

    cvclForwardMap.put (new Integer (NEQ), ComparisonOp.NEQ);
    cvclBackwardMap.put (ComparisonOp.NEQ, new Integer (NEQ));

    cvclForwardMap.put (new Integer (NOT), BoolOp.NOT);
    cvclBackwardMap.put (BoolOp.NOT, new Integer (NOT));

    cvclForwardMap.put (new Integer (AND), BoolOp.AND);
    cvclBackwardMap.put (BoolOp.AND, new Integer (AND));

    cvclForwardMap.put (new Integer (OR), BoolOp.OR);
    cvclBackwardMap.put (BoolOp.OR, new Integer (OR));

    cvclForwardMap.put (new Integer (GT), ComparisonOp.GT);
    cvclBackwardMap.put (ComparisonOp.GT, new Integer (GT));

    cvclForwardMap.put (new Integer (GE), ComparisonOp.GEQ);
    cvclBackwardMap.put (ComparisonOp.GEQ, new Integer (GE));

    cvclForwardMap.put (new Integer (LT), ComparisonOp.LT);
    cvclBackwardMap.put (ComparisonOp.LT, new Integer (LT));

    cvclForwardMap.put (new Integer (LE), ComparisonOp.LEQ);
    cvclBackwardMap.put (ComparisonOp.LEQ, new Integer (LE));

    cvclForwardMap.put (new Integer (PLUS), NumericOp.PLUS);
    cvclBackwardMap.put (NumericOp.PLUS, new Integer (PLUS));

    cvclForwardMap.put (new Integer (MINUS), NumericOp.MINUS);
    cvclBackwardMap.put (NumericOp.MINUS, new Integer (MINUS));

    cvclForwardMap.put (new Integer (MULT), NumericOp.MULT);
    cvclBackwardMap.put (NumericOp.MULT, new Integer (MULT));

    cvclForwardMap.put (new Integer (DIVIDE), NumericOp.DIV);
    cvclBackwardMap.put (NumericOp.DIV, new Integer (DIVIDE));

    cvclForwardMap.put (new Integer (UMINUS), NumericOp.UN_MINUS);
    cvclBackwardMap.put (NumericOp.UN_MINUS, new Integer (UMINUS));
    

  }
  public Map getForwardMap ()
  {
    return cvclForwardMap;
  }
  public Map getBackwardMap ()
  {
    return cvclBackwardMap;
  }
}
