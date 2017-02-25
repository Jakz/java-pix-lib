package com.pixbits.lib.io.xml;

import java.io.CharArrayWriter;
import java.time.ZonedDateTime;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.Map;
import java.util.Stack;
import java.util.function.Predicate;

import javax.xml.bind.DatatypeConverter;
import javax.xml.bind.annotation.adapters.HexBinaryAdapter;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

public abstract class XMLHandler<T> extends DefaultHandler
{
  private final CharArrayWriter buffer = new CharArrayWriter();
  private final Stack<Map<String,Object>> stack = new Stack<>();
  
  private final HexBinaryAdapter hexConverter = new HexBinaryAdapter();
  
  private Attributes currentAttributes = null;

  protected String asString() { return buffer.toString().replaceAll("[\r\n]"," ").trim(); }
  
  protected ZonedDateTime asZonedDateTime()
  {
    String value = asString();
    GregorianCalendar calendar = (GregorianCalendar)DatatypeConverter.parseDateTime(value);
    ZonedDateTime time = calendar.toZonedDateTime();
    return time;
  }
  
  protected void assertTrue(boolean condition) throws SAXException
  {
    if (!condition)
      throw new SAXException();
  }
  
  protected int asInt()
  {
    String value = asString(); 
    return !value.isEmpty() ? Integer.parseInt(asString()) : 0;
  }
  
  protected long asLong()
  {
    String value = asString();
    return !value.isEmpty() ? Long.parseLong(asString()) : 0;
  }
  
  protected float asFloat()
  {
    String value = asString();
    return !value.isEmpty() ? Float.parseFloat(asString()) : 0.0f;
  }
  
  protected double asDouble()
  {
    String value = asString();
    return !value.isEmpty() ? Double.parseDouble(asString()) : 0.0;
  }
  
  protected long asHexLong()
  {
    String value = asString();
    return !value.isEmpty() ? Long.parseLong(asString(), 16) : 0;
  }
  
  @Override public final void characters(char[] ch, int start, int length)
  {
    buffer.write(ch,start,length);
  }
  
  @Override public void endElement(String namespaceURI, String name, String qName) throws SAXException
  {
    end(namespaceURI, name);
    stack.pop();
  }
  
  @Override public void startElement(String namespaceURI, String name, String qName, Attributes attr) throws SAXException
  {
    currentAttributes = attr;
    clearBuffer();
    stack.push(new HashMap<>());
    start(namespaceURI, name, attr);
  }
  
  protected String attrString(String key) throws SAXException
  { 
    String value = currentAttributes.getValue(key);
    
    if (value == null)
      throw new SAXException(String.format("Attribute '%s' is missing.", key));
      
    return currentAttributes.getValue(key);
  }
  
  protected long longAttributeOrDefault(String key, long value)
  {
    String o = currentAttributes.getValue(key);
    
    if (o == null)
      return value;
    else if (o.equals("EOF"))
      return Long.MIN_VALUE;
    else
      return Long.valueOf(o);
  }
  
  protected byte[] hexByteArray(String key)
  {
    return hexConverter.unmarshal(currentAttributes.getValue(key));
  }
  
  protected boolean boolOrDefault(String key, boolean value)
  {
    String o = currentAttributes.getValue(key);
    return o != null ? Boolean.valueOf(key) : value; 
  }
  
  protected long longHexAttributeOrDefault(String key, long value)
  {
    String o = currentAttributes.getValue(key);
    
    if (o == null)
      return value;
    else if (o.equals("EOF"))
      return Long.MIN_VALUE;
    else
      return Long.valueOf(o, 16);
  }
  
  protected String stringAttributeOrDefault(String key, String value)
  {
    String o = currentAttributes.getValue(key);
    return o != null ? o : value;
  }
  
  @SuppressWarnings("unchecked")
  protected <U> U value(String key) { return (U)stack.peek().get(key); }
  @SuppressWarnings("unchecked")
  protected <U> U valueOrDefault(String key, U value) { return (U)stack.peek().getOrDefault(key, value); }

  protected void map(String key, Object o) { stack.peek().put(key, o); }
  protected void mapOuter(String key, Object o) { stack.get(stack.size()-2).put(key, o); }
  
  protected double getDoubleAttribute(String key, Predicate<Double> predicate)
  {
    try
    {
      double d = Double.parseDouble(currentAttributes.getValue(key));
      if (!predicate.test(d))
        throw new NumberFormatException();
      return d;
    }
    catch (NullPointerException | NumberFormatException e)
    {
      throw new IllegalArgumentException(
        String.format("Attribute %s is missing, or not parseable as double or not satifsfying predicate", key)
      );
    }
  }
  
  protected final void clearBuffer() { buffer.reset(); }
  
  protected abstract void init();
  protected abstract void start(String ns, String name, Attributes attr) throws SAXException;
  protected abstract void end(String ns, String name) throws SAXException;
  
  abstract public T get();
}
