package de.bolay.pubsub;

public interface Notifier <O extends Observer> {
  void notify(O observer);
}
