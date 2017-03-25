package com.pixbits.lib.io.archive;

import java.io.IOException;
import java.nio.file.Path;

public class FormatUnrecognizedException extends IOException
{
  public final Path path;
  
  public FormatUnrecognizedException(Path path, String message)
  {
    super(message);
    this.path = path;
  }
}
