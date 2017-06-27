package com.pixbits.lib.ui.charts;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.event.ComponentListener;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.function.BiFunction;

import javax.swing.JPanel;
import javax.swing.event.AncestorEvent;
import javax.swing.event.AncestorListener;

import com.pixbits.lib.functional.MinMaxCollector;
import com.pixbits.lib.lang.Rect;
import com.pixbits.lib.ui.canvas.Brush;
import com.pixbits.lib.ui.canvas.CanvasPanel;
import com.pixbits.lib.ui.canvas.Rectangle;

public class BarChartPanel<T extends Measurable> extends CanvasPanel
{
  private final List<T> data;
  
  private Brush brush;
  private FillMode fillMode;
  private Anchor anchor;
  private int barWidth;
  private int barMargin;
  
  private boolean autoRebuild;
  private AncestorListener shownListener;

  public BarChartPanel(Dimension dimension)
  {
    super(dimension);
    data = new ArrayList<>();
    
    brush = new Brush(Color.RED);
    fillMode = FillMode.DONT_RESIZE;
    anchor = Anchor.BOTTOM;
    barWidth = 1;
    barMargin = 0;
    
    autoRebuild = false;
    
    this.addComponentListener(new ComponentAdapter()
    {
      @Override public void componentResized(ComponentEvent event)
      {
        rebuild();
      }
    });
  }
  
  public void add(T measurable)
  {
    data.add(measurable);
    if (autoRebuild)
      rebuild();
  }
  
  public void add(Collection<T> measurables)
  {
    data.addAll(measurables);
  }
  
  public void add(T... measurables)
  {
    data.addAll(Arrays.asList(measurables));
    if (autoRebuild)
      rebuild();
  }
  
  public void insert(int index, T measurable)
  {
    data.add(index, measurable);
    if (autoRebuild)
      rebuild();
  }
  
  public void setAutoRebuild(boolean autoRebuild)
  {
    this.autoRebuild = autoRebuild;
  }
  
  /* generate a function which calculates bars */
  protected BiFunction<Integer, Float, Rect> calculateBarBuilder(Rect bounds, int barDelta)
  {
    switch (anchor)
    {
      case BOTTOM:
      {
        return (i, v) -> {
          int x = i * barDelta;
          int y = (int) ((1.0f - v) * bounds.h);
          
          int w = barWidth;
          int h = bounds.h - y;
          
          return new Rect(bounds.x+x, bounds.y+y, w, h);
        };
      }
      
      case TOP:
      {
        return (i, v) -> {
          int x = i * barDelta;
          int y = 0;
          
          int w = barWidth;
          int h = (int)(bounds.h * v);
          
          return new Rect(bounds.x+x, bounds.y+y, w, h);
        };
      }
      
      case LEFT:
      {
        return (i, v) -> {
          int x = 0;
          int y = i * barDelta;
          
          int w = (int)(bounds.w * v);
          int h = barWidth;
          
          return new Rect(bounds.x+x, bounds.y+y, w, h);
        };
      }
      
      case RIGHT:
      {
        return (i, v) -> {
          int x = (int) ((1.0f - v) * bounds.w);
          int y = i * barDelta;
          
          int w = bounds.w - x;
          int h = barWidth;
          
          return new Rect(bounds.x+x, bounds.y+y, w, h);
        };
      }
    }
    
    return null;
  }
  
  protected void rebuild()
  {
    /* if panel is not visible we need to delay this when it is shown because we have
     * no correct size of the component */
    if (!isShowing())
    {
      if (shownListener == null)
      {
        shownListener = new AncestorListener() {
          @Override public void ancestorAdded(AncestorEvent event) { rebuild(); }
          @Override public void ancestorRemoved(AncestorEvent event) { }
          @Override public void ancestorMoved(AncestorEvent event) { }
        };
        this.addAncestorListener(shownListener);
      }
      return;
    }
    
    clear();
    
    /* required parameters */
    int firstIndex, lastIndex = 0;
    
    final Rect bounds = new Rect(5, 5, getWidth()-10, getHeight()-10);
    final int barWidth = this.barWidth;
    final int barMargin = this.barMargin;
    final int barDelta = barWidth + barMargin;
    
    float min, max;

    /* precompute parameters */
    firstIndex = 0;
    
    if (fillMode == FillMode.DONT_RESIZE)
    {
      int maxShown = bounds.w / (barWidth+barMargin);
      lastIndex = Math.min(maxShown, data.size());
    }

    MinMaxCollector<Measurable> minMax = data.subList(firstIndex, lastIndex).stream()
      .collect(MinMaxCollector.of((o1,o2) -> Float.compare(o1.chartValue(), o2.chartValue())));
    
    min = minMax.min().chartValue();
    max = minMax.max().chartValue();
    
    BiFunction<Integer, Float, Rect> rectBuilder = calculateBarBuilder(bounds, barDelta);
    
    /* draw bars */
    final int count = lastIndex - firstIndex;
    for (int i = 0; i < count; ++i)
    {
      int which = i + firstIndex;
      float value = (data.get(which).chartValue() - min) / (max - min);
      
      Rect rect = rectBuilder.apply(which, value);
      System.out.println("Rect("+rect+")");
      add(new Rectangle(rect, brush));
    }
    
    repaint();
    
    if (shownListener != null)
    {
      this.removeAncestorListener(shownListener);
      shownListener = null;
    }
  }
}
