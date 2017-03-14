package com.pixbits.lib.searcher;

import java.util.function.Predicate;

public abstract class DummyPredicate<T> extends SearchPredicate<T>
{
  @Override String getName() { return null; }
  @Override String getDescription() { return null; }

  @Override String getExample() { return null; }

  @Override abstract public Predicate<T> buildPredicate(String token);
}
