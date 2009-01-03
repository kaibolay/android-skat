package de.bolay.skat.net.server.fake;

import de.bolay.pubsub.Observers;
import de.bolay.skat.net.client.observers.TableLobbyObserver.TableLobby;
import de.bolay.skat.net.server.notifiers.TableLobbyNotifier;

class FakeTableLobby implements TableLobby {
  private final TableLobbyNotifier notifier;
  private final String playerName;
  private final String[] omaNames;

  FakeTableLobby(Observers observers, String playerName, String[] omaNames) {
    notifier = new TableLobbyNotifier(observers, this);
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
    notifier.playerJoined(omaName);
    notifier.chatMessage(omaName, "Hello again " + playerName + "!");
  }

  public void sendChatMessage(String text) {
    notifier.chatMessage(playerName, text);
  }
}