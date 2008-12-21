package de.bolay.skat.net.auto;

import static org.junit.Assert.fail;
import de.bolay.skat.net.Ranking;
import de.bolay.skat.net.client.observers.PendingLoginObserver;

public class AutoPendingLoginObserver implements PendingLoginObserver {

  private final String username;
  private final String password;

  public AutoPendingLoginObserver (String username, String password) {
    this.username = username;
    this.password = password;
  }

  public void pendingLogin(PendingLogin pendingLogin) {
    System.out.println("PendingLoginObserver.pendingLogin()");
    pendingLogin.attemptLogin(username, password);
  }

  public void loginFailed(LoginStatus status, PendingLogin pendingLogin) {
    fail("login failed: " + status);
  }

  public void loginSucceeded(Ranking ranking) {
    System.out.println("PendingLoginObserver.loginSucceeded("
        + ranking + ")");
  }

  public void serverBusy() {
    fail("Server busy");
  }
}