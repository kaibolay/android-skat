package de.bolay.util;

import java.util.EnumSet;
import java.util.Iterator;

class EnumSetCombinationIterator<T extends Enum<T>>
    implements Iterator<EnumSet<T>> {

  private final T[] values;
  private EnumSet<T> nextSet;

  public EnumSetCombinationIterator(T[] values, EnumSet<T> none) {
    this.values = values;
    nextSet = none;
  }

  public boolean hasNext() {
    return nextSet != null;
  }

  public EnumSet<T> next() {
    EnumSet<T> retval = nextSet.clone();
    updateNext();
    return retval;
  }

  private void updateNext() {
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