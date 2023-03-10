// $ANTLR 2.7.4: "SymbolicExecutor.g" -> "SymbolicExecutor.java"$

    package edu.toronto.cs.yasm.abstractor;

public interface SymbolicExecutorTokenTypes {
	int EOF = 1;
	int NULL_TREE_LOOKAHEAD = 3;
	int LITERAL_typedef = 4;
	int LITERAL___asm__ = 5;
	int SEMI = 6;
	int ASSIGN = 7;
	int LITERAL_struct = 8;
	int LITERAL_union = 9;
	int LITERAL_enum = 10;
	int LITERAL_auto = 11;
	int LITERAL_register = 12;
	int LITERAL_extern = 13;
	int LITERAL_static = 14;
	int LITERAL___inline = 15;
	int LITERAL_const = 16;
	int LITERAL_volatile = 17;
	int LITERAL_void = 18;
	int LITERAL_char = 19;
	int LITERAL_short = 20;
	int LITERAL_int = 21;
	int LITERAL_long = 22;
	int LITERAL_float = 23;
	int LITERAL_double = 24;
	int LITERAL_signed = 25;
	int LITERAL_unsigned = 26;
	int ID = 27;
	int LCURLY = 28;
	int RCURLY = 29;
	int COMMA = 30;
	int COLON = 31;
	int STAR = 32;
	int LITERAL___attribute__ = 33;
	int LPAREN = 34;
	int RPAREN = 35;
	int LITERAL_asm = 36;
	int LBRACKET = 37;
	int RBRACKET = 38;
	int VARARGS = 39;
	int LITERAL_while = 40;
	int LITERAL_goto = 41;
	int LITERAL_break = 42;
	int LITERAL_return = 43;
	int LITERAL_if = 44;
	int LITERAL_else = 45;
	int LITERAL___nd_goto = 46;
	int StringLiteral = 47;
	int INC = 48;
	int DEC = 49;
	int PTR = 50;
	int DOT = 51;
	int LOR = 52;
	int LAND = 53;
	int BOR = 54;
	int BXOR = 55;
	int BAND = 56;
	int EQUAL = 57;
	int NOT_EQUAL = 58;
	int LT = 59;
	int LTE = 60;
	int GT = 61;
	int GTE = 62;
	int LSHIFT = 63;
	int RSHIFT = 64;
	int PLUS = 65;
	int MINUS = 66;
	int DIV = 67;
	int MOD = 68;
	int LITERAL_sizeof = 69;
	int BNOT = 70;
	int LNOT = 71;
	int CharLiteral = 72;
	int IntOctalConst = 73;
	int LongOctalConst = 74;
	int UnsignedOctalConst = 75;
	int IntIntConst = 76;
	int LongIntConst = 77;
	int UnsignedIntConst = 78;
	int IntHexConst = 79;
	int LongHexConst = 80;
	int UnsignedHexConst = 81;
	int FloatDoubleConst = 82;
	int DoubleDoubleConst = 83;
	int LongDoubleConst = 84;
	int NTypedefName = 85;
	int NInitDecl = 86;
	int NDeclarator = 87;
	int NStructDeclarator = 88;
	int NDeclaration = 89;
	int NCast = 90;
	int NPointerGroup = 91;
	int NFunctionCallArgs = 92;
	int NNonemptyAbstractDeclarator = 93;
	int NInitializer = 94;
	int NStatementExpr = 95;
	int NEmptyExpression = 96;
	int NParameterTypeList = 97;
	int NFunctionDef = 98;
	int NCompoundStatement = 99;
	int NParameterDeclaration = 100;
	int NCommaExpr = 101;
	int NUnaryExpr = 102;
	int NLabel = 103;
	int NPostfixExpr = 104;
	int NRangeExpr = 105;
	int NStringSeq = 106;
	int NAsmAttribute = 107;
	int NGnuAsmExpr = 108;
	int NProgram = 109;
	int NPureExpr = 110;
	int NPureExpressionGroup = 111;
	int NFunctionDeclSpecifiers = 112;
	int NFunctionBody = 113;
	int NFunctionCall = 114;
	int NFunctionCallStmt = 115;
	int NFunctionCallAssignStmt = 116;
	int NAssignStmt = 117;
	int NImplicitAssignStmt = 118;
	int NWhile = 119;
	int NIf = 120;
	int NScope = 121;
	int NLocalDeclarations = 122;
	int NDeclSpecifiers = 123;
	int NEmptyScope = 124;
	int NReturn = 125;
	int NGoto = 126;
	int NContinue = 127;
	int NBreak = 128;
	int NLabelledStmt = 129;
	int NArrayIndex = 130;
	int NArrayElement = 131;
	int NBlock = 132;
	int NStructFields = 133;
	int NNDGoto = 134;
	int NLCurlyInitializer = 135;
	int NLHS = 136;
	int NRHS = 137;
	int Vocabulary = 138;
	int QUESTION = 139;
	int DIV_ASSIGN = 140;
	int PLUS_ASSIGN = 141;
	int MINUS_ASSIGN = 142;
	int STAR_ASSIGN = 143;
	int MOD_ASSIGN = 144;
	int RSHIFT_ASSIGN = 145;
	int LSHIFT_ASSIGN = 146;
	int BAND_ASSIGN = 147;
	int BOR_ASSIGN = 148;
	int BXOR_ASSIGN = 149;
	int Whitespace = 150;
	int Comment = 151;
	int CPPComment = 152;
	int PREPROC_DIRECTIVE = 153;
	int Space = 154;
	int LineDirective = 155;
	int BadStringLiteral = 156;
	int Escape = 157;
	int Digit = 158;
	int LongSuffix = 159;
	int UnsignedSuffix = 160;
	int FloatSuffix = 161;
	int Exponent = 162;
	int Number = 163;
}
