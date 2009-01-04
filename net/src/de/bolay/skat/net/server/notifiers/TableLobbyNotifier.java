package de.bolay.skat.net.server.notifiers;

import de.bolay.pubsub.Notifier;
import de.bolay.pubsub.Observers;
import de.bolay.skat.net.client.observers.TableLobbyObserver;
import de.bolay.skat.net.client.observers.LobbyObserver.Lobby;

public class TableLobbyNotifier extends LobbyNotifier {
  public TableLobbyNotifier(Observers observers, Lobby tableLobby) {
    super(observers, TableLobbyObserver.class, tableLobby);
  }

  public void playerJoined(final String name) {
    observers.notify(TableLobbyObserver.class,
        new Notifier<TableLobbyObserver>() {
          public void notify(TableLobbyObserver observer) {
            observer.playerJoined(name);
          }
    });
  }
}
