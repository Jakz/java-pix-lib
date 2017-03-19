package com.pixbits.lib;

import java.net.URISyntaxException;
import java.net.URL;
import java.nio.file.Path;
import java.nio.file.Paths;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;

@RunWith(Suite.class)
@Suite.SuiteClasses({
  //CompressorTest.class,
  ArchiveExplorerTest.class
})
public class Tests
{
  public static String getResourceBase()
  {
    String basePackage = Tests.class.getPackage().getName().replaceAll("\\.", "/");
    return basePackage + "/dummy/";
  }
  
  public static Path getPathForReadableResource(String path)
  {
    URL url = Tests.class.getClassLoader().getResource(Tests.getResourceBase()+path);
    Path file = null;
    
    try
    {
      file = Paths.get(url.toURI());
    } 
    catch (URISyntaxException e)
    {
      file = Paths.get(url.getPath());
    }
    
    return file;
  }
}