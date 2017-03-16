package com.pixbits.lib.io.archive;

import java.util.function.Predicate;

public class ScannerOptions
{
  public boolean multithreaded;
  public boolean discardUnknownSizes;
  
  public boolean scanSubfolders = true;
  public boolean scanArchives = true;
  public boolean scanNestedArchives = true;
  public boolean assumeCRCisCorrect = true;
  
  public Predicate<ScannerEntry> shouldSkip;
}
