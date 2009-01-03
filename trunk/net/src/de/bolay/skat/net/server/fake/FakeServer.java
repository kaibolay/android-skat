package de.bolay.skat.net.server.fake;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import de.bolay.log.Logger;
import de.bolay.pubsub.Observer;
import de.bolay.pubsub.Observers;
import de.bolay.skat.Position;
import de.bolay.skat.net.client.ServerConnection;
import de.bolay.skat.net.server.notifiers.ConnectionNotifier;

public class FakeServer implements ServerConnection, Runnable {
  private static final String FAKE_PASSWORD = "fake";
  private static final String[] OMA_NAMES = {"Oma #1", "Oma #2"};

  private final Logger.Factory logFactory;
  private final Observers observers;
  private final ExecutorService executorService;

  private boolean isUp;

  public void run() {
    String playerName = new FakePendingLogin(observers, FAKE_PASSWORD,
        OMA_NAMES[0], OMA_NAMES[1]).getPlayerName();
    new FakeMainLobby(observers, playerName, OMA_NAMES).handle();
    if (!isUp()) {
      return;
    }
    new FakeTableLobby(observers, playerName, OMA_NAMES).handle();

    Deal deal = new Deal(playerName, OMA_NAMES[0], OMA_NAMES[1]);
    while (isUp()) {
      BiddingResult biddingResult =
          new FakeBidding(logFactory, observers, playerName, deal).handle();
      if (biddingResult != null) {
        Position playCard = Position.FORE_HAND;
        while (isUp() && playCard != null) {
          playCard = new FakeTrick(logFactory, observers, playerName,
              biddingResult).handle(playCard);
        }
      }
      deal.next();
    }
  }

  public FakeServer(Logger.Factory logFactory) {
    this.logFactory = logFactory;
    observers = new Observers();
    isUp = false;
    executorService = Executors.newSingleThreadExecutor();
  }

  public void addObserver(Observer observer) {
    observers.add(observer);
  }

  public synchronized void close() {
    isUp = false;
    new ConnectionNotifier(observers).disconnected();
  }

  public synchronized boolean isUp() {
    return isUp;
  }

  public synchronized void open() {
    if (isUp) {
      throw new IllegalStateException("FakeServer connection is already open");
    }
    isUp = true;
    executorService.execute(this);
  }
}
