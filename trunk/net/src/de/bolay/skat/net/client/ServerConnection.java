package de.bolay.skat.net.client;

import de.bolay.skat.net.client.observers.ObserverFactory;

public interface ServerConnection {
  /** Open connection (after installing observers */
  void open(ObserverFactory observerFactory);

  /** Close connection (by shutting down handler) */
  void close();

  /** Check on the status of the connection */
  boolean isUp();
}
