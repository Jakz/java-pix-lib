package com.pixbits.lib.plugin.events;

@FunctionalInterface
public interface PluginChangeListener
{
  public void pluginStateChanged(PluginChangeEvent event);
}
