package com.pixbits.lib.searcher;

import java.util.function.Predicate;

public abstract class BasicPredicate<T> implements SearchPredicate<T>
{
  final String name;
  final String description;
  final String example;
  
  public BasicPredicate(String name, String example, String desc)
  {
    this.name = name; 
    this.description = desc;
    this.example = example;
  }
  
  public BasicPredicate()
  {
    this.name = null;
    this.description = null;
    this.example = null;
  }
  
  @Override public abstract Predicate<T> buildPredicate(String token);
  public String getName() { return name; }
  public String getExample() { return example; }
  public String getDescription() { return description; }
}