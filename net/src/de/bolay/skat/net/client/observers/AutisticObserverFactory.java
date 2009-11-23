package de.bolay.skat.net.client.observers;

import java.util.Set;

import de.bolay.skat.Card;
import de.bolay.skat.Game;
import de.bolay.skat.Level;
import de.bolay.skat.Position;
import de.bolay.skat.net.Ranking;

public abstract class AutisticObserverFactory implements ObserverFactory {

  @Override public BiddingObserver createBiddingObserver() {
    return new BiddingObserver() {
      @Override public void bidSolicited(String listenerName, int nextValue, Bid bid) {}
      @Override public void biddingEnded(String soloPlayer) {}
      @Override public void gameAnnounced(String soloPlayer, int bidValue, Game game,
          Level bidLevel) {}
      @Override public void gotCards(Set<Card> cards, Position position, String leftOpponent,
          String rightOpponent) {}
      @Override public void gotSkat(Set<Card> skat, AnnounceGame announceGame) {}
      @Override public void heardAccept(String announcerName, int value) {}
      @Override public void heardBid(String challengerName, String listenerName, int value) {}
      @Override public void heardPass(String announcerName, int value) {}
      @Override public void repsonseSolicited(String challengerName, int value, Response response) {}
      @Override public void skatPickedUp(String soloPlayer) {}
      @Override public void wonBidding(int bidValue, PickSkat pickSkat) {}
    };
  }

  @Override public ConnectionObserver createConnectionObserver() {
    return new ConnectionObserver() {
      @Override public void disconnected() {}
    };
  }

  @Override public MainLobbyObserver createMainLobbyObserver() {
    return new MainLobbyObserver() {
      @Override public void playerJoined(String name, Ranking ranking) {}
      @Override public void chatMessageReceived(String sender, String text) {}
      @Override public void entered(Lobby lobby) {}
      @Override public void playerLeft(String name) {}
      @Override public void serverNotificationReceived(String html) {}
    };
  }

  @Override public PendingLoginObserver createPendingLoginObserver() {
    return new PendingLoginObserver() {
      @Override public void loginFailed(LoginStatus status, PendingLogin pendingLogin) {}
      @Override public void loginSucceeded(Ranking currentRanking) {}
      @Override public void pendingLogin(PendingLogin pendingLogin) {}
      @Override public void serverBusy() {}
    };
  }

  @Override public TableLobbyObserver createTableLobbyObserver() {
    return new TableLobbyObserver() {
      @Override public void playerJoined(String name) {}
      @Override public void chatMessageReceived(String sender, String text) {}
      @Override public void entered(Lobby lobby) {}
      @Override public void playerLeft(String name) {}
      @Override public void serverNotificationReceived(String html) {}
    };
  }

  @Override public TrickObserver createTrickObserver() {
    return new TrickObserver() {
      @Override public void cardPlayed(String playerName, Card card) {}
      @Override public void cardSolicited(Turn yourTurn) {}
      @Override public void gameOver(boolean won, int point, int score) {}
      @Override public void gameStarts(Game announcedGame, Level announcedLevel, Set<Card> cards) {}
      @Override public void newTrick(Position position) {}
    };
  }
}
