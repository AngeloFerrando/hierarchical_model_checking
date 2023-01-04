// $ANTLR 2.7.4: "DeclsParser.g" -> "DeclsParser.java"$

    package edu.toronto.cs.yasm.abstractor;

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

    import edu.toronto.cs.cparser.block.*;
    import edu.toronto.cs.cparser.*;
    import edu.toronto.cs.tp.cvcl.*;
      // *** NOTE: Expr and Type below refer to CVCL classes
    import java.util.List;


public class DeclsParser extends antlr.TreeParser       implements DeclsParserTokenTypes
 {

  int ctr = 0;
public DeclsParser() {
	tokenNames = _tokenNames;
}

	public final void declarationList(AST _t,
		ValidityChecker vc, List decls
	) throws RecognitionException {
		
		Block declarationList_AST_in = (_t == ASTNULL) ? null : (Block)_t;
		returnAST = null;
		ASTPair currentAST = new ASTPair();
		Block declarationList_AST = null;
		Expr e;
		
		if (_t==null) _t=ASTNULL;
		switch ( _t.getType()) {
		case NLocalDeclarations:
		{
			AST __t2 = _t;
			Block tmp1_AST = null;
			Block tmp1_AST_in = null;
			tmp1_AST = (Block)astFactory.create((Block)_t);
			tmp1_AST_in = (Block)_t;
			astFactory.addASTChild(currentAST, tmp1_AST);
			ASTPair __currentAST2 = currentAST.copy();
			currentAST.root = currentAST.child;
			currentAST.child = null;
			match(_t,NLocalDeclarations);
			_t = _t.getFirstChild();
			{
			_loop4:
			do {
				if (_t==null) _t=ASTNULL;
				if ((_t.getType()==NBlock)) {
					e=declaration(_t,vc);
					_t = _retTree;
					astFactory.addASTChild(currentAST, returnAST);
					decls.add (e);
				}
				else {
					break _loop4;
				}
				
			} while (true);
			}
			currentAST = __currentAST2;
			_t = __t2;
			_t = _t.getNextSibling();
			declarationList_AST = (Block)currentAST.root;
			break;
		}
		case NParameterTypeList:
		{
			AST __t5 = _t;
			Block tmp2_AST = null;
			Block tmp2_AST_in = null;
			tmp2_AST = (Block)astFactory.create((Block)_t);
			tmp2_AST_in = (Block)_t;
			astFactory.addASTChild(currentAST, tmp2_AST);
			ASTPair __currentAST5 = currentAST.copy();
			currentAST.root = currentAST.child;
			currentAST.child = null;
			match(_t,NParameterTypeList);
			_t = _t.getFirstChild();
			{
			_loop7:
			do {
				if (_t==null) _t=ASTNULL;
				if ((_t.getType()==NBlock)) {
					e=declaration(_t,vc);
					_t = _retTree;
					astFactory.addASTChild(currentAST, returnAST);
					decls.add (e);
				}
				else {
					break _loop7;
				}
				
			} while (true);
			}
			currentAST = __currentAST5;
			_t = __t5;
			_t = _t.getNextSibling();
			declarationList_AST = (Block)currentAST.root;
			break;
		}
		default:
		{
			throw new NoViableAltException(_t);
		}
		}
		returnAST = declarationList_AST;
		_retTree = _t;
	}
	
	public final Expr  declaration(AST _t,
		ValidityChecker vc
	) throws RecognitionException {
		Expr e;
		
		Block declaration_AST_in = (_t == ASTNULL) ? null : (Block)_t;
		returnAST = null;
		ASTPair currentAST = new ASTPair();
		Block declaration_AST = null;
		
		Type t;
		String declName;
		e = null;
		
		
		AST __t9 = _t;
		Block tmp3_AST = null;
		Block tmp3_AST_in = null;
		tmp3_AST = (Block)astFactory.create((Block)_t);
		tmp3_AST_in = (Block)_t;
		astFactory.addASTChild(currentAST, tmp3_AST);
		ASTPair __currentAST9 = currentAST.copy();
		currentAST.root = currentAST.child;
		currentAST.child = null;
		match(_t,NBlock);
		_t = _t.getFirstChild();
		t=declSpecifiers(_t,vc);
		_t = _retTree;
		astFactory.addASTChild(currentAST, returnAST);
		
		// System.err.println ("%%% " + ctr + " line: " + ##.getLineNum ());
		// System.err.println ("%%% " + ctr + " type: " + t);
		
		{
		if (_t==null) _t=ASTNULL;
		switch ( _t.getType()) {
		case NDeclarator:
		{
			declName=declarator(_t);
			_t = _retTree;
			astFactory.addASTChild(currentAST, returnAST);
			e = vc.varExpr (declName, t);
			break;
		}
		case 3:
		case NInitializer:
		{
			break;
		}
		default:
		{
			throw new NoViableAltException(_t);
		}
		}
		}
		{
		if (_t==null) _t=ASTNULL;
		switch ( _t.getType()) {
		case NInitializer:
		{
			Block tmp4_AST = null;
			Block tmp4_AST_in = null;
			tmp4_AST = (Block)astFactory.create((Block)_t);
			tmp4_AST_in = (Block)_t;
			astFactory.addASTChild(currentAST, tmp4_AST);
			match(_t,NInitializer);
			_t = _t.getNextSibling();
			break;
		}
		case 3:
		{
			break;
		}
		default:
		{
			throw new NoViableAltException(_t);
		}
		}
		}
		ctr++;
		currentAST = __currentAST9;
		_t = __t9;
		_t = _t.getNextSibling();
		declaration_AST = (Block)currentAST.root;
		returnAST = declaration_AST;
		_retTree = _t;
		return e;
	}
	
	public final Type  declSpecifiers(AST _t,
		ValidityChecker vc
	) throws RecognitionException {
		Type t;
		
		Block declSpecifiers_AST_in = (_t == ASTNULL) ? null : (Block)_t;
		returnAST = null;
		ASTPair currentAST = new ASTPair();
		Block declSpecifiers_AST = null;
		
		AST __t13 = _t;
		Block tmp5_AST = null;
		Block tmp5_AST_in = null;
		tmp5_AST = (Block)astFactory.create((Block)_t);
		tmp5_AST_in = (Block)_t;
		astFactory.addASTChild(currentAST, tmp5_AST);
		ASTPair __currentAST13 = currentAST.copy();
		currentAST.root = currentAST.child;
		currentAST.child = null;
		match(_t,NDeclSpecifiers);
		_t = _t.getFirstChild();
		t=typeSpecifier(_t,vc);
		_t = _retTree;
		astFactory.addASTChild(currentAST, returnAST);
		{
		if (_t==null) _t=ASTNULL;
		switch ( _t.getType()) {
		case NPointerGroup:
		{
			pointerGroup(_t);
			_t = _retTree;
			astFactory.addASTChild(currentAST, returnAST);
			t = vc.intType ();
			break;
		}
		case 3:
		{
			break;
		}
		default:
		{
			throw new NoViableAltException(_t);
		}
		}
		}
		currentAST = __currentAST13;
		_t = __t13;
		_t = _t.getNextSibling();
		declSpecifiers_AST = (Block)currentAST.root;
		returnAST = declSpecifiers_AST;
		_retTree = _t;
		return t;
	}
	
	public final String  declarator(AST _t) throws RecognitionException {
		String declName;
		
		Block declarator_AST_in = (_t == ASTNULL) ? null : (Block)_t;
		returnAST = null;
		ASTPair currentAST = new ASTPair();
		Block declarator_AST = null;
		Block id = null;
		Block id_AST = null;
		
		AST __t27 = _t;
		Block tmp6_AST = null;
		Block tmp6_AST_in = null;
		tmp6_AST = (Block)astFactory.create((Block)_t);
		tmp6_AST_in = (Block)_t;
		astFactory.addASTChild(currentAST, tmp6_AST);
		ASTPair __currentAST27 = currentAST.copy();
		currentAST.root = currentAST.child;
		currentAST.child = null;
		match(_t,NDeclarator);
		_t = _t.getFirstChild();
		{
		if (_t==null) _t=ASTNULL;
		switch ( _t.getType()) {
		case ID:
		{
			id = (Block)_t;
			Block id_AST_in = null;
			id_AST = (Block)astFactory.create(id);
			astFactory.addASTChild(currentAST, id_AST);
			match(_t,ID);
			_t = _t.getNextSibling();
			declName = id.getText ();
			break;
		}
		case LPAREN:
		{
			Block tmp7_AST = null;
			Block tmp7_AST_in = null;
			tmp7_AST = (Block)astFactory.create((Block)_t);
			tmp7_AST_in = (Block)_t;
			astFactory.addASTChild(currentAST, tmp7_AST);
			match(_t,LPAREN);
			_t = _t.getNextSibling();
			declName=declarator(_t);
			_t = _retTree;
			astFactory.addASTChild(currentAST, returnAST);
			Block tmp8_AST = null;
			Block tmp8_AST_in = null;
			tmp8_AST = (Block)astFactory.create((Block)_t);
			tmp8_AST_in = (Block)_t;
			astFactory.addASTChild(currentAST, tmp8_AST);
			match(_t,RPAREN);
			_t = _t.getNextSibling();
			break;
		}
		default:
		{
			throw new NoViableAltException(_t);
		}
		}
		}
		currentAST = __currentAST27;
		_t = __t27;
		_t = _t.getNextSibling();
		declarator_AST = (Block)currentAST.root;
		returnAST = declarator_AST;
		_retTree = _t;
		return declName;
	}
	
	public final Type  typeSpecifier(AST _t,
		ValidityChecker vc
	) throws RecognitionException {
		Type t;
		
		Block typeSpecifier_AST_in = (_t == ASTNULL) ? null : (Block)_t;
		returnAST = null;
		ASTPair currentAST = new ASTPair();
		Block typeSpecifier_AST = null;
		
		if (_t==null) _t=ASTNULL;
		switch ( _t.getType()) {
		case LITERAL_short:
		case LITERAL_int:
		case LITERAL_long:
		{
			{
			if (_t==null) _t=ASTNULL;
			switch ( _t.getType()) {
			case LITERAL_short:
			{
				Block tmp9_AST = null;
				Block tmp9_AST_in = null;
				tmp9_AST = (Block)astFactory.create((Block)_t);
				tmp9_AST_in = (Block)_t;
				astFactory.addASTChild(currentAST, tmp9_AST);
				match(_t,LITERAL_short);
				_t = _t.getNextSibling();
				break;
			}
			case LITERAL_int:
			{
				Block tmp10_AST = null;
				Block tmp10_AST_in = null;
				tmp10_AST = (Block)astFactory.create((Block)_t);
				tmp10_AST_in = (Block)_t;
				astFactory.addASTChild(currentAST, tmp10_AST);
				match(_t,LITERAL_int);
				_t = _t.getNextSibling();
				break;
			}
			case LITERAL_long:
			{
				Block tmp11_AST = null;
				Block tmp11_AST_in = null;
				tmp11_AST = (Block)astFactory.create((Block)_t);
				tmp11_AST_in = (Block)_t;
				astFactory.addASTChild(currentAST, tmp11_AST);
				match(_t,LITERAL_long);
				_t = _t.getNextSibling();
				break;
			}
			default:
			{
				throw new NoViableAltException(_t);
			}
			}
			}
			t = vc.intType ();
			typeSpecifier_AST = (Block)currentAST.root;
			break;
		}
		case LITERAL_float:
		case LITERAL_double:
		{
			{
			if (_t==null) _t=ASTNULL;
			switch ( _t.getType()) {
			case LITERAL_float:
			{
				Block tmp12_AST = null;
				Block tmp12_AST_in = null;
				tmp12_AST = (Block)astFactory.create((Block)_t);
				tmp12_AST_in = (Block)_t;
				astFactory.addASTChild(currentAST, tmp12_AST);
				match(_t,LITERAL_float);
				_t = _t.getNextSibling();
				break;
			}
			case LITERAL_double:
			{
				Block tmp13_AST = null;
				Block tmp13_AST_in = null;
				tmp13_AST = (Block)astFactory.create((Block)_t);
				tmp13_AST_in = (Block)_t;
				astFactory.addASTChild(currentAST, tmp13_AST);
				match(_t,LITERAL_double);
				_t = _t.getNextSibling();
				break;
			}
			default:
			{
				throw new NoViableAltException(_t);
			}
			}
			}
			t = vc.realType ();
			typeSpecifier_AST = (Block)currentAST.root;
			break;
		}
		case LITERAL_void:
		{
			Block tmp14_AST = null;
			Block tmp14_AST_in = null;
			tmp14_AST = (Block)astFactory.create((Block)_t);
			tmp14_AST_in = (Block)_t;
			astFactory.addASTChild(currentAST, tmp14_AST);
			match(_t,LITERAL_void);
			_t = _t.getNextSibling();
			t = vc.createType ("VOID_DUMMY");
			typeSpecifier_AST = (Block)currentAST.root;
			break;
		}
		case LITERAL_struct:
		{
			t=structSpecifier(_t,vc);
			_t = _retTree;
			astFactory.addASTChild(currentAST, returnAST);
			typeSpecifier_AST = (Block)currentAST.root;
			break;
		}
		default:
		{
			throw new NoViableAltException(_t);
		}
		}
		returnAST = typeSpecifier_AST;
		_retTree = _t;
		return t;
	}
	
	public final void pointerGroup(AST _t) throws RecognitionException {
		
		Block pointerGroup_AST_in = (_t == ASTNULL) ? null : (Block)_t;
		returnAST = null;
		ASTPair currentAST = new ASTPair();
		Block pointerGroup_AST = null;
		
		AST __t21 = _t;
		Block tmp15_AST = null;
		Block tmp15_AST_in = null;
		tmp15_AST = (Block)astFactory.create((Block)_t);
		tmp15_AST_in = (Block)_t;
		astFactory.addASTChild(currentAST, tmp15_AST);
		ASTPair __currentAST21 = currentAST.copy();
		currentAST.root = currentAST.child;
		currentAST.child = null;
		match(_t,NPointerGroup);
		_t = _t.getFirstChild();
		{
		int _cnt23=0;
		_loop23:
		do {
			if (_t==null) _t=ASTNULL;
			if ((_t.getType()==STAR)) {
				Block tmp16_AST = null;
				Block tmp16_AST_in = null;
				tmp16_AST = (Block)astFactory.create((Block)_t);
				tmp16_AST_in = (Block)_t;
				astFactory.addASTChild(currentAST, tmp16_AST);
				match(_t,STAR);
				_t = _t.getNextSibling();
			}
			else {
				if ( _cnt23>=1 ) { break _loop23; } else {throw new NoViableAltException(_t);}
			}
			
			_cnt23++;
		} while (true);
		}
		currentAST = __currentAST21;
		_t = __t21;
		_t = _t.getNextSibling();
		pointerGroup_AST = (Block)currentAST.root;
		returnAST = pointerGroup_AST;
		_retTree = _t;
	}
	
	public final Type  structSpecifier(AST _t,
		ValidityChecker vc
	) throws RecognitionException {
		Type t;
		
		Block structSpecifier_AST_in = (_t == ASTNULL) ? null : (Block)_t;
		returnAST = null;
		ASTPair currentAST = new ASTPair();
		Block structSpecifier_AST = null;
		
		{
		Block tmp17_AST = null;
		Block tmp17_AST_in = null;
		tmp17_AST = (Block)astFactory.create((Block)_t);
		tmp17_AST_in = (Block)_t;
		astFactory.addASTChild(currentAST, tmp17_AST);
		match(_t,LITERAL_struct);
		_t = _t.getNextSibling();
		}
		
		// t = vc.intType ();
		t = vc.createType ("STRUCT_DUMMY");
		
		structSpecifier_AST = (Block)currentAST.root;
		returnAST = structSpecifier_AST;
		_retTree = _t;
		return t;
	}
	
	public final String  initDecl(AST _t) throws RecognitionException {
		String declName;
		
		Block initDecl_AST_in = (_t == ASTNULL) ? null : (Block)_t;
		returnAST = null;
		ASTPair currentAST = new ASTPair();
		Block initDecl_AST = null;
		
		AST __t25 = _t;
		Block tmp18_AST = null;
		Block tmp18_AST_in = null;
		tmp18_AST = (Block)astFactory.create((Block)_t);
		tmp18_AST_in = (Block)_t;
		astFactory.addASTChild(currentAST, tmp18_AST);
		ASTPair __currentAST25 = currentAST.copy();
		currentAST.root = currentAST.child;
		currentAST.child = null;
		match(_t,NInitDecl);
		_t = _t.getFirstChild();
		declName=declarator(_t);
		_t = _retTree;
		astFactory.addASTChild(currentAST, returnAST);
		currentAST = __currentAST25;
		_t = __t25;
		_t = _t.getNextSibling();
		initDecl_AST = (Block)currentAST.root;
		returnAST = initDecl_AST;
		_retTree = _t;
		return declName;
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
	
	}
	
