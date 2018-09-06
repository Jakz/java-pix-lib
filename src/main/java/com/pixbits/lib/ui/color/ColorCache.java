package com.pixbits.lib.ui.color;

import java.awt.Color;
import java.util.HashMap;
import java.util.Map;

public class ColorCache<T>
{
  private final ColorGenerator generator;
  private final Map<T, Color> colors;
  
  public ColorCache(ColorGenerator generator)
  {
    this.generator = generator;
    this.colors = new HashMap<>();
  }
  
  public Color getColor(T key)
  {
    return colors.computeIfAbsent(key, k -> generator.getColor());
  }
  
  public Color get(T key) { return getColor(key); }
  public void set(T k, Color v) { colors.put(k, v); }
  
  public void clear()
  {
    colors.clear();
  }
}
