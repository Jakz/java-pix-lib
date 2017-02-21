package com.pixbits.lib.util;

import java.util.ArrayList;
import java.util.List;

public class ShutdownManager
{
  private final List<Runnable> tasks;
  
  private final Thread hook = new Thread()
  {
    @Override
    public void run()
    {
      cleanup();
    }
  };
  
  public ShutdownManager(boolean register)
  {
    tasks = new ArrayList<>();
    
    if (register)
      register();
  }
  
  public void register()
  {
    Runtime.getRuntime().addShutdownHook(hook);
  }
  
  public void unregister()
  {
    Runtime.getRuntime().removeShutdownHook(hook);
  }
  
  public void addTask(Runnable task)
  {
    tasks.add(task);
  }
  
  protected void cleanup()
  {
    tasks.forEach(Runnable::run);
  }
}
