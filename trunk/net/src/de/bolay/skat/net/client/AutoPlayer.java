package de.bolay.skat.net.client;

import de.bolay.log.Logger;
import de.bolay.skat.net.auto.AutoBiddingObserver;
import de.bolay.skat.net.auto.AutoMainLobbyObserver;
import de.bolay.skat.net.auto.AutoPendingLoginObserver;
import de.bolay.skat.net.auto.AutoTableLobbyObserver;
import de.bolay.skat.net.auto.AutoTrickObserver;

public class AutoPlayer {
  private static final long WAIT = 3 * 1000;
  private static final long TIMEOUT = 60 * 1000;

  private final ServerConnection connection;
  private final Logger log;

  public AutoPlayer(Logger.Factory logFactory, ServerConnection connection,
      String username, String password) {
    log = logFactory.getLogger(AutoPlayer.class.getName());
    this.connection = connection;

    connection.addObserver(new AutoPendingLoginObserver(logFactory,
        username, password));
    connection.addObserver(new AutoMainLobbyObserver(logFactory));
    connection.addObserver(new AutoTableLobbyObserver(logFactory));
    connection.addObserver(new AutoBiddingObserver(logFactory));
    connection.addObserver(new AutoTrickObserver(logFactory));
  }

  public void play() {
    log.info("opening connection");
    connection.open();
    for (int i = 0; i < TIMEOUT/WAIT; i++) {
      if (!connection.isUp()) {
        throw new IllegalStateException("connection died");
      }
      log.debug("live connection in iteration #" + i + " sleeping...");
      try {
        Thread.sleep(WAIT);
      } catch (InterruptedException ie) {
        throw new RuntimeException(ie);
      }
    }
    log.info("done");
  }
}
