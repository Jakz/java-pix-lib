package com.pixbits.lib.io.stream;

import java.io.IOException;
import java.io.InputStream;

public class SkippingInputStream extends InputStream implements AutoCloseable
{
  private final InputStream is;
  private final int bytesToSkip;
  private int toSkip;
  
  public SkippingInputStream(InputStream is, int bytesToSkip)
  {
    this.is = is;
    this.bytesToSkip = bytesToSkip;
    this.toSkip = bytesToSkip;
  }
  
  private void skipToStart() throws IOException
  {
    if (toSkip > 0)
    {
      byte[] skip = new byte[toSkip];
      toSkip -= is.read(skip);
    }
  }
  
  @Override
  public int read() throws IOException
  {
    if (toSkip > 0) skipToStart();
    return is.read();   
  }
  
  @Override
  public int read(byte[] buffer) throws IOException
  {
    if (toSkip > 0) skipToStart();
    return is.read(buffer);
  }
  
  @Override
  public int read(byte[] buffer, int s, int l) throws IOException
  {
    if (toSkip > 0) skipToStart();
    return is.read(buffer, s, l);
  }
  
  @Override
  public void close() throws IOException
  {
    is.close();
  }
}
