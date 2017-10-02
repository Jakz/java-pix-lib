package com.pixbits.lib.io.xml.gpx;

import java.math.BigDecimal;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;
import javax.xml.bind.annotation.adapters.XmlAdapter;

public class GpxWaypointAdapter extends XmlAdapter<GpxWaypointAdapter.JAXBGpxWaypoint,GpxWaypoint>
{
  @XmlType(propOrder={"lon", "lat", "ele", "time", "extensions"})
  static class JAXBGpxWaypoint
  {
    @XmlAttribute(required = true) BigDecimal lat;
    @XmlAttribute(required = true) BigDecimal lon;
    @XmlElement GpxTime time;
    @XmlElement BigDecimal ele;
    @XmlElement GpxExtension extensions;

  }
  
  @Override
  public GpxWaypoint unmarshal(GpxWaypointAdapter.JAXBGpxWaypoint xw) throws Exception
  {
    GpxWaypoint waypoint = new GpxWaypoint();
    if (xw.ele != null)
      waypoint.coordinate = new Coordinate(xw.lat.doubleValue(), xw.lon.doubleValue(), xw.ele.doubleValue());
    else
      waypoint.coordinate = new Coordinate(xw.lat.doubleValue(), xw.lon.doubleValue());
    
    if (xw.time != null)
      waypoint.time = xw.time.time;
    
    waypoint.extensions = xw.extensions;
    
    return waypoint;
  }

  @Override
  public JAXBGpxWaypoint marshal(GpxWaypoint w) throws Exception
  {
    JAXBGpxWaypoint xw = new JAXBGpxWaypoint();
    
    xw.lat = BigDecimal.valueOf(w.coordinate.lat());
    xw.lon = BigDecimal.valueOf(w.coordinate.lng());
    
    if (w.time != null)
      xw.time = new GpxTime(w.time);
    
    if (!Double.isNaN(w.coordinate.alt()))
      xw.ele = BigDecimal.valueOf(w.coordinate.alt());
    
    xw.extensions = w.extensions;
    
    return xw;
  }

}
