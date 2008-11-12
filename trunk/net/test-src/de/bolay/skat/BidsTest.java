package de.bolay.skat;

import static org.junit.Assert.assertEquals;
import junit.framework.Assert;

import org.junit.Test;

public class BidsTest {
  private static final int MIN_BID = 18;
  private static final int MAX_BID = 264;
  private static final int NUM_BIDS = 62;

  @Test
  public void testBids() {
    int num = 0;
    int bid = Bids.first();
    assertEquals("Lowest bid", bid, MIN_BID);
    try {
      while (true) {
        bid = Bids.nextBid(bid);
        num++;
      }
    } catch (IllegalArgumentException iae) {
      Assert.assertEquals(iae.getMessage(), "no bid after " + MAX_BID);
    }
    assertEquals("Highest bid", bid, MAX_BID);
    assertEquals("Number of Bids", num, NUM_BIDS);
    assertEquals("Last bid", Bids.last(), MAX_BID);
  }
}
