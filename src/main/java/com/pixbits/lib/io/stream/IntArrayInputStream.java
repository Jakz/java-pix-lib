package com.pixbits.lib.io.stream;

import java.io.InputStream;

public class IntArrayInputStream extends InputStream
{
  private final int[] data;
  private int position;
  private int nibble;
  
  public IntArrayInputStream(int[] data)
  {
    this.data = data;
    this.position = 0;
    this.nibble = 0;
  }
  
  @Override public int read()
  {
    if (position >= data.length)
      return -1;
    
    int c = (data[position] >> (8*(3-nibble)) ) & 0xFF;
    
    ++nibble;
    if (nibble == 4)
    {
      nibble = 0;
      ++position;
    }
    
    return c;
  }
}
