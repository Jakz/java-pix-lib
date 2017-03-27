package com.pixbits.lib.searcher;

import java.util.function.Predicate;

@FunctionalInterface
public interface SearcherInterface<T>
{
  Predicate<T> search(String text);
}
