package de.bolay.skat.net.client.observers;

import de.bolay.pubsub.Observer;
import de.bolay.skat.net.Ranking;

public interface MainLobbyObserver extends Observer {
  public interface MainLobby {
    void sendChatMessage(String text);
  }

  void entered(MainLobby mainLobby);
  void serverNotificationReceived(String html);
  void playerJoined(String name, Ranking ranking);
  void playerLeft(String name);
  void chatMessageReceived(String sender, String text);
}
