package com.pixbits.lib.searcher;

import java.util.function.Function;
import java.util.function.Predicate;

public class LambdaPredicate<T> extends BasicPredicate<T>
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
