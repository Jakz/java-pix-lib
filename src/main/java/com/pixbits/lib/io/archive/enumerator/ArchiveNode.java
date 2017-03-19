package com.pixbits.lib.io.archive.enumerator;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.stream.Stream;

public class ArchiveNode extends Node implements Iterable<Node>
{
  List<Node> children;
  
  public ArchiveNode(String name, long size, long csize, long crc)
  {
    super(name, size, csize, crc);
    children = new ArrayList<>();
  }
  
  public void add(Node node)
  {
    children.add(node);
  }
  
  public String toString() { return String.format("%s (size: %d, crc: %08x, count: %d)", name, size, crc, children.size()); }

  public Node get(int index) { return children.get(index); }
  public int count() { return children.size(); }
  public Iterator<Node> iterator() { return children.iterator(); }
  public Stream<Node> stream() { return children.stream(); }
  
  public boolean isLeaf() { return false; }
}
