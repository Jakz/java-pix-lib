package com.pixbits.lib.io.archive;

import java.nio.file.Path;
import java.util.Set;
import java.util.function.Consumer;
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
  
  public Consumer<VerifierEntry> onSkip = s -> {};
  public Consumer<String> onFaultyArchive = s -> {};
  public Consumer<VerifierEntry> onEntryFound = s -> {};
  
  public Predicate<VerifierEntry> shouldSkip = e -> false;
}
