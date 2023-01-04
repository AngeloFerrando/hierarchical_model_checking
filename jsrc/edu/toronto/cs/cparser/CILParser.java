// $ANTLR 2.7.4: "CILParser.g" -> "CILParser.java"$
 package edu.toronto.cs.cparser; 
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

import java.io.*;

import antlr.CommonAST;
import antlr.DumpASTVisitor;

import edu.toronto.cs.cparser.block.*;

public class CILParser extends antlr.LLkParser       implements CILTokenTypes
 {

  // Suppport C++-style single-line comments?
  public static boolean CPPComments = true;

  // access to symbol table
  public CSymbolTable symbolTable = new CSymbolTable();

  // source for names to unnamed scopes
  protected int unnamedScopeCounter = 0;

  public boolean isTypedefName(String name) {
    boolean returnValue = false;
    TNode node = symbolTable.lookupNameInCurrentScope(name);
    //System.out.println ("Looking up and got: ");
    //TNode.printTree (node);
    if (node == null)
    {
      System.err.println ("Failed looking up " + name);
      return false;
    }

    node = (TNode) node.getFirstChild ().getFirstChild ();

    //System.out.println ("First child is: ");
    //TNode.printTree (node);
    
    for (; node != null; node = (TNode) node.getNextSibling() ) {
      if(node.getType() == LITERAL_typedef) {
        returnValue = true;
        break;
      }
    }
    //System.out.println (name + " is a typedef? " + returnValue);
    //System.out.println ("The symbol table is....");
    //System.out.println (symbolTable);
    return returnValue;
  }


  public String getAScopeName() {
    return "" + (unnamedScopeCounter++);
  }

  public void pushScope(String scopeName) {
    symbolTable.pushScope(scopeName);
  }

  public void popScope() {
    symbolTable.popScope();
  }

  int traceDepth = 0;
  public void reportError(RecognitionException ex) {
    try {
      System.err.println("ANTLR Parsing Error: "+ex + " token name:" + tokenNames[LA(1)]);
      ex.printStackTrace(System.err);
    }
    catch (TokenStreamException e) {
      System.err.println("ANTLR Parsing Error: "+ex);
      ex.printStackTrace(System.err);              
    }
  }
  public void reportError(String s) {
    System.err.println("ANTLR Parsing Error from String: " + s);
  }
  public void reportWarning(String s) {
    System.err.println("ANTLR Parsing Warning from String: " + s);
  }
  public void match(int t) throws MismatchedTokenException {
    boolean debugging = false;

    if ( debugging ) {
      for (int x=0; x<traceDepth; x++) System.out.print(" ");
      try {
        System.out.println("Match("+tokenNames[t]+") with LA(1)="+
            tokenNames[LA(1)] + ((inputState.guessing>0)?" [inputState.guessing "+ inputState.guessing + "]":""));
      }
      catch (TokenStreamException e) {
        System.out.println("Match("+tokenNames[t]+") " + ((inputState.guessing>0)?" [inputState.guessing "+ inputState.guessing + "]":""));

      }

    }
    try {
      if ( LA(1)!=t ) {
        if ( debugging ){
          for (int x=0; x<traceDepth; x++) System.out.print(" ");
          System.out.println("token mismatch: "+tokenNames[LA(1)]
              + "!="+tokenNames[t]);
        }
        throw new MismatchedTokenException(tokenNames, LT(1), t, false, getFilename());

      } else {
        // mark token as consumed -- fetch next token deferred until LA/LT
        consume();
      }
    }
    catch (TokenStreamException e) {
    }

  }
  public void traceIn(String rname) {
    traceDepth += 1;
    for (int x=0; x<traceDepth; x++) System.out.print(" ");
    try {
      System.out.println("> "+rname+"; LA(1)==("+ tokenNames[LT(1).getType()] 
          + ") " + LT(1).getText() + " [inputState.guessing "+ inputState.guessing + "]");
    }
    catch (TokenStreamException e) {
    }
  }
  public void traceOut(String rname) {
    for (int x=0; x<traceDepth; x++) System.out.print(" ");
    try {
      System.out.println("< "+rname+"; LA(1)==("+ tokenNames[LT(1).getType()] 
          + ") "+LT(1).getText() + " [inputState.guessing "+ inputState.guessing + "]");
    }
    catch (TokenStreamException e) {
    }
    traceDepth -= 1;
  }


protected CILParser(TokenBuffer tokenBuf, int k) {
  super(tokenBuf,k);
  tokenNames = _tokenNames;
  buildTokenTypeASTClassMap();
  astFactory = new ASTFactory(getTokenTypeToASTClassMap());
}

public CILParser(TokenBuffer tokenBuf) {
  this(tokenBuf,2);
}

protected CILParser(TokenStream lexer, int k) {
  super(lexer,k);
  tokenNames = _tokenNames;
  buildTokenTypeASTClassMap();
  astFactory = new ASTFactory(getTokenTypeToASTClassMap());
}

public CILParser(TokenStream lexer) {
  this(lexer,2);
}

public CILParser(ParserSharedInputState state) {
  super(state,2);
  tokenNames = _tokenNames;
  buildTokenTypeASTClassMap();
  astFactory = new ASTFactory(getTokenTypeToASTClassMap());
}

	public final void translationUnit() throws RecognitionException, TokenStreamException {
		
		returnAST = null;
		ASTPair currentAST = new ASTPair();
		Block translationUnit_AST = null;
		
		switch ( LA(1)) {
		case LITERAL_typedef:
		case LITERAL___asm__:
		case LITERAL_struct:
		case LITERAL_union:
		case LITERAL_enum:
		case LITERAL_auto:
		case LITERAL_register:
		case LITERAL_extern:
		case LITERAL_static:
		case LITERAL___inline:
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
		case ID:
		case STAR:
		{
			externalList();
			astFactory.addASTChild(currentAST, returnAST);
			if ( inputState.guessing==0 ) {
				translationUnit_AST = (Block)currentAST.root;
				
				translationUnit_AST = (Block)astFactory.make( (new ASTArray(2)).add((Block)astFactory.create(NProgram)).add(translationUnit_AST));
				
				currentAST.root = translationUnit_AST;
				currentAST.child = translationUnit_AST!=null &&translationUnit_AST.getFirstChild()!=null ?
					translationUnit_AST.getFirstChild() : translationUnit_AST;
				currentAST.advanceChildToEnd();
			}
			translationUnit_AST = (Block)currentAST.root;
			break;
		}
		case EOF:
		{
			if ( inputState.guessing==0 ) {
				
				System.err.println ( "Empty source file!" );
				
			}
			translationUnit_AST = (Block)currentAST.root;
			break;
		}
		default:
		{
			throw new NoViableAltException(LT(1), getFilename());
		}
		}
		returnAST = translationUnit_AST;
	}
	
	public final void externalList() throws RecognitionException, TokenStreamException {
		
		returnAST = null;
		ASTPair currentAST = new ASTPair();
		Block externalList_AST = null;
		
		{
		int _cnt4=0;
		_loop4:
		do {
			if ((_tokenSet_0.member(LA(1)))) {
				externalDef();
				astFactory.addASTChild(currentAST, returnAST);
			}
			else {
				if ( _cnt4>=1 ) { break _loop4; } else {throw new NoViableAltException(LT(1), getFilename());}
			}
			
			_cnt4++;
		} while (true);
		}
		externalList_AST = (Block)currentAST.root;
		returnAST = externalList_AST;
	}
	
	public final void externalDef() throws RecognitionException, TokenStreamException {
		
		returnAST = null;
		ASTPair currentAST = new ASTPair();
		Block externalDef_AST = null;
		
		boolean synPredMatched7 = false;
		if (((_tokenSet_1.member(LA(1))) && (_tokenSet_2.member(LA(2))))) {
			int _m7 = mark();
			synPredMatched7 = true;
			inputState.guessing++;
			try {
				{
				if ((LA(1)==LITERAL_typedef) && (true)) {
					match(LITERAL_typedef);
				}
				else if ((_tokenSet_1.member(LA(1))) && (_tokenSet_2.member(LA(2)))) {
					declaration();
				}
				else {
					throw new NoViableAltException(LT(1), getFilename());
				}
				
				}
			}
			catch (RecognitionException pe) {
				synPredMatched7 = false;
			}
			rewind(_m7);
			inputState.guessing--;
		}
		if ( synPredMatched7 ) {
			declaration();
			astFactory.addASTChild(currentAST, returnAST);
			externalDef_AST = (Block)currentAST.root;
		}
		else {
			boolean synPredMatched9 = false;
			if (((LA(1)==LITERAL___asm__))) {
				int _m9 = mark();
				synPredMatched9 = true;
				inputState.guessing++;
				try {
					{
					match(LITERAL___asm__);
					}
				}
				catch (RecognitionException pe) {
					synPredMatched9 = false;
				}
				rewind(_m9);
				inputState.guessing--;
			}
			if ( synPredMatched9 ) {
				gnuAsmExpr();
				match(SEMI);
				externalDef_AST = (Block)currentAST.root;
			}
			else if ((_tokenSet_3.member(LA(1))) && (_tokenSet_4.member(LA(2)))) {
				functionDef();
				astFactory.addASTChild(currentAST, returnAST);
				externalDef_AST = (Block)currentAST.root;
			}
			else {
				throw new NoViableAltException(LT(1), getFilename());
			}
			}
			returnAST = externalDef_AST;
		}
		
	public final void declaration() throws RecognitionException, TokenStreamException {
		
		returnAST = null;
		ASTPair currentAST = new ASTPair();
		Block declaration_AST = null;
		Block ds_AST = null;
		Block d_AST = null;
		Block i_AST = null;
		String declName = "";
		
		declSpecifiers();
		ds_AST = (Block)returnAST;
		{
		switch ( LA(1)) {
		case ID:
		case STAR:
		case LPAREN:
		{
			declName=declarator(false);
			d_AST = (Block)returnAST;
			if ( inputState.guessing==0 ) {
				
				/*
				AST ds1, d1;
				ds1 = astFactory.dupList (#ds);
				d1 = astFactory.dupList (#d);
				symbolTable.add ( declName, #(null, ds1, d1) );
				*/
				
			}
			break;
		}
		case SEMI:
		case ASSIGN:
		case LITERAL___attribute__:
		case LITERAL_asm:
		{
			break;
		}
		default:
		{
			throw new NoViableAltException(LT(1), getFilename());
		}
		}
		}
		{
		switch ( LA(1)) {
		case LITERAL___attribute__:
		case LITERAL_asm:
		{
			attributeDecl();
			break;
		}
		case SEMI:
		case ASSIGN:
		{
			break;
		}
		default:
		{
			throw new NoViableAltException(LT(1), getFilename());
		}
		}
		}
		{
		switch ( LA(1)) {
		case ASSIGN:
		{
			Block tmp2_AST = null;
			tmp2_AST = (Block)astFactory.create(LT(1));
			match(ASSIGN);
			initializer();
			i_AST = (Block)returnAST;
			break;
		}
		case SEMI:
		{
			break;
		}
		default:
		{
			throw new NoViableAltException(LT(1), getFilename());
		}
		}
		}
		match(SEMI);
		if ( inputState.guessing==0 ) {
			declaration_AST = (Block)currentAST.root;
			
			declaration_AST = (Block)astFactory.make( (new ASTArray(4)).add((Block)astFactory.create(NDeclaration)).add(ds_AST).add(d_AST).add(i_AST));
			symbolTable.add ( declName, declaration_AST );
			
			currentAST.root = declaration_AST;
			currentAST.child = declaration_AST!=null &&declaration_AST.getFirstChild()!=null ?
				declaration_AST.getFirstChild() : declaration_AST;
			currentAST.advanceChildToEnd();
		}
		returnAST = declaration_AST;
	}
	
	public final void gnuAsmExpr() throws RecognitionException, TokenStreamException {
		
		returnAST = null;
		ASTPair currentAST = new ASTPair();
		Block gnuAsmExpr_AST = null;
		
		Block tmp4_AST = null;
		tmp4_AST = (Block)astFactory.create(LT(1));
		astFactory.makeASTRoot(currentAST, tmp4_AST);
		match(LITERAL___asm__);
		{
		switch ( LA(1)) {
		case LITERAL_volatile:
		{
			Block tmp5_AST = null;
			tmp5_AST = (Block)astFactory.create(LT(1));
			astFactory.addASTChild(currentAST, tmp5_AST);
			match(LITERAL_volatile);
			break;
		}
		case LPAREN:
		{
			break;
		}
		default:
		{
			throw new NoViableAltException(LT(1), getFilename());
		}
		}
		}
		Block tmp6_AST = null;
		tmp6_AST = (Block)astFactory.create(LT(1));
		astFactory.addASTChild(currentAST, tmp6_AST);
		match(LPAREN);
		stringConst();
		astFactory.addASTChild(currentAST, returnAST);
		{
		if ((LA(1)==COLON) && (_tokenSet_5.member(LA(2)))) {
			Block tmp7_AST = null;
			tmp7_AST = (Block)astFactory.create(LT(1));
			astFactory.addASTChild(currentAST, tmp7_AST);
			match(COLON);
			{
			switch ( LA(1)) {
			case StringLiteral:
			{
				strOptExprPair();
				astFactory.addASTChild(currentAST, returnAST);
				{
				_loop132:
				do {
					if ((LA(1)==COMMA)) {
						Block tmp8_AST = null;
						tmp8_AST = (Block)astFactory.create(LT(1));
						astFactory.addASTChild(currentAST, tmp8_AST);
						match(COMMA);
						strOptExprPair();
						astFactory.addASTChild(currentAST, returnAST);
					}
					else {
						break _loop132;
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
				throw new NoViableAltException(LT(1), getFilename());
			}
			}
			}
			{
			if ((LA(1)==COLON) && (_tokenSet_5.member(LA(2)))) {
				Block tmp9_AST = null;
				tmp9_AST = (Block)astFactory.create(LT(1));
				astFactory.addASTChild(currentAST, tmp9_AST);
				match(COLON);
				{
				switch ( LA(1)) {
				case StringLiteral:
				{
					strOptExprPair();
					astFactory.addASTChild(currentAST, returnAST);
					{
					_loop136:
					do {
						if ((LA(1)==COMMA)) {
							Block tmp10_AST = null;
							tmp10_AST = (Block)astFactory.create(LT(1));
							astFactory.addASTChild(currentAST, tmp10_AST);
							match(COMMA);
							strOptExprPair();
							astFactory.addASTChild(currentAST, returnAST);
						}
						else {
							break _loop136;
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
					throw new NoViableAltException(LT(1), getFilename());
				}
				}
				}
			}
			else if ((LA(1)==COLON||LA(1)==RPAREN) && (LA(2)==SEMI||LA(2)==StringLiteral)) {
			}
			else {
				throw new NoViableAltException(LT(1), getFilename());
			}
			
			}
		}
		else if ((LA(1)==COLON||LA(1)==RPAREN) && (LA(2)==SEMI||LA(2)==StringLiteral)) {
		}
		else {
			throw new NoViableAltException(LT(1), getFilename());
		}
		
		}
		{
		switch ( LA(1)) {
		case COLON:
		{
			Block tmp11_AST = null;
			tmp11_AST = (Block)astFactory.create(LT(1));
			astFactory.addASTChild(currentAST, tmp11_AST);
			match(COLON);
			stringConst();
			astFactory.addASTChild(currentAST, returnAST);
			{
			_loop139:
			do {
				if ((LA(1)==COMMA)) {
					Block tmp12_AST = null;
					tmp12_AST = (Block)astFactory.create(LT(1));
					astFactory.addASTChild(currentAST, tmp12_AST);
					match(COMMA);
					stringConst();
					astFactory.addASTChild(currentAST, returnAST);
				}
				else {
					break _loop139;
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
			throw new NoViableAltException(LT(1), getFilename());
		}
		}
		}
		Block tmp13_AST = null;
		tmp13_AST = (Block)astFactory.create(LT(1));
		astFactory.addASTChild(currentAST, tmp13_AST);
		match(RPAREN);
		if ( inputState.guessing==0 ) {
			gnuAsmExpr_AST = (Block)currentAST.root;
			gnuAsmExpr_AST.setType(NGnuAsmExpr);
		}
		gnuAsmExpr_AST = (Block)currentAST.root;
		returnAST = gnuAsmExpr_AST;
	}
	
	public final void functionDef() throws RecognitionException, TokenStreamException {
		
		returnAST = null;
		ASTPair currentAST = new ASTPair();
		Block functionDef_AST = null;
		Block ds_AST = null;
		Block d_AST = null;
		String declName;
		
		functionDeclSpecifiers();
		ds_AST = (Block)returnAST;
		astFactory.addASTChild(currentAST, returnAST);
		declName=declarator(true);
		d_AST = (Block)returnAST;
		astFactory.addASTChild(currentAST, returnAST);
		if ( inputState.guessing==0 ) {
			
			/*
			AST d2, ds2;
			d2 = astFactory.dupList(#d);
			ds2 = astFactory.dupList(#ds);
			symbolTable.add(declName, #(null, ds2, d2));
			*/
			pushScope(declName);
			
		}
		if ( inputState.guessing==0 ) {
			popScope();
		}
		scope(declName);
		astFactory.addASTChild(currentAST, returnAST);
		if ( inputState.guessing==0 ) {
			functionDef_AST = (Block)currentAST.root;
			
			functionDef_AST = (Block)astFactory.make( (new ASTArray(2)).add((Block)astFactory.create(NFunctionDef)).add(functionDef_AST));
			symbolTable.add (declName, functionDef_AST);
			
			currentAST.root = functionDef_AST;
			currentAST.child = functionDef_AST!=null &&functionDef_AST.getFirstChild()!=null ?
				functionDef_AST.getFirstChild() : functionDef_AST;
			currentAST.advanceChildToEnd();
		}
		functionDef_AST = (Block)currentAST.root;
		returnAST = functionDef_AST;
	}
	
	public final void declSpecifiers() throws RecognitionException, TokenStreamException {
		
		returnAST = null;
		ASTPair currentAST = new ASTPair();
		Block declSpecifiers_AST = null;
		Block s_AST = null;
		int specCount=0;
		
		{
		int _cnt18=0;
		_loop18:
		do {
			switch ( LA(1)) {
			case LITERAL_typedef:
			case LITERAL_auto:
			case LITERAL_register:
			case LITERAL_extern:
			case LITERAL_static:
			case LITERAL___inline:
			{
				storageClassSpecifier();
				s_AST = (Block)returnAST;
				astFactory.addASTChild(currentAST, returnAST);
				break;
			}
			case LITERAL_const:
			case LITERAL_volatile:
			{
				typeQualifier();
				astFactory.addASTChild(currentAST, returnAST);
				break;
			}
			default:
				boolean synPredMatched17 = false;
				if (((_tokenSet_6.member(LA(1))) && (_tokenSet_7.member(LA(2))))) {
					int _m17 = mark();
					synPredMatched17 = true;
					inputState.guessing++;
					try {
						{
						if ((LA(1)==LITERAL_struct) && (true)) {
							match(LITERAL_struct);
						}
						else if ((LA(1)==LITERAL_union) && (true)) {
							match(LITERAL_union);
						}
						else if ((LA(1)==LITERAL_enum) && (true)) {
							match(LITERAL_enum);
						}
						else if ((_tokenSet_6.member(LA(1))) && (true)) {
							typeSpecifier(specCount);
						}
						else {
							throw new NoViableAltException(LT(1), getFilename());
						}
						
						}
					}
					catch (RecognitionException pe) {
						synPredMatched17 = false;
					}
					rewind(_m17);
					inputState.guessing--;
				}
				if ( synPredMatched17 ) {
					specCount=typeSpecifier(specCount);
					astFactory.addASTChild(currentAST, returnAST);
				}
				else if ((LA(1)==STAR) && (_tokenSet_8.member(LA(2)))) {
					pointerGroup();
					astFactory.addASTChild(currentAST, returnAST);
				}
			else {
				if ( _cnt18>=1 ) { break _loop18; } else {throw new NoViableAltException(LT(1), getFilename());}
			}
			}
			_cnt18++;
		} while (true);
		}
		if ( inputState.guessing==0 ) {
			declSpecifiers_AST = (Block)currentAST.root;
			declSpecifiers_AST = (Block)astFactory.make( (new ASTArray(2)).add((Block)astFactory.create(NDeclSpecifiers)).add(declSpecifiers_AST));
			currentAST.root = declSpecifiers_AST;
			currentAST.child = declSpecifiers_AST!=null &&declSpecifiers_AST.getFirstChild()!=null ?
				declSpecifiers_AST.getFirstChild() : declSpecifiers_AST;
			currentAST.advanceChildToEnd();
		}
		declSpecifiers_AST = (Block)currentAST.root;
		returnAST = declSpecifiers_AST;
	}
	
	public final String  declarator(
		boolean isFunctionDefinition
	) throws RecognitionException, TokenStreamException {
		String declName;
		
		returnAST = null;
		ASTPair currentAST = new ASTPair();
		Block declarator_AST = null;
		Token  id = null;
		Block id_AST = null;
		Block p_AST = null;
		Block i_AST = null;
		declName = "";
		
		{
		switch ( LA(1)) {
		case STAR:
		{
			pointerGroup();
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
			throw new NoViableAltException(LT(1), getFilename());
		}
		}
		}
		{
		switch ( LA(1)) {
		case ID:
		{
			id = LT(1);
			id_AST = (Block)astFactory.create(id);
			astFactory.addASTChild(currentAST, id_AST);
			match(ID);
			if ( inputState.guessing==0 ) {
				
				// *** don't mess with returned name; symbolTable expects
				// declName to be unscoped
				declName = id.getText();
				
				// OK to modify text since symbolTable does not use it
				id_AST.setText
				(symbolTable.addCurrentScopeToName (id.getText ()));
				
			}
			break;
		}
		case LPAREN:
		{
			Block tmp14_AST = null;
			tmp14_AST = (Block)astFactory.create(LT(1));
			astFactory.addASTChild(currentAST, tmp14_AST);
			match(LPAREN);
			declName=declarator(false);
			astFactory.addASTChild(currentAST, returnAST);
			Block tmp15_AST = null;
			tmp15_AST = (Block)astFactory.create(LT(1));
			astFactory.addASTChild(currentAST, tmp15_AST);
			match(RPAREN);
			break;
		}
		default:
		{
			throw new NoViableAltException(LT(1), getFilename());
		}
		}
		}
		{
		_loop92:
		do {
			switch ( LA(1)) {
			case LPAREN:
			{
				Block tmp16_AST = null;
				tmp16_AST = (Block)astFactory.create(LT(1));
				match(LPAREN);
				if ( inputState.guessing==0 ) {
					
					if (isFunctionDefinition) {
					pushScope(declName);
					}
					else {
					pushScope("!"+declName); 
					}
					
				}
				{
				boolean synPredMatched89 = false;
				if (((_tokenSet_1.member(LA(1))) && (_tokenSet_9.member(LA(2))))) {
					int _m89 = mark();
					synPredMatched89 = true;
					inputState.guessing++;
					try {
						{
						declSpecifiers();
						}
					}
					catch (RecognitionException pe) {
						synPredMatched89 = false;
					}
					rewind(_m89);
					inputState.guessing--;
				}
				if ( synPredMatched89 ) {
					parameterTypeList();
					p_AST = (Block)returnAST;
					if ( inputState.guessing==0 ) {
						declarator_AST = (Block)currentAST.root;
						
						declarator_AST = (Block)astFactory.make( (new ASTArray(3)).add(null).add(declarator_AST).add((Block)astFactory.make( (new ASTArray(2)).add((Block)astFactory.create(NParameterTypeList)).add(p_AST))));
						
						currentAST.root = declarator_AST;
						currentAST.child = declarator_AST!=null &&declarator_AST.getFirstChild()!=null ?
							declarator_AST.getFirstChild() : declarator_AST;
						currentAST.advanceChildToEnd();
					}
				}
				else if ((LA(1)==ID||LA(1)==RPAREN) && (_tokenSet_10.member(LA(2)))) {
					{
					switch ( LA(1)) {
					case ID:
					{
						idList();
						i_AST = (Block)returnAST;
						break;
					}
					case RPAREN:
					{
						break;
					}
					default:
					{
						throw new NoViableAltException(LT(1), getFilename());
					}
					}
					}
					if ( inputState.guessing==0 ) {
						declarator_AST = (Block)currentAST.root;
						
						declarator_AST = (Block)astFactory.make( (new ASTArray(3)).add(null).add(declarator_AST).add((Block)astFactory.make( (new ASTArray(2)).add((Block)astFactory.create(NParameterTypeList)).add(i_AST))));
						
						currentAST.root = declarator_AST;
						currentAST.child = declarator_AST!=null &&declarator_AST.getFirstChild()!=null ?
							declarator_AST.getFirstChild() : declarator_AST;
						currentAST.advanceChildToEnd();
					}
				}
				else {
					throw new NoViableAltException(LT(1), getFilename());
				}
				
				}
				if ( inputState.guessing==0 ) {
					
					popScope();
					
				}
				Block tmp17_AST = null;
				tmp17_AST = (Block)astFactory.create(LT(1));
				match(RPAREN);
				break;
			}
			case LBRACKET:
			{
				Block tmp18_AST = null;
				tmp18_AST = (Block)astFactory.create(LT(1));
				astFactory.addASTChild(currentAST, tmp18_AST);
				match(LBRACKET);
				{
				switch ( LA(1)) {
				case ID:
				case STAR:
				case LPAREN:
				case StringLiteral:
				case BAND:
				case PLUS:
				case MINUS:
				case LITERAL_sizeof:
				case BNOT:
				case LNOT:
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
				{
					pureExpr();
					astFactory.addASTChild(currentAST, returnAST);
					break;
				}
				case RBRACKET:
				{
					break;
				}
				default:
				{
					throw new NoViableAltException(LT(1), getFilename());
				}
				}
				}
				Block tmp19_AST = null;
				tmp19_AST = (Block)astFactory.create(LT(1));
				astFactory.addASTChild(currentAST, tmp19_AST);
				match(RBRACKET);
				break;
			}
			default:
			{
				break _loop92;
			}
			}
		} while (true);
		}
		if ( inputState.guessing==0 ) {
			declarator_AST = (Block)currentAST.root;
			declarator_AST = (Block)astFactory.make( (new ASTArray(2)).add((Block)astFactory.create(NDeclarator)).add(declarator_AST));
			currentAST.root = declarator_AST;
			currentAST.child = declarator_AST!=null &&declarator_AST.getFirstChild()!=null ?
				declarator_AST.getFirstChild() : declarator_AST;
			currentAST.advanceChildToEnd();
		}
		declarator_AST = (Block)currentAST.root;
		returnAST = declarator_AST;
		return declName;
	}
	
	public final void attributeDecl() throws RecognitionException, TokenStreamException {
		
		returnAST = null;
		ASTPair currentAST = new ASTPair();
		Block attributeDecl_AST = null;
		
		switch ( LA(1)) {
		case LITERAL___attribute__:
		{
			Block tmp20_AST = null;
			tmp20_AST = (Block)astFactory.create(LT(1));
			astFactory.makeASTRoot(currentAST, tmp20_AST);
			match(LITERAL___attribute__);
			Block tmp21_AST = null;
			tmp21_AST = (Block)astFactory.create(LT(1));
			astFactory.addASTChild(currentAST, tmp21_AST);
			match(LPAREN);
			Block tmp22_AST = null;
			tmp22_AST = (Block)astFactory.create(LT(1));
			astFactory.addASTChild(currentAST, tmp22_AST);
			match(LPAREN);
			attributeList();
			astFactory.addASTChild(currentAST, returnAST);
			Block tmp23_AST = null;
			tmp23_AST = (Block)astFactory.create(LT(1));
			astFactory.addASTChild(currentAST, tmp23_AST);
			match(RPAREN);
			Block tmp24_AST = null;
			tmp24_AST = (Block)astFactory.create(LT(1));
			astFactory.addASTChild(currentAST, tmp24_AST);
			match(RPAREN);
			attributeDecl_AST = (Block)currentAST.root;
			break;
		}
		case LITERAL_asm:
		{
			Block tmp25_AST = null;
			tmp25_AST = (Block)astFactory.create(LT(1));
			astFactory.makeASTRoot(currentAST, tmp25_AST);
			match(LITERAL_asm);
			Block tmp26_AST = null;
			tmp26_AST = (Block)astFactory.create(LT(1));
			astFactory.addASTChild(currentAST, tmp26_AST);
			match(LPAREN);
			stringConst();
			astFactory.addASTChild(currentAST, returnAST);
			Block tmp27_AST = null;
			tmp27_AST = (Block)astFactory.create(LT(1));
			astFactory.addASTChild(currentAST, tmp27_AST);
			match(RPAREN);
			if ( inputState.guessing==0 ) {
				attributeDecl_AST = (Block)currentAST.root;
				attributeDecl_AST.setType( NAsmAttribute );
			}
			attributeDecl_AST = (Block)currentAST.root;
			break;
		}
		default:
		{
			throw new NoViableAltException(LT(1), getFilename());
		}
		}
		returnAST = attributeDecl_AST;
	}
	
	public final void initializer() throws RecognitionException, TokenStreamException {
		
		returnAST = null;
		ASTPair currentAST = new ASTPair();
		Block initializer_AST = null;
		
		{
		switch ( LA(1)) {
		case ID:
		case STAR:
		case LPAREN:
		case StringLiteral:
		case BAND:
		case PLUS:
		case MINUS:
		case LITERAL_sizeof:
		case BNOT:
		case LNOT:
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
		{
			pureExpr();
			astFactory.addASTChild(currentAST, returnAST);
			if ( inputState.guessing==0 ) {
				initializer_AST = (Block)currentAST.root;
				initializer_AST = (Block)astFactory.make( (new ASTArray(2)).add((Block)astFactory.create(NInitializer)).add(initializer_AST));
				currentAST.root = initializer_AST;
				currentAST.child = initializer_AST!=null &&initializer_AST.getFirstChild()!=null ?
					initializer_AST.getFirstChild() : initializer_AST;
				currentAST.advanceChildToEnd();
			}
			break;
		}
		case LCURLY:
		{
			match(LCURLY);
			initializerList();
			astFactory.addASTChild(currentAST, returnAST);
			{
			switch ( LA(1)) {
			case COMMA:
			{
				match(COMMA);
				break;
			}
			case RCURLY:
			{
				break;
			}
			default:
			{
				throw new NoViableAltException(LT(1), getFilename());
			}
			}
			}
			match(RCURLY);
			if ( inputState.guessing==0 ) {
				initializer_AST = (Block)currentAST.root;
				initializer_AST = (Block)astFactory.make( (new ASTArray(2)).add((Block)astFactory.create(NLCurlyInitializer)).add(initializer_AST));
				currentAST.root = initializer_AST;
				currentAST.child = initializer_AST!=null &&initializer_AST.getFirstChild()!=null ?
					initializer_AST.getFirstChild() : initializer_AST;
				currentAST.advanceChildToEnd();
			}
			break;
		}
		default:
		{
			throw new NoViableAltException(LT(1), getFilename());
		}
		}
		}
		initializer_AST = (Block)currentAST.root;
		returnAST = initializer_AST;
	}
	
	public final void storageClassSpecifier() throws RecognitionException, TokenStreamException {
		
		returnAST = null;
		ASTPair currentAST = new ASTPair();
		Block storageClassSpecifier_AST = null;
		
		switch ( LA(1)) {
		case LITERAL_auto:
		{
			Block tmp31_AST = null;
			tmp31_AST = (Block)astFactory.create(LT(1));
			astFactory.addASTChild(currentAST, tmp31_AST);
			match(LITERAL_auto);
			storageClassSpecifier_AST = (Block)currentAST.root;
			break;
		}
		case LITERAL_register:
		{
			Block tmp32_AST = null;
			tmp32_AST = (Block)astFactory.create(LT(1));
			astFactory.addASTChild(currentAST, tmp32_AST);
			match(LITERAL_register);
			storageClassSpecifier_AST = (Block)currentAST.root;
			break;
		}
		case LITERAL_typedef:
		{
			Block tmp33_AST = null;
			tmp33_AST = (Block)astFactory.create(LT(1));
			astFactory.addASTChild(currentAST, tmp33_AST);
			match(LITERAL_typedef);
			storageClassSpecifier_AST = (Block)currentAST.root;
			break;
		}
		case LITERAL_extern:
		case LITERAL_static:
		case LITERAL___inline:
		{
			functionStorageClassSpecifier();
			astFactory.addASTChild(currentAST, returnAST);
			storageClassSpecifier_AST = (Block)currentAST.root;
			break;
		}
		default:
		{
			throw new NoViableAltException(LT(1), getFilename());
		}
		}
		returnAST = storageClassSpecifier_AST;
	}
	
	public final void typeQualifier() throws RecognitionException, TokenStreamException {
		
		returnAST = null;
		ASTPair currentAST = new ASTPair();
		Block typeQualifier_AST = null;
		
		switch ( LA(1)) {
		case LITERAL_const:
		{
			Block tmp34_AST = null;
			tmp34_AST = (Block)astFactory.create(LT(1));
			astFactory.addASTChild(currentAST, tmp34_AST);
			match(LITERAL_const);
			typeQualifier_AST = (Block)currentAST.root;
			break;
		}
		case LITERAL_volatile:
		{
			Block tmp35_AST = null;
			tmp35_AST = (Block)astFactory.create(LT(1));
			astFactory.addASTChild(currentAST, tmp35_AST);
			match(LITERAL_volatile);
			typeQualifier_AST = (Block)currentAST.root;
			break;
		}
		default:
		{
			throw new NoViableAltException(LT(1), getFilename());
		}
		}
		returnAST = typeQualifier_AST;
	}
	
	public final int  typeSpecifier(
		int specCount
	) throws RecognitionException, TokenStreamException {
		int retSpecCount;
		
		returnAST = null;
		ASTPair currentAST = new ASTPair();
		Block typeSpecifier_AST = null;
		retSpecCount = specCount + 1;
		
		{
		switch ( LA(1)) {
		case LITERAL_void:
		{
			Block tmp36_AST = null;
			tmp36_AST = (Block)astFactory.create(LT(1));
			astFactory.addASTChild(currentAST, tmp36_AST);
			match(LITERAL_void);
			break;
		}
		case LITERAL_char:
		{
			Block tmp37_AST = null;
			tmp37_AST = (Block)astFactory.create(LT(1));
			astFactory.addASTChild(currentAST, tmp37_AST);
			match(LITERAL_char);
			break;
		}
		case LITERAL_short:
		{
			Block tmp38_AST = null;
			tmp38_AST = (Block)astFactory.create(LT(1));
			astFactory.addASTChild(currentAST, tmp38_AST);
			match(LITERAL_short);
			break;
		}
		case LITERAL_int:
		{
			Block tmp39_AST = null;
			tmp39_AST = (Block)astFactory.create(LT(1));
			astFactory.addASTChild(currentAST, tmp39_AST);
			match(LITERAL_int);
			break;
		}
		case LITERAL_long:
		{
			Block tmp40_AST = null;
			tmp40_AST = (Block)astFactory.create(LT(1));
			astFactory.addASTChild(currentAST, tmp40_AST);
			match(LITERAL_long);
			break;
		}
		case LITERAL_float:
		{
			Block tmp41_AST = null;
			tmp41_AST = (Block)astFactory.create(LT(1));
			astFactory.addASTChild(currentAST, tmp41_AST);
			match(LITERAL_float);
			break;
		}
		case LITERAL_double:
		{
			Block tmp42_AST = null;
			tmp42_AST = (Block)astFactory.create(LT(1));
			astFactory.addASTChild(currentAST, tmp42_AST);
			match(LITERAL_double);
			break;
		}
		case LITERAL_signed:
		{
			Block tmp43_AST = null;
			tmp43_AST = (Block)astFactory.create(LT(1));
			astFactory.addASTChild(currentAST, tmp43_AST);
			match(LITERAL_signed);
			break;
		}
		case LITERAL_unsigned:
		{
			Block tmp44_AST = null;
			tmp44_AST = (Block)astFactory.create(LT(1));
			astFactory.addASTChild(currentAST, tmp44_AST);
			match(LITERAL_unsigned);
			break;
		}
		case LITERAL_struct:
		case LITERAL_union:
		{
			structOrUnionSpecifier();
			astFactory.addASTChild(currentAST, returnAST);
			{
			_loop25:
			do {
				if ((LA(1)==LITERAL___attribute__||LA(1)==LITERAL_asm) && (LA(2)==LPAREN)) {
					attributeDecl();
					astFactory.addASTChild(currentAST, returnAST);
				}
				else {
					break _loop25;
				}
				
			} while (true);
			}
			break;
		}
		case LITERAL_enum:
		{
			enumSpecifier();
			astFactory.addASTChild(currentAST, returnAST);
			break;
		}
		default:
			if (((LA(1)==ID))&&( specCount == 0 )) {
				typedefName();
				astFactory.addASTChild(currentAST, returnAST);
			}
		else {
			throw new NoViableAltException(LT(1), getFilename());
		}
		}
		}
		typeSpecifier_AST = (Block)currentAST.root;
		returnAST = typeSpecifier_AST;
		return retSpecCount;
	}
	
	public final void pointerGroup() throws RecognitionException, TokenStreamException {
		
		returnAST = null;
		ASTPair currentAST = new ASTPair();
		Block pointerGroup_AST = null;
		
		{
		int _cnt64=0;
		_loop64:
		do {
			if ((LA(1)==STAR) && (_tokenSet_11.member(LA(2)))) {
				Block tmp45_AST = null;
				tmp45_AST = (Block)astFactory.create(LT(1));
				astFactory.addASTChild(currentAST, tmp45_AST);
				match(STAR);
				{
				_loop63:
				do {
					if ((LA(1)==LITERAL_const||LA(1)==LITERAL_volatile) && (_tokenSet_11.member(LA(2)))) {
						typeQualifier();
						astFactory.addASTChild(currentAST, returnAST);
					}
					else {
						break _loop63;
					}
					
				} while (true);
				}
			}
			else {
				if ( _cnt64>=1 ) { break _loop64; } else {throw new NoViableAltException(LT(1), getFilename());}
			}
			
			_cnt64++;
		} while (true);
		}
		if ( inputState.guessing==0 ) {
			pointerGroup_AST = (Block)currentAST.root;
			pointerGroup_AST = (Block)astFactory.make( (new ASTArray(2)).add((Block)astFactory.create(NPointerGroup)).add(pointerGroup_AST));
			currentAST.root = pointerGroup_AST;
			currentAST.child = pointerGroup_AST!=null &&pointerGroup_AST.getFirstChild()!=null ?
				pointerGroup_AST.getFirstChild() : pointerGroup_AST;
			currentAST.advanceChildToEnd();
		}
		pointerGroup_AST = (Block)currentAST.root;
		returnAST = pointerGroup_AST;
	}
	
	public final void functionStorageClassSpecifier() throws RecognitionException, TokenStreamException {
		
		returnAST = null;
		ASTPair currentAST = new ASTPair();
		Block functionStorageClassSpecifier_AST = null;
		
		switch ( LA(1)) {
		case LITERAL_extern:
		{
			Block tmp46_AST = null;
			tmp46_AST = (Block)astFactory.create(LT(1));
			astFactory.addASTChild(currentAST, tmp46_AST);
			match(LITERAL_extern);
			functionStorageClassSpecifier_AST = (Block)currentAST.root;
			break;
		}
		case LITERAL_static:
		{
			Block tmp47_AST = null;
			tmp47_AST = (Block)astFactory.create(LT(1));
			astFactory.addASTChild(currentAST, tmp47_AST);
			match(LITERAL_static);
			functionStorageClassSpecifier_AST = (Block)currentAST.root;
			break;
		}
		case LITERAL___inline:
		{
			Block tmp48_AST = null;
			tmp48_AST = (Block)astFactory.create(LT(1));
			astFactory.addASTChild(currentAST, tmp48_AST);
			match(LITERAL___inline);
			functionStorageClassSpecifier_AST = (Block)currentAST.root;
			break;
		}
		default:
		{
			throw new NoViableAltException(LT(1), getFilename());
		}
		}
		returnAST = functionStorageClassSpecifier_AST;
	}
	
	public final void structOrUnionSpecifier() throws RecognitionException, TokenStreamException {
		
		returnAST = null;
		ASTPair currentAST = new ASTPair();
		Block structOrUnionSpecifier_AST = null;
		Block sou_AST = null;
		Token  id = null;
		Block id_AST = null;
		Block s_AST = null;
		Token  id1 = null;
		Block id1_AST = null;
		String scopeName;
		
		structOrUnion();
		sou_AST = (Block)returnAST;
		{
		boolean synPredMatched30 = false;
		if (((LA(1)==ID) && (LA(2)==LCURLY))) {
			int _m30 = mark();
			synPredMatched30 = true;
			inputState.guessing++;
			try {
				{
				match(ID);
				match(LCURLY);
				}
			}
			catch (RecognitionException pe) {
				synPredMatched30 = false;
			}
			rewind(_m30);
			inputState.guessing--;
		}
		if ( synPredMatched30 ) {
			id = LT(1);
			id_AST = (Block)astFactory.create(id);
			match(ID);
			match(LCURLY);
			if ( inputState.guessing==0 ) {
				
				scopeName = sou_AST.getText() + " " + id_AST.getText();
				pushScope(scopeName);
				
			}
			structDeclarationList();
			s_AST = (Block)returnAST;
			if ( inputState.guessing==0 ) {
				
				popScope();
				
			}
			match(RCURLY);
			if ( inputState.guessing==0 ) {
				structOrUnionSpecifier_AST = (Block)currentAST.root;
				
				structOrUnionSpecifier_AST = (Block)astFactory.make( (new ASTArray(3)).add(sou_AST).add(id_AST).add((Block)astFactory.make( (new ASTArray(2)).add((Block)astFactory.create(NStructFields)).add(s_AST))));
				
				// -- store type declaration in symbol table
				symbolTable.add (id.getText (), structOrUnionSpecifier_AST);
				
				currentAST.root = structOrUnionSpecifier_AST;
				currentAST.child = structOrUnionSpecifier_AST!=null &&structOrUnionSpecifier_AST.getFirstChild()!=null ?
					structOrUnionSpecifier_AST.getFirstChild() : structOrUnionSpecifier_AST;
				currentAST.advanceChildToEnd();
			}
		}
		else if ((LA(1)==ID) && (_tokenSet_12.member(LA(2)))) {
			id1 = LT(1);
			id1_AST = (Block)astFactory.create(id1);
			match(ID);
			if ( inputState.guessing==0 ) {
				structOrUnionSpecifier_AST = (Block)currentAST.root;
				structOrUnionSpecifier_AST = (Block)astFactory.make( (new ASTArray(2)).add(sou_AST).add(id1_AST));
				currentAST.root = structOrUnionSpecifier_AST;
				currentAST.child = structOrUnionSpecifier_AST!=null &&structOrUnionSpecifier_AST.getFirstChild()!=null ?
					structOrUnionSpecifier_AST.getFirstChild() : structOrUnionSpecifier_AST;
				currentAST.advanceChildToEnd();
			}
		}
		else {
			throw new NoViableAltException(LT(1), getFilename());
		}
		
		}
		returnAST = structOrUnionSpecifier_AST;
	}
	
	public final void enumSpecifier() throws RecognitionException, TokenStreamException {
		
		returnAST = null;
		ASTPair currentAST = new ASTPair();
		Block enumSpecifier_AST = null;
		Token  i = null;
		Block i_AST = null;
		
		Block tmp51_AST = null;
		tmp51_AST = (Block)astFactory.create(LT(1));
		astFactory.makeASTRoot(currentAST, tmp51_AST);
		match(LITERAL_enum);
		{
		boolean synPredMatched54 = false;
		if (((LA(1)==ID) && (LA(2)==LCURLY))) {
			int _m54 = mark();
			synPredMatched54 = true;
			inputState.guessing++;
			try {
				{
				match(ID);
				match(LCURLY);
				}
			}
			catch (RecognitionException pe) {
				synPredMatched54 = false;
			}
			rewind(_m54);
			inputState.guessing--;
		}
		if ( synPredMatched54 ) {
			i = LT(1);
			i_AST = (Block)astFactory.create(i);
			astFactory.addASTChild(currentAST, i_AST);
			match(ID);
			Block tmp52_AST = null;
			tmp52_AST = (Block)astFactory.create(LT(1));
			astFactory.addASTChild(currentAST, tmp52_AST);
			match(LCURLY);
			enumList(i.getText());
			astFactory.addASTChild(currentAST, returnAST);
			match(RCURLY);
		}
		else if ((LA(1)==LCURLY)) {
			Block tmp54_AST = null;
			tmp54_AST = (Block)astFactory.create(LT(1));
			astFactory.addASTChild(currentAST, tmp54_AST);
			match(LCURLY);
			enumList("anonymous");
			astFactory.addASTChild(currentAST, returnAST);
			match(RCURLY);
		}
		else if ((LA(1)==ID) && (_tokenSet_12.member(LA(2)))) {
			Block tmp56_AST = null;
			tmp56_AST = (Block)astFactory.create(LT(1));
			astFactory.addASTChild(currentAST, tmp56_AST);
			match(ID);
		}
		else {
			throw new NoViableAltException(LT(1), getFilename());
		}
		
		}
		enumSpecifier_AST = (Block)currentAST.root;
		returnAST = enumSpecifier_AST;
	}
	
	public final void typedefName() throws RecognitionException, TokenStreamException {
		
		returnAST = null;
		ASTPair currentAST = new ASTPair();
		Block typedefName_AST = null;
		Token  i = null;
		Block i_AST = null;
		
		if (!( isTypedefName ( LT(1).getText() ) ))
		  throw new SemanticException(" isTypedefName ( LT(1).getText() ) ");
		i = LT(1);
		i_AST = (Block)astFactory.create(i);
		astFactory.addASTChild(currentAST, i_AST);
		match(ID);
		if ( inputState.guessing==0 ) {
			typedefName_AST = (Block)currentAST.root;
			typedefName_AST = (Block)astFactory.make( (new ASTArray(2)).add((Block)astFactory.create(NTypedefName)).add(i_AST));
			currentAST.root = typedefName_AST;
			currentAST.child = typedefName_AST!=null &&typedefName_AST.getFirstChild()!=null ?
				typedefName_AST.getFirstChild() : typedefName_AST;
			currentAST.advanceChildToEnd();
		}
		typedefName_AST = (Block)currentAST.root;
		returnAST = typedefName_AST;
	}
	
	public final void structOrUnion() throws RecognitionException, TokenStreamException {
		
		returnAST = null;
		ASTPair currentAST = new ASTPair();
		Block structOrUnion_AST = null;
		
		switch ( LA(1)) {
		case LITERAL_struct:
		{
			Block tmp57_AST = null;
			tmp57_AST = (Block)astFactory.create(LT(1));
			astFactory.addASTChild(currentAST, tmp57_AST);
			match(LITERAL_struct);
			structOrUnion_AST = (Block)currentAST.root;
			break;
		}
		case LITERAL_union:
		{
			Block tmp58_AST = null;
			tmp58_AST = (Block)astFactory.create(LT(1));
			astFactory.addASTChild(currentAST, tmp58_AST);
			match(LITERAL_union);
			structOrUnion_AST = (Block)currentAST.root;
			break;
		}
		default:
		{
			throw new NoViableAltException(LT(1), getFilename());
		}
		}
		returnAST = structOrUnion_AST;
	}
	
	public final void structDeclarationList() throws RecognitionException, TokenStreamException {
		
		returnAST = null;
		ASTPair currentAST = new ASTPair();
		Block structDeclarationList_AST = null;
		
		{
		int _cnt34=0;
		_loop34:
		do {
			if ((_tokenSet_13.member(LA(1)))) {
				structDeclaration();
				astFactory.addASTChild(currentAST, returnAST);
			}
			else {
				if ( _cnt34>=1 ) { break _loop34; } else {throw new NoViableAltException(LT(1), getFilename());}
			}
			
			_cnt34++;
		} while (true);
		}
		structDeclarationList_AST = (Block)currentAST.root;
		returnAST = structDeclarationList_AST;
	}
	
	public final void structDeclaration() throws RecognitionException, TokenStreamException {
		
		returnAST = null;
		ASTPair currentAST = new ASTPair();
		Block structDeclaration_AST = null;
		
		specifierQualifierList();
		astFactory.addASTChild(currentAST, returnAST);
		structDeclaratorList();
		astFactory.addASTChild(currentAST, returnAST);
		{
		int _cnt37=0;
		_loop37:
		do {
			if ((LA(1)==SEMI)) {
				match(SEMI);
			}
			else {
				if ( _cnt37>=1 ) { break _loop37; } else {throw new NoViableAltException(LT(1), getFilename());}
			}
			
			_cnt37++;
		} while (true);
		}
		structDeclaration_AST = (Block)currentAST.root;
		returnAST = structDeclaration_AST;
	}
	
	public final void specifierQualifierList() throws RecognitionException, TokenStreamException {
		
		returnAST = null;
		ASTPair currentAST = new ASTPair();
		Block specifierQualifierList_AST = null;
		int specCount = 0;
		
		{
		int _cnt42=0;
		_loop42:
		do {
			boolean synPredMatched41 = false;
			if (((_tokenSet_6.member(LA(1))) && (_tokenSet_14.member(LA(2))))) {
				int _m41 = mark();
				synPredMatched41 = true;
				inputState.guessing++;
				try {
					{
					if ((LA(1)==LITERAL_struct) && (true)) {
						match(LITERAL_struct);
					}
					else if ((LA(1)==LITERAL_union) && (true)) {
						match(LITERAL_union);
					}
					else if ((LA(1)==LITERAL_enum) && (true)) {
						match(LITERAL_enum);
					}
					else if ((_tokenSet_6.member(LA(1))) && (true)) {
						typeSpecifier(specCount);
					}
					else {
						throw new NoViableAltException(LT(1), getFilename());
					}
					
					}
				}
				catch (RecognitionException pe) {
					synPredMatched41 = false;
				}
				rewind(_m41);
				inputState.guessing--;
			}
			if ( synPredMatched41 ) {
				specCount=typeSpecifier(specCount);
				astFactory.addASTChild(currentAST, returnAST);
			}
			else if ((LA(1)==LITERAL_const||LA(1)==LITERAL_volatile)) {
				typeQualifier();
				astFactory.addASTChild(currentAST, returnAST);
			}
			else if ((LA(1)==STAR) && (_tokenSet_15.member(LA(2)))) {
				pointerGroup();
				astFactory.addASTChild(currentAST, returnAST);
			}
			else {
				if ( _cnt42>=1 ) { break _loop42; } else {throw new NoViableAltException(LT(1), getFilename());}
			}
			
			_cnt42++;
		} while (true);
		}
		specifierQualifierList_AST = (Block)currentAST.root;
		returnAST = specifierQualifierList_AST;
	}
	
	public final void structDeclaratorList() throws RecognitionException, TokenStreamException {
		
		returnAST = null;
		ASTPair currentAST = new ASTPair();
		Block structDeclaratorList_AST = null;
		
		structDeclarator();
		astFactory.addASTChild(currentAST, returnAST);
		{
		_loop45:
		do {
			if ((LA(1)==COMMA)) {
				match(COMMA);
				structDeclarator();
				astFactory.addASTChild(currentAST, returnAST);
			}
			else {
				break _loop45;
			}
			
		} while (true);
		}
		structDeclaratorList_AST = (Block)currentAST.root;
		returnAST = structDeclaratorList_AST;
	}
	
	public final void structDeclarator() throws RecognitionException, TokenStreamException {
		
		returnAST = null;
		ASTPair currentAST = new ASTPair();
		Block structDeclarator_AST = null;
		
		{
		switch ( LA(1)) {
		case ID:
		case STAR:
		case LPAREN:
		{
			declarator(false);
			astFactory.addASTChild(currentAST, returnAST);
			break;
		}
		case SEMI:
		case COMMA:
		case COLON:
		case LITERAL___attribute__:
		case LITERAL_asm:
		{
			break;
		}
		default:
		{
			throw new NoViableAltException(LT(1), getFilename());
		}
		}
		}
		{
		switch ( LA(1)) {
		case COLON:
		{
			Block tmp61_AST = null;
			tmp61_AST = (Block)astFactory.create(LT(1));
			astFactory.addASTChild(currentAST, tmp61_AST);
			match(COLON);
			pureExpr();
			astFactory.addASTChild(currentAST, returnAST);
			break;
		}
		case SEMI:
		case COMMA:
		case LITERAL___attribute__:
		case LITERAL_asm:
		{
			break;
		}
		default:
		{
			throw new NoViableAltException(LT(1), getFilename());
		}
		}
		}
		{
		_loop50:
		do {
			if ((LA(1)==LITERAL___attribute__||LA(1)==LITERAL_asm)) {
				attributeDecl();
				astFactory.addASTChild(currentAST, returnAST);
			}
			else {
				break _loop50;
			}
			
		} while (true);
		}
		if ( inputState.guessing==0 ) {
			structDeclarator_AST = (Block)currentAST.root;
			structDeclarator_AST = (Block)astFactory.make( (new ASTArray(2)).add((Block)astFactory.create(NStructDeclarator)).add(structDeclarator_AST));
			currentAST.root = structDeclarator_AST;
			currentAST.child = structDeclarator_AST!=null &&structDeclarator_AST.getFirstChild()!=null ?
				structDeclarator_AST.getFirstChild() : structDeclarator_AST;
			currentAST.advanceChildToEnd();
		}
		structDeclarator_AST = (Block)currentAST.root;
		returnAST = structDeclarator_AST;
	}
	
	public final void pureExpr() throws RecognitionException, TokenStreamException {
		
		returnAST = null;
		ASTPair currentAST = new ASTPair();
		Block pureExpr_AST = null;
		
		pureLogicalOrExpr();
		astFactory.addASTChild(currentAST, returnAST);
		pureExpr_AST = (Block)currentAST.root;
		returnAST = pureExpr_AST;
	}
	
	public final void enumList(
		String enumName
	) throws RecognitionException, TokenStreamException {
		
		returnAST = null;
		ASTPair currentAST = new ASTPair();
		Block enumList_AST = null;
		
		enumerator(enumName);
		astFactory.addASTChild(currentAST, returnAST);
		{
		_loop57:
		do {
			if ((LA(1)==COMMA)) {
				match(COMMA);
				enumerator(enumName);
				astFactory.addASTChild(currentAST, returnAST);
			}
			else {
				break _loop57;
			}
			
		} while (true);
		}
		enumList_AST = (Block)currentAST.root;
		returnAST = enumList_AST;
	}
	
	public final void enumerator(
		String enumName
	) throws RecognitionException, TokenStreamException {
		
		returnAST = null;
		ASTPair currentAST = new ASTPair();
		Block enumerator_AST = null;
		Token  i = null;
		Block i_AST = null;
		
		i = LT(1);
		i_AST = (Block)astFactory.create(i);
		astFactory.addASTChild(currentAST, i_AST);
		match(ID);
		if ( inputState.guessing==0 ) {
			symbolTable.add(  i.getText(),
			(Block)astFactory.make( (new ASTArray(3)).add(null).add((Block)astFactory.create(LITERAL_enum,"enum")).add((Block)astFactory.create(ID,enumName)))
			);
			
		}
		{
		switch ( LA(1)) {
		case ASSIGN:
		{
			Block tmp63_AST = null;
			tmp63_AST = (Block)astFactory.create(LT(1));
			astFactory.addASTChild(currentAST, tmp63_AST);
			match(ASSIGN);
			pureExpr();
			astFactory.addASTChild(currentAST, returnAST);
			break;
		}
		case RCURLY:
		case COMMA:
		{
			break;
		}
		default:
		{
			throw new NoViableAltException(LT(1), getFilename());
		}
		}
		}
		enumerator_AST = (Block)currentAST.root;
		returnAST = enumerator_AST;
	}
	
	public final void attributeList() throws RecognitionException, TokenStreamException {
		
		returnAST = null;
		ASTPair currentAST = new ASTPair();
		Block attributeList_AST = null;
		
		attribute();
		astFactory.addASTChild(currentAST, returnAST);
		{
		_loop68:
		do {
			if ((LA(1)==COMMA) && ((LA(2) >= LITERAL_typedef && LA(2) <= Number))) {
				Block tmp64_AST = null;
				tmp64_AST = (Block)astFactory.create(LT(1));
				astFactory.addASTChild(currentAST, tmp64_AST);
				match(COMMA);
				attribute();
				astFactory.addASTChild(currentAST, returnAST);
			}
			else {
				break _loop68;
			}
			
		} while (true);
		}
		{
		switch ( LA(1)) {
		case COMMA:
		{
			Block tmp65_AST = null;
			tmp65_AST = (Block)astFactory.create(LT(1));
			astFactory.addASTChild(currentAST, tmp65_AST);
			match(COMMA);
			break;
		}
		case RPAREN:
		{
			break;
		}
		default:
		{
			throw new NoViableAltException(LT(1), getFilename());
		}
		}
		}
		attributeList_AST = (Block)currentAST.root;
		returnAST = attributeList_AST;
	}
	
	protected final void stringConst() throws RecognitionException, TokenStreamException {
		
		returnAST = null;
		ASTPair currentAST = new ASTPair();
		Block stringConst_AST = null;
		
		{
		int _cnt238=0;
		_loop238:
		do {
			if ((LA(1)==StringLiteral)) {
				Block tmp66_AST = null;
				tmp66_AST = (Block)astFactory.create(LT(1));
				astFactory.addASTChild(currentAST, tmp66_AST);
				match(StringLiteral);
			}
			else {
				if ( _cnt238>=1 ) { break _loop238; } else {throw new NoViableAltException(LT(1), getFilename());}
			}
			
			_cnt238++;
		} while (true);
		}
		if ( inputState.guessing==0 ) {
			stringConst_AST = (Block)currentAST.root;
			stringConst_AST = (Block)astFactory.make( (new ASTArray(2)).add((Block)astFactory.create(NStringSeq)).add(stringConst_AST));
			currentAST.root = stringConst_AST;
			currentAST.child = stringConst_AST!=null &&stringConst_AST.getFirstChild()!=null ?
				stringConst_AST.getFirstChild() : stringConst_AST;
			currentAST.advanceChildToEnd();
		}
		stringConst_AST = (Block)currentAST.root;
		returnAST = stringConst_AST;
	}
	
	public final void attribute() throws RecognitionException, TokenStreamException {
		
		returnAST = null;
		ASTPair currentAST = new ASTPair();
		Block attribute_AST = null;
		
		{
		_loop73:
		do {
			if ((_tokenSet_16.member(LA(1)))) {
				{
				Block tmp67_AST = null;
				tmp67_AST = (Block)astFactory.create(LT(1));
				astFactory.addASTChild(currentAST, tmp67_AST);
				match(_tokenSet_16);
				}
			}
			else if ((LA(1)==LPAREN)) {
				Block tmp68_AST = null;
				tmp68_AST = (Block)astFactory.create(LT(1));
				astFactory.addASTChild(currentAST, tmp68_AST);
				match(LPAREN);
				attributeList();
				astFactory.addASTChild(currentAST, returnAST);
				Block tmp69_AST = null;
				tmp69_AST = (Block)astFactory.create(LT(1));
				astFactory.addASTChild(currentAST, tmp69_AST);
				match(RPAREN);
			}
			else {
				break _loop73;
			}
			
		} while (true);
		}
		attribute_AST = (Block)currentAST.root;
		returnAST = attribute_AST;
	}
	
	public final void idList() throws RecognitionException, TokenStreamException {
		
		returnAST = null;
		ASTPair currentAST = new ASTPair();
		Block idList_AST = null;
		
		Block tmp70_AST = null;
		tmp70_AST = (Block)astFactory.create(LT(1));
		astFactory.addASTChild(currentAST, tmp70_AST);
		match(ID);
		{
		_loop76:
		do {
			if ((LA(1)==COMMA)) {
				match(COMMA);
				Block tmp72_AST = null;
				tmp72_AST = (Block)astFactory.create(LT(1));
				astFactory.addASTChild(currentAST, tmp72_AST);
				match(ID);
			}
			else {
				break _loop76;
			}
			
		} while (true);
		}
		idList_AST = (Block)currentAST.root;
		returnAST = idList_AST;
	}
	
	public final void initializerList() throws RecognitionException, TokenStreamException {
		
		returnAST = null;
		ASTPair currentAST = new ASTPair();
		Block initializerList_AST = null;
		
		initializer();
		astFactory.addASTChild(currentAST, returnAST);
		{
		_loop82:
		do {
			if ((LA(1)==COMMA) && (_tokenSet_17.member(LA(2)))) {
				match(COMMA);
				initializer();
				astFactory.addASTChild(currentAST, returnAST);
			}
			else {
				break _loop82;
			}
			
		} while (true);
		}
		initializerList_AST = (Block)currentAST.root;
		returnAST = initializerList_AST;
	}
	
	public final void parameterTypeList() throws RecognitionException, TokenStreamException {
		
		returnAST = null;
		ASTPair currentAST = new ASTPair();
		Block parameterTypeList_AST = null;
		
		parameterDeclaration();
		astFactory.addASTChild(currentAST, returnAST);
		{
		_loop95:
		do {
			if ((LA(1)==COMMA) && (_tokenSet_1.member(LA(2)))) {
				match(COMMA);
				parameterDeclaration();
				astFactory.addASTChild(currentAST, returnAST);
			}
			else {
				break _loop95;
			}
			
		} while (true);
		}
		{
		switch ( LA(1)) {
		case COMMA:
		{
			match(COMMA);
			Block tmp76_AST = null;
			tmp76_AST = (Block)astFactory.create(LT(1));
			astFactory.addASTChild(currentAST, tmp76_AST);
			match(VARARGS);
			break;
		}
		case RPAREN:
		{
			break;
		}
		default:
		{
			throw new NoViableAltException(LT(1), getFilename());
		}
		}
		}
		parameterTypeList_AST = (Block)currentAST.root;
		returnAST = parameterTypeList_AST;
	}
	
	public final void parameterDeclaration() throws RecognitionException, TokenStreamException {
		
		returnAST = null;
		ASTPair currentAST = new ASTPair();
		Block parameterDeclaration_AST = null;
		Block ds_AST = null;
		Block d_AST = null;
		String declName = "";
		
		declSpecifiers();
		ds_AST = (Block)returnAST;
		astFactory.addASTChild(currentAST, returnAST);
		{
		boolean synPredMatched100 = false;
		if (((_tokenSet_18.member(LA(1))) && (_tokenSet_19.member(LA(2))))) {
			int _m100 = mark();
			synPredMatched100 = true;
			inputState.guessing++;
			try {
				{
				declarator(false);
				}
			}
			catch (RecognitionException pe) {
				synPredMatched100 = false;
			}
			rewind(_m100);
			inputState.guessing--;
		}
		if ( synPredMatched100 ) {
			declName=declarator(false);
			d_AST = (Block)returnAST;
			astFactory.addASTChild(currentAST, returnAST);
			if ( inputState.guessing==0 ) {
				
				/*
				AST d2, ds2;
				d2 = astFactory.dupList(#d);
				ds2 = astFactory.dupList(#ds);
				symbolTable.add(declName, #(null, ds2, d2));
				*/
				
			}
		}
		else if ((_tokenSet_20.member(LA(1))) && (_tokenSet_21.member(LA(2)))) {
			nonemptyAbstractDeclarator();
			astFactory.addASTChild(currentAST, returnAST);
		}
		else if ((LA(1)==COMMA||LA(1)==RPAREN)) {
		}
		else {
			throw new NoViableAltException(LT(1), getFilename());
		}
		
		}
		if ( inputState.guessing==0 ) {
			parameterDeclaration_AST = (Block)currentAST.root;
			
			parameterDeclaration_AST = (Block)astFactory.make( (new ASTArray(2)).add((Block)astFactory.create(NParameterDeclaration)).add(parameterDeclaration_AST));
			symbolTable.add (declName, parameterDeclaration_AST);
			
			currentAST.root = parameterDeclaration_AST;
			currentAST.child = parameterDeclaration_AST!=null &&parameterDeclaration_AST.getFirstChild()!=null ?
				parameterDeclaration_AST.getFirstChild() : parameterDeclaration_AST;
			currentAST.advanceChildToEnd();
		}
		parameterDeclaration_AST = (Block)currentAST.root;
		returnAST = parameterDeclaration_AST;
	}
	
	public final void nonemptyAbstractDeclarator() throws RecognitionException, TokenStreamException {
		
		returnAST = null;
		ASTPair currentAST = new ASTPair();
		Block nonemptyAbstractDeclarator_AST = null;
		
		{
		switch ( LA(1)) {
		case STAR:
		{
			pointerGroup();
			astFactory.addASTChild(currentAST, returnAST);
			{
			_loop217:
			do {
				switch ( LA(1)) {
				case LPAREN:
				{
					{
					Block tmp77_AST = null;
					tmp77_AST = (Block)astFactory.create(LT(1));
					astFactory.addASTChild(currentAST, tmp77_AST);
					match(LPAREN);
					{
					if ((_tokenSet_20.member(LA(1))) && (_tokenSet_22.member(LA(2)))) {
						nonemptyAbstractDeclarator();
						astFactory.addASTChild(currentAST, returnAST);
					}
					else if ((_tokenSet_1.member(LA(1))) && (_tokenSet_9.member(LA(2)))) {
						parameterTypeList();
						astFactory.addASTChild(currentAST, returnAST);
					}
					else if ((LA(1)==RPAREN)) {
					}
					else {
						throw new NoViableAltException(LT(1), getFilename());
					}
					
					}
					Block tmp78_AST = null;
					tmp78_AST = (Block)astFactory.create(LT(1));
					astFactory.addASTChild(currentAST, tmp78_AST);
					match(RPAREN);
					}
					break;
				}
				case LBRACKET:
				{
					{
					Block tmp79_AST = null;
					tmp79_AST = (Block)astFactory.create(LT(1));
					astFactory.addASTChild(currentAST, tmp79_AST);
					match(LBRACKET);
					{
					switch ( LA(1)) {
					case ID:
					case STAR:
					case LPAREN:
					case StringLiteral:
					case BAND:
					case PLUS:
					case MINUS:
					case LITERAL_sizeof:
					case BNOT:
					case LNOT:
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
					{
						pureExpr();
						astFactory.addASTChild(currentAST, returnAST);
						break;
					}
					case RBRACKET:
					{
						break;
					}
					default:
					{
						throw new NoViableAltException(LT(1), getFilename());
					}
					}
					}
					Block tmp80_AST = null;
					tmp80_AST = (Block)astFactory.create(LT(1));
					astFactory.addASTChild(currentAST, tmp80_AST);
					match(RBRACKET);
					}
					break;
				}
				default:
				{
					break _loop217;
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
			int _cnt223=0;
			_loop223:
			do {
				switch ( LA(1)) {
				case LPAREN:
				{
					{
					Block tmp81_AST = null;
					tmp81_AST = (Block)astFactory.create(LT(1));
					astFactory.addASTChild(currentAST, tmp81_AST);
					match(LPAREN);
					{
					if ((_tokenSet_20.member(LA(1))) && (_tokenSet_22.member(LA(2)))) {
						nonemptyAbstractDeclarator();
						astFactory.addASTChild(currentAST, returnAST);
					}
					else if ((_tokenSet_1.member(LA(1))) && (_tokenSet_9.member(LA(2)))) {
						parameterTypeList();
						astFactory.addASTChild(currentAST, returnAST);
					}
					else if ((LA(1)==RPAREN)) {
					}
					else {
						throw new NoViableAltException(LT(1), getFilename());
					}
					
					}
					Block tmp82_AST = null;
					tmp82_AST = (Block)astFactory.create(LT(1));
					astFactory.addASTChild(currentAST, tmp82_AST);
					match(RPAREN);
					}
					break;
				}
				case LBRACKET:
				{
					{
					Block tmp83_AST = null;
					tmp83_AST = (Block)astFactory.create(LT(1));
					astFactory.addASTChild(currentAST, tmp83_AST);
					match(LBRACKET);
					{
					switch ( LA(1)) {
					case ID:
					case STAR:
					case LPAREN:
					case StringLiteral:
					case BAND:
					case PLUS:
					case MINUS:
					case LITERAL_sizeof:
					case BNOT:
					case LNOT:
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
					{
						pureExpr();
						astFactory.addASTChild(currentAST, returnAST);
						break;
					}
					case RBRACKET:
					{
						break;
					}
					default:
					{
						throw new NoViableAltException(LT(1), getFilename());
					}
					}
					}
					Block tmp84_AST = null;
					tmp84_AST = (Block)astFactory.create(LT(1));
					astFactory.addASTChild(currentAST, tmp84_AST);
					match(RBRACKET);
					}
					break;
				}
				default:
				{
					if ( _cnt223>=1 ) { break _loop223; } else {throw new NoViableAltException(LT(1), getFilename());}
				}
				}
				_cnt223++;
			} while (true);
			}
			break;
		}
		default:
		{
			throw new NoViableAltException(LT(1), getFilename());
		}
		}
		}
		if ( inputState.guessing==0 ) {
			nonemptyAbstractDeclarator_AST = (Block)currentAST.root;
			nonemptyAbstractDeclarator_AST = (Block)astFactory.make( (new ASTArray(2)).add((Block)astFactory.create(NNonemptyAbstractDeclarator)).add(nonemptyAbstractDeclarator_AST));
			currentAST.root = nonemptyAbstractDeclarator_AST;
			currentAST.child = nonemptyAbstractDeclarator_AST!=null &&nonemptyAbstractDeclarator_AST.getFirstChild()!=null ?
				nonemptyAbstractDeclarator_AST.getFirstChild() : nonemptyAbstractDeclarator_AST;
			currentAST.advanceChildToEnd();
		}
		nonemptyAbstractDeclarator_AST = (Block)currentAST.root;
		returnAST = nonemptyAbstractDeclarator_AST;
	}
	
	public final void functionDeclSpecifiers() throws RecognitionException, TokenStreamException {
		
		returnAST = null;
		ASTPair currentAST = new ASTPair();
		Block functionDeclSpecifiers_AST = null;
		int specCount = 0;
		
		{
		int _cnt108=0;
		_loop108:
		do {
			switch ( LA(1)) {
			case LITERAL_extern:
			case LITERAL_static:
			case LITERAL___inline:
			{
				functionStorageClassSpecifier();
				astFactory.addASTChild(currentAST, returnAST);
				break;
			}
			case LITERAL_const:
			case LITERAL_volatile:
			{
				typeQualifier();
				astFactory.addASTChild(currentAST, returnAST);
				break;
			}
			default:
				boolean synPredMatched107 = false;
				if (((_tokenSet_6.member(LA(1))) && (_tokenSet_4.member(LA(2))))) {
					int _m107 = mark();
					synPredMatched107 = true;
					inputState.guessing++;
					try {
						{
						if ((LA(1)==LITERAL_struct) && (true)) {
							match(LITERAL_struct);
						}
						else if ((LA(1)==LITERAL_union) && (true)) {
							match(LITERAL_union);
						}
						else if ((LA(1)==LITERAL_enum) && (true)) {
							match(LITERAL_enum);
						}
						else if ((_tokenSet_6.member(LA(1))) && (true)) {
							typeSpecifier(specCount);
						}
						else {
							throw new NoViableAltException(LT(1), getFilename());
						}
						
						}
					}
					catch (RecognitionException pe) {
						synPredMatched107 = false;
					}
					rewind(_m107);
					inputState.guessing--;
				}
				if ( synPredMatched107 ) {
					specCount=typeSpecifier(specCount);
					astFactory.addASTChild(currentAST, returnAST);
				}
			else {
				if ( _cnt108>=1 ) { break _loop108; } else {throw new NoViableAltException(LT(1), getFilename());}
			}
			}
			_cnt108++;
		} while (true);
		}
		{
		if ((LA(1)==STAR) && (_tokenSet_23.member(LA(2)))) {
			pointerGroup();
			astFactory.addASTChild(currentAST, returnAST);
		}
		else if ((_tokenSet_18.member(LA(1))) && (_tokenSet_24.member(LA(2)))) {
		}
		else {
			throw new NoViableAltException(LT(1), getFilename());
		}
		
		}
		if ( inputState.guessing==0 ) {
			functionDeclSpecifiers_AST = (Block)currentAST.root;
			functionDeclSpecifiers_AST = (Block)astFactory.make( (new ASTArray(2)).add((Block)astFactory.create(NDeclSpecifiers)).add(functionDeclSpecifiers_AST));
			currentAST.root = functionDeclSpecifiers_AST;
			currentAST.child = functionDeclSpecifiers_AST!=null &&functionDeclSpecifiers_AST.getFirstChild()!=null ?
				functionDeclSpecifiers_AST.getFirstChild() : functionDeclSpecifiers_AST;
			currentAST.advanceChildToEnd();
		}
		functionDeclSpecifiers_AST = (Block)currentAST.root;
		returnAST = functionDeclSpecifiers_AST;
	}
	
	public final void scope(
		String scopeName
	) throws RecognitionException, TokenStreamException {
		
		returnAST = null;
		ASTPair currentAST = new ASTPair();
		Block scope_AST = null;
		
		if ((LA(1)==LCURLY) && (LA(2)==RCURLY)) {
			match(LCURLY);
			match(RCURLY);
			if ( inputState.guessing==0 ) {
				scope_AST = (Block)currentAST.root;
				
				scope_AST = (Block)astFactory.make( (new ASTArray(2)).add((Block)astFactory.create(NScope,scopeName)).add((Block)astFactory.create(NLocalDeclarations)));
				
				currentAST.root = scope_AST;
				currentAST.child = scope_AST!=null &&scope_AST.getFirstChild()!=null ?
					scope_AST.getFirstChild() : scope_AST;
				currentAST.advanceChildToEnd();
			}
			scope_AST = (Block)currentAST.root;
		}
		else if ((LA(1)==LCURLY) && (_tokenSet_25.member(LA(2)))) {
			match(LCURLY);
			if ( inputState.guessing==0 ) {
				
				pushScope(scopeName);
				
			}
			optLocalDeclarations();
			astFactory.addASTChild(currentAST, returnAST);
			{
			switch ( LA(1)) {
			case LITERAL___asm__:
			case SEMI:
			case ID:
			case LCURLY:
			case STAR:
			case LPAREN:
			case LITERAL_while:
			case LITERAL_goto:
			case LITERAL_break:
			case LITERAL_return:
			case LITERAL_if:
			case LITERAL___nd_goto:
			{
				statementList();
				astFactory.addASTChild(currentAST, returnAST);
				break;
			}
			case RCURLY:
			{
				break;
			}
			default:
			{
				throw new NoViableAltException(LT(1), getFilename());
			}
			}
			}
			if ( inputState.guessing==0 ) {
				popScope();
			}
			match(RCURLY);
			if ( inputState.guessing==0 ) {
				scope_AST = (Block)currentAST.root;
				scope_AST = (Block)astFactory.make( (new ASTArray(2)).add((Block)astFactory.create(NScope,scopeName)).add(scope_AST));
				currentAST.root = scope_AST;
				currentAST.child = scope_AST!=null &&scope_AST.getFirstChild()!=null ?
					scope_AST.getFirstChild() : scope_AST;
				currentAST.advanceChildToEnd();
			}
			scope_AST = (Block)currentAST.root;
		}
		else {
			throw new NoViableAltException(LT(1), getFilename());
		}
		
		returnAST = scope_AST;
	}
	
	public final void declarationList() throws RecognitionException, TokenStreamException {
		
		returnAST = null;
		ASTPair currentAST = new ASTPair();
		Block declarationList_AST = null;
		
		{
		int _cnt114=0;
		_loop114:
		do {
			boolean synPredMatched113 = false;
			if (((_tokenSet_1.member(LA(1))) && (_tokenSet_2.member(LA(2))))) {
				int _m113 = mark();
				synPredMatched113 = true;
				inputState.guessing++;
				try {
					{
					declarationPredictor();
					}
				}
				catch (RecognitionException pe) {
					synPredMatched113 = false;
				}
				rewind(_m113);
				inputState.guessing--;
			}
			if ( synPredMatched113 ) {
				declaration();
				astFactory.addASTChild(currentAST, returnAST);
			}
			else {
				if ( _cnt114>=1 ) { break _loop114; } else {throw new NoViableAltException(LT(1), getFilename());}
			}
			
			_cnt114++;
		} while (true);
		}
		declarationList_AST = (Block)currentAST.root;
		returnAST = declarationList_AST;
	}
	
	public final void declarationPredictor() throws RecognitionException, TokenStreamException {
		
		returnAST = null;
		ASTPair currentAST = new ASTPair();
		Block declarationPredictor_AST = null;
		
		{
		if ((LA(1)==LITERAL_typedef) && (LA(2)==EOF)) {
			Block tmp89_AST = null;
			tmp89_AST = (Block)astFactory.create(LT(1));
			astFactory.addASTChild(currentAST, tmp89_AST);
			match(LITERAL_typedef);
		}
		else if ((_tokenSet_1.member(LA(1))) && (_tokenSet_2.member(LA(2)))) {
			declaration();
			astFactory.addASTChild(currentAST, returnAST);
		}
		else {
			throw new NoViableAltException(LT(1), getFilename());
		}
		
		}
		declarationPredictor_AST = (Block)currentAST.root;
		returnAST = declarationPredictor_AST;
	}
	
	public final void optLocalDeclarations() throws RecognitionException, TokenStreamException {
		
		returnAST = null;
		ASTPair currentAST = new ASTPair();
		Block optLocalDeclarations_AST = null;
		optLocalDeclarations_AST = (Block)astFactory.create(NLocalDeclarations);
		
		{
		boolean synPredMatched122 = false;
		if (((_tokenSet_1.member(LA(1))) && (_tokenSet_2.member(LA(2))))) {
			int _m122 = mark();
			synPredMatched122 = true;
			inputState.guessing++;
			try {
				{
				declarationPredictor();
				}
			}
			catch (RecognitionException pe) {
				synPredMatched122 = false;
			}
			rewind(_m122);
			inputState.guessing--;
		}
		if ( synPredMatched122 ) {
			declarationList();
			astFactory.addASTChild(currentAST, returnAST);
		}
		else if ((_tokenSet_26.member(LA(1))) && (_tokenSet_27.member(LA(2)))) {
		}
		else {
			throw new NoViableAltException(LT(1), getFilename());
		}
		
		}
		if ( inputState.guessing==0 ) {
			optLocalDeclarations_AST = (Block)currentAST.root;
			optLocalDeclarations_AST = (Block)astFactory.make( (new ASTArray(2)).add((Block)astFactory.create(NLocalDeclarations)).add(optLocalDeclarations_AST));
			currentAST.root = optLocalDeclarations_AST;
			currentAST.child = optLocalDeclarations_AST!=null &&optLocalDeclarations_AST.getFirstChild()!=null ?
				optLocalDeclarations_AST.getFirstChild() : optLocalDeclarations_AST;
			currentAST.advanceChildToEnd();
		}
		optLocalDeclarations_AST = (Block)currentAST.root;
		returnAST = optLocalDeclarations_AST;
	}
	
	public final void statementList() throws RecognitionException, TokenStreamException {
		
		returnAST = null;
		ASTPair currentAST = new ASTPair();
		Block statementList_AST = null;
		
		{
		int _cnt125=0;
		_loop125:
		do {
			if ((_tokenSet_28.member(LA(1)))) {
				stmt();
				astFactory.addASTChild(currentAST, returnAST);
			}
			else {
				if ( _cnt125>=1 ) { break _loop125; } else {throw new NoViableAltException(LT(1), getFilename());}
			}
			
			_cnt125++;
		} while (true);
		}
		statementList_AST = (Block)currentAST.root;
		returnAST = statementList_AST;
	}
	
	public final void stmt() throws RecognitionException, TokenStreamException {
		
		returnAST = null;
		ASTPair currentAST = new ASTPair();
		Block stmt_AST = null;
		
		switch ( LA(1)) {
		case LCURLY:
		{
			scope(getAScopeName());
			astFactory.addASTChild(currentAST, returnAST);
			stmt_AST = (Block)currentAST.root;
			break;
		}
		case LITERAL_while:
		case LITERAL_goto:
		case LITERAL_break:
		case LITERAL_return:
		case LITERAL_if:
		case LITERAL___nd_goto:
		{
			controlStmt();
			astFactory.addASTChild(currentAST, returnAST);
			stmt_AST = (Block)currentAST.root;
			break;
		}
		case LITERAL___asm__:
		{
			gnuAsmExpr();
			match(SEMI);
			stmt_AST = (Block)currentAST.root;
			break;
		}
		case SEMI:
		{
			match(SEMI);
			stmt_AST = (Block)currentAST.root;
			break;
		}
		default:
			if ((LA(1)==ID) && (LA(2)==LPAREN)) {
				functionCallStmt();
				astFactory.addASTChild(currentAST, returnAST);
				stmt_AST = (Block)currentAST.root;
			}
			else if ((_tokenSet_18.member(LA(1))) && (_tokenSet_29.member(LA(2)))) {
				assignStmt();
				astFactory.addASTChild(currentAST, returnAST);
				stmt_AST = (Block)currentAST.root;
			}
			else if ((LA(1)==ID) && (LA(2)==COLON)) {
				labelledStmt();
				astFactory.addASTChild(currentAST, returnAST);
				stmt_AST = (Block)currentAST.root;
			}
		else {
			throw new NoViableAltException(LT(1), getFilename());
		}
		}
		returnAST = stmt_AST;
	}
	
	public final void functionCallStmt() throws RecognitionException, TokenStreamException {
		
		returnAST = null;
		ASTPair currentAST = new ASTPair();
		Block functionCallStmt_AST = null;
		
		functionCall();
		astFactory.addASTChild(currentAST, returnAST);
		match(SEMI);
		if ( inputState.guessing==0 ) {
			functionCallStmt_AST = (Block)currentAST.root;
			functionCallStmt_AST = (Block)astFactory.make( (new ASTArray(2)).add((Block)astFactory.create(NFunctionCallStmt)).add(functionCallStmt_AST));
			currentAST.root = functionCallStmt_AST;
			currentAST.child = functionCallStmt_AST!=null &&functionCallStmt_AST.getFirstChild()!=null ?
				functionCallStmt_AST.getFirstChild() : functionCallStmt_AST;
			currentAST.advanceChildToEnd();
		}
		functionCallStmt_AST = (Block)currentAST.root;
		returnAST = functionCallStmt_AST;
	}
	
	public final void assignStmt() throws RecognitionException, TokenStreamException {
		
		returnAST = null;
		ASTPair currentAST = new ASTPair();
		Block assignStmt_AST = null;
		
		if ((_tokenSet_18.member(LA(1))) && (_tokenSet_30.member(LA(2)))) {
			lval();
			astFactory.addASTChild(currentAST, returnAST);
			match(ASSIGN);
			{
			if ((LA(1)==ID) && (LA(2)==LPAREN)) {
				functionCall();
				astFactory.addASTChild(currentAST, returnAST);
			}
			else if ((_tokenSet_31.member(LA(1))) && (_tokenSet_32.member(LA(2)))) {
				pureExpr();
				astFactory.addASTChild(currentAST, returnAST);
			}
			else {
				throw new NoViableAltException(LT(1), getFilename());
			}
			
			}
			match(SEMI);
			if ( inputState.guessing==0 ) {
				assignStmt_AST = (Block)currentAST.root;
				assignStmt_AST = (Block)astFactory.make( (new ASTArray(2)).add((Block)astFactory.create(NAssignStmt)).add(assignStmt_AST));
				currentAST.root = assignStmt_AST;
				currentAST.child = assignStmt_AST!=null &&assignStmt_AST.getFirstChild()!=null ?
					assignStmt_AST.getFirstChild() : assignStmt_AST;
				currentAST.advanceChildToEnd();
			}
			assignStmt_AST = (Block)currentAST.root;
		}
		else if ((_tokenSet_18.member(LA(1))) && (_tokenSet_33.member(LA(2)))) {
			implicitAssignStmt();
			astFactory.addASTChild(currentAST, returnAST);
			assignStmt_AST = (Block)currentAST.root;
		}
		else {
			throw new NoViableAltException(LT(1), getFilename());
		}
		
		returnAST = assignStmt_AST;
	}
	
	public final void labelledStmt() throws RecognitionException, TokenStreamException {
		
		returnAST = null;
		ASTPair currentAST = new ASTPair();
		Block labelledStmt_AST = null;
		Token  id = null;
		Block id_AST = null;
		Block s_AST = null;
		
		id = LT(1);
		id_AST = (Block)astFactory.create(id);
		match(ID);
		match(COLON);
		{
		if ((_tokenSet_28.member(LA(1))) && (_tokenSet_34.member(LA(2)))) {
			stmt();
			s_AST = (Block)returnAST;
		}
		else if ((_tokenSet_35.member(LA(1))) && (_tokenSet_27.member(LA(2)))) {
		}
		else {
			throw new NoViableAltException(LT(1), getFilename());
		}
		
		}
		if ( inputState.guessing==0 ) {
			labelledStmt_AST = (Block)currentAST.root;
			labelledStmt_AST = (Block)astFactory.make( (new ASTArray(3)).add((Block)astFactory.create(NLabelledStmt)).add((Block)astFactory.make( (new ASTArray(2)).add((Block)astFactory.create(NLabel)).add(id_AST))).add(s_AST));
			currentAST.root = labelledStmt_AST;
			currentAST.child = labelledStmt_AST!=null &&labelledStmt_AST.getFirstChild()!=null ?
				labelledStmt_AST.getFirstChild() : labelledStmt_AST;
			currentAST.advanceChildToEnd();
		}
		returnAST = labelledStmt_AST;
	}
	
	public final void controlStmt() throws RecognitionException, TokenStreamException {
		
		returnAST = null;
		ASTPair currentAST = new ASTPair();
		Block controlStmt_AST = null;
		Token  r = null;
		Block r_AST = null;
		
		switch ( LA(1)) {
		case LITERAL_while:
		{
			match(LITERAL_while);
			match(LPAREN);
			pureExpr();
			astFactory.addASTChild(currentAST, returnAST);
			match(RPAREN);
			stmt();
			astFactory.addASTChild(currentAST, returnAST);
			if ( inputState.guessing==0 ) {
				controlStmt_AST = (Block)currentAST.root;
				controlStmt_AST = (Block)astFactory.make( (new ASTArray(2)).add((Block)astFactory.create(NWhile)).add(controlStmt_AST));
				currentAST.root = controlStmt_AST;
				currentAST.child = controlStmt_AST!=null &&controlStmt_AST.getFirstChild()!=null ?
					controlStmt_AST.getFirstChild() : controlStmt_AST;
				currentAST.advanceChildToEnd();
			}
			controlStmt_AST = (Block)currentAST.root;
			break;
		}
		case LITERAL_goto:
		{
			match(LITERAL_goto);
			Block tmp100_AST = null;
			tmp100_AST = (Block)astFactory.create(LT(1));
			astFactory.addASTChild(currentAST, tmp100_AST);
			match(ID);
			match(SEMI);
			if ( inputState.guessing==0 ) {
				controlStmt_AST = (Block)currentAST.root;
				controlStmt_AST = (Block)astFactory.make( (new ASTArray(2)).add((Block)astFactory.create(NGoto)).add(controlStmt_AST));
				currentAST.root = controlStmt_AST;
				currentAST.child = controlStmt_AST!=null &&controlStmt_AST.getFirstChild()!=null ?
					controlStmt_AST.getFirstChild() : controlStmt_AST;
				currentAST.advanceChildToEnd();
			}
			controlStmt_AST = (Block)currentAST.root;
			break;
		}
		case LITERAL_break:
		{
			match(LITERAL_break);
			match(SEMI);
			if ( inputState.guessing==0 ) {
				controlStmt_AST = (Block)currentAST.root;
				controlStmt_AST = (Block)astFactory.make( (new ASTArray(2)).add((Block)astFactory.create(NBreak)).add(controlStmt_AST));
				currentAST.root = controlStmt_AST;
				currentAST.child = controlStmt_AST!=null &&controlStmt_AST.getFirstChild()!=null ?
					controlStmt_AST.getFirstChild() : controlStmt_AST;
				currentAST.advanceChildToEnd();
			}
			controlStmt_AST = (Block)currentAST.root;
			break;
		}
		case LITERAL_return:
		{
			r = LT(1);
			r_AST = (Block)astFactory.create(r);
			match(LITERAL_return);
			{
			switch ( LA(1)) {
			case ID:
			case STAR:
			case LPAREN:
			case StringLiteral:
			case BAND:
			case PLUS:
			case MINUS:
			case LITERAL_sizeof:
			case BNOT:
			case LNOT:
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
			{
				pureExpr();
				astFactory.addASTChild(currentAST, returnAST);
				break;
			}
			case SEMI:
			{
				break;
			}
			default:
			{
				throw new NoViableAltException(LT(1), getFilename());
			}
			}
			}
			match(SEMI);
			if ( inputState.guessing==0 ) {
				controlStmt_AST = (Block)currentAST.root;
				
				controlStmt_AST = (Block)astFactory.make( (new ASTArray(2)).add((Block)astFactory.create(NReturn)).add(controlStmt_AST));
				controlStmt_AST.setLineNum (r.getLine ());
				
				currentAST.root = controlStmt_AST;
				currentAST.child = controlStmt_AST!=null &&controlStmt_AST.getFirstChild()!=null ?
					controlStmt_AST.getFirstChild() : controlStmt_AST;
				currentAST.advanceChildToEnd();
			}
			controlStmt_AST = (Block)currentAST.root;
			break;
		}
		case LITERAL_if:
		{
			match(LITERAL_if);
			match(LPAREN);
			pureExpr();
			astFactory.addASTChild(currentAST, returnAST);
			match(RPAREN);
			stmt();
			astFactory.addASTChild(currentAST, returnAST);
			{
			if ((LA(1)==LITERAL_else) && (_tokenSet_28.member(LA(2)))) {
				match(LITERAL_else);
				stmt();
				astFactory.addASTChild(currentAST, returnAST);
			}
			else if ((_tokenSet_35.member(LA(1))) && (_tokenSet_27.member(LA(2)))) {
			}
			else {
				throw new NoViableAltException(LT(1), getFilename());
			}
			
			}
			if ( inputState.guessing==0 ) {
				controlStmt_AST = (Block)currentAST.root;
				controlStmt_AST = (Block)astFactory.make( (new ASTArray(2)).add((Block)astFactory.create(NIf)).add(controlStmt_AST));
				currentAST.root = controlStmt_AST;
				currentAST.child = controlStmt_AST!=null &&controlStmt_AST.getFirstChild()!=null ?
					controlStmt_AST.getFirstChild() : controlStmt_AST;
				currentAST.advanceChildToEnd();
			}
			controlStmt_AST = (Block)currentAST.root;
			break;
		}
		case LITERAL___nd_goto:
		{
			match(LITERAL___nd_goto);
			match(LPAREN);
			stringList();
			astFactory.addASTChild(currentAST, returnAST);
			match(RPAREN);
			match(SEMI);
			if ( inputState.guessing==0 ) {
				controlStmt_AST = (Block)currentAST.root;
				controlStmt_AST = (Block)astFactory.make( (new ASTArray(2)).add((Block)astFactory.create(NNDGoto)).add(controlStmt_AST));
				currentAST.root = controlStmt_AST;
				currentAST.child = controlStmt_AST!=null &&controlStmt_AST.getFirstChild()!=null ?
					controlStmt_AST.getFirstChild() : controlStmt_AST;
				currentAST.advanceChildToEnd();
			}
			controlStmt_AST = (Block)currentAST.root;
			break;
		}
		default:
		{
			throw new NoViableAltException(LT(1), getFilename());
		}
		}
		returnAST = controlStmt_AST;
	}
	
	public final void strOptExprPair() throws RecognitionException, TokenStreamException {
		
		returnAST = null;
		ASTPair currentAST = new ASTPair();
		Block strOptExprPair_AST = null;
		
		stringConst();
		astFactory.addASTChild(currentAST, returnAST);
		{
		switch ( LA(1)) {
		case LPAREN:
		{
			Block tmp113_AST = null;
			tmp113_AST = (Block)astFactory.create(LT(1));
			astFactory.addASTChild(currentAST, tmp113_AST);
			match(LPAREN);
			pureExpr();
			astFactory.addASTChild(currentAST, returnAST);
			Block tmp114_AST = null;
			tmp114_AST = (Block)astFactory.create(LT(1));
			astFactory.addASTChild(currentAST, tmp114_AST);
			match(RPAREN);
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
			throw new NoViableAltException(LT(1), getFilename());
		}
		}
		}
		strOptExprPair_AST = (Block)currentAST.root;
		returnAST = strOptExprPair_AST;
	}
	
	public final void stringList() throws RecognitionException, TokenStreamException {
		
		returnAST = null;
		ASTPair currentAST = new ASTPair();
		Block stringList_AST = null;
		
		Block tmp115_AST = null;
		tmp115_AST = (Block)astFactory.create(LT(1));
		astFactory.addASTChild(currentAST, tmp115_AST);
		match(StringLiteral);
		{
		_loop149:
		do {
			if ((LA(1)==COMMA)) {
				match(COMMA);
				Block tmp117_AST = null;
				tmp117_AST = (Block)astFactory.create(LT(1));
				astFactory.addASTChild(currentAST, tmp117_AST);
				match(StringLiteral);
			}
			else {
				break _loop149;
			}
			
		} while (true);
		}
		stringList_AST = (Block)currentAST.root;
		returnAST = stringList_AST;
	}
	
	public final void functionCall() throws RecognitionException, TokenStreamException {
		
		returnAST = null;
		ASTPair currentAST = new ASTPair();
		Block functionCall_AST = null;
		Token  id = null;
		Block id_AST = null;
		Block a_AST = null;
		
		id = LT(1);
		id_AST = (Block)astFactory.create(id);
		astFactory.addASTChild(currentAST, id_AST);
		match(ID);
		match(LPAREN);
		{
		switch ( LA(1)) {
		case ID:
		case STAR:
		case LPAREN:
		case StringLiteral:
		case BAND:
		case PLUS:
		case MINUS:
		case LITERAL_sizeof:
		case BNOT:
		case LNOT:
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
		{
			argExprList();
			a_AST = (Block)returnAST;
			astFactory.addASTChild(currentAST, returnAST);
			break;
		}
		case RPAREN:
		{
			break;
		}
		default:
		{
			throw new NoViableAltException(LT(1), getFilename());
		}
		}
		}
		match(RPAREN);
		if ( inputState.guessing==0 ) {
			functionCall_AST = (Block)currentAST.root;
			
			// Increment call site counter in function definition Block
			Block functionDefBlock =
			(Block) symbolTable.lookupNameInCurrentScope (id.getText ());
			
			if (functionDefBlock == null)
			throw new
			RuntimeException ("Unable to find definition of function " +
			"(must declare before use): " +
			id.getText ());
			
			functionDefBlock.setNumCallSites
			(functionDefBlock.getNumCallSites () + 1);
			functionCall_AST = (Block)astFactory.make( (new ASTArray(2)).add((Block)astFactory.create(NFunctionCall)).add(functionCall_AST));
			// XXX 0-based
			functionCall_AST.setCallIndex (functionDefBlock.getNumCallSites () - 1);
			
			currentAST.root = functionCall_AST;
			currentAST.child = functionCall_AST!=null &&functionCall_AST.getFirstChild()!=null ?
				functionCall_AST.getFirstChild() : functionCall_AST;
			currentAST.advanceChildToEnd();
		}
		functionCall_AST = (Block)currentAST.root;
		returnAST = functionCall_AST;
	}
	
	public final void lval() throws RecognitionException, TokenStreamException {
		
		returnAST = null;
		ASTPair currentAST = new ASTPair();
		Block lval_AST = null;
		Token  id = null;
		Block id_AST = null;
		
		switch ( LA(1)) {
		case ID:
		{
			id = LT(1);
			id_AST = (Block)astFactory.create(id);
			astFactory.addASTChild(currentAST, id_AST);
			match(ID);
			{
			if ((_tokenSet_36.member(LA(1))) && (_tokenSet_31.member(LA(2)))) {
				lvalSuffixes();
				astFactory.addASTChild(currentAST, returnAST);
				if ( inputState.guessing==0 ) {
					lval_AST = (Block)currentAST.root;
					lval_AST = (Block)astFactory.make( (new ASTArray(2)).add((Block)astFactory.create(NPostfixExpr)).add(lval_AST));
					currentAST.root = lval_AST;
					currentAST.child = lval_AST!=null &&lval_AST.getFirstChild()!=null ?
						lval_AST.getFirstChild() : lval_AST;
					currentAST.advanceChildToEnd();
				}
			}
			else if ((_tokenSet_37.member(LA(1))) && (_tokenSet_38.member(LA(2)))) {
			}
			else {
				throw new NoViableAltException(LT(1), getFilename());
			}
			
			}
			if ( inputState.guessing==0 ) {
				
				TNode declaration = symbolTable.lookupNameInCurrentScope(id.getText ());
				
				// declaration will be null if id is a struct field name (XXX really?)
				if (declaration != null)
				// -- replace text with fully scoped name
				id_AST.setText (declaration.getFirstChild ().
				getNextSibling ().
				getFirstChild ().getText ());
				
			}
			lval_AST = (Block)currentAST.root;
			break;
		}
		case STAR:
		{
			pointerGroup();
			astFactory.addASTChild(currentAST, returnAST);
			pureExpr();
			astFactory.addASTChild(currentAST, returnAST);
			lval_AST = (Block)currentAST.root;
			break;
		}
		case LPAREN:
		{
			match(LPAREN);
			lval();
			astFactory.addASTChild(currentAST, returnAST);
			match(RPAREN);
			if ( inputState.guessing==0 ) {
				lval_AST = (Block)currentAST.root;
				lval_AST = (Block)astFactory.make( (new ASTArray(2)).add((Block)astFactory.create(NPureExpressionGroup)).add(lval_AST));
				currentAST.root = lval_AST;
				currentAST.child = lval_AST!=null &&lval_AST.getFirstChild()!=null ?
					lval_AST.getFirstChild() : lval_AST;
				currentAST.advanceChildToEnd();
			}
			lval_AST = (Block)currentAST.root;
			break;
		}
		default:
		{
			throw new NoViableAltException(LT(1), getFilename());
		}
		}
		returnAST = lval_AST;
	}
	
	public final void implicitAssignStmt() throws RecognitionException, TokenStreamException {
		
		returnAST = null;
		ASTPair currentAST = new ASTPair();
		Block implicitAssignStmt_AST = null;
		
		lval();
		astFactory.addASTChild(currentAST, returnAST);
		{
		switch ( LA(1)) {
		case INC:
		{
			Block tmp122_AST = null;
			tmp122_AST = (Block)astFactory.create(LT(1));
			astFactory.addASTChild(currentAST, tmp122_AST);
			match(INC);
			break;
		}
		case DEC:
		{
			Block tmp123_AST = null;
			tmp123_AST = (Block)astFactory.create(LT(1));
			astFactory.addASTChild(currentAST, tmp123_AST);
			match(DEC);
			break;
		}
		default:
		{
			throw new NoViableAltException(LT(1), getFilename());
		}
		}
		}
		match(SEMI);
		if ( inputState.guessing==0 ) {
			implicitAssignStmt_AST = (Block)currentAST.root;
			implicitAssignStmt_AST = (Block)astFactory.make( (new ASTArray(2)).add((Block)astFactory.create(NImplicitAssignStmt)).add(implicitAssignStmt_AST));
			currentAST.root = implicitAssignStmt_AST;
			currentAST.child = implicitAssignStmt_AST!=null &&implicitAssignStmt_AST.getFirstChild()!=null ?
				implicitAssignStmt_AST.getFirstChild() : implicitAssignStmt_AST;
			currentAST.advanceChildToEnd();
		}
		implicitAssignStmt_AST = (Block)currentAST.root;
		returnAST = implicitAssignStmt_AST;
	}
	
	public final void lvalSuffixes() throws RecognitionException, TokenStreamException {
		
		returnAST = null;
		ASTPair currentAST = new ASTPair();
		Block lvalSuffixes_AST = null;
		
		{
		int _cnt160=0;
		_loop160:
		do {
			if ((_tokenSet_36.member(LA(1))) && (_tokenSet_31.member(LA(2)))) {
				lvalSuffix();
				astFactory.addASTChild(currentAST, returnAST);
			}
			else {
				if ( _cnt160>=1 ) { break _loop160; } else {throw new NoViableAltException(LT(1), getFilename());}
			}
			
			_cnt160++;
		} while (true);
		}
		lvalSuffixes_AST = (Block)currentAST.root;
		returnAST = lvalSuffixes_AST;
	}
	
	public final void primaryLval() throws RecognitionException, TokenStreamException {
		
		returnAST = null;
		ASTPair currentAST = new ASTPair();
		Block primaryLval_AST = null;
		
		Block tmp125_AST = null;
		tmp125_AST = (Block)astFactory.create(LT(1));
		astFactory.addASTChild(currentAST, tmp125_AST);
		match(ID);
		primaryLval_AST = (Block)currentAST.root;
		returnAST = primaryLval_AST;
	}
	
	public final void lvalSuffix() throws RecognitionException, TokenStreamException {
		
		returnAST = null;
		ASTPair currentAST = new ASTPair();
		Block lvalSuffix_AST = null;
		Block e_AST = null;
		
		switch ( LA(1)) {
		case LBRACKET:
		{
			match(LBRACKET);
			pureExpr();
			e_AST = (Block)returnAST;
			astFactory.addASTChild(currentAST, returnAST);
			match(RBRACKET);
			if ( inputState.guessing==0 ) {
				lvalSuffix_AST = (Block)currentAST.root;
				lvalSuffix_AST = (Block)astFactory.make( (new ASTArray(2)).add((Block)astFactory.create(NArrayIndex)).add(e_AST));
				currentAST.root = lvalSuffix_AST;
				currentAST.child = lvalSuffix_AST!=null &&lvalSuffix_AST.getFirstChild()!=null ?
					lvalSuffix_AST.getFirstChild() : lvalSuffix_AST;
				currentAST.advanceChildToEnd();
			}
			lvalSuffix_AST = (Block)currentAST.root;
			break;
		}
		case PTR:
		case DOT:
		{
			{
			switch ( LA(1)) {
			case PTR:
			{
				Block tmp128_AST = null;
				tmp128_AST = (Block)astFactory.create(LT(1));
				astFactory.addASTChild(currentAST, tmp128_AST);
				match(PTR);
				break;
			}
			case DOT:
			{
				Block tmp129_AST = null;
				tmp129_AST = (Block)astFactory.create(LT(1));
				astFactory.addASTChild(currentAST, tmp129_AST);
				match(DOT);
				break;
			}
			default:
			{
				throw new NoViableAltException(LT(1), getFilename());
			}
			}
			}
			Block tmp130_AST = null;
			tmp130_AST = (Block)astFactory.create(LT(1));
			astFactory.addASTChild(currentAST, tmp130_AST);
			match(ID);
			lvalSuffix_AST = (Block)currentAST.root;
			break;
		}
		default:
		{
			throw new NoViableAltException(LT(1), getFilename());
		}
		}
		returnAST = lvalSuffix_AST;
	}
	
	public final void pureLogicalOrExpr() throws RecognitionException, TokenStreamException {
		
		returnAST = null;
		ASTPair currentAST = new ASTPair();
		Block pureLogicalOrExpr_AST = null;
		
		pureLogicalAndExpr();
		astFactory.addASTChild(currentAST, returnAST);
		{
		_loop166:
		do {
			if ((LA(1)==LOR) && (_tokenSet_31.member(LA(2)))) {
				Block tmp131_AST = null;
				tmp131_AST = (Block)astFactory.create(LT(1));
				astFactory.makeASTRoot(currentAST, tmp131_AST);
				match(LOR);
				pureLogicalAndExpr();
				astFactory.addASTChild(currentAST, returnAST);
			}
			else {
				break _loop166;
			}
			
		} while (true);
		}
		pureLogicalOrExpr_AST = (Block)currentAST.root;
		returnAST = pureLogicalOrExpr_AST;
	}
	
	public final void pureLogicalAndExpr() throws RecognitionException, TokenStreamException {
		
		returnAST = null;
		ASTPair currentAST = new ASTPair();
		Block pureLogicalAndExpr_AST = null;
		
		pureInclusiveOrExpr();
		astFactory.addASTChild(currentAST, returnAST);
		{
		_loop169:
		do {
			if ((LA(1)==LAND) && (_tokenSet_31.member(LA(2)))) {
				Block tmp132_AST = null;
				tmp132_AST = (Block)astFactory.create(LT(1));
				astFactory.makeASTRoot(currentAST, tmp132_AST);
				match(LAND);
				pureInclusiveOrExpr();
				astFactory.addASTChild(currentAST, returnAST);
			}
			else {
				break _loop169;
			}
			
		} while (true);
		}
		pureLogicalAndExpr_AST = (Block)currentAST.root;
		returnAST = pureLogicalAndExpr_AST;
	}
	
	public final void pureInclusiveOrExpr() throws RecognitionException, TokenStreamException {
		
		returnAST = null;
		ASTPair currentAST = new ASTPair();
		Block pureInclusiveOrExpr_AST = null;
		
		pureExclusiveOrExpr();
		astFactory.addASTChild(currentAST, returnAST);
		{
		_loop172:
		do {
			if ((LA(1)==BOR) && (_tokenSet_31.member(LA(2)))) {
				Block tmp133_AST = null;
				tmp133_AST = (Block)astFactory.create(LT(1));
				astFactory.makeASTRoot(currentAST, tmp133_AST);
				match(BOR);
				pureExclusiveOrExpr();
				astFactory.addASTChild(currentAST, returnAST);
			}
			else {
				break _loop172;
			}
			
		} while (true);
		}
		pureInclusiveOrExpr_AST = (Block)currentAST.root;
		returnAST = pureInclusiveOrExpr_AST;
	}
	
	public final void pureExclusiveOrExpr() throws RecognitionException, TokenStreamException {
		
		returnAST = null;
		ASTPair currentAST = new ASTPair();
		Block pureExclusiveOrExpr_AST = null;
		
		pureBitAndExpr();
		astFactory.addASTChild(currentAST, returnAST);
		{
		_loop175:
		do {
			if ((LA(1)==BXOR) && (_tokenSet_31.member(LA(2)))) {
				Block tmp134_AST = null;
				tmp134_AST = (Block)astFactory.create(LT(1));
				astFactory.makeASTRoot(currentAST, tmp134_AST);
				match(BXOR);
				pureBitAndExpr();
				astFactory.addASTChild(currentAST, returnAST);
			}
			else {
				break _loop175;
			}
			
		} while (true);
		}
		pureExclusiveOrExpr_AST = (Block)currentAST.root;
		returnAST = pureExclusiveOrExpr_AST;
	}
	
	public final void pureBitAndExpr() throws RecognitionException, TokenStreamException {
		
		returnAST = null;
		ASTPair currentAST = new ASTPair();
		Block pureBitAndExpr_AST = null;
		
		pureEqualityExpr();
		astFactory.addASTChild(currentAST, returnAST);
		{
		_loop178:
		do {
			if ((LA(1)==BAND) && (_tokenSet_31.member(LA(2)))) {
				Block tmp135_AST = null;
				tmp135_AST = (Block)astFactory.create(LT(1));
				astFactory.makeASTRoot(currentAST, tmp135_AST);
				match(BAND);
				pureEqualityExpr();
				astFactory.addASTChild(currentAST, returnAST);
			}
			else {
				break _loop178;
			}
			
		} while (true);
		}
		pureBitAndExpr_AST = (Block)currentAST.root;
		returnAST = pureBitAndExpr_AST;
	}
	
	public final void pureEqualityExpr() throws RecognitionException, TokenStreamException {
		
		returnAST = null;
		ASTPair currentAST = new ASTPair();
		Block pureEqualityExpr_AST = null;
		
		pureRelationalExpr();
		astFactory.addASTChild(currentAST, returnAST);
		{
		_loop182:
		do {
			if ((LA(1)==EQUAL||LA(1)==NOT_EQUAL) && (_tokenSet_31.member(LA(2)))) {
				{
				switch ( LA(1)) {
				case EQUAL:
				{
					Block tmp136_AST = null;
					tmp136_AST = (Block)astFactory.create(LT(1));
					astFactory.makeASTRoot(currentAST, tmp136_AST);
					match(EQUAL);
					break;
				}
				case NOT_EQUAL:
				{
					Block tmp137_AST = null;
					tmp137_AST = (Block)astFactory.create(LT(1));
					astFactory.makeASTRoot(currentAST, tmp137_AST);
					match(NOT_EQUAL);
					break;
				}
				default:
				{
					throw new NoViableAltException(LT(1), getFilename());
				}
				}
				}
				pureRelationalExpr();
				astFactory.addASTChild(currentAST, returnAST);
			}
			else {
				break _loop182;
			}
			
		} while (true);
		}
		pureEqualityExpr_AST = (Block)currentAST.root;
		returnAST = pureEqualityExpr_AST;
	}
	
	public final void pureRelationalExpr() throws RecognitionException, TokenStreamException {
		
		returnAST = null;
		ASTPair currentAST = new ASTPair();
		Block pureRelationalExpr_AST = null;
		
		pureShiftExpr();
		astFactory.addASTChild(currentAST, returnAST);
		{
		_loop186:
		do {
			if (((LA(1) >= LT && LA(1) <= GTE)) && (_tokenSet_31.member(LA(2)))) {
				{
				switch ( LA(1)) {
				case LT:
				{
					Block tmp138_AST = null;
					tmp138_AST = (Block)astFactory.create(LT(1));
					astFactory.makeASTRoot(currentAST, tmp138_AST);
					match(LT);
					break;
				}
				case LTE:
				{
					Block tmp139_AST = null;
					tmp139_AST = (Block)astFactory.create(LT(1));
					astFactory.makeASTRoot(currentAST, tmp139_AST);
					match(LTE);
					break;
				}
				case GT:
				{
					Block tmp140_AST = null;
					tmp140_AST = (Block)astFactory.create(LT(1));
					astFactory.makeASTRoot(currentAST, tmp140_AST);
					match(GT);
					break;
				}
				case GTE:
				{
					Block tmp141_AST = null;
					tmp141_AST = (Block)astFactory.create(LT(1));
					astFactory.makeASTRoot(currentAST, tmp141_AST);
					match(GTE);
					break;
				}
				default:
				{
					throw new NoViableAltException(LT(1), getFilename());
				}
				}
				}
				pureShiftExpr();
				astFactory.addASTChild(currentAST, returnAST);
			}
			else {
				break _loop186;
			}
			
		} while (true);
		}
		pureRelationalExpr_AST = (Block)currentAST.root;
		returnAST = pureRelationalExpr_AST;
	}
	
	public final void pureShiftExpr() throws RecognitionException, TokenStreamException {
		
		returnAST = null;
		ASTPair currentAST = new ASTPair();
		Block pureShiftExpr_AST = null;
		
		pureAdditiveExpr();
		astFactory.addASTChild(currentAST, returnAST);
		{
		_loop190:
		do {
			if ((LA(1)==LSHIFT||LA(1)==RSHIFT) && (_tokenSet_31.member(LA(2)))) {
				{
				switch ( LA(1)) {
				case LSHIFT:
				{
					Block tmp142_AST = null;
					tmp142_AST = (Block)astFactory.create(LT(1));
					astFactory.makeASTRoot(currentAST, tmp142_AST);
					match(LSHIFT);
					break;
				}
				case RSHIFT:
				{
					Block tmp143_AST = null;
					tmp143_AST = (Block)astFactory.create(LT(1));
					astFactory.makeASTRoot(currentAST, tmp143_AST);
					match(RSHIFT);
					break;
				}
				default:
				{
					throw new NoViableAltException(LT(1), getFilename());
				}
				}
				}
				pureAdditiveExpr();
				astFactory.addASTChild(currentAST, returnAST);
			}
			else {
				break _loop190;
			}
			
		} while (true);
		}
		pureShiftExpr_AST = (Block)currentAST.root;
		returnAST = pureShiftExpr_AST;
	}
	
	public final void pureAdditiveExpr() throws RecognitionException, TokenStreamException {
		
		returnAST = null;
		ASTPair currentAST = new ASTPair();
		Block pureAdditiveExpr_AST = null;
		
		pureMultExpr();
		astFactory.addASTChild(currentAST, returnAST);
		{
		_loop194:
		do {
			if ((LA(1)==PLUS||LA(1)==MINUS) && (_tokenSet_31.member(LA(2)))) {
				{
				switch ( LA(1)) {
				case PLUS:
				{
					Block tmp144_AST = null;
					tmp144_AST = (Block)astFactory.create(LT(1));
					astFactory.makeASTRoot(currentAST, tmp144_AST);
					match(PLUS);
					break;
				}
				case MINUS:
				{
					Block tmp145_AST = null;
					tmp145_AST = (Block)astFactory.create(LT(1));
					astFactory.makeASTRoot(currentAST, tmp145_AST);
					match(MINUS);
					break;
				}
				default:
				{
					throw new NoViableAltException(LT(1), getFilename());
				}
				}
				}
				pureMultExpr();
				astFactory.addASTChild(currentAST, returnAST);
			}
			else {
				break _loop194;
			}
			
		} while (true);
		}
		pureAdditiveExpr_AST = (Block)currentAST.root;
		returnAST = pureAdditiveExpr_AST;
	}
	
	public final void pureMultExpr() throws RecognitionException, TokenStreamException {
		
		returnAST = null;
		ASTPair currentAST = new ASTPair();
		Block pureMultExpr_AST = null;
		
		pureCastExpr();
		astFactory.addASTChild(currentAST, returnAST);
		{
		_loop198:
		do {
			if ((_tokenSet_39.member(LA(1))) && (_tokenSet_31.member(LA(2)))) {
				{
				switch ( LA(1)) {
				case STAR:
				{
					Block tmp146_AST = null;
					tmp146_AST = (Block)astFactory.create(LT(1));
					astFactory.makeASTRoot(currentAST, tmp146_AST);
					match(STAR);
					break;
				}
				case DIV:
				{
					Block tmp147_AST = null;
					tmp147_AST = (Block)astFactory.create(LT(1));
					astFactory.makeASTRoot(currentAST, tmp147_AST);
					match(DIV);
					break;
				}
				case MOD:
				{
					Block tmp148_AST = null;
					tmp148_AST = (Block)astFactory.create(LT(1));
					astFactory.makeASTRoot(currentAST, tmp148_AST);
					match(MOD);
					break;
				}
				default:
				{
					throw new NoViableAltException(LT(1), getFilename());
				}
				}
				}
				pureCastExpr();
				astFactory.addASTChild(currentAST, returnAST);
			}
			else {
				break _loop198;
			}
			
		} while (true);
		}
		pureMultExpr_AST = (Block)currentAST.root;
		returnAST = pureMultExpr_AST;
	}
	
	public final void pureCastExpr() throws RecognitionException, TokenStreamException {
		
		returnAST = null;
		ASTPair currentAST = new ASTPair();
		Block pureCastExpr_AST = null;
		
		boolean synPredMatched201 = false;
		if (((LA(1)==LPAREN) && (_tokenSet_13.member(LA(2))))) {
			int _m201 = mark();
			synPredMatched201 = true;
			inputState.guessing++;
			try {
				{
				match(LPAREN);
				typeName();
				match(RPAREN);
				}
			}
			catch (RecognitionException pe) {
				synPredMatched201 = false;
			}
			rewind(_m201);
			inputState.guessing--;
		}
		if ( synPredMatched201 ) {
			match(LPAREN);
			typeName();
			astFactory.addASTChild(currentAST, returnAST);
			Block tmp150_AST = null;
			tmp150_AST = (Block)astFactory.create(LT(1));
			astFactory.addASTChild(currentAST, tmp150_AST);
			match(RPAREN);
			{
			pureExpr();
			astFactory.addASTChild(currentAST, returnAST);
			}
			if ( inputState.guessing==0 ) {
				pureCastExpr_AST = (Block)currentAST.root;
				pureCastExpr_AST = (Block)astFactory.make( (new ASTArray(2)).add((Block)astFactory.create(NCast,"(")).add(pureCastExpr_AST));
				currentAST.root = pureCastExpr_AST;
				currentAST.child = pureCastExpr_AST!=null &&pureCastExpr_AST.getFirstChild()!=null ?
					pureCastExpr_AST.getFirstChild() : pureCastExpr_AST;
				currentAST.advanceChildToEnd();
			}
			pureCastExpr_AST = (Block)currentAST.root;
		}
		else if ((_tokenSet_31.member(LA(1))) && (_tokenSet_40.member(LA(2)))) {
			pureUnaryExpr();
			astFactory.addASTChild(currentAST, returnAST);
			pureCastExpr_AST = (Block)currentAST.root;
		}
		else {
			throw new NoViableAltException(LT(1), getFilename());
		}
		
		returnAST = pureCastExpr_AST;
	}
	
	public final void typeName() throws RecognitionException, TokenStreamException {
		
		returnAST = null;
		ASTPair currentAST = new ASTPair();
		Block typeName_AST = null;
		
		specifierQualifierList();
		astFactory.addASTChild(currentAST, returnAST);
		{
		switch ( LA(1)) {
		case STAR:
		case LPAREN:
		case LBRACKET:
		{
			nonemptyAbstractDeclarator();
			astFactory.addASTChild(currentAST, returnAST);
			break;
		}
		case RPAREN:
		{
			break;
		}
		default:
		{
			throw new NoViableAltException(LT(1), getFilename());
		}
		}
		}
		typeName_AST = (Block)currentAST.root;
		returnAST = typeName_AST;
	}
	
	public final void pureUnaryExpr() throws RecognitionException, TokenStreamException {
		
		returnAST = null;
		ASTPair currentAST = new ASTPair();
		Block pureUnaryExpr_AST = null;
		Block u_AST = null;
		
		switch ( LA(1)) {
		case ID:
		case STAR:
		case LPAREN:
		case StringLiteral:
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
		{
			purePrimaryExpr();
			astFactory.addASTChild(currentAST, returnAST);
			pureUnaryExpr_AST = (Block)currentAST.root;
			break;
		}
		case BAND:
		case PLUS:
		case MINUS:
		case BNOT:
		case LNOT:
		{
			unaryOperator();
			u_AST = (Block)returnAST;
			astFactory.addASTChild(currentAST, returnAST);
			pureCastExpr();
			astFactory.addASTChild(currentAST, returnAST);
			if ( inputState.guessing==0 ) {
				pureUnaryExpr_AST = (Block)currentAST.root;
				pureUnaryExpr_AST = (Block)astFactory.make( (new ASTArray(2)).add((Block)astFactory.create(NUnaryExpr)).add(pureUnaryExpr_AST));
				currentAST.root = pureUnaryExpr_AST;
				currentAST.child = pureUnaryExpr_AST!=null &&pureUnaryExpr_AST.getFirstChild()!=null ?
					pureUnaryExpr_AST.getFirstChild() : pureUnaryExpr_AST;
				currentAST.advanceChildToEnd();
			}
			pureUnaryExpr_AST = (Block)currentAST.root;
			break;
		}
		case LITERAL_sizeof:
		{
			Block tmp151_AST = null;
			tmp151_AST = (Block)astFactory.create(LT(1));
			astFactory.makeASTRoot(currentAST, tmp151_AST);
			match(LITERAL_sizeof);
			{
			boolean synPredMatched206 = false;
			if (((LA(1)==LPAREN) && (_tokenSet_13.member(LA(2))))) {
				int _m206 = mark();
				synPredMatched206 = true;
				inputState.guessing++;
				try {
					{
					match(LPAREN);
					typeName();
					}
				}
				catch (RecognitionException pe) {
					synPredMatched206 = false;
				}
				rewind(_m206);
				inputState.guessing--;
			}
			if ( synPredMatched206 ) {
				Block tmp152_AST = null;
				tmp152_AST = (Block)astFactory.create(LT(1));
				astFactory.addASTChild(currentAST, tmp152_AST);
				match(LPAREN);
				typeName();
				astFactory.addASTChild(currentAST, returnAST);
				Block tmp153_AST = null;
				tmp153_AST = (Block)astFactory.create(LT(1));
				astFactory.addASTChild(currentAST, tmp153_AST);
				match(RPAREN);
			}
			else if ((_tokenSet_31.member(LA(1))) && (_tokenSet_40.member(LA(2)))) {
				pureUnaryExpr();
				astFactory.addASTChild(currentAST, returnAST);
			}
			else {
				throw new NoViableAltException(LT(1), getFilename());
			}
			
			}
			pureUnaryExpr_AST = (Block)currentAST.root;
			break;
		}
		default:
		{
			throw new NoViableAltException(LT(1), getFilename());
		}
		}
		returnAST = pureUnaryExpr_AST;
	}
	
	public final void purePrimaryExpr() throws RecognitionException, TokenStreamException {
		
		returnAST = null;
		ASTPair currentAST = new ASTPair();
		Block purePrimaryExpr_AST = null;
		
		switch ( LA(1)) {
		case CharLiteral:
		{
			charConst();
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
			intConst();
			astFactory.addASTChild(currentAST, returnAST);
			purePrimaryExpr_AST = (Block)currentAST.root;
			break;
		}
		case FloatDoubleConst:
		case DoubleDoubleConst:
		case LongDoubleConst:
		{
			floatConst();
			astFactory.addASTChild(currentAST, returnAST);
			purePrimaryExpr_AST = (Block)currentAST.root;
			break;
		}
		case StringLiteral:
		{
			stringConst();
			astFactory.addASTChild(currentAST, returnAST);
			purePrimaryExpr_AST = (Block)currentAST.root;
			break;
		}
		default:
			if ((LA(1)==LPAREN) && (_tokenSet_31.member(LA(2)))) {
				match(LPAREN);
				pureExpr();
				astFactory.addASTChild(currentAST, returnAST);
				match(RPAREN);
				if ( inputState.guessing==0 ) {
					purePrimaryExpr_AST = (Block)currentAST.root;
					purePrimaryExpr_AST = (Block)astFactory.make( (new ASTArray(2)).add((Block)astFactory.create(NPureExpressionGroup,"(")).add(purePrimaryExpr_AST));
					currentAST.root = purePrimaryExpr_AST;
					currentAST.child = purePrimaryExpr_AST!=null &&purePrimaryExpr_AST.getFirstChild()!=null ?
						purePrimaryExpr_AST.getFirstChild() : purePrimaryExpr_AST;
					currentAST.advanceChildToEnd();
				}
				purePrimaryExpr_AST = (Block)currentAST.root;
			}
			else if ((_tokenSet_18.member(LA(1))) && (_tokenSet_40.member(LA(2)))) {
				lval();
				astFactory.addASTChild(currentAST, returnAST);
				purePrimaryExpr_AST = (Block)currentAST.root;
			}
		else {
			throw new NoViableAltException(LT(1), getFilename());
		}
		}
		returnAST = purePrimaryExpr_AST;
	}
	
	public final void unaryOperator() throws RecognitionException, TokenStreamException {
		
		returnAST = null;
		ASTPair currentAST = new ASTPair();
		Block unaryOperator_AST = null;
		
		switch ( LA(1)) {
		case BAND:
		{
			Block tmp156_AST = null;
			tmp156_AST = (Block)astFactory.create(LT(1));
			astFactory.addASTChild(currentAST, tmp156_AST);
			match(BAND);
			unaryOperator_AST = (Block)currentAST.root;
			break;
		}
		case PLUS:
		{
			Block tmp157_AST = null;
			tmp157_AST = (Block)astFactory.create(LT(1));
			astFactory.addASTChild(currentAST, tmp157_AST);
			match(PLUS);
			unaryOperator_AST = (Block)currentAST.root;
			break;
		}
		case MINUS:
		{
			Block tmp158_AST = null;
			tmp158_AST = (Block)astFactory.create(LT(1));
			astFactory.addASTChild(currentAST, tmp158_AST);
			match(MINUS);
			unaryOperator_AST = (Block)currentAST.root;
			break;
		}
		case BNOT:
		{
			Block tmp159_AST = null;
			tmp159_AST = (Block)astFactory.create(LT(1));
			astFactory.addASTChild(currentAST, tmp159_AST);
			match(BNOT);
			unaryOperator_AST = (Block)currentAST.root;
			break;
		}
		case LNOT:
		{
			Block tmp160_AST = null;
			tmp160_AST = (Block)astFactory.create(LT(1));
			astFactory.addASTChild(currentAST, tmp160_AST);
			match(LNOT);
			unaryOperator_AST = (Block)currentAST.root;
			break;
		}
		default:
		{
			throw new NoViableAltException(LT(1), getFilename());
		}
		}
		returnAST = unaryOperator_AST;
	}
	
	protected final void charConst() throws RecognitionException, TokenStreamException {
		
		returnAST = null;
		ASTPair currentAST = new ASTPair();
		Block charConst_AST = null;
		
		Block tmp161_AST = null;
		tmp161_AST = (Block)astFactory.create(LT(1));
		astFactory.addASTChild(currentAST, tmp161_AST);
		match(CharLiteral);
		charConst_AST = (Block)currentAST.root;
		returnAST = charConst_AST;
	}
	
	protected final void intConst() throws RecognitionException, TokenStreamException {
		
		returnAST = null;
		ASTPair currentAST = new ASTPair();
		Block intConst_AST = null;
		
		switch ( LA(1)) {
		case IntOctalConst:
		{
			Block tmp162_AST = null;
			tmp162_AST = (Block)astFactory.create(LT(1));
			astFactory.addASTChild(currentAST, tmp162_AST);
			match(IntOctalConst);
			intConst_AST = (Block)currentAST.root;
			break;
		}
		case LongOctalConst:
		{
			Block tmp163_AST = null;
			tmp163_AST = (Block)astFactory.create(LT(1));
			astFactory.addASTChild(currentAST, tmp163_AST);
			match(LongOctalConst);
			intConst_AST = (Block)currentAST.root;
			break;
		}
		case UnsignedOctalConst:
		{
			Block tmp164_AST = null;
			tmp164_AST = (Block)astFactory.create(LT(1));
			astFactory.addASTChild(currentAST, tmp164_AST);
			match(UnsignedOctalConst);
			intConst_AST = (Block)currentAST.root;
			break;
		}
		case IntIntConst:
		{
			Block tmp165_AST = null;
			tmp165_AST = (Block)astFactory.create(LT(1));
			astFactory.addASTChild(currentAST, tmp165_AST);
			match(IntIntConst);
			intConst_AST = (Block)currentAST.root;
			break;
		}
		case LongIntConst:
		{
			Block tmp166_AST = null;
			tmp166_AST = (Block)astFactory.create(LT(1));
			astFactory.addASTChild(currentAST, tmp166_AST);
			match(LongIntConst);
			intConst_AST = (Block)currentAST.root;
			break;
		}
		case UnsignedIntConst:
		{
			Block tmp167_AST = null;
			tmp167_AST = (Block)astFactory.create(LT(1));
			astFactory.addASTChild(currentAST, tmp167_AST);
			match(UnsignedIntConst);
			intConst_AST = (Block)currentAST.root;
			break;
		}
		case IntHexConst:
		{
			Block tmp168_AST = null;
			tmp168_AST = (Block)astFactory.create(LT(1));
			astFactory.addASTChild(currentAST, tmp168_AST);
			match(IntHexConst);
			intConst_AST = (Block)currentAST.root;
			break;
		}
		case LongHexConst:
		{
			Block tmp169_AST = null;
			tmp169_AST = (Block)astFactory.create(LT(1));
			astFactory.addASTChild(currentAST, tmp169_AST);
			match(LongHexConst);
			intConst_AST = (Block)currentAST.root;
			break;
		}
		case UnsignedHexConst:
		{
			Block tmp170_AST = null;
			tmp170_AST = (Block)astFactory.create(LT(1));
			astFactory.addASTChild(currentAST, tmp170_AST);
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
	
	protected final void floatConst() throws RecognitionException, TokenStreamException {
		
		returnAST = null;
		ASTPair currentAST = new ASTPair();
		Block floatConst_AST = null;
		
		switch ( LA(1)) {
		case FloatDoubleConst:
		{
			Block tmp171_AST = null;
			tmp171_AST = (Block)astFactory.create(LT(1));
			astFactory.addASTChild(currentAST, tmp171_AST);
			match(FloatDoubleConst);
			floatConst_AST = (Block)currentAST.root;
			break;
		}
		case DoubleDoubleConst:
		{
			Block tmp172_AST = null;
			tmp172_AST = (Block)astFactory.create(LT(1));
			astFactory.addASTChild(currentAST, tmp172_AST);
			match(DoubleDoubleConst);
			floatConst_AST = (Block)currentAST.root;
			break;
		}
		case LongDoubleConst:
		{
			Block tmp173_AST = null;
			tmp173_AST = (Block)astFactory.create(LT(1));
			astFactory.addASTChild(currentAST, tmp173_AST);
			match(LongDoubleConst);
			floatConst_AST = (Block)currentAST.root;
			break;
		}
		default:
		{
			throw new NoViableAltException(LT(1), getFilename());
		}
		}
		returnAST = floatConst_AST;
	}
	
	public final void purePostfixExpr() throws RecognitionException, TokenStreamException {
		
		returnAST = null;
		ASTPair currentAST = new ASTPair();
		Block purePostfixExpr_AST = null;
		
		lval();
		astFactory.addASTChild(currentAST, returnAST);
		{
		switch ( LA(1)) {
		case LBRACKET:
		{
			purePostfixSuffix();
			astFactory.addASTChild(currentAST, returnAST);
			if ( inputState.guessing==0 ) {
				purePostfixExpr_AST = (Block)currentAST.root;
				purePostfixExpr_AST = (Block)astFactory.make( (new ASTArray(2)).add((Block)astFactory.create(NPostfixExpr)).add(purePostfixExpr_AST));
				currentAST.root = purePostfixExpr_AST;
				currentAST.child = purePostfixExpr_AST!=null &&purePostfixExpr_AST.getFirstChild()!=null ?
					purePostfixExpr_AST.getFirstChild() : purePostfixExpr_AST;
				currentAST.advanceChildToEnd();
			}
			break;
		}
		case EOF:
		{
			break;
		}
		default:
		{
			throw new NoViableAltException(LT(1), getFilename());
		}
		}
		}
		purePostfixExpr_AST = (Block)currentAST.root;
		returnAST = purePostfixExpr_AST;
	}
	
	public final void purePostfixSuffix() throws RecognitionException, TokenStreamException {
		
		returnAST = null;
		ASTPair currentAST = new ASTPair();
		Block purePostfixSuffix_AST = null;
		
		{
		int _cnt229=0;
		_loop229:
		do {
			if ((LA(1)==LBRACKET)) {
				Block tmp174_AST = null;
				tmp174_AST = (Block)astFactory.create(LT(1));
				astFactory.addASTChild(currentAST, tmp174_AST);
				match(LBRACKET);
				pureExpr();
				astFactory.addASTChild(currentAST, returnAST);
				Block tmp175_AST = null;
				tmp175_AST = (Block)astFactory.create(LT(1));
				astFactory.addASTChild(currentAST, tmp175_AST);
				match(RBRACKET);
			}
			else {
				if ( _cnt229>=1 ) { break _loop229; } else {throw new NoViableAltException(LT(1), getFilename());}
			}
			
			_cnt229++;
		} while (true);
		}
		purePostfixSuffix_AST = (Block)currentAST.root;
		returnAST = purePostfixSuffix_AST;
	}
	
	public final void argExprList() throws RecognitionException, TokenStreamException {
		
		returnAST = null;
		ASTPair currentAST = new ASTPair();
		Block argExprList_AST = null;
		
		pureExpr();
		astFactory.addASTChild(currentAST, returnAST);
		{
		_loop234:
		do {
			if ((LA(1)==COMMA)) {
				match(COMMA);
				pureExpr();
				astFactory.addASTChild(currentAST, returnAST);
			}
			else {
				break _loop234;
			}
			
		} while (true);
		}
		argExprList_AST = (Block)currentAST.root;
		returnAST = argExprList_AST;
	}
	
	public final void dummy() throws RecognitionException, TokenStreamException {
		
		returnAST = null;
		ASTPair currentAST = new ASTPair();
		Block dummy_AST = null;
		
		switch ( LA(1)) {
		case NTypedefName:
		{
			Block tmp177_AST = null;
			tmp177_AST = (Block)astFactory.create(LT(1));
			astFactory.addASTChild(currentAST, tmp177_AST);
			match(NTypedefName);
			dummy_AST = (Block)currentAST.root;
			break;
		}
		case NInitDecl:
		{
			Block tmp178_AST = null;
			tmp178_AST = (Block)astFactory.create(LT(1));
			astFactory.addASTChild(currentAST, tmp178_AST);
			match(NInitDecl);
			dummy_AST = (Block)currentAST.root;
			break;
		}
		case NDeclarator:
		{
			Block tmp179_AST = null;
			tmp179_AST = (Block)astFactory.create(LT(1));
			astFactory.addASTChild(currentAST, tmp179_AST);
			match(NDeclarator);
			dummy_AST = (Block)currentAST.root;
			break;
		}
		case NStructDeclarator:
		{
			Block tmp180_AST = null;
			tmp180_AST = (Block)astFactory.create(LT(1));
			astFactory.addASTChild(currentAST, tmp180_AST);
			match(NStructDeclarator);
			dummy_AST = (Block)currentAST.root;
			break;
		}
		case NDeclaration:
		{
			Block tmp181_AST = null;
			tmp181_AST = (Block)astFactory.create(LT(1));
			astFactory.addASTChild(currentAST, tmp181_AST);
			match(NDeclaration);
			dummy_AST = (Block)currentAST.root;
			break;
		}
		case NCast:
		{
			Block tmp182_AST = null;
			tmp182_AST = (Block)astFactory.create(LT(1));
			astFactory.addASTChild(currentAST, tmp182_AST);
			match(NCast);
			dummy_AST = (Block)currentAST.root;
			break;
		}
		case NPointerGroup:
		{
			Block tmp183_AST = null;
			tmp183_AST = (Block)astFactory.create(LT(1));
			astFactory.addASTChild(currentAST, tmp183_AST);
			match(NPointerGroup);
			dummy_AST = (Block)currentAST.root;
			break;
		}
		case NFunctionCallArgs:
		{
			Block tmp184_AST = null;
			tmp184_AST = (Block)astFactory.create(LT(1));
			astFactory.addASTChild(currentAST, tmp184_AST);
			match(NFunctionCallArgs);
			dummy_AST = (Block)currentAST.root;
			break;
		}
		case NNonemptyAbstractDeclarator:
		{
			Block tmp185_AST = null;
			tmp185_AST = (Block)astFactory.create(LT(1));
			astFactory.addASTChild(currentAST, tmp185_AST);
			match(NNonemptyAbstractDeclarator);
			dummy_AST = (Block)currentAST.root;
			break;
		}
		case NInitializer:
		{
			Block tmp186_AST = null;
			tmp186_AST = (Block)astFactory.create(LT(1));
			astFactory.addASTChild(currentAST, tmp186_AST);
			match(NInitializer);
			dummy_AST = (Block)currentAST.root;
			break;
		}
		case NStatementExpr:
		{
			Block tmp187_AST = null;
			tmp187_AST = (Block)astFactory.create(LT(1));
			astFactory.addASTChild(currentAST, tmp187_AST);
			match(NStatementExpr);
			dummy_AST = (Block)currentAST.root;
			break;
		}
		case NEmptyExpression:
		{
			Block tmp188_AST = null;
			tmp188_AST = (Block)astFactory.create(LT(1));
			astFactory.addASTChild(currentAST, tmp188_AST);
			match(NEmptyExpression);
			dummy_AST = (Block)currentAST.root;
			break;
		}
		case NParameterTypeList:
		{
			Block tmp189_AST = null;
			tmp189_AST = (Block)astFactory.create(LT(1));
			astFactory.addASTChild(currentAST, tmp189_AST);
			match(NParameterTypeList);
			dummy_AST = (Block)currentAST.root;
			break;
		}
		case NFunctionDef:
		{
			Block tmp190_AST = null;
			tmp190_AST = (Block)astFactory.create(LT(1));
			astFactory.addASTChild(currentAST, tmp190_AST);
			match(NFunctionDef);
			dummy_AST = (Block)currentAST.root;
			break;
		}
		case NCompoundStatement:
		{
			Block tmp191_AST = null;
			tmp191_AST = (Block)astFactory.create(LT(1));
			astFactory.addASTChild(currentAST, tmp191_AST);
			match(NCompoundStatement);
			dummy_AST = (Block)currentAST.root;
			break;
		}
		case NParameterDeclaration:
		{
			Block tmp192_AST = null;
			tmp192_AST = (Block)astFactory.create(LT(1));
			astFactory.addASTChild(currentAST, tmp192_AST);
			match(NParameterDeclaration);
			dummy_AST = (Block)currentAST.root;
			break;
		}
		case NCommaExpr:
		{
			Block tmp193_AST = null;
			tmp193_AST = (Block)astFactory.create(LT(1));
			astFactory.addASTChild(currentAST, tmp193_AST);
			match(NCommaExpr);
			dummy_AST = (Block)currentAST.root;
			break;
		}
		case NUnaryExpr:
		{
			Block tmp194_AST = null;
			tmp194_AST = (Block)astFactory.create(LT(1));
			astFactory.addASTChild(currentAST, tmp194_AST);
			match(NUnaryExpr);
			dummy_AST = (Block)currentAST.root;
			break;
		}
		case NLabel:
		{
			Block tmp195_AST = null;
			tmp195_AST = (Block)astFactory.create(LT(1));
			astFactory.addASTChild(currentAST, tmp195_AST);
			match(NLabel);
			dummy_AST = (Block)currentAST.root;
			break;
		}
		case NPostfixExpr:
		{
			Block tmp196_AST = null;
			tmp196_AST = (Block)astFactory.create(LT(1));
			astFactory.addASTChild(currentAST, tmp196_AST);
			match(NPostfixExpr);
			dummy_AST = (Block)currentAST.root;
			break;
		}
		case NRangeExpr:
		{
			Block tmp197_AST = null;
			tmp197_AST = (Block)astFactory.create(LT(1));
			astFactory.addASTChild(currentAST, tmp197_AST);
			match(NRangeExpr);
			dummy_AST = (Block)currentAST.root;
			break;
		}
		case NStringSeq:
		{
			Block tmp198_AST = null;
			tmp198_AST = (Block)astFactory.create(LT(1));
			astFactory.addASTChild(currentAST, tmp198_AST);
			match(NStringSeq);
			dummy_AST = (Block)currentAST.root;
			break;
		}
		case NAsmAttribute:
		{
			Block tmp199_AST = null;
			tmp199_AST = (Block)astFactory.create(LT(1));
			astFactory.addASTChild(currentAST, tmp199_AST);
			match(NAsmAttribute);
			dummy_AST = (Block)currentAST.root;
			break;
		}
		case NGnuAsmExpr:
		{
			Block tmp200_AST = null;
			tmp200_AST = (Block)astFactory.create(LT(1));
			astFactory.addASTChild(currentAST, tmp200_AST);
			match(NGnuAsmExpr);
			dummy_AST = (Block)currentAST.root;
			break;
		}
		default:
		{
			throw new NoViableAltException(LT(1), getFilename());
		}
		}
		returnAST = dummy_AST;
	}
	
	public final void cilDummy() throws RecognitionException, TokenStreamException {
		
		returnAST = null;
		ASTPair currentAST = new ASTPair();
		Block cilDummy_AST = null;
		
		switch ( LA(1)) {
		case NProgram:
		{
			Block tmp201_AST = null;
			tmp201_AST = (Block)astFactory.create(LT(1));
			astFactory.addASTChild(currentAST, tmp201_AST);
			match(NProgram);
			cilDummy_AST = (Block)currentAST.root;
			break;
		}
		case NPureExpr:
		{
			Block tmp202_AST = null;
			tmp202_AST = (Block)astFactory.create(LT(1));
			astFactory.addASTChild(currentAST, tmp202_AST);
			match(NPureExpr);
			cilDummy_AST = (Block)currentAST.root;
			break;
		}
		case NPureExpressionGroup:
		{
			Block tmp203_AST = null;
			tmp203_AST = (Block)astFactory.create(LT(1));
			astFactory.addASTChild(currentAST, tmp203_AST);
			match(NPureExpressionGroup);
			cilDummy_AST = (Block)currentAST.root;
			break;
		}
		case NFunctionDeclSpecifiers:
		{
			Block tmp204_AST = null;
			tmp204_AST = (Block)astFactory.create(LT(1));
			astFactory.addASTChild(currentAST, tmp204_AST);
			match(NFunctionDeclSpecifiers);
			cilDummy_AST = (Block)currentAST.root;
			break;
		}
		case NFunctionBody:
		{
			Block tmp205_AST = null;
			tmp205_AST = (Block)astFactory.create(LT(1));
			astFactory.addASTChild(currentAST, tmp205_AST);
			match(NFunctionBody);
			cilDummy_AST = (Block)currentAST.root;
			break;
		}
		case NFunctionCall:
		{
			Block tmp206_AST = null;
			tmp206_AST = (Block)astFactory.create(LT(1));
			astFactory.addASTChild(currentAST, tmp206_AST);
			match(NFunctionCall);
			cilDummy_AST = (Block)currentAST.root;
			break;
		}
		case NFunctionCallStmt:
		{
			Block tmp207_AST = null;
			tmp207_AST = (Block)astFactory.create(LT(1));
			astFactory.addASTChild(currentAST, tmp207_AST);
			match(NFunctionCallStmt);
			cilDummy_AST = (Block)currentAST.root;
			break;
		}
		case NFunctionCallAssignStmt:
		{
			Block tmp208_AST = null;
			tmp208_AST = (Block)astFactory.create(LT(1));
			astFactory.addASTChild(currentAST, tmp208_AST);
			match(NFunctionCallAssignStmt);
			cilDummy_AST = (Block)currentAST.root;
			break;
		}
		case NAssignStmt:
		{
			Block tmp209_AST = null;
			tmp209_AST = (Block)astFactory.create(LT(1));
			astFactory.addASTChild(currentAST, tmp209_AST);
			match(NAssignStmt);
			cilDummy_AST = (Block)currentAST.root;
			break;
		}
		case NImplicitAssignStmt:
		{
			Block tmp210_AST = null;
			tmp210_AST = (Block)astFactory.create(LT(1));
			astFactory.addASTChild(currentAST, tmp210_AST);
			match(NImplicitAssignStmt);
			cilDummy_AST = (Block)currentAST.root;
			break;
		}
		case NWhile:
		{
			Block tmp211_AST = null;
			tmp211_AST = (Block)astFactory.create(LT(1));
			astFactory.addASTChild(currentAST, tmp211_AST);
			match(NWhile);
			cilDummy_AST = (Block)currentAST.root;
			break;
		}
		case NIf:
		{
			Block tmp212_AST = null;
			tmp212_AST = (Block)astFactory.create(LT(1));
			astFactory.addASTChild(currentAST, tmp212_AST);
			match(NIf);
			cilDummy_AST = (Block)currentAST.root;
			break;
		}
		case NScope:
		{
			Block tmp213_AST = null;
			tmp213_AST = (Block)astFactory.create(LT(1));
			astFactory.addASTChild(currentAST, tmp213_AST);
			match(NScope);
			cilDummy_AST = (Block)currentAST.root;
			break;
		}
		case NLocalDeclarations:
		{
			Block tmp214_AST = null;
			tmp214_AST = (Block)astFactory.create(LT(1));
			astFactory.addASTChild(currentAST, tmp214_AST);
			match(NLocalDeclarations);
			cilDummy_AST = (Block)currentAST.root;
			break;
		}
		case NDeclSpecifiers:
		{
			Block tmp215_AST = null;
			tmp215_AST = (Block)astFactory.create(LT(1));
			astFactory.addASTChild(currentAST, tmp215_AST);
			match(NDeclSpecifiers);
			cilDummy_AST = (Block)currentAST.root;
			break;
		}
		case NEmptyScope:
		{
			Block tmp216_AST = null;
			tmp216_AST = (Block)astFactory.create(LT(1));
			astFactory.addASTChild(currentAST, tmp216_AST);
			match(NEmptyScope);
			cilDummy_AST = (Block)currentAST.root;
			break;
		}
		case NReturn:
		{
			Block tmp217_AST = null;
			tmp217_AST = (Block)astFactory.create(LT(1));
			astFactory.addASTChild(currentAST, tmp217_AST);
			match(NReturn);
			cilDummy_AST = (Block)currentAST.root;
			break;
		}
		case NGoto:
		{
			Block tmp218_AST = null;
			tmp218_AST = (Block)astFactory.create(LT(1));
			astFactory.addASTChild(currentAST, tmp218_AST);
			match(NGoto);
			cilDummy_AST = (Block)currentAST.root;
			break;
		}
		case NContinue:
		{
			Block tmp219_AST = null;
			tmp219_AST = (Block)astFactory.create(LT(1));
			astFactory.addASTChild(currentAST, tmp219_AST);
			match(NContinue);
			cilDummy_AST = (Block)currentAST.root;
			break;
		}
		case NBreak:
		{
			Block tmp220_AST = null;
			tmp220_AST = (Block)astFactory.create(LT(1));
			astFactory.addASTChild(currentAST, tmp220_AST);
			match(NBreak);
			cilDummy_AST = (Block)currentAST.root;
			break;
		}
		case NLabelledStmt:
		{
			Block tmp221_AST = null;
			tmp221_AST = (Block)astFactory.create(LT(1));
			astFactory.addASTChild(currentAST, tmp221_AST);
			match(NLabelledStmt);
			cilDummy_AST = (Block)currentAST.root;
			break;
		}
		case NLabel:
		{
			Block tmp222_AST = null;
			tmp222_AST = (Block)astFactory.create(LT(1));
			astFactory.addASTChild(currentAST, tmp222_AST);
			match(NLabel);
			cilDummy_AST = (Block)currentAST.root;
			break;
		}
		case NArrayIndex:
		{
			Block tmp223_AST = null;
			tmp223_AST = (Block)astFactory.create(LT(1));
			astFactory.addASTChild(currentAST, tmp223_AST);
			match(NArrayIndex);
			cilDummy_AST = (Block)currentAST.root;
			break;
		}
		case NArrayElement:
		{
			Block tmp224_AST = null;
			tmp224_AST = (Block)astFactory.create(LT(1));
			astFactory.addASTChild(currentAST, tmp224_AST);
			match(NArrayElement);
			cilDummy_AST = (Block)currentAST.root;
			break;
		}
		case NBlock:
		{
			Block tmp225_AST = null;
			tmp225_AST = (Block)astFactory.create(LT(1));
			astFactory.addASTChild(currentAST, tmp225_AST);
			match(NBlock);
			cilDummy_AST = (Block)currentAST.root;
			break;
		}
		case NStructFields:
		{
			Block tmp226_AST = null;
			tmp226_AST = (Block)astFactory.create(LT(1));
			astFactory.addASTChild(currentAST, tmp226_AST);
			match(NStructFields);
			cilDummy_AST = (Block)currentAST.root;
			break;
		}
		case NNDGoto:
		{
			Block tmp227_AST = null;
			tmp227_AST = (Block)astFactory.create(LT(1));
			astFactory.addASTChild(currentAST, tmp227_AST);
			match(NNDGoto);
			cilDummy_AST = (Block)currentAST.root;
			break;
		}
		case NLCurlyInitializer:
		{
			Block tmp228_AST = null;
			tmp228_AST = (Block)astFactory.create(LT(1));
			astFactory.addASTChild(currentAST, tmp228_AST);
			match(NLCurlyInitializer);
			cilDummy_AST = (Block)currentAST.root;
			break;
		}
		case NLHS:
		{
			Block tmp229_AST = null;
			tmp229_AST = (Block)astFactory.create(LT(1));
			astFactory.addASTChild(currentAST, tmp229_AST);
			match(NLHS);
			cilDummy_AST = (Block)currentAST.root;
			break;
		}
		case NRHS:
		{
			Block tmp230_AST = null;
			tmp230_AST = (Block)astFactory.create(LT(1));
			astFactory.addASTChild(currentAST, tmp230_AST);
			match(NRHS);
			cilDummy_AST = (Block)currentAST.root;
			break;
		}
		default:
		{
			throw new NoViableAltException(LT(1), getFilename());
		}
		}
		returnAST = cilDummy_AST;
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
		long[] data = { 4563402544L, 0L, 0L};
		return data;
	}
	public static final BitSet _tokenSet_0 = new BitSet(mk_tokenSet_0());
	private static final long[] mk_tokenSet_1() {
		long[] data = { 4563402512L, 0L, 0L};
		return data;
	}
	public static final BitSet _tokenSet_1 = new BitSet(mk_tokenSet_1());
	private static final long[] mk_tokenSet_2() {
		long[] data = { 99321118672L, 0L, 0L};
		return data;
	}
	public static final BitSet _tokenSet_2 = new BitSet(mk_tokenSet_2());
	private static final long[] mk_tokenSet_3() {
		long[] data = { 268429056L, 0L, 0L};
		return data;
	}
	public static final BitSet _tokenSet_3 = new BitSet(mk_tokenSet_3());
	private static final long[] mk_tokenSet_4() {
		long[] data = { 22011700992L, 0L, 0L};
		return data;
	}
	public static final BitSet _tokenSet_4 = new BitSet(mk_tokenSet_4());
	private static final long[] mk_tokenSet_5() {
		long[] data = { 140773995577344L, 0L, 0L};
		return data;
	}
	public static final BitSet _tokenSet_5 = new BitSet(mk_tokenSet_5());
	private static final long[] mk_tokenSet_6() {
		long[] data = { 268175104L, 0L, 0L};
		return data;
	}
	public static final BitSet _tokenSet_6 = new BitSet(mk_tokenSet_6());
	private static final long[] mk_tokenSet_7() {
		long[] data = { 272193552336L, 0L, 0L};
		return data;
	}
	public static final BitSet _tokenSet_7 = new BitSet(mk_tokenSet_7());
	private static final long[] mk_tokenSet_8() {
		long[] data = { 271925116880L, 0L, 0L};
		return data;
	}
	public static final BitSet _tokenSet_8 = new BitSet(mk_tokenSet_8());
	private static final long[] mk_tokenSet_9() {
		long[] data = { 194884140816L, 0L, 0L};
		return data;
	}
	public static final BitSet _tokenSet_9 = new BitSet(mk_tokenSet_9());
	private static final long[] mk_tokenSet_10() {
		long[] data = { 269777633472L, 0L, 0L};
		return data;
	}
	public static final BitSet _tokenSet_10 = new BitSet(mk_tokenSet_10());
	private static final long[] mk_tokenSet_11() {
		long[] data = { 72198605598883792L, 2097126L, 0L, 0L};
		return data;
	}
	public static final BitSet _tokenSet_11 = new BitSet(mk_tokenSet_11());
	private static final long[] mk_tokenSet_12() {
		long[] data = { 274072600528L, 0L, 0L};
		return data;
	}
	public static final BitSet _tokenSet_12 = new BitSet(mk_tokenSet_12());
	private static final long[] mk_tokenSet_13() {
		long[] data = { 4563339008L, 0L, 0L};
		return data;
	}
	public static final BitSet _tokenSet_13 = new BitSet(mk_tokenSet_13());
	private static final long[] mk_tokenSet_14() {
		long[] data = { 274340972352L, 0L, 0L};
		return data;
	}
	public static final BitSet _tokenSet_14 = new BitSet(mk_tokenSet_14());
	private static final long[] mk_tokenSet_15() {
		long[] data = { 274072536896L, 0L, 0L};
		return data;
	}
	public static final BitSet _tokenSet_15 = new BitSet(mk_tokenSet_15());
	private static final long[] mk_tokenSet_16() {
		long[] data = { -52613349392L, -1L, 68719476735L, 0L, 0L, 0L};
		return data;
	}
	public static final BitSet _tokenSet_16 = new BitSet(mk_tokenSet_16());
	private static final long[] mk_tokenSet_17() {
		long[] data = { 72198353403772928L, 2097126L, 0L, 0L};
		return data;
	}
	public static final BitSet _tokenSet_17 = new BitSet(mk_tokenSet_17());
	private static final long[] mk_tokenSet_18() {
		long[] data = { 21609054208L, 0L, 0L};
		return data;
	}
	public static final BitSet _tokenSet_18 = new BitSet(mk_tokenSet_18());
	private static final long[] mk_tokenSet_19() {
		long[] data = { 194481684480L, 0L, 0L};
		return data;
	}
	public static final BitSet _tokenSet_19 = new BitSet(mk_tokenSet_19());
	private static final long[] mk_tokenSet_20() {
		long[] data = { 158913789952L, 0L, 0L};
		return data;
	}
	public static final BitSet _tokenSet_20 = new BitSet(mk_tokenSet_20());
	private static final long[] mk_tokenSet_21() {
		long[] data = { 72198801019895568L, 2097126L, 0L, 0L};
		return data;
	}
	public static final BitSet _tokenSet_21 = new BitSet(mk_tokenSet_21());
	private static final long[] mk_tokenSet_22() {
		long[] data = { 72198799946153744L, 2097126L, 0L, 0L};
		return data;
	}
	public static final BitSet _tokenSet_22 = new BitSet(mk_tokenSet_22());
	private static final long[] mk_tokenSet_23() {
		long[] data = { 21609250816L, 0L, 0L};
		return data;
	}
	public static final BitSet _tokenSet_23 = new BitSet(mk_tokenSet_23());
	private static final long[] mk_tokenSet_24() {
		long[] data = { 159316639744L, 0L, 0L};
		return data;
	}
	public static final BitSet _tokenSet_24 = new BitSet(mk_tokenSet_24());
	private static final long[] mk_tokenSet_25() {
		long[] data = { 104476153216880L, 0L, 0L};
		return data;
	}
	public static final BitSet _tokenSet_25 = new BitSet(mk_tokenSet_25());
	private static final long[] mk_tokenSet_26() {
		long[] data = { 104476018999392L, 0L, 0L};
		return data;
	}
	public static final BitSet _tokenSet_26 = new BitSet(mk_tokenSet_26());
	private static final long[] mk_tokenSet_27() {
		long[] data = { 76560256288686066L, 2097126L, 0L, 0L};
		return data;
	}
	public static final BitSet _tokenSet_27 = new BitSet(mk_tokenSet_27());
	private static final long[] mk_tokenSet_28() {
		long[] data = { 104475482128480L, 0L, 0L};
		return data;
	}
	public static final BitSet _tokenSet_28 = new BitSet(mk_tokenSet_28());
	private static final long[] mk_tokenSet_29() {
		long[] data = { 76420615225147520L, 2097126L, 0L, 0L};
		return data;
	}
	public static final BitSet _tokenSet_29 = new BitSet(mk_tokenSet_29());
	private static final long[] mk_tokenSet_30() {
		long[] data = { 75576190295015552L, 2097126L, 0L, 0L};
		return data;
	}
	public static final BitSet _tokenSet_30 = new BitSet(mk_tokenSet_30());
	private static final long[] mk_tokenSet_31() {
		long[] data = { 72198353135337472L, 2097126L, 0L, 0L};
		return data;
	}
	public static final BitSet _tokenSet_31 = new BitSet(mk_tokenSet_31());
	private static final long[] mk_tokenSet_32() {
		long[] data = { -985003236325568L, 2097151L, 0L, 0L};
		return data;
	}
	public static final BitSet _tokenSet_32 = new BitSet(mk_tokenSet_32());
	private static final long[] mk_tokenSet_33() {
		long[] data = { 76420615225147392L, 2097126L, 0L, 0L};
		return data;
	}
	public static final BitSet _tokenSet_33 = new BitSet(mk_tokenSet_33());
	private static final long[] mk_tokenSet_34() {
		long[] data = { 76560256288686064L, 2097126L, 0L, 0L};
		return data;
	}
	public static final BitSet _tokenSet_34 = new BitSet(mk_tokenSet_34());
	private static final long[] mk_tokenSet_35() {
		long[] data = { 139660391088224L, 0L, 0L};
		return data;
	}
	public static final BitSet _tokenSet_35 = new BitSet(mk_tokenSet_35());
	private static final long[] mk_tokenSet_36() {
		long[] data = { 3377837159481344L, 0L, 0L};
		return data;
	}
	public static final BitSet _tokenSet_36 = new BitSet(mk_tokenSet_36());
	private static final long[] mk_tokenSet_37() {
		long[] data = { -3658644805648190L, 31L, 0L, 0L};
		return data;
	}
	public static final BitSet _tokenSet_37 = new BitSet(mk_tokenSet_37());
	private static final long[] mk_tokenSet_38() {
		long[] data = { -549755813902L, 2097151L, 0L, 0L};
		return data;
	}
	public static final BitSet _tokenSet_38 = new BitSet(mk_tokenSet_38());
	private static final long[] mk_tokenSet_39() {
		long[] data = { 4294967296L, 24L, 0L, 0L};
		return data;
	}
	public static final BitSet _tokenSet_39 = new BitSet(mk_tokenSet_39());
	private static final long[] mk_tokenSet_40() {
		long[] data = { -140190282481470L, 2097151L, 0L, 0L};
		return data;
	}
	public static final BitSet _tokenSet_40 = new BitSet(mk_tokenSet_40());
	
	}
