package com.pixbits.lib.io.xml.gpx;

import java.time.ZonedDateTime;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;

@XmlJavaTypeAdapter(GpxWaypointAdapter.class)
public class GpxWaypoint
{
  Coordinate coordinate;
  ZonedDateTime time;
  GpxExtension extensions;
 
  public Coordinate coordinate() { return coordinate; }
  public ZonedDateTime time() { return time; }
  public GpxExtension extensions() { return extensions; }
  
  public void setCoordinate(Coordinate coordinate) { this.coordinate = coordinate; }
  public void setTime(ZonedDateTime time) { this.time = time; }
  public void setExtensions(GpxExtension extensions) { this.extensions = extensions; }
}
