package com.pixbits.lib.exceptions;

import java.nio.file.Path;

public class FileNotFoundException extends RuntimeException
{
  public final Path path;
  public final String message;
  
  public FileNotFoundException(Path path)
  {
    this(path, null);
  }
  
  public FileNotFoundException(Path path, String message)
  {
    this.path = path;
    this.message = message;
  }
}
