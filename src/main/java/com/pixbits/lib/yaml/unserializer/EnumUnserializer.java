package com.pixbits.lib.yaml.unserializer;

import com.pixbits.lib.yaml.YamlException;
import com.pixbits.lib.yaml.YamlNode;
import com.pixbits.lib.yaml.YamlUnserializer;

public class EnumUnserializer<T> implements YamlUnserializer<T>
{
  Class<T> type;
  
  public EnumUnserializer(Class<T> type)
  {
    this.type = type;
  }
  
  @Override
  public T unserialize(YamlNode node)
  {
    String name = node.rawGet();
    try
    {
      //TODO: maybe there's a way to do it without hacks
      return (T)Enum.valueOf((Class)type, name);
    }
    catch (IllegalArgumentException e)
    {
      throw new YamlException("Enum constant not found: "+type.getName()+"::"+name, e);
    }
  }

}
