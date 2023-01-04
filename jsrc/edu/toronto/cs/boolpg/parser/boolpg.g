header { package edu.toronto.cs.boolpg.parser;}{
/**
 * Parser for boolean programs
 **/     
import java.util.*;
}

class BoolProgramLexer extends Lexer;

options {k=6; filter=true; }

LPAREN   : '(';
RPAREN   : ')';



ASSIGNOP : ":=";

//CHOICE : "||";
//LBRACKET : '[';
//RBRACKET : ']';

IMPLIES  : "->";
IFF      : "<->";

EQ       : '=';

OR       : '|';

AND      : '&';

NEG      : '!' ;

LBRACE   : '{';

RBRACE   : '}';

COMMA    : ',';

PLUS     : '+';

MINUS    : '-';

MULT     : '*';

DIV      : '/';

ID  : ATOM ('.' ATOM)*;

protected ATOM    : ('a'..'z'|'A'..'Z'|'_') ('a'..'z'|'A'..'Z'|'_'|'0'..'9'|'\\'|'$'| '#'| '-')*;

protected DIGIT : '0'..'9';

NUMBER  : DIGIT (DIGIT)*;

COL      : ':';

SEMI	: ';';

COMMENT : "//" (options {greedy=true;}:~'\n')*
	{ 	$setType(Token.SKIP); } ;

WS      : (' ' | '\r' | '\t')+ { 
	$setType(Token.SKIP); };
	
NEWLINE : '\n'
	{
	newline();
	$setType(Token.SKIP);
	};


{
import java.util.*;
import edu.toronto.cs.boolpg.parser.VariableTable.*;
}

class BoolProgramParser extends Parser;

options { k = 2; buildAST=true; defaultErrorHandler = false;}

tokens {
    START = "start";
    BOOL = "bool";
    TRUE = "true";
    FALSE = "false";
    UNKNOWN = "unknown";
    SKIP = "skip";
    IF = "if";
    THEN = "then";
    ELSE = "else";
    FI = "fi";
    GOTO = "goto";
    CHOICE = "choice";
}

{
    // -- map from labels to command indexes
    Map labelMap = new HashMap ();

    // -- symbol table to keep track of variables
    VariableTable symbolTable = new VariableTable (1);

    // -- current command index
    int currentCommand = 1;

    public VariableTable getSymbolTable ()
    { return symbolTable; }

    public int size ()
    {
        return currentCommand;
    }
    public Map getLabelMap () { return labelMap; }
}

// -- start of the parser
start!: one:varBlock two:cmdBlock EOF!
        {
            #start = #([START], #one, #two);
        };
        

varBlock: varDecl (SEMI! varDecl)* (SEMI!)?;
varDecl {String v;}: 
        type v = varnameStr (EQ^ boolConstant)? 
        {
            symbolTable.declarePropositional (v);
        }
    ;
type: BOOL;
varname: ID;
protected varnameStr returns [String val]: 
        v:ID { val = v.getText (); };

cmdBlock: (line { currentCommand++; })+;
line {String lbl;}: 
        (lbl = labelValued 
            {labelMap.put (lbl, new Integer (currentCommand));}
            COL^)? command;

protected labelValued returns [String val]:
        v:ID { val = v.getText (); }
    ;

label: ID;
// -- command 
command: atomicCommand;
atomicCommand: assignBlock | skip | ite |  gotoCmd | choiceCmd;

// -- an assignment block is one or more assignments
// -- separated by a comma
assignBlock: assign (COMMA assign)*;
assign: ID ASSIGNOP^ assignment;
protected assignment: ID! LPAREN! boolExpr COMMA^ boolExpr RPAREN!;

skip : SKIP;
ite: IF^ LPAREN! boolExpr RPAREN! (THEN!)? iteBody;
iteBody: gotoCmd ELSE^ gotoCmd FI!;

gotoCmd: GOTO^ label;
choiceCmd: CHOICE^ LPAREN! label COMMA! label (COMMA! label)* RPAREN!;

// -- a boolean expression
boolExpr:  implExpr;
implExpr : iffExpr (IMPLIES^ iffExpr)*;
iffExpr : orExpr (IFF^ orExpr)*;
orExpr : andExpr ((OR^ | XOR^) andExpr)*;
andExpr : negExpr (AND^ negExpr)*;
negExpr : (NEG^)? basicExpr;
basicExpr : leafExpr | LPAREN! boolExpr RPAREN! ;
protected leafExpr: ID | boolConstant;
boolConstant: TRUE | FALSE | UNKNOWN;


{
    import edu.toronto.cs.mvset.*;
    import edu.toronto.cs.cfa.*;
    import edu.toronto.cs.boolpg.parser.VariableTable.*;
    
    import java.util.*;
}
class BoolProgramBuilder extends TreeParser;
options {defaultErrorHandler = false; }
{
    MvSet mvT;
    MvSet mvF;
    MvSet mvM;
    MvSet mvD;
    CFA cfa;
    int currentCommand = 1;
    VariableTable symbolTable;
    Map labelMap;
    
    MvSet preVarCube;
    MvSet postVarCube;
    int[] preToPostMap;
    int[] postToPreMap;

    boolean useHyperEdges; 
    
    public BoolProgramBuilder (VariableTable _symbolTable, 
                               Map _labelMap, 
                               int size, boolean _useHyperEdges)
    {
        symbolTable = _symbolTable;
        labelMap = _labelMap;
        useHyperEdges = _useHyperEdges;
        cfa = new CFA (size);
        MvSetFactory factory = symbolTable.getMvSetFactory ();
        mvT = factory.top ();
        mvF = factory.bot ();
        mvM = factory.infoBot ();
        mvD = factory.infoTop ();


        preVarCube = factory.buildCube (symbolTable.getVariableIds (0));
        postVarCube = factory.buildCube (symbolTable.getVariableIds (1));
        preToPostMap = symbolTable.variableMap (0, 1);
        postToPreMap = symbolTable.variableMap (1, 0);

    }
    
    public CFA getCFA () { return cfa; }

    MvSet doAssign (MvSet var, MvSet tExp, MvSet fExp)
    {
        MvSet nextVar = currToNext (var);
        
        MvSet leftBranch;
        
        leftBranch = nextVar.eq (mvT).and 
            (tExp.infoAnd (mvT).infoOr 
                (fExp.infoAnd (mvT).not ()));

        MvSet rightBranch;
        rightBranch = nextVar.eq (mvF).and 
            (fExp.infoAnd (mvT).infoOr
                (tExp.infoAnd (mvT).not ()));

        return leftBranch.or 
                (rightBranch.or 
                    (nextVar.eq (mvM).and (mvD)));
      
    }

    MvSet currToNext (MvSet set)
    {
       return set.renameArgs (symbolTable.variableMap (0, 1));
    }
    
    MvSet doSkip () 
    {
        MvSet skip = mvT;

        for (Iterator it = symbolTable.getVariables ().iterator (); 
            it.hasNext ();)
        {
            Variable v = (Variable)it.next ();
            if (! (v instanceof StateVariable)) continue;
            StateVariable var = (StateVariable) v;
            if (!var.isShadow ())
            {
                MvSet mvSet = var.getMvSet ();
                MvSet shadow = ((StateVariable)var.getShadow (1)).getMvSet ();
                shadow = shadow.eq (mvT).and (mvSet).or
                  (shadow.eq (mvF).and (mvSet.not ())).or 
                    (shadow.eq (mvM).and (mvD));
                skip = skip.and (shadow);
               
            }

        }
        return skip;
    }
    
    public MvSet myBranch (MvSet cond)
    {
        return currToNext (cond.eq (mvM)).and (doSkip ());
    }
    
    public MvSet iteBranch (MvSet cond)
    {
        // XXX cannto quite explain this yet
        //MvSet condTru-eNext = currToNext (cond.eq (mvT));
        //return cond.and (doSkip ()).and (condTrueNext);
        //return cond.and (doSkip ());
        
        return currToNext (cond.eq (mvF).not ()).and (cond.and (doSkip ()));
    }


    
    MvRelation toReln (MvSet reln)
    {
        return new MvSetMvRelation (reln, 
            preVarCube, postVarCube, preToPostMap, postToPreMap);
    }


}

start:
        #(START varBlock cmdBlock);

varBlock {MvSet init = mvT; MvSet v;}:
        (v = varDecl { init = init.and (v);})+
        {
            cfa.addNode ("init", mvT);
            cfa.addEdge ("init", 0, 1, toReln (init));
        };

varDecl returns [MvSet mvSet] {MvSet var; MvSet val = mvM; }: 
        (
        type var = varname 
    |
        #(EQ type var = varname val = boolConstant)
        )
        {
            mvSet = null;
            if (val.equals (mvM))
                mvSet = doAssign (var, mvF, mvF);
            else if (val.equals (mvT))
                mvSet = doAssign (var, mvT, mvF);
            else if (val.equals (mvF))
                mvSet = doAssign (var, mvF, mvT);
        }
    ;

type: BOOL;

// -- shoud return variable from the symbol table
varname returns [MvSet val] {String name;} : 
        name = string  
        { val = ((StateVariable)symbolTable.getByName (name)).getMvSet (); };

// -- should return an mvSet representing the type
boolConstant returns [MvSet val]: 
        (TRUE { val = mvT; })
    | 
        (FALSE { val = mvF; })
    | 
        (UNKNOWN {val = mvM; })
    ;

cmdBlock: 
        (line 
            { 
                currentCommand++; 
            }
        )+;

line { String lbl = String.valueOf (currentCommand); }:
        (command
    |
        #(COL lbl = label command))
        {
            cfa.addNode (lbl, mvT);
        }

    ;
label returns [String val]: 
        val = string;

// -- should return the string
string returns [String val]: 
        v:ID { val = v.getText (); };

command:
        atomicCommand;
atomicCommand {int dest;}:
        assignBlock 
    |   skip 
    |   ite 
    |   (dest = gotoCmd 
            {
               cfa.addEdge ("goto", currentCommand, dest, toReln (doSkip ()));
            }
        )
    |   choiceCmd
    ;

assignBlock {MvSet mvSet = mvT; MvSet v;} :
        (v = assign { mvSet = mvSet.and (v); })
        (COMMA v = assign { mvSet = mvSet.and (v); })*
        {
            cfa.addEdge ("assign", currentCommand, currentCommand + 1,
                toReln (mvSet));
        };

assign returns [MvSet mvSet] {MvSet var; MvSet tExp; MvSet fExp; }: 
        #(ASSIGNOP var = varname #(COMMA tExp = boolExpr fExp = boolExpr))
        {
            mvSet = doAssign (var, tExp, fExp);
        }
    ;
gotoCmd returns [int dest] { String lbl; }: 
        #(GOTO lbl = label)
        {
            dest = ((Integer)labelMap.get (lbl)).intValue ();
        }
        ;
choiceCmd :
        #(CHOICE (choiceLabel)+);
protected choiceLabel { String lbl; }:
        lbl = label
        {
            int dest = ((Integer)labelMap.get (lbl)).intValue ();
            cfa.addEdge ("choice-" + lbl, currentCommand, dest, 
                    toReln (doSkip ()));
        }
        ;
ite { MvSet cond; int thenDest; int elseDest; }: 
        #(IF cond = boolExpr #(ELSE thenDest = gotoCmd elseDest = gotoCmd))
        {
            cfa.addEdge ("ite-then", currentCommand, thenDest, 
                toReln (iteBranch (cond)));
            cfa.addEdge ("ite-else", currentCommand, elseDest, 
                toReln (iteBranch (cond.not ())));
            if (useHyperEdges)
               cfa.addEdge ("ite-merge", currentCommand, thenDest, 
                toReln (myBranch(cond)), elseDest);
            // -- add things to cfa
            //cfa.addEdge ("ite-then", currentCommand, thenDest,
            //doSkip ().and (cond));

            //cfa.addEdge ("ite-else", currentCommand, elseDest,
            //doSkip ().and (cond.not ()));
        }
    ;
skip: 
        SKIP 
        { 
            cfa.addEdge ("skip", currentCommand, 
                currentCommand + 1, toReln (doSkip ()));
        }
    ;

boolExpr returns [MvSet val] {MvSet lhs; MvSet rhs;}:
        val = leafExpr 
    |
        #(OR lhs = boolExpr rhs = boolExpr) { val = lhs.or (rhs); }
    |
        #(AND lhs = boolExpr rhs = boolExpr) { val = lhs.and (rhs); }
    |
        #(NEG lhs = boolExpr) { val = lhs.not (); }
    |
        #(IMPLIES lhs = boolExpr rhs = boolExpr) { val = lhs.impl (rhs); }
    |
        #(IFF lhs = boolExpr rhs = boolExpr) 
        { val = lhs.impl (rhs).and (rhs.impl (lhs)); }
    ;

leafExpr returns [MvSet val]:
        val = varname 
    |
        val = boolConstant
    ;
