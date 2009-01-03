package de.bolay.skat.net.server.fake;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import org.junit.Before;
import org.junit.Test;

import de.bolay.skat.Card;
import de.bolay.skat.Position;

public class DealTest {
  private static final String[] PLAYERS =
      {"Player #1", "Player #2", "Player #3"};
  private static final int SIZE_OF_HAND = 10;
  private Deal deal;

  @Before
  public void setUp() throws Exception {
    deal = new Deal(PLAYERS[0], PLAYERS[1], PLAYERS[2]);
  }

  @Test
  public void testGetName() {
    Set<String> seen = new HashSet<String>();
    for (Position position: Position.values()) {
      String name = deal.getName(position);
      assertTrue("duplicate player " + name + " for " + position
          + ", already seen: " + seen,
          seen.add(name));
    }
    assertEquals("number of names", PLAYERS.length, seen.size());
  }

  @Test
  public void testGetPosition() {
    Set<Position> seen = new HashSet<Position>();
    for (String player: PLAYERS) {
      Position position = deal.getPosition(player);
      assertTrue("duplicate position " + position + " for " + player
          + ", already seen: " + seen,
          seen.add(position));
    }
    assertEquals("number of positions", Position.values().length, seen.size());
  }

  @Test
  public void testGetAllNames() {
    Set<String> allNames = deal.getAllNames();
    assertTrue("all names: " + allNames,
        allNames.containsAll(Arrays.asList(PLAYERS)));
    assertEquals("number of players", PLAYERS.length, allNames.size());
  }

  @Test
  public void testGetCardsByNameAndSkat() {
    Set<Card> seen = new HashSet<Card>();
    for (String player: PLAYERS) {
      Set<Card> hand = deal.getCards(player);
      assertEquals("size of hand", SIZE_OF_HAND, hand.size());
      int numSeen = seen.size();
      seen.addAll(hand);
      assertTrue("duplicate cards " + hand + ", already seen: " + seen,
          seen.size() == numSeen + SIZE_OF_HAND);
    }
    seen.addAll(deal.getSkat());
    assertEquals("number of cards", Card.NUM_CARDS, seen.size());
  }

  @Test
  public void testGetCardsByPositionAndSkat() {
    Set<Card> seen = new HashSet<Card>();
    for (Position position: Position.values()) {
      Set<Card> hand = deal.getCards(position);
      assertEquals("size of hand", SIZE_OF_HAND, hand.size());
      int numSeen = seen.size();
      seen.addAll(hand);
      assertTrue("duplicate cards " + hand + ", already seen: " + seen,
          seen.size() == numSeen + SIZE_OF_HAND);
    }
    seen.addAll(deal.getSkat());
    assertEquals("number of cards", Card.NUM_CARDS, seen.size());
  }

  @Test(expected = IllegalArgumentException.class)
  public void testDuplicatePlayers() {
    new Deal(PLAYERS[0], PLAYERS[1], PLAYERS[0]);
  }

  @Test
  public void testNext() {
    // remember old positions
    Map<String, Position> oldPosition = new HashMap<String, Position>();
    for (String player: PLAYERS) {
      oldPosition.put(player, deal.getPosition(player));
    }

    deal.next();

    // test basics
    testGetName();
    testGetPosition();
    testGetAllNames();
    testGetCardsByNameAndSkat();
    testGetCardsByPositionAndSkat();

    // make sure roles (positions) changed
    for (String player: PLAYERS) {
      assertEquals("new position",
          oldPosition.get(player).before(), deal.getPosition(player));
    }
  }
}
