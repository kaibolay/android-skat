package de.bolay.skat.net.client;

import de.bolay.pubsub.Observer;

public interface ServerConnection {
  /** add an observer */
  void addObserver(Observer observer);

  /** Open connection (after installing observers */
  void open();
  
  /** Close connection (by shutting down handler) */
  void close();

  /** Check on the status of the connection */
  boolean isUp();
}
