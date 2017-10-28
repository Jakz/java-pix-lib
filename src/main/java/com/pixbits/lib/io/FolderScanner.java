package com.pixbits.lib.io;

import java.io.IOException;
import java.nio.file.AccessDeniedException;
import java.nio.file.DirectoryStream;
import java.nio.file.FileSystems;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.PathMatcher;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;
import com.pixbits.lib.exceptions.FileNotFoundException;
import com.pixbits.lib.functional.StreamException;

public class FolderScanner
{
  final Set<Path> files;
  final Set<Path> excluded;
  final PathMatcher filter;
  final boolean recursive;
  final boolean multithreaded = true;
  final boolean includeFilesPassedIfMatching = true;
    
  public FolderScanner(boolean recursive)
  {
    this(FileSystems.getDefault().getPathMatcher("glob:*.*"), null, recursive);
  }
  
  public FolderScanner(PathMatcher filter, boolean recursive)
  {
    this(filter, null, recursive);
  }
  
  public FolderScanner(String filter, Set<Path> excluded, boolean recursive)
  {
    this(FileSystems.getDefault().getPathMatcher(filter), excluded, recursive);
  }
  
  public FolderScanner(Set<Path> excluded, boolean recursive)
  {
    this(FileSystems.getDefault().getPathMatcher("glob:*.*"), excluded, recursive);
  }
  
  public FolderScanner(PathMatcher filter, Set<Path> excluded, boolean recursive)
  {
    files = new HashSet<Path>();
    this.filter = filter;
    this.excluded = excluded;
    this.recursive = recursive;
  }
  
  private boolean shouldExclude(Path path)
  {
    return excluded != null && (excluded.contains(path) || excluded.stream().anyMatch(p -> path.startsWith(p)));
  }
  
  public Set<Path> scan(Collection<Path> roots) throws IOException
  {
    files.clear();
    
    // TODO: implement parallelism
    for (Path root : roots)
    {      
      if (!Files.exists(root))
        throw new FileNotFoundException(root);     
      else if (Files.isDirectory(root) && !Files.isHidden(root))
        innerScan(root);
      else if (filter.matches(root.getFileName()))
        files.add(root);
    }
    
    return new HashSet<>(files);
  }
  
  public Set<Path> scan(Path root) throws IOException
  {
    files.clear();
    if (!Files.exists(root))
      throw new FileNotFoundException(root);    
    else if (Files.isDirectory(root))
      innerScan(root);
    else if (filter.matches(root.getFileName()) && !shouldExclude(root))
      files.add(root);

    return new HashSet<>(files);
  }
   
  private void innerScan(Path folder) throws IOException
  {
    try (DirectoryStream<Path> stream = Files.newDirectoryStream(folder))
    {
      stream.forEach(StreamException.rethrowConsumer(e ->
      {                        
        if (!shouldExclude(e))
        {
          if (Files.isDirectory(e) && recursive)
            innerScan(e);
          else if (filter.matches(e.getFileName()))
            files.add(e);
        }
      }));
    }
    catch (AccessDeniedException e)
    {
      /* silently kill */
    }
  }
}
