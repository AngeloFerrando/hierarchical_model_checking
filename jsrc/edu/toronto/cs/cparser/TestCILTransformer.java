package edu.toronto.cs.cparser;

import java.io.*;

import antlr.*;
import antlr.collections.AST;
import antlr.debug.misc.ASTFrame;

import edu.toronto.cs.cparser.*;
import edu.toronto.cs.cparser.block.*;
import edu.toronto.cs.expr.*;

/*
 * Usage: java edu.toronto.cs.cparser.TestCilTransformer <cSourcefile>
 * [traceFrom [traceTo]]
 */

class TestCILTransformer
{
  private TestCILTransformer () {}

  public static void main(String [] args) throws Exception
  {
		  int traceFrom = 0;
		  int traceTo = 0;

		  String programName = args [0];

		  if (args.length >= 2)
				  traceFrom =
				  	(new Integer (Integer.parseInt (args [1]))).intValue ();

		  if (args.length >= 3)
				  traceTo =
				  	(new Integer (Integer.parseInt (args [2]))).intValue ();

		  Reader reader = new FileReader (programName);


			/* (no output)
		  CILTransformerUtil.getProgram	(new ExprFactoryImpl (),
		                                 reader,
										 traceFrom,
										 traceTo);
		  	*/

		  System.out.println (CILTransformerUtil.getProgram
		  						(new ExprFactoryImpl (),
								 reader,
								 traceFrom,
								 traceTo).toString ());
  }
}
