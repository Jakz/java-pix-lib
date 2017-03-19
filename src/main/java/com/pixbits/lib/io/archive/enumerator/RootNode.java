package com.pixbits.lib.io.archive.enumerator;

import java.util.Iterator;
import java.util.stream.Stream;

public class RootNode extends ArchiveNode
{  
  public RootNode(String name)
  {
    super(name, -1, -1, -1);
  }
  
  public String toString() { return "root"; }

  public Iterator<Node> iterator() { return children.iterator(); }
  public Stream<Node> stream() { return children.stream(); }
  
  public boolean isLeaf() { return false; }
}
