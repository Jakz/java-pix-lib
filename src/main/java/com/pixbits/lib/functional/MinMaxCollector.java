package com.pixbits.lib.functional;

import java.util.Comparator;
import java.util.stream.Collector;

public class MinMaxCollector<T>
{
  protected int count;
  protected final Comparator<? super T> comparator;
  protected T min, max;
  
  protected MinMaxCollector(Comparator<? super T> comparator)
  {
    this.comparator = comparator;
  }
  
  public int count() { return count; }
  public T min() { return min; }
  public T max() { return max; }
  
  public void accept(T object)
  {
    if (count == 0)
      min = max = object;
    else if (comparator.compare(object, min) < 0)
      min = object;
    else if (comparator.compare(object, max) > 0)
      max = object;
    
    ++count;
  }
  
  public MinMaxCollector<T> combine(MinMaxCollector<T> other)
  {
    if (this.count == 0) return other;
    else if (other.count == 0) return this;
    
    this.count += other.count;
    
    if (comparator.compare(other.min, min) < 0)
      this.min = other.min;
    
    if (comparator.compare(other.max, max) > 0)
      this.max = other.max;
    
    return this;
  }
  
  public static <T> Collector<T, MinMaxCollector<T>, MinMaxCollector<T>> of(Comparator<? super T> comparator)
  {
    return Collector.of(
      () -> new MinMaxCollector<>(comparator),
      MinMaxCollector::accept,
      MinMaxCollector::combine,
      Collector.Characteristics.IDENTITY_FINISH
    );
  }

  public static <T extends Comparable<? super T>> Collector<T, MinMaxCollector<T>, MinMaxCollector<T>> of()
  {
    return of(Comparator.naturalOrder());
  }
}
