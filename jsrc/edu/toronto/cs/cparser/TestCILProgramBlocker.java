package edu.toronto.cs.cparser;

import java.io.*;

import antlr.*;
import antlr.collections.AST;
import antlr.debug.misc.ASTFrame;

import edu.toronto.cs.cparser.*;
import edu.toronto.cs.cparser.block.*;

class TestCILProgramBlocker
{
  private TestCILProgramBlocker () {}

  // -- show a tree representation of an AST
  public static void drawFrame(String name, AST ptree)
  {
    ASTFrame frame = new ASTFrame(name, ptree);
    frame.setVisible(true);
  }

  public static void main(String [] args) throws Exception
  {
    for (int i = 0; i < args.length; i++)
    {
      String programName = args [i];

      Reader reader = new FileReader (programName);

      drawFrame ("Blocked CIL Program",
                  CILProgramBlockerUtil.getProgramBlock (reader));
    }
  }
}
