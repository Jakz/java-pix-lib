package com.pixbits.lib.cpp;

import java.util.BitSet;

public class UInt8
{
  private int value;
  
  public UInt8(int value)
  {
    this.value = value & 0xFF;
  }
  
  public UInt8()
  {
    this(0);
  }
  
  public BitSet toBitSet()
  {
    BitSet set = new BitSet(8);
    for (int i = 0; i < 8; ++i)
      if ((value & (1 << i)) != 0)
        set.set(i);
    return set;
  }
  
  public void toByteArray(byte[] array)
  {
    array[0] = (byte)(value & 0xFF);
  }
  
  public byte[] toByteArray()
  {
    byte[] array = new byte[1];
    toByteArray(array);
    return array;
  }
}
