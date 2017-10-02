package com.pixbits.lib.ui.table;

import java.util.List;
import java.util.stream.Collectors;

public abstract class ManagedListSelectionListener<T> extends SimpleListSelectionListener
{
  private final DataSource<T> data;
  
  public ManagedListSelectionListener(DataSource<T> data)
  {
    this.data = data;
  }
  
  @Override
  protected final void multipleSelection(List<Integer> indices)
  {
    multipleDataSelection(indices.stream().map(data::get).collect(Collectors.toList()));
  }

  @Override
  protected final void singleSelection(int index)
  {
    singleSelection(data.get(index));
  }
  
  protected abstract void singleSelection(T object);
  protected abstract void multipleDataSelection(List<T> objects);
  
  public static class Adapter<U> extends ManagedListSelectionListener<U>
  {
    public Adapter(DataSource<U> data) { super(data); }
    @Override protected void singleSelection(U object) { }
    @Override protected void multipleDataSelection(List<U> objects) { }
    @Override protected void commonActionBefore() { }
    @Override protected void commonActionAfter() { }
    @Override protected void clearSelection() { }   
  }
}
