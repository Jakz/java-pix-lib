package com.pixbits.lib.plugin;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import com.pixbits.lib.plugin.events.PluginChangeEvent;
import com.pixbits.lib.plugin.events.PluginChangeListener;

public class PluginSet<P extends Plugin>
{
  private final Set<P> plugins;
  private List<PluginChangeListener> listeners;
  
  public PluginSet()
  {
    plugins = new HashSet<>();
    listeners = new ArrayList<>();
  }
  
  public void add(P plugin)
  {
    plugins.add(plugin);
  }
  
  @SuppressWarnings("unchecked")
  public void enable(PluginManager<P, ?> manager, PluginID id)
  {
    P plugin = getPlugin(id).orElse(manager.build((Class<? extends P>)id.getType()));
    
    if (plugin.isEnabled())
      return;
    
    /* if plugin is mutually exclusive we need to disable all others of same type */
    if (plugin.getPluginType().isMutuallyExclusive())
    {
      getEnabledPlugins(plugin.getPluginType()).stream()
      .filter( p -> p.isEnabled() )
      .forEach( p -> p.setEnabled(false) );
    }
    
    plugins.add(plugin);
    plugin.setEnabled(true);
    
    listeners.forEach(l -> 
      l.pluginStateChanged(new PluginChangeEvent(PluginChangeEvent.Type.ENABLED, this, plugin))
    );
  }
  
  public void disable(PluginID id)
  {
    Optional<P> plugin = getPlugin(id);
    
    if (plugin.isPresent())
    {
      plugin.get().setEnabled(false);
      
      listeners.forEach(l -> 
        l.pluginStateChanged(new PluginChangeEvent(PluginChangeEvent.Type.DISABLED, this, plugin.get()))
      );
    }
  }
  
  @SuppressWarnings("unchecked")
  public <T extends Plugin> Set<T> getPlugins(PluginType<?> type)
  {
    return (Set<T>)(Set<?>)stream().filter( p -> p.getPluginType() == type).collect(Collectors.toSet()); 
  }
  
  @SuppressWarnings("unchecked")
  public <T extends Plugin> Set<T> getEnabledPlugins(PluginType<?> type)
  {
    return (Set<T>)(Set<?>)getPlugins(type).stream().filter(p -> p.isEnabled()).collect(Collectors.toSet());
  }
  
  @SuppressWarnings("unchecked")
  public <T extends Plugin> T getPlugin(PluginType<?> type)
  {
    return (T)stream().filter( p -> p.getPluginType() == type).findFirst().orElse(null);
  }
  
  @SuppressWarnings("unchecked")
  public <T extends Plugin> T getEnabledPlugin(PluginType<?> type)
  {
    return (T)stream().filter( p -> p.getPluginType() == type && p.isEnabled()).findFirst().orElse(null);
  }
  
  public boolean hasPlugin(PluginType<?> type)
  {
    return stream().anyMatch( p -> p.getPluginType() == type );
  }
  
  public boolean hasPlugin(PluginID id)
  {
    return getPlugin(id).isPresent();
  }
  
  public Optional<P> getPlugin(PluginID id)
  {
    return stream().filter( p -> p.getID().equals(id) ).findFirst();
  }
  
  public Stream<P> stream() { return plugins.stream(); }
  
  public void addListener(PluginChangeListener listener)
  {
    this.listeners.add(listener);
  }
  
  public void removeListener(PluginChangeListener listener)
  {
    this.listeners.remove(listener);
  }
  
  public void clearListeners()
  {
    this.listeners.clear();
  }
}
