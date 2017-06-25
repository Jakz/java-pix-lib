package com.pixbits.lib.ui.canvas;

public abstract class Entity implements Comparable<Entity>
{
  private EntityHolder container;
  private int z;
  
  protected Entity()
  {
    z = 0;
  }
  
  void  container(EntityHolder container) { this.container = container; }
  
  
  
  public int z() { return z; }
  public void z(int z)
  { 
    this.z = z; 
    if (container != null) container.markDirty();
  }
  
  @Override public final int compareTo(Entity other)
  {
    return z - other.z;
  }
  
  abstract void draw(GfxContext context);
}
