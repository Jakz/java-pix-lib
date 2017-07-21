package com.pixbits.lib.ui.color;

public class Color
{
  private int color;
  
  public Color(int c)
  {
    this.color = c;
  }
  
  public Color(int r, int g, int b)
  {
    this(r,g,b,255);
  }
  
  public Color(int r, int g, int b, int a)
  {
    this.color = (a << 24) | (r << 16) | (g << 8) | (b);
  }
  
  public Color(java.awt.Color color)
  {
    this.color = color.getRGB();
  }
  
  public int r() { return (color >>> 16) & 0xFF; }
  public int g() { return (color >>> 8) & 0xFF; }
  public int b() { return (color >>> 0) & 0xFF; }
  public int a() { return (color >>> 24) & 0xFF; }

  public void r(int r)
  {
    color &= ~0x00FF0000;
    color |= r << 16;
  }
  
  public void g(int g)
  {
    color &= ~0x0000FF00;
    color |= g << 8;
  }
  
  public void b(int b)
  {
    color &= ~0x000000FF;
    color |= b << 0;
  }
  
  public void a(int a)
  {
    color &= ~0xFF000000;
    color |= a << 24;
  }
  
  public Color mix(Color o, final float p)
  {
    final float f = 1.0f - p;
    return new Color((int)(r()*f + o.r()*p), (int)(g()*f + o.g()*p), (int)(b()*f + o.b()*p));
  }
  
  public java.awt.Color toAWT()
  {
    return new java.awt.Color(color);
  }
  
  public int toInt()
  {
    return color;
  }
  
  public String toString()
  {
    return String.format("{ r: %d, g: %d, b: %d }", r(), g(), b());
  }
  
  public final static Color black = new Color(0,0,0);
}
