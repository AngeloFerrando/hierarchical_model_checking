// $ANTLR 2.7.4: "CILProgramBlocker.g" -> "CILProgramBlocker.java"$
 package edu.toronto.cs.cparser; 
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

import java.io.*;

import antlr.CommonAST;
import antlr.DumpASTVisitor;

import edu.toronto.cs.cparser.block.*;


public class CILProgramBlocker extends antlr.TreeParser       implements CILProgramBlockerTokenTypes
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


public CILProgramBlocker() {
	tokenNames = _tokenNames;
}

	public final void translationUnit(AST _t) throws RecognitionException {
		
		Block translationUnit_AST_in = (_t == ASTNULL) ? null : (Block)_t;
		returnAST = null;
		ASTPair currentAST = new ASTPair();
		Block translationUnit_AST = null;
		Block list_AST = null;
		Block list = null;
		
		AST __t2 = _t;
		Block tmp1_AST = null;
		Block tmp1_AST_in = null;
		tmp1_AST = (Block)astFactory.create((Block)_t);
		tmp1_AST_in = (Block)_t;
		astFactory.addASTChild(currentAST, tmp1_AST);
		ASTPair __currentAST2 = currentAST.copy();
		currentAST.root = currentAST.child;
		currentAST.child = null;
		match(_t,NProgram);
		_t = _t.getFirstChild();
		list = _t==ASTNULL ? null : (Block)_t;
		externalList(_t);
		_t = _retTree;
		list_AST = (Block)returnAST;
		astFactory.addASTChild(currentAST, returnAST);
		currentAST = __currentAST2;
		_t = __t2;
		_t = _t.getNextSibling();
		if ( inputState.guessing==0 ) {
			translationUnit_AST = (Block)currentAST.root;
			
			Block b = new Block (BlockType.PROGRAM);
			translationUnit_AST = (Block)astFactory.make( (new ASTArray(2)).add(b).add(list_AST));
			
			currentAST.root = translationUnit_AST;
			currentAST.child = translationUnit_AST!=null &&translationUnit_AST.getFirstChild()!=null ?
				translationUnit_AST.getFirstChild() : translationUnit_AST;
			currentAST.advanceChildToEnd();
		}
		translationUnit_AST = (Block)currentAST.root;
		returnAST = translationUnit_AST;
		_retTree = _t;
	}
	
	public final void externalList(AST _t) throws RecognitionException {
		
		Block externalList_AST_in = (_t == ASTNULL) ? null : (Block)_t;
		returnAST = null;
		ASTPair currentAST = new ASTPair();
		Block externalList_AST = null;
		
		{
		int _cnt5=0;
		_loop5:
		do {
			if (_t==null) _t=ASTNULL;
			if ((_t.getType()==NDeclaration||_t.getType()==NFunctionDef)) {
				externalDef(_t);
				_t = _retTree;
				astFactory.addASTChild(currentAST, returnAST);
			}
			else {
				if ( _cnt5>=1 ) { break _loop5; } else {throw new NoViableAltException(_t);}
			}
			
			_cnt5++;
		} while (true);
		}
		externalList_AST = (Block)currentAST.root;
		returnAST = externalList_AST;
		_retTree = _t;
	}
	
	public final void externalDef(AST _t) throws RecognitionException {
		
		Block externalDef_AST_in = (_t == ASTNULL) ? null : (Block)_t;
		returnAST = null;
		ASTPair currentAST = new ASTPair();
		Block externalDef_AST = null;
		
		if (_t==null) _t=ASTNULL;
		switch ( _t.getType()) {
		case NDeclaration:
		{
			declaration(_t);
			_t = _retTree;
			astFactory.addASTChild(currentAST, returnAST);
			externalDef_AST = (Block)currentAST.root;
			break;
		}
		case NFunctionDef:
		{
			functionDef(_t);
			_t = _retTree;
			astFactory.addASTChild(currentAST, returnAST);
			externalDef_AST = (Block)currentAST.root;
			break;
		}
		default:
		{
			throw new NoViableAltException(_t);
		}
		}
		returnAST = externalDef_AST;
		_retTree = _t;
	}
	
	public final void declaration(AST _t) throws RecognitionException {
		
		Block declaration_AST_in = (_t == ASTNULL) ? null : (Block)_t;
		returnAST = null;
		ASTPair currentAST = new ASTPair();
		Block declaration_AST = null;
		Block specs_AST = null;
		Block specs = null;
		Block d_AST = null;
		Block d = null;
		Block i_AST = null;
		Block i = null;
		
		AST __t8 = _t;
		Block tmp2_AST = null;
		Block tmp2_AST_in = null;
		tmp2_AST = (Block)astFactory.create((Block)_t);
		tmp2_AST_in = (Block)_t;
		ASTPair __currentAST8 = currentAST.copy();
		currentAST.root = currentAST.child;
		currentAST.child = null;
		match(_t,NDeclaration);
		_t = _t.getFirstChild();
		specs = _t==ASTNULL ? null : (Block)_t;
		declSpecifiers(_t);
		_t = _retTree;
		specs_AST = (Block)returnAST;
		{
		if (_t==null) _t=ASTNULL;
		switch ( _t.getType()) {
		case NDeclarator:
		{
			d = _t==ASTNULL ? null : (Block)_t;
			declarator(_t);
			_t = _retTree;
			d_AST = (Block)returnAST;
			break;
		}
		case 3:
		case NInitializer:
		case NLCurlyInitializer:
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
		case NLCurlyInitializer:
		{
			i = _t==ASTNULL ? null : (Block)_t;
			initializer(_t);
			_t = _retTree;
			i_AST = (Block)returnAST;
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
		currentAST = __currentAST8;
		_t = __t8;
		_t = _t.getNextSibling();
		if ( inputState.guessing==0 ) {
			declaration_AST = (Block)currentAST.root;
			
			Block b = new Block (BlockType.DECLARATION);
			declaration_AST = (Block)astFactory.make( (new ASTArray(4)).add(b).add(specs_AST).add(d_AST).add(i_AST));
			
			currentAST.root = declaration_AST;
			currentAST.child = declaration_AST!=null &&declaration_AST.getFirstChild()!=null ?
				declaration_AST.getFirstChild() : declaration_AST;
			currentAST.advanceChildToEnd();
		}
		returnAST = declaration_AST;
		_retTree = _t;
	}
	
	public final void functionDef(AST _t) throws RecognitionException {
		
		Block functionDef_AST_in = (_t == ASTNULL) ? null : (Block)_t;
		returnAST = null;
		ASTPair currentAST = new ASTPair();
		Block functionDef_AST = null;
		Block r = null;
		Block r_AST = null;
		Block funcSpecs_AST = null;
		Block funcSpecs = null;
		Block funcSig_AST = null;
		Block funcSig = null;
		Block funcBody_AST = null;
		Block funcBody = null;
		
		AST __t117 = _t;
		r = _t==ASTNULL ? null :(Block)_t;
		Block r_AST_in = null;
		r_AST = (Block)astFactory.create(r);
		ASTPair __currentAST117 = currentAST.copy();
		currentAST.root = currentAST.child;
		currentAST.child = null;
		match(_t,NFunctionDef);
		_t = _t.getFirstChild();
		funcSpecs = _t==ASTNULL ? null : (Block)_t;
		functionDeclSpecifiers(_t);
		_t = _retTree;
		funcSpecs_AST = (Block)returnAST;
		funcSig = _t==ASTNULL ? null : (Block)_t;
		declarator(_t);
		_t = _retTree;
		funcSig_AST = (Block)returnAST;
		funcBody = _t==ASTNULL ? null : (Block)_t;
		scope(_t);
		_t = _retTree;
		funcBody_AST = (Block)returnAST;
		currentAST = __currentAST117;
		_t = __t117;
		_t = _t.getNextSibling();
		if ( inputState.guessing==0 ) {
			functionDef_AST = (Block)currentAST.root;
			
			Block b = new Block (BlockType.FUNCTION_DEFINITION);
			b.setNumCallSites (r_AST.getNumCallSites ());
			functionDef_AST = (Block)astFactory.make( (new ASTArray(4)).add(b).add(funcSpecs_AST).add(funcSig_AST).add(funcBody_AST));
			
			currentAST.root = functionDef_AST;
			currentAST.child = functionDef_AST!=null &&functionDef_AST.getFirstChild()!=null ?
				functionDef_AST.getFirstChild() : functionDef_AST;
			currentAST.advanceChildToEnd();
		}
		returnAST = functionDef_AST;
		_retTree = _t;
	}
	
	public final void declSpecifiers(AST _t) throws RecognitionException {
		
		Block declSpecifiers_AST_in = (_t == ASTNULL) ? null : (Block)_t;
		returnAST = null;
		ASTPair currentAST = new ASTPair();
		Block declSpecifiers_AST = null;
		
		AST __t12 = _t;
		Block tmp3_AST = null;
		Block tmp3_AST_in = null;
		tmp3_AST = (Block)astFactory.create((Block)_t);
		tmp3_AST_in = (Block)_t;
		astFactory.addASTChild(currentAST, tmp3_AST);
		ASTPair __currentAST12 = currentAST.copy();
		currentAST.root = currentAST.child;
		currentAST.child = null;
		match(_t,NDeclSpecifiers);
		_t = _t.getFirstChild();
		{
		int _cnt14=0;
		_loop14:
		do {
			if (_t==null) _t=ASTNULL;
			switch ( _t.getType()) {
			case LITERAL_typedef:
			case LITERAL_auto:
			case LITERAL_register:
			case LITERAL_extern:
			case LITERAL_static:
			case LITERAL___inline:
			{
				storageClassSpecifier(_t);
				_t = _retTree;
				astFactory.addASTChild(currentAST, returnAST);
				break;
			}
			case LITERAL_const:
			case LITERAL_volatile:
			{
				typeQualifier(_t);
				_t = _retTree;
				astFactory.addASTChild(currentAST, returnAST);
				break;
			}
			case LITERAL_struct:
			case LITERAL_union:
			case LITERAL_enum:
			case LITERAL_void:
			case LITERAL_char:
			case LITERAL_short:
			case LITERAL_int:
			case LITERAL_long:
			case LITERAL_float:
			case LITERAL_double:
			case LITERAL_signed:
			case LITERAL_unsigned:
			case NTypedefName:
			case LITERAL_typeof:
			case LITERAL___complex:
			{
				typeSpecifier(_t);
				_t = _retTree;
				astFactory.addASTChild(currentAST, returnAST);
				break;
			}
			case NPointerGroup:
			{
				pointerGroup(_t);
				_t = _retTree;
				astFactory.addASTChild(currentAST, returnAST);
				break;
			}
			default:
			{
				if ( _cnt14>=1 ) { break _loop14; } else {throw new NoViableAltException(_t);}
			}
			}
			_cnt14++;
		} while (true);
		}
		currentAST = __currentAST12;
		_t = __t12;
		_t = _t.getNextSibling();
		declSpecifiers_AST = (Block)currentAST.root;
		returnAST = declSpecifiers_AST;
		_retTree = _t;
	}
	
	public final void declarator(AST _t) throws RecognitionException {
		
		Block declarator_AST_in = (_t == ASTNULL) ? null : (Block)_t;
		returnAST = null;
		ASTPair currentAST = new ASTPair();
		Block declarator_AST = null;
		Block id = null;
		Block id_AST = null;
		
		AST __t99 = _t;
		Block tmp4_AST = null;
		Block tmp4_AST_in = null;
		tmp4_AST = (Block)astFactory.create((Block)_t);
		tmp4_AST_in = (Block)_t;
		astFactory.addASTChild(currentAST, tmp4_AST);
		ASTPair __currentAST99 = currentAST.copy();
		currentAST.root = currentAST.child;
		currentAST.child = null;
		match(_t,NDeclarator);
		_t = _t.getFirstChild();
		{
		if (_t==null) _t=ASTNULL;
		switch ( _t.getType()) {
		case NPointerGroup:
		{
			pointerGroup(_t);
			_t = _retTree;
			astFactory.addASTChild(currentAST, returnAST);
			break;
		}
		case ID:
		case LPAREN:
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
		case ID:
		{
			id = (Block)_t;
			Block id_AST_in = null;
			id_AST = (Block)astFactory.create(id);
			astFactory.addASTChild(currentAST, id_AST);
			match(_t,ID);
			_t = _t.getNextSibling();
			break;
		}
		case LPAREN:
		{
			Block tmp5_AST = null;
			Block tmp5_AST_in = null;
			tmp5_AST = (Block)astFactory.create((Block)_t);
			tmp5_AST_in = (Block)_t;
			astFactory.addASTChild(currentAST, tmp5_AST);
			match(_t,LPAREN);
			_t = _t.getNextSibling();
			declarator(_t);
			_t = _retTree;
			astFactory.addASTChild(currentAST, returnAST);
			Block tmp6_AST = null;
			Block tmp6_AST_in = null;
			tmp6_AST = (Block)astFactory.create((Block)_t);
			tmp6_AST_in = (Block)_t;
			astFactory.addASTChild(currentAST, tmp6_AST);
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
		{
		_loop107:
		do {
			if (_t==null) _t=ASTNULL;
			switch ( _t.getType()) {
			case NParameterTypeList:
			{
				AST __t103 = _t;
				Block tmp7_AST = null;
				Block tmp7_AST_in = null;
				tmp7_AST = (Block)astFactory.create((Block)_t);
				tmp7_AST_in = (Block)_t;
				astFactory.addASTChild(currentAST, tmp7_AST);
				ASTPair __currentAST103 = currentAST.copy();
				currentAST.root = currentAST.child;
				currentAST.child = null;
				match(_t,NParameterTypeList);
				_t = _t.getFirstChild();
				{
				if (_t==null) _t=ASTNULL;
				switch ( _t.getType()) {
				case NParameterDeclaration:
				{
					parameterTypeList(_t);
					_t = _retTree;
					astFactory.addASTChild(currentAST, returnAST);
					break;
				}
				case 3:
				case ID:
				{
					{
					if (_t==null) _t=ASTNULL;
					switch ( _t.getType()) {
					case ID:
					{
						idList(_t);
						_t = _retTree;
						astFactory.addASTChild(currentAST, returnAST);
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
					break;
				}
				default:
				{
					throw new NoViableAltException(_t);
				}
				}
				}
				currentAST = __currentAST103;
				_t = __t103;
				_t = _t.getNextSibling();
				break;
			}
			case LBRACKET:
			{
				Block tmp8_AST = null;
				Block tmp8_AST_in = null;
				tmp8_AST = (Block)astFactory.create((Block)_t);
				tmp8_AST_in = (Block)_t;
				astFactory.addASTChild(currentAST, tmp8_AST);
				match(_t,LBRACKET);
				_t = _t.getNextSibling();
				{
				if (_t==null) _t=ASTNULL;
				switch ( _t.getType()) {
				case ID:
				case STAR:
				case PTR:
				case DOT:
				case LOR:
				case LAND:
				case BOR:
				case BXOR:
				case BAND:
				case EQUAL:
				case NOT_EQUAL:
				case LT:
				case LTE:
				case GT:
				case GTE:
				case LSHIFT:
				case RSHIFT:
				case PLUS:
				case MINUS:
				case DIV:
				case MOD:
				case LITERAL_sizeof:
				case CharLiteral:
				case IntOctalConst:
				case LongOctalConst:
				case UnsignedOctalConst:
				case IntIntConst:
				case LongIntConst:
				case UnsignedIntConst:
				case IntHexConst:
				case LongHexConst:
				case UnsignedHexConst:
				case FloatDoubleConst:
				case DoubleDoubleConst:
				case LongDoubleConst:
				case NCast:
				case NUnaryExpr:
				case NPostfixExpr:
				case NStringSeq:
				case NPureExpressionGroup:
				case NArrayElement:
				case LITERAL___alignof:
				{
					pureExpr(_t);
					_t = _retTree;
					astFactory.addASTChild(currentAST, returnAST);
					break;
				}
				case RBRACKET:
				{
					break;
				}
				default:
				{
					throw new NoViableAltException(_t);
				}
				}
				}
				Block tmp9_AST = null;
				Block tmp9_AST_in = null;
				tmp9_AST = (Block)astFactory.create((Block)_t);
				tmp9_AST_in = (Block)_t;
				astFactory.addASTChild(currentAST, tmp9_AST);
				match(_t,RBRACKET);
				_t = _t.getNextSibling();
				break;
			}
			default:
			{
				break _loop107;
			}
			}
		} while (true);
		}
		currentAST = __currentAST99;
		_t = __t99;
		_t = _t.getNextSibling();
		declarator_AST = (Block)currentAST.root;
		returnAST = declarator_AST;
		_retTree = _t;
	}
	
	public final void initializer(AST _t) throws RecognitionException {
		
		Block initializer_AST_in = (_t == ASTNULL) ? null : (Block)_t;
		returnAST = null;
		ASTPair currentAST = new ASTPair();
		Block initializer_AST = null;
		
		if (_t==null) _t=ASTNULL;
		switch ( _t.getType()) {
		case NInitializer:
		{
			AST __t86 = _t;
			Block tmp10_AST = null;
			Block tmp10_AST_in = null;
			tmp10_AST = (Block)astFactory.create((Block)_t);
			tmp10_AST_in = (Block)_t;
			astFactory.addASTChild(currentAST, tmp10_AST);
			ASTPair __currentAST86 = currentAST.copy();
			currentAST.root = currentAST.child;
			currentAST.child = null;
			match(_t,NInitializer);
			_t = _t.getFirstChild();
			{
			if (_t==null) _t=ASTNULL;
			switch ( _t.getType()) {
			case NInitializerElementLabel:
			{
				initializerElementLabel(_t);
				_t = _retTree;
				astFactory.addASTChild(currentAST, returnAST);
				break;
			}
			case ID:
			case STAR:
			case PTR:
			case DOT:
			case LOR:
			case LAND:
			case BOR:
			case BXOR:
			case BAND:
			case EQUAL:
			case NOT_EQUAL:
			case LT:
			case LTE:
			case GT:
			case GTE:
			case LSHIFT:
			case RSHIFT:
			case PLUS:
			case MINUS:
			case DIV:
			case MOD:
			case LITERAL_sizeof:
			case CharLiteral:
			case IntOctalConst:
			case LongOctalConst:
			case UnsignedOctalConst:
			case IntIntConst:
			case LongIntConst:
			case UnsignedIntConst:
			case IntHexConst:
			case LongHexConst:
			case UnsignedHexConst:
			case FloatDoubleConst:
			case DoubleDoubleConst:
			case LongDoubleConst:
			case NCast:
			case NUnaryExpr:
			case NPostfixExpr:
			case NStringSeq:
			case NPureExpressionGroup:
			case NArrayElement:
			case LITERAL___alignof:
			{
				break;
			}
			default:
			{
				throw new NoViableAltException(_t);
			}
			}
			}
			pureExpr(_t);
			_t = _retTree;
			astFactory.addASTChild(currentAST, returnAST);
			currentAST = __currentAST86;
			_t = __t86;
			_t = _t.getNextSibling();
			initializer_AST = (Block)currentAST.root;
			break;
		}
		case NLCurlyInitializer:
		{
			lcurlyInitializer(_t);
			_t = _retTree;
			astFactory.addASTChild(currentAST, returnAST);
			initializer_AST = (Block)currentAST.root;
			break;
		}
		default:
		{
			throw new NoViableAltException(_t);
		}
		}
		returnAST = initializer_AST;
		_retTree = _t;
	}
	
	public final void storageClassSpecifier(AST _t) throws RecognitionException {
		
		Block storageClassSpecifier_AST_in = (_t == ASTNULL) ? null : (Block)_t;
		returnAST = null;
		ASTPair currentAST = new ASTPair();
		Block storageClassSpecifier_AST = null;
		
		if (_t==null) _t=ASTNULL;
		switch ( _t.getType()) {
		case LITERAL_auto:
		{
			Block tmp11_AST = null;
			Block tmp11_AST_in = null;
			tmp11_AST = (Block)astFactory.create((Block)_t);
			tmp11_AST_in = (Block)_t;
			astFactory.addASTChild(currentAST, tmp11_AST);
			match(_t,LITERAL_auto);
			_t = _t.getNextSibling();
			storageClassSpecifier_AST = (Block)currentAST.root;
			break;
		}
		case LITERAL_register:
		{
			Block tmp12_AST = null;
			Block tmp12_AST_in = null;
			tmp12_AST = (Block)astFactory.create((Block)_t);
			tmp12_AST_in = (Block)_t;
			astFactory.addASTChild(currentAST, tmp12_AST);
			match(_t,LITERAL_register);
			_t = _t.getNextSibling();
			storageClassSpecifier_AST = (Block)currentAST.root;
			break;
		}
		case LITERAL_typedef:
		{
			Block tmp13_AST = null;
			Block tmp13_AST_in = null;
			tmp13_AST = (Block)astFactory.create((Block)_t);
			tmp13_AST_in = (Block)_t;
			astFactory.addASTChild(currentAST, tmp13_AST);
			match(_t,LITERAL_typedef);
			_t = _t.getNextSibling();
			storageClassSpecifier_AST = (Block)currentAST.root;
			break;
		}
		case LITERAL_extern:
		case LITERAL_static:
		case LITERAL___inline:
		{
			functionStorageClassSpecifier(_t);
			_t = _retTree;
			astFactory.addASTChild(currentAST, returnAST);
			storageClassSpecifier_AST = (Block)currentAST.root;
			break;
		}
		default:
		{
			throw new NoViableAltException(_t);
		}
		}
		returnAST = storageClassSpecifier_AST;
		_retTree = _t;
	}
	
	public final void typeQualifier(AST _t) throws RecognitionException {
		
		Block typeQualifier_AST_in = (_t == ASTNULL) ? null : (Block)_t;
		returnAST = null;
		ASTPair currentAST = new ASTPair();
		Block typeQualifier_AST = null;
		
		if (_t==null) _t=ASTNULL;
		switch ( _t.getType()) {
		case LITERAL_const:
		{
			Block tmp14_AST = null;
			Block tmp14_AST_in = null;
			tmp14_AST = (Block)astFactory.create((Block)_t);
			tmp14_AST_in = (Block)_t;
			astFactory.addASTChild(currentAST, tmp14_AST);
			match(_t,LITERAL_const);
			_t = _t.getNextSibling();
			typeQualifier_AST = (Block)currentAST.root;
			break;
		}
		case LITERAL_volatile:
		{
			Block tmp15_AST = null;
			Block tmp15_AST_in = null;
			tmp15_AST = (Block)astFactory.create((Block)_t);
			tmp15_AST_in = (Block)_t;
			astFactory.addASTChild(currentAST, tmp15_AST);
			match(_t,LITERAL_volatile);
			_t = _t.getNextSibling();
			typeQualifier_AST = (Block)currentAST.root;
			break;
		}
		default:
		{
			throw new NoViableAltException(_t);
		}
		}
		returnAST = typeQualifier_AST;
		_retTree = _t;
	}
	
	public final void typeSpecifier(AST _t) throws RecognitionException {
		
		Block typeSpecifier_AST_in = (_t == ASTNULL) ? null : (Block)_t;
		returnAST = null;
		ASTPair currentAST = new ASTPair();
		Block typeSpecifier_AST = null;
		
		if (_t==null) _t=ASTNULL;
		switch ( _t.getType()) {
		case LITERAL_void:
		{
			Block tmp16_AST = null;
			Block tmp16_AST_in = null;
			tmp16_AST = (Block)astFactory.create((Block)_t);
			tmp16_AST_in = (Block)_t;
			astFactory.addASTChild(currentAST, tmp16_AST);
			match(_t,LITERAL_void);
			_t = _t.getNextSibling();
			typeSpecifier_AST = (Block)currentAST.root;
			break;
		}
		case LITERAL_char:
		{
			Block tmp17_AST = null;
			Block tmp17_AST_in = null;
			tmp17_AST = (Block)astFactory.create((Block)_t);
			tmp17_AST_in = (Block)_t;
			astFactory.addASTChild(currentAST, tmp17_AST);
			match(_t,LITERAL_char);
			_t = _t.getNextSibling();
			typeSpecifier_AST = (Block)currentAST.root;
			break;
		}
		case LITERAL_short:
		{
			Block tmp18_AST = null;
			Block tmp18_AST_in = null;
			tmp18_AST = (Block)astFactory.create((Block)_t);
			tmp18_AST_in = (Block)_t;
			astFactory.addASTChild(currentAST, tmp18_AST);
			match(_t,LITERAL_short);
			_t = _t.getNextSibling();
			typeSpecifier_AST = (Block)currentAST.root;
			break;
		}
		case LITERAL_int:
		{
			Block tmp19_AST = null;
			Block tmp19_AST_in = null;
			tmp19_AST = (Block)astFactory.create((Block)_t);
			tmp19_AST_in = (Block)_t;
			astFactory.addASTChild(currentAST, tmp19_AST);
			match(_t,LITERAL_int);
			_t = _t.getNextSibling();
			typeSpecifier_AST = (Block)currentAST.root;
			break;
		}
		case LITERAL_long:
		{
			Block tmp20_AST = null;
			Block tmp20_AST_in = null;
			tmp20_AST = (Block)astFactory.create((Block)_t);
			tmp20_AST_in = (Block)_t;
			astFactory.addASTChild(currentAST, tmp20_AST);
			match(_t,LITERAL_long);
			_t = _t.getNextSibling();
			typeSpecifier_AST = (Block)currentAST.root;
			break;
		}
		case LITERAL_float:
		{
			Block tmp21_AST = null;
			Block tmp21_AST_in = null;
			tmp21_AST = (Block)astFactory.create((Block)_t);
			tmp21_AST_in = (Block)_t;
			astFactory.addASTChild(currentAST, tmp21_AST);
			match(_t,LITERAL_float);
			_t = _t.getNextSibling();
			typeSpecifier_AST = (Block)currentAST.root;
			break;
		}
		case LITERAL_double:
		{
			Block tmp22_AST = null;
			Block tmp22_AST_in = null;
			tmp22_AST = (Block)astFactory.create((Block)_t);
			tmp22_AST_in = (Block)_t;
			astFactory.addASTChild(currentAST, tmp22_AST);
			match(_t,LITERAL_double);
			_t = _t.getNextSibling();
			typeSpecifier_AST = (Block)currentAST.root;
			break;
		}
		case LITERAL_signed:
		{
			Block tmp23_AST = null;
			Block tmp23_AST_in = null;
			tmp23_AST = (Block)astFactory.create((Block)_t);
			tmp23_AST_in = (Block)_t;
			astFactory.addASTChild(currentAST, tmp23_AST);
			match(_t,LITERAL_signed);
			_t = _t.getNextSibling();
			typeSpecifier_AST = (Block)currentAST.root;
			break;
		}
		case LITERAL_unsigned:
		{
			Block tmp24_AST = null;
			Block tmp24_AST_in = null;
			tmp24_AST = (Block)astFactory.create((Block)_t);
			tmp24_AST_in = (Block)_t;
			astFactory.addASTChild(currentAST, tmp24_AST);
			match(_t,LITERAL_unsigned);
			_t = _t.getNextSibling();
			typeSpecifier_AST = (Block)currentAST.root;
			break;
		}
		case LITERAL_struct:
		{
			structSpecifier(_t);
			_t = _retTree;
			astFactory.addASTChild(currentAST, returnAST);
			{
			_loop20:
			do {
				if (_t==null) _t=ASTNULL;
				if ((_t.getType()==LITERAL___attribute__||_t.getType()==NAsmAttribute)) {
					attributeDecl(_t);
					_t = _retTree;
					astFactory.addASTChild(currentAST, returnAST);
				}
				else {
					break _loop20;
				}
				
			} while (true);
			}
			typeSpecifier_AST = (Block)currentAST.root;
			break;
		}
		case LITERAL_union:
		{
			unionSpecifier(_t);
			_t = _retTree;
			astFactory.addASTChild(currentAST, returnAST);
			{
			_loop22:
			do {
				if (_t==null) _t=ASTNULL;
				if ((_t.getType()==LITERAL___attribute__||_t.getType()==NAsmAttribute)) {
					attributeDecl(_t);
					_t = _retTree;
					astFactory.addASTChild(currentAST, returnAST);
				}
				else {
					break _loop22;
				}
				
			} while (true);
			}
			typeSpecifier_AST = (Block)currentAST.root;
			break;
		}
		case LITERAL_enum:
		{
			enumSpecifier(_t);
			_t = _retTree;
			astFactory.addASTChild(currentAST, returnAST);
			typeSpecifier_AST = (Block)currentAST.root;
			break;
		}
		case NTypedefName:
		{
			typedefName(_t);
			_t = _retTree;
			astFactory.addASTChild(currentAST, returnAST);
			typeSpecifier_AST = (Block)currentAST.root;
			break;
		}
		case LITERAL_typeof:
		{
			AST __t23 = _t;
			Block tmp25_AST = null;
			Block tmp25_AST_in = null;
			tmp25_AST = (Block)astFactory.create((Block)_t);
			tmp25_AST_in = (Block)_t;
			astFactory.addASTChild(currentAST, tmp25_AST);
			ASTPair __currentAST23 = currentAST.copy();
			currentAST.root = currentAST.child;
			currentAST.child = null;
			match(_t,LITERAL_typeof);
			_t = _t.getFirstChild();
			Block tmp26_AST = null;
			Block tmp26_AST_in = null;
			tmp26_AST = (Block)astFactory.create((Block)_t);
			tmp26_AST_in = (Block)_t;
			astFactory.addASTChild(currentAST, tmp26_AST);
			match(_t,LPAREN);
			_t = _t.getNextSibling();
			{
			if (_t==null) _t=ASTNULL;
			switch ( _t.getType()) {
			case LITERAL_struct:
			case LITERAL_union:
			case LITERAL_enum:
			case LITERAL_const:
			case LITERAL_volatile:
			case LITERAL_void:
			case LITERAL_char:
			case LITERAL_short:
			case LITERAL_int:
			case LITERAL_long:
			case LITERAL_float:
			case LITERAL_double:
			case LITERAL_signed:
			case LITERAL_unsigned:
			case NTypedefName:
			case NPointerGroup:
			case LITERAL_typeof:
			case LITERAL___complex:
			{
				typeName(_t);
				_t = _retTree;
				astFactory.addASTChild(currentAST, returnAST);
				break;
			}
			case ID:
			case STAR:
			case PTR:
			case DOT:
			case LOR:
			case LAND:
			case BOR:
			case BXOR:
			case BAND:
			case EQUAL:
			case NOT_EQUAL:
			case LT:
			case LTE:
			case GT:
			case GTE:
			case LSHIFT:
			case RSHIFT:
			case PLUS:
			case MINUS:
			case DIV:
			case MOD:
			case LITERAL_sizeof:
			case CharLiteral:
			case IntOctalConst:
			case LongOctalConst:
			case UnsignedOctalConst:
			case IntIntConst:
			case LongIntConst:
			case UnsignedIntConst:
			case IntHexConst:
			case LongHexConst:
			case UnsignedHexConst:
			case FloatDoubleConst:
			case DoubleDoubleConst:
			case LongDoubleConst:
			case NCast:
			case NUnaryExpr:
			case NPostfixExpr:
			case NStringSeq:
			case NPureExpressionGroup:
			case NArrayElement:
			case LITERAL___alignof:
			{
				pureExpr(_t);
				_t = _retTree;
				astFactory.addASTChild(currentAST, returnAST);
				break;
			}
			default:
			{
				throw new NoViableAltException(_t);
			}
			}
			}
			Block tmp27_AST = null;
			Block tmp27_AST_in = null;
			tmp27_AST = (Block)astFactory.create((Block)_t);
			tmp27_AST_in = (Block)_t;
			astFactory.addASTChild(currentAST, tmp27_AST);
			match(_t,RPAREN);
			_t = _t.getNextSibling();
			currentAST = __currentAST23;
			_t = __t23;
			_t = _t.getNextSibling();
			typeSpecifier_AST = (Block)currentAST.root;
			break;
		}
		case LITERAL___complex:
		{
			Block tmp28_AST = null;
			Block tmp28_AST_in = null;
			tmp28_AST = (Block)astFactory.create((Block)_t);
			tmp28_AST_in = (Block)_t;
			astFactory.addASTChild(currentAST, tmp28_AST);
			match(_t,LITERAL___complex);
			_t = _t.getNextSibling();
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
	}
	
	public final void pointerGroup(AST _t) throws RecognitionException {
		
		Block pointerGroup_AST_in = (_t == ASTNULL) ? null : (Block)_t;
		returnAST = null;
		ASTPair currentAST = new ASTPair();
		Block pointerGroup_AST = null;
		
		AST __t77 = _t;
		Block tmp29_AST = null;
		Block tmp29_AST_in = null;
		tmp29_AST = (Block)astFactory.create((Block)_t);
		tmp29_AST_in = (Block)_t;
		astFactory.addASTChild(currentAST, tmp29_AST);
		ASTPair __currentAST77 = currentAST.copy();
		currentAST.root = currentAST.child;
		currentAST.child = null;
		match(_t,NPointerGroup);
		_t = _t.getFirstChild();
		{
		int _cnt81=0;
		_loop81:
		do {
			if (_t==null) _t=ASTNULL;
			if ((_t.getType()==STAR)) {
				Block tmp30_AST = null;
				Block tmp30_AST_in = null;
				tmp30_AST = (Block)astFactory.create((Block)_t);
				tmp30_AST_in = (Block)_t;
				astFactory.addASTChild(currentAST, tmp30_AST);
				match(_t,STAR);
				_t = _t.getNextSibling();
				{
				_loop80:
				do {
					if (_t==null) _t=ASTNULL;
					if ((_t.getType()==LITERAL_const||_t.getType()==LITERAL_volatile)) {
						typeQualifier(_t);
						_t = _retTree;
						astFactory.addASTChild(currentAST, returnAST);
					}
					else {
						break _loop80;
					}
					
				} while (true);
				}
			}
			else {
				if ( _cnt81>=1 ) { break _loop81; } else {throw new NoViableAltException(_t);}
			}
			
			_cnt81++;
		} while (true);
		}
		currentAST = __currentAST77;
		_t = __t77;
		_t = _t.getNextSibling();
		pointerGroup_AST = (Block)currentAST.root;
		returnAST = pointerGroup_AST;
		_retTree = _t;
	}
	
	public final void functionStorageClassSpecifier(AST _t) throws RecognitionException {
		
		Block functionStorageClassSpecifier_AST_in = (_t == ASTNULL) ? null : (Block)_t;
		returnAST = null;
		ASTPair currentAST = new ASTPair();
		Block functionStorageClassSpecifier_AST = null;
		
		if (_t==null) _t=ASTNULL;
		switch ( _t.getType()) {
		case LITERAL_extern:
		{
			Block tmp31_AST = null;
			Block tmp31_AST_in = null;
			tmp31_AST = (Block)astFactory.create((Block)_t);
			tmp31_AST_in = (Block)_t;
			astFactory.addASTChild(currentAST, tmp31_AST);
			match(_t,LITERAL_extern);
			_t = _t.getNextSibling();
			functionStorageClassSpecifier_AST = (Block)currentAST.root;
			break;
		}
		case LITERAL_static:
		{
			Block tmp32_AST = null;
			Block tmp32_AST_in = null;
			tmp32_AST = (Block)astFactory.create((Block)_t);
			tmp32_AST_in = (Block)_t;
			astFactory.addASTChild(currentAST, tmp32_AST);
			match(_t,LITERAL_static);
			_t = _t.getNextSibling();
			functionStorageClassSpecifier_AST = (Block)currentAST.root;
			break;
		}
		case LITERAL___inline:
		{
			Block tmp33_AST = null;
			Block tmp33_AST_in = null;
			tmp33_AST = (Block)astFactory.create((Block)_t);
			tmp33_AST_in = (Block)_t;
			astFactory.addASTChild(currentAST, tmp33_AST);
			match(_t,LITERAL___inline);
			_t = _t.getNextSibling();
			functionStorageClassSpecifier_AST = (Block)currentAST.root;
			break;
		}
		default:
		{
			throw new NoViableAltException(_t);
		}
		}
		returnAST = functionStorageClassSpecifier_AST;
		_retTree = _t;
	}
	
	public final void structSpecifier(AST _t) throws RecognitionException {
		
		Block structSpecifier_AST_in = (_t == ASTNULL) ? null : (Block)_t;
		returnAST = null;
		ASTPair currentAST = new ASTPair();
		Block structSpecifier_AST = null;
		
		AST __t30 = _t;
		Block tmp34_AST = null;
		Block tmp34_AST_in = null;
		tmp34_AST = (Block)astFactory.create((Block)_t);
		tmp34_AST_in = (Block)_t;
		astFactory.addASTChild(currentAST, tmp34_AST);
		ASTPair __currentAST30 = currentAST.copy();
		currentAST.root = currentAST.child;
		currentAST.child = null;
		match(_t,LITERAL_struct);
		_t = _t.getFirstChild();
		Block tmp35_AST = null;
		Block tmp35_AST_in = null;
		tmp35_AST = (Block)astFactory.create((Block)_t);
		tmp35_AST_in = (Block)_t;
		astFactory.addASTChild(currentAST, tmp35_AST);
		match(_t,ID);
		_t = _t.getNextSibling();
		{
		if (_t==null) _t=ASTNULL;
		switch ( _t.getType()) {
		case NStructFields:
		{
			structDeclarationList(_t);
			_t = _retTree;
			astFactory.addASTChild(currentAST, returnAST);
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
		currentAST = __currentAST30;
		_t = __t30;
		_t = _t.getNextSibling();
		structSpecifier_AST = (Block)currentAST.root;
		returnAST = structSpecifier_AST;
		_retTree = _t;
	}
	
	public final void attributeDecl(AST _t) throws RecognitionException {
		
		Block attributeDecl_AST_in = (_t == ASTNULL) ? null : (Block)_t;
		returnAST = null;
		ASTPair currentAST = new ASTPair();
		Block attributeDecl_AST = null;
		
		if (_t==null) _t=ASTNULL;
		switch ( _t.getType()) {
		case LITERAL___attribute__:
		{
			AST __t66 = _t;
			Block tmp36_AST = null;
			Block tmp36_AST_in = null;
			tmp36_AST = (Block)astFactory.create((Block)_t);
			tmp36_AST_in = (Block)_t;
			astFactory.addASTChild(currentAST, tmp36_AST);
			ASTPair __currentAST66 = currentAST.copy();
			currentAST.root = currentAST.child;
			currentAST.child = null;
			match(_t,LITERAL___attribute__);
			_t = _t.getFirstChild();
			{
			_loop68:
			do {
				if (_t==null) _t=ASTNULL;
				if (((_t.getType() >= LITERAL_typedef && _t.getType() <= LITERAL___imag))) {
					Block tmp37_AST = null;
					Block tmp37_AST_in = null;
					tmp37_AST = (Block)astFactory.create((Block)_t);
					tmp37_AST_in = (Block)_t;
					astFactory.addASTChild(currentAST, tmp37_AST);
					if ( _t==null ) throw new MismatchedTokenException();
					_t = _t.getNextSibling();
				}
				else {
					break _loop68;
				}
				
			} while (true);
			}
			currentAST = __currentAST66;
			_t = __t66;
			_t = _t.getNextSibling();
			attributeDecl_AST = (Block)currentAST.root;
			break;
		}
		case NAsmAttribute:
		{
			AST __t69 = _t;
			Block tmp38_AST = null;
			Block tmp38_AST_in = null;
			tmp38_AST = (Block)astFactory.create((Block)_t);
			tmp38_AST_in = (Block)_t;
			astFactory.addASTChild(currentAST, tmp38_AST);
			ASTPair __currentAST69 = currentAST.copy();
			currentAST.root = currentAST.child;
			currentAST.child = null;
			match(_t,NAsmAttribute);
			_t = _t.getFirstChild();
			Block tmp39_AST = null;
			Block tmp39_AST_in = null;
			tmp39_AST = (Block)astFactory.create((Block)_t);
			tmp39_AST_in = (Block)_t;
			astFactory.addASTChild(currentAST, tmp39_AST);
			match(_t,LPAREN);
			_t = _t.getNextSibling();
			pureExpr(_t);
			_t = _retTree;
			astFactory.addASTChild(currentAST, returnAST);
			Block tmp40_AST = null;
			Block tmp40_AST_in = null;
			tmp40_AST = (Block)astFactory.create((Block)_t);
			tmp40_AST_in = (Block)_t;
			astFactory.addASTChild(currentAST, tmp40_AST);
			match(_t,RPAREN);
			_t = _t.getNextSibling();
			currentAST = __currentAST69;
			_t = __t69;
			_t = _t.getNextSibling();
			attributeDecl_AST = (Block)currentAST.root;
			break;
		}
		default:
		{
			throw new NoViableAltException(_t);
		}
		}
		returnAST = attributeDecl_AST;
		_retTree = _t;
	}
	
	public final void unionSpecifier(AST _t) throws RecognitionException {
		
		Block unionSpecifier_AST_in = (_t == ASTNULL) ? null : (Block)_t;
		returnAST = null;
		ASTPair currentAST = new ASTPair();
		Block unionSpecifier_AST = null;
		
		AST __t33 = _t;
		Block tmp41_AST = null;
		Block tmp41_AST_in = null;
		tmp41_AST = (Block)astFactory.create((Block)_t);
		tmp41_AST_in = (Block)_t;
		astFactory.addASTChild(currentAST, tmp41_AST);
		ASTPair __currentAST33 = currentAST.copy();
		currentAST.root = currentAST.child;
		currentAST.child = null;
		match(_t,LITERAL_union);
		_t = _t.getFirstChild();
		structOrUnionBody(_t);
		_t = _retTree;
		astFactory.addASTChild(currentAST, returnAST);
		currentAST = __currentAST33;
		_t = __t33;
		_t = _t.getNextSibling();
		unionSpecifier_AST = (Block)currentAST.root;
		returnAST = unionSpecifier_AST;
		_retTree = _t;
	}
	
	public final void enumSpecifier(AST _t) throws RecognitionException {
		
		Block enumSpecifier_AST_in = (_t == ASTNULL) ? null : (Block)_t;
		returnAST = null;
		ASTPair currentAST = new ASTPair();
		Block enumSpecifier_AST = null;
		
		AST __t57 = _t;
		Block tmp42_AST = null;
		Block tmp42_AST_in = null;
		tmp42_AST = (Block)astFactory.create((Block)_t);
		tmp42_AST_in = (Block)_t;
		astFactory.addASTChild(currentAST, tmp42_AST);
		ASTPair __currentAST57 = currentAST.copy();
		currentAST.root = currentAST.child;
		currentAST.child = null;
		match(_t,LITERAL_enum);
		_t = _t.getFirstChild();
		{
		if (_t==null) _t=ASTNULL;
		switch ( _t.getType()) {
		case ID:
		{
			Block tmp43_AST = null;
			Block tmp43_AST_in = null;
			tmp43_AST = (Block)astFactory.create((Block)_t);
			tmp43_AST_in = (Block)_t;
			astFactory.addASTChild(currentAST, tmp43_AST);
			match(_t,ID);
			_t = _t.getNextSibling();
			break;
		}
		case 3:
		case LCURLY:
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
		case LCURLY:
		{
			Block tmp44_AST = null;
			Block tmp44_AST_in = null;
			tmp44_AST = (Block)astFactory.create((Block)_t);
			tmp44_AST_in = (Block)_t;
			astFactory.addASTChild(currentAST, tmp44_AST);
			match(_t,LCURLY);
			_t = _t.getNextSibling();
			enumList(_t);
			_t = _retTree;
			astFactory.addASTChild(currentAST, returnAST);
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
		currentAST = __currentAST57;
		_t = __t57;
		_t = _t.getNextSibling();
		enumSpecifier_AST = (Block)currentAST.root;
		returnAST = enumSpecifier_AST;
		_retTree = _t;
	}
	
	public final void typedefName(AST _t) throws RecognitionException {
		
		Block typedefName_AST_in = (_t == ASTNULL) ? null : (Block)_t;
		returnAST = null;
		ASTPair currentAST = new ASTPair();
		Block typedefName_AST = null;
		
		AST __t28 = _t;
		Block tmp45_AST = null;
		Block tmp45_AST_in = null;
		tmp45_AST = (Block)astFactory.create((Block)_t);
		tmp45_AST_in = (Block)_t;
		astFactory.addASTChild(currentAST, tmp45_AST);
		ASTPair __currentAST28 = currentAST.copy();
		currentAST.root = currentAST.child;
		currentAST.child = null;
		match(_t,NTypedefName);
		_t = _t.getFirstChild();
		Block tmp46_AST = null;
		Block tmp46_AST_in = null;
		tmp46_AST = (Block)astFactory.create((Block)_t);
		tmp46_AST_in = (Block)_t;
		astFactory.addASTChild(currentAST, tmp46_AST);
		match(_t,ID);
		_t = _t.getNextSibling();
		currentAST = __currentAST28;
		_t = __t28;
		_t = _t.getNextSibling();
		typedefName_AST = (Block)currentAST.root;
		returnAST = typedefName_AST;
		_retTree = _t;
	}
	
	public final void typeName(AST _t) throws RecognitionException {
		
		Block typeName_AST_in = (_t == ASTNULL) ? null : (Block)_t;
		returnAST = null;
		ASTPair currentAST = new ASTPair();
		Block typeName_AST = null;
		
		specifierQualifierList(_t);
		_t = _retTree;
		astFactory.addASTChild(currentAST, returnAST);
		{
		if (_t==null) _t=ASTNULL;
		switch ( _t.getType()) {
		case NNonemptyAbstractDeclarator:
		{
			nonemptyAbstractDeclarator(_t);
			_t = _retTree;
			astFactory.addASTChild(currentAST, returnAST);
			break;
		}
		case ID:
		case STAR:
		case RPAREN:
		case PTR:
		case DOT:
		case LOR:
		case LAND:
		case BOR:
		case BXOR:
		case BAND:
		case EQUAL:
		case NOT_EQUAL:
		case LT:
		case LTE:
		case GT:
		case GTE:
		case LSHIFT:
		case RSHIFT:
		case PLUS:
		case MINUS:
		case DIV:
		case MOD:
		case LITERAL_sizeof:
		case CharLiteral:
		case IntOctalConst:
		case LongOctalConst:
		case UnsignedOctalConst:
		case IntIntConst:
		case LongIntConst:
		case UnsignedIntConst:
		case IntHexConst:
		case LongHexConst:
		case UnsignedHexConst:
		case FloatDoubleConst:
		case DoubleDoubleConst:
		case LongDoubleConst:
		case NCast:
		case NUnaryExpr:
		case NPostfixExpr:
		case NStringSeq:
		case NPureExpressionGroup:
		case NArrayElement:
		case LITERAL___alignof:
		{
			break;
		}
		default:
		{
			throw new NoViableAltException(_t);
		}
		}
		}
		typeName_AST = (Block)currentAST.root;
		returnAST = typeName_AST;
		_retTree = _t;
	}
	
	public final void pureExpr(AST _t) throws RecognitionException {
		
		Block pureExpr_AST_in = (_t == ASTNULL) ? null : (Block)_t;
		returnAST = null;
		ASTPair currentAST = new ASTPair();
		Block pureExpr_AST = null;
		
		if (_t==null) _t=ASTNULL;
		switch ( _t.getType()) {
		case LOR:
		{
			pureLogicalOrExpr(_t);
			_t = _retTree;
			astFactory.addASTChild(currentAST, returnAST);
			pureExpr_AST = (Block)currentAST.root;
			break;
		}
		case LAND:
		{
			pureLogicalAndExpr(_t);
			_t = _retTree;
			astFactory.addASTChild(currentAST, returnAST);
			pureExpr_AST = (Block)currentAST.root;
			break;
		}
		case EQUAL:
		case NOT_EQUAL:
		{
			pureEqualityExpr(_t);
			_t = _retTree;
			astFactory.addASTChild(currentAST, returnAST);
			pureExpr_AST = (Block)currentAST.root;
			break;
		}
		case LT:
		case LTE:
		case GT:
		case GTE:
		{
			pureRelationalExpr(_t);
			_t = _retTree;
			astFactory.addASTChild(currentAST, returnAST);
			pureExpr_AST = (Block)currentAST.root;
			break;
		}
		case PLUS:
		case MINUS:
		{
			pureAdditiveExpr(_t);
			_t = _retTree;
			astFactory.addASTChild(currentAST, returnAST);
			pureExpr_AST = (Block)currentAST.root;
			break;
		}
		case STAR:
		case DIV:
		case MOD:
		{
			pureMultExpr(_t);
			_t = _retTree;
			astFactory.addASTChild(currentAST, returnAST);
			pureExpr_AST = (Block)currentAST.root;
			break;
		}
		case BOR:
		case BXOR:
		case BAND:
		case LSHIFT:
		case RSHIFT:
		{
			pureBitwiseExpr(_t);
			_t = _retTree;
			astFactory.addASTChild(currentAST, returnAST);
			pureExpr_AST = (Block)currentAST.root;
			break;
		}
		case NCast:
		{
			pureCastExpr(_t);
			_t = _retTree;
			astFactory.addASTChild(currentAST, returnAST);
			pureExpr_AST = (Block)currentAST.root;
			break;
		}
		case LITERAL_sizeof:
		case NUnaryExpr:
		case NPostfixExpr:
		case LITERAL___alignof:
		{
			pureUnaryExpr(_t);
			_t = _retTree;
			astFactory.addASTChild(currentAST, returnAST);
			pureExpr_AST = (Block)currentAST.root;
			break;
		}
		case ID:
		case PTR:
		case DOT:
		case CharLiteral:
		case IntOctalConst:
		case LongOctalConst:
		case UnsignedOctalConst:
		case IntIntConst:
		case LongIntConst:
		case UnsignedIntConst:
		case IntHexConst:
		case LongHexConst:
		case UnsignedHexConst:
		case FloatDoubleConst:
		case DoubleDoubleConst:
		case LongDoubleConst:
		case NStringSeq:
		case NPureExpressionGroup:
		case NArrayElement:
		{
			purePrimaryExpr(_t);
			_t = _retTree;
			astFactory.addASTChild(currentAST, returnAST);
			pureExpr_AST = (Block)currentAST.root;
			break;
		}
		default:
		{
			throw new NoViableAltException(_t);
		}
		}
		returnAST = pureExpr_AST;
		_retTree = _t;
	}
	
	public final void structDeclarationList(AST _t) throws RecognitionException {
		
		Block structDeclarationList_AST_in = (_t == ASTNULL) ? null : (Block)_t;
		returnAST = null;
		ASTPair currentAST = new ASTPair();
		Block structDeclarationList_AST = null;
		
		AST __t40 = _t;
		Block tmp47_AST = null;
		Block tmp47_AST_in = null;
		tmp47_AST = (Block)astFactory.create((Block)_t);
		tmp47_AST_in = (Block)_t;
		astFactory.addASTChild(currentAST, tmp47_AST);
		ASTPair __currentAST40 = currentAST.copy();
		currentAST.root = currentAST.child;
		currentAST.child = null;
		match(_t,NStructFields);
		_t = _t.getFirstChild();
		{
		_loop42:
		do {
			if (_t==null) _t=ASTNULL;
			if ((_tokenSet_0.member(_t.getType()))) {
				structDeclaration(_t);
				_t = _retTree;
				astFactory.addASTChild(currentAST, returnAST);
			}
			else {
				break _loop42;
			}
			
		} while (true);
		}
		currentAST = __currentAST40;
		_t = __t40;
		_t = _t.getNextSibling();
		structDeclarationList_AST = (Block)currentAST.root;
		returnAST = structDeclarationList_AST;
		_retTree = _t;
	}
	
	public final void structOrUnionBody(AST _t) throws RecognitionException {
		
		Block structOrUnionBody_AST_in = (_t == ASTNULL) ? null : (Block)_t;
		returnAST = null;
		ASTPair currentAST = new ASTPair();
		Block structOrUnionBody_AST = null;
		
		{
		boolean synPredMatched37 = false;
		if (((_t.getType()==ID))) {
			AST __t37 = _t;
			synPredMatched37 = true;
			inputState.guessing++;
			try {
				{
				match(_t,ID);
				_t = _t.getNextSibling();
				match(_t,NStructFields);
				_t = _t.getNextSibling();
				}
			}
			catch (RecognitionException pe) {
				synPredMatched37 = false;
			}
			_t = __t37;
			inputState.guessing--;
		}
		if ( synPredMatched37 ) {
			Block tmp48_AST = null;
			Block tmp48_AST_in = null;
			tmp48_AST = (Block)astFactory.create((Block)_t);
			tmp48_AST_in = (Block)_t;
			astFactory.addASTChild(currentAST, tmp48_AST);
			match(_t,ID);
			_t = _t.getNextSibling();
			Block tmp49_AST_in = null;
			match(_t,NStructFields);
			_t = _t.getNextSibling();
			{
			if (_t==null) _t=ASTNULL;
			switch ( _t.getType()) {
			case NStructFields:
			{
				structDeclarationList(_t);
				_t = _retTree;
				astFactory.addASTChild(currentAST, returnAST);
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
		}
		else if ((_t.getType()==ID)) {
			Block tmp50_AST = null;
			Block tmp50_AST_in = null;
			tmp50_AST = (Block)astFactory.create((Block)_t);
			tmp50_AST_in = (Block)_t;
			astFactory.addASTChild(currentAST, tmp50_AST);
			match(_t,ID);
			_t = _t.getNextSibling();
		}
		else {
			throw new NoViableAltException(_t);
		}
		
		}
		structOrUnionBody_AST = (Block)currentAST.root;
		returnAST = structOrUnionBody_AST;
		_retTree = _t;
	}
	
	public final void structDeclaration(AST _t) throws RecognitionException {
		
		Block structDeclaration_AST_in = (_t == ASTNULL) ? null : (Block)_t;
		returnAST = null;
		ASTPair currentAST = new ASTPair();
		Block structDeclaration_AST = null;
		
		specifierQualifierList(_t);
		_t = _retTree;
		astFactory.addASTChild(currentAST, returnAST);
		structDeclaratorList(_t);
		_t = _retTree;
		astFactory.addASTChild(currentAST, returnAST);
		structDeclaration_AST = (Block)currentAST.root;
		returnAST = structDeclaration_AST;
		_retTree = _t;
	}
	
	public final void specifierQualifierList(AST _t) throws RecognitionException {
		
		Block specifierQualifierList_AST_in = (_t == ASTNULL) ? null : (Block)_t;
		returnAST = null;
		ASTPair currentAST = new ASTPair();
		Block specifierQualifierList_AST = null;
		
		{
		int _cnt46=0;
		_loop46:
		do {
			if (_t==null) _t=ASTNULL;
			switch ( _t.getType()) {
			case LITERAL_struct:
			case LITERAL_union:
			case LITERAL_enum:
			case LITERAL_void:
			case LITERAL_char:
			case LITERAL_short:
			case LITERAL_int:
			case LITERAL_long:
			case LITERAL_float:
			case LITERAL_double:
			case LITERAL_signed:
			case LITERAL_unsigned:
			case NTypedefName:
			case LITERAL_typeof:
			case LITERAL___complex:
			{
				typeSpecifier(_t);
				_t = _retTree;
				astFactory.addASTChild(currentAST, returnAST);
				break;
			}
			case LITERAL_const:
			case LITERAL_volatile:
			{
				typeQualifier(_t);
				_t = _retTree;
				astFactory.addASTChild(currentAST, returnAST);
				break;
			}
			case NPointerGroup:
			{
				pointerGroup(_t);
				_t = _retTree;
				astFactory.addASTChild(currentAST, returnAST);
				break;
			}
			default:
			{
				if ( _cnt46>=1 ) { break _loop46; } else {throw new NoViableAltException(_t);}
			}
			}
			_cnt46++;
		} while (true);
		}
		specifierQualifierList_AST = (Block)currentAST.root;
		returnAST = specifierQualifierList_AST;
		_retTree = _t;
	}
	
	public final void structDeclaratorList(AST _t) throws RecognitionException {
		
		Block structDeclaratorList_AST_in = (_t == ASTNULL) ? null : (Block)_t;
		returnAST = null;
		ASTPair currentAST = new ASTPair();
		Block structDeclaratorList_AST = null;
		
		{
		int _cnt49=0;
		_loop49:
		do {
			if (_t==null) _t=ASTNULL;
			if ((_t.getType()==NStructDeclarator)) {
				structDeclarator(_t);
				_t = _retTree;
				astFactory.addASTChild(currentAST, returnAST);
			}
			else {
				if ( _cnt49>=1 ) { break _loop49; } else {throw new NoViableAltException(_t);}
			}
			
			_cnt49++;
		} while (true);
		}
		structDeclaratorList_AST = (Block)currentAST.root;
		returnAST = structDeclaratorList_AST;
		_retTree = _t;
	}
	
	public final void structDeclarator(AST _t) throws RecognitionException {
		
		Block structDeclarator_AST_in = (_t == ASTNULL) ? null : (Block)_t;
		returnAST = null;
		ASTPair currentAST = new ASTPair();
		Block structDeclarator_AST = null;
		
		AST __t51 = _t;
		Block tmp51_AST = null;
		Block tmp51_AST_in = null;
		tmp51_AST = (Block)astFactory.create((Block)_t);
		tmp51_AST_in = (Block)_t;
		astFactory.addASTChild(currentAST, tmp51_AST);
		ASTPair __currentAST51 = currentAST.copy();
		currentAST.root = currentAST.child;
		currentAST.child = null;
		match(_t,NStructDeclarator);
		_t = _t.getFirstChild();
		{
		if (_t==null) _t=ASTNULL;
		switch ( _t.getType()) {
		case NDeclarator:
		{
			declarator(_t);
			_t = _retTree;
			astFactory.addASTChild(currentAST, returnAST);
			break;
		}
		case 3:
		case COLON:
		case LITERAL___attribute__:
		case NAsmAttribute:
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
		case COLON:
		{
			Block tmp52_AST = null;
			Block tmp52_AST_in = null;
			tmp52_AST = (Block)astFactory.create((Block)_t);
			tmp52_AST_in = (Block)_t;
			astFactory.addASTChild(currentAST, tmp52_AST);
			match(_t,COLON);
			_t = _t.getNextSibling();
			pureExpr(_t);
			_t = _retTree;
			astFactory.addASTChild(currentAST, returnAST);
			break;
		}
		case 3:
		case LITERAL___attribute__:
		case NAsmAttribute:
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
		_loop55:
		do {
			if (_t==null) _t=ASTNULL;
			if ((_t.getType()==LITERAL___attribute__||_t.getType()==NAsmAttribute)) {
				attributeDecl(_t);
				_t = _retTree;
				astFactory.addASTChild(currentAST, returnAST);
			}
			else {
				break _loop55;
			}
			
		} while (true);
		}
		currentAST = __currentAST51;
		_t = __t51;
		_t = _t.getNextSibling();
		structDeclarator_AST = (Block)currentAST.root;
		returnAST = structDeclarator_AST;
		_retTree = _t;
	}
	
	public final void enumList(AST _t) throws RecognitionException {
		
		Block enumList_AST_in = (_t == ASTNULL) ? null : (Block)_t;
		returnAST = null;
		ASTPair currentAST = new ASTPair();
		Block enumList_AST = null;
		
		{
		int _cnt62=0;
		_loop62:
		do {
			if (_t==null) _t=ASTNULL;
			if ((_t.getType()==ID)) {
				enumerator(_t);
				_t = _retTree;
				astFactory.addASTChild(currentAST, returnAST);
			}
			else {
				if ( _cnt62>=1 ) { break _loop62; } else {throw new NoViableAltException(_t);}
			}
			
			_cnt62++;
		} while (true);
		}
		enumList_AST = (Block)currentAST.root;
		returnAST = enumList_AST;
		_retTree = _t;
	}
	
	public final void enumerator(AST _t) throws RecognitionException {
		
		Block enumerator_AST_in = (_t == ASTNULL) ? null : (Block)_t;
		returnAST = null;
		ASTPair currentAST = new ASTPair();
		Block enumerator_AST = null;
		
		Block tmp53_AST = null;
		Block tmp53_AST_in = null;
		tmp53_AST = (Block)astFactory.create((Block)_t);
		tmp53_AST_in = (Block)_t;
		astFactory.addASTChild(currentAST, tmp53_AST);
		match(_t,ID);
		_t = _t.getNextSibling();
		{
		if (_t==null) _t=ASTNULL;
		switch ( _t.getType()) {
		case ASSIGN:
		{
			Block tmp54_AST = null;
			Block tmp54_AST_in = null;
			tmp54_AST = (Block)astFactory.create((Block)_t);
			tmp54_AST_in = (Block)_t;
			astFactory.addASTChild(currentAST, tmp54_AST);
			match(_t,ASSIGN);
			_t = _t.getNextSibling();
			pureExpr(_t);
			_t = _retTree;
			astFactory.addASTChild(currentAST, returnAST);
			break;
		}
		case 3:
		case ID:
		{
			break;
		}
		default:
		{
			throw new NoViableAltException(_t);
		}
		}
		}
		enumerator_AST = (Block)currentAST.root;
		returnAST = enumerator_AST;
		_retTree = _t;
	}
	
	public final void initDeclList(AST _t) throws RecognitionException {
		
		Block initDeclList_AST_in = (_t == ASTNULL) ? null : (Block)_t;
		returnAST = null;
		ASTPair currentAST = new ASTPair();
		Block initDeclList_AST = null;
		
		{
		int _cnt72=0;
		_loop72:
		do {
			if (_t==null) _t=ASTNULL;
			if ((_t.getType()==NInitDecl)) {
				initDecl(_t);
				_t = _retTree;
				astFactory.addASTChild(currentAST, returnAST);
			}
			else {
				if ( _cnt72>=1 ) { break _loop72; } else {throw new NoViableAltException(_t);}
			}
			
			_cnt72++;
		} while (true);
		}
		initDeclList_AST = (Block)currentAST.root;
		returnAST = initDeclList_AST;
		_retTree = _t;
	}
	
	public final void initDecl(AST _t) throws RecognitionException {
		
		Block initDecl_AST_in = (_t == ASTNULL) ? null : (Block)_t;
		returnAST = null;
		ASTPair currentAST = new ASTPair();
		Block initDecl_AST = null;
		Block d_AST = null;
		Block d = null;
		Block in_AST = null;
		Block in = null;
		
		AST __t74 = _t;
		Block tmp55_AST = null;
		Block tmp55_AST_in = null;
		tmp55_AST = (Block)astFactory.create((Block)_t);
		tmp55_AST_in = (Block)_t;
		ASTPair __currentAST74 = currentAST.copy();
		currentAST.root = currentAST.child;
		currentAST.child = null;
		match(_t,NInitDecl);
		_t = _t.getFirstChild();
		d = _t==ASTNULL ? null : (Block)_t;
		declarator(_t);
		_t = _retTree;
		d_AST = (Block)returnAST;
		if ( inputState.guessing==0 ) {
			initDecl_AST = (Block)currentAST.root;
			initDecl_AST = d_AST;
			currentAST.root = initDecl_AST;
			currentAST.child = initDecl_AST!=null &&initDecl_AST.getFirstChild()!=null ?
				initDecl_AST.getFirstChild() : initDecl_AST;
			currentAST.advanceChildToEnd();
		}
		{
		if (_t==null) _t=ASTNULL;
		switch ( _t.getType()) {
		case ASSIGN:
		{
			Block tmp56_AST_in = null;
			match(_t,ASSIGN);
			_t = _t.getNextSibling();
			in = _t==ASTNULL ? null : (Block)_t;
			initializer(_t);
			_t = _retTree;
			in_AST = (Block)returnAST;
			if ( inputState.guessing==0 ) {
				initDecl_AST = (Block)currentAST.root;
				initDecl_AST = (Block)astFactory.make( (new ASTArray(2)).add(d_AST).add(in_AST));
				currentAST.root = initDecl_AST;
				currentAST.child = initDecl_AST!=null &&initDecl_AST.getFirstChild()!=null ?
					initDecl_AST.getFirstChild() : initDecl_AST;
				currentAST.advanceChildToEnd();
			}
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
		currentAST = __currentAST74;
		_t = __t74;
		_t = _t.getNextSibling();
		returnAST = initDecl_AST;
		_retTree = _t;
	}
	
	public final void idList(AST _t) throws RecognitionException {
		
		Block idList_AST_in = (_t == ASTNULL) ? null : (Block)_t;
		returnAST = null;
		ASTPair currentAST = new ASTPair();
		Block idList_AST = null;
		
		Block tmp57_AST = null;
		Block tmp57_AST_in = null;
		tmp57_AST = (Block)astFactory.create((Block)_t);
		tmp57_AST_in = (Block)_t;
		astFactory.addASTChild(currentAST, tmp57_AST);
		match(_t,ID);
		_t = _t.getNextSibling();
		{
		_loop84:
		do {
			if (_t==null) _t=ASTNULL;
			if ((_t.getType()==COMMA)) {
				Block tmp58_AST = null;
				Block tmp58_AST_in = null;
				tmp58_AST = (Block)astFactory.create((Block)_t);
				tmp58_AST_in = (Block)_t;
				astFactory.addASTChild(currentAST, tmp58_AST);
				match(_t,COMMA);
				_t = _t.getNextSibling();
				Block tmp59_AST = null;
				Block tmp59_AST_in = null;
				tmp59_AST = (Block)astFactory.create((Block)_t);
				tmp59_AST_in = (Block)_t;
				astFactory.addASTChild(currentAST, tmp59_AST);
				match(_t,ID);
				_t = _t.getNextSibling();
			}
			else {
				break _loop84;
			}
			
		} while (true);
		}
		idList_AST = (Block)currentAST.root;
		returnAST = idList_AST;
		_retTree = _t;
	}
	
	public final void initializerElementLabel(AST _t) throws RecognitionException {
		
		Block initializerElementLabel_AST_in = (_t == ASTNULL) ? null : (Block)_t;
		returnAST = null;
		ASTPair currentAST = new ASTPair();
		Block initializerElementLabel_AST = null;
		
		AST __t89 = _t;
		Block tmp60_AST = null;
		Block tmp60_AST_in = null;
		tmp60_AST = (Block)astFactory.create((Block)_t);
		tmp60_AST_in = (Block)_t;
		astFactory.addASTChild(currentAST, tmp60_AST);
		ASTPair __currentAST89 = currentAST.copy();
		currentAST.root = currentAST.child;
		currentAST.child = null;
		match(_t,NInitializerElementLabel);
		_t = _t.getFirstChild();
		{
		if (_t==null) _t=ASTNULL;
		switch ( _t.getType()) {
		case LBRACKET:
		{
			{
			Block tmp61_AST = null;
			Block tmp61_AST_in = null;
			tmp61_AST = (Block)astFactory.create((Block)_t);
			tmp61_AST_in = (Block)_t;
			astFactory.addASTChild(currentAST, tmp61_AST);
			match(_t,LBRACKET);
			_t = _t.getNextSibling();
			pureExpr(_t);
			_t = _retTree;
			astFactory.addASTChild(currentAST, returnAST);
			Block tmp62_AST = null;
			Block tmp62_AST_in = null;
			tmp62_AST = (Block)astFactory.create((Block)_t);
			tmp62_AST_in = (Block)_t;
			astFactory.addASTChild(currentAST, tmp62_AST);
			match(_t,RBRACKET);
			_t = _t.getNextSibling();
			{
			if (_t==null) _t=ASTNULL;
			switch ( _t.getType()) {
			case ASSIGN:
			{
				Block tmp63_AST = null;
				Block tmp63_AST_in = null;
				tmp63_AST = (Block)astFactory.create((Block)_t);
				tmp63_AST_in = (Block)_t;
				astFactory.addASTChild(currentAST, tmp63_AST);
				match(_t,ASSIGN);
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
			}
			break;
		}
		case ID:
		{
			Block tmp64_AST = null;
			Block tmp64_AST_in = null;
			tmp64_AST = (Block)astFactory.create((Block)_t);
			tmp64_AST_in = (Block)_t;
			astFactory.addASTChild(currentAST, tmp64_AST);
			match(_t,ID);
			_t = _t.getNextSibling();
			Block tmp65_AST = null;
			Block tmp65_AST_in = null;
			tmp65_AST = (Block)astFactory.create((Block)_t);
			tmp65_AST_in = (Block)_t;
			astFactory.addASTChild(currentAST, tmp65_AST);
			match(_t,COLON);
			_t = _t.getNextSibling();
			break;
		}
		case DOT:
		{
			Block tmp66_AST = null;
			Block tmp66_AST_in = null;
			tmp66_AST = (Block)astFactory.create((Block)_t);
			tmp66_AST_in = (Block)_t;
			astFactory.addASTChild(currentAST, tmp66_AST);
			match(_t,DOT);
			_t = _t.getNextSibling();
			Block tmp67_AST = null;
			Block tmp67_AST_in = null;
			tmp67_AST = (Block)astFactory.create((Block)_t);
			tmp67_AST_in = (Block)_t;
			astFactory.addASTChild(currentAST, tmp67_AST);
			match(_t,ID);
			_t = _t.getNextSibling();
			Block tmp68_AST = null;
			Block tmp68_AST_in = null;
			tmp68_AST = (Block)astFactory.create((Block)_t);
			tmp68_AST_in = (Block)_t;
			astFactory.addASTChild(currentAST, tmp68_AST);
			match(_t,ASSIGN);
			_t = _t.getNextSibling();
			break;
		}
		default:
		{
			throw new NoViableAltException(_t);
		}
		}
		}
		currentAST = __currentAST89;
		_t = __t89;
		_t = _t.getNextSibling();
		initializerElementLabel_AST = (Block)currentAST.root;
		returnAST = initializerElementLabel_AST;
		_retTree = _t;
	}
	
	public final void lcurlyInitializer(AST _t) throws RecognitionException {
		
		Block lcurlyInitializer_AST_in = (_t == ASTNULL) ? null : (Block)_t;
		returnAST = null;
		ASTPair currentAST = new ASTPair();
		Block lcurlyInitializer_AST = null;
		
		AST __t94 = _t;
		Block tmp69_AST = null;
		Block tmp69_AST_in = null;
		tmp69_AST = (Block)astFactory.create((Block)_t);
		tmp69_AST_in = (Block)_t;
		astFactory.addASTChild(currentAST, tmp69_AST);
		ASTPair __currentAST94 = currentAST.copy();
		currentAST.root = currentAST.child;
		currentAST.child = null;
		match(_t,NLCurlyInitializer);
		_t = _t.getFirstChild();
		initializerList(_t);
		_t = _retTree;
		astFactory.addASTChild(currentAST, returnAST);
		currentAST = __currentAST94;
		_t = __t94;
		_t = _t.getNextSibling();
		lcurlyInitializer_AST = (Block)currentAST.root;
		returnAST = lcurlyInitializer_AST;
		_retTree = _t;
	}
	
	public final void initializerList(AST _t) throws RecognitionException {
		
		Block initializerList_AST_in = (_t == ASTNULL) ? null : (Block)_t;
		returnAST = null;
		ASTPair currentAST = new ASTPair();
		Block initializerList_AST = null;
		
		{
		_loop97:
		do {
			if (_t==null) _t=ASTNULL;
			if ((_t.getType()==NInitializer||_t.getType()==NLCurlyInitializer)) {
				initializer(_t);
				_t = _retTree;
				astFactory.addASTChild(currentAST, returnAST);
			}
			else {
				break _loop97;
			}
			
		} while (true);
		}
		initializerList_AST = (Block)currentAST.root;
		returnAST = initializerList_AST;
		_retTree = _t;
	}
	
	public final void parameterTypeList(AST _t) throws RecognitionException {
		
		Block parameterTypeList_AST_in = (_t == ASTNULL) ? null : (Block)_t;
		returnAST = null;
		ASTPair currentAST = new ASTPair();
		Block parameterTypeList_AST = null;
		
		{
		int _cnt111=0;
		_loop111:
		do {
			if (_t==null) _t=ASTNULL;
			if ((_t.getType()==NParameterDeclaration)) {
				parameterDeclaration(_t);
				_t = _retTree;
				astFactory.addASTChild(currentAST, returnAST);
				{
				if (_t==null) _t=ASTNULL;
				switch ( _t.getType()) {
				case COMMA:
				{
					Block tmp70_AST = null;
					Block tmp70_AST_in = null;
					tmp70_AST = (Block)astFactory.create((Block)_t);
					tmp70_AST_in = (Block)_t;
					astFactory.addASTChild(currentAST, tmp70_AST);
					match(_t,COMMA);
					_t = _t.getNextSibling();
					break;
				}
				case SEMI:
				{
					Block tmp71_AST = null;
					Block tmp71_AST_in = null;
					tmp71_AST = (Block)astFactory.create((Block)_t);
					tmp71_AST_in = (Block)_t;
					astFactory.addASTChild(currentAST, tmp71_AST);
					match(_t,SEMI);
					_t = _t.getNextSibling();
					break;
				}
				case 3:
				case RPAREN:
				case VARARGS:
				case NParameterDeclaration:
				{
					break;
				}
				default:
				{
					throw new NoViableAltException(_t);
				}
				}
				}
			}
			else {
				if ( _cnt111>=1 ) { break _loop111; } else {throw new NoViableAltException(_t);}
			}
			
			_cnt111++;
		} while (true);
		}
		{
		if (_t==null) _t=ASTNULL;
		switch ( _t.getType()) {
		case VARARGS:
		{
			Block tmp72_AST = null;
			Block tmp72_AST_in = null;
			tmp72_AST = (Block)astFactory.create((Block)_t);
			tmp72_AST_in = (Block)_t;
			astFactory.addASTChild(currentAST, tmp72_AST);
			match(_t,VARARGS);
			_t = _t.getNextSibling();
			break;
		}
		case 3:
		case RPAREN:
		{
			break;
		}
		default:
		{
			throw new NoViableAltException(_t);
		}
		}
		}
		parameterTypeList_AST = (Block)currentAST.root;
		returnAST = parameterTypeList_AST;
		_retTree = _t;
	}
	
	public final void parameterDeclaration(AST _t) throws RecognitionException {
		
		Block parameterDeclaration_AST_in = (_t == ASTNULL) ? null : (Block)_t;
		returnAST = null;
		ASTPair currentAST = new ASTPair();
		Block parameterDeclaration_AST = null;
		
		AST __t114 = _t;
		Block tmp73_AST = null;
		Block tmp73_AST_in = null;
		tmp73_AST = (Block)astFactory.create((Block)_t);
		tmp73_AST_in = (Block)_t;
		astFactory.addASTChild(currentAST, tmp73_AST);
		ASTPair __currentAST114 = currentAST.copy();
		currentAST.root = currentAST.child;
		currentAST.child = null;
		match(_t,NParameterDeclaration);
		_t = _t.getFirstChild();
		declSpecifiers(_t);
		_t = _retTree;
		astFactory.addASTChild(currentAST, returnAST);
		{
		if (_t==null) _t=ASTNULL;
		switch ( _t.getType()) {
		case NDeclarator:
		{
			declarator(_t);
			_t = _retTree;
			astFactory.addASTChild(currentAST, returnAST);
			break;
		}
		case NNonemptyAbstractDeclarator:
		{
			nonemptyAbstractDeclarator(_t);
			_t = _retTree;
			astFactory.addASTChild(currentAST, returnAST);
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
		currentAST = __currentAST114;
		_t = __t114;
		_t = _t.getNextSibling();
		if ( inputState.guessing==0 ) {
			parameterDeclaration_AST = (Block)currentAST.root;
			
			parameterDeclaration_AST.setType (NBlock);
			parameterDeclaration_AST.setBlockType (BlockType.DECLARATION);
			
		}
		parameterDeclaration_AST = (Block)currentAST.root;
		returnAST = parameterDeclaration_AST;
		_retTree = _t;
	}
	
	public final void nonemptyAbstractDeclarator(AST _t) throws RecognitionException {
		
		Block nonemptyAbstractDeclarator_AST_in = (_t == ASTNULL) ? null : (Block)_t;
		returnAST = null;
		ASTPair currentAST = new ASTPair();
		Block nonemptyAbstractDeclarator_AST = null;
		
		AST __t221 = _t;
		Block tmp74_AST = null;
		Block tmp74_AST_in = null;
		tmp74_AST = (Block)astFactory.create((Block)_t);
		tmp74_AST_in = (Block)_t;
		astFactory.addASTChild(currentAST, tmp74_AST);
		ASTPair __currentAST221 = currentAST.copy();
		currentAST.root = currentAST.child;
		currentAST.child = null;
		match(_t,NNonemptyAbstractDeclarator);
		_t = _t.getFirstChild();
		{
		if (_t==null) _t=ASTNULL;
		switch ( _t.getType()) {
		case NPointerGroup:
		{
			pointerGroup(_t);
			_t = _retTree;
			astFactory.addASTChild(currentAST, returnAST);
			{
			_loop228:
			do {
				if (_t==null) _t=ASTNULL;
				switch ( _t.getType()) {
				case LPAREN:
				{
					{
					Block tmp75_AST = null;
					Block tmp75_AST_in = null;
					tmp75_AST = (Block)astFactory.create((Block)_t);
					tmp75_AST_in = (Block)_t;
					astFactory.addASTChild(currentAST, tmp75_AST);
					match(_t,LPAREN);
					_t = _t.getNextSibling();
					{
					if (_t==null) _t=ASTNULL;
					switch ( _t.getType()) {
					case NNonemptyAbstractDeclarator:
					{
						nonemptyAbstractDeclarator(_t);
						_t = _retTree;
						astFactory.addASTChild(currentAST, returnAST);
						break;
					}
					case NParameterDeclaration:
					{
						parameterTypeList(_t);
						_t = _retTree;
						astFactory.addASTChild(currentAST, returnAST);
						break;
					}
					case RPAREN:
					{
						break;
					}
					default:
					{
						throw new NoViableAltException(_t);
					}
					}
					}
					Block tmp76_AST = null;
					Block tmp76_AST_in = null;
					tmp76_AST = (Block)astFactory.create((Block)_t);
					tmp76_AST_in = (Block)_t;
					astFactory.addASTChild(currentAST, tmp76_AST);
					match(_t,RPAREN);
					_t = _t.getNextSibling();
					}
					break;
				}
				case LBRACKET:
				{
					{
					Block tmp77_AST = null;
					Block tmp77_AST_in = null;
					tmp77_AST = (Block)astFactory.create((Block)_t);
					tmp77_AST_in = (Block)_t;
					astFactory.addASTChild(currentAST, tmp77_AST);
					match(_t,LBRACKET);
					_t = _t.getNextSibling();
					{
					if (_t==null) _t=ASTNULL;
					switch ( _t.getType()) {
					case ID:
					case STAR:
					case PTR:
					case DOT:
					case LOR:
					case LAND:
					case BOR:
					case BXOR:
					case BAND:
					case EQUAL:
					case NOT_EQUAL:
					case LT:
					case LTE:
					case GT:
					case GTE:
					case LSHIFT:
					case RSHIFT:
					case PLUS:
					case MINUS:
					case DIV:
					case MOD:
					case LITERAL_sizeof:
					case CharLiteral:
					case IntOctalConst:
					case LongOctalConst:
					case UnsignedOctalConst:
					case IntIntConst:
					case LongIntConst:
					case UnsignedIntConst:
					case IntHexConst:
					case LongHexConst:
					case UnsignedHexConst:
					case FloatDoubleConst:
					case DoubleDoubleConst:
					case LongDoubleConst:
					case NCast:
					case NUnaryExpr:
					case NPostfixExpr:
					case NStringSeq:
					case NPureExpressionGroup:
					case NArrayElement:
					case LITERAL___alignof:
					{
						pureExpr(_t);
						_t = _retTree;
						astFactory.addASTChild(currentAST, returnAST);
						break;
					}
					case RBRACKET:
					{
						break;
					}
					default:
					{
						throw new NoViableAltException(_t);
					}
					}
					}
					Block tmp78_AST = null;
					Block tmp78_AST_in = null;
					tmp78_AST = (Block)astFactory.create((Block)_t);
					tmp78_AST_in = (Block)_t;
					astFactory.addASTChild(currentAST, tmp78_AST);
					match(_t,RBRACKET);
					_t = _t.getNextSibling();
					}
					break;
				}
				default:
				{
					break _loop228;
				}
				}
			} while (true);
			}
			break;
		}
		case LPAREN:
		case LBRACKET:
		{
			{
			int _cnt234=0;
			_loop234:
			do {
				if (_t==null) _t=ASTNULL;
				switch ( _t.getType()) {
				case LPAREN:
				{
					{
					Block tmp79_AST = null;
					Block tmp79_AST_in = null;
					tmp79_AST = (Block)astFactory.create((Block)_t);
					tmp79_AST_in = (Block)_t;
					astFactory.addASTChild(currentAST, tmp79_AST);
					match(_t,LPAREN);
					_t = _t.getNextSibling();
					{
					if (_t==null) _t=ASTNULL;
					switch ( _t.getType()) {
					case NNonemptyAbstractDeclarator:
					{
						nonemptyAbstractDeclarator(_t);
						_t = _retTree;
						astFactory.addASTChild(currentAST, returnAST);
						break;
					}
					case NParameterDeclaration:
					{
						parameterTypeList(_t);
						_t = _retTree;
						astFactory.addASTChild(currentAST, returnAST);
						break;
					}
					case RPAREN:
					{
						break;
					}
					default:
					{
						throw new NoViableAltException(_t);
					}
					}
					}
					Block tmp80_AST = null;
					Block tmp80_AST_in = null;
					tmp80_AST = (Block)astFactory.create((Block)_t);
					tmp80_AST_in = (Block)_t;
					astFactory.addASTChild(currentAST, tmp80_AST);
					match(_t,RPAREN);
					_t = _t.getNextSibling();
					}
					break;
				}
				case LBRACKET:
				{
					{
					Block tmp81_AST = null;
					Block tmp81_AST_in = null;
					tmp81_AST = (Block)astFactory.create((Block)_t);
					tmp81_AST_in = (Block)_t;
					astFactory.addASTChild(currentAST, tmp81_AST);
					match(_t,LBRACKET);
					_t = _t.getNextSibling();
					{
					if (_t==null) _t=ASTNULL;
					switch ( _t.getType()) {
					case ID:
					case STAR:
					case PTR:
					case DOT:
					case LOR:
					case LAND:
					case BOR:
					case BXOR:
					case BAND:
					case EQUAL:
					case NOT_EQUAL:
					case LT:
					case LTE:
					case GT:
					case GTE:
					case LSHIFT:
					case RSHIFT:
					case PLUS:
					case MINUS:
					case DIV:
					case MOD:
					case LITERAL_sizeof:
					case CharLiteral:
					case IntOctalConst:
					case LongOctalConst:
					case UnsignedOctalConst:
					case IntIntConst:
					case LongIntConst:
					case UnsignedIntConst:
					case IntHexConst:
					case LongHexConst:
					case UnsignedHexConst:
					case FloatDoubleConst:
					case DoubleDoubleConst:
					case LongDoubleConst:
					case NCast:
					case NUnaryExpr:
					case NPostfixExpr:
					case NStringSeq:
					case NPureExpressionGroup:
					case NArrayElement:
					case LITERAL___alignof:
					{
						pureExpr(_t);
						_t = _retTree;
						astFactory.addASTChild(currentAST, returnAST);
						break;
					}
					case RBRACKET:
					{
						break;
					}
					default:
					{
						throw new NoViableAltException(_t);
					}
					}
					}
					Block tmp82_AST = null;
					Block tmp82_AST_in = null;
					tmp82_AST = (Block)astFactory.create((Block)_t);
					tmp82_AST_in = (Block)_t;
					astFactory.addASTChild(currentAST, tmp82_AST);
					match(_t,RBRACKET);
					_t = _t.getNextSibling();
					}
					break;
				}
				default:
				{
					if ( _cnt234>=1 ) { break _loop234; } else {throw new NoViableAltException(_t);}
				}
				}
				_cnt234++;
			} while (true);
			}
			break;
		}
		default:
		{
			throw new NoViableAltException(_t);
		}
		}
		}
		currentAST = __currentAST221;
		_t = __t221;
		_t = _t.getNextSibling();
		nonemptyAbstractDeclarator_AST = (Block)currentAST.root;
		returnAST = nonemptyAbstractDeclarator_AST;
		_retTree = _t;
	}
	
	public final void functionDeclSpecifiers(AST _t) throws RecognitionException {
		
		Block functionDeclSpecifiers_AST_in = (_t == ASTNULL) ? null : (Block)_t;
		returnAST = null;
		ASTPair currentAST = new ASTPair();
		Block functionDeclSpecifiers_AST = null;
		
		AST __t119 = _t;
		Block tmp83_AST = null;
		Block tmp83_AST_in = null;
		tmp83_AST = (Block)astFactory.create((Block)_t);
		tmp83_AST_in = (Block)_t;
		astFactory.addASTChild(currentAST, tmp83_AST);
		ASTPair __currentAST119 = currentAST.copy();
		currentAST.root = currentAST.child;
		currentAST.child = null;
		match(_t,NDeclSpecifiers);
		_t = _t.getFirstChild();
		{
		int _cnt121=0;
		_loop121:
		do {
			if (_t==null) _t=ASTNULL;
			switch ( _t.getType()) {
			case LITERAL_extern:
			case LITERAL_static:
			case LITERAL___inline:
			{
				functionStorageClassSpecifier(_t);
				_t = _retTree;
				astFactory.addASTChild(currentAST, returnAST);
				break;
			}
			case LITERAL_const:
			case LITERAL_volatile:
			{
				typeQualifier(_t);
				_t = _retTree;
				astFactory.addASTChild(currentAST, returnAST);
				break;
			}
			case LITERAL_struct:
			case LITERAL_union:
			case LITERAL_enum:
			case LITERAL_void:
			case LITERAL_char:
			case LITERAL_short:
			case LITERAL_int:
			case LITERAL_long:
			case LITERAL_float:
			case LITERAL_double:
			case LITERAL_signed:
			case LITERAL_unsigned:
			case NTypedefName:
			case LITERAL_typeof:
			case LITERAL___complex:
			{
				typeSpecifier(_t);
				_t = _retTree;
				astFactory.addASTChild(currentAST, returnAST);
				break;
			}
			case NPointerGroup:
			{
				pointerGroup(_t);
				_t = _retTree;
				astFactory.addASTChild(currentAST, returnAST);
				break;
			}
			default:
			{
				if ( _cnt121>=1 ) { break _loop121; } else {throw new NoViableAltException(_t);}
			}
			}
			_cnt121++;
		} while (true);
		}
		currentAST = __currentAST119;
		_t = __t119;
		_t = _t.getNextSibling();
		functionDeclSpecifiers_AST = (Block)currentAST.root;
		returnAST = functionDeclSpecifiers_AST;
		_retTree = _t;
	}
	
	public final void scope(AST _t) throws RecognitionException {
		
		Block scope_AST_in = (_t == ASTNULL) ? null : (Block)_t;
		returnAST = null;
		ASTPair currentAST = new ASTPair();
		Block scope_AST = null;
		Block od_AST = null;
		Block od = null;
		Block os_AST = null;
		Block os = null;
		
		AST __t130 = _t;
		Block tmp84_AST = null;
		Block tmp84_AST_in = null;
		tmp84_AST = (Block)astFactory.create((Block)_t);
		tmp84_AST_in = (Block)_t;
		ASTPair __currentAST130 = currentAST.copy();
		currentAST.root = currentAST.child;
		currentAST.child = null;
		match(_t,NScope);
		_t = _t.getFirstChild();
		od = _t==ASTNULL ? null : (Block)_t;
		optDeclarations(_t);
		_t = _retTree;
		od_AST = (Block)returnAST;
		os = _t==ASTNULL ? null : (Block)_t;
		optStatements(_t);
		_t = _retTree;
		os_AST = (Block)returnAST;
		currentAST = __currentAST130;
		_t = __t130;
		_t = _t.getNextSibling();
		if ( inputState.guessing==0 ) {
			scope_AST = (Block)currentAST.root;
			
			Block b = new Block (BlockType.SCOPE);
			Block mergedOS = Block.mergeCopy ((Block) os_AST);
			scope_AST = (Block)astFactory.make( (new ASTArray(3)).add(b).add(od_AST).add(mergedOS));
			
			currentAST.root = scope_AST;
			currentAST.child = scope_AST!=null &&scope_AST.getFirstChild()!=null ?
				scope_AST.getFirstChild() : scope_AST;
			currentAST.advanceChildToEnd();
		}
		returnAST = scope_AST;
		_retTree = _t;
	}
	
	public final void declarationList(AST _t) throws RecognitionException {
		
		Block declarationList_AST_in = (_t == ASTNULL) ? null : (Block)_t;
		returnAST = null;
		ASTPair currentAST = new ASTPair();
		Block declarationList_AST = null;
		
		{
		int _cnt124=0;
		_loop124:
		do {
			if (_t==null) _t=ASTNULL;
			switch ( _t.getType()) {
			case LITERAL___label__:
			{
				localLabelDecl(_t);
				_t = _retTree;
				astFactory.addASTChild(currentAST, returnAST);
				break;
			}
			case NDeclaration:
			{
				declaration(_t);
				_t = _retTree;
				astFactory.addASTChild(currentAST, returnAST);
				break;
			}
			default:
			{
				if ( _cnt124>=1 ) { break _loop124; } else {throw new NoViableAltException(_t);}
			}
			}
			_cnt124++;
		} while (true);
		}
		declarationList_AST = (Block)currentAST.root;
		returnAST = declarationList_AST;
		_retTree = _t;
	}
	
	public final void localLabelDecl(AST _t) throws RecognitionException {
		
		Block localLabelDecl_AST_in = (_t == ASTNULL) ? null : (Block)_t;
		returnAST = null;
		ASTPair currentAST = new ASTPair();
		Block localLabelDecl_AST = null;
		
		AST __t126 = _t;
		Block tmp85_AST = null;
		Block tmp85_AST_in = null;
		tmp85_AST = (Block)astFactory.create((Block)_t);
		tmp85_AST_in = (Block)_t;
		astFactory.addASTChild(currentAST, tmp85_AST);
		ASTPair __currentAST126 = currentAST.copy();
		currentAST.root = currentAST.child;
		currentAST.child = null;
		match(_t,LITERAL___label__);
		_t = _t.getFirstChild();
		{
		int _cnt128=0;
		_loop128:
		do {
			if (_t==null) _t=ASTNULL;
			if ((_t.getType()==ID)) {
				Block tmp86_AST = null;
				Block tmp86_AST_in = null;
				tmp86_AST = (Block)astFactory.create((Block)_t);
				tmp86_AST_in = (Block)_t;
				astFactory.addASTChild(currentAST, tmp86_AST);
				match(_t,ID);
				_t = _t.getNextSibling();
			}
			else {
				if ( _cnt128>=1 ) { break _loop128; } else {throw new NoViableAltException(_t);}
			}
			
			_cnt128++;
		} while (true);
		}
		currentAST = __currentAST126;
		_t = __t126;
		_t = _t.getNextSibling();
		localLabelDecl_AST = (Block)currentAST.root;
		returnAST = localLabelDecl_AST;
		_retTree = _t;
	}
	
	public final void optDeclarations(AST _t) throws RecognitionException {
		
		Block optDeclarations_AST_in = (_t == ASTNULL) ? null : (Block)_t;
		returnAST = null;
		ASTPair currentAST = new ASTPair();
		Block optDeclarations_AST = null;
		
		AST __t132 = _t;
		Block tmp87_AST = null;
		Block tmp87_AST_in = null;
		tmp87_AST = (Block)astFactory.create((Block)_t);
		tmp87_AST_in = (Block)_t;
		astFactory.addASTChild(currentAST, tmp87_AST);
		ASTPair __currentAST132 = currentAST.copy();
		currentAST.root = currentAST.child;
		currentAST.child = null;
		match(_t,NLocalDeclarations);
		_t = _t.getFirstChild();
		{
		if (_t==null) _t=ASTNULL;
		switch ( _t.getType()) {
		case NDeclaration:
		case LITERAL___label__:
		{
			declarationList(_t);
			_t = _retTree;
			astFactory.addASTChild(currentAST, returnAST);
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
		currentAST = __currentAST132;
		_t = __t132;
		_t = _t.getNextSibling();
		optDeclarations_AST = (Block)currentAST.root;
		returnAST = optDeclarations_AST;
		_retTree = _t;
	}
	
	public final void optStatements(AST _t) throws RecognitionException {
		
		Block optStatements_AST_in = (_t == ASTNULL) ? null : (Block)_t;
		returnAST = null;
		ASTPair currentAST = new ASTPair();
		Block optStatements_AST = null;
		
		{
		if (_t==null) _t=ASTNULL;
		switch ( _t.getType()) {
		case NFunctionCallStmt:
		case NFunctionCallAssignStmt:
		case NAssignStmt:
		case NImplicitAssignStmt:
		case NWhile:
		case NIf:
		case NScope:
		case NReturn:
		case NGoto:
		case NBreak:
		case NLabelledStmt:
		case NNDGoto:
		{
			statementList(_t);
			_t = _retTree;
			astFactory.addASTChild(currentAST, returnAST);
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
		optStatements_AST = (Block)currentAST.root;
		returnAST = optStatements_AST;
		_retTree = _t;
	}
	
	public final void statementList(AST _t) throws RecognitionException {
		
		Block statementList_AST_in = (_t == ASTNULL) ? null : (Block)_t;
		returnAST = null;
		ASTPair currentAST = new ASTPair();
		Block statementList_AST = null;
		
		{
		int _cnt138=0;
		_loop138:
		do {
			if (_t==null) _t=ASTNULL;
			if ((_tokenSet_1.member(_t.getType()))) {
				stmt(_t);
				_t = _retTree;
				astFactory.addASTChild(currentAST, returnAST);
			}
			else {
				if ( _cnt138>=1 ) { break _loop138; } else {throw new NoViableAltException(_t);}
			}
			
			_cnt138++;
		} while (true);
		}
		statementList_AST = (Block)currentAST.root;
		returnAST = statementList_AST;
		_retTree = _t;
	}
	
	public final void stmt(AST _t) throws RecognitionException {
		
		Block stmt_AST_in = (_t == ASTNULL) ? null : (Block)_t;
		returnAST = null;
		ASTPair currentAST = new ASTPair();
		Block stmt_AST = null;
		
		if (_t==null) _t=ASTNULL;
		switch ( _t.getType()) {
		case NAssignStmt:
		case NImplicitAssignStmt:
		{
			assignStmt(_t);
			_t = _retTree;
			astFactory.addASTChild(currentAST, returnAST);
			if ( inputState.guessing==0 ) {
				stmt_AST = (Block)currentAST.root;
				
				Block b = new Block (BlockType.STATEMENT_LIST);
				stmt_AST = (Block)astFactory.make( (new ASTArray(2)).add(b).add(stmt_AST));
				
				currentAST.root = stmt_AST;
				currentAST.child = stmt_AST!=null &&stmt_AST.getFirstChild()!=null ?
					stmt_AST.getFirstChild() : stmt_AST;
				currentAST.advanceChildToEnd();
			}
			stmt_AST = (Block)currentAST.root;
			break;
		}
		case NFunctionCallAssignStmt:
		{
			functionCallAssignStmt(_t);
			_t = _retTree;
			astFactory.addASTChild(currentAST, returnAST);
			stmt_AST = (Block)currentAST.root;
			break;
		}
		case NFunctionCallStmt:
		{
			functionCallStmt(_t);
			_t = _retTree;
			astFactory.addASTChild(currentAST, returnAST);
			stmt_AST = (Block)currentAST.root;
			break;
		}
		case NLabelledStmt:
		{
			labelledStmt(_t);
			_t = _retTree;
			astFactory.addASTChild(currentAST, returnAST);
			stmt_AST = (Block)currentAST.root;
			break;
		}
		case NWhile:
		case NIf:
		case NReturn:
		case NGoto:
		case NBreak:
		case NNDGoto:
		{
			controlStmt(_t);
			_t = _retTree;
			astFactory.addASTChild(currentAST, returnAST);
			stmt_AST = (Block)currentAST.root;
			break;
		}
		case NScope:
		{
			scope(_t);
			_t = _retTree;
			astFactory.addASTChild(currentAST, returnAST);
			stmt_AST = (Block)currentAST.root;
			break;
		}
		default:
		{
			throw new NoViableAltException(_t);
		}
		}
		returnAST = stmt_AST;
		_retTree = _t;
	}
	
	public final void assignStmt(AST _t) throws RecognitionException {
		
		Block assignStmt_AST_in = (_t == ASTNULL) ? null : (Block)_t;
		returnAST = null;
		ASTPair currentAST = new ASTPair();
		Block assignStmt_AST = null;
		
		if (_t==null) _t=ASTNULL;
		switch ( _t.getType()) {
		case NAssignStmt:
		{
			AST __t157 = _t;
			Block tmp88_AST = null;
			Block tmp88_AST_in = null;
			tmp88_AST = (Block)astFactory.create((Block)_t);
			tmp88_AST_in = (Block)_t;
			astFactory.addASTChild(currentAST, tmp88_AST);
			ASTPair __currentAST157 = currentAST.copy();
			currentAST.root = currentAST.child;
			currentAST.child = null;
			match(_t,NAssignStmt);
			_t = _t.getFirstChild();
			pureExpr(_t);
			_t = _retTree;
			astFactory.addASTChild(currentAST, returnAST);
			pureExpr(_t);
			_t = _retTree;
			astFactory.addASTChild(currentAST, returnAST);
			currentAST = __currentAST157;
			_t = __t157;
			_t = _t.getNextSibling();
			assignStmt_AST = (Block)currentAST.root;
			break;
		}
		case NImplicitAssignStmt:
		{
			AST __t158 = _t;
			Block tmp89_AST = null;
			Block tmp89_AST_in = null;
			tmp89_AST = (Block)astFactory.create((Block)_t);
			tmp89_AST_in = (Block)_t;
			astFactory.addASTChild(currentAST, tmp89_AST);
			ASTPair __currentAST158 = currentAST.copy();
			currentAST.root = currentAST.child;
			currentAST.child = null;
			match(_t,NImplicitAssignStmt);
			_t = _t.getFirstChild();
			lval(_t);
			_t = _retTree;
			astFactory.addASTChild(currentAST, returnAST);
			{
			if (_t==null) _t=ASTNULL;
			switch ( _t.getType()) {
			case INC:
			{
				Block tmp90_AST = null;
				Block tmp90_AST_in = null;
				tmp90_AST = (Block)astFactory.create((Block)_t);
				tmp90_AST_in = (Block)_t;
				astFactory.addASTChild(currentAST, tmp90_AST);
				match(_t,INC);
				_t = _t.getNextSibling();
				break;
			}
			case DEC:
			{
				Block tmp91_AST = null;
				Block tmp91_AST_in = null;
				tmp91_AST = (Block)astFactory.create((Block)_t);
				tmp91_AST_in = (Block)_t;
				astFactory.addASTChild(currentAST, tmp91_AST);
				match(_t,DEC);
				_t = _t.getNextSibling();
				break;
			}
			default:
			{
				throw new NoViableAltException(_t);
			}
			}
			}
			currentAST = __currentAST158;
			_t = __t158;
			_t = _t.getNextSibling();
			assignStmt_AST = (Block)currentAST.root;
			break;
		}
		default:
		{
			throw new NoViableAltException(_t);
		}
		}
		returnAST = assignStmt_AST;
		_retTree = _t;
	}
	
	public final void functionCallAssignStmt(AST _t) throws RecognitionException {
		
		Block functionCallAssignStmt_AST_in = (_t == ASTNULL) ? null : (Block)_t;
		returnAST = null;
		ASTPair currentAST = new ASTPair();
		Block functionCallAssignStmt_AST = null;
		
		AST __t178 = _t;
		Block tmp92_AST = null;
		Block tmp92_AST_in = null;
		tmp92_AST = (Block)astFactory.create((Block)_t);
		tmp92_AST_in = (Block)_t;
		astFactory.addASTChild(currentAST, tmp92_AST);
		ASTPair __currentAST178 = currentAST.copy();
		currentAST.root = currentAST.child;
		currentAST.child = null;
		match(_t,NFunctionCallAssignStmt);
		_t = _t.getFirstChild();
		lval(_t);
		_t = _retTree;
		astFactory.addASTChild(currentAST, returnAST);
		functionCall(_t);
		_t = _retTree;
		astFactory.addASTChild(currentAST, returnAST);
		currentAST = __currentAST178;
		_t = __t178;
		_t = _t.getNextSibling();
		if ( inputState.guessing==0 ) {
			functionCallAssignStmt_AST = (Block)currentAST.root;
			
			Block b = new Block (BlockType.FUNCTION_CALL);
			functionCallAssignStmt_AST = (Block)astFactory.make( (new ASTArray(2)).add(b).add(functionCallAssignStmt_AST));
			
			currentAST.root = functionCallAssignStmt_AST;
			currentAST.child = functionCallAssignStmt_AST!=null &&functionCallAssignStmt_AST.getFirstChild()!=null ?
				functionCallAssignStmt_AST.getFirstChild() : functionCallAssignStmt_AST;
			currentAST.advanceChildToEnd();
		}
		functionCallAssignStmt_AST = (Block)currentAST.root;
		returnAST = functionCallAssignStmt_AST;
		_retTree = _t;
	}
	
	public final void functionCallStmt(AST _t) throws RecognitionException {
		
		Block functionCallStmt_AST_in = (_t == ASTNULL) ? null : (Block)_t;
		returnAST = null;
		ASTPair currentAST = new ASTPair();
		Block functionCallStmt_AST = null;
		
		AST __t176 = _t;
		Block tmp93_AST = null;
		Block tmp93_AST_in = null;
		tmp93_AST = (Block)astFactory.create((Block)_t);
		tmp93_AST_in = (Block)_t;
		astFactory.addASTChild(currentAST, tmp93_AST);
		ASTPair __currentAST176 = currentAST.copy();
		currentAST.root = currentAST.child;
		currentAST.child = null;
		match(_t,NFunctionCallStmt);
		_t = _t.getFirstChild();
		functionCall(_t);
		_t = _retTree;
		astFactory.addASTChild(currentAST, returnAST);
		currentAST = __currentAST176;
		_t = __t176;
		_t = _t.getNextSibling();
		if ( inputState.guessing==0 ) {
			functionCallStmt_AST = (Block)currentAST.root;
			
			Block b = new Block (BlockType.FUNCTION_CALL);
			functionCallStmt_AST = (Block)astFactory.make( (new ASTArray(2)).add(b).add(functionCallStmt_AST));
			
			currentAST.root = functionCallStmt_AST;
			currentAST.child = functionCallStmt_AST!=null &&functionCallStmt_AST.getFirstChild()!=null ?
				functionCallStmt_AST.getFirstChild() : functionCallStmt_AST;
			currentAST.advanceChildToEnd();
		}
		functionCallStmt_AST = (Block)currentAST.root;
		returnAST = functionCallStmt_AST;
		_retTree = _t;
	}
	
	public final void labelledStmt(AST _t) throws RecognitionException {
		
		Block labelledStmt_AST_in = (_t == ASTNULL) ? null : (Block)_t;
		returnAST = null;
		ASTPair currentAST = new ASTPair();
		Block labelledStmt_AST = null;
		Block id = null;
		Block id_AST = null;
		Block s_AST = null;
		Block s = null;
		
		AST __t161 = _t;
		Block tmp94_AST = null;
		Block tmp94_AST_in = null;
		tmp94_AST = (Block)astFactory.create((Block)_t);
		tmp94_AST_in = (Block)_t;
		astFactory.addASTChild(currentAST, tmp94_AST);
		ASTPair __currentAST161 = currentAST.copy();
		currentAST.root = currentAST.child;
		currentAST.child = null;
		match(_t,NLabelledStmt);
		_t = _t.getFirstChild();
		AST __t162 = _t;
		Block tmp95_AST = null;
		Block tmp95_AST_in = null;
		tmp95_AST = (Block)astFactory.create((Block)_t);
		tmp95_AST_in = (Block)_t;
		astFactory.addASTChild(currentAST, tmp95_AST);
		ASTPair __currentAST162 = currentAST.copy();
		currentAST.root = currentAST.child;
		currentAST.child = null;
		match(_t,NLabel);
		_t = _t.getFirstChild();
		id = (Block)_t;
		Block id_AST_in = null;
		id_AST = (Block)astFactory.create(id);
		astFactory.addASTChild(currentAST, id_AST);
		match(_t,ID);
		_t = _t.getNextSibling();
		currentAST = __currentAST162;
		_t = __t162;
		_t = _t.getNextSibling();
		{
		if (_t==null) _t=ASTNULL;
		switch ( _t.getType()) {
		case NFunctionCallStmt:
		case NFunctionCallAssignStmt:
		case NAssignStmt:
		case NImplicitAssignStmt:
		case NWhile:
		case NIf:
		case NScope:
		case NReturn:
		case NGoto:
		case NBreak:
		case NLabelledStmt:
		case NNDGoto:
		{
			s = _t==ASTNULL ? null : (Block)_t;
			stmt(_t);
			_t = _retTree;
			s_AST = (Block)returnAST;
			astFactory.addASTChild(currentAST, returnAST);
			if ( inputState.guessing==0 ) {
				s_AST.setLabel (id.getText ());
			}
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
		currentAST = __currentAST161;
		_t = __t161;
		_t = _t.getNextSibling();
		if ( inputState.guessing==0 ) {
			labelledStmt_AST = (Block)currentAST.root;
			
			labelledStmt_AST = (Block)astFactory.make( (new ASTArray(3)).add((Block)astFactory.create(NBlock)).add((Block)astFactory.make( (new ASTArray(2)).add((Block)astFactory.create(tmp95_AST)).add(id_AST))).add(s_AST));
			labelledStmt_AST.setBlockType (BlockType.LABELLED_STATEMENT);
			labelledStmt_AST.setLabel (id.getText ());
			
			currentAST.root = labelledStmt_AST;
			currentAST.child = labelledStmt_AST!=null &&labelledStmt_AST.getFirstChild()!=null ?
				labelledStmt_AST.getFirstChild() : labelledStmt_AST;
			currentAST.advanceChildToEnd();
		}
		labelledStmt_AST = (Block)currentAST.root;
		returnAST = labelledStmt_AST;
		_retTree = _t;
	}
	
	public final void controlStmt(AST _t) throws RecognitionException {
		
		Block controlStmt_AST_in = (_t == ASTNULL) ? null : (Block)_t;
		returnAST = null;
		ASTPair currentAST = new ASTPair();
		Block controlStmt_AST = null;
		Block whileExpr_AST = null;
		Block whileExpr = null;
		Block whileStmt_AST = null;
		Block whileStmt = null;
		Block id = null;
		Block id_AST = null;
		Block r = null;
		Block r_AST = null;
		Block retExpr_AST = null;
		Block retExpr = null;
		Block ifExpr_AST = null;
		Block ifExpr = null;
		Block ifStmt1_AST = null;
		Block ifStmt1 = null;
		Block ifStmt2_AST = null;
		Block ifStmt2 = null;
		Block list_AST = null;
		Block list = null;
		
		if (_t==null) _t=ASTNULL;
		switch ( _t.getType()) {
		case NWhile:
		{
			AST __t165 = _t;
			Block tmp96_AST = null;
			Block tmp96_AST_in = null;
			tmp96_AST = (Block)astFactory.create((Block)_t);
			tmp96_AST_in = (Block)_t;
			ASTPair __currentAST165 = currentAST.copy();
			currentAST.root = currentAST.child;
			currentAST.child = null;
			match(_t,NWhile);
			_t = _t.getFirstChild();
			whileExpr = _t==ASTNULL ? null : (Block)_t;
			pureExpr(_t);
			_t = _retTree;
			whileExpr_AST = (Block)returnAST;
			whileStmt = _t==ASTNULL ? null : (Block)_t;
			stmt(_t);
			_t = _retTree;
			whileStmt_AST = (Block)returnAST;
			currentAST = __currentAST165;
			_t = __t165;
			_t = _t.getNextSibling();
			if ( inputState.guessing==0 ) {
				controlStmt_AST = (Block)currentAST.root;
				
				Block b = new Block (BlockType.WHILE);
				controlStmt_AST = (Block)astFactory.make( (new ASTArray(3)).add(b).add(whileExpr_AST).add(whileStmt_AST));
				
				currentAST.root = controlStmt_AST;
				currentAST.child = controlStmt_AST!=null &&controlStmt_AST.getFirstChild()!=null ?
					controlStmt_AST.getFirstChild() : controlStmt_AST;
				currentAST.advanceChildToEnd();
			}
			break;
		}
		case NGoto:
		{
			AST __t166 = _t;
			Block tmp97_AST = null;
			Block tmp97_AST_in = null;
			tmp97_AST = (Block)astFactory.create((Block)_t);
			tmp97_AST_in = (Block)_t;
			ASTPair __currentAST166 = currentAST.copy();
			currentAST.root = currentAST.child;
			currentAST.child = null;
			match(_t,NGoto);
			_t = _t.getFirstChild();
			id = (Block)_t;
			Block id_AST_in = null;
			id_AST = (Block)astFactory.create(id);
			match(_t,ID);
			_t = _t.getNextSibling();
			currentAST = __currentAST166;
			_t = __t166;
			_t = _t.getNextSibling();
			if ( inputState.guessing==0 ) {
				controlStmt_AST = (Block)currentAST.root;
				
				Block b = new Block (BlockType.GOTO);
				controlStmt_AST = (Block)astFactory.make( (new ASTArray(2)).add(b).add(id_AST));
				
				currentAST.root = controlStmt_AST;
				currentAST.child = controlStmt_AST!=null &&controlStmt_AST.getFirstChild()!=null ?
					controlStmt_AST.getFirstChild() : controlStmt_AST;
				currentAST.advanceChildToEnd();
			}
			break;
		}
		case NBreak:
		{
			Block tmp98_AST = null;
			Block tmp98_AST_in = null;
			tmp98_AST = (Block)astFactory.create((Block)_t);
			tmp98_AST_in = (Block)_t;
			match(_t,NBreak);
			_t = _t.getNextSibling();
			if ( inputState.guessing==0 ) {
				controlStmt_AST = (Block)currentAST.root;
				controlStmt_AST = new Block (BlockType.BREAK);
				currentAST.root = controlStmt_AST;
				currentAST.child = controlStmt_AST!=null &&controlStmt_AST.getFirstChild()!=null ?
					controlStmt_AST.getFirstChild() : controlStmt_AST;
				currentAST.advanceChildToEnd();
			}
			break;
		}
		case NReturn:
		{
			AST __t167 = _t;
			r = _t==ASTNULL ? null :(Block)_t;
			Block r_AST_in = null;
			r_AST = (Block)astFactory.create(r);
			ASTPair __currentAST167 = currentAST.copy();
			currentAST.root = currentAST.child;
			currentAST.child = null;
			match(_t,NReturn);
			_t = _t.getFirstChild();
			{
			if (_t==null) _t=ASTNULL;
			switch ( _t.getType()) {
			case ID:
			case STAR:
			case PTR:
			case DOT:
			case LOR:
			case LAND:
			case BOR:
			case BXOR:
			case BAND:
			case EQUAL:
			case NOT_EQUAL:
			case LT:
			case LTE:
			case GT:
			case GTE:
			case LSHIFT:
			case RSHIFT:
			case PLUS:
			case MINUS:
			case DIV:
			case MOD:
			case LITERAL_sizeof:
			case CharLiteral:
			case IntOctalConst:
			case LongOctalConst:
			case UnsignedOctalConst:
			case IntIntConst:
			case LongIntConst:
			case UnsignedIntConst:
			case IntHexConst:
			case LongHexConst:
			case UnsignedHexConst:
			case FloatDoubleConst:
			case DoubleDoubleConst:
			case LongDoubleConst:
			case NCast:
			case NUnaryExpr:
			case NPostfixExpr:
			case NStringSeq:
			case NPureExpressionGroup:
			case NArrayElement:
			case LITERAL___alignof:
			{
				retExpr = _t==ASTNULL ? null : (Block)_t;
				pureExpr(_t);
				_t = _retTree;
				retExpr_AST = (Block)returnAST;
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
			currentAST = __currentAST167;
			_t = __t167;
			_t = _t.getNextSibling();
			if ( inputState.guessing==0 ) {
				controlStmt_AST = (Block)currentAST.root;
				
				Block b = new Block (BlockType.RETURN);
				b.setLineNum (r_AST.getLineNum ());
				controlStmt_AST = (Block)astFactory.make( (new ASTArray(2)).add(b).add(retExpr_AST));
				
				currentAST.root = controlStmt_AST;
				currentAST.child = controlStmt_AST!=null &&controlStmt_AST.getFirstChild()!=null ?
					controlStmt_AST.getFirstChild() : controlStmt_AST;
				currentAST.advanceChildToEnd();
			}
			break;
		}
		case NIf:
		{
			AST __t169 = _t;
			Block tmp99_AST = null;
			Block tmp99_AST_in = null;
			tmp99_AST = (Block)astFactory.create((Block)_t);
			tmp99_AST_in = (Block)_t;
			ASTPair __currentAST169 = currentAST.copy();
			currentAST.root = currentAST.child;
			currentAST.child = null;
			match(_t,NIf);
			_t = _t.getFirstChild();
			ifExpr = _t==ASTNULL ? null : (Block)_t;
			pureExpr(_t);
			_t = _retTree;
			ifExpr_AST = (Block)returnAST;
			ifStmt1 = _t==ASTNULL ? null : (Block)_t;
			stmt(_t);
			_t = _retTree;
			ifStmt1_AST = (Block)returnAST;
			{
			if (_t==null) _t=ASTNULL;
			switch ( _t.getType()) {
			case NFunctionCallStmt:
			case NFunctionCallAssignStmt:
			case NAssignStmt:
			case NImplicitAssignStmt:
			case NWhile:
			case NIf:
			case NScope:
			case NReturn:
			case NGoto:
			case NBreak:
			case NLabelledStmt:
			case NNDGoto:
			{
				ifStmt2 = _t==ASTNULL ? null : (Block)_t;
				stmt(_t);
				_t = _retTree;
				ifStmt2_AST = (Block)returnAST;
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
			currentAST = __currentAST169;
			_t = __t169;
			_t = _t.getNextSibling();
			if ( inputState.guessing==0 ) {
				controlStmt_AST = (Block)currentAST.root;
				
				Block b = new Block (BlockType.IF);
				controlStmt_AST = (Block)astFactory.make( (new ASTArray(4)).add(b).add(ifExpr_AST).add(ifStmt1_AST).add(ifStmt2_AST));
				
				currentAST.root = controlStmt_AST;
				currentAST.child = controlStmt_AST!=null &&controlStmt_AST.getFirstChild()!=null ?
					controlStmt_AST.getFirstChild() : controlStmt_AST;
				currentAST.advanceChildToEnd();
			}
			break;
		}
		case NNDGoto:
		{
			AST __t171 = _t;
			Block tmp100_AST = null;
			Block tmp100_AST_in = null;
			tmp100_AST = (Block)astFactory.create((Block)_t);
			tmp100_AST_in = (Block)_t;
			ASTPair __currentAST171 = currentAST.copy();
			currentAST.root = currentAST.child;
			currentAST.child = null;
			match(_t,NNDGoto);
			_t = _t.getFirstChild();
			list = _t==ASTNULL ? null : (Block)_t;
			ndGotoStringList(_t);
			_t = _retTree;
			list_AST = (Block)returnAST;
			currentAST = __currentAST171;
			_t = __t171;
			_t = _t.getNextSibling();
			if ( inputState.guessing==0 ) {
				controlStmt_AST = (Block)currentAST.root;
				
				Block b = new Block (BlockType.NDGOTO);
				controlStmt_AST = (Block)astFactory.make( (new ASTArray(2)).add(b).add(list_AST));
				
				currentAST.root = controlStmt_AST;
				currentAST.child = controlStmt_AST!=null &&controlStmt_AST.getFirstChild()!=null ?
					controlStmt_AST.getFirstChild() : controlStmt_AST;
				currentAST.advanceChildToEnd();
			}
			break;
		}
		default:
		{
			throw new NoViableAltException(_t);
		}
		}
		returnAST = controlStmt_AST;
		_retTree = _t;
	}
	
	public final void gnuAsmExpr(AST _t) throws RecognitionException {
		
		Block gnuAsmExpr_AST_in = (_t == ASTNULL) ? null : (Block)_t;
		returnAST = null;
		ASTPair currentAST = new ASTPair();
		Block gnuAsmExpr_AST = null;
		
		AST __t141 = _t;
		Block tmp101_AST = null;
		Block tmp101_AST_in = null;
		tmp101_AST = (Block)astFactory.create((Block)_t);
		tmp101_AST_in = (Block)_t;
		astFactory.addASTChild(currentAST, tmp101_AST);
		ASTPair __currentAST141 = currentAST.copy();
		currentAST.root = currentAST.child;
		currentAST.child = null;
		match(_t,NGnuAsmExpr);
		_t = _t.getFirstChild();
		{
		if (_t==null) _t=ASTNULL;
		switch ( _t.getType()) {
		case LITERAL_volatile:
		{
			Block tmp102_AST = null;
			Block tmp102_AST_in = null;
			tmp102_AST = (Block)astFactory.create((Block)_t);
			tmp102_AST_in = (Block)_t;
			astFactory.addASTChild(currentAST, tmp102_AST);
			match(_t,LITERAL_volatile);
			_t = _t.getNextSibling();
			break;
		}
		case LPAREN:
		{
			break;
		}
		default:
		{
			throw new NoViableAltException(_t);
		}
		}
		}
		Block tmp103_AST = null;
		Block tmp103_AST_in = null;
		tmp103_AST = (Block)astFactory.create((Block)_t);
		tmp103_AST_in = (Block)_t;
		astFactory.addASTChild(currentAST, tmp103_AST);
		match(_t,LPAREN);
		_t = _t.getNextSibling();
		stringConst(_t);
		_t = _retTree;
		astFactory.addASTChild(currentAST, returnAST);
		{
		if (_t==null) _t=ASTNULL;
		if ((_t.getType()==COLON)) {
			Block tmp104_AST = null;
			Block tmp104_AST_in = null;
			tmp104_AST = (Block)astFactory.create((Block)_t);
			tmp104_AST_in = (Block)_t;
			astFactory.addASTChild(currentAST, tmp104_AST);
			match(_t,COLON);
			_t = _t.getNextSibling();
			{
			if (_t==null) _t=ASTNULL;
			switch ( _t.getType()) {
			case NStringSeq:
			{
				strOptExprPair(_t);
				_t = _retTree;
				astFactory.addASTChild(currentAST, returnAST);
				{
				_loop146:
				do {
					if (_t==null) _t=ASTNULL;
					if ((_t.getType()==COMMA)) {
						Block tmp105_AST = null;
						Block tmp105_AST_in = null;
						tmp105_AST = (Block)astFactory.create((Block)_t);
						tmp105_AST_in = (Block)_t;
						astFactory.addASTChild(currentAST, tmp105_AST);
						match(_t,COMMA);
						_t = _t.getNextSibling();
						strOptExprPair(_t);
						_t = _retTree;
						astFactory.addASTChild(currentAST, returnAST);
					}
					else {
						break _loop146;
					}
					
				} while (true);
				}
				break;
			}
			case COLON:
			case RPAREN:
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
			if ((_t.getType()==COLON)) {
				Block tmp106_AST = null;
				Block tmp106_AST_in = null;
				tmp106_AST = (Block)astFactory.create((Block)_t);
				tmp106_AST_in = (Block)_t;
				astFactory.addASTChild(currentAST, tmp106_AST);
				match(_t,COLON);
				_t = _t.getNextSibling();
				{
				if (_t==null) _t=ASTNULL;
				switch ( _t.getType()) {
				case NStringSeq:
				{
					strOptExprPair(_t);
					_t = _retTree;
					astFactory.addASTChild(currentAST, returnAST);
					{
					_loop150:
					do {
						if (_t==null) _t=ASTNULL;
						if ((_t.getType()==COMMA)) {
							Block tmp107_AST = null;
							Block tmp107_AST_in = null;
							tmp107_AST = (Block)astFactory.create((Block)_t);
							tmp107_AST_in = (Block)_t;
							astFactory.addASTChild(currentAST, tmp107_AST);
							match(_t,COMMA);
							_t = _t.getNextSibling();
							strOptExprPair(_t);
							_t = _retTree;
							astFactory.addASTChild(currentAST, returnAST);
						}
						else {
							break _loop150;
						}
						
					} while (true);
					}
					break;
				}
				case COLON:
				case RPAREN:
				{
					break;
				}
				default:
				{
					throw new NoViableAltException(_t);
				}
				}
				}
			}
			else if ((_t.getType()==COLON||_t.getType()==RPAREN)) {
			}
			else {
				throw new NoViableAltException(_t);
			}
			
			}
		}
		else if ((_t.getType()==COLON||_t.getType()==RPAREN)) {
		}
		else {
			throw new NoViableAltException(_t);
		}
		
		}
		{
		if (_t==null) _t=ASTNULL;
		switch ( _t.getType()) {
		case COLON:
		{
			Block tmp108_AST = null;
			Block tmp108_AST_in = null;
			tmp108_AST = (Block)astFactory.create((Block)_t);
			tmp108_AST_in = (Block)_t;
			astFactory.addASTChild(currentAST, tmp108_AST);
			match(_t,COLON);
			_t = _t.getNextSibling();
			stringConst(_t);
			_t = _retTree;
			astFactory.addASTChild(currentAST, returnAST);
			{
			_loop153:
			do {
				if (_t==null) _t=ASTNULL;
				if ((_t.getType()==COMMA)) {
					Block tmp109_AST = null;
					Block tmp109_AST_in = null;
					tmp109_AST = (Block)astFactory.create((Block)_t);
					tmp109_AST_in = (Block)_t;
					astFactory.addASTChild(currentAST, tmp109_AST);
					match(_t,COMMA);
					_t = _t.getNextSibling();
					stringConst(_t);
					_t = _retTree;
					astFactory.addASTChild(currentAST, returnAST);
				}
				else {
					break _loop153;
				}
				
			} while (true);
			}
			break;
		}
		case RPAREN:
		{
			break;
		}
		default:
		{
			throw new NoViableAltException(_t);
		}
		}
		}
		Block tmp110_AST = null;
		Block tmp110_AST_in = null;
		tmp110_AST = (Block)astFactory.create((Block)_t);
		tmp110_AST_in = (Block)_t;
		astFactory.addASTChild(currentAST, tmp110_AST);
		match(_t,RPAREN);
		_t = _t.getNextSibling();
		currentAST = __currentAST141;
		_t = __t141;
		_t = _t.getNextSibling();
		gnuAsmExpr_AST = (Block)currentAST.root;
		returnAST = gnuAsmExpr_AST;
		_retTree = _t;
	}
	
	protected final void stringConst(AST _t) throws RecognitionException {
		
		Block stringConst_AST_in = (_t == ASTNULL) ? null : (Block)_t;
		returnAST = null;
		ASTPair currentAST = new ASTPair();
		Block stringConst_AST = null;
		
		AST __t259 = _t;
		Block tmp111_AST = null;
		Block tmp111_AST_in = null;
		tmp111_AST = (Block)astFactory.create((Block)_t);
		tmp111_AST_in = (Block)_t;
		astFactory.addASTChild(currentAST, tmp111_AST);
		ASTPair __currentAST259 = currentAST.copy();
		currentAST.root = currentAST.child;
		currentAST.child = null;
		match(_t,NStringSeq);
		_t = _t.getFirstChild();
		{
		int _cnt261=0;
		_loop261:
		do {
			if (_t==null) _t=ASTNULL;
			if ((_t.getType()==StringLiteral)) {
				Block tmp112_AST = null;
				Block tmp112_AST_in = null;
				tmp112_AST = (Block)astFactory.create((Block)_t);
				tmp112_AST_in = (Block)_t;
				astFactory.addASTChild(currentAST, tmp112_AST);
				match(_t,StringLiteral);
				_t = _t.getNextSibling();
			}
			else {
				if ( _cnt261>=1 ) { break _loop261; } else {throw new NoViableAltException(_t);}
			}
			
			_cnt261++;
		} while (true);
		}
		currentAST = __currentAST259;
		_t = __t259;
		_t = _t.getNextSibling();
		stringConst_AST = (Block)currentAST.root;
		returnAST = stringConst_AST;
		_retTree = _t;
	}
	
	public final void strOptExprPair(AST _t) throws RecognitionException {
		
		Block strOptExprPair_AST_in = (_t == ASTNULL) ? null : (Block)_t;
		returnAST = null;
		ASTPair currentAST = new ASTPair();
		Block strOptExprPair_AST = null;
		
		stringConst(_t);
		_t = _retTree;
		astFactory.addASTChild(currentAST, returnAST);
		{
		if (_t==null) _t=ASTNULL;
		switch ( _t.getType()) {
		case LPAREN:
		{
			Block tmp113_AST = null;
			Block tmp113_AST_in = null;
			tmp113_AST = (Block)astFactory.create((Block)_t);
			tmp113_AST_in = (Block)_t;
			astFactory.addASTChild(currentAST, tmp113_AST);
			match(_t,LPAREN);
			_t = _t.getNextSibling();
			pureExpr(_t);
			_t = _retTree;
			astFactory.addASTChild(currentAST, returnAST);
			Block tmp114_AST = null;
			Block tmp114_AST_in = null;
			tmp114_AST = (Block)astFactory.create((Block)_t);
			tmp114_AST_in = (Block)_t;
			astFactory.addASTChild(currentAST, tmp114_AST);
			match(_t,RPAREN);
			_t = _t.getNextSibling();
			break;
		}
		case COMMA:
		case COLON:
		case RPAREN:
		{
			break;
		}
		default:
		{
			throw new NoViableAltException(_t);
		}
		}
		}
		strOptExprPair_AST = (Block)currentAST.root;
		returnAST = strOptExprPair_AST;
		_retTree = _t;
	}
	
	public final void lval(AST _t) throws RecognitionException {
		
		Block lval_AST_in = (_t == ASTNULL) ? null : (Block)_t;
		returnAST = null;
		ASTPair currentAST = new ASTPair();
		Block lval_AST = null;
		
		if (_t==null) _t=ASTNULL;
		switch ( _t.getType()) {
		case PTR:
		{
			AST __t183 = _t;
			Block tmp115_AST = null;
			Block tmp115_AST_in = null;
			tmp115_AST = (Block)astFactory.create((Block)_t);
			tmp115_AST_in = (Block)_t;
			astFactory.addASTChild(currentAST, tmp115_AST);
			ASTPair __currentAST183 = currentAST.copy();
			currentAST.root = currentAST.child;
			currentAST.child = null;
			match(_t,PTR);
			_t = _t.getFirstChild();
			lval(_t);
			_t = _retTree;
			astFactory.addASTChild(currentAST, returnAST);
			lval(_t);
			_t = _retTree;
			astFactory.addASTChild(currentAST, returnAST);
			currentAST = __currentAST183;
			_t = __t183;
			_t = _t.getNextSibling();
			lval_AST = (Block)currentAST.root;
			break;
		}
		case DOT:
		{
			AST __t184 = _t;
			Block tmp116_AST = null;
			Block tmp116_AST_in = null;
			tmp116_AST = (Block)astFactory.create((Block)_t);
			tmp116_AST_in = (Block)_t;
			astFactory.addASTChild(currentAST, tmp116_AST);
			ASTPair __currentAST184 = currentAST.copy();
			currentAST.root = currentAST.child;
			currentAST.child = null;
			match(_t,DOT);
			_t = _t.getFirstChild();
			lval(_t);
			_t = _retTree;
			astFactory.addASTChild(currentAST, returnAST);
			lval(_t);
			_t = _retTree;
			astFactory.addASTChild(currentAST, returnAST);
			currentAST = __currentAST184;
			_t = __t184;
			_t = _t.getNextSibling();
			lval_AST = (Block)currentAST.root;
			break;
		}
		case NArrayElement:
		{
			AST __t185 = _t;
			Block tmp117_AST = null;
			Block tmp117_AST_in = null;
			tmp117_AST = (Block)astFactory.create((Block)_t);
			tmp117_AST_in = (Block)_t;
			astFactory.addASTChild(currentAST, tmp117_AST);
			ASTPair __currentAST185 = currentAST.copy();
			currentAST.root = currentAST.child;
			currentAST.child = null;
			match(_t,NArrayElement);
			_t = _t.getFirstChild();
			Block tmp118_AST = null;
			Block tmp118_AST_in = null;
			tmp118_AST = (Block)astFactory.create((Block)_t);
			tmp118_AST_in = (Block)_t;
			astFactory.addASTChild(currentAST, tmp118_AST);
			match(_t,ID);
			_t = _t.getNextSibling();
			{
			int _cnt187=0;
			_loop187:
			do {
				if (_t==null) _t=ASTNULL;
				if ((_t.getType()==NArrayIndex)) {
					arraySuffix(_t);
					_t = _retTree;
					astFactory.addASTChild(currentAST, returnAST);
				}
				else {
					if ( _cnt187>=1 ) { break _loop187; } else {throw new NoViableAltException(_t);}
				}
				
				_cnt187++;
			} while (true);
			}
			currentAST = __currentAST185;
			_t = __t185;
			_t = _t.getNextSibling();
			lval_AST = (Block)currentAST.root;
			break;
		}
		case ID:
		{
			Block tmp119_AST = null;
			Block tmp119_AST_in = null;
			tmp119_AST = (Block)astFactory.create((Block)_t);
			tmp119_AST_in = (Block)_t;
			astFactory.addASTChild(currentAST, tmp119_AST);
			match(_t,ID);
			_t = _t.getNextSibling();
			lval_AST = (Block)currentAST.root;
			break;
		}
		default:
		{
			throw new NoViableAltException(_t);
		}
		}
		returnAST = lval_AST;
		_retTree = _t;
	}
	
	public final void ndGotoStringList(AST _t) throws RecognitionException {
		
		Block ndGotoStringList_AST_in = (_t == ASTNULL) ? null : (Block)_t;
		returnAST = null;
		ASTPair currentAST = new ASTPair();
		Block ndGotoStringList_AST = null;
		
		{
		int _cnt174=0;
		_loop174:
		do {
			if (_t==null) _t=ASTNULL;
			if ((_t.getType()==StringLiteral)) {
				Block tmp120_AST = null;
				Block tmp120_AST_in = null;
				tmp120_AST = (Block)astFactory.create((Block)_t);
				tmp120_AST_in = (Block)_t;
				astFactory.addASTChild(currentAST, tmp120_AST);
				match(_t,StringLiteral);
				_t = _t.getNextSibling();
			}
			else {
				if ( _cnt174>=1 ) { break _loop174; } else {throw new NoViableAltException(_t);}
			}
			
			_cnt174++;
		} while (true);
		}
		ndGotoStringList_AST = (Block)currentAST.root;
		returnAST = ndGotoStringList_AST;
		_retTree = _t;
	}
	
	public final void functionCall(AST _t) throws RecognitionException {
		
		Block functionCall_AST_in = (_t == ASTNULL) ? null : (Block)_t;
		returnAST = null;
		ASTPair currentAST = new ASTPair();
		Block functionCall_AST = null;
		Block r = null;
		Block r_AST = null;
		
		AST __t180 = _t;
		r = _t==ASTNULL ? null :(Block)_t;
		Block r_AST_in = null;
		r_AST = (Block)astFactory.create(r);
		astFactory.addASTChild(currentAST, r_AST);
		ASTPair __currentAST180 = currentAST.copy();
		currentAST.root = currentAST.child;
		currentAST.child = null;
		match(_t,NFunctionCall);
		_t = _t.getFirstChild();
		Block tmp121_AST = null;
		Block tmp121_AST_in = null;
		tmp121_AST = (Block)astFactory.create((Block)_t);
		tmp121_AST_in = (Block)_t;
		astFactory.addASTChild(currentAST, tmp121_AST);
		match(_t,ID);
		_t = _t.getNextSibling();
		{
		if (_t==null) _t=ASTNULL;
		switch ( _t.getType()) {
		case ID:
		case STAR:
		case PTR:
		case DOT:
		case LOR:
		case LAND:
		case BOR:
		case BXOR:
		case BAND:
		case EQUAL:
		case NOT_EQUAL:
		case LT:
		case LTE:
		case GT:
		case GTE:
		case LSHIFT:
		case RSHIFT:
		case PLUS:
		case MINUS:
		case DIV:
		case MOD:
		case LITERAL_sizeof:
		case CharLiteral:
		case IntOctalConst:
		case LongOctalConst:
		case UnsignedOctalConst:
		case IntIntConst:
		case LongIntConst:
		case UnsignedIntConst:
		case IntHexConst:
		case LongHexConst:
		case UnsignedHexConst:
		case FloatDoubleConst:
		case DoubleDoubleConst:
		case LongDoubleConst:
		case NCast:
		case NUnaryExpr:
		case NPostfixExpr:
		case NStringSeq:
		case NPureExpressionGroup:
		case NArrayElement:
		case LITERAL___alignof:
		{
			argExprList(_t);
			_t = _retTree;
			astFactory.addASTChild(currentAST, returnAST);
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
		currentAST = __currentAST180;
		_t = __t180;
		_t = _t.getNextSibling();
		if ( inputState.guessing==0 ) {
			functionCall_AST = (Block)currentAST.root;
			functionCall_AST.setCallIndex (r_AST.getCallIndex ());
		}
		functionCall_AST = (Block)currentAST.root;
		returnAST = functionCall_AST;
		_retTree = _t;
	}
	
	public final void argExprList(AST _t) throws RecognitionException {
		
		Block argExprList_AST_in = (_t == ASTNULL) ? null : (Block)_t;
		returnAST = null;
		ASTPair currentAST = new ASTPair();
		Block argExprList_AST = null;
		
		{
		int _cnt256=0;
		_loop256:
		do {
			if (_t==null) _t=ASTNULL;
			if ((_tokenSet_2.member(_t.getType()))) {
				pureExpr(_t);
				_t = _retTree;
				astFactory.addASTChild(currentAST, returnAST);
			}
			else {
				if ( _cnt256>=1 ) { break _loop256; } else {throw new NoViableAltException(_t);}
			}
			
			_cnt256++;
		} while (true);
		}
		argExprList_AST = (Block)currentAST.root;
		returnAST = argExprList_AST;
		_retTree = _t;
	}
	
	public final void arraySuffix(AST _t) throws RecognitionException {
		
		Block arraySuffix_AST_in = (_t == ASTNULL) ? null : (Block)_t;
		returnAST = null;
		ASTPair currentAST = new ASTPair();
		Block arraySuffix_AST = null;
		
		AST __t189 = _t;
		Block tmp122_AST = null;
		Block tmp122_AST_in = null;
		tmp122_AST = (Block)astFactory.create((Block)_t);
		tmp122_AST_in = (Block)_t;
		astFactory.addASTChild(currentAST, tmp122_AST);
		ASTPair __currentAST189 = currentAST.copy();
		currentAST.root = currentAST.child;
		currentAST.child = null;
		match(_t,NArrayIndex);
		_t = _t.getFirstChild();
		pureExpr(_t);
		_t = _retTree;
		astFactory.addASTChild(currentAST, returnAST);
		currentAST = __currentAST189;
		_t = __t189;
		_t = _t.getNextSibling();
		arraySuffix_AST = (Block)currentAST.root;
		returnAST = arraySuffix_AST;
		_retTree = _t;
	}
	
	public final void pureLogicalOrExpr(AST _t) throws RecognitionException {
		
		Block pureLogicalOrExpr_AST_in = (_t == ASTNULL) ? null : (Block)_t;
		returnAST = null;
		ASTPair currentAST = new ASTPair();
		Block pureLogicalOrExpr_AST = null;
		
		AST __t198 = _t;
		Block tmp123_AST = null;
		Block tmp123_AST_in = null;
		tmp123_AST = (Block)astFactory.create((Block)_t);
		tmp123_AST_in = (Block)_t;
		astFactory.addASTChild(currentAST, tmp123_AST);
		ASTPair __currentAST198 = currentAST.copy();
		currentAST.root = currentAST.child;
		currentAST.child = null;
		match(_t,LOR);
		_t = _t.getFirstChild();
		pureExpr(_t);
		_t = _retTree;
		astFactory.addASTChild(currentAST, returnAST);
		pureExpr(_t);
		_t = _retTree;
		astFactory.addASTChild(currentAST, returnAST);
		currentAST = __currentAST198;
		_t = __t198;
		_t = _t.getNextSibling();
		pureLogicalOrExpr_AST = (Block)currentAST.root;
		returnAST = pureLogicalOrExpr_AST;
		_retTree = _t;
	}
	
	public final void pureLogicalAndExpr(AST _t) throws RecognitionException {
		
		Block pureLogicalAndExpr_AST_in = (_t == ASTNULL) ? null : (Block)_t;
		returnAST = null;
		ASTPair currentAST = new ASTPair();
		Block pureLogicalAndExpr_AST = null;
		
		AST __t200 = _t;
		Block tmp124_AST = null;
		Block tmp124_AST_in = null;
		tmp124_AST = (Block)astFactory.create((Block)_t);
		tmp124_AST_in = (Block)_t;
		astFactory.addASTChild(currentAST, tmp124_AST);
		ASTPair __currentAST200 = currentAST.copy();
		currentAST.root = currentAST.child;
		currentAST.child = null;
		match(_t,LAND);
		_t = _t.getFirstChild();
		pureExpr(_t);
		_t = _retTree;
		astFactory.addASTChild(currentAST, returnAST);
		pureExpr(_t);
		_t = _retTree;
		astFactory.addASTChild(currentAST, returnAST);
		currentAST = __currentAST200;
		_t = __t200;
		_t = _t.getNextSibling();
		pureLogicalAndExpr_AST = (Block)currentAST.root;
		returnAST = pureLogicalAndExpr_AST;
		_retTree = _t;
	}
	
	public final void pureEqualityExpr(AST _t) throws RecognitionException {
		
		Block pureEqualityExpr_AST_in = (_t == ASTNULL) ? null : (Block)_t;
		returnAST = null;
		ASTPair currentAST = new ASTPair();
		Block pureEqualityExpr_AST = null;
		
		if (_t==null) _t=ASTNULL;
		switch ( _t.getType()) {
		case EQUAL:
		{
			AST __t202 = _t;
			Block tmp125_AST = null;
			Block tmp125_AST_in = null;
			tmp125_AST = (Block)astFactory.create((Block)_t);
			tmp125_AST_in = (Block)_t;
			astFactory.addASTChild(currentAST, tmp125_AST);
			ASTPair __currentAST202 = currentAST.copy();
			currentAST.root = currentAST.child;
			currentAST.child = null;
			match(_t,EQUAL);
			_t = _t.getFirstChild();
			pureExpr(_t);
			_t = _retTree;
			astFactory.addASTChild(currentAST, returnAST);
			pureExpr(_t);
			_t = _retTree;
			astFactory.addASTChild(currentAST, returnAST);
			currentAST = __currentAST202;
			_t = __t202;
			_t = _t.getNextSibling();
			pureEqualityExpr_AST = (Block)currentAST.root;
			break;
		}
		case NOT_EQUAL:
		{
			AST __t203 = _t;
			Block tmp126_AST = null;
			Block tmp126_AST_in = null;
			tmp126_AST = (Block)astFactory.create((Block)_t);
			tmp126_AST_in = (Block)_t;
			astFactory.addASTChild(currentAST, tmp126_AST);
			ASTPair __currentAST203 = currentAST.copy();
			currentAST.root = currentAST.child;
			currentAST.child = null;
			match(_t,NOT_EQUAL);
			_t = _t.getFirstChild();
			pureExpr(_t);
			_t = _retTree;
			astFactory.addASTChild(currentAST, returnAST);
			pureExpr(_t);
			_t = _retTree;
			astFactory.addASTChild(currentAST, returnAST);
			currentAST = __currentAST203;
			_t = __t203;
			_t = _t.getNextSibling();
			pureEqualityExpr_AST = (Block)currentAST.root;
			break;
		}
		default:
		{
			throw new NoViableAltException(_t);
		}
		}
		returnAST = pureEqualityExpr_AST;
		_retTree = _t;
	}
	
	public final void pureRelationalExpr(AST _t) throws RecognitionException {
		
		Block pureRelationalExpr_AST_in = (_t == ASTNULL) ? null : (Block)_t;
		returnAST = null;
		ASTPair currentAST = new ASTPair();
		Block pureRelationalExpr_AST = null;
		
		if (_t==null) _t=ASTNULL;
		switch ( _t.getType()) {
		case LT:
		{
			AST __t205 = _t;
			Block tmp127_AST = null;
			Block tmp127_AST_in = null;
			tmp127_AST = (Block)astFactory.create((Block)_t);
			tmp127_AST_in = (Block)_t;
			astFactory.addASTChild(currentAST, tmp127_AST);
			ASTPair __currentAST205 = currentAST.copy();
			currentAST.root = currentAST.child;
			currentAST.child = null;
			match(_t,LT);
			_t = _t.getFirstChild();
			pureExpr(_t);
			_t = _retTree;
			astFactory.addASTChild(currentAST, returnAST);
			pureExpr(_t);
			_t = _retTree;
			astFactory.addASTChild(currentAST, returnAST);
			currentAST = __currentAST205;
			_t = __t205;
			_t = _t.getNextSibling();
			pureRelationalExpr_AST = (Block)currentAST.root;
			break;
		}
		case LTE:
		{
			AST __t206 = _t;
			Block tmp128_AST = null;
			Block tmp128_AST_in = null;
			tmp128_AST = (Block)astFactory.create((Block)_t);
			tmp128_AST_in = (Block)_t;
			astFactory.addASTChild(currentAST, tmp128_AST);
			ASTPair __currentAST206 = currentAST.copy();
			currentAST.root = currentAST.child;
			currentAST.child = null;
			match(_t,LTE);
			_t = _t.getFirstChild();
			pureExpr(_t);
			_t = _retTree;
			astFactory.addASTChild(currentAST, returnAST);
			pureExpr(_t);
			_t = _retTree;
			astFactory.addASTChild(currentAST, returnAST);
			currentAST = __currentAST206;
			_t = __t206;
			_t = _t.getNextSibling();
			pureRelationalExpr_AST = (Block)currentAST.root;
			break;
		}
		case GT:
		{
			AST __t207 = _t;
			Block tmp129_AST = null;
			Block tmp129_AST_in = null;
			tmp129_AST = (Block)astFactory.create((Block)_t);
			tmp129_AST_in = (Block)_t;
			astFactory.addASTChild(currentAST, tmp129_AST);
			ASTPair __currentAST207 = currentAST.copy();
			currentAST.root = currentAST.child;
			currentAST.child = null;
			match(_t,GT);
			_t = _t.getFirstChild();
			pureExpr(_t);
			_t = _retTree;
			astFactory.addASTChild(currentAST, returnAST);
			pureExpr(_t);
			_t = _retTree;
			astFactory.addASTChild(currentAST, returnAST);
			currentAST = __currentAST207;
			_t = __t207;
			_t = _t.getNextSibling();
			pureRelationalExpr_AST = (Block)currentAST.root;
			break;
		}
		case GTE:
		{
			AST __t208 = _t;
			Block tmp130_AST = null;
			Block tmp130_AST_in = null;
			tmp130_AST = (Block)astFactory.create((Block)_t);
			tmp130_AST_in = (Block)_t;
			astFactory.addASTChild(currentAST, tmp130_AST);
			ASTPair __currentAST208 = currentAST.copy();
			currentAST.root = currentAST.child;
			currentAST.child = null;
			match(_t,GTE);
			_t = _t.getFirstChild();
			pureExpr(_t);
			_t = _retTree;
			astFactory.addASTChild(currentAST, returnAST);
			pureExpr(_t);
			_t = _retTree;
			astFactory.addASTChild(currentAST, returnAST);
			currentAST = __currentAST208;
			_t = __t208;
			_t = _t.getNextSibling();
			pureRelationalExpr_AST = (Block)currentAST.root;
			break;
		}
		default:
		{
			throw new NoViableAltException(_t);
		}
		}
		returnAST = pureRelationalExpr_AST;
		_retTree = _t;
	}
	
	public final void pureAdditiveExpr(AST _t) throws RecognitionException {
		
		Block pureAdditiveExpr_AST_in = (_t == ASTNULL) ? null : (Block)_t;
		returnAST = null;
		ASTPair currentAST = new ASTPair();
		Block pureAdditiveExpr_AST = null;
		
		if (_t==null) _t=ASTNULL;
		switch ( _t.getType()) {
		case PLUS:
		{
			AST __t210 = _t;
			Block tmp131_AST = null;
			Block tmp131_AST_in = null;
			tmp131_AST = (Block)astFactory.create((Block)_t);
			tmp131_AST_in = (Block)_t;
			astFactory.addASTChild(currentAST, tmp131_AST);
			ASTPair __currentAST210 = currentAST.copy();
			currentAST.root = currentAST.child;
			currentAST.child = null;
			match(_t,PLUS);
			_t = _t.getFirstChild();
			pureExpr(_t);
			_t = _retTree;
			astFactory.addASTChild(currentAST, returnAST);
			pureExpr(_t);
			_t = _retTree;
			astFactory.addASTChild(currentAST, returnAST);
			currentAST = __currentAST210;
			_t = __t210;
			_t = _t.getNextSibling();
			pureAdditiveExpr_AST = (Block)currentAST.root;
			break;
		}
		case MINUS:
		{
			AST __t211 = _t;
			Block tmp132_AST = null;
			Block tmp132_AST_in = null;
			tmp132_AST = (Block)astFactory.create((Block)_t);
			tmp132_AST_in = (Block)_t;
			astFactory.addASTChild(currentAST, tmp132_AST);
			ASTPair __currentAST211 = currentAST.copy();
			currentAST.root = currentAST.child;
			currentAST.child = null;
			match(_t,MINUS);
			_t = _t.getFirstChild();
			pureExpr(_t);
			_t = _retTree;
			astFactory.addASTChild(currentAST, returnAST);
			pureExpr(_t);
			_t = _retTree;
			astFactory.addASTChild(currentAST, returnAST);
			currentAST = __currentAST211;
			_t = __t211;
			_t = _t.getNextSibling();
			pureAdditiveExpr_AST = (Block)currentAST.root;
			break;
		}
		default:
		{
			throw new NoViableAltException(_t);
		}
		}
		returnAST = pureAdditiveExpr_AST;
		_retTree = _t;
	}
	
	public final void pureMultExpr(AST _t) throws RecognitionException {
		
		Block pureMultExpr_AST_in = (_t == ASTNULL) ? null : (Block)_t;
		returnAST = null;
		ASTPair currentAST = new ASTPair();
		Block pureMultExpr_AST = null;
		
		if (_t==null) _t=ASTNULL;
		switch ( _t.getType()) {
		case STAR:
		{
			AST __t213 = _t;
			Block tmp133_AST = null;
			Block tmp133_AST_in = null;
			tmp133_AST = (Block)astFactory.create((Block)_t);
			tmp133_AST_in = (Block)_t;
			astFactory.addASTChild(currentAST, tmp133_AST);
			ASTPair __currentAST213 = currentAST.copy();
			currentAST.root = currentAST.child;
			currentAST.child = null;
			match(_t,STAR);
			_t = _t.getFirstChild();
			pureExpr(_t);
			_t = _retTree;
			astFactory.addASTChild(currentAST, returnAST);
			pureExpr(_t);
			_t = _retTree;
			astFactory.addASTChild(currentAST, returnAST);
			currentAST = __currentAST213;
			_t = __t213;
			_t = _t.getNextSibling();
			pureMultExpr_AST = (Block)currentAST.root;
			break;
		}
		case DIV:
		{
			AST __t214 = _t;
			Block tmp134_AST = null;
			Block tmp134_AST_in = null;
			tmp134_AST = (Block)astFactory.create((Block)_t);
			tmp134_AST_in = (Block)_t;
			astFactory.addASTChild(currentAST, tmp134_AST);
			ASTPair __currentAST214 = currentAST.copy();
			currentAST.root = currentAST.child;
			currentAST.child = null;
			match(_t,DIV);
			_t = _t.getFirstChild();
			pureExpr(_t);
			_t = _retTree;
			astFactory.addASTChild(currentAST, returnAST);
			pureExpr(_t);
			_t = _retTree;
			astFactory.addASTChild(currentAST, returnAST);
			currentAST = __currentAST214;
			_t = __t214;
			_t = _t.getNextSibling();
			pureMultExpr_AST = (Block)currentAST.root;
			break;
		}
		case MOD:
		{
			AST __t215 = _t;
			Block tmp135_AST = null;
			Block tmp135_AST_in = null;
			tmp135_AST = (Block)astFactory.create((Block)_t);
			tmp135_AST_in = (Block)_t;
			astFactory.addASTChild(currentAST, tmp135_AST);
			ASTPair __currentAST215 = currentAST.copy();
			currentAST.root = currentAST.child;
			currentAST.child = null;
			match(_t,MOD);
			_t = _t.getFirstChild();
			pureExpr(_t);
			_t = _retTree;
			astFactory.addASTChild(currentAST, returnAST);
			pureExpr(_t);
			_t = _retTree;
			astFactory.addASTChild(currentAST, returnAST);
			currentAST = __currentAST215;
			_t = __t215;
			_t = _t.getNextSibling();
			pureMultExpr_AST = (Block)currentAST.root;
			break;
		}
		default:
		{
			throw new NoViableAltException(_t);
		}
		}
		returnAST = pureMultExpr_AST;
		_retTree = _t;
	}
	
	public final void pureBitwiseExpr(AST _t) throws RecognitionException {
		
		Block pureBitwiseExpr_AST_in = (_t == ASTNULL) ? null : (Block)_t;
		returnAST = null;
		ASTPair currentAST = new ASTPair();
		Block pureBitwiseExpr_AST = null;
		
		if (_t==null) _t=ASTNULL;
		switch ( _t.getType()) {
		case BOR:
		{
			AST __t192 = _t;
			Block tmp136_AST = null;
			Block tmp136_AST_in = null;
			tmp136_AST = (Block)astFactory.create((Block)_t);
			tmp136_AST_in = (Block)_t;
			astFactory.addASTChild(currentAST, tmp136_AST);
			ASTPair __currentAST192 = currentAST.copy();
			currentAST.root = currentAST.child;
			currentAST.child = null;
			match(_t,BOR);
			_t = _t.getFirstChild();
			pureExpr(_t);
			_t = _retTree;
			astFactory.addASTChild(currentAST, returnAST);
			pureExpr(_t);
			_t = _retTree;
			astFactory.addASTChild(currentAST, returnAST);
			currentAST = __currentAST192;
			_t = __t192;
			_t = _t.getNextSibling();
			pureBitwiseExpr_AST = (Block)currentAST.root;
			break;
		}
		case BXOR:
		{
			AST __t193 = _t;
			Block tmp137_AST = null;
			Block tmp137_AST_in = null;
			tmp137_AST = (Block)astFactory.create((Block)_t);
			tmp137_AST_in = (Block)_t;
			astFactory.addASTChild(currentAST, tmp137_AST);
			ASTPair __currentAST193 = currentAST.copy();
			currentAST.root = currentAST.child;
			currentAST.child = null;
			match(_t,BXOR);
			_t = _t.getFirstChild();
			pureExpr(_t);
			_t = _retTree;
			astFactory.addASTChild(currentAST, returnAST);
			pureExpr(_t);
			_t = _retTree;
			astFactory.addASTChild(currentAST, returnAST);
			currentAST = __currentAST193;
			_t = __t193;
			_t = _t.getNextSibling();
			pureBitwiseExpr_AST = (Block)currentAST.root;
			break;
		}
		case BAND:
		{
			AST __t194 = _t;
			Block tmp138_AST = null;
			Block tmp138_AST_in = null;
			tmp138_AST = (Block)astFactory.create((Block)_t);
			tmp138_AST_in = (Block)_t;
			astFactory.addASTChild(currentAST, tmp138_AST);
			ASTPair __currentAST194 = currentAST.copy();
			currentAST.root = currentAST.child;
			currentAST.child = null;
			match(_t,BAND);
			_t = _t.getFirstChild();
			pureExpr(_t);
			_t = _retTree;
			astFactory.addASTChild(currentAST, returnAST);
			pureExpr(_t);
			_t = _retTree;
			astFactory.addASTChild(currentAST, returnAST);
			currentAST = __currentAST194;
			_t = __t194;
			_t = _t.getNextSibling();
			pureBitwiseExpr_AST = (Block)currentAST.root;
			break;
		}
		case LSHIFT:
		{
			AST __t195 = _t;
			Block tmp139_AST = null;
			Block tmp139_AST_in = null;
			tmp139_AST = (Block)astFactory.create((Block)_t);
			tmp139_AST_in = (Block)_t;
			astFactory.addASTChild(currentAST, tmp139_AST);
			ASTPair __currentAST195 = currentAST.copy();
			currentAST.root = currentAST.child;
			currentAST.child = null;
			match(_t,LSHIFT);
			_t = _t.getFirstChild();
			pureExpr(_t);
			_t = _retTree;
			astFactory.addASTChild(currentAST, returnAST);
			pureExpr(_t);
			_t = _retTree;
			astFactory.addASTChild(currentAST, returnAST);
			currentAST = __currentAST195;
			_t = __t195;
			_t = _t.getNextSibling();
			pureBitwiseExpr_AST = (Block)currentAST.root;
			break;
		}
		case RSHIFT:
		{
			AST __t196 = _t;
			Block tmp140_AST = null;
			Block tmp140_AST_in = null;
			tmp140_AST = (Block)astFactory.create((Block)_t);
			tmp140_AST_in = (Block)_t;
			astFactory.addASTChild(currentAST, tmp140_AST);
			ASTPair __currentAST196 = currentAST.copy();
			currentAST.root = currentAST.child;
			currentAST.child = null;
			match(_t,RSHIFT);
			_t = _t.getFirstChild();
			pureExpr(_t);
			_t = _retTree;
			astFactory.addASTChild(currentAST, returnAST);
			pureExpr(_t);
			_t = _retTree;
			astFactory.addASTChild(currentAST, returnAST);
			currentAST = __currentAST196;
			_t = __t196;
			_t = _t.getNextSibling();
			pureBitwiseExpr_AST = (Block)currentAST.root;
			break;
		}
		default:
		{
			throw new NoViableAltException(_t);
		}
		}
		returnAST = pureBitwiseExpr_AST;
		_retTree = _t;
	}
	
	public final void pureCastExpr(AST _t) throws RecognitionException {
		
		Block pureCastExpr_AST_in = (_t == ASTNULL) ? null : (Block)_t;
		returnAST = null;
		ASTPair currentAST = new ASTPair();
		Block pureCastExpr_AST = null;
		Block e_AST = null;
		Block e = null;
		
		AST __t217 = _t;
		Block tmp141_AST = null;
		Block tmp141_AST_in = null;
		tmp141_AST = (Block)astFactory.create((Block)_t);
		tmp141_AST_in = (Block)_t;
		ASTPair __currentAST217 = currentAST.copy();
		currentAST.root = currentAST.child;
		currentAST.child = null;
		match(_t,NCast);
		_t = _t.getFirstChild();
		typeName(_t);
		_t = _retTree;
		e = _t==ASTNULL ? null : (Block)_t;
		pureExpr(_t);
		_t = _retTree;
		e_AST = (Block)returnAST;
		currentAST = __currentAST217;
		_t = __t217;
		_t = _t.getNextSibling();
		if ( inputState.guessing==0 ) {
			pureCastExpr_AST = (Block)currentAST.root;
			pureCastExpr_AST = e_AST;
			currentAST.root = pureCastExpr_AST;
			currentAST.child = pureCastExpr_AST!=null &&pureCastExpr_AST.getFirstChild()!=null ?
				pureCastExpr_AST.getFirstChild() : pureCastExpr_AST;
			currentAST.advanceChildToEnd();
		}
		returnAST = pureCastExpr_AST;
		_retTree = _t;
	}
	
	public final void pureUnaryExpr(AST _t) throws RecognitionException {
		
		Block pureUnaryExpr_AST_in = (_t == ASTNULL) ? null : (Block)_t;
		returnAST = null;
		ASTPair currentAST = new ASTPair();
		Block pureUnaryExpr_AST = null;
		
		if (_t==null) _t=ASTNULL;
		switch ( _t.getType()) {
		case NPostfixExpr:
		{
			purePostfixExpr(_t);
			_t = _retTree;
			astFactory.addASTChild(currentAST, returnAST);
			pureUnaryExpr_AST = (Block)currentAST.root;
			break;
		}
		case NUnaryExpr:
		{
			AST __t236 = _t;
			Block tmp142_AST = null;
			Block tmp142_AST_in = null;
			tmp142_AST = (Block)astFactory.create((Block)_t);
			tmp142_AST_in = (Block)_t;
			astFactory.addASTChild(currentAST, tmp142_AST);
			ASTPair __currentAST236 = currentAST.copy();
			currentAST.root = currentAST.child;
			currentAST.child = null;
			match(_t,NUnaryExpr);
			_t = _t.getFirstChild();
			unaryOperator(_t);
			_t = _retTree;
			astFactory.addASTChild(currentAST, returnAST);
			pureExpr(_t);
			_t = _retTree;
			astFactory.addASTChild(currentAST, returnAST);
			currentAST = __currentAST236;
			_t = __t236;
			_t = _t.getNextSibling();
			pureUnaryExpr_AST = (Block)currentAST.root;
			break;
		}
		case LITERAL_sizeof:
		{
			AST __t237 = _t;
			Block tmp143_AST = null;
			Block tmp143_AST_in = null;
			tmp143_AST = (Block)astFactory.create((Block)_t);
			tmp143_AST_in = (Block)_t;
			astFactory.addASTChild(currentAST, tmp143_AST);
			ASTPair __currentAST237 = currentAST.copy();
			currentAST.root = currentAST.child;
			currentAST.child = null;
			match(_t,LITERAL_sizeof);
			_t = _t.getFirstChild();
			{
			if (_t==null) _t=ASTNULL;
			switch ( _t.getType()) {
			case LPAREN:
			{
				Block tmp144_AST = null;
				Block tmp144_AST_in = null;
				tmp144_AST = (Block)astFactory.create((Block)_t);
				tmp144_AST_in = (Block)_t;
				astFactory.addASTChild(currentAST, tmp144_AST);
				match(_t,LPAREN);
				_t = _t.getNextSibling();
				typeName(_t);
				_t = _retTree;
				astFactory.addASTChild(currentAST, returnAST);
				Block tmp145_AST = null;
				Block tmp145_AST_in = null;
				tmp145_AST = (Block)astFactory.create((Block)_t);
				tmp145_AST_in = (Block)_t;
				astFactory.addASTChild(currentAST, tmp145_AST);
				match(_t,RPAREN);
				_t = _t.getNextSibling();
				break;
			}
			case ID:
			case STAR:
			case PTR:
			case DOT:
			case LOR:
			case LAND:
			case BOR:
			case BXOR:
			case BAND:
			case EQUAL:
			case NOT_EQUAL:
			case LT:
			case LTE:
			case GT:
			case GTE:
			case LSHIFT:
			case RSHIFT:
			case PLUS:
			case MINUS:
			case DIV:
			case MOD:
			case LITERAL_sizeof:
			case CharLiteral:
			case IntOctalConst:
			case LongOctalConst:
			case UnsignedOctalConst:
			case IntIntConst:
			case LongIntConst:
			case UnsignedIntConst:
			case IntHexConst:
			case LongHexConst:
			case UnsignedHexConst:
			case FloatDoubleConst:
			case DoubleDoubleConst:
			case LongDoubleConst:
			case NCast:
			case NUnaryExpr:
			case NPostfixExpr:
			case NStringSeq:
			case NPureExpressionGroup:
			case NArrayElement:
			case LITERAL___alignof:
			{
				pureExpr(_t);
				_t = _retTree;
				astFactory.addASTChild(currentAST, returnAST);
				break;
			}
			default:
			{
				throw new NoViableAltException(_t);
			}
			}
			}
			currentAST = __currentAST237;
			_t = __t237;
			_t = _t.getNextSibling();
			pureUnaryExpr_AST = (Block)currentAST.root;
			break;
		}
		case LITERAL___alignof:
		{
			AST __t241 = _t;
			Block tmp146_AST = null;
			Block tmp146_AST_in = null;
			tmp146_AST = (Block)astFactory.create((Block)_t);
			tmp146_AST_in = (Block)_t;
			astFactory.addASTChild(currentAST, tmp146_AST);
			ASTPair __currentAST241 = currentAST.copy();
			currentAST.root = currentAST.child;
			currentAST.child = null;
			match(_t,LITERAL___alignof);
			_t = _t.getFirstChild();
			{
			if (_t==null) _t=ASTNULL;
			switch ( _t.getType()) {
			case LPAREN:
			{
				Block tmp147_AST = null;
				Block tmp147_AST_in = null;
				tmp147_AST = (Block)astFactory.create((Block)_t);
				tmp147_AST_in = (Block)_t;
				astFactory.addASTChild(currentAST, tmp147_AST);
				match(_t,LPAREN);
				_t = _t.getNextSibling();
				typeName(_t);
				_t = _retTree;
				astFactory.addASTChild(currentAST, returnAST);
				Block tmp148_AST = null;
				Block tmp148_AST_in = null;
				tmp148_AST = (Block)astFactory.create((Block)_t);
				tmp148_AST_in = (Block)_t;
				astFactory.addASTChild(currentAST, tmp148_AST);
				match(_t,RPAREN);
				_t = _t.getNextSibling();
				break;
			}
			case ID:
			case STAR:
			case PTR:
			case DOT:
			case LOR:
			case LAND:
			case BOR:
			case BXOR:
			case BAND:
			case EQUAL:
			case NOT_EQUAL:
			case LT:
			case LTE:
			case GT:
			case GTE:
			case LSHIFT:
			case RSHIFT:
			case PLUS:
			case MINUS:
			case DIV:
			case MOD:
			case LITERAL_sizeof:
			case CharLiteral:
			case IntOctalConst:
			case LongOctalConst:
			case UnsignedOctalConst:
			case IntIntConst:
			case LongIntConst:
			case UnsignedIntConst:
			case IntHexConst:
			case LongHexConst:
			case UnsignedHexConst:
			case FloatDoubleConst:
			case DoubleDoubleConst:
			case LongDoubleConst:
			case NCast:
			case NUnaryExpr:
			case NPostfixExpr:
			case NStringSeq:
			case NPureExpressionGroup:
			case NArrayElement:
			case LITERAL___alignof:
			{
				pureExpr(_t);
				_t = _retTree;
				astFactory.addASTChild(currentAST, returnAST);
				break;
			}
			default:
			{
				throw new NoViableAltException(_t);
			}
			}
			}
			currentAST = __currentAST241;
			_t = __t241;
			_t = _t.getNextSibling();
			pureUnaryExpr_AST = (Block)currentAST.root;
			break;
		}
		default:
		{
			throw new NoViableAltException(_t);
		}
		}
		returnAST = pureUnaryExpr_AST;
		_retTree = _t;
	}
	
	public final void purePrimaryExpr(AST _t) throws RecognitionException {
		
		Block purePrimaryExpr_AST_in = (_t == ASTNULL) ? null : (Block)_t;
		returnAST = null;
		ASTPair currentAST = new ASTPair();
		Block purePrimaryExpr_AST = null;
		
		if (_t==null) _t=ASTNULL;
		switch ( _t.getType()) {
		case ID:
		case PTR:
		case DOT:
		case NArrayElement:
		{
			lval(_t);
			_t = _retTree;
			astFactory.addASTChild(currentAST, returnAST);
			purePrimaryExpr_AST = (Block)currentAST.root;
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
			intConst(_t);
			_t = _retTree;
			astFactory.addASTChild(currentAST, returnAST);
			purePrimaryExpr_AST = (Block)currentAST.root;
			break;
		}
		case FloatDoubleConst:
		case DoubleDoubleConst:
		case LongDoubleConst:
		{
			floatConst(_t);
			_t = _retTree;
			astFactory.addASTChild(currentAST, returnAST);
			purePrimaryExpr_AST = (Block)currentAST.root;
			break;
		}
		case CharLiteral:
		{
			charConst(_t);
			_t = _retTree;
			astFactory.addASTChild(currentAST, returnAST);
			purePrimaryExpr_AST = (Block)currentAST.root;
			break;
		}
		case NStringSeq:
		{
			stringConst(_t);
			_t = _retTree;
			astFactory.addASTChild(currentAST, returnAST);
			purePrimaryExpr_AST = (Block)currentAST.root;
			break;
		}
		case NPureExpressionGroup:
		{
			AST __t253 = _t;
			Block tmp149_AST = null;
			Block tmp149_AST_in = null;
			tmp149_AST = (Block)astFactory.create((Block)_t);
			tmp149_AST_in = (Block)_t;
			astFactory.addASTChild(currentAST, tmp149_AST);
			ASTPair __currentAST253 = currentAST.copy();
			currentAST.root = currentAST.child;
			currentAST.child = null;
			match(_t,NPureExpressionGroup);
			_t = _t.getFirstChild();
			pureExpr(_t);
			_t = _retTree;
			astFactory.addASTChild(currentAST, returnAST);
			currentAST = __currentAST253;
			_t = __t253;
			_t = _t.getNextSibling();
			purePrimaryExpr_AST = (Block)currentAST.root;
			break;
		}
		default:
		{
			throw new NoViableAltException(_t);
		}
		}
		returnAST = purePrimaryExpr_AST;
		_retTree = _t;
	}
	
	public final void purePostfixExpr(AST _t) throws RecognitionException {
		
		Block purePostfixExpr_AST_in = (_t == ASTNULL) ? null : (Block)_t;
		returnAST = null;
		ASTPair currentAST = new ASTPair();
		Block purePostfixExpr_AST = null;
		
		AST __t246 = _t;
		Block tmp150_AST = null;
		Block tmp150_AST_in = null;
		tmp150_AST = (Block)astFactory.create((Block)_t);
		tmp150_AST_in = (Block)_t;
		astFactory.addASTChild(currentAST, tmp150_AST);
		ASTPair __currentAST246 = currentAST.copy();
		currentAST.root = currentAST.child;
		currentAST.child = null;
		match(_t,NPostfixExpr);
		_t = _t.getFirstChild();
		lval(_t);
		_t = _retTree;
		astFactory.addASTChild(currentAST, returnAST);
		{
		int _cnt250=0;
		_loop250:
		do {
			if (_t==null) _t=ASTNULL;
			switch ( _t.getType()) {
			case NFunctionCallArgs:
			{
				AST __t248 = _t;
				Block tmp151_AST = null;
				Block tmp151_AST_in = null;
				tmp151_AST = (Block)astFactory.create((Block)_t);
				tmp151_AST_in = (Block)_t;
				astFactory.addASTChild(currentAST, tmp151_AST);
				ASTPair __currentAST248 = currentAST.copy();
				currentAST.root = currentAST.child;
				currentAST.child = null;
				match(_t,NFunctionCallArgs);
				_t = _t.getFirstChild();
				{
				if (_t==null) _t=ASTNULL;
				switch ( _t.getType()) {
				case ID:
				case STAR:
				case PTR:
				case DOT:
				case LOR:
				case LAND:
				case BOR:
				case BXOR:
				case BAND:
				case EQUAL:
				case NOT_EQUAL:
				case LT:
				case LTE:
				case GT:
				case GTE:
				case LSHIFT:
				case RSHIFT:
				case PLUS:
				case MINUS:
				case DIV:
				case MOD:
				case LITERAL_sizeof:
				case CharLiteral:
				case IntOctalConst:
				case LongOctalConst:
				case UnsignedOctalConst:
				case IntIntConst:
				case LongIntConst:
				case UnsignedIntConst:
				case IntHexConst:
				case LongHexConst:
				case UnsignedHexConst:
				case FloatDoubleConst:
				case DoubleDoubleConst:
				case LongDoubleConst:
				case NCast:
				case NUnaryExpr:
				case NPostfixExpr:
				case NStringSeq:
				case NPureExpressionGroup:
				case NArrayElement:
				case LITERAL___alignof:
				{
					argExprList(_t);
					_t = _retTree;
					astFactory.addASTChild(currentAST, returnAST);
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
				currentAST = __currentAST248;
				_t = __t248;
				_t = _t.getNextSibling();
				break;
			}
			case LBRACKET:
			{
				Block tmp152_AST = null;
				Block tmp152_AST_in = null;
				tmp152_AST = (Block)astFactory.create((Block)_t);
				tmp152_AST_in = (Block)_t;
				astFactory.addASTChild(currentAST, tmp152_AST);
				match(_t,LBRACKET);
				_t = _t.getNextSibling();
				pureExpr(_t);
				_t = _retTree;
				astFactory.addASTChild(currentAST, returnAST);
				Block tmp153_AST = null;
				Block tmp153_AST_in = null;
				tmp153_AST = (Block)astFactory.create((Block)_t);
				tmp153_AST_in = (Block)_t;
				astFactory.addASTChild(currentAST, tmp153_AST);
				match(_t,RBRACKET);
				_t = _t.getNextSibling();
				break;
			}
			default:
			{
				if ( _cnt250>=1 ) { break _loop250; } else {throw new NoViableAltException(_t);}
			}
			}
			_cnt250++;
		} while (true);
		}
		currentAST = __currentAST246;
		_t = __t246;
		_t = _t.getNextSibling();
		purePostfixExpr_AST = (Block)currentAST.root;
		returnAST = purePostfixExpr_AST;
		_retTree = _t;
	}
	
	public final void unaryOperator(AST _t) throws RecognitionException {
		
		Block unaryOperator_AST_in = (_t == ASTNULL) ? null : (Block)_t;
		returnAST = null;
		ASTPair currentAST = new ASTPair();
		Block unaryOperator_AST = null;
		
		if (_t==null) _t=ASTNULL;
		switch ( _t.getType()) {
		case BAND:
		{
			Block tmp154_AST = null;
			Block tmp154_AST_in = null;
			tmp154_AST = (Block)astFactory.create((Block)_t);
			tmp154_AST_in = (Block)_t;
			astFactory.addASTChild(currentAST, tmp154_AST);
			match(_t,BAND);
			_t = _t.getNextSibling();
			unaryOperator_AST = (Block)currentAST.root;
			break;
		}
		case STAR:
		{
			Block tmp155_AST = null;
			Block tmp155_AST_in = null;
			tmp155_AST = (Block)astFactory.create((Block)_t);
			tmp155_AST_in = (Block)_t;
			astFactory.addASTChild(currentAST, tmp155_AST);
			match(_t,STAR);
			_t = _t.getNextSibling();
			unaryOperator_AST = (Block)currentAST.root;
			break;
		}
		case PLUS:
		{
			Block tmp156_AST = null;
			Block tmp156_AST_in = null;
			tmp156_AST = (Block)astFactory.create((Block)_t);
			tmp156_AST_in = (Block)_t;
			astFactory.addASTChild(currentAST, tmp156_AST);
			match(_t,PLUS);
			_t = _t.getNextSibling();
			unaryOperator_AST = (Block)currentAST.root;
			break;
		}
		case MINUS:
		{
			Block tmp157_AST = null;
			Block tmp157_AST_in = null;
			tmp157_AST = (Block)astFactory.create((Block)_t);
			tmp157_AST_in = (Block)_t;
			astFactory.addASTChild(currentAST, tmp157_AST);
			match(_t,MINUS);
			_t = _t.getNextSibling();
			unaryOperator_AST = (Block)currentAST.root;
			break;
		}
		case BNOT:
		{
			Block tmp158_AST = null;
			Block tmp158_AST_in = null;
			tmp158_AST = (Block)astFactory.create((Block)_t);
			tmp158_AST_in = (Block)_t;
			astFactory.addASTChild(currentAST, tmp158_AST);
			match(_t,BNOT);
			_t = _t.getNextSibling();
			unaryOperator_AST = (Block)currentAST.root;
			break;
		}
		case LNOT:
		{
			Block tmp159_AST = null;
			Block tmp159_AST_in = null;
			tmp159_AST = (Block)astFactory.create((Block)_t);
			tmp159_AST_in = (Block)_t;
			astFactory.addASTChild(currentAST, tmp159_AST);
			match(_t,LNOT);
			_t = _t.getNextSibling();
			unaryOperator_AST = (Block)currentAST.root;
			break;
		}
		case LAND:
		{
			Block tmp160_AST = null;
			Block tmp160_AST_in = null;
			tmp160_AST = (Block)astFactory.create((Block)_t);
			tmp160_AST_in = (Block)_t;
			astFactory.addASTChild(currentAST, tmp160_AST);
			match(_t,LAND);
			_t = _t.getNextSibling();
			unaryOperator_AST = (Block)currentAST.root;
			break;
		}
		case LITERAL___real:
		{
			Block tmp161_AST = null;
			Block tmp161_AST_in = null;
			tmp161_AST = (Block)astFactory.create((Block)_t);
			tmp161_AST_in = (Block)_t;
			astFactory.addASTChild(currentAST, tmp161_AST);
			match(_t,LITERAL___real);
			_t = _t.getNextSibling();
			unaryOperator_AST = (Block)currentAST.root;
			break;
		}
		case LITERAL___imag:
		{
			Block tmp162_AST = null;
			Block tmp162_AST_in = null;
			tmp162_AST = (Block)astFactory.create((Block)_t);
			tmp162_AST_in = (Block)_t;
			astFactory.addASTChild(currentAST, tmp162_AST);
			match(_t,LITERAL___imag);
			_t = _t.getNextSibling();
			unaryOperator_AST = (Block)currentAST.root;
			break;
		}
		default:
		{
			throw new NoViableAltException(_t);
		}
		}
		returnAST = unaryOperator_AST;
		_retTree = _t;
	}
	
	protected final void intConst(AST _t) throws RecognitionException {
		
		Block intConst_AST_in = (_t == ASTNULL) ? null : (Block)_t;
		returnAST = null;
		ASTPair currentAST = new ASTPair();
		Block intConst_AST = null;
		
		if (_t==null) _t=ASTNULL;
		switch ( _t.getType()) {
		case IntOctalConst:
		{
			Block tmp163_AST = null;
			Block tmp163_AST_in = null;
			tmp163_AST = (Block)astFactory.create((Block)_t);
			tmp163_AST_in = (Block)_t;
			astFactory.addASTChild(currentAST, tmp163_AST);
			match(_t,IntOctalConst);
			_t = _t.getNextSibling();
			intConst_AST = (Block)currentAST.root;
			break;
		}
		case LongOctalConst:
		{
			Block tmp164_AST = null;
			Block tmp164_AST_in = null;
			tmp164_AST = (Block)astFactory.create((Block)_t);
			tmp164_AST_in = (Block)_t;
			astFactory.addASTChild(currentAST, tmp164_AST);
			match(_t,LongOctalConst);
			_t = _t.getNextSibling();
			intConst_AST = (Block)currentAST.root;
			break;
		}
		case UnsignedOctalConst:
		{
			Block tmp165_AST = null;
			Block tmp165_AST_in = null;
			tmp165_AST = (Block)astFactory.create((Block)_t);
			tmp165_AST_in = (Block)_t;
			astFactory.addASTChild(currentAST, tmp165_AST);
			match(_t,UnsignedOctalConst);
			_t = _t.getNextSibling();
			intConst_AST = (Block)currentAST.root;
			break;
		}
		case IntIntConst:
		{
			Block tmp166_AST = null;
			Block tmp166_AST_in = null;
			tmp166_AST = (Block)astFactory.create((Block)_t);
			tmp166_AST_in = (Block)_t;
			astFactory.addASTChild(currentAST, tmp166_AST);
			match(_t,IntIntConst);
			_t = _t.getNextSibling();
			intConst_AST = (Block)currentAST.root;
			break;
		}
		case LongIntConst:
		{
			Block tmp167_AST = null;
			Block tmp167_AST_in = null;
			tmp167_AST = (Block)astFactory.create((Block)_t);
			tmp167_AST_in = (Block)_t;
			astFactory.addASTChild(currentAST, tmp167_AST);
			match(_t,LongIntConst);
			_t = _t.getNextSibling();
			intConst_AST = (Block)currentAST.root;
			break;
		}
		case UnsignedIntConst:
		{
			Block tmp168_AST = null;
			Block tmp168_AST_in = null;
			tmp168_AST = (Block)astFactory.create((Block)_t);
			tmp168_AST_in = (Block)_t;
			astFactory.addASTChild(currentAST, tmp168_AST);
			match(_t,UnsignedIntConst);
			_t = _t.getNextSibling();
			intConst_AST = (Block)currentAST.root;
			break;
		}
		case IntHexConst:
		{
			Block tmp169_AST = null;
			Block tmp169_AST_in = null;
			tmp169_AST = (Block)astFactory.create((Block)_t);
			tmp169_AST_in = (Block)_t;
			astFactory.addASTChild(currentAST, tmp169_AST);
			match(_t,IntHexConst);
			_t = _t.getNextSibling();
			intConst_AST = (Block)currentAST.root;
			break;
		}
		case LongHexConst:
		{
			Block tmp170_AST = null;
			Block tmp170_AST_in = null;
			tmp170_AST = (Block)astFactory.create((Block)_t);
			tmp170_AST_in = (Block)_t;
			astFactory.addASTChild(currentAST, tmp170_AST);
			match(_t,LongHexConst);
			_t = _t.getNextSibling();
			intConst_AST = (Block)currentAST.root;
			break;
		}
		case UnsignedHexConst:
		{
			Block tmp171_AST = null;
			Block tmp171_AST_in = null;
			tmp171_AST = (Block)astFactory.create((Block)_t);
			tmp171_AST_in = (Block)_t;
			astFactory.addASTChild(currentAST, tmp171_AST);
			match(_t,UnsignedHexConst);
			_t = _t.getNextSibling();
			intConst_AST = (Block)currentAST.root;
			break;
		}
		default:
		{
			throw new NoViableAltException(_t);
		}
		}
		returnAST = intConst_AST;
		_retTree = _t;
	}
	
	protected final void floatConst(AST _t) throws RecognitionException {
		
		Block floatConst_AST_in = (_t == ASTNULL) ? null : (Block)_t;
		returnAST = null;
		ASTPair currentAST = new ASTPair();
		Block floatConst_AST = null;
		
		if (_t==null) _t=ASTNULL;
		switch ( _t.getType()) {
		case FloatDoubleConst:
		{
			Block tmp172_AST = null;
			Block tmp172_AST_in = null;
			tmp172_AST = (Block)astFactory.create((Block)_t);
			tmp172_AST_in = (Block)_t;
			astFactory.addASTChild(currentAST, tmp172_AST);
			match(_t,FloatDoubleConst);
			_t = _t.getNextSibling();
			floatConst_AST = (Block)currentAST.root;
			break;
		}
		case DoubleDoubleConst:
		{
			Block tmp173_AST = null;
			Block tmp173_AST_in = null;
			tmp173_AST = (Block)astFactory.create((Block)_t);
			tmp173_AST_in = (Block)_t;
			astFactory.addASTChild(currentAST, tmp173_AST);
			match(_t,DoubleDoubleConst);
			_t = _t.getNextSibling();
			floatConst_AST = (Block)currentAST.root;
			break;
		}
		case LongDoubleConst:
		{
			Block tmp174_AST = null;
			Block tmp174_AST_in = null;
			tmp174_AST = (Block)astFactory.create((Block)_t);
			tmp174_AST_in = (Block)_t;
			astFactory.addASTChild(currentAST, tmp174_AST);
			match(_t,LongDoubleConst);
			_t = _t.getNextSibling();
			floatConst_AST = (Block)currentAST.root;
			break;
		}
		default:
		{
			throw new NoViableAltException(_t);
		}
		}
		returnAST = floatConst_AST;
		_retTree = _t;
	}
	
	protected final void charConst(AST _t) throws RecognitionException {
		
		Block charConst_AST_in = (_t == ASTNULL) ? null : (Block)_t;
		returnAST = null;
		ASTPair currentAST = new ASTPair();
		Block charConst_AST = null;
		
		Block tmp175_AST = null;
		Block tmp175_AST_in = null;
		tmp175_AST = (Block)astFactory.create((Block)_t);
		tmp175_AST_in = (Block)_t;
		astFactory.addASTChild(currentAST, tmp175_AST);
		match(_t,CharLiteral);
		_t = _t.getNextSibling();
		charConst_AST = (Block)currentAST.root;
		returnAST = charConst_AST;
		_retTree = _t;
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
		"Number",
		"\"typeof\"",
		"\"__complex\"",
		"NInitializerElementLabel",
		"\"__label__\"",
		"\"__alignof\"",
		"\"__real\"",
		"\"__imag\""
	};
	
	private static final long[] mk_tokenSet_0() {
		long[] data = { 134153984L, 136314880L, 206158430208L, 0L, 0L, 0L};
		return data;
	}
	public static final BitSet _tokenSet_0 = new BitSet(mk_tokenSet_0());
	private static final long[] mk_tokenSet_1() {
		long[] data = { 0L, 7203507603979108352L, 67L, 0L, 0L, 0L};
		return data;
	}
	public static final BitSet _tokenSet_1 = new BitSet(mk_tokenSet_1());
	private static final long[] mk_tokenSet_2() {
		long[] data = { -1125895477657600L, 146509993606975L, 1099511627784L, 0L, 0L, 0L};
		return data;
	}
	public static final BitSet _tokenSet_2 = new BitSet(mk_tokenSet_2());
	}
	
