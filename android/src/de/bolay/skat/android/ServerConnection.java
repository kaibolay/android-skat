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
import android.util.Log;

import com.skatonline.client.ServerConnectionImpl;

import de.bolay.pubsub.Observers;
import de.bolay.skat.net.Ranking;
import de.bolay.skat.net.client.observers.ConnectionObserver;
import de.bolay.skat.net.client.observers.MainLobbyObserver;

public class ServerConnection extends Service {
  public static final String LOGIN = "LOGIN";

  public static final String LOG_TAG = "ServerConnection";

  private volatile Looper connectionLooper;
  private volatile ServiceHandler connectionHandler;

  public de.bolay.skat.net.client.ServerConnection connection;

  private class SimpleConnectionObserver implements ConnectionObserver {
    Ranking myRanking;

    public void disconnected() {
      Log.i(LOG_TAG, "ConnectionObserver.disconnected()");
    }

    public void loginAttempted(LoginStatus status) {
      Log.i(LOG_TAG, "ConnectionObserver.loginAttempted(" + status + ")");
    }

    public void rankingReceived(Ranking ranking) {
      System.out.println("ConnectionObserver.rankingReceived(" + ranking + ")");
      myRanking = ranking;
    }
  }

  private static class SimpleMainLobbyObserver implements MainLobbyObserver {
    Map<String, Ranking> scoreboard = new HashMap<String, Ranking>();

    public void serverNotificationReceived(String html) {
      Log.i(LOG_TAG,
          "MainLobbyObserver.serverNotificationReceived(\"" + html + "\")");
    }

    public void playerJoined(String name, Ranking ranking) {
      Log.i(LOG_TAG, "MainLobbyObserver.playerJoined(" + name + ", "
          + ranking + ")");
      scoreboard.put(name, ranking);
    }

    public void playerLeft(String name) {
      Log.i(LOG_TAG, "MainLobbyObserver.playerLeft(" + name + ")");
      scoreboard.remove(name);
    }

    public void chatMessageReceived(String sender, String text) {
      Log.i(LOG_TAG, "MainLobbyObserver.chatMessageReceived(\"" + sender
          + "\", \"" + text + "\")");
    }
  }

  private final class ServiceHandler extends Handler {

    public ServiceHandler(Looper looper) {
      super(looper);
    }

    @Override
    public void handleMessage(Message msg) {
      Bundle arguments = (Bundle) msg.obj;
      String username = arguments.getString("username");
      String password = arguments.getString("password");

      Log.i("ServiceStartArguments",
          "Username: " + username + " Password: " + password);

      connection.login(username, password);
      Log.i("ServiceStartArguments", "Done with #" + msg.arg1);
    }
  }

  @Override
  public void onCreate() {
    Observers observers = new Observers();
    observers.add(new SimpleConnectionObserver());
    observers.add(new SimpleMainLobbyObserver());
    connection = ServerConnectionImpl.Server.PREMIUM.getConnection(observers);


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
      Log.i("ServiceStartArguments",
              "Starting #" + startId + ": " + intent.getExtras());

      Message msg = connectionHandler.obtainMessage();
      msg.arg1 = startId;
      msg.obj = intent.getExtras();
      connectionHandler.sendMessage(msg);
      Log.i("ServiceStartArguments", "Sending: " + msg);
  }
}
