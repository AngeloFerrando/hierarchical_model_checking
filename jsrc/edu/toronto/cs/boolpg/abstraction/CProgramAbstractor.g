header {package edu.toronto.cs.boolpg.abstraction;}
{
    import java.util.*;
    
    import edu.toronto.cs.tp.cvcl.*;
    import edu.toronto.cs.cparser.*;
    import edu.toronto.cs.yasm.abstraction.*;
}

class CProgramAbstractor extends GnuCTreeParser;

{
    private static final boolean DEBUG = true;

    private StringBuffer bpBuffer = new StringBuffer ();

    private int bpLabelIndex = 1;

    private Stack varMapStack = new Stack ();
	private ValidityChecker vc;
    private HashMap initialVarMap = new HashMap ();
    private StmtAbstraction hfunction = new StmtAbstraction ();
    private List predicates = new ArrayList ();

    private void init () 
    {
        vc = CVCLUtil.newValidityChecker ();
        // stack initial empty varmap
        varMapStack.push (initialVarMap);
        {
            predicates = Util.somePred (vc);
            // --***********************
            bpPredDeclarations ();
        }
        
        if (DEBUG) {
            for (int i = 0; i < predicates.size(); i++) {
                debug(predicates.get(i).toString());
            }
        }
    }

    private void debug (String s) {
        if (DEBUG) {
            System.out.println (">>> " + s);
        }
    }

    /**************************************************
     * PREDICATE TABLE METHODS
     **************************************************/

    // given a TP Expr, returns the index of the boolean variable associated
    // with it or "unknown" if there is none
    private String predLookup (Expr pred) {
        debug ("looking up predicate: " + pred);
        for (int i = 0; i < predicates.size (); i++) {
            if (pred.equal ((Expr)predicates.get (i), pred)) { 
                // shouldn't .equal be a static method?
                return bpBoolIdent (i);
            }
        }
        // if reached here, lookup failed; return "unknown"
        return "unknown";
    }

    /**************************************************
     * VARMAP METHODS
     **************************************************/

    // returns current varmap as a Map (external use)
    public Map getMap () {
        return (Map) varMapStack.peek ();
    }

    // returns current varmap as a HashMap (internal use)
    private HashMap getVarMap () {
        return (HashMap) varMapStack.peek ();
    }

    // dups top varmap; should be called when entering new control-flow block
    private void push () {
        debug ("push");
        // varMapStack.push (getVarMap ().clone ());
        // for now just reinitialize map
        initVarMap ();
    }

    // pops top varmap and reinitializes new top; should be called when
    // exiting control-flow block
    private void pop () {
        debug ("pop");
        // varMapStack.pop ();
        // reinitialize new top
        // for now just reinitialize map
        initVarMap ();
    }

    // initializes varmap to map identifier "x" to Expr (x)
    private void initVarMap () {
        Map varMap = getVarMap ();
        Iterator keys = varMap.keySet ().iterator ();
        while (keys.hasNext ()) {
            String key = (String) keys.next ();
            varMap.put (key, vc.varExpr (key, vc.realType ()));
        }
    }

    // maps key to value in current varmap
    private void mapPut (Object key, Object value) {
        debug ("put: " + key + " -> " + value);
        getVarMap ().put (key, value);
    }    

    // returns value associated with key in current varmap
    private Object mapGet (Object key) {
        return getVarMap ().get (key);
    }

    private String varMapString () {
        Map varMap = getVarMap ();
        Iterator keys = varMap.keySet ().iterator ();
        StringBuffer s = new StringBuffer ();
        while (keys.hasNext ()) {
            String key = (String) keys.next ();
            s.append (key + " -> " + (Expr) varMap.get (key) + "\n");
        }
        return s.toString ();
    }

    /**************************************************
     * BOOLEAN PROGRAM GENERATION METHODS
     **************************************************/

    // returns bp as String
    public String getBooleanProgram () {
        return bpBuffer.toString ();
    }

    // add predicate declarations to bp
    private void bpPredDeclarations () {
        for (int i = 0; i < predicates.size (); i++) {
            bpBuffer.append ("bool " + bpBoolIdent (i) + ";\n");
        }
    }

    // adds result of bpTransBlockString () to bp
    private void bpTransBlock () {
        bpBuffer.append (bpTransBlockString ()); 
    }

    // adds if (cond) goto <thenLabel> else goto <elseLabel> fi to bp
    private void bpIfGotoElseGotoFi (Expr cond, String thenLabel, String elseLabel) {
        bpBuffer.append ("if (" + predLookup (cond) + ") goto " + thenLabel + " else goto " + elseLabel + " fi" + "\n");
    }

    // adds goto <label> to bp
    private void bpGoto (String label) {
        bpBuffer.append ("goto " + label + "\n");
    }

    private void bpLabelledStatement (String label, String stmt) {
        bpBuffer.append (label + ":\n" + stmt + "\n");
    }

    private void bpLabel (String label) {
        bpBuffer.append (label + ":\n");
    }

    // print self loop to end program
    private void bpEndProgramLoop () {
        bpLabel("_end");
        bpGoto("_end");
    }

    // returns a boolean variable identifier with index i
    private String bpBoolIdent (int i) {
        return "b" + String.valueOf (i);
    }

    // returns a bp label with index i
    private String bpLabelString (int i) {
        return "l" + String.valueOf (i);
    }

    // returns comma and newline separated String of transition statements
    private String bpTransBlockString () {
        StringBuffer result = new StringBuffer ();
        List stmts = bpTransBlockList ();

        if (stmts.isEmpty () )
        {
            return "";
        }
        else if (stmts.size () == 1)
        {
            result.append ((String) stmts.get (0));
        }
        else
        {
            // print all but the last statement followed by a comma
            for (int i = 0; i < stmts.size () - 1; i++)
            {
                result.append ((String) stmts.get (i) + ",\n");
            }
            // print last statement without a comma
            result.append ((String) stmts.get (stmts.size () - 1));
        }
        result.append ("\n");
        return result.toString ();

    }

    // returns list of transition statements
    private List bpTransBlockList () {
        Expr targetPred;
        Expr[] antecedents;
        List stmts = new ArrayList ();
        Ladder l = new Ladder();
        l.sCompute(vc, predicates, getVarMap(), l);
        for (int i = 0; i < predicates.size (); i++) {
            //targetPred = (Expr) predicates.get (i);
            /*
            System.out.println ("passing: ");
            System.out.println (varMapString ());
            */
            antecedents = l.getAbstraction(i, vc);

            //System.out.print("%%%%%%%%%%%%%%%%%%%%%%%%");
            //System.out.println(antecedents[0]);
            //System.out.print("&&&&&&&&&&&&&&&&&&&&&&&&");
            //System.out.println(antecedents[1]);


            //antecedents = hfunction.computeH (vc, 
            //                       predicates, targetPred, getVarMap ());
            stmts.add (bpTransStmtString (bpBoolIdent (i), antecedents));
        }
        return stmts;
    }

    // returns a String representing a single boolean program transition
    // statement (b = H (.,.))
    private String bpTransStmtString (String predIdent, Expr[] antecedents) {
        assert antecedents.length == 2 : "not enough antecedents!";
        String[] ant = new String[2];

        for (int i = 0; i <= 1; i++) {
            if (antecedents [i].isTrue ()) {
                ant [i] = "true";
            } else if (antecedents [i].isFalse ()) {
                ant [i] = "false";
            } else {
                ant [i] = bpSubstitutedExprString (antecedents [i].toString ());
            }
        }

        return (predIdent + " := H (" + ant [0] + "," + ant [1] + ")");
    }

    // returns a String representing a bp boolean expression with TP Expr
    // toString substrings replaced with their bp equivalents
    private String bpSubstitutedExprString (String s) {
        HashMap symMap = new HashMap ();
        HashMap exprMap = new HashMap ();
        int startIndex;

        for (int i = 0; i < predicates.size (); i++) {
            String predExpr = predicates.get (i).toString ();
            String boolIdent = bpBoolIdent(i);
            debug(predExpr + " -> " + boolIdent);
            exprMap.put (predExpr, boolIdent);
        }

        // substitute expressions with predicate identifiers
        for (Iterator it = exprMap.keySet ().iterator (); it.hasNext ();) {
            String subExpr = (String) it.next ();
            String quotedSubExpr = "\\Q" + subExpr + "\\E";
            String predIdent = (String) exprMap.get (subExpr);
            s = s.replaceAll (quotedSubExpr, predIdent);
        }

        symMap.put ("AND", "&");
        symMap.put ("OR" , "|");
        symMap.put ("NOT", "!");

        // substitute operators
        for (Iterator it = symMap.keySet ().iterator (); it.hasNext ();) {
            String key = (String) it.next ();
            s = s.replaceAll (key, (String) symMap.get (key));
        }
        return s;

    }

    private List bpNextTwoLabels () {
        return bpNextNLabels (2);
    }

    private List bpNextThreeLabels () {
        return bpNextNLabels (3);
    }

    // returns a list containing the next n labels as Strings
    private List bpNextNLabels (int n) {
        LinkedList l = new LinkedList ();
        for (int i = 0; i < n; i++) {
            l.add (bpLabelString (bpLabelIndex++));
        }
        return l;
    }

    /*
    private String bpThenLabelString () {
        return "lt" + bpThenLabelIndex;
    }

    private String bpElseLabelString () {
        return "le" + bpElseLabelIndex;
    }

    private String bpNextLabelString () {
        return "ln" + bpNextLabelIndex;
    }

    private String bpNextThenLabelString () {
        return "lt" + ++bpThenLabelIndex;
    }

    private String bpNextElseLabelString () {
        return "le" + ++bpElseLabelIndex;
    }

    private String bpNextNextLabelString () {
        return "ln" + ++bpNextLabelIndex;
    }
    */
}

translationUnit  options {
  defaultErrorHandler=false;
}
    { init (); }
        :       ( externalList
                    {
                        bpTransBlock ();
                        bpEndProgramLoop ();
                    } )? 
        ;

/*
compoundStatement
        :       #( NCompoundStatement
                ( kDeclarationList
                | functionDef
                )*
                ( statementList )?
                RCURLY
                )
        ;

kDeclarationList
        :       
                (   //ANTLR doesn't know that declarationList properly eats all the declarations
                    //so it warns about the ambiguity
                    options {
                        warnWhenFollowAmbig = false;
                    } :
                localLabelDecl
                | kDeclaration
                )+
        ;

kDeclaration
        :       #( NDeclaration
                    declSpecifiers
                    (                   
                        kInitDeclList
                    )?
                    ( SEMI )+
                )
        ;

kInitDeclList
        :       ( kInitDecl )+
        ;


kInitDecl
                                        { String declName = ""; }
        :       #( NInitDecl
                kDeclarator
                ( attributeDecl )*
                ( ASSIGN initializer
                | COLON expr
                )?
                )
        ;

kDeclarator
        :   #( NDeclarator
                ( pointerGroup )?               

                ( id:ID
                    {
                        String idName = id.getText ();
                        Expr e = vc.varExpr (idName, vc.realType ());
                        System.out.println ("declaration "+ idName + " " + e);
                        map.put (idName, e);
                    }

                | LPAREN declarator RPAREN
                )

                (   #( NParameterTypeList
                      (
                        parameterTypeList
                        | (idList)?
                      )
                      RPAREN
                    )
                 | LBRACKET ( expr )? RBRACKET
                )*
             )
        ;
*/

declarator
        :   #( NDeclarator
                ( pointerGroup )?               

                ( id:ID
                    {
                        String idName = id.getText ();
                        Expr e = vc.varExpr (idName, vc.realType ());
                        debug ("declaration "+ idName + " " + e);
                        mapPut (idName, e);
                    }

                | LPAREN declarator RPAREN
                )

                (   #( NParameterTypeList
                      (
                        parameterTypeList
                        | (idList)?
                      )
                      RPAREN
                    )
                 | LBRACKET ( expr )? RBRACKET
                )*
             )
        ;

assignExpr
        { Expr rExpr;}
        :       #( ASSIGN id:ID rExpr = cilRValExpr )
                {   
                    mapPut (id.getText (), rExpr);
                }    
        ;

/* cil expressions that can appear on the RHS of an assignment */
cilRValExpr returns [Expr e = new Expr ()]
        :       e = additiveExpr
        |       e = multExpr
        |       e = cilCondExpr
        |       LPAREN e = cilRValExpr RPAREN
        ;

/* cil expressions that can appear as an if-condition */
cilCondExpr returns [Expr e = new Expr ()]       
        :       e = logicalOrExpr
        |       e = logicalAndExpr
        |       e = equalityExpr
        |       e = relationalExpr    // gt, lt, le, ge
        |       e = logicalNotExpr
        |       e = primaryExpr
        ;

logicalNotExpr returns [Expr e = new Expr ()]
        { Expr e1; }
        :       #( NUnaryExpr LNOT e1 = cilRValExpr )
                { e = vc.notExpr (e1); }
        ;

logicalOrExpr returns [Expr e = new Expr ()]
        { Expr e1; Expr e2; }
        :       #( LOR e1 = cilRValExpr e2 = cilRValExpr )
                { e = vc.orExpr (e1, e2); }
        ;

logicalAndExpr returns [Expr e = new Expr ()]
        { Expr e1; Expr e2; }
        :       #( LAND e1 = cilRValExpr e2 = cilRValExpr )
                { e = vc.andExpr (e1, e2); }
        ;

equalityExpr returns [Expr e = new Expr ()]
        { Expr e1; Expr e2; }
        :       #( EQUAL e1 = cilRValExpr e2 = cilRValExpr )
                { e = vc.eqExpr (e1, e2); }
        |       #( NOT_EQUAL e1 = cilRValExpr e2 = cilRValExpr )
                { e = vc.notExpr (vc.eqExpr (e1, e2)); }
        ;

relationalExpr returns [Expr e = new Expr ()]
        { Expr e1; Expr e2; }
        :       #( LT e1 = cilRValExpr e2 = cilRValExpr )
                { e = vc.ltExpr (e1, e2); }
        |       #( LTE e1 = cilRValExpr e2 = cilRValExpr )
                { e = vc.leExpr (e1, e2); }
        |       #( GT e1 = cilRValExpr e2 = cilRValExpr )
                { e = vc.gtExpr (e1, e2); }
        |       #( GTE e1 = cilRValExpr e2 = cilRValExpr )
                { e = vc.geExpr (e1, e2); }
        ;

additiveExpr returns [Expr e = new Expr ()]
        { Expr e1; Expr e2; }
        :       #( PLUS e1 = cilRValExpr e2 = cilRValExpr )
                { e = vc.plusExpr (e1, e2); }
        |       #( MINUS e1 = cilRValExpr e2 = cilRValExpr )
                { e = vc.minusExpr (e1, e2); }
        ;

multExpr returns [Expr e = new Expr ()]
        { Expr e1; Expr e2; }
        :       #( STAR e1 = cilRValExpr e2 = cilRValExpr )
                { e = vc.multExpr (e1, e2); }
        |       #( DIV e1 = cilRValExpr e2 = cilRValExpr )
                { e = vc.divideExpr (e1, e2); }
        /* CVC doesn't have mod ...
        |       #( MOD e1 = cilRValExpr e2 = cilRValExpr )
                { e = vc.minusExpr (e1, e2); }
        */
        ;

postfixExpr
        { Expr e1; Expr e2 = new Expr (); }
        :       #( NPostfixExpr
                    e1 = pe:primaryExpr
                    ( INC
                        {
                            // same as e1 = e1 + 1
                            e2 = vc.plusExpr (e1, vc.ratExpr (1, 1));
                        }
                    | DEC
                        { 
                            // same as e1 = e1 - 1
                            e2 = vc.minusExpr (e1, vc.ratExpr (1, 1));
                        }
                    )
                )
                { mapPut (pe.getText (), e2); }
        ;

primaryExpr returns [Expr e = new Expr ()]
        :       id:ID
                {
                    debug ("looking up variable: " + #primaryExpr.getText ());
                    e = (Expr) mapGet (id.getText ());
                    debug ("got: " + e);
                }
        |       n:Number
                {
                    e = vc.ratExpr (Integer.parseInt (n.getText ()), 1);
                }
        /* CVC doesn't have chars or strings ...
        |       charConst
        |       stringConst
        */
        |       #( NExpressionGroup e = cilRValExpr )
        ;

statementBody
        :       SEMI                    // Empty statements
        |       compoundStatement       // Group of statements
        |       #(NStatementExpr expr)                    // Expressions

// Iteration statements:

        |       whileStmt
        |       #( "do" statement expr )
        |       #( "for"
                expr expr expr
                statement
                )

// Jump statements:

        |       #( "goto" expr )
        |       "continue" 
        |       "break"
        |       #( "return" ( expr )? )


// Labeled statements:

        |       #( NLabel id:ID (statement)? )
                {
                    // assume we've reached the end of a CFG block
                    bpTransBlock ();
                    // copy label to bp
                    bpLabel (id.getText ());
                }
        |       #( "case" expr (statement)? )
        |       #( "default" (statement)? )

// Selection statements:

        |       ifStmt
        |       #( "switch" expr statement )
        ;

whileStmt
        {
            Expr cond;
            List labels = bpNextThreeLabels ();
            String labelHead = (String) labels.remove (0);
            String labelBody = (String) labels.remove (0);
            String labelEnd  = (String) labels.remove (0);
        }
        :
                #( "while"
                    // while (cond) =>
                    // head: if (cond) then goto body else goto end
                    // body: <body>
                    // goto l1
                    // end:
                    {
                        // assume we have reached the end of a control-flow
                        // block here
                        bpTransBlock ();
                        push (); // reset variable map so condition variables
                                // aren't looked up---will have to do this
                                // properly later
                    }
                    cond = cilCondExpr
                    {
                        // head:
                        bpLabel (labelHead);
                        // if (cond) then goto body else goto end
                        bpIfGotoElseGotoFi (
                            cond,
                            labelBody,
                            labelEnd);
                        // body:
                        bpLabel (labelBody);
                        push ();
                    }
                    statement
                    {
                        bpTransBlock ();
                        pop ();

                        // <body>
                        bpGoto (labelHead);
                        // end:
                        bpLabel (labelEnd);
                    }
                 )
        ;

ifStmt
        {
            Expr cond;
            List labels = bpNextThreeLabels ();
        }
        :
                #( "if"
                    // if (cond) then <then-block> =>
                    //  if (cond) goto body else goto end fi
                    //  body: <then-transition>
                    //  end:
                    //
                    // if (cond) then <then-block> else <else-block> =>
                    //  if (cond) goto body1 else goto body2 fi
                    //  body1: <then-transition>
                    //  goto end
                    //  body2: <else-transition>
                    //  end:
                    //
                    {
                        // assume we have reached the end of a control-flow
                        // block here
                        bpTransBlock ();
                        push (); // reset variable map so condition variables
                                // aren't looked up---will have to do this
                                // properly later
                    }
                    cond = cilCondExpr
                    {
                        bpIfGotoElseGotoFi (
                            cond,
                            (String) labels.get (0),
                            (String) labels.get (1));
                        bpLabel ((String) labels.remove (0));
                        push ();
                    }
                    statement  
                    {
                        // then
                        // l1: <then-transition>
                        bpTransBlock ();
                        pop ();
                    }
                    ( "else" {
                            // goto l3
                            bpGoto ((String) labels.get (1));
                            push ();
                        }
                       statement
                        {
                            // else
                            // l2: <else-transition>
                            bpLabelledStatement (
                                (String) labels.remove (0),
                                bpTransBlockString ());
                            pop ();
                        }
                    )?
                    {
                        bpLabel ((String) labels.remove (0));
                    }
                 )
        ;
