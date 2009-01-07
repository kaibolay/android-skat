package de.bolay.skat.net.server.fake;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import de.bolay.log.Logger;
import de.bolay.skat.Position;
import de.bolay.skat.net.client.observers.ObserverFactory;
import de.bolay.skat.net.server.ServerConnection;

public class FakeServer implements ServerConnection, Runnable {
  private static final String FAKE_PASSWORD = "fake";
  private static final String[] OMA_NAMES = {"Oma #1", "Oma #2"};

  private final Logger.Factory logFactory;
  private final ExecutorService executorService;

  private ObserverFactory observerFactory;

  public void run() {
    String playerName = new FakePendingLogin(
        observerFactory.createPendingLoginObserver(), FAKE_PASSWORD,
        OMA_NAMES[0], OMA_NAMES[1]).getPlayerName();
    new FakeMainLobby(observerFactory.createMainLobbyObserver(), playerName,
        OMA_NAMES).handle();
    if (!isUp()) {
      return;
    }
    new FakeTableLobby(observerFactory.createTableLobbyObserver(), playerName,
        OMA_NAMES).handle();

    Deal deal = new Deal(playerName, OMA_NAMES[0], OMA_NAMES[1]);
    while (isUp()) {
      BiddingResult biddingResult =
          new FakeBidding(logFactory, observerFactory.createBiddingObserver(),
              playerName, deal).handle();
      if (biddingResult != null) {
        Position playCard = Position.FORE_HAND;
        while (isUp() && playCard != null) {
          playCard = new FakeTrick(logFactory,
              observerFactory.createTrickObserver(), playerName,
              biddingResult).handle(playCard);
        }
      }
      deal.next();
    }
  }

  public FakeServer(Logger.Factory logFactory) {
    this.logFactory = logFactory;
    observerFactory = null;
    executorService = Executors.newSingleThreadExecutor();
  }

  public synchronized void close() {
    if (observerFactory != null) {
      observerFactory.createConnectionObserver().disconnected();
      observerFactory = null;
    }
  }

  public synchronized boolean isUp() {
    return (observerFactory != null);
  }

  public synchronized void open(
      @SuppressWarnings("hiding") ObserverFactory observerFactory) {
    if (isUp()) {
      throw new IllegalStateException("FakeServer connection is already open");
    }
    this.observerFactory = observerFactory;
    executorService.execute(this);
  }
}
