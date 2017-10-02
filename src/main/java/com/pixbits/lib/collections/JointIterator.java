package com.pixbits.lib.collections;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;

public class JointIterator<T> implements Iterator<T>
{
  private final Iterable<Iterable<T>> collections;
  private Iterator<Iterable<T>> iterator;
  private Iterator<T> innerIterator;
  
  public JointIterator(Iterable<T>... collections)
  {
    this.collections = new ArrayList<>(Arrays.asList(collections));
    this.iterator = this.collections.iterator();
  }

  @Override
  public boolean hasNext()
  {
    // TODO Auto-generated method stub
    return false;
  }

  @Override
  public T next()
  {
    // TODO Auto-generated method stub
    return null;
  }
}
