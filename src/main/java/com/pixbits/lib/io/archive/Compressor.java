package com.pixbits.lib.io.archive;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.RandomAccessFile;
import java.nio.file.Path;
import java.util.Date;
import java.util.List;
import java.util.function.Consumer;

import com.pixbits.lib.log.Log;
import com.pixbits.lib.log.ProgressLogger;

import net.sf.sevenzipjbinding.IOutCreateArchive7z;
import net.sf.sevenzipjbinding.IOutCreateArchiveZip;
import net.sf.sevenzipjbinding.IOutCreateCallback;
import net.sf.sevenzipjbinding.IOutItem7z;
import net.sf.sevenzipjbinding.IOutItemBase;
import net.sf.sevenzipjbinding.IOutItemZip;
import net.sf.sevenzipjbinding.ISequentialInStream;
import net.sf.sevenzipjbinding.SevenZip;
import net.sf.sevenzipjbinding.SevenZipException;
import net.sf.sevenzipjbinding.impl.InputStreamSequentialInStream;
import net.sf.sevenzipjbinding.impl.OutItemFactory;
import net.sf.sevenzipjbinding.impl.RandomAccessFileOutStream;

public class Compressor<H extends Compressible>
{ 
  private static final ProgressLogger progressLogger = Log.getProgressLogger(Compressor.class);
  
  private final CompressorOptions options;
  private Consumer<H> beforeAddingEntryToArchiveCallback = h -> {};
  
  public Compressor(CompressorOptions options)
  {
    this.options = options;
  }
  
  public void setCallbackOnAddingEntryToArchive(Consumer<H> callback)
  {
    this.beforeAddingEntryToArchiveCallback = callback;
  }
  
  private static interface ItemDecorator<H extends Compressible, T>
  {
    public void decorate(T item, H handle);
  }
  
  private static class ItemDecorator7z<H extends Compressible> implements ItemDecorator<H, IOutItem7z>
  {
    public void decorate(IOutItem7z item, H handle)
    {
      item.setDataSize(handle.size());
      item.setPropertyPath(handle.fileName());
    }
  }
  
  private static class ItemDecoratorZip<H extends Compressible> implements ItemDecorator<H, IOutItemZip>
  {
    public void decorate(IOutItemZip item, H handle)
    {
      item.setDataSize(handle.size());
      item.setPropertyPath(handle.fileName());
    }
  }
  
  private final class CreateCallback<T extends IOutItemBase> implements IOutCreateCallback<T>
  {
    private final List<H> handles;
    private final long totalSize;
    private final ItemDecorator<H,T> decorator;
    private InputStream currentStream = null;
    
    CreateCallback(List<H> handles, ItemDecorator<H, T> decorator)
    {
      this.handles = handles;
      totalSize = handles.stream().mapToLong(H::size).sum();
      this.decorator = decorator;
    }
    
    @Override public void setTotal(long total) throws SevenZipException
    {
      
    }

    @Override public void setCompleted(long complete) throws SevenZipException
    {
      progressLogger.updateProgress(complete/(float)totalSize, "");
    }

    @Override
    public void setOperationResult(boolean success) throws SevenZipException
    {
      try
      { 
        currentStream.close();
      }
      catch (IOException e)
      {
        e.printStackTrace();
      }
    }

    @Override
    public T getItemInformation(int index, OutItemFactory<T> factory) throws SevenZipException
    {
      T item = factory.createOutItem();
      H handle = handles.get(index);
      
      decorator.decorate(item, handle);

      beforeAddingEntryToArchiveCallback.accept(handle);      
      return item;
    }
    
    class MyISequentialInStream extends InputStreamSequentialInStream
    {
      MyISequentialInStream(InputStream is)
      {
        super(is);
        System.out.println("MySequentialInStream::init");
      }
      
      @Override public int read(byte[] data) throws SevenZipException
      {
        int i = super.read(data);
        
        System.out.println("MySequentialInStream::read "+i+" "+Thread.currentThread().getName());
        
        return i;
      }
      
      @Override public void close() throws IOException
      {
        System.out.println("MyISequentialInStream::close");
        super.close();
      }
    }

    @Override
    public ISequentialInStream getStream(int index) throws SevenZipException
    {
      try
      {
        currentStream = handles.get(index).getInputStream();
        System.out.println("CreateCallback::getStream");

        return new MyISequentialInStream(currentStream);
      }
      catch (IOException e)
      {
        e.printStackTrace();
        return null;
      }
    }
  }
  
  public void createArchive(Path dest, List<H> handles) throws FileNotFoundException, SevenZipException
  {
    RandomAccessFile raf = new RandomAccessFile(dest.toFile(), "rw");
        
    switch (options.format)
    {
      case _7ZIP:
      {
        IOutCreateArchive7z archive = SevenZip.openOutArchive7z();
        archive.setThreadCount(0);
        archive.setLevel(options.compressionLevel);
        boolean solid = options.useSolidArchives;
        archive.setSolid(solid);
        if (solid)
        {
          archive.setSolidFiles(handles.size());
          archive.setSolidSize(handles.stream().mapToLong(H::size).sum());
        }
        
        CreateCallback<IOutItem7z> callback = new CreateCallback<IOutItem7z>(handles, new ItemDecorator7z<H>());
        archive.setTrace(true);

        archive.createArchive(new RandomAccessFileOutStream(raf), handles.size(), callback);
        
        break;
      }
      case ZIP:
      {
        // TODO: test
        IOutCreateArchiveZip archive = SevenZip.openOutArchiveZip();
        archive.setLevel(options.compressionLevel);
        
        CreateCallback<IOutItemZip> callback = new CreateCallback<IOutItemZip>(handles, new ItemDecoratorZip<H>());
        
        archive.setTrace(true);
        archive.createArchive(new RandomAccessFileOutStream(raf), handles.size(), callback);
        
        break;
      }
    }
    
    progressLogger.endProgress();
  }
}
