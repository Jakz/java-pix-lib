package com.pixbits.lib.lang;

import java.util.Objects;

import com.google.gson.JsonArray;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonElement;
import com.google.gson.JsonPrimitive;
import com.google.gson.JsonSerializationContext;
import com.pixbits.lib.json.JsonnableContext;
import com.pixbits.lib.lang.Size.Int;

public class Point implements JsonnableContext<Point>
{
  public static class Int
  {
    public final int x;
    public final int y;
    
    public Int() { this.x = 0; this.y = 0; }
    public Int(int x, int y) { this.x = x; this.y =y; }
    @Override public boolean equals(Object o) { return o instanceof Int && ((Int)o).x == x && ((Int)o).y == y; }
    @Override public int hashCode() { return Objects.hash((Integer)x, (Integer)y); }
    
    public Size scale(float percent) { return new Size((x*percent), (y*percent)); }
  }
  
  
  
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
  
  public Point divide(float v) { return new Point(x/v, y/v); }
  public Point sum(Point other) { return new Point(this.x + other.x, this.y + other.y); }
  
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
  
  public static Point.Int of (java.awt.Point point) { return new Point.Int(point.x, point.y); }
}
>>>>>>> branch 'master' of git@github.com:Jakz/java-pix-lib.git
