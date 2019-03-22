package com.pixbits.lib.searcher;

import java.util.function.Function;
import java.util.function.Predicate;

public class LambdaPredicate<T> implements SearchPredicate<T>
{
  private final Function<String, Predicate<T>> builder;
  
  public LambdaPredicate(Function<String, Predicate<T>> builder)
  {
    this.builder = builder;
  }

  @Override public Predicate<T> buildPredicate(String token)
  {
    return builder.apply(token);
  }
}
