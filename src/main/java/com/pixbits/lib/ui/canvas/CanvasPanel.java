package com.pixbits.lib.ui.canvas;

import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import javax.swing.JPanel;

public class CanvasPanel extends JPanel implements EntityHolder
{
  private Gfx gfx;
  
  private final List<Entity> entities;
  private boolean dirty = false;
  
  public CanvasPanel(Dimension dimension)
  {
    this.setPreferredSize(dimension);
    this.setOpaque(true);
    this.entities = new ArrayList<Entity>();
  }
  
  protected void sort()
  {
    if (dirty)
    {
      Collections.sort(entities);
      dirty = false;
    }
  }
  
  public void add(Entity entity)
  { 
    entities.add(entity);
    entity.container(this);
    dirty = true;
  }
  
  public void clear()
  {
    entities.clear();
  }
  
  @Override public void markDirty()
  {
    boolean wasDirty = dirty;
    dirty = true;
    if (wasDirty) repaint();
  }
  
  @Override public void paintComponent(Graphics gx)
  {
    Graphics2D g = (Graphics2D)gx;
   
    GfxContext context = new GfxContext(new Gfx(g, true));
    
    if (dirty)
      sort();
        
    for (Entity entity : entities)
    {
      entity.draw(context);
    }
    
  }
  
  
}
