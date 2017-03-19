package com.pixbits.lib;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.Date;

import org.junit.BeforeClass;
import org.junit.Test;

import com.pixbits.lib.io.archive.ArchiveFormat;
import com.pixbits.lib.io.archive.Compressible;
import com.pixbits.lib.io.archive.Compressor;
import com.pixbits.lib.io.archive.CompressorOptions;

import net.sf.sevenzipjbinding.SevenZipNativeInitializationException;

import static com.pixbits.lib.Tests.getResourceBase;
import static com.pixbits.lib.Tests.getPathForReadableResource;

public class CompressorTest
{
  @BeforeClass
  public static void init()
  {
    try
    {
      net.sf.sevenzipjbinding.SevenZip.initSevenZipFromPlatformJAR();
    } 
    catch (SevenZipNativeInitializationException e)
    {
      e.printStackTrace();
    }
  }
    

  
  Path getPathForWritableResource(String path)
  {
    String basePath = getClass().getProtectionDomain().getCodeSource().getLocation().getFile();
    return Paths.get(basePath + getResourceBase() + path);
  }
  
  CompressorOptions defaultOptions()
  {
    return new CompressorOptions(ArchiveFormat.ZIP, true, 9);

  }

  @Test
  public void testCorrectCreation()
  {
    Path path1 = getPathForReadableResource("testfile1.txt");
    Path path2 = getPathForReadableResource("testfile2.txt");
    
    Compressor<Compressible> compressor = new Compressor<Compressible>(defaultOptions());
    
    Compressible entry1 = Compressible.ofBytes("foo.txt", "foo".getBytes());//Compressible.ofPath(path1);
    Compressible entry2 = Compressible.ofBytes("foo2.txt", "foo2".getBytes());//Compressible.ofPath(path1);
    
    //Compressible entry1 = Compressible.ofPath(path1);
    //Compressible entry2 = Compressible.ofPath(path2);
    
    try
    {
      Path destination = getPathForWritableResource("testarchive.zip");
      compressor.createArchive(destination, Arrays.asList(entry1, entry2));
    } catch (FileNotFoundException e)
    {
      e.printStackTrace();
    } catch (IOException e)
    {
      e.printStackTrace();
    }
    
  }
}
