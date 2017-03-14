package com.pixbits.lib.searcher;

import java.util.List;
import java.util.function.Function;
import java.util.function.Predicate;

public abstract class SearchParser<T>
{
  public abstract Function<List<SearchPredicate<T>>,Predicate<T>> parse(String string);
  
  protected Predicate<T> buildSinglePredicate(List<SearchPredicate<T>> predicates, String token)
  {
    for (SearchPredicate<T> predicate : predicates)
    {
      Predicate<T> pred = predicate.buildPredicate(token);
      if (pred != null)
      {
        return pred;
      }
    }
    
    return null;
  }
}
