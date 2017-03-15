package com.pixbits.lib.io.archive;

import java.nio.file.Path;

public class FormatUnrecognizedException extends Exception
{
  public final Path path;
  
  public FormatUnrecognizedException(Path path, String message)
  {
    super(message);
    this.path = path;
  }
}
