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

import com.skatonline.client.Server;
import com.skatonline.client.ServerConnectionImpl;

import de.bolay.log.AndroidLogger;
import de.bolay.log.Logger;
import de.bolay.skat.net.Ranking;
import de.bolay.skat.net.client.observers.ConnectionObserver;
import de.bolay.skat.net.client.observers.MainLobbyObserver;
import de.bolay.skat.net.client.observers.PendingLoginObserver.LoginStatus;
import de.bolay.skat.net.client.observers.PendingLoginObserver.PendingLogin;

public class ServerConnection extends Service {
  private static final Logger.Factory LOG_FACTORY =
      new AndroidLogger.Factory();
  private static final Logger LOG = LOG_FACTORY.getLogger(
      ServerConnection.class.getSimpleName());
  public static final String LOGIN = "LOGIN";

  private volatile Looper connectionLooper;
  private volatile ServiceHandler connectionHandler;

  public de.bolay.skat.net.client.ServerConnection connection;

  public String username;
  public String password;

  private class SimpleConnectionObserver implements ConnectionObserver {
    Ranking myRanking;

    public void disconnected() {
      LOG.info("ConnectionObserver.disconnected()");
    }

    public void loginAttempted(LoginStatus status) {
      LOG.info("ConnectionObserver.loginAttempted(%s)", status);
    }

    public void rankingReceived(Ranking ranking) {
      LOG.info("ConnectionObserver.rankingReceived(%s)", ranking);
      myRanking = ranking;
    }

    public void pendingLogin(PendingLogin pendingLogin) {
      pendingLogin.attemptLogin(username, password);
      LOG.info("sent pendingLogin");
    }
  }

  private static class SimpleMainLobbyObserver implements MainLobbyObserver {
    Map<String, Ranking> scoreboard = new HashMap<String, Ranking>();

    public void serverNotificationReceived(String html) {
      LOG.info(
          "MainLobbyObserver.serverNotificationReceived(\"%s\")", html);
    }

    public void playerJoined(String name, Ranking ranking) {
      LOG.info("MainLobbyObserver.playerJoined(\"%s\", %s)", name, ranking);
      scoreboard.put(name, ranking);
    }

    public void playerLeft(String name) {
      LOG.info("MainLobbyObserver.playerLeft(\"%s\", %s)", name);
      scoreboard.remove(name);
    }

    public void chatMessageReceived(String sender, String text) {
      LOG.info("MainLobbyObserver.chatMessageReceived(\"%s\", \"%s\")",
          sender, text);
    }

    public void entered(MainLobby mailLobby) {
      // TODO Auto-generated method stub

    }
  }

  private final class ServiceHandler extends Handler {

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
    connection = ServerConnectionImpl.getConnection(
        LOG_FACTORY, Server.PREMIUM);
    connection.addObserver(new SimpleConnectionObserver());
    connection.addObserver(new SimpleMainLobbyObserver());

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
