header { package edu.toronto.cs.cparser; }

{
import java.io.*;

import antlr.CommonAST;
import antlr.DumpASTVisitor;

import edu.toronto.cs.cparser.block.*;
}
                     
class CILParser extends Parser;

options
{
  k = 2;
  exportVocab = CIL;
  buildAST = true;
  ASTLabelType = "Block";
  defaultErrorHandler = false;

  // Copied following options from java grammar.
  codeGenMakeSwitchThreshold = 2;
  codeGenBitsetTestThreshold = 3;
}


{
  // Suppport C++-style single-line comments?
  public static boolean CPPComments = true;

  // access to symbol table
  public CSymbolTable symbolTable = new CSymbolTable();

  // source for names to unnamed scopes
  protected int unnamedScopeCounter = 0;

  public boolean isTypedefName(String name) {
    boolean returnValue = false;
    TNode node = symbolTable.lookupNameInCurrentScope(name);
    //System.out.println ("Looking up and got: ");
    //TNode.printTree (node);
    if (node == null)
    {
      System.err.println ("Failed looking up " + name);
      return false;
    }

    node = (TNode) node.getFirstChild ().getFirstChild ();

    //System.out.println ("First child is: ");
    //TNode.printTree (node);
    
    for (; node != null; node = (TNode) node.getNextSibling() ) {
      if(node.getType() == LITERAL_typedef) {
        returnValue = true;
        break;
      }
    }
    //System.out.println (name + " is a typedef? " + returnValue);
    //System.out.println ("The symbol table is....");
    //System.out.println (symbolTable);
    return returnValue;
  }


  public String getAScopeName() {
    return "" + (unnamedScopeCounter++);
  }

  public void pushScope(String scopeName) {
    symbolTable.pushScope(scopeName);
  }

  public void popScope() {
    symbolTable.popScope();
  }

  int traceDepth = 0;
  public void reportError(RecognitionException ex) {
    try {
      System.err.println("ANTLR Parsing Error: "+ex + " token name:" + tokenNames[LA(1)]);
      ex.printStackTrace(System.err);
    }
    catch (TokenStreamException e) {
      System.err.println("ANTLR Parsing Error: "+ex);
      ex.printStackTrace(System.err);              
    }
  }
  public void reportError(String s) {
    System.err.println("ANTLR Parsing Error from String: " + s);
  }
  public void reportWarning(String s) {
    System.err.println("ANTLR Parsing Warning from String: " + s);
  }
  public void match(int t) throws MismatchedTokenException {
    boolean debugging = false;

    if ( debugging ) {
      for (int x=0; x<traceDepth; x++) System.out.print(" ");
      try {
        System.out.println("Match("+tokenNames[t]+") with LA(1)="+
            tokenNames[LA(1)] + ((inputState.guessing>0)?" [inputState.guessing "+ inputState.guessing + "]":""));
      }
      catch (TokenStreamException e) {
        System.out.println("Match("+tokenNames[t]+") " + ((inputState.guessing>0)?" [inputState.guessing "+ inputState.guessing + "]":""));

      }

    }
    try {
      if ( LA(1)!=t ) {
        if ( debugging ){
          for (int x=0; x<traceDepth; x++) System.out.print(" ");
          System.out.println("token mismatch: "+tokenNames[LA(1)]
              + "!="+tokenNames[t]);
        }
        throw new MismatchedTokenException(tokenNames, LT(1), t, false, getFilename());

      } else {
        // mark token as consumed -- fetch next token deferred until LA/LT
        consume();
      }
    }
    catch (TokenStreamException e) {
    }

  }
  public void traceIn(String rname) {
    traceDepth += 1;
    for (int x=0; x<traceDepth; x++) System.out.print(" ");
    try {
      System.out.println("> "+rname+"; LA(1)==("+ tokenNames[LT(1).getType()] 
          + ") " + LT(1).getText() + " [inputState.guessing "+ inputState.guessing + "]");
    }
    catch (TokenStreamException e) {
    }
  }
  public void traceOut(String rname) {
    for (int x=0; x<traceDepth; x++) System.out.print(" ");
    try {
      System.out.println("< "+rname+"; LA(1)==("+ tokenNames[LT(1).getType()] 
          + ") "+LT(1).getText() + " [inputState.guessing "+ inputState.guessing + "]");
    }
    catch (TokenStreamException e) {
    }
    traceDepth -= 1;
  }

}

// PARSER DEFINITION BEGINS

translationUnit
        :       externalList
                {
                  ## = #(#[NProgram], ##);
                }

        |       /* Empty source files are *not* allowed.  */
                {
                System.err.println ( "Empty source file!" );
                }
        ;


externalList
        :       ( externalDef )+
        ;


externalDef
        : ( "typedef" | declaration )=> declaration
        | ( "__asm__" ) => gnuAsmExpr! SEMI!
        | functionDef
        ;

declaration!
        { String declName = ""; }
        :       ds:declSpecifiers
                (
                  declName = d:declarator [false]
                  {
                    /*
                    AST ds1, d1;
                    ds1 = astFactory.dupList (#ds);
                    d1 = astFactory.dupList (#d);
                    symbolTable.add ( declName, #(null, ds1, d1) );
                    */
                  }
                )?
                ( attributeDecl! )?
                (
                  ASSIGN
                  i:initializer
                )?
                SEMI!
                {
                  ## = #( #[NDeclaration], #ds, #d, #i );
                  symbolTable.add ( declName, ## );
                }
        ;

declSpecifiers 
                                { int specCount=0; }
        :       (               options { // this loop properly aborts when
                                          //  it finds a non-typedefName ID MBZ
                                          warnWhenFollowAmbig = false;
                                        } :
                  s:storageClassSpecifier
                | typeQualifier
                | ( "struct" | "union" | "enum" | typeSpecifier[specCount] )=>
                        specCount = typeSpecifier[specCount]
                | pointerGroup // want to associate pointer symbols with
                               // the decl specifiers (i.e. type), not with
                               // the declaration name        
                )+
                  { ## = #(#[NDeclSpecifiers], ##); }
        ;

storageClassSpecifier
        :       "auto"                  
      |       "register"              
      |       "typedef"               
        |       functionStorageClassSpecifier
        ;


functionStorageClassSpecifier
        :       "extern"
        |       "static"
        |       "__inline"
        ;


typeQualifier
        :       "const"
        |       "volatile"
        ;

typeSpecifier [int specCount] returns [int retSpecCount]
                                                        { retSpecCount = specCount + 1; }
        :
        (       "void"
        |       "char"
        |       "short"
        |       "int"
        |       "long"
        |       "float"
        |       "double"
        |       "signed"
        |       "unsigned"
        |       structOrUnionSpecifier
                ( options { warnWhenFollowAmbig = false; }:
                  attributeDecl
                )*
        |       enumSpecifier
        |       { specCount == 0 }? typedefName
        )
        ;


typedefName
        :       { isTypedefName ( LT(1).getText() ) }?
                i:ID                    { ## = #(#[NTypedefName], #i); }
        ;

structOrUnionSpecifier!
                                        { String scopeName; }
        :       sou:structOrUnion!
                ( ( ID LCURLY )=> id:ID LCURLY!
                    // struct type declaration
                    // e.g. struct MyStruct { int x; }
                    {
                      scopeName = #sou.getText() + " " + #id.getText();
                      pushScope(scopeName);
                    }
                  s:structDeclarationList
                    {
                      popScope();
                    }
                  RCURLY!
                  {
                    ## = #( #sou, #id, #( #[NStructFields], #s ) );

                    // -- store type declaration in symbol table
                    symbolTable.add (id.getText (), ##);
                  }
                /* XXX No anonymous struct decls in CIL
                | LCURLY!
                    {
                      scopeName = getAScopeName();
                      // #l1.setText(scopeName);
                      pushScope(scopeName);
                    }
                  structDeclarationList
                    { popScope(); }
                  RCURLY!
                */
                | id1:ID
                  // struct instantiation
                  // e.g. struct MyStruct foo;
                  { ## = #( #sou, #id1 ); }
                )
        ;


structOrUnion
        :       "struct"
        |       "union"
        ;


structDeclarationList
        :       ( structDeclaration )+
        ;


structDeclaration
        :       specifierQualifierList structDeclaratorList ( SEMI! )+
        ;


specifierQualifierList
                                { int specCount = 0; }
        :       (               options {   // this loop properly aborts when
                                            // it finds a non-typedefName ID MBZ
                                            warnWhenFollowAmbig = false;
                                        } :
                ( "struct" | "union" | "enum" | typeSpecifier[specCount] )=>
                        specCount = typeSpecifier[specCount]
                | typeQualifier
                | pointerGroup
                )+
        ;


structDeclaratorList
        :       structDeclarator ( COMMA! structDeclarator )*
        ;


structDeclarator
        : ( declarator[false] )?
          ( COLON pureExpr )?
          ( attributeDecl )*
          { ## = #( #[NStructDeclarator], ##); }
        ;

enumSpecifier
        :       "enum"^
                ( ( ID LCURLY )=> i:ID LCURLY enumList[i.getText()] RCURLY!
                | LCURLY enumList["anonymous"] RCURLY!
                | ID
                )
        ;


enumList[String enumName]
        :       enumerator[enumName] ( COMMA! enumerator[enumName] )*  
        ;

enumerator[String enumName]
        :       i:ID                { symbolTable.add(  i.getText(),
                                                        #(   null,
                                                            #[LITERAL_enum, "enum"],
                                                            #[ ID, enumName]
                                                         )
                                                     );
                                    }
                (ASSIGN pureExpr)?
        ;


/*
initDeclList[AST declarationSpecifiers]
        :       initDecl[declarationSpecifiers] 
                // ( COMMA! initDecl[declarationSpecifiers] )*
        ;


initDecl[AST declarationSpecifiers]
                                        { String declName = ""; }
        :       declName = d:declarator[false]
                          {   AST ds1, d1;
                              ds1 = astFactory.dupList(declarationSpecifiers);
                              d1 = astFactory.dupList(#d);
                              symbolTable.add(declName, #(null, ds1, d1) );
                          }
                ( attributeDecl )*          
                ( ASSIGN initializer // Global initialized variables cannot
                                     // be separated by CIL
                | COLON pureExpr     // Struct field initializer?
                )?
                                        { ## = #( #[NInitDecl], ## ); }

        ;
*/

pointerGroup
        :       ( STAR ( typeQualifier )* )+    { ## = #( #[NPointerGroup], ##); }
        ;

attributeDecl
        :       "__attribute__"^ LPAREN LPAREN attributeList RPAREN RPAREN
                | "asm"^ LPAREN stringConst RPAREN { ##.setType( NAsmAttribute ); }
        ;        

attributeList
        :       attribute ( options{warnWhenFollowAmbig=false;}: COMMA attribute)*  ( COMMA )?
        ;

attribute
        :       ( ~(LPAREN | RPAREN | COMMA)
                |  LPAREN attributeList RPAREN
                )*
        ;

idList
        :       ID ( COMMA! ID )*
        ;


initializer
        :       ( pureExpr
                    { ## = #( #[NInitializer], ## ); }
                | LCURLY! initializerList ( COMMA! )? RCURLY!
                    { ## = #( #[NLCurlyInitializer], ##); }
                )
        ;

initializerList
        :       initializer ( COMMA! initializer )*
        ;

declarator[boolean isFunctionDefinition] returns [String declName]
                                                { declName = ""; }
        :
                // -- we want pointers to be associated with the type, not the
                // name
                // restored for function pointer declaration
                ( pointerGroup )?

                ( id:ID
                  {
                    // *** don't mess with returned name; symbolTable expects
                    // declName to be unscoped
                    declName = id.getText();

                    // OK to modify text since symbolTable does not use it
                    #id.setText
                      (symbolTable.addCurrentScopeToName (id.getText ()));
                  }
                | LPAREN declName = declarator[false] RPAREN
                )

                ( !  LPAREN
                                                { 
                                                    if (isFunctionDefinition) {
                                                        pushScope(declName);
                                                    }
                                                    else {
                                                        pushScope("!"+declName); 
                                                    }
                                                }
                    (                           
                        (declSpecifiers)=> p:parameterTypeList
                                                {
                                                ## = #( null, ##, #( #[NParameterTypeList], #p ) );
                                                }

                        | (i:idList)?
                                                {
                                                ## = #( null, ##, #( #[NParameterTypeList], #i ) );
                                                }
                    )
                                                {
                                                popScope();
                                                }    
                  RPAREN                        
                | LBRACKET ( pureExpr )? RBRACKET
                )*
                                                { ## = #( #[NDeclarator], ## ); }
        ;
 
parameterTypeList
        :       parameterDeclaration
                (   options {
                            warnWhenFollowAmbig = false;
                        } : 
                  COMMA!
                  parameterDeclaration
                )*
                ( COMMA!
                  VARARGS
                )?
        ;


parameterDeclaration
                            { String declName = ""; }
        :       ds:declSpecifiers
                ( ( declarator[false] )=> declName = d:declarator[false]
                            {
                              /*
                            AST d2, ds2;
                            d2 = astFactory.dupList(#d);
                            ds2 = astFactory.dupList(#ds);
                            symbolTable.add(declName, #(null, ds2, d2));
                            */
                            }
                | nonemptyAbstractDeclarator
                )?
                            {
                            ## = #( #[NParameterDeclaration], ## );
                            symbolTable.add (declName, ##);
                            }
        ;

/* JTC:
 * This handles both new and old style functions.
 * see declarator rule to see differences in parameters
 * and here (declaration SEMI)* is the param type decls for the
 * old style.  may want to do some checking to check for illegal
 * combinations (but I assume all parsed code will be legal?)
 */

functionDef
  { String declName; }
  : // CIL function defs must have a return type
    (functionDeclSpecifiers) => ds:functionDeclSpecifiers
    declName = d:declarator[true]
      {
        /*
      AST d2, ds2;
      d2 = astFactory.dupList(#d);
      ds2 = astFactory.dupList(#ds);
      symbolTable.add(declName, #(null, ds2, d2));
      */
      pushScope(declName);
      }
    // ( declaration )* (VARARGS)? ( SEMI! )*
      { popScope(); }
    scope[declName]
      {
        ## = #( #[NFunctionDef], ## );
        symbolTable.add (declName, ##);
      }
  ;

functionDeclSpecifiers
                                { int specCount = 0; }
        :       (               options {   // this loop properly aborts when
                                            // it finds a non-typedefName ID MBZ
                                            warnWhenFollowAmbig = false;
                                        } :
                  functionStorageClassSpecifier
                | typeQualifier
                | ( "struct" | "union" | "enum" | typeSpecifier[specCount] )=>
                        specCount = typeSpecifier[specCount]
                )+
                ( pointerGroup )? // Added this so function return type (i.e.
                                  // this rule) eats up pointers, not function
                                  // name (i.e. declarator rule)
                { ## = #(#[NDeclSpecifiers], ##); }
        ;

declarationList
        :       (               options {   // this loop properly aborts when
                                            // it finds a non-typedefName ID MBZ
                                            warnWhenFollowAmbig = false;
                                        } :
                ( declarationPredictor )=> declaration
                )+
        ;

declarationPredictor
        :       (options {      //only want to look at declaration if I don't see typedef
                    warnWhenFollowAmbig = false;
                }:
                "typedef"
                | declaration
                )
        ;


scope[String scopeName]
        : LCURLY! RCURLY!
          {
            ## = #(#[NScope, scopeName], #[NLocalDeclarations]);
          }
        |       LCURLY!
                            {
                                pushScope(scopeName);
                            }
                optLocalDeclarations
                ( statementList )?
                            { popScope(); }
                RCURLY!
                            { ## = #( #[NScope, scopeName], ##); }
        ;

optLocalDeclarations
  { ## = #[NLocalDeclarations]; }
  : ( ( declarationPredictor )=> declarationList )?
      { ## = #(#[NLocalDeclarations], ##); }
  ;
    
statementList
        :       ( stmt )+
        ;

// CIL Statements
stmt
  : functionCallStmt
  | assignStmt
  | scope[getAScopeName()]
  | labelledStmt
  | controlStmt
  | gnuAsmExpr! SEMI! // XXX accept but discard asm instructions
  | SEMI! // XXX accept but discard no-op statements
  ;

gnuAsmExpr
        :       "__asm__"^ ("volatile")? 
                LPAREN stringConst
                ( options { warnWhenFollowAmbig = false; }:
                  COLON (strOptExprPair ( COMMA strOptExprPair)* )?
                  ( options { warnWhenFollowAmbig = false; }:
                    COLON (strOptExprPair ( COMMA strOptExprPair)* )?
                  )?
                )?
                ( COLON stringConst ( COMMA stringConst)* )?
                RPAREN
                                { ##.setType(NGnuAsmExpr); }
        ;

//GCC requires the PARENs
strOptExprPair
        :  stringConst ( LPAREN pureExpr RPAREN )?
        ;

labelledStmt!
  : id:ID COLON! ( s:stmt )? // Matches empty labelled statement
    { ## = #(#[NLabelledStmt], #(#[NLabel], id), s); }
  ;

controlStmt  
  : "while"! LPAREN! pureExpr RPAREN! stmt
    { ## = #(#[NWhile], ##); }
  | "goto"! ID SEMI!
    { ## = #(#[NGoto], ##); }
  | "break"! SEMI!
    { ## = #(#[NBreak], ##); }
  | r:"return"! ( pureExpr )? SEMI!
    {
      ## = #(#[NReturn], ##);
      ##.setLineNum (r.getLine ());
    }
  | "if"!
     LPAREN! pureExpr RPAREN! stmt  
    ( //standard if-else ambiguity
            options {
                warnWhenFollowAmbig = false;
            } :
    "else"! stmt )?
    { ## = #(#[NIf], ##); }
  | "__nd_goto"! LPAREN! stringList RPAREN! SEMI!
    { ## = #(#[NNDGoto], ##); }
  ;

stringList
  : StringLiteral (COMMA! StringLiteral)*
  ;

functionCallStmt
  : functionCall SEMI!
    { ## = #(#[NFunctionCallStmt], ##); }
  ;

assignStmt
  // XXX changed lval to pureExpr to allow for more complex LHS
  : lval ASSIGN! ( functionCall
                 | pureExpr
                 )
    SEMI!
    { ## = #( #[NAssignStmt], ##); }
  | implicitAssignStmt  
  ;

implicitAssignStmt
  : lval (INC | DEC) SEMI! // CIL gets rid of prefix ++ and --
      { ## = #(#[NImplicitAssignStmt], ##); }
  ;

// An expression which yields an assignable memory location

/*
lval
  : id:ID ( arraySuffixes { ## = #(#[NArrayElement], ##); } )?
          (
            PTR^ lval
          | DOT^ lval
          // | ( arraySuffix )+ { ## = #(#[NArrayElement], ##); }
          )?
    {
      TNode declaration = symbolTable.lookupNameInCurrentScope(id.getText ());

      // declaration will be null if id is a struct field name (XXX really?)
      if (declaration != null)
        // -- replace text with fully scoped name
        #id.setText (declaration.getFirstChild ().
                                 getNextSibling ().
                                   getFirstChild ().getText ());
    }
  ;
*/

lval
  : id:ID ( lvalSuffixes { ## = #( #[NPostfixExpr], ## ); } )?
    {
      TNode declaration = symbolTable.lookupNameInCurrentScope(id.getText ());

      // declaration will be null if id is a struct field name (XXX really?)
      if (declaration != null)
        // -- replace text with fully scoped name
        #id.setText (declaration.getFirstChild ().
                                 getNextSibling ().
                                   getFirstChild ().getText ());
    }
  | pointerGroup pureExpr 
  | LPAREN! lval RPAREN! { ## = #( #[NPureExpressionGroup], ##); }
  ;

// casting
// lval arrow ID
// lval dot ID
// array
// star expr
// ID

primaryLval
  : ID
  ;  

lvalSuffixes
  : ( lvalSuffix )+  
  ;

lvalSuffix
  : LBRACKET! e:pureExpr RBRACKET! { ## = #( #[NArrayIndex], e ); }
  | ( PTR | DOT ) ID
  ;

// An expression which has no side effects and yields a value assignable to an
// lval.
pureExpr
  : pureLogicalOrExpr
  ;

pureLogicalOrExpr
  : pureLogicalAndExpr ( LOR^ pureLogicalAndExpr )*
  ;

pureLogicalAndExpr
  : pureInclusiveOrExpr ( LAND^ pureInclusiveOrExpr )*
  ;

pureInclusiveOrExpr
  : pureExclusiveOrExpr ( BOR^ pureExclusiveOrExpr )*
  ;

pureExclusiveOrExpr
  : pureBitAndExpr ( BXOR^ pureBitAndExpr )*
  ;

pureBitAndExpr
  : pureEqualityExpr ( BAND^ pureEqualityExpr )*
  ;

pureEqualityExpr
  : pureRelationalExpr ( ( EQUAL^ | NOT_EQUAL^ ) pureRelationalExpr )*
  ;

pureRelationalExpr
  : pureShiftExpr ( ( LT^ | LTE^ | GT^ | GTE^ ) pureShiftExpr )*
  ;

pureShiftExpr
  : pureAdditiveExpr ( ( LSHIFT^ | RSHIFT^ ) pureAdditiveExpr )*
  ;

pureAdditiveExpr
  : pureMultExpr ( ( PLUS^ | MINUS^ ) pureMultExpr )*
  ;

pureMultExpr
  : pureCastExpr ( ( STAR^ | DIV^ | MOD^ ) pureCastExpr)*
  ;

pureCastExpr
  : ( LPAREN typeName RPAREN )=>
    LPAREN! typeName RPAREN ( pureExpr )
      { ## = #( #[NCast, "("], ## ); }
  | pureUnaryExpr
  ;

pureUnaryExpr  
  // : purePostfixExpr
  : purePrimaryExpr
  | u:unaryOperator pureCastExpr { ## = #( #[NUnaryExpr], ## ); }
  | "sizeof"^
    ( ( LPAREN typeName )=> LPAREN typeName RPAREN
    | pureUnaryExpr
    )
  ;

purePrimaryExpr
        : charConst
        | intConst
        | floatConst
        | stringConst
        | LPAREN! pureExpr RPAREN!
            { ## = #( #[NPureExpressionGroup, "("], ## ); }
        | lval
        ;

typeName
        :       specifierQualifierList (nonemptyAbstractDeclarator)?
        ;

nonemptyAbstractDeclarator
        :   (
                pointerGroup
                (   (LPAREN  
                    (   nonemptyAbstractDeclarator
                        | parameterTypeList
                    )?
                    RPAREN)
                | (LBRACKET (pureExpr)? RBRACKET)
                )*

            |   (   (LPAREN  
                    (   nonemptyAbstractDeclarator
                        | parameterTypeList
                    )?
                    RPAREN)
                | (LBRACKET (pureExpr)? RBRACKET)
                )+
            )
                            {   ## = #( #[NNonemptyAbstractDeclarator], ## ); }
                                
        ;

/*
unaryExpr
        :       postfixExpr
        |       INC^ unaryExpr
        |       DEC^ unaryExpr
        |       u:unaryOperator pureCastExpr { ## = #( #[NUnaryExpr], ## ); }

        |       "sizeof"^
                ( ( LPAREN typeName )=> LPAREN typeName RPAREN
                | unaryExpr
                )
        ;
*/

unaryOperator
        :       BAND
        // |       STAR // moved into lval
        |       PLUS
        |       MINUS
        |       BNOT
        |       LNOT
        ;

purePostfixExpr
        :       lval
                ( 
                purePostfixSuffix {## = #( #[NPostfixExpr], ## );} 
                )?
        ;

purePostfixSuffix
        :
                /*
                ( functionCall // XXX Matches function call as expression ...
                | LBRACKET pureExpr RBRACKET
                )+
                */
                ( LBRACKET pureExpr RBRACKET )+ // array expression
        ;

functionCall
        : id:ID LPAREN! (a:argExprList)? RPAREN!
          {
            // Increment call site counter in function definition Block
            Block functionDefBlock =
              (Block) symbolTable.lookupNameInCurrentScope (id.getText ());

            if (functionDefBlock == null)
              throw new
                RuntimeException ("Unable to find definition of function " +
                                  "(must declare before use): " +
                                  id.getText ());
                
            functionDefBlock.setNumCallSites
              (functionDefBlock.getNumCallSites () + 1);
            ## = #(#[NFunctionCall], ##);
            // XXX 0-based
            ##.setCallIndex (functionDefBlock.getNumCallSites () - 1);
          }
        ;

/*
primaryExpr
        :       lval
        |       charConst
        |       intConst
        |       floatConst
        |       stringConst

// JTC:
// ID should catch the enumerator
// leaving it in gives ambiguous err
//      | enumerator
        |       LPAREN! pureExpr RPAREN!        { ## = #( #[NExpressionGroup, "("], ## ); }
        ;
*/

argExprList
        :       pureExpr ( COMMA! pureExpr )*
        ;

protected
charConst
        :       CharLiteral
        ;

protected
stringConst
        :       (StringLiteral)+                { ## = #(#[NStringSeq], ##); }
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

dummy
  : NTypedefName
  | NInitDecl
  | NDeclarator
  | NStructDeclarator
  | NDeclaration
  | NCast
  | NPointerGroup
  | NFunctionCallArgs
  | NNonemptyAbstractDeclarator
  | NInitializer
  | NStatementExpr
  | NEmptyExpression
  | NParameterTypeList
  | NFunctionDef
  | NCompoundStatement
  | NParameterDeclaration
  | NCommaExpr
  | NUnaryExpr
  | NLabel
  | NPostfixExpr
  | NRangeExpr
  | NStringSeq
  | NAsmAttribute
  | NGnuAsmExpr
  ;

cilDummy
  : NProgram
  | NPureExpr
  | NPureExpressionGroup
  | NFunctionDeclSpecifiers
  | NFunctionBody
  | NFunctionCall
  | NFunctionCallStmt
  | NFunctionCallAssignStmt
  | NAssignStmt
  | NImplicitAssignStmt
  | NWhile
  | NIf
  | NScope
  | NLocalDeclarations
  | NDeclSpecifiers
  | NEmptyScope
  | NReturn
  | NGoto
  | NContinue
  | NBreak
  | NLabelledStmt
  | NLabel
  | NArrayIndex
  | NArrayElement
  | NBlock
  | NStructFields
  | NNDGoto
  | NLCurlyInitializer
  | NLHS
  | NRHS
  ;
 

// PARSER DEFINITION ENDS

{
        //import CToken;
        import java.io.*;
        //import LineObject;
        import antlr.*;
}

class CILLexer extends Lexer;

options
        {
        k = 3;
        exportVocab = CIL;
        testLiterals = false;
        }

{
  LineObject lineObject = new LineObject();
  String originalSource = "";
  PreprocessorInfoChannel preprocessorInfoChannel = new PreprocessorInfoChannel();
  int tokenNumber = 0;
  boolean countingTokens = true;
  int deferredLineCount = 0;

  public void setCountingTokens(boolean ct) 
  {
    countingTokens = ct;
    if ( countingTokens ) {
      tokenNumber = 0;
    }
    else {
      tokenNumber = 1;
    }
  }

  public void setOriginalSource(String src) 
  {
    originalSource = src;
    lineObject.setSource(src);
  }
  public void setSource(String src) 
  {
    lineObject.setSource(src);
  }
  
  public PreprocessorInfoChannel getPreprocessorInfoChannel() 
  {
    return preprocessorInfoChannel;
  }

  public void setPreprocessingDirective(String pre)
  {
    preprocessorInfoChannel.addLineForTokenNumber( pre, new Integer(tokenNumber) );
  }
  
  protected Token makeToken(int t)
  {
    if ( t != Token.SKIP && countingTokens) {
        tokenNumber++;
    }
    CToken tok = (CToken) super.makeToken(t);
    tok.setLine(lineObject.line);
    tok.setSource(lineObject.source);
    tok.setTokenNumber(tokenNumber);

    lineObject.line += deferredLineCount;
    deferredLineCount = 0;
    return tok;
  }

    public void deferredNewline() { 
        deferredLineCount++;
    }

    public void newline() { 
        lineObject.newline();
    }






}

protected
Vocabulary
        :       '\3'..'\377'
        ;


/* Operators: */

ASSIGN          : '=' ;
COLON           : ':' ;
COMMA           : ',' ;
QUESTION        : '?' ;
SEMI            : ';' ;
PTR             : "->" ;


// DOT & VARARGS are commented out since they are generated as part of
// the Number rule below due to some bizarre lexical ambiguity shme.

// DOT  :       '.' ;
protected
DOT:;

// VARARGS      : "..." ;
protected
VARARGS:;


LPAREN          : '(' ;
RPAREN          : ')' ;
LBRACKET        : '[' ;
RBRACKET        : ']' ;
LCURLY          : '{' ;
RCURLY          : '}' ;

EQUAL           : "==" ;
NOT_EQUAL       : "!=" ;
LTE             : "<=" ;
LT              : "<" ;
GTE             : ">=" ;
GT              : ">" ;

DIV             : '/' ;
DIV_ASSIGN      : "/=" ;
PLUS            : '+' ;
PLUS_ASSIGN     : "+=" ;
INC             : "++" ;
MINUS           : '-' ;
MINUS_ASSIGN    : "-=" ;
DEC             : "--" ;
STAR            : '*' ;
STAR_ASSIGN     : "*=" ;
MOD             : '%' ;
MOD_ASSIGN      : "%=" ;
RSHIFT          : ">>" ;
RSHIFT_ASSIGN   : ">>=" ;
LSHIFT          : "<<" ;
LSHIFT_ASSIGN   : "<<=" ;

LAND            : "&&" ;
LNOT            : '!' ;
LOR             : "||" ;

BAND            : '&' ;
BAND_ASSIGN     : "&=" ;
BNOT            : '~' ;
BOR             : '|' ;
BOR_ASSIGN      : "|=" ;
BXOR            : '^' ;
BXOR_ASSIGN     : "^=" ;


Whitespace
        :       ( ( '\003'..'\010' | '\t' | '\013' | '\f' | '\016'.. '\037' | '\177'..'\377' | ' ' )
                | "\r\n"                { newline(); }
                | ( '\n' | '\r' )       { newline(); }
                )                       { _ttype = Token.SKIP;  }
        ;


Comment
        :       "/*"
                ( { LA(2) != '/' }? '*'
                | "\r\n"                { deferredNewline(); }
                | ( '\r' | '\n' )       { deferredNewline();    }
                | ~( '*'| '\r' | '\n' )
                )*
                "*/"                    { _ttype = Token.SKIP;  
                                        }
        ;


CPPComment
        :
                "//" ( ~('\n') )* 
                        {
                        _ttype = Token.SKIP;
                        }
        ;

PREPROC_DIRECTIVE
options {
  paraphrase = "a line directive";
}

        :
        '#'
        ( ( "line" || (( ' ' | '\t' | '\014')+ '0'..'9')) => LineDirective      
            | (~'\n')*                                  { setPreprocessingDirective(getText()); }
        )
                {  
                    _ttype = Token.SKIP;
                }
        ;

protected  Space:
        ( ' ' | '\t' | '\014')
        ;

protected LineDirective
{
        boolean oldCountingTokens = countingTokens;
        countingTokens = false;
}
:
                {
                        lineObject = new LineObject();
                        deferredLineCount = 0;
                }
        ("line")?  //this would be for if the directive started "#line", but not there for GNU directives
        (Space)+
        n:Number { lineObject.setLine(Integer.parseInt(n.getText())); } 
        (Space)* // changed from (Space)+
        (       fn:StringLiteral {  try { 
                                          lineObject.setSource(fn.getText().substring(1,fn.getText().length()-1)); 
                                    } 
                                    catch (StringIndexOutOfBoundsException e) { /*not possible*/ } 
                                 }
                | fi:ID { lineObject.setSource(fi.getText()); }
        )?
        (Space)*
        ("1"            { lineObject.setEnteringFile(true); } )?
        (Space)*
        ("2"            { lineObject.setReturningToFile(true); } )?
        (Space)*
        ("3"            { lineObject.setSystemHeader(true); } )?
        (Space)*
        ("4"            { lineObject.setTreatAsC(true); } )?
        (~('\r' | '\n'))*
        ("\r\n" | "\r" | "\n")
                {
                        preprocessorInfoChannel.addLineForTokenNumber(new LineObject(lineObject), new Integer(tokenNumber));
                        countingTokens = oldCountingTokens;
                }
        ;



/* Literals: */

/*
 * Note that we do NOT handle tri-graphs nor multi-byte sequences.
 */


/*
 * Note that we can't have empty character constants (even though we
 * can have empty strings :-).
 */
CharLiteral
        :       '\'' ( Escape | ~( '\'' ) ) '\''
        ;


/*
 * Can't have raw imbedded newlines in string constants.  Strict reading of
 * the standard gives odd dichotomy between newlines & carriage returns.
 * Go figure.
 */
StringLiteral
        :       '"'
                ( Escape
                | ( 
                    '\r'        { deferredNewline(); }
                  | '\n'        {
                                deferredNewline();
                                _ttype = BadStringLiteral;
                                }
                  | '\\' '\n'   {
                                deferredNewline();
                                }
                  )
                | ~( '"' | '\r' | '\n' | '\\' )
                )*
                '"'
        ;


protected BadStringLiteral
        :       // Imaginary token.
        ;


/*
 * Handle the various escape sequences.
 *
 * Note carefully that these numeric escape *sequences* are *not* of the
 * same form as the C language numeric *constants*.
 *
 * There is no such thing as a binary numeric escape sequence.
 *
 * Octal escape sequences are either 1, 2, or 3 octal digits exactly.
 *
 * There is no such thing as a decimal escape sequence.
 *
 * Hexadecimal escape sequences are begun with a leading \x and continue
 * until a non-hexadecimal character is found.
 *
 * No real handling of tri-graph sequences, yet.
 */

protected
Escape  
        :       '\\'
                ( options{warnWhenFollowAmbig=false;}:
                  'a'
                | 'b'
                | 'f'
                | 'n'
                | 'r'
                | 't'
                | 'v'
                | '"'
                | '\''
                | '\\'
                | '?'
                | ('0'..'3') ( options{warnWhenFollowAmbig=false;}: Digit ( options{warnWhenFollowAmbig=false;}: Digit )? )?
                | ('4'..'7') ( options{warnWhenFollowAmbig=false;}: Digit )?
                | 'x' ( options{warnWhenFollowAmbig=false;}: Digit | 'a'..'f' | 'A'..'F' )+
                )
        ;


/* Numeric Constants: */

protected
Digit
        :       '0'..'9'
        ;

protected
LongSuffix
        :       'l'
        |       'L'
        ;

protected
UnsignedSuffix
        :       'u'
        |       'U'
        ;

protected
FloatSuffix
        :       'f'
        |       'F'
        ;

protected
Exponent
        :       ( 'e' | 'E' ) ( '+' | '-' )? ( Digit )+
        ;


protected
DoubleDoubleConst:;

protected
FloatDoubleConst:;

protected
LongDoubleConst:;

protected
IntOctalConst:;

protected
LongOctalConst:;

protected
UnsignedOctalConst:;

protected
IntIntConst:;

protected
LongIntConst:;

protected
UnsignedIntConst:;

protected
IntHexConst:;

protected
LongHexConst:;

protected
UnsignedHexConst:;




Number
        :       ( ( Digit )+ ( '.' | 'e' | 'E' ) )=> ( Digit )+
                ( '.' ( Digit )* ( Exponent )?
                | Exponent
                )                       { _ttype = DoubleDoubleConst;   }
                ( FloatSuffix           { _ttype = FloatDoubleConst;    }
                | LongSuffix            { _ttype = LongDoubleConst;     }
                )*

        |       ( "..." )=> "..."       { _ttype = VARARGS;     }

        |       '.'                     { _ttype = DOT; }
                ( ( Digit )+ ( Exponent )?
                                        { _ttype = DoubleDoubleConst;   }
                  ( FloatSuffix         { _ttype = FloatDoubleConst;    }
                  | LongSuffix          { _ttype = LongDoubleConst;     }
                  )*
                )?

        |       '0' ( '0'..'7' )*       { _ttype = IntOctalConst;       }
                ( LongSuffix            { _ttype = LongOctalConst;      }
                | UnsignedSuffix        { _ttype = UnsignedOctalConst;  }
                )*

        |       '1'..'9' ( Digit )*     { _ttype = IntIntConst;         }
                ( LongSuffix            { _ttype = LongIntConst;        }
                | UnsignedSuffix        { _ttype = UnsignedIntConst;    }
                )*

        |       '0' ( 'x' | 'X' ) ( 'a'..'f' | 'A'..'F' | Digit )+
                                        { _ttype = IntHexConst;         }
                ( LongSuffix            { _ttype = LongHexConst;        }
                | UnsignedSuffix        { _ttype = UnsignedHexConst;    }
                )*
        ;


ID
        options 
                {
                testLiterals = true; 
                }
        :       ( '@' )?
                ( 'a'..'z' | 'A'..'Z' | '_' )
                ( "::" | '@' | 'a'..'z' | 'A'..'Z' | '_' | '0'..'9' )*
        ;


