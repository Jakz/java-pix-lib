package com.pixbits.lib.lang;

import java.util.concurrent.atomic.AtomicLong;

import static java.lang.Double.doubleToLongBits;
import static java.lang.Double.longBitsToDouble;


public class AtomicDouble extends Number
{
  private AtomicLong bits;

  public AtomicDouble() { this(0f); }

  public AtomicDouble(double initialValue)
  {
    bits = new AtomicLong(doubleToLongBits(initialValue));
  }

  public final boolean compareAndSet(double expect, double update) 
  {
    return bits.compareAndSet(doubleToLongBits(expect), doubleToLongBits(update));
  }

  public final void set(double newValue)
  {
    bits.set(doubleToLongBits(newValue));
  }

  public final double getAndSet(double newValue)
  {
    return longBitsToDouble(bits.getAndSet(doubleToLongBits(newValue)));
  }

  public final boolean weakCompareAndSet(double expect, double update)
  {
    return bits.weakCompareAndSet(doubleToLongBits(expect), doubleToLongBits(update));
  }
  
  public final double get() { return longBitsToDouble(bits.get()); }
  public float floatValue() { return (float)get(); }
  public double doubleValue() { return floatValue(); }
  public int intValue() { return (int)get(); }
  public long longValue() { return (long)get(); }
}