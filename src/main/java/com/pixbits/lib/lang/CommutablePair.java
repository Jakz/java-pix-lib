package com.pixbits.lib.lang;

import java.util.Objects;

public class CommutablePair<U,V>
{
  public final U first;
  public final V second;
  
  public CommutablePair(U first, V second)
  {
    this.first = first;
    this.second = second;
  }
  

  public int hashCode()
  { 
    int hashCode1 = first.hashCode();
    int hashCode2 = second.hashCode(); 
    
    if (hashCode1 < hashCode2)
      return Objects.hash(hashCode1, hashCode2);
    else
      return Objects.hash(hashCode2, hashCode1);
  }
  
  public boolean equals(Object o) 
  { 
    if (o instanceof CommutablePair)
    {
      int thc1 = first.hashCode();
      int thc2 = second.hashCode();
      
      CommutablePair<?,?> p = ((CommutablePair<?,?>)o);
      
      int ohc1 = p.first.hashCode();
      int ohc2 = p.second.hashCode();
      
      boolean tf = thc1 < thc2, of = ohc1 < ohc2;
      
      if (tf == of)
        return first.equals(p.first) && second.equals(p.second);
      else
        return first.equals(p.second) && second.equals(p.first);
    }
    
    return o instanceof CommutablePair && 
        ((CommutablePair<?,?>)o).first.equals(first) && 
        ((CommutablePair<?,?>)o).second.equals(second);
  }
}
