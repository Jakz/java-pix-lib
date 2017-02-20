package com.pixbits.lib.plugin;

public interface PluginType<T extends PluginType<T>> extends Comparable<T>
{
  default public boolean isMutuallyExclusive() { return false; }
  default public boolean isRequired() { return false; }
}
