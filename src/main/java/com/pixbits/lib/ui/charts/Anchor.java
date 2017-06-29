package com.pixbits.lib.ui.charts;

public enum Anchor
{
  LEFT(false),
  RIGHT(false),
  TOP(true),
  BOTTOM(true)
  ;
  
  private Anchor(boolean vertical)
  {
    this.vertical = vertical;
  }
  
  public final boolean vertical;
}
