package de.bolay.skat.net.client.observers;

import de.bolay.pubsub.Observer;

public interface TableLobbyObserver extends Observer {
  public interface TableLobby {
    void sendChatMessage(String text);
  }

  void entered(TableLobby tableLobby);
  void serverNotificationReceived(String html);
  void playerJoined(String name);
  void playerLeft(String name);
  void chatMessageReceived(String sender, String text);
}
