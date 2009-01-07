package de.bolay.skat.net.server;

import de.bolay.skat.net.client.observers.ObserverFactory;

public interface ServerConnection {
  /** Open connection */
  void open(ObserverFactory observerFactory);

  /** Close connection (by shutting down handler) */
  void close();

  /** Check on the status of the connection */
  boolean isUp();
}
