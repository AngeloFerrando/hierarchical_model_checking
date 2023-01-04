package edu.toronto.cs.cparser.block;

import edu.toronto.cs.boolpg.abstraction.*;
import edu.toronto.cs.cparser.*;
import edu.toronto.cs.expr.*;

public class Block extends TNode implements CILTokenTypes
{
  static final String NUM_CALL_SITES_ATTRIB_NAME = "numCallSites";
  static final String CALL_INDEX_ATTRIB_NAME = "callIndex";

  public Block ()
  {
    new Block (BlockType.UNKNOWN);
  }

  public Block (BlockType type)
  {
    setType (NBlock);
    setBlockType (type);
  }

  public BlockType getBlockType ()
  {
    return (BlockType) getAttribute (BlockType.attribName);
  }

  public void setBlockType (BlockType type)
  {
    setAttribute (BlockType.attribName, type);
  }

  /** 
   * When called on a PROGRAM Block, finds a "main" function definition 
   * amongst its children, and returns that subtree.
   * 
   * @param block 
   * @return 
   */
  public Block getMainFunctionDef ()
  {
    assert getBlockType () == BlockType.PROGRAM;

    Block curBlock = (Block) getFirstChild ();

    while (curBlock != null)
    {
      if (curBlock.getBlockType () == BlockType.FUNCTION_DEFINITION)
        if (curBlock.getFunctionName ().equals ("main"))
          return curBlock;
      
      curBlock = (Block) curBlock.getNextSibling ();    
    }

    throw new RuntimeException ("No main function definition");
  }

  /** 
   * @deprecated Use getLocalDecls ().
   */
  public Block getDecls ()
  {
    return getLocalDecls ();
  }

  /** 
   * A scope AST looks like:
   * 
   * NBlock [scope]
   *   NLocalDeclarations
   *   ...
   *
   * @return the <I>first child</I> of the NLocalDeclarations subtree.
   */
  public Block getLocalDecls ()
  {
    /*
    if (getBlockType () == BlockType.FUNCTION_DEFINITION)
      return getBody ().getLocalDecls ();

    if (getBlockType () == BlockType.SCOPE)
      return (Block) getFirstChild ().getFirstChild ();

    throw new RuntimeException ("Invalid Block type");  
    */

    return (Block) getLocalDeclsRoot ().getFirstChild ();
  }

  public Block getLocalDeclsRoot ()
  {
    if (getBlockType () == BlockType.FUNCTION_DEFINITION)
        return getBody ().getLocalDeclsRoot ();

    if (getBlockType () == BlockType.SCOPE)
      return (Block) getFirstChild ();

    throw new RuntimeException ("Invalid Block type");  
  }

  public Block getWhileBody ()
  {
    if (getBlockType () == BlockType.WHILE)
      return (Block) getFirstChild ().getNextSibling ();

    throw new RuntimeException ("Invalid Block type");  
  }

  public Block getBody ()
  {
    if (getBlockType () == BlockType.FUNCTION_DEFINITION)
      return (Block) getFirstChild ().getNextSibling ().getNextSibling ();

    if (getBlockType () == BlockType.SCOPE)
      return (Block) getFirstChild ().getNextSibling ();

    if (getBlockType () == BlockType.PROGRAM)
      return (Block) getFirstChild ();  

    throw new RuntimeException ("Invalid Block type");  
  }

  public Block getCond ()
  {
    if (getBlockType () == BlockType.IF ||
        getBlockType () == BlockType.WHILE)
      return (Block) getFirstChild ();

    throw new RuntimeException ("Invalid Block type");  
  }

  public Block getThenBlock ()
  {
    if (getBlockType () == BlockType.IF)
      return (Block) getFirstChild ().getNextSibling ();

    throw new RuntimeException ("Invalid Block type");  
  }

  public Block getElseBlock ()
  {
    if (getBlockType () == BlockType.IF)
    {
      if (getNumberOfChildren () == 3)
        // 1: cond, 2: then, 3: else
        return (Block) getLastChild ();
      else
        return null;  
    }

    throw new RuntimeException ("Invalid Block type");  
  }

  public String getLabel ()
  {
    return (String) getAttribute ("label");
  }

  public void setLabel (String label)
  {
    if (label == null)
      return;

    setAttribute ("label", label);
  }

  /** 
   * A function definition AST looks like this:
   * 
   * NBlock [functionDefinition]
   *   NFunctionDeclSpecifiers
   *   NDeclarator
   *     ID
   *     ...
   *   NBlock [scope]
   *
   * @return the text of the "ID" node.
   */
  public String getFunctionName ()
  {
    if (getBlockType () == BlockType.FUNCTION_DEFINITION)
      return
        getFirstChild ().getNextSibling ().getFirstChild ().getText ();

    throw new RuntimeException ("Invalid Block type");  
  }

  /** 
   * A function definition AST looks like this:
   * 
   * NBlock [functionDefinition]
   *   NDeclSpecifiers
   *   ...
   *
   * @return the NDeclSpecifiers subtree.
   */
  public Block getFunctionReturnType ()
  {
    if (getBlockType () == BlockType.FUNCTION_DEFINITION)
      return (Block) getFirstChild ();
    
    throw new RuntimeException ("Invalid Block type");  
  }

  public boolean isNonVoidFunction ()
  {
    if (getBlockType () == BlockType.FUNCTION_DEFINITION)
    {
      if (getFirstChild ().getFirstChild ().getType () == LITERAL_void)
        return false;
      else
        return true;
    }

    throw new RuntimeException ("Invalid Block type");  
  }

  /** 
   * A function definition AST looks like this:
   *
   * NBlock [functionDefinition]
   *   NFunctionDeclSpecifiers
   *   NDeclarator
   *     ID
   *     NParameterTypeList
   *   NBlock [scope]
   *
   * @return the NParameterTypeList subtree.
   */
  public Block getFunctionParameters ()
  {
    if (getBlockType () == BlockType.FUNCTION_DEFINITION)
      return (Block)
        getFirstChild ().
        getNextSibling ().
          getFirstChild ().
          getNextSibling ();
    
    throw new RuntimeException ("Invalid Block type");  
  }

  public Block getFunctionParameterList ()
  {
    if (getBlockType () == BlockType.FUNCTION_DEFINITION)
      return (Block) getFunctionParameters ().getFirstChild ();
    
    throw new RuntimeException ("Invalid Block type");  
  }

  /** 
   * A goto AST looks like this:
   *
   * NBlock [goto]
   *   ID
   * 
   * @return the text of the ID subtree.
   */
  public String getGotoTargetLabel ()
  {
    BlockType type = getBlockType ();

    if (type == BlockType.GOTO)
      return getFirstChild ().getText ();

    if (type == BlockType.BREAK)
      return null;
    
    throw new RuntimeException ("Invalid Block type");
  }

  public Block getReturnExpr ()
  {
    if (getBlockType () == BlockType.RETURN)
      return (Block) getFirstChild ();

    throw new RuntimeException ("Invalid Block type");  
  }


  /** 
   * A labelled statement AST looks like this:
   *
   * NBlock [labelledStatement]
   *   NLabel
   *   NBlock
   * 
   * @return the NBlock child subtree.
   */
  public Block getLabelledStmt ()
  {
    if (getBlockType () == BlockType.LABELLED_STATEMENT)
      return (Block) getFirstChild ().getNextSibling ();

    throw new RuntimeException ("Invalid Block type");
  }

  /** 
   * @return true if this is a DECLARATION Block which has a name; the only
   * case which currently yields false is the "void" declaration in a function
   * parameter list.
   */
  public boolean isNamedDeclaration ()
  {
    assert getBlockType () == BlockType.DECLARATION
      : "Invalid Block type";

    // Matches the case where a function's parameter list is simply "void"
    if (getFirstChild ().getFirstChild ().getType () == LITERAL_void)
      return false;

    return true; 
  }

  /** 
   * A <I>variable</I> or <I>function prototype</I> declaration AST looks like:
   *
   * NBlock [declaration]
   *   NDeclSpecifiers
   *   NDeclarator
   *     ID
   *   NInitializer
   *
   * A <I>struct/union/enum type</I> declaration AST looks like:
   *
   * NBlock [declaration]
   *   NDeclSpecifiers
   *     LITERAL_struct | LITERAL_union | LITERAL_enum
   *       ID
   *       ...
   *
   * A <I>void</I> declaration AST looks like:
   *
   * NBlock [declaration]
   *   NDeclSpecifiers
   *     LITERAL_void
   * 
   * @return the name of the declaration; null if the declaration is void.
   */
  public String getDeclName ()
  {
    assert getBlockType () == BlockType.DECLARATION
      : "Invalid Block type";

    if (getFirstChild ().getNextSibling () == null)
    {
      if (getFirstChild ().getFirstChild () != null)
      {
        if (getFirstChild ().getFirstChild ().getType () == LITERAL_struct ||
            getFirstChild ().getFirstChild ().getType () == LITERAL_union ||
            getFirstChild ().getFirstChild ().getType () == LITERAL_enum)
          // struct/union type declaration
          return getFirstChild ().
                        getFirstChild ().
                          getFirstChild ().getText ();
        else if (getFirstChild ().getFirstChild ().getType () == LITERAL_void)
          return null;
      }

      throw new RuntimeException ("Invalid Block structure");  
    }
    else if (getFirstChild ().getNextSibling ().getType () == NDeclarator)
      // variable or function prototype declaration
      return getFirstChild ().
                  getNextSibling ().
                    getFirstChild ().getText ();
    else 
          throw new RuntimeException ("Invalid Block structure");  
  }

  /** 
   * A function call AST looks like either:
   *
   * NBlock [functionCall]
   *   NFunctionCallStmt
   *     NFunctionCall
   *       ID (call name)
   *       ... (args)
   *
   * e.g. foo (1)
   *
   * or
   *
   * NBlock [functionCall]
   *   NFunctionCallAssignStmt
   *     ID (lval)
   *     NFunctionCall
   *       ID (call name)
   *       ... (args)
   * 
   * e.g. y = foo (1)
   *
   * @return the call name.
   */
  public String getFunctionCallName ()
  {
    assert getBlockType () == BlockType.FUNCTION_CALL
      : "Invalid Block type";

    if (getFirstChild ().getType () == NFunctionCallStmt)
      return getFirstChild ().
                    getFirstChild ().
                      getFirstChild ().getText ();  
    else if (getFirstChild ().getType () == NFunctionCallAssignStmt)
      return getFirstChild ().
                    getFirstChild ().
                    getNextSibling ().
                      getFirstChild ().getText ();
    else
      throw new RuntimeException ("Invalid Block structure");                  
  }

  public String getFunctionCallReturnVarName ()
  {
    assert getBlockType () == BlockType.FUNCTION_CALL
      : "Invalid Block type";

    if (getFirstChild ().getType () == NFunctionCallStmt)
      return null;
    else if (getFirstChild ().getType () == NFunctionCallAssignStmt)
      return getFirstChild ().
                    getFirstChild ().getText ();
    else
      throw new RuntimeException ("Invalid Block structure");                  
  }

  public Block getFunctionCallArgs ()
  {
    assert getBlockType () == BlockType.FUNCTION_CALL
      : "Invalid Block type";

    if (getFirstChild ().getType () == NFunctionCallStmt)
          return (Block) getFirstChild ().
                                getFirstChild ().
                                  getFirstChild ().
                                  getNextSibling ();
        else if (getFirstChild ().getType () == NFunctionCallAssignStmt)
          return (Block) getFirstChild ().
                                getFirstChild ().
                                getNextSibling ().
                                  getFirstChild ().
                                  getNextSibling ();
        else
          throw new RuntimeException ("Invalid Block structure");
  }

  public void setNumCallSites (int numCallSites)
  {
    setAttribute
      (NUM_CALL_SITES_ATTRIB_NAME, new Integer (numCallSites));
  }

  public int getNumCallSites ()
  {
    Integer numCallSites =
      (Integer) getAttribute (NUM_CALL_SITES_ATTRIB_NAME);
    if (numCallSites == null)
      return 0;
    
    return numCallSites.intValue ();  
  }

  public void setCallIndex (int v)
  {
    setAttribute
      (CALL_INDEX_ATTRIB_NAME, new Integer (v));
  }

  public int getCallIndex ()
  {
    if (getType () == NFunctionCall)
    {
      Integer callIndex =
        (Integer) getAttribute (CALL_INDEX_ATTRIB_NAME);
      return callIndex == null ? 0 : callIndex.intValue ();
    }
    else if (getType () == NBlock &&
             getBlockType () == BlockType.FUNCTION_CALL)
    {
      if (getFirstChild ().getType () == NFunctionCallAssignStmt)
        return
          ((Block) getFirstChild ().getFirstChild ().getNextSibling ()).
                   getCallIndex ();
      else if (getFirstChild ().getType () == NFunctionCallStmt)
        return
          ((Block) getFirstChild ().getFirstChild ()).getCallIndex ();    
      else
        throw new RuntimeException ("Invalid Block structure");    
    }
    else
      throw new RuntimeException ("Invalid Block type");
  }

  /* ------------------------- BLOCK MERGE METHODS ------------------------- */

  // Returns a Block which is m and n merged; returns null
  // if m and n could not be merged
  public static Block mergeNodes (Block m, Block n)
  {
    if (m.getBlockType () == BlockType.STATEMENT_LIST)
      if (n.getBlockType () == BlockType.STATEMENT_LIST)
      {
        Block mergedBlock = new Block (BlockType.STATEMENT_LIST);
        mergedBlock.setLabel (m.getLabel ());
        mergedBlock.addChild (m.getFirstChild ());
        mergedBlock.addChild (n.getFirstChild ());
        return mergedBlock;
      }
    return null;
  }

  // If elt can be merged with the head of list, returns a list
  // with the merge performed, otherwise returns a list which
  // is just elt cons'd with list
  public static Block mergeCons (Block elt, Block list)
  {
    if (list == null)
      return elt;

    // merge (elt cdr (list))
    Block mergeResult = mergeNodes (elt, list);

    if (mergeResult == null)
    {
      // cons (elt list)
      elt.setNextSibling (list);
      return elt;
    }

    // cons (mergeResult cdr (list))
    mergeResult.setNextSibling ((Block) list.getNextSibling ());
    return mergeResult;
  }

  // Returns a copy of list with merges performed
  public static Block mergeCopy (Block list)
  {
    if (list == null)
      return null;

    // cons (car (list) cdr (list))
    return mergeCons (list, mergeCopy ((Block) list.getNextSibling ()));
  }
}
