package com.pixbits.lib.ui.color;

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
  
  public GradientColorGenerator(Color... colors)
  {
    this(colors, null);
  }
  
  public GradientColorGenerator(Color c1, Color c2)
  {
    this(new Color[] {c1, c2});
  }
  
  public Color getColor(float v)
  {
    //TODO: clamp to 0.0 1.0?
    
    if (weights == null)
    {
      float rangePerSlot = 1.0f / (colors.length - 1);
      int slot = (int)(v / rangePerSlot);
      float mod = v - slot*rangePerSlot;
      
      return colors[slot].mix(colors[slot+1], mod);
    }
    else
      return null;
  }
}
