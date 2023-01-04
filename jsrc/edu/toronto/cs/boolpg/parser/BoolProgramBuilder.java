// $ANTLR 2.7.4: "boolpg.g" -> "BoolProgramBuilder.java"$
 package edu.toronto.cs.boolpg.parser;
import antlr.TreeParser;
import antlr.Token;
import antlr.collections.AST;
import antlr.RecognitionException;
import antlr.ANTLRException;
import antlr.NoViableAltException;
import antlr.MismatchedTokenException;
import antlr.SemanticException;
import antlr.collections.impl.BitSet;
import antlr.ASTPair;
import antlr.collections.impl.ASTArray;

    import edu.toronto.cs.mvset.*;
    import edu.toronto.cs.cfa.*;
    import edu.toronto.cs.boolpg.parser.VariableTable.*;
    
    import java.util.*;


public class BoolProgramBuilder extends antlr.TreeParser       implements BoolProgramLexerTokenTypes
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


public BoolProgramBuilder() {
	tokenNames = _tokenNames;
}

	public final void start(AST _t) throws RecognitionException {
		
		AST start_AST_in = (_t == ASTNULL) ? null : (AST)_t;
		
		AST __t89 = _t;
		AST tmp41_AST_in = (AST)_t;
		match(_t,START);
		_t = _t.getFirstChild();
		varBlock(_t);
		_t = _retTree;
		cmdBlock(_t);
		_t = _retTree;
		_t = __t89;
		_t = _t.getNextSibling();
		_retTree = _t;
	}
	
	public final void varBlock(AST _t) throws RecognitionException {
		
		AST varBlock_AST_in = (_t == ASTNULL) ? null : (AST)_t;
		MvSet init = mvT; MvSet v;
		
		{
		int _cnt92=0;
		_loop92:
		do {
			if (_t==null) _t=ASTNULL;
			if ((_t.getType()==EQ||_t.getType()==BOOL)) {
				v=varDecl(_t);
				_t = _retTree;
				init = init.and (v);
			}
			else {
				if ( _cnt92>=1 ) { break _loop92; } else {throw new NoViableAltException(_t);}
			}
			
			_cnt92++;
		} while (true);
		}
		
		cfa.addNode ("init", mvT);
		cfa.addEdge ("init", 0, 1, toReln (init));
		
		_retTree = _t;
	}
	
	public final void cmdBlock(AST _t) throws RecognitionException {
		
		AST cmdBlock_AST_in = (_t == ASTNULL) ? null : (AST)_t;
		
		{
		int _cnt104=0;
		_loop104:
		do {
			if (_t==null) _t=ASTNULL;
			if ((_tokenSet_0.member(_t.getType()))) {
				line(_t);
				_t = _retTree;
				
				currentCommand++; 
				
			}
			else {
				if ( _cnt104>=1 ) { break _loop104; } else {throw new NoViableAltException(_t);}
			}
			
			_cnt104++;
		} while (true);
		}
		_retTree = _t;
	}
	
	public final MvSet  varDecl(AST _t) throws RecognitionException {
		MvSet mvSet;
		
		AST varDecl_AST_in = (_t == ASTNULL) ? null : (AST)_t;
		MvSet var; MvSet val = mvM;
		
		{
		if (_t==null) _t=ASTNULL;
		switch ( _t.getType()) {
		case BOOL:
		{
			type(_t);
			_t = _retTree;
			var=varname(_t);
			_t = _retTree;
			break;
		}
		case EQ:
		{
			AST __t95 = _t;
			AST tmp42_AST_in = (AST)_t;
			match(_t,EQ);
			_t = _t.getFirstChild();
			type(_t);
			_t = _retTree;
			var=varname(_t);
			_t = _retTree;
			val=boolConstant(_t);
			_t = _retTree;
			_t = __t95;
			_t = _t.getNextSibling();
			break;
		}
		default:
		{
			throw new NoViableAltException(_t);
		}
		}
		}
		
		mvSet = null;
		if (val.equals (mvM))
		mvSet = doAssign (var, mvF, mvF);
		else if (val.equals (mvT))
		mvSet = doAssign (var, mvT, mvF);
		else if (val.equals (mvF))
		mvSet = doAssign (var, mvF, mvT);
		
		_retTree = _t;
		return mvSet;
	}
	
	public final void type(AST _t) throws RecognitionException {
		
		AST type_AST_in = (_t == ASTNULL) ? null : (AST)_t;
		
		AST tmp43_AST_in = (AST)_t;
		match(_t,BOOL);
		_t = _t.getNextSibling();
		_retTree = _t;
	}
	
	public final MvSet  varname(AST _t) throws RecognitionException {
		MvSet val;
		
		AST varname_AST_in = (_t == ASTNULL) ? null : (AST)_t;
		String name;
		
		name=string(_t);
		_t = _retTree;
		val = ((StateVariable)symbolTable.getByName (name)).getMvSet ();
		_retTree = _t;
		return val;
	}
	
	public final MvSet  boolConstant(AST _t) throws RecognitionException {
		MvSet val;
		
		AST boolConstant_AST_in = (_t == ASTNULL) ? null : (AST)_t;
		
		if (_t==null) _t=ASTNULL;
		switch ( _t.getType()) {
		case TRUE:
		{
			{
			AST tmp44_AST_in = (AST)_t;
			match(_t,TRUE);
			_t = _t.getNextSibling();
			val = mvT;
			}
			break;
		}
		case FALSE:
		{
			{
			AST tmp45_AST_in = (AST)_t;
			match(_t,FALSE);
			_t = _t.getNextSibling();
			val = mvF;
			}
			break;
		}
		case UNKNOWN:
		{
			{
			AST tmp46_AST_in = (AST)_t;
			match(_t,UNKNOWN);
			_t = _t.getNextSibling();
			val = mvM;
			}
			break;
		}
		default:
		{
			throw new NoViableAltException(_t);
		}
		}
		_retTree = _t;
		return val;
	}
	
	public final String  string(AST _t) throws RecognitionException {
		String val;
		
		AST string_AST_in = (_t == ASTNULL) ? null : (AST)_t;
		AST v = null;
		
		v = (AST)_t;
		match(_t,ID);
		_t = _t.getNextSibling();
		val = v.getText ();
		_retTree = _t;
		return val;
	}
	
	public final void line(AST _t) throws RecognitionException {
		
		AST line_AST_in = (_t == ASTNULL) ? null : (AST)_t;
		String lbl = String.valueOf (currentCommand);
		
		{
		if (_t==null) _t=ASTNULL;
		switch ( _t.getType()) {
		case ASSIGNOP:
		case SKIP:
		case IF:
		case GOTO:
		case CHOICE:
		{
			command(_t);
			_t = _retTree;
			break;
		}
		case COL:
		{
			AST __t107 = _t;
			AST tmp47_AST_in = (AST)_t;
			match(_t,COL);
			_t = _t.getFirstChild();
			lbl=label(_t);
			_t = _retTree;
			command(_t);
			_t = _retTree;
			_t = __t107;
			_t = _t.getNextSibling();
			break;
		}
		default:
		{
			throw new NoViableAltException(_t);
		}
		}
		}
		
		cfa.addNode (lbl, mvT);
		
		_retTree = _t;
	}
	
	public final void command(AST _t) throws RecognitionException {
		
		AST command_AST_in = (_t == ASTNULL) ? null : (AST)_t;
		
		atomicCommand(_t);
		_t = _retTree;
		_retTree = _t;
	}
	
	public final String  label(AST _t) throws RecognitionException {
		String val;
		
		AST label_AST_in = (_t == ASTNULL) ? null : (AST)_t;
		
		val=string(_t);
		_t = _retTree;
		_retTree = _t;
		return val;
	}
	
	public final void atomicCommand(AST _t) throws RecognitionException {
		
		AST atomicCommand_AST_in = (_t == ASTNULL) ? null : (AST)_t;
		int dest;
		
		if (_t==null) _t=ASTNULL;
		switch ( _t.getType()) {
		case ASSIGNOP:
		{
			assignBlock(_t);
			_t = _retTree;
			break;
		}
		case SKIP:
		{
			skip(_t);
			_t = _retTree;
			break;
		}
		case IF:
		{
			ite(_t);
			_t = _retTree;
			break;
		}
		case GOTO:
		{
			{
			dest=gotoCmd(_t);
			_t = _retTree;
			
			cfa.addEdge ("goto", currentCommand, dest, toReln (doSkip ()));
			
			}
			break;
		}
		case CHOICE:
		{
			choiceCmd(_t);
			_t = _retTree;
			break;
		}
		default:
		{
			throw new NoViableAltException(_t);
		}
		}
		_retTree = _t;
	}
	
	public final void assignBlock(AST _t) throws RecognitionException {
		
		AST assignBlock_AST_in = (_t == ASTNULL) ? null : (AST)_t;
		MvSet mvSet = mvT; MvSet v;
		
		{
		v=assign(_t);
		_t = _retTree;
		mvSet = mvSet.and (v);
		}
		{
		_loop116:
		do {
			if (_t==null) _t=ASTNULL;
			if ((_t.getType()==COMMA)) {
				AST tmp48_AST_in = (AST)_t;
				match(_t,COMMA);
				_t = _t.getNextSibling();
				v=assign(_t);
				_t = _retTree;
				mvSet = mvSet.and (v);
			}
			else {
				break _loop116;
			}
			
		} while (true);
		}
		
		cfa.addEdge ("assign", currentCommand, currentCommand + 1,
		toReln (mvSet));
		
		_retTree = _t;
	}
	
	public final void skip(AST _t) throws RecognitionException {
		
		AST skip_AST_in = (_t == ASTNULL) ? null : (AST)_t;
		
		AST tmp49_AST_in = (AST)_t;
		match(_t,SKIP);
		_t = _t.getNextSibling();
		
		cfa.addEdge ("skip", currentCommand, 
		currentCommand + 1, toReln (doSkip ()));
		
		_retTree = _t;
	}
	
	public final void ite(AST _t) throws RecognitionException {
		
		AST ite_AST_in = (_t == ASTNULL) ? null : (AST)_t;
		MvSet cond; int thenDest; int elseDest;
		
		AST __t128 = _t;
		AST tmp50_AST_in = (AST)_t;
		match(_t,IF);
		_t = _t.getFirstChild();
		cond=boolExpr(_t);
		_t = _retTree;
		AST __t129 = _t;
		AST tmp51_AST_in = (AST)_t;
		match(_t,ELSE);
		_t = _t.getFirstChild();
		thenDest=gotoCmd(_t);
		_t = _retTree;
		elseDest=gotoCmd(_t);
		_t = _retTree;
		_t = __t129;
		_t = _t.getNextSibling();
		_t = __t128;
		_t = _t.getNextSibling();
		
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
		
		_retTree = _t;
	}
	
	public final int  gotoCmd(AST _t) throws RecognitionException {
		int dest;
		
		AST gotoCmd_AST_in = (_t == ASTNULL) ? null : (AST)_t;
		String lbl;
		
		AST __t121 = _t;
		AST tmp52_AST_in = (AST)_t;
		match(_t,GOTO);
		_t = _t.getFirstChild();
		lbl=label(_t);
		_t = _retTree;
		_t = __t121;
		_t = _t.getNextSibling();
		
		dest = ((Integer)labelMap.get (lbl)).intValue ();
		
		_retTree = _t;
		return dest;
	}
	
	public final void choiceCmd(AST _t) throws RecognitionException {
		
		AST choiceCmd_AST_in = (_t == ASTNULL) ? null : (AST)_t;
		
		AST __t123 = _t;
		AST tmp53_AST_in = (AST)_t;
		match(_t,CHOICE);
		_t = _t.getFirstChild();
		{
		int _cnt125=0;
		_loop125:
		do {
			if (_t==null) _t=ASTNULL;
			if ((_t.getType()==ID)) {
				choiceLabel(_t);
				_t = _retTree;
			}
			else {
				if ( _cnt125>=1 ) { break _loop125; } else {throw new NoViableAltException(_t);}
			}
			
			_cnt125++;
		} while (true);
		}
		_t = __t123;
		_t = _t.getNextSibling();
		_retTree = _t;
	}
	
	public final MvSet  assign(AST _t) throws RecognitionException {
		MvSet mvSet;
		
		AST assign_AST_in = (_t == ASTNULL) ? null : (AST)_t;
		MvSet var; MvSet tExp; MvSet fExp;
		
		AST __t118 = _t;
		AST tmp54_AST_in = (AST)_t;
		match(_t,ASSIGNOP);
		_t = _t.getFirstChild();
		var=varname(_t);
		_t = _retTree;
		AST __t119 = _t;
		AST tmp55_AST_in = (AST)_t;
		match(_t,COMMA);
		_t = _t.getFirstChild();
		tExp=boolExpr(_t);
		_t = _retTree;
		fExp=boolExpr(_t);
		_t = _retTree;
		_t = __t119;
		_t = _t.getNextSibling();
		_t = __t118;
		_t = _t.getNextSibling();
		
		mvSet = doAssign (var, tExp, fExp);
		
		_retTree = _t;
		return mvSet;
	}
	
	public final MvSet  boolExpr(AST _t) throws RecognitionException {
		MvSet val;
		
		AST boolExpr_AST_in = (_t == ASTNULL) ? null : (AST)_t;
		MvSet lhs; MvSet rhs;
		
		if (_t==null) _t=ASTNULL;
		switch ( _t.getType()) {
		case ID:
		case TRUE:
		case FALSE:
		case UNKNOWN:
		{
			val=leafExpr(_t);
			_t = _retTree;
			break;
		}
		case OR:
		{
			AST __t132 = _t;
			AST tmp56_AST_in = (AST)_t;
			match(_t,OR);
			_t = _t.getFirstChild();
			lhs=boolExpr(_t);
			_t = _retTree;
			rhs=boolExpr(_t);
			_t = _retTree;
			_t = __t132;
			_t = _t.getNextSibling();
			val = lhs.or (rhs);
			break;
		}
		case AND:
		{
			AST __t133 = _t;
			AST tmp57_AST_in = (AST)_t;
			match(_t,AND);
			_t = _t.getFirstChild();
			lhs=boolExpr(_t);
			_t = _retTree;
			rhs=boolExpr(_t);
			_t = _retTree;
			_t = __t133;
			_t = _t.getNextSibling();
			val = lhs.and (rhs);
			break;
		}
		case NEG:
		{
			AST __t134 = _t;
			AST tmp58_AST_in = (AST)_t;
			match(_t,NEG);
			_t = _t.getFirstChild();
			lhs=boolExpr(_t);
			_t = _retTree;
			_t = __t134;
			_t = _t.getNextSibling();
			val = lhs.not ();
			break;
		}
		case IMPLIES:
		{
			AST __t135 = _t;
			AST tmp59_AST_in = (AST)_t;
			match(_t,IMPLIES);
			_t = _t.getFirstChild();
			lhs=boolExpr(_t);
			_t = _retTree;
			rhs=boolExpr(_t);
			_t = _retTree;
			_t = __t135;
			_t = _t.getNextSibling();
			val = lhs.impl (rhs);
			break;
		}
		case IFF:
		{
			AST __t136 = _t;
			AST tmp60_AST_in = (AST)_t;
			match(_t,IFF);
			_t = _t.getFirstChild();
			lhs=boolExpr(_t);
			_t = _retTree;
			rhs=boolExpr(_t);
			_t = _retTree;
			_t = __t136;
			_t = _t.getNextSibling();
			val = lhs.impl (rhs).and (rhs.impl (lhs));
			break;
		}
		default:
		{
			throw new NoViableAltException(_t);
		}
		}
		_retTree = _t;
		return val;
	}
	
	protected final void choiceLabel(AST _t) throws RecognitionException {
		
		AST choiceLabel_AST_in = (_t == ASTNULL) ? null : (AST)_t;
		String lbl;
		
		lbl=label(_t);
		_t = _retTree;
		
		int dest = ((Integer)labelMap.get (lbl)).intValue ();
		cfa.addEdge ("choice-" + lbl, currentCommand, dest, 
		toReln (doSkip ()));
		
		_retTree = _t;
	}
	
	public final MvSet  leafExpr(AST _t) throws RecognitionException {
		MvSet val;
		
		AST leafExpr_AST_in = (_t == ASTNULL) ? null : (AST)_t;
		
		if (_t==null) _t=ASTNULL;
		switch ( _t.getType()) {
		case ID:
		{
			val=varname(_t);
			_t = _retTree;
			break;
		}
		case TRUE:
		case FALSE:
		case UNKNOWN:
		{
			val=boolConstant(_t);
			_t = _retTree;
			break;
		}
		default:
		{
			throw new NoViableAltException(_t);
		}
		}
		_retTree = _t;
		return val;
	}
	
	
	public static final String[] _tokenNames = {
		"<0>",
		"EOF",
		"<2>",
		"NULL_TREE_LOOKAHEAD",
		"LPAREN",
		"RPAREN",
		"ASSIGNOP",
		"IMPLIES",
		"IFF",
		"EQ",
		"OR",
		"AND",
		"NEG",
		"LBRACE",
		"RBRACE",
		"COMMA",
		"PLUS",
		"MINUS",
		"MULT",
		"DIV",
		"ID",
		"ATOM",
		"DIGIT",
		"NUMBER",
		"COL",
		"SEMI",
		"COMMENT",
		"WS",
		"NEWLINE",
		"\"start\"",
		"\"bool\"",
		"\"true\"",
		"\"false\"",
		"\"unknown\"",
		"\"skip\"",
		"\"if\"",
		"\"then\"",
		"\"else\"",
		"\"fi\"",
		"\"goto\"",
		"\"choice\"",
		"XOR"
	};
	
	private static final long[] mk_tokenSet_0() {
		long[] data = { 1700823826496L, 0L};
		return data;
	}
	public static final BitSet _tokenSet_0 = new BitSet(mk_tokenSet_0());
	}
	
