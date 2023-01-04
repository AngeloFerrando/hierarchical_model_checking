package edu.toronto.cs.cparser;

import edu.toronto.cs.cparser.block.*;
import java.io.*;

// -- only used by the main method
import antlr.debug.misc.ASTFrame;
import antlr.collections.AST;


/**
 * Describe class <code>CParserUtil</code> here.
 *
 * @author <a href="mailto:kelvin@epoch.cs">Kelvin Ku</a>
 * @version 1.0
 */
public class GnuCParserUtil
{
  /**
   * Describe <code>getParser</code> method here.
   *
   * @param file a <code>Reader</code> value
   * @return a <code>GnuCParser</code> value
   */
  public static GnuCParser getParser (Reader file)
  {
    GnuCLexer lexer = new GnuCLexer (file);
    lexer.setTokenObjectClass (CToken.class.getName ());
    lexer.initialize (); // Important, adds token aliases!
	
    GnuCParser parser = new GnuCParser (lexer);
    parser.setASTNodeClass (TNode.class.getName ());
    TNode.setTokenVocabulary (GNUCTokenTypes.class.getName ());

    return parser;
  }

  public static void main (String[] args) throws Exception
  {
    for (int i = 0; i < args.length; i++)
      {
	String programName = args [i];
	Reader reader = new FileReader (programName);
	GnuCParser parser = GnuCParserUtil.getParser (reader);
	parser.translationUnit ();
	if (parser.getAST () != null)
	  drawFrame (programName, parser.getAST ());
      }
  }
  
  // -- show a tree representation of an AST
  private static void drawFrame(String name, AST ptree)
  {
    ASTFrame frame = new ASTFrame(name, ptree);
    frame.setVisible(true);
  }

}
