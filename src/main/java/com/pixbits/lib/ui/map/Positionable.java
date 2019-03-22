package com.pixbits.lib.ui.map;

import java.util.stream.StreamSupport;

import com.pixbits.lib.io.xml.gpx.Bounds;
import com.pixbits.lib.io.xml.gpx.Coordinate;

public interface Positionable
{
  Iterable<Coordinate> coordinates();
  
  default void centerAndFit(JXMap map)
  {
    Bounds bounds = new Bounds();
    bounds.updateBound(coordinates());
    map.zoomToFit(bounds, 0.7f);
  }
  
  default Coordinate findNearestPointTo(Coordinate coordinate)
  {
    Iterable<Coordinate> coordinates = coordinates();
    return StreamSupport.stream(coordinates.spliterator(), false)
      .min((c1,c2) -> Double.compare(c1.distance(coordinate), c2.distance(coordinate)))
      .get();
  }
}
