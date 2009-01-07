package de.bolay.skat.net.auto;

import java.util.concurrent.atomic.AtomicInteger;

import de.bolay.log.Logger;
import de.bolay.skat.net.client.observers.BiddingObserver;
import de.bolay.skat.net.client.observers.ConnectionObserver;
import de.bolay.skat.net.client.observers.MainLobbyObserver;
import de.bolay.skat.net.client.observers.ObserverFactory;
import de.bolay.skat.net.client.observers.PendingLoginObserver;
import de.bolay.skat.net.client.observers.TableLobbyObserver;
import de.bolay.skat.net.client.observers.TrickObserver;
import de.bolay.skat.net.server.ServerConnection;

public class AutoPlayer implements RoundCompletedObserver, ConnectionObserver {
  private static final long WAIT = 3 * 1000;
  private static final long TIMEOUT = 30 * 1000;

  private final ServerConnection connection;
  private final Logger log;
  private final AtomicInteger roundsRemaining = new AtomicInteger();
  private final ObserverFactory observerFactory;

  public AutoPlayer(final Logger.Factory logFactory,
      ServerConnection connection,
      final String username, final String password) {
    log = logFactory.getLogger(AutoPlayer.class.getName());
    this.connection = connection;
    final RoundCompletedObserver roundCompletedObserver = this;
    final ConnectionObserver connectionObserver = this;
    observerFactory = new ObserverFactory() {
      public ConnectionObserver createConnectionObserver() {
        return connectionObserver;
      }

      public PendingLoginObserver createPendingLoginObserver() {
        return new AutoPendingLoginObserver(logFactory, username, password);
      }

      public MainLobbyObserver createMainLobbyObserver() {
        return new AutoMainLobbyObserver(logFactory, username);
      }

      public TableLobbyObserver createTableLobbyObserver() {
        return new AutoTableLobbyObserver(logFactory, username);
      }

      public BiddingObserver createBiddingObserver() {
        return new AutoBiddingObserver(logFactory, username,
            roundCompletedObserver);
      }

      public TrickObserver createTrickObserver() {
        return new AutoTrickObserver(logFactory, username,
            roundCompletedObserver);
      }};
  }

  public void play(int numGames) {
    roundsRemaining.set(numGames);
    log.info("opening connection");
    connection.open(observerFactory);
    while (continuePlaying()) {
      waitForEndOfRound(numGames - roundsRemaining.get() + 1);
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

  public void disconnected() {
    log.error("server disconnected");
    roundsRemaining.set(0);
  }
}
