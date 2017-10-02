package com.pixbits.lib.io.xml.gpx;

import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.annotation.XmlAnyElement;

import org.w3c.dom.Element;

public class GpxWpt
{
  protected List<Element> elements;

  @XmlAnyElement
  public List<Element> getExtensions()
  {
    if (elements == null) 
      elements = new ArrayList<Element>();
    return elements;   
  }
}
