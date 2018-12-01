package com.pixbits.lib.lang;

import com.google.gson.*;
import com.pixbits.lib.json.JsonnableContext;

public class IntRange implements JsonnableContext<IntRange>
{
  public int min, max;
  
  public int first() { return min; }
  public int last() { return max; }
  
  public IntRange() { }

  public IntRange(int min, int max) { this.min = min; this.max = max; }
  
  public IntRange(String string)
  {
    String[] tks = string.split(":");
    
    try
    {
      if (tks.length != 2)
        throw new NumberFormatException();
      else
      {
        min = Integer.valueOf(tks[0]);
        max = Integer.valueOf(tks[1]);
      }
    }
    catch (NumberFormatException e)
    {
      min = 0;
      max = 1;
      throw e;
    }
  }
  

  public String toString() { return min+":"+max; }

  @Override
  public JsonElement serialize(JsonSerializationContext context)
  {
    JsonArray a = new JsonArray();
    a.add(new JsonPrimitive(min));
    a.add(new JsonPrimitive(max));
    return a;
  }

  @Override
  public void unserialize(JsonElement element, JsonDeserializationContext context)
  {
    JsonArray a = element.getAsJsonArray();
    min = a.get(0).getAsInt();
    max = a.get(1).getAsInt();
  }
}