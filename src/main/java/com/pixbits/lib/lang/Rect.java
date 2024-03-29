package com.pixbits.lib.lang;

import com.google.gson.*;
import com.pixbits.lib.json.JsonnableContext;

public class Rect implements JsonnableContext<Rect>
{
  public int x;
  public int y;
  public int w;
  public int h;
  
  public boolean isInside(int x, int y)
  {
    return x >= this.x && x < this.x + w && y >= this.y && y < this.y + h;
  }
  
  public Rect(String string)
  {
    String[] tks = string.split(",");
    
    if (tks.length != 4)
      throw new NumberFormatException();
    else
    {
      x = Integer.parseInt(tks[0].trim());
      y = Integer.parseInt(tks[1].trim());
      w = Integer.parseInt(tks[2].trim());
      h = Integer.parseInt(tks[3].trim());

    }
  }
  
  public Rect(int[] v)
  {
    if (v.length != 4) throw new NumberFormatException("must be 4 values");
    
    x = v[0]; y = v[1];
    w = v[2]; h = v[3];
  }
  
  public java.awt.Rectangle toRectangle()
  {
    return new java.awt.Rectangle(x,y,w,h);
  }
  
  public static Rect parseRect(String string)
  {
    return new Rect(string);
  }
  
  public String toString()
  {
    return x+", "+y+", "+w+", "+h;
  }
  
  public Rect() { }
  public Rect(java.awt.Rectangle r) { this(r.x, r.y, r.width, r.height); }
  public Rect(int x, int y, int width, int height)
  {
    this.x = x;
    this.y = y;
    this.w = width;
    this.h = height;
  }
  
  public Rect multiply(int v)
  {
    return new Rect(x*v, y*v, w*v, h*v);
  }
  
  public void add(Rect rect)
  {
    this.x += rect.x;
    this.y += rect.y;
    this.w += rect.w;
    this.h += rect.h;
  }
  
  public void translate(Point point)
  {
    translate(point.x, point.y);
  }
  
  public void translate(float x, float y)
  {
    this.x += x;
    this.y += y;
  }

  @Override
  public JsonElement serialize(JsonSerializationContext context)
  {
    JsonArray a = new JsonArray();
    a.add(new JsonPrimitive(x));
    a.add(new JsonPrimitive(y));
    a.add(new JsonPrimitive(w));
    a.add(new JsonPrimitive(h));
    return a;
  }

  @Override
  public void unserialize(JsonElement element, JsonDeserializationContext context)
  {
    if (element != null)
    {
      JsonArray a = element.getAsJsonArray();
      x = a.get(0).getAsInt();
      y = a.get(1).getAsInt();
      w = a.get(2).getAsInt();
      h = a.get(3).getAsInt();
    }
  }
  
  public static Rect ofJson(JsonElement element)
  {
    Rect r = new Rect();
    
    if (element != null)
    {
      /* fallback in case of serialization as java.awt.Rectangle */
      if (element.isJsonObject())
      {
        JsonObject obj = element.getAsJsonObject();
        r.x = obj.get("x").getAsInt();
        r.y = obj.get("y").getAsInt();
        r.w = obj.get("width").getAsInt();
        r.h = obj.get("height").getAsInt();
      }
      else
        r.unserialize(element, null);
    }
    
    if (r.w == 0 && r.h == 0)
      return null;
    
    return r;
  }
}
