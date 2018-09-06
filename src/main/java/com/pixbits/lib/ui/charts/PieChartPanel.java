package com.pixbits.lib.ui.charts;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionListener;
import java.util.Arrays;
import java.util.function.Function;
import com.pixbits.lib.functional.MinMaxCollector;
import com.pixbits.lib.lang.Point;
import com.pixbits.lib.lang.Size;
import com.pixbits.lib.ui.canvas.Arc;
import com.pixbits.lib.ui.canvas.Brush;
import com.pixbits.lib.ui.charts.events.PieChartMouseListener;
import com.pixbits.lib.ui.color.ColorCache;
import com.pixbits.lib.ui.color.PleasantColorGenerator;

public class PieChartPanel<T extends Measurable> extends ChartPanel<T>
{
  Size pieSize;
  boolean keepSquare;
  
  private Function<T, Brush> brush;


  public PieChartPanel(Dimension dimension)
  {
    super(dimension);
    
    pieSize = new Size(0.5f,0.5f);
    keepSquare = false;
    
    final ColorCache<T> scg = new ColorCache<>(new PleasantColorGenerator());
    brush = m -> new Brush(scg.getColor(m), Color.BLACK, 2.0f);
  }
  
  @Override protected void doRebuild()
  {    
    clear();
    
    if (!data.isEmpty())
    {
      final Point center = new Point(getWidth()/2, getHeight()/2);
      Size realSize = null;
      
      if (!keepSquare)
      {
        realSize = new Size(pieSize.w * getWidth(), pieSize.h * getHeight());
      }
      
      MinMaxCollector<Measurable> minMax = data.stream()
          .collect(MinMaxCollector.of((o1,o2) -> Float.compare(o1.chartValue(), o2.chartValue())));
      
      float sum = (float)data.stream().mapToDouble(Measurable::chartValue).sum();
      
      final float min = minMax.min().chartValue();
      final float max = minMax.max().chartValue();
      
      if (min <= 0.0f)
        throw new IllegalArgumentException("PieChartPanel doesn't accept non-positive values.");
            
      float s = 0.0f;
      for (T t : data)
      {
        float nvalue = t.chartValue() / sum;
        
        float sangle = s * -360.0f;
        float delta = nvalue * -360.0f;
        
        add(new Arc(center.x - realSize.w/2, center.y - realSize.h/2, realSize.w, realSize.h, sangle, delta, brush.apply(t)));
        s += nvalue;
      }
    }

    repaint();
  }
  
  private static class WrapperListener extends MouseAdapter
  {
    enum State
    {
      OUTSIDE_PIE,
      INSIDE_PIE
    };
    
    private State state;
    private final PieChartPanel<?> parent;
    private final PieChartMouseListener listener;
    
    public WrapperListener(PieChartPanel<?> parent, PieChartMouseListener listener)
    { 
      this.state = State.OUTSIDE_PIE;
      this.parent = parent;
      this.listener = listener; 
    }
    
    @Override
    public void mouseMoved(MouseEvent e)
    {
      final int x = e.getX(), y = e.getY();
      final Point center = new Point(parent.getWidth()/2, parent.getHeight()/2);

      //TODO: keepSquare management?
      
      float dx = Math.abs(center.x - x), dy = Math.abs(center.y - y);
      float distance = (float)Math.sqrt(dx*dx + dy*dy);
      float radius = parent.pieSize.w/2.0f * parent.getWidth();
      //System.out.println(distance+" "+radius);
      
      boolean isInside = distance <= radius; 
      
      if (isInside && state == State.OUTSIDE_PIE)
      {
        listener.enteredPie();
        state = State.INSIDE_PIE;
      }
      else if (!isInside && state == State.INSIDE_PIE)
      {
        listener.exitedPie();
        state = State.OUTSIDE_PIE;
      }
    }
  }
  
  public void addListener(PieChartMouseListener listener)
  {
    WrapperListener wlistener = new WrapperListener(this, listener);
    addMouseListener(wlistener);
    addMouseMotionListener(wlistener);
  }
}
