package com.pixbits.lib.ui.charts;

@FunctionalInterface
public interface Measurable
{
  public float chartValue();
  public default String chartLabel() { return ""; }
  
  public static Measurable of(final float value, final String label)
  {
    return new Measurable()
    {
      @Override public float chartValue() { return value; }
      @Override public String chartLabel() { return label; }
    };
  }
}
