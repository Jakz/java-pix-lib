package com.pixbits.lib.io.archive;

import java.nio.file.Path;
import java.util.Set;
import java.util.function.Predicate;

public class ScannerOptions
{
  public boolean multithreaded;
  
  public boolean scanBinaries = true;
  public boolean scanSubfolders = true;
  public boolean scanArchives = true;
  public boolean scanNestedArchives = true;
  public boolean assumeCRCisCorrect = true;
  public Set<Path> ignoredPaths;
  
  public Predicate<ScannerEntry> shouldSkip = e -> false;
}
