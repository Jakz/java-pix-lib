package com.pixbits.lib.yaml.unserializer;

import java.util.function.Function;

import com.pixbits.lib.yaml.YamlNode;
import com.pixbits.lib.yaml.YamlUnserializer;

public class EnumUnserializer<T extends Enum<T>> implements YamlUnserializer<T>
{
  private final Class<T> clazz;
  private final boolean logError;
  private final Function<String, String> remapper;
  
  public EnumUnserializer(Class<T> clazz)
  {
    this(clazz, s -> s, false);
  }
  
  public EnumUnserializer(Class<T> clazz, Function<String, String> remapper)
  {
    this(clazz, remapper, false);
  }
 
  public EnumUnserializer(Class<T> clazz, Function<String, String> remapper, boolean logError)
  {
    this.clazz = clazz;
    this.logError = logError;
    this.remapper = remapper;
  }
  
  @Override
  public T unserialize(YamlNode node)
  {
    String value = node.asString();
        
    try
    {
      return Enum.valueOf(clazz, remapper.apply(value));
    }
    catch (IllegalArgumentException e)
    {
      if (logError)
      {
        System.out.println("EnumUnserializer<"+clazz.getSimpleName()+">: value not found for "+value);
        e.printStackTrace();
      }
      return null;
    }  
  }
}
