package edu.toronto.cs.yasm.pgenerator;


import java.util.List;

public interface PredicateGenerator
{
  /**
   * runs the predicate generator and returns true if 
   * anything has changed like new predicates are added 
   * or the abstraction of the program has changed
   *
   * @return a <code>boolean</code> value
   */
  boolean find ();

  /**
   * returns a list of new predicates found
   *
   * @return a <code>List</code> value
   */
  List getNewPreds ();
}
