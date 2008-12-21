package de.bolay.skat.net.server.fake;

import de.bolay.pubsub.Observers;
import de.bolay.skat.Position;
import de.bolay.skat.net.client.observers.TableLobbyObserver.TableLobby;
import de.bolay.skat.net.server.notifiers.TableLobbyNotifier;

class FakeTableLobby implements TableLobby {
  private final TableLobbyNotifier notifier;
  private final String playerName;
  private final Deal deal;

  FakeTableLobby(Observers observers, String playerName, Deal deal) {
    notifier = new TableLobbyNotifier(observers, this);
    this.playerName = playerName;
    this.deal = deal;
  }

  void handle() {
    notifier.serverNotification("Welcome to the <b>fake</b> lobby!");
    for (Position position : Position.values()) {
      final String name = deal.getName(position);
      if (!playerName.equals(name)) {
        join(name);
      }
    }
    Position playerPosition = deal.getPosition(playerName);
    notifier.gotCards(playerPosition, deal.getCards(playerName),
        deal.getName(playerPosition.after()),
        deal.getName(playerPosition.before()));
  }

  private void join(String omaName) {
    notifier.playerJoined(omaName);
    notifier.chatMessage(omaName, "Hello again " + playerName + "!");
  }

  public void sendChatMessage(String text) {
    notifier.chatMessage(playerName, text);
  }
}