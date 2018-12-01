package com.pixbits.lib.io.xml.gpx;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;

import javax.xml.bind.annotation.XmlElement;

public class GpxTrack
{
  @XmlElement String name;
  @XmlElement(name = "cmt") String comment;
  @XmlElement(name = "desc") String description;
  @XmlElement(name = "src") String source;
  @XmlElement(name = "type") String type;
  @XmlElement(name = "link") List<GpxLink> links;
  @XmlElement(name = "number") Integer trackNumber;
  @XmlElement GpxExtension extensions;
  @XmlElement(name = "trkseg") List<GpxTrackSegment> segments;
  
  GpxTrack()
  {

  }
  
  public GpxTrack(String name)
  {
    this.name = name;
    this.segments = new ArrayList<>();
  }
  
  public List<GpxTrackSegment> segments() { return segments; }
  public String name() { return name; }
  
  public Stream<GpxTrackSegment> stream() { return segments().stream(); }
}
