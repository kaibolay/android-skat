package de.bolay.skat.android;

import java.util.HashMap;
import java.util.Map;

import android.app.Service;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.IBinder;
import android.os.Looper;
import android.os.Message;
import de.bolay.log.AndroidLogger;
import de.bolay.log.Logger;
import de.bolay.skat.net.Ranking;
import de.bolay.skat.net.client.observers.AutisticObserverFactory;
import de.bolay.skat.net.client.observers.ConnectionObserver;
import de.bolay.skat.net.client.observers.MainLobbyObserver;
import de.bolay.skat.net.server.ServerConnection;
import de.bolay.skat.net.server.fake.FakeServer;

public class ServerConnectionService extends Service {
  private static final Logger.Factory LOG_FACTORY =
      new AndroidLogger.Factory();
  private static final Logger LOG = LOG_FACTORY.getLogger(
      ServerConnectionService.class.getSimpleName());
  public static final String LOGIN = "LOGIN";

  private volatile Looper connectionLooper;
  private volatile ServiceHandler connectionHandler;

  public ServerConnection connection;

  public String username;
  public String password;

  private class SimpleConnectionObserver implements ConnectionObserver {
    private final Logger LOG = LOG_FACTORY.getLogger(
        ConnectionObserver.class.getSimpleName());

    public void disconnected() {
      LOG.info("disconnected()");
    }
  }

  private static class SimpleMainLobbyObserver implements MainLobbyObserver {
    private final Logger LOG = LOG_FACTORY.getLogger(
        SimpleMainLobbyObserver.class.getSimpleName());

    Map<String, Ranking> scoreboard = new HashMap<String, Ranking>();

    public void serverNotificationReceived(String html) {
      LOG.info("serverNotificationReceived(\"%s\")", html);
    }

    public void playerJoined(String name, Ranking ranking) {
      LOG.info("playerJoined(\"%s\", %s)", name, ranking);
      scoreboard.put(name, ranking);
    }

    public void playerLeft(String name) {
      LOG.info("playerLeft(\"%s\", %s)", name);
      scoreboard.remove(name);
    }

    public void chatMessageReceived(String sender, String text) {
      LOG.info("chatMessageReceived(\"%s\", \"%s\")",
          sender, text);
    }

    public void entered(Lobby mainLobby) {
      LOG.info("entered()");
    }
  }

  private final class ServiceHandler extends Handler {
    private final Logger LOG = LOG_FACTORY.getLogger(
        ServiceHandler.class.getSimpleName());

    public ServiceHandler(Looper looper) {
      super(looper);
    }

    @Override
    public void handleMessage(Message msg) {
      Bundle arguments = (Bundle) msg.obj;
      username = arguments.getString("username");
      password = arguments.getString("password");

      LOG.info("ServiceStartArguments: msg1: %s, Username: %s, Password: %s",
          msg.arg1, username, password);
    }
  }

  @Override
  public void onCreate() {
    connection = new FakeServer(LOG_FACTORY);
    connection.open(new AutisticObserverFactory() {
      @Override public ConnectionObserver createConnectionObserver() {
        return new SimpleConnectionObserver();
      }

      @Override public MainLobbyObserver createMainLobbyObserver() {
        return new SimpleMainLobbyObserver();
      }
    });

    HandlerThread thread = new HandlerThread("ServerConnection");
    thread.start();

    connectionLooper = thread.getLooper();
    connectionHandler = new ServiceHandler(connectionLooper);
  }

  @Override
  public void onDestroy() {
      connectionLooper.quit();
  }

  @Override
  public IBinder onBind(Intent intent) {
    return null;
  }

  @Override
  public void onStart(Intent intent, int startId) {
    LOG.info("Starting #%d: %s", startId, intent.getExtras());

    Message msg = connectionHandler.obtainMessage();
    msg.arg1 = startId;
    msg.obj = intent.getExtras();
    connectionHandler.sendMessage(msg);
    LOG.info("Sending: %s", msg);
  }
}
