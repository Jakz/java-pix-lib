package com.pixbits.lib.json;

import java.lang.reflect.Type;
import java.nio.file.Path;
import java.nio.file.Paths;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;
import com.google.gson.JsonSerializationContext;

public class PathAdapter implements JsonAdapter<Path>
{
  @Override
  public Path deserialize(JsonElement element, Type type, JsonDeserializationContext context) throws JsonParseException
  {
    String string = context.deserialize(element, String.class);
    return Paths.get(string);
  }

  @Override
  public JsonElement serialize(Path src, Type type, JsonSerializationContext context)
  {
    return context.serialize(src.toString());
  }
}