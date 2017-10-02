package com.pixbits.lib.io.stream;

import java.io.FilterInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.ConcurrentModificationException;
import java.util.List;

import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

public class MonitoredInputStream extends FilterInputStream
{
  private volatile long mark = 0;
  private volatile long lastTriggeredLocation = 0;
  private volatile long location = 0;
  private final int threshold;
  private final List<ChangeListener> listeners = new ArrayList<>(4);

  public MonitoredInputStream(InputStream in, int threshold)
  {
    super(in);
    this.threshold = threshold;
  }

  public MonitoredInputStream(InputStream in)
  {
    super(in);
    this.threshold = 1024*16;
  }

  public void addChangeListener(ChangeListener l) { if (!listeners.contains(l)) listeners.add(l); }
  public void removeChangeListener(ChangeListener l) { listeners.remove(l); }

  protected void triggerChanged(final long location)
  {
    if (threshold > 0 && Math.abs(location-lastTriggeredLocation) < threshold)
      return;

    lastTriggeredLocation = location;

    if (listeners.isEmpty())
      return;
    
    try
    {
      final ChangeEvent evt = new ChangeEvent(this);
      for (ChangeListener l : listeners)
        l.stateChanged(evt);
    } 
    catch (ConcurrentModificationException e)
    {
      triggerChanged(location);
    }
  }


  @Override public int read() throws IOException
  {
    final int i = super.read();
    if (i != -1) triggerChanged(location++);
    return i;
  }

  @Override public int read(byte[] b, int off, int len) throws IOException
  {
    final int i = super.read(b, off, len);
    if (i > 0) triggerChanged(location += i);
    return i;
  }

  @Override public long skip(long n) throws IOException
  {
    final long i = super.skip(n);
    if (i > 0) triggerChanged(location += i);
    return i;
  }

  @Override public void mark(int readlimit)
  {
    super.mark(readlimit);
    mark = location;
  }

  @Override public void reset() throws IOException
  {
    super.reset();
    if (location != mark) 
      triggerChanged( location = mark );
  }
  
  public long location() { return location; }
}
