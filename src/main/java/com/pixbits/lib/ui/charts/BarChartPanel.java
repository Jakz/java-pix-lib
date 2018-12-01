package com.pixbits.lib.ui.charts;

import java.awt.Color;
import java.awt.Dimension;
import java.util.function.BiFunction;
import java.util.function.Function;

import com.pixbits.lib.functional.MinMaxCollector;
import com.pixbits.lib.lang.Pair;
import com.pixbits.lib.lang.Rect;
import com.pixbits.lib.ui.canvas.Brush;
import com.pixbits.lib.ui.canvas.Rectangle;
import com.pixbits.lib.ui.color.HashColorCache;
import com.pixbits.lib.ui.color.PleasantColorGenerator;

public class BarChartPanel<T extends Measurable> extends ChartPanel<T>
{  
  private Function<T, Brush> brush;
  private FillMode fillMode;
  private Anchor anchor;
  private int barWidth;
  private int barMargin;
  
  private Pair<NormalizeMode, NormalizeMode> axisNormalize;
  
  public BarChartPanel(Dimension dimension)
  {
    super(dimension);
    
    //brush = m -> cbrush;
    final HashColorCache<T> scg = new HashColorCache<>(new PleasantColorGenerator());
    brush = m -> new Brush(scg.getColor(m), Color.BLACK);
    
    fillMode = FillMode.FILL_FRACTIONAL;
    anchor = Anchor.BOTTOM;
    barWidth = 1;
    barMargin = 0;
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
  
  @Override
  protected void doRebuild()
  {
    /*GradientColorGenerator gcg = new GradientColorGenerator(
        new com.pixbits.lib.ui.color.Color[] {
          new com.pixbits.lib.ui.color.Color(Color.GREEN),
          new com.pixbits.lib.ui.color.Color(Color.RED), 
          new com.pixbits.lib.ui.color.Color(Color.BLUE),
          new com.pixbits.lib.ui.color.Color(Color.YELLOW),
        },
        new float[] {
          0.2f,
          0.2f,
          10.0f,
        }
    );
    for (int i = 0; i < getWidth(); ++i)
    {
      float v = i / (float)getWidth();
      com.pixbits.lib.ui.color.Color color = gcg.getColor(v);
      this.add(new Line(new Point(i, 0), new Point(i, getHeight()), new Brush(color.toAWT(), 1.0f)));

    }
    
    if (true)
      return;*/
    
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
      T element = data.get(which);

      float value = (element.chartValue() - min) / (max - min);
      
      Rect rect = rectBuilder.apply(which, value);
      //System.out.println("Rect("+rect+")");
      add(new Rectangle(rect, brush.apply(element)));
    }
    
    repaint();
  }
}
