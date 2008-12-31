package de.bolay.log;

import com.google.common.base.Nullable;

public class JavaLogger extends AbstractLogger {

  public static class Factory implements Logger.Factory {
    public Logger getLogger(String tag) {
      return new JavaLogger(tag);
    }
  }

  private final java.util.logging.Logger javaLogger;

  public JavaLogger(String tag) {
    javaLogger = java.util.logging.Logger.getLogger(tag);
  }

  @Override
  public void log(Level level, @Nullable Throwable throwable,
      String formatString, Object... args) {
    javaLogger.logp(convertLevel(level),
        /* source class */ (String) null, /* source method */ (String) null,
        String.format(formatString, args), throwable);
  }

  private static java.util.logging.Level convertLevel(Level level) {
    switch(level) {
       case DEBUG:
         return java.util.logging.Level.FINEST;
       case INFO:
         return java.util.logging.Level.INFO;
       case ERROR:
         return java.util.logging.Level.SEVERE;
    }
    throw new IllegalStateException("Unkown level: " + level);
  }
}
