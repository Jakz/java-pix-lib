package com.pixbits.lib.lang;

public class BoolPair
{
  public final boolean first;
  public final boolean second;
  
  public BoolPair(boolean first, boolean second)
  {
    this.first = first;
    this.second = second;
  }
  
  public BoolPair and(boolean first, boolean second)
  {
    return new BoolPair(first && this.first, second && this.second);
  }
  
  public BoolPair and(BoolPair other)
  {
    return and(other.first, other.second);
  }
  
  public BoolPair or(boolean first, boolean second)
  {
    return new BoolPair(first || this.first, second || this.second);
  }
  
  public BoolPair or(BoolPair other)
  {
    return or(other.first, other.second);
  }
}
