package de.bolay.skat.net.client.observers;

import de.bolay.pubsub.Observer;
import de.bolay.skat.net.Ranking;

public interface ConnectionObserver extends Observer {

  public enum LoginStatus {
    SUCCESS, WRONG_CREDENTIALS, USER_DISABLED, SERVER_BUSY, UNKNOWN
  }

  void loginAttempted(LoginStatus status);
  void rankingReceived(Ranking ranking);
  void disconnected();
}
