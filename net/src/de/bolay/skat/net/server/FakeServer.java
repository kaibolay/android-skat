package de.bolay.skat.net.server;

import java.util.concurrent.CountDownLatch;

import de.bolay.pubsub.Notifier;
import de.bolay.pubsub.Observer;
import de.bolay.pubsub.Observers;
import de.bolay.skat.net.Ranking;
import de.bolay.skat.net.client.ServerConnection;
import de.bolay.skat.net.client.observers.ConnectionObserver;
import de.bolay.skat.net.client.observers.PendingLoginObserver;
import de.bolay.skat.net.client.observers.PendingLoginObserver.LoginStatus;
import de.bolay.skat.net.client.observers.PendingLoginObserver.PendingLogin;

public class FakeServer implements ServerConnection, Runnable {
  private static final Ranking NO_RANKING = new Ranking(0, 0, 0, 0, 0);
  private final Observers observers;
  private boolean isUp;
  @SuppressWarnings("unused")
  private CountDownLatch loggedIn = new CountDownLatch(1);

  private class FakePendingLogin implements PendingLogin {

    private final PendingLogin self;

    FakePendingLogin() {
      self = this;
    }

    public void attemptLogin(String username, String password) {
      if (username != "fake" || password != "fake") {
        observers.notify(PendingLoginObserver.class,
            new Notifier<PendingLoginObserver>() {
                public void notify(PendingLoginObserver observer) {
                  observer.loginFailed(LoginStatus.WRONG_CREDENTIALS, self);
            }});
      }
      observers.notify(PendingLoginObserver.class,
          new Notifier<PendingLoginObserver>() {
              public void notify(PendingLoginObserver observer) {
                observer.loginSucceeded(NO_RANKING);
          }});
    }
  }

  public void run() {
    while (isUp()) {
      observers.notify(PendingLoginObserver.class,
          new Notifier<PendingLoginObserver>() {
              public void notify(PendingLoginObserver observer) {
                observer.pendingLogin(new FakePendingLogin());
          }});
    }
  }

  public FakeServer() {
    this.observers = new Observers();
    isUp = false;
  }

  public void addObserver(Observer observer) {
    observers.add(observer);
  }

  public synchronized void close() {
    isUp = false;
    observers.notify(ConnectionObserver.class,
        new Notifier<ConnectionObserver>() {
            public void notify(ConnectionObserver observer) {
              observer.disconnected();
        }});
  }

  public synchronized boolean isUp() {
    return isUp;
  }

  public synchronized void open() {
    isUp = true;
  }
}
