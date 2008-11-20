package de.bolay.util;

import java.util.EnumSet;
import java.util.Iterator;

public class EnumSetCombinations<T extends Enum<T>>
    implements Iterable<EnumSet<T>> {

  private final Class<T> type;

  public EnumSetCombinations(Class<T> type) {
    this.type = type;
  }

  public Iterator<EnumSet<T>> iterator() {
    return new EnumSetCombinationIterator<T>(
        type.getEnumConstants(), EnumSet.noneOf(type));
  }
}
