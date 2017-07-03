package com.pixbits.lib.ui.canvas;

import java.awt.Color;

public class Brush
{
  private float strokeWidth;
  private Color strokeColor;
  private Color fillColor;
  
  public Brush(Color fillColor, Color strokeColor, float width)
  {
    this.fillColor = fillColor;
    this.strokeColor = strokeColor;
    this.strokeWidth = width;
  }
  
  public Brush()
  {
    this(Color.WHITE, Color.BLACK, 0.0f);
  }
  
  public Brush(Color fillColor)
  {
    this(fillColor, null, 0.0f);
  }
  
  public Brush(Color fillColor, Color strokeColor)
  {
    this(fillColor, strokeColor, 1.0f);
  }
  
  public Brush(Color strokeColor, float width)
  {
    this(null, strokeColor, width);
  }
  
  public boolean hasStroke() { return strokeWidth != 0.0f; }
  
  public float strokeWidth() { return strokeWidth; }
  public void strokeWidth(float strokeWidth) { this.strokeWidth = strokeWidth; }

  public Color strokeColor() { return strokeColor; }
  public void strokeColor(Color strokeColor) { this.strokeColor = strokeColor; }
  
  public Color color() { return fillColor; }
  public void color(Color color) { this.fillColor = color; }
}
