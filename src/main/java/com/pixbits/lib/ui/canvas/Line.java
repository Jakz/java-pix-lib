package com.pixbits.lib.ui.canvas;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Stroke;
import java.awt.geom.Line2D;

import com.pixbits.lib.lang.Point;

public class Line extends Entity
{
  Point from, to;
  Brush brush;
  
  public Line(Point from, Point to, Color color)
  {
    this(from, to, new Brush(color, 1.0f));
  }

  public Line(Point from, Point to, Brush brush)
  {
    this.from = from;
    this.to = to;
    this.brush = brush;
  }

  @Override
  void draw(GfxContext c)
  {
    if (brush.hasStroke())
    {
      Stroke stroke = new BasicStroke(brush.strokeWidth());
      Graphics2D g = c.gfx.g();
      c.gfx.saveColor(brush.strokeColor());
      g.setStroke(stroke);  
      g.draw(new Line2D.Float(from.x, from.y, to.x, to.y));
      c.gfx.restoreColor();
    }
  }

}
