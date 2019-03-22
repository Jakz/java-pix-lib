package com.pixbits.lib.ui.map;

import com.pixbits.lib.io.xml.gpx.Bounds;

public interface JXMap
{
  void zoomToFit(Bounds bounds, float maxFraction);
}
