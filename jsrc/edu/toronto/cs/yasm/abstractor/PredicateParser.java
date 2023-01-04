// $ANTLR 2.7.4: "PredicateParser.g" -> "PredicateParser.java"$
 package edu.toronto.cs.yasm.abstractor; 
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

  import edu.toronto.cs.cparser.*;
  import edu.toronto.cs.cparser.block.*;

public class PredicateParser extends antlr.LLkParser       implements PredicateParserTokenTypes
 {

protected PredicateParser(TokenBuffer tokenBuf, int k) {
  super(tokenBuf,k);
  tokenNames = _tokenNames;
  buildTokenTypeASTClassMap();
  astFactory = new ASTFactory(getTokenTypeToASTClassMap());
}

public PredicateParser(TokenBuffer tokenBuf) {
  this(tokenBuf,1);
}

protected PredicateParser(TokenStream lexer, int k) {
  super(lexer,k);
  tokenNames = _tokenNames;
  buildTokenTypeASTClassMap();
  astFactory = new ASTFactory(getTokenTypeToASTClassMap());
}

public PredicateParser(TokenStream lexer) {
  this(lexer,1);
}

public PredicateParser(ParserSharedInputState state) {
  super(state,1);
  tokenNames = _tokenNames;
  buildTokenTypeASTClassMap();
  astFactory = new ASTFactory(getTokenTypeToASTClassMap());
}

	public final void parse() throws RecognitionException, TokenStreamException {
		
		returnAST = null;
		ASTPair currentAST = new ASTPair();
		Block parse_AST = null;
		
		{
		int _cnt3=0;
		_loop3:
		do {
			if ((_tokenSet_0.member(LA(1)))) {
				predicate();
				astFactory.addASTChild(currentAST, returnAST);
				match(SEMI);
			}
			else {
				if ( _cnt3>=1 ) { break _loop3; } else {throw new NoViableAltException(LT(1), getFilename());}
			}
			
			_cnt3++;
		} while (true);
		}
		parse_AST = (Block)currentAST.root;
		returnAST = parse_AST;
	}
	
	public final void predicate() throws RecognitionException, TokenStreamException {
		
		returnAST = null;
		ASTPair currentAST = new ASTPair();
		Block predicate_AST = null;
		
		boolExpr();
		astFactory.addASTChild(currentAST, returnAST);
		{
		_loop6:
		do {
			if ((LA(1)==LOR||LA(1)==LAND)) {
				boolOp();
				astFactory.addASTChild(currentAST, returnAST);
				boolExpr();
				astFactory.addASTChild(currentAST, returnAST);
			}
			else {
				break _loop6;
			}
			
		} while (true);
		}
		predicate_AST = (Block)currentAST.root;
		returnAST = predicate_AST;
	}
	
	public final void boolExpr() throws RecognitionException, TokenStreamException {
		
		returnAST = null;
		ASTPair currentAST = new ASTPair();
		Block boolExpr_AST = null;
		Block e1_AST = null;
		Block op_AST = null;
		Block e2_AST = null;
		
		arithExpr();
		e1_AST = (Block)returnAST;
		arithCompOp();
		op_AST = (Block)returnAST;
		arithExpr();
		e2_AST = (Block)returnAST;
		boolExpr_AST = (Block)currentAST.root;
		boolExpr_AST = (Block)astFactory.make( (new ASTArray(3)).add(op_AST).add(e1_AST).add(e2_AST));
		currentAST.root = boolExpr_AST;
		currentAST.child = boolExpr_AST!=null &&boolExpr_AST.getFirstChild()!=null ?
			boolExpr_AST.getFirstChild() : boolExpr_AST;
		currentAST.advanceChildToEnd();
		returnAST = boolExpr_AST;
	}
	
	public final void boolOp() throws RecognitionException, TokenStreamException {
		
		returnAST = null;
		ASTPair currentAST = new ASTPair();
		Block boolOp_AST = null;
		
		switch ( LA(1)) {
		case LOR:
		{
			Block tmp2_AST = null;
			tmp2_AST = (Block)astFactory.create(LT(1));
			astFactory.addASTChild(currentAST, tmp2_AST);
			match(LOR);
			boolOp_AST = (Block)currentAST.root;
			break;
		}
		case LAND:
		{
			Block tmp3_AST = null;
			tmp3_AST = (Block)astFactory.create(LT(1));
			astFactory.addASTChild(currentAST, tmp3_AST);
			match(LAND);
			boolOp_AST = (Block)currentAST.root;
			break;
		}
		default:
		{
			throw new NoViableAltException(LT(1), getFilename());
		}
		}
		returnAST = boolOp_AST;
	}
	
	public final void arithExpr() throws RecognitionException, TokenStreamException {
		
		returnAST = null;
		ASTPair currentAST = new ASTPair();
		Block arithExpr_AST = null;
		
		switch ( LA(1)) {
		case ID:
		case IntOctalConst:
		case LongOctalConst:
		case UnsignedOctalConst:
		case IntIntConst:
		case LongIntConst:
		case UnsignedIntConst:
		case IntHexConst:
		case LongHexConst:
		case UnsignedHexConst:
		{
			arithFact();
			astFactory.addASTChild(currentAST, returnAST);
			{
			_loop11:
			do {
				if ((_tokenSet_1.member(LA(1)))) {
					{
					switch ( LA(1)) {
					case PLUS:
					{
						Block tmp4_AST = null;
						tmp4_AST = (Block)astFactory.create(LT(1));
						astFactory.makeASTRoot(currentAST, tmp4_AST);
						match(PLUS);
						break;
					}
					case MINUS:
					{
						Block tmp5_AST = null;
						tmp5_AST = (Block)astFactory.create(LT(1));
						astFactory.makeASTRoot(currentAST, tmp5_AST);
						match(MINUS);
						break;
					}
					case STAR:
					{
						Block tmp6_AST = null;
						tmp6_AST = (Block)astFactory.create(LT(1));
						astFactory.makeASTRoot(currentAST, tmp6_AST);
						match(STAR);
						break;
					}
					case DIV:
					{
						Block tmp7_AST = null;
						tmp7_AST = (Block)astFactory.create(LT(1));
						astFactory.makeASTRoot(currentAST, tmp7_AST);
						match(DIV);
						break;
					}
					case MOD:
					{
						Block tmp8_AST = null;
						tmp8_AST = (Block)astFactory.create(LT(1));
						astFactory.makeASTRoot(currentAST, tmp8_AST);
						match(MOD);
						break;
					}
					default:
					{
						throw new NoViableAltException(LT(1), getFilename());
					}
					}
					}
					arithFact();
					astFactory.addASTChild(currentAST, returnAST);
				}
				else {
					break _loop11;
				}
				
			} while (true);
			}
			arithExpr_AST = (Block)currentAST.root;
			break;
		}
		case LPAREN:
		{
			match(LPAREN);
			arithExpr();
			astFactory.addASTChild(currentAST, returnAST);
			match(RPAREN);
			arithExpr_AST = (Block)currentAST.root;
			break;
		}
		default:
		{
			throw new NoViableAltException(LT(1), getFilename());
		}
		}
		returnAST = arithExpr_AST;
	}
	
	public final void arithCompOp() throws RecognitionException, TokenStreamException {
		
		returnAST = null;
		ASTPair currentAST = new ASTPair();
		Block arithCompOp_AST = null;
		
		switch ( LA(1)) {
		case LT:
		{
			Block tmp11_AST = null;
			tmp11_AST = (Block)astFactory.create(LT(1));
			astFactory.addASTChild(currentAST, tmp11_AST);
			match(LT);
			arithCompOp_AST = (Block)currentAST.root;
			break;
		}
		case LTE:
		{
			Block tmp12_AST = null;
			tmp12_AST = (Block)astFactory.create(LT(1));
			astFactory.addASTChild(currentAST, tmp12_AST);
			match(LTE);
			arithCompOp_AST = (Block)currentAST.root;
			break;
		}
		case GT:
		{
			Block tmp13_AST = null;
			tmp13_AST = (Block)astFactory.create(LT(1));
			astFactory.addASTChild(currentAST, tmp13_AST);
			match(GT);
			arithCompOp_AST = (Block)currentAST.root;
			break;
		}
		case GTE:
		{
			Block tmp14_AST = null;
			tmp14_AST = (Block)astFactory.create(LT(1));
			astFactory.addASTChild(currentAST, tmp14_AST);
			match(GTE);
			arithCompOp_AST = (Block)currentAST.root;
			break;
		}
		case EQUAL:
		{
			Block tmp15_AST = null;
			tmp15_AST = (Block)astFactory.create(LT(1));
			astFactory.addASTChild(currentAST, tmp15_AST);
			match(EQUAL);
			arithCompOp_AST = (Block)currentAST.root;
			break;
		}
		case NOT_EQUAL:
		{
			Block tmp16_AST = null;
			tmp16_AST = (Block)astFactory.create(LT(1));
			astFactory.addASTChild(currentAST, tmp16_AST);
			match(NOT_EQUAL);
			arithCompOp_AST = (Block)currentAST.root;
			break;
		}
		default:
		{
			throw new NoViableAltException(LT(1), getFilename());
		}
		}
		returnAST = arithCompOp_AST;
	}
	
	public final void arithFact() throws RecognitionException, TokenStreamException {
		
		returnAST = null;
		ASTPair currentAST = new ASTPair();
		Block arithFact_AST = null;
		
		switch ( LA(1)) {
		case ID:
		{
			Block tmp17_AST = null;
			tmp17_AST = (Block)astFactory.create(LT(1));
			astFactory.addASTChild(currentAST, tmp17_AST);
			match(ID);
			arithFact_AST = (Block)currentAST.root;
			break;
		}
		case IntOctalConst:
		case LongOctalConst:
		case UnsignedOctalConst:
		case IntIntConst:
		case LongIntConst:
		case UnsignedIntConst:
		case IntHexConst:
		case LongHexConst:
		case UnsignedHexConst:
		{
			intConst();
			astFactory.addASTChild(currentAST, returnAST);
			arithFact_AST = (Block)currentAST.root;
			break;
		}
		default:
		{
			throw new NoViableAltException(LT(1), getFilename());
		}
		}
		returnAST = arithFact_AST;
	}
	
	protected final void intConst() throws RecognitionException, TokenStreamException {
		
		returnAST = null;
		ASTPair currentAST = new ASTPair();
		Block intConst_AST = null;
		
		switch ( LA(1)) {
		case IntOctalConst:
		{
			Block tmp18_AST = null;
			tmp18_AST = (Block)astFactory.create(LT(1));
			astFactory.addASTChild(currentAST, tmp18_AST);
			match(IntOctalConst);
			intConst_AST = (Block)currentAST.root;
			break;
		}
		case LongOctalConst:
		{
			Block tmp19_AST = null;
			tmp19_AST = (Block)astFactory.create(LT(1));
			astFactory.addASTChild(currentAST, tmp19_AST);
			match(LongOctalConst);
			intConst_AST = (Block)currentAST.root;
			break;
		}
		case UnsignedOctalConst:
		{
			Block tmp20_AST = null;
			tmp20_AST = (Block)astFactory.create(LT(1));
			astFactory.addASTChild(currentAST, tmp20_AST);
			match(UnsignedOctalConst);
			intConst_AST = (Block)currentAST.root;
			break;
		}
		case IntIntConst:
		{
			Block tmp21_AST = null;
			tmp21_AST = (Block)astFactory.create(LT(1));
			astFactory.addASTChild(currentAST, tmp21_AST);
			match(IntIntConst);
			intConst_AST = (Block)currentAST.root;
			break;
		}
		case LongIntConst:
		{
			Block tmp22_AST = null;
			tmp22_AST = (Block)astFactory.create(LT(1));
			astFactory.addASTChild(currentAST, tmp22_AST);
			match(LongIntConst);
			intConst_AST = (Block)currentAST.root;
			break;
		}
		case UnsignedIntConst:
		{
			Block tmp23_AST = null;
			tmp23_AST = (Block)astFactory.create(LT(1));
			astFactory.addASTChild(currentAST, tmp23_AST);
			match(UnsignedIntConst);
			intConst_AST = (Block)currentAST.root;
			break;
		}
		case IntHexConst:
		{
			Block tmp24_AST = null;
			tmp24_AST = (Block)astFactory.create(LT(1));
			astFactory.addASTChild(currentAST, tmp24_AST);
			match(IntHexConst);
			intConst_AST = (Block)currentAST.root;
			break;
		}
		case LongHexConst:
		{
			Block tmp25_AST = null;
			tmp25_AST = (Block)astFactory.create(LT(1));
			astFactory.addASTChild(currentAST, tmp25_AST);
			match(LongHexConst);
			intConst_AST = (Block)currentAST.root;
			break;
		}
		case UnsignedHexConst:
		{
			Block tmp26_AST = null;
			tmp26_AST = (Block)astFactory.create(LT(1));
			astFactory.addASTChild(currentAST, tmp26_AST);
			match(UnsignedHexConst);
			intConst_AST = (Block)currentAST.root;
			break;
		}
		default:
		{
			throw new NoViableAltException(LT(1), getFilename());
		}
		}
		returnAST = intConst_AST;
	}
	
	public final void arithOp() throws RecognitionException, TokenStreamException {
		
		returnAST = null;
		ASTPair currentAST = new ASTPair();
		Block arithOp_AST = null;
		
		switch ( LA(1)) {
		case PLUS:
		{
			Block tmp27_AST = null;
			tmp27_AST = (Block)astFactory.create(LT(1));
			astFactory.addASTChild(currentAST, tmp27_AST);
			match(PLUS);
			arithOp_AST = (Block)currentAST.root;
			break;
		}
		case MINUS:
		{
			Block tmp28_AST = null;
			tmp28_AST = (Block)astFactory.create(LT(1));
			astFactory.addASTChild(currentAST, tmp28_AST);
			match(MINUS);
			arithOp_AST = (Block)currentAST.root;
			break;
		}
		case STAR:
		{
			Block tmp29_AST = null;
			tmp29_AST = (Block)astFactory.create(LT(1));
			astFactory.addASTChild(currentAST, tmp29_AST);
			match(STAR);
			arithOp_AST = (Block)currentAST.root;
			break;
		}
		case DIV:
		{
			Block tmp30_AST = null;
			tmp30_AST = (Block)astFactory.create(LT(1));
			astFactory.addASTChild(currentAST, tmp30_AST);
			match(DIV);
			arithOp_AST = (Block)currentAST.root;
			break;
		}
		case MOD:
		{
			Block tmp31_AST = null;
			tmp31_AST = (Block)astFactory.create(LT(1));
			astFactory.addASTChild(currentAST, tmp31_AST);
			match(MOD);
			arithOp_AST = (Block)currentAST.root;
			break;
		}
		default:
		{
			throw new NoViableAltException(LT(1), getFilename());
		}
		}
		returnAST = arithOp_AST;
	}
	
	
	public static final String[] _tokenNames = {
		"<0>",
		"EOF",
		"<2>",
		"NULL_TREE_LOOKAHEAD",
		"\"typedef\"",
		"\"__asm__\"",
		"SEMI",
		"ASSIGN",
		"\"struct\"",
		"\"union\"",
		"\"enum\"",
		"\"auto\"",
		"\"register\"",
		"\"extern\"",
		"\"static\"",
		"\"__inline\"",
		"\"const\"",
		"\"volatile\"",
		"\"void\"",
		"\"char\"",
		"\"short\"",
		"\"int\"",
		"\"long\"",
		"\"float\"",
		"\"double\"",
		"\"signed\"",
		"\"unsigned\"",
		"ID",
		"LCURLY",
		"RCURLY",
		"COMMA",
		"COLON",
		"STAR",
		"\"__attribute__\"",
		"LPAREN",
		"RPAREN",
		"\"asm\"",
		"LBRACKET",
		"RBRACKET",
		"VARARGS",
		"\"while\"",
		"\"goto\"",
		"\"break\"",
		"\"return\"",
		"\"if\"",
		"\"else\"",
		"\"__nd_goto\"",
		"StringLiteral",
		"INC",
		"DEC",
		"PTR",
		"DOT",
		"LOR",
		"LAND",
		"BOR",
		"BXOR",
		"BAND",
		"EQUAL",
		"NOT_EQUAL",
		"LT",
		"LTE",
		"GT",
		"GTE",
		"LSHIFT",
		"RSHIFT",
		"PLUS",
		"MINUS",
		"DIV",
		"MOD",
		"\"sizeof\"",
		"BNOT",
		"LNOT",
		"CharLiteral",
		"IntOctalConst",
		"LongOctalConst",
		"UnsignedOctalConst",
		"IntIntConst",
		"LongIntConst",
		"UnsignedIntConst",
		"IntHexConst",
		"LongHexConst",
		"UnsignedHexConst",
		"FloatDoubleConst",
		"DoubleDoubleConst",
		"LongDoubleConst",
		"NTypedefName",
		"NInitDecl",
		"NDeclarator",
		"NStructDeclarator",
		"NDeclaration",
		"NCast",
		"NPointerGroup",
		"NFunctionCallArgs",
		"NNonemptyAbstractDeclarator",
		"NInitializer",
		"NStatementExpr",
		"NEmptyExpression",
		"NParameterTypeList",
		"NFunctionDef",
		"NCompoundStatement",
		"NParameterDeclaration",
		"NCommaExpr",
		"NUnaryExpr",
		"NLabel",
		"NPostfixExpr",
		"NRangeExpr",
		"NStringSeq",
		"NAsmAttribute",
		"NGnuAsmExpr",
		"NProgram",
		"NPureExpr",
		"NPureExpressionGroup",
		"NFunctionDeclSpecifiers",
		"NFunctionBody",
		"NFunctionCall",
		"NFunctionCallStmt",
		"NFunctionCallAssignStmt",
		"NAssignStmt",
		"NImplicitAssignStmt",
		"NWhile",
		"NIf",
		"NScope",
		"NLocalDeclarations",
		"NDeclSpecifiers",
		"NEmptyScope",
		"NReturn",
		"NGoto",
		"NContinue",
		"NBreak",
		"NLabelledStmt",
		"NArrayIndex",
		"NArrayElement",
		"NBlock",
		"NStructFields",
		"NNDGoto",
		"NLCurlyInitializer",
		"NLHS",
		"NRHS",
		"Vocabulary",
		"QUESTION",
		"DIV_ASSIGN",
		"PLUS_ASSIGN",
		"MINUS_ASSIGN",
		"STAR_ASSIGN",
		"MOD_ASSIGN",
		"RSHIFT_ASSIGN",
		"LSHIFT_ASSIGN",
		"BAND_ASSIGN",
		"BOR_ASSIGN",
		"BXOR_ASSIGN",
		"Whitespace",
		"Comment",
		"CPPComment",
		"a line directive",
		"Space",
		"LineDirective",
		"BadStringLiteral",
		"Escape",
		"Digit",
		"LongSuffix",
		"UnsignedSuffix",
		"FloatSuffix",
		"Exponent",
		"Number"
	};
	
	protected void buildTokenTypeASTClassMap() {
		tokenTypeToASTClassMap=null;
	};
	
	private static final long[] mk_tokenSet_0() {
		long[] data = { 17314086912L, 261632L, 0L, 0L};
		return data;
	}
	public static final BitSet _tokenSet_0 = new BitSet(mk_tokenSet_0());
	private static final long[] mk_tokenSet_1() {
		long[] data = { 4294967296L, 30L, 0L, 0L};
		return data;
	}
	public static final BitSet _tokenSet_1 = new BitSet(mk_tokenSet_1());
	
	}
