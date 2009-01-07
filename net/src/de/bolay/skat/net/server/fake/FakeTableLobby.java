package de.bolay.skat.net.server.fake;

import de.bolay.skat.net.client.observers.TableLobbyObserver;
import de.bolay.skat.net.client.observers.LobbyObserver.Lobby;

class FakeTableLobby extends FakeLobby implements Lobby {
  private final TableLobbyObserver tableLobbyObserver;

  FakeTableLobby(TableLobbyObserver tableLobbyObserver, String playerName,
      String[] omaNames) {
    super(tableLobbyObserver, playerName, omaNames);
    this.tableLobbyObserver = tableLobbyObserver;
  }

  @Override
  public void handle() {
    super.handle();
  }

  @Override
  protected void join(String omaName) {
    tableLobbyObserver.playerJoined(omaName);
  }
}