package com.pixbits.lib.ui.color;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.image.BufferedImage;
import java.util.HashMap;
import java.util.Map;

import javax.swing.ImageIcon;

public class SquareIconGenerator
{
  private final Map<Color, Image> cache;
  
  private ColorGenerator generator;
  private final Dimension size;
  private Color strokeColor;
  private int strokeWidth;
  
  public SquareIconGenerator(ColorGenerator generator, int width, int height, int strokeWidth, Color strokeColor)
  {
    this.cache = new HashMap<>();
    this.generator = generator;
    this.size = new Dimension(width, height);
    this.strokeWidth = strokeWidth;
    this.strokeColor = strokeColor;
  }
  
  public SquareIconGenerator()
  {
    this(null, 5, 5, 0, null);
  }
  
  public SquareIconGenerator(int width, int height)
  {
    this(null, width, height, 0, null);
  }
  
  public SquareIconGenerator(ColorGenerator generator, int width, int height)
  {
    this(generator, width, height, 0, null);
  }
  
  public SquareIconGenerator(int width, int height, Color strokeColor)
  {
    this(null, width, height, 1, strokeColor);
  }
  
  public void flushCache()
  {
    cache.clear();
  }
  
  public void setDimension(int width, int height)
  {
    if (size.width != width || size.height != height)
    {
      size.width = width;
      size.height = height;
      flushCache();
    }
  }
  
  public void setStrokeWidth(int strokeWidth)
  {
    if (strokeWidth != this.strokeWidth)
    {
      this.strokeWidth = strokeWidth;
      flushCache();
    }
  }
  
  public void setStrokeColor(Color color)
  {
    if (!color.equals(this.strokeColor))
    {
      this.strokeColor = color;
      flushCache();
    }
  }
    
  public Image generate(Color color)
  {
    Image image = cache.get(color);
    
    if (image == null)
    {
      BufferedImage bimage = new BufferedImage(size.width, size.height, BufferedImage.TYPE_INT_ARGB);
      Graphics g = bimage.getGraphics();
      g.setColor(color);
      g.fillRect(0, 0, size.width, size.height);
      
      if (strokeWidth != 0)
      {
        g.setColor(strokeColor);
        g.drawRect(0, 0, size.width-1, size.height-1);
      }
      
      cache.put(color, bimage);
      return bimage;
    }
    else
      return image;
  }
  
  public Image generate()
  {
    if (generator == null)
      throw new IllegalStateException("SquareColorGenerator::generate() requires a generator to be set");
    
    return generate(generator.getColor());
  }
  
  public ImageIcon generateIcon() { return new ImageIcon(generate()); }
  public ImageIcon generateIcon(Color color) { return new ImageIcon(generate(color)); }

}
