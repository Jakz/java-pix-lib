package com.pixbits.lib.ui.color;

import java.awt.Color;

public class ColorUtils
{
  public static String colorToHex(Color color)
  {
    return String.format("#%02X%02X%02X", color.getRed(), color.getGreen(), color.getBlue());
  }
  
  public static Color withAlpha(Color c, int alpha) { return new Color(c.getRed(), c.getGreen(), c.getBlue(), alpha); }
}
