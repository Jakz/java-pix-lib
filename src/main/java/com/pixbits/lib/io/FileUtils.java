package com.pixbits.lib.io;

import java.io.IOException;
import java.nio.MappedByteBuffer;
import java.nio.channels.FileChannel;
import java.nio.file.DirectoryStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.concurrent.atomic.AtomicLong;
import java.util.zip.CRC32;
import java.util.zip.Checksum;

public class FileUtils
{
  public static long calculateCRCFast(Path filename) throws IOException
  {
    final int SIZE = 16 * 1024;
    try (FileChannel channel = (FileChannel)Files.newByteChannel(filename))
    {
      CRC32 crc = new CRC32();
      int length = (int) channel.size();
      MappedByteBuffer mb = channel.map(FileChannel.MapMode.READ_ONLY, 0, length);
      byte[] bytes = new byte[SIZE];
      int nGet;
      
      while (mb.hasRemaining())
      {
         nGet = Math.min(mb.remaining(), SIZE);
         mb.get(bytes, 0, nGet);
         crc.update(bytes, 0, nGet);
      }
      
      return crc.getValue();
    }
  }
  
  public static long calculateCRC32(byte[] data, int off, int size)
  {
    Checksum checksum = new CRC32();
    checksum.update(data, off, size);
    return checksum.getValue();
  }
  
  public static long folderSize(Path root, boolean recursive, boolean includeHidden) throws IOException
  {
    AtomicLong length = new AtomicLong();
    
    try (DirectoryStream<Path> files = Files.newDirectoryStream(root))
    {
      for (Path path : files)
      {
        if (Files.isDirectory(path))
        {
          if (recursive)
            length.addAndGet(folderSize(path, recursive, includeHidden));
        }
        else if (!Files.isHidden(path) || includeHidden)
          length.addAndGet(Files.size(path));
      }
    }

    return length.get();
  }
  
  public static String lastPathComponent(String path) {
    int index = path.lastIndexOf("/");
    
    if (index != -1)
      return path.substring(index+1);
    else
      return path;
  }
  
  public static String pathExtension(Path path) {
    String fileName = path.getFileName().toString();
    int index = fileName.lastIndexOf('.');
    return index != -1 ? fileName.substring(index+1) : "";
  }
  
  public static String fileNameWithoutExtension(Path path) {
    String fileName = path.getFileName().toString();
    int lastDot = fileName.lastIndexOf(".");
    
    return lastDot == -1 ? fileName : fileName.substring(0, lastDot);
  }
  
  public static String trimExtension(String fileName) {
    int lastDot = fileName.lastIndexOf(".");
    
    return lastDot == -1 ? fileName : fileName.substring(0, lastDot);
  }
}
