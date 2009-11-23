package de.bolay.log;

import java.io.PrintStream;

import javax.annotation.Nullable;

public class ConsoleLogger extends AbstractLogger {

  public static class Factory implements Logger.Factory {
    private final Logger.Level minLevel;

    public Factory(Logger.Level minLevel) {
      this.minLevel = minLevel;
    }

    public Logger getLogger(String tag) {
      return new ConsoleLogger(tag, minLevel);
    }
  }

  public ConsoleLogger(String tag, Level minLevel) {
    super(tag, minLevel);
  }

  @Override
  public void log(Level level, @Nullable Throwable throwable,
      String formatString, Object... args) {
    if (!shouldLog(level)) {
      return;
    }
    PrintStream printStream = System.out;
    if (level != Logger.Level.INFO) {
        printStream = System.err;
    }
    appendTag(printStream);
    printStream.append(String.format(formatString, args));
    appendThrowable(printStream, throwable);
    printStream.println();
    printStream.flush();
  }


  private void appendTag(PrintStream printStream) {
    if (tag != null) {
      printStream.append(tag);
      printStream.append(": ");
    }
  }

  private void appendThrowable(PrintStream printStream,
      @Nullable Throwable throwable) {
    if (throwable != null) {
      printStream.append(" - ");
      throwable.printStackTrace(printStream);
    }
  }
}
