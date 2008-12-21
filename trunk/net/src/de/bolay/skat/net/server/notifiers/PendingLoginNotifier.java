package de.bolay.skat.net.server.notifiers;

import de.bolay.pubsub.Notifier;
import de.bolay.pubsub.Observers;
import de.bolay.skat.net.Ranking;
import de.bolay.skat.net.client.observers.PendingLoginObserver;
import de.bolay.skat.net.client.observers.PendingLoginObserver.LoginStatus;
import de.bolay.skat.net.client.observers.PendingLoginObserver.PendingLogin;

public class PendingLoginNotifier  {
  private final Observers observers;
  private final PendingLogin pendingLogin;

  public PendingLoginNotifier(Observers observers, PendingLogin pendingLogin) {
    this.observers = observers;
    this.pendingLogin = pendingLogin;
  }

  public void pendingLogin() {
    observers.notify(PendingLoginObserver.class,
        new Notifier<PendingLoginObserver>() {
          public void notify(PendingLoginObserver observer) {
            observer.pendingLogin(pendingLogin);
          }
    });
  }

  public void loginFailed(final LoginStatus status) {
    observers.notify(PendingLoginObserver.class,
        new Notifier<PendingLoginObserver>() {
          public void notify(PendingLoginObserver observer) {
            observer.loginFailed(status, pendingLogin);
          }
    });
  }

  public void loginSucceeded(final Ranking currentRanking) {
    observers.notify(PendingLoginObserver.class,
        new Notifier<PendingLoginObserver>() {
          public void notify(PendingLoginObserver observer) {
            observer.loginSucceeded(currentRanking);
          }
    });
  }

  public void serverBusy() {
    observers.notify(PendingLoginObserver.class,
        new Notifier<PendingLoginObserver>() {
          public void notify(PendingLoginObserver observer) {
            observer.serverBusy();
          }});
  }
}
