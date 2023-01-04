header
{
    package edu.toronto.cs.yasm.abstractor;
}

{
    import edu.toronto.cs.cparser.block.*;
    import edu.toronto.cs.cparser.*;
    import edu.toronto.cs.expr.*;
    import java.util.*;
}

class SymbolicExecutor extends TreeParser;

options
{
    buildAST = false;
    ASTLabelType = "Block";
    defaultErrorHandler = false;
    importVocab = CIL;
}

{
  ExprFactory fac;

  public void setExprFactory (ExprFactory _fac)
  {
    fac = _fac;
  }
}

block [MemoryModel mmIn] returns [MemoryModel mmOut]
  : #( NBlock mmOut = stmtList [mmIn] )
  ;

stmtList [MemoryModel mmIn] returns [MemoryModel mmOut]
  : ( mmIn = stmt[mmIn] )+ { mmOut = mmIn; }
  ;

stmt [MemoryModel mmIn] returns [MemoryModel mmOut]
  : mmOut = asmt [mmIn]
  | mmOut = labelledStmt [mmIn]
  ;  

returnStmt [String returnVarName, MemoryModel mmIn] returns [MemoryModel mmOut]
  { Expr e = null; }
  : #( r:NBlock (e = pureExpr [mmIn])? )
    {
      assert #r.getBlockType () == BlockType.RETURN : "Invalid Block type";
      if (e != null)
        mmIn.assign (fac.var (returnVarName), e);
      mmOut = mmIn;
    }
  ;

labelledStmt [MemoryModel mmIn] returns [MemoryModel mmOut]
  : label #( NBlock mmOut = stmt [mmIn] )
  ;

label  
  : #( NLabel ID )
  ;

functionCallArgs [List declarations, MemoryModel mmIn]
  returns [MemoryModel mmOut]
  {
    Expr e;
    Iterator it = declarations.iterator ();
  }
  : ( e = pureExpr [mmIn]
      {
        Expr curArg = (Expr) it.next ();
        mmIn.assign (curArg, e);
      }
    )*
    { mmOut = mmIn; }
  ;

asmt [MemoryModel mmIn] returns [MemoryModel mmOut]
  {
    Expr l, e;
    Operator op;
  }
  : #(NAssignStmt l = lval[mmIn] e = pureExpr[mmIn])
    {
      mmIn.assign (l, e);
      mmOut = mmIn;
    }
  | #(NImplicitAssignStmt l = lval [mmIn] op = implicitAssignOp )
    {
      mmIn.assign (l, fac.op (op).binApply (l, fac.intExpr (1)));
      mmOut = mmIn;
    }
  ;

implicitAssignOp returns [Operator op]
  : INC { op = NumericOp.PLUS; }
  | DEC { op = NumericOp.MINUS; }
  ;

// XXX Retained for compatibility
expr [MemoryModel mm] returns [Expr e]
  : e = pureExpr [mm]
  ;  

pureExpr [MemoryModel mm] returns [Expr e]
  : e = pureLogicalOrExpr [mm]
  | e = pureLogicalAndExpr [mm]
  | e = pureEqualityExpr [mm]
  | e = pureRelationalExpr [mm]
  | e = pureAdditiveExpr [mm]
  | e = pureMultExpr [mm]
  | e = pureUnaryExpr [mm]
  | e = purePrimaryExpr [mm]
  ;  

pureLogicalOrExpr [MemoryModel mm] returns [Expr e]
  { Expr e1, e2; }
  : #( LOR e1 = pureExpr [mm] e2 = pureExpr [mm]) 
    { e = fac.op (BoolOp.OR).binApply (e1, e2); }
  ;

pureLogicalAndExpr [MemoryModel mm] returns [Expr e]
  { Expr e1, e2; }
  : #( LAND e1 = pureExpr [mm] e2 = pureExpr [mm] )
    { e = fac.op (BoolOp.AND).binApply (e1, e2); }
  ;

pureEqualityExpr [MemoryModel mm] returns [Expr e]
  { Expr e1, e2; }
  : #( EQUAL e1 = pureExpr [mm] e2 = pureExpr [mm])
    { e = fac.op (ComparisonOp.EQ).binApply (e1, e2); }
  | #( NOT_EQUAL e1 = pureExpr [mm] e2 = pureExpr [mm])
    {
      e = fac.op (BoolOp.NOT).
        unaryApply (fac.op (ComparisonOp.EQ).binApply (e1, e2));
    }
  ;

pureRelationalExpr [MemoryModel mm] returns [Expr e]
  {
    Expr e1, e2;
    Operator op;
  }
  : ( #( LT e1 = pureExpr [mm] e2 = pureExpr [mm])
      { op = ComparisonOp.LT; }
    | #( LTE e1 = pureExpr [mm] e2 = pureExpr [mm])
      { op = ComparisonOp.LEQ; }
    | #( GT e1 = pureExpr [mm] e2 = pureExpr [mm])
      { op = ComparisonOp.GT; }
    | #( GTE e1 = pureExpr [mm] e2 = pureExpr [mm])
      { op = ComparisonOp.GEQ; }
    )
    { e = fac.op (op).binApply (e1, e2); }
  ;

pureAdditiveExpr [MemoryModel mm] returns [Expr e]
  {
    Expr e1, e2;
    Operator op;
  }
  : ( #( PLUS e1 = pureExpr [mm] e2 = pureExpr [mm])
      { op = NumericOp.PLUS; }
    | #( MINUS e1 = pureExpr [mm] e2 = pureExpr [mm])
      { op = NumericOp.MINUS; }
    )
    { e = fac.op (op).binApply (e1, e2); }
  ;

pureMultExpr [MemoryModel mm] returns [Expr e]
  {
    Expr e1, e2;
    Operator op;
  }
  : ( #( STAR e1 = pureExpr [mm] e2 = pureExpr [mm])
      { op = NumericOp.MULT; }
    | #( DIV e1 = pureExpr [mm] e2 = pureExpr [mm])
      { op = NumericOp.DIV; }
    | #( MOD e1 = pureExpr [mm] e2 = pureExpr [mm])
      { op = NumericOp.MOD; }
    )
    { e = fac.op (op).binApply (e1, e2); }
  ;

pureUnaryExpr [MemoryModel mm] returns [Expr e]
  : e = purePostfixExpr [mm]
  | e = purePrefixExpr [mm]
  ;

purePostfixExpr [MemoryModel mm] returns [Expr e]
  : #( NPostfixExpr
        e = lval [mm] // XXX Does not do the right thing --- assume we won't
                 // execute a statement with an array or function call
                 // component for now
        /*
        ( #( NFunctionCallArgs (argExprList)? RPAREN )
        | LBRACKET pureExpr RBRACKET
        )+
        */
    )
  ;

purePrefixExpr [MemoryModel mm] returns [Expr e]
  { Operator op; }
  : #( NUnaryExpr op = unaryOperator e = pureExpr [mm])  
    {
      if (op != null)
        e = fac.op (op).unaryApply (e);
    }
  ;

unaryOperator returns [Operator op]
  : BAND  { op = null; } // XXX Ignore pointer operators
  | STAR  { op = null; } // XXX
  | PLUS  { op = null; }
  | MINUS { op = NumericOp.UN_MINUS; }
  | LNOT  { op = BoolOp.NOT; }
  | LAND  { op = BoolOp.AND; } // XXX how would this arise? "&& b"?
  ;

purePrimaryExpr [MemoryModel mm] returns [Expr e]
  : 
        // XXX this is very very wrong but should do for now
        e = lval [mm] { e = mm.lookup (e); }
        
  | n:intConst
    {
      try
      {
        e = fac.intExpr (Integer.parseInt (n.getText ()));
      }
      catch (NumberFormatException ex)
      {
        throw new RecognitionException ("Unable to parse number");
      }   
    }
  | #( NPureExpressionGroup e = pureExpr [mm] ) // parenthesized expr
  ;

protected
intConst
        :       IntOctalConst
        |       LongOctalConst
        |       UnsignedOctalConst
        |       IntIntConst
        |       LongIntConst
        |       UnsignedIntConst
        |       IntHexConst
        |       LongHexConst
        |       UnsignedHexConst
        ;

lval [MemoryModel mm] returns [Expr e]
  { Expr l; }
  : id:ID
      { e = fac.var (id.getText ()); }
  | #( PTR id1:ID l = lval [mm] )
      {
        // XXX ignore struct fields for now
        //e = fac.var(id1.getText () + "->" + ((VariableOp) l.op ()).name ()); 
        e = fac.var (id1.getText ());
      } 
  | #( DOT id2:ID l = lval [mm] )
      {
        // XXX ignore struct fields for now
        // e = fac.var(id2.getText () + "." + ((VariableOp) l.op ()).name ());
        e = fac.var (id1.getText ());
      } 
  // XXX Does not handle array expressions    
  ;

/*
primaryExpr [MemoryModel mm] returns [Expr e]  
  :       id:ID
          {
            e = mm.lookup (fac.var(id.getText ()));
          }
  |       n:Number        
          {
            try {
              e = fac.intExpr (Integer.parseInt (n.getText ()));
                } catch (NumberFormatException ex) {
                 throw new RecognitionException ("Unable to parse number");
            }   
          }
  |       #( NExpressionGroup e = expr[mm] ) // parenthesized expr
  ;

postfixAsmt [MemoryModel mmIn] returns [MemoryModel mmOut]
  { Expr lhs, rhs; }
  : #( NPostfixExpr lhs = lval [mmIn] rhs = postfixExprOp [mmIn.lookup (lhs)])
    {
      mmIn.assign (lhs, rhs);
      mmOut = mmIn;
    }
  ;

postfixExprOp [Expr eIn] returns [Expr eOut]
  : INC 
    { eOut = fac.op (NumericOp.PLUS).binApply (eIn, fac.intExpr (1)); }
  | DEC 
    { eOut = fac.op (NumericOp.MINUS).binApply (eIn, fac.intExpr (1)); }
  ;
  

logicalExpr [MemoryModel mm] returns [Expr eOut]
  {
    Expr lhs, rhs;
    Operator op;
  }
  : (
    #( LOR lhs = expr [mm] rhs = expr [mm] )
    { op = BoolOp.OR; }
  | #( LAND lhs = expr [mm] rhs = expr [mm] )
    { op = BoolOp.AND; }
    )
    { eOut = fac.op (op).binApply (lhs, rhs); }
  ;

prefixAsmt [MemoryModel mmIn] returns [MemoryModel mmOut]
        { Expr e1, e2; }
        :       #( INC e1 = lval [mmIn] )
                {
                  // ++e1 is e1 = e1 + 1
                  e2 = fac.op (NumericOp.PLUS).binApply (e1, fac.intExpr (1));
                  mmIn.assign (e1, e2);
                  mmOut = mmIn;
                }
        |       #( DEC e1 = lval [mmIn] )
                {
                  // --e1 is e1 = e1 - 1
                  e2 = fac.op (NumericOp.MINUS).binApply (e1, fac.intExpr (1));
                  mmIn.assign (e1, e2);
                  mmOut = mmIn;
                }
        ;

unaryExpr [MemoryModel mm] returns [Expr e]
        {
          Expr e1;
          Operator op;
        }
        :       #( NUnaryExpr op = unaryOperator e1 = expr [mm])
                {
                  e = e1;

                  if (op != null)
                    e = fac.op (op).unaryApply (e1);
                }
        ;

unaryOperator returns [Operator op]
  : PLUS { op = null; }
  | MINUS { op = NumericOp.UN_MINUS; }
  | LNOT { op = BoolOp.NOT; }
  | LAND { op = BoolOp.AND; }
  | BAND { op = null; } // XXX Ignores pointer operators
  | STAR { op = null; } // XXX
  ;

equalityExpr [MemoryModel mm] returns [Expr e] {Expr lhs; Expr rhs;}
        :       #( EQUAL lhs = expr[mm] rhs = expr[mm] ) 
                { e = fac.op (ComparisonOp.EQ).binApply (lhs, rhs); }
        |       #( NOT_EQUAL lhs = expr[mm] rhs = expr[mm] )
                { 
                  // convert to ! (lhs == rhs), since CVC doesn't have !=
                  e = fac.op (BoolOp.NOT).
                  unaryApply (fac.op (ComparisonOp.EQ).binApply (lhs, rhs));
                }
        ;

relationalExpr [MemoryModel mm] returns [Expr e] {Expr lhs; Expr rhs;}
        :       #( LT lhs = expr[mm] rhs = expr[mm])
                { e = fac.op (ComparisonOp.LT).binApply (lhs, rhs);}
        |       #( LTE lhs = expr[mm] rhs = expr[mm])
                { e = fac.op (ComparisonOp.LEQ).binApply (lhs, rhs);}
        |       #( GT lhs = expr[mm] rhs = expr[mm])
                { e = fac.op (ComparisonOp.GT).binApply (lhs, rhs);}
        |       #( GTE lhs = expr[mm] rhs = expr[mm])
                { e = fac.op (ComparisonOp.GEQ).binApply (lhs, rhs);}
        ;

assignExpr [MemoryModel mmIn] returns [MemoryModel mmOut]
  { Expr l, e; }
    
  :       #( ASSIGN l = lval[mmIn] e = expr[mmIn])
          {
            mmIn.assign (l, e);
            mmOut = mmIn;
          }
  |       mmOut = prefixAsmt [mmIn]
  |       mmOut = postfixAsmt [mmIn]
  ;

arithExpr [MemoryModel mm] returns [Expr e]
  {
    Expr e1, e2;
    Operator op;
  }
  : (
    #( PLUS  e1 = expr [mm] e2 = expr [mm] )
    { op = NumericOp.PLUS; }
  | #( MINUS e1 = expr [mm] e2 = expr [mm] )
    { op = NumericOp.MINUS; }
  | #( STAR e1 = expr [mm] e2 = expr [mm] )
    { op = NumericOp.MULT; }
  | #( DIV e1 = expr [mm] e2 = expr [mm] )
    { op = NumericOp.DIV; }
  | #( MOD e1 = expr [mm] e2 = expr [mm] )
    { op = NumericOp.MOD; }
    )
    { e = fac.op (op).binApply (e1, e2); }
  ;  
*/  


