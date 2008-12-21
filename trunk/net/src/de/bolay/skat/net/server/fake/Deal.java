package de.bolay.skat.net.server.fake;

import static java.util.Collections.shuffle;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import de.bolay.skat.Card;
import de.bolay.skat.Position;

class Deal {
  private static class Player {
    private final String name;
    private final Set<Card> cards;

    Player(String name, Set<Card> cards) {
      this.name = name;
      this.cards = cards;
    }

    String getName() {
      return name;
    }

    Set<Card> getCards() {
      return cards;
    }
  }

  private static class Deck {
    private final List<Card> cards = Arrays.asList(Card.values());
    private int picked = 0;

    Deck() {
      shuffle(cards);
    }

    Set<Card> pickCards(int num) {
      if (num < 0 || picked + num > cards.size()) {
        throw new IllegalArgumentException("Already picked " + picked
            + " cards of " + cards.size() + " total, can't pick " + num
            + " more");
      }
      picked += num;
      return new HashSet<Card>(cards.subList(picked-num, picked));
    }

    void assertNoneRemaining() {
      int remaining = cards.size() - picked;
      if (remaining != 0) {
        throw new IllegalStateException("Still have " + remaining);
      }
    }
  }

  private static class RandomPositions {
    private List<Position> positions = new ArrayList<Position>(
        Arrays.asList(Position.values()));

    RandomPositions() {
      shuffle(positions);
    }

    Position pick() {
      return positions.remove(positions.size()-1);
    }

    void assertNoneRemaining() {
      if (positions.size() != 0) {
        throw new IllegalStateException("Still have " + positions.size());
      }
    }
  }

  private final Map<Position, Player> players;
  private final Map<String, Position> positions;
  private Set<Card> skat;

  Deal(String player1, String player2, String player3) {
    players = new HashMap<Position, Player>();
    positions = new HashMap<String, Position>();

    Deck deck = new Deck();
    RandomPositions randomPositions = new RandomPositions();

    seat(player1, randomPositions.pick(), deck);
    seat(player2, randomPositions.pick(), deck);
    seat(player3, randomPositions.pick(), deck);
    if (positions.size() != 3 || players.size() != 3) {
      throw new IllegalArgumentException("Duplicate player names? "
          + player1 + ", " + player2 + ", " + player3 + " - with: "
          + positions + " and " + players);
    }
    randomPositions.assertNoneRemaining();
    skat = deck.pickCards(2);
    deck.assertNoneRemaining();
  }

  private void seat(String name, Position position, Deck deck) {
    players.put(position, new Player(name, deck.pickCards(10)));
    positions.put(name, position);
  }

  /**
   * Move players to the next position and deal new cards
   */
  public void next() {
    Map<Position, String> previous = new HashMap<Position, String>();
    for (Position position : Position.values()) {
      previous.put(position, getName(position));
    }

    Deck deck = new Deck();
    for (Position position : Position.values()) {
      seat(previous.get(position.after()), position, deck);
    }

    skat = deck.pickCards(2);
    deck.assertNoneRemaining();
  }

  Position getPosition(String name) {
    return positions.get(name);
  }

  Set<Card> getCards(String name) {
    return getCards(getPosition(name));
  }

  Set<Card> getCards(Position position) {
    return players.get(position).getCards();
  }

  String getName(Position position) {
    return players.get(position).getName();
  }

  Set<String> getAllName() {
    return positions.keySet();
  }

  Set<Card> getSkat() {
    return skat;
  }
}
