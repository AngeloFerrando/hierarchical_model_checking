package edu.toronto.cs.cparser;

import edu.toronto.cs.cparser.block.*;
import java.io.*;

/**
 * Describe class <code>CParserUtil</code> here.
 *
 * @author <a href="mailto:kelvin@epoch.cs">Kelvin Ku</a>
 * @version 1.0
 */
public class CILParserUtil
{
  /**
   * Describe <code>getParser</code> method here.
   *
   * @param file a <code>Reader</code> value
   * @return a <code>GnuCParser</code> value
   */
  public static CILParser getParser (Reader file)
  {
    CILLexer lexer = new CILLexer (file);
    lexer.setTokenObjectClass (CToken.class.getName ());

    CILParser parser = new CILParser (lexer);
    parser.setASTNodeClass (Block.class.getName ());
    Block.setTokenVocabulary (CILTokenTypes.class.getName ());

    return parser;
  }
}
