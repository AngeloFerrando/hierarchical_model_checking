header { package edu.toronto.cs.cparser; }


{
import java.io.*;

import antlr.CommonAST;
import antlr.DumpASTVisitor;

import edu.toronto.cs.cparser.block.*;
}

                     
class CILProgramBlocker extends TreeParser;

options
        {
        importVocab = CIL;
        buildAST = true;
        ASTLabelType = "Block";
        defaultErrorHandler = false;

        // Copied following options from java grammar.
        codeGenMakeSwitchThreshold = 2;
        codeGenBitsetTestThreshold = 3;
        }


{
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


}

translationUnit
  options { defaultErrorHandler=false; }
  : #( NProgram list:externalList )
      {
        Block b = new Block (BlockType.PROGRAM);
        ## = #(b, list);
      }
  ;

externalList
        :       ( externalDef )+
        ;

externalDef
        :       declaration
        |       functionDef
        ;

declaration
        :!      #( NDeclaration
                     specs:declSpecifiers
                     ( d:declarator )?
                     ( i:initializer )?
                )
                  {
                    Block b = new Block (BlockType.DECLARATION);
                    ## = #( b, specs, d, i );
                  }
        ;

declSpecifiers 
        : #( NDeclSpecifiers
                ( storageClassSpecifier
                | typeQualifier
                | typeSpecifier
                | pointerGroup
                )+
          )
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


typeSpecifier
        :       "void"
        |       "char"
        |       "short"
        |       "int"
        |       "long"
        |       "float"
        |       "double"
        |       "signed"
        |       "unsigned"
        |       structSpecifier ( attributeDecl )*
        |       unionSpecifier  ( attributeDecl )*
        |       enumSpecifier
        |       typedefName
        |       #("typeof" LPAREN
                    ( (typeName )=> typeName 
                    | pureExpr
                    )
                    RPAREN
                )
        |       "__complex"
        ;


typedefName
        :       #(NTypedefName ID)
        ;


structSpecifier
        :   #( "struct" ID ( structDeclarationList )? )
        ;

unionSpecifier
        :   #( "union" structOrUnionBody )
        ;
   
structOrUnionBody
        :       ( (ID NStructFields) => ID NStructFields!
                        ( structDeclarationList )?
                | ID
                )
        ;

structDeclarationList
        :       #( NStructFields ( structDeclaration )* )
        ;

structDeclaration
        :       specifierQualifierList structDeclaratorList
        ;

specifierQualifierList
        :       (
                typeSpecifier
                | typeQualifier
                | pointerGroup
                )+
        ;

structDeclaratorList
        :       ( structDeclarator )+
        ;

structDeclarator
        :
        #( NStructDeclarator      
            ( declarator )?
            ( COLON pureExpr )?
            ( attributeDecl )*
        )
        ;

enumSpecifier
        :   #(  "enum"
                ( ID )? 
                ( LCURLY enumList )?
            )
        ;

enumList
        :       ( enumerator )+
        ;

enumerator
        :       ID ( ASSIGN pureExpr )?
        ;

attributeDecl:
        #( "__attribute__" (.)* )
        | #( NAsmAttribute LPAREN pureExpr RPAREN )
        ;

initDeclList
        :       ( initDecl )+
        ;

// XXX Should clean up this structure in CILParser instead
initDecl!
        :       #( NInitDecl
                d:declarator
                { ## = #d; }
                // ( attributeDecl )*
                ( ASSIGN! in:initializer
                  { ## = #(d, in); }
                // | COLON pureExpr
                )?
                )
        ;

pointerGroup
        :       #( NPointerGroup ( STAR ( typeQualifier )* )+ )
        ;

idList
        :       ID ( COMMA ID )*
        ;

initializer
        :       #( NInitializer (initializerElementLabel)? pureExpr )
                |   lcurlyInitializer
        ;

initializerElementLabel
        :   #( NInitializerElementLabel
                (
                    ( LBRACKET pureExpr RBRACKET (ASSIGN)? )
                    | ID COLON
                    | DOT ID ASSIGN
                )
            )
        ;

lcurlyInitializer
        :  #( NLCurlyInitializer initializerList )
        ;

initializerList
        :       ( initializer )*
        ;


declarator
        :   #( NDeclarator
                // Pointers are associated with declSpecifiers, not decl name
                // XXX re-added for function pointers
                ( pointerGroup )?

                ( id:ID
                | LPAREN declarator RPAREN
                )

                (   #( NParameterTypeList
                      (
                        parameterTypeList
                        | (idList)?
                      )
                      // RPAREN
                    )
                 | LBRACKET ( pureExpr )? RBRACKET
                )*
             )
        ;


 
parameterTypeList
        :       ( parameterDeclaration ( COMMA | SEMI )? )+ ( VARARGS )?
        ;
    


parameterDeclaration
        :       #( NParameterDeclaration
                declSpecifiers
                (declarator | nonemptyAbstractDeclarator)?
                )
                {
                  ##.setType (NBlock);
                  ##.setBlockType (BlockType.DECLARATION);
                }
        ;


functionDef
        :!  #( r:NFunctionDef
                funcSpecs:functionDeclSpecifiers
                funcSig:declarator
                funcBody:scope
            )
            {
              Block b = new Block (BlockType.FUNCTION_DEFINITION);
              b.setNumCallSites (#r.getNumCallSites ());
              ## = #(b, funcSpecs, funcSig, funcBody);
            }
        ;

functionDeclSpecifiers
        : #( NDeclSpecifiers       
                ( functionStorageClassSpecifier
                | typeQualifier
                | typeSpecifier
                | pointerGroup
                )+
                // ( pointerGroup )?
           )     
        ;

declarationList
        :       
                (   //ANTLR doesn't know that declarationList properly eats all the declarations
                    //so it warns about the ambiguity
                    options {
                        warnWhenFollowAmbig = false;
                    } :
                localLabelDecl
                | declaration
                )+
        ;

localLabelDecl
        :   #("__label__" (ID)+ )
        ;
   
scope
        :!      #( NScope
                od:optDeclarations
                os:optStatements
                )
                {
                    Block b = new Block (BlockType.SCOPE);
                    Block mergedOS = Block.mergeCopy ((Block) #os);
                    ## = #(b, od, mergedOS);
                }
        ;

optDeclarations
        : #( NLocalDeclarations ( declarationList )? )
        ;

optStatements
        :       ( statementList )?
        ;

statementList
        :       ( stmt )+
        ;

// CIL Statements
stmt
  : assignStmt
    {
      Block b = new Block (BlockType.STATEMENT_LIST);
      ## = #(b, ##);
    }
  | functionCallAssignStmt  
  | functionCallStmt
  | labelledStmt
  | controlStmt
  | scope
  // | gnuAsmExpr
  // | SEMI
  ;

gnuAsmExpr
        :   #(NGnuAsmExpr
                ("volatile")? 
                LPAREN stringConst
                ( options { warnWhenFollowAmbig = false; }:
                  COLON (strOptExprPair ( COMMA strOptExprPair)* )?
                  ( options { warnWhenFollowAmbig = false; }:
                    COLON (strOptExprPair ( COMMA strOptExprPair)* )?
                  )?
                )?
                ( COLON stringConst ( COMMA stringConst)* )?
                RPAREN
            )
        ;

strOptExprPair
        :  stringConst ( LPAREN pureExpr RPAREN )?
        ;

assignStmt
  : #( NAssignStmt pureExpr pureExpr ) // XXX first pureExpr was lval before
  | #( NImplicitAssignStmt lval ( INC | DEC ) )
  ;  

labelledStmt
  : #( NLabelledStmt #( NLabel id:ID )
       ( s:stmt { #s.setLabel (id.getText ()); } )?
     )
    {
      ## = #(#[NBlock], #(#[NLabel], id), s);
      ##.setBlockType (BlockType.LABELLED_STATEMENT);
      ##.setLabel (id.getText ());
    }
  ;  

controlStmt!
  : #( NWhile whileExpr:pureExpr whileStmt:stmt )
    {
      Block b = new Block (BlockType.WHILE);
      ## = #(b, whileExpr, whileStmt);
    }
  | #( NGoto id:ID )
    {
      Block b = new Block (BlockType.GOTO);
      ## = #(b, id);
    }
  | NBreak
    { ## = new Block (BlockType.BREAK); }
  | #( r:NReturn ( retExpr:pureExpr )? )
    {
      Block b = new Block (BlockType.RETURN);
      b.setLineNum (#r.getLineNum ());
      ## = #(b, retExpr);
    }
  | #( NIf ifExpr:pureExpr ifStmt1:stmt ( ifStmt2:stmt )? )
    {
      Block b = new Block (BlockType.IF);
      ## = #(b, ifExpr, ifStmt1, ifStmt2);
    }
  | #( NNDGoto list:ndGotoStringList )
    {
      Block b = new Block (BlockType.NDGOTO);
      ## = #(b, list);
    }
  ;

ndGotoStringList
  : ( StringLiteral )+  
  ;

functionCallStmt
  : #( NFunctionCallStmt functionCall )
    {
      Block b = new Block (BlockType.FUNCTION_CALL);
      ## = #(b, ##);
    }
  ;

functionCallAssignStmt
  : #( NFunctionCallAssignStmt lval functionCall )
    {
      Block b = new Block (BlockType.FUNCTION_CALL);
      ## = #(b, ##);
    }
  ;

functionCall
  : #( r:NFunctionCall ID (argExprList)? )  
    { ##.setCallIndex (#r.getCallIndex ()); }
  ;

lval
  : #( PTR lval lval )
  | #( DOT lval lval )
  | #( NArrayElement ID ( arraySuffix )+ )
  | ID
  ;

arraySuffix
  : #( NArrayIndex pureExpr )
  ;  

pureExpr
  : pureLogicalOrExpr
  | pureLogicalAndExpr
  | pureEqualityExpr
  | pureRelationalExpr
  | pureAdditiveExpr
  | pureMultExpr
  | pureBitwiseExpr
  | pureCastExpr
  | pureUnaryExpr
  | purePrimaryExpr
  ;  

pureBitwiseExpr
  : #( BOR pureExpr pureExpr )  
  | #( BXOR pureExpr pureExpr )  
  | #( BAND pureExpr pureExpr )  
  | #( LSHIFT pureExpr pureExpr )  
  | #( RSHIFT pureExpr pureExpr )  
  ;

pureLogicalOrExpr
  : #( LOR pureExpr pureExpr ) 
  ;

pureLogicalAndExpr
  : #( LAND pureExpr pureExpr )
  ;


pureEqualityExpr
  : #( EQUAL pureExpr pureExpr)
  | #( NOT_EQUAL pureExpr pureExpr)
  ;

pureRelationalExpr
  : #( LT pureExpr pureExpr)
  | #( LTE pureExpr pureExpr)
  | #( GT pureExpr pureExpr)
  | #( GTE pureExpr pureExpr)
  ;

pureAdditiveExpr
  : #( PLUS pureExpr pureExpr)
  | #( MINUS pureExpr pureExpr)
  ;

pureMultExpr
  : #( STAR pureExpr pureExpr)
  | #( DIV pureExpr pureExpr)
  | #( MOD pureExpr pureExpr)
  ;

pureCastExpr!
  : #( NCast typeName e:pureExpr)
    { ## = #e; } // XXX Discarding type information
  ;

typeName
  : specifierQualifierList (nonemptyAbstractDeclarator)?
  ;

nonemptyAbstractDeclarator
        :   #( NNonemptyAbstractDeclarator
            (   pointerGroup
                (   (LPAREN  
                    (   nonemptyAbstractDeclarator
                        | parameterTypeList
                    )?
                    RPAREN)
                | (LBRACKET (pureExpr)? RBRACKET)
                )*

            |  (   (LPAREN  
                    (   nonemptyAbstractDeclarator
                        | parameterTypeList
                    )?
                    RPAREN)
                | (LBRACKET (pureExpr)? RBRACKET)
                )+
            )
            )
        ;

pureUnaryExpr
  : purePostfixExpr
  | #( NUnaryExpr unaryOperator pureExpr)
  | #( "sizeof"
        ( ( LPAREN typeName )=> LPAREN typeName RPAREN
        | pureExpr
        )
    )
  | #( "__alignof"
        ( ( LPAREN typeName )=> LPAREN typeName RPAREN
        | pureExpr
        )
    )
  ;

purePostfixExpr
  : #( NPostfixExpr
        lval
        ( #( NFunctionCallArgs (argExprList)? )
        | LBRACKET pureExpr RBRACKET
        )+
    )
  ;

unaryOperator
  : BAND
  | STAR
  | PLUS
  | MINUS
  | BNOT
  | LNOT
  | LAND
  | "__real"
  | "__imag"
  ;

purePrimaryExpr
  : lval
  | intConst
  | floatConst
  | charConst
  | stringConst
  | #( NPureExpressionGroup pureExpr )
  ;

argExprList
        :       ( pureExpr )+
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

