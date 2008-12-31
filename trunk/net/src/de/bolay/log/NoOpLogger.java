package de.bolay.log;

public class NoOpLogger implements Logger {

  public static class Factory implements Logger.Factory {
    public Logger getLogger(String tag) {
      return new NoOpLogger();
    }
  }

  public void log(Level level, Throwable throwable,
      String formatString, Object... args) {}
  public void debug(String formatString, Object... args) {}
  public void debug(Throwable throwable, String formatString, Object... args) {}
  public void error(String formatString, Object... args) {}
  public void error(Throwable throwable, String formatString, Object... args) {}
  public void info(String formatString, Object... args) {}
  public void info(Throwable throwable, String formatString, Object... args) {}
  public void log(Level level, String formatString, Object... args) {}
}
