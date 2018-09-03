package com.pixbits.lib.ui.charts.events;

import com.pixbits.lib.ui.charts.Measurable;

public interface PieChartMouseListener
{
  default public void enteredPie() { }
  default public void exitedPie() { }
  
  public void enteredArc(Measurable measurable);
  public void exitedArc(Measurable measurable);
}
