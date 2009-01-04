package de.bolay.skat.net.client.observers;

import de.bolay.skat.net.Ranking;

public interface MainLobbyObserver extends LobbyObserver {
  void playerJoined(String name, Ranking ranking);
}
