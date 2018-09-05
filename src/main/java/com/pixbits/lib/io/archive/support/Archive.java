package com.pixbits.lib.io.archive.support;

import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.stream.Stream;

import com.pixbits.lib.io.archive.ArchiveFormat;
import com.pixbits.lib.io.archive.FormatUnrecognizedException;
import com.pixbits.lib.io.archive.handles.ArchiveHandle;

import net.sf.sevenzipjbinding.IArchiveExtractCallback;
import net.sf.sevenzipjbinding.IInArchive;
import net.sf.sevenzipjbinding.PropID;
import net.sf.sevenzipjbinding.SevenZip;
import net.sf.sevenzipjbinding.SevenZipException;
import net.sf.sevenzipjbinding.impl.RandomAccessFileInStream;

public class Archive
{
  public static class Item
  {
    private Archive archive;
    public final String path;
    public final int index;
    public final long crc;
    public final long size;
    public final long compressedSize;
    
    Item(Archive archive, String path, int index, long crc, long size, long compressedSize)
    {
      this.archive = archive;
      this.path = path;
      this.index = index;
      this.crc = crc;
      this.size = size;
      this.compressedSize = compressedSize;
    }
    
    public ArchiveHandle handle() { return archive.buildHandle(index); }
  }
  
  private final Path path;
  
  private IInArchive archive;
  private RandomAccessFile rfile;
  
  private ArchiveFormat format;
  private final List<Item> items;
  
  public Archive(Path path)
  {
    this.path = path;
    items = new ArrayList<>();
  }
  
  public Archive(Path path, boolean cacheInformations) throws FormatUnrecognizedException, IOException
  {
    this(path);
    if (cacheInformations)
      cacheInformations();
  }
  
  public void cacheInformations() throws FormatUnrecognizedException, IOException
  {
    open();
    
    items.clear();
    
    int itemCount = archive.getNumberOfItems();
    for (int i = 0; i < itemCount; ++i)
    {
      String path = (String)archive.getProperty(i, PropID.PATH);
      long size = (long)archive.getProperty(i, PropID.SIZE);
      Long lcompressedSize = (Long)archive.getProperty(i, PropID.PACKED_SIZE);
      long compressedSize = lcompressedSize != null ? lcompressedSize : -1;
      long crc = Integer.toUnsignedLong((Integer)archive.getProperty(i, PropID.CRC));
      
      items.add(new Item(this, path, i, crc, size, compressedSize));
    }
    
    close();
    archive = null;
  }
  
  public void extract(Item item, Path dest) throws IOException, FormatUnrecognizedException
  {
    extract(item, dest, stream -> new ArchiveExtractCallback(stream));
  }
  
  public void extract(Item item, Path dest, final Consumer<Float> onProgress, final Consumer<Boolean> onComplete) throws IOException, FormatUnrecognizedException
  {
    extract(item, dest, stream -> new ArchiveExtractCallback.Logging(stream, onProgress, onComplete));
  }
  
  private void extract(Item item, Path dest, Function<ArchiveExtractStream, ArchiveExtractCallback> builder) throws IOException, FormatUnrecognizedException
  {
    open();
    
    if (items.isEmpty())
      cacheInformations();
    
    if (item.index >= items.size())
      throw new IOException("Item at index "+item.index+" for archive "+path.toString()+" doesn't exist");
 
    final ArchiveToFileExtractStream stream = new ArchiveToFileExtractStream(dest);
    final ArchiveExtractCallback callback = builder.apply(stream);
    
    archive.extract(new int[] { item.index }, false, callback);
    callback.close();
    stream.close();
  }
  
  public int size() { return items.size(); }
  public Stream<Item> stream() { return items.stream(); }
  public Iterator<Item> iterator() { return items.iterator(); }
  public Item itemAt(int index) { return items.get(index); }
  
  private ArchiveHandle buildHandle(int index)
  {
    Item item = items.get(index);
    return new ArchiveHandle(path, format, item.path, index, item.size, item.compressedSize, item.crc);
  }
  
  
  public void open() throws FormatUnrecognizedException
  {
    try
    {
      if (archive == null)
      {
        rfile = new RandomAccessFile(path.toFile(), "r");
        archive = SevenZip.openInArchive(null, new RandomAccessFileInStream(rfile));
        format = ArchiveFormat.formatForNative(archive.getArchiveFormat());
      }

      if (format == null)
      {
        archive = null;
        throw new FormatUnrecognizedException(path, "Archive format unrecognized");
      }
    }
    catch (IOException e)
    {
      throw new FormatUnrecognizedException(path, "Archive format unrecognized");
    }
  }
  
  public void close() throws IOException, SevenZipException
  {
    if (archive != null)
    {
      archive.close();
      archive = null;
    }
    
    if (rfile != null)
    {
      rfile.close();
      rfile = null;
    }
  }
}
