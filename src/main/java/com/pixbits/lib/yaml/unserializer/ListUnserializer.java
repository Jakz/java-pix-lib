package com.pixbits.lib.yaml.unserializer;

import com.pixbits.lib.yaml.YamlNode;
import com.pixbits.lib.yaml.YamlUnserializer;

public class ListUnserializer<T> implements YamlUnserializer<T>
{
  Class<T> type;
  
  public ListUnserializer(Class<T> type)
  {
    this.type = type;
  }
  
  @Override
  public T unserialize(YamlNode node)
  {
    return null;
  }

}
