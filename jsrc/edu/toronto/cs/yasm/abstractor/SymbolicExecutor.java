// $ANTLR 2.7.4: "SymbolicExecutor.g" -> "SymbolicExecutor.java"$

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
    import edu.toronto.cs.expr.*;
    import java.util.*;


public class SymbolicExecutor extends antlr.TreeParser       implements SymbolicExecutorTokenTypes
 {

  ExprFactory fac;

  public void setExprFactory (ExprFactory _fac)
  {
    fac = _fac;
  }
public SymbolicExecutor() {
	tokenNames = _tokenNames;
}

	public final MemoryModel  block(AST _t,
		MemoryModel mmIn
	) throws RecognitionException {
		MemoryModel mmOut;
		
		Block block_AST_in = (_t == ASTNULL) ? null : (Block)_t;
		
		AST __t2 = _t;
		Block tmp1_AST_in = (Block)_t;
		match(_t,NBlock);
		_t = _t.getFirstChild();
		mmOut=stmtList(_t,mmIn);
		_t = _retTree;
		_t = __t2;
		_t = _t.getNextSibling();
		_retTree = _t;
		return mmOut;
	}
	
	public final MemoryModel  stmtList(AST _t,
		MemoryModel mmIn
	) throws RecognitionException {
		MemoryModel mmOut;
		
		Block stmtList_AST_in = (_t == ASTNULL) ? null : (Block)_t;
		
		{
		int _cnt5=0;
		_loop5:
		do {
			if (_t==null) _t=ASTNULL;
			if ((_t.getType()==NLabel||_t.getType()==NAssignStmt||_t.getType()==NImplicitAssignStmt)) {
				mmIn=stmt(_t,mmIn);
				_t = _retTree;
			}
			else {
				if ( _cnt5>=1 ) { break _loop5; } else {throw new NoViableAltException(_t);}
			}
			
			_cnt5++;
		} while (true);
		}
		mmOut = mmIn;
		_retTree = _t;
		return mmOut;
	}
	
	public final MemoryModel  stmt(AST _t,
		MemoryModel mmIn
	) throws RecognitionException {
		MemoryModel mmOut;
		
		Block stmt_AST_in = (_t == ASTNULL) ? null : (Block)_t;
		
		if (_t==null) _t=ASTNULL;
		switch ( _t.getType()) {
		case NAssignStmt:
		case NImplicitAssignStmt:
		{
			mmOut=asmt(_t,mmIn);
			_t = _retTree;
			break;
		}
		case NLabel:
		{
			mmOut=labelledStmt(_t,mmIn);
			_t = _retTree;
			break;
		}
		default:
		{
			throw new NoViableAltException(_t);
		}
		}
		_retTree = _t;
		return mmOut;
	}
	
	public final MemoryModel  asmt(AST _t,
		MemoryModel mmIn
	) throws RecognitionException {
		MemoryModel mmOut;
		
		Block asmt_AST_in = (_t == ASTNULL) ? null : (Block)_t;
		
		Expr l, e;
		Operator op;
		
		
		if (_t==null) _t=ASTNULL;
		switch ( _t.getType()) {
		case NAssignStmt:
		{
			AST __t18 = _t;
			Block tmp2_AST_in = (Block)_t;
			match(_t,NAssignStmt);
			_t = _t.getFirstChild();
			l=lval(_t,mmIn);
			_t = _retTree;
			e=pureExpr(_t,mmIn);
			_t = _retTree;
			_t = __t18;
			_t = _t.getNextSibling();
			
			mmIn.assign (l, e);
			mmOut = mmIn;
			
			break;
		}
		case NImplicitAssignStmt:
		{
			AST __t19 = _t;
			Block tmp3_AST_in = (Block)_t;
			match(_t,NImplicitAssignStmt);
			_t = _t.getFirstChild();
			l=lval(_t,mmIn);
			_t = _retTree;
			op=implicitAssignOp(_t);
			_t = _retTree;
			_t = __t19;
			_t = _t.getNextSibling();
			
			mmIn.assign (l, fac.op (op).binApply (l, fac.intExpr (1)));
			mmOut = mmIn;
			
			break;
		}
		default:
		{
			throw new NoViableAltException(_t);
		}
		}
		_retTree = _t;
		return mmOut;
	}
	
	public final MemoryModel  labelledStmt(AST _t,
		MemoryModel mmIn
	) throws RecognitionException {
		MemoryModel mmOut;
		
		Block labelledStmt_AST_in = (_t == ASTNULL) ? null : (Block)_t;
		
		label(_t);
		_t = _retTree;
		AST __t11 = _t;
		Block tmp4_AST_in = (Block)_t;
		match(_t,NBlock);
		_t = _t.getFirstChild();
		mmOut=stmt(_t,mmIn);
		_t = _retTree;
		_t = __t11;
		_t = _t.getNextSibling();
		_retTree = _t;
		return mmOut;
	}
	
	public final MemoryModel  returnStmt(AST _t,
		String returnVarName, MemoryModel mmIn
	) throws RecognitionException {
		MemoryModel mmOut;
		
		Block returnStmt_AST_in = (_t == ASTNULL) ? null : (Block)_t;
		Block r = null;
		Expr e = null;
		
		AST __t8 = _t;
		r = _t==ASTNULL ? null :(Block)_t;
		match(_t,NBlock);
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
		case EQUAL:
		case NOT_EQUAL:
		case LT:
		case LTE:
		case GT:
		case GTE:
		case PLUS:
		case MINUS:
		case DIV:
		case MOD:
		case IntOctalConst:
		case LongOctalConst:
		case UnsignedOctalConst:
		case IntIntConst:
		case LongIntConst:
		case UnsignedIntConst:
		case IntHexConst:
		case LongHexConst:
		case UnsignedHexConst:
		case NUnaryExpr:
		case NPostfixExpr:
		case NPureExpressionGroup:
		{
			e=pureExpr(_t,mmIn);
			_t = _retTree;
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
		_t = __t8;
		_t = _t.getNextSibling();
		
		assert r.getBlockType () == BlockType.RETURN : "Invalid Block type";
		if (e != null)
		mmIn.assign (fac.var (returnVarName), e);
		mmOut = mmIn;
		
		_retTree = _t;
		return mmOut;
	}
	
	public final Expr  pureExpr(AST _t,
		MemoryModel mm
	) throws RecognitionException {
		Expr e;
		
		Block pureExpr_AST_in = (_t == ASTNULL) ? null : (Block)_t;
		
		if (_t==null) _t=ASTNULL;
		switch ( _t.getType()) {
		case LOR:
		{
			e=pureLogicalOrExpr(_t,mm);
			_t = _retTree;
			break;
		}
		case LAND:
		{
			e=pureLogicalAndExpr(_t,mm);
			_t = _retTree;
			break;
		}
		case EQUAL:
		case NOT_EQUAL:
		{
			e=pureEqualityExpr(_t,mm);
			_t = _retTree;
			break;
		}
		case LT:
		case LTE:
		case GT:
		case GTE:
		{
			e=pureRelationalExpr(_t,mm);
			_t = _retTree;
			break;
		}
		case PLUS:
		case MINUS:
		{
			e=pureAdditiveExpr(_t,mm);
			_t = _retTree;
			break;
		}
		case STAR:
		case DIV:
		case MOD:
		{
			e=pureMultExpr(_t,mm);
			_t = _retTree;
			break;
		}
		case NUnaryExpr:
		case NPostfixExpr:
		{
			e=pureUnaryExpr(_t,mm);
			_t = _retTree;
			break;
		}
		case ID:
		case PTR:
		case DOT:
		case IntOctalConst:
		case LongOctalConst:
		case UnsignedOctalConst:
		case IntIntConst:
		case LongIntConst:
		case UnsignedIntConst:
		case IntHexConst:
		case LongHexConst:
		case UnsignedHexConst:
		case NPureExpressionGroup:
		{
			e=purePrimaryExpr(_t,mm);
			_t = _retTree;
			break;
		}
		default:
		{
			throw new NoViableAltException(_t);
		}
		}
		_retTree = _t;
		return e;
	}
	
	public final void label(AST _t) throws RecognitionException {
		
		Block label_AST_in = (_t == ASTNULL) ? null : (Block)_t;
		
		AST __t13 = _t;
		Block tmp5_AST_in = (Block)_t;
		match(_t,NLabel);
		_t = _t.getFirstChild();
		Block tmp6_AST_in = (Block)_t;
		match(_t,ID);
		_t = _t.getNextSibling();
		_t = __t13;
		_t = _t.getNextSibling();
		_retTree = _t;
	}
	
	public final MemoryModel  functionCallArgs(AST _t,
		List declarations, MemoryModel mmIn
	) throws RecognitionException {
		MemoryModel mmOut;
		
		Block functionCallArgs_AST_in = (_t == ASTNULL) ? null : (Block)_t;
		
		Expr e;
		Iterator it = declarations.iterator ();
		
		
		{
		_loop16:
		do {
			if (_t==null) _t=ASTNULL;
			if ((_tokenSet_0.member(_t.getType()))) {
				e=pureExpr(_t,mmIn);
				_t = _retTree;
				
				Expr curArg = (Expr) it.next ();
				mmIn.assign (curArg, e);
				
			}
			else {
				break _loop16;
			}
			
		} while (true);
		}
		mmOut = mmIn;
		_retTree = _t;
		return mmOut;
	}
	
	public final Expr  lval(AST _t,
		MemoryModel mm
	) throws RecognitionException {
		Expr e;
		
		Block lval_AST_in = (_t == ASTNULL) ? null : (Block)_t;
		Block id = null;
		Block id1 = null;
		Block id2 = null;
		Expr l;
		
		if (_t==null) _t=ASTNULL;
		switch ( _t.getType()) {
		case ID:
		{
			id = (Block)_t;
			match(_t,ID);
			_t = _t.getNextSibling();
			e = fac.var (id.getText ());
			break;
		}
		case PTR:
		{
			AST __t55 = _t;
			Block tmp7_AST_in = (Block)_t;
			match(_t,PTR);
			_t = _t.getFirstChild();
			id1 = (Block)_t;
			match(_t,ID);
			_t = _t.getNextSibling();
			l=lval(_t,mm);
			_t = _retTree;
			_t = __t55;
			_t = _t.getNextSibling();
			
			// XXX ignore struct fields for now
			//e = fac.var(id1.getText () + "->" + ((VariableOp) l.op ()).name ()); 
			e = fac.var (id1.getText ());
			
			break;
		}
		case DOT:
		{
			AST __t56 = _t;
			Block tmp8_AST_in = (Block)_t;
			match(_t,DOT);
			_t = _t.getFirstChild();
			id2 = (Block)_t;
			match(_t,ID);
			_t = _t.getNextSibling();
			l=lval(_t,mm);
			_t = _retTree;
			_t = __t56;
			_t = _t.getNextSibling();
			
			// XXX ignore struct fields for now
			// e = fac.var(id2.getText () + "." + ((VariableOp) l.op ()).name ());
			e = fac.var (id1.getText ());
			
			break;
		}
		default:
		{
			throw new NoViableAltException(_t);
		}
		}
		_retTree = _t;
		return e;
	}
	
	public final Operator  implicitAssignOp(AST _t) throws RecognitionException {
		Operator op;
		
		Block implicitAssignOp_AST_in = (_t == ASTNULL) ? null : (Block)_t;
		
		if (_t==null) _t=ASTNULL;
		switch ( _t.getType()) {
		case INC:
		{
			Block tmp9_AST_in = (Block)_t;
			match(_t,INC);
			_t = _t.getNextSibling();
			op = NumericOp.PLUS;
			break;
		}
		case DEC:
		{
			Block tmp10_AST_in = (Block)_t;
			match(_t,DEC);
			_t = _t.getNextSibling();
			op = NumericOp.MINUS;
			break;
		}
		default:
		{
			throw new NoViableAltException(_t);
		}
		}
		_retTree = _t;
		return op;
	}
	
	public final Expr  expr(AST _t,
		MemoryModel mm
	) throws RecognitionException {
		Expr e;
		
		Block expr_AST_in = (_t == ASTNULL) ? null : (Block)_t;
		
		e=pureExpr(_t,mm);
		_t = _retTree;
		_retTree = _t;
		return e;
	}
	
	public final Expr  pureLogicalOrExpr(AST _t,
		MemoryModel mm
	) throws RecognitionException {
		Expr e;
		
		Block pureLogicalOrExpr_AST_in = (_t == ASTNULL) ? null : (Block)_t;
		Expr e1, e2;
		
		AST __t24 = _t;
		Block tmp11_AST_in = (Block)_t;
		match(_t,LOR);
		_t = _t.getFirstChild();
		e1=pureExpr(_t,mm);
		_t = _retTree;
		e2=pureExpr(_t,mm);
		_t = _retTree;
		_t = __t24;
		_t = _t.getNextSibling();
		e = fac.op (BoolOp.OR).binApply (e1, e2);
		_retTree = _t;
		return e;
	}
	
	public final Expr  pureLogicalAndExpr(AST _t,
		MemoryModel mm
	) throws RecognitionException {
		Expr e;
		
		Block pureLogicalAndExpr_AST_in = (_t == ASTNULL) ? null : (Block)_t;
		Expr e1, e2;
		
		AST __t26 = _t;
		Block tmp12_AST_in = (Block)_t;
		match(_t,LAND);
		_t = _t.getFirstChild();
		e1=pureExpr(_t,mm);
		_t = _retTree;
		e2=pureExpr(_t,mm);
		_t = _retTree;
		_t = __t26;
		_t = _t.getNextSibling();
		e = fac.op (BoolOp.AND).binApply (e1, e2);
		_retTree = _t;
		return e;
	}
	
	public final Expr  pureEqualityExpr(AST _t,
		MemoryModel mm
	) throws RecognitionException {
		Expr e;
		
		Block pureEqualityExpr_AST_in = (_t == ASTNULL) ? null : (Block)_t;
		Expr e1, e2;
		
		if (_t==null) _t=ASTNULL;
		switch ( _t.getType()) {
		case EQUAL:
		{
			AST __t28 = _t;
			Block tmp13_AST_in = (Block)_t;
			match(_t,EQUAL);
			_t = _t.getFirstChild();
			e1=pureExpr(_t,mm);
			_t = _retTree;
			e2=pureExpr(_t,mm);
			_t = _retTree;
			_t = __t28;
			_t = _t.getNextSibling();
			e = fac.op (ComparisonOp.EQ).binApply (e1, e2);
			break;
		}
		case NOT_EQUAL:
		{
			AST __t29 = _t;
			Block tmp14_AST_in = (Block)_t;
			match(_t,NOT_EQUAL);
			_t = _t.getFirstChild();
			e1=pureExpr(_t,mm);
			_t = _retTree;
			e2=pureExpr(_t,mm);
			_t = _retTree;
			_t = __t29;
			_t = _t.getNextSibling();
			
			e = fac.op (BoolOp.NOT).
			unaryApply (fac.op (ComparisonOp.EQ).binApply (e1, e2));
			
			break;
		}
		default:
		{
			throw new NoViableAltException(_t);
		}
		}
		_retTree = _t;
		return e;
	}
	
	public final Expr  pureRelationalExpr(AST _t,
		MemoryModel mm
	) throws RecognitionException {
		Expr e;
		
		Block pureRelationalExpr_AST_in = (_t == ASTNULL) ? null : (Block)_t;
		
		Expr e1, e2;
		Operator op;
		
		
		{
		if (_t==null) _t=ASTNULL;
		switch ( _t.getType()) {
		case LT:
		{
			AST __t32 = _t;
			Block tmp15_AST_in = (Block)_t;
			match(_t,LT);
			_t = _t.getFirstChild();
			e1=pureExpr(_t,mm);
			_t = _retTree;
			e2=pureExpr(_t,mm);
			_t = _retTree;
			_t = __t32;
			_t = _t.getNextSibling();
			op = ComparisonOp.LT;
			break;
		}
		case LTE:
		{
			AST __t33 = _t;
			Block tmp16_AST_in = (Block)_t;
			match(_t,LTE);
			_t = _t.getFirstChild();
			e1=pureExpr(_t,mm);
			_t = _retTree;
			e2=pureExpr(_t,mm);
			_t = _retTree;
			_t = __t33;
			_t = _t.getNextSibling();
			op = ComparisonOp.LEQ;
			break;
		}
		case GT:
		{
			AST __t34 = _t;
			Block tmp17_AST_in = (Block)_t;
			match(_t,GT);
			_t = _t.getFirstChild();
			e1=pureExpr(_t,mm);
			_t = _retTree;
			e2=pureExpr(_t,mm);
			_t = _retTree;
			_t = __t34;
			_t = _t.getNextSibling();
			op = ComparisonOp.GT;
			break;
		}
		case GTE:
		{
			AST __t35 = _t;
			Block tmp18_AST_in = (Block)_t;
			match(_t,GTE);
			_t = _t.getFirstChild();
			e1=pureExpr(_t,mm);
			_t = _retTree;
			e2=pureExpr(_t,mm);
			_t = _retTree;
			_t = __t35;
			_t = _t.getNextSibling();
			op = ComparisonOp.GEQ;
			break;
		}
		default:
		{
			throw new NoViableAltException(_t);
		}
		}
		}
		e = fac.op (op).binApply (e1, e2);
		_retTree = _t;
		return e;
	}
	
	public final Expr  pureAdditiveExpr(AST _t,
		MemoryModel mm
	) throws RecognitionException {
		Expr e;
		
		Block pureAdditiveExpr_AST_in = (_t == ASTNULL) ? null : (Block)_t;
		
		Expr e1, e2;
		Operator op;
		
		
		{
		if (_t==null) _t=ASTNULL;
		switch ( _t.getType()) {
		case PLUS:
		{
			AST __t38 = _t;
			Block tmp19_AST_in = (Block)_t;
			match(_t,PLUS);
			_t = _t.getFirstChild();
			e1=pureExpr(_t,mm);
			_t = _retTree;
			e2=pureExpr(_t,mm);
			_t = _retTree;
			_t = __t38;
			_t = _t.getNextSibling();
			op = NumericOp.PLUS;
			break;
		}
		case MINUS:
		{
			AST __t39 = _t;
			Block tmp20_AST_in = (Block)_t;
			match(_t,MINUS);
			_t = _t.getFirstChild();
			e1=pureExpr(_t,mm);
			_t = _retTree;
			e2=pureExpr(_t,mm);
			_t = _retTree;
			_t = __t39;
			_t = _t.getNextSibling();
			op = NumericOp.MINUS;
			break;
		}
		default:
		{
			throw new NoViableAltException(_t);
		}
		}
		}
		e = fac.op (op).binApply (e1, e2);
		_retTree = _t;
		return e;
	}
	
	public final Expr  pureMultExpr(AST _t,
		MemoryModel mm
	) throws RecognitionException {
		Expr e;
		
		Block pureMultExpr_AST_in = (_t == ASTNULL) ? null : (Block)_t;
		
		Expr e1, e2;
		Operator op;
		
		
		{
		if (_t==null) _t=ASTNULL;
		switch ( _t.getType()) {
		case STAR:
		{
			AST __t42 = _t;
			Block tmp21_AST_in = (Block)_t;
			match(_t,STAR);
			_t = _t.getFirstChild();
			e1=pureExpr(_t,mm);
			_t = _retTree;
			e2=pureExpr(_t,mm);
			_t = _retTree;
			_t = __t42;
			_t = _t.getNextSibling();
			op = NumericOp.MULT;
			break;
		}
		case DIV:
		{
			AST __t43 = _t;
			Block tmp22_AST_in = (Block)_t;
			match(_t,DIV);
			_t = _t.getFirstChild();
			e1=pureExpr(_t,mm);
			_t = _retTree;
			e2=pureExpr(_t,mm);
			_t = _retTree;
			_t = __t43;
			_t = _t.getNextSibling();
			op = NumericOp.DIV;
			break;
		}
		case MOD:
		{
			AST __t44 = _t;
			Block tmp23_AST_in = (Block)_t;
			match(_t,MOD);
			_t = _t.getFirstChild();
			e1=pureExpr(_t,mm);
			_t = _retTree;
			e2=pureExpr(_t,mm);
			_t = _retTree;
			_t = __t44;
			_t = _t.getNextSibling();
			op = NumericOp.MOD;
			break;
		}
		default:
		{
			throw new NoViableAltException(_t);
		}
		}
		}
		e = fac.op (op).binApply (e1, e2);
		_retTree = _t;
		return e;
	}
	
	public final Expr  pureUnaryExpr(AST _t,
		MemoryModel mm
	) throws RecognitionException {
		Expr e;
		
		Block pureUnaryExpr_AST_in = (_t == ASTNULL) ? null : (Block)_t;
		
		if (_t==null) _t=ASTNULL;
		switch ( _t.getType()) {
		case NPostfixExpr:
		{
			e=purePostfixExpr(_t,mm);
			_t = _retTree;
			break;
		}
		case NUnaryExpr:
		{
			e=purePrefixExpr(_t,mm);
			_t = _retTree;
			break;
		}
		default:
		{
			throw new NoViableAltException(_t);
		}
		}
		_retTree = _t;
		return e;
	}
	
	public final Expr  purePrimaryExpr(AST _t,
		MemoryModel mm
	) throws RecognitionException {
		Expr e;
		
		Block purePrimaryExpr_AST_in = (_t == ASTNULL) ? null : (Block)_t;
		Block n = null;
		
		if (_t==null) _t=ASTNULL;
		switch ( _t.getType()) {
		case ID:
		case PTR:
		case DOT:
		{
			e=lval(_t,mm);
			_t = _retTree;
			e = mm.lookup (e);
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
			n = _t==ASTNULL ? null : (Block)_t;
			intConst(_t);
			_t = _retTree;
			
			try
			{
			e = fac.intExpr (Integer.parseInt (n.getText ()));
			}
			catch (NumberFormatException ex)
			{
			throw new RecognitionException ("Unable to parse number");
			}   
			
			break;
		}
		case NPureExpressionGroup:
		{
			AST __t52 = _t;
			Block tmp24_AST_in = (Block)_t;
			match(_t,NPureExpressionGroup);
			_t = _t.getFirstChild();
			e=pureExpr(_t,mm);
			_t = _retTree;
			_t = __t52;
			_t = _t.getNextSibling();
			break;
		}
		default:
		{
			throw new NoViableAltException(_t);
		}
		}
		_retTree = _t;
		return e;
	}
	
	public final Expr  purePostfixExpr(AST _t,
		MemoryModel mm
	) throws RecognitionException {
		Expr e;
		
		Block purePostfixExpr_AST_in = (_t == ASTNULL) ? null : (Block)_t;
		
		AST __t47 = _t;
		Block tmp25_AST_in = (Block)_t;
		match(_t,NPostfixExpr);
		_t = _t.getFirstChild();
		e=lval(_t,mm);
		_t = _retTree;
		_t = __t47;
		_t = _t.getNextSibling();
		_retTree = _t;
		return e;
	}
	
	public final Expr  purePrefixExpr(AST _t,
		MemoryModel mm
	) throws RecognitionException {
		Expr e;
		
		Block purePrefixExpr_AST_in = (_t == ASTNULL) ? null : (Block)_t;
		Operator op;
		
		AST __t49 = _t;
		Block tmp26_AST_in = (Block)_t;
		match(_t,NUnaryExpr);
		_t = _t.getFirstChild();
		op=unaryOperator(_t);
		_t = _retTree;
		e=pureExpr(_t,mm);
		_t = _retTree;
		_t = __t49;
		_t = _t.getNextSibling();
		
		if (op != null)
		e = fac.op (op).unaryApply (e);
		
		_retTree = _t;
		return e;
	}
	
	public final Operator  unaryOperator(AST _t) throws RecognitionException {
		Operator op;
		
		Block unaryOperator_AST_in = (_t == ASTNULL) ? null : (Block)_t;
		
		if (_t==null) _t=ASTNULL;
		switch ( _t.getType()) {
		case BAND:
		{
			Block tmp27_AST_in = (Block)_t;
			match(_t,BAND);
			_t = _t.getNextSibling();
			op = null;
			break;
		}
		case STAR:
		{
			Block tmp28_AST_in = (Block)_t;
			match(_t,STAR);
			_t = _t.getNextSibling();
			op = null;
			break;
		}
		case PLUS:
		{
			Block tmp29_AST_in = (Block)_t;
			match(_t,PLUS);
			_t = _t.getNextSibling();
			op = null;
			break;
		}
		case MINUS:
		{
			Block tmp30_AST_in = (Block)_t;
			match(_t,MINUS);
			_t = _t.getNextSibling();
			op = NumericOp.UN_MINUS;
			break;
		}
		case LNOT:
		{
			Block tmp31_AST_in = (Block)_t;
			match(_t,LNOT);
			_t = _t.getNextSibling();
			op = BoolOp.NOT;
			break;
		}
		case LAND:
		{
			Block tmp32_AST_in = (Block)_t;
			match(_t,LAND);
			_t = _t.getNextSibling();
			op = BoolOp.AND;
			break;
		}
		default:
		{
			throw new NoViableAltException(_t);
		}
		}
		_retTree = _t;
		return op;
	}
	
	protected final void intConst(AST _t) throws RecognitionException {
		
		Block intConst_AST_in = (_t == ASTNULL) ? null : (Block)_t;
		
		if (_t==null) _t=ASTNULL;
		switch ( _t.getType()) {
		case IntOctalConst:
		{
			Block tmp33_AST_in = (Block)_t;
			match(_t,IntOctalConst);
			_t = _t.getNextSibling();
			break;
		}
		case LongOctalConst:
		{
			Block tmp34_AST_in = (Block)_t;
			match(_t,LongOctalConst);
			_t = _t.getNextSibling();
			break;
		}
		case UnsignedOctalConst:
		{
			Block tmp35_AST_in = (Block)_t;
			match(_t,UnsignedOctalConst);
			_t = _t.getNextSibling();
			break;
		}
		case IntIntConst:
		{
			Block tmp36_AST_in = (Block)_t;
			match(_t,IntIntConst);
			_t = _t.getNextSibling();
			break;
		}
		case LongIntConst:
		{
			Block tmp37_AST_in = (Block)_t;
			match(_t,LongIntConst);
			_t = _t.getNextSibling();
			break;
		}
		case UnsignedIntConst:
		{
			Block tmp38_AST_in = (Block)_t;
			match(_t,UnsignedIntConst);
			_t = _t.getNextSibling();
			break;
		}
		case IntHexConst:
		{
			Block tmp39_AST_in = (Block)_t;
			match(_t,IntHexConst);
			_t = _t.getNextSibling();
			break;
		}
		case LongHexConst:
		{
			Block tmp40_AST_in = (Block)_t;
			match(_t,LongHexConst);
			_t = _t.getNextSibling();
			break;
		}
		case UnsignedHexConst:
		{
			Block tmp41_AST_in = (Block)_t;
			match(_t,UnsignedHexConst);
			_t = _t.getNextSibling();
			break;
		}
		default:
		{
			throw new NoViableAltException(_t);
		}
		}
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
		"Number"
	};
	
	private static final long[] mk_tokenSet_0() {
		long[] data = { 9096145351810744320L, 142111878151710L, 0L, 0L};
		return data;
	}
	public static final BitSet _tokenSet_0 = new BitSet(mk_tokenSet_0());
	}
	
