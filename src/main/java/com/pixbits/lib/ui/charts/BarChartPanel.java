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
import com.pixbits.lib.lang.FloatRange;
import com.pixbits.lib.lang.Pair;
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
  
  private Pair<NormalizeMode, NormalizeMode> axisNormalize;
  
  private boolean autoRebuild;
  
  private AncestorListener shownListener;

  public BarChartPanel(Dimension dimension)
  {
    super(dimension);
    data = new ArrayList<>();
    
    brush = new Brush(Color.RED);
    fillMode = FillMode.FILL;
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
  protected BiFunction<Integer, Float, Rect> calculateBarBuilder(Rect bounds, float barDelta, float barWidth)
  {
    switch (anchor)
    {
      case BOTTOM:
      {
        return (i, v) -> {
          float x = i * barDelta;
          int y = (int) ((1.0f - v) * bounds.h);
          
          float w = barWidth;
          int h = bounds.h - y;
          
          return new Rect((int)(bounds.x+x), bounds.y+y, (int)w, h);
        };
      }
      
      case TOP:
      {
        return (i, v) -> {
          float x = i * barDelta;
          int y = 0;
          
          float w = barWidth;
          int h = (int)(bounds.h * v);
          
          return new Rect((int)(bounds.x+x), bounds.y+y, (int)w, h);
        };
      }
      
      case LEFT:
      {
        return (i, v) -> {
          int x = 0;
          float y = i * barDelta;
          
          int w = (int)(bounds.w * v);
          float h = barWidth;
          
          return new Rect(bounds.x+x, (int)(bounds.y+y), w, (int)h);
        };
      }
      
      case RIGHT:
      {
        return (i, v) -> {
          int x = (int) ((1.0f - v) * bounds.w);
          float y = i * barDelta;
          
          int w = bounds.w - x;
          float h = barWidth;
          
          return new Rect(bounds.x+x, (int)(bounds.y+y), w, (int)h);
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
    
    Rect bounds = new Rect(5, 5, getWidth()-10, getHeight()-10);
    float barWidth = this.barWidth;
    final int barMargin = this.barMargin;
    float barDelta = barWidth + barMargin;
    
    float min, max;

    /* precompute parameters */
    firstIndex = 0;
    
    if (fillMode == FillMode.DONT_RESIZE)
    {
      int maxShown = (int)(bounds.w / (barWidth+barMargin));

      lastIndex = Math.min(maxShown, data.size());     
      barWidth = this.barWidth;
      barDelta = this.barWidth + barMargin;
    }
    else if (fillMode == FillMode.FILL_FRACTIONAL)
    {
      float allowedPerRow = bounds.w / (float)data.size();
      
      lastIndex = data.size();
      barWidth = allowedPerRow - barMargin;
      barDelta = allowedPerRow;
    }
    else if (fillMode == FillMode.FILL)
    {
      int varyingBound = anchor.vertical ? bounds.w : bounds.h;
      int allowedPerRow = (int) (varyingBound / (float)data.size());
      int adjustedSize = allowedPerRow * data.size();
      int deltaBoundMargin = varyingBound - adjustedSize;
      
      System.out.println("allowedPerRow: "+allowedPerRow+" ajustedSize: "+adjustedSize+" bounds: "+bounds);
      
      int firstMargin = deltaBoundMargin / 2;
      
      if (!anchor.vertical)
      {
        bounds.h = adjustedSize;
        bounds.y = firstMargin;
      }
      else
      {
        bounds.w = adjustedSize;
        bounds.x = firstMargin;
      }
      
      lastIndex = data.size();
      barWidth = allowedPerRow - barMargin;
      barDelta = allowedPerRow;
    }

    MinMaxCollector<Measurable> minMax = data.subList(firstIndex, lastIndex).stream()
      .collect(MinMaxCollector.of((o1,o2) -> Float.compare(o1.chartValue(), o2.chartValue())));
    
    min = minMax.min().chartValue();
    max = minMax.max().chartValue();
    
    BiFunction<Integer, Float, Rect> rectBuilder = calculateBarBuilder(bounds, barDelta, barWidth);
    
    /* draw bars */
    final int count = lastIndex - firstIndex;
    for (int i = 0; i < count; ++i)
    {
      int which = i + firstIndex;
      float value = (data.get(which).chartValue() - min) / (max - min);
      
      Rect rect = rectBuilder.apply(which, value);
      //System.out.println("Rect("+rect+")");
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
