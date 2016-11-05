package com.pixbits.lib.plugin;

public class PluginException extends RuntimeException
{
  PluginException(String message)
  {
    super("Plugin exception: "+message);
  }
}
