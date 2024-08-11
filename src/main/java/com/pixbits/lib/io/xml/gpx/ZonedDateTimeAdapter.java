package com.pixbits.lib.io.xml.gpx;

import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.GregorianCalendar;

import jakarta.xml.bind.DatatypeConverter;
import jakarta.xml.bind.annotation.adapters.XmlAdapter;

public class ZonedDateTimeAdapter extends XmlAdapter<String,GpxTime>
{
  private final DateTimeFormatter formatter;

  ZonedDateTimeAdapter()
  {
    formatter = DateTimeFormatter.ISO_OFFSET_DATE_TIME;
  }
  
  @Override
  public GpxTime unmarshal(String value) throws Exception
  {
    GregorianCalendar calendar = (GregorianCalendar)DatatypeConverter.parseDateTime(value);
    ZonedDateTime time = calendar.toZonedDateTime();
    return new GpxTime(time);
  }

  @Override
  public String marshal(GpxTime v) throws Exception
  {
    return formatter.format(v.time);
  }

}
