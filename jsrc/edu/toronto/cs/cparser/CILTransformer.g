/*%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%

        Copyright (c) Non, Inc. 1998 -- All Rights Reserved

PROJECT:        C Compiler
MODULE:         GnuCTreeParser
FILE:           GnuCTreeParser.g

AUTHOR:         Monty Zukowski (jamz@cdsnet.net) April 28, 1998

DESCRIPTION:

                This tree grammar is for a Gnu C AST.  No actions in it,
                subclass to do something useful.

%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%*/
header { package edu.toronto.cs.cparser; }


{
import java.io.*;

import antlr.CommonAST;
import antlr.DumpASTVisitor;

import edu.toronto.cs.expr.*;

import java.util.*;
}

                     
class CILTransformer extends TreeParser;

options
        {
        importVocab = GNUC;
        buildAST = true;
        ASTLabelType = "TNode";

        // Copied following options from java grammar.
        codeGenMakeSwitchThreshold = 2;
        codeGenBitsetTestThreshold = 3;
        }


{
        Map callNumMap;
        int traceDepth = 0;
        public void reportError(RecognitionException ex) {
          if ( ex != null)   {
                System.err.println("ANTLR Tree Parsing RecognitionException Error: " + ex.getClass().getName() + " " + ex );
                ex.printStackTrace(System.err);
          }
        }
        public void reportError(NoViableAltException ex) {
                System.err.println("ANTLR Tree Parsing NoViableAltException Error: " + ex.toString());
                TNode.printTree( ex.node );
                ex.printStackTrace(System.err);
        }
        public void reportError(MismatchedTokenException ex) {
          if ( ex != null)   {
                TNode.printTree( ex.node );
                System.err.println("ANTLR Tree Parsing MismatchedTokenException Error: " + ex );
                ex.printStackTrace(System.err);
          }
        }
        public void reportError(String s) {
                System.err.println("ANTLR Error from String: " + s);
        }
        public void reportWarning(String s) {
                System.err.println("ANTLR Warning from String: " + s);
        }
        protected void match(AST t, int ttype) throws MismatchedTokenException {
                //System.out.println("match("+ttype+"); cursor is "+t);
                super.match(t, ttype);
        }
        public void match(AST t, BitSet b) throws MismatchedTokenException {
                //System.out.println("match("+b+"); cursor is "+t);
                super.match(t, b);
        }
        protected void matchNot(AST t, int ttype) throws MismatchedTokenException {
                //System.out.println("matchNot("+ttype+"); cursor is "+t);
                super.matchNot(t, ttype);
                }
        public void traceIn(String rname, AST t) {
          traceDepth += 1;
          for (int x=0; x<traceDepth; x++) System.out.print(" ");
          super.traceIn(rname, t);   
        }
        public void traceOut(String rname, AST t) {
          for (int x=0; x<traceDepth; x++) System.out.print(" ");
          super.traceOut(rname, t);
          traceDepth -= 1;
        }

        public void incNumCallSites (String funcName)
        {
          Integer numCalls = (Integer) callNumMap.get (funcName);
          if (numCalls == null)
            numCalls = new Integer (1);
          else
            numCalls = new Integer (numCalls.intValue () + 1);  
          callNumMap.put (funcName, numCalls);
        }

        public int getNumCallSites (String funcName)
        {
          Integer numCalls = (Integer) callNumMap.get (funcName);
          if (numCalls == null)
            return 0;
          else
            return numCalls.intValue ();  
        }

        public Map getCallNumMap ()
        {
          return callNumMap;
        }

        boolean stmtBlocking = false;
}

translationUnit [ExprFactory fac, boolean _stmtBlocking] returns [Expr e]
options { defaultErrorHandler=false; }
  {
    e = null;
    callNumMap = new HashMap ();
    stmtBlocking = _stmtBlocking;
  }
  : ( e = externalList [fac] )? 
  ;

externalList [ExprFactory fac] returns [Expr e]
  {
    e = null;
    List defList = new LinkedList ();
    Expr d;
  }
  : (
      d = externalDef [fac]
      { defList.add (d); }
    )+
    { e = fac.op (CILProgramOp.PROGRAM).naryApply (defList); }
  ;

externalDef [ExprFactory fac] returns [Expr e]
  { e = null; }
  : e = declaration [fac]
  | e = functionDef [fac]
  | e = asm_expr [fac]
  | SEMI { e = fac.op (CILEmptyStmtOp.EMPTY); }
  // | e = typelessDeclaration [fac] // removed, CIL: all decls are typed
  ;

asm_expr [ExprFactory fac] returns [Expr e]
  {
    e = null;
    Expr discard;
  }
  : #( "asm" ( "volatile" )? LCURLY discard = expr [fac] RCURLY ( SEMI )+ )
    { e = fac.op (new JavaObjectOp (##)); }
  ;

declaration [ExprFactory fac] returns [Expr e]
  {
    e = null;
    Expr ds;
    // Expr id = fac.op (CILNullOp.NULL);
    Expr id = null;
  }
  : #( NDeclaration
       ds = declSpecifiers [fac]
       ( id = initDecl [fac] )? // changed from "initDeclList",
                                 // CIL: expects at most one declarator
       ( SEMI )+
     )
  {
    // XXX should move pointerGroup in id into ds 
    e = fac.op (CILDeclarationOp.DECL).naryApply (new Expr[] {ds, id});
  }
  ;

declSpecifiers [ExprFactory fac] returns [Expr e]
  {
    e = null;
    List declSpecList = new LinkedList ();
    Expr scs;
    Expr tq;
    Expr ts;
  }
  : ( scs = storageClassSpecifier [fac]
      { declSpecList.add (scs); }
    | tq = typeQualifier [fac]
      { declSpecList.add (tq); }
    | ts = typeSpecifier [fac]
      { declSpecList.add (ts); }
    )+
    { e = fac.op (CILListOp.LIST).naryApply (declSpecList); }
  ;

storageClassSpecifier [ExprFactory fac] returns [Expr e]
  { e = fac.op (CILUnknownOp.UNKNOWN); }
  : "auto"
  | "register"
  | "typedef"
  | e = functionStorageClassSpecifier [fac]
  ;

functionStorageClassSpecifier [ExprFactory fac] returns [Expr e]
  { e = fac.op (CILUnknownOp.UNKNOWN); }
  : "extern"
  | "static"
  | "inline"
  ;

typeQualifier [ExprFactory fac] returns [Expr e]
  { e = fac.op (CILUnknownOp.UNKNOWN); }
  : "const"
    { e = fac.op (CILTypeSpecifierOp.CONST); }
  | "volatile"
  ;

typeSpecifier [ExprFactory fac] returns [Expr e]
  {
    e = null;
    Expr discard;
  }
  : "void"
    { e = fac.op (CILTypeSpecifierOp.VOID); }
  | "char"
    { e = fac.op (CILTypeSpecifierOp.CHAR); }
  | "short"
    { e = fac.op (CILTypeSpecifierOp.SHORT); }
  | "int"
    { e = fac.op (CILTypeSpecifierOp.INT); }
  | "long"
    { e = fac.op (CILTypeSpecifierOp.LONG); }
  | "float"
    { e = fac.op (CILTypeSpecifierOp.FLOAT); }
  | "double"
    { e = fac.op (CILTypeSpecifierOp.DOUBLE); }
  | "signed"
    { e = fac.op (CILTypeSpecifierOp.SIGNED); }
  | "unsigned"
    { e = fac.op (CILTypeSpecifierOp.UNSIGNED); }
  | e = structSpecifier [fac] ( discard = attributeDecl [fac] )*
    // XXX ignores attributes
  | e = unionSpecifier [fac] ( discard = attributeDecl [fac] )*
    // XXX ignores attributes
  | e = enumSpecifier [fac]
  | typedefName
  /*
  | #("typeof" LPAREN
        ( (typeName )=> typeName 
        | expr
        )
        RPAREN
    )
  | "__complex"
  */
  | "__builtin_va_list"
  	{ e = fac.op (CILTypeSpecifierOp.VOID); }
  ;

typedefName
        :       #(NTypedefName ID)
        ;

structSpecifier [ExprFactory fac] returns [Expr e]
  {
    e = null;
    Expr body;
  }
  : #( "struct" body = structOrUnionBody [fac] )
    {
      e = fac.op (CILRecordOp.STRUCT).naryApply (body.args ());
    }
  ;

unionSpecifier [ExprFactory fac] returns [Expr e]
  {
    e = null;
    Expr body;
  }
  : #( "union" body = structOrUnionBody [fac] )
    { e = fac.op (CILRecordOp.STRUCT).naryApply (body.args ()); }
  ;

structOrUnionBody [ExprFactory fac] returns [Expr e]
  {
    // e = fac.op (CILNullOp.NULL);
    e =  null;
    Expr sdl;
  }
  : ( (ID LCURLY) => id1:ID LCURLY // possibly empty named struct
      { e = fac.var (id1.getText ()); }
      (
        sdl = structDeclarationList [fac]
        { e = fac.op (CILListOp.LIST).binApply (e, sdl); }
          // list is unwrapped in struct/unionSpecifier
      )?
      RCURLY  
    /* removed, CIL: all structs are named 
    | LCURLY
      ( structDeclarationList )?
      RCURLY
    */  
    | id2:ID // e.g. "node" in "struct node *n;" or "struct node;"
      {
        e = fac.op (CILListOp.LIST).binApply
          (fac.var (id2.getText ()), null /* for uniformity */); }
          // list is unwrapped in struct/unionSpecifier
    )
  ;

structDeclarationList [ExprFactory fac] returns [Expr e]
  {
    e = null;
    Expr sd;
    List sdList = new LinkedList ();
  }
  : (
      sd = structDeclaration [fac]
      { sdList.add (sd); }
    )+
    { e = fac.op (CILListOp.LIST).naryApply (sdList); }
  ;

structDeclaration [ExprFactory fac] returns [Expr e]
  {
    e = null;
    Expr sql;
    Expr sd;
  }
  : sql = specifierQualifierList [fac]
    sd = structDeclarator [fac]
   // changed from "structDeclaratorList", CIL: expects at most one declarator
    { e = fac.op (CILDeclarationOp.DECL).naryApply (new Expr[] {sql, sd}); }
  ;

specifierQualifierList [ExprFactory fac] returns [Expr e]
  {
    e = null;
    List sqList = new LinkedList ();
    Expr ts;
    Expr tq;
  }
  : ( ts = typeSpecifier [fac]
      { sqList.add (ts); }
    | tq = typeQualifier [fac]
      { sqList.add (tq); }
    )+
    { e = fac.op (CILListOp.LIST).naryApply (sqList); }
  ;

/*
structDeclaratorList
  : ( structDeclarator )+
  ;
*/

structDeclarator [ExprFactory fac] returns [Expr e]
  {
    e = null;
    Expr discard;
  }
  :
  #( NStructDeclarator      
      ( e = declarator [fac] )?
      ( COLON discard = expr [fac] )? // XXX ignores initial values
      ( discard = attributeDecl [fac] )* // XXX ignores attributes
  )
  ;

enumSpecifier [ExprFactory fac] returns [Expr e]
  {
    e = null;
    Expr enList;
  }
  : #(  "enum"
        id:ID // removed ()?, CIL: enums must have names
        { e = fac.var (id.getText ()); }
        (
          LCURLY enList = enumList [fac] RCURLY
          { e = fac.op (CILListOp.LIST).binApply (e, enList); }
        )?
        { e = fac.op (CILEnumOp.ENUM).naryApply (e.args ()); }
    )
  ;

enumList [ExprFactory fac] returns [Expr e]
  {
    e = null;
    List enList = new LinkedList ();
    Expr en;
  }
  : (
      en = enumerator [fac]
      { enList.add (en); }
    )+
    { e = fac.op (CILListOp.LIST).naryApply (enList); }
  ;

enumerator [ExprFactory fac] returns [Expr e]
  {
    e = null;
    Expr discard;
  }
  : id:ID ( ASSIGN discard = expr [fac] )? // XXX ignores initial value
    { e = fac.var (id.getText ()); }
  ;

attributeDecl [ExprFactory fac] returns [Expr e]
  {
    e = null;
    Expr discard;
  }
  :
  #( "__attribute" (.)* )
  | #( NAsmAttribute LPAREN discard = expr [fac] RPAREN )
  ;

initDeclList [ExprFactory fac] returns [Expr e]
  {
    // e = fac.op (CILNullOp.NULL);
    e = null;
    List declList = new LinkedList ();
    Expr decl;
  }
  : ( decl = initDecl [fac] { declList.add (decl); } )+
    { e = fac.op (CILListOp.LIST).naryApply (declList); }
  ;

initDecl [ExprFactory fac] returns [Expr e]
  {
    // e = fac.op (CILNullOp.NULL);
    e = null;
  }
  : #( NInitDecl
    e = declarator [fac]
    // ( attributeDecl )* // XXX ignores attributes
    /* XXX handle initial values later
    ( ASSIGN initializer
    | COLON expr
    )?
    */
    )
  ;

pointerGroup [ExprFactory fac] returns [Expr e]
  {
    e = null;
    int numPtrs = 0;
  }
//  : #( NPointerGroup ( STAR { ++numPtrs; } ( typeQualifier )* )+ )
// XXX dunno what to do with a typeQualifier here
  : #( NPointerGroup ( STAR { ++numPtrs; } )+ )
    { e = fac.op (new CILDeclarationPointerOp (numPtrs)); }
  ;

idList
        :       ID ( COMMA ID )*
        ;

initializer [ExprFactory fac] returns [Expr e]
  { e = null; }
  : // #( NInitializer (initializerElementLabel)? expr )
    //                  ^^^ XXX what is this?
       #( NInitializer e = expr [fac] )
    | e = lcurlyInitializer [fac]
  ;

/*
initializerElementLabel
        :   #( NInitializerElementLabel
                (
                    ( LBRACKET expr RBRACKET (ASSIGN)? )
                    | ID COLON
                    | DOT ID ASSIGN
                )
            )
        ;
*/

lcurlyInitializer [ExprFactory fac] returns [Expr e]
  { e = null; }
  : #( NLcurlyInitializer
       e = initializerList [fac]
       RCURLY
     )
  ;

initializerList [ExprFactory fac] returns [Expr e]
  {
    e = null;
    List initList = new LinkedList ();
    Expr init;
  }
  : (
      init = initializer [fac]
      { initList.add (init); }
    )*
  ;

declarator [ExprFactory fac] returns [Expr e]
  {
    e = null;
    Expr nestedDeclarator;
    // Expr parmList = fac.op (CILNullOp.NULL);
    Expr parmList = null;
    // Expr pg = fac.op (CILNullOp.NULL);
    Expr pg = null;
  }
  : #( NDeclarator
        ( pg = pointerGroup [fac] )?

        ( id:ID
        | LPAREN
          nestedDeclarator = declarator [fac]
          RPAREN
          // most likely function pointer declaration (what else could it be?)
        )

        (   #( NParameterTypeList
              (
                parmList = parameterTypeList [fac]
                // | (idList)? // removed, CIL: parameters must be typed (?)
              )? // added "?"
              RPAREN
            )
         // | LBRACKET ( expr )? RBRACKET // XXX handle arrays later
        )* // XXX unlikely that we'll encounter more than one parameter list;
           // multi-dimensional arrays are likely though
     )
    {
      if (id == null)
        e = fac.op (CILListOp.LIST).naryApply
            (new Expr[] {pg, fac.var ("function pointer"), parmList});
                                   // ^^^ for now XXX
      else      
        e = fac.op (CILListOp.LIST).naryApply
              (new Expr[] {pg, fac.var (id.getText ()), parmList});
    }
  ;

parameterTypeList [ExprFactory fac] returns [Expr e]
  {
    // e = fac.op (CILNullOp.NULL);
    e = null;
    Expr parm;
    List parmList = new LinkedList ();
  }
  : (
      parm = parameterDeclaration [fac] ( COMMA | SEMI )?
      { parmList.add (parm); }
    )+ ( VARARGS )?
    { e = fac.op (CILListOp.LIST).naryApply (parmList); }
  ;

parameterDeclaration [ExprFactory fac] returns [Expr e]
  {
    // e = fac.op (CILNullOp.NULL);
    e = null;
    Expr specs;
    // Expr sig = fac.op (CILNullOp.NULL);
    Expr sig = null;
    Expr discard;
  }
  : #( NParameterDeclaration
    specs = declSpecifiers [fac]
    // ( declarator | nonemptyAbstractDeclarator)? // XXX what is this?
    ( nonemptyAbstractDeclarator [fac] )? // XXX what is this?
    ( sig = declarator [fac] )?
    )
    { e = fac.op (CILDeclarationOp.DECL).naryApply (new Expr[] {specs, sig}); }
  ;

functionDef [ExprFactory fac] returns [Expr e]
  {
    e = null;
    Expr specs = null;
    Expr body = null;
    Expr sig;
  }
  : #( NFunctionDef
       ( specs = functionDeclSpecifiers [fac] )? 
       sig = declarator [fac]
       // ( declaration [fac] | VARARGS )* XXX old-style parameter decls
       body = compoundStatement [fac]
     )
  { e = fac.op (CILFunctionDefOp.FUNCTION_DEF).naryApply
                  (new Expr[]
                    {
                      specs,
                      sig,
                      body,
                      fac.intExpr (getNumCallSites (
                        CILFunctionDefOp.getFunctionNameFromSig (sig))),
                      fac.intExpr (##.getLineNum ())
                    }
                  );
  }
  ;

functionDeclSpecifiers [ExprFactory fac] returns [Expr e]
  {
    e = null;
    List declSpecList = new LinkedList ();
    Expr fscs;
    Expr tq;
    Expr ts;
  }
  : ( fscs = functionStorageClassSpecifier [fac]
      { declSpecList.add (fscs); }
    | tq = typeQualifier [fac]
      { declSpecList.add (tq); }
    | ts = typeSpecifier [fac]
      { declSpecList.add (ts); }
    )+
    { e = fac.op (CILListOp.LIST).naryApply (declSpecList); }
  ;

declarationList [ExprFactory fac] returns [Expr e]
  {
    List declList = new LinkedList ();
    e = null;
    Expr decl;
  }
  :       
  (
    //ANTLR doesn't know that declarationList properly eats all the declarations
    //so it warns about the ambiguity
    options { warnWhenFollowAmbig = false; } :
    localLabelDecl
    { declList.add (##); }
  | decl = declaration [fac]
    { declList.add (decl); }
  )+
  { e = fac.op (CILListOp.LIST).naryApply (declList); }
  ;

localLabelDecl
        :   #("__label__" (ID)+ )
        ;
   
compoundStatement [ExprFactory fac] returns [Expr e]
  {
    e = null;
    Expr declList = null;
    Expr stmtList = null;
  }
  : #( NCompoundStatement
       /* XXX no nested function definitions
       ( declarationList
       | functionDef [fac]
       )*
       */
       ( declList = declarationList [fac] )?
       ( stmtList = statementList [fac] )?
       RCURLY
     )
    {
      /*
      if (declList == null)
        declList = fac.op (CILNullOp.NULL);
      if (stmtList == null)
        stmtList = fac.op (CILNullOp.NULL);
      */
      e = fac.op (CILScopeOp.SCOPE).binApply (declList, stmtList);
    }
  ;

statementList [ExprFactory fac] returns [Expr e] // e is a LIST
  {
    e = null;
    List stmtList = new LinkedList ();
    List curSList = new LinkedList ();
    Expr stmt;
  }
  : (
      stmt = statement [fac]
      {
        if (stmtBlocking)
        {
          // -- merge simple assignments
          if (stmt != null &&
            stmt.op () == CILAssignOp.ASSIGN &&
            stmt.arg (1) != null && // XXX remove this when null ops are gone
            stmt.arg (1).op () != CILFunctionCallOp.FCALL)
          {
            curSList.add (stmt);
          }
          else
          {
            // -- encountered unmergeable statement
            // -- form SLIST from curSList, add to stmtList, and re-initialize
            // former; XXX should factor this out into a method, since it's
            // re-used below
            if (!curSList.isEmpty ())
            {
              stmtList.add (fac.op (CILListOp.SLIST).naryApply (curSList));
              curSList = new LinkedList ();
            }
            // add stmt to stmtList (as the "sibling" of curSList)
            stmtList.add (stmt);
          }
        }
        else
        {
          stmtList.add (stmt);
        }
      }
    )+
    {
      // -- reached end of statements comprising this statementList
      // -- form SLIST from curSList, add to stmtList, and re-initialize
      // former
      if (stmtBlocking)
      {
        if (!curSList.isEmpty ())
        {
          stmtList.add (fac.op (CILListOp.SLIST).naryApply (curSList));
          // curSList = new LinkedList (); // XXX not necessary
        }
      }

      e = fac.op (CILListOp.LIST).naryApply (stmtList);
    }
  ;

statement [ExprFactory fac] returns [Expr e]
  { e = null; }
  : e = statementBody [fac]
  ;

statementBody [ExprFactory fac] returns [Expr e]
  {
    Expr discard;
    e = null;
    // Expr e1 = fac.op (CILNullOp.NULL);
    Expr e1 = null;
    Expr s1 = null;
    // Expr s2 = fac.op (CILNullOp.NULL);
    Expr s2 = null;
  }
        : (     
                SEMI                             // Empty statements
                { e = fac.op (CILEmptyStmtOp.EMPTY).
                     unaryApply (fac.intExpr (##.getLineNum ())); }
// Group of statements
        |       e = compoundStatement [fac]      
// Expressions
        |       #(NStatementExpr e = expr [fac]) 

// Iteration statements:

        | #( "while" e1 = expr [fac] s1 = statement [fac] )
          { e = fac.op (CILWhileOp.WHILE).naryApply 
           (new Expr[] {e1, s1, 
                        fac.intExpr (##.getLineNum ())}); }

// Jump statements:

        | #( "goto" id1:ID )
          {
            assert !id1.getText ().equals ("") && id1.getText () != null :
            id1;
            e = fac.op (CILGotoOp.GOTO).binApply 
              (fac.var (id1.getText ()), 
               fac.intExpr (##.getLineNum ())); }
        | "break"
          { e = fac.op (CILBreakOp.BREAK).unaryApply 
                    (fac.intExpr (##.getLineNum ())); }
        | #( "return" ( e1 = expr [fac] )? )
          { e = fac.op (CILReturnOp.RETURN).binApply 
            (e1, fac.intExpr (##.getLineNum ())); }

// Labeled statements:
        | #( NLabel id:ID (e1 = statement [fac])? )
          {
            e = fac.op (CILLabelledStmtOp.LSTMT).naryApply
              (new Expr[] {fac.var (id.getText ()), e1, 
                           fac.intExpr (##.getLineNum ())});
          }

// Selection statements:

        | #( "if"
              e1 = expr [fac] s1 = statement [fac]
              ( "else" s2 = statement [fac] )?
           )
           {
            e = fac.op (CILIfStmtOp.IF_THEN_ELSE).naryApply
              (new Expr[] {e1, s1, s2, 
                           fac.intExpr (##.getLineNum ())});
           }

// Non-deterministic goto:

       | #( "__nd_goto" e1 = ndGotoLabelList [fac] )
          {
            e = fac.op (CILNDGotoOp.NDGOTO).
               binApply (e1, fac.intExpr (##.getLineNum ()));
          }
       )
       {
         // if (e == null) System.out.println ("got a null e for " + ##);
       }
       ;

ndGotoLabelList [ExprFactory fac] returns [Expr e]        
  {
    e = null;
    List stringList = new LinkedList ();
  }
  : (
      s:StringLiteral
      { stringList.add (fac.var (s.getText ())); }
    )+
    { e = fac.op (CILListOp.LIST).naryApply (stringList); }
  ;

expr [ExprFactory fac] returns [Expr e]
  { e = null; }
  : e = assignExpr [fac]
  | e = logicalOrExpr [fac]
  | e = logicalAndExpr [fac]
  | inclusiveOrExpr [fac]
  | exclusiveOrExpr [fac]
  | bitAndExpr [fac]
  | shiftExpr [fac]
  | e = equalityExpr [fac]
  | e = relationalExpr [fac]
  | e = additiveExpr [fac]
  | e = multExpr [fac]
  | e = castExpr [fac]
  | e = unaryExpr [fac]
  | e = postfixExpr [fac]
  | e = primaryExpr [fac]
  | e = initializer [fac]
  // | rangeExpr
  | gnuAsmExpr [fac]
  | e = compoundStatementExpr [fac]
  ;

compoundStatementExpr [ExprFactory fac] returns [Expr e]
  { e = null; }
  : #(LPAREN e = compoundStatement [fac] RPAREN)
  ;

/*
rangeExpr
        :   #(NRangeExpr expr VARARGS expr)
        ;
*/

gnuAsmExpr [ExprFactory fac]
        :   #(NGnuAsmExpr
                ("volatile")? 
                LPAREN stringConst
                ( options { warnWhenFollowAmbig = false; }:
                  COLON (strOptExprPair [fac] ( COMMA strOptExprPair [fac])* )?
                  ( options { warnWhenFollowAmbig = false; }:
                    COLON (strOptExprPair [fac] ( COMMA strOptExprPair [fac])* )?
                  )?
                )?
                ( COLON stringConst ( COMMA stringConst)* )?
                RPAREN
            )
        ;

strOptExprPair [ExprFactory fac]
  { Expr discard; }
        :  stringConst ( LPAREN discard = expr [fac] RPAREN )?
        ;
        
assignExpr [ExprFactory fac] returns [Expr e]
  {
    e = null;
    Expr e1;
    Expr e2;
  }
  : #( ASSIGN e1 = expr [fac] e2 = expr [fac])
    { //e = fac.op (CILAssignOp.ASSIGN).binApply (e1, e2); 
      e = fac.op (CILAssignOp.ASSIGN).naryApply 
            (new Expr[] {e1, e2, fac.intExpr (##.getLineNum ())});
    }
  ;

/*
conditionalExpr
        :       #( QUESTION expr (expr)? COLON expr )
        ;
*/        

logicalOrExpr [ExprFactory fac] returns [Expr e]
  {
    e = null;
    Expr e1;
    Expr e2;
  }
  : #( LOR e1 = expr [fac] e2 = expr [fac] ) 
    { e = fac.op (BoolOp.OR).binApply (e1, e2); }
  ;

logicalAndExpr [ExprFactory fac] returns [Expr e]
  {
    e = null;
    Expr e1;
    Expr e2;
  }
  : #( LAND e1 = expr [fac] e2 = expr [fac] )
    { e = fac.op (BoolOp.AND).binApply (e1, e2); }
  ;

inclusiveOrExpr [ExprFactory fac] returns [Expr e]
  {
    e = null;
    Expr e1;
    Expr e2;
  }
  : #( BOR e1 = expr [fac] e2 = expr [fac] )
    { e = fac.op (CILBitwiseOp.BOR).binApply (e1, e2); }
  ;

exclusiveOrExpr [ExprFactory fac] returns [Expr e]
  {
    e = null;
    Expr e1;
    Expr e2;
  }
  : #( BXOR e1 = expr [fac] e2 = expr [fac] )
    { e = fac.op (CILBitwiseOp.BXOR).binApply (e1, e2); }
  ;

bitAndExpr [ExprFactory fac] returns [Expr e]
  {
    e = null;
    Expr e1;
    Expr e2;
  }
  : #( BAND e1 = expr [fac] e2 = expr [fac] )
    { e = fac.op (CILBitwiseOp.BAND).binApply (e1, e2); }
  ;

equalityExpr [ExprFactory fac] returns [Expr e]
  {
    e = null;
    Expr e1;
    Expr e2;
  }
  : #( EQUAL e1 = expr [fac] e2 = expr [fac])
    { e = fac.op (ComparisonOp.EQ).binApply (e1, e2); }
  | #( NOT_EQUAL e1 = expr [fac] e2 = expr [fac])
    {
      e = fac.op (BoolOp.NOT).unaryApply 
        (fac.op (ComparisonOp.EQ).binApply (e1, e2));
    }
  ;

relationalExpr [ExprFactory fac] returns [Expr e]
  {
    e = null;
    Expr e1;
    Expr e2;
  }
  : #( LT e1 = expr [fac] e2 = expr [fac])
    { e = fac.op (ComparisonOp.LT).binApply (e1, e2); }
  | #( LTE e1 = expr [fac] e2 = expr [fac])
    { e = fac.op (ComparisonOp.LEQ).binApply (e1, e2); }
  | #( GT e1 = expr [fac] e2 = expr [fac])
    { e = fac.op (ComparisonOp.GT).binApply (e1, e2); }
  | #( GTE e1 = expr [fac] e2 = expr [fac])
    { e = fac.op (ComparisonOp.GEQ).binApply (e1, e2); }
  ;

shiftExpr [ExprFactory fac] returns [Expr e]
  {
    e = null;
    Expr e1;
    Expr e2;
  }
  : #( LSHIFT e1 = expr [fac] e2 = expr [fac])
    { e = fac.op (CILBitwiseOp.LSHIFT).binApply (e1, e2); }
  | #( RSHIFT e1 = expr [fac] e2 = expr [fac])
    { e = fac.op (CILBitwiseOp.RSHIFT).binApply (e1, e2); }
  ;

additiveExpr [ExprFactory fac] returns [Expr e]
  {
    e = null;
    Expr e1;
    Expr e2;
  }
  : #( PLUS e1 = expr [fac] e2 = expr [fac])
    { e = fac.op (NumericOp.PLUS).binApply (e1, e2); }
  | #( MINUS e1 = expr [fac] e2 = expr [fac])
    { e = fac.op (NumericOp.MINUS).binApply (e1, e2); }
  ;

multExpr [ExprFactory fac] returns [Expr e]
  {
    e = null;
    Expr e1;
    Expr e2;
  }
  : #( STAR e1 = expr [fac] e2 = expr [fac])
    { e = fac.op (NumericOp.MULT).binApply (e1, e2); }
  | #( DIV e1 = expr [fac] e2 = expr [fac])
    { e = fac.op (NumericOp.DIV).binApply (e1, e2); }
  | #( MOD e1 = expr [fac] e2 = expr [fac])
    { e = fac.op (NumericOp.MOD).binApply (e1, e2); }
  ;

castExpr [ExprFactory fac] returns [Expr e]
  { e = null; }
  : #( NCast typeName [fac] RPAREN e = expr [fac] )
  ;

typeName [ExprFactory fac]
  { Expr discard; }
  : discard = specifierQualifierList [fac] (nonemptyAbstractDeclarator [fac])?
  ;

nonemptyAbstractDeclarator [ExprFactory fac]
  { Expr discard; }
        :   #( NNonemptyAbstractDeclarator
            (   discard = pointerGroup [fac]
                (   (LPAREN  
                    (   nonemptyAbstractDeclarator [fac]
                        | discard = parameterTypeList [fac]
                    )?
                    RPAREN)
                | (LBRACKET (discard = expr [fac])? RBRACKET)
                )*

            |  (   (LPAREN  
                    (   nonemptyAbstractDeclarator [fac]
                        | discard = parameterTypeList [fac]
                    )?
                    RPAREN)
                | (LBRACKET (discard = expr [fac])? RBRACKET)
                )+
            )
            )
        ;

unaryExpr [ExprFactory fac] returns [Expr e]
  {
    e = null;
    Operator op;
    Expr e1;
    Expr discard;
  }
  : #( INC e1 = expr [fac] )
    {
      e = fac.op (CILAssignOp.ASSIGN).binApply
        (e1, fac.op (NumericOp.PLUS).binApply (e1, fac.intExpr (1)));
    }
  | #( DEC e1 = expr [fac] )
    {
      e = fac.op (CILAssignOp.ASSIGN).binApply
        (e1, fac.op (NumericOp.MINUS).binApply (e1, fac.intExpr (1)));
    }
  | #( NUnaryExpr op = unaryOperator e1 = expr [fac] )
    { e = fac.op (op).unaryApply (e1); }
  | #( "sizeof"
        ( ( LPAREN typeName [fac] )=> LPAREN typeName [fac] RPAREN
        | discard = expr [fac]
        )
    )
  /*
  | #( "__alignof"
        ( ( LPAREN typeName )=> LPAREN typeName RPAREN
        | expr
        )
    )
  */
  ;

unaryOperator returns [Operator op]
  { op = CILUnknownOp.UNKNOWN; } // XXX catch-all for unhandled cases
  : 
  ( BAND
    { op = CILIndirectionOp.ADDR_OF; }
  | STAR
    { op = CILIndirectionOp.DEREF; }
  | PLUS
  | MINUS
    { op = NumericOp.UN_MINUS; }
  | BNOT
  | LNOT
    { op = BoolOp.NOT; }
  | LAND
  | "__real"
  | "__imag"
  )
  ;

postfixExpr [ExprFactory fac] returns [Expr e]
  {
    e = null;
    Expr discard;
    Expr ael = null;
  }
  : #( NPostfixExpr
        e = primaryExpr [fac]
        ( PTR id1:ID
          {
            e = fac.op (CILIndirectionOp.ARROW).binApply
              (e, fac.var (id1.getText ()));
          }
        | DOT id2:ID
          {
            e = fac.op (CILIndirectionOp.DOT).binApply
              (e, fac.var (id2.getText ()));
          }
        | // function call  
          #( NFunctionCallArgs (ael = argExprList [fac])? RPAREN )
          {
			// XXX function pointers not supported
			if (e.op () instanceof CILIndirectionOp)
					return null;

            if (!(e.op () instanceof VariableOp))
              throw new RecognitionException
                ("<<<\n" + e + "(" + e.op ().getClass () + ")\n" + ## + "\n>>>\n");
            String funcName = ((VariableOp) e.op ()).name ();
            incNumCallSites (funcName);
            /*
            System.err.println ("Parser: Making function call for: " +
                                funcName + ", " +
                                (getNumCallSites (funcName) - 1));
            */
            e = fac.op (CILFunctionCallOp.FCALL).naryApply
              (new Expr[]
                {fac.var (funcName), ael, null,
                 fac.intExpr (getNumCallSites (funcName) - 1), 
                 fac.intExpr (##.getLineNum ())});
                 // XXX 0-based
          }
        | LBRACKET discard = expr [fac] RBRACKET
          // array element access
        )* // separated operators, since INC/DEC have side effects, whereas
           // ops above do not
        ( INC
          {
            e = fac.op (CILAssignOp.ASSIGN).binApply
              (e, fac.op (NumericOp.PLUS).binApply (e, fac.intExpr (1)));
          }
        | DEC
          {
            e = fac.op (CILAssignOp.ASSIGN).binApply
              (e, fac.op (NumericOp.MINUS).binApply (e, fac.intExpr (1)));
          }
        )?
        // will never mix INC/DEC with other operators?
    )
  ;

primaryExpr [ExprFactory fac] returns [Expr e]
  { e = null; }
  : ID
    { e = fac.var (##.getText ()); }
  | Number
    {
      try
      {
        e = fac.intExpr (Integer.parseInt (##.getText ()));
      }
      catch (NumberFormatException ex)
      {
        // XXX Use 0 for now, if we couldn't parse it as an int above
        e = fac.intExpr (0);
      }
    }
  | charConst
    { e = fac.op (new JavaObjectOp (##)); }
  | stringConst
    { e = fac.op (new JavaObjectOp (##)); }
  | #( NExpressionGroup e = expr [fac] )
  ;

argExprList [ExprFactory fac] returns [Expr e]
  {
    e = null;
    List argList = new LinkedList ();
    Expr arg;
  }
  : (
      arg = expr [fac]
      { argList.add (arg); }
    )+
    { e = fac.op (CILListOp.LIST).naryApply (argList); }
  ;

protected
charConst
        :       CharLiteral
        ;

protected
stringConst
        :       #(NStringSeq (StringLiteral)+)
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

protected
floatConst
        :       FloatDoubleConst
        |       DoubleDoubleConst
        |       LongDoubleConst
        ;

