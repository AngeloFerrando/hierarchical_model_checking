header
{
    package edu.toronto.cs.yasm.abstractor;
}

{
    import edu.toronto.cs.cparser.block.*;
    import edu.toronto.cs.cparser.*;
    import edu.toronto.cs.tp.cvcl.*;
      // *** NOTE: Expr and Type below refer to CVCL classes
    import java.util.List;
}

class DeclsParser extends TreeParser;

options
{
    buildAST = true;
    ASTLabelType = "Block";
    defaultErrorHandler = false;
    importVocab = CIL;

}

{
  int ctr = 0;
}

declarationList [ValidityChecker vc, List decls]
  { Expr e; }
  : #( NLocalDeclarations ( e = declaration [vc] { decls.add (e); } )* )
  | #( NParameterTypeList ( e = declaration [vc] { decls.add (e); } )* )
  ;

declaration [ValidityChecker vc] returns [Expr e]
  {
    Type t;
    String declName;
    e = null;
  }
  : #( NBlock
       t = declSpecifiers [vc] // what do we do in the case of a typedef or
                               // struct type declaration?
       {
         // System.err.println ("%%% " + ctr + " line: " + ##.getLineNum ());
         // System.err.println ("%%% " + ctr + " type: " + t);
       }
       (
         declName = declarator
         { e = vc.varExpr (declName, t); }
       )?
       ( NInitializer )? // XXX Ignores initial value
       { ctr++; }
    )
  ;

declSpecifiers [ValidityChecker vc] returns [Type t]
  : #( NDeclSpecifiers t = typeSpecifier [vc]
      ( pointerGroup { t = vc.intType (); } )?
    )
  ;

typeSpecifier [ValidityChecker vc] returns [Type t]
  : ( "short" | "int" | "long" )
      { t = vc.intType (); }
  | ( "float" | "double" )
      { t = vc.realType (); }
  | "void"
      { t = vc.createType ("VOID_DUMMY"); }
  | t = structSpecifier [vc]
  ;

structSpecifier [ValidityChecker vc] returns [Type t]
  : ( "struct" ) // XXX Ignores children
    {
      // t = vc.intType ();
      t = vc.createType ("STRUCT_DUMMY");
    }
  ;

pointerGroup
  : #( NPointerGroup ( STAR )+ )
  ;

initDecl returns [String declName]
  : #( NInitDecl
       declName = declarator
       /* XXX Ignores initialization
       ( ASSIGN initializer
       | COLON expr
       )?
       */
    )
  ;

declarator returns [String declName]
  : #( NDeclarator
        // ( pointerGroup )?

        ( id:ID
          { declName = id.getText (); }
        | LPAREN declName = declarator RPAREN
        )

        /*
        (   #( NParameterTypeList      // XXX function prototype or def header
              (
                parameterTypeList
                | (idList)?
              )
              RPAREN
            )
         | LBRACKET ( expr )? RBRACKET // XXX array decl
        )*
        */
     )
  ;

/*
declaration [ValidityChecker vc] returns [edu.toronto.cs.tp.cvcl.Expr e]
        {
          Type t;
          e = null;
        }
*/
        /*
        :       #( NBlock
                    t = typeSpecifier [vc]
                    #( NInitDecl #( NDeclarator id1:ID ) ) 
                )
                { e = vc.varExpr (id1.getText (), t); }
        |       #( NBlock #( "struct" id2:ID ) ) // XXX ignores rest of children
                { e = vc.varExpr (id2.getText (), vc.intType ()); }
        */
/* 
        : #( NBlock ( ( NDeclSpecifiers "struct" ) ) =>
                        e = structDeclaration [vc]
                    | t = typeSpecifier [vc]
                      #( NInitDecl #( NDeclarator id1:ID ) )
                      { e = vc.varExpr (id1.getText (), t); }
                    )
          )
        ; 
*/

/*
structDeclaration [ValidityChecker vc]
  returns [edu.toronto.cs.tp.cvcl.Expr e]        
        { e = null; }
        : #( NDeclSpecifiers "struct" )
          { e = vc.varExpr ("foo", vc.intType ()); }
        ;

typeSpecifier [ValidityChecker vc] returns [Type t]
        { t = null; }
        : #( NDeclSpecifiers
            (
              ( "short" | "int" | "long" )
                { t = vc.intType (); }
            | ( "float" | "double" )
                { t = vc.realType (); }
            )
          )
        ;
*/

