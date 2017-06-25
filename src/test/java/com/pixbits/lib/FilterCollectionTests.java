package com.pixbits.lib;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.util.Arrays;
import java.util.Iterator;
import java.util.function.Predicate;
import java.util.stream.Collectors;

import org.junit.Test;

import com.pixbits.lib.collections.FilterableList;
import com.pixbits.lib.collections.FilteredSlice;

public class FilterCollectionTests
{
  private <T> FilterableList<T> list(T... args)
  {
    return new FilterableList<>(Arrays.asList(args), Collectors.toList());
  }
  
  private <T> void assertMatch(FilteredSlice<T> slice, T... values)
  {
    assertEquals(values.length, slice.size());
    Iterator<T> it = slice.iterator();
    int i = 0;
    while (it.hasNext())
      assertEquals(it.next(), values[i++]);
  }
  
  @Test
  public void testSingleFilter()
  {
    FilterableList<Integer> list = list(1,2,3,4,5);
    FilteredSlice<Integer> slice = list.filter(i -> i < 3);
    
    assertMatch(slice, 1, 2);
  }
  
  @Test
  public void testOrFilter()
  {
    FilterableList<Integer> list = list(1,2,3,4,5);
    FilteredSlice<Integer> slice = list.filter(((Predicate<Integer>)(i -> i == 1)).or(i -> i == 5));
    assertMatch(slice, 1, 5);
  }
  
  @Test
  public void testAndFilter()
  {
    FilterableList<Integer> list = list(1,2,3,4,5);
    FilteredSlice<Integer> slice = list.filter(((Predicate<Integer>)(i -> i <= 2)).and(i -> i >= 2));
    assertMatch(slice, 2);
  }
}
