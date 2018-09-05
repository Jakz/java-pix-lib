package com.pixbits.lib.lang;

import java.util.Objects;

public class Pair<U,V>
{
  public final U first;
  public final V second;
  
  public Pair(U first, V second)
  {
    this.first = first;
    this.second = second;
  }
  
  public int hashCode() { return Objects.hash(first, second); }
  public boolean equals(Object o) { return o instanceof Pair && ((Pair<?,?>)o).first.equals(first) && ((Pair<?,?>)o).second.equals(second); }
}
