package de.bolay.skat.net.client;

import java.util.concurrent.atomic.AtomicInteger;

import de.bolay.log.Logger;
import de.bolay.skat.net.auto.AutoBiddingObserver;
import de.bolay.skat.net.auto.AutoMainLobbyObserver;
import de.bolay.skat.net.auto.AutoPendingLoginObserver;
import de.bolay.skat.net.auto.AutoTableLobbyObserver;
import de.bolay.skat.net.auto.AutoTrickObserver;
import de.bolay.skat.net.auto.RoundCompletedObserver;

public class AutoPlayer implements RoundCompletedObserver {
  private static final long WAIT = 3 * 1000;
  private static final long TIMEOUT = 30 * 1000;

  private final ServerConnection connection;
  private final Logger log;
  private final AtomicInteger roundsRemaining = new AtomicInteger();

  public AutoPlayer(Logger.Factory logFactory, ServerConnection connection,
      String username, String password) {
    log = logFactory.getLogger(AutoPlayer.class.getName());
    this.connection = connection;

    connection.addObserver(new AutoPendingLoginObserver(logFactory,
        username, password));
    connection.addObserver(new AutoMainLobbyObserver(logFactory, username));
    connection.addObserver(new AutoTableLobbyObserver(logFactory, username));
    connection.addObserver(new AutoBiddingObserver(logFactory, username,
        this));
    connection.addObserver(new AutoTrickObserver(logFactory, username,
        this));
  }

  public void play(int numGames) {
    roundsRemaining.set(numGames);
    log.info("opening connection");
    connection.open();
    while (continuePlaying()) {
      waitForEndOfRound(roundsRemaining.get());
    }
    log.info("done");
  }

  private void waitForEndOfRound(int round) {
    for (int i = 0; i < TIMEOUT/WAIT; i++) {
      if (roundsRemaining.get() < round) {
        return;
      }
      if (!connection.isUp()) {
        throw new RuntimeException("Connection died");
      }
      try {
        log.debug("waiting for round #%d to end...", round);
        Thread.sleep(WAIT);
      } catch (InterruptedException ie) {
        throw new RuntimeException(ie);
      }
    }
    throw new RuntimeException("timeout waiting for round #" + round);
  }

  private boolean continuePlaying() {
    return roundsRemaining.get() > 0;
  }

  public void roundCompleted() {
    log.info("completed round. %d more to go...",
        roundsRemaining.decrementAndGet());
    if (!continuePlaying()) {
      connection.close();
    }
  }
}
