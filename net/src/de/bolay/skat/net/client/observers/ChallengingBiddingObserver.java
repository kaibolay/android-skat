package de.bolay.skat.net.client.observers;

import de.bolay.pubsub.Observer;

public interface ChallengingBiddingObserver extends Observer {
  public interface Bid {
    void bid(int value);
    void pass();
  }
  
  void bidSolicited(String playerName, int nextValue, Bid bid);
}
