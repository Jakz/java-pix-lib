package com.pixbits.lib.log;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.function.Consumer;
import java.util.stream.Stream;

public class LogBuffer implements Iterable<LogBuffer.Entry>
{
  public static class Entry
  {
    public final Log level;
    public final LogScope scope;
    public final LogAttribute attrbute;
    public final String message;
    
    private Entry(Log level, LogScope scope, String message, LogAttribute attribute)
    {
      this.level = level;
      this.scope = scope;
      this.message = message;
      this.attrbute = attribute;
    }
  }
  
  public void setCallback(Consumer<LogBuffer> callback) { this.callback = callback; }
  
  private final List<Entry> entries;
  private Consumer<LogBuffer> callback;
  
  public void wipe() { entries.clear(); }
  
  @Override public Iterator<Entry> iterator() { return entries.iterator(); }
  public Stream<Entry> stream() { return entries.stream(); }
  public int size() { return entries.size(); }
  public Entry get(int index) { return entries.get(index); }
  
  public <T extends LogAttribute> void addEntry(Log level, LogScope scope, String message, T attribute)
  {
    synchronized (entries)
    {    
      entries.add(new Entry(level, scope, message, attribute));
      if (callback != null)
        callback.accept(this);
    }
  }
  
  public LogBuffer()
  {
    entries = new ArrayList<>();
  }
}
