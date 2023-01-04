package edu.toronto.cs.yasm.abstractor;

import edu.toronto.cs.cparser.*;
import edu.toronto.cs.cparser.block.*;

import java.io.FileReader;

public class TestPredicateParser
{
  public static void main (String[] args) throws Exception
  {
    CILLexer lexer = new CILLexer (new FileReader (args [0]));
    lexer.setTokenObjectClass (CToken.class.getName ());

    PredicateParser parser = new PredicateParser (lexer);
    parser.setASTNodeClass (Block.class.getName ());
    Block.setTokenVocabulary (CILTokenTypes.class.getName ());

    parser.parse ();

    TNode.printTree (parser.getAST ());
  }
}
