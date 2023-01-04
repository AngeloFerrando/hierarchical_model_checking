package edu.toronto.cs.cparser;

import java.io.*;

import antlr.*;
import antlr.collections.AST;
import antlr.debug.misc.ASTFrame;

import edu.toronto.cs.cparser.*;
import edu.toronto.cs.cparser.block.*;

class TestCILParser
{
  private TestCILParser () {}

  // -- show a tree representation of an AST
  public static void drawFrame(String name, AST ptree)
  {
    ASTFrame frame = new ASTFrame(name, ptree);
    frame.setVisible(true);
  }

  public static void main(String[] args) throws Exception
  {
    for (int i = 0; i < args.length; i++)
    {
      String programName = args[i];

      Reader reader = new FileReader (programName);
      CILLexer lexer =
        new CILLexer ( reader );
      lexer.setTokenObjectClass("edu.toronto.cs.cparser.CToken");

      // Parse the input expression.
      CILParser parser = new CILParser ( lexer );

      // set AST node type to TNode or get nasty cast class errors
      parser.setASTNodeType (Block.class.getName());
      Block.setTokenVocabulary ("edu.toronto.cs.cparser.CILTokenTypes");

      parser.translationUnit ();
      AST t = parser.getAST ();

      System.out.println (parser.symbolTable.toString ());

      if (t != null)
        drawFrame ("CIL Program AST", t);
    }
  }
}
