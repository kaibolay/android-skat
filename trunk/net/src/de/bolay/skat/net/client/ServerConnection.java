package de.bolay.skat.net.client;

public interface ServerConnection {
  /** Check on the status of the connection */
  boolean isUp();

  /** Close connection (by shutting down handler) */
  void close();

  /** (Re-)Log in */
  void login(String username, String password);
}
