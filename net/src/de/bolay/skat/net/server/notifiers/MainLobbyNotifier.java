package de.bolay.skat.net.server.notifiers;

import de.bolay.pubsub.Notifier;
import de.bolay.pubsub.Observers;
import de.bolay.skat.net.Ranking;
import de.bolay.skat.net.client.observers.MainLobbyObserver;
import de.bolay.skat.net.client.observers.LobbyObserver.Lobby;

public class MainLobbyNotifier extends LobbyNotifier {
  public MainLobbyNotifier(Observers observers, Lobby mainLobby) {
    super(observers, MainLobbyObserver.class, mainLobby);
  }

  public void playerJoined(final String name, final Ranking ranking) {
    observers.notify(MainLobbyObserver.class,
        new Notifier<MainLobbyObserver>() {
          public void notify(MainLobbyObserver observer) {
            observer.playerJoined(name, ranking);
          }
    });
  }
}
