package de.bolay.skat.net.client;

import static org.junit.Assert.fail;
import de.bolay.skat.net.auto.AutoBiddingObserver;
import de.bolay.skat.net.auto.AutoMainLobbyObserver;
import de.bolay.skat.net.auto.AutoPendingLoginObserver;
import de.bolay.skat.net.auto.AutoTableLobbyObserver;
import de.bolay.skat.net.auto.AutoTrickObserver;

public class AutoPlayer {
  private static final long WAIT = 3 * 1000;
  private static final long TIMEOUT = 60 * 1000;

  private final ServerConnection connection;

  public AutoPlayer(ServerConnection connection,
      String username, String password) {
    this.connection = connection;

    connection.addObserver(new AutoPendingLoginObserver(
        username, password));
    connection.addObserver(new AutoMainLobbyObserver());
    connection.addObserver(new AutoTableLobbyObserver());
    connection.addObserver(new AutoBiddingObserver());
    connection.addObserver(new AutoTrickObserver());
  }

  public void play() {
    connection.open();
    for (int i = 0; i < TIMEOUT/WAIT; i++) {
      if (!connection.isUp()) {
        fail("Connection died");
      }
      System.out.println("live connection in iteration #" + i + " sleeping...");
      try {
        Thread.sleep(WAIT);
      } catch (InterruptedException ie) {
        fail(ie.getMessage());
      }
    }
  }
}
