package com.pixbits.lib.io.archive;

import java.nio.file.FileSystem;
import java.nio.file.FileSystems;
import java.nio.file.PathMatcher;
import java.util.Arrays;
import java.util.stream.Collectors;

public enum ArchiveFormat
{
  _7ZIP(".7z", true, true),
  ZIP(".zip", true, true),
  RAR(".rar", true, false)
  ;
  
  private ArchiveFormat(String extension, boolean canRead, boolean canWrite) 
  { 
    this.extension = extension;
    this.canRead = canRead;
    this.canWrite = canWrite;
  }
  
  private final String extension;
  public final boolean canRead;
  public final boolean canWrite;
  
  public String extension() { return extension.substring(1); }
  
  public final static ArchiveFormat[] readableFormats;
  public final static String[] readableExtensions;
  
  public final static PathMatcher getReadableMatcher(FileSystem fs)
  {
    String pattern = Arrays.stream(readableExtensions).collect(Collectors.joining(",", "glob:*.{", "}"));
    return fs.getPathMatcher(pattern);
  }
  
  public final static PathMatcher getReadableMatcher() { return getReadableMatcher(FileSystems.getDefault()); }
  
  static
  {
    readableFormats = Arrays.stream(values())
      .filter(f -> f.canRead)
      .toArray(i -> new ArchiveFormat[i]);
    
    readableExtensions = Arrays.stream(readableFormats)
        .map(ArchiveFormat::extension)
        .toArray(i -> new String[i]);
  }

}