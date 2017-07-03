package com.pixbits.lib.ui.color;

import java.awt.Color;
import java.util.Random;

public class PleasantColorGenerator implements ColorGenerator
{
  double hue;
  float saturation;
  float brightness;
  
  public PleasantColorGenerator(float saturation, float brightness)
  {
    this.hue = new Random().nextFloat();
    this.saturation = saturation;
    this.brightness = brightness;
  }
  
  public PleasantColorGenerator()
  {
    this(0.5f, 0.95f);
  }
  
  final double conjugate = (1.0 + Math.sqrt(5.0)) / 2.0;
  private double getNextHue(int m)
  {
    hue = (hue + conjugate*m) % 1.0f;
    return hue;
  }

  @Override
  public Color getColor()
  {
    return Color.getHSBColor((float)getNextHue(1), saturation, brightness);
  }
}
