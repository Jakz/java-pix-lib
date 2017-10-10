package com.pixbits.lib.io.archive.handles;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.nio.file.Path;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import com.google.gson.JsonSerializationContext;
import com.pixbits.lib.io.archive.ArchiveFormat;
import com.pixbits.lib.json.JsonAdapter;
import com.pixbits.lib.log.Log;

// TODO: this design is flawed, we must let each handle to manage serialization and deserialization by themselves
public class JsonHandleAdapter implements JsonAdapter<Handle>
{
  @Override
  public JsonElement serialize(Handle o, java.lang.reflect.Type type, JsonSerializationContext context)
  {    
    JsonObject j = new JsonObject();
    
    Class<?> handleType = o.getClass();
    
    j.add("type", context.serialize(handleType.getName()));
    j.add("path", context.serialize(o.path()));
    j.addProperty("crc", String.format("%08X", o.crc()));

    if (handleType == BinaryHandle.class)
    {
      BinaryHandle h = (BinaryHandle)o;
      /* not any additional data */
    }
    else if (handleType == ArchiveHandle.class)
    {
      ArchiveHandle h = (ArchiveHandle)o;
      j.add("format", context.serialize(h.format));
      j.addProperty("name", h.internalName);
      j.addProperty("index", h.indexInArchive);
      j.addProperty("size", h.size);
      j.addProperty("csize", h.compressedSize);
    }
    else if (handleType == NestedArchiveHandle.class)
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
    }
    else
    {
      try
      {
        Method method = handleType.getDeclaredMethod("serializeToJson", JsonObject.class, JsonSerializationContext.class);
        method.invoke(o, j, context);
      } 
      catch (NoSuchMethodException|SecurityException|IllegalAccessException|IllegalArgumentException|InvocationTargetException e)
      {
        Log.getLogger().e("Fatal error while trying to serialize a handle of type "+handleType.getName()+
            ". A public method serializeToJson(JsonObject, JsonSerializationContext) is required in the class.");
        throw new JsonParseException("Error serializing Handle", e);
      }
    }

    return j;
  }

  @Override
  public Handle deserialize(JsonElement json, java.lang.reflect.Type type, JsonDeserializationContext context) throws JsonParseException
  {
    JsonObject o = json.getAsJsonObject();
    Class<?> handleType = null;
    
    try
    {
      handleType = Class.forName(context.deserialize(o.get("type"), String.class));
  
      Path path = context.deserialize(o.get("path"), Path.class);
      long crc = Long.parseUnsignedLong(o.get("crc").getAsString(), 16);

      if (handleType == BinaryHandle.class)
      {
        return new BinaryHandle(path, crc);
      }
      else if (handleType == ArchiveHandle.class)
      {
        ArchiveFormat format = context.deserialize(o.get("format"), ArchiveFormat.class);
        String name = o.get("name").getAsString();
        int index = o.get("index").getAsInt();
        long size = o.get("size").getAsLong();
        long csize = o.get("csize").getAsLong();
        
        return new ArchiveHandle(path, format, name, index, size, csize, crc);
      }
      else if (handleType == NestedArchiveHandle.class)
      {
        ArchiveFormat format = context.deserialize(o.get("format"), ArchiveFormat.class);
        String name = o.get("name").getAsString();
        int index = o.get("index").getAsInt();
        ArchiveFormat nformat = context.deserialize(o.get("nformat"), ArchiveFormat.class);
        String nname = o.get("nname").getAsString();
        int nindex = o.get("nindex").getAsInt();
        long size = o.get("size").getAsLong();
        long csize = o.get("csize").getAsLong();
        
        return new NestedArchiveHandle(path, format, name, index, nformat, nname, nindex, size, csize, crc);
      }
      else
      {
        Constructor<?> method = handleType.getConstructor(JsonObject.class, JsonDeserializationContext.class);
        Handle handle = (Handle) method.newInstance(o, context);
        return handle;
      }
    } 
    catch (NoSuchMethodException | SecurityException | ClassCastException | InstantiationException | IllegalAccessException | IllegalArgumentException | ClassNotFoundException e)
    {
      if (handleType != null)
        Log.getLogger().e("Fatal error while trying to deserialize a handle of type "+handleType.getName()+
          ": a public constructor which accepts a JsonObject and a JsonDeserializationContext is required");
      e.printStackTrace();
      throw new JsonParseException("Error deserializing Handle, wrong handle type?", e);
    }
    catch (InvocationTargetException e)
    {
      Log.getLogger().e("Fatal error while constructing a handle of type "+handleType.getName()+
          ": "+e.getTargetException().getMessage());
      e.getTargetException().printStackTrace();
      throw new JsonParseException("Error deserializing Handle", e);
    }
  };
  
  
}
