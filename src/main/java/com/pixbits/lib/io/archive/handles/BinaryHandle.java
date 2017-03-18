package com.pixbits.lib.io.archive.handles;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;

import com.pixbits.lib.io.FileUtils;

public class BinaryHandle extends Handle
{
  private Path path;
  private long crc;

  public BinaryHandle(Path file)
  {
    this(file, -1);
  }
  
  public BinaryHandle(Path file, long crc)
  {
    this.path = file.normalize();
    this.crc = crc;
  }

  @Override public boolean equals(Object object)
  {
    return (object instanceof BinaryHandle) && ((BinaryHandle)object).path.equals(this.path);
  }
  
  @Override public int hashCode() { return path.hashCode(); }
  
  @Override public Path path() { return path; }
  @Override public String relativePath() { return path.getFileName().toString(); } 
  @Override public String fileName() { return path().toString(); }
  
  @Override
  public String toString() { return path.getFileName().toString(); }
  @Override
  public String plainName() { return path.getFileName().toString().substring(0, path.getFileName().toString().lastIndexOf('.')); }
  @Override
  public String plainInternalName() { return plainName(); }
  
  @Override public long size() {
    try
    {
      return Files.size(path);
    }
    catch (IOException e)
    {
      e.printStackTrace();
      return 0;
    }
  }
  
  @Override public long compressedSize() { return size(); }
  
  @Override public long crc()
  {
    if (crc != -1)
      return crc;
    else
      try
      { 
        crc = FileUtils.calculateCRCFast(path()); 
        return crc;
      } catch (IOException e) 
      { 
        e.printStackTrace(); 
        return -1;
      }
  }
  
  
  @Override public boolean isArchive() { return false; }

  @Override public String getInternalExtension() { return getExtension(); }
  
  @Override
  public void relocate(Path file)
  {
    this.path = file;
  }
  
  @Override
  public Handle relocateInternal(String internalName)
  {
    throw new UnsupportedOperationException("a binary handle doesn't have an internal filename");
  }

  @Override
  public InputStream getInputStream() throws IOException
  {
    return Files.newInputStream(path);
  }

  @Override public Handle getVerifierHandle() { return this; }
}