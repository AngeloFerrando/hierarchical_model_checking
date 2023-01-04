package edu.toronto.cs.yasm.abstractor;

import java.io.*;

import java.util.*;

import antlr.debug.misc.ASTFrame;

import edu.toronto.cs.cparser.*;
import edu.toronto.cs.cparser.block.*;
import edu.toronto.cs.boolpg.abstraction.*;
import edu.toronto.cs.yasm.pprogram.*;
import edu.toronto.cs.expr.*;

public class TestNullAbstractor
{
  public static void main(String[] args)
  {
    for (int i = 0; i < args.length; i++)
    {
      try
      {
        String programName = args[i];

        Reader reader = new FileReader (programName);

        CILParser cilParser = CILParserUtil.getParser (reader);
        cilParser.translationUnit ();

        CILProgramBlocker cilBlocker = new CILProgramBlocker ();
        cilBlocker.setASTNodeType (Block.class.getName());
        cilBlocker.translationUnit (cilParser.getAST ());

        NullAbstractor abst =
          new NullAbstractor (new ExprFactoryImpl ());

        /*
        ASTFrame frame = 
          new ASTFrame ("Blocked CIL Program", cilBlocker.getAST ());  
        frame.setVisible (true);  
        */

        PProgram p = abst.doProgramAbstract ((Block) cilBlocker.getAST ());

        /*
        // Print indexed statements
        System.out.println ();

        List statementList = p.getStmtList ();

        for (int l = 0; l < statementList.size (); l++)
        {
          PStmt pStmt = (PStmt) statementList.get (l);
          System.out.print ("[l" + (l + 1) + "] ");
          pStmt.printMe ();
          if (pStmt.getNext () == null)
          {
            System.out.println (" -> <nothing>");
            continue;
          }
          System.out.println (" -> " + pStmt.getNext ().getLabel ());
        }

        // Print labelled statements
        System.out.println ();

        for (Iterator it =
            p.getLabelledStmtsMap ().keySet ().iterator ();
            it.hasNext ();)
        {
          String label = (String) it.next ();
          System.out.print ("[" + label + "] ");
          ((PStmt) p.getLabelledStmtsMap ().get (label)).printMe ();
        }
        */

      }
      catch ( Exception e )
      {
        System.err.println ( "exception: " + e);
        e.printStackTrace();
      }
    }
  }
}

