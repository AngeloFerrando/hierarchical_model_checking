package edu.toronto.cs.yasm.abstractor;

import edu.toronto.cs.cparser.*;
import edu.toronto.cs.cparser.block.*;
import edu.toronto.cs.tp.cvcl.*;
import edu.toronto.cs.boolpg.abstraction.*;

import java.io.*;
import java.util.*;

import antlr.debug.misc.ASTFrame;

public class TestDeclsParser
{
  public static void main (String[] args) throws Exception
  {
    ValidityChecker vc = CVCLUtil.newValidityChecker ();

    CILParser cilParser = CILParserUtil.getParser (new FileReader (args [0]));

    Block.setTokenVocabulary ("edu.toronto.cs.cparser.CILTokenTypes");

    CILProgramBlocker cilBlocker = new CILProgramBlocker ();
    cilBlocker.setASTNodeType (Block.class.getName ());

    DeclsParser dParser = new DeclsParser ();
    dParser.setASTNodeType (Block.class.getName ());

    // -- parse
    cilParser.translationUnit ();

    // -- block
    cilBlocker.translationUnit (cilParser.getAST ());

    Block mainDecls =
      ((Block) cilBlocker.getAST ()).
        getMainFunctionDef ().getLocalDeclsRoot ();

    ASTFrame frame = new ASTFrame ("mainDecls", mainDecls);
    frame.setVisible (true);

    List l = new java.util.LinkedList ();

    // -- process decls
    dParser.declarationList (mainDecls, vc, l);

    for (Iterator it = l.iterator (); it.hasNext ();)
      System.out.println ((Expr) it.next ());
  }
}
