package com.pixbits.lib.io.archive.enumerator;

public abstract class Node
{
  long crc;
  long size;
  long csize;
  String name;
  
  public Node(String name, long size, long csize, long crc)
  {
    this.crc = crc;
    this.size = size;
    this.name = name;
  }
  
  public abstract boolean isLeaf();
}
