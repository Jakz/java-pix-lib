package com.pixbits.lib.io.archive;

import java.io.BufferedInputStream;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;

import com.pixbits.lib.exceptions.FileNotFoundException;

import net.sf.sevenzipjbinding.util.ByteArrayStream;

public interface Compressible
{
  String fileName();
  long size();
  InputStream getInputStream() throws IOException;
  
  public static Compressible ofPath(final Path path)
  {
    if (!Files.exists(path) || Files.isDirectory(path))
      throw new FileNotFoundException(path, "can't create a Compressible instance of a non exising or directory path");
    
    return new Compressible() {

      @Override
      public String fileName() { return path.getFileName().toString(); }

      @Override public long size()
      { 
        try { return Files.size(path); } 
        catch (IOException e) { e.printStackTrace(); } 
        return 0;
      }

      @Override
      public InputStream getInputStream() throws IOException
      {
        return new BufferedInputStream(Files.newInputStream(path));
      }     
    };
  }
  
  public static Compressible ofBytes(String name, byte[] bytes)
  {
    return new Compressible()
    {      
      @Override
      public String fileName() { return name; }

      @Override public long size()
      { 
        return bytes.length;
      }

      @Override
      public InputStream getInputStream() throws IOException
      {
        return new ByteArrayInputStream(bytes);
      }     
    };
  }
}
