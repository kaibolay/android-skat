package de.bolay.skat.net.client.observers;

import de.bolay.pubsub.Observer;

public interface PassiveBiddingObserver extends Observer {
  void hearBid(String playerName, int value);
  void hearResponse(String playerName, boolean accepted);
}
