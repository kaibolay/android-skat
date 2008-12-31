package de.bolay.log;

public interface Logger {
  public enum Level {DEBUG, INFO, ERROR};

  public interface Factory {
    Logger getLogger(String tag);
  }

  void log(Level level, String formatString, Object... args);
  void log(Level level, Throwable throwable,
      String formatString, Object... args);

  void debug(String formatString, Object... args);
  void debug(Throwable throwable, String formatString, Object... args);
  void info(String formatString, Object... args);
  void info(Throwable throwable, String formatString, Object... args);
  void error(String formatString, Object... args);
  void error(Throwable throwable, String formatString, Object... args);
}
