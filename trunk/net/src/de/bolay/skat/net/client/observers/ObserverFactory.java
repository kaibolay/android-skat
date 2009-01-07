package de.bolay.skat.net.client.observers;

public interface ObserverFactory {
  ConnectionObserver createConnectionObserver();
  PendingLoginObserver createPendingLoginObserver();
  MainLobbyObserver createMainLobbyObserver();
  TableLobbyObserver createTableLobbyObserver();
  BiddingObserver createBiddingObserver();
  TrickObserver createTrickObserver();
}