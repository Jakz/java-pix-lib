package com.pixbits.lib.io.archive.handles;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Path;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonObject;
import com.google.gson.JsonSerializationContext;

public class HandleLink extends Handle
{
  private final Handle original;
  
  public HandleLink(Handle original)
  {
    this.original = original;
  }
  
  @Override public Handle getVerifierHandle() { return original.getVerifierHandle(); }

  @Override public String toString() { return original.toString(); }

  @Override
  public Path path()
  {
    return original.path();
  }

  @Override
  public String relativePath()
  {
    return original.relativePath();
  }

  @Override
  public String fileName()
  {
    return original.fileName();
  }

  @Override
  public String plainName()
  {
    return original.plainName();
  }

  @Override
  public String plainInternalName()
  {
    return original.plainInternalName();
  }
  
  @Override
  public String internalName()
  {
    return original.internalName();
  }

  @Override
  public void relocate(Path file)
  {
    throw new UnsupportedOperationException("a HandleLink can't be relocated");
  }

  @Override
  public Handle relocateInternal(String internalName)
  {
    throw new UnsupportedOperationException("a HandleLink can't be relocated");
  }

  @Override
  public boolean isArchive()
  {
    return original.isArchive();
  }

  @Override
  public String getInternalExtension()
  {
    return original.getInternalExtension();
  }

  @Override
  public InputStream getInputStream() throws IOException
  {
    return original.getInputStream();
  }

  @Override
  public long crc()
  {
    return original.crc();
  }

  @Override
  public long size()
  {
    return original.size();
  }

  @Override
  public long compressedSize()
  {
    return original.compressedSize();
  }
  
  public void serializeToJson(JsonObject j, JsonSerializationContext context)
  {
    j.add("original", context.serialize(original, Handle.class));
  }
  
  public HandleLink(JsonObject o, JsonDeserializationContext context)
  {
    original = context.deserialize(o.get("original"), Handle.class);
  }

}
