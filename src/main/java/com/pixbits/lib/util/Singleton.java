package com.pixbits.lib.util;

import java.util.function.Supplier;

public class Singleton<T>
{
  private Supplier<T> builder;
  private T instance;
  
  public Singleton(Supplier<T> builder)
  {
    this.builder = builder;
  }
  
  public T get()
  {
    if (instance == null)
      instance = builder.get();
    
    return instance;
  }
}
