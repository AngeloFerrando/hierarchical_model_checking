package edu.toronto.cs.cparser;

import edu.toronto.cs.cparser.block.*;

import java.io.*;

import antlr.*;

public class CILProgramBlockerUtil
{
  public static Block getProgramBlock (Reader file) throws
    RecognitionException, TokenStreamException
  {
    CILParser cilParser = CILParserUtil.getParser (file);
    cilParser.translationUnit ();

    CILProgramBlocker cilBlocker = new CILProgramBlocker ();
    cilBlocker.setASTNodeClass (Block.class.getName ());

    cilBlocker.translationUnit (cilParser.getAST ());

    return (Block) cilBlocker.getAST ();
  }
}
