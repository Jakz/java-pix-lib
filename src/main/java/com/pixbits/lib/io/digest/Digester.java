package com.pixbits.lib.io.digest;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.security.DigestInputStream;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.zip.CRC32;
import java.util.zip.CheckedInputStream;

/**
 * Computes <code>DigestInfo</code> for an <code>InputStream</code> which contains
 * CRC32, MD5 and SHA-1 hashcodes for the specified stream.
 * 
 * It's possible to specify which digests to calculate, to provide a compute CRC32 value
 * and to specify if the instance will be used in a multithreaded context or not.
 * @author Jack
 */
public class Digester
{  
  private final DigestOptions options;
  private final byte[] buffer;

  public Digester(DigestOptions options)
  {
    this.options = options;
    this.buffer = new byte[options.bufferSize];
  }
  
  public DigestInfo digestOnlyCRC(DigestableCRC handle)
  {
    return new DigestInfo(handle.crc(), null, null);
  }
  
  public DigestInfo digest(DigestableCRC digestable, InputStream is) throws IOException, NoSuchAlgorithmException
  {
    final byte[] buffer = options.multiThreaded ? new byte[8192] : this.buffer;
    
    InputStream fis = new BufferedInputStream(is);
    CheckedInputStream crc = null;
    MessageDigest md5 = null;
    MessageDigest sha1 = null;
    boolean realRead = false;

    if (digestable == null)
    {
      crc = new CheckedInputStream(is, new CRC32());
      fis = crc;
      realRead = true;
    }
    
    if (options.computeMD5)
    {
      md5 = MessageDigest.getInstance("MD5");
      fis = new DigestInputStream(fis, md5);
      realRead = true;
    }
    
    if (options.computeSHA1)
    {
      sha1 = MessageDigest.getInstance("SHA-1");
      fis = new DigestInputStream(fis, sha1);
      realRead = true;
    }

    if (realRead)
      for (; fis.read(buffer) > 0; );
    else
      is.close();
    
    if (crc != null)
      crc.close();
    
    //TODO: maybe there are multple roms with same CRC32

    return new DigestInfo(
       digestable == null ? crc.getChecksum().getValue() : digestable.crc(),
       options.computeMD5 ? md5.digest() : null,
       options.computeSHA1 ? sha1.digest() : null
    );
  }
}
