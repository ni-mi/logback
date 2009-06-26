package ch.qos.logback.classic.issue.lbclassic135;

import ch.qos.logback.classic.Logger;
import ch.qos.logback.classic.LoggerContext;
import ch.qos.logback.classic.PatternLayout;
import ch.qos.logback.classic.spi.ILoggingEvent;
import ch.qos.logback.core.FileAppender;
import ch.qos.logback.core.contention.ThreadedThroughputCalculator;

/**
 * Short sample code testing the throughput of a fair lock.
 * 
 * @author Ceki Gulcu
 */
public class LoggingToFileThrouhput {

  static int THREAD_COUNT = 10;
  static long OVERALL_DURATION_IN_MILLIS = 5000;

  public static void main(String args[]) throws InterruptedException {

    ThreadedThroughputCalculator tp = new ThreadedThroughputCalculator(
        OVERALL_DURATION_IN_MILLIS);
    tp.printEnvironmentInfo("lbclassic135  LoggingToFileThrouhput");

    LoggerContext lc = new LoggerContext();
    Logger logger = buildLoggerContext(lc);
    
    for (int i = 0; i < 2; i++) {
      tp.execute(buildArray(logger));
    }

    tp.execute(buildArray(logger));
    tp.printThroughput("File:   ");
    lc.stop();
  }

  static Logger buildLoggerContext(LoggerContext lc) {
    Logger root = lc.getLogger(LoggerContext.ROOT_NAME);

    PatternLayout patternLayout = new PatternLayout();
    patternLayout.setContext(lc);
    patternLayout.setPattern("%d %l [%t] - %msg%n");
    patternLayout.start();
    FileAppender<ILoggingEvent> fileAppender = new FileAppender<ILoggingEvent>();
    fileAppender.setContext(lc);
    fileAppender.setFile("target/lbclassic135.log");
    fileAppender.setLayout(patternLayout);
    fileAppender.setAppend(false);
    fileAppender.start();
    root.addAppender(fileAppender);
    return lc.getLogger(LoggingToFileThrouhput.class);
  }

  static LoggingRunnable[] buildArray(Logger logger) {
   
    LoggingRunnable[] array = new LoggingRunnable[THREAD_COUNT];
    for (int i = 0; i < THREAD_COUNT; i++) {
      array[i] = new LoggingRunnable(logger);
    }
    return array;
  }
}

//=== lbclassic135  LoggingToFileThrouhput === 10 Threads
//java.runtime.version = 1.6.0_05-b13
//java.vendor          = Sun Microsystems Inc.
//java.version         = 1.6.0_05
//os.name              = Windows XP
//
// File:   total of 309402 operations, or 61 operations per millisecond