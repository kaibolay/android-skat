package de.bolay.skat.net.server.fake;

import de.bolay.skat.net.server.notifiers.LobbyNotifier;

abstract class FakeLobby {
  private final String playerName;
  private final String[] omaNames;

  private LobbyNotifier notifier;

  FakeLobby(String playerName, String[] omaNames) {
    this.playerName = playerName;
    this.omaNames = omaNames;
  }

  protected void handle(@SuppressWarnings("hiding") LobbyNotifier notifier) {
    this.notifier = notifier;
    notifier.entered();
    notifier.serverNotification("Welcome to the <b>fake</b> lobby!");
    joinAndChat(omaNames[0]);
    joinAndChat(omaNames[1]);
  }

  void joinAndChat(String omaName) {
    join(omaName);
    notifier.chatMessage(omaName, "Hello " + playerName + "!");
  }

  protected abstract void join(String omaName);

  public void sendChatMessage(String text) {
    notifier.chatMessage(playerName, text);
  }
}
