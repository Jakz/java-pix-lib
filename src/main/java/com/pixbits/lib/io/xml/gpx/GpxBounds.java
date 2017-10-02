package com.pixbits.lib.io.xml.gpx;

import java.math.BigDecimal;

import javax.xml.bind.annotation.XmlAttribute;

public class GpxBounds
{
  @XmlAttribute BigDecimal minlat;
  @XmlAttribute BigDecimal minlon;
  @XmlAttribute BigDecimal maxlat;
  @XmlAttribute BigDecimal maxlon;
}
