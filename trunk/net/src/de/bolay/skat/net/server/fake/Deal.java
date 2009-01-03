package de.bolay.skat.net.server.fake;

import static java.util.Collections.shuffle;

import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import de.bolay.skat.Card;
import de.bolay.skat.Position;

class Deal {
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

  private Table table;
  private Set<Card> skat;

  Deal(String foreHand, String middleName, String backHand) {
    init(foreHand, middleName, backHand);
  }

  private void init(String foreHand, String middleName, String backHand) {
    Deck deck = new Deck();
    table = new Table(
        foreHand, deck.pickCards(10),
        middleName, deck.pickCards(10),
        backHand, deck.pickCards(10));
    skat = deck.pickCards(2);
    deck.assertNoneRemaining();
  }

  /**
   * Move players to the next position and deal new cards
   */
  public void next() {
    init(table.getName(Position.MIDDLE_HAND), table.getName(Position.BACK_HAND),
        table.getName(Position.FORE_HAND));
  }


  Position getPosition(String name) {
    return table.getPosition(name);
  }

  Set<Card> getCards(String name) {
    return table.getCards(name);
  }

  Set<Card> getCards(Position position) {
    return table.getCards(position);
  }

  String getName(Position position) {
    return table.getName(position);
  }

  Set<String> getAllNames() {
    return table.getAllNames();
  }

  Set<Card> getSkat() {
    return skat;
  }

  Table getTable() {
    return table;
  }
}
