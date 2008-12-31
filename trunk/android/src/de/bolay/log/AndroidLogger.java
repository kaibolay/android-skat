package de.bolay.log;

import java.io.PrintWriter;
import java.io.StringWriter;

import android.util.Log;

import com.google.common.base.Nullable;

public class AndroidLogger extends AbstractLogger {
  public static class Factory implements Logger.Factory {
    public Logger getLogger() {
      return new AndroidLogger();
    }

    public Logger getLogger(String tag) {
      return new AndroidLogger(tag);
    }

    public Logger getLogger(Logger.Level minLevel) {
      return new AndroidLogger(minLevel);
    }

    public Logger getLogger(String tag, Logger.Level minLevel) {
      return new AndroidLogger(tag, minLevel);
    }
  }

  public AndroidLogger() {
    super();
  }

  public AndroidLogger(String tag) {
    super(tag);
  }

  public AndroidLogger(Level minLevel) {
    super(minLevel);
  }

  public AndroidLogger(String tag, Level minLevel) {
    super(tag, minLevel);
  }

  @Override
  public void log(Level level, @Nullable Throwable throwable,
      String formatString, Object... args) {
    if (!shouldLog(level)) {
      return;
    }
    StringWriter stringWriter = new StringWriter();
    stringWriter.append(String.format(formatString, args));
    appendThrowable(new PrintWriter(stringWriter), throwable);
    Log.println(convertLevel(level), tag, stringWriter.toString());
  }

  private void appendThrowable(PrintWriter printWriter,
      @Nullable Throwable throwable) {
    if (throwable != null) {
      printWriter.append(" - ");
      throwable.printStackTrace(printWriter);
    }
  }

  private int convertLevel(Level level) {
    switch(level) {
      case DEBUG:
        return android.util.Log.DEBUG;
      case INFO:
        return android.util.Log.INFO;
      case ERROR:
        return android.util.Log.ERROR;
    }
    throw new IllegalStateException("Unkown level: " + level);
  }
}
