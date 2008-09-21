package de.bolay.pubsub;

import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;

import org.junit.Before;
import org.junit.Test;

import de.bolay.pubsub.Notifier;
import de.bolay.pubsub.Observer;
import de.bolay.pubsub.Observers;

public class ObserversTest {
  private class ObserverA implements Observer {
    int id, a1, a2;

    public ObserverA(int id) {
      this.id = id;
    }

    public void didA1() {
      a1++;
    }

    public void didA2() {
      a2++;
    }

    public void assertDidA1(String msg, int times) {
      assertSame(this + ": " + msg + ", but for A1", times, a1);
    }

    public void assertDidA2(String msg, int times) {
      assertSame(this + ": " + msg + ", but for A2", times, a2);
    }

    @Override public String toString() {
      return this.getClass().getSimpleName() + " #" + id+ " counted A1: " + a1
          + ", A2: " + a2;
    }
  }

  private class ObserverB implements Observer {
    int id, b;

    public ObserverB(int id) {
      this.id = id;
    }

    public void didB() {
      b++;
    }

    public void assertDidB(String msg, int times) {
      assertSame(this + ": " + msg + ", but for B", times, b);
    }

    @Override public String toString() {
      return this.getClass().getSimpleName() + " #" + id + " counted B: " + b;
    }
  }

  private static final int NUM_OBSERVERS_A = 1;
  private static final int NUM_OBSERVERS_B = 3;
  private static final int SOME_OBSERVERS_B = 2;

  private static final int SOME_TIMES = 7;
  private static final int MANY_TIMES = 103;

  private Observers observers;
  private ObserverA[] aObservers = new ObserverA[NUM_OBSERVERS_A];
  private ObserverB[] bObservers = new ObserverB[NUM_OBSERVERS_B];

  @Before public void setUp() throws Exception {
    observers = new Observers();
    for (int i = 0; i < aObservers.length; i++) {
      aObservers[i] = new ObserverA(i);
    }
    for (int i = 0; i < bObservers.length; i++) {
      bObservers[i] = new ObserverB(i);
    }
  }

  private void addAllObservers() {
    addObserversForA();
    addObserversForB();
  }

  private void addObserversForA() {
    addObserversForA(aObservers.length);
  }

  private void addObserversForA(int howMany) {
    for (int i = 0; i < howMany; i++) {
      observers.add(aObservers[i]);
    }
  }

  private void addObserversForB() {
    addObserversForB(bObservers.length);
  }

  private void addObserversForB(int howMany) {
    for (int i = 0; i < howMany; i++) {
      observers.add(bObservers[i]);
    }
  }

  void doA(int times, final boolean doA1, final boolean doA2) {
    for (int i = 0; i < times; i++) {
      observers.notify(ObserverA.class, new Notifier<ObserverA>() {
        public void notify(ObserverA observer) {
          if (doA1) observer.didA1();
          if (doA2) observer.didA2();
        }
      });
    }
  }

  void doB(final int times) {
    observers.notify(ObserverB.class, new Notifier<ObserverB>() {
      public void notify(ObserverB observer) {
        for (int i = 0; i < times; i++) {
          observer.didB();
        }
      }
    });
  }

  void doAll(int times) {
    doA(times, true, true);
    doB(times);
  }

  void assertAllDidObserve(String msg, int a1, int a2, int b) {
    for (ObserverA observerA : aObservers) {
      observerA.assertDidA1(msg, a1);
      observerA.assertDidA2(msg, a2);
    }
    for (ObserverB observerB : bObservers) {
      observerB.assertDidB(msg, b);
    }
  }


  @Test public void testNoObservers() {
    doAll(SOME_TIMES); assertAllDidObserve("no observers", 0, 0, 0);
  }

  @Test public void testAllObservers() {
    addAllObservers();
    doAll(SOME_TIMES);
    assertAllDidObserve("all observers", SOME_TIMES, SOME_TIMES, SOME_TIMES);
  }

  @Test public void testOnlyAObservers() {
    addObserversForA();
    doAll(SOME_TIMES);
    assertAllDidObserve("only A observers", SOME_TIMES, SOME_TIMES, 0);
  }

  @Test public void testOnlyBObservers() {
    addObserversForB();
    doAll(SOME_TIMES);
    assertAllDidObserve("only B observers", 0, 0, SOME_TIMES);
  }

  @Test public void testDoA1A2differently() {
    addObserversForA();
    doA(SOME_TIMES, true, false);
    assertAllDidObserve("only A1", SOME_TIMES, 0, 0);
    doA(MANY_TIMES, false, true);
    assertAllDidObserve("A1 and A2 different times", SOME_TIMES, MANY_TIMES, 0);
  }

  /* assume only some B observers should register */
  private void testSomeBs() {
    doB(SOME_TIMES);
    for (int i = 0; i < bObservers.length; i++) {
      if (i < SOME_OBSERVERS_B) {
        bObservers[i].assertDidB("some B registered - incl. this", SOME_TIMES);
      } else {
        bObservers[i].assertDidB("some B registered - not this", 0);
      }
    }
  }

  @Test public void testSomeBObservers() {
    assertSomeButNotAllObserveB();
    addObserversForB(SOME_OBSERVERS_B);
    testSomeBs();
  }

  private void assertSomeButNotAllObserveB() {
    assertTrue("there should be some B observers, but less than all",
        SOME_OBSERVERS_B > 0 && SOME_OBSERVERS_B < NUM_OBSERVERS_B);
  }

  @Test public void testRemoveSomeObserverOfB() {
    addAllObservers();
    for (int i = SOME_OBSERVERS_B; i < bObservers.length; i++) {
      observers.remove(bObservers[i]);
    }
    assertSomeButNotAllObserveB();
  }
}
