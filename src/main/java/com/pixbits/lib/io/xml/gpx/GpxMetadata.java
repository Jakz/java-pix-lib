package com.pixbits.lib.io.xml.gpx;

import java.util.List;

import javax.xml.bind.annotation.XmlElement;

public class GpxMetadata
{
  @XmlElement String name;
  @XmlElement(name = "desc") String description;
  @XmlElement GpxPerson author;
  @XmlElement GpxCopyright copyright;
  @XmlElement(name = "link") List<GpxLink> links;
  @XmlElement GpxTime time;
  @XmlElement String keywords;
  @XmlElement GpxBounds bounds;
  @XmlElement GpxExtension extensions;
  
  GpxMetadata()
  {
  }
}
