package de.bolay.skat.net.server.fake;

import de.bolay.pubsub.Observers;
import de.bolay.skat.net.Ranking;
import de.bolay.skat.net.client.observers.LobbyObserver.Lobby;
import de.bolay.skat.net.server.notifiers.MainLobbyNotifier;

class FakeMainLobby extends FakeLobby implements Lobby {
  private static final Ranking NO_RANKING = new Ranking(0, 0, 0, 0, 0);

  private final MainLobbyNotifier notifier;

  FakeMainLobby(Observers observers, String playerName, String[] omaNames) {
    super(playerName, omaNames);
    notifier = new MainLobbyNotifier(observers, this);
  }

  public void handle() {
    super.handle(notifier);
  }

  @Override
  protected void join(String omaName) {
    notifier.playerJoined(omaName, NO_RANKING);
  }
}