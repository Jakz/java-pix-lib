package com.pixbits.lib.io.xml.gpx;

import jakarta.xml.bind.annotation.XmlAttribute;
import jakarta.xml.bind.annotation.XmlElement;

public class GpxCopyright
{
  @XmlAttribute String author;
  @XmlElement int year;
  @XmlElement String license;
}
