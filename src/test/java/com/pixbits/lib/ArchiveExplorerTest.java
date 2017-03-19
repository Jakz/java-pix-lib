package com.pixbits.lib;

import org.junit.BeforeClass;
import org.junit.Test;


import com.pixbits.lib.io.archive.ArchiveFormat;
import com.pixbits.lib.io.archive.enumerator.ArchiveExplorer;
import com.pixbits.lib.io.archive.enumerator.ArchiveNode;
import com.pixbits.lib.io.archive.enumerator.BinaryNode;
import com.pixbits.lib.io.archive.enumerator.RootNode;

import net.sf.sevenzipjbinding.SevenZipNativeInitializationException;

import static com.pixbits.lib.Tests.getResourceBase;
import static org.junit.Assert.*;
import static org.hamcrest.core.IsInstanceOf.instanceOf;


import java.io.IOException;
import java.nio.file.Paths;

import static com.pixbits.lib.Tests.getPathForReadableResource;

public class ArchiveExplorerTest
{
  private static ArchiveExplorer explorer;
  
  @BeforeClass
  public static void setup()
  {
    explorer = new ArchiveExplorer();

    try
    {
      net.sf.sevenzipjbinding.SevenZip.initSevenZipFromPlatformJAR();
    } 
    catch (SevenZipNativeInitializationException e)
    {
      e.printStackTrace();
    }
  }
  
  @Test
  public void testSimpleArchiveMultipleFiles()
  {
    try
    {
      RootNode node = explorer.generateTree(getPathForReadableResource("simple_archive_multiple_files.zip"), ArchiveFormat.ZIP);

      assertEquals(node.count(), 2);
      assertThat(node.get(0), instanceOf(BinaryNode.class));
      assertThat(node.get(1), instanceOf(BinaryNode.class));

      //explorer.printTree(System.out, node);
    } 
    catch (IOException e)
    {
      e.printStackTrace();
    }
  }
  
  @Test
  public void testNestedArchiveWithPlainFile()
  {
    try
    {
      RootNode node = explorer.generateTree(getPathForReadableResource("nested_archive_with_plain.zip"), ArchiveFormat.ZIP);
      //explorer.printTree(System.out, node);
    } 
    catch (IOException e)
    {
      e.printStackTrace();
    }
  }
}
