package com.pixbits.lib.io.digest;

public class DigestOptions
{
  final int bufferSize;
  final boolean multiThreaded;
  final boolean computeCRC;
  final boolean computeMD5;
  final boolean computeSHA1;
  
  public DigestOptions(int bufferSize, boolean crc, boolean md5, boolean sha1, boolean multiThreaded)
  {
    this.bufferSize = bufferSize;
    this.computeCRC = crc;
    this.computeMD5 = md5;
    this.computeSHA1 = sha1;
    this.multiThreaded = multiThreaded;
  }
}
