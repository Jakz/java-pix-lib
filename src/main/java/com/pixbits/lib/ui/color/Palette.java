package com.pixbits.lib.ui.color;

import java.util.Arrays;
import java.util.Iterator;
import java.util.Objects;
import java.util.stream.Stream;

public class Palette implements Iterable<Color>
{
  private final Color[] colors;
  
  public Palette(int[] colors)
  {
    this.colors = new Color[colors.length];
    for (int i = 0; i < colors.length; ++i)
    {
      this.colors[i] = new Color(colors[i]);
    }
  }
  
  public Palette(int size, Color base)
  {
    colors = new Color[size];
    Arrays.fill(colors, base);
  }
  
  public Palette(int size) { this(size, Color.black); }
  public Palette() { this(256); }
  
  public void set(int index, Color color)
  {
    Objects.requireNonNull(color);
    colors[index] = color;
  }
  
  public void set(int index, int r, int g, int b)
  {
    set(index, r, g, b, 0xFF);
  }
  
  public void set(int index, int r, int g, int b, int a)
  {
    colors[index] = new Color(r,g,b,a);
  }
  
  public Color get(int index) { return colors[index]; }
  public Color get(byte index) { return colors[index & 0xFF]; }

  @Override
  public Iterator<Color> iterator() 
  {
    return Arrays.asList(colors).iterator();
  }
  
  public Stream<Color> stream()
  {
    return Arrays.asList(colors).stream();
  }
}
