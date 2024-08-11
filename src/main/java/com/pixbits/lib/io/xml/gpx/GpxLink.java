package com.pixbits.lib.io.xml.gpx;

import jakarta.xml.bind.annotation.XmlAttribute;
import jakarta.xml.bind.annotation.XmlElement;

public class GpxLink
{
  @XmlAttribute(name = "href", required = true) String uri;
  @XmlElement String text;
  @XmlElement String mimeType;
}
