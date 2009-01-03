package de.bolay.skat.net.auto;

import de.bolay.pubsub.Observer;

public interface RoundCompletedObserver extends Observer {
  void roundCompleted();
}
