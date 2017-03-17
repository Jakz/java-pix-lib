package com.pixbits.lib.io.digest;

import com.pixbits.lib.lang.StringUtils;

public class DigestInfo
{
  public final long crc;
  public final byte[] md5;
  public final byte[] sha1;
  
  public DigestInfo(long crc, byte[] md5, byte[] sha1)
  {
    this.crc = crc;
    this.md5 = md5;
    this.sha1 = sha1;
  }
  
  @Override public String toString() 
  { 
    return String.format("DigestInfo(crc: %08X, md5: %s, sha1: %s)", 
        crc, 
        StringUtils.toHexString(md5), 
        StringUtils.toHexString(sha1)); 
  }
}
