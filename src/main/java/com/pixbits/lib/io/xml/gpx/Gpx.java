package com.pixbits.lib.io.xml.gpx;

import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Stream;

import javax.xml.bind.annotation.XmlAnyAttribute;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.namespace.QName;

@XmlRootElement(name = "gpx") 
public class Gpx
{
  private Map<QName, Object> any;
  @XmlAnyAttribute Map<QName, Object> getAny() { if (any == null) any = new HashMap<>(); return any; }
  
  @XmlAttribute(required = true) String version;
  @XmlAttribute(required = true) String creator;
   
  @XmlElement GpxMetadata metadata;
  @XmlElement GpxExtension extensions;
  @XmlElement(name = "trk") List<GpxTrack> tracks;
  @XmlElement List<GpxRoute> rte;
  @XmlElement List<GpxWpt> wpt;
  /*List<GpxWaypoint> waypoints;*/

  public Gpx()
  { 
    tracks = new ArrayList<>();
  }
 
  public Gpx(String version, String creator)
  {
    this.version = version;
    this.creator = creator;
    tracks = new ArrayList<>();
    //waypoints = new ArrayList<>();
  }
  
  public List<GpxTrack> tracks() { return tracks; }
  public String name() { return metadata.name; }
  public ZonedDateTime time() { return metadata.time.time; }
  
  public void addTrack(GpxTrack track) { tracks.add(track); }
  public void setName(String name) { metadata.name = name; }
  
  public Stream<GpxTrack> stream() { return tracks().stream(); }
}
