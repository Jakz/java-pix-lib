package com.pixbits.lib.log;

import java.util.Optional;
import java.util.function.Consumer;
import java.util.function.Supplier;

public class Reporter<T>
{
  private Optional<Consumer<T>> destination;
  
  public Reporter()
  {
    this.destination = Optional.empty();
  }
  
  public Reporter(Consumer<T> destination)
  {
    setDestination(destination);
  }
  
  public void setDestination(Consumer<T> destination)
  {
    this.destination = Optional.of(destination);
  }
  
  public void report(Supplier<T> supplier)
  {
    destination.ifPresent(c -> c.accept(supplier.get()));
  }
}
