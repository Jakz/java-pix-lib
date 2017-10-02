package com.pixbits.lib.io.xml.gpx;

import java.time.ZonedDateTime;

import javax.xml.bind.annotation.XmlType;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;

@XmlType
@XmlJavaTypeAdapter(ZonedDateTimeAdapter.class)
public class GpxTime
{
  ZonedDateTime time;
  
  GpxTime() { }
  GpxTime(ZonedDateTime time) { this.time = time; }
}
