package com.pixbits.lib.functional;

import java.util.Comparator;
import java.util.function.BinaryOperator;
import java.util.function.Function;
import java.util.stream.Collector;

public class MinMaxTotCollector<T,U> //extends MinMaxCollector<T>
{
  /*protected U total;
  protected int count;
  protected final Comparator<? super T> comparator;
  protected T min, max;


  Function<T,U> mapper;
  BinaryOperator<U> combiner;
  
  MinMaxTotCollector(Comparator<? super U> comparator, Function<T,U> mapper, BinaryOperator<U> combiner, U init)
  {
    super(comparator);
    this.mapper = mapper;
    this.combiner = combiner;
    this.total = init;
  }

  public U total() { return total; }
  public int count() { return count; }
  public T min() { return min; }
  public T max() { return max; }
  
  @Override
  public void accept(T object)
  {
    if (count == 0)
      min = max = object;
    else if (comparator.compare(object, min) < 0)
      min = object;
    else if (comparator.compare(object, max) > 0)
      max = object;
    
    ++count;
    total = combiner.apply(total, mapper.apply(object));
  }
  
  //TODO: could reuse parent class?
  public MinMaxTotCollector<T,U> combine(MinMaxTotCollector<T,U> other)
  {
    if (this.count == 0) return other;
    else if (other.count == 0) return this;
    
    this.count += other.count;
    this.total = combiner.apply(this.total, other.total);
    
    if (comparator.compare(other.min, min) < 0)
      this.min = other.min;
    
    if (comparator.compare(other.max, max) > 0)
      this.max = other.max;
    
    return this;
  }
  
  
  public static <Y, V extends Comparable<? super V>> Collector<Y, MinMaxTotCollector<Y,V>, MinMaxTotCollector<Y,V>> of(Function<Y,V> mapper, BinaryOperator<V> combiner, V init)
  {
    return of(Comparator.naturalOrder(), mapper, combiner, init);
  }

  public static <T, V extends Comparable<? super V>> Collector<T, MinMaxTotCollector<T,V>, MinMaxTotCollector<T,V>> of(Comparator<? super V> comparator, Function<T,V> mapper, BinaryOperator<V> combiner, V init)
  {
    return Collector.of(
      () -> new MinMaxTotCollector<>(comparator, mapper, combiner, init),
      MinMaxTotCollector::accept,
      MinMaxTotCollector::combine,
      Collector.Characteristics.IDENTITY_FINISH
    );
  }*/
}
