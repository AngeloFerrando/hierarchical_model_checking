package edu.toronto.cs.util;

import org.apache.log4j.*;

// A reminder of the logging priority:
//
// DEBUG < INFO < WARN < ERROR < FATAL

/**
 ** This class allows to create and maintain logs for java programs.
 **/
public class Logger {

  public static final String LOG_FILENAME = "logger.file.name";

  /** Runs the configurator for the logs. **/
  static {

    //    BasicConfigurator.configure();
    if (System.getProperty (LOG_FILENAME) != null)
      PropertyConfigurator.configure (System.getProperty (LOG_FILENAME));
    else
      BasicConfigurator.configure ();
  }

  /** Category assigned to this particular logger. **/
  protected Category log;

  /**
   ** You may not create a logger not associated with a category.
   **/
  private Logger () {}

  /**
   ** Make a logger corresponding to the specified category.
   **
   ** @param catname the name of the logging category (usually just
   ** the class name).
   **/
  public Logger (String catname)
  {
    log = Category.getInstance(catname);
  }

  /**
   ** Request to log a debugging message.
   **
   ** @param message the debugging message to log.
   **/
  public void debug (Object message) 
  {
    log.debug(message);
  }

  /**
   ** Request to log a debugging message with the stack trace of the
   ** Throwable t passed as parameter.
   **
   ** @param message the debugging message to log.
   ** @param t the exception to log, including its stack trace.
   **/
  public void debug (Object message, Throwable t)
  {
    log.debug (message, t);
  }

  /**
   ** Log with DEBUG priority the stack trace of the Throwable t
   ** passed as parameter.
   **
   ** @param t the exception to log, including its stack trace.
   **/  
  public void debug (Throwable t)
  {
    debug ("", t);
  }

  /**
   ** Request to log some information.
   **
   ** @param message the information (message) to log.
   **/
  public void info (Object message) 
  {
    log.info(message);
  }

  /**
   ** Request to log an informative message with the stack trace of the
   ** Throwable t passed as parameter.
   **
   ** @param message the information (message) to log.
   ** @param t the exception to log, including its stack trace.
   **/
  public void info (Object message, Throwable t)
  {
    log.info (message, t);
  }

  /**
   ** Log with INFO priority the stack trace of the Throwable t
   ** passed as parameter.
   **
   ** @param t the exception to log, including its stack trace.
   **/  
  public void info (Throwable t)
  {
    info ("", t);
  }

  /**
   ** Request to log a warning message.
   **
   ** @param message the warning message to log.
   **/
  public void warn (Object message)
  {
    log.warn(message);
  }

  /**
   ** Request to log a warning message with the stack trace of the
   ** Throwable t passed as parameter.
   **
   ** @param message the warning message to log.
   ** @param t the exception to log, including its stack trace.
   **/
  public void warn (Object message, Throwable t)
  {
    log.warn (message, t);
  }

  /**
   ** Log with WARN priority the stack trace of the Throwable t
   ** passed as parameter.
   **
   ** @param t the exception to log, including its stack trace.
   **/  
  public void warn (Throwable t)
  {
    warn ("", t);
  }

  /**
   ** Request to log an error message.
   **
   ** @param message the error message to log.
   **/
  public void error (Object message)
  {
    log.error(message);
  }

  /**
   ** Request to log an error message with the stack trace of the
   ** Throwable t passed as parameter.
   **
   ** @param message the error message to log.
   ** @param t the exception to log, including its stack trace.
   **/
  public void error (Object message, Throwable t)
  {
    log.error (message, t);
  }

  /**
   ** Log with ERROR priority the stack trace of the Throwable t
   ** passed as parameter.
   **
   ** @param t the exception to log, including its stack trace.
   **/  
  public void error (Throwable t)
  {
    error ("", t);
  }
  

  /**
   ** Request to log a fatal error message.
   **
   ** @param message the fatal error message to log.
   **/
  public void fatal (Object message) 
  {
    log.fatal(message);
  }

  /**
   ** Request to log a fatal error message with the stack trace of the
   ** Throwable t passed as parameter.
   **
   ** @param message the fatal error message to log.
   ** @param t the exception to log, including its stack trace.
   **/
  public void fatal (Object message, Throwable t)
  {
    log.fatal (message, t);
  }
  
  /**
   ** Log with FATAL priority the stack trace of the Throwable t
   ** passed as parameter.
   **
   ** @param t the exception to log, including its stack trace.
   **/
  public void fatal (Throwable t)
  {
    fatal ("", t);
  }

}
