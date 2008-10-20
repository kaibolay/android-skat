package de.bolay.skat.net.client.observers;

import java.util.Set;

import de.bolay.pubsub.Observer;
import de.bolay.skat.Card;
import de.bolay.skat.Game.Position;

public interface TableLobbyObserver extends Observer {
  public interface TableLobby {
    void sendChatMessage(String test);
  }

  void entered(TableLobby tableLobby);
  void serverNotificationReceived(String html);
  void playerJoined(String name);
  void playerLeft(String name);
  void chatMessageReceived(String sender, String text);
  
  void gotCards(Set<Card> hand, Position position);
}
