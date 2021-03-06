package com.pixbits.lib.plugin;

import java.lang.reflect.Field;

public class PluginArgument
{
  private final Class<?> type;
  private final String name;
  private final String description;
  private final String params;
  private final Field field;
  private final Plugin plugin;
  
  PluginArgument(Plugin plugin, Field field, String name, Class<?> type, String description, String params)
  {
    this.plugin = plugin;
    this.field = field;
    this.name = name;
    this.type = type;
    this.description = description;
    this.params = params;
  }
  
  public Class<?> getType() { return type; }
  public String getName() { return name; }
  public String getDescription() { return description; }
  public String getParams() { return params; }
  
  public Object get()
  {
    try
    {
      return field.get(plugin);
    }
    catch (IllegalAccessException e)
    {
      e.printStackTrace();
      return null;
    }
  }
  
  public void set(Object object)
  {
    try
    {
      field.set(plugin, object);
    }
    catch (IllegalAccessException e)
    {
      e.printStackTrace();
    }
  }
}
