package de.bolay.skat.net.client.observers;

import de.bolay.pubsub.Observer;

public interface ConnectionObserver extends Observer {
  void disconnected();
}
