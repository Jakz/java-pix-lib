package com.pixbits.lib.ui.charts;

import java.awt.Dimension;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

import javax.swing.event.AncestorEvent;
import javax.swing.event.AncestorListener;

import com.pixbits.lib.ui.canvas.CanvasPanel;

public abstract class ChartPanel<T extends Measurable> extends CanvasPanel
{
  protected final List<T> data;

  private boolean autoRebuild;
  private AncestorListener shownListener;
  
  public ChartPanel(Dimension dimension)
  {
    super(dimension);
    data = new ArrayList<>();
    
    autoRebuild = false; 
    this.addComponentListener(new ComponentAdapter()
    {
      @Override public void componentResized(ComponentEvent event)
      {
        rebuild();
      }
    });
  }
  
  private final void rebuild()
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
    
    doRebuild();
    
    if (shownListener != null)
    {
      this.removeAncestorListener(shownListener);
      shownListener = null;
    }
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
  
  protected abstract void doRebuild();
  
  

}
