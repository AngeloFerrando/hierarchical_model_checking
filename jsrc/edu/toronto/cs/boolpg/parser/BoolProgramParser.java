// $ANTLR 2.7.4: "boolpg.g" -> "BoolProgramParser.java"$
 package edu.toronto.cs.boolpg.parser;
import antlr.TokenBuffer;
import antlr.TokenStreamException;
import antlr.TokenStreamIOException;
import antlr.ANTLRException;
import antlr.LLkParser;
import antlr.Token;
import antlr.TokenStream;
import antlr.RecognitionException;
import antlr.NoViableAltException;
import antlr.MismatchedTokenException;
import antlr.SemanticException;
import antlr.ParserSharedInputState;
import antlr.collections.impl.BitSet;
import antlr.collections.AST;
import java.util.Hashtable;
import antlr.ASTFactory;
import antlr.ASTPair;
import antlr.collections.impl.ASTArray;

import java.util.*;
import edu.toronto.cs.boolpg.parser.VariableTable.*;

public class BoolProgramParser extends antlr.LLkParser       implements BoolProgramLexerTokenTypes
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

protected BoolProgramParser(TokenBuffer tokenBuf, int k) {
  super(tokenBuf,k);
  tokenNames = _tokenNames;
  buildTokenTypeASTClassMap();
  astFactory = new ASTFactory(getTokenTypeToASTClassMap());
}

public BoolProgramParser(TokenBuffer tokenBuf) {
  this(tokenBuf,2);
}

protected BoolProgramParser(TokenStream lexer, int k) {
  super(lexer,k);
  tokenNames = _tokenNames;
  buildTokenTypeASTClassMap();
  astFactory = new ASTFactory(getTokenTypeToASTClassMap());
}

public BoolProgramParser(TokenStream lexer) {
  this(lexer,2);
}

public BoolProgramParser(ParserSharedInputState state) {
  super(state,2);
  tokenNames = _tokenNames;
  buildTokenTypeASTClassMap();
  astFactory = new ASTFactory(getTokenTypeToASTClassMap());
}

	public final void start() throws RecognitionException, TokenStreamException {
		
		returnAST = null;
		ASTPair currentAST = new ASTPair();
		AST start_AST = null;
		AST one_AST = null;
		AST two_AST = null;
		
		varBlock();
		one_AST = (AST)returnAST;
		cmdBlock();
		two_AST = (AST)returnAST;
		match(Token.EOF_TYPE);
		start_AST = (AST)currentAST.root;
		
		start_AST = (AST)astFactory.make( (new ASTArray(3)).add(astFactory.create(START)).add(one_AST).add(two_AST));
		
		currentAST.root = start_AST;
		currentAST.child = start_AST!=null &&start_AST.getFirstChild()!=null ?
			start_AST.getFirstChild() : start_AST;
		currentAST.advanceChildToEnd();
		returnAST = start_AST;
	}
	
	public final void varBlock() throws RecognitionException, TokenStreamException {
		
		returnAST = null;
		ASTPair currentAST = new ASTPair();
		AST varBlock_AST = null;
		
		varDecl();
		astFactory.addASTChild(currentAST, returnAST);
		{
		_loop40:
		do {
			if ((LA(1)==SEMI) && (LA(2)==BOOL)) {
				match(SEMI);
				varDecl();
				astFactory.addASTChild(currentAST, returnAST);
			}
			else {
				break _loop40;
			}
			
		} while (true);
		}
		{
		switch ( LA(1)) {
		case SEMI:
		{
			match(SEMI);
			break;
		}
		case ID:
		case SKIP:
		case IF:
		case GOTO:
		case CHOICE:
		{
			break;
		}
		default:
		{
			throw new NoViableAltException(LT(1), getFilename());
		}
		}
		}
		varBlock_AST = (AST)currentAST.root;
		returnAST = varBlock_AST;
	}
	
	public final void cmdBlock() throws RecognitionException, TokenStreamException {
		
		returnAST = null;
		ASTPair currentAST = new ASTPair();
		AST cmdBlock_AST = null;
		
		{
		int _cnt49=0;
		_loop49:
		do {
			if ((_tokenSet_0.member(LA(1)))) {
				line();
				astFactory.addASTChild(currentAST, returnAST);
				currentCommand++;
			}
			else {
				if ( _cnt49>=1 ) { break _loop49; } else {throw new NoViableAltException(LT(1), getFilename());}
			}
			
			_cnt49++;
		} while (true);
		}
		cmdBlock_AST = (AST)currentAST.root;
		returnAST = cmdBlock_AST;
	}
	
	public final void varDecl() throws RecognitionException, TokenStreamException {
		
		returnAST = null;
		ASTPair currentAST = new ASTPair();
		AST varDecl_AST = null;
		String v;
		
		type();
		astFactory.addASTChild(currentAST, returnAST);
		v=varnameStr();
		astFactory.addASTChild(currentAST, returnAST);
		{
		switch ( LA(1)) {
		case EQ:
		{
			AST tmp4_AST = null;
			tmp4_AST = astFactory.create(LT(1));
			astFactory.makeASTRoot(currentAST, tmp4_AST);
			match(EQ);
			boolConstant();
			astFactory.addASTChild(currentAST, returnAST);
			break;
		}
		case ID:
		case SEMI:
		case SKIP:
		case IF:
		case GOTO:
		case CHOICE:
		{
			break;
		}
		default:
		{
			throw new NoViableAltException(LT(1), getFilename());
		}
		}
		}
		
		symbolTable.declarePropositional (v);
		
		varDecl_AST = (AST)currentAST.root;
		returnAST = varDecl_AST;
	}
	
	public final void type() throws RecognitionException, TokenStreamException {
		
		returnAST = null;
		ASTPair currentAST = new ASTPair();
		AST type_AST = null;
		
		AST tmp5_AST = null;
		tmp5_AST = astFactory.create(LT(1));
		astFactory.addASTChild(currentAST, tmp5_AST);
		match(BOOL);
		type_AST = (AST)currentAST.root;
		returnAST = type_AST;
	}
	
	protected final String  varnameStr() throws RecognitionException, TokenStreamException {
		String val;
		
		returnAST = null;
		ASTPair currentAST = new ASTPair();
		AST varnameStr_AST = null;
		Token  v = null;
		AST v_AST = null;
		
		v = LT(1);
		v_AST = astFactory.create(v);
		astFactory.addASTChild(currentAST, v_AST);
		match(ID);
		val = v.getText ();
		varnameStr_AST = (AST)currentAST.root;
		returnAST = varnameStr_AST;
		return val;
	}
	
	public final void boolConstant() throws RecognitionException, TokenStreamException {
		
		returnAST = null;
		ASTPair currentAST = new ASTPair();
		AST boolConstant_AST = null;
		
		switch ( LA(1)) {
		case TRUE:
		{
			AST tmp6_AST = null;
			tmp6_AST = astFactory.create(LT(1));
			astFactory.addASTChild(currentAST, tmp6_AST);
			match(TRUE);
			boolConstant_AST = (AST)currentAST.root;
			break;
		}
		case FALSE:
		{
			AST tmp7_AST = null;
			tmp7_AST = astFactory.create(LT(1));
			astFactory.addASTChild(currentAST, tmp7_AST);
			match(FALSE);
			boolConstant_AST = (AST)currentAST.root;
			break;
		}
		case UNKNOWN:
		{
			AST tmp8_AST = null;
			tmp8_AST = astFactory.create(LT(1));
			astFactory.addASTChild(currentAST, tmp8_AST);
			match(UNKNOWN);
			boolConstant_AST = (AST)currentAST.root;
			break;
		}
		default:
		{
			throw new NoViableAltException(LT(1), getFilename());
		}
		}
		returnAST = boolConstant_AST;
	}
	
	public final void varname() throws RecognitionException, TokenStreamException {
		
		returnAST = null;
		ASTPair currentAST = new ASTPair();
		AST varname_AST = null;
		
		AST tmp9_AST = null;
		tmp9_AST = astFactory.create(LT(1));
		astFactory.addASTChild(currentAST, tmp9_AST);
		match(ID);
		varname_AST = (AST)currentAST.root;
		returnAST = varname_AST;
	}
	
	public final void line() throws RecognitionException, TokenStreamException {
		
		returnAST = null;
		ASTPair currentAST = new ASTPair();
		AST line_AST = null;
		String lbl;
		
		{
		if ((LA(1)==ID) && (LA(2)==COL)) {
			lbl=labelValued();
			astFactory.addASTChild(currentAST, returnAST);
			labelMap.put (lbl, new Integer (currentCommand));
			AST tmp10_AST = null;
			tmp10_AST = astFactory.create(LT(1));
			astFactory.makeASTRoot(currentAST, tmp10_AST);
			match(COL);
		}
		else if ((_tokenSet_0.member(LA(1))) && (_tokenSet_1.member(LA(2)))) {
		}
		else {
			throw new NoViableAltException(LT(1), getFilename());
		}
		
		}
		command();
		astFactory.addASTChild(currentAST, returnAST);
		line_AST = (AST)currentAST.root;
		returnAST = line_AST;
	}
	
	protected final String  labelValued() throws RecognitionException, TokenStreamException {
		String val;
		
		returnAST = null;
		ASTPair currentAST = new ASTPair();
		AST labelValued_AST = null;
		Token  v = null;
		AST v_AST = null;
		
		v = LT(1);
		v_AST = astFactory.create(v);
		astFactory.addASTChild(currentAST, v_AST);
		match(ID);
		val = v.getText ();
		labelValued_AST = (AST)currentAST.root;
		returnAST = labelValued_AST;
		return val;
	}
	
	public final void command() throws RecognitionException, TokenStreamException {
		
		returnAST = null;
		ASTPair currentAST = new ASTPair();
		AST command_AST = null;
		
		atomicCommand();
		astFactory.addASTChild(currentAST, returnAST);
		command_AST = (AST)currentAST.root;
		returnAST = command_AST;
	}
	
	public final void label() throws RecognitionException, TokenStreamException {
		
		returnAST = null;
		ASTPair currentAST = new ASTPair();
		AST label_AST = null;
		
		AST tmp11_AST = null;
		tmp11_AST = astFactory.create(LT(1));
		astFactory.addASTChild(currentAST, tmp11_AST);
		match(ID);
		label_AST = (AST)currentAST.root;
		returnAST = label_AST;
	}
	
	public final void atomicCommand() throws RecognitionException, TokenStreamException {
		
		returnAST = null;
		ASTPair currentAST = new ASTPair();
		AST atomicCommand_AST = null;
		
		switch ( LA(1)) {
		case ID:
		{
			assignBlock();
			astFactory.addASTChild(currentAST, returnAST);
			atomicCommand_AST = (AST)currentAST.root;
			break;
		}
		case SKIP:
		{
			skip();
			astFactory.addASTChild(currentAST, returnAST);
			atomicCommand_AST = (AST)currentAST.root;
			break;
		}
		case IF:
		{
			ite();
			astFactory.addASTChild(currentAST, returnAST);
			atomicCommand_AST = (AST)currentAST.root;
			break;
		}
		case GOTO:
		{
			gotoCmd();
			astFactory.addASTChild(currentAST, returnAST);
			atomicCommand_AST = (AST)currentAST.root;
			break;
		}
		case CHOICE:
		{
			choiceCmd();
			astFactory.addASTChild(currentAST, returnAST);
			atomicCommand_AST = (AST)currentAST.root;
			break;
		}
		default:
		{
			throw new NoViableAltException(LT(1), getFilename());
		}
		}
		returnAST = atomicCommand_AST;
	}
	
	public final void assignBlock() throws RecognitionException, TokenStreamException {
		
		returnAST = null;
		ASTPair currentAST = new ASTPair();
		AST assignBlock_AST = null;
		
		assign();
		astFactory.addASTChild(currentAST, returnAST);
		{
		_loop58:
		do {
			if ((LA(1)==COMMA)) {
				AST tmp12_AST = null;
				tmp12_AST = astFactory.create(LT(1));
				astFactory.addASTChild(currentAST, tmp12_AST);
				match(COMMA);
				assign();
				astFactory.addASTChild(currentAST, returnAST);
			}
			else {
				break _loop58;
			}
			
		} while (true);
		}
		assignBlock_AST = (AST)currentAST.root;
		returnAST = assignBlock_AST;
	}
	
	public final void skip() throws RecognitionException, TokenStreamException {
		
		returnAST = null;
		ASTPair currentAST = new ASTPair();
		AST skip_AST = null;
		
		AST tmp13_AST = null;
		tmp13_AST = astFactory.create(LT(1));
		astFactory.addASTChild(currentAST, tmp13_AST);
		match(SKIP);
		skip_AST = (AST)currentAST.root;
		returnAST = skip_AST;
	}
	
	public final void ite() throws RecognitionException, TokenStreamException {
		
		returnAST = null;
		ASTPair currentAST = new ASTPair();
		AST ite_AST = null;
		
		AST tmp14_AST = null;
		tmp14_AST = astFactory.create(LT(1));
		astFactory.makeASTRoot(currentAST, tmp14_AST);
		match(IF);
		match(LPAREN);
		boolExpr();
		astFactory.addASTChild(currentAST, returnAST);
		match(RPAREN);
		{
		switch ( LA(1)) {
		case THEN:
		{
			match(THEN);
			break;
		}
		case GOTO:
		{
			break;
		}
		default:
		{
			throw new NoViableAltException(LT(1), getFilename());
		}
		}
		}
		iteBody();
		astFactory.addASTChild(currentAST, returnAST);
		ite_AST = (AST)currentAST.root;
		returnAST = ite_AST;
	}
	
	public final void gotoCmd() throws RecognitionException, TokenStreamException {
		
		returnAST = null;
		ASTPair currentAST = new ASTPair();
		AST gotoCmd_AST = null;
		
		AST tmp18_AST = null;
		tmp18_AST = astFactory.create(LT(1));
		astFactory.makeASTRoot(currentAST, tmp18_AST);
		match(GOTO);
		label();
		astFactory.addASTChild(currentAST, returnAST);
		gotoCmd_AST = (AST)currentAST.root;
		returnAST = gotoCmd_AST;
	}
	
	public final void choiceCmd() throws RecognitionException, TokenStreamException {
		
		returnAST = null;
		ASTPair currentAST = new ASTPair();
		AST choiceCmd_AST = null;
		
		AST tmp19_AST = null;
		tmp19_AST = astFactory.create(LT(1));
		astFactory.makeASTRoot(currentAST, tmp19_AST);
		match(CHOICE);
		match(LPAREN);
		label();
		astFactory.addASTChild(currentAST, returnAST);
		match(COMMA);
		label();
		astFactory.addASTChild(currentAST, returnAST);
		{
		_loop68:
		do {
			if ((LA(1)==COMMA)) {
				match(COMMA);
				label();
				astFactory.addASTChild(currentAST, returnAST);
			}
			else {
				break _loop68;
			}
			
		} while (true);
		}
		match(RPAREN);
		choiceCmd_AST = (AST)currentAST.root;
		returnAST = choiceCmd_AST;
	}
	
	public final void assign() throws RecognitionException, TokenStreamException {
		
		returnAST = null;
		ASTPair currentAST = new ASTPair();
		AST assign_AST = null;
		
		AST tmp24_AST = null;
		tmp24_AST = astFactory.create(LT(1));
		astFactory.addASTChild(currentAST, tmp24_AST);
		match(ID);
		AST tmp25_AST = null;
		tmp25_AST = astFactory.create(LT(1));
		astFactory.makeASTRoot(currentAST, tmp25_AST);
		match(ASSIGNOP);
		assignment();
		astFactory.addASTChild(currentAST, returnAST);
		assign_AST = (AST)currentAST.root;
		returnAST = assign_AST;
	}
	
	protected final void assignment() throws RecognitionException, TokenStreamException {
		
		returnAST = null;
		ASTPair currentAST = new ASTPair();
		AST assignment_AST = null;
		
		match(ID);
		match(LPAREN);
		boolExpr();
		astFactory.addASTChild(currentAST, returnAST);
		AST tmp28_AST = null;
		tmp28_AST = astFactory.create(LT(1));
		astFactory.makeASTRoot(currentAST, tmp28_AST);
		match(COMMA);
		boolExpr();
		astFactory.addASTChild(currentAST, returnAST);
		match(RPAREN);
		assignment_AST = (AST)currentAST.root;
		returnAST = assignment_AST;
	}
	
	public final void boolExpr() throws RecognitionException, TokenStreamException {
		
		returnAST = null;
		ASTPair currentAST = new ASTPair();
		AST boolExpr_AST = null;
		
		implExpr();
		astFactory.addASTChild(currentAST, returnAST);
		boolExpr_AST = (AST)currentAST.root;
		returnAST = boolExpr_AST;
	}
	
	public final void iteBody() throws RecognitionException, TokenStreamException {
		
		returnAST = null;
		ASTPair currentAST = new ASTPair();
		AST iteBody_AST = null;
		
		gotoCmd();
		astFactory.addASTChild(currentAST, returnAST);
		AST tmp30_AST = null;
		tmp30_AST = astFactory.create(LT(1));
		astFactory.makeASTRoot(currentAST, tmp30_AST);
		match(ELSE);
		gotoCmd();
		astFactory.addASTChild(currentAST, returnAST);
		match(FI);
		iteBody_AST = (AST)currentAST.root;
		returnAST = iteBody_AST;
	}
	
	public final void implExpr() throws RecognitionException, TokenStreamException {
		
		returnAST = null;
		ASTPair currentAST = new ASTPair();
		AST implExpr_AST = null;
		
		iffExpr();
		astFactory.addASTChild(currentAST, returnAST);
		{
		_loop72:
		do {
			if ((LA(1)==IMPLIES)) {
				AST tmp32_AST = null;
				tmp32_AST = astFactory.create(LT(1));
				astFactory.makeASTRoot(currentAST, tmp32_AST);
				match(IMPLIES);
				iffExpr();
				astFactory.addASTChild(currentAST, returnAST);
			}
			else {
				break _loop72;
			}
			
		} while (true);
		}
		implExpr_AST = (AST)currentAST.root;
		returnAST = implExpr_AST;
	}
	
	public final void iffExpr() throws RecognitionException, TokenStreamException {
		
		returnAST = null;
		ASTPair currentAST = new ASTPair();
		AST iffExpr_AST = null;
		
		orExpr();
		astFactory.addASTChild(currentAST, returnAST);
		{
		_loop75:
		do {
			if ((LA(1)==IFF)) {
				AST tmp33_AST = null;
				tmp33_AST = astFactory.create(LT(1));
				astFactory.makeASTRoot(currentAST, tmp33_AST);
				match(IFF);
				orExpr();
				astFactory.addASTChild(currentAST, returnAST);
			}
			else {
				break _loop75;
			}
			
		} while (true);
		}
		iffExpr_AST = (AST)currentAST.root;
		returnAST = iffExpr_AST;
	}
	
	public final void orExpr() throws RecognitionException, TokenStreamException {
		
		returnAST = null;
		ASTPair currentAST = new ASTPair();
		AST orExpr_AST = null;
		
		andExpr();
		astFactory.addASTChild(currentAST, returnAST);
		{
		_loop79:
		do {
			if ((LA(1)==OR||LA(1)==XOR)) {
				{
				switch ( LA(1)) {
				case OR:
				{
					AST tmp34_AST = null;
					tmp34_AST = astFactory.create(LT(1));
					astFactory.makeASTRoot(currentAST, tmp34_AST);
					match(OR);
					break;
				}
				case XOR:
				{
					AST tmp35_AST = null;
					tmp35_AST = astFactory.create(LT(1));
					astFactory.makeASTRoot(currentAST, tmp35_AST);
					match(XOR);
					break;
				}
				default:
				{
					throw new NoViableAltException(LT(1), getFilename());
				}
				}
				}
				andExpr();
				astFactory.addASTChild(currentAST, returnAST);
			}
			else {
				break _loop79;
			}
			
		} while (true);
		}
		orExpr_AST = (AST)currentAST.root;
		returnAST = orExpr_AST;
	}
	
	public final void andExpr() throws RecognitionException, TokenStreamException {
		
		returnAST = null;
		ASTPair currentAST = new ASTPair();
		AST andExpr_AST = null;
		
		negExpr();
		astFactory.addASTChild(currentAST, returnAST);
		{
		_loop82:
		do {
			if ((LA(1)==AND)) {
				AST tmp36_AST = null;
				tmp36_AST = astFactory.create(LT(1));
				astFactory.makeASTRoot(currentAST, tmp36_AST);
				match(AND);
				negExpr();
				astFactory.addASTChild(currentAST, returnAST);
			}
			else {
				break _loop82;
			}
			
		} while (true);
		}
		andExpr_AST = (AST)currentAST.root;
		returnAST = andExpr_AST;
	}
	
	public final void negExpr() throws RecognitionException, TokenStreamException {
		
		returnAST = null;
		ASTPair currentAST = new ASTPair();
		AST negExpr_AST = null;
		
		{
		switch ( LA(1)) {
		case NEG:
		{
			AST tmp37_AST = null;
			tmp37_AST = astFactory.create(LT(1));
			astFactory.makeASTRoot(currentAST, tmp37_AST);
			match(NEG);
			break;
		}
		case LPAREN:
		case ID:
		case TRUE:
		case FALSE:
		case UNKNOWN:
		{
			break;
		}
		default:
		{
			throw new NoViableAltException(LT(1), getFilename());
		}
		}
		}
		basicExpr();
		astFactory.addASTChild(currentAST, returnAST);
		negExpr_AST = (AST)currentAST.root;
		returnAST = negExpr_AST;
	}
	
	public final void basicExpr() throws RecognitionException, TokenStreamException {
		
		returnAST = null;
		ASTPair currentAST = new ASTPair();
		AST basicExpr_AST = null;
		
		switch ( LA(1)) {
		case ID:
		case TRUE:
		case FALSE:
		case UNKNOWN:
		{
			leafExpr();
			astFactory.addASTChild(currentAST, returnAST);
			basicExpr_AST = (AST)currentAST.root;
			break;
		}
		case LPAREN:
		{
			match(LPAREN);
			boolExpr();
			astFactory.addASTChild(currentAST, returnAST);
			match(RPAREN);
			basicExpr_AST = (AST)currentAST.root;
			break;
		}
		default:
		{
			throw new NoViableAltException(LT(1), getFilename());
		}
		}
		returnAST = basicExpr_AST;
	}
	
	protected final void leafExpr() throws RecognitionException, TokenStreamException {
		
		returnAST = null;
		ASTPair currentAST = new ASTPair();
		AST leafExpr_AST = null;
		
		switch ( LA(1)) {
		case ID:
		{
			AST tmp40_AST = null;
			tmp40_AST = astFactory.create(LT(1));
			astFactory.addASTChild(currentAST, tmp40_AST);
			match(ID);
			leafExpr_AST = (AST)currentAST.root;
			break;
		}
		case TRUE:
		case FALSE:
		case UNKNOWN:
		{
			boolConstant();
			astFactory.addASTChild(currentAST, returnAST);
			leafExpr_AST = (AST)currentAST.root;
			break;
		}
		default:
		{
			throw new NoViableAltException(LT(1), getFilename());
		}
		}
		returnAST = leafExpr_AST;
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
	
	protected void buildTokenTypeASTClassMap() {
		tokenTypeToASTClassMap=null;
	};
	
	private static final long[] mk_tokenSet_0() {
		long[] data = { 1700808097792L, 0L};
		return data;
	}
	public static final BitSet _tokenSet_0 = new BitSet(mk_tokenSet_0());
	private static final long[] mk_tokenSet_1() {
		long[] data = { 1700808097874L, 0L};
		return data;
	}
	public static final BitSet _tokenSet_1 = new BitSet(mk_tokenSet_1());
	
	}
