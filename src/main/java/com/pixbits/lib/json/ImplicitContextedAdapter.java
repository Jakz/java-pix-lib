package com.pixbits.lib.json;

import java.lang.reflect.Type;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonElement;
import com.google.gson.JsonSerializationContext;

public class ImplicitContextedAdapter<T extends JsonnableContext> implements JsonAdapter<T>
{
  final Class<T> clazz;

  public ImplicitContextedAdapter(Class<T> clazz) { this.clazz = clazz; }

  public JsonElement serialize(T value, Type type, JsonSerializationContext context)
  {
    try
    {
      return value.serialize(context);
    }
    catch (IllegalAccessException e)
    {
      e.printStackTrace();
      return null;
    }
  }

  public T deserialize(JsonElement element, Type type, JsonDeserializationContext context)
  {
    try {
      T v = clazz.newInstance();
      v.unserialize(element, context);
      return v;
    }
    catch (Exception e)
    {
      e.printStackTrace();
      return null;
    }
  }
}
