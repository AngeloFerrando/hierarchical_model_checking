package edu.toronto.cs.yasm.pgenerator;

import java.util.*;

/**
 * A predicate generator that chains other predicate generators
 *
 * @author <a href="mailto:maxin@eon.cs">Xin Ma</a>
 * @version 1.0
 */
public class ChainPredicateGenerator implements PredicateGenerator
{
  // -- list of generators we know about
  List pGenerators;

  // -- predicate generator that actually found something
  PredicateGenerator worked;

  public ChainPredicateGenerator ()
  {
    pGenerators = new LinkedList ();
  }

  public ChainPredicateGenerator add (PredicateGenerator p)
  {
    pGenerators.add (p);
    return this;
  }

  public boolean find ()
  {
    for (Iterator it = pGenerators.iterator (); it.hasNext ();)
      {
	worked = (PredicateGenerator) it.next ();
	if (worked.find ()) return true;
      }
    worked = null;
    return false;
    
  }
  
  public List getNewPreds ()
  {
    return worked != null ? worked.getNewPreds () : new LinkedList ();
  }
  

}
