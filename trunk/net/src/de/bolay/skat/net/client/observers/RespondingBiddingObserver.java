package de.bolay.skat.net.client.observers;

import de.bolay.pubsub.Observer;

public interface RespondingBiddingObserver extends Observer {
  public interface Response {
    void accept();
    void pass();
  }
  
  void offerReceived(String playerName, int value, Response response);
}
