package com.pixbits.lib.json;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.lang.reflect.Type;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import com.google.gson.JsonSerializationContext;

public class JsonLazySerializer<T> implements JsonAdapter<T>
{
  Class<T> clazz;
  boolean skipBool, skipFloat, skipInt;
  
  public JsonLazySerializer(Class<T> clazz, boolean skipBool, boolean skipInt, boolean skipFloat)
  {
    this.clazz = clazz;
    this.skipBool = skipBool;
    this.skipInt = skipInt;
    this.skipFloat = skipFloat;
  }
  
  
  @Override
  public JsonElement serialize(T src, Type typeOfSrc, JsonSerializationContext context)
  {
    try
    {
      Field[] fields = clazz.getFields();
      JsonObject o = new JsonObject();
      
      for (Field f : fields)
      {
        if (!Modifier.isStatic(f.getModifiers()))
        {
          Object v = f.get(src);
          Class<?> t = f.getType();
          
          if (skipBool && (t == Boolean.class || t == Boolean.TYPE) && !(Boolean)v)
            continue;
          else if (skipInt && (t == Integer.class || t == Integer.TYPE) && ((Integer)v) == 0)
            continue;
          else if (skipFloat && (t == Float.class || t == Float.TYPE) && ((Float)v) == 0.0f)
            continue;
          else
          {
            o.add(f.getName(), context.serialize(v, f.getType()));
          }
        }
      }
      
      return o;
    }
    catch (Exception e)
    {
      e.printStackTrace();
    }
    
    return null;
  }

  @Override
  public T deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException
  {
    try
    {
      T o = clazz.newInstance();
      JsonObject j = json.getAsJsonObject();
      
      Field[] fields = clazz.getFields();
      
      for (Field f : fields)
      {
        if (!Modifier.isStatic(f.getModifiers()))
        {
          if (j.has(f.getName()))
            f.set(o, context.deserialize(j.get(f.getName()), f.getType()));
        }
      }
      
      return o;
    }
    catch (Exception e)
    {
      e.printStackTrace();
    }

    return null;
  }
   
}
