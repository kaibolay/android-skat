package de.bolay.skat.net.client.observers;

import de.bolay.pubsub.Observer;
import de.bolay.skat.net.Ranking;

public interface PendingLoginObserver extends Observer {
  public interface PendingLogin {
    void attemptLogin(String username, String password);
  }
  
  public enum LoginStatus {
    WRONG_CREDENTIALS, USER_DISABLED, UNKNOWN
  }

  void pendingLogin(PendingLogin pendingLogin);
  void serverBusy();
  void loginFailed(LoginStatus status, PendingLogin pendingLogin);
  void loginSucceeded(Ranking currentRanking);
}
