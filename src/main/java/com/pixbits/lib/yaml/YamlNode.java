package com.pixbits.lib.yaml;

import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import com.pixbits.lib.lang.Pair;

public class YamlNode implements Iterable<YamlNode>
{
  final YamlEnvironment env;
  final Object node;
  
  private <K,V> Map<K,V> asMap() { return (Map<K,V>)node; }
  private <V> Map<String,V> asStringMap() { return asMap(); }
  
  private <V> List<V> asList() { return (List<V>)node; }
  
  YamlNode(YamlEnvironment env, Object node)
  {
    if (node instanceof YamlNode)
      throw new RuntimeException();
    
    this.env = env;
    this.node = node;
  }
  
  public YamlEnvironment environment() { return env; }
  
  public boolean exists() { return node != null; }
   
  public <T> T rawGet() { return (T)node; }
  
  public String asString()
  { 
    YamlUnserializer<String> unserializer = env.findUnserializer(String.class);
    return unserializer.unserialize(this);    
  }
  
  public int asInt()
  {  
    YamlUnserializer<Integer> unserializer = env.findUnserializer(Integer.class);
    return unserializer.unserialize(this);
  }
  
  public float asFloat()
  {
    YamlUnserializer<Float> unserializer = env.findUnserializer(Float.class);
    return unserializer.unserialize(this);
  }
  
  public YamlNode get(String key)
  {
    return new YamlNode(env, rawGet(key));
  }
  
  public <T> T rawGet(String key)
  {
    Map<String, T> map = asStringMap();
    return map.get(key);
  }
  
  public YamlNode get(int index)
  {
    return new YamlNode(env, rawGet(index));
  }
  
  public <T> T rawGet(int index)
  {
    List<T> list = asList();
    return list.get(index);
  }

  public Set<YamlNode> keySet()
  {
    Map<Object, Object> entries = asMap();
    return entries.keySet().stream()
      .map(k -> new YamlNode(env, k))
      .collect(Collectors.toSet());
  }
  
  public Set<String> stringKeySet()
  {
    Map<Object, Object> entries = asMap();
    return entries.keySet().stream()
      .map(k -> (String)k)
      .collect(Collectors.toSet());
  }
  
  public Set<Pair<String, YamlNode>> stringEntrySet()
  {
    Map<Object, Object> entries = asMap();
    return entries.entrySet().stream()
      .map(e -> new Pair<String, YamlNode>((String)e.getKey(), new YamlNode(env, e.getValue())))
      .collect(Collectors.toSet());
  }
  
  public int size()
  { 
    if (node instanceof java.util.Map)
      return asMap().size();
    else if (node instanceof List)
      return asList().size();
    else
      return 0;
  }
  
  public boolean isSequence() { return node instanceof List; }
  public boolean isMap() { return node instanceof java.util.Map; }
  public boolean isFloat() {
    try { Float.parseFloat(asString()); return true; } catch (NumberFormatException e) { return false; }
  }
  public boolean isInteger() {
    try { Integer.parseInt(asString()); return true; } catch (NumberFormatException e) { return false; }
  }
  
  @Override
  public Iterator<YamlNode> iterator()
  { 
    return new YamlNodeIterator(this);
  }
}
