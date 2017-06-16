package com.pixbits.lib.lang;

import com.google.gson.JsonArray;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonElement;
import com.google.gson.JsonPrimitive;
import com.google.gson.JsonSerializationContext;
import com.pixbits.lib.json.JsonnableContext;

public class Point implements JsonnableContext<Point>
{
  public float x;
  public float y;
  
  public Point() { }
  public Point(float x, float y)
  {
    this.x = x;
    this.y = y;
  }
  public Point(String string)
  {
    String[] tks = string.split(",");
    if (tks.length != 2)
      throw new NumberFormatException();
    this.x = Float.parseFloat(tks[0].trim());
    this.y = Float.parseFloat(tks[1].trim());
  }
  
  public static Point parsePoint(String string)
  {
      return new Point(string);
  }
  
  public String toString() { return x+", "+y; }
  
  
  
  @Override
  public JsonElement serialize(JsonSerializationContext context)
  {
    JsonArray a = new JsonArray();
    a.add(new JsonPrimitive(x));
    a.add(new JsonPrimitive(y));
    return a;
  }

  @Override
  public void unserialize(JsonElement element, JsonDeserializationContext context)
  {
    JsonArray a = element.getAsJsonArray();
    x = a.get(0).getAsFloat();
    y = a.get(1).getAsFloat();
  }
}
