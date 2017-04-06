package com.pixbits.lib.json;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Type;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

import com.google.gson.*;

/**
 * Implicit enum JSon serialization for types which implement <code>JsonnableEnum</code>. Serialization is done by using the method
 * exposed by the interface.
 * @param <T>
 */
public class JsonEnumAdapter<T extends JsonnableEnum<T>> implements JsonAdapter<T>
{
  private final Class<?> eclazz;
  private final Map<String, T> mapping;
  
  @SuppressWarnings("unchecked")
  private Map<String, T> cacheValues()
  {
    try
    {
      T[] values = (T[])eclazz.getMethod("values").invoke(null);
      return Arrays.stream(values).collect(Collectors.toMap(t -> t.toJson(), t -> t));
    }
    catch (Exception e)
    {    
      e.printStackTrace();      
      return null;
    }
  }
  
  public JsonEnumAdapter(Class<?> eclazz, boolean cacheValues)
  {
    this.eclazz = eclazz;
    
    if (cacheValues)  
      mapping = cacheValues();
    else
      mapping = null;
  }
  
  @Override
  public JsonElement serialize(T src, Type typeOfSrc, JsonSerializationContext context)
  {
    return new JsonPrimitive(src.toJson());
  }

  @SuppressWarnings("unchecked")
  @Override
  public T deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException
  {
    try
    {
      if (mapping != null)
        return mapping.get(json.getAsString());
      else
      {      
        String str = json.getAsString();
        T[] values = (T[])eclazz.getMethod("values").invoke(null);
        
        for (T v : values)
          if (v.toJson().equals(str))
            return v;
      }
    } catch (Exception e)
    {
      e.printStackTrace();
    }
     
    return null;
  }

}
