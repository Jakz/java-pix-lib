package com.pixbits.lib.ui.color;

import java.awt.Color;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MappableColorGenerator<T>
{
  private final ColorGenerator generator;
  private final Map<T, Color> colors;
  
  public MappableColorGenerator(ColorGenerator generator)
  {
    this.generator = generator;
    this.colors = new HashMap<>();
  }
  
  public Color getColor(T key)
  {
    return colors.computeIfAbsent(key, k -> generator.getColor());
  }
}
