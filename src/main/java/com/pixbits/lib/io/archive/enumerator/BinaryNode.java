package com.pixbits.lib.io.archive.enumerator;

public class BinaryNode extends Node
{
  public BinaryNode(String name, long size, long csize, long crc)
  {
    super(name, size, csize, crc);
  }
  
  public String toString() { return String.format("%s (size: %d, crc: %08x)", name, size, crc); }
  public boolean isLeaf() { return true; }
}
