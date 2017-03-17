package com.pixbits.lib.io.archive;

import com.pixbits.lib.io.archive.handles.Handle;
import com.pixbits.lib.io.digest.DigestableCRC;

public interface Verifiable
{
  public long crc();
  public long size();
  
  public byte[] md5();
  public byte[] sha1();
}
