package com.pixbits.lib.yaml;

@FunctionalInterface
public interface YamlUnserializer<T>
{
  public T unserialize(YamlNode node);
}
