package com.pixbits.lib.plugin.events;

import com.pixbits.lib.plugin.Plugin;
import com.pixbits.lib.plugin.PluginSet;

public class PluginChangeEvent
{
  public static enum Type
  {
    ENABLED,
    DISABLED
  }
  
  public final Type type;
  public final PluginSet<?> source;
  public final Plugin plugin;
  
  public PluginChangeEvent(Type type, PluginSet<?> source, Plugin plugin)
  {
    this.type = type;
    this.source = source;
    this.plugin = plugin;
  }
  
}
