package de.bolay.skat.net.server.fake;

import de.bolay.pubsub.Observers;
import de.bolay.skat.net.Ranking;
import de.bolay.skat.net.client.observers.MainLobbyObserver.MainLobby;
import de.bolay.skat.net.server.notifiers.MainLobbyNotifier;

class FakeMainLobby implements MainLobby {
  private static final Ranking NO_RANKING = new Ranking(0, 0, 0, 0, 0);

  private final MainLobbyNotifier notifier;
  private final String playerName;
  private final String[] omaNames;

  FakeMainLobby(Observers observers, String playerName, String[] omaNames) {
    notifier = new MainLobbyNotifier(observers, this);
    this.playerName = playerName;
    this.omaNames = omaNames;
  }

  void handle() {
    notifier.entered();
    notifier.serverNotification("Welcome to the <b>fake</b> lobby!");
    join(omaNames[0]);
    join(omaNames[1]);
  }

  private void join(String omaName) {
    notifier.playerJoined(omaName, NO_RANKING);
    notifier.chatMessage(omaName, "Hello " + playerName + "!");
  }

  public void sendChatMessage(String text) {
    notifier.chatMessage(playerName, text);
  }
}