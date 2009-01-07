package de.bolay.skat.net.server.fake;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import de.bolay.log.JavaLogger;
import de.bolay.log.Logger;
import de.bolay.skat.net.auto.AutoPlayer;

public class FakeServerTest {
  private static final String USERNAME = "FakeTester";
  private static final String PASSWORD = "fake";
  private static final int ROUNDS = 10;

  private JavaLogger.Factory logFactory;
  private FakeServer fakeServer;

  @Before
  public void setUp() throws Exception {
    logFactory = new JavaLogger.Factory(Logger.Level.DEBUG);
    fakeServer = new FakeServer(logFactory);
  }

  @After
  public void tearDown() throws Exception {
    if (fakeServer != null) {
      fakeServer.close();
    }
  }

  @Test
  public void testRun() {
    new AutoPlayer(logFactory, fakeServer, USERNAME, PASSWORD).play(ROUNDS);
    // TODO: assert something?
  }
}
