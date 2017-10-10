package com.pixbits.lib.yaml.unserializer;

import java.util.ArrayList;
import java.util.List;

import com.pixbits.lib.yaml.YamlNode;
import com.pixbits.lib.yaml.YamlUnserializer;

public class ListUnserializer<T> implements YamlUnserializer<List<T>>
{
  Class<T> type;
  
  public ListUnserializer(Class<T> type)
  {
    this.type = type;
  }
  
  @Override
  public List<T> unserialize(YamlNode node)
  {
    int size = node.size();
    List<T> result = new ArrayList<T>();
    
    for (int i = 0; i < size; ++i)
    {
      YamlUnserializer<T> unserializer = node.environment().findUnserializer(type);
      T element = unserializer.unserialize(node.get(i));
      result.add(element);
    }
    
    return result;
  }

}
