package de.bolay.pubsub;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

public class Observers {
  private final Set<Observer> observers;

  public Observers() {
    observers = Collections.synchronizedSet(new HashSet<Observer>());
  }

  public void add(Observer observer) {
    observers.add(observer);
  }

  public void remove(Observer observer) {
    observers.remove(observer);
  }

  public <T extends Observer> void notify(Class<T> observerType,
      Notifier<T> notifier) {
    for (Observer observer : observers) {
      if (observerType.isAssignableFrom(observer.getClass())) {
        @SuppressWarnings("unchecked") // see line above!
        T observerForT = (T) observer;
        notifier.notify(observerForT);
      }
    }
  }

  @Override public String toString() {
    return this.getClass().getSimpleName() + " with " + observers;
  }
}
