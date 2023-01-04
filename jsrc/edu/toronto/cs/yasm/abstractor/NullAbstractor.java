package edu.toronto.cs.yasm.abstractor;

import edu.toronto.cs.cparser.block.*;
import edu.toronto.cs.expr.*;
import edu.toronto.cs.yasm.pprogram.*;

import java.util.*;

public class NullAbstractor implements Abstractor
{

  ExprFactory exprFactory;

  public NullAbstractor (ExprFactory _exprFactory)
  {
    exprFactory = _exprFactory;
  }

  public PProgram doProgramAbstract (Block programBlock)
  {
    assert programBlock.getBlockType () == BlockType.PROGRAM :
      "Invalid block type";

    // XXX Two passes over programBlock
    return new PProgram (exprFactory,
                         doShallowFunctionDefAbstract (programBlock),
                         doShallowDeclAbstract (programBlock));
  }

  public Map doShallowFunctionDefAbstract (Block block)
  {
    assert block.getBlockType () == BlockType.PROGRAM
      : "Invalid block type";

    Map functionDefs = new HashMap ();  

    block = block.getBody ();

    while (block != null)
    {
      if (block.getBlockType () == BlockType.FUNCTION_DEFINITION)
      {
        System.err.println (">>> " + block.getFunctionName ());
        functionDefs.put (block.getFunctionName (), doFunctionAbstract (block));
      }
      
      if (block.getNextSibling () == null)
        System.err.println (">>> last block: " + block);
      block = (Block) block.getNextSibling ();  
    }

    return functionDefs;
  }

  /** 
   * Returns a Map of the top-level declarations in a given PROGRAM or
   * SCOPE Block (i.e. does not descend into nested scopes; this is sufficient
   * for CIL programs since all nested declarations are moved to top-level).
   * The Map is: (String) declaration-name -> (PDecl) DECLARATION Block.
   */
  public Map doShallowDeclAbstract (Block block)
  {
    assert block.getBlockType () == BlockType.PROGRAM ||
           block.getBlockType () == BlockType.FUNCTION_DEFINITION
      : "Invalid block type";

    Map retMap = new HashMap ();

    if (block.getBlockType () == BlockType.FUNCTION_DEFINITION)
    {
      mapDeclList (retMap, block.getFunctionParameterList ());
      mapDeclList (retMap, block.getLocalDecls ());
    }
    else if (block.getBlockType () == BlockType.PROGRAM)
      // global decls are scattered throughout the body of a PROGRAM Block
      mapDeclList (retMap, block.getBody ());
    else
      throw new RuntimeException ("Something is really wrong");

    // System.err.println (">>> doShallowDeclAbstract " + retMap);  

    return retMap;
  }

  private void mapDeclList (Map declMap, Block block)
  {
    while (block != null)
    {
      if (block.getBlockType () == BlockType.DECLARATION)
        if (block.isNamedDeclaration ())
          declMap.put (block.getDeclName (), new PDecl (block));

      block = (Block) block.getNextSibling ();
    }
  }

  // Initial abstraction
  public PFunctionDef doFunctionAbstract (Block functionDefBlock)
  {
    if (functionDefBlock.getBlockType () == BlockType.FUNCTION_DEFINITION)
    {
      // XXX really hacky (?); have to be able to refer to PFunctionDef as
      // it is being defined
      PFunctionDef def = new PFunctionDef (functionDefBlock);
      def.addToHead
        (doInsideFunctionAbstract (def, functionDefBlock.getBody ())); 
      def.setLocalDecls (doShallowDeclAbstract (functionDefBlock));

      /*
      return new PFunctionDef (
        doInsideFunctionAbstract (functionDefBlock.getBody ()), 
        doShallowDeclAbstract (functionDefBlock),
        functionDefBlock
      );
      */
      return def;
    }

    throw new RuntimeException ("Invalid Block type");
  }

  // Dispatches block to appropriate abstractor method; no nested
  // function definitions, so we don't need to cover
  // BlockType.FUNCTION_DEFINITION
  public PStmt doInsideFunctionAbstract (PFunctionDef def, Block block)
  {
    if (block == null)
      return null;

    BlockType type = block.getBlockType ();

    if (type == BlockType.SCOPE)
      return doScopeAbstract (def, block);
    else if (type == BlockType.IF)
      return doIfAbstract (def, block);
    else if (type == BlockType.WHILE)
      return doWhileAbstract (def, block);
    else if (type == BlockType.STATEMENT_LIST)
      return doStmtListAbstract (def, block);
    else if (type == BlockType.LABELLED_STATEMENT)
      return doLabelledStmtAbstract (def, block);
    else if (type == BlockType.FUNCTION_CALL)
      return doFunctionCallAbstract (def, block);  
    else if (type == BlockType.GOTO)
      return doGotoAbstract (def, block);  
    else if (type == BlockType.NDGOTO)
      return doNDGotoAbstract (def, block);
    else if (type == BlockType.BREAK)
      return doBreakAbstract (def, block);  
    else if (type == BlockType.RETURN)
      return doReturnAbstract (def, block);  
    else
    {
      System.err.println (def);
      System.err.println ();
      System.err.println (block);
      throw new RuntimeException ("Can't handle block type " + type);
    }
  }

  // Returns head with tail linked to the end of the list it points to
  private PStmt cons (PStmt head, PStmt tail)
  {
    if (head == null)
      return tail;

    head.setTail (tail);

    return head;
  }

  // Abstracts block and each of its siblings in order; the results are
  // linked and the head is returned
  private PStmt doChainAbstract (PFunctionDef def, Block block)
  {
    if (block == null)
      return null;

    return (cons (doInsideFunctionAbstract (def, block),
          doChainAbstract (def, (Block) block.getNextSibling ())));    
  }

  // Abstracts each statement in the body of the scope
  private PStmt doScopeAbstract (PFunctionDef def, Block block)
  {
    Block body = block.getBody ();

    if (body == null)
      return new SkipPStmt (def, block);

    return doChainAbstract (def, body);
  }

  // Abstracts the condition as "unknown" and the then and else parts
  private PStmt doIfAbstract (PFunctionDef def, Block block)
  {
    return new IfPStmt (
        def,
        block,
        doCondAbstract (block.getCond ()),
        doInsideFunctionAbstract (def, block.getThenBlock ()),
        doInsideFunctionAbstract (def, block.getElseBlock ()));
    // exitGoto to be patched
  }

  private PStmt doWhileAbstract (PFunctionDef def, Block block)
  {
    WhilePStmt wp =
      new WhilePStmt (def,
                      block,
                      doCondAbstract (block.getCond ()),
                      doInsideFunctionAbstract (def, block.getWhileBody ()));
    // elseGoto to be patched                                 
    return wp;
  }

  /** 
   * Abstracts an if/while condition as "unknown".
   *
   * @param block a Block of BlockType IF or WHILE.
   * @return a PCond with value "unknown" and a reference to block.
   */
  private PCond doCondAbstract (Block block)
  {
    return new PCond (block, exprFactory.op (BiLatticeOp.IBOT));

  }

  /** 
   * Abstracts a list of statements as an empty parallel assignment.
   */
  private PStmt doStmtListAbstract (PFunctionDef def, Block block)
  {
    return new PrllAsmtPStmt (def, block);
  }

  private PStmt doLabelledStmtAbstract (PFunctionDef def, Block block)
  {
    return doInsideFunctionAbstract (def, block.getLabelledStmt ());
  }

  private PStmt doFunctionCallAbstract (PFunctionDef def, Block block)
  {
    return new FunctionCallPStmt (def, block);
  }

  private PStmt doGotoAbstract (PFunctionDef def, Block block)
  {
    return new GotoPStmt (def, block);
  }

  private PStmt doNDGotoAbstract (PFunctionDef def, Block block)
  {
    return new NDGotoPStmt (def, block);
  }

  private PStmt doBreakAbstract (PFunctionDef def, Block block)
  {
    return new BreakPStmt (def, block);
  }

  private PStmt doReturnAbstract (PFunctionDef def, Block block)
  {
    return new ReturnPStmt (def, block);
  }

  /** 
   * Returns the first FUNCTION_DEFINITION Block found in block's children with
   * function name "main"; returns null * if no such Block could be found.
   * 
   * @param block 
   * @return 
   */
   /* XXX No longer used
  public Block getMainFunctionBlock (Block programBlock)
  {
    assert programBlock.getBlockType () == BlockType.PROGRAM :
      "Invalid block type";

    Block b = (Block) programBlock.getFirstChild ();  

    while (b != null)
    {
      if (b.getBlockType () == BlockType.FUNCTION_DEFINITION)
        if (b.getFunctionName ().equals ("main"))
          return b;

      b = (Block) b.getNextSibling ();    
    }

    return null;
  }
  */


}

