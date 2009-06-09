package de.bolay.log;


public class JavaLogger extends AbstractLogger {

  public static class Factory implements Logger.Factory {
    private final Level minLevel;

    public Factory() {
      minLevel = null;
    }

    public Factory(Level minLevel) {
      this.minLevel = minLevel;
    }

    public Logger getLogger(String tag) {
      if (minLevel == null) {
        return new JavaLogger(tag);
      } else {
        return new JavaLogger(tag, minLevel);
      }
    }
  }

  private final java.util.logging.Logger javaLogger;

  public JavaLogger(String tag) {
    this(tag, (Level) null);
  }

  public JavaLogger(String tag, /* Nullable */ Level minLevel) {
    javaLogger = java.util.logging.Logger.getLogger(tag);
    if (minLevel != null) {
      javaLogger.setLevel(convertLevel(minLevel));
    }
  }

  @Override
  public void log(Level level, /* Nullable */ Throwable throwable,
      String formatString, Object... args) {
    javaLogger.logp(convertLevel(level),
        /* source class */ (String) null, /* source method */ (String) null,
        format(formatString, args), throwable);
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
