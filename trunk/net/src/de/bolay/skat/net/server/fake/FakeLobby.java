package de.bolay.skat.net.server.fake;

import de.bolay.skat.net.client.observers.LobbyObserver;
import de.bolay.skat.net.client.observers.LobbyObserver.Lobby;

abstract class FakeLobby implements Lobby {
  private final String playerName;
  private final String[] omaNames;

  private final LobbyObserver lobbyObserver;

  FakeLobby(LobbyObserver lobbyObserver, String playerName, String[] omaNames) {
    this.lobbyObserver = lobbyObserver;
    this.playerName = playerName;
    this.omaNames = omaNames;
  }

  protected void handle() {
    lobbyObserver.entered(this);
    lobbyObserver.serverNotificationReceived(
        "Welcome to the <b>fake</b> lobby!");
    joinAndChat(omaNames[0]);
    joinAndChat(omaNames[1]);
  }

  void joinAndChat(String omaName) {
    join(omaName);
    lobbyObserver.chatMessageReceived(omaName, "Hello " + playerName + "!");
  }

  protected abstract void join(String omaName);

  public void sendChatMessage(String text) {
    lobbyObserver.chatMessageReceived(playerName, text);
  }
}
