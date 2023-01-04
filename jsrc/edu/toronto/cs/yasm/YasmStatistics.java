package edu.toronto.cs.yasm;

import edu.toronto.cs.util.StopWatch;

/**
 * YasmStatistics.java
 *
 *
 * Created: Wed Jul  7 12:32:03 2004
 *
 * @author <a href="mailto:maxin@epoch.cs">Xin Ma</a>
 * @version 1.0
 */
public class YasmStatistics 
{
  /**
   * measures overall time
   *
   */
  StopWatch overall;
  
  /**
   * time took to compute H function
   *
   */
  StopWatch refine;


  /**
   *predicateAbstraction time
   *
   */
  StopWatch predicateAbstraction;
  /**
   * model-checking time
   *
   */
  StopWatch mc;

  /**
   * Time it took to find new predicates;
   *
   */
  StopWatch newPred;

  /**
   * Time taken to compute symbolic representation of the model
   *
   */
  StopWatch compile;

  /**
   * Parsing time
   *
   */
  StopWatch parse;
  
  
  public YasmStatistics () 
  {
    overall = new StopWatch ();
  } // YasmStatistics constructor
  
  public void start ()
  {
    overall.reset ();
  }
  public void stop ()
  {
    overall.stop ();
  }

  public void startPredicateAbstraction ()
  {
    if (predicateAbstraction == null)
      predicateAbstraction = new StopWatch ();
    else
      predicateAbstraction.resume ();
  }
  public void stopPredicateAbstraction ()
  {
    predicateAbstraction.pause ();
  }

  public void startCompile ()
  {
    if (compile == null)
      compile = new StopWatch ();
    else
      compile.resume ();
  }
  public void stopCompile ()
  {
    compile.pause ();
  }
  

  public void startParse ()
  {
    if (parse == null)
      parse = new StopWatch ();
    else
      parse.resume ();
  }
  public void stopParse ()
  {
    parse.pause ();
  }
  


  public void startRefine ()
  {
    if (refine == null)
      refine = new StopWatch ();
    else
      refine.resume ();
  }
  public void stopRefine ()
  {
    refine.pause ();
  }

  public void startMC ()
  {
    if (mc == null)
      mc = new StopWatch ();
    else
      mc.resume ();
  }
  public void stopMC ()
  {
    mc.pause ();
  }

  public void startNewPred ()
  {
    if (newPred == null)
      newPred = new StopWatch ();
    else
      newPred.resume ();
  }
  public void stopNewPred ()
  {
    newPred.pause ();
  }

  private StringBuffer appendnl (StringBuffer sb, String s)
  {
    sb.append (s);
    sb.append ("\n");
    return sb;
  }
  public String toString ()
  {
    StringBuffer sb = new StringBuffer ();
    appendnl (sb, "************************************");
    appendnl (sb, "STATISTICS");
    appendnl (sb, "Overall time: " + overall);
    appendnl (sb, "Parsing time: " + parse);
    appendnl (sb, "Refine time: " + refine);
    appendnl (sb, "Model compilation: " + compile);
    appendnl (sb, "Model-Checking time: " + mc);
    appendnl (sb, "New predicates time: " + newPred);
    appendnl (sb, "PredicateAbstraction time: " + predicateAbstraction);
    return sb.toString ();
  }
  
} // YasmStatistics
