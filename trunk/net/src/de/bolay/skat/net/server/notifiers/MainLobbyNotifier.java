package de.bolay.skat.net.server.notifiers;

import de.bolay.pubsub.Notifier;
import de.bolay.pubsub.Observers;
import de.bolay.skat.net.Ranking;
import de.bolay.skat.net.client.observers.MainLobbyObserver;
import de.bolay.skat.net.client.observers.MainLobbyObserver.MainLobby;

public class MainLobbyNotifier  {
  private final Observers observers;

  public MainLobbyNotifier(Observers observers, final MainLobby mainLobby) {
    this.observers = observers;
    observers.notify(MainLobbyObserver.class, 
        new Notifier<MainLobbyObserver>() {
          public void notify(MainLobbyObserver observer) {
            observer.entered(mainLobby);
          }
    });      
  }
    
  public void serverNotification(final String html) {
    observers.notify(MainLobbyObserver.class, 
        new Notifier<MainLobbyObserver>() {
          public void notify(MainLobbyObserver observer) {
            observer.serverNotificationReceived(html);
          }
    });
  }
    
  public void chatMessage(final String sender, final String text) {
    observers.notify(MainLobbyObserver.class,
        new Notifier<MainLobbyObserver>() {
          public void notify(MainLobbyObserver observer) {
            observer.chatMessageReceived(sender, text);
          }
    });
  }
    
  public void playerJoined(final String name, final Ranking ranking) {
    observers.notify(MainLobbyObserver.class,
        new Notifier<MainLobbyObserver>() {
          public void notify(MainLobbyObserver observer) {
            observer.playerJoined(name, ranking);
          }
    });
  }
    
  public void playerLeft(final String name) {
    observers.notify(MainLobbyObserver.class,
        new Notifier<MainLobbyObserver>() {
          public void notify(MainLobbyObserver observer) {
            observer.playerLeft(name);
          }
    });
  }
}
