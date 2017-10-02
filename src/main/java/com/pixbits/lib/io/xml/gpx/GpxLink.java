package com.pixbits.lib.io.xml.gpx;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;

public class GpxLink
{
  @XmlAttribute(name = "href", required = true) String uri;
  @XmlElement String text;
  @XmlElement String mimeType;
}
