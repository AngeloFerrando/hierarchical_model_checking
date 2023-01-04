package edu.toronto.cs.binarytree;

/**
 ** This is a binary tree node.
 **/
public class BTNode
{

  protected Object Data;
  protected BTNode right, left;

  /**
   ** Constructs a new BTNode assigning null to Data, right
   ** and left.
   **/
  public BTNode()
  {
    Data = null;
    right = null;
    left = null;
  }

  /**
   ** Constructs a new BTNode contaning the specified Object as its Data
   ** and sets right and left to null.
   **
   ** @param d data to be stored in the node.
   **/
  public BTNode(Object d)
  {
    Data = d;
    right = null;
    left = null;
  }

  /**
   ** Constructs a new BTNode with given data as well as left and right
   ** children.
   **
   ** @param d  data to be stored in the node.
   ** @param l  left child.
   ** @param r  right child.
   **/
  public BTNode(Object d, BTNode l, BTNode r)
  {
    Data = d;
    right = r;
    left = l;
  }

  /**
   ** Retrieves the Data from the BTNode.
   **
   ** @return data contained in the node.
   **/
  public Object getData()
  {
    return Data;
  }

  /**
   ** Retrieves the left child of the BTNode.
   **
   ** @return left child.
   **/
  public BTNode getLeft()
  {
    return left;
  }

  /**
   ** Retrieves the right child of the BTNode.
   **
   ** @return right child.
   **/
  public BTNode getRight()
  {
    return right;
  }
    
  /**
   ** Changes the Data from the BTNode.
   **/
  public void setData(Object d)
  {
    Data = d;
  }

  /**
   ** Changes the left child of the BTNode.
   **/
  public void setLeft(BTNode l)
  {
    left = l;
  }

  /**
   ** Changes the right child of the BTNode.
   **/
  public void setRight(BTNode r)
  {
    right = r;
  }

}
