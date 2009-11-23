package de.bolay.skat;

import static java.util.Collections.shuffle;
import static java.util.Collections.sort;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.util.Arrays;
import java.util.Comparator;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

import javax.annotation.Nullable;

import org.junit.Test;

import com.google.common.collect.HashMultimap;
import com.google.common.collect.Multimap;

import de.bolay.skat.Card.Rank;
import de.bolay.skat.Card.Suit;

public class GameTest {
  private void assertSymmetric(Comparator<Card> comp) {
    List<Card> deck = new LinkedList<Card>(Arrays.asList(Card.values()));
    while (!deck.isEmpty()) {
      Card card = deck.remove(deck.size()-1);
      assertTrue(card + " should equal itself", comp.compare(card, card) == 0);
      for (Card otherCard : deck) {
        assertTrue("Comparator not symmetric for " + card + " and " + otherCard,
            Integer.signum(comp.compare(card, otherCard)) ==
            -Integer.signum(comp.compare(otherCard, card)));
      }
    }
  }

  private List<Card> sortShuffledDeck(Comparator<Card> comp) {
    List<Card> deck = Arrays.asList(Card.values());
    shuffle(deck);
    sort(deck, comp);
    return deck;
  }

  private void assertShuffleSort(Comparator<Card> comp) {
    List<Card> deck1 = sortShuffledDeck(comp);
    List<Card> deck2 = sortShuffledDeck(comp);
    assertTrue("Sorted decks", Arrays.equals(deck1.toArray(), deck2.toArray()));
  }

  private void assertComparator(Comparator<Card> comp) {
    assertSymmetric(comp);
    assertShuffleSort(comp);
  }

  private void assertTrump(Comparator<Card> comp, Card.Suit trumpSuit) {
    Set<Card> trumps = Card.ofSuit(trumpSuit);
    trumps.addAll(Card.ofRank(Card.Rank.JACK));
    for (Card trump : trumps) {
      for (Card otherCard : Card.values()) {
        if (otherCard.getSuit() == trumpSuit) continue;
        if (otherCard.getRank() == Card.Rank.JACK) continue;
        assertTrue(trump + " should trump " + otherCard,
            comp.compare(trump, otherCard) > 0);
      }
    }
  }

  private void assertTens(Comparator<Card> comp, boolean jacksAreTrump) {
    for (Card ten : Card.ofRank(Card.Rank.TEN)) {
      for (Card otherCard : Card.ofSuit(ten.getSuit())) {
        if (otherCard == ten) continue;
        int res = comp.compare(ten, otherCard);
        Rank otherRank = otherCard.getRank();
        if (jacksAreTrump) {
          // Tens are high and only Aces and Jacks beat them
          if (otherRank == Card.Rank.ACE || otherRank == Card.Rank.JACK) {
            assertTrue(otherCard + " should beat " + ten, res < 0);
          } else {
            assertTrue(ten + " should be higher than " + otherCard, res > 0);
          }
        } else {
          // Tens are "regular" and only beat 7, 8, and 9.
          if (otherRank == Card.Rank.SEVEN || otherRank == Card.Rank.EIGHT ||
              otherRank == Card.Rank.NINE) {
            assertTrue(ten + " should be higher than " + otherCard, res > 0);
          } else {
            assertTrue(otherCard + " should beat " + ten, res < 0);
          }
        }
      }
    }
  }

  @Test
  public void testCanFollow() {
    for (Game game : Game.values()) {
      Multimap<Suit, Card> cardsBySuit = HashMultimap.create();
      for (Suit suit : Suit.values()) {
        cardsBySuit.putAll(suit, Card.ofSuit(suit));
      }
      Suit trumpSuit = game.getTrumpSuit();
      if (!game.isNull()) {
        // move all jacks to trump suit
        Set<Card> jacks = Card.ofRank(Rank.JACK);
        for (Suit suit : Suit.values()) {
          cardsBySuit.get(suit).removeAll(jacks);
        }
        cardsBySuit.putAll(trumpSuit, jacks);
      }
      for (Suit suit : cardsBySuit.keySet()) {
        assertNumCardsInSuit(game, suit, trumpSuit,
            cardsBySuit.get(suit).size());

        for (Card card : cardsBySuit.get(suit)) {
          for (Card otherCard : Card.values()) {
            if (card == otherCard) {
              continue;
            }
            assertEquals(otherCard + " can follow " + card + " for " + game,
                cardsBySuit.get(suit).contains(otherCard),
                game.canFollow(card).apply(otherCard));
          }
        }
      }
    }
  }

  /**
   * Sanity-check number of cards (including trumps) in given suit
   */
  private void assertNumCardsInSuit(Game game, Suit suit,
      @Nullable Suit trumpSuit, int actual) {
    int numCardsInSuit;
    if (suit == trumpSuit) {
      if (suit == null) {
        numCardsInSuit = 4; // jacks in grand
      } else {
        numCardsInSuit = 11;
      }
    } else  {
      if (game.isNull()) {
        numCardsInSuit = 8;
      } else {
        numCardsInSuit = 7;
      }
    }
    assertEquals(
        ((suit == null)  ? "Jacks" : "Cards in " + suit + " (incl. trumps)")
        + " for " + game, numCardsInSuit, actual);
  }

  @Test
  public void testCardComparators() {
    for (Game game : Game.values()) {
      Comparator<Card> comparator = game.getCardComparator();
      assertComparator(comparator);
      if (game == Game.NULL || game == Game.GRAND) {
        assertNull("No trump suit for " + game, game.getTrumpSuit());
      } else {
        assertTrump(comparator, game.getTrumpSuit());
      }
      assertTens(comparator, /* jacksAreTrump */ game != Game.NULL);
    }
  }
}
