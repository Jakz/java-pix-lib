package com.pixbits.lib.io.archive;

import java.nio.file.FileSystem;
import java.nio.file.FileSystems;
import java.nio.file.Path;
import java.nio.file.PathMatcher;
import java.util.Arrays;
import java.util.Optional;
import java.util.stream.Collectors;

public enum ArchiveFormat
{
  _7ZIP("7z", net.sf.sevenzipjbinding.ArchiveFormat.SEVEN_ZIP, true, true),
  ZIP("zip", net.sf.sevenzipjbinding.ArchiveFormat.ZIP, true, true),
  RAR("rar", net.sf.sevenzipjbinding.ArchiveFormat.RAR, true, false)
  ;
  
  private ArchiveFormat(String extension, net.sf.sevenzipjbinding.ArchiveFormat nativeFormat, boolean canRead, boolean canWrite) 
  { 
    this.extension = extension;
    this.nativeFormat = nativeFormat;
    this.canRead = canRead;
    this.canWrite = canWrite;
  }
  
  private final String extension;
  public final net.sf.sevenzipjbinding.ArchiveFormat nativeFormat;
  public final boolean canRead;
  public final boolean canWrite;
  
  public String extension() { return extension; }
  public String dottedExtension() { return "."+extension; }
  
  public final static ArchiveFormat[] readableFormats;
  public final static String[] readableExtensions;
  
  public static ArchiveFormat formatForNative(net.sf.sevenzipjbinding.ArchiveFormat nativeFormat)
  {
    Optional<ArchiveFormat> format = Arrays.stream(values()).filter(f -> f.nativeFormat == nativeFormat).findFirst();
    
    if (!format.isPresent())
      throw new NullPointerException("Unknown format for native format "+nativeFormat);
    
    return format.get();
  }
  
  public static ArchiveFormat guessFormat(Path path) { return guessFormat(path.getFileName().toString()); }
  
  public static ArchiveFormat guessFormat(String fileName)
  {
    Optional<ArchiveFormat> ai = Arrays.stream(values()).filter(f -> fileName.endsWith(f.dottedExtension())).findFirst();
    return ai.isPresent() ? ai.get() : null;
  }
  
  public static PathMatcher getReadableMatcher(FileSystem fs)
  {
    String pattern = Arrays.stream(readableExtensions).collect(Collectors.joining(",", "glob:*.{", "}"));
    return fs.getPathMatcher(pattern);
  }
  
  public static PathMatcher getReadableMatcher() { return getReadableMatcher(FileSystems.getDefault()); }
  
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