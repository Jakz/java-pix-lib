package com.pixbits.lib.searcher;

import java.util.Collections;
import java.util.List;
import java.util.function.Predicate;

public class Searcher<T> implements SearcherInterface<T>
{
  private final SearchParser<T> parser;
  private final List<SearchPredicate<T>> predicates;
  
  protected Searcher()
  {
    parser = null;
    predicates = Collections.singletonList(new LambdaPredicate<T>(s -> t -> true));
  }
  
  public Searcher(SearchParser<T> parser, List<SearchPredicate<T>> predicates)
  {
    this.parser = parser;
    this.predicates = predicates;
  }
  
  public Predicate<T> search(String text)
  {
    return parser.parse(text).apply(predicates);
  }
}
