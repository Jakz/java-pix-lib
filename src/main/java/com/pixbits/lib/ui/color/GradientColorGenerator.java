package com.pixbits.lib.ui.color;

public class GradientColorGenerator
{
  private float weights[];
  private Color colors[];
  
  private float addedWeights[];
  
  public GradientColorGenerator(Color[] colors, float[] weights)
  {
    this.colors = colors;
    this.weights = weights;
    
    if (weights != null)
    {
      if (colors.length != weights.length + 1)
        throw new IllegalArgumentException("A weighted gradient must specify a weight less than the amount of colors");
      
      this.addedWeights = new float[weights.length];

      float sum = 0.0f;
      for (float w : weights) sum += w;
   
      for (int i = 0; i < weights.length; ++i)
      {
        weights[i] /= sum;
        addedWeights[i] = (i > 0 ? addedWeights[i-1] : 0) + weights[i];
      }
    }
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
      float mod = (v - slot*rangePerSlot) / rangePerSlot;
            
      return colors[slot].mix(colors[slot+1], mod);
    }
    else
    {
      for (int i = 0; i < addedWeights.length; ++i)
      {
        if (v < addedWeights[i])
        {
          if (i > 0)
            v -= addedWeights[i-1];
          
          float mod = v / weights[i];
          
          return colors[i].mix(colors[i+1], mod);
        }
      }
      
      return Color.black;
    }
  }
}
