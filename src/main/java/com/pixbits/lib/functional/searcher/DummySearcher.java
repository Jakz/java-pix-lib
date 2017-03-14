package com.pixbits.lib.functional.searcher;

import java.util.function.Predicate;

public class DummySearcher<T> extends Searcher<T>
{
  public DummySearcher() { }
  
  @Override public Predicate<T> search(String text)
  {
    return r -> true;
  }
}
