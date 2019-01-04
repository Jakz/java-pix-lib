package com.pixbits.lib.ui.map;

import com.pixbits.lib.io.xml.gpx.Coordinate;

public class MapElement<T extends Positionable>
{  
  private final T element;
  private final JXMap map;
  
  private boolean visible;
  
  public MapElement(JXMap map, T element)
  {
    this.map = map;
    this.element = element;
    visible = true; 
  }
  
  boolean isVisible() { return visible; }
  
  void hide() { visible = false; }
  void show() { visible = true; }
  
  public T element() { return element; }
}
