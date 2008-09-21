package de.bolay.skat.net.client.observers;

import de.bolay.pubsub.Observer;
import de.bolay.skat.net.Ranking;

public interface MainLobbyObserver extends Observer {
  void playerJoined(String name, Ranking ranking);
  void playerLeft(String name);
  void serverNotificationReceived(String html);
  void chatMessageReceived(String sender, String text);
}
