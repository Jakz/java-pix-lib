package com.pixbits.lib.io.digest;

import com.pixbits.lib.io.archive.VerifierOptions;

public class DigestOptions
{
  public static int DEFAULT_BUFFER_SIZE = 1024*32;

  final int bufferSize;
  final boolean multiThreaded;
  final boolean computeCRC;
  final boolean computeMD5;
  final boolean computeSHA1;
  
  public DigestOptions(VerifierOptions voptions, boolean multiThreaded)
  {
    this(true, voptions.matchMD5, voptions.matchSHA1, multiThreaded);
  }
  
  public DigestOptions(int bufferSize, boolean crc, boolean md5, boolean sha1, boolean multiThreaded)
  {
    this.bufferSize = bufferSize;
    this.computeCRC = crc;
    this.computeMD5 = md5;
    this.computeSHA1 = sha1;
    this.multiThreaded = multiThreaded;
  }
  
  public DigestOptions(boolean crc, boolean md5, boolean sha1, boolean multiThreaded)
  {
    this(DEFAULT_BUFFER_SIZE, crc, md5, sha1, multiThreaded);
  }
}
