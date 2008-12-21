package de.bolay.skat.net.server.fake;

import de.bolay.pubsub.Observer;
import de.bolay.pubsub.Observers;
import de.bolay.skat.Position;
import de.bolay.skat.net.client.ServerConnection;
import de.bolay.skat.net.server.notifiers.ConnectionNotifier;

public class FakeServer implements ServerConnection, Runnable {
  private static final String FAKE_PASSWORD = "fake";
  private static final String[] OMA_NAMES = {"Oma #1", "Oma #2"};
  final Observers observers;
  private boolean isUp;

  public void run() {
    String playerName = new FakePendingLogin(observers, FAKE_PASSWORD,
        OMA_NAMES[0], OMA_NAMES[1]).getPlayerName();
    while (isUp()) {
      Deal deal = new Deal(playerName, OMA_NAMES[0], OMA_NAMES[1]);
      new FakeMainLobby(observers, playerName, deal).handle();
      while (isUp()) {
        new FakeTableLobby(observers, playerName, deal).handle();
        if (!isUp()) {
          break;
        }
        BiddingResult biddingResult =
            new FakeBidding(observers, playerName, deal).handle();
        if (!isUp()) {
          break;
        }
        if (biddingResult != null) {
          Position playCard = Position.FORE_HAND;
          while (isUp() && playCard != null) {
            playCard = new FakeTrick(observers, playerName, deal, biddingResult)
                .handle(playCard);
          }
        }
        deal.next();
      }
    }
    close();
  }

  public FakeServer() {
    this.observers = new Observers();
    isUp = false;
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
    isUp = true;
  }
}
