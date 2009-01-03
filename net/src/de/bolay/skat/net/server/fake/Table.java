package de.bolay.skat.net.server.fake;

import java.util.Map;
import java.util.Set;

import com.google.common.collect.BiMap;
import com.google.common.collect.ImmutableBiMap;
import com.google.common.collect.ImmutableMap;

import de.bolay.skat.Card;
import de.bolay.skat.Position;

class Table {
  private final BiMap<Position, String> names;
  private final Map<Position, Set<Card>> cards;

  Table(String foreHandName, Set<Card> foreHandCards,
      String middleHandName, Set<Card> middleHandCards,
      String backHandName, Set<Card> backHandCards) {
    names = new ImmutableBiMap.Builder<Position, String>()
        .put(Position.FORE_HAND, foreHandName)
        .put(Position.MIDDLE_HAND, middleHandName)
        .put(Position.BACK_HAND, backHandName)
        .build();
    cards = new ImmutableMap.Builder<Position, Set<Card>>()
        .put(Position.FORE_HAND, foreHandCards)
        .put(Position.MIDDLE_HAND, middleHandCards)
        .put(Position.BACK_HAND, backHandCards)
        .build();
  }

  Position getPosition(String name) {
    return names.inverse().get(name);
  }

  Set<Card> getCards(String name) {
    return getCards(getPosition(name));
  }

  Set<Card> getCards(Position position) {
    return cards.get(position);
  }

  String getName(Position position) {
    return names.get(position);
  }

  Set<String> getAllNames() {
    return names.values();
  }
}
