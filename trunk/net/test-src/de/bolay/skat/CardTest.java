package de.bolay.skat;

import static org.junit.Assert.assertSame;

import java.util.Set;

import org.junit.Test;

public class CardTest {

  @Test
  public void testOfSuit() {
    for (Card.Suit suit : Card.Suit.values()) {
      Set<Card> cards = Card.ofSuit(suit);
      assertSame("Number of cards in " + suit,
          Card.NUM_RANKS, cards.size());
      for (Card card : cards) {
        assertSame(card + " in suit", suit, card.getSuit());
      }
    }
  }

  @Test
  public void testOfRank() {
    for (Card.Rank rank : Card.Rank.values()) {
      Set<Card> cards = Card.ofRank(rank);
      assertSame("Number of cards of " + rank,
          Card.NUM_SUITS, cards.size());
      for (Card card : cards) {
        assertSame(card + " of rank", rank, card.getRank());
      }
    }
  }
}
