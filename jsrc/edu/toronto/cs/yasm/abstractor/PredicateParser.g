header { package edu.toronto.cs.yasm.abstractor; }

{
  import edu.toronto.cs.cparser.*;
  import edu.toronto.cs.cparser.block.*;
}

class PredicateParser extends Parser;

options
{
  buildAST = true;
  ASTLabelType = "Block";
  importVocab = CIL;
  defaultErrorHandler = false;
}

parse
  : ( predicate SEMI! )+
  ;

// e.g.
// x = 5
// x & y 

predicate
  : boolExpr ( boolOp boolExpr )*
  ;

boolExpr!
  : e1:arithExpr op:arithCompOp e2:arithExpr
    { #boolExpr = #(op, e1, e2); }
  ;

arithExpr
  // XXX How to do it this way? Labelled subrules not implemented yet
  // : arithFact (r:( arithOp arithFact ))*
  : arithFact ( ( PLUS^ | MINUS^ | STAR^ | DIV^ | MOD^ ) arithFact )*
  /*
  : arithFact ( ( op:arithOp f:arithFact )
                {
                  System.out.println ("match");
                  TNode t = #(op, f);
                  TNode.printTree (t);
                  System.out.println ("match END");
                }
              )*
  */
  | LPAREN! arithExpr RPAREN!
  ;  

arithFact
  : ID
  | intConst
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

arithCompOp
  : LT | LTE | GT | GTE | EQUAL | NOT_EQUAL
  ;

boolOp
  : LOR | LAND
  ;

arithOp
  : PLUS | MINUS | STAR | DIV | MOD
  ;  
