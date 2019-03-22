package com.pixbits.lib.ui.map;

import org.jxmapviewer.JXMapViewer;
import org.jxmapviewer.painter.Painter;

public interface MapPainter extends Painter<JXMapViewer>
{
  public void invalidate();
}
