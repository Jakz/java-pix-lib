package com.pixbits.lib.ui.color;

import java.awt.Color;

public class GradientColorGenerator
{
  public float weights[];
  public Color colors[];
  
  public float addedWeights[];
  
  public GradientColorGenerator(Color[] colors, float[] weights)
  {
    this.colors = colors;
    this.weights = weights;
  }
  
  public GradientColorGenerator(Color[] colors)
  {
    this(colors, null);
  }
  
  public GradientColorGenerator(Color c1, Color c2)
  {
    
  }
  
  public Color getColor(float v)
  {
    return null;
  }
}
