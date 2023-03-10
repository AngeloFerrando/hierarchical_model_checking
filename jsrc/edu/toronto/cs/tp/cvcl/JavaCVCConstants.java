package edu.toronto.cs.tp.cvcl;

/* ----------------------------------------------------------------------------
 * This file was automatically generated by SWIG (http://www.swig.org).
 * Version: 1.3.21
 *
 * Do not make changes to this file unless you know what you are doing--modify
 * the SWIG interface file instead.
 * ----------------------------------------------------------------------------- */


public interface JavaCVCConstants {
  // enums and constants
  public final static int NULL_KIND = JavaCVCJNI.get_NULL_KIND();
  public final static int RAW_LIST = JavaCVCJNI.get_RAW_LIST();
  public final static int ID = JavaCVCJNI.get_ID();
  public final static int STRING_EXPR = JavaCVCJNI.get_STRING_EXPR();
  public final static int RATIONAL_EXPR = JavaCVCJNI.get_RATIONAL_EXPR();
  public final static int TRUE = JavaCVCJNI.get_TRUE();
  public final static int FALSE = JavaCVCJNI.get_FALSE();
  public final static int BOOLEAN = JavaCVCJNI.get_BOOLEAN();
  public final static int TUPLETYPE = JavaCVCJNI.get_TUPLETYPE();
  public final static int ARROW = JavaCVCJNI.get_ARROW();
  public final static int TYPE = JavaCVCJNI.get_TYPE();
  public final static int TYPEDECL = JavaCVCJNI.get_TYPEDECL();
  public final static int TYPEDEF = JavaCVCJNI.get_TYPEDEF();
  public final static int EQ = JavaCVCJNI.get_EQ();
  public final static int NEQ = JavaCVCJNI.get_NEQ();
  public final static int NOT = JavaCVCJNI.get_NOT();
  public final static int AND = JavaCVCJNI.get_AND();
  public final static int OR = JavaCVCJNI.get_OR();
  public final static int XOR = JavaCVCJNI.get_XOR();
  public final static int IFF = JavaCVCJNI.get_IFF();
  public final static int IMPLIES = JavaCVCJNI.get_IMPLIES();
  public final static int AND_R = JavaCVCJNI.get_AND_R();
  public final static int IFF_R = JavaCVCJNI.get_IFF_R();
  public final static int ITE_R = JavaCVCJNI.get_ITE_R();
  public final static int ITE = JavaCVCJNI.get_ITE();
  public final static int FORALL = JavaCVCJNI.get_FORALL();
  public final static int EXISTS = JavaCVCJNI.get_EXISTS();
  public final static int UFUNC = JavaCVCJNI.get_UFUNC();
  public final static int ASSERT = JavaCVCJNI.get_ASSERT();
  public final static int QUERY = JavaCVCJNI.get_QUERY();
  public final static int DBG = JavaCVCJNI.get_DBG();
  public final static int TRACE = JavaCVCJNI.get_TRACE();
  public final static int UNTRACE = JavaCVCJNI.get_UNTRACE();
  public final static int OPTION = JavaCVCJNI.get_OPTION();
  public final static int HELP = JavaCVCJNI.get_HELP();
  public final static int TRANSFORM = JavaCVCJNI.get_TRANSFORM();
  public final static int PRINT = JavaCVCJNI.get_PRINT();
  public final static int CALL = JavaCVCJNI.get_CALL();
  public final static int ECHO = JavaCVCJNI.get_ECHO();
  public final static int INCLUDE = JavaCVCJNI.get_INCLUDE();
  public final static int DUMP_PROOF = JavaCVCJNI.get_DUMP_PROOF();
  public final static int DUMP_ASSUMPTIONS = JavaCVCJNI.get_DUMP_ASSUMPTIONS();
  public final static int DUMP_SIG = JavaCVCJNI.get_DUMP_SIG();
  public final static int DUMP_TCC = JavaCVCJNI.get_DUMP_TCC();
  public final static int DUMP_TCC_ASSUMPTIONS = JavaCVCJNI.get_DUMP_TCC_ASSUMPTIONS();
  public final static int DUMP_TCC_PROOF = JavaCVCJNI.get_DUMP_TCC_PROOF();
  public final static int DUMP_CLOSURE = JavaCVCJNI.get_DUMP_CLOSURE();
  public final static int DUMP_CLOSURE_PROOF = JavaCVCJNI.get_DUMP_CLOSURE_PROOF();
  public final static int WHERE = JavaCVCJNI.get_WHERE();
  public final static int COUNTEREXAMPLE = JavaCVCJNI.get_COUNTEREXAMPLE();
  public final static int POP = JavaCVCJNI.get_POP();
  public final static int POP_SCOPE = JavaCVCJNI.get_POP_SCOPE();
  public final static int POPTO = JavaCVCJNI.get_POPTO();
  public final static int PUSH = JavaCVCJNI.get_PUSH();
  public final static int CONTEXT = JavaCVCJNI.get_CONTEXT();
  public final static int FORGET = JavaCVCJNI.get_FORGET();
  public final static int GET_TYPE = JavaCVCJNI.get_GET_TYPE();
  public final static int CHECK_TYPE = JavaCVCJNI.get_CHECK_TYPE();
  public final static int GET_CHILD = JavaCVCJNI.get_GET_CHILD();
  public final static int SUBSTITUTE = JavaCVCJNI.get_SUBSTITUTE();
  public final static int TCC = JavaCVCJNI.get_TCC();
  public final static int VARDECL = JavaCVCJNI.get_VARDECL();
  public final static int VARDECLS = JavaCVCJNI.get_VARDECLS();
  public final static int BOUND_VAR = JavaCVCJNI.get_BOUND_VAR();
  public final static int BOUND_ID = JavaCVCJNI.get_BOUND_ID();
  public final static int UPDATE = JavaCVCJNI.get_UPDATE();
  public final static int UPDATE_SELECT = JavaCVCJNI.get_UPDATE_SELECT();
  public final static int RECORD_TYPE = JavaCVCJNI.get_RECORD_TYPE();
  public final static int FIELD_DECL = JavaCVCJNI.get_FIELD_DECL();
  public final static int REC_LITERAL = JavaCVCJNI.get_REC_LITERAL();
  public final static int REC_ENTRY = JavaCVCJNI.get_REC_ENTRY();
  public final static int REC_SELECT = JavaCVCJNI.get_REC_SELECT();
  public final static int REC_UPDATE = JavaCVCJNI.get_REC_UPDATE();
  public final static int TUPLE = JavaCVCJNI.get_TUPLE();
  public final static int TUPLE_SELECT = JavaCVCJNI.get_TUPLE_SELECT();
  public final static int TUPLE_UPDATE = JavaCVCJNI.get_TUPLE_UPDATE();
  public final static int ARRAY = JavaCVCJNI.get_ARRAY();
  public final static int READ = JavaCVCJNI.get_READ();
  public final static int WRITE = JavaCVCJNI.get_WRITE();
  public final static int CONST_ARRAY = JavaCVCJNI.get_CONST_ARRAY();
  public final static int SUBRANGE = JavaCVCJNI.get_SUBRANGE();
  public final static int SCALARTYPE = JavaCVCJNI.get_SCALARTYPE();
  public final static int SUBTYPE = JavaCVCJNI.get_SUBTYPE();
  public final static int DATATYPE = JavaCVCJNI.get_DATATYPE();
  public final static int CONSTRUCTOR = JavaCVCJNI.get_CONSTRUCTOR();
  public final static int DATATYPE_UPDATE = JavaCVCJNI.get_DATATYPE_UPDATE();
  public final static int IF = JavaCVCJNI.get_IF();
  public final static int IFTHEN = JavaCVCJNI.get_IFTHEN();
  public final static int ELSE = JavaCVCJNI.get_ELSE();
  public final static int LET = JavaCVCJNI.get_LET();
  public final static int LETDECLS = JavaCVCJNI.get_LETDECLS();
  public final static int LETDECL = JavaCVCJNI.get_LETDECL();
  public final static int LAMBDA = JavaCVCJNI.get_LAMBDA();
  public final static int APPLY = JavaCVCJNI.get_APPLY();
  public final static int SIMULATE = JavaCVCJNI.get_SIMULATE();
  public final static int CONSTDEF = JavaCVCJNI.get_CONSTDEF();
  public final static int CONST = JavaCVCJNI.get_CONST();
  public final static int VARLIST = JavaCVCJNI.get_VARLIST();
  public final static int UCONST = JavaCVCJNI.get_UCONST();
  public final static int FUNCDEF = JavaCVCJNI.get_FUNCDEF();
  public final static int REAL = JavaCVCJNI.get_REAL();
  public final static int INT = JavaCVCJNI.get_INT();
  public final static int UMINUS = JavaCVCJNI.get_UMINUS();
  public final static int PLUS = JavaCVCJNI.get_PLUS();
  public final static int MINUS = JavaCVCJNI.get_MINUS();
  public final static int MULT = JavaCVCJNI.get_MULT();
  public final static int DIVIDE = JavaCVCJNI.get_DIVIDE();
  public final static int INTDIV = JavaCVCJNI.get_INTDIV();
  public final static int MOD = JavaCVCJNI.get_MOD();
  public final static int LT = JavaCVCJNI.get_LT();
  public final static int LE = JavaCVCJNI.get_LE();
  public final static int GT = JavaCVCJNI.get_GT();
  public final static int GE = JavaCVCJNI.get_GE();
  public final static int IS_INTEGER = JavaCVCJNI.get_IS_INTEGER();
  public final static int NEGINF = JavaCVCJNI.get_NEGINF();
  public final static int POSINF = JavaCVCJNI.get_POSINF();
  public final static int DARK_SHADOW = JavaCVCJNI.get_DARK_SHADOW();
  public final static int GRAY_SHADOW = JavaCVCJNI.get_GRAY_SHADOW();
  public final static int FLOOR = JavaCVCJNI.get_FLOOR();
  public final static int BITVECTOR = JavaCVCJNI.get_BITVECTOR();
  public final static int CONCAT = JavaCVCJNI.get_CONCAT();
  public final static int BIT_AND = JavaCVCJNI.get_BIT_AND();
  public final static int BIT_NEG = JavaCVCJNI.get_BIT_NEG();
  public final static int EXTRACT = JavaCVCJNI.get_EXTRACT();
  public final static int INT_TO_BV = JavaCVCJNI.get_INT_TO_BV();
  public final static int BV_TO_INT = JavaCVCJNI.get_BV_TO_INT();
  public final static int BOOL_EXTRACT = JavaCVCJNI.get_BOOL_EXTRACT();
  public final static int PF_APPLY = JavaCVCJNI.get_PF_APPLY();
  public final static int PF_HOLE = JavaCVCJNI.get_PF_HOLE();
  public final static int POW = JavaCVCJNI.get_POW();
  public final static int EMPTY = JavaCVCJNI.get_EMPTY();
  public final static int UNION = JavaCVCJNI.get_UNION();
  public final static int INTER = JavaCVCJNI.get_INTER();
  public final static int DIFF = JavaCVCJNI.get_DIFF();
  public final static int SINGLETON = JavaCVCJNI.get_SINGLETON();
  public final static int IN = JavaCVCJNI.get_IN();
  public final static int INCS = JavaCVCJNI.get_INCS();
  public final static int INCIN = JavaCVCJNI.get_INCIN();
  public final static int LAST_KIND = JavaCVCJNI.get_LAST_KIND();
}
