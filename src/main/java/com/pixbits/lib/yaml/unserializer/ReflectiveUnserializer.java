package com.pixbits.lib.yaml.unserializer;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.function.Function;

import com.pixbits.lib.yaml.YamlException;
import com.pixbits.lib.yaml.YamlNode;
import com.pixbits.lib.yaml.YamlUnserializer;

public class ReflectiveUnserializer<T> implements YamlUnserializer<T>
{
  private final Class<T> type;
  private final Field[] fields;
  private Function<String, String> fieldRemapper;
  
  public ReflectiveUnserializer(Class<T> type)
  {
    this(type, s -> s);
  }
  
  public ReflectiveUnserializer(Class<T> type, Function<String, String> fieldRemapper)
  {
    this.type = type;
    this.fields = type.getFields();
    this.fieldRemapper = fieldRemapper;
  }

  @Override
  public T unserialize(YamlNode node)
  {
    T object;
    
    try
    {
      object = type.getDeclaredConstructor().newInstance();
    }
    catch (Exception e)
    {
      if ((type.getModifiers() & Modifier.STATIC) == 0)
        throw new YamlException("Unable to istantiate type "+type.getSimpleName()+", class is not static, that may be the cause", e);
      else
        throw new YamlException("Unable to istantiate type "+type.getSimpleName(), e);
    }
    
    try
    {
      for (Field field : fields)
      {
        String yamlFieldName = fieldRemapper.apply(field.getName());
        YamlNode child = node.get(yamlFieldName);

        if (child.exists())
        {
          YamlUnserializer<?> unserializer = node.environment().findUnserializer(field.getGenericType());
          field.set(object, unserializer.unserialize(child));
        }
        else
        {
          System.out.println("Children not found while unserializing "+type.getSimpleName()+": "+field.getName());
        }
      }
    } 
    catch (IllegalArgumentException | IllegalAccessException e)
    {
      e.printStackTrace();
    }
    
    return object;
  }
}
