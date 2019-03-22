package com.pixbits.lib.ui.map;

import java.util.HashSet;
import java.util.Set;
import java.util.stream.Stream;

import org.jxmapviewer.viewer.WaypointPainter;

import com.pixbits.lib.io.xml.gpx.Coordinate;

public class MarkerPainter<T> extends WaypointPainter<Marker> implements MapPainter, Positionable
{
  private final Set<Marker> markers;
  
  public MarkerPainter()
  {
    this.markers = new HashSet<>();
  }
  
  public void addMarker(Marker marker, T data) 
  {
    markers.add(marker);
    invalidate();
  }
  
  public void clear()
  { 
    markers.clear(); 
    invalidate();
  }
  
  @Override
  public void invalidate()
  { 
    super.setWaypoints(markers);
  }

  @Override
  public Iterable<Coordinate> coordinates()
  {
    Stream<Coordinate> stream = markers.stream().map(m -> m.c);
    return stream::iterator;
  }
}