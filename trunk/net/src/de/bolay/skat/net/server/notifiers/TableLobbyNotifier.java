package de.bolay.skat.net.server.notifiers;

import java.util.Set;

import de.bolay.pubsub.Notifier;
import de.bolay.pubsub.Observers;
import de.bolay.skat.Card;
import de.bolay.skat.Position;
import de.bolay.skat.net.client.observers.TableLobbyObserver;
import de.bolay.skat.net.client.observers.TableLobbyObserver.TableLobby;

public class TableLobbyNotifier  {
  private final Observers observers;
  private final TableLobby tableLobby;

  public TableLobbyNotifier(Observers observers, TableLobby tableLobby) {
    this.observers = observers;
    this.tableLobby = tableLobby;
  }

  public void entered() {
    observers.notify(TableLobbyObserver.class,
        new Notifier<TableLobbyObserver>() {
          public void notify(TableLobbyObserver observer) {
            observer.entered(tableLobby);
          }
    });
  }

  public void serverNotification(final String html) {
    observers.notify(TableLobbyObserver.class,
        new Notifier<TableLobbyObserver>() {
          public void notify(TableLobbyObserver observer) {
            observer.serverNotificationReceived(html);
          }
    });
  }

  public void chatMessage(final String sender, final String text) {
    observers.notify(TableLobbyObserver.class,
        new Notifier<TableLobbyObserver>() {
          public void notify(TableLobbyObserver observer) {
            observer.chatMessageReceived(sender, text);
          }
    });
  }

  public void playerJoined(final String name) {
    observers.notify(TableLobbyObserver.class,
        new Notifier<TableLobbyObserver>() {
          public void notify(TableLobbyObserver observer) {
            observer.playerJoined(name);
          }
    });
  }

  public void playerLeft(final String name) {
    observers.notify(TableLobbyObserver.class,
        new Notifier<TableLobbyObserver>() {
          public void notify(TableLobbyObserver observer) {
            observer.playerLeft(name);
          }
    });
  }

  public void gotCards(final Position position, final Set<Card> hand,
      final String leftOpponent, final String rightOpponent) {
    observers.notify(TableLobbyObserver.class,
        new Notifier<TableLobbyObserver>() {
          public void notify(TableLobbyObserver observer) {
            observer.gotCards(hand, position, leftOpponent, rightOpponent);
          }
        });
  }
}
