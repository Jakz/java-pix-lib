package com.pixbits.lib.io.archive.enumerator;

import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.file.Path;

import com.pixbits.lib.functional.StreamException;
import com.pixbits.lib.io.archive.ArchiveFormat;
import com.pixbits.lib.io.archive.support.MemoryArchive;

import net.sf.sevenzipjbinding.IInArchive;
import net.sf.sevenzipjbinding.IInStream;
import net.sf.sevenzipjbinding.PropID;
import net.sf.sevenzipjbinding.SevenZip;
import net.sf.sevenzipjbinding.impl.RandomAccessFileInStream;

public class ArchiveExplorer
{
  private void analyze(ArchiveNode parent, IInArchive archive) throws IOException
  {
    int count = archive.getNumberOfItems();
    
    for (int i = 0; i < count; ++i)
    {
      String name = (String)archive.getProperty(i, PropID.PATH);
      long size = (long)archive.getProperty(i, PropID.SIZE);
      long csize = (long)archive.getProperty(i, PropID.PACKED_SIZE);
      long crc = (long)(int)archive.getProperty(i, PropID.CRC);
      
      ArchiveFormat iformat = ArchiveFormat.guessFormat(name);
      
      if (iformat != null)
      {
        MemoryArchive istream = MemoryArchive.load(archive, i, (int)size);
        IInArchive iarchive = SevenZip.openInArchive(iformat.nativeFormat, istream);
        ArchiveNode node = new ArchiveNode(name, size, csize, crc);
        parent.add(node);
        analyze(node, iarchive);
      }
      else
      {
        BinaryNode node = new BinaryNode(name, size, csize, crc);
        parent.add(node);
      }
    }   
  }

  public RootNode generateTree(IInStream stream, ArchiveFormat format) throws IOException
  {
    try (IInArchive archive = SevenZip.openInArchive(format.nativeFormat, stream))
    {
      RootNode root = new RootNode("root");
      analyze(root, archive);
      return root;
    }
  }
  
  public RootNode generateTree(Path path, ArchiveFormat format) throws IOException
  {
    try (RandomAccessFile raf = new RandomAccessFile(path.toFile(), "r"))
    {
      try (IInStream stream = new RandomAccessFileInStream(raf))
      {
        return generateTree(stream, format);
      }
    }
  }
  
  private void recursivePrint(Appendable out, Node node, int indent) throws IOException
  {
    for (int i = 0; i < indent; ++i)
      out.append(' ');
    
    out.append(node.toString());
    out.append('\n');
    
    if (!node.isLeaf())
    {
      ArchiveNode inode = (ArchiveNode)node;
      inode.stream().forEach(
          StreamException.rethrowConsumer(
              ith -> recursivePrint(out, ith, indent+2)
          )
      );
    }
  }
  
  public void printTree(Appendable out, RootNode node) throws IOException
  {
    recursivePrint(out, node, 0);
  }
}
