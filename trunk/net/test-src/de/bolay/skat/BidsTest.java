package de.bolay.skat;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import junit.framework.Assert;

import org.junit.Test;

public class BidsTest {
  private static final int MIN_BID = 18;
  private static final int MAX_BID = 264;
  private static final int NUM_BIDS = 62;

  @Test
  public void testBids() {
    int num = 0;
    Bids bids = new Bids();
    assertEquals("Lowest bid", bids.current(), MIN_BID);
    try {
      while (true) {
        int previous = bids.current();
        bids.next();
        assertTrue("Bids get higher", bids.current() > previous);
        num++;
      }
    } catch (IllegalArgumentException iae) {
      Assert.assertEquals(iae.getMessage(), "no bid after " + MAX_BID);
    }
    assertEquals("Highest bid", bids.current(), MAX_BID);
    assertEquals("Number of bids", num, NUM_BIDS);
    assertTrue("Is last bid", bids.isLast());
  }
}
