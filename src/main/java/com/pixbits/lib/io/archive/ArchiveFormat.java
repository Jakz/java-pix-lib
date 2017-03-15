package com.pixbits.lib.io.archive;

public enum ArchiveFormat
{
  _7ZIP(".7z"),
  ZIP(".zip")
  ;
  
  private ArchiveFormat(String extension) { this.extension = extension; }
  
  public final String extension;
}