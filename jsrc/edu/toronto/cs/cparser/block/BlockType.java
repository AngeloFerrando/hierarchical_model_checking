package edu.toronto.cs.cparser.block;

public class BlockType
{
    private final String name;

    public static final String attribName = "blockType";

    private BlockType (String _name)
    {
        name = _name;
    }

    public String toString ()
    {
        return name;
    }

    public static final BlockType UNKNOWN = 
        new BlockType ("unknown");

    public static final BlockType PROGRAM =
        new BlockType ("program");

    public static final BlockType FUNCTION_DEFINITION =
        new BlockType ("functionDefinition");

    public static final BlockType SCOPE =
        new BlockType ("scope");

    public static final BlockType STATEMENT_LIST =
        new BlockType ("statementList");

    public static final BlockType LABELLED_STATEMENT =
        new BlockType ("labelledStatement"); 

    public static final BlockType IF =
        new BlockType ("if");

    public static final BlockType WHILE =
        new BlockType ("while");

    public static final BlockType DECLARATION =
        new BlockType ("declaration"); 

    public static final BlockType FUNCTION_CALL =
        new BlockType ("functionCall"); 

    public static final BlockType GOTO =
        new BlockType ("goto"); 

    public static final BlockType BREAK =
        new BlockType ("break"); 

    public static final BlockType RETURN =
        new BlockType ("return"); 

    public static final BlockType NDGOTO =
        new BlockType ("ndGoto"); 
}
