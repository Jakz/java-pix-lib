package com.pixbits.lib.ui.charts;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;

import javax.swing.event.AncestorEvent;
import javax.swing.event.AncestorListener;

import com.pixbits.lib.functional.MinMaxCollector;
import com.pixbits.lib.lang.Point;
import com.pixbits.lib.lang.Rect;
import com.pixbits.lib.lang.Size;
import com.pixbits.lib.ui.canvas.Arc;
import com.pixbits.lib.ui.canvas.Brush;
import com.pixbits.lib.ui.canvas.CanvasPanel;
import com.pixbits.lib.ui.color.MappableColorGenerator;
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
    
    final MappableColorGenerator<T> scg = new MappableColorGenerator<>(new PleasantColorGenerator());
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
}
