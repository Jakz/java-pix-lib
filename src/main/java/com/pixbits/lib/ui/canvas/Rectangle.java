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
  Brush brush;
  
  public Rectangle(Rect rect, Brush brush) { this.rect = rect; this.brush = brush; }
  public Rectangle(Rect rect, Color color) { this.rect = rect; this.brush = new Brush(color); }
  public Rectangle(java.awt.Rectangle rect, Color color) { this(new Rect(rect), color); }
  public Rectangle(int x, int y, int w, int h, Color color) { this(new Rect(x, y, w, h), color); }
  public Rectangle(int x, int y, int w, int h, Brush brush) { this(new Rect(x, y, w, h), brush); }

  public Brush brush() { return brush; }
  public void brush(Brush brush) { this.brush = brush; }

  @Override
  void draw(GfxContext c)
  {
    //System.out.println("Rectangle("+rect.x+", "+rect.y+", "+rect.w+", "+rect.h+", "+brush.color());
    
    c.gfx.fillRect(rect.x, rect.y, rect.w, rect.h, brush.color());
    
    if (brush.hasStroke())
    {
      Stroke stroke = new BasicStroke(brush.strokeWidth());
      Graphics2D g = c.gfx.g();
      c.gfx.saveColor(brush.strokeColor());
      g.setStroke(stroke);
      
      g.draw(new Line2D.Float(rect.x, rect.y, rect.x+rect.w, rect.y));
      g.draw(new Line2D.Float(rect.x, rect.y+rect.h, rect.x+rect.w, rect.y+rect.h));
      g.draw(new Line2D.Float(rect.x, rect.y, rect.x, rect.y+rect.h));
      g.draw(new Line2D.Float(rect.x+rect.w, rect.y, rect.x+rect.w, rect.y+rect.h));

      c.gfx.restoreColor();
    }
  }

}
