package de.bolay.skat.net.server.fake;

import java.util.Set;

import de.bolay.skat.Card;
import de.bolay.skat.Game;
import de.bolay.skat.Level;
import de.bolay.skat.Position;

class BiddingResult {
  Table table;
  Game game;
  Level level;
  Position soloPosition;
  Set<Card> skat;
}