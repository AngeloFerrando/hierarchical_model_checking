package edu.toronto.cs.boolpg.parser;

import java.io.*;
import antlr.CommonAST;
import antlr.collections.AST;
import antlr.debug.misc.ASTFrame;

public class TestDriver
{
  public static void main (String[] args)
  {
    // -- create a lexer
    BoolProgramLexer lex = new BoolProgramLexer (System.in);
    BoolProgramParser parser = new BoolProgramParser (lex);
    try 
      {
	parser.start ();
	CommonAST parseTree = (CommonAST) parser.getAST ();
	//System.out.println (parser.getSymbolTable ());
	
	drawFrame (parseTree);
	
      } catch (Exception ex) {
	ex.printStackTrace ();
      }
  }

  public static void drawFrame(CommonAST ptree)
  {
    ASTFrame frame = new ASTFrame("The tree", ptree);
    frame.setVisible(true);
  }
  
}
