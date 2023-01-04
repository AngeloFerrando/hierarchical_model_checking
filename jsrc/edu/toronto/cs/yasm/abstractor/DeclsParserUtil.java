package edu.toronto.cs.yasm.abstractor;

import java.io.*;
import antlr.*;
import edu.toronto.cs.cparser.block.*;
import edu.toronto.cs.expr.*;
import edu.toronto.cs.tp.cvcl.ValidityChecker;
import edu.toronto.cs.tp.cvcl.Type;

public class DeclsParserUtil
{
  public static DeclsParser newBlockDeclsParser ()
  {
    DeclsParser declsParser = new DeclsParser ();
    declsParser.setASTNodeClass (Block.class.getName ());
    Block.setTokenVocabulary
      (edu.toronto.cs.cparser.CILTokenTypes.class.getName ());
    return declsParser;
  }

  public static Type parseBlockTypeSpecifiers (Block b, ValidityChecker vc)
  throws RecognitionException
  {
    return newBlockDeclsParser ().declSpecifiers (b, vc);
  }

  public static Type parseExprTypeSpecifiers (Expr e, ValidityChecker vc)
  {
    return vc.intType (); // XXX stub
  }

  public static edu.toronto.cs.tp.cvcl.Expr parseBlockDecl
    (Block b, ValidityChecker vc)
  {
    try
    {
      return newBlockDeclsParser ().declaration (b, vc);
    }
    catch (RecognitionException ex)  
    {
      return null;
    }
  }

  public static edu.toronto.cs.tp.cvcl.Expr parseExprDecl
    (Expr e, ValidityChecker vc)
  {
    return vc.varExpr (CILDeclarationOp.getDeclName (e),
                       vc.intType () /* XXX for now */);
  }
}
