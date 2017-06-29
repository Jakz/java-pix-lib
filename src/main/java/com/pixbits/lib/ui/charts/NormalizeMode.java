package com.pixbits.lib.ui.charts;

public class NormalizeMode
{
  private static enum Type
  {
    FIXED,
    AUTOMATIC
  };
  
  private final Type type;
  private final float param;
  
  private NormalizeMode(Type type, float param)
  {
    this.type = type;
    this.param = param;
  }
  
  public static NormalizeMode fixed(float value)
  {
    return new NormalizeMode(Type.FIXED, value);
  }
  
  public static NormalizeMode automatic()
  {
    return new NormalizeMode(Type.AUTOMATIC, 0.0f);
  }
  
  public static NormalizeMode automatic(float tolerance)
  {
    return new NormalizeMode(Type.AUTOMATIC, tolerance);
  }
}
