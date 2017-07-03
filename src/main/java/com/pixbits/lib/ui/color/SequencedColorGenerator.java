package com.pixbits.lib.ui.color;

import java.awt.Color;
import java.util.ArrayList;
import java.util.List;

public class SequencedColorGenerator
{
  private final ColorGenerator generator;
  private final List<Color> colors;
  
  public SequencedColorGenerator(ColorGenerator generator)
  {
    this.generator = generator;
    this.colors = new ArrayList<>();
  }
  
  public Color getColor(int nth)
  {
    while (nth >= colors.size())
      colors.add(generator.getColor());
    
    return colors.get(nth);

  }
}
