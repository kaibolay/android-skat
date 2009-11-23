package de.bolay.skat.net.client.observers;

import de.bolay.pubsub.Observer;

public interface LobbyObserver extends Observer {
  public interface Lobby {
    void sendChatMessage(String text);
  }

  void entered(Lobby lobby);
  void serverNotificationReceived(String html);
  void playerLeft(String name);
  void chatMessageReceived(String sender, String text);
}
