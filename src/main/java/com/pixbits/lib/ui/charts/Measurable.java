package com.pixbits.lib.ui.charts;

@FunctionalInterface
public interface Measurable<T>
{
  public float chartValue();
  public default String chartLabel() { return ""; }
  public default T chartData() { return null; }
  
  public static <T> Measurable<T> of(final float value, final String label)
  {
    return Measurable.of(value, label, null);
  }
  
  public static <T> Measurable<T> of(final float value, final String label, final T data)
  {
    return new Measurable<T>()
    {
      @Override public float chartValue() { return value; }
      @Override public String chartLabel() { return label; }
      @Override public T chartData() { return data; }
    };
  }
}
