package com.pixbits.lib.io.archive;

public interface Verifiable
{
  public long crc();
  public long size();
  
  public byte[] md5();
  public byte[] sha1();
}
