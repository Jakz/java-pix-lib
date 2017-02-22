package com.pixbits.lib.io.xml;

import java.io.IOException;
import java.nio.file.Path;

import org.xml.sax.EntityResolver;
import org.xml.sax.ErrorHandler;
import org.xml.sax.SAXException;
import org.xml.sax.SAXParseException;
import org.xml.sax.XMLReader;
import org.xml.sax.helpers.XMLReaderFactory;

public class XMLParser<T> implements ErrorHandler
{
  private final XMLHandler<T> handler;
  private final EntityResolver resolver;
  
  public XMLParser(XMLHandler<T> handler)
  {
    this(handler, null);
  }
  
  public XMLParser(XMLHandler<T> handler, EntityResolver resolver)
  {
    this.handler = handler;
    this.resolver = resolver;
  }
  
  public T load(Path file) throws IOException, SAXException
  {
    XMLReader reader = XMLReaderFactory.createXMLReader();
    reader.setErrorHandler(this);
    reader.setContentHandler(handler);
    if (resolver != null)
      reader.setEntityResolver(resolver);
    handler.init();
    reader.parse(file.toString());
    return handler.get();
  }

  private String getParseExceptionInfo(SAXParseException spe)
  {
    String systemId = spe.getSystemId();
    if (systemId == null) systemId = "null";
    return String.format("URI=%s Line=%d: %s", systemId, spe.getLineNumber(), spe.getMessage());
  }

  
  @Override
  public void warning(SAXParseException exception) throws SAXException
  {
  
  }

  @Override
  public void error(SAXParseException exception) throws SAXException
  {
    String message = "Error: " + getParseExceptionInfo(exception);
    throw new SAXException(message);
  }

  @Override
  public void fatalError(SAXParseException exception) throws SAXException
  {
    String message = "Fatal Error: " + getParseExceptionInfo(exception);
    throw new SAXException(message);
  }
  
  
}
