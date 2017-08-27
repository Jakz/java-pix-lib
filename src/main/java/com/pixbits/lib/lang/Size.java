package com.pixbits.lib.lang;

import com.google.gson.JsonArray;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonElement;
import com.google.gson.JsonPrimitive;
import com.google.gson.JsonSerializationContext;
import com.pixbits.lib.json.JsonnableContext;

public class Size implements JsonnableContext<Size>
{
  public float w;
  public float h;
  
  public Size() { }
  public Size(float w, float h)
  {
    this.w = w;
    this.h = h;
  }
  public Size(String string)
  {
    String[] tks = string.split(",");
    if (tks.length != 2)
      throw new NumberFormatException();
    this.w = Float.parseFloat(tks[0].trim());
    this.h = Float.parseFloat(tks[1].trim());
  }
  
  public static Size parsePoint(String string)
  {
      return new Size(string);
  }
  
  public String toString() { return w+", "+h; }
  
  
  
  @Override
  public JsonElement serialize(JsonSerializationContext context)
  {
    JsonArray a = new JsonArray();
    a.add(new JsonPrimitive(w));
    a.add(new JsonPrimitive(h));
    return a;
  }

  @Override
  public void unserialize(JsonElement element, JsonDeserializationContext context)
  {
    JsonArray a = element.getAsJsonArray();
    w = a.get(0).getAsFloat();
    h = a.get(1).getAsFloat();
  }
}
