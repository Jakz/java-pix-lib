package com.pixbits.lib.util;

public class PixMath
{
  private static final double DOUBLE_EPSILON = 1e-7;
  private static final double FLOAT_EPSILON = 1e-5;
  
  public static boolean areEquivalent(double x, double y)
  {
    return x == y || java.lang.Math.abs(x - y) < DOUBLE_EPSILON;
  }
  
  public static boolean areEquivalent(float x, float y)
  {
    return x == y || java.lang.Math.abs(x - y) < FLOAT_EPSILON;
  }
}
