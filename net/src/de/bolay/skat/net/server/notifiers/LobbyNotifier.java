package de.bolay.skat.net.server.notifiers;

import de.bolay.pubsub.Notifier;
import de.bolay.pubsub.Observers;
import de.bolay.skat.net.client.observers.LobbyObserver;
import de.bolay.skat.net.client.observers.LobbyObserver.Lobby;

public class LobbyNotifier {
  protected final Observers observers;
  protected final Lobby lobby;
  protected final Class<? extends LobbyObserver> observerType;

  public LobbyNotifier(Observers observers,
      Class<? extends LobbyObserver> observerType, Lobby lobby) {
    this.observers = observers;
    this.observerType = observerType;
    this.lobby = lobby;
  }

  public void entered() {
    observers.notify(observerType,
        new Notifier<LobbyObserver>() {
          public void notify(LobbyObserver observer) {
            observer.entered(lobby);
          }
    });
  }

  public void serverNotification(final String html) {
    observers.notify(observerType,
        new Notifier<LobbyObserver>() {
          public void notify(LobbyObserver observer) {
            observer.serverNotificationReceived(html);
          }
    });
  }

  public void chatMessage(final String sender, final String text) {
    observers.notify(observerType,
        new Notifier<LobbyObserver>() {
          public void notify(LobbyObserver observer) {
            observer.chatMessageReceived(sender, text);
          }
    });
  }

  public void playerLeft(final String name) {
    observers.notify(observerType,
        new Notifier<LobbyObserver>() {
          public void notify(LobbyObserver observer) {
            observer.playerLeft(name);
          }
    });
  }

}
