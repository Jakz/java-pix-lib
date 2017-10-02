package com.pixbits.lib.io.xml.gpx;

import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.annotation.XmlAnyElement;

import org.w3c.dom.Element;

public class GpxExtension
{
  protected List<Element> extensions;

  @XmlAnyElement
  public List<Element> getExtensions()
  {
    if (extensions == null) 
      extensions = new ArrayList<Element>();
    return extensions;   
  }
}
