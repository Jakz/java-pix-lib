package com.pixbits.lib.io.archive.handles;

import java.nio.file.Path;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import com.google.gson.JsonSerializationContext;
import com.pixbits.lib.io.archive.ArchiveFormat;
import com.pixbits.lib.json.JsonAdapter;

public class JsonHandleAdapter implements JsonAdapter<Handle>
{
  private static enum Type
  {
    binary,
    archived,
    nested
  }
  
  private static Type typeForClass(Class<? extends Handle> clazz)
  {
    if (clazz == BinaryHandle.class)
      return Type.binary;
    else if (clazz == ArchiveHandle.class)
      return Type.archived;
    else if (clazz == NestedArchiveHandle.class)
      return Type.nested;
    else
      throw new JsonParseException("Unkown handle type for JSON serialization");
  }

  @Override
  public JsonElement serialize(Handle o, java.lang.reflect.Type type, JsonSerializationContext context)
  {    
    JsonObject j = new JsonObject();
    
    Type handleType = typeForClass(o.getClass());
    
    j.add("type", context.serialize(handleType));
    j.add("path", context.serialize(o.file()));
    j.addProperty("crc", String.format("%08X", o.crc()));

    switch (handleType)
    {
      case binary:
      {
        BinaryHandle h = (BinaryHandle)o;
        break;
      }
      case archived:
      {
        ArchiveHandle h = (ArchiveHandle)o;
        j.add("format", context.serialize(h.format));
        j.addProperty("name", h.internalName);
        j.addProperty("index", h.indexInArchive);
        j.addProperty("size", h.size);
        j.addProperty("csize", h.compressedSize);
        break;
      }
      case nested:
      {
        NestedArchiveHandle h = (NestedArchiveHandle)o;
        j.add("format", context.serialize(h.format));
        j.addProperty("name", h.internalName);
        j.addProperty("index", h.indexInArchive);
        j.add("nformat", context.serialize(h.nestedFormat));
        j.addProperty("nname", h.nestedInternalName);
        j.addProperty("nindex", h.nestedIndexInArchive);
        j.addProperty("size", h.size);
        j.addProperty("csize", h.compressedSize);
        break;
      }
    }
    
    return j;
  }

  @Override
  public Handle deserialize(JsonElement json, java.lang.reflect.Type type, JsonDeserializationContext context) throws JsonParseException
  {
    JsonObject o = json.getAsJsonObject();
    
    Type handleType = context.deserialize(o.get("type"), Type.class);
    
    Path path = context.deserialize(o.get("path"), Path.class);
    long crc = Long.parseUnsignedLong(o.get("crc").getAsString(), 16);
    
    switch (handleType)
    {
      case binary:
      {
        return new BinaryHandle(path, crc);
      }
      case archived:
      {
        ArchiveFormat format = context.deserialize(o.get("format"), ArchiveFormat.class);
        String name = o.get("name").getAsString();
        int index = o.get("index").getAsInt();
        long size = o.get("size").getAsLong();
        long csize = o.get("csize").getAsLong();
        
        return new ArchiveHandle(path, format, name, index, size, csize, crc);
      }
      case nested:
      {
        ArchiveFormat format = context.deserialize(o.get("format"), ArchiveFormat.class);
        String name = o.get("name").getAsString();
        int index = o.get("index").getAsInt();
        ArchiveFormat nformat = context.deserialize(o.get("nformat"), ArchiveFormat.class);
        String nname = o.get("nname").getAsString();
        int nindex = o.get("nindenx").getAsInt();
        long size = o.get("size").getAsLong();
        long csize = o.get("csize").getAsLong();
        
        return new NestedArchiveHandle(path, format, name, index, nformat, nname, nindex, size, csize, crc);
      }
    }
    
    throw new JsonParseException("Error deserializing Handle, wrong handle type?");
  };
  
  
}
