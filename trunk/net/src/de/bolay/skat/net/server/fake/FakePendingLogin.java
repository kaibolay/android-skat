package de.bolay.skat.net.server.fake;

import de.bolay.skat.net.Ranking;
import de.bolay.skat.net.client.observers.PendingLoginObserver;
import de.bolay.skat.net.client.observers.PendingLoginObserver.LoginStatus;
import de.bolay.skat.net.client.observers.PendingLoginObserver.PendingLogin;

class FakePendingLogin implements PendingLogin {
  private static final Ranking NO_RANKING = new Ranking(0, 0, 0, 0, 0);

  private final PendingLoginObserver pendingLoginObserver;
  private final String fakePassword;
  private final String oma1Name;
  private final String oma2Name;

  private String playerName;

  FakePendingLogin(PendingLoginObserver pendingLoginObserver,
      String fakePassword, String oma1Name, String oma2Name) {
    this.pendingLoginObserver = pendingLoginObserver;
    this.fakePassword = fakePassword;
    this.oma1Name = oma1Name;
    this.oma2Name = oma2Name;
    pendingLoginObserver.pendingLogin(this);
  }

  public void attemptLogin(String username, String password) {
    if (fakePassword.equals(password) &&
        !username.equals(oma1Name) && !username.equals(oma2Name)) {
      playerName = username;
      pendingLoginObserver.loginSucceeded(NO_RANKING);
    } else {
      pendingLoginObserver.loginFailed(LoginStatus.WRONG_CREDENTIALS, this);
    }
  }

  String getPlayerName() {
    return playerName;
  }
}
