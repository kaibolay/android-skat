package de.bolay.skat.net.server.fake;

import de.bolay.pubsub.Observers;
import de.bolay.skat.net.Ranking;
import de.bolay.skat.net.client.observers.PendingLoginObserver.LoginStatus;
import de.bolay.skat.net.client.observers.PendingLoginObserver.PendingLogin;
import de.bolay.skat.net.server.notifiers.PendingLoginNotifier;

class FakePendingLogin implements PendingLogin {
  private static final Ranking NO_RANKING = new Ranking(0, 0, 0, 0, 0);

  private final PendingLoginNotifier notifier;
  private final String fakePassword;
  private final String oma1Name;
  private final String oma2Name;

  private String playerName;

  FakePendingLogin(Observers observers, String fakePassword,
      String oma1Name, String oma2Name) {
    notifier = new PendingLoginNotifier(observers, this);
    this.fakePassword = fakePassword;
    this.oma1Name = oma1Name;
    this.oma2Name = oma2Name;
    notifier.pendingLogin();
  }

  public void attemptLogin(String username, String password) {
    if (fakePassword.equals(password) &&
        !username.equals(oma1Name) && !username.equals(oma2Name)) {
      playerName = username;
      notifier.loginSucceeded(NO_RANKING);
    } else {
      notifier.loginFailed(LoginStatus.WRONG_CREDENTIALS);
    }
  }

  String getPlayerName() {
    return playerName;
  }
}
