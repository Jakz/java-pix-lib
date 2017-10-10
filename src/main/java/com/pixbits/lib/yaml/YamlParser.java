package com.pixbits.lib.yaml;

import java.io.FileReader;
import java.io.IOException;
import java.nio.file.Path;
import java.util.function.Function;

import com.esotericsoftware.yamlbeans.YamlReader;

public class YamlParser
{
  private final YamlEnvironment env;
  
  public YamlParser()
  {
    this.env = new YamlEnvironment(this);
  }
  
  public YamlNode parse(Path path) throws IOException
  {
    try (FileReader freader = new FileReader(path.toString()))
    {
      YamlReader reader = new YamlReader(freader);
      YamlNode root = new YamlNode(env, reader.read());
      return root;  
    }
  }
    
  public <T> T parse(Path path, Class<T> type) throws IOException
  {
    YamlNode node = parse(path);
    return unserialize(node, type);
  }
  
  public <T> T unserialize(YamlNode node, Class<T> type)
  {
    YamlUnserializer<T> unserializer = env.findUnserializer(type);
    return unserializer.unserialize(node);
  }
  
  public <T> void registerUnserializer(Class<T> type, YamlUnserializer<T> unserializer)
  {
    env.registerUnserializer(type, unserializer);
  }
  
  public void setReflectiveUnserializeFieldRemapper(Function<String, String> remapper)
  {
    env.setUnserializeFieldNameRemapper(remapper);
  }
}
