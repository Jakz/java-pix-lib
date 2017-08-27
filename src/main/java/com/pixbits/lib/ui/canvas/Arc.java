package com.pixbits.lib.ui.canvas;

import java.awt.BasicStroke;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.Stroke;
import java.awt.geom.Arc2D;

import com.pixbits.lib.lang.Point;
import com.pixbits.lib.lang.Size;

public class Arc extends Entity
{
  Brush brush;
  
  final Point center;
  final Size size;
  final float angle, delta;
  
  Arc2D.Float arc;
  
  public Arc(float x, float y, float w, float h, float angle, float delta, Brush brush)
  {
    this.center = new Point(x,y);
    this.size = new Size(w,h);
    this.angle = angle;
    this.delta = delta;
    this.brush = brush;
    
    this.arc = new Arc2D.Float(x, y, w, h, angle, delta, Arc2D.PIE);
  }
  
  @Override
  void draw(GfxContext c)
  {
    Graphics2D g = c.gfx.g();
    
    c.gfx.saveColor();

    if (brush.hasFill())
    {
      g.setColor(brush.color());
      g.fill(arc);
    }
    
    if (brush.hasStroke())
    {
      Stroke stroke = new BasicStroke(brush.strokeWidth());
      g.setColor(brush.strokeColor());
      g.setStroke(stroke);
      g.draw(arc);  
    }
    
    c.gfx.restoreColor();
  }
}
