package edu.toronto.cs.tp.cvcl;

/* ----------------------------------------------------------------------------
 * This file was automatically generated by SWIG (http://www.swig.org).
 * Version: 1.3.21
 *
 * Do not make changes to this file unless you know what you are doing--modify
 * the SWIG interface file instead.
 * ----------------------------------------------------------------------------- */


class JavaCVCJNI {
  public final static native long new_CIntArray(int jarg1);
  public final static native void delete_CIntArray(long jarg1);
  public final static native int CIntArray_getitem(long jarg1, int jarg2);
  public final static native void CIntArray_setitem(long jarg1, int jarg2, int jarg3);
  public final static native long CIntArray_cast(long jarg1);
  public final static native long CIntArray_frompointer(long jarg1);
  public final static native long new_CVectorExpr(long jarg1);
  public final static native long CVectorExpr_size(long jarg1);
  public final static native boolean CVectorExpr_isEmpty(long jarg1);
  public final static native void CVectorExpr_clear(long jarg1);
  public final static native void CVectorExpr_add(long jarg1, long jarg2);
  public final static native long CVectorExpr_get(long jarg1, int jarg2);
  public final static native void CVectorExpr_set(long jarg1, int jarg2, long jarg3);
  public final static native void delete_CVectorExpr(long jarg1);
  public final static native long new_CVectorType(long jarg1);
  public final static native long CVectorType_size(long jarg1);
  public final static native boolean CVectorType_isEmpty(long jarg1);
  public final static native void CVectorType_clear(long jarg1);
  public final static native void CVectorType_add(long jarg1, long jarg2);
  public final static native long CVectorType_get(long jarg1, int jarg2);
  public final static native void CVectorType_set(long jarg1, int jarg2, long jarg3);
  public final static native void delete_CVectorType(long jarg1);
  public final static native int get_NULL_KIND();
  public final static native int get_RAW_LIST();
  public final static native int get_ID();
  public final static native int get_STRING_EXPR();
  public final static native int get_RATIONAL_EXPR();
  public final static native int get_TRUE();
  public final static native int get_FALSE();
  public final static native int get_BOOLEAN();
  public final static native int get_TUPLETYPE();
  public final static native int get_ARROW();
  public final static native int get_TYPE();
  public final static native int get_TYPEDECL();
  public final static native int get_TYPEDEF();
  public final static native int get_EQ();
  public final static native int get_NEQ();
  public final static native int get_NOT();
  public final static native int get_AND();
  public final static native int get_OR();
  public final static native int get_XOR();
  public final static native int get_IFF();
  public final static native int get_IMPLIES();
  public final static native int get_AND_R();
  public final static native int get_IFF_R();
  public final static native int get_ITE_R();
  public final static native int get_ITE();
  public final static native int get_FORALL();
  public final static native int get_EXISTS();
  public final static native int get_UFUNC();
  public final static native int get_ASSERT();
  public final static native int get_QUERY();
  public final static native int get_DBG();
  public final static native int get_TRACE();
  public final static native int get_UNTRACE();
  public final static native int get_OPTION();
  public final static native int get_HELP();
  public final static native int get_TRANSFORM();
  public final static native int get_PRINT();
  public final static native int get_CALL();
  public final static native int get_ECHO();
  public final static native int get_INCLUDE();
  public final static native int get_DUMP_PROOF();
  public final static native int get_DUMP_ASSUMPTIONS();
  public final static native int get_DUMP_SIG();
  public final static native int get_DUMP_TCC();
  public final static native int get_DUMP_TCC_ASSUMPTIONS();
  public final static native int get_DUMP_TCC_PROOF();
  public final static native int get_DUMP_CLOSURE();
  public final static native int get_DUMP_CLOSURE_PROOF();
  public final static native int get_WHERE();
  public final static native int get_COUNTEREXAMPLE();
  public final static native int get_POP();
  public final static native int get_POP_SCOPE();
  public final static native int get_POPTO();
  public final static native int get_PUSH();
  public final static native int get_CONTEXT();
  public final static native int get_FORGET();
  public final static native int get_GET_TYPE();
  public final static native int get_CHECK_TYPE();
  public final static native int get_GET_CHILD();
  public final static native int get_SUBSTITUTE();
  public final static native int get_TCC();
  public final static native int get_VARDECL();
  public final static native int get_VARDECLS();
  public final static native int get_BOUND_VAR();
  public final static native int get_BOUND_ID();
  public final static native int get_UPDATE();
  public final static native int get_UPDATE_SELECT();
  public final static native int get_RECORD_TYPE();
  public final static native int get_FIELD_DECL();
  public final static native int get_REC_LITERAL();
  public final static native int get_REC_ENTRY();
  public final static native int get_REC_SELECT();
  public final static native int get_REC_UPDATE();
  public final static native int get_TUPLE();
  public final static native int get_TUPLE_SELECT();
  public final static native int get_TUPLE_UPDATE();
  public final static native int get_ARRAY();
  public final static native int get_READ();
  public final static native int get_WRITE();
  public final static native int get_CONST_ARRAY();
  public final static native int get_SUBRANGE();
  public final static native int get_SCALARTYPE();
  public final static native int get_SUBTYPE();
  public final static native int get_DATATYPE();
  public final static native int get_CONSTRUCTOR();
  public final static native int get_DATATYPE_UPDATE();
  public final static native int get_IF();
  public final static native int get_IFTHEN();
  public final static native int get_ELSE();
  public final static native int get_LET();
  public final static native int get_LETDECLS();
  public final static native int get_LETDECL();
  public final static native int get_LAMBDA();
  public final static native int get_APPLY();
  public final static native int get_SIMULATE();
  public final static native int get_CONSTDEF();
  public final static native int get_CONST();
  public final static native int get_VARLIST();
  public final static native int get_UCONST();
  public final static native int get_FUNCDEF();
  public final static native int get_REAL();
  public final static native int get_INT();
  public final static native int get_UMINUS();
  public final static native int get_PLUS();
  public final static native int get_MINUS();
  public final static native int get_MULT();
  public final static native int get_DIVIDE();
  public final static native int get_INTDIV();
  public final static native int get_MOD();
  public final static native int get_LT();
  public final static native int get_LE();
  public final static native int get_GT();
  public final static native int get_GE();
  public final static native int get_IS_INTEGER();
  public final static native int get_NEGINF();
  public final static native int get_POSINF();
  public final static native int get_DARK_SHADOW();
  public final static native int get_GRAY_SHADOW();
  public final static native int get_FLOOR();
  public final static native int get_BITVECTOR();
  public final static native int get_CONCAT();
  public final static native int get_BIT_AND();
  public final static native int get_BIT_NEG();
  public final static native int get_EXTRACT();
  public final static native int get_INT_TO_BV();
  public final static native int get_BV_TO_INT();
  public final static native int get_BOOL_EXTRACT();
  public final static native int get_PF_APPLY();
  public final static native int get_PF_HOLE();
  public final static native int get_POW();
  public final static native int get_EMPTY();
  public final static native int get_UNION();
  public final static native int get_INTER();
  public final static native int get_DIFF();
  public final static native int get_SINGLETON();
  public final static native int get_IN();
  public final static native int get_INCS();
  public final static native int get_INCIN();
  public final static native int get_LAST_KIND();
  public final static native String Proof_toString(long jarg1);
  public final static native long Rational_getNumerator(long jarg1);
  public final static native long Rational_getDenominator(long jarg1);
  public final static native boolean Rational_isInteger(long jarg1);
  public final static native int Rational_getInt(long jarg1);
  public final static native long new_Rational__SWIG_0();
  public final static native long new_Rational__SWIG_1(long jarg1);
  public final static native long new_Rational__SWIG_2(int jarg1, int jarg2);
  public final static native int CLFlags_countFlags__SWIG_0(long jarg1, String jarg2);
  public final static native int CLFlags_countFlags__SWIG_1(long jarg1, String jarg2, long jarg3);
  public final static native void CLFlags_setFlag__SWIG_0(long jarg1, String jarg2, boolean jarg3);
  public final static native void CLFlags_setFlag__SWIG_1(long jarg1, String jarg2, int jarg3);
  public final static native void CLFlags_setFlag__SWIG_2(long jarg1, String jarg2, String jarg3);
  public final static native void CLFlags_setFlag__SWIG_3(long jarg1, String jarg2, long jarg3);
  public final static native void CLFlags_setFlag__SWIG_4(long jarg1, String jarg2, long jarg3);
  public final static native void delete_CLFlags(long jarg1);
  public final static native long ExprManager_rebuildExpr(long jarg1, long jarg2);
  public final static native java.math.BigInteger Expr_getIndex(long jarg1);
  public final static native long new_Expr__SWIG_0();
  public final static native long new_Expr__SWIG_1(long jarg1);
  public final static native long Expr_getRational(long jarg1);
  public final static native int Expr_arity(long jarg1);
  public final static native int Expr_getKind(long jarg1);
  public final static native long new_Expr__SWIG_2(long jarg1, long jarg2);
  public final static native long new_Expr__SWIG_3(long jarg1, long jarg2, long jarg3);
  public final static native long new_Expr__SWIG_4(long jarg1, long jarg2, long jarg3, long jarg4);
  public final static native long new_Expr__SWIG_5(long jarg1, long jarg2, long jarg3, long jarg4, long jarg5);
  public final static native long new_Expr__SWIG_6(long jarg1, long jarg2, long jarg3);
  public final static native long new_Expr__SWIG_7(long jarg1, int jarg2);
  public final static native long new_Expr__SWIG_8(long jarg1, int jarg2, long jarg3);
  public final static native long new_Expr__SWIG_9(long jarg1, int jarg2, long jarg3, long jarg4);
  public final static native long new_Expr__SWIG_10(long jarg1, int jarg2, long jarg3, long jarg4, long jarg5);
  public final static native long new_Expr__SWIG_11(long jarg1, int jarg2, long jarg3);
  public final static native boolean Expr_hasOp(long jarg1);
  public final static native long Expr_getOp(long jarg1);
  public final static native boolean Expr_isFalse(long jarg1);
  public final static native boolean Expr_isTrue(long jarg1);
  public final static native boolean Expr_isBoolConst(long jarg1);
  public final static native boolean Expr_isVar(long jarg1);
  public final static native boolean Expr_isString(long jarg1);
  public final static native boolean Expr_isClosure(long jarg1);
  public final static native boolean Expr_isQuantifier(long jarg1);
  public final static native boolean Expr_isLambda(long jarg1);
  public final static native boolean Expr_isApply(long jarg1);
  public final static native boolean Expr_isRecord(long jarg1);
  public final static native boolean Expr_isRecordAccess(long jarg1);
  public final static native boolean Expr_isTupleAccess(long jarg1);
  public final static native int Expr_isGeneric__SWIG_0(long jarg1);
  public final static native boolean Expr_isGeneric__SWIG_1(long jarg1, int jarg2);
  public final static native boolean Expr_isEq(long jarg1);
  public final static native boolean Expr_isNot(long jarg1);
  public final static native boolean Expr_isAnd(long jarg1);
  public final static native boolean Expr_isOr(long jarg1);
  public final static native boolean Expr_isITE(long jarg1);
  public final static native boolean Expr_isIff(long jarg1);
  public final static native boolean Expr_isImpl(long jarg1);
  public final static native boolean Expr_isForall(long jarg1);
  public final static native boolean Expr_isExists(long jarg1);
  public final static native boolean Expr_isRational(long jarg1);
  public final static native boolean Expr_isNull(long jarg1);
  public final static native long Expr_eqExpr(long jarg1, long jarg2);
  public final static native long Expr_notExpr(long jarg1);
  public final static native long Expr_negate(long jarg1);
  public final static native long Expr_andExpr__SWIG_0(long jarg1, long jarg2);
  public final static native long Expr_andExpr__SWIG_1(long jarg1);
  public final static native long Expr_orExpr__SWIG_0(long jarg1, long jarg2);
  public final static native long Expr_orExpr__SWIG_1(long jarg1);
  public final static native long Expr_iteExpr(long jarg1, long jarg2, long jarg3);
  public final static native long Expr_iffExpr(long jarg1, long jarg2);
  public final static native long Expr_impExpr(long jarg1, long jarg2);
  public final static native long Expr_assign(long jarg1, long jarg2);
  public final static native long Expr_get(long jarg1, int jarg2);
  public final static native String Expr_toString(long jarg1);
  public final static native boolean Expr_equal(long jarg1, long jarg2, long jarg3);
  public final static native int Expr_computeHashCode(long jarg1, long jarg2);
  public final static native String Op_toString(long jarg1);
  public final static native long new_Op__SWIG_0(long jarg1);
  public final static native long new_Op__SWIG_1(long jarg1, int jarg2);
  public final static native int Op_getKind(long jarg1);
  public final static native long new_Op__SWIG_2(int jarg1);
  public final static native boolean Op_hasExpr(long jarg1);
  public final static native long Op_getExpr(long jarg1);
  public final static native long Op_getOp(long jarg1, long jarg2);
  public final static native long new_Type();
  public final static native String Type_toString(long jarg1);
  public final static native long Type_getExpr(long jarg1);
  public final static native int Type_arity(long jarg1);
  public final static native void delete_Statistics(long jarg1);
  public final static native boolean Theorem3_isNull(long jarg1);
  public final static native boolean Theorem3_isRewrite(long jarg1);
  public final static native boolean Theorem3_isAxiom(long jarg1);
  public final static native long Theorem3_getExpr(long jarg1);
  public final static native long Theorem3_getLHS(long jarg1);
  public final static native long Theorem3_getRHS(long jarg1);
  public final static native long Theorem3_getProof(long jarg1);
  public final static native boolean Theorem3_withProof(long jarg1);
  public final static native boolean Theorem3_withAssumptions(long jarg1);
  public final static native String Theorem3_toString(long jarg1);
  public final static native void Theorem3_printx(long jarg1);
  public final static native void Theorem3_print(long jarg1);
  public final static native boolean Theorem3_isLiteral(long jarg1);
  public final static native int Theorem3_getScope(long jarg1);
  public final static native void delete_Theorem3(long jarg1);
  public final static native void delete_Context(long jarg1);
  public final static native long ValidityChecker_createFlags();
  public final static native long ValidityChecker_create__SWIG_0();
  public final static native long ValidityChecker_create__SWIG_1(long jarg1);
  public final static native long ValidityChecker_boolType(long jarg1);
  public final static native long ValidityChecker_realType(long jarg1);
  public final static native long ValidityChecker_intType(long jarg1);
  public final static native long ValidityChecker_subrangeType(long jarg1, long jarg2, long jarg3);
  public final static native long ValidityChecker_subtypeType(long jarg1, long jarg2);
  public final static native long ValidityChecker_tupleType__SWIG_0(long jarg1, long jarg2, long jarg3);
  public final static native long ValidityChecker_tupleType__SWIG_1(long jarg1, long jarg2, long jarg3, long jarg4);
  public final static native long ValidityChecker_tupleType__SWIG_2(long jarg1, long jarg2);
  public final static native long ValidityChecker_recordType__SWIG_0(long jarg1, String jarg2, long jarg3);
  public final static native long ValidityChecker_recordType__SWIG_1(long jarg1, String jarg2, long jarg3, String jarg4, long jarg5);
  public final static native long ValidityChecker_recordType__SWIG_2(long jarg1, String jarg2, long jarg3, String jarg4, long jarg5, String jarg6, long jarg7);
  public final static native long ValidityChecker_recordType__SWIG_3(long jarg1, long jarg2, long jarg3);
  public final static native long ValidityChecker_arrayType(long jarg1, long jarg2, long jarg3);
  public final static native long ValidityChecker_funType(long jarg1, long jarg2, long jarg3);
  public final static native long ValidityChecker_createType__SWIG_0(long jarg1, String jarg2);
  public final static native long ValidityChecker_createType__SWIG_1(long jarg1, String jarg2, long jarg3);
  public final static native long ValidityChecker_lookupType(long jarg1, String jarg2);
  public final static native long ValidityChecker_getEM(long jarg1);
  public final static native long ValidityChecker_varExpr(long jarg1, String jarg2, long jarg3);
  public final static native long ValidityChecker_boundVarExpr(long jarg1, String jarg2, String jarg3, long jarg4);
  public final static native long ValidityChecker_lookupVar(long jarg1, String jarg2, long jarg3);
  public final static native long ValidityChecker_getType(long jarg1, long jarg2);
  public final static native long ValidityChecker_getBaseType(long jarg1, long jarg2);
  public final static native long ValidityChecker_listExpr__SWIG_0(long jarg1, long jarg2);
  public final static native long ValidityChecker_listExpr__SWIG_1(long jarg1, long jarg2);
  public final static native long ValidityChecker_listExpr__SWIG_2(long jarg1, long jarg2, long jarg3);
  public final static native long ValidityChecker_listExpr__SWIG_3(long jarg1, long jarg2, long jarg3, long jarg4);
  public final static native long ValidityChecker_listExpr__SWIG_4(long jarg1, String jarg2, long jarg3);
  public final static native long ValidityChecker_listExpr__SWIG_5(long jarg1, String jarg2, long jarg3);
  public final static native long ValidityChecker_listExpr__SWIG_6(long jarg1, String jarg2, long jarg3, long jarg4);
  public final static native void ValidityChecker_printExpr(long jarg1, long jarg2);
  public final static native long ValidityChecker_parseExpr(long jarg1, long jarg2);
  public final static native long ValidityChecker_importExpr(long jarg1, long jarg2);
  public final static native long ValidityChecker_importType(long jarg1, long jarg2);
  public final static native long ValidityChecker_trueExpr(long jarg1);
  public final static native long ValidityChecker_falseExpr(long jarg1);
  public final static native long ValidityChecker_notExpr(long jarg1, long jarg2);
  public final static native long ValidityChecker_andExpr__SWIG_0(long jarg1, long jarg2, long jarg3);
  public final static native long ValidityChecker_andExpr__SWIG_1(long jarg1, long jarg2);
  public final static native long ValidityChecker_orExpr__SWIG_0(long jarg1, long jarg2, long jarg3);
  public final static native long ValidityChecker_orExpr__SWIG_1(long jarg1, long jarg2);
  public final static native long ValidityChecker_impliesExpr(long jarg1, long jarg2, long jarg3);
  public final static native long ValidityChecker_iffExpr(long jarg1, long jarg2, long jarg3);
  public final static native long ValidityChecker_eqExpr(long jarg1, long jarg2, long jarg3);
  public final static native long ValidityChecker_iteExpr(long jarg1, long jarg2, long jarg3, long jarg4);
  public final static native long ValidityChecker_createOp(long jarg1, String jarg2, long jarg3);
  public final static native long ValidityChecker_funExpr__SWIG_0(long jarg1, long jarg2, long jarg3);
  public final static native long ValidityChecker_funExpr__SWIG_1(long jarg1, long jarg2, long jarg3, long jarg4);
  public final static native long ValidityChecker_funExpr__SWIG_2(long jarg1, long jarg2, long jarg3, long jarg4, long jarg5);
  public final static native long ValidityChecker_funExpr__SWIG_3(long jarg1, long jarg2, long jarg3);
  public final static native long ValidityChecker_ratExpr__SWIG_0(long jarg1, int jarg2, int jarg3);
  public final static native long ValidityChecker_ratExpr__SWIG_1(long jarg1, String jarg2, String jarg3, int jarg4);
  public final static native long ValidityChecker_ratExpr__SWIG_2(long jarg1, String jarg2, int jarg3);
  public final static native long ValidityChecker_uminusExpr(long jarg1, long jarg2);
  public final static native long ValidityChecker_plusExpr(long jarg1, long jarg2, long jarg3);
  public final static native long ValidityChecker_minusExpr(long jarg1, long jarg2, long jarg3);
  public final static native long ValidityChecker_multExpr(long jarg1, long jarg2, long jarg3);
  public final static native long ValidityChecker_powExpr(long jarg1, long jarg2, long jarg3);
  public final static native long ValidityChecker_divideExpr(long jarg1, long jarg2, long jarg3);
  public final static native long ValidityChecker_ltExpr(long jarg1, long jarg2, long jarg3);
  public final static native long ValidityChecker_leExpr(long jarg1, long jarg2, long jarg3);
  public final static native long ValidityChecker_gtExpr(long jarg1, long jarg2, long jarg3);
  public final static native long ValidityChecker_geExpr(long jarg1, long jarg2, long jarg3);
  public final static native long ValidityChecker_floorExpr(long jarg1, long jarg2);
  public final static native long ValidityChecker_recordExpr__SWIG_0(long jarg1, String jarg2, long jarg3);
  public final static native long ValidityChecker_recordExpr__SWIG_1(long jarg1, String jarg2, long jarg3, String jarg4, long jarg5);
  public final static native long ValidityChecker_recordExpr__SWIG_2(long jarg1, String jarg2, long jarg3, String jarg4, long jarg5, String jarg6, long jarg7);
  public final static native long ValidityChecker_recordExpr__SWIG_3(long jarg1, long jarg2, long jarg3);
  public final static native long ValidityChecker_recSelectExpr(long jarg1, long jarg2, String jarg3);
  public final static native long ValidityChecker_recUpdateExpr(long jarg1, long jarg2, String jarg3, long jarg4);
  public final static native long ValidityChecker_readExpr(long jarg1, long jarg2, long jarg3);
  public final static native long ValidityChecker_writeExpr(long jarg1, long jarg2, long jarg3, long jarg4);
  public final static native long ValidityChecker_tupleExpr(long jarg1, long jarg2);
  public final static native long ValidityChecker_tupleSelectExpr(long jarg1, long jarg2, int jarg3);
  public final static native long ValidityChecker_tupleUpdateExpr(long jarg1, long jarg2, int jarg3, long jarg4);
  public final static native long ValidityChecker_datatypeUpdateExpr(long jarg1, long jarg2, long jarg3, long jarg4);
  public final static native long ValidityChecker_forallExpr(long jarg1, long jarg2, long jarg3);
  public final static native long ValidityChecker_existsExpr(long jarg1, long jarg2, long jarg3);
  public final static native long ValidityChecker_lambdaExpr(long jarg1, long jarg2, long jarg3);
  public final static native long ValidityChecker_simulateExpr(long jarg1, long jarg2, long jarg3, long jarg4, long jarg5);
  public final static native void ValidityChecker_assertFormula(long jarg1, long jarg2);
  public final static native long ValidityChecker_simplify(long jarg1, long jarg2);
  public final static native long ValidityChecker_simplifyThm(long jarg1, long jarg2);
  public final static native void ValidityChecker_printV(long jarg1, long jarg2);
  public final static native boolean ValidityChecker_query(long jarg1, long jarg2, boolean jarg3);
  public final static native void ValidityChecker_getAssertions(long jarg1, long jarg2);
  public final static native void ValidityChecker_getCounterExample(long jarg1, long jarg2);
  public final static native boolean ValidityChecker_inconsistent(long jarg1, long jarg2);
  public final static native long ValidityChecker_getProof(long jarg1);
  public final static native void ValidityChecker_getAssumptions(long jarg1, long jarg2);
  public final static native long ValidityChecker_getTCC(long jarg1);
  public final static native void ValidityChecker_getAssumptionsTCC(long jarg1, long jarg2);
  public final static native long ValidityChecker_getProofTCC(long jarg1);
  public final static native long ValidityChecker_getClosure(long jarg1);
  public final static native long ValidityChecker_getProofClosure(long jarg1);
  public final static native void ValidityChecker_push(long jarg1);
  public final static native void ValidityChecker_pop(long jarg1);
  public final static native void ValidityChecker_popto(long jarg1, int jarg2);
  public final static native int ValidityChecker_scopeLevel(long jarg1);
  public final static native long ValidityChecker_createContext(long jarg1);
  public final static native long ValidityChecker_getCurrentContext(long jarg1);
  public final static native long ValidityChecker_switchContext(long jarg1, long jarg2);
  public final static native long ValidityChecker_getStatistics(long jarg1);
  public final static native void ValidityChecker_printStatistics(long jarg1);
  public final static native long new_ValidityChecker__SWIG_0();
  public final static native long new_ValidityChecker__SWIG_1(long jarg1);
}
