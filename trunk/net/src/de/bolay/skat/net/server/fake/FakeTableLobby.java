package de.bolay.skat.net.server.fake;

import de.bolay.pubsub.Observers;
import de.bolay.skat.net.client.observers.LobbyObserver.Lobby;
import de.bolay.skat.net.server.notifiers.TableLobbyNotifier;

class FakeTableLobby extends FakeLobby implements Lobby {
  private final TableLobbyNotifier notifier;

  FakeTableLobby(Observers observers, String playerName, String[] omaNames) {
    super(playerName, omaNames);
    notifier = new TableLobbyNotifier(observers, this);
  }

  public void handle() {
    super.handle(notifier);
  }

  @Override
  protected void join(String omaName) {
    notifier.playerJoined(omaName);
  }
}