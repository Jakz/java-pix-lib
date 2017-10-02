package com.pixbits.lib.lang;

import com.google.gson.JsonArray;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonElement;
import com.google.gson.JsonPrimitive;
import com.google.gson.JsonSerializationContext;
import com.pixbits.lib.json.JsonnableContext;

public class Point3D implements JsonnableContext<Point3D>
{
  public double x;
  public double y;
  public double z;
  
  public Point3D() { }
  
  public Point3D(double x, double y) { this(x, y, 0.0); }
  public Point3D(double x, double y, double z)
  {
    this.x = x;
    this.y = y;
    this.z = z;
  }
  
  public Point3D(String string)
  {
    String[] tks = string.split(",");
    if (tks.length != 3)
      throw new NumberFormatException();
    this.x = Float.parseFloat(tks[0].trim());
    this.y = Float.parseFloat(tks[1].trim());
    this.z = Float.parseFloat(tks[2].trim());

  }
  
  public static Point3D parsePoint(String string)
  {
      return new Point3D(string);
  }
  
  public String toString() { return x+", "+y+", "+z; }
  
  public Point3D divide(double v) { return new Point3D(x/v, y/v, z/v); }
  public Point3D sum(Point3D other) { return new Point3D(this.x + other.x, this.y + other.y, this.z + other.z); }

  @Override
  public JsonElement serialize(JsonSerializationContext context)
  {
    JsonArray a = new JsonArray();
    a.add(new JsonPrimitive(x));
    a.add(new JsonPrimitive(y));
    a.add(new JsonPrimitive(z));
    return a;
  }

  @Override
  public void unserialize(JsonElement element, JsonDeserializationContext context)
  {
    JsonArray a = element.getAsJsonArray();
    x = a.get(0).getAsFloat();
    y = a.get(1).getAsFloat();
    z = a.get(2).getAsFloat();
  }
}
