package de.bolay.skat.net.auto;

import de.bolay.log.Logger;
import de.bolay.skat.net.Ranking;
import de.bolay.skat.net.client.observers.PendingLoginObserver;

public class AutoPendingLoginObserver implements PendingLoginObserver {
  private final Logger log;
  private final String username;
  private final String password;

  public AutoPendingLoginObserver(Logger.Factory logFactory,
      String username, String password) {
    log = logFactory.getLogger(AutoPendingLoginObserver.class.getName()
        + " for " + username);
    this.username = username;
    this.password = password;
  }

  public void pendingLogin(PendingLogin pendingLogin) {
    log.info("pendingLogin()");
    pendingLogin.attemptLogin(username, password);
  }

  public void loginFailed(LoginStatus status, PendingLogin pendingLogin) {
    log.error("loginFailed(%s)", pendingLogin);
    throw new IllegalStateException();
  }

  public void loginSucceeded(Ranking ranking) {
    log.info("loginSucceeded(%s)", ranking);
  }

  public void serverBusy() {
    log.error("serverBusy()");
    throw new IllegalStateException();
  }
}