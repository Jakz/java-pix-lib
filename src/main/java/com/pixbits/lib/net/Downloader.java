package com.pixbits.lib.net;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.Scanner;

public class Downloader
{
  public static String downloadAsString(String address) throws IOException
  {
    URL url = new URL(address);
    
    try (InputStream is = url.openStream())
    {
      try (Scanner scanner = new Scanner(is, "UTF-8"))
      {
        return scanner.useDelimiter("\\A").next();
      }
    } 
  }
}
