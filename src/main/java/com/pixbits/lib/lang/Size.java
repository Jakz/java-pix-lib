package com.pixbits.lib.lang;

import java.util.Objects;

import com.google.gson.JsonArray;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonElement;
import com.google.gson.JsonPrimitive;
import com.google.gson.JsonSerializationContext;
import com.pixbits.lib.json.JsonnableContext;

public class Size implements JsonnableContext<Size>
{
  public static class Int
  {
    public final int w;
    public final int h;
    
    public Int() { this.w = 0; this.h = 0; }
    public Int(int w, int h) { this.w = w; this.h = h; }
    @Override public boolean equals(Object o) { return o instanceof Int && ((Int)o).w == w && ((Int)o).h == h; }
    @Override public int hashCode() { return Objects.hash((Integer)w, (Integer)h); }
    
    public Size scale(float percent) { return new Size((w*percent), (h*percent)); }
  }
  
  
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
