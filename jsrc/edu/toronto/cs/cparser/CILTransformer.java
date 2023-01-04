// $ANTLR 2.7.4: "CILTransformer.g" -> "CILTransformer.java"$
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

import edu.toronto.cs.expr.*;

import java.util.*;


public class CILTransformer extends antlr.TreeParser       implements CILTransformerTokenTypes
 {

        Map callNumMap;
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

        public void incNumCallSites (String funcName)
        {
          Integer numCalls = (Integer) callNumMap.get (funcName);
          if (numCalls == null)
            numCalls = new Integer (1);
          else
            numCalls = new Integer (numCalls.intValue () + 1);  
          callNumMap.put (funcName, numCalls);
        }

        public int getNumCallSites (String funcName)
        {
          Integer numCalls = (Integer) callNumMap.get (funcName);
          if (numCalls == null)
            return 0;
          else
            return numCalls.intValue ();  
        }

        public Map getCallNumMap ()
        {
          return callNumMap;
        }

        boolean stmtBlocking = false;
public CILTransformer() {
	tokenNames = _tokenNames;
}

	public final Expr  translationUnit(AST _t,
		ExprFactory fac, boolean _stmtBlocking
	) throws RecognitionException {
		Expr e;
		
		TNode translationUnit_AST_in = (_t == ASTNULL) ? null : (TNode)_t;
		returnAST = null;
		ASTPair currentAST = new ASTPair();
		TNode translationUnit_AST = null;
		
		e = null;
		callNumMap = new HashMap ();
		stmtBlocking = _stmtBlocking;
		
		
		{
		if (_t==null) _t=ASTNULL;
		switch ( _t.getType()) {
		case LITERAL_asm:
		case SEMI:
		case NDeclaration:
		case NFunctionDef:
		{
			e=externalList(_t,fac);
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
		translationUnit_AST = (TNode)currentAST.root;
		returnAST = translationUnit_AST;
		_retTree = _t;
		return e;
	}
	
	public final Expr  externalList(AST _t,
		ExprFactory fac
	) throws RecognitionException {
		Expr e;
		
		TNode externalList_AST_in = (_t == ASTNULL) ? null : (TNode)_t;
		returnAST = null;
		ASTPair currentAST = new ASTPair();
		TNode externalList_AST = null;
		
		e = null;
		List defList = new LinkedList ();
		Expr d;
		
		
		try {      // for error handling
			{
			int _cnt5=0;
			_loop5:
			do {
				if (_t==null) _t=ASTNULL;
				if ((_tokenSet_0.member(_t.getType()))) {
					d=externalDef(_t,fac);
					_t = _retTree;
					astFactory.addASTChild(currentAST, returnAST);
					if ( inputState.guessing==0 ) {
						defList.add (d);
					}
				}
				else {
					if ( _cnt5>=1 ) { break _loop5; } else {throw new NoViableAltException(_t);}
				}
				
				_cnt5++;
			} while (true);
			}
			if ( inputState.guessing==0 ) {
				e = fac.op (CILProgramOp.PROGRAM).naryApply (defList);
			}
			externalList_AST = (TNode)currentAST.root;
		}
		catch (RecognitionException ex) {
			if (inputState.guessing==0) {
				reportError(ex);
				if (_t!=null) {_t = _t.getNextSibling();}
			} else {
			  throw ex;
			}
		}
		returnAST = externalList_AST;
		_retTree = _t;
		return e;
	}
	
	public final Expr  externalDef(AST _t,
		ExprFactory fac
	) throws RecognitionException {
		Expr e;
		
		TNode externalDef_AST_in = (_t == ASTNULL) ? null : (TNode)_t;
		returnAST = null;
		ASTPair currentAST = new ASTPair();
		TNode externalDef_AST = null;
		e = null;
		
		try {      // for error handling
			if (_t==null) _t=ASTNULL;
			switch ( _t.getType()) {
			case NDeclaration:
			{
				e=declaration(_t,fac);
				_t = _retTree;
				astFactory.addASTChild(currentAST, returnAST);
				externalDef_AST = (TNode)currentAST.root;
				break;
			}
			case NFunctionDef:
			{
				e=functionDef(_t,fac);
				_t = _retTree;
				astFactory.addASTChild(currentAST, returnAST);
				externalDef_AST = (TNode)currentAST.root;
				break;
			}
			case LITERAL_asm:
			{
				e=asm_expr(_t,fac);
				_t = _retTree;
				astFactory.addASTChild(currentAST, returnAST);
				externalDef_AST = (TNode)currentAST.root;
				break;
			}
			case SEMI:
			{
				TNode tmp1_AST = null;
				TNode tmp1_AST_in = null;
				tmp1_AST = (TNode)astFactory.create((TNode)_t);
				tmp1_AST_in = (TNode)_t;
				astFactory.addASTChild(currentAST, tmp1_AST);
				match(_t,SEMI);
				_t = _t.getNextSibling();
				if ( inputState.guessing==0 ) {
					e = fac.op (CILEmptyStmtOp.EMPTY);
				}
				externalDef_AST = (TNode)currentAST.root;
				break;
			}
			default:
			{
				throw new NoViableAltException(_t);
			}
			}
		}
		catch (RecognitionException ex) {
			if (inputState.guessing==0) {
				reportError(ex);
				if (_t!=null) {_t = _t.getNextSibling();}
			} else {
			  throw ex;
			}
		}
		returnAST = externalDef_AST;
		_retTree = _t;
		return e;
	}
	
	public final Expr  declaration(AST _t,
		ExprFactory fac
	) throws RecognitionException {
		Expr e;
		
		TNode declaration_AST_in = (_t == ASTNULL) ? null : (TNode)_t;
		returnAST = null;
		ASTPair currentAST = new ASTPair();
		TNode declaration_AST = null;
		
		e = null;
		Expr ds;
		// Expr id = fac.op (CILNullOp.NULL);
		Expr id = null;
		
		
		try {      // for error handling
			AST __t13 = _t;
			TNode tmp2_AST = null;
			TNode tmp2_AST_in = null;
			tmp2_AST = (TNode)astFactory.create((TNode)_t);
			tmp2_AST_in = (TNode)_t;
			astFactory.addASTChild(currentAST, tmp2_AST);
			ASTPair __currentAST13 = currentAST.copy();
			currentAST.root = currentAST.child;
			currentAST.child = null;
			match(_t,NDeclaration);
			_t = _t.getFirstChild();
			ds=declSpecifiers(_t,fac);
			_t = _retTree;
			astFactory.addASTChild(currentAST, returnAST);
			{
			if (_t==null) _t=ASTNULL;
			switch ( _t.getType()) {
			case NInitDecl:
			{
				id=initDecl(_t,fac);
				_t = _retTree;
				astFactory.addASTChild(currentAST, returnAST);
				break;
			}
			case SEMI:
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
			int _cnt16=0;
			_loop16:
			do {
				if (_t==null) _t=ASTNULL;
				if ((_t.getType()==SEMI)) {
					TNode tmp3_AST = null;
					TNode tmp3_AST_in = null;
					tmp3_AST = (TNode)astFactory.create((TNode)_t);
					tmp3_AST_in = (TNode)_t;
					astFactory.addASTChild(currentAST, tmp3_AST);
					match(_t,SEMI);
					_t = _t.getNextSibling();
				}
				else {
					if ( _cnt16>=1 ) { break _loop16; } else {throw new NoViableAltException(_t);}
				}
				
				_cnt16++;
			} while (true);
			}
			currentAST = __currentAST13;
			_t = __t13;
			_t = _t.getNextSibling();
			if ( inputState.guessing==0 ) {
				
				// XXX should move pointerGroup in id into ds 
				e = fac.op (CILDeclarationOp.DECL).naryApply (new Expr[] {ds, id});
				
			}
			declaration_AST = (TNode)currentAST.root;
		}
		catch (RecognitionException ex) {
			if (inputState.guessing==0) {
				reportError(ex);
				if (_t!=null) {_t = _t.getNextSibling();}
			} else {
			  throw ex;
			}
		}
		returnAST = declaration_AST;
		_retTree = _t;
		return e;
	}
	
	public final Expr  functionDef(AST _t,
		ExprFactory fac
	) throws RecognitionException {
		Expr e;
		
		TNode functionDef_AST_in = (_t == ASTNULL) ? null : (TNode)_t;
		returnAST = null;
		ASTPair currentAST = new ASTPair();
		TNode functionDef_AST = null;
		
		e = null;
		Expr specs = null;
		Expr body = null;
		Expr sig;
		
		
		try {      // for error handling
			AST __t102 = _t;
			TNode tmp4_AST = null;
			TNode tmp4_AST_in = null;
			tmp4_AST = (TNode)astFactory.create((TNode)_t);
			tmp4_AST_in = (TNode)_t;
			astFactory.addASTChild(currentAST, tmp4_AST);
			ASTPair __currentAST102 = currentAST.copy();
			currentAST.root = currentAST.child;
			currentAST.child = null;
			match(_t,NFunctionDef);
			_t = _t.getFirstChild();
			{
			if (_t==null) _t=ASTNULL;
			switch ( _t.getType()) {
			case LITERAL_volatile:
			case LITERAL_struct:
			case LITERAL_union:
			case LITERAL_enum:
			case LITERAL_extern:
			case LITERAL_static:
			case LITERAL_const:
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
			case LITERAL_inline:
			case LITERAL___builtin_va_list:
			{
				specs=functionDeclSpecifiers(_t,fac);
				_t = _retTree;
				astFactory.addASTChild(currentAST, returnAST);
				break;
			}
			case NDeclarator:
			{
				break;
			}
			default:
			{
				throw new NoViableAltException(_t);
			}
			}
			}
			sig=declarator(_t,fac);
			_t = _retTree;
			astFactory.addASTChild(currentAST, returnAST);
			body=compoundStatement(_t,fac);
			_t = _retTree;
			astFactory.addASTChild(currentAST, returnAST);
			currentAST = __currentAST102;
			_t = __t102;
			_t = _t.getNextSibling();
			if ( inputState.guessing==0 ) {
				functionDef_AST = (TNode)currentAST.root;
				e = fac.op (CILFunctionDefOp.FUNCTION_DEF).naryApply
				(new Expr[]
				{
				specs,
				sig,
				body,
				fac.intExpr (getNumCallSites (
				CILFunctionDefOp.getFunctionNameFromSig (sig))),
				fac.intExpr (functionDef_AST.getLineNum ())
				}
				);
				
			}
			functionDef_AST = (TNode)currentAST.root;
		}
		catch (RecognitionException ex) {
			if (inputState.guessing==0) {
				reportError(ex);
				if (_t!=null) {_t = _t.getNextSibling();}
			} else {
			  throw ex;
			}
		}
		returnAST = functionDef_AST;
		_retTree = _t;
		return e;
	}
	
	public final Expr  asm_expr(AST _t,
		ExprFactory fac
	) throws RecognitionException {
		Expr e;
		
		TNode asm_expr_AST_in = (_t == ASTNULL) ? null : (TNode)_t;
		returnAST = null;
		ASTPair currentAST = new ASTPair();
		TNode asm_expr_AST = null;
		
		e = null;
		Expr discard;
		
		
		try {      // for error handling
			AST __t8 = _t;
			TNode tmp5_AST = null;
			TNode tmp5_AST_in = null;
			tmp5_AST = (TNode)astFactory.create((TNode)_t);
			tmp5_AST_in = (TNode)_t;
			astFactory.addASTChild(currentAST, tmp5_AST);
			ASTPair __currentAST8 = currentAST.copy();
			currentAST.root = currentAST.child;
			currentAST.child = null;
			match(_t,LITERAL_asm);
			_t = _t.getFirstChild();
			{
			if (_t==null) _t=ASTNULL;
			switch ( _t.getType()) {
			case LITERAL_volatile:
			{
				TNode tmp6_AST = null;
				TNode tmp6_AST_in = null;
				tmp6_AST = (TNode)astFactory.create((TNode)_t);
				tmp6_AST_in = (TNode)_t;
				astFactory.addASTChild(currentAST, tmp6_AST);
				match(_t,LITERAL_volatile);
				_t = _t.getNextSibling();
				break;
			}
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
			TNode tmp7_AST = null;
			TNode tmp7_AST_in = null;
			tmp7_AST = (TNode)astFactory.create((TNode)_t);
			tmp7_AST_in = (TNode)_t;
			astFactory.addASTChild(currentAST, tmp7_AST);
			match(_t,LCURLY);
			_t = _t.getNextSibling();
			discard=expr(_t,fac);
			_t = _retTree;
			astFactory.addASTChild(currentAST, returnAST);
			TNode tmp8_AST = null;
			TNode tmp8_AST_in = null;
			tmp8_AST = (TNode)astFactory.create((TNode)_t);
			tmp8_AST_in = (TNode)_t;
			astFactory.addASTChild(currentAST, tmp8_AST);
			match(_t,RCURLY);
			_t = _t.getNextSibling();
			{
			int _cnt11=0;
			_loop11:
			do {
				if (_t==null) _t=ASTNULL;
				if ((_t.getType()==SEMI)) {
					TNode tmp9_AST = null;
					TNode tmp9_AST_in = null;
					tmp9_AST = (TNode)astFactory.create((TNode)_t);
					tmp9_AST_in = (TNode)_t;
					astFactory.addASTChild(currentAST, tmp9_AST);
					match(_t,SEMI);
					_t = _t.getNextSibling();
				}
				else {
					if ( _cnt11>=1 ) { break _loop11; } else {throw new NoViableAltException(_t);}
				}
				
				_cnt11++;
			} while (true);
			}
			currentAST = __currentAST8;
			_t = __t8;
			_t = _t.getNextSibling();
			if ( inputState.guessing==0 ) {
				asm_expr_AST = (TNode)currentAST.root;
				e = fac.op (new JavaObjectOp (asm_expr_AST));
			}
			asm_expr_AST = (TNode)currentAST.root;
		}
		catch (RecognitionException ex) {
			if (inputState.guessing==0) {
				reportError(ex);
				if (_t!=null) {_t = _t.getNextSibling();}
			} else {
			  throw ex;
			}
		}
		returnAST = asm_expr_AST;
		_retTree = _t;
		return e;
	}
	
	public final Expr  expr(AST _t,
		ExprFactory fac
	) throws RecognitionException {
		Expr e;
		
		TNode expr_AST_in = (_t == ASTNULL) ? null : (TNode)_t;
		returnAST = null;
		ASTPair currentAST = new ASTPair();
		TNode expr_AST = null;
		e = null;
		
		try {      // for error handling
			if (_t==null) _t=ASTNULL;
			switch ( _t.getType()) {
			case ASSIGN:
			{
				e=assignExpr(_t,fac);
				_t = _retTree;
				astFactory.addASTChild(currentAST, returnAST);
				expr_AST = (TNode)currentAST.root;
				break;
			}
			case LOR:
			{
				e=logicalOrExpr(_t,fac);
				_t = _retTree;
				astFactory.addASTChild(currentAST, returnAST);
				expr_AST = (TNode)currentAST.root;
				break;
			}
			case LAND:
			{
				e=logicalAndExpr(_t,fac);
				_t = _retTree;
				astFactory.addASTChild(currentAST, returnAST);
				expr_AST = (TNode)currentAST.root;
				break;
			}
			case BOR:
			{
				inclusiveOrExpr(_t,fac);
				_t = _retTree;
				astFactory.addASTChild(currentAST, returnAST);
				expr_AST = (TNode)currentAST.root;
				break;
			}
			case BXOR:
			{
				exclusiveOrExpr(_t,fac);
				_t = _retTree;
				astFactory.addASTChild(currentAST, returnAST);
				expr_AST = (TNode)currentAST.root;
				break;
			}
			case BAND:
			{
				bitAndExpr(_t,fac);
				_t = _retTree;
				astFactory.addASTChild(currentAST, returnAST);
				expr_AST = (TNode)currentAST.root;
				break;
			}
			case LSHIFT:
			case RSHIFT:
			{
				shiftExpr(_t,fac);
				_t = _retTree;
				astFactory.addASTChild(currentAST, returnAST);
				expr_AST = (TNode)currentAST.root;
				break;
			}
			case EQUAL:
			case NOT_EQUAL:
			{
				e=equalityExpr(_t,fac);
				_t = _retTree;
				astFactory.addASTChild(currentAST, returnAST);
				expr_AST = (TNode)currentAST.root;
				break;
			}
			case LT:
			case LTE:
			case GT:
			case GTE:
			{
				e=relationalExpr(_t,fac);
				_t = _retTree;
				astFactory.addASTChild(currentAST, returnAST);
				expr_AST = (TNode)currentAST.root;
				break;
			}
			case PLUS:
			case MINUS:
			{
				e=additiveExpr(_t,fac);
				_t = _retTree;
				astFactory.addASTChild(currentAST, returnAST);
				expr_AST = (TNode)currentAST.root;
				break;
			}
			case STAR:
			case DIV:
			case MOD:
			{
				e=multExpr(_t,fac);
				_t = _retTree;
				astFactory.addASTChild(currentAST, returnAST);
				expr_AST = (TNode)currentAST.root;
				break;
			}
			case NCast:
			{
				e=castExpr(_t,fac);
				_t = _retTree;
				astFactory.addASTChild(currentAST, returnAST);
				expr_AST = (TNode)currentAST.root;
				break;
			}
			case INC:
			case DEC:
			case LITERAL_sizeof:
			case NUnaryExpr:
			{
				e=unaryExpr(_t,fac);
				_t = _retTree;
				astFactory.addASTChild(currentAST, returnAST);
				expr_AST = (TNode)currentAST.root;
				break;
			}
			case NPostfixExpr:
			{
				e=postfixExpr(_t,fac);
				_t = _retTree;
				astFactory.addASTChild(currentAST, returnAST);
				expr_AST = (TNode)currentAST.root;
				break;
			}
			case ID:
			case CharLiteral:
			case NExpressionGroup:
			case NStringSeq:
			case Number:
			{
				e=primaryExpr(_t,fac);
				_t = _retTree;
				astFactory.addASTChild(currentAST, returnAST);
				expr_AST = (TNode)currentAST.root;
				break;
			}
			case NInitializer:
			case NLcurlyInitializer:
			{
				e=initializer(_t,fac);
				_t = _retTree;
				astFactory.addASTChild(currentAST, returnAST);
				expr_AST = (TNode)currentAST.root;
				break;
			}
			case NGnuAsmExpr:
			{
				gnuAsmExpr(_t,fac);
				_t = _retTree;
				astFactory.addASTChild(currentAST, returnAST);
				expr_AST = (TNode)currentAST.root;
				break;
			}
			case LPAREN:
			{
				e=compoundStatementExpr(_t,fac);
				_t = _retTree;
				astFactory.addASTChild(currentAST, returnAST);
				expr_AST = (TNode)currentAST.root;
				break;
			}
			default:
			{
				throw new NoViableAltException(_t);
			}
			}
		}
		catch (RecognitionException ex) {
			if (inputState.guessing==0) {
				reportError(ex);
				if (_t!=null) {_t = _t.getNextSibling();}
			} else {
			  throw ex;
			}
		}
		returnAST = expr_AST;
		_retTree = _t;
		return e;
	}
	
	public final Expr  declSpecifiers(AST _t,
		ExprFactory fac
	) throws RecognitionException {
		Expr e;
		
		TNode declSpecifiers_AST_in = (_t == ASTNULL) ? null : (TNode)_t;
		returnAST = null;
		ASTPair currentAST = new ASTPair();
		TNode declSpecifiers_AST = null;
		
		e = null;
		List declSpecList = new LinkedList ();
		Expr scs;
		Expr tq;
		Expr ts;
		
		
		try {      // for error handling
			{
			int _cnt19=0;
			_loop19:
			do {
				if (_t==null) _t=ASTNULL;
				switch ( _t.getType()) {
				case LITERAL_typedef:
				case LITERAL_auto:
				case LITERAL_register:
				case LITERAL_extern:
				case LITERAL_static:
				case LITERAL_inline:
				{
					scs=storageClassSpecifier(_t,fac);
					_t = _retTree;
					astFactory.addASTChild(currentAST, returnAST);
					if ( inputState.guessing==0 ) {
						declSpecList.add (scs);
					}
					break;
				}
				case LITERAL_volatile:
				case LITERAL_const:
				{
					tq=typeQualifier(_t,fac);
					_t = _retTree;
					astFactory.addASTChild(currentAST, returnAST);
					if ( inputState.guessing==0 ) {
						declSpecList.add (tq);
					}
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
				case LITERAL___builtin_va_list:
				{
					ts=typeSpecifier(_t,fac);
					_t = _retTree;
					astFactory.addASTChild(currentAST, returnAST);
					if ( inputState.guessing==0 ) {
						declSpecList.add (ts);
					}
					break;
				}
				default:
				{
					if ( _cnt19>=1 ) { break _loop19; } else {throw new NoViableAltException(_t);}
				}
				}
				_cnt19++;
			} while (true);
			}
			if ( inputState.guessing==0 ) {
				e = fac.op (CILListOp.LIST).naryApply (declSpecList);
			}
			declSpecifiers_AST = (TNode)currentAST.root;
		}
		catch (RecognitionException ex) {
			if (inputState.guessing==0) {
				reportError(ex);
				if (_t!=null) {_t = _t.getNextSibling();}
			} else {
			  throw ex;
			}
		}
		returnAST = declSpecifiers_AST;
		_retTree = _t;
		return e;
	}
	
	public final Expr  initDecl(AST _t,
		ExprFactory fac
	) throws RecognitionException {
		Expr e;
		
		TNode initDecl_AST_in = (_t == ASTNULL) ? null : (TNode)_t;
		returnAST = null;
		ASTPair currentAST = new ASTPair();
		TNode initDecl_AST = null;
		
		// e = fac.op (CILNullOp.NULL);
		e = null;
		
		
		try {      // for error handling
			AST __t69 = _t;
			TNode tmp10_AST = null;
			TNode tmp10_AST_in = null;
			tmp10_AST = (TNode)astFactory.create((TNode)_t);
			tmp10_AST_in = (TNode)_t;
			astFactory.addASTChild(currentAST, tmp10_AST);
			ASTPair __currentAST69 = currentAST.copy();
			currentAST.root = currentAST.child;
			currentAST.child = null;
			match(_t,NInitDecl);
			_t = _t.getFirstChild();
			e=declarator(_t,fac);
			_t = _retTree;
			astFactory.addASTChild(currentAST, returnAST);
			currentAST = __currentAST69;
			_t = __t69;
			_t = _t.getNextSibling();
			initDecl_AST = (TNode)currentAST.root;
		}
		catch (RecognitionException ex) {
			if (inputState.guessing==0) {
				reportError(ex);
				if (_t!=null) {_t = _t.getNextSibling();}
			} else {
			  throw ex;
			}
		}
		returnAST = initDecl_AST;
		_retTree = _t;
		return e;
	}
	
	public final Expr  storageClassSpecifier(AST _t,
		ExprFactory fac
	) throws RecognitionException {
		Expr e;
		
		TNode storageClassSpecifier_AST_in = (_t == ASTNULL) ? null : (TNode)_t;
		returnAST = null;
		ASTPair currentAST = new ASTPair();
		TNode storageClassSpecifier_AST = null;
		e = fac.op (CILUnknownOp.UNKNOWN);
		
		try {      // for error handling
			if (_t==null) _t=ASTNULL;
			switch ( _t.getType()) {
			case LITERAL_auto:
			{
				TNode tmp11_AST = null;
				TNode tmp11_AST_in = null;
				tmp11_AST = (TNode)astFactory.create((TNode)_t);
				tmp11_AST_in = (TNode)_t;
				astFactory.addASTChild(currentAST, tmp11_AST);
				match(_t,LITERAL_auto);
				_t = _t.getNextSibling();
				storageClassSpecifier_AST = (TNode)currentAST.root;
				break;
			}
			case LITERAL_register:
			{
				TNode tmp12_AST = null;
				TNode tmp12_AST_in = null;
				tmp12_AST = (TNode)astFactory.create((TNode)_t);
				tmp12_AST_in = (TNode)_t;
				astFactory.addASTChild(currentAST, tmp12_AST);
				match(_t,LITERAL_register);
				_t = _t.getNextSibling();
				storageClassSpecifier_AST = (TNode)currentAST.root;
				break;
			}
			case LITERAL_typedef:
			{
				TNode tmp13_AST = null;
				TNode tmp13_AST_in = null;
				tmp13_AST = (TNode)astFactory.create((TNode)_t);
				tmp13_AST_in = (TNode)_t;
				astFactory.addASTChild(currentAST, tmp13_AST);
				match(_t,LITERAL_typedef);
				_t = _t.getNextSibling();
				storageClassSpecifier_AST = (TNode)currentAST.root;
				break;
			}
			case LITERAL_extern:
			case LITERAL_static:
			case LITERAL_inline:
			{
				e=functionStorageClassSpecifier(_t,fac);
				_t = _retTree;
				astFactory.addASTChild(currentAST, returnAST);
				storageClassSpecifier_AST = (TNode)currentAST.root;
				break;
			}
			default:
			{
				throw new NoViableAltException(_t);
			}
			}
		}
		catch (RecognitionException ex) {
			if (inputState.guessing==0) {
				reportError(ex);
				if (_t!=null) {_t = _t.getNextSibling();}
			} else {
			  throw ex;
			}
		}
		returnAST = storageClassSpecifier_AST;
		_retTree = _t;
		return e;
	}
	
	public final Expr  typeQualifier(AST _t,
		ExprFactory fac
	) throws RecognitionException {
		Expr e;
		
		TNode typeQualifier_AST_in = (_t == ASTNULL) ? null : (TNode)_t;
		returnAST = null;
		ASTPair currentAST = new ASTPair();
		TNode typeQualifier_AST = null;
		e = fac.op (CILUnknownOp.UNKNOWN);
		
		try {      // for error handling
			if (_t==null) _t=ASTNULL;
			switch ( _t.getType()) {
			case LITERAL_const:
			{
				TNode tmp14_AST = null;
				TNode tmp14_AST_in = null;
				tmp14_AST = (TNode)astFactory.create((TNode)_t);
				tmp14_AST_in = (TNode)_t;
				astFactory.addASTChild(currentAST, tmp14_AST);
				match(_t,LITERAL_const);
				_t = _t.getNextSibling();
				if ( inputState.guessing==0 ) {
					e = fac.op (CILTypeSpecifierOp.CONST);
				}
				typeQualifier_AST = (TNode)currentAST.root;
				break;
			}
			case LITERAL_volatile:
			{
				TNode tmp15_AST = null;
				TNode tmp15_AST_in = null;
				tmp15_AST = (TNode)astFactory.create((TNode)_t);
				tmp15_AST_in = (TNode)_t;
				astFactory.addASTChild(currentAST, tmp15_AST);
				match(_t,LITERAL_volatile);
				_t = _t.getNextSibling();
				typeQualifier_AST = (TNode)currentAST.root;
				break;
			}
			default:
			{
				throw new NoViableAltException(_t);
			}
			}
		}
		catch (RecognitionException ex) {
			if (inputState.guessing==0) {
				reportError(ex);
				if (_t!=null) {_t = _t.getNextSibling();}
			} else {
			  throw ex;
			}
		}
		returnAST = typeQualifier_AST;
		_retTree = _t;
		return e;
	}
	
	public final Expr  typeSpecifier(AST _t,
		ExprFactory fac
	) throws RecognitionException {
		Expr e;
		
		TNode typeSpecifier_AST_in = (_t == ASTNULL) ? null : (TNode)_t;
		returnAST = null;
		ASTPair currentAST = new ASTPair();
		TNode typeSpecifier_AST = null;
		
		e = null;
		Expr discard;
		
		
		try {      // for error handling
			if (_t==null) _t=ASTNULL;
			switch ( _t.getType()) {
			case LITERAL_void:
			{
				TNode tmp16_AST = null;
				TNode tmp16_AST_in = null;
				tmp16_AST = (TNode)astFactory.create((TNode)_t);
				tmp16_AST_in = (TNode)_t;
				astFactory.addASTChild(currentAST, tmp16_AST);
				match(_t,LITERAL_void);
				_t = _t.getNextSibling();
				if ( inputState.guessing==0 ) {
					e = fac.op (CILTypeSpecifierOp.VOID);
				}
				typeSpecifier_AST = (TNode)currentAST.root;
				break;
			}
			case LITERAL_char:
			{
				TNode tmp17_AST = null;
				TNode tmp17_AST_in = null;
				tmp17_AST = (TNode)astFactory.create((TNode)_t);
				tmp17_AST_in = (TNode)_t;
				astFactory.addASTChild(currentAST, tmp17_AST);
				match(_t,LITERAL_char);
				_t = _t.getNextSibling();
				if ( inputState.guessing==0 ) {
					e = fac.op (CILTypeSpecifierOp.CHAR);
				}
				typeSpecifier_AST = (TNode)currentAST.root;
				break;
			}
			case LITERAL_short:
			{
				TNode tmp18_AST = null;
				TNode tmp18_AST_in = null;
				tmp18_AST = (TNode)astFactory.create((TNode)_t);
				tmp18_AST_in = (TNode)_t;
				astFactory.addASTChild(currentAST, tmp18_AST);
				match(_t,LITERAL_short);
				_t = _t.getNextSibling();
				if ( inputState.guessing==0 ) {
					e = fac.op (CILTypeSpecifierOp.SHORT);
				}
				typeSpecifier_AST = (TNode)currentAST.root;
				break;
			}
			case LITERAL_int:
			{
				TNode tmp19_AST = null;
				TNode tmp19_AST_in = null;
				tmp19_AST = (TNode)astFactory.create((TNode)_t);
				tmp19_AST_in = (TNode)_t;
				astFactory.addASTChild(currentAST, tmp19_AST);
				match(_t,LITERAL_int);
				_t = _t.getNextSibling();
				if ( inputState.guessing==0 ) {
					e = fac.op (CILTypeSpecifierOp.INT);
				}
				typeSpecifier_AST = (TNode)currentAST.root;
				break;
			}
			case LITERAL_long:
			{
				TNode tmp20_AST = null;
				TNode tmp20_AST_in = null;
				tmp20_AST = (TNode)astFactory.create((TNode)_t);
				tmp20_AST_in = (TNode)_t;
				astFactory.addASTChild(currentAST, tmp20_AST);
				match(_t,LITERAL_long);
				_t = _t.getNextSibling();
				if ( inputState.guessing==0 ) {
					e = fac.op (CILTypeSpecifierOp.LONG);
				}
				typeSpecifier_AST = (TNode)currentAST.root;
				break;
			}
			case LITERAL_float:
			{
				TNode tmp21_AST = null;
				TNode tmp21_AST_in = null;
				tmp21_AST = (TNode)astFactory.create((TNode)_t);
				tmp21_AST_in = (TNode)_t;
				astFactory.addASTChild(currentAST, tmp21_AST);
				match(_t,LITERAL_float);
				_t = _t.getNextSibling();
				if ( inputState.guessing==0 ) {
					e = fac.op (CILTypeSpecifierOp.FLOAT);
				}
				typeSpecifier_AST = (TNode)currentAST.root;
				break;
			}
			case LITERAL_double:
			{
				TNode tmp22_AST = null;
				TNode tmp22_AST_in = null;
				tmp22_AST = (TNode)astFactory.create((TNode)_t);
				tmp22_AST_in = (TNode)_t;
				astFactory.addASTChild(currentAST, tmp22_AST);
				match(_t,LITERAL_double);
				_t = _t.getNextSibling();
				if ( inputState.guessing==0 ) {
					e = fac.op (CILTypeSpecifierOp.DOUBLE);
				}
				typeSpecifier_AST = (TNode)currentAST.root;
				break;
			}
			case LITERAL_signed:
			{
				TNode tmp23_AST = null;
				TNode tmp23_AST_in = null;
				tmp23_AST = (TNode)astFactory.create((TNode)_t);
				tmp23_AST_in = (TNode)_t;
				astFactory.addASTChild(currentAST, tmp23_AST);
				match(_t,LITERAL_signed);
				_t = _t.getNextSibling();
				if ( inputState.guessing==0 ) {
					e = fac.op (CILTypeSpecifierOp.SIGNED);
				}
				typeSpecifier_AST = (TNode)currentAST.root;
				break;
			}
			case LITERAL_unsigned:
			{
				TNode tmp24_AST = null;
				TNode tmp24_AST_in = null;
				tmp24_AST = (TNode)astFactory.create((TNode)_t);
				tmp24_AST_in = (TNode)_t;
				astFactory.addASTChild(currentAST, tmp24_AST);
				match(_t,LITERAL_unsigned);
				_t = _t.getNextSibling();
				if ( inputState.guessing==0 ) {
					e = fac.op (CILTypeSpecifierOp.UNSIGNED);
				}
				typeSpecifier_AST = (TNode)currentAST.root;
				break;
			}
			case LITERAL_struct:
			{
				e=structSpecifier(_t,fac);
				_t = _retTree;
				astFactory.addASTChild(currentAST, returnAST);
				{
				_loop25:
				do {
					if (_t==null) _t=ASTNULL;
					if ((_t.getType()==NAsmAttribute||_t.getType()==LITERAL___attribute)) {
						discard=attributeDecl(_t,fac);
						_t = _retTree;
						astFactory.addASTChild(currentAST, returnAST);
					}
					else {
						break _loop25;
					}
					
				} while (true);
				}
				typeSpecifier_AST = (TNode)currentAST.root;
				break;
			}
			case LITERAL_union:
			{
				e=unionSpecifier(_t,fac);
				_t = _retTree;
				astFactory.addASTChild(currentAST, returnAST);
				{
				_loop27:
				do {
					if (_t==null) _t=ASTNULL;
					if ((_t.getType()==NAsmAttribute||_t.getType()==LITERAL___attribute)) {
						discard=attributeDecl(_t,fac);
						_t = _retTree;
						astFactory.addASTChild(currentAST, returnAST);
					}
					else {
						break _loop27;
					}
					
				} while (true);
				}
				typeSpecifier_AST = (TNode)currentAST.root;
				break;
			}
			case LITERAL_enum:
			{
				e=enumSpecifier(_t,fac);
				_t = _retTree;
				astFactory.addASTChild(currentAST, returnAST);
				typeSpecifier_AST = (TNode)currentAST.root;
				break;
			}
			case NTypedefName:
			{
				typedefName(_t);
				_t = _retTree;
				astFactory.addASTChild(currentAST, returnAST);
				typeSpecifier_AST = (TNode)currentAST.root;
				break;
			}
			case LITERAL___builtin_va_list:
			{
				TNode tmp25_AST = null;
				TNode tmp25_AST_in = null;
				tmp25_AST = (TNode)astFactory.create((TNode)_t);
				tmp25_AST_in = (TNode)_t;
				astFactory.addASTChild(currentAST, tmp25_AST);
				match(_t,LITERAL___builtin_va_list);
				_t = _t.getNextSibling();
				if ( inputState.guessing==0 ) {
					e = fac.op (CILTypeSpecifierOp.VOID);
				}
				typeSpecifier_AST = (TNode)currentAST.root;
				break;
			}
			default:
			{
				throw new NoViableAltException(_t);
			}
			}
		}
		catch (RecognitionException ex) {
			if (inputState.guessing==0) {
				reportError(ex);
				if (_t!=null) {_t = _t.getNextSibling();}
			} else {
			  throw ex;
			}
		}
		returnAST = typeSpecifier_AST;
		_retTree = _t;
		return e;
	}
	
	public final Expr  functionStorageClassSpecifier(AST _t,
		ExprFactory fac
	) throws RecognitionException {
		Expr e;
		
		TNode functionStorageClassSpecifier_AST_in = (_t == ASTNULL) ? null : (TNode)_t;
		returnAST = null;
		ASTPair currentAST = new ASTPair();
		TNode functionStorageClassSpecifier_AST = null;
		e = fac.op (CILUnknownOp.UNKNOWN);
		
		try {      // for error handling
			if (_t==null) _t=ASTNULL;
			switch ( _t.getType()) {
			case LITERAL_extern:
			{
				TNode tmp26_AST = null;
				TNode tmp26_AST_in = null;
				tmp26_AST = (TNode)astFactory.create((TNode)_t);
				tmp26_AST_in = (TNode)_t;
				astFactory.addASTChild(currentAST, tmp26_AST);
				match(_t,LITERAL_extern);
				_t = _t.getNextSibling();
				functionStorageClassSpecifier_AST = (TNode)currentAST.root;
				break;
			}
			case LITERAL_static:
			{
				TNode tmp27_AST = null;
				TNode tmp27_AST_in = null;
				tmp27_AST = (TNode)astFactory.create((TNode)_t);
				tmp27_AST_in = (TNode)_t;
				astFactory.addASTChild(currentAST, tmp27_AST);
				match(_t,LITERAL_static);
				_t = _t.getNextSibling();
				functionStorageClassSpecifier_AST = (TNode)currentAST.root;
				break;
			}
			case LITERAL_inline:
			{
				TNode tmp28_AST = null;
				TNode tmp28_AST_in = null;
				tmp28_AST = (TNode)astFactory.create((TNode)_t);
				tmp28_AST_in = (TNode)_t;
				astFactory.addASTChild(currentAST, tmp28_AST);
				match(_t,LITERAL_inline);
				_t = _t.getNextSibling();
				functionStorageClassSpecifier_AST = (TNode)currentAST.root;
				break;
			}
			default:
			{
				throw new NoViableAltException(_t);
			}
			}
		}
		catch (RecognitionException ex) {
			if (inputState.guessing==0) {
				reportError(ex);
				if (_t!=null) {_t = _t.getNextSibling();}
			} else {
			  throw ex;
			}
		}
		returnAST = functionStorageClassSpecifier_AST;
		_retTree = _t;
		return e;
	}
	
	public final Expr  structSpecifier(AST _t,
		ExprFactory fac
	) throws RecognitionException {
		Expr e;
		
		TNode structSpecifier_AST_in = (_t == ASTNULL) ? null : (TNode)_t;
		returnAST = null;
		ASTPair currentAST = new ASTPair();
		TNode structSpecifier_AST = null;
		
		e = null;
		Expr body;
		
		
		try {      // for error handling
			AST __t31 = _t;
			TNode tmp29_AST = null;
			TNode tmp29_AST_in = null;
			tmp29_AST = (TNode)astFactory.create((TNode)_t);
			tmp29_AST_in = (TNode)_t;
			astFactory.addASTChild(currentAST, tmp29_AST);
			ASTPair __currentAST31 = currentAST.copy();
			currentAST.root = currentAST.child;
			currentAST.child = null;
			match(_t,LITERAL_struct);
			_t = _t.getFirstChild();
			body=structOrUnionBody(_t,fac);
			_t = _retTree;
			astFactory.addASTChild(currentAST, returnAST);
			currentAST = __currentAST31;
			_t = __t31;
			_t = _t.getNextSibling();
			if ( inputState.guessing==0 ) {
				
				e = fac.op (CILRecordOp.STRUCT).naryApply (body.args ());
				
			}
			structSpecifier_AST = (TNode)currentAST.root;
		}
		catch (RecognitionException ex) {
			if (inputState.guessing==0) {
				reportError(ex);
				if (_t!=null) {_t = _t.getNextSibling();}
			} else {
			  throw ex;
			}
		}
		returnAST = structSpecifier_AST;
		_retTree = _t;
		return e;
	}
	
	public final Expr  attributeDecl(AST _t,
		ExprFactory fac
	) throws RecognitionException {
		Expr e;
		
		TNode attributeDecl_AST_in = (_t == ASTNULL) ? null : (TNode)_t;
		returnAST = null;
		ASTPair currentAST = new ASTPair();
		TNode attributeDecl_AST = null;
		
		e = null;
		Expr discard;
		
		
		try {      // for error handling
			if (_t==null) _t=ASTNULL;
			switch ( _t.getType()) {
			case LITERAL___attribute:
			{
				AST __t61 = _t;
				TNode tmp30_AST = null;
				TNode tmp30_AST_in = null;
				tmp30_AST = (TNode)astFactory.create((TNode)_t);
				tmp30_AST_in = (TNode)_t;
				astFactory.addASTChild(currentAST, tmp30_AST);
				ASTPair __currentAST61 = currentAST.copy();
				currentAST.root = currentAST.child;
				currentAST.child = null;
				match(_t,LITERAL___attribute);
				_t = _t.getFirstChild();
				{
				_loop63:
				do {
					if (_t==null) _t=ASTNULL;
					if (((_t.getType() >= LITERAL_typedef && _t.getType() <= LITERAL___imag))) {
						TNode tmp31_AST = null;
						TNode tmp31_AST_in = null;
						tmp31_AST = (TNode)astFactory.create((TNode)_t);
						tmp31_AST_in = (TNode)_t;
						astFactory.addASTChild(currentAST, tmp31_AST);
						if ( _t==null ) throw new MismatchedTokenException();
						_t = _t.getNextSibling();
					}
					else {
						break _loop63;
					}
					
				} while (true);
				}
				currentAST = __currentAST61;
				_t = __t61;
				_t = _t.getNextSibling();
				attributeDecl_AST = (TNode)currentAST.root;
				break;
			}
			case NAsmAttribute:
			{
				AST __t64 = _t;
				TNode tmp32_AST = null;
				TNode tmp32_AST_in = null;
				tmp32_AST = (TNode)astFactory.create((TNode)_t);
				tmp32_AST_in = (TNode)_t;
				astFactory.addASTChild(currentAST, tmp32_AST);
				ASTPair __currentAST64 = currentAST.copy();
				currentAST.root = currentAST.child;
				currentAST.child = null;
				match(_t,NAsmAttribute);
				_t = _t.getFirstChild();
				TNode tmp33_AST = null;
				TNode tmp33_AST_in = null;
				tmp33_AST = (TNode)astFactory.create((TNode)_t);
				tmp33_AST_in = (TNode)_t;
				astFactory.addASTChild(currentAST, tmp33_AST);
				match(_t,LPAREN);
				_t = _t.getNextSibling();
				discard=expr(_t,fac);
				_t = _retTree;
				astFactory.addASTChild(currentAST, returnAST);
				TNode tmp34_AST = null;
				TNode tmp34_AST_in = null;
				tmp34_AST = (TNode)astFactory.create((TNode)_t);
				tmp34_AST_in = (TNode)_t;
				astFactory.addASTChild(currentAST, tmp34_AST);
				match(_t,RPAREN);
				_t = _t.getNextSibling();
				currentAST = __currentAST64;
				_t = __t64;
				_t = _t.getNextSibling();
				attributeDecl_AST = (TNode)currentAST.root;
				break;
			}
			default:
			{
				throw new NoViableAltException(_t);
			}
			}
		}
		catch (RecognitionException ex) {
			if (inputState.guessing==0) {
				reportError(ex);
				if (_t!=null) {_t = _t.getNextSibling();}
			} else {
			  throw ex;
			}
		}
		returnAST = attributeDecl_AST;
		_retTree = _t;
		return e;
	}
	
	public final Expr  unionSpecifier(AST _t,
		ExprFactory fac
	) throws RecognitionException {
		Expr e;
		
		TNode unionSpecifier_AST_in = (_t == ASTNULL) ? null : (TNode)_t;
		returnAST = null;
		ASTPair currentAST = new ASTPair();
		TNode unionSpecifier_AST = null;
		
		e = null;
		Expr body;
		
		
		try {      // for error handling
			AST __t33 = _t;
			TNode tmp35_AST = null;
			TNode tmp35_AST_in = null;
			tmp35_AST = (TNode)astFactory.create((TNode)_t);
			tmp35_AST_in = (TNode)_t;
			astFactory.addASTChild(currentAST, tmp35_AST);
			ASTPair __currentAST33 = currentAST.copy();
			currentAST.root = currentAST.child;
			currentAST.child = null;
			match(_t,LITERAL_union);
			_t = _t.getFirstChild();
			body=structOrUnionBody(_t,fac);
			_t = _retTree;
			astFactory.addASTChild(currentAST, returnAST);
			currentAST = __currentAST33;
			_t = __t33;
			_t = _t.getNextSibling();
			if ( inputState.guessing==0 ) {
				e = fac.op (CILRecordOp.STRUCT).naryApply (body.args ());
			}
			unionSpecifier_AST = (TNode)currentAST.root;
		}
		catch (RecognitionException ex) {
			if (inputState.guessing==0) {
				reportError(ex);
				if (_t!=null) {_t = _t.getNextSibling();}
			} else {
			  throw ex;
			}
		}
		returnAST = unionSpecifier_AST;
		_retTree = _t;
		return e;
	}
	
	public final Expr  enumSpecifier(AST _t,
		ExprFactory fac
	) throws RecognitionException {
		Expr e;
		
		TNode enumSpecifier_AST_in = (_t == ASTNULL) ? null : (TNode)_t;
		returnAST = null;
		ASTPair currentAST = new ASTPair();
		TNode enumSpecifier_AST = null;
		TNode id = null;
		TNode id_AST = null;
		
		e = null;
		Expr enList;
		
		
		try {      // for error handling
			AST __t53 = _t;
			TNode tmp36_AST = null;
			TNode tmp36_AST_in = null;
			tmp36_AST = (TNode)astFactory.create((TNode)_t);
			tmp36_AST_in = (TNode)_t;
			astFactory.addASTChild(currentAST, tmp36_AST);
			ASTPair __currentAST53 = currentAST.copy();
			currentAST.root = currentAST.child;
			currentAST.child = null;
			match(_t,LITERAL_enum);
			_t = _t.getFirstChild();
			id = (TNode)_t;
			TNode id_AST_in = null;
			id_AST = (TNode)astFactory.create(id);
			astFactory.addASTChild(currentAST, id_AST);
			match(_t,ID);
			_t = _t.getNextSibling();
			if ( inputState.guessing==0 ) {
				e = fac.var (id.getText ());
			}
			{
			if (_t==null) _t=ASTNULL;
			switch ( _t.getType()) {
			case LCURLY:
			{
				TNode tmp37_AST = null;
				TNode tmp37_AST_in = null;
				tmp37_AST = (TNode)astFactory.create((TNode)_t);
				tmp37_AST_in = (TNode)_t;
				astFactory.addASTChild(currentAST, tmp37_AST);
				match(_t,LCURLY);
				_t = _t.getNextSibling();
				enList=enumList(_t,fac);
				_t = _retTree;
				astFactory.addASTChild(currentAST, returnAST);
				TNode tmp38_AST = null;
				TNode tmp38_AST_in = null;
				tmp38_AST = (TNode)astFactory.create((TNode)_t);
				tmp38_AST_in = (TNode)_t;
				astFactory.addASTChild(currentAST, tmp38_AST);
				match(_t,RCURLY);
				_t = _t.getNextSibling();
				if ( inputState.guessing==0 ) {
					e = fac.op (CILListOp.LIST).binApply (e, enList);
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
			if ( inputState.guessing==0 ) {
				e = fac.op (CILEnumOp.ENUM).naryApply (e.args ());
			}
			currentAST = __currentAST53;
			_t = __t53;
			_t = _t.getNextSibling();
			enumSpecifier_AST = (TNode)currentAST.root;
		}
		catch (RecognitionException ex) {
			if (inputState.guessing==0) {
				reportError(ex);
				if (_t!=null) {_t = _t.getNextSibling();}
			} else {
			  throw ex;
			}
		}
		returnAST = enumSpecifier_AST;
		_retTree = _t;
		return e;
	}
	
	public final void typedefName(AST _t) throws RecognitionException {
		
		TNode typedefName_AST_in = (_t == ASTNULL) ? null : (TNode)_t;
		returnAST = null;
		ASTPair currentAST = new ASTPair();
		TNode typedefName_AST = null;
		
		try {      // for error handling
			AST __t29 = _t;
			TNode tmp39_AST = null;
			TNode tmp39_AST_in = null;
			tmp39_AST = (TNode)astFactory.create((TNode)_t);
			tmp39_AST_in = (TNode)_t;
			astFactory.addASTChild(currentAST, tmp39_AST);
			ASTPair __currentAST29 = currentAST.copy();
			currentAST.root = currentAST.child;
			currentAST.child = null;
			match(_t,NTypedefName);
			_t = _t.getFirstChild();
			TNode tmp40_AST = null;
			TNode tmp40_AST_in = null;
			tmp40_AST = (TNode)astFactory.create((TNode)_t);
			tmp40_AST_in = (TNode)_t;
			astFactory.addASTChild(currentAST, tmp40_AST);
			match(_t,ID);
			_t = _t.getNextSibling();
			currentAST = __currentAST29;
			_t = __t29;
			_t = _t.getNextSibling();
			typedefName_AST = (TNode)currentAST.root;
		}
		catch (RecognitionException ex) {
			if (inputState.guessing==0) {
				reportError(ex);
				if (_t!=null) {_t = _t.getNextSibling();}
			} else {
			  throw ex;
			}
		}
		returnAST = typedefName_AST;
		_retTree = _t;
	}
	
	public final Expr  structOrUnionBody(AST _t,
		ExprFactory fac
	) throws RecognitionException {
		Expr e;
		
		TNode structOrUnionBody_AST_in = (_t == ASTNULL) ? null : (TNode)_t;
		returnAST = null;
		ASTPair currentAST = new ASTPair();
		TNode structOrUnionBody_AST = null;
		TNode id1 = null;
		TNode id1_AST = null;
		TNode id2 = null;
		TNode id2_AST = null;
		
		// e = fac.op (CILNullOp.NULL);
		e =  null;
		Expr sdl;
		
		
		try {      // for error handling
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
					match(_t,LCURLY);
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
				id1 = (TNode)_t;
				TNode id1_AST_in = null;
				id1_AST = (TNode)astFactory.create(id1);
				astFactory.addASTChild(currentAST, id1_AST);
				match(_t,ID);
				_t = _t.getNextSibling();
				TNode tmp41_AST = null;
				TNode tmp41_AST_in = null;
				tmp41_AST = (TNode)astFactory.create((TNode)_t);
				tmp41_AST_in = (TNode)_t;
				astFactory.addASTChild(currentAST, tmp41_AST);
				match(_t,LCURLY);
				_t = _t.getNextSibling();
				if ( inputState.guessing==0 ) {
					e = fac.var (id1.getText ());
				}
				{
				if (_t==null) _t=ASTNULL;
				switch ( _t.getType()) {
				case LITERAL_volatile:
				case LITERAL_struct:
				case LITERAL_union:
				case LITERAL_enum:
				case LITERAL_const:
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
				case LITERAL___builtin_va_list:
				{
					sdl=structDeclarationList(_t,fac);
					_t = _retTree;
					astFactory.addASTChild(currentAST, returnAST);
					if ( inputState.guessing==0 ) {
						e = fac.op (CILListOp.LIST).binApply (e, sdl);
					}
					break;
				}
				case RCURLY:
				{
					break;
				}
				default:
				{
					throw new NoViableAltException(_t);
				}
				}
				}
				TNode tmp42_AST = null;
				TNode tmp42_AST_in = null;
				tmp42_AST = (TNode)astFactory.create((TNode)_t);
				tmp42_AST_in = (TNode)_t;
				astFactory.addASTChild(currentAST, tmp42_AST);
				match(_t,RCURLY);
				_t = _t.getNextSibling();
			}
			else if ((_t.getType()==ID)) {
				id2 = (TNode)_t;
				TNode id2_AST_in = null;
				id2_AST = (TNode)astFactory.create(id2);
				astFactory.addASTChild(currentAST, id2_AST);
				match(_t,ID);
				_t = _t.getNextSibling();
				if ( inputState.guessing==0 ) {
					
					e = fac.op (CILListOp.LIST).binApply
					(fac.var (id2.getText ()), null /* for uniformity */);
				}
			}
			else {
				throw new NoViableAltException(_t);
			}
			
			}
			structOrUnionBody_AST = (TNode)currentAST.root;
		}
		catch (RecognitionException ex) {
			if (inputState.guessing==0) {
				reportError(ex);
				if (_t!=null) {_t = _t.getNextSibling();}
			} else {
			  throw ex;
			}
		}
		returnAST = structOrUnionBody_AST;
		_retTree = _t;
		return e;
	}
	
	public final Expr  structDeclarationList(AST _t,
		ExprFactory fac
	) throws RecognitionException {
		Expr e;
		
		TNode structDeclarationList_AST_in = (_t == ASTNULL) ? null : (TNode)_t;
		returnAST = null;
		ASTPair currentAST = new ASTPair();
		TNode structDeclarationList_AST = null;
		
		e = null;
		Expr sd;
		List sdList = new LinkedList ();
		
		
		try {      // for error handling
			{
			int _cnt41=0;
			_loop41:
			do {
				if (_t==null) _t=ASTNULL;
				if ((_tokenSet_1.member(_t.getType()))) {
					sd=structDeclaration(_t,fac);
					_t = _retTree;
					astFactory.addASTChild(currentAST, returnAST);
					if ( inputState.guessing==0 ) {
						sdList.add (sd);
					}
				}
				else {
					if ( _cnt41>=1 ) { break _loop41; } else {throw new NoViableAltException(_t);}
				}
				
				_cnt41++;
			} while (true);
			}
			if ( inputState.guessing==0 ) {
				e = fac.op (CILListOp.LIST).naryApply (sdList);
			}
			structDeclarationList_AST = (TNode)currentAST.root;
		}
		catch (RecognitionException ex) {
			if (inputState.guessing==0) {
				reportError(ex);
				if (_t!=null) {_t = _t.getNextSibling();}
			} else {
			  throw ex;
			}
		}
		returnAST = structDeclarationList_AST;
		_retTree = _t;
		return e;
	}
	
	public final Expr  structDeclaration(AST _t,
		ExprFactory fac
	) throws RecognitionException {
		Expr e;
		
		TNode structDeclaration_AST_in = (_t == ASTNULL) ? null : (TNode)_t;
		returnAST = null;
		ASTPair currentAST = new ASTPair();
		TNode structDeclaration_AST = null;
		
		e = null;
		Expr sql;
		Expr sd;
		
		
		try {      // for error handling
			sql=specifierQualifierList(_t,fac);
			_t = _retTree;
			astFactory.addASTChild(currentAST, returnAST);
			sd=structDeclarator(_t,fac);
			_t = _retTree;
			astFactory.addASTChild(currentAST, returnAST);
			if ( inputState.guessing==0 ) {
				e = fac.op (CILDeclarationOp.DECL).naryApply (new Expr[] {sql, sd});
			}
			structDeclaration_AST = (TNode)currentAST.root;
		}
		catch (RecognitionException ex) {
			if (inputState.guessing==0) {
				reportError(ex);
				if (_t!=null) {_t = _t.getNextSibling();}
			} else {
			  throw ex;
			}
		}
		returnAST = structDeclaration_AST;
		_retTree = _t;
		return e;
	}
	
	public final Expr  specifierQualifierList(AST _t,
		ExprFactory fac
	) throws RecognitionException {
		Expr e;
		
		TNode specifierQualifierList_AST_in = (_t == ASTNULL) ? null : (TNode)_t;
		returnAST = null;
		ASTPair currentAST = new ASTPair();
		TNode specifierQualifierList_AST = null;
		
		e = null;
		List sqList = new LinkedList ();
		Expr ts;
		Expr tq;
		
		
		try {      // for error handling
			{
			int _cnt45=0;
			_loop45:
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
				case LITERAL___builtin_va_list:
				{
					ts=typeSpecifier(_t,fac);
					_t = _retTree;
					astFactory.addASTChild(currentAST, returnAST);
					if ( inputState.guessing==0 ) {
						sqList.add (ts);
					}
					break;
				}
				case LITERAL_volatile:
				case LITERAL_const:
				{
					tq=typeQualifier(_t,fac);
					_t = _retTree;
					astFactory.addASTChild(currentAST, returnAST);
					if ( inputState.guessing==0 ) {
						sqList.add (tq);
					}
					break;
				}
				default:
				{
					if ( _cnt45>=1 ) { break _loop45; } else {throw new NoViableAltException(_t);}
				}
				}
				_cnt45++;
			} while (true);
			}
			if ( inputState.guessing==0 ) {
				e = fac.op (CILListOp.LIST).naryApply (sqList);
			}
			specifierQualifierList_AST = (TNode)currentAST.root;
		}
		catch (RecognitionException ex) {
			if (inputState.guessing==0) {
				reportError(ex);
				if (_t!=null) {_t = _t.getNextSibling();}
			} else {
			  throw ex;
			}
		}
		returnAST = specifierQualifierList_AST;
		_retTree = _t;
		return e;
	}
	
	public final Expr  structDeclarator(AST _t,
		ExprFactory fac
	) throws RecognitionException {
		Expr e;
		
		TNode structDeclarator_AST_in = (_t == ASTNULL) ? null : (TNode)_t;
		returnAST = null;
		ASTPair currentAST = new ASTPair();
		TNode structDeclarator_AST = null;
		
		e = null;
		Expr discard;
		
		
		try {      // for error handling
			AST __t47 = _t;
			TNode tmp43_AST = null;
			TNode tmp43_AST_in = null;
			tmp43_AST = (TNode)astFactory.create((TNode)_t);
			tmp43_AST_in = (TNode)_t;
			astFactory.addASTChild(currentAST, tmp43_AST);
			ASTPair __currentAST47 = currentAST.copy();
			currentAST.root = currentAST.child;
			currentAST.child = null;
			match(_t,NStructDeclarator);
			_t = _t.getFirstChild();
			{
			if (_t==null) _t=ASTNULL;
			switch ( _t.getType()) {
			case NDeclarator:
			{
				e=declarator(_t,fac);
				_t = _retTree;
				astFactory.addASTChild(currentAST, returnAST);
				break;
			}
			case 3:
			case COLON:
			case NAsmAttribute:
			case LITERAL___attribute:
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
				TNode tmp44_AST = null;
				TNode tmp44_AST_in = null;
				tmp44_AST = (TNode)astFactory.create((TNode)_t);
				tmp44_AST_in = (TNode)_t;
				astFactory.addASTChild(currentAST, tmp44_AST);
				match(_t,COLON);
				_t = _t.getNextSibling();
				discard=expr(_t,fac);
				_t = _retTree;
				astFactory.addASTChild(currentAST, returnAST);
				break;
			}
			case 3:
			case NAsmAttribute:
			case LITERAL___attribute:
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
			_loop51:
			do {
				if (_t==null) _t=ASTNULL;
				if ((_t.getType()==NAsmAttribute||_t.getType()==LITERAL___attribute)) {
					discard=attributeDecl(_t,fac);
					_t = _retTree;
					astFactory.addASTChild(currentAST, returnAST);
				}
				else {
					break _loop51;
				}
				
			} while (true);
			}
			currentAST = __currentAST47;
			_t = __t47;
			_t = _t.getNextSibling();
			structDeclarator_AST = (TNode)currentAST.root;
		}
		catch (RecognitionException ex) {
			if (inputState.guessing==0) {
				reportError(ex);
				if (_t!=null) {_t = _t.getNextSibling();}
			} else {
			  throw ex;
			}
		}
		returnAST = structDeclarator_AST;
		_retTree = _t;
		return e;
	}
	
	public final Expr  declarator(AST _t,
		ExprFactory fac
	) throws RecognitionException {
		Expr e;
		
		TNode declarator_AST_in = (_t == ASTNULL) ? null : (TNode)_t;
		returnAST = null;
		ASTPair currentAST = new ASTPair();
		TNode declarator_AST = null;
		TNode id = null;
		TNode id_AST = null;
		
		e = null;
		Expr nestedDeclarator;
		// Expr parmList = fac.op (CILNullOp.NULL);
		Expr parmList = null;
		// Expr pg = fac.op (CILNullOp.NULL);
		Expr pg = null;
		
		
		try {      // for error handling
			AST __t85 = _t;
			TNode tmp45_AST = null;
			TNode tmp45_AST_in = null;
			tmp45_AST = (TNode)astFactory.create((TNode)_t);
			tmp45_AST_in = (TNode)_t;
			astFactory.addASTChild(currentAST, tmp45_AST);
			ASTPair __currentAST85 = currentAST.copy();
			currentAST.root = currentAST.child;
			currentAST.child = null;
			match(_t,NDeclarator);
			_t = _t.getFirstChild();
			{
			if (_t==null) _t=ASTNULL;
			switch ( _t.getType()) {
			case NPointerGroup:
			{
				pg=pointerGroup(_t,fac);
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
				id = (TNode)_t;
				TNode id_AST_in = null;
				id_AST = (TNode)astFactory.create(id);
				astFactory.addASTChild(currentAST, id_AST);
				match(_t,ID);
				_t = _t.getNextSibling();
				break;
			}
			case LPAREN:
			{
				TNode tmp46_AST = null;
				TNode tmp46_AST_in = null;
				tmp46_AST = (TNode)astFactory.create((TNode)_t);
				tmp46_AST_in = (TNode)_t;
				astFactory.addASTChild(currentAST, tmp46_AST);
				match(_t,LPAREN);
				_t = _t.getNextSibling();
				nestedDeclarator=declarator(_t,fac);
				_t = _retTree;
				astFactory.addASTChild(currentAST, returnAST);
				TNode tmp47_AST = null;
				TNode tmp47_AST_in = null;
				tmp47_AST = (TNode)astFactory.create((TNode)_t);
				tmp47_AST_in = (TNode)_t;
				astFactory.addASTChild(currentAST, tmp47_AST);
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
			_loop91:
			do {
				if (_t==null) _t=ASTNULL;
				if ((_t.getType()==NParameterTypeList)) {
					AST __t89 = _t;
					TNode tmp48_AST = null;
					TNode tmp48_AST_in = null;
					tmp48_AST = (TNode)astFactory.create((TNode)_t);
					tmp48_AST_in = (TNode)_t;
					astFactory.addASTChild(currentAST, tmp48_AST);
					ASTPair __currentAST89 = currentAST.copy();
					currentAST.root = currentAST.child;
					currentAST.child = null;
					match(_t,NParameterTypeList);
					_t = _t.getFirstChild();
					{
					if (_t==null) _t=ASTNULL;
					switch ( _t.getType()) {
					case NParameterDeclaration:
					{
						parmList=parameterTypeList(_t,fac);
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
					TNode tmp49_AST = null;
					TNode tmp49_AST_in = null;
					tmp49_AST = (TNode)astFactory.create((TNode)_t);
					tmp49_AST_in = (TNode)_t;
					astFactory.addASTChild(currentAST, tmp49_AST);
					match(_t,RPAREN);
					_t = _t.getNextSibling();
					currentAST = __currentAST89;
					_t = __t89;
					_t = _t.getNextSibling();
				}
				else {
					break _loop91;
				}
				
			} while (true);
			}
			currentAST = __currentAST85;
			_t = __t85;
			_t = _t.getNextSibling();
			if ( inputState.guessing==0 ) {
				
				if (id == null)
				e = fac.op (CILListOp.LIST).naryApply
				(new Expr[] {pg, fac.var ("function pointer"), parmList});
				// ^^^ for now XXX
				else      
				e = fac.op (CILListOp.LIST).naryApply
				(new Expr[] {pg, fac.var (id.getText ()), parmList});
				
			}
			declarator_AST = (TNode)currentAST.root;
		}
		catch (RecognitionException ex) {
			if (inputState.guessing==0) {
				reportError(ex);
				if (_t!=null) {_t = _t.getNextSibling();}
			} else {
			  throw ex;
			}
		}
		returnAST = declarator_AST;
		_retTree = _t;
		return e;
	}
	
	public final Expr  enumList(AST _t,
		ExprFactory fac
	) throws RecognitionException {
		Expr e;
		
		TNode enumList_AST_in = (_t == ASTNULL) ? null : (TNode)_t;
		returnAST = null;
		ASTPair currentAST = new ASTPair();
		TNode enumList_AST = null;
		
		e = null;
		List enList = new LinkedList ();
		Expr en;
		
		
		try {      // for error handling
			{
			int _cnt57=0;
			_loop57:
			do {
				if (_t==null) _t=ASTNULL;
				if ((_t.getType()==ID)) {
					en=enumerator(_t,fac);
					_t = _retTree;
					astFactory.addASTChild(currentAST, returnAST);
					if ( inputState.guessing==0 ) {
						enList.add (en);
					}
				}
				else {
					if ( _cnt57>=1 ) { break _loop57; } else {throw new NoViableAltException(_t);}
				}
				
				_cnt57++;
			} while (true);
			}
			if ( inputState.guessing==0 ) {
				e = fac.op (CILListOp.LIST).naryApply (enList);
			}
			enumList_AST = (TNode)currentAST.root;
		}
		catch (RecognitionException ex) {
			if (inputState.guessing==0) {
				reportError(ex);
				if (_t!=null) {_t = _t.getNextSibling();}
			} else {
			  throw ex;
			}
		}
		returnAST = enumList_AST;
		_retTree = _t;
		return e;
	}
	
	public final Expr  enumerator(AST _t,
		ExprFactory fac
	) throws RecognitionException {
		Expr e;
		
		TNode enumerator_AST_in = (_t == ASTNULL) ? null : (TNode)_t;
		returnAST = null;
		ASTPair currentAST = new ASTPair();
		TNode enumerator_AST = null;
		TNode id = null;
		TNode id_AST = null;
		
		e = null;
		Expr discard;
		
		
		try {      // for error handling
			id = (TNode)_t;
			TNode id_AST_in = null;
			id_AST = (TNode)astFactory.create(id);
			astFactory.addASTChild(currentAST, id_AST);
			match(_t,ID);
			_t = _t.getNextSibling();
			{
			if (_t==null) _t=ASTNULL;
			switch ( _t.getType()) {
			case ASSIGN:
			{
				TNode tmp50_AST = null;
				TNode tmp50_AST_in = null;
				tmp50_AST = (TNode)astFactory.create((TNode)_t);
				tmp50_AST_in = (TNode)_t;
				astFactory.addASTChild(currentAST, tmp50_AST);
				match(_t,ASSIGN);
				_t = _t.getNextSibling();
				discard=expr(_t,fac);
				_t = _retTree;
				astFactory.addASTChild(currentAST, returnAST);
				break;
			}
			case RCURLY:
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
			if ( inputState.guessing==0 ) {
				e = fac.var (id.getText ());
			}
			enumerator_AST = (TNode)currentAST.root;
		}
		catch (RecognitionException ex) {
			if (inputState.guessing==0) {
				reportError(ex);
				if (_t!=null) {_t = _t.getNextSibling();}
			} else {
			  throw ex;
			}
		}
		returnAST = enumerator_AST;
		_retTree = _t;
		return e;
	}
	
	public final Expr  initDeclList(AST _t,
		ExprFactory fac
	) throws RecognitionException {
		Expr e;
		
		TNode initDeclList_AST_in = (_t == ASTNULL) ? null : (TNode)_t;
		returnAST = null;
		ASTPair currentAST = new ASTPair();
		TNode initDeclList_AST = null;
		
		// e = fac.op (CILNullOp.NULL);
		e = null;
		List declList = new LinkedList ();
		Expr decl;
		
		
		try {      // for error handling
			{
			int _cnt67=0;
			_loop67:
			do {
				if (_t==null) _t=ASTNULL;
				if ((_t.getType()==NInitDecl)) {
					decl=initDecl(_t,fac);
					_t = _retTree;
					astFactory.addASTChild(currentAST, returnAST);
					if ( inputState.guessing==0 ) {
						declList.add (decl);
					}
				}
				else {
					if ( _cnt67>=1 ) { break _loop67; } else {throw new NoViableAltException(_t);}
				}
				
				_cnt67++;
			} while (true);
			}
			if ( inputState.guessing==0 ) {
				e = fac.op (CILListOp.LIST).naryApply (declList);
			}
			initDeclList_AST = (TNode)currentAST.root;
		}
		catch (RecognitionException ex) {
			if (inputState.guessing==0) {
				reportError(ex);
				if (_t!=null) {_t = _t.getNextSibling();}
			} else {
			  throw ex;
			}
		}
		returnAST = initDeclList_AST;
		_retTree = _t;
		return e;
	}
	
	public final Expr  pointerGroup(AST _t,
		ExprFactory fac
	) throws RecognitionException {
		Expr e;
		
		TNode pointerGroup_AST_in = (_t == ASTNULL) ? null : (TNode)_t;
		returnAST = null;
		ASTPair currentAST = new ASTPair();
		TNode pointerGroup_AST = null;
		
		e = null;
		int numPtrs = 0;
		
		
		try {      // for error handling
			AST __t71 = _t;
			TNode tmp51_AST = null;
			TNode tmp51_AST_in = null;
			tmp51_AST = (TNode)astFactory.create((TNode)_t);
			tmp51_AST_in = (TNode)_t;
			astFactory.addASTChild(currentAST, tmp51_AST);
			ASTPair __currentAST71 = currentAST.copy();
			currentAST.root = currentAST.child;
			currentAST.child = null;
			match(_t,NPointerGroup);
			_t = _t.getFirstChild();
			{
			int _cnt73=0;
			_loop73:
			do {
				if (_t==null) _t=ASTNULL;
				if ((_t.getType()==STAR)) {
					TNode tmp52_AST = null;
					TNode tmp52_AST_in = null;
					tmp52_AST = (TNode)astFactory.create((TNode)_t);
					tmp52_AST_in = (TNode)_t;
					astFactory.addASTChild(currentAST, tmp52_AST);
					match(_t,STAR);
					_t = _t.getNextSibling();
					if ( inputState.guessing==0 ) {
						++numPtrs;
					}
				}
				else {
					if ( _cnt73>=1 ) { break _loop73; } else {throw new NoViableAltException(_t);}
				}
				
				_cnt73++;
			} while (true);
			}
			currentAST = __currentAST71;
			_t = __t71;
			_t = _t.getNextSibling();
			if ( inputState.guessing==0 ) {
				e = fac.op (new CILDeclarationPointerOp (numPtrs));
			}
			pointerGroup_AST = (TNode)currentAST.root;
		}
		catch (RecognitionException ex) {
			if (inputState.guessing==0) {
				reportError(ex);
				if (_t!=null) {_t = _t.getNextSibling();}
			} else {
			  throw ex;
			}
		}
		returnAST = pointerGroup_AST;
		_retTree = _t;
		return e;
	}
	
	public final void idList(AST _t) throws RecognitionException {
		
		TNode idList_AST_in = (_t == ASTNULL) ? null : (TNode)_t;
		returnAST = null;
		ASTPair currentAST = new ASTPair();
		TNode idList_AST = null;
		
		try {      // for error handling
			TNode tmp53_AST = null;
			TNode tmp53_AST_in = null;
			tmp53_AST = (TNode)astFactory.create((TNode)_t);
			tmp53_AST_in = (TNode)_t;
			astFactory.addASTChild(currentAST, tmp53_AST);
			match(_t,ID);
			_t = _t.getNextSibling();
			{
			_loop76:
			do {
				if (_t==null) _t=ASTNULL;
				if ((_t.getType()==COMMA)) {
					TNode tmp54_AST = null;
					TNode tmp54_AST_in = null;
					tmp54_AST = (TNode)astFactory.create((TNode)_t);
					tmp54_AST_in = (TNode)_t;
					astFactory.addASTChild(currentAST, tmp54_AST);
					match(_t,COMMA);
					_t = _t.getNextSibling();
					TNode tmp55_AST = null;
					TNode tmp55_AST_in = null;
					tmp55_AST = (TNode)astFactory.create((TNode)_t);
					tmp55_AST_in = (TNode)_t;
					astFactory.addASTChild(currentAST, tmp55_AST);
					match(_t,ID);
					_t = _t.getNextSibling();
				}
				else {
					break _loop76;
				}
				
			} while (true);
			}
			idList_AST = (TNode)currentAST.root;
		}
		catch (RecognitionException ex) {
			if (inputState.guessing==0) {
				reportError(ex);
				if (_t!=null) {_t = _t.getNextSibling();}
			} else {
			  throw ex;
			}
		}
		returnAST = idList_AST;
		_retTree = _t;
	}
	
	public final Expr  initializer(AST _t,
		ExprFactory fac
	) throws RecognitionException {
		Expr e;
		
		TNode initializer_AST_in = (_t == ASTNULL) ? null : (TNode)_t;
		returnAST = null;
		ASTPair currentAST = new ASTPair();
		TNode initializer_AST = null;
		e = null;
		
		try {      // for error handling
			if (_t==null) _t=ASTNULL;
			switch ( _t.getType()) {
			case NInitializer:
			{
				AST __t78 = _t;
				TNode tmp56_AST = null;
				TNode tmp56_AST_in = null;
				tmp56_AST = (TNode)astFactory.create((TNode)_t);
				tmp56_AST_in = (TNode)_t;
				astFactory.addASTChild(currentAST, tmp56_AST);
				ASTPair __currentAST78 = currentAST.copy();
				currentAST.root = currentAST.child;
				currentAST.child = null;
				match(_t,NInitializer);
				_t = _t.getFirstChild();
				e=expr(_t,fac);
				_t = _retTree;
				astFactory.addASTChild(currentAST, returnAST);
				currentAST = __currentAST78;
				_t = __t78;
				_t = _t.getNextSibling();
				initializer_AST = (TNode)currentAST.root;
				break;
			}
			case NLcurlyInitializer:
			{
				e=lcurlyInitializer(_t,fac);
				_t = _retTree;
				astFactory.addASTChild(currentAST, returnAST);
				initializer_AST = (TNode)currentAST.root;
				break;
			}
			default:
			{
				throw new NoViableAltException(_t);
			}
			}
		}
		catch (RecognitionException ex) {
			if (inputState.guessing==0) {
				reportError(ex);
				if (_t!=null) {_t = _t.getNextSibling();}
			} else {
			  throw ex;
			}
		}
		returnAST = initializer_AST;
		_retTree = _t;
		return e;
	}
	
	public final Expr  lcurlyInitializer(AST _t,
		ExprFactory fac
	) throws RecognitionException {
		Expr e;
		
		TNode lcurlyInitializer_AST_in = (_t == ASTNULL) ? null : (TNode)_t;
		returnAST = null;
		ASTPair currentAST = new ASTPair();
		TNode lcurlyInitializer_AST = null;
		e = null;
		
		try {      // for error handling
			AST __t80 = _t;
			TNode tmp57_AST = null;
			TNode tmp57_AST_in = null;
			tmp57_AST = (TNode)astFactory.create((TNode)_t);
			tmp57_AST_in = (TNode)_t;
			astFactory.addASTChild(currentAST, tmp57_AST);
			ASTPair __currentAST80 = currentAST.copy();
			currentAST.root = currentAST.child;
			currentAST.child = null;
			match(_t,NLcurlyInitializer);
			_t = _t.getFirstChild();
			e=initializerList(_t,fac);
			_t = _retTree;
			astFactory.addASTChild(currentAST, returnAST);
			TNode tmp58_AST = null;
			TNode tmp58_AST_in = null;
			tmp58_AST = (TNode)astFactory.create((TNode)_t);
			tmp58_AST_in = (TNode)_t;
			astFactory.addASTChild(currentAST, tmp58_AST);
			match(_t,RCURLY);
			_t = _t.getNextSibling();
			currentAST = __currentAST80;
			_t = __t80;
			_t = _t.getNextSibling();
			lcurlyInitializer_AST = (TNode)currentAST.root;
		}
		catch (RecognitionException ex) {
			if (inputState.guessing==0) {
				reportError(ex);
				if (_t!=null) {_t = _t.getNextSibling();}
			} else {
			  throw ex;
			}
		}
		returnAST = lcurlyInitializer_AST;
		_retTree = _t;
		return e;
	}
	
	public final Expr  initializerList(AST _t,
		ExprFactory fac
	) throws RecognitionException {
		Expr e;
		
		TNode initializerList_AST_in = (_t == ASTNULL) ? null : (TNode)_t;
		returnAST = null;
		ASTPair currentAST = new ASTPair();
		TNode initializerList_AST = null;
		
		e = null;
		List initList = new LinkedList ();
		Expr init;
		
		
		try {      // for error handling
			{
			_loop83:
			do {
				if (_t==null) _t=ASTNULL;
				if ((_t.getType()==NInitializer||_t.getType()==NLcurlyInitializer)) {
					init=initializer(_t,fac);
					_t = _retTree;
					astFactory.addASTChild(currentAST, returnAST);
					if ( inputState.guessing==0 ) {
						initList.add (init);
					}
				}
				else {
					break _loop83;
				}
				
			} while (true);
			}
			initializerList_AST = (TNode)currentAST.root;
		}
		catch (RecognitionException ex) {
			if (inputState.guessing==0) {
				reportError(ex);
				if (_t!=null) {_t = _t.getNextSibling();}
			} else {
			  throw ex;
			}
		}
		returnAST = initializerList_AST;
		_retTree = _t;
		return e;
	}
	
	public final Expr  parameterTypeList(AST _t,
		ExprFactory fac
	) throws RecognitionException {
		Expr e;
		
		TNode parameterTypeList_AST_in = (_t == ASTNULL) ? null : (TNode)_t;
		returnAST = null;
		ASTPair currentAST = new ASTPair();
		TNode parameterTypeList_AST = null;
		
		// e = fac.op (CILNullOp.NULL);
		e = null;
		Expr parm;
		List parmList = new LinkedList ();
		
		
		try {      // for error handling
			{
			int _cnt95=0;
			_loop95:
			do {
				if (_t==null) _t=ASTNULL;
				if ((_t.getType()==NParameterDeclaration)) {
					parm=parameterDeclaration(_t,fac);
					_t = _retTree;
					astFactory.addASTChild(currentAST, returnAST);
					{
					if (_t==null) _t=ASTNULL;
					switch ( _t.getType()) {
					case COMMA:
					{
						TNode tmp59_AST = null;
						TNode tmp59_AST_in = null;
						tmp59_AST = (TNode)astFactory.create((TNode)_t);
						tmp59_AST_in = (TNode)_t;
						astFactory.addASTChild(currentAST, tmp59_AST);
						match(_t,COMMA);
						_t = _t.getNextSibling();
						break;
					}
					case SEMI:
					{
						TNode tmp60_AST = null;
						TNode tmp60_AST_in = null;
						tmp60_AST = (TNode)astFactory.create((TNode)_t);
						tmp60_AST_in = (TNode)_t;
						astFactory.addASTChild(currentAST, tmp60_AST);
						match(_t,SEMI);
						_t = _t.getNextSibling();
						break;
					}
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
					if ( inputState.guessing==0 ) {
						parmList.add (parm);
					}
				}
				else {
					if ( _cnt95>=1 ) { break _loop95; } else {throw new NoViableAltException(_t);}
				}
				
				_cnt95++;
			} while (true);
			}
			{
			if (_t==null) _t=ASTNULL;
			switch ( _t.getType()) {
			case VARARGS:
			{
				TNode tmp61_AST = null;
				TNode tmp61_AST_in = null;
				tmp61_AST = (TNode)astFactory.create((TNode)_t);
				tmp61_AST_in = (TNode)_t;
				astFactory.addASTChild(currentAST, tmp61_AST);
				match(_t,VARARGS);
				_t = _t.getNextSibling();
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
			if ( inputState.guessing==0 ) {
				e = fac.op (CILListOp.LIST).naryApply (parmList);
			}
			parameterTypeList_AST = (TNode)currentAST.root;
		}
		catch (RecognitionException ex) {
			if (inputState.guessing==0) {
				reportError(ex);
				if (_t!=null) {_t = _t.getNextSibling();}
			} else {
			  throw ex;
			}
		}
		returnAST = parameterTypeList_AST;
		_retTree = _t;
		return e;
	}
	
	public final Expr  parameterDeclaration(AST _t,
		ExprFactory fac
	) throws RecognitionException {
		Expr e;
		
		TNode parameterDeclaration_AST_in = (_t == ASTNULL) ? null : (TNode)_t;
		returnAST = null;
		ASTPair currentAST = new ASTPair();
		TNode parameterDeclaration_AST = null;
		
		// e = fac.op (CILNullOp.NULL);
		e = null;
		Expr specs;
		// Expr sig = fac.op (CILNullOp.NULL);
		Expr sig = null;
		Expr discard;
		
		
		try {      // for error handling
			AST __t98 = _t;
			TNode tmp62_AST = null;
			TNode tmp62_AST_in = null;
			tmp62_AST = (TNode)astFactory.create((TNode)_t);
			tmp62_AST_in = (TNode)_t;
			astFactory.addASTChild(currentAST, tmp62_AST);
			ASTPair __currentAST98 = currentAST.copy();
			currentAST.root = currentAST.child;
			currentAST.child = null;
			match(_t,NParameterDeclaration);
			_t = _t.getFirstChild();
			specs=declSpecifiers(_t,fac);
			_t = _retTree;
			astFactory.addASTChild(currentAST, returnAST);
			{
			if (_t==null) _t=ASTNULL;
			switch ( _t.getType()) {
			case NNonemptyAbstractDeclarator:
			{
				nonemptyAbstractDeclarator(_t,fac);
				_t = _retTree;
				astFactory.addASTChild(currentAST, returnAST);
				break;
			}
			case 3:
			case NDeclarator:
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
			case NDeclarator:
			{
				sig=declarator(_t,fac);
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
			currentAST = __currentAST98;
			_t = __t98;
			_t = _t.getNextSibling();
			if ( inputState.guessing==0 ) {
				e = fac.op (CILDeclarationOp.DECL).naryApply (new Expr[] {specs, sig});
			}
			parameterDeclaration_AST = (TNode)currentAST.root;
		}
		catch (RecognitionException ex) {
			if (inputState.guessing==0) {
				reportError(ex);
				if (_t!=null) {_t = _t.getNextSibling();}
			} else {
			  throw ex;
			}
		}
		returnAST = parameterDeclaration_AST;
		_retTree = _t;
		return e;
	}
	
	public final void nonemptyAbstractDeclarator(AST _t,
		ExprFactory fac
	) throws RecognitionException {
		
		TNode nonemptyAbstractDeclarator_AST_in = (_t == ASTNULL) ? null : (TNode)_t;
		returnAST = null;
		ASTPair currentAST = new ASTPair();
		TNode nonemptyAbstractDeclarator_AST = null;
		Expr discard;
		
		try {      // for error handling
			AST __t191 = _t;
			TNode tmp63_AST = null;
			TNode tmp63_AST_in = null;
			tmp63_AST = (TNode)astFactory.create((TNode)_t);
			tmp63_AST_in = (TNode)_t;
			astFactory.addASTChild(currentAST, tmp63_AST);
			ASTPair __currentAST191 = currentAST.copy();
			currentAST.root = currentAST.child;
			currentAST.child = null;
			match(_t,NNonemptyAbstractDeclarator);
			_t = _t.getFirstChild();
			{
			if (_t==null) _t=ASTNULL;
			switch ( _t.getType()) {
			case NPointerGroup:
			{
				discard=pointerGroup(_t,fac);
				_t = _retTree;
				astFactory.addASTChild(currentAST, returnAST);
				{
				_loop198:
				do {
					if (_t==null) _t=ASTNULL;
					switch ( _t.getType()) {
					case LPAREN:
					{
						{
						TNode tmp64_AST = null;
						TNode tmp64_AST_in = null;
						tmp64_AST = (TNode)astFactory.create((TNode)_t);
						tmp64_AST_in = (TNode)_t;
						astFactory.addASTChild(currentAST, tmp64_AST);
						match(_t,LPAREN);
						_t = _t.getNextSibling();
						{
						if (_t==null) _t=ASTNULL;
						switch ( _t.getType()) {
						case NNonemptyAbstractDeclarator:
						{
							nonemptyAbstractDeclarator(_t,fac);
							_t = _retTree;
							astFactory.addASTChild(currentAST, returnAST);
							break;
						}
						case NParameterDeclaration:
						{
							discard=parameterTypeList(_t,fac);
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
						TNode tmp65_AST = null;
						TNode tmp65_AST_in = null;
						tmp65_AST = (TNode)astFactory.create((TNode)_t);
						tmp65_AST_in = (TNode)_t;
						astFactory.addASTChild(currentAST, tmp65_AST);
						match(_t,RPAREN);
						_t = _t.getNextSibling();
						}
						break;
					}
					case LBRACKET:
					{
						{
						TNode tmp66_AST = null;
						TNode tmp66_AST_in = null;
						tmp66_AST = (TNode)astFactory.create((TNode)_t);
						tmp66_AST_in = (TNode)_t;
						astFactory.addASTChild(currentAST, tmp66_AST);
						match(_t,LBRACKET);
						_t = _t.getNextSibling();
						{
						if (_t==null) _t=ASTNULL;
						switch ( _t.getType()) {
						case ID:
						case ASSIGN:
						case STAR:
						case LPAREN:
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
						case INC:
						case DEC:
						case LITERAL_sizeof:
						case CharLiteral:
						case NCast:
						case NExpressionGroup:
						case NInitializer:
						case NUnaryExpr:
						case NPostfixExpr:
						case NStringSeq:
						case NLcurlyInitializer:
						case NGnuAsmExpr:
						case Number:
						{
							discard=expr(_t,fac);
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
						TNode tmp67_AST = null;
						TNode tmp67_AST_in = null;
						tmp67_AST = (TNode)astFactory.create((TNode)_t);
						tmp67_AST_in = (TNode)_t;
						astFactory.addASTChild(currentAST, tmp67_AST);
						match(_t,RBRACKET);
						_t = _t.getNextSibling();
						}
						break;
					}
					default:
					{
						break _loop198;
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
				int _cnt204=0;
				_loop204:
				do {
					if (_t==null) _t=ASTNULL;
					switch ( _t.getType()) {
					case LPAREN:
					{
						{
						TNode tmp68_AST = null;
						TNode tmp68_AST_in = null;
						tmp68_AST = (TNode)astFactory.create((TNode)_t);
						tmp68_AST_in = (TNode)_t;
						astFactory.addASTChild(currentAST, tmp68_AST);
						match(_t,LPAREN);
						_t = _t.getNextSibling();
						{
						if (_t==null) _t=ASTNULL;
						switch ( _t.getType()) {
						case NNonemptyAbstractDeclarator:
						{
							nonemptyAbstractDeclarator(_t,fac);
							_t = _retTree;
							astFactory.addASTChild(currentAST, returnAST);
							break;
						}
						case NParameterDeclaration:
						{
							discard=parameterTypeList(_t,fac);
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
						TNode tmp69_AST = null;
						TNode tmp69_AST_in = null;
						tmp69_AST = (TNode)astFactory.create((TNode)_t);
						tmp69_AST_in = (TNode)_t;
						astFactory.addASTChild(currentAST, tmp69_AST);
						match(_t,RPAREN);
						_t = _t.getNextSibling();
						}
						break;
					}
					case LBRACKET:
					{
						{
						TNode tmp70_AST = null;
						TNode tmp70_AST_in = null;
						tmp70_AST = (TNode)astFactory.create((TNode)_t);
						tmp70_AST_in = (TNode)_t;
						astFactory.addASTChild(currentAST, tmp70_AST);
						match(_t,LBRACKET);
						_t = _t.getNextSibling();
						{
						if (_t==null) _t=ASTNULL;
						switch ( _t.getType()) {
						case ID:
						case ASSIGN:
						case STAR:
						case LPAREN:
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
						case INC:
						case DEC:
						case LITERAL_sizeof:
						case CharLiteral:
						case NCast:
						case NExpressionGroup:
						case NInitializer:
						case NUnaryExpr:
						case NPostfixExpr:
						case NStringSeq:
						case NLcurlyInitializer:
						case NGnuAsmExpr:
						case Number:
						{
							discard=expr(_t,fac);
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
						TNode tmp71_AST = null;
						TNode tmp71_AST_in = null;
						tmp71_AST = (TNode)astFactory.create((TNode)_t);
						tmp71_AST_in = (TNode)_t;
						astFactory.addASTChild(currentAST, tmp71_AST);
						match(_t,RBRACKET);
						_t = _t.getNextSibling();
						}
						break;
					}
					default:
					{
						if ( _cnt204>=1 ) { break _loop204; } else {throw new NoViableAltException(_t);}
					}
					}
					_cnt204++;
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
			currentAST = __currentAST191;
			_t = __t191;
			_t = _t.getNextSibling();
			nonemptyAbstractDeclarator_AST = (TNode)currentAST.root;
		}
		catch (RecognitionException ex) {
			if (inputState.guessing==0) {
				reportError(ex);
				if (_t!=null) {_t = _t.getNextSibling();}
			} else {
			  throw ex;
			}
		}
		returnAST = nonemptyAbstractDeclarator_AST;
		_retTree = _t;
	}
	
	public final Expr  functionDeclSpecifiers(AST _t,
		ExprFactory fac
	) throws RecognitionException {
		Expr e;
		
		TNode functionDeclSpecifiers_AST_in = (_t == ASTNULL) ? null : (TNode)_t;
		returnAST = null;
		ASTPair currentAST = new ASTPair();
		TNode functionDeclSpecifiers_AST = null;
		
		e = null;
		List declSpecList = new LinkedList ();
		Expr fscs;
		Expr tq;
		Expr ts;
		
		
		try {      // for error handling
			{
			int _cnt106=0;
			_loop106:
			do {
				if (_t==null) _t=ASTNULL;
				switch ( _t.getType()) {
				case LITERAL_extern:
				case LITERAL_static:
				case LITERAL_inline:
				{
					fscs=functionStorageClassSpecifier(_t,fac);
					_t = _retTree;
					astFactory.addASTChild(currentAST, returnAST);
					if ( inputState.guessing==0 ) {
						declSpecList.add (fscs);
					}
					break;
				}
				case LITERAL_volatile:
				case LITERAL_const:
				{
					tq=typeQualifier(_t,fac);
					_t = _retTree;
					astFactory.addASTChild(currentAST, returnAST);
					if ( inputState.guessing==0 ) {
						declSpecList.add (tq);
					}
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
				case LITERAL___builtin_va_list:
				{
					ts=typeSpecifier(_t,fac);
					_t = _retTree;
					astFactory.addASTChild(currentAST, returnAST);
					if ( inputState.guessing==0 ) {
						declSpecList.add (ts);
					}
					break;
				}
				default:
				{
					if ( _cnt106>=1 ) { break _loop106; } else {throw new NoViableAltException(_t);}
				}
				}
				_cnt106++;
			} while (true);
			}
			if ( inputState.guessing==0 ) {
				e = fac.op (CILListOp.LIST).naryApply (declSpecList);
			}
			functionDeclSpecifiers_AST = (TNode)currentAST.root;
		}
		catch (RecognitionException ex) {
			if (inputState.guessing==0) {
				reportError(ex);
				if (_t!=null) {_t = _t.getNextSibling();}
			} else {
			  throw ex;
			}
		}
		returnAST = functionDeclSpecifiers_AST;
		_retTree = _t;
		return e;
	}
	
	public final Expr  compoundStatement(AST _t,
		ExprFactory fac
	) throws RecognitionException {
		Expr e;
		
		TNode compoundStatement_AST_in = (_t == ASTNULL) ? null : (TNode)_t;
		returnAST = null;
		ASTPair currentAST = new ASTPair();
		TNode compoundStatement_AST = null;
		
		e = null;
		Expr declList = null;
		Expr stmtList = null;
		
		
		try {      // for error handling
			AST __t115 = _t;
			TNode tmp72_AST = null;
			TNode tmp72_AST_in = null;
			tmp72_AST = (TNode)astFactory.create((TNode)_t);
			tmp72_AST_in = (TNode)_t;
			astFactory.addASTChild(currentAST, tmp72_AST);
			ASTPair __currentAST115 = currentAST.copy();
			currentAST.root = currentAST.child;
			currentAST.child = null;
			match(_t,NCompoundStatement);
			_t = _t.getFirstChild();
			{
			if (_t==null) _t=ASTNULL;
			switch ( _t.getType()) {
			case NDeclaration:
			case LITERAL___label__:
			{
				declList=declarationList(_t,fac);
				_t = _retTree;
				astFactory.addASTChild(currentAST, returnAST);
				break;
			}
			case RCURLY:
			case SEMI:
			case LITERAL_while:
			case LITERAL_goto:
			case LITERAL_break:
			case LITERAL_return:
			case LITERAL_if:
			case NStatementExpr:
			case NCompoundStatement:
			case NLabel:
			case LITERAL___nd_goto:
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
			case SEMI:
			case LITERAL_while:
			case LITERAL_goto:
			case LITERAL_break:
			case LITERAL_return:
			case LITERAL_if:
			case NStatementExpr:
			case NCompoundStatement:
			case NLabel:
			case LITERAL___nd_goto:
			{
				stmtList=statementList(_t,fac);
				_t = _retTree;
				astFactory.addASTChild(currentAST, returnAST);
				break;
			}
			case RCURLY:
			{
				break;
			}
			default:
			{
				throw new NoViableAltException(_t);
			}
			}
			}
			TNode tmp73_AST = null;
			TNode tmp73_AST_in = null;
			tmp73_AST = (TNode)astFactory.create((TNode)_t);
			tmp73_AST_in = (TNode)_t;
			astFactory.addASTChild(currentAST, tmp73_AST);
			match(_t,RCURLY);
			_t = _t.getNextSibling();
			currentAST = __currentAST115;
			_t = __t115;
			_t = _t.getNextSibling();
			if ( inputState.guessing==0 ) {
				
				/*
				if (declList == null)
				declList = fac.op (CILNullOp.NULL);
				if (stmtList == null)
				stmtList = fac.op (CILNullOp.NULL);
				*/
				e = fac.op (CILScopeOp.SCOPE).binApply (declList, stmtList);
				
			}
			compoundStatement_AST = (TNode)currentAST.root;
		}
		catch (RecognitionException ex) {
			if (inputState.guessing==0) {
				reportError(ex);
				if (_t!=null) {_t = _t.getNextSibling();}
			} else {
			  throw ex;
			}
		}
		returnAST = compoundStatement_AST;
		_retTree = _t;
		return e;
	}
	
	public final Expr  declarationList(AST _t,
		ExprFactory fac
	) throws RecognitionException {
		Expr e;
		
		TNode declarationList_AST_in = (_t == ASTNULL) ? null : (TNode)_t;
		returnAST = null;
		ASTPair currentAST = new ASTPair();
		TNode declarationList_AST = null;
		
		List declList = new LinkedList ();
		e = null;
		Expr decl;
		
		
		try {      // for error handling
			{
			int _cnt109=0;
			_loop109:
			do {
				if (_t==null) _t=ASTNULL;
				switch ( _t.getType()) {
				case LITERAL___label__:
				{
					localLabelDecl(_t);
					_t = _retTree;
					astFactory.addASTChild(currentAST, returnAST);
					if ( inputState.guessing==0 ) {
						declarationList_AST = (TNode)currentAST.root;
						declList.add (declarationList_AST);
					}
					break;
				}
				case NDeclaration:
				{
					decl=declaration(_t,fac);
					_t = _retTree;
					astFactory.addASTChild(currentAST, returnAST);
					if ( inputState.guessing==0 ) {
						declList.add (decl);
					}
					break;
				}
				default:
				{
					if ( _cnt109>=1 ) { break _loop109; } else {throw new NoViableAltException(_t);}
				}
				}
				_cnt109++;
			} while (true);
			}
			if ( inputState.guessing==0 ) {
				e = fac.op (CILListOp.LIST).naryApply (declList);
			}
			declarationList_AST = (TNode)currentAST.root;
		}
		catch (RecognitionException ex) {
			if (inputState.guessing==0) {
				reportError(ex);
				if (_t!=null) {_t = _t.getNextSibling();}
			} else {
			  throw ex;
			}
		}
		returnAST = declarationList_AST;
		_retTree = _t;
		return e;
	}
	
	public final void localLabelDecl(AST _t) throws RecognitionException {
		
		TNode localLabelDecl_AST_in = (_t == ASTNULL) ? null : (TNode)_t;
		returnAST = null;
		ASTPair currentAST = new ASTPair();
		TNode localLabelDecl_AST = null;
		
		try {      // for error handling
			AST __t111 = _t;
			TNode tmp74_AST = null;
			TNode tmp74_AST_in = null;
			tmp74_AST = (TNode)astFactory.create((TNode)_t);
			tmp74_AST_in = (TNode)_t;
			astFactory.addASTChild(currentAST, tmp74_AST);
			ASTPair __currentAST111 = currentAST.copy();
			currentAST.root = currentAST.child;
			currentAST.child = null;
			match(_t,LITERAL___label__);
			_t = _t.getFirstChild();
			{
			int _cnt113=0;
			_loop113:
			do {
				if (_t==null) _t=ASTNULL;
				if ((_t.getType()==ID)) {
					TNode tmp75_AST = null;
					TNode tmp75_AST_in = null;
					tmp75_AST = (TNode)astFactory.create((TNode)_t);
					tmp75_AST_in = (TNode)_t;
					astFactory.addASTChild(currentAST, tmp75_AST);
					match(_t,ID);
					_t = _t.getNextSibling();
				}
				else {
					if ( _cnt113>=1 ) { break _loop113; } else {throw new NoViableAltException(_t);}
				}
				
				_cnt113++;
			} while (true);
			}
			currentAST = __currentAST111;
			_t = __t111;
			_t = _t.getNextSibling();
			localLabelDecl_AST = (TNode)currentAST.root;
		}
		catch (RecognitionException ex) {
			if (inputState.guessing==0) {
				reportError(ex);
				if (_t!=null) {_t = _t.getNextSibling();}
			} else {
			  throw ex;
			}
		}
		returnAST = localLabelDecl_AST;
		_retTree = _t;
	}
	
	public final Expr  statementList(AST _t,
		ExprFactory fac
	) throws RecognitionException {
		Expr e;
		
		TNode statementList_AST_in = (_t == ASTNULL) ? null : (TNode)_t;
		returnAST = null;
		ASTPair currentAST = new ASTPair();
		TNode statementList_AST = null;
		
		e = null;
		List stmtList = new LinkedList ();
		List curSList = new LinkedList ();
		Expr stmt;
		
		
		try {      // for error handling
			{
			int _cnt120=0;
			_loop120:
			do {
				if (_t==null) _t=ASTNULL;
				if ((_tokenSet_2.member(_t.getType()))) {
					stmt=statement(_t,fac);
					_t = _retTree;
					astFactory.addASTChild(currentAST, returnAST);
					if ( inputState.guessing==0 ) {
						
						if (stmtBlocking)
						{
						// -- merge simple assignments
						if (stmt != null &&
						stmt.op () == CILAssignOp.ASSIGN &&
						stmt.arg (1) != null && // XXX remove this when null ops are gone
						stmt.arg (1).op () != CILFunctionCallOp.FCALL)
						{
						curSList.add (stmt);
						}
						else
						{
						// -- encountered unmergeable statement
						// -- form SLIST from curSList, add to stmtList, and re-initialize
						// former; XXX should factor this out into a method, since it's
						// re-used below
						if (!curSList.isEmpty ())
						{
						stmtList.add (fac.op (CILListOp.SLIST).naryApply (curSList));
						curSList = new LinkedList ();
						}
						// add stmt to stmtList (as the "sibling" of curSList)
						stmtList.add (stmt);
						}
						}
						else
						{
						stmtList.add (stmt);
						}
						
					}
				}
				else {
					if ( _cnt120>=1 ) { break _loop120; } else {throw new NoViableAltException(_t);}
				}
				
				_cnt120++;
			} while (true);
			}
			if ( inputState.guessing==0 ) {
				
				// -- reached end of statements comprising this statementList
				// -- form SLIST from curSList, add to stmtList, and re-initialize
				// former
				if (stmtBlocking)
				{
				if (!curSList.isEmpty ())
				{
				stmtList.add (fac.op (CILListOp.SLIST).naryApply (curSList));
				// curSList = new LinkedList (); // XXX not necessary
				}
				}
				
				e = fac.op (CILListOp.LIST).naryApply (stmtList);
				
			}
			statementList_AST = (TNode)currentAST.root;
		}
		catch (RecognitionException ex) {
			if (inputState.guessing==0) {
				reportError(ex);
				if (_t!=null) {_t = _t.getNextSibling();}
			} else {
			  throw ex;
			}
		}
		returnAST = statementList_AST;
		_retTree = _t;
		return e;
	}
	
	public final Expr  statement(AST _t,
		ExprFactory fac
	) throws RecognitionException {
		Expr e;
		
		TNode statement_AST_in = (_t == ASTNULL) ? null : (TNode)_t;
		returnAST = null;
		ASTPair currentAST = new ASTPair();
		TNode statement_AST = null;
		e = null;
		
		try {      // for error handling
			e=statementBody(_t,fac);
			_t = _retTree;
			astFactory.addASTChild(currentAST, returnAST);
			statement_AST = (TNode)currentAST.root;
		}
		catch (RecognitionException ex) {
			if (inputState.guessing==0) {
				reportError(ex);
				if (_t!=null) {_t = _t.getNextSibling();}
			} else {
			  throw ex;
			}
		}
		returnAST = statement_AST;
		_retTree = _t;
		return e;
	}
	
	public final Expr  statementBody(AST _t,
		ExprFactory fac
	) throws RecognitionException {
		Expr e;
		
		TNode statementBody_AST_in = (_t == ASTNULL) ? null : (TNode)_t;
		returnAST = null;
		ASTPair currentAST = new ASTPair();
		TNode statementBody_AST = null;
		TNode id1 = null;
		TNode id1_AST = null;
		TNode id = null;
		TNode id_AST = null;
		
		Expr discard;
		e = null;
		// Expr e1 = fac.op (CILNullOp.NULL);
		Expr e1 = null;
		Expr s1 = null;
		// Expr s2 = fac.op (CILNullOp.NULL);
		Expr s2 = null;
		
		
		try {      // for error handling
			{
			if (_t==null) _t=ASTNULL;
			switch ( _t.getType()) {
			case SEMI:
			{
				TNode tmp76_AST = null;
				TNode tmp76_AST_in = null;
				tmp76_AST = (TNode)astFactory.create((TNode)_t);
				tmp76_AST_in = (TNode)_t;
				astFactory.addASTChild(currentAST, tmp76_AST);
				match(_t,SEMI);
				_t = _t.getNextSibling();
				if ( inputState.guessing==0 ) {
					statementBody_AST = (TNode)currentAST.root;
					e = fac.op (CILEmptyStmtOp.EMPTY).
					unaryApply (fac.intExpr (statementBody_AST.getLineNum ()));
				}
				break;
			}
			case NCompoundStatement:
			{
				e=compoundStatement(_t,fac);
				_t = _retTree;
				astFactory.addASTChild(currentAST, returnAST);
				break;
			}
			case NStatementExpr:
			{
				AST __t124 = _t;
				TNode tmp77_AST = null;
				TNode tmp77_AST_in = null;
				tmp77_AST = (TNode)astFactory.create((TNode)_t);
				tmp77_AST_in = (TNode)_t;
				astFactory.addASTChild(currentAST, tmp77_AST);
				ASTPair __currentAST124 = currentAST.copy();
				currentAST.root = currentAST.child;
				currentAST.child = null;
				match(_t,NStatementExpr);
				_t = _t.getFirstChild();
				e=expr(_t,fac);
				_t = _retTree;
				astFactory.addASTChild(currentAST, returnAST);
				currentAST = __currentAST124;
				_t = __t124;
				_t = _t.getNextSibling();
				break;
			}
			case LITERAL_while:
			{
				AST __t125 = _t;
				TNode tmp78_AST = null;
				TNode tmp78_AST_in = null;
				tmp78_AST = (TNode)astFactory.create((TNode)_t);
				tmp78_AST_in = (TNode)_t;
				astFactory.addASTChild(currentAST, tmp78_AST);
				ASTPair __currentAST125 = currentAST.copy();
				currentAST.root = currentAST.child;
				currentAST.child = null;
				match(_t,LITERAL_while);
				_t = _t.getFirstChild();
				e1=expr(_t,fac);
				_t = _retTree;
				astFactory.addASTChild(currentAST, returnAST);
				s1=statement(_t,fac);
				_t = _retTree;
				astFactory.addASTChild(currentAST, returnAST);
				currentAST = __currentAST125;
				_t = __t125;
				_t = _t.getNextSibling();
				if ( inputState.guessing==0 ) {
					statementBody_AST = (TNode)currentAST.root;
					e = fac.op (CILWhileOp.WHILE).naryApply 
					(new Expr[] {e1, s1, 
					fac.intExpr (statementBody_AST.getLineNum ())});
				}
				break;
			}
			case LITERAL_goto:
			{
				AST __t126 = _t;
				TNode tmp79_AST = null;
				TNode tmp79_AST_in = null;
				tmp79_AST = (TNode)astFactory.create((TNode)_t);
				tmp79_AST_in = (TNode)_t;
				astFactory.addASTChild(currentAST, tmp79_AST);
				ASTPair __currentAST126 = currentAST.copy();
				currentAST.root = currentAST.child;
				currentAST.child = null;
				match(_t,LITERAL_goto);
				_t = _t.getFirstChild();
				id1 = (TNode)_t;
				TNode id1_AST_in = null;
				id1_AST = (TNode)astFactory.create(id1);
				astFactory.addASTChild(currentAST, id1_AST);
				match(_t,ID);
				_t = _t.getNextSibling();
				currentAST = __currentAST126;
				_t = __t126;
				_t = _t.getNextSibling();
				if ( inputState.guessing==0 ) {
					statementBody_AST = (TNode)currentAST.root;
					
					assert !id1.getText ().equals ("") && id1.getText () != null :
					id1;
					e = fac.op (CILGotoOp.GOTO).binApply 
					(fac.var (id1.getText ()), 
					fac.intExpr (statementBody_AST.getLineNum ()));
				}
				break;
			}
			case LITERAL_break:
			{
				TNode tmp80_AST = null;
				TNode tmp80_AST_in = null;
				tmp80_AST = (TNode)astFactory.create((TNode)_t);
				tmp80_AST_in = (TNode)_t;
				astFactory.addASTChild(currentAST, tmp80_AST);
				match(_t,LITERAL_break);
				_t = _t.getNextSibling();
				if ( inputState.guessing==0 ) {
					statementBody_AST = (TNode)currentAST.root;
					e = fac.op (CILBreakOp.BREAK).unaryApply 
					(fac.intExpr (statementBody_AST.getLineNum ()));
				}
				break;
			}
			case LITERAL_return:
			{
				AST __t127 = _t;
				TNode tmp81_AST = null;
				TNode tmp81_AST_in = null;
				tmp81_AST = (TNode)astFactory.create((TNode)_t);
				tmp81_AST_in = (TNode)_t;
				astFactory.addASTChild(currentAST, tmp81_AST);
				ASTPair __currentAST127 = currentAST.copy();
				currentAST.root = currentAST.child;
				currentAST.child = null;
				match(_t,LITERAL_return);
				_t = _t.getFirstChild();
				{
				if (_t==null) _t=ASTNULL;
				switch ( _t.getType()) {
				case ID:
				case ASSIGN:
				case STAR:
				case LPAREN:
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
				case INC:
				case DEC:
				case LITERAL_sizeof:
				case CharLiteral:
				case NCast:
				case NExpressionGroup:
				case NInitializer:
				case NUnaryExpr:
				case NPostfixExpr:
				case NStringSeq:
				case NLcurlyInitializer:
				case NGnuAsmExpr:
				case Number:
				{
					e1=expr(_t,fac);
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
				currentAST = __currentAST127;
				_t = __t127;
				_t = _t.getNextSibling();
				if ( inputState.guessing==0 ) {
					statementBody_AST = (TNode)currentAST.root;
					e = fac.op (CILReturnOp.RETURN).binApply 
					(e1, fac.intExpr (statementBody_AST.getLineNum ()));
				}
				break;
			}
			case NLabel:
			{
				AST __t129 = _t;
				TNode tmp82_AST = null;
				TNode tmp82_AST_in = null;
				tmp82_AST = (TNode)astFactory.create((TNode)_t);
				tmp82_AST_in = (TNode)_t;
				astFactory.addASTChild(currentAST, tmp82_AST);
				ASTPair __currentAST129 = currentAST.copy();
				currentAST.root = currentAST.child;
				currentAST.child = null;
				match(_t,NLabel);
				_t = _t.getFirstChild();
				id = (TNode)_t;
				TNode id_AST_in = null;
				id_AST = (TNode)astFactory.create(id);
				astFactory.addASTChild(currentAST, id_AST);
				match(_t,ID);
				_t = _t.getNextSibling();
				{
				if (_t==null) _t=ASTNULL;
				switch ( _t.getType()) {
				case SEMI:
				case LITERAL_while:
				case LITERAL_goto:
				case LITERAL_break:
				case LITERAL_return:
				case LITERAL_if:
				case NStatementExpr:
				case NCompoundStatement:
				case NLabel:
				case LITERAL___nd_goto:
				{
					e1=statement(_t,fac);
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
				currentAST = __currentAST129;
				_t = __t129;
				_t = _t.getNextSibling();
				if ( inputState.guessing==0 ) {
					statementBody_AST = (TNode)currentAST.root;
					
					e = fac.op (CILLabelledStmtOp.LSTMT).naryApply
					(new Expr[] {fac.var (id.getText ()), e1, 
					fac.intExpr (statementBody_AST.getLineNum ())});
					
				}
				break;
			}
			case LITERAL_if:
			{
				AST __t131 = _t;
				TNode tmp83_AST = null;
				TNode tmp83_AST_in = null;
				tmp83_AST = (TNode)astFactory.create((TNode)_t);
				tmp83_AST_in = (TNode)_t;
				astFactory.addASTChild(currentAST, tmp83_AST);
				ASTPair __currentAST131 = currentAST.copy();
				currentAST.root = currentAST.child;
				currentAST.child = null;
				match(_t,LITERAL_if);
				_t = _t.getFirstChild();
				e1=expr(_t,fac);
				_t = _retTree;
				astFactory.addASTChild(currentAST, returnAST);
				s1=statement(_t,fac);
				_t = _retTree;
				astFactory.addASTChild(currentAST, returnAST);
				{
				if (_t==null) _t=ASTNULL;
				switch ( _t.getType()) {
				case LITERAL_else:
				{
					TNode tmp84_AST = null;
					TNode tmp84_AST_in = null;
					tmp84_AST = (TNode)astFactory.create((TNode)_t);
					tmp84_AST_in = (TNode)_t;
					astFactory.addASTChild(currentAST, tmp84_AST);
					match(_t,LITERAL_else);
					_t = _t.getNextSibling();
					s2=statement(_t,fac);
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
				currentAST = __currentAST131;
				_t = __t131;
				_t = _t.getNextSibling();
				if ( inputState.guessing==0 ) {
					statementBody_AST = (TNode)currentAST.root;
					
					e = fac.op (CILIfStmtOp.IF_THEN_ELSE).naryApply
					(new Expr[] {e1, s1, s2, 
					fac.intExpr (statementBody_AST.getLineNum ())});
					
				}
				break;
			}
			case LITERAL___nd_goto:
			{
				AST __t133 = _t;
				TNode tmp85_AST = null;
				TNode tmp85_AST_in = null;
				tmp85_AST = (TNode)astFactory.create((TNode)_t);
				tmp85_AST_in = (TNode)_t;
				astFactory.addASTChild(currentAST, tmp85_AST);
				ASTPair __currentAST133 = currentAST.copy();
				currentAST.root = currentAST.child;
				currentAST.child = null;
				match(_t,LITERAL___nd_goto);
				_t = _t.getFirstChild();
				e1=ndGotoLabelList(_t,fac);
				_t = _retTree;
				astFactory.addASTChild(currentAST, returnAST);
				currentAST = __currentAST133;
				_t = __t133;
				_t = _t.getNextSibling();
				if ( inputState.guessing==0 ) {
					statementBody_AST = (TNode)currentAST.root;
					
					e = fac.op (CILNDGotoOp.NDGOTO).
					binApply (e1, fac.intExpr (statementBody_AST.getLineNum ()));
					
				}
				break;
			}
			default:
			{
				throw new NoViableAltException(_t);
			}
			}
			}
			if ( inputState.guessing==0 ) {
				
				// if (e == null) System.out.println ("got a null e for " + ##);
				
			}
			statementBody_AST = (TNode)currentAST.root;
		}
		catch (RecognitionException ex) {
			if (inputState.guessing==0) {
				reportError(ex);
				if (_t!=null) {_t = _t.getNextSibling();}
			} else {
			  throw ex;
			}
		}
		returnAST = statementBody_AST;
		_retTree = _t;
		return e;
	}
	
	public final Expr  ndGotoLabelList(AST _t,
		ExprFactory fac
	) throws RecognitionException {
		Expr e;
		
		TNode ndGotoLabelList_AST_in = (_t == ASTNULL) ? null : (TNode)_t;
		returnAST = null;
		ASTPair currentAST = new ASTPair();
		TNode ndGotoLabelList_AST = null;
		TNode s = null;
		TNode s_AST = null;
		
		e = null;
		List stringList = new LinkedList ();
		
		
		try {      // for error handling
			{
			int _cnt136=0;
			_loop136:
			do {
				if (_t==null) _t=ASTNULL;
				if ((_t.getType()==StringLiteral)) {
					s = (TNode)_t;
					TNode s_AST_in = null;
					s_AST = (TNode)astFactory.create(s);
					astFactory.addASTChild(currentAST, s_AST);
					match(_t,StringLiteral);
					_t = _t.getNextSibling();
					if ( inputState.guessing==0 ) {
						stringList.add (fac.var (s.getText ()));
					}
				}
				else {
					if ( _cnt136>=1 ) { break _loop136; } else {throw new NoViableAltException(_t);}
				}
				
				_cnt136++;
			} while (true);
			}
			if ( inputState.guessing==0 ) {
				e = fac.op (CILListOp.LIST).naryApply (stringList);
			}
			ndGotoLabelList_AST = (TNode)currentAST.root;
		}
		catch (RecognitionException ex) {
			if (inputState.guessing==0) {
				reportError(ex);
				if (_t!=null) {_t = _t.getNextSibling();}
			} else {
			  throw ex;
			}
		}
		returnAST = ndGotoLabelList_AST;
		_retTree = _t;
		return e;
	}
	
	public final Expr  assignExpr(AST _t,
		ExprFactory fac
	) throws RecognitionException {
		Expr e;
		
		TNode assignExpr_AST_in = (_t == ASTNULL) ? null : (TNode)_t;
		returnAST = null;
		ASTPair currentAST = new ASTPair();
		TNode assignExpr_AST = null;
		
		e = null;
		Expr e1;
		Expr e2;
		
		
		try {      // for error handling
			AST __t157 = _t;
			TNode tmp86_AST = null;
			TNode tmp86_AST_in = null;
			tmp86_AST = (TNode)astFactory.create((TNode)_t);
			tmp86_AST_in = (TNode)_t;
			astFactory.addASTChild(currentAST, tmp86_AST);
			ASTPair __currentAST157 = currentAST.copy();
			currentAST.root = currentAST.child;
			currentAST.child = null;
			match(_t,ASSIGN);
			_t = _t.getFirstChild();
			e1=expr(_t,fac);
			_t = _retTree;
			astFactory.addASTChild(currentAST, returnAST);
			e2=expr(_t,fac);
			_t = _retTree;
			astFactory.addASTChild(currentAST, returnAST);
			currentAST = __currentAST157;
			_t = __t157;
			_t = _t.getNextSibling();
			if ( inputState.guessing==0 ) {
				assignExpr_AST = (TNode)currentAST.root;
				//e = fac.op (CILAssignOp.ASSIGN).binApply (e1, e2); 
				e = fac.op (CILAssignOp.ASSIGN).naryApply 
				(new Expr[] {e1, e2, fac.intExpr (assignExpr_AST.getLineNum ())});
				
			}
			assignExpr_AST = (TNode)currentAST.root;
		}
		catch (RecognitionException ex) {
			if (inputState.guessing==0) {
				reportError(ex);
				if (_t!=null) {_t = _t.getNextSibling();}
			} else {
			  throw ex;
			}
		}
		returnAST = assignExpr_AST;
		_retTree = _t;
		return e;
	}
	
	public final Expr  logicalOrExpr(AST _t,
		ExprFactory fac
	) throws RecognitionException {
		Expr e;
		
		TNode logicalOrExpr_AST_in = (_t == ASTNULL) ? null : (TNode)_t;
		returnAST = null;
		ASTPair currentAST = new ASTPair();
		TNode logicalOrExpr_AST = null;
		
		e = null;
		Expr e1;
		Expr e2;
		
		
		try {      // for error handling
			AST __t159 = _t;
			TNode tmp87_AST = null;
			TNode tmp87_AST_in = null;
			tmp87_AST = (TNode)astFactory.create((TNode)_t);
			tmp87_AST_in = (TNode)_t;
			astFactory.addASTChild(currentAST, tmp87_AST);
			ASTPair __currentAST159 = currentAST.copy();
			currentAST.root = currentAST.child;
			currentAST.child = null;
			match(_t,LOR);
			_t = _t.getFirstChild();
			e1=expr(_t,fac);
			_t = _retTree;
			astFactory.addASTChild(currentAST, returnAST);
			e2=expr(_t,fac);
			_t = _retTree;
			astFactory.addASTChild(currentAST, returnAST);
			currentAST = __currentAST159;
			_t = __t159;
			_t = _t.getNextSibling();
			if ( inputState.guessing==0 ) {
				e = fac.op (BoolOp.OR).binApply (e1, e2);
			}
			logicalOrExpr_AST = (TNode)currentAST.root;
		}
		catch (RecognitionException ex) {
			if (inputState.guessing==0) {
				reportError(ex);
				if (_t!=null) {_t = _t.getNextSibling();}
			} else {
			  throw ex;
			}
		}
		returnAST = logicalOrExpr_AST;
		_retTree = _t;
		return e;
	}
	
	public final Expr  logicalAndExpr(AST _t,
		ExprFactory fac
	) throws RecognitionException {
		Expr e;
		
		TNode logicalAndExpr_AST_in = (_t == ASTNULL) ? null : (TNode)_t;
		returnAST = null;
		ASTPair currentAST = new ASTPair();
		TNode logicalAndExpr_AST = null;
		
		e = null;
		Expr e1;
		Expr e2;
		
		
		try {      // for error handling
			AST __t161 = _t;
			TNode tmp88_AST = null;
			TNode tmp88_AST_in = null;
			tmp88_AST = (TNode)astFactory.create((TNode)_t);
			tmp88_AST_in = (TNode)_t;
			astFactory.addASTChild(currentAST, tmp88_AST);
			ASTPair __currentAST161 = currentAST.copy();
			currentAST.root = currentAST.child;
			currentAST.child = null;
			match(_t,LAND);
			_t = _t.getFirstChild();
			e1=expr(_t,fac);
			_t = _retTree;
			astFactory.addASTChild(currentAST, returnAST);
			e2=expr(_t,fac);
			_t = _retTree;
			astFactory.addASTChild(currentAST, returnAST);
			currentAST = __currentAST161;
			_t = __t161;
			_t = _t.getNextSibling();
			if ( inputState.guessing==0 ) {
				e = fac.op (BoolOp.AND).binApply (e1, e2);
			}
			logicalAndExpr_AST = (TNode)currentAST.root;
		}
		catch (RecognitionException ex) {
			if (inputState.guessing==0) {
				reportError(ex);
				if (_t!=null) {_t = _t.getNextSibling();}
			} else {
			  throw ex;
			}
		}
		returnAST = logicalAndExpr_AST;
		_retTree = _t;
		return e;
	}
	
	public final Expr  inclusiveOrExpr(AST _t,
		ExprFactory fac
	) throws RecognitionException {
		Expr e;
		
		TNode inclusiveOrExpr_AST_in = (_t == ASTNULL) ? null : (TNode)_t;
		returnAST = null;
		ASTPair currentAST = new ASTPair();
		TNode inclusiveOrExpr_AST = null;
		
		e = null;
		Expr e1;
		Expr e2;
		
		
		try {      // for error handling
			AST __t163 = _t;
			TNode tmp89_AST = null;
			TNode tmp89_AST_in = null;
			tmp89_AST = (TNode)astFactory.create((TNode)_t);
			tmp89_AST_in = (TNode)_t;
			astFactory.addASTChild(currentAST, tmp89_AST);
			ASTPair __currentAST163 = currentAST.copy();
			currentAST.root = currentAST.child;
			currentAST.child = null;
			match(_t,BOR);
			_t = _t.getFirstChild();
			e1=expr(_t,fac);
			_t = _retTree;
			astFactory.addASTChild(currentAST, returnAST);
			e2=expr(_t,fac);
			_t = _retTree;
			astFactory.addASTChild(currentAST, returnAST);
			currentAST = __currentAST163;
			_t = __t163;
			_t = _t.getNextSibling();
			if ( inputState.guessing==0 ) {
				e = fac.op (CILBitwiseOp.BOR).binApply (e1, e2);
			}
			inclusiveOrExpr_AST = (TNode)currentAST.root;
		}
		catch (RecognitionException ex) {
			if (inputState.guessing==0) {
				reportError(ex);
				if (_t!=null) {_t = _t.getNextSibling();}
			} else {
			  throw ex;
			}
		}
		returnAST = inclusiveOrExpr_AST;
		_retTree = _t;
		return e;
	}
	
	public final Expr  exclusiveOrExpr(AST _t,
		ExprFactory fac
	) throws RecognitionException {
		Expr e;
		
		TNode exclusiveOrExpr_AST_in = (_t == ASTNULL) ? null : (TNode)_t;
		returnAST = null;
		ASTPair currentAST = new ASTPair();
		TNode exclusiveOrExpr_AST = null;
		
		e = null;
		Expr e1;
		Expr e2;
		
		
		try {      // for error handling
			AST __t165 = _t;
			TNode tmp90_AST = null;
			TNode tmp90_AST_in = null;
			tmp90_AST = (TNode)astFactory.create((TNode)_t);
			tmp90_AST_in = (TNode)_t;
			astFactory.addASTChild(currentAST, tmp90_AST);
			ASTPair __currentAST165 = currentAST.copy();
			currentAST.root = currentAST.child;
			currentAST.child = null;
			match(_t,BXOR);
			_t = _t.getFirstChild();
			e1=expr(_t,fac);
			_t = _retTree;
			astFactory.addASTChild(currentAST, returnAST);
			e2=expr(_t,fac);
			_t = _retTree;
			astFactory.addASTChild(currentAST, returnAST);
			currentAST = __currentAST165;
			_t = __t165;
			_t = _t.getNextSibling();
			if ( inputState.guessing==0 ) {
				e = fac.op (CILBitwiseOp.BXOR).binApply (e1, e2);
			}
			exclusiveOrExpr_AST = (TNode)currentAST.root;
		}
		catch (RecognitionException ex) {
			if (inputState.guessing==0) {
				reportError(ex);
				if (_t!=null) {_t = _t.getNextSibling();}
			} else {
			  throw ex;
			}
		}
		returnAST = exclusiveOrExpr_AST;
		_retTree = _t;
		return e;
	}
	
	public final Expr  bitAndExpr(AST _t,
		ExprFactory fac
	) throws RecognitionException {
		Expr e;
		
		TNode bitAndExpr_AST_in = (_t == ASTNULL) ? null : (TNode)_t;
		returnAST = null;
		ASTPair currentAST = new ASTPair();
		TNode bitAndExpr_AST = null;
		
		e = null;
		Expr e1;
		Expr e2;
		
		
		try {      // for error handling
			AST __t167 = _t;
			TNode tmp91_AST = null;
			TNode tmp91_AST_in = null;
			tmp91_AST = (TNode)astFactory.create((TNode)_t);
			tmp91_AST_in = (TNode)_t;
			astFactory.addASTChild(currentAST, tmp91_AST);
			ASTPair __currentAST167 = currentAST.copy();
			currentAST.root = currentAST.child;
			currentAST.child = null;
			match(_t,BAND);
			_t = _t.getFirstChild();
			e1=expr(_t,fac);
			_t = _retTree;
			astFactory.addASTChild(currentAST, returnAST);
			e2=expr(_t,fac);
			_t = _retTree;
			astFactory.addASTChild(currentAST, returnAST);
			currentAST = __currentAST167;
			_t = __t167;
			_t = _t.getNextSibling();
			if ( inputState.guessing==0 ) {
				e = fac.op (CILBitwiseOp.BAND).binApply (e1, e2);
			}
			bitAndExpr_AST = (TNode)currentAST.root;
		}
		catch (RecognitionException ex) {
			if (inputState.guessing==0) {
				reportError(ex);
				if (_t!=null) {_t = _t.getNextSibling();}
			} else {
			  throw ex;
			}
		}
		returnAST = bitAndExpr_AST;
		_retTree = _t;
		return e;
	}
	
	public final Expr  shiftExpr(AST _t,
		ExprFactory fac
	) throws RecognitionException {
		Expr e;
		
		TNode shiftExpr_AST_in = (_t == ASTNULL) ? null : (TNode)_t;
		returnAST = null;
		ASTPair currentAST = new ASTPair();
		TNode shiftExpr_AST = null;
		
		e = null;
		Expr e1;
		Expr e2;
		
		
		try {      // for error handling
			if (_t==null) _t=ASTNULL;
			switch ( _t.getType()) {
			case LSHIFT:
			{
				AST __t177 = _t;
				TNode tmp92_AST = null;
				TNode tmp92_AST_in = null;
				tmp92_AST = (TNode)astFactory.create((TNode)_t);
				tmp92_AST_in = (TNode)_t;
				astFactory.addASTChild(currentAST, tmp92_AST);
				ASTPair __currentAST177 = currentAST.copy();
				currentAST.root = currentAST.child;
				currentAST.child = null;
				match(_t,LSHIFT);
				_t = _t.getFirstChild();
				e1=expr(_t,fac);
				_t = _retTree;
				astFactory.addASTChild(currentAST, returnAST);
				e2=expr(_t,fac);
				_t = _retTree;
				astFactory.addASTChild(currentAST, returnAST);
				currentAST = __currentAST177;
				_t = __t177;
				_t = _t.getNextSibling();
				if ( inputState.guessing==0 ) {
					e = fac.op (CILBitwiseOp.LSHIFT).binApply (e1, e2);
				}
				shiftExpr_AST = (TNode)currentAST.root;
				break;
			}
			case RSHIFT:
			{
				AST __t178 = _t;
				TNode tmp93_AST = null;
				TNode tmp93_AST_in = null;
				tmp93_AST = (TNode)astFactory.create((TNode)_t);
				tmp93_AST_in = (TNode)_t;
				astFactory.addASTChild(currentAST, tmp93_AST);
				ASTPair __currentAST178 = currentAST.copy();
				currentAST.root = currentAST.child;
				currentAST.child = null;
				match(_t,RSHIFT);
				_t = _t.getFirstChild();
				e1=expr(_t,fac);
				_t = _retTree;
				astFactory.addASTChild(currentAST, returnAST);
				e2=expr(_t,fac);
				_t = _retTree;
				astFactory.addASTChild(currentAST, returnAST);
				currentAST = __currentAST178;
				_t = __t178;
				_t = _t.getNextSibling();
				if ( inputState.guessing==0 ) {
					e = fac.op (CILBitwiseOp.RSHIFT).binApply (e1, e2);
				}
				shiftExpr_AST = (TNode)currentAST.root;
				break;
			}
			default:
			{
				throw new NoViableAltException(_t);
			}
			}
		}
		catch (RecognitionException ex) {
			if (inputState.guessing==0) {
				reportError(ex);
				if (_t!=null) {_t = _t.getNextSibling();}
			} else {
			  throw ex;
			}
		}
		returnAST = shiftExpr_AST;
		_retTree = _t;
		return e;
	}
	
	public final Expr  equalityExpr(AST _t,
		ExprFactory fac
	) throws RecognitionException {
		Expr e;
		
		TNode equalityExpr_AST_in = (_t == ASTNULL) ? null : (TNode)_t;
		returnAST = null;
		ASTPair currentAST = new ASTPair();
		TNode equalityExpr_AST = null;
		
		e = null;
		Expr e1;
		Expr e2;
		
		
		try {      // for error handling
			if (_t==null) _t=ASTNULL;
			switch ( _t.getType()) {
			case EQUAL:
			{
				AST __t169 = _t;
				TNode tmp94_AST = null;
				TNode tmp94_AST_in = null;
				tmp94_AST = (TNode)astFactory.create((TNode)_t);
				tmp94_AST_in = (TNode)_t;
				astFactory.addASTChild(currentAST, tmp94_AST);
				ASTPair __currentAST169 = currentAST.copy();
				currentAST.root = currentAST.child;
				currentAST.child = null;
				match(_t,EQUAL);
				_t = _t.getFirstChild();
				e1=expr(_t,fac);
				_t = _retTree;
				astFactory.addASTChild(currentAST, returnAST);
				e2=expr(_t,fac);
				_t = _retTree;
				astFactory.addASTChild(currentAST, returnAST);
				currentAST = __currentAST169;
				_t = __t169;
				_t = _t.getNextSibling();
				if ( inputState.guessing==0 ) {
					e = fac.op (ComparisonOp.EQ).binApply (e1, e2);
				}
				equalityExpr_AST = (TNode)currentAST.root;
				break;
			}
			case NOT_EQUAL:
			{
				AST __t170 = _t;
				TNode tmp95_AST = null;
				TNode tmp95_AST_in = null;
				tmp95_AST = (TNode)astFactory.create((TNode)_t);
				tmp95_AST_in = (TNode)_t;
				astFactory.addASTChild(currentAST, tmp95_AST);
				ASTPair __currentAST170 = currentAST.copy();
				currentAST.root = currentAST.child;
				currentAST.child = null;
				match(_t,NOT_EQUAL);
				_t = _t.getFirstChild();
				e1=expr(_t,fac);
				_t = _retTree;
				astFactory.addASTChild(currentAST, returnAST);
				e2=expr(_t,fac);
				_t = _retTree;
				astFactory.addASTChild(currentAST, returnAST);
				currentAST = __currentAST170;
				_t = __t170;
				_t = _t.getNextSibling();
				if ( inputState.guessing==0 ) {
					
					e = fac.op (BoolOp.NOT).unaryApply 
					(fac.op (ComparisonOp.EQ).binApply (e1, e2));
					
				}
				equalityExpr_AST = (TNode)currentAST.root;
				break;
			}
			default:
			{
				throw new NoViableAltException(_t);
			}
			}
		}
		catch (RecognitionException ex) {
			if (inputState.guessing==0) {
				reportError(ex);
				if (_t!=null) {_t = _t.getNextSibling();}
			} else {
			  throw ex;
			}
		}
		returnAST = equalityExpr_AST;
		_retTree = _t;
		return e;
	}
	
	public final Expr  relationalExpr(AST _t,
		ExprFactory fac
	) throws RecognitionException {
		Expr e;
		
		TNode relationalExpr_AST_in = (_t == ASTNULL) ? null : (TNode)_t;
		returnAST = null;
		ASTPair currentAST = new ASTPair();
		TNode relationalExpr_AST = null;
		
		e = null;
		Expr e1;
		Expr e2;
		
		
		try {      // for error handling
			if (_t==null) _t=ASTNULL;
			switch ( _t.getType()) {
			case LT:
			{
				AST __t172 = _t;
				TNode tmp96_AST = null;
				TNode tmp96_AST_in = null;
				tmp96_AST = (TNode)astFactory.create((TNode)_t);
				tmp96_AST_in = (TNode)_t;
				astFactory.addASTChild(currentAST, tmp96_AST);
				ASTPair __currentAST172 = currentAST.copy();
				currentAST.root = currentAST.child;
				currentAST.child = null;
				match(_t,LT);
				_t = _t.getFirstChild();
				e1=expr(_t,fac);
				_t = _retTree;
				astFactory.addASTChild(currentAST, returnAST);
				e2=expr(_t,fac);
				_t = _retTree;
				astFactory.addASTChild(currentAST, returnAST);
				currentAST = __currentAST172;
				_t = __t172;
				_t = _t.getNextSibling();
				if ( inputState.guessing==0 ) {
					e = fac.op (ComparisonOp.LT).binApply (e1, e2);
				}
				relationalExpr_AST = (TNode)currentAST.root;
				break;
			}
			case LTE:
			{
				AST __t173 = _t;
				TNode tmp97_AST = null;
				TNode tmp97_AST_in = null;
				tmp97_AST = (TNode)astFactory.create((TNode)_t);
				tmp97_AST_in = (TNode)_t;
				astFactory.addASTChild(currentAST, tmp97_AST);
				ASTPair __currentAST173 = currentAST.copy();
				currentAST.root = currentAST.child;
				currentAST.child = null;
				match(_t,LTE);
				_t = _t.getFirstChild();
				e1=expr(_t,fac);
				_t = _retTree;
				astFactory.addASTChild(currentAST, returnAST);
				e2=expr(_t,fac);
				_t = _retTree;
				astFactory.addASTChild(currentAST, returnAST);
				currentAST = __currentAST173;
				_t = __t173;
				_t = _t.getNextSibling();
				if ( inputState.guessing==0 ) {
					e = fac.op (ComparisonOp.LEQ).binApply (e1, e2);
				}
				relationalExpr_AST = (TNode)currentAST.root;
				break;
			}
			case GT:
			{
				AST __t174 = _t;
				TNode tmp98_AST = null;
				TNode tmp98_AST_in = null;
				tmp98_AST = (TNode)astFactory.create((TNode)_t);
				tmp98_AST_in = (TNode)_t;
				astFactory.addASTChild(currentAST, tmp98_AST);
				ASTPair __currentAST174 = currentAST.copy();
				currentAST.root = currentAST.child;
				currentAST.child = null;
				match(_t,GT);
				_t = _t.getFirstChild();
				e1=expr(_t,fac);
				_t = _retTree;
				astFactory.addASTChild(currentAST, returnAST);
				e2=expr(_t,fac);
				_t = _retTree;
				astFactory.addASTChild(currentAST, returnAST);
				currentAST = __currentAST174;
				_t = __t174;
				_t = _t.getNextSibling();
				if ( inputState.guessing==0 ) {
					e = fac.op (ComparisonOp.GT).binApply (e1, e2);
				}
				relationalExpr_AST = (TNode)currentAST.root;
				break;
			}
			case GTE:
			{
				AST __t175 = _t;
				TNode tmp99_AST = null;
				TNode tmp99_AST_in = null;
				tmp99_AST = (TNode)astFactory.create((TNode)_t);
				tmp99_AST_in = (TNode)_t;
				astFactory.addASTChild(currentAST, tmp99_AST);
				ASTPair __currentAST175 = currentAST.copy();
				currentAST.root = currentAST.child;
				currentAST.child = null;
				match(_t,GTE);
				_t = _t.getFirstChild();
				e1=expr(_t,fac);
				_t = _retTree;
				astFactory.addASTChild(currentAST, returnAST);
				e2=expr(_t,fac);
				_t = _retTree;
				astFactory.addASTChild(currentAST, returnAST);
				currentAST = __currentAST175;
				_t = __t175;
				_t = _t.getNextSibling();
				if ( inputState.guessing==0 ) {
					e = fac.op (ComparisonOp.GEQ).binApply (e1, e2);
				}
				relationalExpr_AST = (TNode)currentAST.root;
				break;
			}
			default:
			{
				throw new NoViableAltException(_t);
			}
			}
		}
		catch (RecognitionException ex) {
			if (inputState.guessing==0) {
				reportError(ex);
				if (_t!=null) {_t = _t.getNextSibling();}
			} else {
			  throw ex;
			}
		}
		returnAST = relationalExpr_AST;
		_retTree = _t;
		return e;
	}
	
	public final Expr  additiveExpr(AST _t,
		ExprFactory fac
	) throws RecognitionException {
		Expr e;
		
		TNode additiveExpr_AST_in = (_t == ASTNULL) ? null : (TNode)_t;
		returnAST = null;
		ASTPair currentAST = new ASTPair();
		TNode additiveExpr_AST = null;
		
		e = null;
		Expr e1;
		Expr e2;
		
		
		try {      // for error handling
			if (_t==null) _t=ASTNULL;
			switch ( _t.getType()) {
			case PLUS:
			{
				AST __t180 = _t;
				TNode tmp100_AST = null;
				TNode tmp100_AST_in = null;
				tmp100_AST = (TNode)astFactory.create((TNode)_t);
				tmp100_AST_in = (TNode)_t;
				astFactory.addASTChild(currentAST, tmp100_AST);
				ASTPair __currentAST180 = currentAST.copy();
				currentAST.root = currentAST.child;
				currentAST.child = null;
				match(_t,PLUS);
				_t = _t.getFirstChild();
				e1=expr(_t,fac);
				_t = _retTree;
				astFactory.addASTChild(currentAST, returnAST);
				e2=expr(_t,fac);
				_t = _retTree;
				astFactory.addASTChild(currentAST, returnAST);
				currentAST = __currentAST180;
				_t = __t180;
				_t = _t.getNextSibling();
				if ( inputState.guessing==0 ) {
					e = fac.op (NumericOp.PLUS).binApply (e1, e2);
				}
				additiveExpr_AST = (TNode)currentAST.root;
				break;
			}
			case MINUS:
			{
				AST __t181 = _t;
				TNode tmp101_AST = null;
				TNode tmp101_AST_in = null;
				tmp101_AST = (TNode)astFactory.create((TNode)_t);
				tmp101_AST_in = (TNode)_t;
				astFactory.addASTChild(currentAST, tmp101_AST);
				ASTPair __currentAST181 = currentAST.copy();
				currentAST.root = currentAST.child;
				currentAST.child = null;
				match(_t,MINUS);
				_t = _t.getFirstChild();
				e1=expr(_t,fac);
				_t = _retTree;
				astFactory.addASTChild(currentAST, returnAST);
				e2=expr(_t,fac);
				_t = _retTree;
				astFactory.addASTChild(currentAST, returnAST);
				currentAST = __currentAST181;
				_t = __t181;
				_t = _t.getNextSibling();
				if ( inputState.guessing==0 ) {
					e = fac.op (NumericOp.MINUS).binApply (e1, e2);
				}
				additiveExpr_AST = (TNode)currentAST.root;
				break;
			}
			default:
			{
				throw new NoViableAltException(_t);
			}
			}
		}
		catch (RecognitionException ex) {
			if (inputState.guessing==0) {
				reportError(ex);
				if (_t!=null) {_t = _t.getNextSibling();}
			} else {
			  throw ex;
			}
		}
		returnAST = additiveExpr_AST;
		_retTree = _t;
		return e;
	}
	
	public final Expr  multExpr(AST _t,
		ExprFactory fac
	) throws RecognitionException {
		Expr e;
		
		TNode multExpr_AST_in = (_t == ASTNULL) ? null : (TNode)_t;
		returnAST = null;
		ASTPair currentAST = new ASTPair();
		TNode multExpr_AST = null;
		
		e = null;
		Expr e1;
		Expr e2;
		
		
		try {      // for error handling
			if (_t==null) _t=ASTNULL;
			switch ( _t.getType()) {
			case STAR:
			{
				AST __t183 = _t;
				TNode tmp102_AST = null;
				TNode tmp102_AST_in = null;
				tmp102_AST = (TNode)astFactory.create((TNode)_t);
				tmp102_AST_in = (TNode)_t;
				astFactory.addASTChild(currentAST, tmp102_AST);
				ASTPair __currentAST183 = currentAST.copy();
				currentAST.root = currentAST.child;
				currentAST.child = null;
				match(_t,STAR);
				_t = _t.getFirstChild();
				e1=expr(_t,fac);
				_t = _retTree;
				astFactory.addASTChild(currentAST, returnAST);
				e2=expr(_t,fac);
				_t = _retTree;
				astFactory.addASTChild(currentAST, returnAST);
				currentAST = __currentAST183;
				_t = __t183;
				_t = _t.getNextSibling();
				if ( inputState.guessing==0 ) {
					e = fac.op (NumericOp.MULT).binApply (e1, e2);
				}
				multExpr_AST = (TNode)currentAST.root;
				break;
			}
			case DIV:
			{
				AST __t184 = _t;
				TNode tmp103_AST = null;
				TNode tmp103_AST_in = null;
				tmp103_AST = (TNode)astFactory.create((TNode)_t);
				tmp103_AST_in = (TNode)_t;
				astFactory.addASTChild(currentAST, tmp103_AST);
				ASTPair __currentAST184 = currentAST.copy();
				currentAST.root = currentAST.child;
				currentAST.child = null;
				match(_t,DIV);
				_t = _t.getFirstChild();
				e1=expr(_t,fac);
				_t = _retTree;
				astFactory.addASTChild(currentAST, returnAST);
				e2=expr(_t,fac);
				_t = _retTree;
				astFactory.addASTChild(currentAST, returnAST);
				currentAST = __currentAST184;
				_t = __t184;
				_t = _t.getNextSibling();
				if ( inputState.guessing==0 ) {
					e = fac.op (NumericOp.DIV).binApply (e1, e2);
				}
				multExpr_AST = (TNode)currentAST.root;
				break;
			}
			case MOD:
			{
				AST __t185 = _t;
				TNode tmp104_AST = null;
				TNode tmp104_AST_in = null;
				tmp104_AST = (TNode)astFactory.create((TNode)_t);
				tmp104_AST_in = (TNode)_t;
				astFactory.addASTChild(currentAST, tmp104_AST);
				ASTPair __currentAST185 = currentAST.copy();
				currentAST.root = currentAST.child;
				currentAST.child = null;
				match(_t,MOD);
				_t = _t.getFirstChild();
				e1=expr(_t,fac);
				_t = _retTree;
				astFactory.addASTChild(currentAST, returnAST);
				e2=expr(_t,fac);
				_t = _retTree;
				astFactory.addASTChild(currentAST, returnAST);
				currentAST = __currentAST185;
				_t = __t185;
				_t = _t.getNextSibling();
				if ( inputState.guessing==0 ) {
					e = fac.op (NumericOp.MOD).binApply (e1, e2);
				}
				multExpr_AST = (TNode)currentAST.root;
				break;
			}
			default:
			{
				throw new NoViableAltException(_t);
			}
			}
		}
		catch (RecognitionException ex) {
			if (inputState.guessing==0) {
				reportError(ex);
				if (_t!=null) {_t = _t.getNextSibling();}
			} else {
			  throw ex;
			}
		}
		returnAST = multExpr_AST;
		_retTree = _t;
		return e;
	}
	
	public final Expr  castExpr(AST _t,
		ExprFactory fac
	) throws RecognitionException {
		Expr e;
		
		TNode castExpr_AST_in = (_t == ASTNULL) ? null : (TNode)_t;
		returnAST = null;
		ASTPair currentAST = new ASTPair();
		TNode castExpr_AST = null;
		e = null;
		
		try {      // for error handling
			AST __t187 = _t;
			TNode tmp105_AST = null;
			TNode tmp105_AST_in = null;
			tmp105_AST = (TNode)astFactory.create((TNode)_t);
			tmp105_AST_in = (TNode)_t;
			astFactory.addASTChild(currentAST, tmp105_AST);
			ASTPair __currentAST187 = currentAST.copy();
			currentAST.root = currentAST.child;
			currentAST.child = null;
			match(_t,NCast);
			_t = _t.getFirstChild();
			typeName(_t,fac);
			_t = _retTree;
			astFactory.addASTChild(currentAST, returnAST);
			TNode tmp106_AST = null;
			TNode tmp106_AST_in = null;
			tmp106_AST = (TNode)astFactory.create((TNode)_t);
			tmp106_AST_in = (TNode)_t;
			astFactory.addASTChild(currentAST, tmp106_AST);
			match(_t,RPAREN);
			_t = _t.getNextSibling();
			e=expr(_t,fac);
			_t = _retTree;
			astFactory.addASTChild(currentAST, returnAST);
			currentAST = __currentAST187;
			_t = __t187;
			_t = _t.getNextSibling();
			castExpr_AST = (TNode)currentAST.root;
		}
		catch (RecognitionException ex) {
			if (inputState.guessing==0) {
				reportError(ex);
				if (_t!=null) {_t = _t.getNextSibling();}
			} else {
			  throw ex;
			}
		}
		returnAST = castExpr_AST;
		_retTree = _t;
		return e;
	}
	
	public final Expr  unaryExpr(AST _t,
		ExprFactory fac
	) throws RecognitionException {
		Expr e;
		
		TNode unaryExpr_AST_in = (_t == ASTNULL) ? null : (TNode)_t;
		returnAST = null;
		ASTPair currentAST = new ASTPair();
		TNode unaryExpr_AST = null;
		
		e = null;
		Operator op;
		Expr e1;
		Expr discard;
		
		
		try {      // for error handling
			if (_t==null) _t=ASTNULL;
			switch ( _t.getType()) {
			case INC:
			{
				AST __t206 = _t;
				TNode tmp107_AST = null;
				TNode tmp107_AST_in = null;
				tmp107_AST = (TNode)astFactory.create((TNode)_t);
				tmp107_AST_in = (TNode)_t;
				astFactory.addASTChild(currentAST, tmp107_AST);
				ASTPair __currentAST206 = currentAST.copy();
				currentAST.root = currentAST.child;
				currentAST.child = null;
				match(_t,INC);
				_t = _t.getFirstChild();
				e1=expr(_t,fac);
				_t = _retTree;
				astFactory.addASTChild(currentAST, returnAST);
				currentAST = __currentAST206;
				_t = __t206;
				_t = _t.getNextSibling();
				if ( inputState.guessing==0 ) {
					
					e = fac.op (CILAssignOp.ASSIGN).binApply
					(e1, fac.op (NumericOp.PLUS).binApply (e1, fac.intExpr (1)));
					
				}
				unaryExpr_AST = (TNode)currentAST.root;
				break;
			}
			case DEC:
			{
				AST __t207 = _t;
				TNode tmp108_AST = null;
				TNode tmp108_AST_in = null;
				tmp108_AST = (TNode)astFactory.create((TNode)_t);
				tmp108_AST_in = (TNode)_t;
				astFactory.addASTChild(currentAST, tmp108_AST);
				ASTPair __currentAST207 = currentAST.copy();
				currentAST.root = currentAST.child;
				currentAST.child = null;
				match(_t,DEC);
				_t = _t.getFirstChild();
				e1=expr(_t,fac);
				_t = _retTree;
				astFactory.addASTChild(currentAST, returnAST);
				currentAST = __currentAST207;
				_t = __t207;
				_t = _t.getNextSibling();
				if ( inputState.guessing==0 ) {
					
					e = fac.op (CILAssignOp.ASSIGN).binApply
					(e1, fac.op (NumericOp.MINUS).binApply (e1, fac.intExpr (1)));
					
				}
				unaryExpr_AST = (TNode)currentAST.root;
				break;
			}
			case NUnaryExpr:
			{
				AST __t208 = _t;
				TNode tmp109_AST = null;
				TNode tmp109_AST_in = null;
				tmp109_AST = (TNode)astFactory.create((TNode)_t);
				tmp109_AST_in = (TNode)_t;
				astFactory.addASTChild(currentAST, tmp109_AST);
				ASTPair __currentAST208 = currentAST.copy();
				currentAST.root = currentAST.child;
				currentAST.child = null;
				match(_t,NUnaryExpr);
				_t = _t.getFirstChild();
				op=unaryOperator(_t);
				_t = _retTree;
				astFactory.addASTChild(currentAST, returnAST);
				e1=expr(_t,fac);
				_t = _retTree;
				astFactory.addASTChild(currentAST, returnAST);
				currentAST = __currentAST208;
				_t = __t208;
				_t = _t.getNextSibling();
				if ( inputState.guessing==0 ) {
					e = fac.op (op).unaryApply (e1);
				}
				unaryExpr_AST = (TNode)currentAST.root;
				break;
			}
			case LITERAL_sizeof:
			{
				AST __t209 = _t;
				TNode tmp110_AST = null;
				TNode tmp110_AST_in = null;
				tmp110_AST = (TNode)astFactory.create((TNode)_t);
				tmp110_AST_in = (TNode)_t;
				astFactory.addASTChild(currentAST, tmp110_AST);
				ASTPair __currentAST209 = currentAST.copy();
				currentAST.root = currentAST.child;
				currentAST.child = null;
				match(_t,LITERAL_sizeof);
				_t = _t.getFirstChild();
				{
				boolean synPredMatched212 = false;
				if (((_t.getType()==LPAREN))) {
					AST __t212 = _t;
					synPredMatched212 = true;
					inputState.guessing++;
					try {
						{
						match(_t,LPAREN);
						_t = _t.getNextSibling();
						typeName(_t,fac);
						_t = _retTree;
						}
					}
					catch (RecognitionException pe) {
						synPredMatched212 = false;
					}
					_t = __t212;
					inputState.guessing--;
				}
				if ( synPredMatched212 ) {
					TNode tmp111_AST = null;
					TNode tmp111_AST_in = null;
					tmp111_AST = (TNode)astFactory.create((TNode)_t);
					tmp111_AST_in = (TNode)_t;
					astFactory.addASTChild(currentAST, tmp111_AST);
					match(_t,LPAREN);
					_t = _t.getNextSibling();
					typeName(_t,fac);
					_t = _retTree;
					astFactory.addASTChild(currentAST, returnAST);
					TNode tmp112_AST = null;
					TNode tmp112_AST_in = null;
					tmp112_AST = (TNode)astFactory.create((TNode)_t);
					tmp112_AST_in = (TNode)_t;
					astFactory.addASTChild(currentAST, tmp112_AST);
					match(_t,RPAREN);
					_t = _t.getNextSibling();
				}
				else if ((_tokenSet_3.member(_t.getType()))) {
					discard=expr(_t,fac);
					_t = _retTree;
					astFactory.addASTChild(currentAST, returnAST);
				}
				else {
					throw new NoViableAltException(_t);
				}
				
				}
				currentAST = __currentAST209;
				_t = __t209;
				_t = _t.getNextSibling();
				unaryExpr_AST = (TNode)currentAST.root;
				break;
			}
			default:
			{
				throw new NoViableAltException(_t);
			}
			}
		}
		catch (RecognitionException ex) {
			if (inputState.guessing==0) {
				reportError(ex);
				if (_t!=null) {_t = _t.getNextSibling();}
			} else {
			  throw ex;
			}
		}
		returnAST = unaryExpr_AST;
		_retTree = _t;
		return e;
	}
	
	public final Expr  postfixExpr(AST _t,
		ExprFactory fac
	) throws RecognitionException {
		Expr e;
		
		TNode postfixExpr_AST_in = (_t == ASTNULL) ? null : (TNode)_t;
		returnAST = null;
		ASTPair currentAST = new ASTPair();
		TNode postfixExpr_AST = null;
		TNode id1 = null;
		TNode id1_AST = null;
		TNode id2 = null;
		TNode id2_AST = null;
		
		e = null;
		Expr discard;
		Expr ael = null;
		
		
		try {      // for error handling
			AST __t216 = _t;
			TNode tmp113_AST = null;
			TNode tmp113_AST_in = null;
			tmp113_AST = (TNode)astFactory.create((TNode)_t);
			tmp113_AST_in = (TNode)_t;
			astFactory.addASTChild(currentAST, tmp113_AST);
			ASTPair __currentAST216 = currentAST.copy();
			currentAST.root = currentAST.child;
			currentAST.child = null;
			match(_t,NPostfixExpr);
			_t = _t.getFirstChild();
			e=primaryExpr(_t,fac);
			_t = _retTree;
			astFactory.addASTChild(currentAST, returnAST);
			{
			_loop220:
			do {
				if (_t==null) _t=ASTNULL;
				switch ( _t.getType()) {
				case PTR:
				{
					TNode tmp114_AST = null;
					TNode tmp114_AST_in = null;
					tmp114_AST = (TNode)astFactory.create((TNode)_t);
					tmp114_AST_in = (TNode)_t;
					astFactory.addASTChild(currentAST, tmp114_AST);
					match(_t,PTR);
					_t = _t.getNextSibling();
					id1 = (TNode)_t;
					TNode id1_AST_in = null;
					id1_AST = (TNode)astFactory.create(id1);
					astFactory.addASTChild(currentAST, id1_AST);
					match(_t,ID);
					_t = _t.getNextSibling();
					if ( inputState.guessing==0 ) {
						
						e = fac.op (CILIndirectionOp.ARROW).binApply
						(e, fac.var (id1.getText ()));
						
					}
					break;
				}
				case DOT:
				{
					TNode tmp115_AST = null;
					TNode tmp115_AST_in = null;
					tmp115_AST = (TNode)astFactory.create((TNode)_t);
					tmp115_AST_in = (TNode)_t;
					astFactory.addASTChild(currentAST, tmp115_AST);
					match(_t,DOT);
					_t = _t.getNextSibling();
					id2 = (TNode)_t;
					TNode id2_AST_in = null;
					id2_AST = (TNode)astFactory.create(id2);
					astFactory.addASTChild(currentAST, id2_AST);
					match(_t,ID);
					_t = _t.getNextSibling();
					if ( inputState.guessing==0 ) {
						
						e = fac.op (CILIndirectionOp.DOT).binApply
						(e, fac.var (id2.getText ()));
						
					}
					break;
				}
				case NFunctionCallArgs:
				{
					AST __t218 = _t;
					TNode tmp116_AST = null;
					TNode tmp116_AST_in = null;
					tmp116_AST = (TNode)astFactory.create((TNode)_t);
					tmp116_AST_in = (TNode)_t;
					astFactory.addASTChild(currentAST, tmp116_AST);
					ASTPair __currentAST218 = currentAST.copy();
					currentAST.root = currentAST.child;
					currentAST.child = null;
					match(_t,NFunctionCallArgs);
					_t = _t.getFirstChild();
					{
					if (_t==null) _t=ASTNULL;
					switch ( _t.getType()) {
					case ID:
					case ASSIGN:
					case STAR:
					case LPAREN:
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
					case INC:
					case DEC:
					case LITERAL_sizeof:
					case CharLiteral:
					case NCast:
					case NExpressionGroup:
					case NInitializer:
					case NUnaryExpr:
					case NPostfixExpr:
					case NStringSeq:
					case NLcurlyInitializer:
					case NGnuAsmExpr:
					case Number:
					{
						ael=argExprList(_t,fac);
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
					TNode tmp117_AST = null;
					TNode tmp117_AST_in = null;
					tmp117_AST = (TNode)astFactory.create((TNode)_t);
					tmp117_AST_in = (TNode)_t;
					astFactory.addASTChild(currentAST, tmp117_AST);
					match(_t,RPAREN);
					_t = _t.getNextSibling();
					currentAST = __currentAST218;
					_t = __t218;
					_t = _t.getNextSibling();
					if ( inputState.guessing==0 ) {
						postfixExpr_AST = (TNode)currentAST.root;
						
									// XXX function pointers not supported
									if (e.op () instanceof CILIndirectionOp)
											return null;
						
						if (!(e.op () instanceof VariableOp))
						throw new RecognitionException
						("<<<\n" + e + "(" + e.op ().getClass () + ")\n" + postfixExpr_AST + "\n>>>\n");
						String funcName = ((VariableOp) e.op ()).name ();
						incNumCallSites (funcName);
						/*
						System.err.println ("Parser: Making function call for: " +
						funcName + ", " +
						(getNumCallSites (funcName) - 1));
						*/
						e = fac.op (CILFunctionCallOp.FCALL).naryApply
						(new Expr[]
						{fac.var (funcName), ael, null,
						fac.intExpr (getNumCallSites (funcName) - 1), 
						fac.intExpr (postfixExpr_AST.getLineNum ())});
						// XXX 0-based
						
					}
					break;
				}
				case LBRACKET:
				{
					TNode tmp118_AST = null;
					TNode tmp118_AST_in = null;
					tmp118_AST = (TNode)astFactory.create((TNode)_t);
					tmp118_AST_in = (TNode)_t;
					astFactory.addASTChild(currentAST, tmp118_AST);
					match(_t,LBRACKET);
					_t = _t.getNextSibling();
					discard=expr(_t,fac);
					_t = _retTree;
					astFactory.addASTChild(currentAST, returnAST);
					TNode tmp119_AST = null;
					TNode tmp119_AST_in = null;
					tmp119_AST = (TNode)astFactory.create((TNode)_t);
					tmp119_AST_in = (TNode)_t;
					astFactory.addASTChild(currentAST, tmp119_AST);
					match(_t,RBRACKET);
					_t = _t.getNextSibling();
					break;
				}
				default:
				{
					break _loop220;
				}
				}
			} while (true);
			}
			{
			if (_t==null) _t=ASTNULL;
			switch ( _t.getType()) {
			case INC:
			{
				TNode tmp120_AST = null;
				TNode tmp120_AST_in = null;
				tmp120_AST = (TNode)astFactory.create((TNode)_t);
				tmp120_AST_in = (TNode)_t;
				astFactory.addASTChild(currentAST, tmp120_AST);
				match(_t,INC);
				_t = _t.getNextSibling();
				if ( inputState.guessing==0 ) {
					
					e = fac.op (CILAssignOp.ASSIGN).binApply
					(e, fac.op (NumericOp.PLUS).binApply (e, fac.intExpr (1)));
					
				}
				break;
			}
			case DEC:
			{
				TNode tmp121_AST = null;
				TNode tmp121_AST_in = null;
				tmp121_AST = (TNode)astFactory.create((TNode)_t);
				tmp121_AST_in = (TNode)_t;
				astFactory.addASTChild(currentAST, tmp121_AST);
				match(_t,DEC);
				_t = _t.getNextSibling();
				if ( inputState.guessing==0 ) {
					
					e = fac.op (CILAssignOp.ASSIGN).binApply
					(e, fac.op (NumericOp.MINUS).binApply (e, fac.intExpr (1)));
					
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
			currentAST = __currentAST216;
			_t = __t216;
			_t = _t.getNextSibling();
			postfixExpr_AST = (TNode)currentAST.root;
		}
		catch (RecognitionException ex) {
			if (inputState.guessing==0) {
				reportError(ex);
				if (_t!=null) {_t = _t.getNextSibling();}
			} else {
			  throw ex;
			}
		}
		returnAST = postfixExpr_AST;
		_retTree = _t;
		return e;
	}
	
	public final Expr  primaryExpr(AST _t,
		ExprFactory fac
	) throws RecognitionException {
		Expr e;
		
		TNode primaryExpr_AST_in = (_t == ASTNULL) ? null : (TNode)_t;
		returnAST = null;
		ASTPair currentAST = new ASTPair();
		TNode primaryExpr_AST = null;
		e = null;
		
		try {      // for error handling
			if (_t==null) _t=ASTNULL;
			switch ( _t.getType()) {
			case ID:
			{
				TNode tmp122_AST = null;
				TNode tmp122_AST_in = null;
				tmp122_AST = (TNode)astFactory.create((TNode)_t);
				tmp122_AST_in = (TNode)_t;
				astFactory.addASTChild(currentAST, tmp122_AST);
				match(_t,ID);
				_t = _t.getNextSibling();
				if ( inputState.guessing==0 ) {
					primaryExpr_AST = (TNode)currentAST.root;
					e = fac.var (primaryExpr_AST.getText ());
				}
				primaryExpr_AST = (TNode)currentAST.root;
				break;
			}
			case Number:
			{
				TNode tmp123_AST = null;
				TNode tmp123_AST_in = null;
				tmp123_AST = (TNode)astFactory.create((TNode)_t);
				tmp123_AST_in = (TNode)_t;
				astFactory.addASTChild(currentAST, tmp123_AST);
				match(_t,Number);
				_t = _t.getNextSibling();
				if ( inputState.guessing==0 ) {
					primaryExpr_AST = (TNode)currentAST.root;
					
					try
					{
					e = fac.intExpr (Integer.parseInt (primaryExpr_AST.getText ()));
					}
					catch (NumberFormatException ex)
					{
					// XXX Use 0 for now, if we couldn't parse it as an int above
					e = fac.intExpr (0);
					}
					
				}
				primaryExpr_AST = (TNode)currentAST.root;
				break;
			}
			case CharLiteral:
			{
				charConst(_t);
				_t = _retTree;
				astFactory.addASTChild(currentAST, returnAST);
				if ( inputState.guessing==0 ) {
					primaryExpr_AST = (TNode)currentAST.root;
					e = fac.op (new JavaObjectOp (primaryExpr_AST));
				}
				primaryExpr_AST = (TNode)currentAST.root;
				break;
			}
			case NStringSeq:
			{
				stringConst(_t);
				_t = _retTree;
				astFactory.addASTChild(currentAST, returnAST);
				if ( inputState.guessing==0 ) {
					primaryExpr_AST = (TNode)currentAST.root;
					e = fac.op (new JavaObjectOp (primaryExpr_AST));
				}
				primaryExpr_AST = (TNode)currentAST.root;
				break;
			}
			case NExpressionGroup:
			{
				AST __t223 = _t;
				TNode tmp124_AST = null;
				TNode tmp124_AST_in = null;
				tmp124_AST = (TNode)astFactory.create((TNode)_t);
				tmp124_AST_in = (TNode)_t;
				astFactory.addASTChild(currentAST, tmp124_AST);
				ASTPair __currentAST223 = currentAST.copy();
				currentAST.root = currentAST.child;
				currentAST.child = null;
				match(_t,NExpressionGroup);
				_t = _t.getFirstChild();
				e=expr(_t,fac);
				_t = _retTree;
				astFactory.addASTChild(currentAST, returnAST);
				currentAST = __currentAST223;
				_t = __t223;
				_t = _t.getNextSibling();
				primaryExpr_AST = (TNode)currentAST.root;
				break;
			}
			default:
			{
				throw new NoViableAltException(_t);
			}
			}
		}
		catch (RecognitionException ex) {
			if (inputState.guessing==0) {
				reportError(ex);
				if (_t!=null) {_t = _t.getNextSibling();}
			} else {
			  throw ex;
			}
		}
		returnAST = primaryExpr_AST;
		_retTree = _t;
		return e;
	}
	
	public final void gnuAsmExpr(AST _t,
		ExprFactory fac
	) throws RecognitionException {
		
		TNode gnuAsmExpr_AST_in = (_t == ASTNULL) ? null : (TNode)_t;
		returnAST = null;
		ASTPair currentAST = new ASTPair();
		TNode gnuAsmExpr_AST = null;
		
		try {      // for error handling
			AST __t141 = _t;
			TNode tmp125_AST = null;
			TNode tmp125_AST_in = null;
			tmp125_AST = (TNode)astFactory.create((TNode)_t);
			tmp125_AST_in = (TNode)_t;
			astFactory.addASTChild(currentAST, tmp125_AST);
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
				TNode tmp126_AST = null;
				TNode tmp126_AST_in = null;
				tmp126_AST = (TNode)astFactory.create((TNode)_t);
				tmp126_AST_in = (TNode)_t;
				astFactory.addASTChild(currentAST, tmp126_AST);
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
			TNode tmp127_AST = null;
			TNode tmp127_AST_in = null;
			tmp127_AST = (TNode)astFactory.create((TNode)_t);
			tmp127_AST_in = (TNode)_t;
			astFactory.addASTChild(currentAST, tmp127_AST);
			match(_t,LPAREN);
			_t = _t.getNextSibling();
			stringConst(_t);
			_t = _retTree;
			astFactory.addASTChild(currentAST, returnAST);
			{
			if (_t==null) _t=ASTNULL;
			if ((_t.getType()==COLON)) {
				TNode tmp128_AST = null;
				TNode tmp128_AST_in = null;
				tmp128_AST = (TNode)astFactory.create((TNode)_t);
				tmp128_AST_in = (TNode)_t;
				astFactory.addASTChild(currentAST, tmp128_AST);
				match(_t,COLON);
				_t = _t.getNextSibling();
				{
				if (_t==null) _t=ASTNULL;
				switch ( _t.getType()) {
				case NStringSeq:
				{
					strOptExprPair(_t,fac);
					_t = _retTree;
					astFactory.addASTChild(currentAST, returnAST);
					{
					_loop146:
					do {
						if (_t==null) _t=ASTNULL;
						if ((_t.getType()==COMMA)) {
							TNode tmp129_AST = null;
							TNode tmp129_AST_in = null;
							tmp129_AST = (TNode)astFactory.create((TNode)_t);
							tmp129_AST_in = (TNode)_t;
							astFactory.addASTChild(currentAST, tmp129_AST);
							match(_t,COMMA);
							_t = _t.getNextSibling();
							strOptExprPair(_t,fac);
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
					TNode tmp130_AST = null;
					TNode tmp130_AST_in = null;
					tmp130_AST = (TNode)astFactory.create((TNode)_t);
					tmp130_AST_in = (TNode)_t;
					astFactory.addASTChild(currentAST, tmp130_AST);
					match(_t,COLON);
					_t = _t.getNextSibling();
					{
					if (_t==null) _t=ASTNULL;
					switch ( _t.getType()) {
					case NStringSeq:
					{
						strOptExprPair(_t,fac);
						_t = _retTree;
						astFactory.addASTChild(currentAST, returnAST);
						{
						_loop150:
						do {
							if (_t==null) _t=ASTNULL;
							if ((_t.getType()==COMMA)) {
								TNode tmp131_AST = null;
								TNode tmp131_AST_in = null;
								tmp131_AST = (TNode)astFactory.create((TNode)_t);
								tmp131_AST_in = (TNode)_t;
								astFactory.addASTChild(currentAST, tmp131_AST);
								match(_t,COMMA);
								_t = _t.getNextSibling();
								strOptExprPair(_t,fac);
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
				TNode tmp132_AST = null;
				TNode tmp132_AST_in = null;
				tmp132_AST = (TNode)astFactory.create((TNode)_t);
				tmp132_AST_in = (TNode)_t;
				astFactory.addASTChild(currentAST, tmp132_AST);
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
						TNode tmp133_AST = null;
						TNode tmp133_AST_in = null;
						tmp133_AST = (TNode)astFactory.create((TNode)_t);
						tmp133_AST_in = (TNode)_t;
						astFactory.addASTChild(currentAST, tmp133_AST);
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
			TNode tmp134_AST = null;
			TNode tmp134_AST_in = null;
			tmp134_AST = (TNode)astFactory.create((TNode)_t);
			tmp134_AST_in = (TNode)_t;
			astFactory.addASTChild(currentAST, tmp134_AST);
			match(_t,RPAREN);
			_t = _t.getNextSibling();
			currentAST = __currentAST141;
			_t = __t141;
			_t = _t.getNextSibling();
			gnuAsmExpr_AST = (TNode)currentAST.root;
		}
		catch (RecognitionException ex) {
			if (inputState.guessing==0) {
				reportError(ex);
				if (_t!=null) {_t = _t.getNextSibling();}
			} else {
			  throw ex;
			}
		}
		returnAST = gnuAsmExpr_AST;
		_retTree = _t;
	}
	
	public final Expr  compoundStatementExpr(AST _t,
		ExprFactory fac
	) throws RecognitionException {
		Expr e;
		
		TNode compoundStatementExpr_AST_in = (_t == ASTNULL) ? null : (TNode)_t;
		returnAST = null;
		ASTPair currentAST = new ASTPair();
		TNode compoundStatementExpr_AST = null;
		e = null;
		
		try {      // for error handling
			AST __t139 = _t;
			TNode tmp135_AST = null;
			TNode tmp135_AST_in = null;
			tmp135_AST = (TNode)astFactory.create((TNode)_t);
			tmp135_AST_in = (TNode)_t;
			astFactory.addASTChild(currentAST, tmp135_AST);
			ASTPair __currentAST139 = currentAST.copy();
			currentAST.root = currentAST.child;
			currentAST.child = null;
			match(_t,LPAREN);
			_t = _t.getFirstChild();
			e=compoundStatement(_t,fac);
			_t = _retTree;
			astFactory.addASTChild(currentAST, returnAST);
			TNode tmp136_AST = null;
			TNode tmp136_AST_in = null;
			tmp136_AST = (TNode)astFactory.create((TNode)_t);
			tmp136_AST_in = (TNode)_t;
			astFactory.addASTChild(currentAST, tmp136_AST);
			match(_t,RPAREN);
			_t = _t.getNextSibling();
			currentAST = __currentAST139;
			_t = __t139;
			_t = _t.getNextSibling();
			compoundStatementExpr_AST = (TNode)currentAST.root;
		}
		catch (RecognitionException ex) {
			if (inputState.guessing==0) {
				reportError(ex);
				if (_t!=null) {_t = _t.getNextSibling();}
			} else {
			  throw ex;
			}
		}
		returnAST = compoundStatementExpr_AST;
		_retTree = _t;
		return e;
	}
	
	protected final void stringConst(AST _t) throws RecognitionException {
		
		TNode stringConst_AST_in = (_t == ASTNULL) ? null : (TNode)_t;
		returnAST = null;
		ASTPair currentAST = new ASTPair();
		TNode stringConst_AST = null;
		
		try {      // for error handling
			AST __t229 = _t;
			TNode tmp137_AST = null;
			TNode tmp137_AST_in = null;
			tmp137_AST = (TNode)astFactory.create((TNode)_t);
			tmp137_AST_in = (TNode)_t;
			astFactory.addASTChild(currentAST, tmp137_AST);
			ASTPair __currentAST229 = currentAST.copy();
			currentAST.root = currentAST.child;
			currentAST.child = null;
			match(_t,NStringSeq);
			_t = _t.getFirstChild();
			{
			int _cnt231=0;
			_loop231:
			do {
				if (_t==null) _t=ASTNULL;
				if ((_t.getType()==StringLiteral)) {
					TNode tmp138_AST = null;
					TNode tmp138_AST_in = null;
					tmp138_AST = (TNode)astFactory.create((TNode)_t);
					tmp138_AST_in = (TNode)_t;
					astFactory.addASTChild(currentAST, tmp138_AST);
					match(_t,StringLiteral);
					_t = _t.getNextSibling();
				}
				else {
					if ( _cnt231>=1 ) { break _loop231; } else {throw new NoViableAltException(_t);}
				}
				
				_cnt231++;
			} while (true);
			}
			currentAST = __currentAST229;
			_t = __t229;
			_t = _t.getNextSibling();
			stringConst_AST = (TNode)currentAST.root;
		}
		catch (RecognitionException ex) {
			if (inputState.guessing==0) {
				reportError(ex);
				if (_t!=null) {_t = _t.getNextSibling();}
			} else {
			  throw ex;
			}
		}
		returnAST = stringConst_AST;
		_retTree = _t;
	}
	
	public final void strOptExprPair(AST _t,
		ExprFactory fac
	) throws RecognitionException {
		
		TNode strOptExprPair_AST_in = (_t == ASTNULL) ? null : (TNode)_t;
		returnAST = null;
		ASTPair currentAST = new ASTPair();
		TNode strOptExprPair_AST = null;
		Expr discard;
		
		try {      // for error handling
			stringConst(_t);
			_t = _retTree;
			astFactory.addASTChild(currentAST, returnAST);
			{
			if (_t==null) _t=ASTNULL;
			switch ( _t.getType()) {
			case LPAREN:
			{
				TNode tmp139_AST = null;
				TNode tmp139_AST_in = null;
				tmp139_AST = (TNode)astFactory.create((TNode)_t);
				tmp139_AST_in = (TNode)_t;
				astFactory.addASTChild(currentAST, tmp139_AST);
				match(_t,LPAREN);
				_t = _t.getNextSibling();
				discard=expr(_t,fac);
				_t = _retTree;
				astFactory.addASTChild(currentAST, returnAST);
				TNode tmp140_AST = null;
				TNode tmp140_AST_in = null;
				tmp140_AST = (TNode)astFactory.create((TNode)_t);
				tmp140_AST_in = (TNode)_t;
				astFactory.addASTChild(currentAST, tmp140_AST);
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
			strOptExprPair_AST = (TNode)currentAST.root;
		}
		catch (RecognitionException ex) {
			if (inputState.guessing==0) {
				reportError(ex);
				if (_t!=null) {_t = _t.getNextSibling();}
			} else {
			  throw ex;
			}
		}
		returnAST = strOptExprPair_AST;
		_retTree = _t;
	}
	
	public final void typeName(AST _t,
		ExprFactory fac
	) throws RecognitionException {
		
		TNode typeName_AST_in = (_t == ASTNULL) ? null : (TNode)_t;
		returnAST = null;
		ASTPair currentAST = new ASTPair();
		TNode typeName_AST = null;
		Expr discard;
		
		try {      // for error handling
			discard=specifierQualifierList(_t,fac);
			_t = _retTree;
			astFactory.addASTChild(currentAST, returnAST);
			{
			if (_t==null) _t=ASTNULL;
			switch ( _t.getType()) {
			case NNonemptyAbstractDeclarator:
			{
				nonemptyAbstractDeclarator(_t,fac);
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
			typeName_AST = (TNode)currentAST.root;
		}
		catch (RecognitionException ex) {
			if (inputState.guessing==0) {
				reportError(ex);
				if (_t!=null) {_t = _t.getNextSibling();}
			} else {
			  throw ex;
			}
		}
		returnAST = typeName_AST;
		_retTree = _t;
	}
	
	public final Operator  unaryOperator(AST _t) throws RecognitionException {
		Operator op;
		
		TNode unaryOperator_AST_in = (_t == ASTNULL) ? null : (TNode)_t;
		returnAST = null;
		ASTPair currentAST = new ASTPair();
		TNode unaryOperator_AST = null;
		op = CILUnknownOp.UNKNOWN;
		
		try {      // for error handling
			{
			if (_t==null) _t=ASTNULL;
			switch ( _t.getType()) {
			case BAND:
			{
				TNode tmp141_AST = null;
				TNode tmp141_AST_in = null;
				tmp141_AST = (TNode)astFactory.create((TNode)_t);
				tmp141_AST_in = (TNode)_t;
				astFactory.addASTChild(currentAST, tmp141_AST);
				match(_t,BAND);
				_t = _t.getNextSibling();
				if ( inputState.guessing==0 ) {
					op = CILIndirectionOp.ADDR_OF;
				}
				break;
			}
			case STAR:
			{
				TNode tmp142_AST = null;
				TNode tmp142_AST_in = null;
				tmp142_AST = (TNode)astFactory.create((TNode)_t);
				tmp142_AST_in = (TNode)_t;
				astFactory.addASTChild(currentAST, tmp142_AST);
				match(_t,STAR);
				_t = _t.getNextSibling();
				if ( inputState.guessing==0 ) {
					op = CILIndirectionOp.DEREF;
				}
				break;
			}
			case PLUS:
			{
				TNode tmp143_AST = null;
				TNode tmp143_AST_in = null;
				tmp143_AST = (TNode)astFactory.create((TNode)_t);
				tmp143_AST_in = (TNode)_t;
				astFactory.addASTChild(currentAST, tmp143_AST);
				match(_t,PLUS);
				_t = _t.getNextSibling();
				break;
			}
			case MINUS:
			{
				TNode tmp144_AST = null;
				TNode tmp144_AST_in = null;
				tmp144_AST = (TNode)astFactory.create((TNode)_t);
				tmp144_AST_in = (TNode)_t;
				astFactory.addASTChild(currentAST, tmp144_AST);
				match(_t,MINUS);
				_t = _t.getNextSibling();
				if ( inputState.guessing==0 ) {
					op = NumericOp.UN_MINUS;
				}
				break;
			}
			case BNOT:
			{
				TNode tmp145_AST = null;
				TNode tmp145_AST_in = null;
				tmp145_AST = (TNode)astFactory.create((TNode)_t);
				tmp145_AST_in = (TNode)_t;
				astFactory.addASTChild(currentAST, tmp145_AST);
				match(_t,BNOT);
				_t = _t.getNextSibling();
				break;
			}
			case LNOT:
			{
				TNode tmp146_AST = null;
				TNode tmp146_AST_in = null;
				tmp146_AST = (TNode)astFactory.create((TNode)_t);
				tmp146_AST_in = (TNode)_t;
				astFactory.addASTChild(currentAST, tmp146_AST);
				match(_t,LNOT);
				_t = _t.getNextSibling();
				if ( inputState.guessing==0 ) {
					op = BoolOp.NOT;
				}
				break;
			}
			case LAND:
			{
				TNode tmp147_AST = null;
				TNode tmp147_AST_in = null;
				tmp147_AST = (TNode)astFactory.create((TNode)_t);
				tmp147_AST_in = (TNode)_t;
				astFactory.addASTChild(currentAST, tmp147_AST);
				match(_t,LAND);
				_t = _t.getNextSibling();
				break;
			}
			case LITERAL___real:
			{
				TNode tmp148_AST = null;
				TNode tmp148_AST_in = null;
				tmp148_AST = (TNode)astFactory.create((TNode)_t);
				tmp148_AST_in = (TNode)_t;
				astFactory.addASTChild(currentAST, tmp148_AST);
				match(_t,LITERAL___real);
				_t = _t.getNextSibling();
				break;
			}
			case LITERAL___imag:
			{
				TNode tmp149_AST = null;
				TNode tmp149_AST_in = null;
				tmp149_AST = (TNode)astFactory.create((TNode)_t);
				tmp149_AST_in = (TNode)_t;
				astFactory.addASTChild(currentAST, tmp149_AST);
				match(_t,LITERAL___imag);
				_t = _t.getNextSibling();
				break;
			}
			default:
			{
				throw new NoViableAltException(_t);
			}
			}
			}
			unaryOperator_AST = (TNode)currentAST.root;
		}
		catch (RecognitionException ex) {
			if (inputState.guessing==0) {
				reportError(ex);
				if (_t!=null) {_t = _t.getNextSibling();}
			} else {
			  throw ex;
			}
		}
		returnAST = unaryOperator_AST;
		_retTree = _t;
		return op;
	}
	
	public final Expr  argExprList(AST _t,
		ExprFactory fac
	) throws RecognitionException {
		Expr e;
		
		TNode argExprList_AST_in = (_t == ASTNULL) ? null : (TNode)_t;
		returnAST = null;
		ASTPair currentAST = new ASTPair();
		TNode argExprList_AST = null;
		
		e = null;
		List argList = new LinkedList ();
		Expr arg;
		
		
		try {      // for error handling
			{
			int _cnt226=0;
			_loop226:
			do {
				if (_t==null) _t=ASTNULL;
				if ((_tokenSet_3.member(_t.getType()))) {
					arg=expr(_t,fac);
					_t = _retTree;
					astFactory.addASTChild(currentAST, returnAST);
					if ( inputState.guessing==0 ) {
						argList.add (arg);
					}
				}
				else {
					if ( _cnt226>=1 ) { break _loop226; } else {throw new NoViableAltException(_t);}
				}
				
				_cnt226++;
			} while (true);
			}
			if ( inputState.guessing==0 ) {
				e = fac.op (CILListOp.LIST).naryApply (argList);
			}
			argExprList_AST = (TNode)currentAST.root;
		}
		catch (RecognitionException ex) {
			if (inputState.guessing==0) {
				reportError(ex);
				if (_t!=null) {_t = _t.getNextSibling();}
			} else {
			  throw ex;
			}
		}
		returnAST = argExprList_AST;
		_retTree = _t;
		return e;
	}
	
	protected final void charConst(AST _t) throws RecognitionException {
		
		TNode charConst_AST_in = (_t == ASTNULL) ? null : (TNode)_t;
		returnAST = null;
		ASTPair currentAST = new ASTPair();
		TNode charConst_AST = null;
		
		try {      // for error handling
			TNode tmp150_AST = null;
			TNode tmp150_AST_in = null;
			tmp150_AST = (TNode)astFactory.create((TNode)_t);
			tmp150_AST_in = (TNode)_t;
			astFactory.addASTChild(currentAST, tmp150_AST);
			match(_t,CharLiteral);
			_t = _t.getNextSibling();
			charConst_AST = (TNode)currentAST.root;
		}
		catch (RecognitionException ex) {
			if (inputState.guessing==0) {
				reportError(ex);
				if (_t!=null) {_t = _t.getNextSibling();}
			} else {
			  throw ex;
			}
		}
		returnAST = charConst_AST;
		_retTree = _t;
	}
	
	protected final void intConst(AST _t) throws RecognitionException {
		
		TNode intConst_AST_in = (_t == ASTNULL) ? null : (TNode)_t;
		returnAST = null;
		ASTPair currentAST = new ASTPair();
		TNode intConst_AST = null;
		
		try {      // for error handling
			if (_t==null) _t=ASTNULL;
			switch ( _t.getType()) {
			case IntOctalConst:
			{
				TNode tmp151_AST = null;
				TNode tmp151_AST_in = null;
				tmp151_AST = (TNode)astFactory.create((TNode)_t);
				tmp151_AST_in = (TNode)_t;
				astFactory.addASTChild(currentAST, tmp151_AST);
				match(_t,IntOctalConst);
				_t = _t.getNextSibling();
				intConst_AST = (TNode)currentAST.root;
				break;
			}
			case LongOctalConst:
			{
				TNode tmp152_AST = null;
				TNode tmp152_AST_in = null;
				tmp152_AST = (TNode)astFactory.create((TNode)_t);
				tmp152_AST_in = (TNode)_t;
				astFactory.addASTChild(currentAST, tmp152_AST);
				match(_t,LongOctalConst);
				_t = _t.getNextSibling();
				intConst_AST = (TNode)currentAST.root;
				break;
			}
			case UnsignedOctalConst:
			{
				TNode tmp153_AST = null;
				TNode tmp153_AST_in = null;
				tmp153_AST = (TNode)astFactory.create((TNode)_t);
				tmp153_AST_in = (TNode)_t;
				astFactory.addASTChild(currentAST, tmp153_AST);
				match(_t,UnsignedOctalConst);
				_t = _t.getNextSibling();
				intConst_AST = (TNode)currentAST.root;
				break;
			}
			case IntIntConst:
			{
				TNode tmp154_AST = null;
				TNode tmp154_AST_in = null;
				tmp154_AST = (TNode)astFactory.create((TNode)_t);
				tmp154_AST_in = (TNode)_t;
				astFactory.addASTChild(currentAST, tmp154_AST);
				match(_t,IntIntConst);
				_t = _t.getNextSibling();
				intConst_AST = (TNode)currentAST.root;
				break;
			}
			case LongIntConst:
			{
				TNode tmp155_AST = null;
				TNode tmp155_AST_in = null;
				tmp155_AST = (TNode)astFactory.create((TNode)_t);
				tmp155_AST_in = (TNode)_t;
				astFactory.addASTChild(currentAST, tmp155_AST);
				match(_t,LongIntConst);
				_t = _t.getNextSibling();
				intConst_AST = (TNode)currentAST.root;
				break;
			}
			case UnsignedIntConst:
			{
				TNode tmp156_AST = null;
				TNode tmp156_AST_in = null;
				tmp156_AST = (TNode)astFactory.create((TNode)_t);
				tmp156_AST_in = (TNode)_t;
				astFactory.addASTChild(currentAST, tmp156_AST);
				match(_t,UnsignedIntConst);
				_t = _t.getNextSibling();
				intConst_AST = (TNode)currentAST.root;
				break;
			}
			case IntHexConst:
			{
				TNode tmp157_AST = null;
				TNode tmp157_AST_in = null;
				tmp157_AST = (TNode)astFactory.create((TNode)_t);
				tmp157_AST_in = (TNode)_t;
				astFactory.addASTChild(currentAST, tmp157_AST);
				match(_t,IntHexConst);
				_t = _t.getNextSibling();
				intConst_AST = (TNode)currentAST.root;
				break;
			}
			case LongHexConst:
			{
				TNode tmp158_AST = null;
				TNode tmp158_AST_in = null;
				tmp158_AST = (TNode)astFactory.create((TNode)_t);
				tmp158_AST_in = (TNode)_t;
				astFactory.addASTChild(currentAST, tmp158_AST);
				match(_t,LongHexConst);
				_t = _t.getNextSibling();
				intConst_AST = (TNode)currentAST.root;
				break;
			}
			case UnsignedHexConst:
			{
				TNode tmp159_AST = null;
				TNode tmp159_AST_in = null;
				tmp159_AST = (TNode)astFactory.create((TNode)_t);
				tmp159_AST_in = (TNode)_t;
				astFactory.addASTChild(currentAST, tmp159_AST);
				match(_t,UnsignedHexConst);
				_t = _t.getNextSibling();
				intConst_AST = (TNode)currentAST.root;
				break;
			}
			default:
			{
				throw new NoViableAltException(_t);
			}
			}
		}
		catch (RecognitionException ex) {
			if (inputState.guessing==0) {
				reportError(ex);
				if (_t!=null) {_t = _t.getNextSibling();}
			} else {
			  throw ex;
			}
		}
		returnAST = intConst_AST;
		_retTree = _t;
	}
	
	protected final void floatConst(AST _t) throws RecognitionException {
		
		TNode floatConst_AST_in = (_t == ASTNULL) ? null : (TNode)_t;
		returnAST = null;
		ASTPair currentAST = new ASTPair();
		TNode floatConst_AST = null;
		
		try {      // for error handling
			if (_t==null) _t=ASTNULL;
			switch ( _t.getType()) {
			case FloatDoubleConst:
			{
				TNode tmp160_AST = null;
				TNode tmp160_AST_in = null;
				tmp160_AST = (TNode)astFactory.create((TNode)_t);
				tmp160_AST_in = (TNode)_t;
				astFactory.addASTChild(currentAST, tmp160_AST);
				match(_t,FloatDoubleConst);
				_t = _t.getNextSibling();
				floatConst_AST = (TNode)currentAST.root;
				break;
			}
			case DoubleDoubleConst:
			{
				TNode tmp161_AST = null;
				TNode tmp161_AST_in = null;
				tmp161_AST = (TNode)astFactory.create((TNode)_t);
				tmp161_AST_in = (TNode)_t;
				astFactory.addASTChild(currentAST, tmp161_AST);
				match(_t,DoubleDoubleConst);
				_t = _t.getNextSibling();
				floatConst_AST = (TNode)currentAST.root;
				break;
			}
			case LongDoubleConst:
			{
				TNode tmp162_AST = null;
				TNode tmp162_AST_in = null;
				tmp162_AST = (TNode)astFactory.create((TNode)_t);
				tmp162_AST_in = (TNode)_t;
				astFactory.addASTChild(currentAST, tmp162_AST);
				match(_t,LongDoubleConst);
				_t = _t.getNextSibling();
				floatConst_AST = (TNode)currentAST.root;
				break;
			}
			default:
			{
				throw new NoViableAltException(_t);
			}
			}
		}
		catch (RecognitionException ex) {
			if (inputState.guessing==0) {
				reportError(ex);
				if (_t!=null) {_t = _t.getNextSibling();}
			} else {
			  throw ex;
			}
		}
		returnAST = floatConst_AST;
		_retTree = _t;
	}
	
	
	public static final String[] _tokenNames = {
		"<0>",
		"EOF",
		"<2>",
		"NULL_TREE_LOOKAHEAD",
		"\"typedef\"",
		"\"asm\"",
		"\"volatile\"",
		"LCURLY",
		"RCURLY",
		"SEMI",
		"\"struct\"",
		"\"union\"",
		"\"enum\"",
		"\"auto\"",
		"\"register\"",
		"\"extern\"",
		"\"static\"",
		"\"const\"",
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
		"COMMA",
		"COLON",
		"ASSIGN",
		"STAR",
		"LPAREN",
		"RPAREN",
		"LBRACKET",
		"RBRACKET",
		"VARARGS",
		"\"while\"",
		"\"do\"",
		"\"for\"",
		"\"goto\"",
		"\"continue\"",
		"\"break\"",
		"\"return\"",
		"\"case\"",
		"\"default\"",
		"\"if\"",
		"\"else\"",
		"\"switch\"",
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
		"QUESTION",
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
		"INC",
		"DEC",
		"\"sizeof\"",
		"BNOT",
		"LNOT",
		"PTR",
		"DOT",
		"CharLiteral",
		"StringLiteral",
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
		"NExpressionGroup",
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
		"NInitializerElementLabel",
		"NLcurlyInitializer",
		"NAsmAttribute",
		"NGnuAsmExpr",
		"NTypeMissing",
		"Vocabulary",
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
		"\"__label__\"",
		"\"inline\"",
		"\"__restrict\"",
		"\"typeof\"",
		"\"__complex\"",
		"\"__builtin_va_list\"",
		"\"__attribute\"",
		"\"__pure__\"",
		"\"__noreturn__\"",
		"\"__nd_goto\"",
		"\"__alignof\"",
		"\"__real\"",
		"\"__imag\""
	};
	
	private static final long[] mk_tokenSet_0() {
		long[] data = { 544L, 281749854617600L, 0L, 0L};
		return data;
	}
	public static final BitSet _tokenSet_0 = new BitSet(mk_tokenSet_0());
	private static final long[] mk_tokenSet_1() {
		long[] data = { 134093888L, 17179869184L, 262144L, 0L, 0L, 0L};
		return data;
	}
	public static final BitSet _tokenSet_1 = new BitSet(mk_tokenSet_1());
	private static final long[] mk_tokenSet_2() {
		long[] data = { 84799834292736L, 9605333580251136L, 4194304L, 0L, 0L, 0L};
		return data;
	}
	public static final BitSet _tokenSet_2 = new BitSet(mk_tokenSet_2());
	private static final long[] mk_tokenSet_3() {
		long[] data = { -1152921496956436480L, 1535747813899567103L, 4096L, 0L, 0L, 0L};
		return data;
	}
	public static final BitSet _tokenSet_3 = new BitSet(mk_tokenSet_3());
	}
	
