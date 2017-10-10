package com.pixbits.lib.yaml.unserializer;

import com.pixbits.lib.yaml.YamlNode;
import com.pixbits.lib.yaml.YamlUnserializer;

public class EnumUnserializer<T extends Enum<T>> implements YamlUnserializer<T>
{
  private final Class<T> clazz;
  private final boolean logError;
  
  public EnumUnserializer(Class<T> clazz)
  {
    this(clazz, false);
  }
 
  public EnumUnserializer(Class<T> clazz, boolean logError)
  {
    this.clazz = clazz;
    this.logError = logError;
  }
  
  @Override
  public T unserialize(YamlNode node)
  {
    String value = node.asString();
    try
    {
      return Enum.valueOf(clazz, value);
    }
    catch (IllegalArgumentException e)
    {
      if (logError)
        e.printStackTrace();
      return null;
    }  
  }
}
