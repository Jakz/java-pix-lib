package com.pixbits.lib.ui.map;

import org.jxmapviewer.viewer.GeoPosition;
import org.jxmapviewer.viewer.Waypoint;

import com.pixbits.lib.io.xml.gpx.Coordinate;

public class Marker implements Waypoint
{
  public final Coordinate c;
  private final GeoPosition p;

  public Marker(Coordinate coord) { c = coord; p = new GeoPosition(coord.lat(), coord.lng()); }
  @Override public GeoPosition getPosition() { return p; }
  
  public static Marker of(Coordinate coord) { return new Marker(coord); }
}