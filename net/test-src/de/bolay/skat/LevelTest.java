package de.bolay.skat;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.util.Arrays;
import java.util.EnumSet;

import org.junit.Test;

import de.bolay.util.EnumSetCombinations;

public class LevelTest {
  private final static EnumSet<Level> VALID_FOR_NOT_NULL =
      EnumSet.of(Level.REGULAR, Level.HAND, Level.SCHNEIDER_ANNOUNCED,
          Level.SCHWARZ_ANNOUNCED, Level.OUVERT);

  private final static EnumSet<Level> VALID_FOR_NULL =
      EnumSet.of(Level.REGULAR, Level.HAND, Level.OUVERT, Level.HAND_OUVERT);

  private EnumSet<Game> getGames(boolean isNull) {
    EnumSet<Game> matchingGames = EnumSet.noneOf(Game.class);
    for (Game game : Game.values()) {
      if (game.isNull() == isNull) {
        matchingGames.add(game);
      }
    }
    return matchingGames;
  }

  public void assertValidity(boolean isNull, EnumSet<Level> validLevels) {
    EnumSet<Level> invalidLevels = EnumSet.complementOf(validLevels);
    for (Level level : Level.values()) {
      if (level.isValid(isNull)) {
        assertTrue("level " + level + " is valid for "
            + (isNull ? "" : "not ") + "null", validLevels.contains(level));
        for (Game game : getGames(isNull)) {
          level.assertValid(game);
        }
      } else {
        assertTrue("level " + level + " is invalid for "
            + (isNull ? "" : "not ") + "null", invalidLevels.contains(level));
        for (Game game : getGames(isNull)) {
          try {
            level.assertValid(game);
            fail("should throw exception for " + game + " and " + level);
          } catch (IllegalArgumentException iae) {
            assertTrue("exception for " + game + " and " + level,
                iae.getMessage().contains("invalid bid level"));
          }
        }
      }
    }
  }

  @Test
  public void testValidity() {
    assertValidity(/* null */ false, VALID_FOR_NOT_NULL);
    assertValidity(/* null */ true, VALID_FOR_NULL);
  }

  private void assertConvert(boolean isNull) {
    for (Level level : Level.values()) {
      if (level.isValid(isNull)) {
        Level converted = Level.of(isNull, level.isHand(isNull),
            level.isSchneiderAnnounced(isNull),
            level.isSchwarzAnnounced(isNull), level.isOuvert());
        assertSame(level, converted);
      }
    }
  }

  @Test
  public void testConversion() {
    assertConvert(/* null */ false);
    assertConvert(/* null */ true);
  }

  private enum BidFlag {
    NULL, HAND, SCHNEIDER_ANNOUNCED, SCHWARZ_ANNOUNCED, OUVERT;
  }

  private void addIfPossible(EnumSet<Level> levels,
      EnumSet<BidFlag> bidFlags) {
    try {
      Level newLevel = Level.of(
          bidFlags.contains(BidFlag.NULL),
          bidFlags.contains(BidFlag.HAND),
          bidFlags.contains(BidFlag.SCHNEIDER_ANNOUNCED),
          bidFlags.contains(BidFlag.SCHWARZ_ANNOUNCED),
          bidFlags.contains(BidFlag.OUVERT));
      levels.add(newLevel);
    } catch (IllegalArgumentException iae) {
      assertTrue("exception for bid level with flags " + bidFlags,
          iae.getMessage().contains("invalid bid level"));
    }
  }

  @Test
  public void testConvertCoverage() {
    EnumSet<Level> levels = EnumSet.noneOf(Level.class);
    for (EnumSet<BidFlag> bidFlags :
        new EnumSetCombinations<BidFlag>(BidFlag.class)) {
      addIfPossible(levels, bidFlags);
    }
    assertEquals("all levels covered",
        EnumSet.copyOf(Arrays.asList(Level.values())), levels);
  }
}
