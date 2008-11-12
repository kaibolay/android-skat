package de.bolay.skat.net.server.notifiers;

import de.bolay.pubsub.Notifier;
import de.bolay.pubsub.Observers;
import de.bolay.skat.net.client.observers.ConnectionObserver;

public class ConnectionNotifier {
  private final Observers observers;

  public ConnectionNotifier(Observers observers) {
    this.observers = observers;
  }

  public void disconnected() {
    observers.notify(ConnectionObserver.class,
        new Notifier<ConnectionObserver>() {
          public void notify(ConnectionObserver observer) {
            observer.disconnected();
          }
    });
  }
}