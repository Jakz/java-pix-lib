package com.pixbits.lib.lang;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.util.HashSet;
import java.util.Set;

import org.junit.Test;

import com.pixbits.lib.lang.CommutablePair;

public class CommutablePairTests
{
  @Test
  public void testEqualitySameOrderDifferentType()
  {
    CommutablePair<Integer, String> p1 = new CommutablePair<>(10, "foo");
    CommutablePair<Integer, String> p2 = new CommutablePair<>(10, "foo");
    
    assertEquals(p1, p2);
    assertEquals(p1.hashCode(), p2.hashCode());
  }
  
  @Test
  public void testEqualityDifferentTypeDifferentOrder()
  {
    CommutablePair<Integer, String> p1 = new CommutablePair<>(10, "foo");
    CommutablePair<String, Integer> p2 = new CommutablePair<>("foo", 10);
    
    assertEquals(p1, p2);
    assertEquals(p1.hashCode(), p2.hashCode());
  }
  
  @Test
  public void testEqualityDifferentOrderSameType()
  {
    CommutablePair<String, String> p1 = new CommutablePair<>("foo", "bar");
    CommutablePair<String, String> p2 = new CommutablePair<>("bar", "foo");
    
    assertEquals(p1, p2);
    assertEquals(p1.hashCode(), p2.hashCode());
  }
  
  @Test
  public void testHashSetPutSameType()
  {
    CommutablePair<String, String> p1 = new CommutablePair<>("foo", "bar");
    CommutablePair<String, String> p2 = new CommutablePair<>("bar", "foo");
    
    Set<CommutablePair<String, String>> set = new HashSet<>();
    set.add(p1);
    set.add(p2);
    
    assertEquals(set.size(), 1);
    assertTrue(set.contains(p1));
    assertTrue(set.contains(p2));
  }
}
