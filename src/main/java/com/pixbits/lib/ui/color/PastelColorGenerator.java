package com.pixbits.lib.ui.color;

import java.awt.Color;
import java.util.concurrent.ThreadLocalRandom;

public class PastelColorGenerator implements ColorGenerator
{
  public PastelColorGenerator() { }
  
  @Override
  public Color getColor()
  {
    final float hue = ThreadLocalRandom.current().nextFloat();
    final float saturation = (ThreadLocalRandom.current().nextInt(2000) + 1000) / 10000f;
    final float luminance = 0.9f;
    return Color.getHSBColor(hue, saturation, luminance);
  }
}
