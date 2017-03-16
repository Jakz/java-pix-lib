package com.pixbits.lib.io.archive;

public abstract class ScannerEntry
{
  public final String fileName;
  public final long size;
  public final long crc;
  
  protected ScannerEntry(String fileName, long size, long crc)
  {
    this.fileName = fileName;
    this.size = size;
    this.crc = crc;
  }
  
  public static class Binary extends ScannerEntry
  {
    public Binary(String fileName, long size, long crc)
    {
      super(fileName, size, crc);
    }
    
    public String toString() { return fileName; }
  }
  
  public static class Archived extends ScannerEntry
  {
    public final String archiveName;
    public final long compressedSize;
    
    public Archived(String archiveName, String fileName, long size, long compressedSize, long crc)
    {
      super(fileName, size, crc);
      this.archiveName = archiveName;
      this.compressedSize = compressedSize;
    }
    
    public String toString() { return archiveName + "/" + fileName; }
  }
  
  public static class Nested extends Archived
  {
    public final String internalArchiveName;
    
    public Nested(String archiveName, String internalArchiveName, String fileName, long size, long compressedSize, long crc)
    {
      super(archiveName, fileName, size, compressedSize, crc);
      this.internalArchiveName = internalArchiveName;
    }
    
    public String toString() { return archiveName + "/" + internalArchiveName + "/" + fileName; }
  }
  

}