package com.pixbits.lib.io.xml;

import java.util.Stack;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

public abstract class XMLScopedHandler<T> extends XMLHandler<T>
{
  protected final Stack<String> scopes = new Stack<>();
  
  @Override public final void endElement(String namespaceURI, String name, String qName) throws SAXException
  {
    scopes.pop();
    //System.out.println("scopes.pop: "+Arrays.toString(scopes.toArray()));
    super.endElement(namespaceURI, name, qName);
  }
  
  @Override public final void startElement(String namespaceURI, String name, String qName, Attributes attr) throws SAXException
  {
    super.startElement(namespaceURI, name, qName, attr);
    scopes.push(name);
    //System.out.println("scopes.push: "+Arrays.toString(scopes.toArray()));
  }
  
  protected void assertCurrentScope(String name) throws SAXException
  {
    if (!isCurrentScope(name))
      throw new SAXException("Expected current scope "+name+" but found "+scopes.peek());
  }
  
  protected String currentScope()
  { 
    return scopes.isEmpty() ? "" : scopes.peek();
  }
    
  protected boolean isCurrentScope(String name)
  {
    return scopes.peek().equals(name);
  }
  
  protected boolean isHierarchyScope(String name)
  {
    // TODO:
    return false;
  }
  
}
