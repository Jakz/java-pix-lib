package com.pixbits.lib.ui.canvas;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Stroke;
import java.awt.geom.Line2D;

import com.pixbits.lib.lang.Rect;

public class Rectangle extends Entity
{
  Rect rect;
  Color color;
  float strokeWidth;
  Color strokeColor;
  
  public Rectangle(Rect rect, Color color) { this.rect = rect; this.color = color; }
  public Rectangle(java.awt.Rectangle rect, Color color) { this(new Rect(rect), color); }
  public Rectangle(int x, int y, int w, int h, Color color) { this(new Rect(x, y, w, h), color); }
  
  public Color color() { return color; }
  public void color(Color color) { this.color = color; }
  
  public float strokeWidth() { return strokeWidth; }
  public void strokeWidth(float width) { this.strokeWidth = width; }
  
  public Color strokeColor() { return strokeColor; }
  public void strokeColor(Color color) { this.strokeColor = color; }
  
  @Override
  void draw(GfxContext c)
  {
    c.gfx.fillRect(rect.x, rect.y, rect.w, rect.h, color);
    
    if (strokeWidth != 0.0f)
    {
      Stroke stroke = new BasicStroke(strokeWidth);
      Graphics2D g = c.gfx.g();
      c.gfx.saveColor(strokeColor);
      g.setStroke(stroke);
      
      g.draw(new Line2D.Float(rect.x, rect.y, rect.x+rect.w, rect.y));
      g.draw(new Line2D.Float(rect.x, rect.y+rect.h, rect.x+rect.w, rect.y+rect.h));
      g.draw(new Line2D.Float(rect.x, rect.y, rect.x, rect.y+rect.h));
      g.draw(new Line2D.Float(rect.x+rect.w, rect.y, rect.x+rect.w, rect.y+rect.h));

      c.gfx.restoreColor();
    }
  }

}
