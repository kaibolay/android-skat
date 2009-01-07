package de.bolay.skat.net.server.fake;

import de.bolay.skat.net.Ranking;
import de.bolay.skat.net.client.observers.MainLobbyObserver;
import de.bolay.skat.net.client.observers.LobbyObserver.Lobby;

class FakeMainLobby extends FakeLobby implements Lobby {
  private static final Ranking NO_RANKING = new Ranking(0, 0, 0, 0, 0);

  private final MainLobbyObserver mainLobbyObserver;

  FakeMainLobby(MainLobbyObserver mainLobbyObserver, String playerName,
      String[] omaNames) {
    super(mainLobbyObserver, playerName, omaNames);
    this.mainLobbyObserver = mainLobbyObserver;
  }

  @Override
  public void handle() {
    super.handle();
  }

  @Override
  protected void join(String omaName) {
    mainLobbyObserver.playerJoined(omaName, NO_RANKING);
  }
}