package com.pixbits.lib.io.stream;

import java.io.BufferedInputStream;
import java.io.DataInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;

public class BinaryInputStream extends InputStream implements AutoCloseable
{
  private final DataInputStream is;
  private ByteOrder order;
  private byte[] buffer;
  private StringBuilder builder;
  
  private boolean isLE() { return order == ByteOrder.LITTLE_ENDIAN; }
  
  public BinaryInputStream(InputStream is, ByteOrder order)
  {
    this.buffer = new byte[8];
    this.is = new DataInputStream(new BufferedInputStream(is));
    this.order = order;
  }

  public BinaryInputStream(InputStream is)
  {
    this(is, ByteOrder.LITTLE_ENDIAN);
  }

  @Override
  public int read() throws IOException
  {
    return is.read();
  }
 
  @Override 
  public void close() throws IOException 
  {
    is.close();
  }
  
  public int readU8() throws IOException 
  {
    return is.read();
  }
  
  public int readU16() throws IOException
  {
    if (isLE())
    {
      int l = is.read(), h = is.read();
      return (h << 8) | l;
    }
    else
      return is.readUnsignedShort();
  }

  public long readU32() throws IOException
  {
    if (isLE())
    {
      is.read(buffer, 0, 4);
      return ((buffer[0] & 0xFF) | ((buffer[1] & 0xFF) << 8) | ((buffer[2] & 0xFF) << 16) | ((buffer[3] & 0xFF) << 24)) & 0xFFFFFFFFL;
    }
    else
      return is.readInt() & 0xFFFFFFFFL;
  }
  
  public long readU64() throws IOException
  {
    is.read(buffer, 0, 8);
    ByteBuffer bbuffer = ByteBuffer.wrap(buffer);
    bbuffer.order(order);
    return bbuffer.getLong();
  }
  
  public double readDouble() throws IOException
  {
    is.read(buffer, 0, 8);
    ByteBuffer bbuffer = ByteBuffer.wrap(buffer);
    bbuffer.order(order);
    return bbuffer.getDouble();
  }
  
  public String readNullTerminatedString() throws IOException
  {
    if (builder == null)
      builder = new StringBuilder();
    else
      builder.delete(0, builder.length());
    
    do
    {
      int c = is.read();
      
      if (c == 0)
        break;
      
      builder.append((char)c);
    }
    while (true);
    
    return builder.toString();
  }
}
