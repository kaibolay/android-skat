package de.bolay.log;



public abstract class AbstractLogger implements Logger {
  private static final String DEFAULT_TAG = Logger.class.getName();
  private static final Logger.Level DEFAULT_LEVEL = Logger.Level.DEBUG;

  protected final String tag;
  protected final Logger.Level minLevel;

  protected AbstractLogger() {
    this(DEFAULT_TAG, DEFAULT_LEVEL);
  }

  protected AbstractLogger(String tag) {
    this(tag, DEFAULT_LEVEL);
  }

  protected AbstractLogger(Level minLevel) {
    this(DEFAULT_TAG, minLevel);
  }

  protected AbstractLogger(String tag, Level minLevel) {
    this.tag = tag;
    this.minLevel = minLevel;
  }

  protected boolean shouldLog(Level level) {
    return level.compareTo(minLevel) >= 0;
  }

  protected String format(String formatString, Object... args) {
    if (args.length > 0) {
      return String.format(formatString, args);
    } else {
      return formatString;
    }
  }

  public abstract void log(Level level, /* Nullable */ Throwable throwable,
      String formatString, Object... args);

  public void log(Level level, String formatString, Object... args) {
    log(level, (Throwable) null, formatString, args);
  }

  public void debug(String formatString, Object... args) {
    log(Logger.Level.DEBUG, formatString, args);
  }

  public void debug(Throwable throwable, String formatString, Object... args) {
    log(Logger.Level.DEBUG, throwable, formatString, args);
  }

  public void info(String formatString, Object... args) {
    log(Logger.Level.INFO, formatString, args);
  }

  public void info(Throwable throwable, String formatString, Object... args) {
    log(Logger.Level.INFO, throwable, formatString, args);
  }

  public void error(String formatString, Object... args) {
    log(Logger.Level.ERROR, formatString, args);
  }

  public void error(Throwable throwable, String formatString, Object... args) {
    log(Logger.Level.ERROR, throwable, formatString, args);
  }
}
