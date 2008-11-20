package de.bolay.util;

import java.util.EnumSet;
import java.util.Iterator;

public class EnumSetCombinations<T extends Enum<T>>
    implements Iterable<EnumSet<T>> {
  private final Class<T> type;

  public EnumSetCombinations(final Class<T> type) {
    this.type = type;
  }

  private class EnumSetCombinationIterator implements Iterator<EnumSet<T>> {
    T[] values = type.getEnumConstants();
    EnumSet<T> nextSet = EnumSet.noneOf(type);

    public boolean hasNext() {
      return nextSet != null;
    }

    public EnumSet<T> next() {
      EnumSet<T> retval = nextSet.clone();
      updateNext();
      return retval;
    }

    void updateNext() {
      for (T value : values) {
        if (nextSet.contains(value)) {
          nextSet.remove(value);
        } else {
          nextSet.add(value);
          return;
        }
      }
      nextSet = null;
    }

    public void remove() {
      throw new UnsupportedOperationException();
    }
  }

  public Iterator<EnumSet<T>> iterator() {
    return new EnumSetCombinationIterator();
  }
}
