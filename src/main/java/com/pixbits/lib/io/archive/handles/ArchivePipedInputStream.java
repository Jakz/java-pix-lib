package com.pixbits.lib.io.archive.handles;

import java.io.IOException;

import com.pixbits.lib.io.PipedInputStream;

import net.sf.sevenzipjbinding.IInArchive;

public class ArchivePipedInputStream extends PipedInputStream
{
  private static final int DEFAULT_BUFFER_SIZE = 128*1024;
  static int bufferSize = DEFAULT_BUFFER_SIZE;
  public static void setBufferSize(int bufferSize) { ArchivePipedInputStream.bufferSize = bufferSize; }
  public static int getBufferSize() { return bufferSize; }
  
  private final IInArchive archive;
  private final int indexInArchive;
  
  public ArchivePipedInputStream(IInArchive archive, int indexInArchive)
  {
    super(bufferSize);
    this.archive = archive;
    this.indexInArchive = indexInArchive;
  }
  
  @Override public synchronized int read(byte[] data) throws IOException
  {
    int i = super.read(data);
    //System.out.println("PipedInput::read "+i+" "+Thread.currentThread().getName());
    return i;
  }

  public int getIndexInArchive() { return indexInArchive; }
  public IInArchive getArchive() { return archive; }
}